EPICS = [
    ("E1. Dane o programie studiów", [
        ("US01", "Jako osoba dokonująca powierzeń chcę zaimportować z arkusza XLSX kierunki i plany studiów wraz z etapami, kursami i formami zajęć, aby nie wprowadzać ich ręcznie.", "Must", "1.0", "UC01"),
        ("US02", "Jako osoba dokonująca powierzeń chcę dodawać, zmieniać i usuwać kierunki, plany studiów, etapy, kursy i formy zajęć, aby dane odpowiadały aktualnemu programowi studiów.", "Must", "1.0", "UC02"),
        ("US03", "Jako osoba dokonująca powierzeń chcę dla instancji planu (naboru) i semestru wprowadzić liczbę studentów w każdej formie zajęć, aby system wyliczył liczbę grup i godzin do powierzenia.", "Must", "1.0", "UC03"),
    ]),
    ("E2. Prowadzący i pensum", [
        ("US04", "Jako osoba dokonująca powierzeń chcę zaimportować z arkusza XLSX prowadzących wraz ze stanowiskiem, grupą pracowniczą, wymiarem etatu i zniżkami pensum, aby szybko przenieść dane z obecnego arkusza.", "Should", "1.0", "UC01"),
        ("US05", "Jako osoba dokonująca powierzeń chcę dodawać i zmieniać dane nauczycieli akademickich i ekspertów zewnętrznych oraz zniżki pensum na dany rok akademicki, aby pensum było liczone poprawnie.", "Must", "1.0", "UC04"),
        ("US06", "Jako osoba dokonująca powierzeń chcę określić, które kursy może prowadzić dany prowadzący, aby przy obsadzie kursu widzieć tylko odpowiednie osoby.", "Could", "2.0", "UC04"),
        ("US07", "Jako prowadzący chcę zgłosić preferowane kursy, formy zajęć i dostępność na semestr, aby otrzymać zajęcia zgodne z moimi możliwościami.", "Should", "1.1", "UC05"),
    ]),
    ("E3. Przygotowanie powierzeń", [
        ("US08", "Jako osoba dokonująca powierzeń chcę powierzyć prowadzącemu określoną liczbę godzin wybranej formy zajęć kursu, aby ustalić obsadę zajęć.", "Must", "1.0", "UC06"),
        ("US09", "Jako osoba dokonująca powierzeń chcę podzielić godziny jednej formy zajęć między kilku prowadzących, także w części grupy, aby elastycznie rozłożyć obciążenie.", "Must", "1.0", "UC06"),
        ("US10", "Jako osoba dokonująca powierzeń chcę zmienić lub usunąć powierzenie, aby uwzględnić zmiany w obsadzie.", "Must", "1.0", "UC08"),
        ("US11", "Jako osoba dokonująca powierzeń chcę przy każdej zmianie powierzenia widzieć stan pensum prowadzącego i ostrzeżenia o przekroczeniu limitów godzin, aby nie naruszyć regulaminu pracy.", "Must", "1.0", "UC07"),
        ("US12", "Jako osoba dokonująca powierzeń chcę widzieć formy zajęć z nieobsadzonymi godzinami oraz nauczycieli bez pełnego pensum, aby uzupełnić obsadę przed wysłaniem propozycji.", "Must", "1.0", "UC09"),
        ("US13", "Jako osoba dokonująca powierzeń chcę skopiować powierzenia z analogicznego semestru poprzedniego roku, aby szybciej przygotować nowy plan.", "Should", "1.1", "UC10"),
        ("US14", "Jako osoba dokonująca powierzeń chcę widzieć preferencje prowadzących podczas przydzielania zajęć, aby lepiej dopasować zajęcia do prowadzących.", "Should", "1.1", "UC06"),
        ("US15", "Jako osoba dokonująca powierzeń chcę, aby system wygenerował wstępny przydział na podstawie pensum, kompetencji i preferencji, aby zacząć pracę od gotowej propozycji.", "Could", "2.0", "—"),
    ]),
    ("E4. Konsultowanie propozycji przydziałów", [
        ("US16", "Jako osoba dokonująca powierzeń chcę jednym działaniem wysłać prowadzącym e-maile z propozycjami przydziału i terminem odpowiedzi, aby nie przygotowywać wiadomości ręcznie.", "Must", "1.0", "UC11"),
        ("US17", "Jako prowadzący chcę zaakceptować propozycję albo zgłosić uwagi do jej pozycji przez odnośnik w wiadomości, aby szybko odpowiedzieć bez logowania się do systemu.", "Must", "1.0", "UC12"),
        ("US18", "Jako nauczyciel akademicki chcę wyrazić w odpowiedzi pisemną zgodę na obciążenie powyżej 125% lub 150% pensum, aby spełnić wymóg regulaminu pracy.", "Must", "1.0", "UC12"),
        ("US19", "Jako prowadzący chcę otrzymać przypomnienie przed upływem terminu odpowiedzi, aby nie przeoczyć propozycji.", "Must", "1.0", "UC13"),
        ("US20", "Jako osoba dokonująca powierzeń chcę, aby propozycje bez odpowiedzi w terminie były automatycznie uznawane za zaakceptowane milcząco, aby nie śledzić terminów ręcznie.", "Must", "1.0", "UC14"),
        ("US21", "Jako osoba dokonująca powierzeń chcę widzieć status propozycji każdego prowadzącego i listę uwag oczekujących na rozwiązanie, aby wiedzieć, co blokuje zatwierdzenie planu.", "Must", "1.0", "UC09, UC15"),
        ("US22", "Jako osoba dokonująca powierzeń chcę rozwiązać uwagę, zmieniając powierzenia albo odrzucając ją z uzasadnieniem, aby prowadzący otrzymał nową wersję propozycji.", "Must", "1.0", "UC15"),
        ("US23", "Jako osoba dokonująca powierzeń chcę, aby AI rozpoznała rodzaj odpowiedzi i powiązała zgłoszone zmiany z pozycjami propozycji, aby szybciej analizować odpowiedzi.", "Should", "1.1", "UC12, UC15"),
        ("US24", "Jako osoba dokonująca powierzeń chcę otrzymać od AI propozycję rozwiązania uwagi (np. alternatywnego prowadzącego), aby szybciej podjąć decyzję.", "Could", "2.0", "UC15"),
    ]),
    ("E5. Zatwierdzanie i przekazanie powierzeń", [
        ("US25", "Jako osoba dokonująca powierzeń chcę zatwierdzić plan powierzeń, gdy jest kompletny i wszystkie propozycje są zaakceptowane, aby przekazać go do działu kształcenia.", "Must", "1.0", "UC16"),
        ("US26", "Jako pracownik działu kształcenia chcę pobrać zatwierdzone powierzenia w formacie XLSX lub CSV, aby ułożyć na ich podstawie plan zajęć.", "Must", "1.0", "UC17"),
        ("US27", "Jako prowadzący chcę przeglądać swoje powierzenia i stan pensum, aby znać swoje obciążenie w roku akademickim.", "Should", "1.1", "UC20"),
    ]),
    ("E6. Dorabianie grup", [
        ("US28", "Jako pracownik działu kształcenia chcę zgłosić potrzebę dorobienia grupy, gdy zapisało się więcej studentów, aby wszyscy studenci mieli miejsce na zajęciach.", "Should", "1.0", "UC18"),
        ("US29", "Jako osoba dokonująca powierzeń chcę dodać grupę do formy zajęć i powierzyć jej godziny prowadzącemu z kontrolą pensum i limitów, także po zatwierdzeniu planu.", "Should", "1.0", "UC19"),
        ("US30", "Jako pracownik działu kształcenia chcę otrzymać powiadomienie o dorobionej grupie, aby uwzględnić ją w planie zajęć.", "Should", "1.0", "UC19"),
    ]),
    ("E7. Administracja", [
        ("US31", "Jako administrator chcę zakładać konta użytkowników i nadawać im role, aby każdy miał dostęp tylko do potrzebnych funkcji.", "Must", "1.0", "UC21"),
        ("US32", "Jako użytkownik chcę logować się kontem uczelnianym (SSO), aby nie pamiętać osobnego hasła.", "Should", "1.1", "UC21"),
    ]),
]

