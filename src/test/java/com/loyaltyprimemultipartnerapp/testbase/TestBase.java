package com.loyaltyprimemultipartnerapp.testbase;

import java.io.InputStream;
import java.util.Properties;

import org.junit.BeforeClass;

import io.restassured.RestAssured;


public class TestBase {
	
	@BeforeClass
	public static void init()
	{
		RestAssured.baseURI="http://localhost:8086";
		//RestAssured.baseURI="https://loyalty-prime-dev.cloud/test-pc-610-op";
	}
	
	public Properties LoadProperties() {
		try {
			InputStream inStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			return prop;
		} catch (Exception e) {
			System.out.println("File not found exception thrown for config.properties file.");
			return null;
		}
	}
	
}
