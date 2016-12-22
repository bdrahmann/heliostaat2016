/* BT_code afhandeling

De ontvangen informatie bestaat uit 2 characters, daarna de info uit de Android
en het wordt afgesloten met een "#".

De return informatie bestaat uit een record dat is opgebouwd uit
een kode van 2 posities, direct gevolgd door de informatie. Het
geheel wordt afgesloten met een "#".


* 1:
* 2: Arduino send Longitude 02
* 3: Arduino send Latitude 03
* 4: Arduino send his date 04
* 5 Arduino send his Time 05
* 6 Arduino send his timezone 06
* 7 Arduino send summer or wintertime 07
* 8 Arduino send Heliostat or sunfolger 08
* 9 Arduino send north/south 09
* 10 Arduino send update interval (sec) 10
* 11 Arduino send moveAwayFromLimitSwitch 11
* 12 Arduino send Hour reset (hour) 12
* 13 Arduino send Target Alt 13
* 14 Arduino send Target Az 14
* 15 Arduino send MachineTargetTabel in ��n keer 15
* 16 Arduino send Manual on/off  M=manual/A=automatisch 16
* 17 Arduino send Windprotection on/off
* 18 Arduino send Sun's alt&az (alt en az gescheiden door '&'
* 19 Arduino send Machine number
* 20 Arduino send Machines alt&az
* 21 Arduino send Targets alt&az

*/

