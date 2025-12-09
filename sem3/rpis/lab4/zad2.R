# a) i)
set.seed(123)
n_kostka <- 60000
u_kostka <- runif(n_kostka)
rzuty <- floor(u_kostka * 6) + 1
u_kostka

# a) ii)
srednia_proby <- mean(rzuty)
wariancja_proby <- var(rzuty)

cat("srednia:", srednia_proby, "(powinno: 3.5)\n")
cat("wariancja:", wariancja_proby, "(powinno ~2.916 [35/12])\n")

# a) iii)
tabela_czestosci <- table(rzuty)
print(tabela_czestosci)

# a) iv)
df_rzuty <- as.data.frame(rzuty)
var(df_rzuty)


# b)
rzuty_sample <- sample(x = 1:6, size = 600, replace = TRUE)
hist(rzuty_sample, breaks = 0:6)

