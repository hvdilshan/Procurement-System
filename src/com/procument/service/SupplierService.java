package com.procument.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.procument.common.DBConnectionUtil;
import com.procument.model.Supplier;
import com.procument.model.User;

public class SupplierService {
    
	private static SupplierService instance;
	private Connection connection;
	
	//singleton usage
	public static SupplierService getInstance() {
		
		if(instance == null) {
			instance = new SupplierService();	
		}
		
		return instance;
		
	}
	
	private SupplierService() {
		super();
		this.connection = (Connection)DBConnectionUtil.getConnection();
	}
	
	public int createUniqueId() {
		//genarate random number
		Random random = new Random();
		return random.nextInt(1234567890);		
	}

	//add supplier
	public boolean addSupplier(String cName, String sName, String sAddress, String sEmail, String sContact, int grade, User user) {
		
		
		int rNumber = createUniqueId();
		
		//creating supplier id
		String sup_id = cName + rNumber;
		
		//query to add supplier
		String query = "insert into suppliers values(?,?,?,?,?,?,?,?)";
		
		//PreparedStatement
		PreparedStatement ps = null;
		
		try {
			ps = (PreparedStatement) connection.prepareStatement(query);
			ps.setString(1, sup_id);
			ps.setString(2, cName);
			ps.setString(3, sName);
			ps.setString(4, sAddress);
			ps.setString(5, sEmail);
			ps.setString(6, sContact);
			ps.setInt(7, grade);
			ps.setInt(8, user.getUser_id());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
	}
	
	public Supplier getSupplierDetails(String supplier_id) {
		Supplier supplier = new Supplier();
		
		//Prepared Statement
		PreparedStatement ps= null;
		//Result set
		ResultSet rs= null;
		
		//sql
		String sql = "select * from suppliers where supplier_id=?";
		//get Supplier
		try {
			ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setString(1, supplier_id);
			
			rs = (ResultSet) ps.executeQuery();
			
			if(rs.first()) {
				supplier.setSupplier_id(rs.getString("supplier_id"));
				supplier.setCompany_name(rs.getString("company_name"));
				supplier.setSupplier_name(rs.getString("supplier_name"));
				supplier.setSupplier_address(rs.getString("supplier_address"));
				supplier.setSupplier_contact(rs.getString("supplier_contact"));
				supplier.setSupplier_email(rs.getString("email"));
				supplier.setGrade(rs.getInt("grade"));
				supplier.setAdded_by(rs.getInt("added_by"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return supplier
		return supplier;
	}
	
	//get all Suppliers
	public ArrayList<Supplier> getAllSuppliers(){
		//initiate supplier list
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		//sql
		String sql = "select * from suppliers";
		//Statement
		Statement st = null;
		//Resultset
		ResultSet rs = null;
		//get all supliers and add to the list
		try {
			st = (Statement) connection.createStatement();
			rs = (ResultSet) st.executeQuery(sql);
			
			while(rs.next()) {
				Supplier supplier = new Supplier();
				
				supplier.setSupplier_id(rs.getString("supplier_id"));
				supplier.setCompany_name(rs.getString("company_name"));
				supplier.setSupplier_name(rs.getString("supplier_name"));
				supplier.setSupplier_address(rs.getString("supplier_address"));
				supplier.setSupplier_contact(rs.getString("supplier_contact"));
				supplier.setSupplier_email(rs.getString("email"));
				supplier.setGrade(rs.getInt("grade"));
				supplier.setAdded_by(rs.getInt("added_by"));
			
				suppliers.add(supplier);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return list
		return suppliers;
	}
}
