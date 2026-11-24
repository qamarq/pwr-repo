/*
ZADANIE 9 – NOT EXISTS, podzapytanie skorelowane

Wypisać koty, które jako jedyne ze swojej bandy miały incydent z danym gatunkiem
wroga.

Oczekiwany wynik:
Imie    | Gatunek
---------+----------
DUDEK   | LIS
JACEK   | KOGUT
KOREK   | CZLOWIEK
KSAWERY | PIES
PUNIA   | PIES
RUDA    | LIS
SONIA   | SOSNA
*/
SELECT DISTINCT k.imie AS "Imie", w.gatunek AS "Gatunek"
FROM kocury k
JOIN wrogowie_kocurow wk ON wk.pseudo = k.pseudo
JOIN wrogowie w ON w.imie_wroga = wk.imie_wroga
WHERE NOT EXISTS (SELECT 1
                  FROM kocury k2
                  JOIN wrogowie_kocurow wk2 ON wk2.pseudo = k2.pseudo
                  JOIN wrogowie w2 ON w2.imie_wroga = wk2.imie_wroga
                  WHERE k2.nr_bandy = k.nr_bandy
                    AND k2.pseudo <> k.pseudo
                    AND w2.gatunek = w.gatunek)
ORDER BY 1, 2;
