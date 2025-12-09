# a)
set.seed(123)
n_unif <- 5000
dane_unif <- runif(n_unif, min = 0, max = 1)

# b)
n_norm <- 3000
mu <- 100
sigma <- 15
dane_norm <- rnorm(n_norm, mean = mu, sd = sigma)

# c)
par(mfrow = c(1, 2))
hist(dane_unif, freq = FALSE, main = "Rozklad Jednostajny [0,1]", 
     xlab = "Wartosc")
lines(density(dane_unif), col = "red", lwd = 2)

hist(dane_norm, freq = FALSE, main = "Rozklad Normalny N(100, 15)", 
     xlab = "Wartosc")
lines(density(dane_norm), col = "blue", lwd = 2)

par(mfrow = c(1, 1))