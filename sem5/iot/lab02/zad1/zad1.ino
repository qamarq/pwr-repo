#define LED_RED 6
#define LED_GREEN 5
#define LED_BLUE 3

#define RED_BUTTON 2
#define GREEN_BUTTON 4

const int leds[3] = {LED_RED, LED_GREEN, LED_BLUE};

int color = 0;        // 0 = czerwony, 1 = zielony, 2 = niebieski
bool ledOn = true;    // czy dioda ma świecić

bool lastRedState = HIGH;
bool lastGreenState = HIGH;

void showLed()
{
  for (int i = 0; i < 3; i++)
  {
    if (ledOn && i == color)
      digitalWrite(leds[i], HIGH);
    else
      digitalWrite(leds[i], LOW);
  }
}

void setup()
{
  pinMode(LED_RED, OUTPUT);
  pinMode(LED_GREEN, OUTPUT);
  pinMode(LED_BLUE, OUTPUT);

  pinMode(RED_BUTTON, INPUT_PULLUP);
  pinMode(GREEN_BUTTON, INPUT_PULLUP);

  showLed();
}

void loop()
{
  bool redState = digitalRead(RED_BUTTON);
  bool greenState = digitalRead(GREEN_BUTTON);

  // czerwony przycisk: włącz/wyłącz (reakcja tylko w momencie naciśnięcia)
  if (redState == LOW && lastRedState == HIGH)
  {
    ledOn = !ledOn;
    showLed();
    delay(300);   // proste ograniczenie drgań styków
  }

  // zielony przycisk: zmiana koloru
  if (greenState == LOW && lastGreenState == HIGH)
  {
    color = (color + 1) % 3;
    showLed();
    delay(300);
  }

  lastRedState = redState;
  lastGreenState = greenState;
}