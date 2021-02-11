package com.procument.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBConnectionUtil extends CommonUtil{

	private static Connection connection;
	private static final Logger Log = Logger.getLogger(DBConnectionUtil.class.getName());
	
	public static Connection getConnection() {
		
		//Checking the instance 
		if(connection == null) {
			
			try 
			{
				Class.forName(properties.getProperty(CommonConstants.DRIVER_NAME));
				connection = DriverManager.getConnection(properties.getProperty(CommonConstants.URL), properties.getProperty(CommonConstants.USERNAME),
						properties.getProperty(CommonConstants.PASSWORD));
			} catch(SQLException exception) {
				exception.printStackTrace();
				Log.log(Level.SEVERE, exception.getMessage());
			}catch (Exception exception) {
				exception.printStackTrace();
				Log.log(Level.SEVERE, exception.getMessage());
			} 			
		}
		
		return connection;
	}
}
