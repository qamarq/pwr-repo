SET DateStyle = 'ISO';

\echo ZAD. 1
SELECT DISTINCT k.imie "W stadzie przed szefem lub bez incydentu",
       CASE WHEN k.w_stadku_od < j.w_stadku_od THEN 'TAK' ELSE 'NIE' END "Przed JACKIEM"
FROM Kocury k
LEFT JOIN Kocury s ON k.szef = s.pseudo
LEFT JOIN Wrogowie_kocurow wk ON wk.pseudo = k.pseudo
JOIN Kocury j ON j.imie = 'JACEK'
WHERE wk.pseudo IS NULL OR (k.w_stadku_od < s.w_stadku_od AND wk.imie_wroga LIKE '%A')
ORDER BY 1;

\echo ZAD. 2
SELECT COALESCE(s.pseudo, 'Brak przelozonego') "Przelozony",
       COALESCE(p.pseudo, 'Brak podwladnego') "Podwladny"
FROM Kocury p
FULL JOIN Kocury s ON p.szef = s.pseudo
WHERE COALESCE(p.plec, 'M') = 'M' AND COALESCE(s.plec, 'M') = 'M'
ORDER BY 1, 2;

\echo ZAD. 3
SELECT k.pseudo "PSEUDO", k.przydzial_myszy "PRZYDZIAL_MYSZY",
       ROUND(SUM(m.przydzial_myszy) / COUNT(DISTINCT w.imie_wroga)) "SUM_W_BANDZIE",
       ROUND(100 * k.przydzial_myszy * COUNT(DISTINCT w.imie_wroga) / SUM(m.przydzial_myszy)) "PROC_W_BANDZIE"
FROM Kocury k
JOIN Bandy b ON k.nr_bandy = b.nr_bandy
JOIN Wrogowie_kocurow wk ON wk.pseudo = k.pseudo
JOIN Wrogowie w ON w.imie_wroga = wk.imie_wroga
JOIN Kocury m ON m.nr_bandy = k.nr_bandy
WHERE b.teren IN ('POLE', 'CALOSC') AND w.stopien_wrogosci BETWEEN 6 AND 10
GROUP BY k.pseudo, k.przydzial_myszy, k.nr_bandy
ORDER BY k.nr_bandy, k.przydzial_myszy;

\echo ZAD. 4
SELECT b.nazwa "Nazwa bandy", COUNT(DISTINCT k.pseudo) "Koty z wrogami"
FROM Bandy b
JOIN Kocury k ON k.nr_bandy = b.nr_bandy
JOIN Wrogowie_kocurow wk ON wk.pseudo = k.pseudo
GROUP BY b.nr_bandy, b.nazwa
ORDER BY b.nr_bandy;

\echo ZAD. 5
SELECT k.funkcja "Funkcja", k.pseudo "Pseudonim kota", COUNT(*) "Liczba wrogow"
FROM Kocury k
JOIN Wrogowie_kocurow wk ON wk.pseudo = k.pseudo
JOIN Wrogowie w ON w.imie_wroga = wk.imie_wroga
GROUP BY k.pseudo, k.funkcja
HAVING COUNT(*) > 1
    OR COUNT(DISTINCT CASE WHEN w.gatunek IN ('CZLOWIEK', 'PIES') THEN w.gatunek END) = 2
ORDER BY k.pseudo;

\echo ZAD. 6a
SELECT b.nr_bandy "NR BANDY", b.nazwa "NAZWA", b.teren "TEREN"
FROM Bandy b
LEFT JOIN Kocury k ON k.nr_bandy = b.nr_bandy
WHERE k.pseudo IS NULL;

\echo ZAD. 6b
SELECT nr_bandy "NR BANDY", nazwa "NAZWA", teren "TEREN" FROM Bandy
EXCEPT
SELECT b.nr_bandy, b.nazwa, b.teren FROM Bandy b JOIN Kocury k ON k.nr_bandy = b.nr_bandy;

\echo ZAD. 7
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

\echo ZAD. 7 - nie sa niczyim szefem (NOT EXISTS)
SELECT k.pseudo "Nie sa niczyim szefem"
FROM Kocury k
WHERE NOT EXISTS (SELECT 1 FROM Kocury p WHERE p.szef = k.pseudo)
ORDER BY 1;

\echo ZAD. 7 - nie sa niczyim szefem (NOT IN)
SELECT pseudo "Nie sa niczyim szefem"
FROM Kocury
WHERE pseudo NOT IN (SELECT szef FROM Kocury)
ORDER BY 1;

\echo ZAD. 8 - z wartoscia sredniej
SELECT nr_bandy "Lepsze bandy",
       ROUND(AVG(przydzial_myszy + COALESCE(myszy_extra, 0)), 1) "Sredni przydzial w bandzie",
       (SELECT ROUND(AVG(przydzial_myszy + COALESCE(myszy_extra, 0)), 1) FROM Kocury) "[Sredni przydzial]"
FROM Kocury
GROUP BY nr_bandy
HAVING AVG(przydzial_myszy + COALESCE(myszy_extra, 0))
     > (SELECT AVG(przydzial_myszy + COALESCE(myszy_extra, 0)) FROM Kocury)
ORDER BY nr_bandy;

\echo ZAD. 8 - bez wartosci sredniej
SELECT nr_bandy "Lepsze bandy",
       ROUND(AVG(przydzial_myszy + COALESCE(myszy_extra, 0)), 1) "Sredni przydzial w bandzie"
FROM Kocury
GROUP BY nr_bandy
HAVING AVG(przydzial_myszy + COALESCE(myszy_extra, 0))
     > (SELECT AVG(przydzial_myszy + COALESCE(myszy_extra, 0)) FROM Kocury)
ORDER BY nr_bandy;

\echo ZAD. 9
SELECT TO_CHAR(w_stadku_od, 'FMMONTH') "Miesiac", COUNT(*) "Liczba rekrutow"
FROM Kocury
GROUP BY EXTRACT(MONTH FROM w_stadku_od), TO_CHAR(w_stadku_od, 'FMMONTH')
ORDER BY EXTRACT(MONTH FROM w_stadku_od);

\echo ZAD. 10
SELECT imie "IMIE", funkcja "FUNKCJA", przydzial_myszy "PRZYDZIAL MYSZY"
FROM Kocury
WHERE przydzial_myszy >= ALL (SELECT 3 * m.przydzial_myszy
                              FROM Kocury m JOIN Bandy b ON m.nr_bandy = b.nr_bandy
                              WHERE m.funkcja = 'MILUSIA' AND b.teren IN ('SAD', 'CALOSC'));