ACTORS = [
    ("Osoba dokonująca powierzeń", "Prodziekan ds. kształcenia przygotowujący powierzenia; główny użytkownik systemu."),
    ("Prowadzący", "Aktor abstrakcyjny – osoba, której powierza się zajęcia. Specjalizacje: Nauczyciel akademicki (ma pensum) i Ekspert zewnętrzny (umowa cywilnoprawna)."),
    ("Pracownik działu kształcenia", "Pobiera zatwierdzone powierzenia i zgłasza potrzebę dorobienia grup."),
    ("Administrator", "Pracownik Działu Informatyzacji zarządzający kontami i rolami."),
    ("Czas", "Aktor inicjujący przypadki wykonywane cyklicznie (przypomnienia, milcząca zgoda po upływie terminu)."),
    ("Serwer pocztowy", "Uczelniany serwer poczty – wysyła wiadomości systemu i przekazuje odpowiedzi prowadzących."),
    ("Usługa AI", "Model językowy analizujący treść odpowiedzi prowadzących i podpowiadający rozwiązania (od wydania 1.1)."),
]

ODP = "Osoba dokonująca powierzeń"

UCS = [
 dict(id="UC06", name="Powierz zajęcia", actor=ODP, sec="—",
  desc="Przypadek umożliwia osobie dokonującej powierzeń powierzenie prowadzącemu określonej liczby godzin wybranej formy zajęć kursu w planie powierzeń semestru.",
  pre="Osoba dokonująca powierzeń jest zalogowana. Istnieje niezatwierdzony plan powierzeń wybranej instancji planu i semestru z określonym zapotrzebowaniem. Wyświetlony jest ekran planu powierzeń.",
  post="Powierzenie zapisano w planie powierzeń. Godziny nieobsadzone zapotrzebowania oraz roczne rozliczenie pensum prowadzącego zostały zaktualizowane.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera z listy zapotrzebowania formę zajęć kursu, dla której chce powierzyć godziny", ""),
     ("2.", "", "Wyświetla szczegóły zapotrzebowania (kurs, forma, liczba grup, liczba godzin, godziny nieobsadzone) oraz prowadzących, którzy mogą prowadzić kurs, wraz ze stanem ich pensum i preferencjami"),
     ("3.", "Wybiera prowadzącego, wprowadza liczbę godzin i opcjonalnie liczbę grup", ""),
     ("4.", "", "Weryfikuje, że dane są kompletne i poprawne, oraz sprawdza pensum i limity godzin prowadzącego (UC07)"),
     ("5.", "", "Wyświetla podgląd: godziny powierzone prowadzącemu w roku akademickim przed zmianą i po niej, pensum po zniżkach i procent jego realizacji"),
     ("6.", "Zatwierdza powierzenie", ""),
     ("7.", "", "Zapisuje powierzenie, aktualizuje godziny nieobsadzone zapotrzebowania i roczne rozliczenie pensum prowadzącego"),
     ("8.", "", "Informuje o zapisaniu powierzenia i odświeża listę zapotrzebowania"),
   ], None),
   ("Scenariusz alternatywny – niepoprawne dane", [
     ("4a", "", "Stwierdza, że dane są niekompletne lub niepoprawne"),
     ("5a", "", "Wyświetla komunikat przy błędnym polu"),
     ("", "", "Przejście do kroku 3"),
   ], None),
   ("Scenariusz alternatywny – ostrzeżenie o obciążeniu (RB6, RB7)", [
     ("5b", "", "Razem z podglądem wyświetla ostrzeżenie, że obciążenie przekroczy 125% (150%) pensum i wymaga pisemnej zgody nauczyciela, lub listę nauczycieli z brakującymi godzinami"),
     ("", "", "Przejście do kroku 6"),
   ], None),
   ("Scenariusz alternatywny – przekroczenie limitu maksymalnego (RB6)", [
     ("4c", "", "Stwierdza, że po zmianie godziny powierzone przekroczą maksymalny limit, a nauczyciel nie ma zgody prorektora"),
     ("5c", "", "Wyświetla komunikat o przekroczeniu limitu i blokuje zapis"),
     ("", "", "Przejście do kroku 3"),
   ], None),
   ("Wyjątek – rezygnacja", [
     ("6d", "Rezygnuje z powierzenia", ""),
     ("7d", "", "Odrzuca wprowadzone dane i wraca do listy zapotrzebowania"),
   ], "Uwaga. Osoba dokonująca powierzeń może zrezygnować w dowolnym momencie między krokami 2 i 6."),
  ],
  data=[
   ("p", "**Dane powierzenia:**"),
   ("b", "Forma zajęć kursu – obowiązkowa, wybór z zapotrzebowań planu z godzinami nieobsadzonymi > 0"),
   ("b", "Prowadzący – obowiązkowy, wybór z listy aktywnych prowadzących"),
   ("b", "Liczba godzin – obowiązkowa, liczba całkowita od 1 do liczby godzin nieobsadzonych"),
   ("b", "Liczba grup – opcjonalna, liczba nieujemna z krokiem 0,5; domyślnie liczba godzin podzielona przez liczbę godzin formy zajęć"),
   ("p", "**Komunikaty (5a, 5c):**"),
   ("b", "„Wybierz prowadzącego”"),
   ("b", "„Liczba godzin musi być liczbą całkowitą od 1 do {godziny nieobsadzone}”"),
   ("b", "„Liczba grup musi być nieujemną wielokrotnością 0,5”"),
   ("b", "„Powierzenie przekracza maksymalny limit godzin prowadzącego ({limit} h). Zmniejsz liczbę godzin lub wybierz innego prowadzącego”"),
   ("p", "**Reguły biznesowe:** RB3, RB6, RB7"),
  ]),
 dict(id="UC07", name="Sprawdź pensum i limity godzin", actor="— (przypadek włączany do UC06, UC08, UC19)", sec="—",
  desc="Przypadek wylicza obciążenie prowadzącego po planowanej zmianie powierzeń i porównuje je z pensum oraz limitami godzin.",
  pre="Przypadek wywołujący przekazał prowadzącego, rok akademicki oraz planowaną zmianę liczby godzin.",
  post="Przypadek wywołujący otrzymał wynik sprawdzenia (brak uwag, ostrzeżenie albo błąd) wraz z wartościami pensum.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "", "Pobiera roczne rozliczenie pensum nauczyciela akademickiego dla roku akademickiego semestru planu"),
     ("2.", "", "Wylicza godziny powierzone po zmianie: sumę godzin powierzeń nauczyciela w obu semestrach roku powiększoną o planowaną zmianę"),
     ("3.", "", "Wylicza pensum po zniżkach i procent jego realizacji (RB4, RB5)"),
     ("4.", "", "Stwierdza, że godziny po zmianie nie przekraczają progu wymagającego pisemnej zgody (RB6)"),
     ("5.", "", "Stwierdza, że godziny po zmianie nie przekraczają pensum po zniżkach albo żaden inny nauczyciel wydziału nie ma brakujących godzin (RB7)"),
     ("6.", "", "Zwraca wynik „brak uwag” wraz z wartościami pensum"),
   ], None),
   ("Scenariusz alternatywny – wymagana zgoda pisemna", [
     ("4a", "", "Stwierdza przekroczenie 125% (grupa badawczo-dydaktyczna) lub 150% (grupa dydaktyczna) pensum bazowego"),
     ("5a", "", "Dołącza do wyniku ostrzeżenie „wymagana pisemna zgoda nauczyciela”"),
     ("", "", "Przejście do kroku 5"),
   ], None),
   ("Scenariusz alternatywny – braki u innych nauczycieli", [
     ("5b", "", "Stwierdza godziny ponadwymiarowe, gdy inni nauczyciele mają brakujące godziny"),
     ("6b", "", "Zwraca ostrzeżenie z listą tych nauczycieli i ich brakujących godzin"),
   ], None),
   ("Scenariusz alternatywny – przekroczenie limitu maksymalnego", [
     ("4c", "", "Stwierdza przekroczenie 220% (grupa badawczo-dydaktyczna bez zgody prorektora), 200% lub 165% (lektor, instruktor) pensum bazowego"),
     ("5c", "", "Zwraca błąd z wartością limitu"),
   ], None),
   ("Scenariusz alternatywny – ekspert zewnętrzny", [
     ("1d", "", "Stwierdza, że prowadzący jest ekspertem zewnętrznym, który nie ma pensum"),
     ("2d", "", "Zwraca wynik „brak uwag”"),
   ], None),
  ],
  data=[
   ("p", "**Wejście:** prowadzący, rok akademicki, zmiana liczby godzin (liczba całkowita, ujemna przy zmniejszeniu powierzenia)."),
   ("p", "**Wyjście:** godziny powierzone przed zmianą i po niej, pensum po zniżkach, procent realizacji, rodzaj wyniku (brak uwag / ostrzeżenie / błąd), lista komunikatów."),
   ("p", "**Reguły biznesowe:** RB4, RB5, RB6, RB7"),
  ]),
 dict(id="UC11", name="Wyślij propozycje przydziałów", actor=ODP, sec="Serwer pocztowy",
  desc="Przypadek umożliwia wysłanie prowadzącym wiadomości e-mail z propozycjami przydziału zajęć na semestr wraz z terminem odpowiedzi.",
  pre="Osoba dokonująca powierzeń jest zalogowana. W semestrze jest co najmniej jeden prowadzący, którego powierzeń jeszcze nie wysłano lub które zmieniły się od wysłania ostatniej wersji propozycji. Wyświetlony jest ekran „Propozycje przydziałów”.",
  post="Dla każdego wybranego prowadzącego utworzono nową wersję propozycji ze statusem „wysłana”, datą wysłania i terminem odpowiedzi. Wiadomości przekazano do serwera pocztowego.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera wysyłkę propozycji", ""),
     ("2.", "", "Wyświetla prowadzących z nowymi lub zmienionymi powierzeniami (godziny w semestrze, procent pensum, rodzaj zmiany) – wszyscy zaznaczeni – oraz domyślny termin odpowiedzi (7 dni)"),
     ("3.", "Zaznacza prowadzących, ustala termin odpowiedzi i opcjonalnie wpisuje dodatkową informację", ""),
     ("4.", "", "Weryfikuje dane i wyświetla podgląd wiadomości dla wybranego prowadzącego"),
     ("5.", "Zatwierdza wysyłkę", ""),
     ("6.", "", "Tworzy nowe wersje propozycji (pozycje są kopią bieżących powierzeń) i przekazuje wiadomości do serwera pocztowego"),
     ("7.", "", "Wyświetla podsumowanie z liczbą wysłanych propozycji"),
   ], None),
   ("Scenariusz alternatywny – niepoprawne dane", [
     ("4a", "", "Stwierdza, że nie zaznaczono żadnego prowadzącego lub termin odpowiedzi jest niepoprawny"),
     ("5a", "", "Wyświetla komunikat"),
     ("", "", "Przejście do kroku 3"),
   ], None),
   ("Scenariusz alternatywny – błąd wysyłki", [
     ("6b", "", "Otrzymuje od serwera pocztowego informację o nieudanej wysyłce części wiadomości"),
     ("7b", "", "Nie tworzy wersji dla tych prowadzących, wyświetla ich listę z przyczyną i umożliwia ponowienie wysyłki"),
     ("", "", "Przejście do kroku 2"),
   ], None),
   ("Wyjątek – rezygnacja", [
     ("5c", "Rezygnuje z wysyłki", ""),
     ("6c", "", "Wraca do listy propozycji; żadna wiadomość nie zostaje wysłana"),
   ], "Uwaga. Rezygnacja jest możliwa w dowolnym momencie między krokami 2 i 5."),
  ],
  data=[
   ("p", "**Dane wysyłki:**"),
   ("b", "Prowadzący – co najmniej jeden zaznaczony"),
   ("b", "Termin odpowiedzi – obowiązkowy, data od dnia następnego do 5 dni przed terminem przekazania powierzeń semestru (22.10 / 20.03)"),
   ("b", "Dodatkowa informacja – opcjonalna, tekst do 1000 znaków"),
   ("p", "**Treść wiadomości:** tytuł i nazwisko prowadzącego, numer wersji, pozycje (kod i nazwa kursu, forma, liczba grup, liczba godzin), suma godzin w semestrze i w roku oraz procent pensum, termin odpowiedzi, odnośnik do formularza odpowiedzi, informacja o milczącej zgodzie, a gdy wersja wymaga zgody pisemnej – informacja, że milcząca zgoda nie wystarcza."),
   ("p", "**Komunikaty (5a, 7b):**"),
   ("b", "„Wybierz co najmniej jednego prowadzącego”"),
   ("b", "„Termin odpowiedzi musi przypadać między {jutro} a {termin przekazania − 5 dni}”"),
   ("b", "„Nie udało się wysłać wiadomości do: {lista}”"),
   ("p", "**Reguły biznesowe:** RB8, RB9"),
  ]),
 dict(id="UC12", name="Odpowiedz na propozycję przydziału", actor="Prowadzący", sec="Usługa AI (od wydania 1.1), Serwer pocztowy",
  desc="Przypadek umożliwia prowadzącemu zaakceptowanie propozycji przydziału albo zgłoszenie uwag do jej pozycji.",
  pre="Prowadzący otrzymał wiadomość z propozycją. Wersja propozycji ma status „wysłana”, a termin odpowiedzi nie upłynął.",
  post="Odpowiedź została zarejestrowana. Wersja propozycji ma status „zaakceptowana” albo „zgłoszono uwagi”.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Otwiera odnośnik z wiadomości", ""),
     ("2.", "", "Wyświetla propozycję: pozycje, sumę godzin, stan pensum w roku akademickim i termin odpowiedzi"),
     ("3.", "Wybiera „Akceptuję”", ""),
     ("4.", "", "Prosi o potwierdzenie; gdy wersja wymaga zgody pisemnej, wyświetla pole zgody na obciążenie ponad 125% (150%) pensum"),
     ("5.", "Potwierdza akceptację (i zaznacza zgodę, jeśli jest wymagana)", ""),
     ("6.", "", "Rejestruje odpowiedź rodzaju „zgoda”, ustawia status wersji „zaakceptowana” i datę akceptacji"),
     ("7.", "", "Wyświetla potwierdzenie i wysyła je prowadzącemu e-mailem"),
   ], None),
   ("Scenariusz alternatywny – zgłoszenie uwag", [
     ("3a", "Wybiera „Zgłaszam uwagi”", ""),
     ("4a", "", "Wyświetla przy każdej pozycji wybór (bez zmian / rezygnuję / zmiana liczby godzin / inna zmiana) i pole treści uwag"),
     ("5a", "Oznacza pozycje i wpisuje treść uwag", ""),
     ("6a", "", "Weryfikuje dane, rejestruje odpowiedź z uwagami do pozycji i ustawia status wersji „zgłoszono uwagi”"),
     ("7a", "", "Przekazuje treść do usługi AI, która rozpoznaje rodzaj odpowiedzi i wskazuje pozycje; zapisuje wynik jako sugestię"),
     ("8a", "", "Wyświetla potwierdzenie przyjęcia uwag i powiadamia osobę dokonującą powierzeń"),
   ], None),
   ("Scenariusz alternatywny – niepoprawne dane", [
     ("6b", "", "Stwierdza brak wymaganej zgody pisemnej, brak treści uwag lub brak oznaczonej pozycji"),
     ("7b", "", "Wyświetla komunikat"),
     ("", "", "Powrót do kroku 4 (akceptacja) lub 5a (uwagi)"),
   ], None),
   ("Scenariusz alternatywny – odpowiedź e-mailem", [
     ("1c", "Odpowiada na wiadomość zwykłym e-mailem", ""),
     ("2c", "", "Odbiera wiadomość od serwera pocztowego, przypisuje ją do najnowszej wersji propozycji nadawcy, rejestruje odpowiedź i ustawia status „zgłoszono uwagi” do czasu weryfikacji (UC15)"),
     ("", "", "Przejście do kroku 7a"),
   ], None),
   ("Wyjątki", [
     ("2d", "", "Stwierdza, że termin odpowiedzi upłynął lub istnieje nowsza wersja propozycji"),
     ("3d", "", "Wyświetla status wersji (np. „zaakceptowana milcząco”) i odnośnik do najnowszej wersji; koniec przypadku"),
     ("7e", "", "Usługa AI jest niedostępna: oznacza odpowiedź do ręcznej weryfikacji"),
     ("", "", "Przejście do kroku 8a"),
   ], None),
  ],
  data=[
   ("p", "**Dane odpowiedzi:**"),
   ("b", "Wybór dla pozycji – przy zgłaszaniu uwag co najmniej jedna pozycja inna niż „bez zmian”"),
   ("b", "Treść uwag – obowiązkowa przy zgłaszaniu uwag, 10–2000 znaków"),
   ("b", "Zgoda na obciążenie – pole wyboru, obowiązkowe przy akceptacji wersji wymagającej zgody pisemnej"),
   ("p", "**Komunikaty (7b, 3d):**"),
   ("b", "„Aby zaakceptować propozycję, potwierdź zgodę na obciążenie powyżej {próg}% pensum”"),
   ("b", "„Wskaż co najmniej jedną pozycję, której dotyczą uwagi”"),
   ("b", "„Opisz uwagi (10–2000 znaków)”"),
   ("b", "„Termin odpowiedzi upłynął {data}. Propozycja została zaakceptowana milcząco”"),
   ("p", "**Reguły biznesowe:** RB6, RB8, RB9"),
  ]),
 dict(id="UC15", name="Rozwiąż uwagę prowadzącego", actor=ODP, sec="Serwer pocztowy, Usługa AI",
  desc="Przypadek umożliwia rozpatrzenie uwag prowadzącego przez zmianę powierzeń albo odrzucenie uwagi z uzasadnieniem. Prowadzący otrzymuje nową wersję propozycji.",
  pre="Istnieje wersja propozycji ze statusem „zgłoszono uwagi”. Wyświetlony jest ekran „Uwagi do rozwiązania”.",
  post="Zapisano rozwiązanie uwagi, wersja ma status „rozwiązana”, a prowadzący, których powierzenia się zmieniły, otrzymali nowe wersje propozycji.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera uwagę z listy", ""),
     ("2.", "", "Wyświetla treść odpowiedzi, pozycje, których dotyczy, wynik analizy AI (rodzaj, pozycje, pewność, propozycję rozwiązania) i stan pensum prowadzącego"),
     ("3.", "Wybiera „Zmień powierzenia”", ""),
     ("4.", "", "Wyświetla powierzenia prowadzącego z możliwością edycji"),
     ("5.", "Zmienia lub usuwa powierzenia, np. przenosi godziny do innego prowadzącego (UC08)", ""),
     ("6.", "Wpisuje komentarz dla prowadzącego i zatwierdza rozwiązanie", ""),
     ("7.", "", "Zapisuje rozwiązanie, ustawia status wersji „rozwiązana” i wyświetla prowadzących, których powierzenia zmieniono"),
     ("8.", "", "Wysyła im nowe wersje propozycji z komentarzem i nowym terminem odpowiedzi (UC11, kroki 3–7)"),
     ("9.", "", "Usuwa uwagę z listy oczekujących"),
   ], None),
   ("Scenariusz alternatywny – odrzucenie uwagi", [
     ("3a", "Wybiera „Odrzuć uwagę”", ""),
     ("4a", "", "Prosi o uzasadnienie"),
     ("5a", "Wpisuje uzasadnienie i zatwierdza", ""),
     ("6a", "", "Weryfikuje uzasadnienie, zapisuje rozwiązanie „odrzucenie”, ustawia status „rozwiązana” i wysyła prowadzącemu nową wersję propozycji o niezmienionych pozycjach z uzasadnieniem i nowym terminem"),
     ("", "", "Przejście do kroku 9"),
   ], None),
   ("Scenariusz alternatywny – korekta rozpoznania AI", [
     ("2b", "Wybiera „Popraw rozpoznanie” i zmienia rodzaj odpowiedzi lub wskazane pozycje", ""),
     ("3b", "", "Zapisuje analizę jako zweryfikowaną; jeśli ustalono rodzaj „zgoda”, ustawia status wersji „zaakceptowana” i kończy przypadek"),
     ("", "", "W przeciwnym razie przejście do kroku 3"),
   ], None),
   ("Wyjątek – przerwanie", [
     ("", "Przerywa rozwiązywanie przed krokiem 6", ""),
     ("", "", "Zmiany powierzeń zapisane w kroku 5 pozostają, a uwaga zostaje na liście oczekujących"),
   ], None),
  ],
  data=[
   ("b", "Komentarz dla prowadzącego – opcjonalny, do 1000 znaków"),
   ("b", "Uzasadnienie odrzucenia – obowiązkowe przy odrzuceniu, 10–1000 znaków"),
   ("b", "Termin odpowiedzi nowej wersji – jak w UC11"),
   ("p", "**Komunikat (6a):** „Podaj uzasadnienie odrzucenia (10–1000 znaków)”"),
   ("p", "**Reguły biznesowe:** RB8, RB9. Wynik AI jest tylko sugestią – decyzję podejmuje osoba dokonująca powierzeń."),
  ]),
 dict(id="UC16", name="Zatwierdź plan powierzeń", actor=ODP, sec="Serwer pocztowy",
  desc="Przypadek umożliwia zatwierdzenie kompletnego planu powierzeń i udostępnienie go działowi kształcenia.",
  pre="Osoba dokonująca powierzeń jest zalogowana. Plan powierzeń wybranej instancji planu i semestru nie jest zatwierdzony. Wyświetlony jest ekran planu powierzeń.",
  post="Ustawiono datę zatwierdzenia i przekazania planu. Plan jest zablokowany do edycji (poza dorabianiem grup – UC19). Dział kształcenia otrzymał powiadomienie i może pobrać powierzenia (UC17).",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera „Zatwierdź plan”", ""),
     ("2.", "", "Sprawdza warunki RB10: pełne obsadzenie każdego zapotrzebowania (godziny i grupy), status najnowszej wersji każdej propozycji „zaakceptowana” lub „zaakceptowana milcząco”, brak przekroczeń limitów i obecność wymaganych zgód pisemnych (RB6), brak godzin ponadwymiarowych przy brakach u innych nauczycieli (RB7), termin przekazania"),
     ("3.", "", "Wyświetla listę spełnionych warunków i podsumowanie planu (kursy, powierzenia, prowadzący, godziny); prosi o potwierdzenie"),
     ("4.", "Potwierdza zatwierdzenie", ""),
     ("5.", "", "Ustawia datę zatwierdzenia i przekazania, blokuje plan do edycji"),
     ("6.", "", "Wysyła powiadomienie do działu kształcenia"),
     ("7.", "", "Informuje o zatwierdzeniu planu"),
   ], None),
   ("Scenariusz alternatywny – warunki niespełnione", [
     ("3a", "", "Wyświetla listę warunków z oznaczonymi niespełnionymi i odnośnikami do braków; zatwierdzenie jest niedostępne"),
     ("4a", "Zamyka okno lub przechodzi do wskazanego braku", ""),
     ("", "", "Koniec przypadku"),
   ], None),
   ("Scenariusz alternatywny – po terminie", [
     ("3b", "", "Stwierdza, że pozostałe warunki są spełnione, ale termin przekazania minął; wyświetla ostrzeżenie i prosi o przyczynę opóźnienia"),
     ("4b", "Podaje przyczynę i potwierdza", ""),
     ("", "", "Przejście do kroku 5"),
   ], None),
   ("Wyjątek – rezygnacja", [
     ("4c", "Rezygnuje z zatwierdzenia", ""),
     ("5c", "", "Zamyka okno bez zmian"),
   ], None),
  ],
  data=[
   ("b", "Przyczyna opóźnienia – obowiązkowa w scenariuszu „po terminie”, 10–500 znaków"),
   ("p", "**Komunikaty (3a, 3b):**"),
   ("b", "„Nie można zatwierdzić planu. Do uzupełnienia: {lista braków}”"),
   ("b", "„Termin przekazania powierzeń minął {data}. Podaj przyczynę opóźnienia”"),
   ("p", "**Reguły biznesowe:** RB6, RB7, RB10. System nie blokuje zatwierdzenia po terminie (powierzenia i tak muszą trafić do działu kształcenia), ale rejestruje opóźnienie i jego przyczynę."),
  ]),
 dict(id="UC19", name="Dorób grupę", actor=ODP, sec="Serwer pocztowy",
  desc="Przypadek umożliwia zwiększenie liczby grup formy zajęć i powierzenie godzin nowej grupy prowadzącemu, również po zatwierdzeniu planu powierzeń.",
  pre="Osoba dokonująca powierzeń jest zalogowana i wybrała plan powierzeń. Opcjonalnie istnieje zgłoszenie działu kształcenia (UC18).",
  post="Zwiększono rzeczywistą liczbę grup zapotrzebowania i zapisano powierzenie godzin nowej grupy (oznaczone jako „po zatwierdzeniu”, jeśli plan był zatwierdzony). Prowadzący otrzymał nową wersję propozycji, zgłoszenie ma status „zrealizowane”, a dział kształcenia został powiadomiony.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera formę zajęć kursu (z planu lub ze zgłoszenia) i opcję „Dorób grupę”", ""),
     ("2.", "", "Wyświetla liczbę studentów, bieżącą liczbę grup, liczbę godzin na grupę i treść zgłoszenia; proponuje 1 dodatkową grupę"),
     ("3.", "Podaje liczbę dodatkowych grup i ewentualnie aktualną liczbę studentów", ""),
     ("4.", "", "Weryfikuje dane, wylicza dodatkowe godziny i wyświetla prowadzących, którzy mogą prowadzić kurs, uporządkowanych według brakujących godzin"),
     ("5.", "Wybiera prowadzącego", ""),
     ("6.", "", "Sprawdza pensum i limity godzin (UC07) i wyświetla wynik"),
     ("7.", "Zatwierdza dorobienie grupy", ""),
     ("8.", "", "Zapisuje nową liczbę grup i powierzenie, oznacza zgłoszenie jako zrealizowane"),
     ("9.", "", "Wysyła prowadzącemu nową wersję propozycji z terminem odpowiedzi 3 dni (UC11) i powiadamia dział kształcenia"),
   ], None),
   ("Scenariusz alternatywny – niepoprawne dane", [
     ("4a", "", "Stwierdza niepoprawną liczbę grup lub studentów i wyświetla komunikat"),
     ("", "", "Przejście do kroku 3"),
   ], None),
   ("Scenariusz alternatywny – wynik sprawdzenia pensum", [
     ("6b", "", "UC07 zwrócił błąd: wyświetla komunikat o przekroczeniu limitu; przejście do kroku 5"),
     ("6c", "", "UC07 zwrócił ostrzeżenie: wyświetla je; przejście do kroku 7"),
   ], None),
   ("Scenariusz alternatywny – powierzenie później", [
     ("5d", "Wybiera „Powierz później” (tylko dla niezatwierdzonego planu)", ""),
     ("6d", "", "Zapisuje nową liczbę grup; godziny nowej grupy są widoczne jako nieobsadzone (UC06)"),
   ], None),
   ("Wyjątek – rezygnacja", [
     ("", "Rezygnuje przed krokiem 7", ""),
     ("", "", "Zamyka okno; nic nie zostaje zapisane"),
   ], None),
  ],
  data=[
   ("b", "Liczba dodatkowych grup – obowiązkowa, liczba całkowita od 1 do 10"),
   ("b", "Liczba studentów – opcjonalna, liczba całkowita nie mniejsza niż dotychczasowa"),
   ("b", "Prowadzący – obowiązkowy (poza scenariuszem „powierzenie później”)"),
   ("p", "**Komunikaty (4a, 6b):**"),
   ("b", "„Liczba dodatkowych grup musi być liczbą całkowitą od 1 do 10”"),
   ("b", "„Liczba studentów nie może być mniejsza niż dotychczasowa ({n})”"),
   ("b", "„Powierzenie przekracza maksymalny limit godzin prowadzącego ({limit} h)”"),
   ("p", "**Reguły biznesowe:** RB3, RB6, RB7, RB9"),
  ]),
]

