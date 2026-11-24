/*
ZADANIE 8 – hierarchia, drzewo + GROUP BY

Wypisać drzewa z hierarchii kotów (kot + wszyscy jego bezpośredni i pośredni
podwładni), które mają co najmniej dwa koty, a suma ich przydziałów myszy jest
większa niż 200.

Oczekiwany wynik:
Korzen | Liczba kotow | Suma przydzialow
--------+--------------+------------------
TYGRYS |           18 |              930
LYSY   |            5 |              284
RAFA   |            5 |              247
*/
SELECT korzen AS "Korzen", COUNT(*) AS "Liczba kotow", SUM(przydzial_myszy) AS "Suma przydzialow"
FROM (SELECT CONNECT_BY_ROOT pseudo AS korzen, przydzial_myszy
      FROM kocury
      CONNECT BY PRIOR pseudo = szef)
GROUP BY korzen
HAVING COUNT(*) >= 2 AND SUM(przydzial_myszy) > 200
ORDER BY 3 DESC;

-- postgres
WITH RECURSIVE pod AS (
    SELECT pseudo AS korzen, pseudo, przydzial_myszy FROM kocury
    UNION ALL
    SELECT p.korzen, k.pseudo, k.przydzial_myszy FROM kocury k JOIN pod p ON k.szef = p.pseudo
)
SELECT korzen AS "Korzen", COUNT(*) AS "Liczba kotow", SUM(przydzial_myszy) AS "Suma przydzialow"
FROM pod
GROUP BY korzen
HAVING COUNT(*) >= 2 AND SUM(przydzial_myszy) > 200
ORDER BY 3 DESC;
