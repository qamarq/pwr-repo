b <- read.csv2("bakteria.csv")

plot(b$czas, b$masa)

model_log <- lm(log(masa) ~ czas, data = b)

summary(model_log)

prognoza_masy <- function(t) {
  exp(predict(model_log, newdata = data.frame(czas = t)))
}

prognoza_masy(9)