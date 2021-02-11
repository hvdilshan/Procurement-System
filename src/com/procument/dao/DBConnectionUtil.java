package com.procument.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.procument.common.CommonConstants;
import com.procument.common.CommonUtil;


public class DBConnectionUtil {
	
	private static Connection connection;
	
	public static Connection getConnection() {
		System.out.println(CommonUtil.properties.getProperty(CommonConstants.DRIVER_NAME));
		//Checking the instance 
		if(connection == null) {
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/procument_system", "root", "");
				System.out.println("Connected");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}			
		}
		
		return connection;
	}
}
