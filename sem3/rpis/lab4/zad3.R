n_gen <- 1000
vals <- c(0,1,2,3)
prob <- c(0.15, 0.25, 0.5, 0.1)

data <- sample(vals, prob=prob, size=n_gen, replace=TRUE)
print(table(data) / n_gen)