oceny <- data.frame(
  Imie = c("Krzysztof", "Maria", "Henryk", "Anna"),
  Plec = c("m", "k", "m", "k"),
  Analiza = c(3.5, 4.5, 5.0, 4.5),
  Algebra = c(4.0, 5.0, 4.0, 3.5)
)

# b)
head(oceny, 2)

# c)
str(oceny)

# d)
mean(oceny$Analiza)

# e)
oceny$Srednia <- (oceny$Analiza + oceny$Algebra) / 2
oceny

# f)
kobiety <- subset(oceny, oceny$Plec == "k")
kobiety

# g)
dobrzy <- oceny[oceny$Analiza >= 4.5 | oceny$Algebra >= 4.5, ]
dobrzy

# h)
sum(oceny$Analiza >= 4.5)
