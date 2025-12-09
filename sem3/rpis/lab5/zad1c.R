srednia <- 109
odchylenie <- 15
n <- 100
z <- qnorm(1 - (1 - 0.9)/2)

CI_low <- srednia - z * odchylenie/sqrt(n)
CI_high <- srednia + z * odchylenie/sqrt(n)

c(CI_low, CI_high)


# wniosek: Z 90% ufnoscia sredni IQ populacji lezy w tym przedziale
# przedzial ufnosci oparty o rozklad normalny (Z)