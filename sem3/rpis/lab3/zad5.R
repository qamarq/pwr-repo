mu <- 170
sigma <- 12

# a) P(X > 180)
p_a <- 1 - pnorm(180, mu, sigma)
print(paste("P(X > 180) =", p_a))

# b) P(X < 165)
p_b <- pnorm(165, mu, sigma)
print(paste("P(X < 165) =", p_b))

# c) P(155 < X < 190)
p_c <- pnorm(190, mu, sigma) - pnorm(155, mu, sigma)
print(paste("P(155 < X < 190) =", p_c))

# d) 10% osob jest wyzsze czyli P(X > k) = 0.1
k <- qnorm(0.9, mu, sigma)
print(paste("Wzrost k =", k, "cm"))