STYLE_RULES = [
    "Źródła: 10 heurystyk użyteczności J. Nielsena, wytyczne WCAG 2.1 na poziomie AA (wymaganie z wizji) oraz kolorystyka nawiązująca do identyfikacji wizualnej PWr (czerwień jako kolor akcentu).",
]
STYLE_LIST = [
    "Stały układ ekranu: górny pasek (nazwa systemu, rok i semestr, instancja planu, użytkownik i rola), menu boczne z liczbą spraw oczekujących, obszar roboczy.",
    "Na ekranie jest jedna akcja główna (przycisk wypełniony); pozostałe akcje to przyciski obrysowane. Akcje nieodwracalne lub wysyłające wiadomości wymagają potwierdzenia w oknie z podsumowaniem skutków.",
    "Stan zawsze pokazuje kolor i tekst jednocześnie (WCAG 1.4.1): zielony – poprawny lub zaakceptowany, żółty – ostrzeżenie lub brak, czerwony – błąd lub blokada, niebieski – informacja lub stan w toku.",
    "Realizację pensum przedstawia pasek postępu z wartościami „godziny / pensum (procent)”.",
    "Pola obowiązkowe oznacza gwiazdka. Komunikat walidacji pojawia się przy polu i mówi po polsku, co poprawić.",
    "Ostrzeżenia regulaminowe wskazują regułę biznesową, z której wynikają (np. RB6).",
    "Wyniki AI są wyświetlane w wyróżnionej ramce z etykietą „Sugestia AI” i pewnością rozpoznania; system nie stosuje ich bez decyzji użytkownika.",
    "Formaty: data dd.mm.rrrr, godziny jako liczby całkowite z jednostką „h”, spacja jako separator tysięcy.",
    "Ekrany dla prowadzących (otwierane z odnośnika w wiadomości) są uproszczone: bez menu bocznego, jedna propozycja na ekranie.",
]

