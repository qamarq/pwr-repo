#!/bin/zsh
set -e
cd "$(dirname "$0")"
CHROME="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
mkdir -p png/act png/ui
plantuml -tpng -o "$PWD/png" diagramy/*.puml
plantuml -tpng -o "$PWD/png/act" diagramy/aktywnosci/*.puml
(cd prototypy && python3 gen.py)
for spec in p1_powierz:750 p9_stan:535 p3_odpowiedz:500 p17_pobierz:600; do
  f=${spec%%:*}; h=${spec##*:}
  "$CHROME" --headless=new --disable-gpu --hide-scrollbars --force-device-scale-factor=2 --window-size=1280,$h --screenshot="$PWD/png/ui/$f.png" "file://$PWD/prototypy/$f.html" 2>/dev/null
done
