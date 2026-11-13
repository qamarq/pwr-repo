import pathlib
top=open('top.inc').read(); side=open('side_odp.inc').read()
def page(name, body, side_html=None, top_html=None):
    s = side if side_html is None else side_html
    t = top if top_html is None else top_html
    html=f'<!doctype html><html lang="pl"><head><meta charset="utf-8"><link rel="stylesheet" href="style.css"></head><body>{t}<div class="wrap">{s}<div class="main">{body}</div></div></body></html>'
    pathlib.Path(name+'.html').write_text(html,encoding='utf8')
def side_on(label):
    return side.replace(' class="on"','').replace(f'<a>{label}',f'<a class="on">{label}',1)
def bar(p,col): return f'<span class="bar"><i style="width:{min(p,100)}%;background:{col}"></i></span>'
G='#2e8b57';Y='#d4a017';R='#c0392b'

# P1 UC06
rows=[("INZ003101","Paradygmaty programowania","W","1","30","30","0",'<span class="tag ok">obsadzone</span>',""),
("INZ003101","Paradygmaty programowania","L","12","360","300","60",'<span class="tag warn">brak 60 h</span>',"sel"),
("INZ003102","Sieci komputerowe","W","1","30","30","0",'<span class="tag ok">obsadzone</span>',""),
("INZ003102","Sieci komputerowe","L","12","360","360","0",'<span class="tag ok">obsadzone</span>',""),
("INZ003103","Programowanie systemów webowych","W","1","30","0","30",'<span class="tag err">nieobsadzone</span>',""),
("INZ003103","Programowanie systemów webowych","P","10","300","240","60",'<span class="tag warn">brak 60 h</span>',""),
("INZ003104","Rachunek prawdopodobieństwa i statystyka","W","1","30","30","0",'<span class="tag ok">obsadzone</span>',""),
("INZ003104","Rachunek prawdopodobieństwa i statystyka","C","6","180","180","0",'<span class="tag ok">obsadzone</span>',""),
("INZ003105","Podstawy przedsiębiorczości","W","1","15","15","0",'<span class="tag ok">obsadzone</span>',"")]
tr="".join(f'<tr class="{c}"><td>{k}</td><td>{n}</td><td>{f}</td><td class="right">{g}</td><td class="right">{h}</td><td class="right">{o}</td><td class="right">{u}</td><td>{s}</td></tr>' for k,n,f,g,h,o,u,s,c in rows)
p1=f'''<h1>Plan powierzeń — semestr 3</h1><div class="sub">Status planu: <span class="tag info">w przygotowaniu</span> &nbsp; Obsadzono 1 185 z 1 335 h (89%) · termin przekazania: 22.10.2026</div>
<div class="grid" style="grid-template-columns:640px 1fr">
<div class="card"><h2>Zapotrzebowanie</h2><div style="margin-bottom:8px"><span class="btn">Tylko nieobsadzone</span><span class="btn">Dorób grupę</span><span class="btn">Wyślij propozycje</span><span class="btn">Zatwierdź plan</span></div>
<table><tr><th>Kod</th><th>Kurs</th><th>Forma</th><th class="right">Grupy</th><th class="right">Godz.</th><th class="right">Obsadz.</th><th class="right">Brak</th><th>Stan</th></tr>{tr}</table></div>
<div class="card"><h2>Nowe powierzenie</h2>
<div class="muted">Paradygmaty programowania — laboratorium (L), 30 h na grupę<br>Nieobsadzone: <b>60 h</b> (2 grupy)</div>
<label>Prowadzący *</label><div class="in">dr hab. inż. Piotr Nowak ▾</div>
<table style="margin-top:6px;font-size:12px"><tr><th>Mogą prowadzić kurs</th><th>Pensum 2026/2027</th></tr>
<tr><td>dr inż. Anna Kowalska <span class="tag info">preferuje L</span></td><td>{bar(88,Y)}210/240 h</td></tr>
<tr><td>mgr inż. Tomasz Wiśniewski</td><td>{bar(81,Y)}195/240 h</td></tr>
<tr class="sel"><td>dr hab. inż. Piotr Nowak</td><td>{bar(100,G)}285/240 h</td></tr></table>
<div style="display:flex;gap:12px"><div style="flex:1"><label>Liczba godzin *</label><div class="in">60</div></div><div style="flex:1"><label>Liczba grup</label><div class="in">2</div></div></div>
<label>Pensum prowadzącego w roku 2026/2027 po zmianie</label>
<div>{bar(100,R)} 285 h → <b>345 h</b> / 240 h (<b>144%</b>)</div>
<div class="alert warn" style="background:#fff8e6">⚠ Obciążenie przekroczy 125% pensum. Wymagana będzie pisemna zgoda nauczyciela — milcząca zgoda nie wystarczy (RB6).</div>
<div class="alert info" style="background:#eef3fc">ℹ 2 nauczycieli ma brakujące godziny: dr inż. Anna Kowalska (−30 h), mgr inż. Tomasz Wiśniewski (−45 h). Powierzenie godzin ponadwymiarowych zablokuje zatwierdzenie planu (RB7).</div>
<div style="margin-top:10px"><span class="btn p">Zapisz powierzenie</span><span class="btn">Anuluj</span></div></div></div>'''
page('p1_powierz',p1)

