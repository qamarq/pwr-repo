# Notatki dokumentacja – PostgreSQL (18)

## Spis treści

- [1. DDL](#1-ddl)
  - [1.1 CREATE TABLE](#11-create-table)
  - [1.2 Typy danych](#12-typy-danych)
  - [1.3 PRIMARY KEY](#13-primary-key)
  - [1.4 NOT NULL / UNIQUE](#14-not-null--unique)
  - [1.5 CHECK](#15-check)
  - [1.6 REFERENCES](#16-references)
  - [1.7 DEFAULT](#17-default)
  - [1.8 ALTER TABLE ADD CONSTRAINT](#18-alter-table-add-constraint)
  - [1.9 DEFERRABLE](#19-deferrable)
  - [1.10 ALTER TABLE – kolumny](#110-alter-table--kolumny)
  - [1.11 DROP TABLE](#111-drop-table)
  - [1.12 IDENTITY](#112-identity)
- [2. DML](#2-dml)
  - [2.1 INSERT … VALUES](#21-insert--values)
  - [2.2 Literał DATE](#22-literał-date)
  - [2.3 UPDATE](#23-update)
  - [2.4 UPDATE … FROM](#24-update--from)
  - [2.5 DELETE](#25-delete)
  - [2.6 RETURNING](#26-returning)
  - [2.7 BEGIN / COMMIT / ROLLBACK](#27-begin--commit--rollback)
- [3. Podstawy SELECT](#3-podstawy-select)
  - [3.1 SELECT … FROM](#31-select--from)
  - [3.2 Alias kolumny](#32-alias-kolumny)
  - [3.3 Alias tabeli](#33-alias-tabeli)
  - [3.4 DISTINCT](#34-distinct)
  - [3.5 DISTINCT ON](#35-distinct-on)
  - [3.6 WHERE](#36-where)
  - [3.7 BETWEEN](#37-between)
  - [3.8 IN](#38-in)
  - [3.9 LIKE / ILIKE](#39-like--ilike)
  - [3.10 IS NULL](#310-is-null)
  - [3.11 ORDER BY](#311-order-by)
  - [3.12 LIMIT / OFFSET](#312-limit--offset)
  - [3.13 FETCH FIRST](#313-fetch-first)
  - [3.14 SELECT bez FROM](#314-select-bez-from)
- [4. Napisy](#4-napisy)
  - [4.1 ||](#41-)
  - [4.2 CONCAT](#42-concat)
  - [4.3 CONCAT_WS](#43-concat_ws)
  - [4.4 LOWER / UPPER / INITCAP](#44-lower--upper--initcap)
  - [4.5 LENGTH](#45-length)
  - [4.6 POSITION / STRPOS](#46-position--strpos)
  - [4.7 SUBSTRING](#47-substring)
  - [4.8 LEFT / RIGHT](#48-left--right)
  - [4.9 LPAD / RPAD](#49-lpad--rpad)
  - [4.10 REPEAT](#410-repeat)
  - [4.11 REPLACE](#411-replace)
  - [4.12 OVERLAY](#412-overlay)
  - [4.13 REGEXP_REPLACE](#413-regexp_replace)
  - [4.14 TRIM](#414-trim)
  - [4.15 Pusty napis](#415-pusty-napis)
- [5. Obsługa NULL](#5-obsługa-null)
  - [5.1 COALESCE](#51-coalesce)
  - [5.2 NULLIF](#52-nullif)
  - [5.3 Porównywanie z NULL](#53-porównywanie-z-null)
  - [5.4 IS DISTINCT FROM](#54-is-distinct-from)
- [6. Liczby](#6-liczby)
  - [6.1 ROUND](#61-round)
  - [6.2 TRUNC](#62-trunc)
  - [6.3 MOD / %](#63-mod--)
  - [6.4 CEIL / FLOOR](#64-ceil--floor)
  - [6.5 Dzielenie całkowite](#65-dzielenie-całkowite)
- [7. Daty](#7-daty)
  - [7.1 CURRENT_DATE / NOW()](#71-current_date--now)
  - [7.2 DATE_TRUNC](#72-date_trunc)
  - [7.3 EXTRACT](#73-extract)
  - [7.4 Arytmetyka dat](#74-arytmetyka-dat)
  - [7.5 INTERVAL](#75-interval)
  - [7.6 AGE](#76-age)
  - [7.7 Ostatni dzień miesiąca](#77-ostatni-dzień-miesiąca)
  - [7.8 Ostatnia środa miesiąca](#78-ostatnia-środa-miesiąca)
  - [7.9 MAKE_DATE](#79-make_date)
- [8. Konwersje](#8-konwersje)
  - [8.1 ::](#81-)
  - [8.2 CAST](#82-cast)
  - [8.3 TO_CHAR(date, format)](#83-to_chardate-format)
  - [8.4 TO_CHAR(number, format)](#84-to_charnumber-format)
  - [8.5 TO_DATE](#85-to_date)
- [9. Warunki](#9-warunki)
  - [9.1 CASE WHEN](#91-case-when)
  - [9.2 CASE wartość](#92-case-wartość)
  - [9.3 GREATEST / LEAST](#93-greatest--least)
- [10. Grupowanie](#10-grupowanie)
  - [10.1 COUNT](#101-count)
  - [10.2 SUM / AVG / MIN / MAX](#102-sum--avg--min--max)
  - [10.3 GROUP BY](#103-group-by)
  - [10.4 HAVING](#104-having)
  - [10.5 Warunkowy COUNT](#105-warunkowy-count)
  - [10.6 FILTER](#106-filter)
  - [10.7 STRING_AGG](#107-string_agg)
  - [10.8 GROUP BY po aliasie / pozycji](#108-group-by-po-aliasie--pozycji)
  - [10.9 ROLLUP](#109-rollup)
- [11. Złączenia](#11-złączenia)
  - [11.1 JOIN](#111-join)
  - [11.2 LEFT JOIN](#112-left-join)
  - [11.3 RIGHT JOIN](#113-right-join)
  - [11.4 FULL JOIN](#114-full-join)
  - [11.5 CROSS JOIN](#115-cross-join)
  - [11.6 USING / NATURAL](#116-using--natural)
  - [11.7 Samozłączenie](#117-samozłączenie)
  - [11.8 Łańcuch złączeń](#118-łańcuch-złączeń)
  - [11.9 Złączenie z konkretnym wierszem](#119-złączenie-z-konkretnym-wierszem)
  - [11.10 Antyzłączenie](#1110-antyzłączenie)
  - [11.11 LATERAL](#1111-lateral)
  - [11.12 Pułapka LEFT JOIN + WHERE](#1112-pułapka-left-join--where)
- [12. Operatory zbiorowe](#12-operatory-zbiorowe)
  - [12.1 UNION / UNION ALL](#121-union--union-all)
  - [12.2 INTERSECT](#122-intersect)
  - [12.3 EXCEPT](#123-except)
  - [12.4 ORDER BY w operacjach zbiorowych](#124-order-by-w-operacjach-zbiorowych)
- [13. Podzapytania](#13-podzapytania)
  - [13.1 Podzapytanie skalarne](#131-podzapytanie-skalarne)
  - [13.2 Podzapytanie w SELECT / HAVING](#132-podzapytanie-w-select--having)
  - [13.3 IN (podzapytanie)](#133-in-podzapytanie)
  - [13.4 Pułapka NOT IN + NULL](#134-pułapka-not-in--null)
  - [13.5 EXISTS / NOT EXISTS](#135-exists--not-exists)
  - [13.6 ALL](#136-all)
  - [13.7 ANY](#137-any)
  - [13.8 Podzapytanie w FROM](#138-podzapytanie-w-from)
  - [13.9 WITH (CTE)](#139-with-cte)
- [14. Zapytania hierarchiczne](#14-zapytania-hierarchiczne)
  - [14.1 WITH RECURSIVE](#141-with-recursive)
  - [14.2 Poziom](#142-poziom)
  - [14.3 W górę drzewa](#143-w-górę-drzewa)
  - [14.4 Ścieżka i korzeń](#144-ścieżka-i-korzeń)
  - [14.5 Sprawdzanie liścia](#145-sprawdzanie-liścia)
  - [14.6 SEARCH DEPTH FIRST](#146-search-depth-first)
  - [14.7 generate_series](#147-generate_series)
- [15. Funkcje okna (OVER)](#15-funkcje-okna-over)
  - [15.1 OVER ()](#151-over-)
  - [15.2 PARTITION BY](#152-partition-by)
  - [15.3 ROW_NUMBER](#153-row_number)
  - [15.4 RANK / DENSE_RANK](#154-rank--dense_rank)
  - [15.5 Suma narastająca](#155-suma-narastająca)
  - [15.6 ROWS BETWEEN](#156-rows-between)
  - [15.7 LAG / LEAD](#157-lag--lead)
  - [15.8 FIRST_VALUE](#158-first_value)
  - [15.9 Klauzula WINDOW](#159-klauzula-window)
  - [15.10 Top-N w grupie](#1510-top-n-w-grupie)
- [16. Pivot](#16-pivot)
  - [16.1 Pivot przez CASE](#161-pivot-przez-case)
  - [16.2 Pivot przez FILTER](#162-pivot-przez-filter)
  - [16.3 crosstab](#163-crosstab)
  - [16.4 Unpivot](#164-unpivot)
- [17. psql](#17-psql)
  - [17.1 \dt](#171-dt)
  - [17.2 \d](#172-d)
  - [17.3 \i](#173-i)
  - [17.4 \x](#174-x)
  - [17.5 \q](#175-q)
- [18. Combosy](#18-combosy)
  - [18.1 Drzewo ze źródłem z SELECT-a](#181-drzewo-ze-źródłem-z-select-a)
  - [18.2 Drzewo jako źródło + JOIN](#182-drzewo-jako-źródło--join)
  - [18.3 Drzewo + GROUP BY (wszyscy podwładni)](#183-drzewo--group-by-wszyscy-podwładni)
  - [18.4 Drzewo z korzeniami z podzapytania](#184-drzewo-z-korzeniami-z-podzapytania)
  - [18.5 Pogrupowany SELECT jako źródło + JOIN](#185-pogrupowany-select-jako-źródło--join)
- [19. Kartkówka – przykładowe zadania](#19-kartkówka--przykładowe-zadania)
  - [19.1 Zadanie 1](#191-zadanie-1)
  - [19.2 Zadanie 2](#192-zadanie-2)
  - [19.3 Zadanie 3](#193-zadanie-3)
  - [19.4 Zadanie 4](#194-zadanie-4)
  - [19.5 Zadanie 5](#195-zadanie-5)
- [20. Kartkówka – zadania z poprzednich lat](#20-kartkówka--zadania-z-poprzednich-lat)
  - [20.1 Pytanie 1 – unikalny wynik walki w funkcji](#201-pytanie-1--unikalny-wynik-walki-w-funkcji)
  - [20.2 Pytanie 2 – statystyki incydentów](#202-pytanie-2--statystyki-incydentów)
  - [20.3 Pytanie 3 – agresja wg gatunków i lat](#203-pytanie-3--agresja-wg-gatunków-i-lat)
  - [20.4 Pytanie 4 – ścieżka władzy i budżet rodziny](#204-pytanie-4--ścieżka-władzy-i-budżet-rodziny)

## 1. DDL

### 1.1 CREATE TABLE

Tworzy tabelę; `IF NOT EXISTS` pomija ją, gdy już istnieje.

```sql
CREATE TABLE IF NOT EXISTS wrogowie (imie_wroga VARCHAR(15) PRIMARY KEY, gatunek VARCHAR(15));
```

### 1.2 Typy danych

`NUMERIC(p)`, `VARCHAR(n)`, `TEXT`, `INTEGER`, `BOOLEAN`, `DATE` (bez czasu) i `TIMESTAMP`.

```sql
pseudo VARCHAR(15), przydzial_myszy NUMERIC(3), w_stadku_od DATE
```

### 1.3 PRIMARY KEY

Unikalny, niepusty identyfikator wiersza; klucz złożony podaje się na poziomie tabeli.

```sql
PRIMARY KEY (pseudo, imie_wroga)
```

### 1.4 NOT NULL / UNIQUE

`UNIQUE` dopuszcza wiele `NULL`-i (`UNIQUE NULLS NOT DISTINCT` tego zabrania).

```sql
nazwa VARCHAR(20) NOT NULL, teren VARCHAR(15) UNIQUE
```

### 1.5 CHECK

CHECK kolumny może korzystać też z innych kolumn.

```sql
max_myszy NUMERIC(3) CHECK (max_myszy >= min_myszy) CHECK (max_myszy < 200)
```

### 1.6 REFERENCES

Klucz obcy, może wskazywać na tę samą tabelę.

```sql
szef VARCHAR(15) REFERENCES kocury(pseudo)
```

### 1.7 DEFAULT

Wartość użyta, gdy kolumna zostanie pominięta w INSERT.

```sql
w_stadku_od DATE DEFAULT CURRENT_DATE
```

### 1.8 ALTER TABLE ADD CONSTRAINT

Dodaje ograniczenie później, np. żeby rozbić cykl `bandy` ↔ `kocury`.

```sql
ALTER TABLE bandy ADD CONSTRAINT bandy_szef_fk FOREIGN KEY (szef_bandy) REFERENCES kocury(pseudo);
```

### 1.9 DEFERRABLE

Sprawdza ograniczenie przy COMMIT zamiast po każdej instrukcji.

```sql
FOREIGN KEY (szef_bandy) REFERENCES kocury(pseudo) DEFERRABLE INITIALLY DEFERRED
```

### 1.10 ALTER TABLE – kolumny

Dodaje, zmienia, zmienia nazwę albo usuwa kolumnę.

```sql
ALTER TABLE kocury ADD COLUMN ulubiona VARCHAR(20);
ALTER TABLE kocury ALTER COLUMN ulubiona TYPE VARCHAR(30);
ALTER TABLE kocury RENAME COLUMN ulubiona TO przysmak;
ALTER TABLE kocury DROP COLUMN przysmak;
```

### 1.11 DROP TABLE

Usuwa tabelę; `CASCADE` usuwa też wskazujące na nią klucze obce.

```sql
DROP TABLE IF EXISTS kocury CASCADE;
```

### 1.12 IDENTITY

Kolumna auto-inkrementowana (starszy sposób: `SERIAL`).

```sql
CREATE TABLE incydenty (id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY, opis VARCHAR(50));
```

## 2. DML

### 2.1 INSERT … VALUES

Wstawia jeden lub więcej wierszy.

```sql
INSERT INTO funkcje (funkcja, min_myszy, max_myszy) VALUES ('SZEFUNIO', 90, 110), ('BANDZIOR', 70, 90);
```

### 2.2 Literał DATE

Stała daty w formacie ISO; zwykły napis `'2008-12-01'` też jest rzutowany automatycznie.

```sql
INSERT INTO kocury (imie, pseudo, w_stadku_od) VALUES ('JACEK', 'PLACEK', DATE '2008-12-01');
```

### 2.3 UPDATE

Zmienia wartości w pasujących wierszach.

```sql
UPDATE bandy SET szef_bandy = 'TYGRYS' WHERE nr_bandy = 1;
```

### 2.4 UPDATE … FROM

Aktualizacja wartościami z innej tabeli.

```sql
UPDATE kocury K SET nr_bandy = B.nr_bandy FROM bandy B WHERE B.szef_bandy = K.szef;
```

### 2.5 DELETE

Usuwa pasujące wiersze.

```sql
DELETE FROM wrogowie_kocurow WHERE imie_wroga = 'KAZIO';
```

### 2.6 RETURNING

Zwraca wiersze zmienione przez INSERT / UPDATE / DELETE.

```sql
UPDATE kocury SET przydzial_myszy = przydzial_myszy + 1 WHERE pseudo = 'ZERO' RETURNING pseudo, przydzial_myszy;
```

### 2.7 BEGIN / COMMIT / ROLLBACK

psql zatwierdza każdą instrukcję automatycznie; `BEGIN` zaczyna transakcję, którą można wycofać.

```sql
BEGIN; DELETE FROM wrogowie_kocurow; ROLLBACK;
```

## 3. Podstawy SELECT

### 3.1 SELECT … FROM

Wybiera kolumny z tabeli.

```sql
SELECT imie, funkcja FROM kocury;
```

### 3.2 Alias kolumny

Nazwy bez cudzysłowów zmieniają się na małe litery; podwójne cudzysłowy zachowują spacje i wielkość liter.

```sql
SELECT przydzial_myszy AS "Zjada" FROM kocury;
```

### 3.3 Alias tabeli

`AS` jest opcjonalne.

```sql
SELECT K.imie FROM kocury AS K;
```

### 3.4 DISTINCT

Usuwa powtarzające się wiersze.

```sql
SELECT DISTINCT funkcja FROM kocury;
```

### 3.5 DISTINCT ON

Zostawia pierwszy wiersz z każdej grupy, wg `ORDER BY` (tylko Postgres).

```sql
SELECT DISTINCT ON (nr_bandy) nr_bandy, pseudo FROM kocury ORDER BY nr_bandy, przydzial_myszy DESC;
```

### 3.6 WHERE

Filtruje wiersze.

```sql
SELECT imie FROM kocury WHERE plec = 'D' AND przydzial_myszy >= 55;
```

### 3.7 BETWEEN

Sprawdzenie zakresu, oba końce włącznie.

```sql
SELECT imie FROM kocury WHERE w_stadku_od BETWEEN '2005-09-01' AND '2007-07-31';
```

### 3.8 IN

Pasuje do dowolnej wartości z listy.

```sql
SELECT imie FROM kocury WHERE funkcja IN ('MILUSIA', 'KOT');
```

### 3.9 LIKE / ILIKE

Dopasowanie wzorca (`%` dowolny tekst, `_` jeden znak); `ILIKE` ignoruje wielkość liter.

```sql
SELECT pseudo FROM kocury WHERE pseudo ILIKE '%a%' AND pseudo ILIKE '%l%';
```

### 3.10 IS NULL

Jedyny poprawny sposób sprawdzenia `NULL`.

```sql
SELECT imie_wroga FROM wrogowie WHERE lapowka IS NULL;
```

### 3.11 ORDER BY

Sortuje wynik; `NULL`-e idą na koniec przy `ASC`, chyba że `NULLS FIRST`.

```sql
SELECT pseudo, myszy_extra FROM kocury ORDER BY myszy_extra DESC NULLS LAST, pseudo;
```

### 3.12 LIMIT / OFFSET

Pierwsze N wierszy po sortowaniu, opcjonalnie z pominięciem kilku.

```sql
SELECT pseudo FROM kocury ORDER BY przydzial_myszy DESC LIMIT 3 OFFSET 1;
```

### 3.13 FETCH FIRST

Wersja `LIMIT` ze standardu SQL.

```sql
SELECT pseudo FROM kocury ORDER BY przydzial_myszy DESC FETCH FIRST 3 ROWS ONLY;
```

### 3.14 SELECT bez FROM

Nie potrzeba sztucznej tabeli.

```sql
SELECT CURRENT_DATE;
```

## 4. Napisy

### 4.1 ||

Łączenie napisów; dowolny `NULL` daje `NULL` w całym wyniku.

```sql
SELECT imie || ' zwany ' || pseudo FROM kocury;
```

### 4.2 CONCAT

Łączy dowolną liczbę argumentów i pomija `NULL`-e.

```sql
SELECT CONCAT(imie, ' zwany ', pseudo, ' od ', w_stadku_od) FROM kocury;
```

### 4.3 CONCAT_WS

Łączy wartości z separatorem między nimi.

```sql
SELECT CONCAT_WS(', ', imie, pseudo, funkcja) FROM kocury;
```

### 4.4 LOWER / UPPER / INITCAP

Zmienia wielkość liter.

```sql
SELECT LOWER(pseudo), INITCAP(imie) FROM kocury;
```

### 4.5 LENGTH

Liczba znaków.

```sql
SELECT pseudo FROM kocury WHERE LENGTH(pseudo) > 4;
```

### 4.6 POSITION / STRPOS

Pozycja podnapisu (od 1), 0 gdy nie znaleziono.

```sql
SELECT pseudo, POSITION('a' IN LOWER(pseudo)), STRPOS(pseudo, 'L') FROM kocury;
```

### 4.7 SUBSTRING

Podnapis od pozycji, z opcjonalną długością (`SUBSTR` też działa).

```sql
SELECT SUBSTRING(pseudo FROM 2 FOR 3), SUBSTR(pseudo, 2, 3) FROM kocury;
```

### 4.8 LEFT / RIGHT

Pierwsze / ostatnie n znaków.

```sql
SELECT LEFT(pseudo, 2), RIGHT(pseudo, 2) FROM kocury;
```

### 4.9 LPAD / RPAD

Dopełnia tekst do długości; przydatne do wcięć w drzewie.

```sql
SELECT LPAD(pseudo, 10, '*') FROM kocury;
```

### 4.10 REPEAT

Powtarza napis n razy.

```sql
SELECT REPEAT(' ', 4) || pseudo FROM kocury;
```

### 4.11 REPLACE

Zamienia wszystkie wystąpienia.

```sql
SELECT REPLACE(pseudo, 'A', '#') FROM kocury;
```

### 4.12 OVERLAY

Zamienia znaki na podanej pozycji.

```sql
SELECT OVERLAY(pseudo PLACING '#' FROM POSITION('A' IN pseudo) FOR 1) FROM kocury WHERE pseudo LIKE '%A%';
```

### 4.13 REGEXP_REPLACE

Zamiana regexem; bez flagi `g` tylko 1. dopasowanie, `i` ignoruje wielkość liter.

```sql
SELECT REGEXP_REPLACE(pseudo, 'a', '#', 'i') FROM kocury;
```

### 4.14 TRIM

Usuwa spacje z obu stron.

```sql
SELECT TRIM('  TYGRYS  ');
```

### 4.15 Pusty napis

`''` to zwykła wartość, nie `NULL`.

```sql
SELECT '' IS NULL;   -- fałsz
```

## 5. Obsługa NULL

### 5.1 COALESCE

Pierwszy argument różny od `NULL`.

```sql
SELECT przydzial_myszy + COALESCE(myszy_extra, 0) FROM kocury;
```

### 5.2 NULLIF

Zwraca `NULL`, gdy oba argumenty są równe.

```sql
SELECT NULLIF(przydzial_myszy, 0) FROM kocury;
```

### 5.3 Porównywanie z NULL

`= NULL` nigdy nie jest prawdą; funkcje agregujące pomijają `NULL`-e.

```sql
SELECT imie FROM kocury WHERE myszy_extra = NULL;   -- zawsze 0 wierszy
```

### 5.4 IS DISTINCT FROM

„Różne” bezpieczne dla `NULL` (`NULL` vs wartość liczy się jako różne).

```sql
SELECT imie FROM kocury WHERE myszy_extra IS DISTINCT FROM 0;
```

## 6. Liczby

### 6.1 ROUND

Zaokrągla, opcjonalnie do n miejsc po przecinku.

```sql
SELECT ROUND(przydzial_myszy / 1.1), ROUND(przydzial_myszy / 1.1, 1) FROM kocury;
```

### 6.2 TRUNC

Obcina część ułamkową bez zaokrąglania.

```sql
SELECT TRUNC(przydzial_myszy / 7) FROM kocury;
```

### 6.3 MOD / %

Reszta z dzielenia.

```sql
SELECT pseudo FROM kocury WHERE przydzial_myszy % 2 = 0;
```

### 6.4 CEIL / FLOOR

Zaokrągla w górę / w dół do liczby całkowitej.

```sql
SELECT CEIL(przydzial_myszy / 7), FLOOR(przydzial_myszy / 7) FROM kocury;
```

### 6.5 Dzielenie całkowite

Liczba całkowita / całkowita obcina część ułamkową: `5 / 2 = 2` (kolumny `NUMERIC` są OK).

```sql
SELECT 5 / 2, 5.0 / 2, 5::numeric / 2;
```

## 7. Daty

### 7.1 CURRENT_DATE / NOW()

Dzisiejsza data albo bieżący znacznik czasu.

```sql
SELECT CURRENT_DATE, NOW(), CURRENT_TIMESTAMP;
```

### 7.2 DATE_TRUNC

Początek miesiąca/roku/…; zwraca timestamp.

```sql
SELECT DATE_TRUNC('month', CURRENT_DATE)::date;
```

### 7.3 EXTRACT

Wyciąga część daty jako liczbę; `DOW` to 0 = niedziela … 6 = sobota.

```sql
SELECT imie_wroga FROM wrogowie_kocurow WHERE EXTRACT(YEAR FROM data_incydentu) = 2009;
```

### 7.4 Arytmetyka dat

`data + n` dodaje dni; data minus data daje liczbę dni (integer).

```sql
SELECT pseudo, CURRENT_DATE - w_stadku_od, w_stadku_od + 7 FROM kocury;
```

### 7.5 INTERVAL

Dodaje miesiące/lata; wynik to timestamp, więc rzutuj z powrotem przez `::date`.

```sql
SELECT (w_stadku_od + INTERVAL '6 months')::date FROM kocury;
```

### 7.6 AGE

Odstęp między dwiema datami w latach/miesiącach/dniach.

```sql
SELECT pseudo FROM kocury WHERE AGE(CURRENT_DATE, w_stadku_od) > INTERVAL '15 years';
```

### 7.7 Ostatni dzień miesiąca

Początek następnego miesiąca minus jeden dzień.

```sql
SELECT (DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month - 1 day')::date;
```

### 7.8 Ostatnia środa miesiąca

Ostatni dzień minus `(DOW + 4) % 7` dni.

```sql
SELECT ostatni - (EXTRACT(DOW FROM ostatni)::int + 4) % 7 FROM (SELECT (DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month - 1 day')::date AS ostatni) AS dni;
```

### 7.9 MAKE_DATE

Tworzy datę z liczb.

```sql
SELECT MAKE_DATE(2009, 1, 7);
```

## 8. Konwersje

### 8.1 ::

Skrót rzutowania w Postgresie.

```sql
SELECT przydzial_myszy::text, '42'::int, '2009-01-07'::date FROM kocury;
```

### 8.2 CAST

Standardowa konwersja typu.

```sql
SELECT CAST(przydzial_myszy AS VARCHAR(10)) FROM kocury;
```

### 8.3 TO_CHAR(date, format)

Formatuje datę; `Month` jest dopełniany spacjami, `FM` usuwa dopełnienie.

```sql
SELECT TO_CHAR(w_stadku_od, 'YYYY-MM-DD'), TO_CHAR(w_stadku_od, 'FMMonth') FROM kocury;
```

### 8.4 TO_CHAR(number, format)

Liczba na tekst wymaga formatu (`::text` jest prostsze).

```sql
SELECT TO_CHAR(przydzial_myszy, 'FM999') FROM kocury;
```

### 8.5 TO_DATE

Tekst na datę według formatu.

```sql
SELECT TO_DATE('07-01-2009', 'DD-MM-YYYY');
```

## 9. Warunki

### 9.1 CASE WHEN

Warunki sprawdzane po kolei; wszystkie gałęzie muszą zwracać ten sam typ.

```sql
SELECT CASE WHEN 12 * przydzial_myszy > 660 THEN (12 * przydzial_myszy)::text ELSE 'Ponizej 660' END FROM kocury;
```

### 9.2 CASE wartość

Porównuje jedno wyrażenie z wartościami.

```sql
SELECT CASE plec WHEN 'M' THEN 'Kocur' ELSE 'Kotka' END FROM kocury;
```

### 9.3 GREATEST / LEAST

Największy / najmniejszy z argumentów.

```sql
SELECT pseudo, GREATEST(przydzial_myszy, COALESCE(myszy_extra, 0)) FROM kocury;
```

## 10. Grupowanie

### 10.1 COUNT

`COUNT(*)` liczy wiersze, `COUNT(kol)` wartości różne od `NULL`, `COUNT(DISTINCT kol)` unikalne wartości.

```sql
SELECT COUNT(*), COUNT(myszy_extra), COUNT(DISTINCT nr_bandy) FROM kocury;
```

### 10.2 SUM / AVG / MIN / MAX

Funkcje agregujące na grupie wierszy.

```sql
SELECT SUM(przydzial_myszy), AVG(przydzial_myszy), MIN(w_stadku_od), MAX(przydzial_myszy) FROM kocury;
```

### 10.3 GROUP BY

Jeden wiersz na grupę; grupowanie po kluczu głównym pozwala wybrać pozostałe kolumny tej tabeli.

```sql
SELECT B.nazwa, COUNT(*) FROM bandy B JOIN kocury K ON K.nr_bandy = B.nr_bandy GROUP BY B.nr_bandy;
```

### 10.4 HAVING

Filtruje grupy (`WHERE` filtruje wiersze przed grupowaniem).

```sql
SELECT pseudo FROM wrogowie_kocurow GROUP BY pseudo HAVING COUNT(*) >= 2;
```

### 10.5 Warunkowy COUNT

`COUNT(CASE …)` liczy tylko wiersze spełniające warunek.

```sql
SELECT pseudo FROM wrogowie_kocurow WK JOIN wrogowie W ON W.imie_wroga = WK.imie_wroga GROUP BY pseudo HAVING COUNT(CASE WHEN W.gatunek = 'PIES' THEN 1 END) > 0;
```

### 10.6 FILTER

Krótszy warunkowy agregat.

```sql
SELECT nr_bandy, COUNT(*) FILTER (WHERE plec = 'D') AS kotki FROM kocury GROUP BY nr_bandy;
```

### 10.7 STRING_AGG

Łączy wartości z grupy w jeden napis.

```sql
SELECT nr_bandy, STRING_AGG(pseudo, ', ' ORDER BY pseudo) FROM kocury GROUP BY nr_bandy;
```

### 10.8 GROUP BY po aliasie / pozycji

Postgres pozwala grupować po aliasie kolumny wynikowej albo jej numerze.

```sql
SELECT EXTRACT(MONTH FROM w_stadku_od) AS miesiac, COUNT(*) FROM kocury GROUP BY miesiac ORDER BY 1;
```

### 10.9 ROLLUP

Dodaje wiersz z sumą (`NULL` w grupowanej kolumnie).

```sql
SELECT nr_bandy, SUM(przydzial_myszy) FROM kocury GROUP BY ROLLUP (nr_bandy);
```

## 11. Złączenia

### 11.1 JOIN

Tylko wiersze z dopasowaniem po obu stronach.

```sql
SELECT K.pseudo, B.nazwa FROM kocury K JOIN bandy B ON B.nr_bandy = K.nr_bandy;
```

### 11.2 LEFT JOIN

Wszystkie wiersze z lewej strony, `NULL`-e tam, gdzie nic nie pasuje.

```sql
SELECT B.nazwa, K.pseudo FROM bandy B LEFT JOIN kocury K ON K.nr_bandy = B.nr_bandy;
```

### 11.3 RIGHT JOIN

Wszystkie wiersze z prawej strony.

```sql
SELECT K.pseudo, B.nazwa FROM kocury K RIGHT JOIN bandy B ON B.nr_bandy = K.nr_bandy;
```

### 11.4 FULL JOIN

Wszystkie wiersze z obu stron.

```sql
SELECT S.pseudo, P.pseudo FROM kocury S FULL JOIN kocury P ON P.szef = S.pseudo;
```

### 11.5 CROSS JOIN

Każdy wiersz z każdym wierszem.

```sql
SELECT K.pseudo, F.funkcja FROM kocury K CROSS JOIN funkcje F;
```

### 11.6 USING / NATURAL

Złączenie po kolumnach o tej samej nazwie.

```sql
SELECT pseudo, nazwa FROM kocury JOIN bandy USING (nr_bandy);
```

### 11.7 Samozłączenie

Złączenie tabeli z samą sobą (kot i jego szef).

```sql
SELECT K.imie, S.imie AS szef FROM kocury K LEFT JOIN kocury S ON S.pseudo = K.szef;
```

### 11.8 Łańcuch złączeń

Każde kolejne samozłączenie to poziom wyżej.

```sql
SELECT K.imie, S1.imie, S2.imie FROM kocury K LEFT JOIN kocury S1 ON S1.pseudo = K.szef LEFT JOIN kocury S2 ON S2.pseudo = S1.szef;
```

### 11.9 Złączenie z konkretnym wierszem

Dołącza jeden konkretny wiersz, żeby porównać z nim każdy wiersz.

```sql
SELECT K.imie FROM kocury K JOIN kocury J ON J.imie = 'JACEK' WHERE K.w_stadku_od < J.w_stadku_od;
```

### 11.10 Antyzłączenie

`LEFT JOIN` + `IS NULL` znajduje wiersze bez dopasowania.

```sql
SELECT B.nazwa FROM bandy B LEFT JOIN kocury K ON K.nr_bandy = B.nr_bandy WHERE K.pseudo IS NULL;
```

### 11.11 LATERAL

Podzapytanie, które może używać kolumn tabel stojących przed nim.

```sql
SELECT K.pseudo, O.imie_wroga FROM kocury K LEFT JOIN LATERAL (SELECT imie_wroga FROM wrogowie_kocurow WK WHERE WK.pseudo = K.pseudo ORDER BY data_incydentu DESC LIMIT 1) O ON TRUE;
```

### 11.12 Pułapka LEFT JOIN + WHERE

Warunek w `WHERE` na prawej tabeli zamienia złączenie w wewnętrzne; daj go do `ON`.

```sql
SELECT B.nazwa, K.pseudo FROM bandy B LEFT JOIN kocury K ON K.nr_bandy = B.nr_bandy AND K.plec = 'M';
```

## 12. Operatory zbiorowe

### 12.1 UNION / UNION ALL

Skleja wyniki; `UNION` usuwa duplikaty, `UNION ALL` nie.

```sql
SELECT pseudo FROM kocury WHERE plec = 'M' UNION ALL SELECT pseudo FROM kocury WHERE przydzial_myszy > 60;
```

### 12.2 INTERSECT

Wiersze obecne w obu wynikach.

```sql
SELECT pseudo FROM kocury WHERE plec = 'M' INTERSECT SELECT pseudo FROM wrogowie_kocurow;
```

### 12.3 EXCEPT

Wiersze z pierwszego wyniku, których nie ma w drugim (`MINUS` w Oracle).

```sql
SELECT nr_bandy FROM bandy EXCEPT SELECT nr_bandy FROM kocury;
```

### 12.4 ORDER BY w operacjach zbiorowych

Piszemy raz na końcu, używając nazw z pierwszego `SELECT`.

```sql
SELECT pseudo, 'Prominent' AS typ FROM kocury UNION ALL SELECT pseudo, 'Szarak' FROM kocury ORDER BY typ, pseudo;
```

## 13. Podzapytania

### 13.1 Podzapytanie skalarne

Zwraca jedną wartość, można jej używać jak stałej.

```sql
SELECT pseudo FROM kocury WHERE przydzial_myszy > (SELECT AVG(przydzial_myszy) FROM kocury);
```

### 13.2 Podzapytanie w SELECT / HAVING

Ta sama wartość powtórzona w każdym wierszu albo porównywana dla każdej grupy.

```sql
SELECT nr_bandy, AVG(przydzial_myszy) FROM kocury GROUP BY nr_bandy HAVING AVG(przydzial_myszy) > (SELECT AVG(przydzial_myszy) FROM kocury);
```

### 13.3 IN (podzapytanie)

Pasuje do dowolnej wartości zwróconej przez podzapytanie.

```sql
SELECT imie FROM kocury WHERE nr_bandy IN (SELECT nr_bandy FROM bandy WHERE teren IN ('SAD', 'POLE'));
```

### 13.4 Pułapka NOT IN + NULL

Jeden `NULL` w podzapytaniu daje pusty wynik.

```sql
SELECT pseudo FROM kocury WHERE pseudo NOT IN (SELECT szef FROM kocury WHERE szef IS NOT NULL);
```

### 13.5 EXISTS / NOT EXISTS

Sprawdza, czy podzapytanie skorelowane zwraca jakikolwiek wiersz; odporne na `NULL`-e.

```sql
SELECT K.pseudo FROM kocury K WHERE NOT EXISTS (SELECT 1 FROM kocury P WHERE P.szef = K.pseudo);
```

### 13.6 ALL

Prawda, jeśli warunek zachodzi dla każdej wartości (pusty zbiór → prawda).

```sql
SELECT imie FROM kocury WHERE przydzial_myszy >= ALL (SELECT 3 * przydzial_myszy FROM kocury WHERE funkcja = 'MILUSIA');
```

### 13.7 ANY

Prawda, jeśli warunek zachodzi dla co najmniej jednej wartości (pusty zbiór → fałsz).

```sql
SELECT imie FROM kocury WHERE przydzial_myszy > ANY (SELECT przydzial_myszy FROM kocury WHERE funkcja = 'KOT');
```

### 13.8 Podzapytanie w FROM

Używa wyniku zapytania jak tabeli (alias opcjonalny od v16).

```sql
SELECT * FROM (SELECT nr_bandy, SUM(przydzial_myszy) AS suma FROM kocury GROUP BY nr_bandy) AS sumy WHERE suma > 200;
```

### 13.9 WITH (CTE)

Nazywa podzapytanie, żeby można było go użyć ponownie.

```sql
WITH sumy AS (SELECT nr_bandy, SUM(przydzial_myszy) AS suma FROM kocury GROUP BY nr_bandy) SELECT * FROM sumy WHERE suma > 200;
```

## 14. Zapytania hierarchiczne

### 14.1 WITH RECURSIVE

Zapytanie o drzewo: wiersze startowe `UNION ALL` krok, który łączy się z powrotem z CTE (nie ma `CONNECT BY`).

```sql
WITH RECURSIVE drzewo AS (SELECT pseudo FROM kocury WHERE szef IS NULL UNION ALL SELECT K.pseudo FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SELECT * FROM drzewo;
```

### 14.2 Poziom

Przenoś kolumnę-licznik, zwiększaną w każdym kroku.

```sql
WITH RECURSIVE drzewo AS (SELECT pseudo, 1 AS poziom FROM kocury WHERE funkcja = 'BANDZIOR' UNION ALL SELECT K.pseudo, D.poziom + 1 FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SELECT REPEAT(' ', 4 * (poziom - 1)) || pseudo FROM drzewo;
```

### 14.3 W górę drzewa

Odwróć kierunek złączenia, żeby iść od kota do jego szefów.

```sql
WITH RECURSIVE szefowie AS (SELECT pseudo, szef FROM kocury WHERE pseudo = 'ZERO' UNION ALL SELECT K.pseudo, K.szef FROM kocury K JOIN szefowie S ON K.pseudo = S.szef) SELECT pseudo FROM szefowie;
```

### 14.4 Ścieżka i korzeń

Przenoś wartość z korzenia i ścieżkę tekstową (rzutuj na `text`, żeby typy się zgadzały).

```sql
WITH RECURSIVE sciezki AS (SELECT imie AS korzen, szef, imie::text AS sciezka FROM kocury WHERE funkcja = 'KOT' UNION ALL SELECT S.korzen, K.szef, S.sciezka || ' | ' || K.imie FROM sciezki S JOIN kocury K ON K.pseudo = S.szef) SELECT korzen, sciezka FROM sciezki WHERE szef IS NULL;
```

### 14.5 Sprawdzanie liścia

Wiersz jest liściem, gdy nikt na niego nie wskazuje.

```sql
SELECT K.pseudo FROM kocury K WHERE NOT EXISTS (SELECT 1 FROM kocury P WHERE P.szef = K.pseudo);
```

### 14.6 SEARCH DEPTH FIRST

Porządkuje wynik jak drzewo (odpowiednik `ORDER SIBLINGS BY` z Oracle).

```sql
WITH RECURSIVE drzewo AS (SELECT pseudo FROM kocury WHERE szef IS NULL UNION ALL SELECT K.pseudo FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SEARCH DEPTH FIRST BY pseudo SET kolejnosc SELECT pseudo FROM drzewo ORDER BY kolejnosc;
```

### 14.7 generate_series

Generuje liczby (albo daty) 1..n.

```sql
SELECT generate_series(1, 12);
```

## 15. Funkcje okna (OVER)

### 15.1 OVER ()

Agregat po wszystkich wierszach bez zwijania ich.

```sql
SELECT pseudo, przydzial_myszy, SUM(przydzial_myszy) OVER () FROM kocury;
```

### 15.2 PARTITION BY

Agregat dla każdej grupy, widoczny w każdym wierszu.

```sql
SELECT pseudo, ROUND(100.0 * przydzial_myszy / SUM(przydzial_myszy) OVER (PARTITION BY nr_bandy)) AS proc FROM kocury;
```

### 15.3 ROW_NUMBER

Numeruje wiersze 1, 2, 3… bez remisów.

```sql
SELECT pseudo, ROW_NUMBER() OVER (PARTITION BY nr_bandy ORDER BY przydzial_myszy DESC) FROM kocury;
```

### 15.4 RANK / DENSE_RANK

Rankingi z remisami: `RANK` 1,2,2,4, a `DENSE_RANK` 1,2,2,3.

```sql
SELECT pseudo, RANK() OVER (ORDER BY przydzial_myszy DESC), DENSE_RANK() OVER (ORDER BY przydzial_myszy DESC) FROM kocury;
```

### 15.5 Suma narastająca

`ORDER BY` wewnątrz `OVER` robi z agregatu sumę narastającą.

```sql
SELECT pseudo, SUM(przydzial_myszy) OVER (ORDER BY w_stadku_od) FROM kocury;
```

### 15.6 ROWS BETWEEN

Określa dokładnie, które wiersze obejmuje okno.

```sql
SELECT pseudo, AVG(przydzial_myszy) OVER (ORDER BY w_stadku_od ROWS BETWEEN 1 PRECEDING AND CURRENT ROW) FROM kocury;
```

### 15.7 LAG / LEAD

Wartość z poprzedniego / następnego wiersza.

```sql
SELECT pseudo, LAG(pseudo) OVER (ORDER BY w_stadku_od), LEAD(pseudo) OVER (ORDER BY w_stadku_od) FROM kocury;
```

### 15.8 FIRST_VALUE

Pierwsza wartość w oknie.

```sql
SELECT pseudo, FIRST_VALUE(pseudo) OVER (PARTITION BY nr_bandy ORDER BY w_stadku_od) FROM kocury;
```

### 15.9 Klauzula WINDOW

Nazywa okno raz i używa go wielokrotnie.

```sql
SELECT pseudo, RANK() OVER w, SUM(przydzial_myszy) OVER w FROM kocury WINDOW w AS (PARTITION BY nr_bandy ORDER BY przydzial_myszy DESC);
```

### 15.10 Top-N w grupie

Funkcji okna nie można używać w `WHERE`, więc filtruj w zapytaniu zewnętrznym.

```sql
SELECT * FROM (SELECT pseudo, nr_bandy, RANK() OVER (PARTITION BY nr_bandy ORDER BY przydzial_myszy DESC) AS rnk FROM kocury) AS ranking WHERE rnk = 1;
```

## 16. Pivot

### 16.1 Pivot przez CASE

Nie ma słowa `PIVOT`; jeden warunkowy agregat na kolumnę.

```sql
SELECT nr_bandy, SUM(CASE WHEN plec = 'M' THEN przydzial_myszy ELSE 0 END) AS samce, SUM(CASE WHEN plec = 'D' THEN przydzial_myszy ELSE 0 END) AS samice FROM kocury GROUP BY nr_bandy;
```

### 16.2 Pivot przez FILTER

Ten sam pivot, krócej.

```sql
SELECT nr_bandy, SUM(przydzial_myszy) FILTER (WHERE plec = 'M') AS samce, SUM(przydzial_myszy) FILTER (WHERE plec = 'D') AS samice FROM kocury GROUP BY nr_bandy;
```

### 16.3 crosstab

Prawdziwy pivot z rozszerzenia `tablefunc`; kolumny wyniku trzeba zadeklarować.

```sql
CREATE EXTENSION IF NOT EXISTS tablefunc;
SELECT * FROM crosstab('SELECT nr_bandy, plec, SUM(przydzial_myszy) FROM kocury GROUP BY 1, 2 ORDER BY 1, 2', 'VALUES (''D''), (''M'')') AS wynik(nr_bandy NUMERIC, samice NUMERIC, samce NUMERIC);
```

### 16.4 Unpivot

Kolumny na wiersze przez `LATERAL (VALUES …)`.

```sql
SELECT K.pseudo, U.rodzaj, U.ile FROM kocury K CROSS JOIN LATERAL (VALUES ('PRZYDZIAL', K.przydzial_myszy), ('EXTRA', K.myszy_extra)) AS U(rodzaj, ile);
```

## 17. psql

### 17.1 \dt

Lista tabel.

```sql
\dt
```

### 17.2 \d

Pokazuje kolumny i ograniczenia tabeli.

```sql
\d kocury
```

### 17.3 \i

Uruchamia plik ze skryptem.

```sql
\i lista3/zad1.sql
```

### 17.4 \x

Przełącza wyświetlanie jedna-kolumna-na-linię dla szerokich wierszy.

```sql
\x
```

### 17.5 \q

Wychodzi z psql.

```sql
\q
```

## 18. Combosy

### 18.1 Drzewo ze źródłem z SELECT-a

Przefiltrowane wiersze wrzucasz do CTE i na nim puszczasz rekurencję; kot wycięty w źródle odcina też całą swoją gałąź (jak warunek w `CONNECT BY`).

```sql
WITH RECURSIVE zrodlo AS (
    SELECT pseudo, szef, przydzial_myszy + COALESCE(myszy_extra, 0) AS myszy
    FROM kocury
    WHERE plec = 'M'
), drzewo AS (
    SELECT pseudo, myszy, 1 AS poziom FROM zrodlo WHERE szef IS NULL
    UNION ALL
    SELECT z.pseudo, z.myszy, d.poziom + 1 FROM zrodlo z JOIN drzewo d ON z.szef = d.pseudo
)
SELECT REPEAT('    ', poziom - 1) || pseudo AS hierarchia, myszy
FROM drzewo;
```

### 18.2 Drzewo jako źródło + JOIN

Najpierw budujesz drzewo, potem traktujesz je jak zwykłą tabelę: złączenie, filtr po poziomie, sortowanie.

```sql
WITH RECURSIVE drzewo AS (
    SELECT pseudo, nr_bandy, 1 AS poziom FROM kocury WHERE szef IS NULL
    UNION ALL
    SELECT k.pseudo, k.nr_bandy, d.poziom + 1 FROM kocury k JOIN drzewo d ON k.szef = d.pseudo
)
SELECT d.poziom, d.pseudo, b.nazwa
FROM drzewo d
JOIN bandy b ON b.nr_bandy = d.nr_bandy
WHERE d.poziom >= 2
ORDER BY d.poziom, d.pseudo;
```

### 18.3 Drzewo + GROUP BY (wszyscy podwładni)

Start = każda para (szef, bezpośredni podwładny), krok przenosi szefa w dół; potem `GROUP BY` po przeniesionym korzeniu.

```sql
WITH RECURSIVE pod AS (
    SELECT szef AS korzen, pseudo FROM kocury WHERE szef IS NOT NULL
    UNION ALL
    SELECT p.korzen, k.pseudo FROM kocury k JOIN pod p ON k.szef = p.pseudo
)
SELECT p.korzen, COUNT(*) AS podwladni, SUM(k.przydzial_myszy) AS myszy_podwladnych
FROM pod p
JOIN kocury k ON k.pseudo = p.pseudo
GROUP BY p.korzen
ORDER BY podwladni DESC, p.korzen;
```

### 18.4 Drzewo z korzeniami z podzapytania

`WHERE` w części startowej może używać podzapytania, np. start od szefów band.

```sql
WITH RECURSIVE drzewo AS (
    SELECT pseudo, 1 AS poziom FROM kocury
    WHERE pseudo IN (SELECT szef_bandy FROM bandy WHERE teren = 'POLE')
    UNION ALL
    SELECT k.pseudo, d.poziom + 1 FROM kocury k JOIN drzewo d ON k.szef = d.pseudo
)
SELECT REPEAT('    ', poziom - 1) || pseudo AS hierarchia
FROM drzewo;
```

### 18.5 Pogrupowany SELECT jako źródło + JOIN

Agregujesz w podzapytaniu, dołączasz je z powrotem do wierszy i porównujesz każdy wiersz z jego grupą.

```sql
SELECT k.pseudo, k.przydzial_myszy, s.srednia
FROM kocury k
JOIN (SELECT nr_bandy, ROUND(AVG(przydzial_myszy), 1) AS srednia
      FROM kocury
      GROUP BY nr_bandy) s ON s.nr_bandy = k.nr_bandy
WHERE k.przydzial_myszy > s.srednia
ORDER BY k.nr_bandy, k.przydzial_myszy DESC;
```

## 19. Kartkówka – przykładowe zadania

### 19.1 Zadanie 1

Dla każdego poziomu hierarchii (zaczynając od TYGRYSA) wyświetl numer poziomu, liczbę kotów, średni przydział myszy i listę ich pseudonimów.

Podpowiedź: drzewo z kolumną poziomu, potem `GROUP BY poziom` na nim.

```sql
WITH RECURSIVE drzewo AS (
    SELECT pseudo, przydzial_myszy, 1 AS poziom FROM kocury WHERE pseudo = 'TYGRYS'
    UNION ALL
    SELECT k.pseudo, k.przydzial_myszy, d.poziom + 1 FROM kocury k JOIN drzewo d ON k.szef = d.pseudo
)
SELECT poziom, COUNT(*) AS ile, ROUND(AVG(przydzial_myszy), 1) AS srednio,
       STRING_AGG(pseudo, ', ' ORDER BY pseudo) AS koty
FROM drzewo
GROUP BY poziom
ORDER BY poziom;
```

Wynik:

```text
poziom | ile | srednio | koty
1      | 1   | 103.0   | TYGRYS
2      | 6   | 51.5    | BOLEK, LOLA, LYSY, MALA, RAFA, ZOMBI
3      | 10  | 47.5    | DAMA, KURKA, LASKA, MALY, MAN, PLACEK, PUSZYSTA, RURA, SZYBKA, UCHO
4      | 1   | 43.0    | ZERO
```

### 19.2 Zadanie 2

Dla kotów pełniących funkcję KOT wyświetl imię i liczbę szczebli dzielących je od głównego szefa.

Podpowiedź: idziesz w górę od kotów z funkcją KOT, przenosisz imię i licznik, zostawiasz wiersz, gdzie `szef IS NULL`.

```sql
WITH RECURSIVE w_gore AS (
    SELECT imie AS korzen, szef, 0 AS szczeble FROM kocury WHERE funkcja = 'KOT'
    UNION ALL
    SELECT w.korzen, k.szef, w.szczeble + 1 FROM w_gore w JOIN kocury k ON k.pseudo = w.szef
)
SELECT korzen AS kot, szczeble AS do_samej_gory
FROM w_gore
WHERE szef IS NULL
ORDER BY szczeble DESC, korzen;
```

Wynik:

```text
kot   | do_samej_gory
LUCEK | 3
DUDEK | 2
LATKA | 2
```

### 19.3 Zadanie 3

Wyświetl hierarchię (z wcięciami) tylko kotów z band działających na terenie POLE lub CALOSC, razem z nazwą bandy.

Podpowiedź: CTE-źródło robi filtr band, drzewo idzie po nim; sortowanie po ścieżce daje kolejność drzewa.

```sql
WITH RECURSIVE zrodlo AS (
    SELECT k.pseudo, k.szef, b.nazwa
    FROM kocury k JOIN bandy b ON b.nr_bandy = k.nr_bandy
    WHERE b.teren IN ('POLE', 'CALOSC')
), drzewo AS (
    SELECT pseudo, nazwa, 1 AS poziom, pseudo::text AS sciezka FROM zrodlo WHERE szef IS NULL
    UNION ALL
    SELECT z.pseudo, z.nazwa, d.poziom + 1, d.sciezka || '/' || z.pseudo FROM zrodlo z JOIN drzewo d ON z.szef = d.pseudo
)
SELECT REPEAT('    ', poziom - 1) || pseudo AS hierarchia, nazwa
FROM drzewo
ORDER BY sciezka;
```

Wynik:

```text
TYGRYS            SZEFOSTWO
    BOLEK         SZEFOSTWO
    LOLA          SZEFOSTWO
    LYSY          CZARNI RYCERZE
        LASKA     CZARNI RYCERZE
        PLACEK    CZARNI RYCERZE
        RURA      CZARNI RYCERZE
        SZYBKA    CZARNI RYCERZE
    MALA          SZEFOSTWO
```

### 19.4 Zadanie 4

Dla każdego kota, który jest czyimś szefem, podaj liczbę wszystkich jego podwładnych (bezpośrednich i pośrednich) i sumę ich przydziałów myszy.

Podpowiedź: to samo co 18.3.

```sql
WITH RECURSIVE pod AS (
    SELECT szef AS korzen, pseudo FROM kocury WHERE szef IS NOT NULL
    UNION ALL
    SELECT p.korzen, k.pseudo FROM kocury k JOIN pod p ON k.szef = p.pseudo
)
SELECT p.korzen, COUNT(*) AS podwladni, SUM(k.przydzial_myszy) AS myszy_podwladnych
FROM pod p
JOIN kocury k ON k.pseudo = p.pseudo
GROUP BY p.korzen
ORDER BY podwladni DESC, p.korzen;
```

Wynik:

```text
korzen | podwladni | myszy_podwladnych
TYGRYS | 17        | 827
LYSY   | 4         | 212
RAFA   | 4         | 182
ZOMBI  | 3         | 124
KURKA  | 1         | 43
```

### 19.5 Zadanie 5

Wyświetl koty, które dostają więcej myszy niż średnia w ich bandzie, razem z tą średnią.

Podpowiedź: to samo co 18.5; z podzapytaniem skorelowanym w `WHERE` też działa, ale wtedy nie pokażesz średniej.

```sql
SELECT k.pseudo, k.przydzial_myszy, s.srednia
FROM kocury k
JOIN (SELECT nr_bandy, ROUND(AVG(przydzial_myszy), 1) AS srednia
      FROM kocury
      GROUP BY nr_bandy) s ON s.nr_bandy = k.nr_bandy
WHERE k.przydzial_myszy > s.srednia
ORDER BY k.nr_bandy, k.przydzial_myszy DESC;
```

## 20. Kartkówka – zadania z poprzednich lat

Założenia: „poziom agresji” = `wrogowie.stopien_wrogosci` (skala 1–10 pasuje do progów z zadania 3). W zadaniu 1 tabela `wrogowie_kocurow` ma mieć kolumnę `wynik` (`'WROG'`, `'KOT'`, `'REMIS'`) – w naszej bazie jej nie ma, więc rozwiązanie testowałem na sztucznie dodanej kolumnie.

### 20.1 Pytanie 1 – unikalny wynik walki w funkcji

Wypisz imiona kotów oraz wynik walki (np. 'WROG', 'KOT', 'REMIS'), jeśli dany kot osiągnął taki wynik, a żaden inny kot pełniący tę samą funkcję nie uzyskał takiego samego wyniku w żadnym incydencie. Struktura wyniku: Imie, Wynik.

Podpowiedź: `NOT EXISTS` z podzapytaniem skorelowanym po funkcji i wyniku, z wykluczeniem samego siebie (`k2.pseudo <> k.pseudo`); `DISTINCT`, bo kot może mieć kilka incydentów z tym samym wynikiem.

```sql
SELECT DISTINCT k.imie, wk.wynik
FROM kocury k
JOIN wrogowie_kocurow wk ON wk.pseudo = k.pseudo
WHERE NOT EXISTS (SELECT 1
                  FROM kocury k2
                  JOIN wrogowie_kocurow wk2 ON wk2.pseudo = k2.pseudo
                  WHERE k2.funkcja = k.funkcja
                    AND k2.pseudo <> k.pseudo
                    AND wk2.wynik = wk.wynik)
ORDER BY k.imie;
```

### 20.2 Pytanie 2 – statystyki incydentów

Dla każdego incydentu wyświetl datę, pseudo, numer bandy kota. Dodatkowo wyznacz, ile innych incydentów miało miejsce w okresie +/- 60 dni od daty bieżącego incydentu (nie wliczając samego siebie). Określ, czy poprzedni incydent był bardziej agresywny, czy mniej – jeżeli nie było poprzedniego incydentu, załóż poziom agresji równy 0. Wyznacz liczbę incydentów od początku roku. Wyznacz numer incydentu (czyli który to jest kolejno incydent) w danej bandzie w danym roku.

Podpowiedź: wszystko funkcjami okna. ±60 dni = `RANGE BETWEEN INTERVAL '60 days' PRECEDING AND INTERVAL '60 days' FOLLOWING` minus 1 (sam incydent); poprzedni = `LAG(..., 1, 0)`; od początku roku = `COUNT(*)` narastająco z `PARTITION BY` rok; numer w bandzie = `ROW_NUMBER()` z `PARTITION BY` banda, rok.

```sql
SELECT wk.data_incydentu, wk.pseudo, k.nr_bandy,
       COUNT(*) OVER (ORDER BY wk.data_incydentu
                      RANGE BETWEEN INTERVAL '60 days' PRECEDING AND INTERVAL '60 days' FOLLOWING) - 1 AS incydenty_w_ok_60dni,
       CASE WHEN LAG(w.stopien_wrogosci, 1, 0) OVER (ORDER BY wk.data_incydentu, wk.pseudo) > w.stopien_wrogosci
            THEN 'TAK' ELSE 'NIE' END AS czy_poprzedni_bardziej_agresywny,
       COUNT(*) OVER (PARTITION BY EXTRACT(YEAR FROM wk.data_incydentu)
                      ORDER BY wk.data_incydentu, wk.pseudo
                      ROWS UNBOUNDED PRECEDING) AS incydentow_od_poczatku_roku,
       ROW_NUMBER() OVER (PARTITION BY k.nr_bandy, EXTRACT(YEAR FROM wk.data_incydentu)
                          ORDER BY wk.data_incydentu, wk.pseudo) AS numer_incydentu_w_bandzie_w_roku
FROM wrogowie_kocurow wk
JOIN kocury k ON k.pseudo = wk.pseudo
JOIN wrogowie w ON w.imie_wroga = wk.imie_wroga
ORDER BY wk.data_incydentu, wk.pseudo;
```

Wynik:

```text
data_incydentu |  pseudo  | nr_bandy | incydenty_w_ok_60dni | czy_poprzedni_bardziej_agresywny | incydentow_od_poczatku_roku | numer_incydentu_w_bandzie_w_roku
----------------+----------+----------+----------------------+----------------------------------+-----------------------------+----------------------------------
2004-10-13     | TYGRYS   |        1 |                    0 | NIE                              |                           1 |                                1
2005-03-07     | ZOMBI    |        3 |                    1 | TAK                              |                           1 |                                1
2005-03-29     | BOLEK    |        1 |                    1 | NIE                              |                           2 |                                1
2006-09-12     | SZYBKA   |        2 |                    0 | TAK                              |                           1 |                                1
2007-03-07     | MALA     |        1 |                    0 | NIE                              |                           1 |                                1
2007-06-12     | TYGRYS   |        1 |                    0 | NIE                              |                           2 |                                2
2007-11-10     | BOLEK    |        1 |                    0 | NIE                              |                           3 |                                3
2008-12-12     | LASKA    |        2 |                    2 | NIE                              |                           1 |                                1
2009-01-07     | LASKA    |        2 |                    2 | NIE                              |                           1 |                                1
2009-02-07     | DAMA     |        4 |                    2 | NIE                              |                           2 |                                1
2009-04-14     | MAN      |        4 |                    1 | TAK                              |                           3 |                                2
2009-05-11     | LYSY     |        2 |                    1 | TAK                              |                           4 |                                2
2009-09-03     | RURA     |        2 |                    0 | NIE                              |                           5 |                                3
2010-07-12     | PLACEK   |        2 |                    0 | TAK                              |                           1 |                                1
2010-11-19     | PUSZYSTA |        3 |                    1 | TAK                              |                           2 |                                1
2010-12-14     | KURKA    |        3 |                    1 | NIE                              |                           3 |                                2
2011-07-13     | MALY     |        4 |                    1 | NIE                              |                           1 |                                1
2011-07-14     | UCHO     |        4 |                    1 | NIE                              |                           2 |                                2
```

### 20.3 Pytanie 3 – agresja wg gatunków i lat

Przygotuj zestawienie liczby incydentów oraz średniego poziomu agresji dla różnych gatunków wrogów (chcemy wszystkie gatunki), jakie miały w latach 2009, 2010, 2011. Kategorie poziomu agresji: Niska [1-4], Srednia [5-8], Wysoka [9-10], przedstawione w kolumnach. Zamiast brakujących wartości zastosuj 0.

Podpowiedź: „wszystkie gatunki” = gatunki × lata przez `CROSS JOIN`, potem `LEFT JOIN` do incydentów (złączonych z wrogami w podzapytaniu, bo inaczej liczyłby się wróg bez incydentu). Pivot przez `COUNT/AVG(CASE …)`, `COALESCE(…, 0)` na brakujące średnie.

```sql
SELECT g.gatunek, r.rok,
       COUNT(CASE WHEN i.agresja BETWEEN 1 AND 4 THEN 1 END) AS niski_liczba,
       COUNT(CASE WHEN i.agresja BETWEEN 5 AND 8 THEN 1 END) AS sredni_liczba,
       COUNT(CASE WHEN i.agresja BETWEEN 9 AND 10 THEN 1 END) AS wysoki_liczba,
       COALESCE(ROUND(AVG(CASE WHEN i.agresja BETWEEN 1 AND 4 THEN i.agresja END), 2), 0) AS niski_srednia,
       COALESCE(ROUND(AVG(CASE WHEN i.agresja BETWEEN 5 AND 8 THEN i.agresja END), 2), 0) AS sredni_srednia,
       COALESCE(ROUND(AVG(CASE WHEN i.agresja BETWEEN 9 AND 10 THEN i.agresja END), 2), 0) AS wysoki_srednia
FROM (SELECT DISTINCT gatunek FROM wrogowie) g
CROSS JOIN (VALUES (2009), (2010), (2011)) AS r(rok)
LEFT JOIN (SELECT w.gatunek, w.stopien_wrogosci AS agresja, EXTRACT(YEAR FROM wk.data_incydentu) AS rok
           FROM wrogowie_kocurow wk
           JOIN wrogowie w ON w.imie_wroga = wk.imie_wroga) i ON i.gatunek = g.gatunek AND i.rok = r.rok
GROUP BY g.gatunek, r.rok
ORDER BY g.gatunek, r.rok;
```

Wynik:

```text
gatunek  | rok  | niski_liczba | sredni_liczba | wysoki_liczba | niski_srednia | sredni_srednia | wysoki_srednia
----------+------+--------------+---------------+---------------+---------------+----------------+----------------
CZLOWIEK | 2009 |            0 |             0 |             2 |             0 |              0 |          10.00
CZLOWIEK | 2010 |            0 |             0 |             0 |             0 |              0 |              0
CZLOWIEK | 2011 |            0 |             1 |             0 |             0 |           7.00 |              0
KOGUT    | 2009 |            0 |             0 |             0 |             0 |              0 |              0
KOGUT    | 2010 |            1 |             0 |             0 |          3.00 |              0 |              0
KOGUT    | 2011 |            0 |             0 |             0 |             0 |              0 |              0
LIS      | 2009 |            0 |             0 |             0 |             0 |              0 |              0
LIS      | 2010 |            0 |             0 |             0 |             0 |              0 |              0
LIS      | 2011 |            0 |             1 |             0 |             0 |           5.00 |              0
PIES     | 2009 |            2 |             0 |             1 |          1.50 |              0 |          10.00
PIES     | 2010 |            1 |             0 |             0 |          4.00 |              0 |              0
PIES     | 2011 |            0 |             0 |             0 |             0 |              0 |              0
SOSNA    | 2009 |            0 |             0 |             0 |             0 |              0 |              0
SOSNA    | 2010 |            1 |             0 |             0 |          1.00 |              0 |              0
SOSNA    | 2011 |            0 |             0 |             0 |             0 |              0 |              0
```

### 20.4 Pytanie 4 – ścieżka władzy i budżet rodziny

Dla każdego kota wyznacz „Ścieżkę Władzy” (ciąg pseudonimów od szefa głównego do danego kota). Dodatkowo oblicz „Całkowity Budżet Rodziny” – sumę przydziału myszy (wraz z ekstra) danego kota oraz wszystkich jego bezpośrednich i pośrednich podwładnych. Wyniki posortuj według budżetu malejąco.

Podpowiedź: dwa rekurencyjne CTE – jedno buduje ścieżkę od korzenia w dół, drugie daje pary (kot, on sam lub dowolny podwładny); potem złączenie i `SUM`.

```sql
WITH RECURSIVE sciezki AS (
    SELECT pseudo, imie, funkcja, pseudo::text AS sciezka
    FROM kocury
    WHERE szef IS NULL
    UNION ALL
    SELECT k.pseudo, k.imie, k.funkcja, s.sciezka || ' -> ' || k.pseudo
    FROM kocury k JOIN sciezki s ON k.szef = s.pseudo
), pod AS (
    SELECT pseudo AS korzen, pseudo FROM kocury
    UNION ALL
    SELECT p.korzen, k.pseudo FROM kocury k JOIN pod p ON k.szef = p.pseudo
)
SELECT s.imie, s.funkcja, s.sciezka,
       SUM(k.przydzial_myszy + COALESCE(k.myszy_extra, 0)) AS budzet_rodzinny
FROM sciezki s
JOIN pod p ON p.korzen = s.pseudo
JOIN kocury k ON k.pseudo = p.pseudo
GROUP BY s.pseudo, s.imie, s.funkcja, s.sciezka
ORDER BY budzet_rodzinny DESC, s.imie;
```

Wynik (zgadza się z przykładem z kartkówki):

```text
 imie   | funkcja  |             sciezka              | budzet_rodzinny
---------+----------+----------------------------------+-----------------
MRUCZEK | SZEFUNIO | TYGRYS                           |            1149
BOLEK   | BANDZIOR | TYGRYS -> LYSY                   |             333
KOREK   | BANDZIOR | TYGRYS -> ZOMBI                  |             247
PUCEK   | LOWCZY   | TYGRYS -> RAFA                   |             247
PUNIA   | LOWCZY   | TYGRYS -> ZOMBI -> KURKA         |             104
MICKA   | MILUSIA  | TYGRYS -> LOLA                   |              72
JACEK   | LOWCZY   | TYGRYS -> LYSY -> PLACEK         |              67
ZUZIA   | LOWCZY   | TYGRYS -> LYSY -> SZYBKA         |              65
RUDA    | MILUSIA  | TYGRYS -> MALA                   |              64
BARI    | LAPACZ   | TYGRYS -> LYSY -> RURA           |              56
SONIA   | MILUSIA  | TYGRYS -> ZOMBI -> PUSZYSTA      |              55
BELA    | MILUSIA  | TYGRYS -> LYSY -> LASKA          |              52
KSAWERY | LAPACZ   | TYGRYS -> RAFA -> MAN            |              51
MELA    | LAPACZ   | TYGRYS -> RAFA -> DAMA           |              51
CHYTRY  | DZIELCZY | TYGRYS -> BOLEK                  |              50
LUCEK   | KOT      | TYGRYS -> ZOMBI -> KURKA -> ZERO |              43
DUDEK   | KOT      | TYGRYS -> RAFA -> MALY           |              40
LATKA   | KOT      | TYGRYS -> RAFA -> UCHO           |              40
```
