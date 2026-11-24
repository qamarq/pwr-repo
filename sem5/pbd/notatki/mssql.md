# Notatki dokumentacja – SQL Server (2022, T-SQL)

## Spis treści

- [1. DDL](#1-ddl)
  - [1.1 CREATE TABLE](#11-create-table)
  - [1.2 Typy danych](#12-typy-danych)
  - [1.3 PRIMARY KEY](#13-primary-key)
  - [1.4 NOT NULL / UNIQUE](#14-not-null--unique)
  - [1.5 Filtrowany indeks unikalny](#15-filtrowany-indeks-unikalny)
  - [1.6 CHECK](#16-check)
  - [1.7 REFERENCES](#17-references)
  - [1.8 DEFAULT](#18-default)
  - [1.9 ALTER TABLE ADD CONSTRAINT](#19-alter-table-add-constraint)
  - [1.10 NOCHECK CONSTRAINT](#110-nocheck-constraint)
  - [1.11 ALTER TABLE – kolumny](#111-alter-table--kolumny)
  - [1.12 DROP TABLE](#112-drop-table)
  - [1.13 IDENTITY](#113-identity)
- [2. DML](#2-dml)
  - [2.1 INSERT … VALUES](#21-insert--values)
  - [2.2 Literał daty](#22-literał-daty)
  - [2.3 UPDATE](#23-update)
  - [2.4 UPDATE … FROM JOIN](#24-update--from-join)
  - [2.5 DELETE](#25-delete)
  - [2.6 OUTPUT](#26-output)
  - [2.7 BEGIN TRAN / COMMIT / ROLLBACK](#27-begin-tran--commit--rollback)
- [3. Podstawy SELECT](#3-podstawy-select)
  - [3.1 SELECT … FROM](#31-select--from)
  - [3.2 Alias kolumny](#32-alias-kolumny)
  - [3.3 Alias tabeli](#33-alias-tabeli)
  - [3.4 DISTINCT](#34-distinct)
  - [3.5 WHERE](#35-where)
  - [3.6 BETWEEN](#36-between)
  - [3.7 IN](#37-in)
  - [3.8 LIKE](#38-like)
  - [3.9 IS NULL](#39-is-null)
  - [3.10 ORDER BY](#310-order-by)
  - [3.11 TOP](#311-top)
  - [3.12 OFFSET … FETCH](#312-offset--fetch)
  - [3.13 SELECT bez FROM](#313-select-bez-from)
- [4. Napisy](#4-napisy)
  - [4.1 +](#41-)
  - [4.2 CONCAT](#42-concat)
  - [4.3 CONCAT_WS](#43-concat_ws)
  - [4.4 LOWER / UPPER](#44-lower--upper)
  - [4.5 LEN](#45-len)
  - [4.6 CHARINDEX](#46-charindex)
  - [4.7 SUBSTRING](#47-substring)
  - [4.8 LEFT / RIGHT](#48-left--right)
  - [4.9 REPLICATE](#49-replicate)
  - [4.10 REPLACE](#410-replace)
  - [4.11 STUFF](#411-stuff)
  - [4.12 TRIM / LTRIM / RTRIM](#412-trim--ltrim--rtrim)
  - [4.13 Pusty napis](#413-pusty-napis)
- [5. Obsługa NULL](#5-obsługa-null)
  - [5.1 ISNULL](#51-isnull)
  - [5.2 COALESCE](#52-coalesce)
  - [5.3 NULLIF](#53-nullif)
  - [5.4 Porównywanie z NULL](#54-porównywanie-z-null)
  - [5.5 IS DISTINCT FROM](#55-is-distinct-from)
- [6. Liczby](#6-liczby)
  - [6.1 ROUND](#61-round)
  - [6.2 Obcinanie](#62-obcinanie)
  - [6.3 %](#63-)
  - [6.4 CEILING / FLOOR](#64-ceiling--floor)
  - [6.5 Dzielenie całkowite](#65-dzielenie-całkowite)
- [7. Daty](#7-daty)
  - [7.1 GETDATE / SYSDATETIME](#71-getdate--sysdatetime)
  - [7.2 YEAR / MONTH / DAY](#72-year--month--day)
  - [7.3 DATEPART](#73-datepart)
  - [7.4 DATEADD](#74-dateadd)
  - [7.5 DATEDIFF](#75-datediff)
  - [7.6 EOMONTH](#76-eomonth)
  - [7.7 DATETRUNC](#77-datetrunc)
  - [7.8 DATEFROMPARTS](#78-datefromparts)
  - [7.9 DATENAME](#79-datename)
  - [7.10 Ostatnia środa miesiąca](#710-ostatnia-środa-miesiąca)
- [8. Konwersje](#8-konwersje)
  - [8.1 CAST](#81-cast)
  - [8.2 CONVERT](#82-convert)
  - [8.3 FORMAT](#83-format)
  - [8.4 TRY_CAST](#84-try_cast)
- [9. Warunki](#9-warunki)
  - [9.1 CASE WHEN](#91-case-when)
  - [9.2 CASE wartość](#92-case-wartość)
  - [9.3 IIF](#93-iif)
  - [9.4 GREATEST / LEAST](#94-greatest--least)
- [10. Grupowanie](#10-grupowanie)
  - [10.1 COUNT](#101-count)
  - [10.2 SUM / AVG / MIN / MAX](#102-sum--avg--min--max)
  - [10.3 GROUP BY](#103-group-by)
  - [10.4 HAVING](#104-having)
  - [10.5 Warunkowy COUNT](#105-warunkowy-count)
  - [10.6 STRING_AGG](#106-string_agg)
  - [10.7 Grupowanie po wyrażeniu](#107-grupowanie-po-wyrażeniu)
  - [10.8 ROLLUP](#108-rollup)
- [11. Złączenia](#11-złączenia)
  - [11.1 JOIN](#111-join)
  - [11.2 LEFT JOIN](#112-left-join)
  - [11.3 RIGHT JOIN](#113-right-join)
  - [11.4 FULL JOIN](#114-full-join)
  - [11.5 CROSS JOIN](#115-cross-join)
  - [11.6 Brak USING / NATURAL](#116-brak-using--natural)
  - [11.7 Samozłączenie](#117-samozłączenie)
  - [11.8 Łańcuch złączeń](#118-łańcuch-złączeń)
  - [11.9 Złączenie z konkretnym wierszem](#119-złączenie-z-konkretnym-wierszem)
  - [11.10 Antyzłączenie](#1110-antyzłączenie)
  - [11.11 CROSS / OUTER APPLY](#1111-cross--outer-apply)
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
  - [14.1 Rekurencyjne CTE](#141-rekurencyjne-cte)
  - [14.2 Poziom](#142-poziom)
  - [14.3 W górę drzewa](#143-w-górę-drzewa)
  - [14.4 Ścieżka i korzeń](#144-ścieżka-i-korzeń)
  - [14.5 Sprawdzanie liścia](#145-sprawdzanie-liścia)
  - [14.6 Kolejność drzewa](#146-kolejność-drzewa)
  - [14.7 MAXRECURSION](#147-maxrecursion)
  - [14.8 GENERATE_SERIES](#148-generate_series)
- [15. Funkcje okna (OVER)](#15-funkcje-okna-over)
  - [15.1 OVER ()](#151-over-)
  - [15.2 PARTITION BY](#152-partition-by)
  - [15.3 ROW_NUMBER](#153-row_number)
  - [15.4 RANK / DENSE_RANK](#154-rank--dense_rank)
  - [15.5 Suma narastająca](#155-suma-narastająca)
  - [15.6 ROWS BETWEEN](#156-rows-between)
  - [15.7 LAG / LEAD](#157-lag--lead)
  - [15.8 FIRST_VALUE](#158-first_value)
  - [15.9 Top-N w grupie](#159-top-n-w-grupie)
- [16. Pivot](#16-pivot)
  - [16.1 PIVOT](#161-pivot)
  - [16.2 UNPIVOT](#162-unpivot)
  - [16.3 Unpivot przez APPLY](#163-unpivot-przez-apply)
  - [16.4 Pivot przez CASE](#164-pivot-przez-case)
- [17. sqlcmd](#17-sqlcmd)
  - [17.1 GO](#171-go)
  - [17.2 :r](#172-r)
  - [17.3 Flaga -I](#173-flaga--i)
  - [17.4 sp_help](#174-sp_help)
  - [17.5 sys.tables](#175-systables)
  - [17.6 EXIT](#176-exit)
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

Tworzy tabelę; nie ma `CREATE TABLE IF NOT EXISTS`.

```sql
CREATE TABLE wrogowie (imie_wroga VARCHAR(15) PRIMARY KEY, gatunek VARCHAR(15));
```

### 1.2 Typy danych

`NUMERIC(p)`, `VARCHAR(n)`, `NVARCHAR(n)` (Unicode), `INT`, `BIT`, `DATE` i `DATETIME2`.

```sql
pseudo VARCHAR(15), przydzial_myszy NUMERIC(3), w_stadku_od DATE
```

### 1.3 PRIMARY KEY

Unikalny, niepusty identyfikator wiersza; klucz złożony podaje się na poziomie tabeli.

```sql
PRIMARY KEY (pseudo, imie_wroga)
```

### 1.4 NOT NULL / UNIQUE

`UNIQUE` dopuszcza tylko jeden `NULL` w kolumnie.

```sql
nazwa VARCHAR(20) NOT NULL, teren VARCHAR(15) UNIQUE
```

### 1.5 Filtrowany indeks unikalny

Unikalne wartości, ale wiele `NULL`-i dozwolonych (potrzebne dla `szef_bandy`).

```sql
CREATE UNIQUE INDEX bandy_szef_uq ON bandy (szef_bandy) WHERE szef_bandy IS NOT NULL;
```

### 1.6 CHECK

CHECK kolumny widzi tylko swoją kolumnę; warunki na kilku kolumnach idą na poziom tabeli.

```sql
plec VARCHAR(1) CHECK (plec IN ('M', 'D')),
CONSTRAINT fu_max_ch CHECK (max_myszy >= min_myszy)
```

### 1.7 REFERENCES

Klucz obcy, może wskazywać na tę samą tabelę.

```sql
szef VARCHAR(15) REFERENCES kocury(pseudo)
```

### 1.8 DEFAULT

Wartość użyta, gdy kolumna zostanie pominięta w INSERT.

```sql
w_stadku_od DATE DEFAULT GETDATE()
```

### 1.9 ALTER TABLE ADD CONSTRAINT

Dodaje ograniczenie później, np. żeby rozbić cykl `bandy` ↔ `kocury`.

```sql
ALTER TABLE bandy ADD CONSTRAINT bandy_szef_fk FOREIGN KEY (szef_bandy) REFERENCES kocury(pseudo);
```

### 1.10 NOCHECK CONSTRAINT

Nie ma `DEFERRABLE`; zamiast tego ograniczenie można wyłączyć i włączyć z powrotem.

```sql
ALTER TABLE bandy NOCHECK CONSTRAINT bandy_szef_fk;
ALTER TABLE bandy WITH CHECK CHECK CONSTRAINT bandy_szef_fk;
```

### 1.11 ALTER TABLE – kolumny

Dodaje, zmienia albo usuwa kolumnę (bez słowa `COLUMN` w `ADD`).

```sql
ALTER TABLE kocury ADD ulubiona VARCHAR(20);
ALTER TABLE kocury ALTER COLUMN ulubiona VARCHAR(30);
ALTER TABLE kocury DROP COLUMN ulubiona;
```

### 1.12 DROP TABLE

Usuwa tabelę; nie ma `CASCADE`, więc najpierw usuń wskazujące na nią klucze obce.

```sql
DROP TABLE IF EXISTS wrogowie_kocurow;
```

### 1.13 IDENTITY

Kolumna auto-inkrementowana.

```sql
CREATE TABLE incydenty (id INT IDENTITY(1, 1) PRIMARY KEY, opis VARCHAR(50));
```

## 2. DML

### 2.1 INSERT … VALUES

Wstawia jeden lub więcej wierszy (do 1000 w jednym `VALUES`).

```sql
INSERT INTO funkcje (funkcja, min_myszy, max_myszy) VALUES ('SZEFUNIO', 90, 110), ('BANDZIOR', 70, 90);
```

### 2.2 Literał daty

Zwykły napis; `DATE '…'` nie jest obsługiwane, `'YYYYMMDD'` działa przy każdych ustawieniach.

```sql
INSERT INTO kocury (imie, pseudo, w_stadku_od) VALUES ('JACEK', 'PLACEK', '2008-12-01');
```

### 2.3 UPDATE

Zmienia wartości w pasujących wierszach.

```sql
UPDATE bandy SET szef_bandy = 'TYGRYS' WHERE nr_bandy = 1;
```

### 2.4 UPDATE … FROM JOIN

Aktualizacja wartościami z dołączonej tabeli.

```sql
UPDATE K SET nr_bandy = B.nr_bandy FROM kocury K JOIN bandy B ON B.szef_bandy = K.szef;
```

### 2.5 DELETE

Usuwa pasujące wiersze.

```sql
DELETE FROM wrogowie_kocurow WHERE imie_wroga = 'KAZIO';
```

### 2.6 OUTPUT

Zwraca zmienione wiersze (`inserted` = nowe wartości, `deleted` = stare).

```sql
UPDATE kocury SET przydzial_myszy += 1 OUTPUT deleted.przydzial_myszy, inserted.przydzial_myszy WHERE pseudo = 'ZERO';
```

### 2.7 BEGIN TRAN / COMMIT / ROLLBACK

Instrukcje zatwierdzają się automatycznie; `BEGIN TRAN` zaczyna transakcję, którą można wycofać.

```sql
BEGIN TRAN; DELETE FROM wrogowie_kocurow; ROLLBACK;
```

## 3. Podstawy SELECT

### 3.1 SELECT … FROM

Wybiera kolumny z tabeli.

```sql
SELECT imie, funkcja FROM kocury;
```

### 3.2 Alias kolumny

Podwójne cudzysłowy albo nawiasy kwadratowe zachowują spacje.

```sql
SELECT przydzial_myszy AS "Zjada", myszy_extra AS [Myszy extra] FROM kocury;
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

### 3.5 WHERE

Filtruje wiersze.

```sql
SELECT imie FROM kocury WHERE plec = 'D' AND przydzial_myszy >= 55;
```

### 3.6 BETWEEN

Sprawdzenie zakresu, oba końce włącznie.

```sql
SELECT imie FROM kocury WHERE w_stadku_od BETWEEN '2005-09-01' AND '2007-07-31';
```

### 3.7 IN

Pasuje do dowolnej wartości z listy.

```sql
SELECT imie FROM kocury WHERE funkcja IN ('MILUSIA', 'KOT');
```

### 3.8 LIKE

Dopasowanie wzorca; przy domyślnym collation nie rozróżnia wielkości liter, `[ ]` pasuje do zbioru znaków.

```sql
SELECT pseudo FROM kocury WHERE pseudo LIKE '%a%' AND pseudo LIKE '[L-T]%';
```

### 3.9 IS NULL

Jedyny poprawny sposób sprawdzenia `NULL`.

```sql
SELECT imie_wroga FROM wrogowie WHERE lapowka IS NULL;
```

### 3.10 ORDER BY

Sortuje wynik; `NULL`-e są na początku przy `ASC`.

```sql
SELECT pseudo, w_stadku_od FROM kocury ORDER BY w_stadku_od DESC, pseudo;
```

### 3.11 TOP

Pierwsze n wierszy; `WITH TIES` zostawia też wiersze równe ostatniemu.

```sql
SELECT TOP 3 WITH TIES pseudo FROM kocury ORDER BY przydzial_myszy DESC;
```

### 3.12 OFFSET … FETCH

Pomija i pobiera wiersze; wymaga `ORDER BY`.

```sql
SELECT pseudo FROM kocury ORDER BY przydzial_myszy DESC OFFSET 1 ROWS FETCH NEXT 3 ROWS ONLY;
```

### 3.13 SELECT bez FROM

Nie potrzeba sztucznej tabeli.

```sql
SELECT GETDATE();
```

## 4. Napisy

### 4.1 +

Łączenie napisów; liczby i daty trzeba rzutować, a `NULL` daje `NULL` w wyniku.

```sql
SELECT imie + ' zwany ' + pseudo + ' z bandy ' + CAST(nr_bandy AS VARCHAR) FROM kocury;
```

### 4.2 CONCAT

Łączy dowolną liczbę argumentów, konwertuje typy i pomija `NULL`-e.

```sql
SELECT CONCAT(imie, ' zwany ', pseudo, ' od ', w_stadku_od) FROM kocury;
```

### 4.3 CONCAT_WS

Łączy wartości z separatorem między nimi.

```sql
SELECT CONCAT_WS(', ', imie, pseudo, funkcja) FROM kocury;
```

### 4.4 LOWER / UPPER

Zmienia wielkość liter (nie ma `INITCAP`).

```sql
SELECT LOWER(pseudo), UPPER(imie) FROM kocury;
```

### 4.5 LEN

Liczba znaków, bez spacji na końcu.

```sql
SELECT pseudo FROM kocury WHERE LEN(pseudo) > 4;
```

### 4.6 CHARINDEX

Pozycja podnapisu (od 1), 0 gdy nie znaleziono; szukany tekst idzie pierwszy.

```sql
SELECT pseudo, CHARINDEX('A', pseudo) FROM kocury;
```

### 4.7 SUBSTRING

Podnapis od pozycji; długość jest wymagana.

```sql
SELECT SUBSTRING(pseudo, 2, 3) FROM kocury;
```

### 4.8 LEFT / RIGHT

Pierwsze / ostatnie n znaków.

```sql
SELECT LEFT(pseudo, 2), RIGHT(pseudo, 2) FROM kocury;
```

### 4.9 REPLICATE

Powtarza napis; używane do dopełniania, bo nie ma `LPAD`.

```sql
SELECT RIGHT(REPLICATE('*', 10) + pseudo, 10) FROM kocury;
```

### 4.10 REPLACE

Zamienia wszystkie wystąpienia.

```sql
SELECT REPLACE(pseudo, 'A', '#') FROM kocury;
```

### 4.11 STUFF

Zamienia znaki na podanej pozycji (jak `OVERLAY` w Postgresie).

```sql
SELECT STUFF(pseudo, CHARINDEX('A', pseudo), 1, '#') FROM kocury WHERE pseudo LIKE '%A%';
```

### 4.12 TRIM / LTRIM / RTRIM

Usuwa spacje z obu stron.

```sql
SELECT TRIM('  TYGRYS  ');
```

### 4.13 Pusty napis

`''` to zwykła wartość, nie `NULL`.

```sql
SELECT CASE WHEN '' IS NULL THEN 'NULL' ELSE 'nie NULL' END;
```

## 5. Obsługa NULL

### 5.1 ISNULL

Zamienia `NULL` na wartość (2 argumenty).

```sql
SELECT przydzial_myszy + ISNULL(myszy_extra, 0) FROM kocury;
```

### 5.2 COALESCE

Pierwszy argument różny od `NULL`.

```sql
SELECT COALESCE(myszy_extra, przydzial_myszy, 0) FROM kocury;
```

### 5.3 NULLIF

Zwraca `NULL`, gdy oba argumenty są równe.

```sql
SELECT NULLIF(przydzial_myszy, 0) FROM kocury;
```

### 5.4 Porównywanie z NULL

`= NULL` nigdy nie jest prawdą; funkcje agregujące pomijają `NULL`-e.

```sql
SELECT imie FROM kocury WHERE myszy_extra = NULL;   -- zawsze 0 wierszy
```

### 5.5 IS DISTINCT FROM

„Różne” bezpieczne dla `NULL` (od 2022).

```sql
SELECT imie FROM kocury WHERE myszy_extra IS DISTINCT FROM 0;
```

## 6. Liczby

### 6.1 ROUND

Zaokrągla do n miejsc po przecinku; drugi argument jest wymagany.

```sql
SELECT ROUND(przydzial_myszy / 1.1, 0), ROUND(przydzial_myszy / 1.1, 1) FROM kocury;
```

### 6.2 Obcinanie

`ROUND(x, n, 1)` obcina zamiast zaokrąglać.

```sql
SELECT ROUND(przydzial_myszy / 7.0, 0, 1) FROM kocury;
```

### 6.3 %

Reszta z dzielenia.

```sql
SELECT pseudo FROM kocury WHERE przydzial_myszy % 2 = 0;
```

### 6.4 CEILING / FLOOR

Zaokrągla w górę / w dół do liczby całkowitej.

```sql
SELECT CEILING(przydzial_myszy / 7.0), FLOOR(przydzial_myszy / 7.0) FROM kocury;
```

### 6.5 Dzielenie całkowite

Liczba całkowita / całkowita obcina część ułamkową: `5 / 2 = 2`.

```sql
SELECT 5 / 2, 5.0 / 2, CAST(5 AS NUMERIC(5, 2)) / 2;
```

## 7. Daty

### 7.1 GETDATE / SYSDATETIME

Bieżąca data i czas; rzutuj na `DATE`, żeby dostać sam dzień.

```sql
SELECT GETDATE(), SYSDATETIME(), CAST(GETDATE() AS DATE);
```

### 7.2 YEAR / MONTH / DAY

Wyciąga część daty jako liczbę.

```sql
SELECT imie_wroga FROM wrogowie_kocurow WHERE YEAR(data_incydentu) = 2009;
```

### 7.3 DATEPART

Dowolna część daty jako liczba; `WEEKDAY` zależy od `@@DATEFIRST`.

```sql
SELECT DATEPART(QUARTER, w_stadku_od), DATEPART(WEEKDAY, w_stadku_od) FROM kocury;
```

### 7.4 DATEADD

Dodaje dni, miesiące, lata… (nie ma `data + n`).

```sql
SELECT DATEADD(MONTH, 6, w_stadku_od) FROM kocury;
```

### 7.5 DATEDIFF

Liczy przekroczone granice jednostek, a nie pełne jednostki.

```sql
SELECT pseudo FROM kocury WHERE w_stadku_od <= DATEADD(YEAR, -15, GETDATE());
```

### 7.6 EOMONTH

Ostatni dzień miesiąca, opcjonalnie n miesięcy do przodu.

```sql
SELECT EOMONTH(GETDATE()), EOMONTH(GETDATE(), 1);
```

### 7.7 DATETRUNC

Początek miesiąca/roku/… (od 2022).

```sql
SELECT DATETRUNC(MONTH, GETDATE());
```

### 7.8 DATEFROMPARTS

Tworzy datę z liczb.

```sql
SELECT DATEFROMPARTS(2009, 1, 7);
```

### 7.9 DATENAME

Nazwa części daty, w języku sesji.

```sql
SELECT DATENAME(MONTH, w_stadku_od), DATENAME(WEEKDAY, w_stadku_od) FROM kocury;
```

### 7.10 Ostatnia środa miesiąca

Ostatni dzień minus liczba dni wstecz do środy, niezależnie od `@@DATEFIRST`.

```sql
SELECT DATEADD(DAY, -((DATEPART(WEEKDAY, EOMONTH(GETDATE())) + @@DATEFIRST + 3) % 7), EOMONTH(GETDATE()));
```

## 8. Konwersje

### 8.1 CAST

Standardowa konwersja typu.

```sql
SELECT CAST(przydzial_myszy AS VARCHAR(10)) FROM kocury;
```

### 8.2 CONVERT

Konwersja z kodem stylu (`23` = `yyyy-mm-dd`).

```sql
SELECT CONVERT(VARCHAR(10), w_stadku_od, 23) FROM kocury;
```

### 8.3 FORMAT

Formatowanie dat i liczb w stylu .NET.

```sql
SELECT FORMAT(w_stadku_od, 'yyyy-MM-dd'), FORMAT(w_stadku_od, 'MMMM') FROM kocury;
```

### 8.4 TRY_CAST

Zwraca `NULL` zamiast błędu, gdy konwersja się nie uda.

```sql
SELECT TRY_CAST('abc' AS INT);
```

## 9. Warunki

### 9.1 CASE WHEN

Warunki sprawdzane po kolei; wszystkie gałęzie muszą zwracać ten sam typ.

```sql
SELECT CASE WHEN 12 * przydzial_myszy > 660 THEN CAST(12 * przydzial_myszy AS VARCHAR) ELSE 'Ponizej 660' END FROM kocury;
```

### 9.2 CASE wartość

Porównuje jedno wyrażenie z wartościami.

```sql
SELECT CASE plec WHEN 'M' THEN 'Kocur' ELSE 'Kotka' END FROM kocury;
```

### 9.3 IIF

Krótkie if-else.

```sql
SELECT IIF(plec = 'M', 'Kocur', 'Kotka') FROM kocury;
```

### 9.4 GREATEST / LEAST

Największy / najmniejszy z argumentów (od 2022).

```sql
SELECT pseudo, GREATEST(przydzial_myszy, ISNULL(myszy_extra, 0)) FROM kocury;
```

## 10. Grupowanie

### 10.1 COUNT

`COUNT(*)` liczy wiersze, `COUNT(kol)` wartości różne od `NULL`, `COUNT(DISTINCT kol)` unikalne wartości.

```sql
SELECT COUNT(*), COUNT(myszy_extra), COUNT(DISTINCT nr_bandy) FROM kocury;
```

### 10.2 SUM / AVG / MIN / MAX

Funkcje agregujące; `AVG` z kolumny `INT` zwraca `INT`.

```sql
SELECT SUM(przydzial_myszy), AVG(przydzial_myszy), MIN(w_stadku_od), MAX(przydzial_myszy) FROM kocury;
```

### 10.3 GROUP BY

Jeden wiersz na grupę; każdą niezagregowaną kolumnę trzeba wypisać, aliasy niedozwolone.

```sql
SELECT B.nr_bandy, B.nazwa, COUNT(*) FROM bandy B JOIN kocury K ON K.nr_bandy = B.nr_bandy GROUP BY B.nr_bandy, B.nazwa;
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

### 10.6 STRING_AGG

Łączy wartości z grupy w jeden napis.

```sql
SELECT nr_bandy, STRING_AGG(pseudo, ', ') WITHIN GROUP (ORDER BY pseudo) FROM kocury GROUP BY nr_bandy;
```

### 10.7 Grupowanie po wyrażeniu

Całe wyrażenie trzeba powtórzyć w `GROUP BY`.

```sql
SELECT MONTH(w_stadku_od), COUNT(*) FROM kocury GROUP BY MONTH(w_stadku_od);
```

### 10.8 ROLLUP

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

### 11.6 Brak USING / NATURAL

Warunek złączenia zawsze piszemy przez `ON`.

```sql
SELECT K.pseudo, B.nazwa FROM kocury K JOIN bandy B ON B.nr_bandy = K.nr_bandy;
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

### 11.11 CROSS / OUTER APPLY

Podzapytanie wykonywane dla każdego wiersza, może używać jego kolumn (`OUTER` zostawia wiersze bez dopasowania).

```sql
SELECT K.pseudo, O.imie_wroga FROM kocury K OUTER APPLY (SELECT TOP 1 imie_wroga FROM wrogowie_kocurow WK WHERE WK.pseudo = K.pseudo ORDER BY data_incydentu DESC) O;
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

Używa wyniku zapytania jak tabeli; alias jest wymagany, a `ORDER BY` w środku wymaga `TOP`.

```sql
SELECT * FROM (SELECT nr_bandy, SUM(przydzial_myszy) AS suma FROM kocury GROUP BY nr_bandy) AS sumy WHERE suma > 200;
```

### 13.9 WITH (CTE)

Nazywa podzapytanie, żeby można było go użyć ponownie; poprzednią instrukcję zakończ `;`.

```sql
WITH sumy AS (SELECT nr_bandy, SUM(przydzial_myszy) AS suma FROM kocury GROUP BY nr_bandy) SELECT * FROM sumy WHERE suma > 200;
```

## 14. Zapytania hierarchiczne

### 14.1 Rekurencyjne CTE

Wiersze startowe `UNION ALL` krok, który łączy się z powrotem z CTE (bez słowa `RECURSIVE`).

```sql
WITH drzewo AS (SELECT pseudo FROM kocury WHERE szef IS NULL UNION ALL SELECT K.pseudo FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SELECT * FROM drzewo;
```

### 14.2 Poziom

Przenoś kolumnę-licznik, zwiększaną w każdym kroku.

```sql
WITH drzewo AS (SELECT pseudo, 1 AS poziom FROM kocury WHERE funkcja = 'BANDZIOR' UNION ALL SELECT K.pseudo, D.poziom + 1 FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SELECT REPLICATE(' ', 4 * (poziom - 1)) + pseudo FROM drzewo;
```

### 14.3 W górę drzewa

Odwróć kierunek złączenia, żeby iść od kota do jego szefów.

```sql
WITH szefowie AS (SELECT pseudo, szef FROM kocury WHERE pseudo = 'ZERO' UNION ALL SELECT K.pseudo, K.szef FROM kocury K JOIN szefowie S ON K.pseudo = S.szef) SELECT pseudo FROM szefowie;
```

### 14.4 Ścieżka i korzeń

Przenoś wartość z korzenia i ścieżkę; obie części muszą mieć dokładnie ten sam typ, więc rzutuj.

```sql
WITH sciezki AS (SELECT imie AS korzen, szef, CAST(imie AS VARCHAR(200)) AS sciezka FROM kocury WHERE funkcja = 'KOT' UNION ALL SELECT S.korzen, K.szef, CAST(S.sciezka + ' | ' + K.imie AS VARCHAR(200)) FROM sciezki S JOIN kocury K ON K.pseudo = S.szef) SELECT korzen, sciezka FROM sciezki WHERE szef IS NULL;
```

### 14.5 Sprawdzanie liścia

Wiersz jest liściem, gdy nikt na niego nie wskazuje.

```sql
SELECT K.pseudo FROM kocury K WHERE NOT EXISTS (SELECT 1 FROM kocury P WHERE P.szef = K.pseudo);
```

### 14.6 Kolejność drzewa

Sortuj po ścieżce budowanej od korzenia w dół (odpowiednik `ORDER SIBLINGS BY` z Oracle).

```sql
WITH drzewo AS (SELECT pseudo, CAST(pseudo AS VARCHAR(200)) AS sciezka FROM kocury WHERE szef IS NULL UNION ALL SELECT K.pseudo, CAST(D.sciezka + '/' + K.pseudo AS VARCHAR(200)) FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SELECT pseudo FROM drzewo ORDER BY sciezka;
```

### 14.7 MAXRECURSION

Limit głębokości rekurencji (domyślnie 100, 0 = bez limitu).

```sql
WITH liczby AS (SELECT 1 AS n UNION ALL SELECT n + 1 FROM liczby WHERE n < 500) SELECT n FROM liczby OPTION (MAXRECURSION 0);
```

### 14.8 GENERATE_SERIES

Generuje liczby 1..n (od 2022).

```sql
SELECT value FROM GENERATE_SERIES(1, 12);
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
SELECT pseudo, ROUND(100.0 * przydzial_myszy / SUM(przydzial_myszy) OVER (PARTITION BY nr_bandy), 0) AS proc FROM kocury;
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

### 15.9 Top-N w grupie

Funkcji okna nie można używać w `WHERE`, więc filtruj w zapytaniu zewnętrznym.

```sql
SELECT * FROM (SELECT pseudo, nr_bandy, RANK() OVER (PARTITION BY nr_bandy ORDER BY przydzial_myszy DESC) AS rnk FROM kocury) AS ranking WHERE rnk = 1;
```

## 16. Pivot

### 16.1 PIVOT

Zamienia wartości z wierszy na kolumny; wartości w `[ ]`, tylko jeden agregat.

```sql
SELECT nr_bandy, [M] AS samce, [D] AS samice FROM (SELECT nr_bandy, plec, przydzial_myszy FROM kocury) AS zrodlo PIVOT (SUM(przydzial_myszy) FOR plec IN ([M], [D])) AS wynik;
```

### 16.2 UNPIVOT

Zamienia kolumny na wiersze; kolumny muszą mieć ten sam typ, `NULL`-e są pomijane.

```sql
SELECT pseudo, rodzaj, ile FROM (SELECT pseudo, przydzial_myszy, myszy_extra FROM kocury) AS zrodlo UNPIVOT (ile FOR rodzaj IN (przydzial_myszy, myszy_extra)) AS wynik;
```

### 16.3 Unpivot przez APPLY

Kolumny na wiersze z własnymi etykietami, z zachowaniem `NULL`-i.

```sql
SELECT K.pseudo, U.rodzaj, U.ile FROM kocury K CROSS APPLY (VALUES ('PRZYDZIAL', K.przydzial_myszy), ('EXTRA', K.myszy_extra)) AS U(rodzaj, ile);
```

### 16.4 Pivot przez CASE

Przenośny pivot: jeden warunkowy agregat na kolumnę.

```sql
SELECT nr_bandy, SUM(CASE WHEN plec = 'M' THEN przydzial_myszy ELSE 0 END) AS samce, SUM(CASE WHEN plec = 'D' THEN przydzial_myszy ELSE 0 END) AS samice FROM kocury GROUP BY nr_bandy;
```

## 17. sqlcmd

### 17.1 GO

Wysyła dotąd wpisaną paczkę na serwer (to nie SQL, tylko polecenie sqlcmd).

```sql
SELECT * FROM kocury;
GO
```

### 17.2 :r

Uruchamia plik ze skryptem.

```sql
:r lista3/zad1.sql
```

### 17.3 Flaga -I

Włącza `QUOTED_IDENTIFIER`, wymagane dla indeksów filtrowanych (używane w `db.sh`).

```sql
sqlcmd -S localhost -U sa -P Admin_123 -C -I
```

### 17.4 sp_help

Pokazuje kolumny i ograniczenia tabeli.

```sql
EXEC sp_help 'kocury';
```

### 17.5 sys.tables

Lista tabel.

```sql
SELECT name FROM sys.tables;
```

### 17.6 EXIT

Wychodzi z sqlcmd.

```sql
EXIT
```

## 18. Combosy

### 18.1 Drzewo ze źródłem z SELECT-a

Przefiltrowane wiersze wrzucasz do CTE i na nim puszczasz rekurencję; kot wycięty w źródle odcina też całą swoją gałąź (jak warunek w `CONNECT BY` w Oracle).

```sql
WITH zrodlo AS (
    SELECT pseudo, szef, przydzial_myszy + ISNULL(myszy_extra, 0) AS myszy
    FROM kocury
    WHERE plec = 'M'
), drzewo AS (
    SELECT pseudo, myszy, 1 AS poziom FROM zrodlo WHERE szef IS NULL
    UNION ALL
    SELECT z.pseudo, z.myszy, d.poziom + 1 FROM zrodlo z JOIN drzewo d ON z.szef = d.pseudo
)
SELECT REPLICATE(' ', 4 * (poziom - 1)) + pseudo AS hierarchia, myszy
FROM drzewo;
```

### 18.2 Drzewo jako źródło + JOIN

Najpierw budujesz drzewo, potem traktujesz je jak zwykłą tabelę: złączenie, filtr po poziomie, sortowanie.

```sql
WITH drzewo AS (
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
WITH pod AS (
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
WITH drzewo AS (
    SELECT pseudo, 1 AS poziom FROM kocury
    WHERE pseudo IN (SELECT szef_bandy FROM bandy WHERE teren = 'POLE')
    UNION ALL
    SELECT k.pseudo, d.poziom + 1 FROM kocury k JOIN drzewo d ON k.szef = d.pseudo
)
SELECT REPLICATE(' ', 4 * (poziom - 1)) + pseudo AS hierarchia
FROM drzewo;
```

### 18.5 Pogrupowany SELECT jako źródło + JOIN

Agregujesz w podzapytaniu, dołączasz je z powrotem do wierszy i porównujesz każdy wiersz z jego grupą.

```sql
SELECT k.pseudo, k.przydzial_myszy, s.srednia
FROM kocury k
JOIN (SELECT nr_bandy, ROUND(AVG(1.0 * przydzial_myszy), 1) AS srednia
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
WITH drzewo AS (
    SELECT pseudo, przydzial_myszy, 1 AS poziom FROM kocury WHERE pseudo = 'TYGRYS'
    UNION ALL
    SELECT k.pseudo, k.przydzial_myszy, d.poziom + 1 FROM kocury k JOIN drzewo d ON k.szef = d.pseudo
)
SELECT poziom, COUNT(*) AS ile, ROUND(AVG(1.0 * przydzial_myszy), 1) AS srednio,
       STRING_AGG(pseudo, ', ') WITHIN GROUP (ORDER BY pseudo) AS koty
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
WITH w_gore AS (
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

Podpowiedź: CTE-źródło robi filtr band, drzewo idzie po nim; sortowanie po ścieżce daje kolejność drzewa. Ścieżka wymaga `CAST` w obu częściach.

```sql
WITH zrodlo AS (
    SELECT k.pseudo, k.szef, b.nazwa
    FROM kocury k JOIN bandy b ON b.nr_bandy = k.nr_bandy
    WHERE b.teren IN ('POLE', 'CALOSC')
), drzewo AS (
    SELECT pseudo, nazwa, 1 AS poziom, CAST(pseudo AS VARCHAR(200)) AS sciezka FROM zrodlo WHERE szef IS NULL
    UNION ALL
    SELECT z.pseudo, z.nazwa, d.poziom + 1, CAST(d.sciezka + '/' + z.pseudo AS VARCHAR(200)) FROM zrodlo z JOIN drzewo d ON z.szef = d.pseudo
)
SELECT REPLICATE(' ', 4 * (poziom - 1)) + pseudo AS hierarchia, nazwa
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
WITH pod AS (
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
JOIN (SELECT nr_bandy, ROUND(AVG(1.0 * przydzial_myszy), 1) AS srednia
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

Podpowiedź: T-SQL nie ma `RANGE` z przesunięciem, więc ±60 dni liczy podzapytanie skorelowane z `DATEADD`; reszta funkcjami okna jak w Postgresie (`LAG(..., 1, 0)`, `COUNT(*)` narastająco, `ROW_NUMBER()`).

```sql
SELECT wk.data_incydentu, wk.pseudo, k.nr_bandy,
       (SELECT COUNT(*) FROM wrogowie_kocurow x
        WHERE x.data_incydentu BETWEEN DATEADD(DAY, -60, wk.data_incydentu) AND DATEADD(DAY, 60, wk.data_incydentu)) - 1 AS incydenty_w_ok_60dni,
       CASE WHEN LAG(w.stopien_wrogosci, 1, 0) OVER (ORDER BY wk.data_incydentu, wk.pseudo) > w.stopien_wrogosci
            THEN 'TAK' ELSE 'NIE' END AS czy_poprzedni_bardziej_agresywny,
       COUNT(*) OVER (PARTITION BY YEAR(wk.data_incydentu)
                      ORDER BY wk.data_incydentu, wk.pseudo
                      ROWS UNBOUNDED PRECEDING) AS incydentow_od_poczatku_roku,
       ROW_NUMBER() OVER (PARTITION BY k.nr_bandy, YEAR(wk.data_incydentu)
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
LEFT JOIN (SELECT w.gatunek, w.stopien_wrogosci AS agresja, YEAR(wk.data_incydentu) AS rok
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

Podpowiedź: dwa rekurencyjne CTE – jedno buduje ścieżkę od korzenia w dół, drugie daje pary (kot, on sam lub dowolny podwładny); potem złączenie i `SUM`. Ścieżka wymaga `CAST` w obu częściach CTE.

```sql
WITH sciezki AS (
    SELECT pseudo, imie, funkcja, CAST(pseudo AS VARCHAR(200)) AS sciezka
    FROM kocury
    WHERE szef IS NULL
    UNION ALL
    SELECT k.pseudo, k.imie, k.funkcja, CAST(s.sciezka + ' -> ' + k.pseudo AS VARCHAR(200))
    FROM kocury k JOIN sciezki s ON k.szef = s.pseudo
), pod AS (
    SELECT pseudo AS korzen, pseudo FROM kocury
    UNION ALL
    SELECT p.korzen, k.pseudo FROM kocury k JOIN pod p ON k.szef = p.pseudo
)
SELECT s.imie, s.funkcja, s.sciezka,
       SUM(k.przydzial_myszy + ISNULL(k.myszy_extra, 0)) AS budzet_rodzinny
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