PROTOS = [
    ("UC09", "p9_stan.png", "Ekran stanu powierzeń – widok „Pensum” z filtrem „tylko problemy”", [
        "U góry podsumowanie semestru (krok 2); kafelki z problemami są wyróżnione kolorem i liczbą.",
        "Przełączniki „Obsada”, „Pensum”, „Propozycje” i filtry (krok 3); na rysunku widok „Pensum” z filtrem „tylko problemy”.",
        "Każdy wiersz pokazuje pasek realizacji pensum, procent, stan i status propozycji (krok 4); wybór wiersza otwiera szczegóły nauczyciela (kroki 5–6).",
        "W widoku „Obsada” przy nieobsadzonych formach zajęć jest przycisk „Powierz zajęcia” (scenariusz „przejście do powierzenia”).",
    ]),
    ("UC17", "p17_pobierz.png", "Ekran zatwierdzonych powierzeń działu kształcenia", [
        "Lista planów powierzeń semestru (krok 2); plan w przygotowaniu nie może zostać zaznaczony (RB10).",
        "Kolumna „Zmiany po zatw.” informuje o powierzeniach dodanych po zatwierdzeniu, np. dorobionych grupach.",
        "Prawa część: format pliku i zakres, w tym „tylko zmiany od” (krok 3, scenariusz „tylko zmiany”); pod spodem nazwa i rozmiar pliku.",
        "Podgląd pierwszych wierszy pliku (krok 4) pozwala sprawdzić zawartość przed pobraniem (kroki 5–7).",
    ]),
    ("UC06", "p1_powierz.png", "Ekran planu powierzeń z formularzem nowego powierzenia", [
        "Lewa część pokazuje zapotrzebowanie planu z godzinami obsadzonymi i brakującymi (kroki 1–2); stan każdej pozycji opisuje etykieta.",
        "Prawa część to formularz powierzenia (krok 3) z listą prowadzących, którzy mogą prowadzić kurs, ich pensum i preferencjami.",
        "Podgląd pensum po zmianie oraz ostrzeżenia RB6 i RB7 (kroki 4–5, scenariusz „ostrzeżenie o obciążeniu”) odświeżają się po każdej zmianie liczby godzin.",
        "Przy przekroczeniu limitu maksymalnego ramka ostrzeżenia staje się czerwona, a przycisk „Zapisz powierzenie” jest nieaktywny.",
    ]),
    ("UC11", "p2_wyslij.png", "Ekran wysyłki propozycji przydziałów", [
        "Lista prowadzących z nowymi lub zmienionymi powierzeniami; zaznaczenie decyduje o wysyłce (kroki 2–3).",
        "Pod listą termin odpowiedzi z podpowiedzią dopuszczalnego zakresu i pole dodatkowej informacji.",
        "Prawa część to podgląd wiadomości dla wskazanego prowadzącego (krok 4); widać w nim informację o wymaganej zgodzie pisemnej.",
        "Przycisk akcji głównej pokazuje liczbę wysyłanych propozycji (krok 5).",
    ]),
    ("UC12", "p3_odpowiedz.png", "Strona odpowiedzi prowadzącego – zgłaszanie uwag", [
        "Strona otwierana z odnośnika w wiadomości; pokazano stan po wybraniu „Zgłaszam uwagi” (kroki 4a–5a).",
        "Przy każdej pozycji prowadzący wybiera rodzaj uwagi; treść uwag jest obowiązkowa.",
        "Po wybraniu „Akceptuję” system wyświetla okno potwierdzenia, a przy obciążeniu ponad 125% (150%) pensum – pole zgody pisemnej (krok 4).",
    ]),
    ("UC15", "p4_uwagi.png", "Ekran uwag do rozwiązania z sugestią AI", [
        "Lista uwag oczekujących z pewnością rozpoznania AI; odpowiedzi niejednoznaczne oznaczono „do weryfikacji” (krok 1).",
        "Szczegóły uwagi: treść odpowiedzi i ramka „Sugestia AI” z rozpoznanym rodzajem, pozycjami i możliwym rozwiązaniem (krok 2); „Popraw rozpoznanie” realizuje scenariusz korekty.",
        "„Zmień powierzenia” przenosi do planu powierzeń przefiltrowanego do powierzeń prowadzącego (kroki 3–5); „Odrzuć uwagę” otwiera okno z polem uzasadnienia.",
    ]),
    ("UC16", "p5_zatwierdz.png", "Okno zatwierdzania planu powierzeń – warunki niespełnione", [
        "Okno pokazuje wynik sprawdzenia warunków RB10 (krok 2); na rysunku – scenariusz „warunki niespełnione”.",
        "Braki są odnośnikami prowadzącymi do odpowiedniej pozycji planu lub uwagi; przycisk „Zatwierdź plan” jest nieaktywny.",
        "Gdy wszystkie warunki są spełnione, okno pokazuje podsumowanie planu, a przycisk jest aktywny (kroki 3–4).",
    ]),
    ("UC19", "p6_dorob.png", "Okno dorabiania grupy na podstawie zgłoszenia działu kształcenia", [
        "Okno otwarte ze zgłoszenia działu kształcenia dla zatwierdzonego planu; u góry treść zgłoszenia (krok 2).",
        "Liczba dodatkowych grup i studentów (krok 3); system od razu wylicza grupy i godziny po zmianie.",
        "Prowadzący uporządkowani według brakujących godzin (kroki 4–5); pod listą wynik sprawdzenia pensum i limitów (krok 6).",
        "Dla zatwierdzonego planu opcja „Powierz później” jest niedostępna.",
    ]),
]

