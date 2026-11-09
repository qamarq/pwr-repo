SET LINESIZE 200
SET PAGESIZE 100
SET TAB OFF
COLUMN "Zjada rocznie" FORMAT A15
COLUMN "Unikalnosc atr. PSEUDO" FORMAT A24
COLUMN "Unikalnosc atr. SZEF" FORMAT A24

PROMPT ZAD. 8
SELECT imie "IMIE",
       DECODE(SIGN(12 * (przydzial_myszy + NVL(myszy_extra, 0)) - 660),
              1, TO_CHAR(12 * (przydzial_myszy + NVL(myszy_extra, 0))),
              0, 'Limit',
              'Ponizej 660') "Zjada rocznie"
FROM Kocury
ORDER BY imie;

PROMPT ZAD. 9
SELECT LPAD(pseudo, 10) || ' - ' || DECODE(COUNT(*), 1, 'Unikalny', 'nieunikalny') "Unikalnosc atr. PSEUDO"
FROM Kocury
GROUP BY pseudo
ORDER BY pseudo;

SELECT LPAD(szef, 10) || ' - ' || DECODE(COUNT(*), 1, 'Unikalny', 'nieunikalny') "Unikalnosc atr. SZEF"
FROM Kocury
WHERE szef IS NOT NULL
GROUP BY szef
ORDER BY szef;

PROMPT ZAD. 10
SELECT pseudo "Pseudonim", COUNT(*) "Liczba wrogow"
FROM Wrogowie_kocurow
GROUP BY pseudo
HAVING COUNT(*) >= 2
ORDER BY pseudo;

EXIT
