#include <Firebase.h>  
#include <FirebaseArduino.h>  
#include <FirebaseCloudMessaging.h>  
#include <FirebaseError.h>  
#include <FirebaseHttpClient.h>  
#include <FirebaseObject.h>  
#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>

#include<ezTime.h>
Timezone myLocalTime;

#include<Servo.h>
Servo servo;

#define FIREBASE_HOST ""  
#define FIREBASE_AUTH ""  

#define WIFI_SSID "Karan's Net 2"  
#define WIFI_PASSWORD "karan@2022"  

String MAC_ADDRESS;
String password;
String serverIp;
int i=0;
boolean leakage =false;
boolean onOffStatus =false;
float initWeight = 30;
float currentWeight = 30;
float prevWeight;
float used;
boolean lastValveStatus = false;

String date,mytime;

unsigned long previousMillis = 0;

//const unsigned long SECOND = 1000;
//const unsigned long HOUR = 3600*SECOND;
//const long interval = (6*HOUR);

const long interval = 60000;

void setup() {  
  Serial.begin(9600);  
  
  // connect to wifi.  
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);  
  Serial.print("connecting");  
  while (WiFi.status() != WL_CONNECTED) {  
    Serial.print(".");  
    delay(500);  
  }  
 
  Serial.println();  
  Serial.print("connected: ");  
  Serial.println(WiFi.localIP());
  Serial.println(WiFi.macAddress());

  MAC_ADDRESS = WiFi.macAddress();
  password = "karan@2002";
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.setString("users/"+MAC_ADDRESS+"/password", password);
  
  if (Firebase.failed()) {
     Serial.print("setting /user failed:");
     Serial.println(Firebase.error());  
     return;
 }

 serverIp = Firebase.getString("params/localhost");
 
 servo.attach(5);
 servo.write(0);
  
  //time
  myLocalTime.setLocation(F("in"));
  waitForSync();
} 
 
void loop() { 
  date = myLocalTime.dateTime("d-m-Y");
  mytime = myLocalTime.dateTime("H:i");

  boolean valveStatus = Firebase.getBool("users/"+MAC_ADDRESS+"/LPG/"+i+"/onOffStatus");
  Serial.println(valveStatus);
  
  if(valveStatus==true && lastValveStatus==false){
    lastValveStatus=true;
    Serial.println("Turning On");
    servo.write(180);
    
  }else if(valveStatus==false && lastValveStatus==true){
    lastValveStatus=false;
    Serial.println("Turning Off");
    servo.write(0);
  }
  
  if(currentWeight == initWeight){
    i++;
    prevWeight = 30;
    Firebase.setFloat("users/"+MAC_ADDRESS+"/LPG/"+i+"/initWeight", initWeight);
    Firebase.setFloat("users/"+MAC_ADDRESS+"/LPG/"+i+"/currentWeight", currentWeight);
    Firebase.setBool("users/"+MAC_ADDRESS+"/LPG/"+i+"/leakage", leakage);
    Firebase.setBool("users/"+MAC_ADDRESS+"/LPG/"+i+"/onOffStatus", onOffStatus);
    Firebase.setString("users/"+MAC_ADDRESS+"/LPG/"+i+"/joinDate", date);
    Firebase.setString("users/"+MAC_ADDRESS+"/LPG/"+i+"/joinTime", mytime);
    writeUsageData();
    sendDataToFirebase();
  }
  else{
    Firebase.setFloat("users/"+MAC_ADDRESS+"/LPG/"+i+"/currentWeight", currentWeight);
    Firebase.setBool("users/"+MAC_ADDRESS+"/LPG/"+i+"/leakage", leakage);

    unsigned long currentMillis = millis();
    if (currentMillis - previousMillis >= interval) {
      previousMillis = currentMillis;
      writeUsageData();
    }
  }
  delay(1000);
}
void writeUsageData()
{

  used = prevWeight - currentWeight;
  prevWeight = currentWeight;
  Firebase.setFloat("users/"+MAC_ADDRESS+"/LPG/"+i+"/usage/"+date+"/"+mytime,used);

  if(currentWeight<=0.0){
    currentWeight = 30;
  }
  else{
    currentWeight -= 0.5;
  }
}

void sendDataToFirebase() {
  WiFiClient client;
  HTTPClient http;
  String key = MAC_ADDRESS;
  key.replace(":","");
  String server = serverIp+"/firebase.php";
  String data = "topic="+key+"&title=Lpg Controller&body=New Lpg has been connected!";
  
  http.begin(client,server);
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  Serial.println(data);
  int httpCode = http.POST(data);
  String payload = http.getString();

  Serial.println(httpCode);
  Serial.println(payload);
  http.end();
}
