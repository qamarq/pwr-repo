#!/usr/bin/env python3

import sys
import datetime
import re
from collections import Counter, defaultdict
from typing import List, Tuple, Dict, Union, Optional


class LogIndex:

    TS = 0
    UID = 1
    ORIG_IP = 2
    ORIG_PORT = 3
    RESP_IP = 4
    RESP_PORT = 5
    TRANS_DEPTH = 6
    METHOD = 7
    HOST = 8
    URI = 9
    REFERRER = 10
    USER_AGENT = 11
    REQ_LEN = 12
    RESP_LEN = 13
    STATUS_CODE = 14
    STATUS_MSG = 15


def read_log(input_stream=None) -> List[Tuple]:
    if input_stream is None:
        input_stream = sys.stdin

    log_entries = []

    for line in input_stream:
        line = line.strip()

        if not line:
            continue

        fields = line.split("\t")

        if len(fields) < 16:
            continue

        try:
            entry = (
                datetime.datetime.fromtimestamp(float(fields[0])),
                fields[1],
                fields[2],
                int(fields[3]),
                fields[4],
                int(fields[5]),
                int(fields[6]) if fields[6] != "-" else 0,
                fields[7],
                fields[8],
                fields[9],
                fields[10] if fields[10] != "-" else "",
                fields[11] if fields[11] != "-" else "",
                int(fields[12]) if fields[12] != "-" else 0,
                int(fields[13]) if fields[13] != "-" else 0,
                int(fields[14]) if fields[14] != "-" else 0,
                fields[15] if fields[15] != "-" else "",
            )
            log_entries.append(entry)
        except (ValueError, IndexError) as e:
            continue

    return log_entries


def sort_log(log: List[Tuple], index: int) -> List[Tuple]:
    if not log:
        return []

    if index < 0 or index >= len(log[0]):
        raise IndexError(
            f"Niepoprawny indeks: {index}. Dozwolony zakres: 0-{len(log[0])-1}"
        )

    try:
        return sorted(log, key=lambda x: x[index])
    except Exception as e:
        raise ValueError(f"Błąd podczas sortowania: {e}")


def get_entries_by_code(log: List[Tuple], code: int) -> List[Tuple]:
    if not isinstance(code, int) or code < 100 or code >= 600:
        raise ValueError(f"Niepoprawny kod HTTP: {code}. Dozwolony zakres: 100-599")

    return [entry for entry in log if entry[LogIndex.STATUS_CODE] == code]


def get_entries_by_addr(log: List[Tuple], addr: str) -> List[Tuple]:
    if re.match(r"^\d+\.\d+\.\d+\.\d+$", addr):
        parts = addr.split(".")
        if not all(0 <= int(p) <= 255 for p in parts):
            raise ValueError(f"Niepoprawny adres IP: {addr}")

    return [
        entry
        for entry in log
        if entry[LogIndex.ORIG_IP] == addr or entry[LogIndex.HOST] == addr
    ]


def get_failed_reads(
    log: List[Tuple], merge: bool = False
) -> Union[List[Tuple], Tuple[List[Tuple], List[Tuple]]]:
    errors_4xx = [entry for entry in log if 400 <= entry[LogIndex.STATUS_CODE] < 500]
    errors_5xx = [entry for entry in log if 500 <= entry[LogIndex.STATUS_CODE] < 600]

    if merge:
        return errors_4xx + errors_5xx
    else:
        return errors_4xx, errors_5xx


def get_entries_by_extension(log: List[Tuple], ext: str) -> List[Tuple]:
    ext = ext.lstrip(".")

    result = []
    for entry in log:
        uri = entry[LogIndex.URI]

        uri_without_params = uri.split("?")[0]

        if uri_without_params.endswith(f".{ext}"):
            result.append(entry)

    return result


def get_top_ips(log: List[Tuple], n: int = 10) -> List[Tuple[str, int]]:
    ip_counter = Counter(entry[LogIndex.ORIG_IP] for entry in log)
    return ip_counter.most_common(n)


def get_unique_methods(log: List[Tuple]) -> List[str]:
    return list(set(entry[LogIndex.METHOD] for entry in log))


