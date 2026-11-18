#include <LiquidCrystal_I2C.h>

#define POTENTIOMETER A0
#define V_REF 5.0

LiquidCrystal_I2C lcd(0x27, 16, 2);

int shownValue = -1;

void setup()
{
  lcd.init();
  lcd.clear();
  lcd.backlight();

  lcd.print("ADC:");
  lcd.setCursor(0, 1);
  lcd.print("U:");

  Serial.begin(9600);
  while (!Serial)
    ;
}

void loop()
{
  int value = analogRead(POTENTIOMETER);

  Serial.print("min:0 max:1023 adc:");
  Serial.println(value);

  if (value != shownValue)
  {
    shownValue = value;

    lcd.setCursor(5, 0);
    lcd.print(value);
    lcd.print("    ");

    lcd.setCursor(5, 1);
    lcd.print(value * V_REF / 1024, 3);
    lcd.print(" V");
  }

  delay(10);
}