MODEL_INTRO = "Model informacyjny rozwija model domenowy z etapu I do poziomu danych przechowywanych w systemie: wszystkie asocjacje mają nazwy i krotności, atrybuty mają typy, a dziedziny wartości opisują typy wyliczeniowe. Model podzielono na trzy diagramy; klasy pochodzące z innej części pokazano bez atrybutów („...”)."
MODEL_CHANGES = [
    "Usunięto klasy reprezentujące role (Osoba dokonująca powierzeń, Dział kształcenia). Uprawnienia użytkowników opisuje atrybut rola klasy Konto użytkownika.",
    "Dodano klasę Kierunek; Plan studiów należy do kierunku.",
    "Instancja planu jest połączona z kursami przez Plan powierzeń i Zapotrzebowanie (Zapotrzebowanie → Forma zajęć → Kurs → Etap), co daje ścieżkę nawigacji od formy zajęć do etapu wymaganą przez RB2.",
    "Powierzane są godziny, nie grupy: liczbaGodzin Powierzenia zajęć jest obowiązkowa, a liczbaGrup ma typ Decimal i może być ułamkowa lub równa 0.",
    "Propozycja przydziału ma wersje. terminOdpowiedzi należy do Wersji propozycji – ustala go każda wysyłka (także po rozwiązaniu uwagi), a każda Odpowiedź dotyczy jednej wersji.",
    "Model obejmuje wiele lat akademickich. Roczne rozliczenie pensum jest powiązane z Rokiem akademickim (co najwyżej jedno na nauczyciela w danym roku), dlatego nauczyciel ma po jednym rozliczeniu na każdy rok.",
    "Dodano klasy i atrybuty wymagane przez przypadki użycia: Zgłoszenie dorobienia grupy (UC18, UC19), Uwaga do pozycji i Analiza AI (UC12), Rozwiązanie uwagi (UC15), atrybut przyczynaOpoznienia Planu powierzeń (UC16) oraz dataModyfikacji Powierzenia zajęć, potrzebny do pobierania samych zmian (UC17).",
]
MODEL_DERIVED = [
    "Zapotrzebowanie: /przewidywanaLiczbaGrup i /liczbaGodzin – wg RB3; /liczbaGrup = rzeczywistaLiczbaGrup, a gdy jej brak – /przewidywanaLiczbaGrup; /godzinyNieobsadzone = /liczbaGodzin − suma liczbaGodzin jego Powierzeń zajęć.",
    "Roczne rozliczenie pensum: /pensumBazowe – wg RB4; /pensumPoZnizkach = /pensumBazowe − suma liczbaGodzin Zniżek pensum (z ograniczeniem RB5); /godzinyPowierzone = suma liczbaGodzin Powierzeń zajęć nauczyciela w Planach powierzeń obu Semestrów roku; /brakujaceGodziny = max(0, /pensumPoZnizkach − /godzinyPowierzone); /godzinyPonadwymiarowe = max(0, /godzinyPowierzone − /pensumPoZnizkach).",
    "Plan powierzeń: /status = zatwierdzony, gdy dataZatwierdzenia jest ustawiona, w przeciwnym razie wPrzygotowaniu.",
    "Propozycja przydziału: /aktualnaWersja = największy numer jej Wersji propozycji.",
]
MODEL_CONSTRAINTS = [
    "Plan powierzeń jest unikalny dla pary (Instancja planu, Semestr) – RB1; Zapotrzebowanie – dla pary (Plan powierzeń, Forma zajęć).",
    "Propozycja przydziału jest unikalna dla pary (Prowadzący, Semestr); Roczne rozliczenie pensum – dla pary (Nauczyciel akademicki, Rok akademicki).",
    "Wersja propozycji.wymagaZgodyPisemnej jest ustalana przy wysyłce: prawda, gdy godziny powierzone w roku przekraczają 125% (grupa badawczo-dydaktyczna) lub 150% (grupa dydaktyczna) pensum bazowego – RB6.",
    "Konto użytkownika nie musi być powiązane z Prowadzącym (np. pracownik działu kształcenia, administrator); prowadzący bez konta odpowiada przez odnośnik z jednorazowym tokenem.",
]


