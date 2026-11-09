# PBD – Lista projektowa nr 2 – raport

Środowisko: PostgreSQL 17 oraz Oracle Database 23ai Free (`gvenzl/oracle-free`), uruchomione w Dockerze (`docker-compose.yml`), z danymi wczytanymi skryptami z listy 1.

- **PostgreSQL** (wybrany SZBD) – zadania 1–13 (`postgres/zadania.sql`, wynik: `postgres/wynik.txt`).
- **Oracle** – zadania 8–10, wymagane w drugim SZBD (`oracle/zadania.sql`, wynik: `oracle/wynik.txt`).

Wszystkie zapytania korzystają wyłącznie z `SELECT` na jednej tabeli (bez podzapytań i złączeń), funkcji SQL oraz grupowania. Na początku skryptu ustawiam `DateStyle = 'ISO'`, dzięki czemu daty są wyświetlane jako `YYYY-MM-DD` i można je doklejać do napisów bez `TO_CHAR`.

---

## Część 1

### Zad. 1 – wrogowie z incydentami w 2009 r.

Imię wroga jest w tabeli `Wrogowie_kocurow`, więc złączenie z `Wrogowie` nie jest potrzebne.

```sql
SELECT imie_wroga "WROG", opis_incydentu "PRZEWINA"
FROM Wrogowie_kocurow
WHERE EXTRACT(YEAR FROM data_incydentu) = 2009;
```

```
    WROG    |             PRZEWINA
------------+-----------------------------------
 KAZIO      | ZLAPAL ZA OGON I ZROBIL WIATRAK
 KAZIO      | CHCIAL OBEDRZEC ZE SKORY
 REKSIO     | WYJATKOWO NIEGRZECZNIE OBSZCZEKAL
 BETHOVEN   | NIE PODZIELIL SIE SWOJA KASZA
 DZIKI BILL | ODGRYZL OGON
```

Zbiór wierszy taki sam jak w przykładzie; zadanie nie określa kolejności, więc nie dodawałem `ORDER BY`.

### Zad. 2 – kotki przyjęte między 2005-09-01 a 2007-07-31

```sql
SELECT imie "IMIE", funkcja "FUNKCJA", w_stadku_od "Z NAMI OD"
FROM Kocury
WHERE plec = 'D' AND w_stadku_od BETWEEN '2005-09-01' AND '2007-07-31';
```

```
 IMIE  | FUNKCJA | Z NAMI OD
-------+---------+------------
 ZUZIA | LOWCZY  | 2006-07-21
 RUDA  | MILUSIA | 2006-09-17
```

### Zad. 3 – nieprzekupni wrogowie

Nieprzekupny wróg to taki, który nie ma określonej łapówki (`lapowka IS NULL`).

```sql
SELECT imie_wroga "WROG", gatunek "GATUNEK", stopien_wrogosci "STOPIEN WROGOSCI"
FROM Wrogowie
WHERE lapowka IS NULL
ORDER BY stopien_wrogosci;
```

```
    WROG    | GATUNEK | STOPIEN WROGOSCI
------------+---------+------------------
 SMUKLA     | SOSNA   |                1
 DZIKI BILL | PIES    |               10
```

### Zad. 4 – dane o kocurach w jednej kolumnie

Operator `||` niejawnie zamienia liczbę i datę na tekst.

```sql
SELECT imie || ' zwany ' || pseudo || ' (fun. ' || funkcja || ') lowi myszki w bandzie '
       || nr_bandy || ' od ' || w_stadku_od "WSZYSTKO O KOCURACH"
FROM Kocury
WHERE plec = 'M'
ORDER BY w_stadku_od DESC, pseudo;
```

```
                            WSZYSTKO O KOCURACH
----------------------------------------------------------------------------
 DUDEK zwany MALY (fun. KOT) lowi myszki w bandzie 4 od 2011-05-15
 LUCEK zwany ZERO (fun. KOT) lowi myszki w bandzie 3 od 2010-03-01
 BARI zwany RURA (fun. LAPACZ) lowi myszki w bandzie 2 od 2009-09-01
 JACEK zwany PLACEK (fun. LOWCZY) lowi myszki w bandzie 2 od 2008-12-01
 KSAWERY zwany MAN (fun. LAPACZ) lowi myszki w bandzie 4 od 2008-07-12
 PUCEK zwany RAFA (fun. LOWCZY) lowi myszki w bandzie 4 od 2006-10-15
 BOLEK zwany LYSY (fun. BANDZIOR) lowi myszki w bandzie 2 od 2006-08-15
 KOREK zwany ZOMBI (fun. BANDZIOR) lowi myszki w bandzie 3 od 2004-03-16
 CHYTRY zwany BOLEK (fun. DZIELCZY) lowi myszki w bandzie 1 od 2002-05-05
 MRUCZEK zwany TYGRYS (fun. SZEFUNIO) lowi myszki w bandzie 1 od 2002-01-01
```

