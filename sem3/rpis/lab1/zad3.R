a <- seq(300, 0, -3)
b <- c("one", "two", "three", "four", 5)
c <- c("one", "two", "three", "four", "5")
d <- rep(c(3, 1, 6), 4)
e <- rep(c(3, 1, 6), each = 3)
f <- c(5, 1, 4, 7)

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

sort(b)
sort(e)


d + f
sum(d * e)
a[35]
a[67:85]


a[a < 100]
print(paste(length(a[a < 100]), "jest mniejszych od 100 w wektorze a"))
