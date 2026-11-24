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
  INITCAP (imie) as Imie,
  pseudo,
  w_stadku_od,
  nr_bandy,
  2012 - 1 - EXTRACT(
    YEAR
    FROM
      w_stadku_od
  ) AS staz,
  COALESCE(przydzial_myszy, 0) + COALESCE(myszy_extra, 0) AS Myszy_razem,
  CASE
    WHEN (
      COALESCE(przydzial_myszy, 0) + COALESCE(myszy_extra, 0)
    ) > 70 THEN 'NAJEDZONY'
    WHEN COALESCE(przydzial_myszy, 0) + COALESCE(myszy_extra, 0) < 50 THEN 'glodny'
    ELSE 'normanly'
  END AS "status"
FROM
  kocury
WHERE
  nr_bandy IN (2, 4)
  AND EXTRACT(
    MONTH
    FROM
      w_stadku_od
  ) >= 6
ORDER BY
  staz DESC,
  pseudo;