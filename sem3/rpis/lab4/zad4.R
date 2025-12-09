par(mfrow = c(1, 2))

# i)
n_prob <- 10
p_bin <- 0.3
liczba_realizacji <- 100

wyniki_bin <- numeric(liczba_realizacji)

for (i in 1:liczba_realizacji) {
  u <- runif(n_prob)
  
  liczba_sukcesow <- length(which(u < p_bin))
  
  wyniki_bin[i] <- liczba_sukcesow
}

mean(wyniki_bin)
hist(wyniki_bin)

# ii) 
p_geom <- 0.4
liczba_realizacji_geom <- 50

wyniki_geom <- numeric(liczba_realizacji_geom)

for (i in 1:liczba_realizacji_geom) {
  
  liczba_prob <- 1
  sukces <- FALSE
  
  while (!sukces) {
    u <- runif(1)
    
    if (u < p_geom) {
      sukces <- TRUE
    } else { 
      liczba_prob <- liczba_prob + 1
    }
  }
  
  wyniki_geom[i] <- liczba_prob
}

mean(wyniki_geom)
hist(wyniki_geom)

par(mfrow = c(1, 1))