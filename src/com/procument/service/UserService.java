package com.procument.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.procument.common.CommonConstants;
import com.procument.common.DBConnectionUtil;
import com.procument.common.QueryUtil;
import com.procument.model.User;


public class UserService {
	protected Connection connection = null;
	private static UserService userService;
	private static final Logger Log = Logger.getLogger(UserService.class.getName());
	
	public static UserService getInstance() {
		if(userService == null) {
			userService = new UserService();
		}
		
		return userService;
	}
	
	//initializing the Connection
	public UserService() {
		super();
		this.connection = DBConnectionUtil.getConnection();
	}
	
	//Employee Login
	public boolean login(String uname, String pass) {
		
		//preparedStatement
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			try {
				ps = (PreparedStatement) connection.prepareStatement(QueryUtil.Q(CommonConstants.LOGIN));
				ps.setString(1, uname);
				ps.setString(2, pass);
				
				rs = (ResultSet) ps.executeQuery();
				
				if(rs.next()) {
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		//check authentication
		
		return false;
	}
	
	public User getUserById(int user_id) {
		//create user instance
		User user= new User();
		
		//ps
		PreparedStatement ps = null;
		
		//rs
		ResultSet rs = null;
		
		//execute query
		try {
			try {
				ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.SELECT_USER_BY_ID));
				ps.setInt(1, user_id);
				
				rs = (ResultSet)ps.executeQuery();
				
				if(rs.first()) {
					user.setUser_id(rs.getInt("user_id"));
					user.setFirst_name(rs.getString("first_name"));
					user.setLast_name(rs.getString("last_name"));
					user.setDepartment(rs.getString("department"));
					user.setDesignation(rs.getString("designation"));
					user.setEmail(rs.getString("email"));
					user.setAddress(rs.getString("address"));
					user.setContact_number(rs.getString("contact_number"));
					user.setUser_picture(rs.getString("user_picture"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		
		//return statement
		return user;
	}
	
	//get employee
	public User getUser(String uname, String pass){
		//create new arraylist
		User user = new User();
		int user_id = 0;
		
		if(login(uname, pass)) {
			//preparedStatement
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				try {
					ps = (PreparedStatement) connection.prepareStatement(QueryUtil.Q(CommonConstants.LOGIN));
					ps.setString(1, uname);
					ps.setString(2, pass);
					
					rs = (ResultSet) ps.executeQuery();
					
					if(rs.next()) {
						user_id = rs.getInt("user_id");
						user.setPrivilage(rs.getInt("privilage_id"));
						user.setPassword(rs.getString("password"));
						user.setUser_name(rs.getString("user_name"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.log(Level.SEVERE, e.getMessage());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
			
			try {
				try {
					ps = (PreparedStatement) connection.prepareStatement(QueryUtil.Q(CommonConstants.SELECT_USER_BY_ID));
					ps.setInt(1, user_id);
					
					rs = (ResultSet) ps.executeQuery();
					
					if(rs.next()) {
						user.setUser_id(rs.getInt("user_id"));
						user.setFirst_name(rs.getString("first_name"));
						user.setLast_name(rs.getString("last_name"));
						user.setDepartment(rs.getString("department"));
						user.setDesignation(rs.getString("designation"));
						user.setEmail(rs.getString("email"));
						user.setAddress(rs.getString("address"));
						user.setContact_number(rs.getString("contact_number"));
						user.setUser_picture(rs.getString("user_picture"));
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.log(Level.SEVERE, e.getMessage());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
		}
		
		return user;
	}
	
	//getAllEmployees
	public ArrayList<User> getAllUsers(){
		
		//create new arraylist
		ArrayList<User> users = new ArrayList<User>();
		
		//fetch the arraylist
		try {
			Statement st = (Statement) connection.createStatement();
			ResultSet rs;
			try {
				rs = (ResultSet) st.executeQuery(QueryUtil.Q(CommonConstants.SELECT_ALL_USERS));
				while (rs.next()) {
					User user = new User(rs.getInt("user_id"), rs.getString("first_name"), rs.getString("last_name"), rs.getString("department"),
							rs.getString("designation"), rs.getString("email"), rs.getString("address"), rs.getString("contact_number"), rs.getString("user_picture"));
					
					users.add(user);
				}
					
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		//return
		
		return users;
	}
	
}
