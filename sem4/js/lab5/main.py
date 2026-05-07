import csv
import re
import logging
import sys
import random
import statistics
import argparse
from pathlib import Path
from collections import namedtuple
from datetime import datetime, date

# ===========================================================================
# Data structures
# ===========================================================================

Station = namedtuple('Station', [
    'code', 'name', 'type', 'voivodeship', 'city',
    'address', 'latitude', 'longitude', 'start_date', 'end_date',
])

# ===========================================================================
# Logging (Task 6) – DEBUG/INFO/WARNING → stdout, ERROR/CRITICAL → stderr
# ===========================================================================

class _BelowErrorFilter(logging.Filter):
    def filter(self, record: logging.LogRecord) -> bool:
        return record.levelno < logging.ERROR


def setup_logging() -> None:
    root = logging.getLogger()
    root.setLevel(logging.DEBUG)
    fmt = logging.Formatter('%(levelname)-8s %(name)s: %(message)s')

    out = logging.StreamHandler(sys.stdout)
    out.setLevel(logging.DEBUG)
    out.addFilter(_BelowErrorFilter())
    out.setFormatter(fmt)

    err = logging.StreamHandler(sys.stderr)
    err.setLevel(logging.ERROR)
    err.setFormatter(fmt)

    root.addHandler(out)
    root.addHandler(err)


log = logging.getLogger(__name__)

# ===========================================================================
# Task 1 – CSV parsing
# ===========================================================================

def parse_stations(path: Path) -> list[Station]:
    """Parse stacje.csv into a list of Station namedtuples."""
    stations: list[Station] = []
    log.info("Opening file: %s", path)
    try:
        with open(path, encoding='utf-8') as f:
            reader = csv.DictReader(f)
            for row in reader:
                raw = ','.join(v for v in row.values() if v)
                log.debug("Read %d bytes", len(raw.encode('utf-8')))
                stations.append(Station(
                    code=row.get('Kod stacji', '').strip(),
                    name=row.get('Nazwa stacji', '').strip(),
                    type=row.get('Rodzaj stacji', '').strip(),
                    voivodeship=row.get('Województwo', '').strip(),
                    city=row.get('Miejscowość', '').strip(),
                    address=row.get('Adres', '').strip(),
                    latitude=row.get('WGS84 φ N', '').strip(),
                    longitude=row.get('WGS84 λ E', '').strip(),
                    start_date=row.get('Data uruchomienia', '').strip(),
                    end_date=row.get('Data zamknięcia', '').strip(),
                ))
    except FileNotFoundError:
        log.error("File not found: %s", path)
        raise
    log.info("Closed file: %s", path)
    return stations


def parse_measurements(path: Path) -> dict:
    """
    Parse a measurement CSV file (GIOŚ format with 6 header rows).

    Returns a dict:
        station_codes : list[str]
        quantity      : str
        frequency     : str
        data          : list of (date_str, {station_code: float | None})
    """
    result: dict = {'station_codes': [], 'quantity': '', 'frequency': '', 'data': []}
    log.info("Opening file: %s", path)
    try:
        with open(path, encoding='utf-8') as f:
            reader = csv.reader(f)
            rows: list[list[str]] = []
            for row in reader:
                raw = ','.join(row)
                log.debug("Read %d bytes", len(raw.encode('utf-8')))
                rows.append(row)
    except FileNotFoundError:
        log.error("File not found: %s", path)
        raise
    log.info("Closed file: %s", path)

    if len(rows) < 7:
        return result

    # Header rows: 0=Nr, 1=Kod stacji, 2=Wskaźnik, 3=Czas uśredniania,
    #              4=Jednostka, 5=Kod stanowiska, 6+=data
    station_codes = [c.strip() for c in rows[1][1:]]
    quantity = rows[2][1].strip() if len(rows[2]) > 1 else ''
    frequency = rows[3][1].strip() if len(rows[3]) > 1 else ''

    result['station_codes'] = station_codes
    result['quantity'] = quantity
    result['frequency'] = frequency

    for row in rows[6:]:
        if not row or not row[0].strip():
            continue
        dt_str = row[0].strip()
        values: dict[str, float | None] = {}
        for i, code in enumerate(station_codes):
            raw_val = row[i + 1].strip() if i + 1 < len(row) else ''
            val: float | None = None
            if raw_val:
                try:
                    val = float(raw_val.replace(',', '.'))
                except ValueError:
                    pass
            values[code] = val
        result['data'].append((dt_str, values))

    return result

