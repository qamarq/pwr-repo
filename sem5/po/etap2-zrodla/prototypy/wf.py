import pathlib

CSS = """
*{box-sizing:border-box;margin:0;padding:0}
body{font-family:Arial,Helvetica,sans-serif;font-size:13px;color:#333;background:#fff;width:1000px;padding:14px}
.frame{border:2px solid #555;border-radius:6px;overflow:hidden}
.bar{border-bottom:1px solid #777;padding:8px 12px;display:flex;gap:16px;background:#eee}
.bar .r{margin-left:auto}
.vid{font-weight:bold;border:1px solid #555;padding:0 6px;background:#fff}
.body{display:flex}
.menu{width:170px;border-right:1px solid #999;padding:10px}
.menu div{padding:5px 6px;margin-bottom:3px;border:1px dashed #bbb}
.menu .on{border:1px solid #555;background:#eee}
.main{flex:1;padding:12px 14px}
.t{font-weight:bold;margin-bottom:4px}
.s{color:#777;margin-bottom:10px}
.box{border:1px solid #888;padding:10px;margin-bottom:10px}
.row{display:flex;gap:10px}
.col{flex:1}
table{width:100%;border-collapse:collapse;margin:4px 0}
th,td{border:1px solid #aaa;padding:4px 6px;text-align:left;vertical-align:top}
th{background:#eee}
.r{text-align:right}
.btn{display:inline-block;border:1px solid #555;padding:3px 10px;margin:4px 6px 0 0;background:#fff}
.btn.p{border-width:2px;font-weight:bold}
.in{border:1px solid #888;padding:3px 6px;min-height:24px;background:#fff;margin-bottom:6px}
label{display:block;color:#666;margin-top:6px}
.note{border:1px dashed #555;padding:6px 8px;margin:6px 0;background:#f6f6f6}
.ph{color:#999}
.modal{border:2px solid #555;padding:12px;margin:10px auto;width:560px;background:#fff;box-shadow:4px 4px 0 #bbb}
.dim{color:#bbb}
"""

MENU_ODP = ["Pulpit", "Stan powierzeń", "Plan powierzeń", "Propozycje przydziałów", "Uwagi do rozwiązania", "Import / eksport"]
MENU_DK = ["Pulpit", "Zatwierdzone powierzenia", "Zgłoszenia grup"]


def menu(items, on):
    return '<div class="menu">' + ''.join(f'<div class="{"on" if i == on else ""}">{i}</div>' for i in items) + '</div>'


def frame(vid, title, user, content, items=None, on=None):
    m = menu(items, on) if items else ''
    return (f'<div class="frame"><div class="bar"><span class="vid">{vid}</span><span>FLAME</span><span>{title}</span>'
            f'<span class="r">{user}</span></div><div class="body">{m}<div class="main">{content}</div></div></div>')


def page(name, html):
    pathlib.Path(f'{name}.html').write_text(f'<!doctype html><html lang="pl"><head><meta charset="utf-8"><style>{CSS}</style></head><body>{html}</body></html>', encoding='utf8')


def tbl(head, rows, right=()):
    h = ''.join(f'<th class="{"r" if i in right else ""}">{c}</th>' for i, c in enumerate(head))
    b = ''.join('<tr>' + ''.join(f'<td class="{"r" if i in right else ""}">{c}</td>' for i, c in enumerate(r)) + '</tr>' for r in rows)
    return f'<table><tr>{h}</tr>{b}</table>'


def btn(t, primary=False):
    return f'<span class="btn{" p" if primary else ""}">[ {t} ]</span>'


ODP = 'Osoba dokonująca powierzeń'
SEM = 'Rok 2026/2027, semestr zimowy'

