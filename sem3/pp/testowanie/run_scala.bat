@echo off
echo Kompilowanie pliku test.scala...
scalac test.scala
if %errorlevel% equ 0 (
    echo Kompilacja zakonczona pomyslnie. Uruchamianie...
    scala Hello
) else (
    echo Blad kompilacji!
    pause
)