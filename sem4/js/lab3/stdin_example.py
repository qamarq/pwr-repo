#!/usr/bin/env python3

import sys
from log_analyzer import (
    read_log,
    LogIndex,
    get_top_ips,
    count_by_method,
    count_status_classes,
    analyze_log,
    print_analysis,
)


def main():

    print("Czytanie logów z wejścia standardowego...", file=sys.stderr)
    print("(Aby zakończyć ręczne wprowadzanie, naciśnij Ctrl+D)\n", file=sys.stderr)

    log = read_log(sys.stdin)

    if not log:
        print("Błąd: Nie wczytano żadnych logów!", file=sys.stderr)
        return 1

    print(f"\n{'='*80}", file=sys.stderr)
    print(f"Wczytano {len(log)} wpisów logów ze stdin", file=sys.stderr)
    print(f"{'='*80}\n", file=sys.stderr)

    print("PODSTAWOWE STATYSTYKI:")
    print("=" * 80)

    timestamps = [entry[LogIndex.TS] for entry in log]
    print(f"\nZakres czasowy:")
    print(f"  Od: {min(timestamps)}")
    print(f"  Do: {max(timestamps)}")
    print(f"  Czas trwania: {max(timestamps) - min(timestamps)}")

    print(f"\nTop 5 adresów IP:")
    top_ips = get_top_ips(log, 5)
    for i, (ip, count) in enumerate(top_ips, 1):
        print(f"  {i}. {ip}: {count} żądań")

    print(f"\nRozkład metod HTTP:")
    methods = count_by_method(log)
    for method, count in sorted(methods.items(), key=lambda x: x[1], reverse=True):
        percentage = count / len(log) * 100
        print(f"  {method}: {count} ({percentage:.2f}%)")

    print(f"\nRozkład kodów statusu:")
    status_classes = count_status_classes(log)
    for class_name, count in sorted(status_classes.items()):
        percentage = count / len(log) * 100
        print(f"  {class_name}: {count} ({percentage:.2f}%)")

    print(f"\nPrzykładowe wpisy (pierwsze 3):")
    for i, entry in enumerate(log[:3], 1):
        print(f"\n  Wpis {i}:")
        print(f"    Czas: {entry[LogIndex.TS]}")
        print(f"    IP: {entry[LogIndex.ORIG_IP]}")
        print(f"    Metoda: {entry[LogIndex.METHOD]}")
        print(f"    URI: {entry[LogIndex.URI]}")
        print(f"    Status: {entry[LogIndex.STATUS_CODE]}")

    print("\n" + "=" * 80)


    return 0


if __name__ == "__main__":
    try:
        exit_code = main()
        sys.exit(exit_code)
    except KeyboardInterrupt:
        print("\n\nPrzerwano przez użytkownika.", file=sys.stderr)
        sys.exit(1)
    except Exception as e:
        print(f"\nBłąd: {e}", file=sys.stderr)
        import traceback

        traceback.print_exc(file=sys.stderr)
        sys.exit(1)
