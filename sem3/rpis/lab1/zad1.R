a <- c(1, 4, 6, 13, -10, 8)
b <- seq(1, 101, 2)
c <- rep(c(4, 7, 9), each = 3)
d <- c("czy", "to", "jest", "wektor", NA)
e <- c("czy", "to", "jest", "wektor", "NA")
f <- c(rep(c(4, 7, 9), 6))

wektory <- list(a, b, c, d, e, f)

for (i in seq_along(wektory)) {
  v <- wektory[[i]]
  print(paste("dlugosc wektora", i, "wynosi", length(v)))
  print(paste("typ wektora", i, "to", typeof(v)))
  print(paste("min wartosc wektora", i, "to", min(v)))
  print(paste("max wartosc wektora", i, "to", max(v)))

  if (typeof(v) != "character") {
    print(paste("suma wektora", i, "to", sum(v)))
  } else {
    print("brak sumy dla characterow")
  }

  cat("\n")
}


sort(d)
sort(e)



a + f
a * f
a + c
a + 10
a * 15
b[26]
f[6:10]

b[b > 50]
print(paste(length(b[b > 50]), "jest wiekszych od 50 w wektorze b"))