def get_entries_in_time_range(
    log: List[Tuple], start: datetime.datetime, end: datetime.datetime
) -> List[Tuple]:
    return [entry for entry in log if start <= entry[LogIndex.TS] < end]


def count_by_method(log: List[Tuple]) -> Dict[str, int]:
    return dict(Counter(entry[LogIndex.METHOD] for entry in log))


def get_top_uris(log: List[Tuple], n: int = 10) -> List[Tuple[str, int]]:
    uri_counter = Counter(entry[LogIndex.URI] for entry in log)
    return uri_counter.most_common(n)


def count_status_classes(log: List[Tuple]) -> Dict[str, int]:
    classes = {"2xx": 0, "3xx": 0, "4xx": 0, "5xx": 0}

    for entry in log:
        code = entry[LogIndex.STATUS_CODE]
        if 200 <= code < 300:
            classes["2xx"] += 1
        elif 300 <= code < 400:
            classes["3xx"] += 1
        elif 400 <= code < 500:
            classes["4xx"] += 1
        elif 500 <= code < 600:
            classes["5xx"] += 1

    return classes


def entry_to_dict(entry: Tuple) -> Dict[str, any]:
    return {
        "ts": entry[LogIndex.TS],
        "uid": entry[LogIndex.UID],
        "orig_ip": entry[LogIndex.ORIG_IP],
        "orig_port": entry[LogIndex.ORIG_PORT],
        "resp_ip": entry[LogIndex.RESP_IP],
        "resp_port": entry[LogIndex.RESP_PORT],
        "trans_depth": entry[LogIndex.TRANS_DEPTH],
        "method": entry[LogIndex.METHOD],
        "host": entry[LogIndex.HOST],
        "uri": entry[LogIndex.URI],
        "referrer": entry[LogIndex.REFERRER],
        "user_agent": entry[LogIndex.USER_AGENT],
        "request_len": entry[LogIndex.REQ_LEN],
        "response_len": entry[LogIndex.RESP_LEN],
        "status_code": entry[LogIndex.STATUS_CODE],
        "status_msg": entry[LogIndex.STATUS_MSG],
    }


def log_to_dict(log: List[Tuple]) -> Dict[str, List[Dict]]:
    session_dict = defaultdict(list)

    for entry in log:
        uid = entry[LogIndex.UID]
        session_dict[uid].append(entry_to_dict(entry))

    return dict(session_dict)


def print_dict_entry_dates(log_dict: Dict[str, List[Dict]]) -> None:
    for uid, entries in log_dict.items():
        if not entries:
            continue

        ips = set(e["orig_ip"] for e in entries)
        hosts = set(e["host"] for e in entries if e["host"])

        num_requests = len(entries)

        timestamps = [e["ts"] for e in entries]
        first_request = min(timestamps)
        last_request = max(timestamps)

        methods = [e["method"] for e in entries]
        method_counts = Counter(methods)
        method_percentages = {
            m: (count / num_requests * 100) for m, count in method_counts.items()
        }

        codes_2xx = sum(1 for e in entries if 200 <= e["status_code"] < 300)
        success_ratio = codes_2xx / num_requests * 100 if num_requests > 0 else 0

        print(f"\n{'='*80}")
        print(f"Sesja UID: {uid}")
        print(f"{'='*80}")
        print(f"Adresy IP: {', '.join(ips)}")
        print(f"Hosty: {', '.join(hosts) if hosts else 'brak'}")
        print(f"Liczba żądań: {num_requests}")
        print(f"Pierwsze żądanie: {first_request}")
        print(f"Ostatnie żądanie: {last_request}")
        print(f"Czas trwania sesji: {last_request - first_request}")
        print(f"\nRozkład metod HTTP:")
        for method, percentage in sorted(method_percentages.items()):
            print(f"  {method}: {percentage:.2f}%")
        print(f"\nUdział kodów 2xx: {success_ratio:.2f}%")


def get_most_active_session(log: List[Tuple]) -> Tuple[str, int]:
    uid_counter = Counter(entry[LogIndex.UID] for entry in log)
    most_common = uid_counter.most_common(1)
    return most_common[0] if most_common else (None, 0)


