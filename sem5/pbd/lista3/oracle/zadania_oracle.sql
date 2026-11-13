SET LINESIZE 200
SET PAGESIZE 100
SET TAB OFF
SET FEEDBACK OFF
COLUMN "Imiona kolejnych szefow" FORMAT A60
COLUMN "Droga sluzbowa" FORMAT A30

PROMPT ZAD. 4
SELECT b.nazwa "Nazwa bandy", COUNT(DISTINCT k.pseudo) "Koty z wrogami"
FROM Bandy b
JOIN Kocury k ON k.nr_bandy = b.nr_bandy
JOIN Wrogowie_kocurow wk ON wk.pseudo = k.pseudo
GROUP BY b.nr_bandy, b.nazwa
ORDER BY b.nr_bandy;

PROMPT ZAD. 5
-- case zostawia tylko czlowieka i psa (reszta null), jak sa 2 rozne to kot ma oba
SELECT k.funkcja "Funkcja", k.pseudo "Pseudonim kota", COUNT(*) "Liczba wrogow"
FROM Kocury k
JOIN Wrogowie_kocurow wk ON wk.pseudo = k.pseudo
JOIN Wrogowie w ON w.imie_wroga = wk.imie_wroga
GROUP BY k.pseudo, k.funkcja
HAVING COUNT(*) > 1
    OR COUNT(DISTINCT CASE WHEN w.gatunek IN ('CZLOWIEK', 'PIES') THEN w.gatunek END) = 2
ORDER BY k.pseudo;

PROMPT ZAD. 6a
SELECT b.nr_bandy "NR BANDY", b.nazwa "NAZWA", b.teren "TEREN"
FROM Bandy b
LEFT JOIN Kocury k ON k.nr_bandy = b.nr_bandy
WHERE k.pseudo IS NULL;

PROMPT ZAD. 6b
SELECT nr_bandy "NR BANDY", nazwa "NAZWA", teren "TEREN" FROM Bandy
MINUS
SELECT b.nr_bandy, b.nazwa, b.teren FROM Bandy b JOIN Kocury k ON k.nr_bandy = b.nr_bandy;

PROMPT ZAD. 7
-- szarak - nikt w bandzie nie je mniej (minimum bez MIN), przy remisie wchodza oba
SELECT pseudo "Pseudonim", przydzial_myszy "Zjada", nr_bandy "Banda", 'Prominent' "Typ",
       CASE WHEN przydzial_myszy > 60 THEN 'powyzej 60' ELSE 'ponizej 60' END "Ocena"
FROM Kocury
WHERE przydzial_myszy > (SELECT AVG(przydzial_myszy) FROM Kocury)
UNION ALL
SELECT k.pseudo, k.przydzial_myszy, k.nr_bandy, 'Szarak',
       CASE WHEN k.przydzial_myszy > 60 THEN 'powyzej 60' ELSE 'ponizej 60' END
FROM Kocury k
WHERE NOT EXISTS (SELECT 1 FROM Kocury m WHERE m.nr_bandy = k.nr_bandy AND m.przydzial_myszy < k.przydzial_myszy)
ORDER BY "Typ", "Banda", "Zjada" DESC;

PROMPT ZAD. 7 - nie sa niczyim szefem (NOT EXISTS)
SELECT k.pseudo "Nie sa niczyim szefem"
FROM Kocury k
WHERE NOT EXISTS (SELECT 1 FROM Kocury p WHERE p.szef = k.pseudo)
ORDER BY 1;

PROMPT ZAD. 7 - nie sa niczyim szefem (NOT IN)
-- 0 wierszy bo szef TYGRYSA to null, a x <> null daje unknown wiec nikt nie przechodzi
SELECT pseudo "Nie sa niczyim szefem"
FROM Kocury
WHERE pseudo NOT IN (SELECT szef FROM Kocury)
ORDER BY 1;

PROMPT ZAD. 8 - z wartoscia sredniej
-- nvl bo 50 + null = null, a avg pomija nulle i liczylby tylko koty z extra
SELECT nr_bandy "Lepsze bandy",
       ROUND(AVG(przydzial_myszy + NVL(myszy_extra, 0)), 1) "Sredni przydzial w bandzie",
       (SELECT ROUND(AVG(przydzial_myszy + NVL(myszy_extra, 0)), 1) FROM Kocury) "[Sredni przydzial]"