# ===========================================================================
# Task 2 – group_measurement_files_by_key
# ===========================================================================

def group_measurement_files_by_key(path: Path) -> dict[tuple[str, str, str], Path]:
    """
    Scan *path* (non-recursively) for files matching <year>_<quantity>_<frequency>.csv.
    Returns {(year, quantity, frequency): Path}.
    """
    pattern = re.compile(r'^(\d{4})_(.+)_([^_]+)\.csv$')
    result: dict[tuple[str, str, str], Path] = {}
    for f in path.iterdir():
        if f.is_file():
            m = pattern.match(f.name)
            if m:
                result[(m.group(1), m.group(2), m.group(3))] = f
    return result

# ===========================================================================
# Task 3 – get_addresses
# ===========================================================================

def get_addresses(path: Path, city: str) -> list[tuple]:
    """
    Return list of (voivodeship, city, street, number_or_None) for all
    stations in *city* (case-insensitive exact match).
    """
    stations = parse_stations(path)
    addr_re = re.compile(
        r'^(?:(?:ul\.|al\.|os\.|pl\.)\s+)?(.+?)(?:\s+(\d+\w*))?(?:\s*,.*)?$',
        re.IGNORECASE,
    )
    city_re = re.compile(r'^' + re.escape(city) + r'$', re.IGNORECASE)
    addresses: list[tuple] = []

    for s in stations:
        if not city_re.match(s.city):
            continue
        m = addr_re.match(s.address)
        if m:
            street = m.group(1).strip() if m.group(1) else s.address
            number = m.group(2)
        else:
            street, number = s.address, None
        addresses.append((s.voivodeship, s.city, street, number))

    if not addresses:
        log.warning("No stations found in city: %s", city)
    return addresses

# ===========================================================================
# Task 4 – regex tasks on stations data
# ===========================================================================

def task4_extract_dates(stations: list[Station]) -> list[str]:
    """4a – all YYYY-MM-DD dates from Data uruchomienia / Data zamknięcia."""
    pattern = re.compile(r'\d{4}-\d{2}-\d{2}')
    dates: list[str] = []
    for s in stations:
        dates.extend(pattern.findall(s.start_date))
        dates.extend(pattern.findall(s.end_date))
    return dates


def task4_extract_coordinates(stations: list[Station]) -> list[tuple[str, str]]:
    """4b – (latitude, longitude) pairs where each has exactly 6 decimal places."""
    pattern = re.compile(r'\d+\.\d{6}')
    coords: list[tuple[str, str]] = []
    for s in stations:
        lat = pattern.search(s.latitude)
        lon = pattern.search(s.longitude)
        if lat and lon:
            coords.append((lat.group(), lon.group()))
    return coords


def task4_find_hyphenated_names(stations: list[Station]) -> list[str]:
    """4c – station names with exactly two parts separated by ' - '."""
    return [s.name for s in stations if len(re.split(r' - ', s.name)) == 2]


def task4_normalize_names(stations: list[Station]) -> list[str]:
    """4d – replace spaces with '_' and Polish diacritics with ASCII equivalents."""
    _pl = {
        'ą': 'a', 'ć': 'c', 'ę': 'e', 'ł': 'l', 'ń': 'n',
        'ó': 'o', 'ś': 's', 'ź': 'z', 'ż': 'z',
        'Ą': 'A', 'Ć': 'C', 'Ę': 'E', 'Ł': 'L', 'Ń': 'N',
        'Ó': 'O', 'Ś': 'S', 'Ź': 'Z', 'Ż': 'Z',
    }

    def _normalize(name: str) -> str:
        name = re.sub(r' ', '_', name)
        name = re.sub(r'[ąćęłńóśźżĄĆĘŁŃÓŚŹŻ]', lambda m: _pl[m.group()], name)
        return name

    return [_normalize(s.name) for s in stations]


def task4_verify_mob_stations(stations: list[Station]) -> bool:
    """4e – verify all stations whose code ends with 'MOB' have type 'mobilna'."""
    mob_re = re.compile(r'MOB$')
    for s in stations:
        if mob_re.search(s.code) and s.type.lower() != 'mobilna':
            return False
    return True


