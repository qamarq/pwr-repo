# Skrypt PowerShell do kompilacji i uruchomienia Scala
param(
    [string]$ScalaFile = "test.scala",
    [string]$MainClass = "Hello"
)

Write-Host "Kompilowanie pliku $ScalaFile..." -ForegroundColor Yellow

# Kompiluj plik Scala
scalac $ScalaFile

if ($LASTEXITCODE -eq 0) {
    Write-Host "Kompilacja zakonczona pomyslnie. Uruchamianie klasy $MainClass..." -ForegroundColor Green
    scala $MainClass
} else {
    Write-Host "Blad kompilacji!" -ForegroundColor Red
    Read-Host "Nacisnij Enter aby zakonczyc"
}