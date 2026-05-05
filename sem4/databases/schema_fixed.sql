-- ============================================================
-- Poprawiony schemat MySQL - wygenerowany na podstawie schema.sql
-- Kodowanie: UTF-8
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;

-- ------------------------------------------------------------
-- Tabele bez zależności
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Miasta` (
  `IdM`    INT NOT NULL AUTO_INCREMENT,
  `NazwaM` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`IdM`),
  UNIQUE KEY `uq_Miasta_NazwaM` (`NazwaM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Przedmioty` (
  `IdP`    INT NOT NULL AUTO_INCREMENT,
  `NazwaP` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`IdP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `Archiwum` (
  `IdN`      INT NOT NULL,
  `Nazwisko` VARCHAR(50),
  `Imie`     VARCHAR(30),
  `DZatr`    DATETIME,
  `DUr`      DATETIME,
  `Plec`     VARCHAR(1),
  `Pensja`   DOUBLE,
  `Pensum`   INT,
  `Telefon`  VARCHAR(20),
  `Premia`   FLOAT,
  PRIMARY KEY (`IdN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Nauczyciele (przed Klasy, bo Klasy.Wych -> Nauczyciele)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Nauczyciele` (
  `IdN`      INT NOT NULL AUTO_INCREMENT,
  `Nazwisko` VARCHAR(30) NOT NULL,
  `Imie`     VARCHAR(30) NOT NULL,
  `DZatr`    DATETIME,
  `DUr`      DATETIME,
  `Plec`     VARCHAR(1),
  `Pensja`   DOUBLE,
  `Pensum`   INT,
  `Telefon`  VARCHAR(15),
  `Premia`   FLOAT,
  PRIMARY KEY (`IdN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Klasy (zależy od Nauczyciele)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Klasy` (
  `Symbol` VARCHAR(6)  NOT NULL,
  `Profil` VARCHAR(30) NOT NULL,
  `Wych`   INT,
  PRIMARY KEY (`Symbol`),
  CONSTRAINT `fk_Klasy_Wych` FOREIGN KEY (`Wych`) REFERENCES `Nauczyciele`(`IdN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Uczniowie (zależy od Klasy i Miasta)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Uczniowie` (
  `Idu`      INT NOT NULL AUTO_INCREMENT,
  `Nazwisko` VARCHAR(30) NOT NULL,
  `Imie`     VARCHAR(30) NOT NULL,
  `DUr`      DATETIME,
  `Plec`     VARCHAR(1),
  `KlasaU`   VARCHAR(6),
  `Miasto`   INT,
  `Email`    VARCHAR(50),
  PRIMARY KEY (`Idu`),
  CONSTRAINT `fk_Uczniowie_KlasaU` FOREIGN KEY (`KlasaU`) REFERENCES `Klasy`(`Symbol`),
  CONSTRAINT `fk_Uczniowie_Miasto` FOREIGN KEY (`Miasto`)  REFERENCES `Miasta`(`IdM`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Oceny (zależy od Uczniowie i Przedmioty)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Oceny` (
  `IdU`   INT   NOT NULL,
  `IdP`   INT   NOT NULL,
  `Ocena` FLOAT,
  `DataO` DATETIME,
  PRIMARY KEY (`IdU`, `IdP`),
  KEY `idx_Oceny_IdP` (`IdP`),
  CONSTRAINT `fk_Oceny_IdU` FOREIGN KEY (`IdU`) REFERENCES `Uczniowie`(`Idu`),
  CONSTRAINT `fk_Oceny_IdP` FOREIGN KEY (`IdP`) REFERENCES `Przedmioty`(`IdP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ------------------------------------------------------------
-- Uczy (zależy od Nauczyciele i Przedmioty)
-- ------------------------------------------------------------

CREATE TABLE IF NOT EXISTS `Uczy` (
  `IdN`     INT NOT NULL,
  `IdP`     INT NOT NULL,
  `IleGodz` INT NOT NULL,
  PRIMARY KEY (`IdN`, `IdP`),
  KEY `idx_Uczy_IdP` (`IdP`),
  CONSTRAINT `fk_Uczy_IdN` FOREIGN KEY (`IdN`) REFERENCES `Nauczyciele`(`IdN`),
  CONSTRAINT `fk_Uczy_IdP` FOREIGN KEY (`IdP`) REFERENCES `Przedmioty`(`IdP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
