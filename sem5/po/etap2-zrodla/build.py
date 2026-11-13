import re, shutil, struct, sys, os
from xml.sax.saxutils import escape
sys.path.insert(0, os.path.dirname(__file__))
from content import *

import tempfile, zipfile
B = os.path.dirname(os.path.abspath(__file__))
TARGET = sys.argv[1] if len(sys.argv) > 1 else os.path.join(B, '..', 'etap2.docx')
OUT = tempfile.mkdtemp()
with zipfile.ZipFile(os.path.join(B, 'szablon-etap2.docx')) as z:
    z.extractall(OUT)

LANG = '<w:lang w:val="pl-PL"/>'
PAGE_W = 9360
images = []


def runs(text, base_rpr=''):
    out = []
    for i, part in enumerate(re.split(r'\*\*', text)):
        if not part:
            continue
        rpr = base_rpr + ('<w:b/>' if i % 2 else '') + LANG
        out.append(f'<w:r><w:rPr>{rpr}</w:rPr><w:t xml:space="preserve">{escape(part)}</w:t></w:r>')
    return ''.join(out)


def p(text='', style='BodyText', ind0=True, jc=None, rpr='', keep=False, spacing=None):
    ppr = f'<w:pStyle w:val="{style}"/>'
    if keep:
        ppr += '<w:keepNext/>'
    if spacing:
        ppr += spacing
    if ind0:
        ppr += '<w:ind w:left="0"/>'
    if jc:
        ppr += f'<w:jc w:val="{jc}"/>'
    return f'<w:p><w:pPr>{ppr}<w:rPr>{LANG}</w:rPr></w:pPr>{runs(text, rpr)}</w:p>'


def bullet(text, style='BodyText', num=9):
    return (f'<w:p><w:pPr><w:pStyle w:val="{style}"/><w:numPr><w:ilvl w:val="0"/><w:numId w:val="{num}"/></w:numPr>'
            f'<w:spacing w:after="60"/><w:ind w:left="360" w:hanging="360"/><w:rPr>{LANG}</w:rPr></w:pPr>{runs(text)}</w:p>')


def heading(text, level):
    return f'<w:p><w:pPr><w:pStyle w:val="Heading{level}"/><w:rPr>{LANG}</w:rPr></w:pPr>{runs(text)}</w:p>'


def title(text, pagebreak=True):
    br = f'<w:p><w:pPr><w:pStyle w:val="Title"/></w:pPr><w:r><w:br w:type="page"/></w:r></w:p>' if pagebreak else ''
    return br + f'<w:p><w:pPr><w:pStyle w:val="Title"/><w:spacing w:after="240"/><w:rPr>{LANG}</w:rPr></w:pPr>{runs(text)}</w:p>'


BORDER = '<w:top w:val="single" w:sz="4" w:space="0" w:color="808080"/><w:left w:val="single" w:sz="4" w:space="0" w:color="808080"/><w:bottom w:val="single" w:sz="4" w:space="0" w:color="808080"/><w:right w:val="single" w:sz="4" w:space="0" w:color="808080"/><w:insideH w:val="single" w:sz="4" w:space="0" w:color="808080"/><w:insideV w:val="single" w:sz="4" w:space="0" w:color="808080"/>'


def cell(content, w, shade=None):
    sh = f'<w:shd w:val="clear" w:color="auto" w:fill="{shade}"/>' if shade else ''
    if not content.strip().endswith('</w:p>'):
        content += '<w:p/>'
    return f'<w:tc><w:tcPr><w:tcW w:w="{w}" w:type="dxa"/>{sh}</w:tcPr>{content}</w:tc>'


def tp(text, bold=False, jc=None, keep=False):
    ppr = '<w:pStyle w:val="Tabletext"/>' + ('<w:keepNext/>' if keep else '') + '<w:spacing w:before="20" w:after="20"/>'
    if jc:
        ppr += f'<w:jc w:val="{jc}"/>'
    return f'<w:p><w:pPr>{ppr}<w:rPr>{LANG}</w:rPr></w:pPr>{runs(text, "<w:b/>" if bold else "")}</w:p>'


