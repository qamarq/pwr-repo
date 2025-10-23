a <- matrix(c(3, 1, 2, 4, 5, 3), nrow = 2, byrow = TRUE)
b <- matrix(c(-1, 2, 3, -4, -5, 6), nrow = 3, byrow = TRUE)
c <- cbind(c(7, 2), c(3, 1))
d <- rbind(c(1, 2, 4), c(3, 5, 7), c(5, 7, 11))

print(a)
print(b)
print(c)
print(d)


a + b
cat("A + t(B):\n")
print(a + t(b))
cat("\nAB:\n")
print(a %*% b)
cat("\nAA:\n")
print(a %*% a)
cat("\nD^-1:\n")
print(solve(d))
cat("\nDD^-1:\n")
print(round(d %*% solve(d), 10))

x1 <- solve(c) %*% a
print(x1)

x2 <- a %*% solve(d)
print(x2)