### Zad. 5 – pierwsze wystąpienie liter A i L

`STRPOS` zwraca pozycję pierwszego wystąpienia litery. Do zamiany użyłem `REGEXP_REPLACE`, który bez flagi `g` zamienia tylko **pierwsze** dopasowanie. Zwykły `REPLACE` zamieniłby wszystkie wystąpienia, np. `LOLA` dałoby `%O%#` zamiast `%OL#`.

```sql
SELECT pseudo "PSEUDO", STRPOS(pseudo, 'A') "A", STRPOS(pseudo, 'L') "L",
       REGEXP_REPLACE(REGEXP_REPLACE(pseudo, 'A', '#'), 'L', '%') "Po wymianie A na # oraz L na %"
FROM Kocury
WHERE pseudo LIKE '%A%' AND pseudo LIKE '%L%'
ORDER BY pseudo;
```

```
 PSEUDO | A | L | Po wymianie A na # oraz L na %
--------+---+---+--------------------------------
 LASKA  | 2 | 1 | %#SKA
 LOLA   | 4 | 1 | %OL#
 MALA   | 2 | 3 | M#%A
 MALY   | 2 | 3 | M#%Y
 PLACEK | 3 | 2 | P%#CEK
```

### Zad. 6 – koty z co najmniej 15-letnim stażem

- staż ≥ 15 lat ⇔ `AGE('2024-07-17', w_stadku_od) >= INTERVAL '15 years'` (data bieżąca: 2024-07-17),
- przystąpienie między 1 marca a 30 września ⇔ miesiąc przystąpienia od 3 do 9,
- obecny przydział jest o 10% wyższy od początkowego, więc początkowy = `ROUND(przydzial_myszy / 1.1)`,
- podwyżka po pół roku członkostwa = `w_stadku_od + INTERVAL '6 months'`; wynik jest typu `TIMESTAMP`, więc rzutuję go na `DATE`.

```sql
SELECT imie "IMIE", w_stadku_od "W stadku", ROUND(przydzial_myszy / 1.1) "Zjadal",
       (w_stadku_od + INTERVAL '6 months')::DATE "Podwyzka", przydzial_myszy "Zjada"
FROM Kocury
WHERE AGE('2024-07-17', w_stadku_od) >= INTERVAL '15 years'
  AND EXTRACT(MONTH FROM w_stadku_od) BETWEEN 3 AND 9
ORDER BY przydzial_myszy DESC;
```

```
  IMIE   |  W stadku  | Zjadal |  Podwyzka  | Zjada
---------+------------+--------+------------+-------
 KOREK   | 2004-03-16 |     68 | 2004-09-16 |    75
 BOLEK   | 2006-08-15 |     65 | 2007-02-15 |    72
 ZUZIA   | 2006-07-21 |     59 | 2007-01-21 |    65
 KSAWERY | 2008-07-12 |     46 | 2009-01-12 |    51
 CHYTRY  | 2002-05-05 |     45 | 2002-11-05 |    50
 RUDA    | 2006-09-17 |     20 | 2007-03-17 |    22
```

### Zad. 7 – kwartalne przydziały

`COALESCE` zamienia brak przydziału dodatkowego na 0. Bez tego warunek `przydzial_myszy > 2 * myszy_extra` miałby dla takich kotów wartość `UNKNOWN` i odpadłyby one z wyniku.

```sql
SELECT imie "IMIE", 3 * przydzial_myszy "MYSZY KWARTALNIE", 3 * COALESCE(myszy_extra, 0) "KWARTALNE DODATKI"
FROM Kocury
WHERE przydzial_myszy > 2 * COALESCE(myszy_extra, 0) AND przydzial_myszy >= 55
ORDER BY przydzial_myszy DESC, imie;
```

```
  IMIE   | MYSZY KWARTALNIE | KWARTALNE DODATKI
---------+------------------+-------------------
 MRUCZEK |              309 |                99
 KOREK   |              225 |                39
 BOLEK   |              216 |                63
 JACEK   |              201 |                 0
 PUCEK   |              195 |                 0
 ZUZIA   |              195 |                 0
 PUNIA   |              183 |                 0
 BARI    |              168 |                 0
```

### Zad. 8 – roczne spożycie myszy (PostgreSQL + Oracle)

