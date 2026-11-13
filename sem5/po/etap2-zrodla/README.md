# Źródła etapu II (FLAME)

- `diagramy/` – PlantUML: diagram PU (`uc1–3`), model informacyjny (`mi1–3`, `enums`), mapa nawigacji (`nav`), diagramy aktywności (`aktywnosci/`)
- `prototypy/` – makiety HTML ekranów P1–P4 generowane przez `gen.py`
- `content.py` – treść dokumentu (historyjki, specyfikacje PU, komentarze)
- `build.py` – składa `../etap2.docx` z `szablon-etap2.docx`

Odtworzenie dokumentu (wymaga `plantuml` i Google Chrome):

```sh
./render.sh
python3 build.py
```