void ReadBT()	// lees de Bluetooth input.
{
	int Heliostat = 1;	// 1 = Zonnevolger, 0 = Heliostaat
	int Manual = 0;		// 0 = automatisch, 1 = manual

	char inChar = 0;
	String s = "";
	String Arduinoinfo = "";
	String Androidinfo = "";
	String inBuffer = "";
	int kode = 0;
	

	// wait for character,
	// 
	// get character sent from Android device
	if (Serial3.available()) {
		inBuffer = "";
		while (inChar != '#')	{	// zolang einde info nog niet bereikt is
			inChar = Serial3.read();	// lees
			inBuffer = inBuffer + String(inChar);
			delay(5);
		}
		Serial.println();
		s = inBuffer.substring(0, 2);	// eerste twee characters is de kode
		Androidinfo = inBuffer.substring(2,inBuffer.length()-1);
		kode = s.toInt();
		Serial.print("50: kode = ");
		Serial.print(kode);
		Serial.print("   Androidinfo = ");
		Serial.println(Androidinfo);
	

		switch (kode) {
		case 1:		// Android send 'Connected'
			BTconnected = true;	
			break;
		case 2:			// Arduino send Longitude 02
			// if Androidinfo<> ""
			// unpack Androidinfo 
			// fill in Androidinfo in longitude
			if (!Androidinfo.equals("")) {
				Androidinfo.toCharArray(strbuffer, 15);
				longitude = strtod(strbuffer, NULL);
				EEPROM.put(4087, longitude);
			}
			dtostrf(longitude, 8, 4, strbuffer);
			Arduinoinfo = strbuffer;
			Arduinoinfo = "02" + Arduinoinfo + "#";
			Serial3.print(Arduinoinfo);
			break;
		case 3:			// Arduino send Latitude
			if (!Androidinfo.equals("")) {
				Androidinfo.toCharArray(strbuffer, 15);
				latitude = strtod(strbuffer, NULL);
				EEPROM.put(4091, latitude);
			}
			dtostrf(latitude, 8, 4, strbuffer);
			Arduinoinfo = strbuffer;
			Arduinoinfo = "03" + Arduinoinfo + "#";
			Serial3.print(Arduinoinfo);
			break;

		case 4:		// Arduino send his date 
			if (!Androidinfo.equals("")) {
				String tyd = "";
				// uitpakken in day:month:year
				tyd = Androidinfo.substring(6);
				year = tyd.toInt();
				yearRTC = year-2000;
				tyd = Androidinfo.substring(3, 5);
				month = tyd.toInt();
				monthRTC = month;
				tyd = Androidinfo.substring(0, 2);
				day = tyd.toInt();
				dayOfMonthRTC = day;
				RTC_Code::setDateDs1307(secondRTC, minuteRTC, hourRTC, 5, dayOfMonthRTC, monthRTC, yearRTC);
				Serial.println("110: " + String(dayOfMonthRTC) + ":" + String(monthRTC) + ":" + String(yearRTC) + "#");
			}

			Arduinoinfo = "04" + String(day) + ":" + String(month) + ":" + String(year) + "#";
			Serial3.print(Arduinoinfo);
			break;
			
		case 5:		// Arduino sends his time
			if (!Androidinfo.equals("")) {
				String tyd = "";
				// uitpakken in hour:min:sec
				tyd = Androidinfo.substring(6);
				second = tyd.toInt();
				secondRTC = second;
				tyd = Androidinfo.substring(3, 5);
				minute = tyd.toInt();
				minuteRTC = minute;
				tyd = Androidinfo.substring(0, 2);
				hour = tyd.toInt();
				hourRTC = hour;
				RTC_Code::setDateDs1307(secondRTC, minuteRTC, hourRTC, 5, dayOfMonthRTC, monthRTC, yearRTC);
				Serial.println("132: " + String(dayOfMonthRTC) + ":" + String(monthRTC) + ":" + String(yearRTC) + "#");
				Serial.println("133: " + String(hour) + ":" + String(minute) + ":" + String(second) + "#");
			}

			Arduinoinfo = "05" + String((int)hour) + ":" + String((int)minute) + ":" + String((int)second) + "#";
			Serial3.print(Arduinoinfo);
			break;

		case 6:		// Arduino send his timezone
			if (!Androidinfo.equals("")) {
				Androidinfo.toCharArray(strbuffer, 15);
				timezone = strtod(strbuffer, NULL);
				EEPROM.put(4083, timezone);
			}
			dtostrf(timezone, 8, 0, strbuffer);
			Arduinoinfo = strbuffer;
			Arduinoinfo = "06" + Arduinoinfo + "#";
			Serial3.print(Arduinoinfo);
			break;

			
		case 7:		// Arduino send summer or wintertime
			if (!Androidinfo.equals("")) {
				if (Androidinfo == "Z") summertime = 1;
				else summertime = 0;
				int sumwin = 0;
				EEPROM.get(4061, sumwin);
				if (summertime != sumwin) {		// if summertime changed
					EEPROM.put(4061, summertime);	//   daylightsaving time in EEPROM
					if (summertime == 1) RTC_Code::setDateDs1307(secondRTC, minuteRTC, hourRTC-1, 5, dayOfMonthRTC, monthRTC, yearRTC); // zet klok terug
					else RTC_Code::setDateDs1307(secondRTC, minuteRTC, hourRTC+1, 5, dayOfMonthRTC, monthRTC, yearRTC); ;	// set clock + 1 hour
				}
			}
			if (summertime == 1)  Arduinoinfo = "Z"; else  Arduinoinfo = "W"; 
			Arduinoinfo = "07" + Arduinoinfo + "#";
			Serial3.print(Arduinoinfo);
			break;
		
		case 8:			// Arduino send Heliostat or sunfolger 08
			if (digitalRead(HeliostatToSun) == HIGH) Heliostat = 1; else Heliostat = 0;
			send08(Heliostat);	// Arduino send Heliostat or sunfolger 08
			break;

		case 9:			// Arduino send north/south
			if (!Androidinfo.equals("")) {		// "0" = North
				// Androidinfo.toCharArray(strbuffer,15);
				useNorthAsZero = Androidinfo.toInt();
				EEPROM.put(4081, useNorthAsZero);
			}
			Arduinoinfo = String(useNorthAsZero);
			Arduinoinfo = "09" + Arduinoinfo + "#";
			Serial3.print(Arduinoinfo);
			break;
			/*
			case 10:			// update interval (sec)
				dtostrf(updateint, 8, 2, strbuffer);
				Arduinoinfo = strbuffer;
				Arduinoinfo = "10" + Arduinoinfo + "#";
				Serial3.print(Arduinoinfo);
				break;
			case 11:
				dtostrf(MoveAway, 8, 4, strbuffer);
				Arduinoinfo = strbuffer;
				Arduinoinfo = "11" + Arduinoinfo + "#";
				Serial3.print(Arduinoinfo);
				break;
			case 12:
				dtostrf(hourReset, 8, 2, strbuffer);
				Arduinoinfo = strbuffer;
				Arduinoinfo = "12" + Arduinoinfo + "#";
				Serial3.print(Arduinoinfo);
				break;
			case 13:
				dtostrf(TargetAlt, 8, 2, strbuffer);
				Arduinoinfo = strbuffer;
				Arduinoinfo = "13" + Arduinoinfo + "#";
				Serial3.print(Arduinoinfo);
				break;
			case 14:
				dtostrf(TArgetAz, 8, 2, strbuffer);
				Arduinoinfo = strbuffer;
				Arduinoinfo = "14" + Arduinoinfo + "#";
				Serial3.print(Arduinoinfo);
				break;
			case 15:
				break;
			*/
		case 16:	// Arduino send Manual on/off
			if (digitalRead(manualModeOnOffPin) == HIGH) Manual = 1; else Manual = 0;
			send16(Manual);	// send Manual on/off
			break;

		case 17:	// Arduino send Windprotection on / off
			if (digitalRead(WindProtectionSwitch) == HIGH) Manual = 1; else Manual = 0;
			send17(Manual);	// send Windprotection on/off
			break;
		};

		


}  // einde if available 

}