Roczne spożycie to `12 * (przydzial_myszy + myszy_extra)`. Zamiast operatorów zbiorowych użyłem `SIGN(spożycie - 660)`, które daje 1, 0 lub −1. Dzięki temu jedno wyrażenie rozróżnia trzy przypadki: prosty `CASE` w PostgreSQL (który nie ma `DECODE`) albo `DECODE` w Oracle. Liczbę trzeba jawnie zamienić na tekst, bo wszystkie gałęzie muszą mieć ten sam typ.

PostgreSQL:

```sql
SELECT imie "IMIE",
       CASE SIGN(12 * (przydzial_myszy + COALESCE(myszy_extra, 0)) - 660)
           WHEN 1 THEN (12 * (przydzial_myszy + COALESCE(myszy_extra, 0)))::TEXT
           WHEN 0 THEN 'Limit'
           ELSE 'Ponizej 660'
       END "Zjada rocznie"
FROM Kocury
ORDER BY imie;
```

Oracle:

```sql
SELECT imie "IMIE",
       DECODE(SIGN(12 * (przydzial_myszy + NVL(myszy_extra, 0)) - 660),
              1, TO_CHAR(12 * (przydzial_myszy + NVL(myszy_extra, 0))),
              0, 'Limit',
              'Ponizej 660') "Zjada rocznie"
FROM Kocury
ORDER BY imie;
```

Wynik (identyczny w obu SZBD):

```
  IMIE   | Zjada rocznie
---------+---------------
 BARI    | 672
 BELA    | Ponizej 660
 BOLEK   | 1116
 CHYTRY  | Ponizej 660
 DUDEK   | Ponizej 660
 JACEK   | 804
 KOREK   | 1056
 KSAWERY | Ponizej 660
 LATKA   | Ponizej 660
 LUCEK   | Ponizej 660
 MELA    | Ponizej 660
 MICKA   | 864
 MRUCZEK | 1632
 PUCEK   | 780
 PUNIA   | 732
 RUDA    | 768
 SONIA   | Limit
 ZUZIA   | 780
```

### Zad. 9 – unikalność atrybutów PSEUDO i SZEF (PostgreSQL + Oracle)

Grupuję po atrybucie i liczę `COUNT(*)` w grupie: 1 oznacza wartość unikalną, więcej – nieunikalną. `LPAD` wyrównuje pseudonimy do prawej jak w przykładzie. Dla `SZEF` pomijam `NULL` (Tygrys nie ma szefa). Zapytania w obu SZBD różnią się tylko `CASE` ↔ `DECODE`.

PostgreSQL:

```sql
SELECT LPAD(pseudo, 10) || ' - ' || CASE COUNT(*) WHEN 1 THEN 'Unikalny' ELSE 'nieunikalny' END "Unikalnosc atr. PSEUDO"
FROM Kocury
GROUP BY pseudo
ORDER BY pseudo;

SELECT LPAD(szef, 10) || ' - ' || CASE COUNT(*) WHEN 1 THEN 'Unikalny' ELSE 'nieunikalny' END "Unikalnosc atr. SZEF"
FROM Kocury
WHERE szef IS NOT NULL
GROUP BY szef
ORDER BY szef;
```

Oracle:

```sql
SELECT LPAD(pseudo, 10) || ' - ' || DECODE(COUNT(*), 1, 'Unikalny', 'nieunikalny') "Unikalnosc atr. PSEUDO"
FROM Kocury
GROUP BY pseudo
ORDER BY pseudo;

SELECT LPAD(szef, 10) || ' - ' || DECODE(COUNT(*), 1, 'Unikalny', 'nieunikalny') "Unikalnosc atr. SZEF"
FROM Kocury
WHERE szef IS NOT NULL
GROUP BY szef
ORDER BY szef;
```

Wynik (identyczny w obu SZBD):

```
 Unikalnosc atr. PSEUDO
------------------------
      BOLEK - Unikalny
       DAMA - Unikalny
      KURKA - Unikalny
      LASKA - Unikalny
       LOLA - Unikalny
       LYSY - Unikalny
       MALA - Unikalny
       MALY - Unikalny
        MAN - Unikalny
     PLACEK - Unikalny
   PUSZYSTA - Unikalny
       RAFA - Unikalny
       RURA - Unikalny
     SZYBKA - Unikalny
     TYGRYS - Unikalny
       UCHO - Unikalny
       ZERO - Unikalny
      ZOMBI - Unikalny

   Unikalnosc atr. SZEF
--------------------------
      KURKA - Unikalny
       LYSY - nieunikalny
       RAFA - nieunikalny
     TYGRYS - nieunikalny
      ZOMBI - nieunikalny
```

