waga <- read.csv2("waga1.csv")

# b)
head(waga, 5)

# c)
str(waga)

# d)
mean(waga$Wzrost)
mean(waga$Waga_przed)

# e)
waga$Wsk_wagi <- waga$Waga_przed / (waga$Wzrost / 100)^2
waga

# f)
kobiety_25 <- waga[waga$plec == 1 & waga$Wsk_wagi > 25, ]
kobiety_25

# g)
mezczyzni <- waga[waga$plec == 0, ]
mezczyzni

# h)
sum(waga$Wzrost > 175)