void SendBT() {	// send de BT output
	int info = 0;	
	Arduinoinfo = dtostrf(second, 3, 0, strbuffer);
	Arduinoinfo = "05" + String((int)hour) + ":" + String((int)minute) + ":" + Arduinoinfo + "#";
	Serial.println("tijd doorgegeven aan ANDROID in 239: " + String(hour) + ":" + String(minute) + ":" + String(second) + "#");
	Serial3.print(Arduinoinfo); // stuur de tijd iedere loop
		
	if (digitalRead(HeliostatToSun) == HIGH) info = 1; else info = 0;
	send08(info);	// Arduino send Heliostat or sunfolger 
	if (digitalRead(manualModeOnOffPin) == HIGH) info = 1; else info = 0;
	send16(info);	// send Manual on/off
	if (digitalRead(WindProtectionSwitch) == HIGH) info = 1; else info = 0;
	send17(info);	// send windprotection on/off
}

void send08(int info) {		// Arduino send Heliostat or sunfolger 08
	if (info == 0) { Arduinoinfo = "H"; }
	else { Arduinoinfo = "Z"; }
	Arduinoinfo = "08" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
}	// end send08

void send16(int manual) {	// Arduino send Manual on/off
	
	if (manual == 0) { Arduinoinfo = "A"; }
	else { Arduinoinfo = "M"; }
	Arduinoinfo = "16" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
}	// end send16

void send17(int info) {

	if (info == 0) { Arduinoinfo = "O"; }
	else { Arduinoinfo = "P"; }
	Arduinoinfo = "17" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
}	// end send17

void send18(float alt, float az) {	// Arduino send Sun's alt&az (alt en az gescheiden door '&'
	
	dtostrf(alt, 8, 3, strbuffer);
	Arduinoinfo = strbuffer;
	Arduinoinfo = Arduinoinfo + "&";
	dtostrf(az, 8, 3, strbuffer);
	Arduinoinfo = Arduinoinfo + strbuffer;
	Arduinoinfo = "18" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
	delay(5);
}	// end send18

void send19(int machnum) {	// Arduino send Machine number
	Arduinoinfo = String(machnum);
	Arduinoinfo = "19" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
	delay(5);
}	// end send19

void send20(float alt, float az) {	// Arduino send Machines alt&az

	dtostrf(alt, 8, 3, strbuffer);
	Arduinoinfo = strbuffer;
	Arduinoinfo = Arduinoinfo + "&";
	dtostrf(az, 8, 3, strbuffer);
	Arduinoinfo = Arduinoinfo + strbuffer;
	Arduinoinfo = "20" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
	delay(5);
}	// end send20

void send21(float alt, float az) {	// Arduino send Targets alt&az

	dtostrf(alt, 8, 3, strbuffer);
	Arduinoinfo = strbuffer;
	Arduinoinfo = Arduinoinfo + "&";
	dtostrf(az, 8, 3, strbuffer);
	Arduinoinfo = Arduinoinfo + strbuffer;
	Arduinoinfo = "21" + Arduinoinfo + "#";
	Serial3.print(Arduinoinfo);
	delay(5);
}	// end send21