Zgodnie z oczekiwaniami `PSEUDO` (klucz główny) jest unikalny, a `SZEF` nie – jeden kot może być szefem wielu kotów.

### Zad. 10 – koty z co najmniej dwoma wrogami (PostgreSQL + Oracle)

Zapytanie identyczne w obu SZBD.

```sql
SELECT pseudo "Pseudonim", COUNT(*) "Liczba wrogow"
FROM Wrogowie_kocurow
GROUP BY pseudo
HAVING COUNT(*) >= 2
ORDER BY pseudo;
```

```
 Pseudonim | Liczba wrogow
-----------+---------------
 BOLEK     |             2
 LASKA     |             2
 TYGRYS    |             2
```

---

## Część 2

### Zad. 11 (dodatkowe) – data pierwszej wypłaty po przerwie

Wypłata przypada w ostatnią środę miesiąca *bieżący + n*. Przyjmuję `n = 1`, jeżeli:

- kot przystąpił do stada po 15. dniu miesiąca, **albo**
- ostatnia środa bieżącego miesiąca już minęła (tak jest 31.10.2024 – środa 30.10 jest wcześniej, więc wszystkie koty dostają myszy 27.11),

w przeciwnym razie `n = 0`.

PostgreSQL nie ma funkcji `LAST_DAY` ani `NEXT_DAY`, więc:

- ostatni dzień miesiąca daje `DATE_TRUNC('month', d) + INTERVAL '1 month - 1 day'`,
- ostatnią środę nie późniejszą od danego dnia daje `DATE_BIN('7 days', dzień, '2000-01-05')`. `DATE_BIN` zaokrągla w dół do wielokrotności 7 dni, licząc od 2000-01-05, czyli od środy.

```sql
SELECT pseudo "PSEUDO", w_stadku_od "W STADKU",
       DATE_BIN('7 days', DATE_TRUNC('month', DATE '2024-10-29'
           + CASE WHEN EXTRACT(DAY FROM w_stadku_od) > 15
                    OR DATE_BIN('7 days', DATE_TRUNC('month', DATE '2024-10-29') + INTERVAL '1 month - 1 day', '2000-01-05') < '2024-10-29'
                  THEN INTERVAL '1 month' ELSE INTERVAL '0' END)
           + INTERVAL '1 month - 1 day', '2000-01-05')::DATE "WYPLATA"
FROM Kocury
ORDER BY w_stadku_od;
```

Dla 31 października wystarczy podmienić datę `2024-10-29` na `2024-10-31` (oba warianty są w skrypcie).

```
29 pazdziernik                          31 pazdziernik
  PSEUDO  |  W STADKU  |  WYPLATA         PSEUDO  |  W STADKU  |  WYPLATA
----------+------------+------------    ----------+------------+------------
 TYGRYS   | 2002-01-01 | 2024-10-30      TYGRYS   | 2002-01-01 | 2024-11-27
 BOLEK    | 2002-05-05 | 2024-10-30      BOLEK    | 2002-05-05 | 2024-11-27
 ZOMBI    | 2004-03-16 | 2024-11-27      ZOMBI    | 2004-03-16 | 2024-11-27
 SZYBKA   | 2006-07-21 | 2024-11-27      SZYBKA   | 2006-07-21 | 2024-11-27
 LYSY     | 2006-08-15 | 2024-10-30      LYSY     | 2006-08-15 | 2024-11-27
 MALA     | 2006-09-17 | 2024-11-27      MALA     | 2006-09-17 | 2024-11-27
 RAFA     | 2006-10-15 | 2024-10-30      RAFA     | 2006-10-15 | 2024-11-27
 KURKA    | 2008-01-01 | 2024-10-30      KURKA    | 2008-01-01 | 2024-11-27
 LASKA    | 2008-02-01 | 2024-10-30      LASKA    | 2008-02-01 | 2024-11-27
 MAN      | 2008-07-12 | 2024-10-30      MAN      | 2008-07-12 | 2024-11-27
 DAMA     | 2008-11-01 | 2024-10-30      DAMA     | 2008-11-01 | 2024-11-27
 PLACEK   | 2008-12-01 | 2024-10-30      PLACEK   | 2008-12-01 | 2024-11-27
 RURA     | 2009-09-01 | 2024-10-30      RURA     | 2009-09-01 | 2024-11-27
 LOLA     | 2009-10-14 | 2024-10-30      LOLA     | 2009-10-14 | 2024-11-27
 ZERO     | 2010-03-01 | 2024-10-30      ZERO     | 2010-03-01 | 2024-11-27
 PUSZYSTA | 2010-11-18 | 2024-11-27      PUSZYSTA | 2010-11-18 | 2024-11-27
 UCHO     | 2011-01-01 | 2024-10-30      UCHO     | 2011-01-01 | 2024-11-27
 MALY     | 2011-05-15 | 2024-10-30      MALY     | 2011-05-15 | 2024-11-27
```