def tbullet(text):
    return (f'<w:p><w:pPr><w:pStyle w:val="Tabletext"/><w:numPr><w:ilvl w:val="0"/><w:numId w:val="9"/></w:numPr>'
            f'<w:spacing w:before="0" w:after="20"/><w:ind w:left="284" w:hanging="284"/><w:rPr>{LANG}</w:rPr></w:pPr>{runs(text)}</w:p>')


def table(widths, rows, header=True, header_fill='BFBFBF', first_col_fill=None):
    grid = ''.join(f'<w:gridCol w:w="{w}"/>' for w in widths)
    xml = (f'<w:tbl><w:tblPr><w:tblW w:w="{sum(widths)}" w:type="dxa"/><w:tblBorders>{BORDER}</w:tblBorders>'
           f'<w:tblLayout w:type="fixed"/><w:tblCellMar><w:left w:w="80" w:type="dxa"/><w:right w:w="80" w:type="dxa"/></w:tblCellMar>'
           f'<w:tblLook w:val="04A0" w:firstRow="1" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/></w:tblPr><w:tblGrid>{grid}</w:tblGrid>')
    for ri, row in enumerate(rows):
        is_h = header and ri == 0
        trpr = '<w:trPr><w:cantSplit/>' + ('<w:tblHeader/>' if is_h else '') + '</w:trPr>'
        cells = ''
        for ci, (c, w) in enumerate(zip(row, widths)):
            fill = header_fill if is_h else (first_col_fill if (first_col_fill and ci == 0) else None)
            content = c if isinstance(c, str) and c.startswith('<w:') else tp(str(c), bold=is_h)
            cells += cell(content, w, fill)
        xml += f'<w:tr>{trpr}{cells}</w:tr>'
    return xml + '</w:tbl>'


def png_size(path):
    with open(path, 'rb') as f:
        d = f.read(24)
    return struct.unpack('>II', d[16:24])


def image(fname, max_w_in=6.5, max_h_in=8.3):
    path = fname if fname.startswith('/') else f'{B}/png/{fname}'
    w, h = png_size(path)
    wi = max_w_in
    hi = wi * h / w
    if hi > max_h_in:
        hi = max_h_in
        wi = hi * w / h
    cx, cy = int(wi * 914400), int(hi * 914400)
    n = len(images) + 1
    rid = f'rIdImg{n}'
    images.append((rid, path, f'image{n}.png'))
    return (f'<w:p><w:pPr><w:pStyle w:val="BodyText"/><w:keepNext/><w:ind w:left="0"/><w:jc w:val="center"/></w:pPr><w:r><w:rPr>{LANG}</w:rPr><w:drawing>'
            f'<wp:inline distT="0" distB="0" distL="0" distR="0"><wp:extent cx="{cx}" cy="{cy}"/><wp:docPr id="{100+n}" name="Obraz {n}"/>'
            f'<wp:cNvGraphicFramePr><a:graphicFrameLocks xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main" noChangeAspect="1"/></wp:cNvGraphicFramePr>'
            f'<a:graphic xmlns:a="http://schemas.openxmlformats.org/drawingml/2006/main"><a:graphicData uri="http://schemas.openxmlformats.org/drawingml/2006/picture">'
            f'<pic:pic xmlns:pic="http://schemas.openxmlformats.org/drawingml/2006/picture"><pic:nvPicPr><pic:cNvPr id="{100+n}" name="image{n}.png"/><pic:cNvPicPr/></pic:nvPicPr>'
            f'<pic:blipFill><a:blip r:embed="{rid}"/><a:stretch><a:fillRect/></a:stretch></pic:blipFill>'
            f'<pic:spPr><a:xfrm><a:off x="0" y="0"/><a:ext cx="{cx}" cy="{cy}"/></a:xfrm><a:prstGeom prst="rect"><a:avLst/></a:prstGeom></pic:spPr></pic:pic>'
            f'</a:graphicData></a:graphic></wp:inline></w:drawing></w:r></w:p>')


fig_no = [0]


def caption(text):
    fig_no[0] += 1
    return p(f'Rysunek {fig_no[0]}. {text}', jc='center', rpr='<w:i/>', spacing='<w:spacing w:before="60" w:after="200"/>')


