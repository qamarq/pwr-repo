phat <- 30/100
n <- 100
z <- qnorm(1-(1-0.99)/2)

CI_low <- phat - z * sqrt(phat*(1-phat)/n)
CI_high <- phat + z * sqrt(phat*(1-phat)/n)

c(CI_low, CI_high)

# test
prop.test(30, 100, conf.level = 0.99, correct = FALSE)


# wniosek: Z 99% ufnościa prawdziwa proporcja 
# studentow z IQ>115 miesci się w tych przedzialach