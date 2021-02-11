package com.procument.service;

import java.sql.SQLException;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.procument.common.DBConnectionUtil;
import com.procument.model.Customer;

public class CustomerService {
	private Connection connection;
	private static CustomerService customerService;
	
	private CustomerService() {
		super();
		connection = (Connection)DBConnectionUtil.getConnection();
	}
	
	public static CustomerService getInstance() {
		if(customerService == null) {
			customerService = new CustomerService();
		}
		
		return customerService;
	}
	
	public Customer getCustomerById(int customer_id) {
		
		//Customer object
		Customer customer = new Customer(); 
		
		//Sql
		String sql = "select * from customer_details where customer_id = ?";
		
		//Prepare statement
		PreparedStatement ps = null;
		//result set
		ResultSet rs = null;
		
		//execute query
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setInt(1, customer_id);
			
			rs = (ResultSet)ps.executeQuery();
			
			if(rs.first()) {
				customer.setCustomer_id(rs.getInt("customer_id"));
				customer.setCustomer_name(rs.getString("customer_name"));
				customer.setRegistered_date(rs.getDate("registered_date"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return statement
		return customer;
	}
}