def flow_table(actor, steps, note):
    a = actor.split(' (')[0] if not actor.startswith('—') else 'Aktor'
    widths = [620, 3000, 3300]
    rows = [['Krok', a, 'System']]
    for nr, act, sysx in steps:
        rows.append([nr, act, sysx])
    if note:
        rows.append(['', note, ''])
    return table(widths, rows, header_fill='E7E6E6')


def uc_spec(uc):
    L, R = 2240, 7120
    out = []
    rows = [
        (tp('Identyfikator', True), tp(uc['id'])),
        (tp('Nazwa', True), tp(uc['name'])),
        (tp('Aktor główny', True), tp(uc['actor'])),
        (tp('Aktorzy pomocniczy', True), tp(uc['sec'])),
        (tp('Krótki opis', True), tp(uc['desc'])),
        (tp('Warunki początkowe', True), tp(uc['pre'])),
        (tp('Warunki końcowe', True), tp(uc['post'])),
    ]
    for name, steps, note in uc['flows']:
        rows.append((tp(name, True), flow_table(uc['actor'], steps, note) + '<w:p><w:pPr><w:spacing w:after="0"/></w:pPr></w:p>'))
    data = ''.join(tbullet(t) if k == 'b' else tp(t) for k, t in uc['data'])
    rows.append((tp('Dane', True), data))
    grid = f'<w:gridCol w:w="{L}"/><w:gridCol w:w="{R}"/>'
    xml = (f'<w:tbl><w:tblPr><w:tblW w:w="{L+R}" w:type="dxa"/><w:tblBorders>{BORDER}</w:tblBorders><w:tblLayout w:type="fixed"/>'
           f'<w:tblCellMar><w:left w:w="80" w:type="dxa"/><w:right w:w="80" w:type="dxa"/></w:tblCellMar><w:tblLook w:val="04A0" w:firstRow="0" w:lastRow="0" w:firstColumn="1" w:lastColumn="0" w:noHBand="0" w:noVBand="1"/></w:tblPr><w:tblGrid>{grid}</w:tblGrid>')
    for l, r in rows:
        xml += f'<w:tr>{cell(l, L, "D9D9D9")}{cell(r, R)}</w:tr>'
    xml += '</w:tbl>'
    out.append(heading(f'{uc["id"]} {uc["name"]}', 2))
    out.append(xml)
    img, note = ACT[uc['id']]
    out.append(p(''))
    out.append(image(img, max_h_in=7.4))
    out.append(caption(f'{uc["id"]} {uc["name"]} – diagram aktywności'))
    out.append(p(f'**Komentarz.** {note}'))
    return ''.join(out)


body = []

# 1. Specyfikacja wymagań
body.append(p('Wymagania funkcjonalne zapisano jako historyjki użytkownika pogrupowane w epiki. Priorytety (MoSCoW) i planowane wydania są zgodne z wizją z etapu I. Ostatnia kolumna wskazuje przypadki użycia realizujące historyjkę. Względem wizji uzupełniono wymagania dotyczące tworzenia i importu danych o kierunkach i planach studiów (E1).'))
for epic, stories in EPICS:
    body.append(p(f'**{epic}**', keep=True, spacing='<w:spacing w:before="200" w:after="80"/>'))
    rows = [['ID', 'Historyjka użytkownika', 'Priorytet', 'Wydanie', 'PU']]
    rows += [[s[0], s[1], s[2], s[3], s[4]] for s in stories]
    body.append(table([720, 5940, 900, 860, 940], rows))
spec_xml = ''.join(body)

# 2. Model PU
body = []
body.append(heading('Diagram przypadków użycia', 1))
body.append(p('Ze względu na liczbę przypadków użycia model przedstawiono w trzech widokach. Przypadek UC07 „Sprawdź pensum i limity godzin” jest włączany (<<include>>) do każdego przypadku zmieniającego powierzenia; UC11 jest włączany do UC19, a UC08 do UC15, ponieważ dorobienie grupy kończy się wysłaniem propozycji, a rozwiązanie uwagi wymaga zmiany powierzeń.'))
for f, c in [('uc1.png', 'Widok 1 – dane i przygotowanie powierzeń'), ('uc2.png', 'Widok 2 – konsultowanie propozycji przydziałów'), ('uc3.png', 'Widok 3 – przekazanie powierzeń, prowadzący i administracja')]:
    body.append(image(f, max_h_in=7.6))
    body.append(caption(c))