FROM Kocury
GROUP BY nr_bandy
HAVING AVG(przydzial_myszy + NVL(myszy_extra, 0))
     > (SELECT AVG(przydzial_myszy + NVL(myszy_extra, 0)) FROM Kocury)
ORDER BY nr_bandy;

PROMPT ZAD. 8 - bez wartosci sredniej
SELECT nr_bandy "Lepsze bandy",
       ROUND(AVG(przydzial_myszy + NVL(myszy_extra, 0)), 1) "Sredni przydzial w bandzie"
FROM Kocury
GROUP BY nr_bandy
HAVING AVG(przydzial_myszy + NVL(myszy_extra, 0))
     > (SELECT AVG(przydzial_myszy + NVL(myszy_extra, 0)) FROM Kocury)
ORDER BY nr_bandy;

PROMPT ZAD. 9
-- nls bo nazwy zaleza od jezyka sesji, nr miesiaca w group by zeby sortowac po kolei a nie alfabetycznie
SELECT TO_CHAR(w_stadku_od, 'FMMONTH', 'NLS_DATE_LANGUAGE=ENGLISH') "Miesiac", COUNT(*) "Liczba rekrutow"
FROM Kocury
GROUP BY EXTRACT(MONTH FROM w_stadku_od), TO_CHAR(w_stadku_od, 'FMMONTH', 'NLS_DATE_LANGUAGE=ENGLISH')
ORDER BY EXTRACT(MONTH FROM w_stadku_od);

PROMPT ZAD. 10
-- >= all zamiast max, CALOSC tez bo inaczej nie zgadza sie z przykladem
SELECT imie "IMIE", funkcja "FUNKCJA", przydzial_myszy "PRZYDZIAL MYSZY"
FROM Kocury
WHERE przydzial_myszy >= ALL (SELECT 3 * m.przydzial_myszy
                              FROM Kocury m JOIN Bandy b ON m.nr_bandy = b.nr_bandy
                              WHERE m.funkcja = 'MILUSIA' AND b.teren IN ('SAD', 'CALOSC'));

PROMPT ZAD. 11
-- where leci dopiero po zbudowaniu drzewa, dlatego ZERO ma poziom 3 mimo ze KURKA ukryta
SELECT LEVEL "Poziom", pseudo "Pseudonim", funkcja "Funkcja", nr_bandy "Nr bandy"
FROM Kocury
WHERE plec = 'M'
START WITH plec = 'M' AND funkcja = 'BANDZIOR'
CONNECT BY PRIOR pseudo = szef
ORDER SIBLINGS BY pseudo;

PROMPT ZAD. 12
-- warunki tylko w start with, szefow po drodze nikt nie sprawdza (TYGRYS ma extra a jest)
SELECT LPAD(' ', 4 * (LEVEL - 1)) || pseudo "Droga sluzbowa"
FROM Kocury
START WITH plec = 'M' AND NVL(myszy_extra, 0) = 0
       AND MONTHS_BETWEEN(DATE '2024-07-17', w_stadku_od) > 180
CONNECT BY PRIOR szef = pseudo
ORDER SIBLINGS BY pseudo;

PROMPT ZAD. 13a
SELECT k.imie "Imie", k.funkcja "Funkcja", s1.imie "Szef 1", s2.imie "Szef 2", s3.imie "Szef 3"
FROM Kocury k
LEFT JOIN Kocury s1 ON k.szef = s1.pseudo
LEFT JOIN Kocury s2 ON s1.szef = s2.pseudo
LEFT JOIN Kocury s3 ON s2.szef = s3.pseudo
WHERE k.funkcja IN ('KOT', 'MILUSIA');

PROMPT ZAD. 13b
SELECT CONNECT_BY_ROOT imie "Imie", CONNECT_BY_ROOT funkcja "Funkcja",
       SUBSTR(SYS_CONNECT_BY_PATH(imie, ' | '), LENGTH(CONNECT_BY_ROOT imie) + 4) "Imiona kolejnych szefow"
FROM Kocury
WHERE CONNECT_BY_ISLEAF = 1
START WITH funkcja IN ('KOT', 'MILUSIA')
CONNECT BY PRIOR szef = pseudo;

EXIT
