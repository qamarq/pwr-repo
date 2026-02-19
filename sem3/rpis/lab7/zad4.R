model_mieszkanie <- function(dane, metraz = 80) {
  model <- lm(Cena ~ Metraz, data = dane)
  reszty <- residuals(model)
  
  list(
    model = summary(model),
    normalnosc_reszt = shapiro.test(reszty),
    estymacja_80m2 = predict(model, newdata = data.frame(Metraz = metraz))
  )
}

m <- read.csv2("mieszkania.csv")
plot(m$Metraz, m$Cena)
abline(lm(Cena ~ Metraz, data = m), col = "red")

model_mieszkanie(m)