def task4_three_part_locations(stations: list[Station]) -> list[str]:
    """4f – station names with exactly three parts separated by ' - '."""
    return [s.name for s in stations if len(re.split(r' - ', s.name)) == 3]


def task4_street_locations(stations: list[Station]) -> list[str]:
    """4g – addresses containing a comma AND ul. / al."""
    pattern = re.compile(r'^(?=.*,)(?=.*\b(?:ul\.|al\.))', re.IGNORECASE)
    return [s.address for s in stations if pattern.search(s.address)]

# ===========================================================================
# Task 5 & 6 – CLI (argparse) with logging
# ===========================================================================

DATA_DIR = Path(__file__).parent / 'data'

_ALIASES: dict[str, str] = {'PM2.5': 'PM25'}


def _available_quantities(data_dir: Path) -> set[str]:
    mdir = data_dir / 'measurements'
    if not mdir.exists():
        return set()
    qty_re = re.compile(r'^\d{4}_(.+)_[^_]+\.csv$')
    return {m.group(1) for f in mdir.iterdir() if (m := qty_re.match(f.name))}


def _validate_date(s: str) -> date:
    try:
        return datetime.strptime(s, '%Y-%m-%d').date()
    except ValueError:
        raise argparse.ArgumentTypeError(f"Invalid date '{s}', expected YYYY-MM-DD")


def _validate_quantity(s: str) -> str:
    s = _ALIASES.get(s, s)
    available = _available_quantities(DATA_DIR)
    if available and s not in available:
        raise argparse.ArgumentTypeError(
            f"Unknown quantity '{s}'.\nAvailable: {', '.join(sorted(available))}"
        )
    return s


def _parse_meas_date(s: str) -> date | None:
    for fmt in ('%d/%m/%y %H:%M', '%Y-%m-%d %H:%M', '%Y-%m-%d'):
        try:
            return datetime.strptime(s, fmt).date()
        except ValueError:
            pass
    return None


# ---------- subcommand: random-station ----------

def cmd_random_station(args: argparse.Namespace) -> None:
    mdir = DATA_DIR / 'measurements'
    files = group_measurement_files_by_key(mdir)
    matching_codes: set[str] = set()

    for (year, qty, freq), fpath in files.items():
        if qty != args.quantity or freq != args.frequency:
            continue
        if not (args.start.year <= int(year) <= args.end.year):
            continue
        data = parse_measurements(fpath)
        for dt_str, values in data['data']:
            mdate = _parse_meas_date(dt_str)
            if mdate and args.start <= mdate <= args.end:
                matching_codes.update(code for code, val in values.items() if val is not None)

    if not matching_codes:
        log.warning(
            "No stations with data for quantity=%s frequency=%s in [%s, %s]",
            args.quantity, args.frequency, args.start, args.end,
        )
        print("No matching stations found.")
        return

    stations = parse_stations(DATA_DIR / 'stacje.csv')
    station_map = {s.code: s for s in stations}
    valid = [c for c in matching_codes if c in station_map]

    if not valid:
        log.warning("Matching measurement codes not found in station metadata")
        print("Station metadata unavailable.")
        return

    s = station_map[random.choice(valid)]
    print(f"Station : {s.name} ({s.code})")
    print(f"Address : {s.address or '—'}, {s.city}, {s.voivodeship}")


# ---------- subcommand: stats ----------

def cmd_stats(args: argparse.Namespace) -> None:
    mdir = DATA_DIR / 'measurements'
    files = group_measurement_files_by_key(mdir)
    values: list[float] = []
    found_file = False

    for (year, qty, freq), fpath in files.items():
        if qty != args.quantity or freq != args.frequency:
            continue
        if not (args.start.year <= int(year) <= args.end.year):
            continue
        found_file = True
        data = parse_measurements(fpath)

        if args.station not in data['station_codes']:
            log.warning("Station %s not present in %s", args.station, fpath.name)
            continue

        for dt_str, row_vals in data['data']:
            mdate = _parse_meas_date(dt_str)
            if mdate and args.start <= mdate <= args.end:
                val = row_vals.get(args.station)
                if val is not None:
                    values.append(val)

    if not found_file:
        log.warning("No files for quantity=%s frequency=%s", args.quantity, args.frequency)

    if not values:
        log.warning(
            "No measurements for station=%s in [%s, %s]", args.station, args.start, args.end
        )
        print("No data found.")
        return

    mean = statistics.mean(values)
    stdev = statistics.stdev(values) if len(values) > 1 else 0.0
    print(f"Station  : {args.station}")
    print(f"Quantity : {args.quantity}  Frequency: {args.frequency}")
    print(f"Period   : {args.start} – {args.end}")
    print(f"Count    : {len(values)}")
    print(f"Mean     : {mean:.4f} ug/m3")
    print(f"Std dev  : {stdev:.4f} ug/m3")


