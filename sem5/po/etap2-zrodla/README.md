# Źródła etapu II (FLAME)

- `diagramy/` – PlantUML: diagram PU (`uc1–3`), model informacyjny (`mi1–3`, `enums`), diagramy aktywności (`aktywnosci/`), mapy nawigacji widoków (`nawigacja/`)
- `prototypy/` – makiety szkieletowe widoków V1–Vn generowane przez `wf.py`
- `content.py` – treść dokumentu (historyjki, specyfikacje PU z WE/WY, komentarze, opis modelu)
- `build.py` – składa `../etap2.docx` z `szablon-etap2.docx`

Odtworzenie dokumentu (wymaga `plantuml`, Google Chrome i Pythona z Pillow do przycięcia zrzutów):

```sh
PY=/ścieżka/do/pythona/z/Pillow ./render.sh
python3 build.py
```
