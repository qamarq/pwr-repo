/*
ZADANIE 2 (krótkie) – grupowanie, funkcje agregujące, HAVING

Dla każdej funkcji (z pominięciem SZEFUNIO) wyświetl:
- "Funkcja",
- "Liczba kotow",
- "Liczba kotek"  – ile kotów pełniących tę funkcję to kotki (plec = 'D'),
- "Z extra"       – ile kotów ma przydzielone myszy extra (wartość różna od NULL),
- "Srednio"       – średnie całkowite spożycie (przydział + extra, NULL = 0)
zaokrąglone do 2 miejsc po przecinku,
- "Rozpietosc"    – różnica między największym a najmniejszym całkowitym
spożyciem w ramach funkcji.
Pokaż tylko funkcje pełnione przez co najmniej 3 koty.
Posortuj malejąco po średnim spożyciu.

Uwaga: "Liczba kotek" i "Z extra" policz w jednym zapytaniu, bez podzapytań.

Oczekiwany wynik:
Funkcja | Liczba kotow | Liczba kotek | Z extra | Srednio | Rozpietosc
---------+--------------+--------------+---------+---------+------------
LOWCZY  |            4 |            2 |       0 |   64.50 |          6
MILUSIA |            4 |            4 |       4 |   60.75 |         20
LAPACZ  |            3 |            1 |       0 |   52.67 |          5
KOT     |            3 |            1 |       0 |   41.00 |          3
*/
SELECT
  funkcja AS "Funkcja",
  COUNT(*) AS "Liczba kotow",
  COUNT(CASE WHEN plec = 'D' THEN 1 END) AS "Liczba kotek",
  COUNT(myszy_extra) AS "Z extra",
  ROUND(AVG(przydzial_myszy + NVL(myszy_extra, 0)), 2) AS "Srednio",
  MAX(przydzial_myszy + NVL(myszy_extra, 0)) - MIN(przydzial_myszy + NVL(myszy_extra, 0)) AS "Rozpietosc"
FROM
  kocury
WHERE
  funkcja <> 'SZEFUNIO'
GROUP BY
  funkcja
HAVING
  COUNT(*) >= 3
ORDER BY
  "Srednio" DESC;
