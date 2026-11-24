/*
ZADANIE 4 (duże) – podzapytania, funkcje okna, złączenia

Rozważ tylko koty z band liczących co najmniej 4 koty (warunek zrealizuj
podzapytaniem, nie wpisuj numerów band na sztywno). Dla każdego kota wyświetl:
- "Banda"             – nazwa bandy,
- "Pseudo",
- "Funkcja",
- "Myszy"             – całkowite spożycie (przydział + extra, NULL = 0),
- "Srednia w bandzie" – średnie całkowite spożycie w bandzie kota
(zaokrąglone do 2 miejsc),
- "Miejsce"           – miejsce kota w bandzie wg spożycia malejąco; koty
z równym spożyciem mają to samo miejsce, a kolejne
miejsce jest pomijane (1, 2, 2, 4, ...),
- "Podwladni"         – liczba BEZPOŚREDNICH podwładnych kota,
- "Wrogowie"          – liczba wrogów kota,
- "Max wrogosc"       – najwyższy stopień wrogości wśród wrogów kota,
a gdy kot nie ma wrogów – '-',
- "Widelki"           – 'PONIZEJ' / 'POWYZEJ' / 'OK' w zależności od tego,
czy całkowite spożycie kota mieści się w przedziale
min_myszy..max_myszy jego funkcji.
Posortuj po nazwie bandy, miejscu, a następnie pseudonimie.


Oczekiwany wynik:
Banda           | Pseudo   | Funkcja  | Myszy | Srednia w bandzie | Miejsce | Podwladni | Wrogowie | Max wrogosc | Widelki
-----------------+----------+----------+-------+-------------------+---------+-----------+----------+-------------+---------
BIALI LOWCY     | ZOMBI    | BANDZIOR |    88 |             61.75 |       1 |         2 |        1 | 7           | OK
BIALI LOWCY     | KURKA    | LOWCZY   |    61 |             61.75 |       2 |         1 |        1 | 4           | OK
BIALI LOWCY     | PUSZYSTA | MILUSIA  |    55 |             61.75 |       3 |         0 |        1 | 1           | POWYZEJ
BIALI LOWCY     | ZERO     | KOT      |    43 |             61.75 |       4 |         0 |        0 | -           | OK
CZARNI RYCERZE  | LYSY     | BANDZIOR |    93 |             66.60 |       1 |         4 |        1 | 1           | POWYZEJ
CZARNI RYCERZE  | PLACEK   | LOWCZY   |    67 |             66.60 |       2 |         0 |        1 | 3           | OK
CZARNI RYCERZE  | SZYBKA   | LOWCZY   |    65 |             66.60 |       3 |         0 |        1 | 1           | OK
CZARNI RYCERZE  | RURA     | LAPACZ   |    56 |             66.60 |       4 |         0 |        1 | 10          | OK
CZARNI RYCERZE  | LASKA    | MILUSIA  |    52 |             66.60 |       5 |         0 |        2 | 10          | POWYZEJ
LACIACI MYSLIWI | RAFA     | LOWCZY   |    65 |             49.40 |       1 |         4 |        0 | -           | OK
LACIACI MYSLIWI | DAMA     | LAPACZ   |    51 |             49.40 |       2 |         0 |        1 | 10          | OK
LACIACI MYSLIWI | MAN      | LAPACZ   |    51 |             49.40 |       2 |         0 |        1 | 2           | OK
LACIACI MYSLIWI | MALY     | KOT      |    40 |             49.40 |       4 |         0 |        1 | 5           | OK
LACIACI MYSLIWI | UCHO     | KOT      |    40 |             49.40 |       4 |         0 |        1 | 7           | OK
SZEFOSTWO       | TYGRYS   | SZEFUNIO |   136 |             80.50 |       1 |         6 |        2 | 10          | POWYZEJ
SZEFOSTWO       | LOLA     | MILUSIA  |    72 |             80.50 |       2 |         0 |        0 | -           | POWYZEJ
SZEFOSTWO       | MALA     | MILUSIA  |    64 |             80.50 |       3 |         0 |        1 | 5           | POWYZEJ
SZEFOSTWO       | BOLEK    | DZIELCZY |    50 |             80.50 |       4 |         0 |        2 | 10          | OK

Dodatkowo (bez funkcji okna): rozwiąż "Srednia w bandzie" i "Miejsce"
drugi raz, używając wyłącznie podzapytań skorelowanych.
*/
SELECT
  B.nazwa,
  K.pseudo,
  AVG(K.przydzial_myszy) OVER (
    PARTITION BY
      K.nr_bandy
  ) AS sredniawbandzieprzydzialu,
  RANK() OVER (
    PARTITION BY
      K.nr_bandy
    ORDER BY
      przydzial_myszy DESC
  ) AS miejscewgprzydzialu
FROM
  kocury K
  JOIN bandy B ON B.nr_bandy = K.nr_bandy
WHERE
  K.nr_bandy in (
    SELECT
      K2.nr_bandy
    FROM
      kocury K2
      JOIN bandy B2 ON B2.nr_bandy = K2.nr_bandy
    GROUP BY
      K2.nr_bandy
    HAVING
      COUNT(*) >= 4
  )
ORDER BY
  B.nazwa;