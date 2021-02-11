package com.procument.service;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.procument.common.CommonConstants;
import com.procument.common.QueryUtil;
import com.procument.model.Item;
import com.procument.model.ProcumentStaff;
import com.procument.model.PurchaseOrder;
import com.procument.model.Site;
import com.procument.model.Supplier;
import com.procument.model.User;
import com.sun.jmx.snmp.Timestamp;

public class SiteManagerService extends UserService {
	
	public SiteManagerService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean makeNewPurchaseOrder(PurchaseOrder purchaseOrder) {
		boolean success = false;
		//price variable
		double price = 0.0;
		
		//notification will send if this varible become true
		boolean notificationSend = false;
		
		//prepared Statement variable
		PreparedStatement ps = null;
		
		//calculate price
		for(Item item : purchaseOrder.getItems()) {
			price += (item.getUnit_price() * item.getQuantity());
		}
		
		//converting java util date to sql date
		java.util.Date date = purchaseOrder.getDilivery_date();
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		
		try {
			try {
				ps = (PreparedStatement) connection.prepareStatement(QueryUtil.Q(CommonConstants.MAKE_NEW_PO));
				ps.setString(1, null);
				ps.setString(2, null);
				ps.setInt(3, purchaseOrder.getUser().getUser_id());
				ps.setString(4, "0");
				ps.setString(5, purchaseOrder.getSupplier().getSupplier_id());
				ps.setInt(6, purchaseOrder.getSite().getSite_id());
				ps.setDate(7, sqlDate);
				ps.setDouble(8, price);
				
				boolean itemRequiredSpecialApprovel = true;
				
				for(Item item : purchaseOrder.getItems()) {
					if(item.isSpecialApproval()) {
						itemRequiredSpecialApprovel = false;
					}
				}
				
				if(purchaseOrder.getDescription().equals("draft_po")) {
					ps.setString(9, "-");
					ps.setString(10, "draft_po");
				}else {
					if(itemRequiredSpecialApprovel) {
						//check price
						if(price<100000.00) {
							ps.setString(9, "Approved");
							ps.setString(10, "PO is under budget");
						}else {
							ps.setString(9, "Pending");
							ps.setString(10, "PO is over the budget");
							notificationSend = true;
						}				
					}else {
						ps.setString(9, "Pending");
						ps.setString(10, "Some items of the PO need some special Approval!");
						notificationSend = true;
					}				
				}
				
				
				int i = ps.executeUpdate();
				
				if(i != 0) {
					success = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//get max po id
		int po_id = getMaxPoId();
		
		if(success) {
			//items inserting into po_items
			//ps
			PreparedStatement psInsertPoItem = null;
			
			int i = 0;
			//execute
			try {
				try {
					psInsertPoItem = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.INSERT_PO_ITEM));
					psInsertPoItem.setInt(1, po_id);
					
					for(Item item : purchaseOrder.getItems()) {
						psInsertPoItem.setInt(2, item.getItem_id());
						psInsertPoItem.setInt(3, item.getQuantity());
						psInsertPoItem.setInt(4, po_id);
						psInsertPoItem.setString(5, item.getItem_name());
						psInsertPoItem.setInt(6, item.getQuantity());
						i = psInsertPoItem.executeUpdate();
						
					}
					
					if(i != 0) {
						success = true;
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(notificationSend) {
				
				//set po id to purchase order
				purchaseOrder.setPo_id(po_id);
				
				//Observer design pattern approach
				//attaching the procument Staff
				for(ProcumentStaff procumentStaff : getAllProcumentStaff()) {
					//assign the id of the purchase order
					procumentStaff.assignPo(purchaseOrder);
					purchaseOrder.attach(procumentStaff);
				}
				
				//add notification
				purchaseOrder.notifyProcumentStaff();
				addNotification(po_id);
			}
			
		}
		
		
		//return statement
		return success;
	}
	
	//get all procument staff people
	public ArrayList<ProcumentStaff> getAllProcumentStaff() {
		ArrayList<ProcumentStaff> procumentStaffs = new ArrayList<ProcumentStaff>();
		ProcumentStaffService procumentStaffService = ProcumentStaffService.getInstance();
		
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.GET_ALL_PROCUMENT_STAFF));
			ResultSet rs = (ResultSet)ps.executeQuery();
			
			while(rs.next()) {
				procumentStaffs.add(procumentStaffService.getUserById(rs.getInt("user_id")));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return procumentStaffs;
	}
	
	public boolean addNotification(int po_id) {
		boolean success = false;
		//ps
		PreparedStatement ps = null;
		
		//execute
		try {
			try {
				ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.ADD_NOTIFICATION));
				ps.setString(1, null);
				ps.setString(2, "PO");
				ps.setInt(4, po_id);
				ps.setInt(5, 0);
				
				//for procument staff
				ps.setString(3, "procument_staff");
				ps.executeUpdate();
				//for site manager
				ps.setString(3, "site_manager");			
				int i = ps.executeUpdate();
				
				if(i != 0) {
					success = true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
	
	//get max po id
	public int getMaxPoId() {
		int max_po_id = 0;
		
		//Statement
		Statement st = null;
		//rs
		ResultSet rs = null;
		
		//execute
		try {
			st = (Statement)connection.createStatement();
			try {
				
				rs = (ResultSet)st.executeQuery(QueryUtil.Q(CommonConstants.GET_MAX_PO_ID));
				
				if(rs.first()) {
					max_po_id = rs.getInt("max(po_id)");
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
		return max_po_id;
	}
	
}