def get_session_paths(log: List[Tuple]) -> Dict[str, List[str]]:
    session_paths = defaultdict(list)

    sorted_log = sorted(log, key=lambda x: (x[LogIndex.UID], x[LogIndex.TS]))

    for entry in sorted_log:
        uid = entry[LogIndex.UID]
        uri = entry[LogIndex.URI]
        session_paths[uid].append(uri)

    return dict(session_paths)


def detect_sus(log: List[Tuple], threshold: int) -> List[Tuple[str, int]]:
    ip_stats = defaultdict(lambda: {"count": 0, "errors_404": 0, "timestamps": []})

    for entry in log:
        ip = entry[LogIndex.ORIG_IP]
        ip_stats[ip]["count"] += 1
        ip_stats[ip]["timestamps"].append(entry[LogIndex.TS])

        if entry[LogIndex.STATUS_CODE] == 404:
            ip_stats[ip]["errors_404"] += 1

    suspicious = []

    for ip, stats in ip_stats.items():
        if stats["count"] > threshold:
            error_ratio = stats["errors_404"] / stats["count"]

            if len(stats["timestamps"]) > 1:
                sorted_ts = sorted(stats["timestamps"])
                time_diffs = [
                    (sorted_ts[i + 1] - sorted_ts[i]).total_seconds()
                    for i in range(len(sorted_ts) - 1)
                ]
                avg_time_diff = (
                    sum(time_diffs) / len(time_diffs) if time_diffs else float("inf")
                )
            else:
                avg_time_diff = float("inf")

            if error_ratio > 0.2 or avg_time_diff < 1.0:
                suspicious.append((ip, stats["count"]))

    return sorted(suspicious, key=lambda x: x[1], reverse=True)


def get_extension_stats(log: List[Tuple]) -> Dict[str, int]:
    extension_counter = Counter()

    for entry in log:
        uri = entry[LogIndex.URI]

        uri_without_params = uri.split("?")[0]

        if "." in uri_without_params:
            ext = uri_without_params.rsplit(".", 1)[-1].lower()

            if len(ext) <= 5 and ext.isalnum():
                extension_counter[ext] += 1

    return dict(extension_counter)


def analyze_log(log: List[Tuple]) -> Dict[str, any]:
    if not log:
        return {"error": "Pusty log"}

    total_requests = len(log)

    timestamps = [entry[LogIndex.TS] for entry in log]
    time_range = (min(timestamps), max(timestamps))
    duration = time_range[1] - time_range[0]

    top_ips = get_top_ips(log, 5)

    top_uris = get_top_uris(log, 5)

    method_distribution = count_by_method(log)

    status_classes = count_status_classes(log)

    errors_4xx, errors_5xx = get_failed_reads(log, merge=False)
    total_errors = len(errors_4xx) + len(errors_5xx)
    error_rate = (total_errors / total_requests * 100) if total_requests > 0 else 0

    extensions = get_extension_stats(log)
    top_extensions = sorted(extensions.items(), key=lambda x: x[1], reverse=True)[:10]

    unique_ips = len(set(entry[LogIndex.ORIG_IP] for entry in log))
    unique_hosts = len(
        set(entry[LogIndex.HOST] for entry in log if entry[LogIndex.HOST])
    )
    unique_sessions = len(set(entry[LogIndex.UID] for entry in log))

    avg_requests_per_session = (
        total_requests / unique_sessions if unique_sessions > 0 else 0
    )

    most_active = get_most_active_session(log)

    return {
        "total_requests": total_requests,
        "time_range": {
            "start": time_range[0],
            "end": time_range[1],
            "duration": str(duration),
        },
        "top_ips": top_ips,
        "top_uris": top_uris,
        "method_distribution": method_distribution,
        "status_code_classes": status_classes,
        "errors": {
            "total": total_errors,
            "error_rate_percent": error_rate,
            "4xx_count": len(errors_4xx),
            "5xx_count": len(errors_5xx),
        },
        "top_file_extensions": top_extensions,
        "unique_stats": {
            "unique_ips": unique_ips,
            "unique_hosts": unique_hosts,
            "unique_sessions": unique_sessions,
        },
        "session_stats": {
            "avg_requests_per_session": avg_requests_per_session,
            "most_active_session": most_active,
        },
    }


