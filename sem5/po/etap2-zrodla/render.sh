#!/bin/zsh
set -e
cd "$(dirname "$0")"
CHROME="/Applications/Google Chrome.app/Contents/MacOS/Google Chrome"
PY=${PY:-python3}
mkdir -p png/act png/nav png/wf
plantuml -tpng -o "$PWD/png" diagramy/*.puml
plantuml -tpng -o "$PWD/png/act" diagramy/aktywnosci/*.puml
plantuml -tpng -o "$PWD/png/nav" diagramy/nawigacja/*.puml
(cd prototypy && python3 wf.py)
for f in prototypy/uc*_v*.html; do
  n=$(basename $f .html)
  "$CHROME" --headless=new --disable-gpu --hide-scrollbars --force-device-scale-factor=2 --window-size=1000,1400 --screenshot="$PWD/png/wf/$n.png" "file://$PWD/$f" 2>/dev/null
done
$PY prototypy/crop.py png/wf/*.png
