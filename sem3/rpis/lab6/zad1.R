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

# --- zad 1a ---

kobiety <- 520
mezczyzni <- 480
kobiety_w <- 220
mezczyzni_w <- 165

p1 <- kobiety_w / kobiety
p2 <- mezczyzni_w / mezczyzni
p <- (kobiety_w + mezczyzni_w) / (kobiety + mezczyzni)

Z <- (p1 - p2) / sqrt(p*(1-p)*(1/kobiety + 1/mezczyzni))
p_value <- 2*(1-pnorm(abs(Z)))

# H0: prawdopodobienstwo ukonczenia studiow nie zalezy od plci
# H1: prawdopodobienstwo ukonczenia studiow zalezy od plci

p_value
wniosek_p(p_value)

res_prop <- prop.test(
  x = c(kobiety_w, mezczyzni_w),
  n = c(kobiety, mezczyzni),
  correct = FALSE
)

wniosek_p(res_prop$p.value)

# --- zad 1b + 1c ---

tab <- matrix(
  c(kobiety_w, kobiety-kobiety_w,
    mezczyzni_w, mezczyzni-mezczyzni_w),
  nrow = 2,
  byrow = TRUE
)

tab

res_chi <- chisq.test(tab)

# H0: wyksztalcenie nie zalezy od plci
# H1: wyksztalcenie zalezy od plci

wniosek_p(res_chi$p.value)

res_f <- fisher.test(tab)
wniosek_p(res_f$p.value)

# --- zad 1d ---

Z2 <- (166 - 174) / sqrt(100/520 + 121/480)
p2 <- 2*(1 - pnorm(abs(Z2)))

# H0: sredni wzrost nie zalezy od plci
# H1: sredni wzrost zalezy od plci

p2
wniosek_p(p2)