# P2 UC11
pr=[("dr hab. inż. Piotr Nowak","nauczyciel","120","144%","zmieniona",True),
("dr inż. Anna Kowalska","nauczyciel","120","100%","nowa",True),
("mgr inż. Tomasz Wiśniewski","nauczyciel","105","81%","nowa",True),
("dr Marta Wójcik","nauczyciel","150","104%","nowa",True),
("mgr inż. Jan Zieliński","ekspert zewn.","30","—","nowa",True),
("dr inż. Adam Lewandowski","nauczyciel","60","96%","bez zmian",False)]
tr="".join(f'<tr class="{"sel" if c else ""}"><td><span class="chk {"on" if c else ""}">{"✓" if c else ""}</span>{n}</td><td>{t}</td><td class="right">{h}</td><td class="right">{p}</td><td><span class="tag {"warn" if s=="zmieniona" else ("grey" if s=="bez zmian" else "info")}">{s}</span></td></tr>' for n,t,h,p,s,c in pr)
p2=f'''<h1>Wysyłka propozycji przydziałów</h1><div class="sub">Semestr zimowy 2026/2027 · wybrano 5 z 6 prowadzących</div>
<div class="grid" style="grid-template-columns:560px 1fr">
<div class="card"><h2>Prowadzący</h2><table><tr><th>Prowadzący</th><th>Rodzaj</th><th class="right">Godz. w sem.</th><th class="right">% pensum</th><th>Propozycja</th></tr>{tr}</table>
<label>Termin odpowiedzi *</label><div class="in" style="width:200px">09.10.2026 📅</div><div class="msg muted">Domyślnie 7 dni od wysyłki; najpóźniej 5 dni przed terminem przekazania (22.10.2026).</div>
<label>Dodatkowa informacja (opcjonalnie)</label><div class="in" style="height:54px">Proszę o zgłaszanie uwag przez formularz w wiadomości.</div>
<div style="margin-top:12px"><span class="btn p">Wyślij 5 propozycji</span><span class="btn">Anuluj</span></div></div>
<div class="card"><h2>Podgląd wiadomości — dr hab. inż. Piotr Nowak</h2>
<div class="muted" style="font-size:12px">Od: flame@pwr.edu.pl · Temat: Propozycja przydziału zajęć — semestr zimowy 2026/2027 (wersja 2)</div><hr style="border:0;border-top:1px solid #eee;margin:8px 0">
<p>Szanowny Panie Profesorze,</p><p style="margin:6px 0">uprzejmie przedstawiam propozycję przydziału zajęć na semestr zimowy 2026/2027:</p>
<table><tr><th>Kurs</th><th>Forma</th><th class="right">Grupy</th><th class="right">Godz.</th></tr>
<tr><td>Paradygmaty programowania</td><td>W</td><td class="right">1</td><td class="right">30</td></tr>
<tr><td>Paradygmaty programowania</td><td>L</td><td class="right">2</td><td class="right">60</td></tr>
<tr><td>Projektowanie oprogramowania</td><td>W</td><td class="right">1</td><td class="right">30</td></tr></table>
<p style="margin:8px 0">Łącznie w roku 2026/2027: <b>345 h</b> (144% pensum). Obciążenie powyżej 125% pensum wymaga Pana <b>pisemnej zgody</b> — prosimy o akceptację w formularzu.</p>
<p>Prosimy o odpowiedź do <b>09.10.2026</b>: <span style="color:#1f4e9c;text-decoration:underline">Akceptuję / Zgłaszam uwagi</span>. Brak odpowiedzi w terminie oznacza akceptację propozycji (z wyjątkiem zgody na przekroczenie 125% pensum).</p>
<p style="margin-top:6px">dr inż. Ewa Malinowska, Prodziekan ds. kształcenia</p></div></div>'''
page('p2_wyslij',side_on('Propozycje przydziałów').replace('<a class="on">Plan powierzeń','<a>Plan powierzeń'),)
page('p2_wyslij',p2,side_on('Propozycje przydziałów'))

