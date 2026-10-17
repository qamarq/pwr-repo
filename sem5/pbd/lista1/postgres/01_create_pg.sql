CREATE TABLE Bandy (
    nr_bandy   NUMERIC(2)   CONSTRAINT ba_nr_pk PRIMARY KEY,
    nazwa      VARCHAR(20)  CONSTRAINT ba_na_nn NOT NULL,
    teren      VARCHAR(15)  CONSTRAINT ba_te_un UNIQUE,
    szef_bandy VARCHAR(15)  CONSTRAINT ba_sz_un UNIQUE
);

CREATE TABLE Funkcje (
    funkcja   VARCHAR(10)  CONSTRAINT fu_fu_pk PRIMARY KEY,
    min_myszy NUMERIC(3)   CONSTRAINT fu_mi_ch CHECK (min_myszy > 5),
    max_myszy NUMERIC(3)   CONSTRAINT fu_ma_ch CHECK (max_myszy < 200),
    CONSTRAINT fu_mm_ch CHECK (max_myszy >= min_myszy)
);

CREATE TABLE Wrogowie (
    imie_wroga       VARCHAR(15)  CONSTRAINT wr_im_pk PRIMARY KEY,
    stopien_wrogosci NUMERIC(2)   CONSTRAINT wr_st_ch CHECK (stopien_wrogosci BETWEEN 1 AND 10),
    gatunek          VARCHAR(15),
    lapowka          VARCHAR(20)
);

CREATE TABLE Kocury (
    imie            VARCHAR(15)  CONSTRAINT ko_im_nn NOT NULL,
    plec            VARCHAR(1)   CONSTRAINT ko_pl_ch CHECK (plec IN ('M', 'D')),
    pseudo          VARCHAR(15)  CONSTRAINT ko_ps_pk PRIMARY KEY,
    funkcja         VARCHAR(10)  CONSTRAINT ko_fu_fk REFERENCES Funkcje(funkcja),
    szef            VARCHAR(15)  CONSTRAINT ko_sz_fk REFERENCES Kocury(pseudo) DEFERRABLE INITIALLY DEFERRED,
    w_stadku_od     DATE         DEFAULT CURRENT_DATE,
    przydzial_myszy NUMERIC(3),
    myszy_extra     NUMERIC(3),
    nr_bandy        NUMERIC(2)   CONSTRAINT ko_nr_fk REFERENCES Bandy(nr_bandy)
);

CREATE TABLE Wrogowie_kocurow (
    pseudo         VARCHAR(15)  CONSTRAINT wk_ps_fk REFERENCES Kocury(pseudo),
    imie_wroga     VARCHAR(15)  CONSTRAINT wk_im_fk REFERENCES Wrogowie(imie_wroga),
    data_incydentu DATE         CONSTRAINT wk_da_nn NOT NULL,
    opis_incydentu VARCHAR(50),
    CONSTRAINT wk_pk PRIMARY KEY (pseudo, imie_wroga)
);

-- wyjatek bo fk bandy<->kocury, kocury nie istnieje jeszcze przy create table bandy
ALTER TABLE Bandy ADD CONSTRAINT ba_sz_fk FOREIGN KEY (szef_bandy) REFERENCES Kocury(pseudo)
    DEFERRABLE INITIALLY DEFERRED;
