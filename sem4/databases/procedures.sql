-- ============================================================
-- Bazy danych – Lista 3 – Procedury MySQL
-- Wygenerowano automatycznie z kwerend Access
-- ============================================================

SET NAMES utf8mb4;
DELIMITER ;;

-- ============ L3z01 ============
DROP PROCEDURE IF EXISTS `L3z01`;;
CREATE PROCEDURE `L3z01`()
BEGIN
    SELECT Uczniowie.Nazwisko, Uczniowie.Imie, Uczniowie.IdU, Uczniowie.KlasaU, Uczniowie.Miasto FROM `Miasta`,`Uczniowie` WHERE (((Uczniowie.KlasaU) LIKE 'I_' Or (Uczniowie.KlasaU) LIKE 'II_' Or (Uczniowie.KlasaU) LIKE 'III_'));
END;;

-- ============ L3z03 ============
DROP PROCEDURE IF EXISTS `L3z03`;;
CREATE PROCEDURE `L3z03`(IN `p_Podaj_symbol_klasy` VARCHAR(50))
BEGIN
    SELECT Uczniowie.Nazwisko, Uczniowie.Imie, Uczniowie.IdU, Uczniowie.DUr, Uczniowie.KlasaU, Miasta.NazwaM FROM `Uczniowie`,`Miasta` WHERE (((Uczniowie.KlasaU)= p_Podaj_symbol_klasy)) ORDER BY Uczniowie.Nazwisko;
END;;

-- ============ L3z08 ============
DROP PROCEDURE IF EXISTS `L3z08`;;
CREATE PROCEDURE `L3z08`()
BEGIN
    SELECT CONCAT(Uczniowie.Nazwisko, " ", Uczniowie.Imie, " - ", Uczniowie.IdU), Przedmioty.NazwaP, Oceny.Ocena, CASE WHEN Ocena=5 THEN 'bdb' WHEN Ocena=4.5 THEN 'db+' WHEN Ocena=4 THEN 'db' WHEN Ocena=3.5 THEN 'dst+' WHEN Ocena=3 THEN 'dst' WHEN Ocena=2 THEN 'dop' WHEN Ocena=1 THEN 'ndst' END FROM `Uczniowie`,`Oceny`,`Przedmioty` ORDER BY Uczniowie.Nazwisko;
END;;

-- ============ L3z10 ============
DROP PROCEDURE IF EXISTS `L3z10`;;
CREATE PROCEDURE `L3z10`()
BEGIN
    SELECT Uczniowie.IdU, Uczniowie.Nazwisko, Uczniowie.Imie, AVG(Oceny.Ocena) AS `Średnia ocen` FROM `Uczniowie`,`Oceny` ORDER BY Uczniowie.Nazwisko;
END;;

-- ============ L3z14 ============
DROP PROCEDURE IF EXISTS `L3z14`;;
CREATE PROCEDURE `L3z14`()
BEGIN
    SELECT Miasta.NazwaM, COUNT(Uczniowie.IdU) AS `Liczba` FROM `Miasta`,`Uczniowie`;
END;;

-- ============ L3z21 ============
DROP PROCEDURE IF EXISTS `L3z21`;;
CREATE PROCEDURE `L3z21`()
BEGIN
    SELECT Klasy.Symbol, SUM(IF(Plec='K',1,0)), SUM(IF(Plec='M',1,0)) FROM `Klasy`,`Uczniowie`;
END;;

-- ============ L3z23 ============
DROP PROCEDURE IF EXISTS `L3z23`;;
CREATE PROCEDURE `L3z23`()
BEGIN
    SELECT CONCAT(Nauczyciele.Nazwisko, " ", Nauczyciele.Imie, " - ", Nauczyciele.IdN), Nauczyciele.Pensja, (ROUND(Nauczyciele.Pensja*0.2, 2)), (Nauczyciele.Pensja+(ROUND(Nauczyciele.Pensja*0.2, 2))) FROM `Nauczyciele` ORDER BY (Nauczyciele.Pensja*1.2);
END;;

-- ============ L3z30 ============
DROP PROCEDURE IF EXISTS `L3z30`;;
CREATE PROCEDURE `L3z30`()
BEGIN
    SELECT Nazwisko, Imie, IdN, DATEDIFF(CURDATE(), DZatr), TIMESTAMPDIFF(MONTH, DZatr, CURDATE()), TIMESTAMPDIFF(YEAR, DZatr, CURDATE()) FROM `Nauczyciele`;
END;;

-- ============ L3z33 ============
DROP PROCEDURE IF EXISTS `L3z33`;;
CREATE PROCEDURE `L3z33`(IN p_Data DATE)
BEGIN
    INSERT INTO `Archiwum`
    SELECT * FROM `Nauczyciele`
    WHERE DATE(`DZatr`) < p_Data;
END;;

-- ============ L3z36 ============
DROP PROCEDURE IF EXISTS `L3z36`;;
CREATE PROCEDURE `L3z36`()
BEGIN
    SELECT Nazwisko, Imie, IdN AS Nr, DATE(DUr) AS `Data urodzenia`, 'N' AS `Typ`
    FROM `Nauczyciele`
    UNION ALL
    SELECT Nazwisko, Imie, Idu AS Nr, DATE(DUr) AS `Data urodzenia`, 'U' AS `Typ`
    FROM `Uczniowie`
    ORDER BY `Data urodzenia` DESC, Nazwisko ASC, Imie ASC;
END;;

DELIMITER ;

-- Wywołania przykładowe:
-- CALL L3z01();
-- CALL L3z03('Ia');
-- CALL L3z33('2015-09-01');

-- ============================================================
-- Przykłady wywołań:
-- ============================================================
-- CALL L3z01();
-- CALL L3z03('Ia');
-- CALL L3z08();
-- CALL L3z10();
-- CALL L3z14();
-- CALL L3z21();
-- CALL L3z23();
-- CALL L3z30();
-- CALL L3z33('2015-09-01');
-- CALL L3z36();