# P3 UC12
topP='<div class="top"><span class="logo">FLAME</span><span class="ctx">Propozycja przydziału zajęć</span><span class="user">dr inż. Anna Kowalska · Prowadzący</span></div>'
p3=f'''<div style="max-width:980px;margin:0 auto"><h1>Propozycja przydziału — semestr zimowy 2026/2027 (wersja 1)</h1><div class="sub">Wysłana 02.10.2026 · termin odpowiedzi: <b>09.10.2026</b> (zostało 4 dni) · <span class="tag info">oczekuje na odpowiedź</span></div>
<div class="grid" style="grid-template-columns:1fr 330px">
<div class="card"><h2>Proponowane zajęcia</h2>
<table><tr><th>Kurs</th><th>Forma</th><th class="right">Grupy</th><th class="right">Godz.</th><th>Twoja uwaga</th></tr>
<tr><td>Bazy danych</td><td>W</td><td class="right">1</td><td class="right">30</td><td><div class="in" style="min-height:26px;padding:4px 8px">bez zmian ▾</div></td></tr>
<tr class="sel"><td>Programowanie systemów webowych</td><td>P</td><td class="right">2</td><td class="right">60</td><td><div class="in" style="min-height:26px;padding:4px 8px">rezygnuję ▾</div></td></tr>
<tr><td>Paradygmaty programowania</td><td>L</td><td class="right">1</td><td class="right">30</td><td><div class="in" style="min-height:26px;padding:4px 8px">bez zmian ▾</div></td></tr></table>
<label>Treść uwag *</label><div class="in" style="height:76px">Nie mogę prowadzić projektu z PSW — w tym semestrze mam urlop naukowy w czwartki. Chętnie przejmę w zamian dodatkową grupę laboratorium z Paradygmatów programowania.</div>
<div class="msg muted">10–2000 znaków. Osoba dokonująca powierzeń otrzyma powiadomienie o Twoich uwagach.</div>
<div style="margin-top:12px"><span class="btn p">Wyślij uwagi</span><span class="btn">Wróć</span></div></div>
<div><div class="card"><h2>Twoje pensum 2026/2027</h2><div>{bar(100,G)}<b>240 h</b> / 240 h (100%)</div><div class="muted" style="margin-top:6px">Semestr letni (zaplanowane): 120 h<br>Ten semestr (propozycja): 120 h</div></div>
<div class="card"><h2>Twoja odpowiedź</h2><div style="margin-bottom:6px"><span class="btn">✓ Akceptuję</span><span class="btn p">Zgłaszam uwagi</span></div><div class="muted" style="font-size:12px">Brak odpowiedzi do 09.10.2026 oznacza akceptację propozycji (milcząca zgoda).</div></div></div></div></div>'''
page('p3_odpowiedz',p3,'',topP)

