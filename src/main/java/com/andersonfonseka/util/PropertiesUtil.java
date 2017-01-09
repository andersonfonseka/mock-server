package com.andersonfonseka.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtil {

	private static PropertiesUtil instance;

	private Properties properties;
	
	private Properties errorProperties;
	
	private PropertiesUtil(){
		
		 properties = new Properties();
		 errorProperties = new Properties();
		 
		 try {
			 
			 InputStream inStream = PropertiesUtil.class.getResourceAsStream("/label.properties");
			 properties.load(inStream);
			 
			 inStream = PropertiesUtil.class.getResourceAsStream("/error.properties");
			 errorProperties.load(inStream);
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static PropertiesUtil getInstance(){
		if (instance == null) {
			instance = new PropertiesUtil();
		}
		return instance;
	}
	
	public String getLabel(String key){
		return properties.getProperty(key);
	}
	
	public String getError(String key){
		return errorProperties.getProperty(key);
	}
	
}
