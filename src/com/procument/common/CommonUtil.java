package com.procument.common;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CommonUtil {
	
	public static final Properties properties = new Properties();
	private static final Logger Log = Logger.getLogger(CommonUtil.class.getName());
	
	static {
		try {
			properties.load(QueryUtil.class.getResourceAsStream(CommonConstants.PROPERTY_FILE));
		}catch(IOException exception) {
			Log.log(Level.SEVERE, exception.getMessage());
		}catch(IllegalArgumentException exception) {
			Log.log(Level.SEVERE, exception.getMessage());
		} catch(RuntimeException exception) {
			Log.log(Level.SEVERE, exception.getMessage());
		}catch (Exception exception) {
			Log.log(Level.SEVERE, exception.getMessage());
		}
	}
}
