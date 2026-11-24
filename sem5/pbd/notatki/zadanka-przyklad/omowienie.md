# Zadania 1–9: co trzeba umieć

## Zad 1: funkcje wierszowe, daty, CASE
- **Data:** `EXTRACT(MONTH FROM data) >= 6` daje drugą połowę roku.
- **Pełne lata stażu:** `TRUNC(MONTHS_BETWEEN(DATE '2012-01-01', w_stadku_od) / 12)`. Samo odejmowanie lat (`2012 - rok`) jest złe, bo nie patrzy na miesiąc i dzień. W Postgresie to `EXTRACT(YEAR FROM AGE(...))`.
- **NULL w sumie:** `przydzial + NVL(myszy_extra, 0)`, bo `50 + NULL = NULL`. W Postgresie zamiast `NVL` jest `COALESCE`.
- **CASE:** warunki sprawdzają się po kolei, wygrywa pierwszy prawdziwy. Dlatego wystarczy `< 50`, potem `<= 70`, a na koniec `ELSE`.
- **INITCAP:** zmienia `BOLEK` na `Bolek`.
- **ORDER BY:** można sortować po aliasie w cudzysłowie (`"Staz"`).

## Zad 2: GROUP BY, agregaty, HAVING
- **COUNT – trzy warianty:** `COUNT(*)` liczy wiersze, `COUNT(kolumna)` pomija NULL-e (stąd „z extra” to po prostu `COUNT(myszy_extra)`), a `COUNT(CASE WHEN warunek THEN 1 END)` liczy tylko wiersze spełniające warunek.
- **Liczenie warunkowe:** `SUM(plec = 'D')` nie działa w Oracle. Działa na przykład w MySQL i myli ludzi.
- **WHERE a HAVING:** `WHERE` filtruje wiersze przed grupowaniem (np. pominięcie SZEFUNIO). `HAVING` filtruje gotowe grupy (`COUNT(*) >= 3`).
- **Rozpiętość:** `MAX(x) - MIN(x)`.
- **Zaokrąglenie:** `ROUND(AVG(...), 2)`.

## Zad 3: złączenia zewnętrzne i mnożenie wierszy
- **Każda banda w wyniku:** banda musi być stroną, z której bierzemy wszystko. Robi się to przez `bandy B LEFT JOIN kocury`, ewentualnie `kocury RIGHT JOIN bandy`.
- **Imię szefa:** drugie złączenie z `kocury` (alias `S`) po `szef_bandy`. Musi być `LEFT`, bo ROCKERSI nie mają szefa.
- **Pułapka:** złączenie z `wrogowie_kocurow` mnoży wiersze. Kot z dwoma wrogami pojawia się dwa razy, dlatego:
  - liczba kotów to `COUNT(DISTINCT K.pseudo)`;
  - liczba incydentów to `COUNT(WK.pseudo)`, który liczy tylko niepuste wiersze;
  - kot bez wrogów to wiersz, w którym `WK.pseudo IS NULL`, a `K.pseudo IS NOT NULL`. Ten drugi warunek odrzuca pustą bandę.
- **GROUP BY:** wszystkie niezagregowane kolumny muszą być w `GROUP BY`. W Oracle to obowiązkowe.

## Zad 4: funkcje okna kontra podzapytania skorelowane
- **Funkcje okna (`OVER`):** liczą agregat, ale nie zwijają wierszy.
  - `AVG(x) OVER (PARTITION BY nr_bandy)` to średnia bandy w każdym wierszu.
  - `RANK() OVER (PARTITION BY nr_bandy ORDER BY x DESC)` daje miejsca 1, 2, 2, 4. `DENSE_RANK` dałby 1, 2, 2, 3, a `ROW_NUMBER` 1, 2, 3, 4.
- **Podzapytanie skorelowane w SELECT:** zapytanie liczone osobno dla każdego wiersza, np. `(SELECT COUNT(*) FROM kocury P WHERE P.szef = K.pseudo)`. Dzięki temu liczenie podwładnych i wrogów nie mnoży wierszy.
- **„-” zamiast NULL:** `NVL(TO_CHAR(MAX(...)), '-')`. Liczbę trzeba zamienić na tekst, bo obie gałęzie muszą mieć ten sam typ.
- **Warunek przez podzapytanie:** bandy z co najmniej 4 kotami to `nr_bandy IN (SELECT nr_bandy ... GROUP BY ... HAVING COUNT(*) >= 4)`.
- **Widełki:** złączenie z tabelą `funkcje` i `CASE` z `min_myszy` / `max_myszy`.
- **Wersja bez okien:**
  - średnia to `(SELECT AVG(...) FROM kocury X WHERE X.nr_bandy = K.nr_bandy)`;
  - miejsce to `1 + liczba kotów w bandzie, które jedzą więcej`. To dokładnie odtwarza działanie `RANK`.

