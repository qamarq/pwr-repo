lambda <- 3 * 2

# a) P(X = 5)
p_a <- dpois(5, lambda)
print(paste("P(X = 5) =", p_a))

# b) P(X ≥ 4) = 1 - P(X ≤ 3)
p_b <- 1 - ppois(3, lambda)
print(paste("P(X ≥ 4) =", p_b))

# c) P(3 ≤ X ≤ 5) = P(X ≤ 5) - P(X ≤ 2)
p_c <- ppois(5, lambda) - ppois(2, lambda)
print(paste("P(3 ≤ X ≤ 5) =", p_c))

# d) 0 ≤ x ≤ 30
x <- 0:30
prawdopodobienstwa <- dpois(x, lambda)
plot(x, prawdopodobienstwa, type="h", lwd=2,
     xlab="sprzedane auta (X)", ylab="P(X = x)")
points(x, prawdopodobienstwa, pch=19)