# UC06 V1 – plan powierzeń (widok startowy)
rows = [
    ["INZ003101", "Paradygmaty programowania", "W", "1", "30", "30", "0", "obsadzone"],
    ["INZ003101", "Paradygmaty programowania", "L", "12", "360", "300", "60", "<b>brak 60 h</b> " + btn("Powierz")],
    ["INZ003102", "Sieci komputerowe", "L", "12", "360", "360", "0", "obsadzone"],
    ["INZ003103", "Programowanie systemów webowych", "W", "1", "30", "0", "30", "<b>nieobsadzone</b> " + btn("Powierz")],
    ["INZ003103", "Programowanie systemów webowych", "P", "10", "300", "240", "60", "<b>brak 60 h</b> " + btn("Powierz")],
    ["INZ003104", "Rachunek prawdopodobieństwa i statystyka", "C", "6", "180", "180", "0", "obsadzone"],
]
c = (f'<div class="t">Plan powierzeń – Informatyka Stosowana, I st., stacj. – nabór 2025, semestr 3</div>'
     f'<div class="s">Status: w przygotowaniu · obsadzono 1185 z 1335 h · termin przekazania 22.10.2026</div>'
     f'<div>{btn("Tylko nieobsadzone")}{btn("Dorób grupę")}{btn("Wyślij propozycje")}{btn("Zatwierdź plan")}</div>'
     + tbl(["Kod", "Kurs", "Forma", "Grupy", "Godz.", "Obsadz.", "Brak", "Stan"], rows, right=(3, 4, 5, 6)))
page('uc06_v1', frame('V1', 'Plan powierzeń', ODP, c, MENU_ODP, 'Plan powierzeń'))

# UC06 V2 – formularz powierzenia
c = ('<div class="t">Nowe powierzenie</div><div class="s">Paradygmaty programowania – laboratorium (L), 30 h na grupę · nieobsadzone: 60 h (2 grupy)</div>'
     '<div class="row"><div class="col"><div class="box"><label>Prowadzący *</label><div class="in">dr hab. inż. Piotr Nowak ▾</div>'
     + tbl(["Mogą prowadzić kurs", "Preferencja", "Pensum 2026/2027"], [["dr inż. Anna Kowalska", "L", "210 / 240 h"], ["mgr inż. Tomasz Wiśniewski", "–", "195 / 240 h"], ["<b>dr hab. inż. Piotr Nowak</b>", "–", "285 / 240 h"]])
     + '<div class="row"><div class="col"><label>Liczba godzin *</label><div class="in">60</div></div><div class="col"><label>Liczba grup</label><div class="in">2</div></div></div></div></div>'
     '<div class="col"><div class="box"><div class="t">Podgląd pensum po zmianie</div>'
     + tbl(["", "Przed", "Po"], [["Godziny powierzone w roku", "285 h", "345 h"], ["Pensum po zniżkach", "240 h", "240 h"], ["Realizacja", "119%", "144%"]])
     + '<div class="note">Ostrzeżenie (RB6): obciążenie przekroczy 125% pensum – wymagana pisemna zgoda nauczyciela.</div>'
     '<div class="note">Ostrzeżenie (RB7): brakujące godziny mają: dr inż. Anna Kowalska (30 h), mgr inż. Tomasz Wiśniewski (45 h).</div>'
     '</div></div></div>' + btn("Zapisz powierzenie", True) + btn("Anuluj"))
page('uc06_v2', frame('V2', 'Plan powierzeń › Nowe powierzenie', ODP, c, MENU_ODP, 'Plan powierzeń'))

# UC09 V1 – stan powierzeń (widok startowy)
summ = ('<div class="row">' + ''.join(f'<div class="col box"><b>{v}</b><br><span class="s">{l}</span></div>' for v, l in [
    ("1335 h", "do powierzenia"), ("1245 h", "obsadzone"), ("90 h", "nieobsadzone"), ("3", "bez pełnego pensum"), ("1", "powyżej 125%"), ("18 / 4 / 1", "propozycje: zaakc. / oczek. / uwagi")]) + '</div>')
rows = [["dr hab. inż. Piotr Nowak", "adiunkt, bad.-dyd.", "345 / 240 h", "144%", "wymaga zgody", "wysłana v2"],
        ["dr inż. Adam Lewandowski", "adiunkt, dyd.", "230 / 240 h", "96%", "brakuje 10 h", "zaakc. milcząco"],
        ["dr inż. Anna Kowalska", "adiunkt, bad.-dyd.", "210 / 240 h", "88%", "brakuje 30 h", "zgłoszono uwagi"],
        ["dr inż. Ewa Zając", "st. wykładowca, dyd.", "300 / 360 h", "83%", "brakuje 60 h", "zaakceptowana"]]