# P4 UC15
p4=f'''<h1>Uwagi do rozwiązania</h1><div class="sub">3 uwagi oczekujące · semestr zimowy 2026/2027</div>
<div class="grid" style="grid-template-columns:330px 1fr">
<div class="card"><table><tr><th>Prowadzący</th><th>Otrzymano</th><th>AI</th></tr>
<tr class="sel"><td><b>dr inż. Anna Kowalska</b><br><span class="muted">PSW (P) — rezygnacja</span></td><td>05.10</td><td><span class="tag ok">94%</span></td></tr>
<tr><td>dr Marta Wójcik<br><span class="muted">e-mail, bez wskazania pozycji</span></td><td>05.10</td><td><span class="tag warn">do weryfikacji</span></td></tr>
<tr><td>mgr inż. Jan Zieliński<br><span class="muted">RP (C) — zmiana godzin</span></td><td>06.10</td><td><span class="tag ok">88%</span></td></tr></table></div>
<div class="card"><h2>dr inż. Anna Kowalska — propozycja wersja 1 (formularz, 05.10.2026 14:12)</h2>
<div class="in" style="background:#fafafa">„Nie mogę prowadzić projektu z PSW — w tym semestrze mam urlop naukowy w czwartki. Chętnie przejmę w zamian dodatkową grupę laboratorium z Paradygmatów programowania.”</div>
<div class="ai" style="margin-top:10px"><div class="lbl">SUGESTIA AI · wymaga Twojej decyzji</div>
Rodzaj odpowiedzi: <b>prośba o zmianę</b> (pewność 94%)<br>• Programowanie systemów webowych, P, 2 gr., 60 h → <b>rezygnacja</b><br>• Paradygmaty programowania, L → <b>propozycja: +1 grupa (30 h)</b><br>
<div style="margin-top:6px">Możliwe rozwiązanie: PSW (P) 60 h → mgr inż. Tomasz Wiśniewski (195 → 255 h, 106%) · Kowalska: −60 h, +30 h z Paradygmatów (L) → 210 / 240 h.</div>
<div style="margin-top:6px"><span class="btn">Popraw rozpoznanie</span></div></div>
<label>Komentarz dla prowadzącego</label><div class="in" style="height:48px">Zgodnie z prośbą przekazuję PSW (P) innemu prowadzącemu i dodaję grupę laboratorium z Paradygmatów.</div>
<div style="margin-top:12px"><span class="btn p">Zmień powierzenia</span><span class="btn">Odrzuć uwagę</span><span class="btn">Pokaż powierzenia prowadzącej</span></div></div></div>'''
page('p4_uwagi',p4,side_on('Uwagi do rozwiązania'))

