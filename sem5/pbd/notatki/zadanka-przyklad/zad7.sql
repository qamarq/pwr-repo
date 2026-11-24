/*
ZADANIE 7 – podzapytanie skorelowane, GROUP BY + HAVING

Wypisać bandy, w których występują co najmniej dwa koty ponadprzeciętne, czyli
takie, których całkowity przydział myszy (przydział + extra, NULL = 0) jest
większy niż średni przydział całkowity kotów o tej samej płci i funkcji.

Oczekiwany wynik:
Banda          | Ponadprzecietne
----------------+-----------------
SZEFOSTWO      |               2
CZARNI RYCERZE |               4
*/
SELECT b.nazwa AS "Banda", COUNT(*) AS "Ponadprzecietne"
FROM kocury k
JOIN bandy b ON b.nr_bandy = k.nr_bandy
WHERE k.przydzial_myszy + NVL(k.myszy_extra, 0) >
      (SELECT AVG(k2.przydzial_myszy + NVL(k2.myszy_extra, 0))
       FROM kocury k2
       WHERE k2.plec = k.plec AND k2.funkcja = k.funkcja)
GROUP BY b.nr_bandy, b.nazwa
HAVING COUNT(*) >= 2
ORDER BY b.nr_bandy;