c = (f'<div class="t">Stan powierzeń – {SEM}</div>' + summ
     + f'<div>{btn("Obsada")}<span class="btn p">[ Pensum ]</span>{btn("Propozycje")} &nbsp; Instancja planu: <span class="in" style="display:inline-block;width:120px">wszystkie ▾</span> <span class="in ph" style="display:inline-block;width:130px">szukaj…</span> ☑ tylko problemy</div>'
     + tbl(["Nauczyciel akademicki", "Stanowisko, grupa", "Godziny w roku / pensum", "%", "Stan", "Propozycja"], rows, right=(3,))
     + '<div class="s">Wybór wiersza otwiera szczegóły (V2).</div>')
page('uc09_v1', frame('V1', 'Stan powierzeń', ODP, c, MENU_ODP, 'Stan powierzeń'))

# UC09 V2 – szczegóły nauczyciela
c = ('<div class="t">dr inż. Anna Kowalska – rok akademicki 2026/2027</div><div class="s">adiunkt, grupa badawczo-dydaktyczna, etat 1,0 · pensum bazowe 240 h · zniżki 0 h · pensum po zniżkach 240 h</div>'
     '<div class="row"><div class="col box"><div class="t">Powierzenia – semestr zimowy (90 h)</div>'
     + tbl(["Kurs", "Forma", "Gr.", "Godz."], [["Bazy danych", "W", "1", "30"], ["Paradygmaty programowania", "L", "2", "60"]], right=(2, 3))
     + '</div><div class="col box"><div class="t">Powierzenia – semestr letni (120 h)</div>'
     + tbl(["Kurs", "Forma", "Gr.", "Godz."], [["Inżynieria oprogramowania", "W", "1", "30"], ["Inżynieria oprogramowania", "P", "3", "90"]], right=(2, 3))
     + '</div></div><div class="box"><b>Razem 210 h / 240 h – brakuje 30 h</b> · propozycja (zimowy): wersja 1, zgłoszono uwagi 05.10.2026</div>'
     + btn("Wróć do zestawienia"))
page('uc09_v2', frame('V2', 'Stan powierzeń › Szczegóły nauczyciela', ODP, c, MENU_ODP, 'Stan powierzeń'))

# UC12 – widoki prowadzącego
PR = 'dr inż. Anna Kowalska (prowadzący)'
poz = [["Bazy danych", "W", "1", "30"], ["Programowanie systemów webowych", "P", "2", "60"], ["Paradygmaty programowania", "L", "1", "30"]]
head = '<div class="t">Propozycja przydziału – semestr zimowy 2026/2027, wersja 1</div><div class="s">wysłana 02.10.2026 · termin odpowiedzi 09.10.2026</div>'
c = (head + '<div class="row"><div class="col">' + tbl(["Kurs", "Forma", "Grupy", "Godz."], poz, right=(2, 3))
     + '</div><div class="col box" style="max-width:300px">Pensum 2026/2027: 240 / 240 h (100%)<br>Ten semestr: 120 h<br>Semestr letni: 120 h</div></div>'
     + '<div class="note">Brak odpowiedzi do 09.10.2026 oznacza akceptację propozycji.</div>' + btn("Akceptuję", True) + btn("Zgłaszam uwagi"))
page('uc12_v1', frame('V1', 'Propozycja przydziału', PR, c))

c = ('<div class="dim">' + head + tbl(["Kurs", "Forma", "Grupy", "Godz."], poz) + '</div>'
     '<div class="modal"><div class="t">Potwierdzenie akceptacji</div><p>Akceptujesz propozycję: 3 pozycje, 120 h w semestrze zimowym.</p>'
     '<p style="margin-top:6px">☐ Wyrażam zgodę na obciążenie powyżej 125% pensum <span class="s">(pole widoczne tylko, gdy wymagana jest zgoda pisemna)</span></p>'
     + btn("Potwierdź", True) + btn("Anuluj") + '</div>')
page('uc12_v2', frame('V2', 'Propozycja przydziału', PR, c))

