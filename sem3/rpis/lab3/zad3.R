a <- 4
b <- 12

# i) P(X < 7)
p_a <- punif(7, a, b)
print(paste("P(X < 7) =", p_a))

# ii) P(5 < X < 11) = P(X ≤ 11) - P(X ≤ 5)
p_b <- punif(11, a, b) - punif(5, a, b)
print(paste("P(5 < X < 11) =", p_b))

# iii) P(X > 10) = 1 - P(X ≤ 10)
p_c <- 1 - punif(10, a, b)
print(paste("P(X > 10) =", p_c))

# iv) P(X > x) = 0.6
# 1 - P(X ≤ x) = 0.6, wiec P(X ≤ x) = 0.4
x_d <- qunif(0.4, a, b)
print(paste("x takie, ze P(X > x) = 0.6:", x_d))