UCS += [
 dict(id="UC09", name="Przeglądaj stan powierzeń", actor=ODP, sec="—",
  desc="Przypadek umożliwia przegląd stanu powierzeń w semestrze: obsady form zajęć, realizacji pensum nauczycieli i statusów propozycji przydziału, z możliwością filtrowania i przejścia do szczegółów.",
  pre="Osoba dokonująca powierzeń jest zalogowana.",
  post="Dane w systemie nie zmieniają się (przypadek służy tylko do odczytu).",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera „Stan powierzeń” i semestr", ""),
     ("2.", "", "Wyświetla podsumowanie semestru: godziny do powierzenia, obsadzone i nieobsadzone, liczbę nauczycieli bez pełnego pensum i powyżej progu zgody pisemnej, liczbę propozycji według statusu"),
     ("3.", "Wybiera widok: „Obsada”, „Pensum” lub „Propozycje” i ustawia filtry", ""),
     ("4.", "", "Wyświetla zestawienie dla widoku i aktualizuje podsumowanie: Obsada – formy zajęć z godzinami obsadzonymi i brakującymi; Pensum – nauczyciele z godzinami w roku, pensum po zniżkach, procentem i stanem; Propozycje – prowadzący z numerem wersji, statusem i terminem odpowiedzi"),
     ("5.", "Wybiera pozycję zestawienia", ""),
     ("6.", "", "Wyświetla szczegóły: dla formy zajęć – jej powierzenia; dla nauczyciela – powierzenia w obu semestrach roku i zniżki pensum; dla propozycji – wersje i odpowiedzi"),
     ("7.", "Kończy przeglądanie lub wraca do kroku 3", ""),
   ], None),
   ("Scenariusz alternatywny – brak pozycji dla filtrów", [
     ("4a", "", "Stwierdza, że żadna pozycja nie spełnia kryteriów"),
     ("5a", "", "Wyświetla informację i proponuje wyczyszczenie filtrów"),
     ("", "", "Przejście do kroku 3"),
   ], None),
   ("Scenariusz alternatywny – przejście do powierzenia", [
     ("7b", "Przy nieobsadzonej formie zajęć wybiera „Powierz zajęcia” (tylko dla niezatwierdzonego planu)", ""),
     ("8b", "", "Uruchamia UC06 dla wybranej formy zajęć"),
   ], None),
   ("Wyjątek – brak planów", [
     ("2c", "", "Stwierdza, że w semestrze nie ma planów powierzeń"),
     ("3c", "", "Wyświetla informację; koniec przypadku"),
   ], None),
  ],
  data=[
   ("p", "**Filtry (wszystkie opcjonalne):**"),
   ("b", "Instancja planu – wybór z listy instancji z planem powierzeń w semestrze; domyślnie wszystkie"),
   ("b", "Kurs – kod lub fragment nazwy, do 100 znaków"),
   ("b", "Prowadzący – fragment imienia lub nazwiska, do 100 znaków"),
   ("b", "„Tylko problemy” – pokazuje formy z nieobsadzonymi godzinami, nauczycieli z brakującymi godzinami lub powyżej progu zgody, propozycje z uwagami lub bez odpowiedzi"),
   ("p", "**Stan pozycji** jest oznaczany kolorem i tekstem wg przewodnika stylu (np. „brakuje 30 h”, „144% – wymaga zgody”)."),
   ("p", "**Komunikaty (5a, 3c):**"),
   ("b", "„Brak pozycji spełniających kryteria. Wyczyść filtry”"),
   ("b", "„W semestrze {semestr} nie utworzono jeszcze planów powierzeń”"),
   ("p", "**Reguły biznesowe:** RB3 (liczba grup i godzin), RB4–RB7 (pensum i limity), RB8 (statusy propozycji)"),
  ]),
 dict(id="UC17", name="Pobierz zatwierdzone powierzenia", actor="Pracownik działu kształcenia", sec="—",
  desc="Przypadek umożliwia pracownikowi działu kształcenia pobranie zatwierdzonych powierzeń semestru w pliku XLSX lub CSV, na podstawie którego układa plan zajęć.",
  pre="Pracownik działu kształcenia jest zalogowany. Wyświetlony jest ekran „Zatwierdzone powierzenia” wybranego semestru.",
  post="Pracownik pobrał plik z powierzeniami wybranych zatwierdzonych planów. Dane w systemie nie zmieniają się.",
  flows=[
   ("Główny przepływ zdarzeń", [
     ("1.", "Wybiera „Zatwierdzone powierzenia”", ""),
     ("2.", "", "Wyświetla plany powierzeń semestru: zatwierdzone (data zatwierdzenia, liczba powierzeń, godziny, liczba zmian po zatwierdzeniu) – zaznaczone – oraz plany w przygotowaniu bez możliwości wyboru"),
     ("3.", "Zaznacza plany, wybiera format pliku i zakres „wszystkie powierzenia”", ""),
     ("4.", "", "Weryfikuje wybór i wyświetla podgląd pierwszych wierszy oraz nazwę i rozmiar pliku (liczba powierzeń, godzin, planów)"),
     ("5.", "Wybiera „Pobierz”", ""),
     ("6.", "", "Generuje plik i udostępnia go do pobrania"),
     ("7.", "Pobiera plik", ""),
   ], None),
   ("Scenariusz alternatywny – tylko zmiany", [
     ("3a", "Wybiera zakres „tylko zmiany od” i podaje datę", ""),
     ("4a", "", "Ogranicza plik do powierzeń dodanych lub zmienionych po tej dacie (np. dorobione grupy)"),
     ("", "", "Przejście do kroku 5"),
   ], None),
   ("Scenariusz alternatywny – niepoprawny wybór", [
     ("4b", "", "Stwierdza, że nie zaznaczono planu, data jest niepoprawna lub w zakresie nie ma powierzeń"),
     ("5b", "", "Wyświetla komunikat"),
     ("", "", "Przejście do kroku 3"),
   ], None),
   ("Wyjątki", [
     ("2c", "", "W semestrze nie ma zatwierdzonego planu: wyświetla informację i plany w przygotowaniu z terminem przekazania; koniec przypadku"),
     ("6d", "", "Generowanie pliku nie powiodło się: wyświetla komunikat i umożliwia ponowienie (przejście do kroku 5)"),
   ], None),
  ],
  data=[
   ("p", "**Dane wyboru:**"),
   ("b", "Plany powierzeń – co najmniej jeden zatwierdzony"),
   ("b", "Format – obowiązkowy: XLSX albo CSV (UTF-8, separator średnik)"),
   ("b", "Data „zmiany od” – obowiązkowa w zakresie „tylko zmiany”, format dd.mm.rrrr, nie późniejsza niż dzisiejsza"),
   ("p", "**Zawartość pliku** (jeden wiersz na powierzenie): kierunek, stopień i forma studiów, instancja planu, semestr, kod i nazwa kursu, forma zajęć, liczba grup, liczba godzin, tytuł, imię, nazwisko i e-mail prowadzącego, znacznik „po zatwierdzeniu”. Nazwa pliku: powierzenia_{rok}{Z|L}_{data}.xlsx/csv."),
   ("p", "**Komunikaty (5b, 2c, 6d):**"),
   ("b", "„Zaznacz co najmniej jeden plan powierzeń”"),
   ("b", "„Brak zmian w powierzeniach od {data}”"),
   ("b", "„Żaden plan powierzeń semestru {semestr} nie jest jeszcze zatwierdzony”"),
   ("b", "„Nie udało się wygenerować pliku. Spróbuj ponownie”"),
   ("p", "**Reguły biznesowe:** RB10 – pobrać można wyłącznie zatwierdzone plany powierzeń."),
  ]),
]

