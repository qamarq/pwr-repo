SET DateStyle = 'ISO';

\echo ZAD. 1
SELECT imie_wroga "WROG", opis_incydentu "PRZEWINA"
FROM Wrogowie_kocurow
WHERE EXTRACT(YEAR FROM data_incydentu) = 2009;

\echo ZAD. 2
SELECT imie "IMIE", funkcja "FUNKCJA", w_stadku_od "Z NAMI OD"
FROM Kocury
WHERE plec = 'D' AND w_stadku_od BETWEEN '2005-09-01' AND '2007-07-31';

\echo ZAD. 3
SELECT imie_wroga "WROG", gatunek "GATUNEK", stopien_wrogosci "STOPIEN WROGOSCI"
FROM Wrogowie
WHERE lapowka IS NULL
ORDER BY stopien_wrogosci;

\echo ZAD. 4
SELECT imie || ' zwany ' || pseudo || ' (fun. ' || funkcja || ') lowi myszki w bandzie '
       || nr_bandy || ' od ' || w_stadku_od "WSZYSTKO O KOCURACH"
FROM Kocury
WHERE plec = 'M'
ORDER BY w_stadku_od DESC, pseudo;

\echo ZAD. 5
SELECT pseudo "PSEUDO", STRPOS(pseudo, 'A') "A", STRPOS(pseudo, 'L') "L",
       REGEXP_REPLACE(REGEXP_REPLACE(pseudo, 'A', '#'), 'L', '%') "Po wymianie A na # oraz L na %"
FROM Kocury
WHERE pseudo LIKE '%A%' AND pseudo LIKE '%L%'
ORDER BY pseudo;

\echo ZAD. 6
SELECT imie "IMIE", w_stadku_od "W stadku", ROUND(przydzial_myszy / 1.1) "Zjadal",
       (w_stadku_od + INTERVAL '6 months')::DATE "Podwyzka", przydzial_myszy "Zjada"
FROM Kocury
WHERE AGE('2024-07-17', w_stadku_od) >= INTERVAL '15 years'
  AND EXTRACT(MONTH FROM w_stadku_od) BETWEEN 3 AND 9
ORDER BY przydzial_myszy DESC;

\echo ZAD. 7
SELECT imie "IMIE", 3 * przydzial_myszy "MYSZY KWARTALNIE", 3 * COALESCE(myszy_extra, 0) "KWARTALNE DODATKI"
FROM Kocury
WHERE przydzial_myszy > 2 * COALESCE(myszy_extra, 0) AND przydzial_myszy >= 55
ORDER BY przydzial_myszy DESC, imie;

\echo ZAD. 8
SELECT imie "IMIE",
       CASE SIGN(12 * (przydzial_myszy + COALESCE(myszy_extra, 0)) - 660)
           WHEN 1 THEN (12 * (przydzial_myszy + COALESCE(myszy_extra, 0)))::TEXT
           WHEN 0 THEN 'Limit'
           ELSE 'Ponizej 660'
       END "Zjada rocznie"
FROM Kocury
ORDER BY imie;

\echo ZAD. 9
SELECT LPAD(pseudo, 10) || ' - ' || CASE COUNT(*) WHEN 1 THEN 'Unikalny' ELSE 'nieunikalny' END "Unikalnosc atr. PSEUDO"
FROM Kocury
GROUP BY pseudo
ORDER BY pseudo;

SELECT LPAD(szef, 10) || ' - ' || CASE COUNT(*) WHEN 1 THEN 'Unikalny' ELSE 'nieunikalny' END "Unikalnosc atr. SZEF"
FROM Kocury
WHERE szef IS NOT NULL
GROUP BY szef
ORDER BY szef;

\echo ZAD. 10
SELECT pseudo "Pseudonim", COUNT(*) "Liczba wrogow"
FROM Wrogowie_kocurow
GROUP BY pseudo
HAVING COUNT(*) >= 2
ORDER BY pseudo;

\echo ZAD. 11 (29 pazdziernik)
SELECT pseudo "PSEUDO", w_stadku_od "W STADKU",
       DATE_BIN('7 days', DATE_TRUNC('month', DATE '2024-10-29'
           + CASE WHEN EXTRACT(DAY FROM w_stadku_od) > 15
                    OR DATE_BIN('7 days', DATE_TRUNC('month', DATE '2024-10-29') + INTERVAL '1 month - 1 day', '2000-01-05') < '2024-10-29'
                  THEN INTERVAL '1 month' ELSE INTERVAL '0' END)
           + INTERVAL '1 month - 1 day', '2000-01-05')::DATE "WYPLATA"
FROM Kocury
ORDER BY w_stadku_od;

\echo ZAD. 11 (31 pazdziernik)
SELECT pseudo "PSEUDO", w_stadku_od "W STADKU",
       DATE_BIN('7 days', DATE_TRUNC('month', DATE '2024-10-31'
           + CASE WHEN EXTRACT(DAY FROM w_stadku_od) > 15
                    OR DATE_BIN('7 days', DATE_TRUNC('month', DATE '2024-10-31') + INTERVAL '1 month - 1 day', '2000-01-05') < '2024-10-31'
                  THEN INTERVAL '1 month' ELSE INTERVAL '0' END)
           + INTERVAL '1 month - 1 day', '2000-01-05')::DATE "WYPLATA"
FROM Kocury
ORDER BY w_stadku_od;

\echo ZAD. 12
SELECT 'Liczba kotow=' " ", COUNT(*) " ", 'lowi jako' " ", funkcja " ", 'i zjada max.' " ",
       TO_CHAR(MAX(przydzial_myszy + COALESCE(myszy_extra, 0)), '99.99') " ", 'myszy miesiecznie' " "
FROM Kocury
WHERE plec = 'D' AND funkcja <> 'SZEFUNIO'
GROUP BY funkcja
HAVING AVG(przydzial_myszy + COALESCE(myszy_extra, 0)) > 50
ORDER BY funkcja;

\echo ZAD. 13
SELECT nr_bandy "Nr bandy", plec "Plec", MIN(przydzial_myszy) "Minimalny przydzial"
FROM Kocury
GROUP BY nr_bandy, plec;
