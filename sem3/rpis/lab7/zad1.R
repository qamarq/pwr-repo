test_kostka <- function(freq, alpha = 0.05) {
  n <- sum(freq)
  exp_freq <- rep(n/6, 6)
  chi2 <- sum((freq - exp_freq)^2 / exp_freq)
  p <- 1 - pchisq(chi2, df = 5)
  
  list(
    H0 = "kostka symetryczna",
    H1 = "kostka niesymetryczna",
    statystyka = chi2,
    p_value = p,
    decyzja = ifelse(p < alpha, "odrzucamy H0", "brak podstaw do odrzucenia H0")
  )
}

freq <- c(171,200,168,213,226,222)
test_kostka(freq)

chisq.test(freq)