# P5 UC16
modal5='''<div class="overlay"><div class="modal"><h2>Zatwierdzenie planu powierzeń — semestr 3, zimowy 2026/2027</h2>
<div class="muted" style="margin-bottom:8px">Zatwierdzony plan zostanie zablokowany do edycji i udostępniony działowi kształcenia.</div>
<table><tr><th>Warunek</th><th>Stan</th></tr>
<tr><td>Wszystkie zapotrzebowania w pełni obsadzone (godziny i grupy)</td><td><span class="tag err">✗ 2 formy, 90 h</span></td></tr>
<tr><td>Najnowsze wersje propozycji zaakceptowane (także milcząco)</td><td><span class="tag err">✗ 1 z uwagami</span></td></tr>
<tr><td>Brak przekroczeń limitów godzin; wymagane zgody pisemne udzielone</td><td><span class="tag ok">✓ spełniony</span></td></tr>
<tr><td>Brak godzin ponadwymiarowych przy brakach u innych nauczycieli</td><td><span class="tag ok">✓ spełniony</span></td></tr>
<tr><td>Termin przekazania (22.10.2026)</td><td><span class="tag ok">✓ zostało 9 dni</span></td></tr></table>
<div class="alert err" style="background:#fdf0ee">Nie można zatwierdzić planu. Do uzupełnienia:<br>• <u>Programowanie systemów webowych — W: brak 30 h</u><br>• <u>Programowanie systemów webowych — P: brak 60 h</u><br>• <u>dr inż. Anna Kowalska — uwaga z 05.10.2026 nierozwiązana</u></div>
<div class="right" style="margin-top:10px"><span class="btn">Zamknij</span><span class="btn p dis">Zatwierdź plan</span></div></div></div>'''
bg=p1[:p1.index('<div class="card"><h2>Nowe powierzenie')]+'<div class="card"><h2>Powierzenia</h2><div class="muted">Wybierz pozycję zapotrzebowania.</div></div></div>'
page('p5_zatwierdz',bg.replace('Obsadzono 1 185 z 1 335 h (89%)','Obsadzono 1 245 z 1 335 h (93%)')+modal5)

# P6 UC19
modal6=f'''<div class="overlay"><div class="modal" style="width:700px"><h2>Dorób grupę — Sieci komputerowe, laboratorium (L)</h2>
<div class="alert info" style="background:#eef3fc;margin-top:0">Zgłoszenie działu kształcenia z 30.10.2026: „Po rejestracji na kurs zapisało się 168 studentów — prosimy o 1 dodatkową grupę L.”</div>
<div style="display:flex;gap:12px"><div style="flex:1"><label>Liczba studentów</label><div class="in">168</div></div><div style="flex:1"><label>Dodatkowe grupy *</label><div class="in">1</div></div><div style="flex:1"><label>Grupy po zmianie</label><div class="in" style="background:#fafafa">13 (+30 h)</div></div></div>
<label>Prowadzący nowej grupy</label>
<table><tr><th></th><th>Prowadzący</th><th>Pensum 2026/2027</th><th>Uwagi</th></tr>
<tr class="sel"><td><span class="chk on">●</span></td><td>dr inż. Anna Kowalska</td><td>{bar(88,Y)}210 → 240 / 240 h</td><td><span class="tag warn">brakuje 30 h</span></td></tr>
<tr><td><span class="chk"></span></td><td>dr inż. Adam Lewandowski</td><td>{bar(96,Y)}230 / 240 h</td><td><span class="tag warn">brakuje 10 h</span> prowadzi już L</td></tr>
<tr><td><span class="chk"></span></td><td>mgr inż. Tomasz Wiśniewski</td><td>{bar(100,G)}255 / 240 h</td><td><span class="tag ok">pełne pensum</span></td></tr></table>
<div class="alert ok" style="background:#eef8f1">✓ Pensum i limity godzin w normie. Prowadząca otrzyma propozycję (wersja 3) z terminem odpowiedzi 3 dni; dział kształcenia zostanie powiadomiony.</div>
<div class="right"><span class="btn">Anuluj</span><span class="btn p">Dorób grupę i wyślij propozycję</span></div></div></div>'''
page('p6_dorob',bg.replace('<span class="tag info">w przygotowaniu</span> &nbsp; Obsadzono 1 185 z 1 335 h (89%)','<span class="tag ok">zatwierdzony 20.10.2026</span> &nbsp; Obsadzono 1 335 z 1 335 h (100%)')+modal6,side_on('Zgłoszenia grup'))

# P UC09 stan powierzeń
def tile(v,l,cls=''):
    return f'<div class="card" style="margin:0;padding:10px 12px"><div style="font-size:20px;font-weight:bold" class="{cls}">{v}</div><div class="muted" style="font-size:12px">{l}</div></div>'
