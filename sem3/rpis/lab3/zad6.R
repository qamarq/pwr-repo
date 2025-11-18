n <- 180
p <- 1/6

# a) P(X = 27)
p_a <- dbinom(27, n, p)
print(paste("P(X = 27) =", p_a))

# b) P(X ≥ 32) = 1 - P(X ≤ 31)
p_b <- 1 - pbinom(31, n, p)
print(paste("P(X ≥ 32) =", p_b))

# c) P(X < 29) = P(X ≤ 28)
p_c <- pbinom(28, n, p)
print(paste("P(X < 29) =", p_c))

# d) P(25 ≤ X ≤ 33) = P(X ≤ 33) - P(X ≤ 24)
p_d <- pbinom(33, n, p) - pbinom(24, n, p)
print(paste("P(25 ≤ X ≤ 33) =", p_d))