## Zad 5: drzewo w Oracle, droga w górę
- **Składnia:** `START WITH` wybiera, od kogo zaczynamy (KOT, MILUSIA). `CONNECT BY` mówi, w którą stronę idziemy.
  - `CONNECT BY pseudo = PRIOR szef` idzie w górę: następny wiersz to szef poprzedniego.
  - `CONNECT BY PRIOR pseudo = szef` idzie w dół, do podwładnych.
- **`LEVEL`:** głębokość, korzeń ma 1, więc liczba szczebli to `LEVEL - 1`.
- **`CONNECT_BY_ROOT pseudo`:** wartość z wiersza startowego, czyli nasz kot.
- **`SYS_CONNECT_BY_PATH(pseudo, ' -> ')`:** cała ścieżka, ale z separatorem na początku, stąd `SUBSTR(..., 5)`.
- **Kolejność wykonania:** `WHERE` działa dopiero po zbudowaniu drzewa. Dlatego `WHERE szef IS NULL` zostawia tylko ostatni wiersz każdej ścieżki, ten z samą górą.
- **Pułapka:** w `ORDER BY` samo `pseudo` to pseudonim bieżącego wiersza (TYGRYS), a nie kota startowego. Trzeba sortować po aliasie albo numerze kolumny.
- **W Postgresie:** `WITH RECURSIVE`. Część startowa `UNION ALL` krok złączony z samym CTE. Licznik i ścieżkę przenosi się w kolumnach.

## Zad 6 i zad 9: „jedyny w grupie”, czyli NOT EXISTS
To ten sam schemat. Zad 9 jest wariantem zad 6.

```sql
WHERE NOT EXISTS (SELECT 1 FROM ... K2 ...
                  WHERE K2.nr_bandy = K.nr_bandy   -- ta sama grupa
                    AND K2.pseudo <> K.pseudo       -- ktoś INNY
                    AND W2.gatunek = W.gatunek)     -- z tym samym gatunkiem
```

- **Bez `K2.pseudo <> K.pseudo`** kot znajduje sam siebie i wynik jest pusty. To najczęstszy błąd.
- **Porównujesz gatunek** (`gatunek`), a nie konkretnego wroga (`imie_wroga`). Dlatego w podzapytaniu też trzeba dołączyć tabelę `wrogowie`.
- **`DISTINCT`:** kot mógł mieć kilka incydentów z tym samym gatunkiem.
- **`NOT EXISTS` a `NOT IN`:** `NOT IN` daje pusty wynik, gdy podzapytanie zwróci choć jeden NULL. `NOT EXISTS` nie ma tego problemu.

## Zad 7: porównanie z grupą, a potem liczenie
- **Krok 1:** podzapytanie skorelowane liczy średnią dla tej samej płci i funkcji:
  `x > (SELECT AVG(...) FROM kocury k2 WHERE k2.plec = k.plec AND k2.funkcja = k.funkcja)`
- **Krok 2:** `GROUP BY` banda i `HAVING COUNT(*) >= 2`.
- **Czytaj treść dokładnie:**
  - „średnia kotów o tej samej funkcji” wlicza samego kota;
  - „średnia **innych** kotów” wymaga dodania `AND k2.pseudo <> k.pseudo`;
  - „całkowity przydział” oznacza przydział + extra z `NVL`.
- **Alternatywa:** `AVG(...) OVER (PARTITION BY plec, funkcja)` w podzapytaniu w `FROM`, a filtr piętro wyżej. Funkcji okna nie wolno użyć w `WHERE` tego samego zapytania.

## Zad 8: poddrzewa i agregacja
- **Bez `START WITH`:** każdy kot jest korzeniem swojego poddrzewa. `CONNECT_BY_ROOT pseudo` mówi, do którego poddrzewa należy wiersz.
- **Agregacja:** w podzapytaniu powstają pary (korzeń, kot z jego poddrzewa). Na zewnątrz jest `GROUP BY korzen HAVING COUNT(*) >= 2 AND SUM(...) > 200`.
- **Sam korzeń** też jest w swoim poddrzewie (`LEVEL = 1`). Żeby liczyć tylko podwładnych, dodaj `WHERE LEVEL > 1`.
- **W Postgresie:** część startowa to `SELECT pseudo AS korzen, pseudo FROM kocury`, czyli wszyscy. Krok przenosi `korzen` w dół, a potem jest `GROUP BY korzen`.

