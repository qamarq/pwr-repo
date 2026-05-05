-- ----------------------------------------------------------
-- MDB Tools - A library for reading MS Access database files
-- Copyright (C) 2000-2011 Brian Bruns and others.
-- Files in libmdb are licensed under LGPL and the utilities under
-- the GPL, see COPYING.LIB and COPYING files respectively.
-- Check out http://mdbtools.sourceforge.net
-- ----------------------------------------------------------

-- That file uses encoding UTF-8

CREATE TABLE `Archiwum`
 (
	`IdN`			int, 
	`Nazwisko`			varchar (50), 
	`Imie`			varchar (30), 
	`DZatr`			datetime, 
	`DUr`			datetime, 
	`Plec`			varchar (1), 
	`Pensja`			double, 
	`Pensum`			int, 
	`Telefon`			varchar (20), 
	`Premia`			float
);

-- CREATE INDEXES ...
ALTER TABLE `Archiwum` ADD PRIMARY KEY (`IdN`);

CREATE TABLE `Klasy`
 (
	`Symbol`			varchar (6) NOT NULL, 
	`Profil`			varchar (30) NOT NULL, 
	`Wych`			int
);

-- CREATE INDEXES ...
ALTER TABLE `Klasy` ADD PRIMARY KEY (`Symbol`);

CREATE TABLE `Miasta`
 (
	`IdM`			int not null auto_increment unique, 
	`NazwaM`			varchar (30) NOT NULL
);

-- CREATE INDEXES ...
ALTER TABLE `Miasta` ADD UNIQUE INDEX `NazwaM` (`NazwaM`);
ALTER TABLE `Miasta` ADD PRIMARY KEY (`IdM`);

CREATE TABLE `Oceny`
 (
	`IdU`			int NOT NULL, 
	`IdP`			int NOT NULL, 
	`Ocena`			float, 
	`DataO`			datetime
);

-- CREATE INDEXES ...
ALTER TABLE `Oceny` ADD INDEX `IdP` (`IdP`);
ALTER TABLE `Oceny` ADD PRIMARY KEY (`IdU`, `IdP`);

CREATE TABLE `Przedmioty`
 (
	`IdP`			int not null auto_increment unique, 
	`NazwaP`			varchar (30) NOT NULL
);

-- CREATE INDEXES ...
ALTER TABLE `Przedmioty` ADD PRIMARY KEY (`IdP`);

CREATE TABLE `Uczniowie`
 (
	`Idu`			int not null auto_increment unique, 
	`Nazwisko`			varchar (30) NOT NULL, 
	`Imie`			varchar (30) NOT NULL, 
	`DUr`			datetime, 
	`Plec`			varchar (1), 
	`KlasaU`			varchar (6), 
	`Miasto`			int, 
	`Email`			varchar (50)
);

-- CREATE INDEXES ...
ALTER TABLE `Uczniowie` ADD PRIMARY KEY (`Idu`);

CREATE TABLE `Uczy`
 (
	`IdN`			int NOT NULL, 
	`IdP`			int NOT NULL, 
	`IleGodz`			int NOT NULL
);

-- CREATE INDEXES ...
ALTER TABLE `Uczy` ADD INDEX `IdP` (`IdP`);
ALTER TABLE `Uczy` ADD PRIMARY KEY (`IdN`, `IdP`);

CREATE TABLE `Nauczyciele`
 (
	`IdN`			int not null auto_increment unique, 
	`Nazwisko`			varchar (30) NOT NULL, 
	`Imie`			varchar (30) NOT NULL, 
	`DZatr`			datetime, 
	`DUr`			datetime, 
	`Plec`			varchar (1), 
	`Pensja`			double, 
	`Pensum`			int, 
	`Telefon`			varchar (15), 
	`Premia`			float
);

-- CREATE INDEXES ...
ALTER TABLE `Nauczyciele` ADD PRIMARY KEY (`IdN`);


-- CREATE Relationships ...
ALTER TABLE `Uczniowie` ADD CONSTRAINT `Uczniowie_KlasaU_fk` FOREIGN KEY (`KlasaU`) REFERENCES `Klasy`(`Symbol`);
ALTER TABLE `Uczniowie` ADD CONSTRAINT `Uczniowie_Miasto_fk` FOREIGN KEY (`Miasto`) REFERENCES `Miasta`(`IdM`);
ALTER TABLE `MSysNavPaneGroups` ADD CONSTRAINT `MSysNavPaneGroups_GroupCategoryID_fk` FOREIGN KEY (`GroupCategoryID`) REFERENCES `MSysNavPaneGroupCategories`(`Id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `MSysNavPaneGroupToObjects` ADD CONSTRAINT `MSysNavPaneGroupToObjects_GroupID_fk` FOREIGN KEY (`GroupID`) REFERENCES `MSysNavPaneGroups`(`Id`) ON UPDATE CASCADE ON DELETE CASCADE;
ALTER TABLE `Klasy` ADD CONSTRAINT `Klasy_Wych_fk` FOREIGN KEY (`Wych`) REFERENCES `Nauczyciele`(`IdN`);
ALTER TABLE `Uczy` ADD CONSTRAINT `Uczy_IdN_fk` FOREIGN KEY (`IdN`) REFERENCES `Nauczyciele`(`IdN`);
ALTER TABLE `Oceny` ADD CONSTRAINT `Oceny_IdP_fk` FOREIGN KEY (`IdP`) REFERENCES `Przedmioty`(`IdP`);
ALTER TABLE `Uczy` ADD CONSTRAINT `Uczy_IdP_fk` FOREIGN KEY (`IdP`) REFERENCES `Przedmioty`(`IdP`);
ALTER TABLE `Oceny` ADD CONSTRAINT `Oceny_IdU_fk` FOREIGN KEY (`IdU`) REFERENCES `Uczniowie`(`Idu`);