tiles='<div class="grid" style="grid-template-columns:repeat(6,1fr);margin-bottom:12px">'+tile('1 335 h','do powierzenia')+tile('1 245 h','obsadzone (93%)')+tile('<span style="color:#b3261e">90 h</span>','nieobsadzone (2 formy)')+tile('<span style="color:#8a6100">3</span>','nauczycieli bez pełnego pensum')+tile('<span style="color:#8a6100">1</span>','powyżej 125% pensum')+tile('18 / 4 / 1','propozycje: zaakc. / oczek. / uwagi')+'</div>'
rows=[("dr hab. inż. Piotr Nowak","adiunkt, bad.-dyd.",240,345,'<span class="tag warn">144% – wymaga zgody</span>','wysłana v2'),
("dr Marta Wójcik","adiunkt, bad.-dyd.",240,250,'<span class="tag ok">pełne pensum</span>','zaakceptowana'),
("mgr inż. Tomasz Wiśniewski","asystent, bad.-dyd.",240,255,'<span class="tag ok">pełne pensum</span>','wysłana v2'),
("dr inż. Adam Lewandowski","adiunkt, dyd.",240,230,'<span class="tag warn">brakuje 10 h</span>','zaakc. milcząco'),
("dr inż. Anna Kowalska","adiunkt, bad.-dyd.",240,210,'<span class="tag warn">brakuje 30 h</span>','<span class="tag err">zgłoszono uwagi</span>'),
("dr inż. Ewa Zając","starszy wykładowca, dyd.",360,300,'<span class="tag warn">brakuje 60 h</span>','zaakceptowana')]
def pct(a,b): return round(100*a/b)
tr="".join(f'<tr class="{"sel" if n.startswith("dr inż. Anna") else ""}"><td>{n}</td><td class="muted">{s}</td><td>{bar(min(pct(h,p),100),G if h>=p and pct(h,p)<=125 else (R if pct(h,p)>125 else Y))}{h} / {p} h</td><td class="right">{pct(h,p)}%</td><td>{t}</td><td>{st}</td></tr>' for n,s,p,h,t,st in rows)
p9=f'''<h1>Stan powierzeń — semestr zimowy 2026/2027</h1><div class="sub">Wszystkie plany powierzeń semestru · stan na 12.10.2026</div>{tiles}
<div class="card"><div style="display:flex;gap:8px;align-items:center;margin-bottom:10px"><span class="btn">Obsada</span><span class="btn p">Pensum</span><span class="btn">Propozycje</span>
<span style="margin-left:auto" class="muted">Instancja planu:</span><div class="in" style="min-height:28px;padding:4px 8px">wszystkie ▾</div><div class="in" style="min-height:28px;padding:4px 8px;width:200px">Szukaj prowadzącego…</div><span><span class="chk on">✓</span>tylko problemy</span></div>
<table><tr><th>Nauczyciel akademicki</th><th>Stanowisko, grupa</th><th>Godziny w roku / pensum po zniżkach</th><th class="right">%</th><th>Stan</th><th>Propozycja</th></tr>{tr}</table>
<div class="muted" style="margin-top:8px;font-size:12px">Wybierz wiersz, aby zobaczyć powierzenia nauczyciela w obu semestrach i zniżki pensum. Filtr „tylko problemy” ukrywa 41 nauczycieli bez uwag.</div></div>'''
page('p9_stan',p9,side_on('Plan powierzeń').replace('<a class="on">Plan powierzeń','<a>Plan powierzeń').replace('<a>Pulpit</a>','<a>Pulpit</a><a class="on">Stan powierzeń</a>'))

