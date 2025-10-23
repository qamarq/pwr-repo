a <- matrix(c(-3, 4, 1, -5, -2, 3), nrow = 2)
b <- cbind(c(1, 3, 5), c(2, 4, 6))
c <- rbind(c(7, -3), c(-2, 1))
d <- matrix(c(1, 3, 5, 2, 5, 3, 4, 7, 2), nrow = 3)

print(a)
print(b)
print(c)
print(d)

# A + B
t(a) + b
b %*% a
# B %*% B
solve(c)
round(c %*% solve(c), 10)

x1 <- b %*% solve(c)
print(x1)
d_inv <- solve(d)
x2 <- d_inv %*% b
print(x2)
