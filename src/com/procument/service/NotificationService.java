package com.procument.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.procument.common.DBConnectionUtil;
import com.procument.model.Notification;

public class NotificationService {
	private Connection connection;
	private static NotificationService notificationService;
	
	private NotificationService() {
		super();
		// TODO Auto-generated constructor stub
		connection = (Connection) DBConnectionUtil.getConnection();
	}
	
	public static NotificationService getInstance() {
		
		if(notificationService == null) {
			notificationService = new NotificationService();
		}
		
		return notificationService;
	}
	
	public ArrayList<Notification> getPoNotification(String user_type){
		//arralist creation
		ArrayList<Notification> notifications = new ArrayList<Notification>();
		
		if(user_type.equals("site_manager")) {
			//sql for pending pos
			String sql = "select * from notification where notifi_type=? and notifi_for=? and status = ?";
			
			//ps
			PreparedStatement ps = null;
			//rs
			ResultSet rs = null;
			//execute
			try {
				ps = (PreparedStatement)connection.prepareStatement(sql);
				ps.setString(1, "PO");
				ps.setString(2, user_type);
				ps.setInt(3, 0);
				
				rs = (ResultSet)ps.executeQuery();
				
				while(rs.next()) {
					Notification notification = new Notification();
					
					notification.setId_notif(rs.getInt("id_notif"));
					notification.setNotifi_type(rs.getString("notifi_type"));
					notification.setNotifi_for(rs.getString("notifi_for"));
					notification.setNotif_rel_id(rs.getInt("notif_rel_id"));
					notification.setStatus(rs.getInt("status"));
					
					notifications.add(notification);
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return notifications;
	}
	
	public boolean addNotification(int po_id) {
		boolean succes = true;
		
		//sql
		String sql = "insert into notification values(?,?,?,?,?)";
		
		//ps
		PreparedStatement ps = null;
		
		//execute
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setString(1, null);
			ps.setString(2, "PO");
			ps.setInt(4, po_id);
			ps.setInt(5, 0);
			
			//for procument staff
			ps.setString(3, "procument_staff");
			ps.executeUpdate();
			//for site manager
			ps.setString(3, "site_manager");			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return succes;
	}
	
	public void addNotification(String notifi_type, String notifi_for, int notif_rel_id) {
		//sql
		String sql = "insert into notification values(?,?,?,?,?)";
		
		//ps
		PreparedStatement ps = null;
		
		//execute
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setString(1, null);
			ps.setString(2, notifi_type);
			ps.setString(3, notifi_for);
			ps.setInt(4, notif_rel_id);
			ps.setInt(5, 0);
			
			ps.executeUpdate();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