# ---------- parser ----------

def build_parser() -> argparse.ArgumentParser:
    parser = argparse.ArgumentParser(
        prog='airquality',
        description='GIOŚ air quality data analysis tool',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=(
            'Examples:\n'
            '  python main.py -q PM10 -f 24g -s 2023-01-01 -e 2023-01-31 random-station\n'
            '  python main.py -q NO2  -f 1g  -s 2023-06-01 -e 2023-06-30 stats DsWrocOrzech\n'
        ),
    )
    parser.add_argument('--quantity', '-q', type=_validate_quantity, required=True,
                        metavar='QUANTITY',
                        help='Measured quantity (e.g. PM10, PM25, NO2, SO2, CO)')
    parser.add_argument('--frequency', '-f', required=True,
                        choices=['1g', '24g', '1m'],
                        help='Averaging period: 1g (hourly), 24g (daily), 1m (monthly)')
    parser.add_argument('--start', '-s', type=_validate_date, required=True,
                        metavar='YYYY-MM-DD', help='Start of time range (inclusive)')
    parser.add_argument('--end', '-e', type=_validate_date, required=True,
                        metavar='YYYY-MM-DD', help='End of time range (inclusive)')

    sub = parser.add_subparsers(dest='command', required=True, title='subcommands')

    sub.add_parser(
        'random-station',
        help='Print name and address of a random station with data in the given period',
    ).set_defaults(func=cmd_random_station)

    stats_p = sub.add_parser(
        'stats',
        help='Calculate mean and std dev for a specific station',
    )
    stats_p.add_argument('station', help='Station code (e.g. DsWrocOrzech)')
    stats_p.set_defaults(func=cmd_stats)

    return parser


def main() -> None:
    setup_logging()
    parser = build_parser()
    args = parser.parse_args()
    if args.start > args.end:
        parser.error("--start must not be after --end")
    args.func(args)


# ===========================================================================
# Demo – run task 4 on the real stacje.csv and print results
# ===========================================================================

def run_task4_demo(stations_path: Path) -> None:
    stations = parse_stations(stations_path)
    total = len(stations)
    print(f"\n{'='*60}")
    print(f"Loaded {total} stations from {stations_path.name}")
    print('='*60)

    dates = task4_extract_dates(stations)
    print(f"\n[4a] Dates (YYYY-MM-DD): {len(dates)} found")
    print("     Sample:", dates[:5])

    coords = task4_extract_coordinates(stations)
    print(f"\n[4b] Coordinate pairs (6 decimals): {len(coords)} found")
    print("     Sample:", coords[:3])

    hyph = task4_find_hyphenated_names(stations)
    print(f"\n[4c] Two-part names (contains ' - '): {len(hyph)} found")
    print("     Sample:", hyph[:5])

    normed = task4_normalize_names(stations)
    print(f"\n[4d] Normalized names (first 5):")
    for n in normed[:5]:
        print("    ", n)

    mob_ok = task4_verify_mob_stations(stations)
    print(f"\n[4e] All MOB-code stations have type 'mobilna': {mob_ok}")

    three = task4_three_part_locations(stations)
    print(f"\n[4f] Three-part names: {len(three)} found")
    print("     Sample:", three[:5])

    streets = task4_street_locations(stations)
    print(f"\n[4g] Addresses with comma + ul./al.: {len(streets)} found")
    print("     Sample:", streets[:5])


if __name__ == '__main__':
    import sys
    if len(sys.argv) > 1 and sys.argv[1] == 'demo':
        setup_logging()
        run_task4_demo(DATA_DIR / 'stacje.csv')
    else:
        main()