KEEP = ["UC06", "UC09", "UC12", "UC17"]
ACT = {
    "UC06": ("act/a06.png", "Pętla obejmuje kroki 3–5 oraz scenariusze „niepoprawne dane” i „przekroczenie limitu maksymalnego”. Ostrzeżenia RB6 i RB7 nie blokują zatwierdzenia powierzenia. Sprawdzenie pensum realizuje włączany przypadek UC07."),
    "UC12": ("act/a12.png", "Diagram pokazuje akceptację (przepływ główny) i zgłoszenie uwag (scenariusz alternatywny); pętle odpowiadają scenariuszowi „niepoprawne dane”. Odpowiedź zwykłym e-mailem dołącza do ścieżki uwag w kroku 7a i dla czytelności została pominięta."),
    "UC09": ("act/a09.png", "Przypadek służy tylko do odczytu, więc nie ma kroków zatwierdzania. Pętla odpowiada wielokrotnej zmianie widoku i filtrów; ze szczegółów nieobsadzonej formy zajęć można przejść bezpośrednio do UC06."),
    "UC17": ("act/a17.png", "Pętla odpowiada scenariuszowi „niepoprawny wybór”. Pobrać można tylko zatwierdzone plany (RB10); zakres „tylko zmiany” pozwala pobrać same zmiany po zatwierdzeniu, np. dorobione grupy."),
    "UC15": ("act/a15.png", "Korekta rozpoznania AI jest opcjonalna i poprzedza wybór sposobu rozwiązania. W obu ścieżkach prowadzący otrzymuje nową wersję propozycji, którą musi ponownie zaakceptować (RB9)."),
    "UC16": ("act/a16.png", "Niespełnienie warunków obsadzenia, akceptacji lub limitów kończy przypadek bez zatwierdzenia. Przekroczenie terminu przekazania nie blokuje zatwierdzenia, ale wymaga podania przyczyny opóźnienia."),
}
UCS = sorted([u for u in UCS if u["id"] in KEEP], key=lambda u: u["id"])
PROTOS = sorted([p for p in PROTOS if p[0] in KEEP], key=lambda p: p[0])
