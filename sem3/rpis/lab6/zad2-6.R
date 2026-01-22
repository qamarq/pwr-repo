wniosek_p <- function(p){
  if(p > 0.05){
    cat("p >", p, "-> nie odrzucamy H0, brak dowodow przeciwko H0\n")
  } else if(p > 0.01){
    cat("0.01 < p <= 0.05 -> odrzucamy H0, mamy dowody przeciwko H0\n")
  } else if(p > 0.001){
    cat("0.001 < p <= 0.01 -> odrzucamy H0, mamy mocne dowody przeciwko H0\n")
  } else {
    cat("p <= 0.001 -> odrzucamy H0, bardzo mocne dowody przeciwko H0\n")
  }
}

dane <- read.csv2("waga1.csv")

dane$przyrost <- dane$Waga_po - dane$Waga_przed

# --- zad 2 ---

res2 <- t.test(dane$przyrost, mu=2)

# H0: wszyscy studenci srednio przytyli 2kg
# H1: sredni przyrost != 2kg

res2$p.value
wniosek_p(res2$p.value)

# --- zad 3 ---

dane$ponad70 <- dane$Waga_po > 70
tab70 <- table(dane$plec, dane$ponad70)

res3 <- prop.test(tab70)

# H0: proporcje sa takie same
# H1: proporcje sa rozne

res3$p.value
wniosek_p(res3$p.value)

# --- zad 4 ---

res4 <- t.test(Wzrost ~ plec, data=dane, mu=-5)

# H0: mezczyzni sa srednio o 5cm wyzsi
# H1: roznica != 5cm

res4$p.value
wniosek_p(res4$p.value)

# --- zad 5 ---

przytyl <- dane$przyrost > 0
res5 <- prop.test(sum(przytyl), length(przytyl), p=0.8)

# H0: 80% studentow przybiera na wadze
# H1: odsetek != 80%

res5$p.value
wniosek_p(res5$p.value)

# --- zad 6 ---

res6 <- t.test(dane$przyrost[dane$plec==0], mu=4)

# H0: mezczyzni srednio przytyli 4kg
# H1: srednia != 4kg

res6$p.value
wniosek_p(res6$p.value)
