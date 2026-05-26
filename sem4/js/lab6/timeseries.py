import math
import datetime
from typing import Union


class TimeSeries:
    def __init__(
        self,
        parameter_name: str,
        station_code: str,
        averaging_time: str,
        dates: list[datetime.datetime],
        values: list[float | None],
        unit: str,
    ):
        self.parameter_name = parameter_name
        self.station_code = station_code
        self.averaging_time = averaging_time
        self._dates = list(dates)
        self._values = list(values)
        self.unit = unit

    @property
    def dates(self) -> list[datetime.datetime]:
        return self._dates

    @property
    def values(self) -> list[float | None]:
        return self._values

    @property
    def mean(self) -> float | None:
        valid = [v for v in self._values if v is not None]
        if not valid:
            return None
        return sum(valid) / len(valid)

    @property
    def stddev(self) -> float | None:
        valid = [v for v in self._values if v is not None]
        n = len(valid)
        if n < 2:
            return None
        m = sum(valid) / n
        return math.sqrt(sum((x - m) ** 2 for x in valid) / n)

    def __getitem__(
        self,
        key: Union[int, slice, datetime.date, datetime.datetime],
    ):
        if isinstance(key, int):
            return (self._dates[key], self._values[key])

        if isinstance(key, slice):
            return [(d, v) for d, v in zip(self._dates[key], self._values[key])]

        if isinstance(key, datetime.datetime):
            for d, v in zip(self._dates, self._values):
                if d == key:
                    return v
            raise KeyError(key)

        if isinstance(key, datetime.date):
            matches = [v for d, v in zip(self._dates, self._values) if d.date() == key]
            if not matches:
                raise KeyError(key)
            return matches if len(matches) > 1 else matches[0]

        raise TypeError(f"Unsupported key type: {type(key)}")

    def __len__(self) -> int:
        return len(self._dates)

    def __add__(self, other: "TimeSeries") -> "TimeSeries":
        if not isinstance(other, TimeSeries):
            return NotImplemented
        if self.station_code != other.station_code:
            raise ValueError(
                f"rozne stacje: " f"{self.station_code!r} vs {other.station_code!r}"
            )
        if self.parameter_name != other.parameter_name:
            raise ValueError(
                f"rozne parametry: "
                f"{self.parameter_name!r} vs {other.parameter_name!r}"
            )
        if self.averaging_time != other.averaging_time:
            raise ValueError(
                f"rozne srednie czasy: "
                f"{self.averaging_time!r} vs {other.averaging_time!r}"
            )
        combined = sorted(
            zip(self._dates + other._dates, self._values + other._values),
            key=lambda pair: pair[0],
        )
        dates, values = zip(*combined) if combined else ([], [])
        return TimeSeries(
            parameter_name=self.parameter_name,
            station_code=self.station_code,
            averaging_time=self.averaging_time,
            dates=list(dates),
            values=list(values),
            unit=self.unit,
        )

    def __repr__(self) -> str:
        return (
            f"TimeSeries(param={self.parameter_name!r}, "
            f"station={self.station_code!r}, "
            f"n={len(self._dates)})"
        )


if __name__ == "__main__":
    from datetime import datetime

    d = lambda s: datetime.fromisoformat(s)

    s1 = TimeSeries(
        "p10",
        "WRO100",
        "1h",
        [d("2024-01-01 00:00"), d("2024-01-01 01:00"), d("2024-01-01 02:00")],
        values=[10.0, 12.5, None],
        unit="µg/m³",
    )

    s2 = TimeSeries(
        "p10",
        "WRO100",
        "1h",
        dates=[d("2024-01-01 03:00"), d("2024-01-01 04:00")],
        values=[9.0, 11.0],
        unit="µg/m³",
    )

    s3 = s1 + s2
    print("polaczone s1 + s2:", s3)
    for date, value in zip(s3.dates, s3.values):
        print(f"  {date}  →  {value}")

    s4 = TimeSeries(
        "p10",
        "WRO100",
        "1h",
        dates=[d("2024-01-01 06:00"), d("2024-01-01 05:00")],
        values=[15.0, 14.0],
        unit="µg/m³",
    )

    s5 = s3 + s4
    print("polaczone nie po kolei s3 + s4:", s5)
    for date, value in zip(s5.dates, s5.values):
        print(f"  {date}  →  {value}")

    # zly znacznik
    s6 = TimeSeries(
        "p12",
        "WRO100",
        "1h",
        dates=[d("2024-01-01 07:00"), d("2024-01-01 08:00")],
        values=[15.0, 14.0],
        unit="µg/m³",
    )

    try:
        s7 = s5 + s6
        print("zle polaczenie:", s5)
        for date, value in zip(s5.dates, s5.values):
            print(f"  {date}  →  {value}")
    except ValueError as e:
        print(f"  ValueError: {e}")

    # zla stacja
    s6 = TimeSeries(
        "p10",
        "WRO101",
        "1h",
        dates=[d("2024-01-01 07:00"), d("2024-01-01 08:00")],
        values=[15.0, 14.0],
        unit="µg/m³",
    )

    try:
        s7 = s5 + s6
        print("zle polaczenie:", s5)
        for date, value in zip(s5.dates, s5.values):
            print(f"  {date}  →  {value}")
    except ValueError as e:
        print(f"  ValueError: {e}")

    # zly czas
    s6 = TimeSeries(
        "p10",
        "WRO100",
        "30min",
        dates=[d("2024-01-01 07:00"), d("2024-01-01 08:00")],
        values=[15.0, 14.0],
        unit="µg/m³",
    )

    try:
        s7 = s5 + s6
        print("zle polaczenie:", s5)
        for date, value in zip(s5.dates, s5.values):
            print(f"  {date}  →  {value}")
    except ValueError as e:
        print(f"  ValueError: {e}")
