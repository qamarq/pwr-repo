-- ============ L3z01 ============
SELECT Uczniowie.Nazwisko,Uczniowie.Imie,Uczniowie.IdU,Uczniowie.KlasaU,Uczniowie.Miasto FROM [Miasta],[Uczniowie] WHERE (((Uczniowie.KlasaU) Like "I?" Or (Uczniowie.KlasaU) Like "II?" Or (Uczniowie.KlasaU) Like "III?")) 

-- ============ L3z03 ============
SELECT Uczniowie.Nazwisko,Uczniowie.Imie,Uczniowie.IdU,Uczniowie.DUr,Uczniowie.KlasaU,Miasta.NazwaM FROM [Uczniowie],[Miasta] WHERE (((Uczniowie.KlasaU)=[Podaj symbol klasy])) ORDER BY Uczniowie.Nazwisko

-- ============ L3z08 ============
SELECT Uczniowie.Nazwisko & " " & Uczniowie.Imie & " - " & Uczniowie.IdU,Przedmioty.NazwaP,Oceny.Ocena,Switch(Ocena=5,'bdb',Ocena=4.5,'db+',Ocena=4,'db',Ocena=3.5,'dst+',Ocena=3,'dst',Ocena=2,'dop',Ocena=1,'ndst') FROM [Uczniowie],[Oceny],[Przedmioty] ORDER BY Uczniowie.Nazwisko

-- ============ L3z10 ============
SELECT Uczniowie.IdU,Uczniowie.Nazwisko,Uczniowie.Imie,Avg(Oceny.Ocena) FROM [Uczniowie],[Oceny] ORDER BY Uczniowie.Nazwisko

-- ============ L3z14 ============
SELECT Miasta.NazwaM,Count(Uczniowie.IdU) FROM [Miasta],[Uczniowie] 

-- ============ L3z21 ============
SELECT Klasy.Symbol,Sum(IIf(Plec='K',1,0)),Sum(IIf(Plec='M',1,0)) FROM [Klasy],[Uczniowie] 

-- ============ L3z23 ============
SELECT (Nauczyciele.Nazwisko & " " & Nauczyciele.Imie & " - " & Nauczyciele.IdN),Nauczyciele.Pensja,(Nauczyciele.Pensja*0.2),(Nauczyciele.Pensja+(Nauczyciele.Pensja*0.2)) FROM [Nauczyciele] ORDER BY (Nauczyciele.Pensja*1.2)

-- ============ L3z30 ============
SELECT Nazwisko,Imie,IdN,DateDiff("d", DZatr, Date()),DateDiff("m", DZatr, Date()),DateDiff("yyyy", DZatr, Date()) FROM [Nauczyciele] 

-- ============ L3z33 ============
SELECT  FROM [Nauczyciele] WHERE DZatr < [Podaj datę graniczną (RRRR-MM-DD)] 

-- ============ L3z36 ============
SELECT DISTINCT  FROM [],[] ORDER BY [Data urodzenia]

