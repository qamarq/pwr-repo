par(mfrow = c(1, 2))

#a) i)
n <- 200
u <- runif(n)
inv <- 2 * sqrt(u)

hist(x_inv, freq = FALSE, breaks = 10)

# b)
n_b <- 400000
resp <- numeric(n_b)
count <- 0
how_many_tries <- 0

while(count < n_b) {
  how_many_tries <- how_many_tries + 1
  cand_x <- runif(1, 0, 2)
  u = runif(1)
  
  if (u <= 0.5 * cand_x) {
    count <- count + 1
    resp[count] <- cand_x
  }
}

hist(resp, freq = FALSE, breaks = 10)

par(mfrow = c(1, 1))
cat("tried: ", how_many_tries)