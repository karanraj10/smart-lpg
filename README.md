# Smart LPG (Android Application + IOT)
## LPG Monitoring / Controller Sysytem

## Introduction

* This system is based on Node MCU, MQ Gas Sensor, Load Cell Sensor, Servo Motor, Buzzer and Android application.
* Android application allows user to login/ register themselves.
* Allows users to get overview of current running LPG.
* Allows users to get Detailed Usages history of LPG as well as report generation.
*	Allows users to control valve with on/off button.
*	Allows users to send Email of new LPG booking.
* When the gas sensor detects gas in atmosphere, it will give a digital output of 1 and if gas is not detected the sensor will give a digital output of 0. 
* Node MCU will take the sensor output and if sensor output is high, then the buzzer will start and notification (Gas Leakage has been detected) will be send to Android application.
* The load cell sensor detects LPG weight, it will give a analog output. Node MCU will take the sensor output and covert into required metric and store into database with date and time.
* When Node MCU detects valve status on in database it will rotate servo motor 180 degree clockwise to turn on valve and if valve status is detected as off it will rotate servo motor 180 degree counter clockwise.


## Technolgy

* Android, Arduino, Firebase, PHP

## Snapshots

### Splash Screen
<img src="https://user-images.githubusercontent.com/60970286/163170088-852e87be-380a-42c0-bd21-dfdb14c60378.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Home Screen
<img src="https://user-images.githubusercontent.com/60970286/163170106-5b04d9b3-da97-46e1-b43a-2bd10162fa02.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Home Screen
<img src="https://user-images.githubusercontent.com/60970286/163170115-dc8370b2-72a3-4fb0-95d8-8f537e97459d.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Control Vlave Screen
<img src="https://user-images.githubusercontent.com/60970286/163170123-a1868231-aece-4f3e-bfb2-830c4599f21a.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Daily Usages Screen
<img src="https://user-images.githubusercontent.com/60970286/163170270-fc86ba52-ad50-4efb-86ed-c07beb018f59.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### History (Past LPGs) Screen
<img src="https://user-images.githubusercontent.com/60970286/163170283-480c47ec-177a-410b-8b3e-1ee9c63465a3.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Usages History Screen
<img src="https://user-images.githubusercontent.com/60970286/163170324-29f50900-5100-4a2f-bd14-b05cf25bae19.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Report (PDF) Screen
<img src="https://user-images.githubusercontent.com/60970286/163170341-bbc8964b-c5dc-40a7-8770-8ba96002a099.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Settings Screen
<img src="https://user-images.githubusercontent.com/60970286/163170180-67d7635b-b12b-4868-afb8-6c38980a5b02.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Update Account Screen
<img src="https://user-images.githubusercontent.com/60970286/163170205-92d4f9cb-8777-47cb-9f54-acd1278db8e2.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Update Password
<img src="https://user-images.githubusercontent.com/60970286/163170232-568376d8-8e93-487b-9b7c-085e92686cce.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Request LPG Screen
<img src="https://user-images.githubusercontent.com/60970286/163170243-77e308f8-759b-4acf-88a2-1ee5a5d7c838.jpg" width="300" height="600"/>
</br></br>
<hr style="border:2px solid grey"></hr>
</br></br>

### Notification Screen
<img src="https://user-images.githubusercontent.com/60970286/163170390-bc69c137-0b16-41b5-8c9f-25b649718466.jpg" width="300" height="600"/>
