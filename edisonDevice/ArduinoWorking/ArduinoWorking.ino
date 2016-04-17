int dataPin = 3;
int clockPin = 2;

int a;
void delay_centiMicroseconds(int len){
  a=0;
  while(a<len)
    a++;
}


void setup() {

  Serial.begin(9600);
  Serial.println("Hello Pinata");
  pinMode(dataPin, INPUT);
  pinMode(clockPin, OUTPUT);
  
}


void loop() {

//while(1){
//  digitalWrite(clockPin, HIGH);
//  delay_centiMicroseconds(200);
//  digitalWrite(clockPin, LOW);
//  delay_centiMicroseconds(200);
//  }


  uint32_t output = 0;
  for (int i = 0; i < 24; i++) {
      digitalWrite(clockPin, HIGH);
      delay_centiMicroseconds(200);
      output |= (digitalRead(dataPin)<< (23-i));
      digitalWrite(clockPin, LOW);
      delay_centiMicroseconds(400);
    }
    digitalWrite(clockPin, HIGH);
    delay_centiMicroseconds(200);
    digitalWrite(clockPin, LOW);
    delay(1000);

    Serial.print(output);
    Serial.println("");
}
