/*
ZADANIE 3 (krótkie) – złączenia zewnętrzne, samozłączenie, agregacja po złączeniu

Dla KAŻDEJ bandy (również takiej, która nie ma kotów ani szefa) wyświetl:
- "Banda"        – nazwa bandy,
- "Szef bandy"   – IMIĘ (nie pseudonim) szefa bandy, a gdy go brak – 'BRAK',
- "Liczba kotow" – liczba kotów należących do bandy,
- "Bez wrogow"   – liczba kotów z bandy, które nie mają żadnego wroga,
- "Incydenty"    – łączna liczba incydentów kotów z bandy.
Posortuj po numerze bandy.

Uwaga: uważaj na to, jak złączenie z wrogowie_kocurow mnoży wiersze
i jak to wpływa na COUNT.

Oczekiwany wynik:
Banda           | Szef bandy | Liczba kotow | Bez wrogow | Incydenty
-----------------+------------+--------------+------------+-----------
SZEFOSTWO       | MRUCZEK    |            4 |          1 |         5
CZARNI RYCERZE  | BOLEK      |            5 |          0 |         6
BIALI LOWCY     | KOREK      |            4 |          1 |         3
LACIACI MYSLIWI | PUCEK      |            5 |          1 |         4
ROCKERSI        | BRAK       |            0 |          0 |         0
*/
SELECT
  B.nazwa AS "Banda",
  NVL(S.imie, 'BRAK') AS "Szef bandy",
  COUNT(DISTINCT K.pseudo) AS "Liczba kotow",
  SUM(
    CASE
      WHEN WK.pseudo IS NULL
      AND K.pseudo IS NOT NULL THEN 1
      ELSE 0
    END
  ) AS "Bez wrogow",
  COUNT(WK.pseudo) AS "Incydenty"
FROM
  kocury K
  RIGHT JOIN bandy B ON B.nr_bandy = K.nr_bandy
  LEFT JOIN kocury S ON B.szef_bandy = S.pseudo
  LEFT JOIN wrogowie_kocurow WK ON K.pseudo = WK.pseudo
GROUP BY
  B.nazwa,
  B.nr_bandy,
  S.imie
ORDER BY
  B.nr_bandy;
