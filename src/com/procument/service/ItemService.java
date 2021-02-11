package com.procument.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.procument.common.DBConnectionUtil;
import com.procument.model.Item;
import com.procument.model.PurchaseOrder;
import com.procument.model.Supplier;

public class ItemService {
	
	private static ItemService instance;
	private Connection connection;
	
	//singleton usage
	public static ItemService getInstance() {
		
		if(instance == null) {
			instance = new ItemService();	
		}
		
		return instance;
		
	}
	
	private ItemService() {
		super();
		this.connection = (Connection)DBConnectionUtil.getConnection();
	}
	
	//get item
	public Item getItem(int item_id) {
		Item item = new Item();
		
		//instance of supplier service class to retrieve supplier accordingly
		SupplierService supplierService = SupplierService.getInstance();
		
		//preparedStatemen
		PreparedStatement ps = null;
		//result set
		ResultSet rs = null;
		
		//PreparedStatement to get supplier
		PreparedStatement psSupplier = null;
		//Result of suppliers belongs to a 
		ResultSet rsSuppliers = null;
		
		//sql
		String sql = "select * from items where item_id = ?";
		
		//sql to retrieve all suppliers bolongs
		String sqlSuppliers = "select * from supplier_item where item_id=?";
		
		try {
			ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setInt(1, item_id);
			psSupplier = (PreparedStatement)connection.prepareStatement(sqlSuppliers);
			
			rs = (ResultSet) ps.executeQuery();
			
			if(rs.first()) {
				//list of suppliers
				ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
				
				item.setItem_id(rs.getInt("item_id"));
				item.setItem_code(rs.getString("item_code"));
				item.setItem_name(rs.getString("item_name"));
				item.setUnit_price(rs.getDouble("unit_price"));
				item.setUnit(rs.getString("unit"));
				item.setSpecialApproval(rs.getBoolean("special_approval"));
				//query assign to the retrieved item_id
				psSupplier.setInt(1, item.getItem_id());
				rsSuppliers = (ResultSet)psSupplier.executeQuery();
				
				while(rsSuppliers.next()) {
					Supplier supplier = new Supplier();
					supplier = supplierService.getSupplierDetails(rsSuppliers.getString("supplier_id"));
					//adding related suppliers to the list
					suppliers.add(supplier);
				}
				
				//supplier list adding to the item
				item.setSuppliers(suppliers);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return item;
	}
	
	//get all Suppliers
	public ArrayList<Item> getAllItems(){
		//initiate item list
		ArrayList<Item> items = new ArrayList<Item>();
		
		//instance of supplier service class to retrieve supplier accordingly
		SupplierService supplierService = SupplierService.getInstance();
		
		//sql to retrieve items
		String sql = "select * from items";
		//sql to retrieve all suppliers bolongs
		String sqlSuppliers = "select * from supplier_item where item_id=?";
		
		//Statement
		Statement st = null;
		//Resultset
		ResultSet rs = null;
		
		//PreparedStatement to get supplier
		PreparedStatement psSupplier = null;
		//Result of suppliers belongs to a 
		ResultSet rsSuppliers = null;
		
		//get all items and add to the list
		try {
			st = (Statement) connection.createStatement();
			rs = (ResultSet) st.executeQuery(sql);
			psSupplier = (PreparedStatement)connection.prepareStatement(sqlSuppliers);
			
			while(rs.next()) {
				
				Item item = new Item();
				
				//list of suppliers
				ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
				
				item.setItem_id(rs.getInt("item_id"));
				item.setItem_code(rs.getString("item_code"));
				item.setItem_name(rs.getString("item_name"));
				item.setUnit_price(rs.getDouble("unit_price"));
				item.setUnit(rs.getString("unit"));
				item.setSpecialApproval(rs.getBoolean("special_approval"));
				
				//query assign to the retrieved item_id
				psSupplier.setInt(1, item.getItem_id());
				rsSuppliers = (ResultSet)psSupplier.executeQuery();
				
				while(rsSuppliers.next()) {
					Supplier supplier = new Supplier();
					supplier = supplierService.getSupplierDetails(rsSuppliers.getString("supplier_id"));
					//adding related suppliers to the list
					suppliers.add(supplier);
				}
				
				//supplier list adding to the item
				item.setSuppliers(suppliers);				
				//add item to item list
				items.add(item);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//return list
		return items;
	}
	
	public Item deleteSupplier(String supplier_id, int item_id){
		Item item = new Item();
		
		//sql
		String sql = "delete from supplier_item where supplier_id=? and item_id=?";
		
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setString(1, supplier_id);
			ps.setInt(2, item_id);
			
			ps.executeUpdate();
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		item = getItem(item_id);
		
		return item;
	}
	
	//Add items to the db
	public boolean addItems(String item_name, double unit_price, String unit, boolean specialApproval, ArrayList<Supplier> suppliers, String url) {
		
		//genarate random number
		Random random = new Random();
		int rNumber = random.nextInt(1234567890);
		
		//creating a unique item_code
		String item_code = item_name + rNumber;
		
		//query to add items
		String query = "insert into items values(?,?,?,?,?,?,?)";
		
		//PreparedStatement
		PreparedStatement ps = null;
		
		try {
			ps = (PreparedStatement) connection.prepareStatement(query);
			ps.setString(1, null);
			ps.setString(2, item_code);
			ps.setString(3, item_name);
			ps.setDouble(4, unit_price);
			ps.setString(5, unit);
			ps.setBoolean(6, specialApproval);
			ps.setString(7, url);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//statement
		Statement maxIdSt = null;
		//Resultset
		ResultSet maxIdRs = null;
		
		//query to get Max id
		String queryMaxId = "select max(item_id) from items";
		int item_id = 0;
		
		//get all supliers and add to the list
		try {
			maxIdSt = (Statement) connection.createStatement();
			maxIdRs = (ResultSet) maxIdSt.executeQuery(queryMaxId);
			
			if(maxIdRs.first()) {
				item_id = maxIdRs.getInt("max(item_id)");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		PreparedStatement psSupplierItem = null;
		
		//query to add items
		String querySupplierItem = "insert into supplier_item values(?,?)";
		
		try {
			psSupplierItem = (PreparedStatement) connection.prepareStatement(querySupplierItem);
			//for each supplier creating new row
			for(Supplier supplier : suppliers) {
				psSupplierItem.setInt(1, item_id);
				psSupplierItem.setString(2, supplier.getSupplier_id());
				
				psSupplierItem.executeUpdate();
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	public ArrayList<Item> getSupplierRelatedItems(Supplier supplier){
		//initiate item list
		ArrayList<Item> items = new ArrayList<Item>();
		
		//ps to get related items
		PreparedStatement ps = null;
		
		//result set variable
		ResultSet rs = null;
		
		//Sql statement to get related item ids
		String sql = "select * from supplier_item s, items i where i.item_id = s.item_id and s.supplier_id=?";
		
		//list of suppliers
		//set selected supplier to the list 
		//to get pagination this is required
		//pagination ask supplier list
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		Supplier sup = new Supplier();
		sup = SupplierService.getInstance().getSupplierDetails(supplier.getSupplier_id());
		suppliers.add(sup);
		
		//try catch with ps
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setString(1, supplier.getSupplier_id());
			
			rs = (ResultSet)ps.executeQuery();
			
			while(rs.next()) {
				Item item = new Item();
				
				
				item.setItem_id(rs.getInt("item_id"));
				item.setItem_code(rs.getString("item_code"));
				item.setItem_name(rs.getString("item_name"));
				item.setUnit_price(rs.getDouble("unit_price"));
				item.setSpecialApproval(rs.getBoolean("special_approval"));
				item.setSuppliers(suppliers);
				item.setUnit(rs.getString("unit"));
				
				items.add(item);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return items;
	}
	
	public ArrayList<Item> searchItemWithSupplier(String searchKey, String searchValue, Supplier supplier){
		//initiate item list
		ArrayList<Item> items = new ArrayList<Item>();
		
		//ps to get related items
		PreparedStatement ps = null;
		
		//result set variable
		ResultSet rs = null;
		
		//Sql statement to get related item ids
		String sql = "select * from supplier_item s, items i where i.item_id = s.item_id and s.supplier_id=? and " + searchKey + " LIKE '%" + searchValue + "%'";
		
		
		//list of suppliers
		//set selected supplier to the list 
		//to get pagination this is required
		//pagination ask supplier list
		ArrayList<Supplier> suppliers = new ArrayList<Supplier>();
		Supplier sup = new Supplier();
		sup = SupplierService.getInstance().getSupplierDetails(supplier.getSupplier_id());
		suppliers.add(sup);
				
		try {
			ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setString(1, supplier.getSupplier_id());
			
			rs = (ResultSet)ps.executeQuery();
			
			while(rs.next()) {
				Item item = new Item();
				
		
				item.setItem_id(rs.getInt("item_id"));
				item.setItem_code(rs.getString("item_code"));
				item.setItem_name(rs.getString("item_name"));
				item.setUnit_price(rs.getDouble("unit_price"));
				item.setSuppliers(suppliers);
				item.setUnit(rs.getString("unit"));
				item.setSpecialApproval(rs.getBoolean("special_approval"));
				
				items.add(item);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return items;
	}
	
	public boolean updateItem(Item itemDb, ArrayList<Supplier> suppliers, String item_name, double unit_price, String unit, boolean specialApproval) {
		boolean succes = true;
		//to check new suppliers
		ArrayList<Supplier> newSuppliers = new ArrayList<Supplier>();
		
		//add supplier to newSupplier if any
		for(Supplier supplier : suppliers) {
			boolean found = false;
			for(Supplier supplierDb : itemDb.getSuppliers()) {
				if(supplierDb.getSupplier_id().equals(supplier.getSupplier_id())) {
					found = true;
				}
			}
			
			if(!found) {
				newSuppliers.add(supplier);
			}
		}
		
		//if new supplier
		if(!newSuppliers.isEmpty()) {
			//sql for add new Supplier if any
			String sql = "insert into supplier_item values(?,?)";
			PreparedStatement ps;
			try {
				ps = (PreparedStatement)connection.prepareStatement(sql);
				ps.setInt(1, itemDb.getItem_id());
				
				for(Supplier supplier : newSuppliers) {
					ps.setString(2, supplier.getSupplier_id());
					ps.executeUpdate();
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//update item
		//sql
		String sql = "update items set item_name=?, unit_price=?, unit=?, special_approval=? where item_id=?";
		
		//prepare Statement
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setString(1, item_name);
			ps.setDouble(2, unit_price);
			ps.setString(3, unit);
			ps.setBoolean(4, specialApproval);
			ps.setInt(5, itemDb.getItem_id());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//delete Notifications if exits and update po table accordingly
		
		return true;
	}

	public boolean deleteItem(Item item) {
		boolean success = false;
		//get affected POs
		ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		
		String sqlAffectedPos = "select * from po_item where item_id=?";
		
		try {
			PreparedStatement psGetAffectedPos = (PreparedStatement)connection.prepareStatement(sqlAffectedPos);
			psGetAffectedPos.setInt(1, item.getItem_id());
			
			ResultSet rsAffectedPos = (ResultSet)psGetAffectedPos.executeQuery();
			
			while(rsAffectedPos.next()) {
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				purchaseOrder = PurchaseOrderService.getInstance().getPoById(rsAffectedPos.getInt("po_id"));
				
				purchaseOrders.add(purchaseOrder);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//delete from po
		String sqlDeleteFromPo = "delete from po_item where item_id = ?";
		try {
			PreparedStatement psDeleteFromPo = (PreparedStatement)connection.prepareStatement(sqlDeleteFromPo);
			psDeleteFromPo.setInt(1, item.getItem_id());
			psDeleteFromPo.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//add notification
		//and update po
		for(PurchaseOrder purchaseOrder : purchaseOrders) {
			NotificationService.getInstance().addNotification("ITEM DELETION", "procument_staff", purchaseOrder.getPo_id());
			NotificationService.getInstance().addNotification("ITEM DELETION", "site_manager", purchaseOrder.getPo_id());
			PurchaseOrderService.getInstance().updatePo(new ArrayList<Item>(), PurchaseOrderService.getInstance().getPoById(purchaseOrder.getPo_id()));
		}
		
		
		//sql for delete item from supplier_item
		String sql = "delete from supplier_item where item_id = ?";
		//ps for delete item from supplier_item
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
			ps.setInt(1, item.getItem_id());
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//sql for delete from items
		String delFromItems= "delete from items where item_id = ?";
		
		//ps for delete from items
		try {
			PreparedStatement psItemDel = (PreparedStatement)connection.prepareStatement(delFromItems);
			psItemDel.setInt(1, item.getItem_id());
			psItemDel.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		return success;
		
	}
}
