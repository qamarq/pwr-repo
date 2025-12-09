dane <- read.csv2("waga1.csv")

#zad 2
t.test(dane$Wzrost, mu = 170)

# H0: wzrost = 170
# H1: !H0

# t = −3.7268, p = 0.0003244
# 95% CI = (162.1673, 167.6105)

# wniosek:
# p < 0.05 -> odrzucamy H0.
# Średni wzrost istotnie różni się od 170 cm (jest niższy: 164.89)

#zad 3
t.test(dane$Wzrost, conf.level = 0.90)

# wniosek: z 90% ufnoscia sredni wzrost populacji lezy w tym przedziale

#zad 4
kobiety <- dane$Wzrost[dane$plec == 1]
t.test(kobiety, mu = 160)
# H0: wzrost = 160
# H1: !H0

# t = −0.9296, p = 0.3574
# 95% CI = (154.7469, 161.9339)

# wniosek:
#p > 0.05 → nie odrzucamy H0
# brak dowodow, ze sredni wzrost studentek rozni sie od 160 cm

#zad 5
t.test(kobiety, conf.level = 0.98)
# wniosek: z 98% ufnoscia sredni wzrost studentek lezy w tym przedziale

#zad 6
mezczyzni <- dane[dane$plec == 0, ]
k <- sum(mezczyzni$Wzrost > 180)
n <- nrow(mezczyzni)
prop.test(k, n, p = 0.25, correct = FALSE)

# H0: p = 0.25
# H1: !H0

# p_hat = 0.2115
# p = 0.5218
# 95% CI = (0.12244, 0.34032)
# wniosek:
# p > 0.05 -> nie odrzucamy H0
# brak dowodow, ze proporcja mezczyzn >180 cm rozni sie od 25%

#zad 7
z <- qnorm(1-(1-0.96)/2)

CI_low <- k - z * sqrt(phat*(1-phat)/n)
CI_high <- k + z * sqrt(phat*(1-phat)/n)

c(CI_low, CI_high)

prop.test(k, n, conf.level = 0.96, correct = FALSE)

# wniosek: Z 96% ufnoscia proporcja mezczyzn >180 cm 
# miesci sie w tym przedziale