body.append(p('**Aktorzy**', keep=True))
body.append(table([2600, 6760], [['Aktor', 'Opis']] + [list(a) for a in ACTORS]))
body.append(p(''))
uc_diag_xml = ''.join(body)

body = []
body.append(heading('Specyfikacja przypadków użycia', 1))
body.append(p('Wyspecyfikowano cztery przypadki użycia (po dwa na członka zespołu): powierzenie zajęć z kontrolą pensum (UC06), przegląd stanu powierzeń (UC09), odpowiedź prowadzącego na propozycję przydziału (UC12) i pobranie zatwierdzonych powierzeń przez dział kształcenia (UC17). Każdy przypadek opisano tabelarycznie i uzupełniono diagramem aktywności z komentarzem. Numery reguł biznesowych (RB) odnoszą się do etapu I.'))
for uc in UCS:
    body.append(uc_spec(uc))
uc_spec_xml = ''.join(body)

body = []
body.append(heading('Prototyp interfejsu', 1))
body.append(heading('Reguły przewodnika stylu', 2))
for t in STYLE_RULES:
    body.append(p(t))
for t in STYLE_LIST:
    body.append(bullet(t))
body.append(p('Mapa nawigacji między ekranami prototypu:', keep=True, spacing='<w:spacing w:before="160" w:after="80"/>'))
body.append(image('nav.png'))
body.append(caption('Mapa nawigacji (P1–P4 – ekrany prototypu)'))
for i, (ucid, f, c, notes) in enumerate(PROTOS, 1):
    name = next(u['name'] for u in UCS if u['id'] == ucid)
    body.append(heading(f'{ucid} {name}', 2))
    body.append(image(f'ui/{f}'))
    body.append(caption(f'P{i} – {c}'))
    for n in notes:
        body.append(bullet(n))
body.append(p('Wynik włączanego przypadku UC07 (sprawdzenie pensum i limitów) jest wyświetlany w ekranie P1.'))
proto_xml = ''.join(body)

# 3. Model informacyjny
body = []
body.append(p(MODEL_INTRO))
for f, c in [('mi1.png', 'Model informacyjny – program studiów i powierzenia'), ('mi2.png', 'Model informacyjny – prowadzący, pensum i konta'), ('mi3.png', 'Model informacyjny – konsultowanie propozycji przydziałów'), ('enums.png', 'Model informacyjny – typy wyliczeniowe')]:
    body.append(image(f, max_h_in=7.8))
    body.append(caption(c))
body.append(p('**Zmiany względem modelu domenowego**', keep=True))
for t in MODEL_CHANGES:
    body.append(bullet(t))
body.append(p('**Atrybuty pochodne**', keep=True, spacing='<w:spacing w:before="160" w:after="80"/>'))
for t in MODEL_DERIVED:
    body.append(bullet(t))
body.append(p('**Ograniczenia**', keep=True, spacing='<w:spacing w:before="160" w:after="80"/>'))
for t in MODEL_CONSTRAINTS:
    body.append(bullet(t))
model_xml = ''.join(body)

def strip_ids(s):
    return re.sub(r' w14:(paraId|textId)="[^"]*"| w:rsid\w*="[^"]*"', '', s)



# --- assemble into template ---
doc = open(f'{OUT}/word/document.xml', encoding='utf8').read()

# project name
doc = strip_ids(doc)
_i = doc.index('SUBJECT')
_s = doc.index('</w:r>', doc.index('w:fldCharType="separate"', _i)) + len('</w:r>')
_e = doc.rfind('<w:r>', _s, doc.index('w:fldCharType="end"', _s))
doc = doc[:_s] + '<w:r><w:rPr><w:lang w:val="pl-PL"/></w:rPr><w:t>FLAME – Faculty Load Allocation Management Engine</w:t></w:r>' + doc[_e:]

