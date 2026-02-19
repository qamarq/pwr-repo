ks_exp <- function(n = 1000, alpha = 0.05) {
  x <- rexp(n, rate = 1)
  
  list(
    normalny = ks.test(x, "pnorm", mean = 1, sd = 1),
    wykladniczy = ks.test(x, "pexp", rate = 1)
  )
}

ks_exp()

ks_gamma <- function(n = 1000) {
  x <- rgamma(n, shape = 100, scale = 1)
  
  list(
    normalny = ks.test(x, "pnorm", mean = 100, sd = 10),
    gamma = ks.test(x, "pgamma", shape = 100, scale = 1)
  )
}

ks_gamma()