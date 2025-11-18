n <- 6
p <- 0.5

# a) P(X = 5)
p_a <- dbinom(5, n, p)
print(paste("P(X = 5) =", p_a))

# b) P(X ≥ 3) = 1 - P(X ≤ 2)
p_b <- 1 - pbinom(2, n, p)
print(paste("P(X ≥ 3) =", p_b))

# c) P(2 ≤ X ≤ 4) = P(X ≤ 4) - P(X ≤ 1)
p_c <- pbinom(4, n, p) - pbinom(1, n, p)
print(paste("P(2 ≤ X ≤ 4) =", p_c))

# d) wykresik
x <- 0:6
prawdopodobienstwa <- dbinom(x, n, p)
plot(x, prawdopodobienstwa, type="h", lwd=3,
     xlab="liczba reszek (X)", ylab="P(X = x)",
     ylim=c(0, max(prawdopodobienstwa)*1.1))
points(x, prawdopodobienstwa, pch=19)