# P UC17 pobierz zatwierdzone
sideDK='<div class="side"><a>Pulpit</a><a class="on">Zatwierdzone powierzenia</a><a>Zgłoszenia grup</a><a>Powiadomienia <span class="badge">2</span></a></div>'
topDK='<div class="top"><span class="logo">FLAME</span><span class="ctx">Rok 2026/2027 · semestr zimowy</span><span class="user">mgr Joanna Krawczyk · Pracownik działu kształcenia</span></div>'
pl=[(True,"Informatyka Stosowana, I st., stacj. — nabór 2025 (sem. 3)","20.10.2026","64","1 365","1"),
(True,"Informatyka Stosowana, I st., stacj. — nabór 2024 (sem. 5)","19.10.2026","58","1 230","0"),
(True,"Inżynieria Systemów, I st., stacj. — nabór 2026 (sem. 1)","21.10.2026","41","990","0"),
(False,"Informatyka Stosowana, II st., stacj. — nabór 2026 (sem. 1)","—","—","—","—")]
tr="".join(f'<tr class="{"sel" if c else ""}"><td><span class="chk {"on" if c else ""}">{"✓" if c else ""}</span>{n}</td><td>{d if c else "<span class=\'tag info\'>w przygotowaniu</span>"}</td><td class="right">{a}</td><td class="right">{h}</td><td class="right">{z}</td></tr>' for c,n,d,a,h,z in pl)
p17=f'''<h1>Zatwierdzone powierzenia — semestr zimowy 2026/2027</h1><div class="sub">3 z 4 planów powierzeń zatwierdzone · termin przekazania: 22.10.2026</div>
<div class="grid" style="grid-template-columns:1fr 330px">
<div class="card"><h2>Plany powierzeń</h2><table><tr><th>Instancja planu</th><th>Zatwierdzony</th><th class="right">Powierzenia</th><th class="right">Godziny</th><th class="right">Zmiany po zatw.</th></tr>{tr}</table>
<h2 style="margin-top:14px">Podgląd pliku (pierwsze wiersze)</h2>
<table style="font-size:11.5px"><tr><th>Kierunek</th><th>Sem.</th><th>Kod</th><th>Kurs</th><th>Forma</th><th class="right">Gr.</th><th class="right">Godz.</th><th>Prowadzący</th><th>Zmiana</th></tr>
<tr><td>IST I st.</td><td>3</td><td>INZ003101</td><td>Paradygmaty programowania</td><td>W</td><td class="right">1</td><td class="right">30</td><td>dr hab. inż. Piotr Nowak</td><td></td></tr>
<tr><td>IST I st.</td><td>3</td><td>INZ003101</td><td>Paradygmaty programowania</td><td>L</td><td class="right">2</td><td class="right">60</td><td>dr hab. inż. Piotr Nowak</td><td></td></tr>
<tr><td>IST I st.</td><td>3</td><td>INZ003102</td><td>Sieci komputerowe</td><td>L</td><td class="right">1</td><td class="right">30</td><td>dr inż. Anna Kowalska</td><td><span class="tag warn">po zatw.</span></td></tr></table></div>
<div class="card"><h2>Pobieranie</h2>
<label>Format pliku *</label><div><span class="chk on">●</span>XLSX &nbsp;&nbsp; <span class="chk"></span>CSV (UTF-8, średnik)</div>
<label>Zakres</label><div><span class="chk on">●</span>wszystkie powierzenia</div><div style="margin-top:4px"><span class="chk"></span>tylko zmiany od: <span class="in" style="display:inline-block;min-height:24px;padding:2px 8px;width:110px">dd.mm.rrrr</span></div>
<div class="alert info" style="background:#eef3fc">Plik: powierzenia_2026Z_2026-10-22.xlsx<br>163 powierzenia · 3 585 h · 3 plany</div>
<div class="muted" style="font-size:12px">Plan w przygotowaniu nie może zostać pobrany (RB10).</div>
<div style="margin-top:12px"><span class="btn p">Pobierz XLSX</span></div></div></div>'''
page('p17_pobierz',p17,sideDK,topDK)