## Funkcje z zadań 1–9 w podziale na bazy

### Wspólne (Oracle, Postgres, MSSQL)
| Funkcja | Co robi | Zadanie |
|---|---|---|
| `COUNT(*)` | liczy wszystkie wiersze w grupie | 2, 3, 4, 7, 8 |
| `COUNT(kol)` | liczy wiersze, w których kolumna nie jest NULL | 2 (`myszy_extra`), 3 (`WK.pseudo`) |
| `COUNT(DISTINCT kol)` | liczy unikalne wartości; ratunek przy mnożeniu wierszy przez JOIN | 3 |
| `COUNT(CASE WHEN … THEN 1 END)` | liczy tylko wiersze spełniające warunek | 2 |
| `SUM(x)` | suma; z `CASE … THEN 1 ELSE 0` też liczy warunkowo | 3, 8 |
| `AVG(x)` | średnia; pomija NULL-e, więc najpierw `NVL`/`COALESCE` | 2, 4, 7 |
| `MIN(x)` / `MAX(x)` | najmniejsza / największa wartość | 2, 4 |
| `ROUND(x, n)` | zaokrągla do n miejsc po przecinku | 2, 4 |
| `COALESCE(a, b, …)` | pierwszy argument różny od NULL | 1, 2, 4 |
| `CASE WHEN … THEN … ELSE … END` | if-else w zapytaniu; wygrywa pierwszy prawdziwy warunek | 1, 3, 4 |
| `EXTRACT(MONTH FROM d)` | część daty jako liczba (w MSSQL: `MONTH(d)` / `DATEPART`) | 1 |
| `RANK() OVER (PARTITION BY … ORDER BY …)` | miejsce z remisami i dziurami: 1, 2, 2, 4 | 4 |
| `DENSE_RANK() OVER (…)` | miejsce z remisami bez dziur: 1, 2, 2, 3 | 4 (alternatywa) |
| `ROW_NUMBER() OVER (…)` | kolejny numer, bez remisów: 1, 2, 3, 4 | 4 (alternatywa) |
| `AVG(x) OVER (PARTITION BY …)` | średnia grupy dopisana do każdego wiersza, bez zwijania | 4, 7 (alternatywa) |
| `EXISTS` / `NOT EXISTS` | czy podzapytanie zwraca jakikolwiek wiersz; odporne na NULL | 6, 9 |
| `IN (SELECT …)` | czy wartość jest w wyniku podzapytania | 4 |

### Oracle
| Funkcja | Co robi | Zadanie |
|---|---|---|
| `NVL(x, 0)` | zamienia NULL na wartość (2 argumenty) | 1, 2, 3, 4, 7 |
| `NVL2(x, a, b)` | `a`, gdy x nie jest NULL, `b`, gdy jest | 2 (alternatywa) |
| `INITCAP(s)` | pierwsza litera wielka, reszta małe: `BOLEK` → `Bolek` | 1 |
| `MONTHS_BETWEEN(d1, d2)` | liczba miesięcy między datami (ułamkowa) | 1 |
| `TRUNC(x)` | obcina część ułamkową (na liczbie); `TRUNC(d)` na dacie daje północ | 1 |
| `TO_CHAR(x)` | liczba/data na tekst; potrzebne, żeby dać `'-'` zamiast NULL | 4 |
| `DATE '2012-01-01'` | literał daty | 1 |
| `START WITH warunek` | od których wierszy zaczyna się drzewo | 5 |
| `CONNECT BY PRIOR pseudo = szef` | idzie w dół, do podwładnych | 8 |
| `CONNECT BY pseudo = PRIOR szef` | idzie w górę, do szefów | 5 |
| `LEVEL` | głębokość w drzewie, start = 1 | 5, 8 |
| `CONNECT_BY_ROOT kol` | wartość z wiersza, od którego zaczęła się ścieżka | 5, 8 |
| `SYS_CONNECT_BY_PATH(kol, sep)` | cała ścieżka jako napis, z separatorem na początku | 5 |
| `SUBSTR(s, od, dl)` | wycina fragment napisu; tu ucina początkowe `' -> '` | 5 |
| `CONNECT_BY_ISLEAF` | 1, gdy wiersz nie ma dzieci | 5 (alternatywa) |
| `dual` | sztuczna tabela z jednym wierszem do `SELECT 2009 FROM dual` | – |

