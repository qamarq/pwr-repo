test_normalnosc <- function(x, alpha = 0.05) {
  sw <- shapiro.test(x)
  
  list(
    H0 = "Rozkład normalny",
    H1 = "Rozkład nienormalny",
    statystyka = sw$statistic,
    p_value = sw$p.value,
    decyzja = ifelse(sw$p.value < alpha, "Odrzucamy H0", "Brak podstaw do odrzucenia H0")
  )
}

m <- read.csv2("mieszkania.csv")

test_normalnosc(m$Cena / m$Metraz)  # cena za m2
test_normalnosc(m$Metraz)