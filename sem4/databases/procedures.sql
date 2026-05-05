-- ============================================================
-- Bazy danych – Lista 2 – Procedury MySQL
-- Zróżnicowane typy: parametryczne, JOIN, LEFT JOIN, UNION, agregacje
-- ============================================================

SET NAMES utf8mb4;
DELIMITER ;;

-- ============ L2z01 ============
-- Uczniowie z klas I-III (filtr LIKE, brak parametru)
DROP PROCEDURE IF EXISTS `L2z01`;;
CREATE PROCEDURE `L2z01`()
BEGIN
    SELECT
        u.`Idu`       AS `Numer`,
        u.`Nazwisko`,
        u.`Imie`,
        u.`KlasaU`    AS `Klasa`,
        m.`NazwaM`    AS `Miasto`
    FROM `Uczniowie` u
    LEFT JOIN `Miasta` m ON u.`Miasto` = m.`IdM`
    WHERE u.`KlasaU` LIKE 'I_'
       OR u.`KlasaU` LIKE 'II_'
       OR u.`KlasaU` LIKE 'III_'
    ORDER BY u.`Nazwisko`, u.`Imie`;
END;;

-- ============ L2z03 ============
-- Uczniowie z klasy podanej jako parametr (INNER JOIN, parametr IN)
-- Użycie: CALL L2z03('Ia');
DROP PROCEDURE IF EXISTS `L2z03`;;
CREATE PROCEDURE `L2z03`(IN p_Klasa VARCHAR(6))
BEGIN
    SELECT
        u.`Idu`        AS `Numer`,
        u.`Nazwisko`,
        u.`Imie`,
        DATE(u.`DUr`)  AS `Data urodzenia`,
        u.`Plec`,
        u.`KlasaU`     AS `Klasa`,
        m.`NazwaM`     AS `Miasto`,
        u.`Email`
    FROM `Uczniowie` u
    INNER JOIN `Miasta` m ON u.`Miasto` = m.`IdM`
    WHERE u.`KlasaU` = p_Klasa
    ORDER BY u.`Nazwisko`, u.`Imie`;
END;;

-- ============ L2z08 ============
-- Oceny słownie: CONCAT, CASE WHEN, INNER JOIN trzech tabel
DROP PROCEDURE IF EXISTS `L2z08`;;
CREATE PROCEDURE `L2z08`()
BEGIN
    SELECT
        CONCAT(u.`Nazwisko`, ' ', u.`Imie`, ' – ', u.`Idu`) AS `Uczeń – numer`,
        p.`NazwaP`                                            AS `Przedmiot`,
        o.`Ocena`,
        CASE
            WHEN o.`Ocena` = 5   THEN 'bdb'
            WHEN o.`Ocena` = 4.5 THEN 'db+'
            WHEN o.`Ocena` = 4   THEN 'db'
            WHEN o.`Ocena` = 3.5 THEN 'dst+'
            WHEN o.`Ocena` = 3   THEN 'dst'
            WHEN o.`Ocena` = 2   THEN 'dop'
            WHEN o.`Ocena` = 1   THEN 'ndst'
            ELSE '?'
        END AS `Ocena słownie`
    FROM `Oceny` o
    INNER JOIN `Uczniowie`  u ON o.`IdU` = u.`Idu`
    INNER JOIN `Przedmioty` p ON o.`IdP` = p.`IdP`
    ORDER BY u.`Nazwisko`, u.`Imie`, p.`NazwaP`;
END;;

-- ============ L2z10 ============
-- Wszyscy uczniowie ze średnią (0.00 jeśli brak ocen) – LEFT JOIN
DROP PROCEDURE IF EXISTS `L2z10`;;
CREATE PROCEDURE `L2z10`()
BEGIN
    SELECT
        u.`Idu`                               AS `Numer`,
        u.`Nazwisko`,
        u.`Imie`,
        ROUND(IFNULL(AVG(o.`Ocena`), 0), 2)  AS `Średnia ocen`
    FROM `Uczniowie` u
    LEFT JOIN `Oceny` o ON u.`Idu` = o.`IdU`
    GROUP BY u.`Idu`, u.`Nazwisko`, u.`Imie`
    ORDER BY u.`Nazwisko`, u.`Imie`;
END;;

-- ============ L2z14 ============
-- Liczba uczniów w każdym mieście (też 0) – RIGHT JOIN + GROUP BY
DROP PROCEDURE IF EXISTS `L2z14`;;
CREATE PROCEDURE `L2z14`()
BEGIN
    SELECT
        m.`NazwaM`      AS `Miasto`,
        COUNT(u.`Idu`)  AS `Liczba uczniów`
    FROM `Uczniowie` u
    RIGHT JOIN `Miasta` m ON u.`Miasto` = m.`IdM`
    GROUP BY m.`IdM`, m.`NazwaM`
    ORDER BY m.`NazwaM`;
END;;