def print_analysis(analysis: Dict) -> None:
    print("\n" + "=" * 80)
    print("ANALIZA LOGU HTTP")
    print("=" * 80)

    print(f"\nCałkowita liczba żądań: {analysis['total_requests']}")

    print(f"\nZakres czasowy:")
    print(f"  Od: {analysis['time_range']['start']}")
    print(f"  Do: {analysis['time_range']['end']}")
    print(f"  Czas trwania: {analysis['time_range']['duration']}")

    print(f"\nNajczęstsze adresy IP:")
    for ip, count in analysis["top_ips"]:
        print(f"  {ip}: {count} żądań")

    print(f"\nNajczęstsze URI:")
    for uri, count in analysis["top_uris"]:
        print(f"  {uri}: {count} żądań")

    print(f"\nRozkład metod HTTP:")
    for method, count in sorted(analysis["method_distribution"].items()):
        percentage = count / analysis["total_requests"] * 100
        print(f"  {method}: {count} ({percentage:.2f}%)")

    print(f"\nRozkład kodów statusu:")
    for class_name, count in sorted(analysis["status_code_classes"].items()):
        percentage = count / analysis["total_requests"] * 100
        print(f"  {class_name}: {count} ({percentage:.2f}%)")

    print(f"\nBłędy:")
    print(f"  Całkowita liczba błędów: {analysis['errors']['total']}")
    print(f"  Wskaźnik błędów: {analysis['errors']['error_rate_percent']:.2f}%")
    print(f"  Błędy 4xx: {analysis['errors']['4xx_count']}")
    print(f"  Błędy 5xx: {analysis['errors']['5xx_count']}")

    print(f"\nNajczęstsze rozszerzenia plików:")
    for ext, count in analysis["top_file_extensions"]:
        print(f"  .{ext}: {count}")

    print(f"\nStatystyki unikalne:")
    print(f"  Unikalne IP: {analysis['unique_stats']['unique_ips']}")
    print(f"  Unikalne hosty: {analysis['unique_stats']['unique_hosts']}")
    print(f"  Unikalne sesje: {analysis['unique_stats']['unique_sessions']}")

    print(f"\nStatystyki sesji:")
    print(
        f"  Średnia liczba żądań na sesję: {analysis['session_stats']['avg_requests_per_session']:.2f}"
    )
    print(
        f"  Najaktywniejsza sesja: {analysis['session_stats']['most_active_session'][0]} "
        f"({analysis['session_stats']['most_active_session'][1]} żądań)"
    )

    print("\n" + "=" * 80)


def main():
    print("Wczytywanie logów z pliku...")

    with open("http_first_100k.log", "r") as f:
        log = read_log(f)

    print(f"Wczytano {len(log)} wpisów logów.\n")

    print("=" * 80)
    print("PRZYKŁADOWE OPERACJE")
    print("=" * 80)

    print("\n1. Znajdowanie żądań z kodem 200:")
    code_200 = get_entries_by_code(log, 200)
    print(f"   Znaleziono: {len(code_200)} wpisów")

    print("\n2. Top 5 najczęstszych IP:")
    top_ips = get_top_ips(log, 5)
    for ip, count in top_ips:
        print(f"   {ip}: {count}")

    print("\n3. Unikalne metody HTTP:")
    methods = get_unique_methods(log)
    print(f"   {', '.join(methods)}")

    print("\n4. Statystyki błędów:")
    errors_4xx, errors_5xx = get_failed_reads(log)
    print(f"   Błędy 4xx: {len(errors_4xx)}")
    print(f"   Błędy 5xx: {len(errors_5xx)}")

    print("\n5. Najaktywniejsza sesja:")
    uid, count = get_most_active_session(log)
    print(f"   UID: {uid}, Liczba żądań: {count}")

    print("\n6. Podejrzane IP (threshold=1000):")
    suspicious = detect_sus(log, 1000)
    for ip, count in suspicious[:5]:
        print(f"   {ip}: {count} żądań")

    print("\n\n")
    analysis = analyze_log(log)
    print_analysis(analysis)


if __name__ == "__main__":
    main()
