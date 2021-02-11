package com.procument.service;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.procument.common.CommonConstants;
import com.procument.common.DBConnectionUtil;
import com.procument.common.QueryUtil;
import com.procument.model.ProcumentStaff;
import com.procument.model.User;

public class ProcumentStaffService {

	private Connection connection;
	private static ProcumentStaffService procumentStaffService;
	
	public static ProcumentStaffService getInstance() {
		if(procumentStaffService == null)
			return new ProcumentStaffService();
		
		return procumentStaffService;
	}
	
	private ProcumentStaffService() {
		connection = (Connection) DBConnectionUtil.getConnection();
	}
	
	public  ProcumentStaff getUserById(int user_id) {
		//create user instance
		ProcumentStaff procumentStaff= new ProcumentStaff();
		
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
					procumentStaff.setUser_id(rs.getInt("user_id"));
					procumentStaff.setFirst_name(rs.getString("first_name"));
					procumentStaff.setLast_name(rs.getString("last_name"));
					procumentStaff.setDepartment(rs.getString("department"));
					procumentStaff.setDesignation(rs.getString("designation"));
					procumentStaff.setEmail(rs.getString("email"));
					procumentStaff.setAddress(rs.getString("address"));
					procumentStaff.setContact_number(rs.getString("contact_number"));
					procumentStaff.setUser_picture(rs.getString("user_picture"));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return statement
		return procumentStaff;
	}
	
	public boolean addNotification(int po_id, int procument_staff_id) {
				
		//ps
		PreparedStatement ps = null;

		//execute
		try {
			try {
				ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.ADD_PO_NOTIFICATION));
				ps.setInt(1, po_id);
				ps.setInt(2, procument_staff_id);
				ps.setInt(3, 0);
				
				int i = ps.executeUpdate();
				
				if(i != 0) {
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