# team
team = ('<w:p><w:pPr><w:tabs><w:tab w:val="left" w:pos="6492"/></w:tabs><w:rPr><w:lang w:val="pl-PL"/></w:rPr></w:pPr>'
        '<w:r><w:rPr><w:lang w:val="pl-PL"/></w:rPr><w:t>Konrad Guzek (284306)</w:t></w:r><w:r><w:rPr><w:lang w:val="pl-PL"/></w:rPr><w:tab/><w:t>dr inż. Bogumiła Hnatkowska</w:t></w:r></w:p>'
        '<w:p><w:r><w:rPr><w:lang w:val="pl-PL"/></w:rPr><w:t>Kamil Marczak (284229)</w:t></w:r></w:p>')
i = doc.index('<w:t>Prowadzący:</w:t></w:r></w:p>') + len('<w:t>Prowadzący:</w:t></w:r></w:p>')
doc = doc[:i] + team + doc[i:]


def replace_between(doc, start_marker, end_marker, new):
    s = doc.index(start_marker)
    s = doc.index('</w:p>', s) + len('</w:p>')
    e = doc.index(end_marker, s)
    e = doc.rfind('<w:p>', s, e) if False else e
    return doc[:s] + new + doc[e:]


doc = strip_ids(doc)


def para_start(doc, idx):
    return doc.rfind('<w:p>', 0, idx) if doc.rfind('<w:p>', 0, idx) > doc.rfind('<w:p ', 0, idx) else doc.rfind('<w:p ', 0, idx)


def para_end(doc, idx):
    return doc.index('</w:p>', idx) + len('</w:p>')


# Specyfikacja wymagań: after title para, until title "Model przypadków użycia"
a = para_end(doc, doc.index('<w:t>wymagań</w:t>'))
b = para_start(doc, doc.index('<w:t>Model przypadków użycia</w:t>'))
doc = doc[:a] + spec_xml + '<w:p><w:pPr><w:pStyle w:val="Title"/></w:pPr><w:r><w:br w:type="page"/></w:r></w:p>' + doc[b:]

# Model PU: from Heading "Diagram przypadków użycia" para through before "Model informacyjny" title
a = para_start(doc, doc.index('<w:t>Diagram przypadków użycia</w:t>'))
b = para_start(doc, doc.index('<w:t xml:space="preserve">Model </w:t>'))
doc = doc[:a] + uc_diag_xml + uc_spec_xml + proto_xml + '<w:p><w:pPr><w:pStyle w:val="Title"/></w:pPr><w:r><w:br w:type="page"/></w:r></w:p>' + doc[b:]

# Model informacyjny: after title until sectPr
a = para_end(doc, doc.index('<w:t>informacyjny</w:t>'))
b = doc.index('<w:sectPr')
doc = doc[:a] + model_xml + doc[b:]

open(f'{OUT}/word/document.xml', 'w', encoding='utf8').write(doc)

# images: media, rels, content types
os.makedirs(f'{OUT}/word/media', exist_ok=True)
rels = open(f'{OUT}/word/_rels/document.xml.rels', encoding='utf8').read()
add = ''
for rid, path, name in images:
    shutil.copy(path, f'{OUT}/word/media/{name}')
    add += f'<Relationship Id="{rid}" Type="http://schemas.openxmlformats.org/officeDocument/2006/relationships/image" Target="media/{name}"/>'
rels = rels.replace('</Relationships>', add + '</Relationships>')
open(f'{OUT}/word/_rels/document.xml.rels', 'w', encoding='utf8').write(rels)
ct = open(f'{OUT}/[Content_Types].xml', encoding='utf8').read()
if 'Extension="png"' not in ct:
    ct = ct.replace('<Default Extension="xml"', '<Default Extension="png" ContentType="image/png"/><Default Extension="xml"')
open(f'{OUT}/[Content_Types].xml', 'w', encoding='utf8').write(ct)
print('images', len(images), 'figs', fig_no[0])

with zipfile.ZipFile(TARGET, 'w', zipfile.ZIP_DEFLATED) as z:
    for root, _, files in os.walk(OUT):
        for f in files:
            full = os.path.join(root, f)
            z.write(full, os.path.relpath(full, OUT))
shutil.rmtree(OUT)
print('zapisano', os.path.abspath(TARGET))
