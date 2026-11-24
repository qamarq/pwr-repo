/*
ZADANIE 5 (duże) – hierarchia, droga służbowa

Dla każdego kota pełniącego funkcję KOT lub MILUSIA wyświetl jego drogę
służbową aż do kota, który nie ma szefa:
- "Kot"             – pseudonim kota,
- "Liczba szczebli" – ilu szefów jest nad kotem,
- "Droga sluzbowa"  – pseudonimy od kota do samej góry, oddzielone ' -> '.
Posortuj malejąco po liczbie szczebli, a następnie po pseudonimie.

Oracle: CONNECT BY (+ SYS_CONNECT_BY_PATH / CONNECT_BY_ISLEAF lub LEVEL),
Postgres: WITH RECURSIVE.

Oczekiwany wynik:
Kot      | Liczba szczebli | Droga sluzbowa
----------+-----------------+----------------------------------
ZERO     |               3 | ZERO -> KURKA -> ZOMBI -> TYGRYS
LASKA    |               2 | LASKA -> LYSY -> TYGRYS
MALY     |               2 | MALY -> RAFA -> TYGRYS
PUSZYSTA |               2 | PUSZYSTA -> ZOMBI -> TYGRYS
UCHO     |               2 | UCHO -> RAFA -> TYGRYS
LOLA     |               1 | LOLA -> TYGRYS
MALA     |               1 | MALA -> TYGRYS
*/
SELECT
  CONNECT_BY_ROOT pseudo,
  LEVEL - 1,
  SYS_CONNECT_BY_PATH (pseudo, ' -> ') AS DROGA
FROM
  kocury
WHERE
  szef IS NULL START
WITH
  funkcja in ('KOT', 'MILUSIA') CONNECT BY pseudo = PRIOR szef
ORDER BY
  LEVEL DESC,
  pseudo;