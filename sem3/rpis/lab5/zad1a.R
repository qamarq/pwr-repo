p0 <- 0.35
phat <- 30/100
n <- 100

Z <- (phat - p0) / sqrt(p0*(1-p0)/n)
p_value <- 2*pnorm(-abs(Z))

Z
p_value

# test
prop.test(30, 100, p = 0.35, correct = FALSE)


# H0: 35% studentów ma IQ wyższe niż 115
# H1: !H0

# wyniki: p = 0.2945 ~= 0.3 > 0.05 wiec nie dorzucamy H0