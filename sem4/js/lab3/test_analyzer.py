#!/usr/bin/env python3

import datetime
from log_analyzer import *


def run_all_tests():

    print("=" * 80)
    print("WCZYTYWANIE DANYCH")
    print("=" * 80)

    with open("http_first_100k.log", "r") as f:
        log = read_log(f)

    print(f"✓ Wczytano {len(log)} wpisów logów\n")

    if log:
        print("Przykładowy wpis (jako słownik):")
        example = entry_to_dict(log[0])
        for key, value in example.items():
            print(f"  {key}: {value}")

    print("\n" + "=" * 80)
    print("TESTY ZADAŃ")
    print("=" * 80)

    print("\n--- ZADANIE 2: Sortowanie logów ---")
    sorted_by_code = sort_log(log[:10], LogIndex.STATUS_CODE)
    print(f"✓ Posortowano {len(sorted_by_code)} wpisów po kodzie statusu")
    print(f"  Kody statusu: {[e[LogIndex.STATUS_CODE] for e in sorted_by_code]}")

    print("\n--- ZADANIE 3: Filtrowanie po kodzie statusu ---")
    code_404 = get_entries_by_code(log, 404)
    code_200 = get_entries_by_code(log, 200)
    print(f"✓ Żądania z kodem 404: {len(code_404)}")
    print(f"✓ Żądania z kodem 200: {len(code_200)}")

    print("\n--- ZADANIE 4: Filtrowanie po adresie ---")
    if log:
        sample_ip = log[0][LogIndex.ORIG_IP]
        by_ip = get_entries_by_addr(log, sample_ip)
        print(f"✓ Żądania z IP {sample_ip}: {len(by_ip)}")

    print("\n--- ZADANIE 5: Błędne żądania ---")
    errors_4xx, errors_5xx = get_failed_reads(log, merge=False)
    print(f"✓ Błędy 4xx: {len(errors_4xx)}")
    print(f"✓ Błędy 5xx: {len(errors_5xx)}")
    all_errors = get_failed_reads(log, merge=True)
    print(f"✓ Wszystkie błędy (merge=True): {len(all_errors)}")

    print("\n--- ZADANIE 6: Filtrowanie po rozszerzeniu ---")
    jpg_files = get_entries_by_extension(log, "jpg")
    html_files = get_entries_by_extension(log, "html")
    nsf_files = get_entries_by_extension(log, "nsf")
    print(f"✓ Pliki .jpg: {len(jpg_files)}")
    print(f"✓ Pliki .html: {len(html_files)}")
    print(f"✓ Pliki .nsf: {len(nsf_files)}")

    print("\n--- ZADANIE 7: Najczęstsze adresy IP ---")
    top_ips = get_top_ips(log, 5)
    print(f"✓ Top 5 adresów IP:")
    for i, (ip, count) in enumerate(top_ips, 1):
        print(f"  {i}. {ip}: {count} żądań")

    print("\n--- ZADANIE 8: Unikalne metody HTTP ---")
    unique_methods = get_unique_methods(log)
    print(f"✓ Znalezione metody: {', '.join(sorted(unique_methods))}")

    print("\n--- ZADANIE 9: Zakres czasu ---")
    if log:
        all_timestamps = [e[LogIndex.TS] for e in log]
        min_ts = min(all_timestamps)
        max_ts = max(all_timestamps)
        mid_ts = min_ts + (max_ts - min_ts) / 2

        time_range_entries = get_entries_in_time_range(log, min_ts, mid_ts)
        print(f"✓ Żądania w pierwszej połowie okresu: {len(time_range_entries)}")
        print(f"  Od: {min_ts}")
        print(f"  Do: {mid_ts}")

    print("\n--- ZADANIE 10: Liczba zapytań per metoda ---")
    method_counts = count_by_method(log)
    print(f"✓ Rozkład metod HTTP:")
    for method, count in sorted(
        method_counts.items(), key=lambda x: x[1], reverse=True
    ):
        percentage = count / len(log) * 100
        print(f"  {method}: {count} ({percentage:.2f}%)")

    print("\n--- ZADANIE 11: Najczęstsze URI ---")
    top_uris = get_top_uris(log, 5)
    print(f"✓ Top 5 URI:")
    for i, (uri, count) in enumerate(top_uris, 1):
        display_uri = uri if len(uri) <= 50 else uri[:47] + "..."
        print(f"  {i}. {display_uri}: {count}")

    print("\n--- ZADANIE 12: Rozkład kodów HTTP ---")
    status_classes = count_status_classes(log)
    print(f"✓ Rozkład klas kodów statusu:")
    for class_name, count in sorted(status_classes.items()):
        percentage = count / len(log) * 100
        print(f"  {class_name}: {count} ({percentage:.2f}%)")

    print("\n--- ZADANIE 13: Zamiana krotki na słownik ---")
    if log:
        entry_dict = entry_to_dict(log[0])
        print(f"✓ Przekonwertowano krotkę na słownik ({len(entry_dict)} pól)")

    print("\n--- ZADANIE 14: Log jako słownik sesji ---")
    log_dict = log_to_dict(log[:1000])
    print(f"✓ Utworzono słownik z {len(log_dict)} sesji")
    print(f"  (na podstawie pierwszych 1000 wpisów)")

    print("\n--- ZADANIE 15: Statystyki sesji ---")
    print(f"✓ Wyświetlanie statystyk dla pierwszych 3 sesji:")
    small_log_dict = dict(list(log_dict.items())[:3])
    print_dict_entry_dates(small_log_dict)

    print("\n--- ZADANIE 16: Najaktywniejsza sesja ---")
    most_active_uid, count = get_most_active_session(log)
    print(f"✓ Najaktywniejsza sesja:")
    print(f"  UID: {most_active_uid}")
    print(f"  Liczba żądań: {count}")

    print("\n--- ZADANIE 17: Odtwarzanie sesji użytkownika ---")
    session_paths = get_session_paths(log[:1000])
    print(f"✓ Utworzono ścieżki dla {len(session_paths)} sesji")
    if session_paths:
        sample_uid = list(session_paths.keys())[0]
        sample_paths = session_paths[sample_uid]
        print(f"  Przykład (sesja {sample_uid}):")
        for path in sample_paths[:5]:
            display_path = path if len(path) <= 60 else path[:57] + "..."
            print(f"    → {display_path}")
        if len(sample_paths) > 5:
            print(f"    ... i {len(sample_paths) - 5} więcej")

    print("\n--- ZADANIE 18: Podejrzane IP ---")
    suspicious = detect_sus(log, 1000)
    print(f"✓ Znaleziono {len(suspicious)} podejrzanych IP (próg: 1000 żądań)")
    if suspicious:
        print(f"  Top 5 podejrzanych IP:")
        for i, (ip, count) in enumerate(suspicious[:5], 1):
            print(f"    {i}. {ip}: {count} żądań")

    print("\n--- ZADANIE 19: Rozkład rozszerzeń plików ---")
    extensions = get_extension_stats(log)
    print(f"✓ Znaleziono {len(extensions)} różnych rozszerzeń")
    top_extensions = sorted(extensions.items(), key=lambda x: x[1], reverse=True)[:10]
    print(f"  Top 10 rozszerzeń:")
    for i, (ext, count) in enumerate(top_extensions, 1):
        print(f"    {i}. .{ext}: {count}")

    print("\n" + "=" * 80)
    print("ZADANIE 20: KOMPLEKSOWA ANALIZA LOGU")
    print("=" * 80)
    analysis = analyze_log(log)
    print_analysis(analysis)

    print("\n" + "=" * 80)
    print("WSZYSTKIE TESTY ZAKOŃCZONE POMYŚLNIE ✓")
    print("=" * 80)


