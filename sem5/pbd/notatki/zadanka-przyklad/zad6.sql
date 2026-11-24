/*
Wypisz imiona kotów oraz gatunek wroga (np. 'CZLOWIEK', 'PIES', 'LIS'), jeśli
dany kot miał incydent z wrogiem tego gatunku, a **żaden inny kot z tej samej
bandy** nie miał incydentu z wrogiem tego samego gatunku.

Struktura wyniku:

Imie, Gatunek
*/
SELECT DISTINCT
  K.imie AS "Imie",
  W.gatunek AS "Gatunek"
FROM
  kocury K
  JOIN wrogowie_kocurow WK ON K.pseudo = WK.pseudo
  JOIN wrogowie W ON WK.imie_wroga = W.imie_wroga
WHERE
  NOT EXISTS (
    SELECT
      1
    FROM
      kocury K2
      JOIN wrogowie_kocurow WK2 ON K2.pseudo = WK2.pseudo
      JOIN wrogowie W2 ON WK2.imie_wroga = W2.imie_wroga
    WHERE
      K2.nr_bandy = K.nr_bandy
      AND K2.pseudo <> K.pseudo
      AND W2.gatunek = W.gatunek
  )
ORDER BY
  1,
  2;