c = (head + tbl(["Kurs", "Forma", "Grupy", "Godz.", "Uwaga do pozycji"],
                 [p_ + [f'<span class="in" style="display:inline-block;width:150px">{u} ▾</span>'] for p_, u in zip(poz, ["bez zmian", "rezygnuję", "bez zmian"])], right=(2, 3))
     + '<label>Treść uwag * (10–2000 znaków)</label><div class="in" style="height:60px">Nie mogę prowadzić projektu z PSW – w tym semestrze mam urlop naukowy w czwartki. Chętnie przejmę w zamian dodatkową grupę laboratorium z Paradygmatów programowania.</div>'
     + btn("Wyślij uwagi", True) + btn("Wróć"))
page('uc12_v3', frame('V3', 'Propozycja przydziału › Zgłoszenie uwag', PR, c))

c = ('<div class="t">Odpowiedź została zapisana</div><div class="box">Propozycja: semestr zimowy 2026/2027, wersja 1<br>Odpowiedź: zgłoszono uwagi (05.10.2026 14:12)<br>'
     'Osoba dokonująca powierzeń otrzymała powiadomienie. Potwierdzenie wysłano na adres e-mail.</div>' + btn("Zamknij"))
page('uc12_v4', frame('V4', 'Propozycja przydziału', PR, c))

# UC17 – dział kształcenia
DK = 'mgr Joanna Krawczyk (dział kształcenia)'
rows = [["☑", "Informatyka Stosowana, I st., stacj. – nabór 2025 (sem. 3)", "20.10.2026", "64", "1365", "1"],
        ["☑", "Informatyka Stosowana, I st., stacj. – nabór 2024 (sem. 5)", "19.10.2026", "58", "1230", "0"],
        ["☑", "Inżynieria Systemów, I st., stacj. – nabór 2026 (sem. 1)", "21.10.2026", "41", "990", "0"],
        ["–", "Informatyka Stosowana, II st., stacj. – nabór 2026 (sem. 1)", "w przygotowaniu", "–", "–", "–"]]
c = (f'<div class="t">Zatwierdzone powierzenia – {SEM}</div><div class="s">3 z 4 planów zatwierdzone · termin przekazania 22.10.2026</div>'
     + tbl(["", "Instancja planu", "Zatwierdzony", "Powierzenia", "Godziny", "Zmiany po zatw."], rows, right=(3, 4, 5))
     + '<div class="row"><div class="col box"><b>Format pliku *</b><br>◉ XLSX &nbsp; ○ CSV (UTF-8, średnik)</div>'
     '<div class="col box"><b>Zakres</b><br>◉ wszystkie powierzenia<br>○ tylko zmiany od: <span class="in ph" style="display:inline-block;width:110px">dd.mm.rrrr</span></div></div>'
     + btn("Pokaż podgląd", True))
page('uc17_v1', frame('V1', 'Zatwierdzone powierzenia', DK, c, MENU_DK, 'Zatwierdzone powierzenia'))

rows = [["IST I st.", "3", "INZ003101", "Paradygmaty programowania", "W", "1", "30", "dr hab. inż. Piotr Nowak", ""],
        ["IST I st.", "3", "INZ003101", "Paradygmaty programowania", "L", "2", "60", "dr hab. inż. Piotr Nowak", ""],
        ["IST I st.", "3", "INZ003102", "Sieci komputerowe", "L", "1", "30", "dr inż. Anna Kowalska", "po zatw."],
        ["…", "", "", "", "", "", "", "", ""]]
c = ('<div class="t">Podgląd pliku</div><div class="s">powierzenia_2026Z_2026-10-22.xlsx · 163 powierzenia · 3585 h · 3 plany</div>'
     + tbl(["Kierunek", "Sem.", "Kod", "Kurs", "Forma", "Gr.", "Godz.", "Prowadzący", "Zmiana"], rows, right=(5, 6))
     + btn("Pobierz XLSX", True) + btn("Wróć"))
page('uc17_v2', frame('V2', 'Zatwierdzone powierzenia › Podgląd', DK, c, MENU_DK, 'Zatwierdzone powierzenia'))