def interactive_demo():
    print("\n" + "=" * 80)
    print("INTERAKTYWNA DEMONSTRACJA ANALIZATORA LOGÓW")
    print("=" * 80)

    print("\nWczytywanie logów...")
    with open("http_first_100k.log", "r") as f:
        log = read_log(f)
    print(f"Wczytano {len(log)} wpisów.\n")

    while True:
        print("\nDostępne opcje:")
        print("1. Pokaż statystyki błędów")
        print("2. Wyszukaj żądania po kodzie HTTP")
        print("3. Wyszukaj żądania po adresie IP")
        print("4. Pokaż top IP")
        print("5. Pokaż top URI")
        print("6. Znajdź pliki z rozszerzeniem")
        print("7. Pokaż najaktywniejszą sesję")
        print("8. Wykryj podejrzane IP")
        print("9. Pełna analiza logu")
        print("0. Wyjście")

        choice = input("\nWybierz opcję (0-9): ").strip()

        if choice == "0":
            print("Do widzenia!")
            break

        elif choice == "1":
            errors_4xx, errors_5xx = get_failed_reads(log)
            print(f"\nBłędy 4xx: {len(errors_4xx)}")
            print(f"Błędy 5xx: {len(errors_5xx)}")
            print(f"Razem: {len(errors_4xx) + len(errors_5xx)}")
            error_rate = (len(errors_4xx) + len(errors_5xx)) / len(log) * 100
            print(f"Wskaźnik błędów: {error_rate:.2f}%")

        elif choice == "2":
            code = input("Podaj kod HTTP (np. 200, 404): ").strip()
            try:
                code = int(code)
                entries = get_entries_by_code(log, code)
                print(f"\nZnaleziono {len(entries)} wpisów z kodem {code}")
                if entries:
                    show = input("Pokazać pierwsze 5? (t/n): ").strip().lower()
                    if show == "t":
                        for i, entry in enumerate(entries[:5], 1):
                            print(
                                f"{i}. {entry[LogIndex.METHOD]} {entry[LogIndex.URI]}"
                            )
            except ValueError as e:
                print(f"Błąd: {e}")

        elif choice == "3":
            addr = input("Podaj adres IP lub host: ").strip()
            try:
                entries = get_entries_by_addr(log, addr)
                print(f"\nZnaleziono {len(entries)} wpisów dla {addr}")
            except ValueError as e:
                print(f"Błąd: {e}")

        elif choice == "4":
            n = input("Ile top IP pokazać? (domyślnie 10): ").strip()
            n = int(n) if n else 10
            top_ips = get_top_ips(log, n)
            print(f"\nTop {n} adresów IP:")
            for i, (ip, count) in enumerate(top_ips, 1):
                print(f"{i}. {ip}: {count} żądań")

        elif choice == "5":
            n = input("Ile top URI pokazać? (domyślnie 10): ").strip()
            n = int(n) if n else 10
            top_uris = get_top_uris(log, n)
            print(f"\nTop {n} URI:")
            for i, (uri, count) in enumerate(top_uris, 1):
                display_uri = uri if len(uri) <= 70 else uri[:67] + "..."
                print(f"{i}. {display_uri}: {count}")

        elif choice == "6":
            ext = input("Podaj rozszerzenie (np. jpg, html): ").strip()
            entries = get_entries_by_extension(log, ext)
            print(f"\nZnaleziono {len(entries)} plików z rozszerzeniem .{ext}")

        elif choice == "7":
            uid, count = get_most_active_session(log)
            print(f"\nNajaktywniejsza sesja:")
            print(f"UID: {uid}")
            print(f"Liczba żądań: {count}")

            show_paths = input("Pokazać ścieżki tej sesji? (t/n): ").strip().lower()
            if show_paths == "t":
                session_entries = [e for e in log if e[LogIndex.UID] == uid]
                print(f"\nŚcieżki sesji {uid}:")
                for i, entry in enumerate(session_entries[:20], 1):
                    print(f"{i}. {entry[LogIndex.METHOD]} {entry[LogIndex.URI]}")
                if len(session_entries) > 20:
                    print(f"... i {len(session_entries) - 20} więcej")

        elif choice == "8":
            threshold = input("Podaj próg liczby żądań (domyślnie 1000): ").strip()
            threshold = int(threshold) if threshold else 1000
            suspicious = detect_sus(log, threshold)
            print(f"\nZnaleziono {len(suspicious)} podejrzanych IP:")
            for i, (ip, count) in enumerate(suspicious[:10], 1):
                print(f"{i}. {ip}: {count} żądań")

        elif choice == "9":
            print("\nPrzeprowadzanie pełnej analizy...")
            analysis = analyze_log(log)
            print_analysis(analysis)

        else:
            print("Nieprawidłowa opcja!")


if __name__ == "__main__":
    import sys

    if len(sys.argv) > 1 and sys.argv[1] == "--interactive":
        interactive_demo()
    else:
        run_all_tests()
