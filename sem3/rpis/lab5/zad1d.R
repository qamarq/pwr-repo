n <- 100
odchylenie <- 15
srednia <- 109

df <- 100 - 1
t <- qt(0.95, df = df)

CI_low <- srednia - t * odchylenie/sqrt(n)
CI_high <- srednia + t * odchylenie/sqrt(n)

c(CI_low, CI_high)

# wniosek: z 90% ufnoscia sredni IQ lezy w tym przedziale
# przedzial ufnosci oparty o rozklad t-Studenta (t)