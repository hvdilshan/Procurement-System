package com.procument.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.procument.common.DBConnectionUtil;
import com.procument.model.Site;
import com.procument.model.User;

public class SiteService {
	private Connection connection;
	private static SiteService siteService;
	//Customer Service instance
	private CustomerService customerService;
	//User service instance
	private UserService userService;
	
	private SiteService() {
		super();
		connection = (Connection) DBConnectionUtil.getConnection();
		//initiate customerService and userService
		customerService = CustomerService.getInstance();
		userService  = new UserService();
	}
	
	public static SiteService getInstance() {
		if(siteService == null) {
			siteService =  new SiteService();
		}
		
		return siteService;
	}
	
	public ArrayList<Site> getRelatedSites(User user){
		
		//arraylist for related sites
		ArrayList<Site> sites = new ArrayList<Site>();
		
		//sql statement
		String sql = "select * from site_details s where s.manager_id = ?";
		
		//ps statement
		PreparedStatement  ps = null;
		//rs statement
		ResultSet rs = null;
		
		//get and assign site details
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setInt(1, user.getUser_id());
			
			rs = (ResultSet)ps.executeQuery();
			
			while(rs.next()) {
				Site site = new Site();
				
				site.setSite_id(rs.getInt("site_id"));
				site.setSite_name(rs.getString("site_name"));
				site.setSite_address(rs.getString("site_address"));
				site.setProject_start_date(rs.getDate("project_start_date"));
				site.setProject_duration(rs.getInt("project_duration"));
				site.setUser(userService.getUserById(rs.getInt("manager_id")));
				site.setCustomer(customerService.getCustomerById(rs.getInt("customer_id")));
				site.setStatus(rs.getString("status"));

				sites.add(site);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return sites;
		
	}
	
	public Site getSiteById(int site_id) {
		
		//Site instance
		Site site = new Site();
		site.setSite_id(site_id);
		//sql
		String sql = "select * from site_details where site_id = ?";
		
		//ps
		PreparedStatement ps = null;
		//rs
		ResultSet rs = null;
		
		//execute
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setInt(1, site_id);
			
			rs = (ResultSet)ps.executeQuery();
			
			if(rs.first()) {
				site.setSite_id(rs.getInt("site_id"));
				site.setSite_name(rs.getString("site_name"));
				site.setSite_address(rs.getString("site_address"));
				site.setProject_start_date(rs.getDate("project_start_date"));
				site.setProject_duration(rs.getInt("project_duration"));
				site.setUser(userService.getUserById(rs.getInt("manager_id")));
				site.setCustomer(customerService.getCustomerById(rs.getInt("customer_id")));
				site.setStatus(rs.getString("status"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//return
		return site;
	}
}
