# Lista 3

- **Zad. 1:** warunek „wróg na A” dotyczy tylko kotów przyjętych przed szefem, bo czytany dosłownie wykluczałby koty bez incydentów (LUCEK, MICKA, PUCEK), a są w przykładzie. Brak incydentów bez podzapytania: `LEFT JOIN` + `IS NULL`.
- **Zad. 2:** filtr płci musi być w `WHERE`, a nie w `ON`, bo w `FULL JOIN` warunek w `ON` nie usuwa wierszy i kotki wróciłyby jako wiersze bez pary. Przez to ZOMBI (same podwładne kotki) nie ma wiersza „Brak podwladnego”, tak jak w przykładzie.
- **Zad. 3:** na polu polują też koty z terenu `CALOSC`. Suma w bandzie dotyczy całej bandy, a złączenie z kolegami z bandy mnoży wiersze przez liczbę wrogów, więc sumę dzielę przez `COUNT(DISTINCT wróg)`.
- **Zad. 5:** warunek „wróg CZLOWIEK i PIES” zawiera się w „więcej niż jeden wróg”, więc oba dają te same koty.
- **Zad. 7:** `NOT IN` zwraca 0 wierszy, bo kolumna `szef` zawiera `NULL` (TYGRYS), a `x <> NULL` daje `UNKNOWN`. `NOT EXISTS` porównuje wiersz po wierszu, więc `NULL` mu nie przeszkadza.
- **Zad. 8:** bez `COALESCE` / `NVL` na `myszy_extra` suma daje `NULL`, a `AVG` pomija `NULL`, więc średnia liczyłaby się tylko z kotów z dodatkowym przydziałem.
- **Zad. 9:** w Oracle nazwy miesięcy zależą od języka sesji, stąd `NLS_DATE_LANGUAGE=ENGLISH`; `FM` usuwa spacje dopełniające nazwę.
- **Zad. 10:** w sadzie operują też miluśki z terenu `CALOSC` (próg 3 · 25 = 75); tylko tak wynik zgadza się z przykładem. `>= ALL` zastępuje `MAX`.
- **Zad. 11:** Oracle najpierw buduje drzewo, a dopiero potem filtruje `WHERE`, dlatego ZERO jest na poziomie 3 (droga przez ukrytą kotkę KURKA).
- **Zad. 12:** warunki są tylko w `START WITH`, więc szefowie po drodze nie są filtrowani (TYGRYS ma dodatkowy przydział, a jest w wyniku).
- **Zad. 13:** wersja na złączeniach działa tylko dla z góry znanej liczby poziomów (3); drzewo nie ma tego ograniczenia.
