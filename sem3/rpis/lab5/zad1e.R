sd <- 15
n <- 100

# test z
Z <- (109 - 115) / (sd/sqrt(n))
p_value <- 2*pnorm(-abs(Z))

Z
p_value


# test t-studenta
t <- (109 - 115) / (sd/sqrt(n))
p_value_t <- 2*pt(-abs(t), df = 99)

t
p_value_t

# H0: srednie IQ = 115
# H1: !H0

# wyniki:
# test Z: p ~ 6.33*10^-5
# test t: p ~ 0.0001223

# Wniosek:
  
# p < 0.05 → odrzucamy H0 i przyjmujemy H1
# sredni IQ istotnie rozni sie od 115 (jest nizszy: 109)