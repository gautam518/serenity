package com.loyaltyprimemultipartnerapp.utlis;

import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {	
	
	public Properties LoadProperties() {

		try {
			InputStream inStream = getClass().getClassLoader().getResourceAsStream("config.properties");
			Properties prop = new Properties();
			prop.load(inStream);
			return prop;
		}catch(Exception e) {
			System.out.println("File not found exception thrown for config.properties file.");
			return null;
		}

	}
}
