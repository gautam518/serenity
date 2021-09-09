package com.loyaltyprimemultipartnerapp.utlis;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TestUtils {
	public static String getRandomAlphaNumbericValue() {
		String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvxyz";
		Integer n = 5;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0; i < n; i++) {
			int index = (int) (AlphaNumericString.length() * Math.random());
			sb.append(AlphaNumericString.charAt(index));
		}
		return sb.toString();
	}

	public static String getRandomValue() {
		Random random = new Random();
		int radomInt = random.nextInt(100000);
		return Integer.toString(radomInt);
	}

	public static String getRandomNumber() {
		Random ran = new Random();
		int num = (ran.nextInt(100000) % 10);

		return Integer.toString(num);
	}

	public static String getSameRandomValue() {
		int radomInt = 65442311;
		return Integer.toString(radomInt);
	}

	public static String getSpecialKeyward() {
		String SPEC = "@#$%^&+=";
		int index;
		String pass = "";
		Random rnd = new Random();
		// 2 random chars from 'SPEC'
		index = rnd.nextInt(SPEC.length());
		pass += SPEC.charAt(index);
		index = rnd.nextInt(SPEC.length());
		pass += SPEC.charAt(index);

		return pass;

	}
	
	public static String getRandowmSpecialCharacters()
	{
		final String alphabet = "!@#$%^&*()}+-=~`:;.,?\"<>";
		final int N = alphabet.length();
		Random rd = new Random();
		int iLength = 30;
		StringBuilder sb = new StringBuilder(iLength);
		for (int i = 0; i < iLength; i++) {
		    sb.append(alphabet.charAt(rd.nextInt(N)));
		}
		return sb.toString();
	}

	// To get date
	public static String getDateTime() {
		String outTime;
		ZonedDateTime dateTime = ZonedDateTime.now();
		outTime = dateTime.toString();
		outTime = outTime.substring(0, 10) + "T00:00:00.000Z";
		return outTime;

	}
	
	public static String GetPreviousDateTime() {		
		String outTime;
		ZonedDateTime dateTime = ZonedDateTime.now();
		outTime = dateTime.minusDays(2).toString();
		outTime = outTime.substring(0, 10) + "T00:00:00.000Z";
		return outTime;
	}
	
	public static String GetPointExpiryDateTime() {		
		String outTime;
		ZonedDateTime dateTime = ZonedDateTime.now();
		//outTime = dateTime.minusDays(61).toString();
		outTime = dateTime.minusMonths(2).toString();
		outTime = outTime.substring(0, 10) + "T00:00:00.000Z";
		return outTime;
	}
	
	public static String GetExpiryFutureDateTime() {
		String outTime;
		
		ZonedDateTime dateTime = ZonedDateTime.now();
		outTime = dateTime.plusMonths(2).toString();
		outTime = outTime.substring(0, 10) + "T00:00:00.000Z";
		return outTime;
	}
}