### Zad. 12 – maksymalny całkowity przydział w grupach funkcyjnych kotek

`WHERE` odrzuca kocury i Szefunia jeszcze przed grupowaniem, a `HAVING` odrzuca grupy o średnim całkowitym przydziale ≤ 50. Wynik jest rozbity na kolumny bez nagłówków (alias `" "`), tak jak w przykładzie.

```sql
SELECT 'Liczba kotow=' " ", COUNT(*) " ", 'lowi jako' " ", funkcja " ", 'i zjada max.' " ",
       TO_CHAR(MAX(przydzial_myszy + COALESCE(myszy_extra, 0)), '99.99') " ", 'myszy miesiecznie' " "
FROM Kocury
WHERE plec = 'D' AND funkcja <> 'SZEFUNIO'
GROUP BY funkcja
HAVING AVG(przydzial_myszy + COALESCE(myszy_extra, 0)) > 50
ORDER BY funkcja;
```

```
---------------+---+-----------+---------+--------------+--------+-------------------
 Liczba kotow= | 1 | lowi jako | LAPACZ  | i zjada max. |  51.00 | myszy miesiecznie
 Liczba kotow= | 2 | lowi jako | LOWCZY  | i zjada max. |  65.00 | myszy miesiecznie
 Liczba kotow= | 4 | lowi jako | MILUSIA | i zjada max. |  72.00 | myszy miesiecznie
```

### Zad. 13 – minimalny przydział w bandach z podziałem na płcie

Grupowanie po dwóch kolumnach naraz. W przykładzie wynik nie jest uporządkowany, więc nie dodawałem `ORDER BY`. Kolejność wierszy wynika z grupowania haszowego, a zbiór wierszy jest taki sam jak w przykładzie.

```sql
SELECT nr_bandy "Nr bandy", plec "Plec", MIN(przydzial_myszy) "Minimalny przydzial"
FROM Kocury
GROUP BY nr_bandy, plec;
```

```
 Nr bandy | Plec | Minimalny przydzial
----------+------+---------------------
        1 | M    |                  50
        1 | D    |                  22
        4 | D    |                  40
        4 | M    |                  40
        3 | D    |                  20
        2 | M    |                  56
        2 | D    |                  24
        3 | M    |                  43
```

---

## Podsumowanie

Wszystkie 13 zadań rozwiązałem w PostgreSQL, a zadania 8–10 dodatkowo w Oracle. Każde zadanie to pojedyncze zapytanie `SELECT` na jednej tabeli, bez podzapytań, złączeń i operatorów zbiorowych. Wyniki są zgodne z przykładami z treści listy. W zadaniach 1 i 13 zgadza się zbiór wierszy, bo kolejność nie była wymagana.

Najważniejsze wnioski:

- **Wartości `NULL`** trzeba obsługiwać jawnie (`COALESCE` / `NVL`). W zadaniach 7, 8 i 12 brak `myszy_extra` dałby `NULL` w całym wyrażeniu arytmetycznym, a warunki miałyby wartość `UNKNOWN`.
- **Funkcje na datach** (`EXTRACT`, `AGE`, `INTERVAL`, `DATE_TRUNC`, `DATE_BIN`) wystarczają do dość złożonej logiki kalendarzowej (zad. 11) bez podzapytań, mimo że PostgreSQL nie ma oracle'owych `LAST_DAY` i `NEXT_DAY`.
- **Wybór wariantów bez `UNION`** – `SIGN` w połączeniu z `CASE` / `DECODE` obsługuje w jednym wyrażeniu przypadki „większe / równe / mniejsze” (zad. 8).
- **Grupowanie** z `COUNT(*)` i `HAVING` daje prosty sposób na sprawdzanie unikalności (zad. 9) i filtrowanie grup (zad. 10, 12).
- **Różnice między PostgreSQL a Oracle** w zadaniach 8–10 okazały się niewielkie: PostgreSQL nie ma `DECODE` (zastępuje go `CASE`), zamiast `NVL` używa standardowego `COALESCE`, a konwersję na tekst można zapisać operatorem `::TEXT`. `LPAD`, `SIGN`, `COUNT` i `HAVING` działają tak samo.
