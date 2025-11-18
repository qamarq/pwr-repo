lambda = 4

# a) P(T > 30s) = P(T > 0.5 min)
p_a <- 1 - pexp(0.5, rate=lambda)
print(paste("P(T > 30s) =", p_a))

# b) P(T < 20s) = P(T < 1/3 min)
p_b <- pexp(1/3, lambda)
print(paste("P(T < 20s) =", p_b))

# c) P(40s < T < 80s) = P(2/3 < T < 4/3)
p_c <- pexp(4/3, lambda) - pexp(2/3, lambda)
print(paste("P(40s < T < 80s) =", p_c))

# d) P(T > t) = 0.2, wiec P(T ≤ t) = 0.8
t_d <- qexp(0.8, lambda)
print(paste("t (min):", t_d))
print(paste("t (s):", t_d*60))

# e) 0 ≤ t ≤ 3
t <- seq(0, 3, by=0.01)
gestosc <- dexp(t, lambda)
plot(t, gestosc, type="l", lwd=2,,
     xlab="czas (min)", ylab="gestosc f(t)")
grid()
