/*
ZADANIE 1 (krótkie) – funkcje wierszowe, daty, CASE, sortowanie

Wyświetl koty z band nr 2 i 4, które przystąpiły do stada w drugiej połowie
roku (czerwiec–grudzień, dowolnego roku). Dla każdego kota podaj:
- "Imie"        – imię zapisane z wielkiej litery, reszta małymi (np. Bolek),
- "Pseudo",
- "Banda"       – numer bandy,
- "Staz"        – liczba PEŁNYCH lat w stadzie na dzień 2012-01-01,
- "Myszy razem" – przydział myszy + myszy extra (NULL traktuj jako 0),
- "Status"      – 'GLODNY' gdy myszy razem < 50,
'NORMALNY' gdy 50..70,
'NAJEDZONY' gdy > 70.
Posortuj malejąco po stażu, a przy równym stażu alfabetycznie po pseudonimie.

Oczekiwany wynik:
Imie    | Pseudo | Banda | Staz | Myszy razem | Status
---------+--------+-------+------+-------------+-----------
Bolek   | LYSY   |     2 |    5 |          93 | NAJEDZONY
Pucek   | RAFA   |     4 |    5 |          65 | NORMALNY
Zuzia   | SZYBKA |     2 |    5 |          65 | NORMALNY
Mela    | DAMA   |     4 |    3 |          51 | NORMALNY
Ksawery | MAN    |     4 |    3 |          51 | NORMALNY
Jacek   | PLACEK |     2 |    3 |          67 | NORMALNY
Bari    | RURA   |     2 |    2 |          56 | NORMALNY
*/
SELECT
  INITCAP(imie) AS "Imie",
  pseudo AS "Pseudo",
  nr_bandy AS "Banda",
  TRUNC(MONTHS_BETWEEN(DATE '2012-01-01', w_stadku_od) / 12) AS "Staz",
  przydzial_myszy + NVL(myszy_extra, 0) AS "Myszy razem",
  CASE
    WHEN przydzial_myszy + NVL(myszy_extra, 0) < 50 THEN 'GLODNY'
    WHEN przydzial_myszy + NVL(myszy_extra, 0) <= 70 THEN 'NORMALNY'
    ELSE 'NAJEDZONY'
  END AS "Status"
FROM
  kocury
WHERE
  nr_bandy IN (2, 4)
  AND EXTRACT(MONTH FROM w_stadku_od) >= 6
ORDER BY
  "Staz" DESC,
  pseudo;
