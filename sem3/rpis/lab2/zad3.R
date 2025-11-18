mieszkania <- read.csv("mieszkania.csv", sep = ";")

# b)
mieszkania[1:6, ]

# c)
str(mieszkania)

# d)
mean(mieszkania$Metraz)
mean(mieszkania$Cena)

# e)
mieszkania$Cena_m2 <- mieszkania$Cena / mieszkania$Metraz
mieszkania

# f)
psie_pole <- mieszkania[mieszkania$Dzielnica == "Psie Pole" & mieszkania$Cena < 400000, ]
psie_pole

# g)
srodmiescie <- mieszkania[mieszkania$Dzielnica == "Srodmiescie" & mieszkania$Metraz > 60, ]
srodmiescie

# h)
sum(mieszkania$Metraz > 60 & mieszkania$Cena < 350000)

# i)
mieszkania$Stosunek <- (mieszkania$Metraz / mieszkania$Pokoje) / mieszkania$Cena
najlepsze <- mieszkania[which.max(mieszkania$Stosunek), ]
najlepsze

# j)
dzielnice <- unique(mieszkania$Dzielnica)
dzielnice
wyniki <- data.frame(Dzielnica = character(), Odchylenie = numeric())

for (d in dzielnice) {
  ceny <- mieszkania$Cena[mieszkania$Dzielnica == d]
  srednia <- mean(ceny)
  odchylenie <- sd(ceny)
  wyniki <- rbind(wyniki, data.frame(Dzielnica = d, Odchylenie = odchylenie))
}

najstabilniejsza <- wyniki[which.min(wyniki$Odchylenie), ]
most_zroznicowana <- wyniki[which.max(wyniki$Odchylenie), ]

najstabilniejsza
most_zroznicowana