### Postgres
| Funkcja | Co robi | Odpowiednik w Oracle |
|---|---|---|
| `COALESCE(x, 0)` | zamienia NULL (nie ma `NVL`) | `NVL` |
| `INITCAP(s)` | jak w Oracle | `INITCAP` |
| `AGE(d1, d2)` | odstęp w latach/miesiącach/dniach | `MONTHS_BETWEEN` |
| `EXTRACT(YEAR FROM AGE(d1, d2))` | pełne lata między datami | `TRUNC(MONTHS_BETWEEN(…) / 12)` |
| `x::text` / `CAST(x AS text)` | rzutowanie na tekst | `TO_CHAR` |
| `WITH RECURSIVE nazwa AS (start UNION ALL krok)` | drzewo; krok łączy się z samym CTE | `START WITH` + `CONNECT BY` |
| kolumna `poziom` / `poziom + 1` | ręczny licznik w rekurencji | `LEVEL` |
| kolumna `korzen` przenoszona w kroku | ręczny korzeń | `CONNECT_BY_ROOT` |
| `sciezka \|\| ' -> ' \|\| pseudo` (start: `pseudo::text`) | ręcznie budowana ścieżka | `SYS_CONNECT_BY_PATH` |
| `REPEAT(' ', n)` | wcięcie w drzewie | `LPAD(' ', n)` |
| `STRING_AGG(kol, ', ' ORDER BY kol)` | sklejenie wartości grupy w napis | `LISTAGG … WITHIN GROUP` |
| `COUNT(*) FILTER (WHERE …)` | krótszy warunkowy COUNT | `COUNT(CASE …)` |

### MSSQL (T-SQL)
| Funkcja | Co robi | Odpowiednik w Oracle |
|---|---|---|
| `ISNULL(x, 0)` | zamienia NULL | `NVL` |
| `MONTH(d)` / `YEAR(d)` / `DATEPART(MONTH, d)` | część daty | `EXTRACT` |
| `DATEDIFF(MONTH, d1, d2)` | liczy przekroczone granice miesięcy, a nie pełne miesiące | `MONTHS_BETWEEN` |
| `DATEADD(YEAR, n, d)` | dodaje lata/miesiące/dni | `ADD_MONTHS` |
| `CAST(x AS VARCHAR(10))` | rzutowanie na tekst | `TO_CHAR` |
| `UPPER(LEFT(s,1)) + LOWER(SUBSTRING(s,2,100))` | nie ma `INITCAP`, trzeba ręcznie | `INITCAP` |
| `+` | łączenie napisów (NULL psuje wynik) | `\|\|` |
| `WITH nazwa AS (start UNION ALL krok)` | drzewo, bez słowa `RECURSIVE` | `CONNECT BY` |
| `CAST(sciezka + ' -> ' + pseudo AS VARCHAR(200))` | ścieżka; obie części CTE muszą mieć ten sam typ | `SYS_CONNECT_BY_PATH` |
| `REPLICATE(' ', n)` | wcięcie w drzewie | `LPAD` |
| `STRING_AGG(kol, ', ') WITHIN GROUP (ORDER BY kol)` | sklejenie wartości grupy | `LISTAGG` |
| `AVG(1.0 * x)` | `AVG` z kolumny INT zwraca INT, `1.0 *` wymusza ułamek | – |

## Ściąga: które narzędzie do czego
| Treść zadania mówi… | Użyj |
|---|---|
| „dla każdej bandy (też pustej)” | `LEFT JOIN` od strony bandy, warunki w `ON` |
| „ile / średnio w grupie” | `GROUP BY` + `HAVING` |
| „w porównaniu do jego grupy” | podzapytanie skorelowane albo `OVER (PARTITION BY)` |
| „miejsce / ranking” | `RANK()` / `DENSE_RANK()` / `ROW_NUMBER()` |
| „jako jedyny / żaden inny” | `NOT EXISTS` + `<> siebie` |
| „szefowie / droga / poddrzewo” | `CONNECT BY` (Oracle) / `WITH RECURSIVE` (Postgres) |
| „zamiast NULL daj 0 / '-'” | `NVL` / `COALESCE` (dla '-' najpierw `TO_CHAR`) |
| złączenie z incydentami | uwaga na mnożenie wierszy: `COUNT(DISTINCT ...)` |