-- ============ L2z17 ============
-- Kto co uczy: a) tylko nauczyciele z zajęciami (INNER JOIN)
--              b) wszyscy nauczyciele (LEFT JOIN)
-- Użycie: CALL L2z17('a') lub CALL L2z17('b')
DROP PROCEDURE IF EXISTS `L2z17`;;
CREATE PROCEDURE `L2z17`(IN p_Tryb CHAR(1))
BEGIN
    IF p_Tryb = 'a' THEN
        -- tylko nauczyciele z przydziałem zajęć
        SELECT
            n.`Nazwisko`,
            n.`Imie`,
            n.`IdN`      AS `Nr nauczyciela`,
            p.`NazwaP`   AS `Przedmiot`,
            u.`IleGodz`  AS `Liczba godzin`
        FROM `Nauczyciele` n
        INNER JOIN `Uczy`       u ON n.`IdN` = u.`IdN`
        INNER JOIN `Przedmioty` p ON u.`IdP` = p.`IdP`
        ORDER BY n.`Nazwisko`, n.`Imie`;
    ELSE
        -- wszyscy nauczyciele (bez zajęć też)
        SELECT
            n.`Nazwisko`,
            n.`Imie`,
            n.`IdN`                   AS `Nr nauczyciela`,
            IFNULL(p.`NazwaP`, '–')   AS `Przedmiot`,
            IFNULL(u.`IleGodz`, 0)    AS `Liczba godzin`
        FROM `Nauczyciele` n
        LEFT JOIN `Uczy`       u ON n.`IdN` = u.`IdN`
        LEFT JOIN `Przedmioty` p ON u.`IdP` = p.`IdP`
        ORDER BY n.`Nazwisko`, n.`Imie`;
    END IF;
END;;

-- ============ L2z21 ============
-- Liczba dziewcząt i chłopców w każdej klasie – SUM + IF + GROUP BY
DROP PROCEDURE IF EXISTS `L2z21`;;
CREATE PROCEDURE `L2z21`()
BEGIN
    SELECT
        k.`Symbol`                       AS `Klasa`,
        SUM(IF(u.`Plec` = 'K', 1, 0))   AS `Dziewczęta`,
        SUM(IF(u.`Plec` = 'M', 1, 0))   AS `Chłopcy`
    FROM `Klasy` k
    LEFT JOIN `Uczniowie` u ON u.`KlasaU` = k.`Symbol`
    GROUP BY k.`Symbol`
    ORDER BY k.`Symbol`;
END;;

-- ============ L2z23 ============
-- Dochód nauczycieli: pensja, nagroda, dochód – sortowanie wg dochodu malejąco
DROP PROCEDURE IF EXISTS `L2z23`;;
CREATE PROCEDURE `L2z23`()
BEGIN
    SELECT
        CONCAT(n.`Nazwisko`, ' ', n.`Imie`, ' – ', n.`IdN`) AS `Nazwisko i imię`,
        ROUND(n.`Pensja`, 2)                                  AS `Pensja`,
        ROUND(n.`Pensja` * 0.2, 2)                            AS `Nagroda (0,2*pensja)`,
        ROUND(n.`Pensja` * 1.2, 2)                            AS `Dochód (Pensja+nagroda)`
    FROM `Nauczyciele` n
    ORDER BY n.`Pensja` * 1.2 DESC, n.`Nazwisko`, n.`Imie`;
END;;

-- ============ L2z30 ============
-- Staż pracy nauczycieli w dniach, miesiącach i latach
DROP PROCEDURE IF EXISTS `L2z30`;;
CREATE PROCEDURE `L2z30`()
BEGIN
    SELECT
        `Nazwisko`,
        `Imie`,
        `IdN`                                        AS `Nr`,
        DATEDIFF(CURDATE(), DATE(`DZatr`))           AS `Staż (dni)`,
        TIMESTAMPDIFF(MONTH, DATE(`DZatr`), CURDATE()) AS `Staż (miesiące)`,
        TIMESTAMPDIFF(YEAR,  DATE(`DZatr`), CURDATE()) AS `Staż (lata)`
    FROM `Nauczyciele`
    WHERE `DZatr` IS NOT NULL
    ORDER BY `Nazwisko`, `Imie`;
END;;

-- ============ L2z33 ============
-- Dołącz do Archiwum nauczycieli zatrudnionych przed podaną datą
-- Użycie: CALL L2z33('2015-09-01');
DROP PROCEDURE IF EXISTS `L2z33`;;
CREATE PROCEDURE `L2z33`(IN p_Data DATE)
BEGIN
    INSERT INTO `Archiwum`
    SELECT * FROM `Nauczyciele`
    WHERE DATE(`DZatr`) < p_Data;
END;;

-- ============ L2z36 ============
-- Spis nauczycieli i uczniów razem z symbolem N/U – UNION ALL
DROP PROCEDURE IF EXISTS `L2z36`;;
CREATE PROCEDURE `L2z36`()
BEGIN
    SELECT
        `Nazwisko`,
        `Imie`,
        `IdN`          AS `Nr`,
        DATE(`DUr`)    AS `Data urodzenia`,
        'N'            AS `Typ`
    FROM `Nauczyciele`
    UNION ALL
    SELECT
        `Nazwisko`,
        `Imie`,
        `Idu`          AS `Nr`,
        DATE(`DUr`)    AS `Data urodzenia`,
        'U'            AS `Typ`
    FROM `Uczniowie`
    ORDER BY `Data urodzenia` DESC, `Nazwisko` ASC, `Imie` ASC;
END;;

DELIMITER ;

-- ============================================================
-- Przykłady wywołań:
-- ============================================================
-- CALL L2z01();
-- CALL L2z03('Ia');
-- CALL L2z08();
-- CALL L2z10();
-- CALL L2z14();
-- CALL L2z17('a');   -- tylko nauczyciele z zajęciami (INNER JOIN)
-- CALL L2z17('b');   -- wszyscy nauczyciele (LEFT JOIN)
-- CALL L2z21();
-- CALL L2z23();
-- CALL L2z30();
-- CALL L2z33('2015-09-01');
-- CALL L2z36();
