# Notatki dokumentacja – Oracle (23ai)

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
- [2. DML](#2-dml)
  - [2.1 INSERT … VALUES](#21-insert--values)
  - [2.2 INSERT ALL](#22-insert-all)
  - [2.3 Literał DATE](#23-literał-date)
  - [2.4 UPDATE](#24-update)
  - [2.5 DELETE](#25-delete)
  - [2.6 COMMIT / ROLLBACK](#26-commit--rollback)
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
  - [3.11 FETCH FIRST](#311-fetch-first)
  - [3.12 ROWNUM](#312-rownum)
  - [3.13 DUAL](#313-dual)
- [4. Napisy](#4-napisy)
  - [4.1 ||](#41-)
  - [4.2 CONCAT](#42-concat)
  - [4.3 LOWER / UPPER / INITCAP](#43-lower--upper--initcap)
  - [4.4 LENGTH](#44-length)
  - [4.5 INSTR](#45-instr)
  - [4.6 SUBSTR](#46-substr)
  - [4.7 LPAD / RPAD](#47-lpad--rpad)
  - [4.8 REPLACE](#48-replace)
  - [4.9 REGEXP_REPLACE](#49-regexp_replace)
  - [4.10 TRIM](#410-trim)
  - [4.11 Zamiana jednego znaku](#411-zamiana-jednego-znaku)
  - [4.12 Pusty napis](#412-pusty-napis)
- [5. Obsługa NULL](#5-obsługa-null)
  - [5.1 NVL](#51-nvl)
  - [5.2 COALESCE](#52-coalesce)
  - [5.3 NVL2](#53-nvl2)
  - [5.4 NULLIF](#54-nullif)
  - [5.5 Porównywanie z NULL](#55-porównywanie-z-null)
- [6. Liczby](#6-liczby)
  - [6.1 ROUND](#61-round)
  - [6.2 TRUNC](#62-trunc)
  - [6.3 MOD](#63-mod)
  - [6.4 CEIL / FLOOR](#64-ceil--floor)
  - [6.5 Dzielenie](#65-dzielenie)
- [7. Daty](#7-daty)
  - [7.1 SYSDATE / CURRENT_DATE](#71-sysdate--current_date)
  - [7.2 TRUNC(date)](#72-truncdate)
  - [7.3 EXTRACT](#73-extract)
  - [7.4 Arytmetyka dat](#74-arytmetyka-dat)
  - [7.5 ADD_MONTHS](#75-add_months)
  - [7.6 MONTHS_BETWEEN](#76-months_between)
  - [7.7 LAST_DAY](#77-last_day)
  - [7.8 NEXT_DAY](#78-next_day)
  - [7.9 INTERVAL](#79-interval)
- [8. Konwersje](#8-konwersje)
  - [8.1 TO_CHAR(number)](#81-to_charnumber)
  - [8.2 TO_CHAR(date, format)](#82-to_chardate-format)
  - [8.3 TO_NUMBER](#83-to_number)
  - [8.4 TO_DATE](#84-to_date)
  - [8.5 CAST](#85-cast)
- [9. Warunki](#9-warunki)
  - [9.1 CASE WHEN](#91-case-when)
  - [9.2 CASE wartość](#92-case-wartość)
  - [9.3 DECODE](#93-decode)
- [10. Grupowanie](#10-grupowanie)
  - [10.1 COUNT](#101-count)
  - [10.2 SUM / AVG / MIN / MAX](#102-sum--avg--min--max)
  - [10.3 GROUP BY](#103-group-by)
  - [10.4 HAVING](#104-having)
  - [10.5 Warunkowy COUNT](#105-warunkowy-count)
  - [10.6 LISTAGG](#106-listagg)
  - [10.7 Grupowanie po wyrażeniu](#107-grupowanie-po-wyrażeniu)
  - [10.8 ROLLUP](#108-rollup)
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
  - [11.11 Złączenie zewnętrzne (+)](#1111-złączenie-zewnętrzne-)
  - [11.12 Pułapka LEFT JOIN + WHERE](#1112-pułapka-left-join--where)
- [12. Operatory zbiorowe](#12-operatory-zbiorowe)
  - [12.1 UNION / UNION ALL](#121-union--union-all)
  - [12.2 INTERSECT](#122-intersect)
  - [12.3 MINUS](#123-minus)
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
  - [14.1 START WITH](#141-start-with)
  - [14.2 CONNECT BY PRIOR](#142-connect-by-prior)
  - [14.3 LEVEL](#143-level)
  - [14.4 ORDER SIBLINGS BY](#144-order-siblings-by)
  - [14.5 WHERE a warunek w CONNECT BY](#145-where-a-warunek-w-connect-by)
  - [14.6 CONNECT_BY_ROOT](#146-connect_by_root)
  - [14.7 SYS_CONNECT_BY_PATH](#147-sys_connect_by_path)
  - [14.8 CONNECT_BY_ISLEAF](#148-connect_by_isleaf)
  - [14.9 Rekurencyjne WITH](#149-rekurencyjne-with)
  - [14.10 Generator wierszy](#1410-generator-wierszy)
- [15. Funkcje okna (OVER)](#15-funkcje-okna-over)
  - [15.1 OVER ()](#151-over-)
  - [15.2 PARTITION BY](#152-partition-by)
  - [15.3 ROW_NUMBER](#153-row_number)
  - [15.4 RANK / DENSE_RANK](#154-rank--dense_rank)
  - [15.5 Suma narastająca](#155-suma-narastająca)
  - [15.6 ROWS BETWEEN](#156-rows-between)
  - [15.7 LAG / LEAD](#157-lag--lead)
  - [15.8 FIRST_VALUE](#158-first_value)
  - [15.9 RATIO_TO_REPORT](#159-ratio_to_report)
  - [15.10 Top-N w grupie](#1510-top-n-w-grupie)
- [16. Pivot](#16-pivot)
  - [16.1 PIVOT](#161-pivot)
  - [16.2 PIVOT z kilkoma agregatami](#162-pivot-z-kilkoma-agregatami)
  - [16.3 UNPIVOT](#163-unpivot)
  - [16.4 Pivot przez CASE](#164-pivot-przez-case)
- [17. SQL*Plus](#17-sqlplus)
  - [17.1 SET LINESIZE / PAGESIZE](#171-set-linesize--pagesize)
  - [17.2 COLUMN FORMAT](#172-column-format)
  - [17.3 @file](#173-file)
  - [17.4 /](#174-)
  - [17.5 DESC](#175-desc)
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
  - [20.5 Zadanie z listy – koty jedzące więcej niż inne koty tej samej funkcji i płci](#205-zadanie-z-listy--koty-jedzące-więcej-niż-inne-koty-tej-samej-funkcji-i-płci)

## 1. DDL

### 1.1 CREATE TABLE

Tworzy tabelę; `IF NOT EXISTS` działa od 23ai.

```sql
CREATE TABLE IF NOT EXISTS wrogowie (imie_wroga VARCHAR2(15) PRIMARY KEY, gatunek VARCHAR2(15));
```

### 1.2 Typy danych

`NUMBER(p)`, `VARCHAR2(n)` i `DATE`, które przechowuje też czas.

```sql
pseudo VARCHAR2(15), przydzial_myszy NUMBER(3), w_stadku_od DATE
```

### 1.3 PRIMARY KEY

Unikalny, niepusty identyfikator wiersza; klucz złożony podaje się na poziomie tabeli.

```sql
PRIMARY KEY (pseudo, imie_wroga)
```

### 1.4 NOT NULL / UNIQUE

W Oracle `UNIQUE` dopuszcza wiele `NULL`-i.

```sql
nazwa VARCHAR2(20) NOT NULL, teren VARCHAR2(15) UNIQUE
```

### 1.5 CHECK

CHECK kolumny widzi tylko swoją kolumnę; warunki na kilku kolumnach idą na poziom tabeli.

```sql
plec VARCHAR2(1) CHECK (plec IN ('M', 'D')),
CONSTRAINT fu_max_ch CHECK (max_myszy >= min_myszy)
```

### 1.6 REFERENCES

Klucz obcy, może wskazywać na tę samą tabelę.

```sql
szef VARCHAR2(15) REFERENCES kocury(pseudo)
```

### 1.7 DEFAULT

Wartość użyta, gdy kolumna zostanie pominięta w INSERT.

```sql
w_stadku_od DATE DEFAULT SYSDATE
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

Dodaje, zmienia albo usuwa kolumnę.

```sql
ALTER TABLE kocury ADD (ulubiona VARCHAR2(20));
ALTER TABLE kocury MODIFY (ulubiona VARCHAR2(30));
ALTER TABLE kocury DROP COLUMN ulubiona;
```

### 1.11 DROP TABLE

Usuwa tabelę; `CASCADE CONSTRAINTS` usuwa też wskazujące na nią klucze obce.

```sql
DROP TABLE kocury CASCADE CONSTRAINTS;
```

## 2. DML

### 2.1 INSERT … VALUES

Wstawia wiersze; kilka wierszy w jednym `VALUES` działa od 23ai.

```sql
INSERT INTO funkcje (funkcja, min_myszy, max_myszy) VALUES ('SZEFUNIO', 90, 110), ('BANDZIOR', 70, 90);
```

### 2.2 INSERT ALL

Wstawianie wielu wierszy w starszych wersjach Oracle.

```sql
INSERT ALL INTO funkcje VALUES ('LOWCZY', 60, 70) INTO funkcje VALUES ('LAPACZ', 50, 60) SELECT * FROM dual;
```

### 2.3 Literał DATE

Stała daty w formacie ISO.

```sql
INSERT INTO kocury (imie, pseudo, w_stadku_od) VALUES ('JACEK', 'PLACEK', DATE '2008-12-01');
```

### 2.4 UPDATE

Zmienia wartości w pasujących wierszach.

```sql
UPDATE bandy SET szef_bandy = 'TYGRYS' WHERE nr_bandy = 1;
```

### 2.5 DELETE

Usuwa pasujące wiersze.

```sql
DELETE FROM wrogowie_kocurow WHERE imie_wroga = 'KAZIO';
```

### 2.6 COMMIT / ROLLBACK

DML nie jest zatwierdzany automatycznie; te polecenia zapisują albo wycofują transakcję.

```sql
COMMIT;
```

## 3. Podstawy SELECT

### 3.1 SELECT … FROM

Wybiera kolumny z tabeli.

```sql
SELECT imie, funkcja FROM kocury;
```

### 3.2 Alias kolumny

Podwójne cudzysłowy zachowują spacje i wielkość liter.

```sql
SELECT przydzial_myszy AS "Zjada" FROM kocury;
```

### 3.3 Alias tabeli

Pisany bez `AS` (w Oracle `AS` w tym miejscu to błąd).

```sql
SELECT K.imie FROM kocury K;
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
SELECT imie FROM kocury WHERE w_stadku_od BETWEEN DATE '2005-09-01' AND DATE '2007-07-31';
```

### 3.7 IN

Pasuje do dowolnej wartości z listy.

```sql
SELECT imie FROM kocury WHERE funkcja IN ('MILUSIA', 'KOT');
```

### 3.8 LIKE

Dopasowanie wzorca (`%` dowolny tekst, `_` jeden znak); rozróżnia wielkość liter, nie ma `ILIKE`.

```sql
SELECT pseudo FROM kocury WHERE LOWER(pseudo) LIKE '%a%';
```

### 3.9 IS NULL

Jedyny poprawny sposób sprawdzenia `NULL`.

```sql
SELECT imie_wroga FROM wrogowie WHERE lapowka IS NULL;
```

### 3.10 ORDER BY

Sortuje wynik; można używać aliasów kolumn.

```sql
SELECT pseudo, w_stadku_od FROM kocury ORDER BY w_stadku_od DESC, pseudo;
```

### 3.11 FETCH FIRST

Pierwsze N wierszy po sortowaniu.

```sql
SELECT pseudo FROM kocury ORDER BY przydzial_myszy DESC FETCH FIRST 3 ROWS ONLY;
```

### 3.12 ROWNUM

Numer wiersza nadawany przed wykonaniem `ORDER BY`.

```sql
SELECT pseudo FROM kocury WHERE ROWNUM <= 3;
```

### 3.13 DUAL

Jednowierszowa sztuczna tabela do wybierania wartości bez prawdziwej tabeli.

```sql
SELECT SYSDATE FROM dual;
```

## 4. Napisy

### 4.1 ||

Łączenie napisów; `NULL` działa jak pusty napis.

```sql
SELECT imie || ' zwany ' || pseudo FROM kocury;
```

### 4.2 CONCAT

Łączy dokładnie 2 argumenty.

```sql
SELECT CONCAT(imie, pseudo) FROM kocury;
```

### 4.3 LOWER / UPPER / INITCAP

Zmienia wielkość liter.

```sql
SELECT LOWER(pseudo), INITCAP(imie) FROM kocury;
```

### 4.4 LENGTH

Liczba znaków.

```sql
SELECT pseudo FROM kocury WHERE LENGTH(pseudo) > 4;
```

### 4.5 INSTR

Pozycja podnapisu (od 1), 0 gdy nie znaleziono.

```sql
SELECT pseudo, INSTR(pseudo, 'A') FROM kocury;
```

### 4.6 SUBSTR

Podnapis od pozycji, z opcjonalną długością.

```sql
SELECT SUBSTR(pseudo, 2, 3) FROM kocury;
```

### 4.7 LPAD / RPAD

Dopełnia tekst do długości; przydatne do wcięć w drzewie.

```sql
SELECT LPAD(pseudo, 10, '*') FROM kocury;
```

### 4.8 REPLACE

Zamienia wszystkie wystąpienia.

```sql
SELECT REPLACE(pseudo, 'A', '#') FROM kocury;
```

### 4.9 REGEXP_REPLACE

Zamiana regexem; tutaj tylko 1. dopasowanie, bez rozróżniania wielkości liter.

```sql
SELECT REGEXP_REPLACE(pseudo, 'a', '#', 1, 1, 'i') FROM kocury;
```

### 4.10 TRIM

Usuwa spacje z obu stron.

```sql
SELECT TRIM('  TYGRYS  ') FROM dual;
```

### 4.11 Zamiana jednego znaku

Odpowiednik `OVERLAY` z Postgresa.

```sql
SELECT SUBSTR(pseudo, 1, INSTR(pseudo, 'A') - 1) || '#' || SUBSTR(pseudo, INSTR(pseudo, 'A') + 1) FROM kocury WHERE pseudo LIKE '%A%';
```

### 4.12 Pusty napis

W Oracle `''` to `NULL`.

```sql
SELECT imie FROM kocury WHERE '' IS NULL;   -- wszystkie wiersze
```

## 5. Obsługa NULL

### 5.1 NVL

Zamienia `NULL` na wartość.

```sql
SELECT przydzial_myszy + NVL(myszy_extra, 0) FROM kocury;
```

### 5.2 COALESCE

Pierwszy argument różny od `NULL`.

```sql
SELECT COALESCE(myszy_extra, przydzial_myszy, 0) FROM kocury;
```

### 5.3 NVL2

Jedna wartość, gdy nie `NULL`, inna, gdy `NULL`.

```sql
SELECT NVL2(myszy_extra, 'ma extra', 'brak') FROM kocury;
```

### 5.4 NULLIF

Zwraca `NULL`, gdy oba argumenty są równe.

```sql
SELECT NULLIF(przydzial_myszy, 0) FROM kocury;
```

### 5.5 Porównywanie z NULL

`= NULL` nigdy nie jest prawdą; funkcje agregujące pomijają `NULL`-e.

```sql
SELECT imie FROM kocury WHERE myszy_extra = NULL;   -- zawsze 0 wierszy
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

### 6.3 MOD

Reszta z dzielenia.

```sql
SELECT pseudo FROM kocury WHERE MOD(przydzial_myszy, 2) = 0;
```

### 6.4 CEIL / FLOOR

Zaokrągla w górę / w dół do liczby całkowitej.

```sql
SELECT CEIL(przydzial_myszy / 7), FLOOR(przydzial_myszy / 7) FROM kocury;
```

### 6.5 Dzielenie

Nie ma dzielenia całkowitego: `5 / 2 = 2.5`.

```sql
SELECT 5 / 2 FROM dual;
```

## 7. Daty

### 7.1 SYSDATE / CURRENT_DATE

Bieżąca data z czasem.

```sql
SELECT SYSDATE, CURRENT_DATE FROM dual;
```

### 7.2 TRUNC(date)

Północ danego dnia albo początek miesiąca/roku z formatem.

```sql
SELECT TRUNC(SYSDATE), TRUNC(SYSDATE, 'MM'), TRUNC(SYSDATE, 'YYYY') FROM dual;
```

### 7.3 EXTRACT

Wyciąga rok, miesiąc albo dzień jako liczbę.

```sql
SELECT imie_wroga FROM wrogowie_kocurow WHERE EXTRACT(YEAR FROM data_incydentu) = 2009;
```

### 7.4 Arytmetyka dat

`+ n` dodaje dni; data minus data daje dni.

```sql
SELECT pseudo, SYSDATE - w_stadku_od, w_stadku_od + 7 FROM kocury;
```

### 7.5 ADD_MONTHS

Dodaje (albo odejmuje) miesiące.

```sql
SELECT imie FROM kocury WHERE w_stadku_od <= ADD_MONTHS(SYSDATE, -15 * 12);
```

### 7.6 MONTHS_BETWEEN

Liczba miesięcy między dwiema datami (ułamkowa).

```sql
SELECT pseudo FROM kocury WHERE MONTHS_BETWEEN(SYSDATE, w_stadku_od) > 15 * 12;
```

### 7.7 LAST_DAY

Ostatni dzień miesiąca danej daty.

```sql
SELECT LAST_DAY(SYSDATE) FROM dual;
```

### 7.8 NEXT_DAY

Pierwszy podany dzień tygodnia po dacie; nazwa dnia zależy od języka NLS.

```sql
SELECT NEXT_DAY(LAST_DAY(SYSDATE) - 7, 'WEDNESDAY') FROM dual;   -- ostatnia środa miesiąca
```

### 7.9 INTERVAL

Dodaje odstęp czasu; wywala się na nieistniejących datach (29 lutego), więc lepiej `ADD_MONTHS`.

```sql
SELECT w_stadku_od + INTERVAL '6' MONTH FROM kocury;
```

## 8. Konwersje

### 8.1 TO_CHAR(number)

Liczba na tekst.

```sql
SELECT TO_CHAR(12 * przydzial_myszy) FROM kocury;
```

### 8.2 TO_CHAR(date, format)

Formatuje datę; `MONTH` jest dopełniany spacjami, `FM` usuwa dopełnienie.

```sql
SELECT TO_CHAR(w_stadku_od, 'YYYY-MM-DD'), TO_CHAR(w_stadku_od, 'FMMONTH') FROM kocury;
```

### 8.3 TO_NUMBER

Tekst na liczbę.

```sql
SELECT TO_NUMBER('42') + 1 FROM dual;
```

### 8.4 TO_DATE

Tekst na datę według formatu.

```sql
SELECT TO_DATE('07-01-2009', 'DD-MM-YYYY') FROM dual;
```

### 8.5 CAST

Standardowa konwersja typu.

```sql
SELECT CAST(przydzial_myszy AS VARCHAR2(10)) FROM kocury;
```

## 9. Warunki

### 9.1 CASE WHEN

Warunki sprawdzane po kolei; wszystkie gałęzie muszą zwracać ten sam typ.

```sql
SELECT CASE WHEN przydzial_myszy > 60 THEN 'powyzej 60' ELSE 'ponizej 60' END FROM kocury;
```

### 9.2 CASE wartość

Porównuje jedno wyrażenie z wartościami.

```sql
SELECT CASE plec WHEN 'M' THEN 'Kocur' ELSE 'Kotka' END FROM kocury;
```

### 9.3 DECODE

Oraclowy skrót dla `CASE wartość`.

```sql
SELECT DECODE(plec, 'M', 'Kocur', 'D', 'Kotka', '?') FROM kocury;
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

Jeden wiersz na grupę; każdą niezagregowaną kolumnę trzeba wypisać.

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

### 10.6 LISTAGG

Łączy wartości z grupy w jeden napis.

```sql
SELECT nr_bandy, LISTAGG(pseudo, ', ') WITHIN GROUP (ORDER BY pseudo) FROM kocury GROUP BY nr_bandy;
```

### 10.7 Grupowanie po wyrażeniu

Grupuje po wyliczonej wartości.

```sql
SELECT EXTRACT(MONTH FROM w_stadku_od), COUNT(*) FROM kocury GROUP BY EXTRACT(MONTH FROM w_stadku_od);
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

### 11.6 USING / NATURAL

Złączenie po kolumnach o tej samej nazwie; tej kolumny używa się potem bez prefiksu.

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

### 11.11 Złączenie zewnętrzne (+)

Stara składnia Oracle; `(+)` oznacza stronę, która może być `NULL`.

```sql
SELECT B.nazwa, K.pseudo FROM bandy B, kocury K WHERE B.nr_bandy = K.nr_bandy(+);
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

### 12.3 MINUS

Wiersze z pierwszego wyniku, których nie ma w drugim (gdzie indziej `EXCEPT`).

```sql
SELECT nr_bandy FROM bandy MINUS SELECT nr_bandy FROM kocury;
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

Używa wyniku zapytania jak tabeli.

```sql
SELECT * FROM (SELECT nr_bandy, SUM(przydzial_myszy) AS suma FROM kocury GROUP BY nr_bandy) WHERE suma > 200;
```

### 13.9 WITH (CTE)

Nazywa podzapytanie, żeby można było go użyć ponownie.

```sql
WITH sumy AS (SELECT nr_bandy, SUM(przydzial_myszy) AS suma FROM kocury GROUP BY nr_bandy) SELECT * FROM sumy WHERE suma > 200;
```

## 14. Zapytania hierarchiczne

### 14.1 START WITH

Wybiera wiersze-korzenie drzewa.

```sql
SELECT pseudo FROM kocury START WITH szef IS NULL CONNECT BY PRIOR pseudo = szef;
```

### 14.2 CONNECT BY PRIOR

Określa powiązanie rodzic–dziecko; `PRIOR` oznacza kolumnę rodzica.

```sql
SELECT pseudo FROM kocury START WITH pseudo = 'ZERO' CONNECT BY PRIOR szef = pseudo;   -- w górę: kot → szefowie
```

### 14.3 LEVEL

Głębokość w drzewie, 1 dla korzenia.

```sql
SELECT LEVEL, LPAD(' ', 4 * (LEVEL - 1)) || pseudo FROM kocury START WITH szef IS NULL CONNECT BY PRIOR pseudo = szef;
```

### 14.4 ORDER SIBLINGS BY

Sortuje dzieci każdego węzła, zachowując kolejność drzewa.

```sql
SELECT pseudo FROM kocury START WITH funkcja = 'BANDZIOR' CONNECT BY PRIOR pseudo = szef ORDER SIBLINGS BY pseudo;
```

### 14.5 WHERE a warunek w CONNECT BY

`WHERE` ukrywa pojedyncze wiersze; warunek w `CONNECT BY` odcina całe gałęzie.

```sql
SELECT pseudo FROM kocury START WITH szef IS NULL CONNECT BY PRIOR pseudo = szef AND plec = 'M';
```

### 14.6 CONNECT_BY_ROOT

Wartość z korzenia bieżącej ścieżki.

```sql
SELECT CONNECT_BY_ROOT imie, imie FROM kocury START WITH funkcja = 'KOT' CONNECT BY PRIOR szef = pseudo;
```

### 14.7 SYS_CONNECT_BY_PATH

Ścieżka od korzenia do bieżącego wiersza jako jeden napis.

```sql
SELECT SYS_CONNECT_BY_PATH(imie, ' | ') FROM kocury START WITH funkcja = 'KOT' CONNECT BY PRIOR szef = pseudo;
```

### 14.8 CONNECT_BY_ISLEAF

1, jeśli wiersz nie ma dzieci.

```sql
SELECT pseudo FROM kocury WHERE CONNECT_BY_ISLEAF = 1 START WITH szef IS NULL CONNECT BY PRIOR pseudo = szef;
```

### 14.9 Rekurencyjne WITH

Rekurencja ze standardu SQL; Oracle wymaga listy kolumn.

```sql
WITH drzewo (pseudo, poziom) AS (SELECT pseudo, 1 FROM kocury WHERE szef IS NULL UNION ALL SELECT K.pseudo, D.poziom + 1 FROM kocury K JOIN drzewo D ON K.szef = D.pseudo) SELECT * FROM drzewo;
```

### 14.10 Generator wierszy

Generuje liczby 1..n.

```sql
SELECT LEVEL FROM dual CONNECT BY LEVEL <= 12;
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
SELECT pseudo, ROUND(100 * przydzial_myszy / SUM(przydzial_myszy) OVER (PARTITION BY nr_bandy)) AS proc FROM kocury;
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

### 15.9 RATIO_TO_REPORT

Udział wiersza w sumie grupy (0–1).

```sql
SELECT pseudo, RATIO_TO_REPORT(przydzial_myszy) OVER (PARTITION BY nr_bandy) FROM kocury;
```

### 15.10 Top-N w grupie

Funkcji okna nie można używać w `WHERE`, więc filtruj w zapytaniu zewnętrznym.

```sql
SELECT * FROM (SELECT pseudo, nr_bandy, RANK() OVER (PARTITION BY nr_bandy ORDER BY przydzial_myszy DESC) AS rnk FROM kocury) WHERE rnk = 1;
```

## 16. Pivot

### 16.1 PIVOT

Zamienia wartości z wierszy na kolumny; źródło powinno zawierać tylko potrzebne kolumny.

```sql
SELECT * FROM (SELECT nr_bandy, plec, przydzial_myszy FROM kocury) PIVOT (SUM(przydzial_myszy) FOR plec IN ('M' AS samce, 'D' AS samice));
```

### 16.2 PIVOT z kilkoma agregatami

Tworzy kolumnę dla każdej pary wartość × agregat (`SAMCE_SUMA`, `SAMCE_ILE`, …).

```sql
SELECT * FROM (SELECT nr_bandy, plec, przydzial_myszy FROM kocury) PIVOT (SUM(przydzial_myszy) AS suma, COUNT(*) AS ile FOR plec IN ('M' AS samce, 'D' AS samice));
```

### 16.3 UNPIVOT

Zamienia kolumny na wiersze; pomija `NULL`-e, chyba że `INCLUDE NULLS`.

```sql
SELECT * FROM kocury UNPIVOT (ile FOR rodzaj IN (przydzial_myszy AS 'PRZYDZIAL', myszy_extra AS 'EXTRA'));
```

### 16.4 Pivot przez CASE

Przenośny pivot: jeden warunkowy agregat na kolumnę.

```sql
SELECT nr_bandy, SUM(CASE WHEN plec = 'M' THEN przydzial_myszy ELSE 0 END) AS samce, SUM(CASE WHEN plec = 'D' THEN przydzial_myszy ELSE 0 END) AS samice FROM kocury GROUP BY nr_bandy;
```

## 17. SQL*Plus

### 17.1 SET LINESIZE / PAGESIZE

Szersze linie i rzadziej powtarzane nagłówki.

```sql
SET LINESIZE 200
SET PAGESIZE 100
```

### 17.2 COLUMN FORMAT

Ustawia szerokość kolumny tekstowej.

```sql
COLUMN imie FORMAT A15
```

### 17.3 @file

Uruchamia plik ze skryptem.

```sql
@lista3/zad1.sql
```

### 17.4 /

Uruchamia ostatnią instrukcję jeszcze raz.

```sql
/
```

### 17.5 DESC

Pokazuje kolumny tabeli.

```sql
DESC kocury
```

## 18. Combosy

### 18.1 Drzewo ze źródłem z SELECT-a

Podzapytanie może być źródłem dla `CONNECT BY`; kot wycięty w źródle odcina też całą swoją gałąź (w przeciwieństwie do `WHERE`, które ukrywa tylko pojedyncze wiersze).

```sql
SELECT LPAD(' ', 4 * (LEVEL - 1)) || pseudo AS hierarchia, myszy
FROM (SELECT pseudo, szef, przydzial_myszy + NVL(myszy_extra, 0) AS myszy
      FROM kocury
      WHERE plec = 'M')
START WITH szef IS NULL
CONNECT BY PRIOR pseudo = szef;
```

### 18.2 Drzewo jako źródło + JOIN

Budujesz drzewo w podzapytaniu (`LEVEL` pod aliasem), potem złączasz i filtrujesz jak zwykłą tabelę.

```sql
SELECT d.poziom, d.pseudo, b.nazwa
FROM (SELECT LEVEL AS poziom, pseudo, nr_bandy
      FROM kocury
      START WITH szef IS NULL
      CONNECT BY PRIOR pseudo = szef) d
JOIN bandy b ON b.nr_bandy = d.nr_bandy
WHERE d.poziom >= 2
ORDER BY d.poziom, d.pseudo;
```

### 18.3 Drzewo + GROUP BY (wszyscy podwładni)

Bez `START WITH` każdy wiersz jest raz korzeniem; `CONNECT_BY_ROOT` + `LEVEL > 1` daje pary (szef, podwładny) do pogrupowania.

```sql
SELECT korzen, COUNT(*) AS podwladni, SUM(przydzial_myszy) AS myszy_podwladnych
FROM (SELECT CONNECT_BY_ROOT pseudo AS korzen, przydzial_myszy
      FROM kocury
      WHERE LEVEL > 1
      CONNECT BY PRIOR pseudo = szef)
GROUP BY korzen
ORDER BY podwladni DESC, korzen;
```

### 18.4 Drzewo z korzeniami z podzapytania

`START WITH` może używać podzapytania, np. start od szefów band.

```sql
SELECT LPAD(' ', 4 * (LEVEL - 1)) || pseudo AS hierarchia
FROM kocury
START WITH pseudo IN (SELECT szef_bandy FROM bandy WHERE teren = 'POLE')
CONNECT BY PRIOR pseudo = szef;
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

Podpowiedź: drzewo w podzapytaniu z `LEVEL AS poziom`, potem `GROUP BY poziom` na zewnątrz.

```sql
SELECT poziom, COUNT(*) AS ile, ROUND(AVG(przydzial_myszy), 1) AS srednio,
       LISTAGG(pseudo, ', ') WITHIN GROUP (ORDER BY pseudo) AS koty
FROM (SELECT LEVEL AS poziom, pseudo, przydzial_myszy
      FROM kocury
      START WITH pseudo = 'TYGRYS'
      CONNECT BY PRIOR pseudo = szef)
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

Podpowiedź: idziesz w górę (`PRIOR szef = pseudo`) od kotów z funkcją KOT, `WHERE szef IS NULL` zostawia wiersz na szczycie, `LEVEL - 1` = liczba szczebli.

```sql
SELECT CONNECT_BY_ROOT imie AS kot, LEVEL - 1 AS do_samej_gory
FROM kocury
WHERE szef IS NULL
START WITH funkcja = 'KOT'
CONNECT BY PRIOR szef = pseudo
ORDER BY do_samej_gory DESC, kot;
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

Podpowiedź: złączenie i filtr band idą do podzapytania-źródła, drzewo idzie po nim.

```sql
SELECT LPAD(' ', 4 * (LEVEL - 1)) || pseudo AS hierarchia, nazwa
FROM (SELECT k.pseudo, k.szef, b.nazwa
      FROM kocury k JOIN bandy b ON b.nr_bandy = k.nr_bandy
      WHERE b.teren IN ('POLE', 'CALOSC'))
START WITH szef IS NULL
CONNECT BY PRIOR pseudo = szef
ORDER SIBLINGS BY pseudo;
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
SELECT korzen, COUNT(*) AS podwladni, SUM(przydzial_myszy) AS myszy_podwladnych
FROM (SELECT CONNECT_BY_ROOT pseudo AS korzen, przydzial_myszy
      FROM kocury
      WHERE LEVEL > 1
      CONNECT BY PRIOR pseudo = szef)
GROUP BY korzen
ORDER BY podwladni DESC, korzen;
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

Podpowiedź: wszystko funkcjami okna. ±60 dni = `RANGE BETWEEN 60 PRECEDING AND 60 FOLLOWING` (na `DATE` liczby to dni) minus 1; poprzedni = `LAG(..., 1, 0)`; od początku roku = `COUNT(*)` narastająco z `PARTITION BY` rok; numer w bandzie = `ROW_NUMBER()` z `PARTITION BY` banda, rok.

```sql
SELECT wk.data_incydentu, wk.pseudo, k.nr_bandy,
       COUNT(*) OVER (ORDER BY wk.data_incydentu
                      RANGE BETWEEN 60 PRECEDING AND 60 FOLLOWING) - 1 AS incydenty_w_ok_60dni,
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

Podpowiedź: „wszystkie gatunki” = gatunki × lata przez `CROSS JOIN` (lata z `dual` + `UNION ALL`), potem `LEFT JOIN` do incydentów złączonych z wrogami w podzapytaniu. Pivot przez `COUNT/AVG(CASE …)`, `COALESCE(…, 0)` na brakujące średnie.

```sql
SELECT g.gatunek, r.rok,
       COUNT(CASE WHEN i.agresja BETWEEN 1 AND 4 THEN 1 END) AS niski_liczba,
       COUNT(CASE WHEN i.agresja BETWEEN 5 AND 8 THEN 1 END) AS sredni_liczba,
       COUNT(CASE WHEN i.agresja BETWEEN 9 AND 10 THEN 1 END) AS wysoki_liczba,
       COALESCE(ROUND(AVG(CASE WHEN i.agresja BETWEEN 1 AND 4 THEN i.agresja END), 2), 0) AS niski_srednia,
       COALESCE(ROUND(AVG(CASE WHEN i.agresja BETWEEN 5 AND 8 THEN i.agresja END), 2), 0) AS sredni_srednia,
       COALESCE(ROUND(AVG(CASE WHEN i.agresja BETWEEN 9 AND 10 THEN i.agresja END), 2), 0) AS wysoki_srednia
FROM (SELECT DISTINCT gatunek FROM wrogowie) g
CROSS JOIN (SELECT 2009 AS rok FROM dual UNION ALL SELECT 2010 FROM dual UNION ALL SELECT 2011 FROM dual) r
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

Podpowiedź: `SYS_CONNECT_BY_PATH` daje ścieżkę (`SUBSTR(…, 5)` ucina początkowe `' -> '`), a budżet to podzapytanie skorelowane z własnym drzewem startującym od danego kota.

```sql
SELECT k.imie, k.funkcja,
       SUBSTR(SYS_CONNECT_BY_PATH(k.pseudo, ' -> '), 5) AS sciezka,
       (SELECT SUM(p.przydzial_myszy + NVL(p.myszy_extra, 0))
        FROM kocury p
        START WITH p.pseudo = k.pseudo
        CONNECT BY PRIOR p.pseudo = p.szef) AS budzet_rodzinny
FROM kocury k
START WITH k.szef IS NULL
CONNECT BY PRIOR k.pseudo = k.szef
ORDER BY budzet_rodzinny DESC, k.imie;
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

### 20.5 Zadanie z listy – koty jedzące więcej niż inne koty tej samej funkcji i płci

Dla każdej bandy policz koty, które jedzą (przydział + extra) więcej niż średnia innych kotów o tej samej funkcji i płci.

Podpowiedź: podzapytanie skorelowane po `funkcja` i `plec`, z wykluczeniem samego siebie (`k2.pseudo <> k.pseudo`). Warunek idzie do `ON` w `LEFT JOIN`, żeby bandy z zerem też były w wyniku (pułapka 11.12). Kot bez „kolegi” w swojej funkcji i płci dostaje średnią `NULL`, więc nie przechodzi.

```sql
SELECT b.nazwa, COUNT(k.pseudo) AS koty_powyzej_sredniej
FROM bandy b
LEFT JOIN kocury k ON k.nr_bandy = b.nr_bandy
     AND k.przydzial_myszy + NVL(k.myszy_extra, 0) >
         (SELECT AVG(k2.przydzial_myszy + NVL(k2.myszy_extra, 0))
          FROM kocury k2
          WHERE k2.funkcja = k.funkcja
            AND k2.plec = k.plec
            AND k2.pseudo <> k.pseudo)
GROUP BY b.nr_bandy, b.nazwa
ORDER BY b.nr_bandy;
```

Wynik:

```text
     nazwa      | koty_powyzej_sredniej
-----------------+-----------------------
SZEFOSTWO       |                     2
CZARNI RYCERZE  |                     4
BIALI LOWCY     |                     1
LACIACI MYSLIWI |                     0
ROCKERSI        |                     0
```

Wersja z funkcjami okna – średnia innych = (suma w grupie − własne) / (liczba w grupie − 1), `NULLIF` chroni przed dzieleniem przez 0 (bez bandy z zerem w wyniku):

```sql
SELECT nr_bandy, COUNT(*) AS koty_powyzej_sredniej
FROM (SELECT nr_bandy,
             przydzial_myszy + NVL(myszy_extra, 0) AS myszy,
             (SUM(przydzial_myszy + NVL(myszy_extra, 0)) OVER (PARTITION BY funkcja, plec)
                - (przydzial_myszy + NVL(myszy_extra, 0)))
             / NULLIF(COUNT(*) OVER (PARTITION BY funkcja, plec) - 1, 0) AS srednia_innych
      FROM kocury) x
WHERE myszy > srednia_innych
GROUP BY nr_bandy
ORDER BY nr_bandy;
```
