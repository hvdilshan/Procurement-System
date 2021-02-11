package com.procument.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.procument.common.QueryUtil;
import com.procument.common.CommonConstants;
import com.procument.common.CommonUtil;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;
import com.procument.common.DBConnectionUtil;
import com.procument.model.Item;
import com.procument.model.ProcumentStaff;
import com.procument.model.PurchaseOrder;
import com.procument.model.Site;
import com.procument.model.Supplier;
import com.procument.model.User;

public class PurchaseOrderService {
	private Connection connection;
	private static PurchaseOrderService purchaseOrderService;
	private static final Logger Log = Logger.getLogger(CommonUtil.class.getName());
	
	private PurchaseOrderService() {
		super();
		connection = (Connection)DBConnectionUtil.getConnection();
	}
	
	public static PurchaseOrderService getInstance() {
		
		if(purchaseOrderService == null) {
			purchaseOrderService = new PurchaseOrderService();
		}
		
		return purchaseOrderService;
	}
	
	public long getTotalPoCost() {
		long cost = 0;
		
		//statement
		Statement st = null;
		//result set
		ResultSet rs = null;
		
		//execute
		try {
			st = (Statement)connection.createStatement();
			try {
				rs = (ResultSet)st.executeQuery(QueryUtil.Q(CommonConstants.GET_TOTAL_PO_COST));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
			
			if(rs.first()) {
				cost = rs.getLong("sum(po_price)");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		
		//return
		return cost;
	}

	public int getTotalPoCount() {
		int count = 0;
		
		//statement
		Statement st = null;
		//result set
		ResultSet rs = null;
		
		//execute
		try {
			st = (Statement)connection.createStatement();
			try {
				rs = (ResultSet)st.executeQuery(QueryUtil.Q(CommonConstants.GET_TOTAL_PO_COUNT));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
			
			if(rs.first()) {
				count = rs.getInt("count(po_id)");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		
		//return
		return count;
	}
	
	public long calculateAveragePoCost(long totalPoCost, int totalPoCount) {
		long avgCost = 0;
		try {
			avgCost = totalPoCost / totalPoCount;
		}catch(ArithmeticException exc) {
			
		}
		return avgCost;
	}
	
	public ArrayList<Item> getPoItems(int po_id){
		//array list initiate
		ArrayList<Item> items = new ArrayList<Item>();
		//ps
		PreparedStatement ps = null;
		//rs
		ResultSet rs = null;
		
		//Item service class instance
		ItemService itemService = ItemService.getInstance();
		
		//execution
		try {
			try {
				ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.GET_PO_ITEMS));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
			ps.setInt(1, po_id);
			
			rs = (ResultSet)ps.executeQuery();
			
			while(rs.next()) {
				Item item = new Item();
				
				item = itemService.getItem(rs.getInt("item_id"));
				item.setQuantity(rs.getInt("quantity"));
				items.add(item);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		
		//return
		return items;
	}
	
	//get all Pos
	public ArrayList<PurchaseOrder> getAllPurchaseOrders() throws Exception{
		//list initialization
		ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		
		//User Service class instance
		UserService userService = new UserService();
		//supplier service class instance
		SupplierService supplierService = SupplierService.getInstance();
		
		//Site Service Instance
		SiteService siteService = SiteService.getInstance();
		
		//statement
		Statement st = null;
		
		//rs
		ResultSet rs = null;
		
		//execute
		try {
			st = (Statement)connection.createStatement();
			rs = (ResultSet)st.executeQuery(QueryUtil.Q(CommonConstants.SELECT_ALL_PURCHASE_ORDERS));
			
			while(rs.next()) {
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				
				purchaseOrder.setPo_id(rs.getInt("po_id"));
				purchaseOrder.setCreated_date(rs.getDate("created_date"));
				purchaseOrder.setCreated_by(rs.getInt("created_by"));
				
				//get user
				User user = userService.getUserById(purchaseOrder.getCreated_by());
				//set created user
				purchaseOrder.setUser(user);
				
				purchaseOrder.setApproved_by(rs.getInt("approved_by"));
				
				//approved by user 
				User approved_by_user = userService.getUserById(purchaseOrder.getApproved_by());
				//set approved by user
				purchaseOrder.setApproved_by_user(approved_by_user);
				
				purchaseOrder.setSupplier_id(rs.getString("supplier_id"));
				
				//Supplier
				Supplier supplier = supplierService.getSupplierDetails(purchaseOrder.getSupplier_id());
				//set supplier
				purchaseOrder.setSupplier(supplier);
				
				purchaseOrder.setSite_id(rs.getInt("site_id"));
				
				//get Site
				Site site = siteService.getSiteById(purchaseOrder.getSite_id());
				purchaseOrder.setSite(site);
				
				purchaseOrder.setDilivery_date(rs.getDate("dilivery_date"));
				purchaseOrder.setPo_price(rs.getDouble("po_price"));
				purchaseOrder.setStatus(rs.getString("status"));
				purchaseOrder.setDescription(rs.getString("description"));
				
				purchaseOrder.setItems(getPoItems(purchaseOrder.getPo_id()));
				
				purchaseOrders.add(purchaseOrder);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		
		//return statement
		return purchaseOrders;
	}
	//get all Pos
	public ArrayList<PurchaseOrder> getAllDraftPurchaseOrders(){
		//list initialization
		ArrayList<PurchaseOrder> purchaseOrders = new ArrayList<PurchaseOrder>();
		
		//User Service class instance
		UserService userService = new UserService();
		//supplier service class instance
		SupplierService supplierService = SupplierService.getInstance();
		
		//Site Service Instance
		SiteService siteService = SiteService.getInstance();
		
		//statement
		Statement st = null;
		
		//rs
		ResultSet rs = null;
		
		//execute
		try {
			st = (Statement)connection.createStatement();
			try {
				rs = (ResultSet)st.executeQuery(QueryUtil.Q(CommonConstants.GET_DRAFT_POS));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
			
			while(rs.next()) {
				PurchaseOrder purchaseOrder = new PurchaseOrder();
				
				purchaseOrder.setPo_id(rs.getInt("po_id"));
				purchaseOrder.setCreated_date(rs.getDate("created_date"));
				purchaseOrder.setCreated_by(rs.getInt("created_by"));
				
				//get user
				User user = userService.getUserById(purchaseOrder.getCreated_by());
				//set created user
				purchaseOrder.setUser(user);
				
				purchaseOrder.setApproved_by(rs.getInt("approved_by"));
				
				//approved by user 
				User approved_by_user = userService.getUserById(purchaseOrder.getApproved_by());
				//set approved by user
				purchaseOrder.setApproved_by_user(approved_by_user);
				
				purchaseOrder.setSupplier_id(rs.getString("supplier_id"));
				
				//Supplier
				Supplier supplier = supplierService.getSupplierDetails(purchaseOrder.getSupplier_id());
				//set supplier
				purchaseOrder.setSupplier(supplier);
				
				purchaseOrder.setSite_id(rs.getInt("site_id"));
				
				//get Site
				Site site = siteService.getSiteById(purchaseOrder.getSite_id());
				purchaseOrder.setSite(site);
				
				purchaseOrder.setDilivery_date(rs.getDate("dilivery_date"));
				purchaseOrder.setPo_price(rs.getDouble("po_price"));
				purchaseOrder.setStatus(rs.getString("status"));
				purchaseOrder.setDescription(rs.getString("description"));
				
				purchaseOrder.setItems(getPoItems(purchaseOrder.getPo_id()));
				
				purchaseOrders.add(purchaseOrder);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		
		//return statement
		return purchaseOrders;
	}

	public PurchaseOrder getPoById(int po_id) {
		PurchaseOrder purchaseOrder = new PurchaseOrder();
		
		//User Service class instance
		UserService userService = new UserService();
		//supplier service class instance
		SupplierService supplierService = SupplierService.getInstance();
		
		//Site Service Instance
		SiteService siteService = SiteService.getInstance();
		
		try {
			//ps
			try {
				PreparedStatement ps =(PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.GET_PO_BY_ID));
				ps.setInt(1, po_id);
				//rs
				ResultSet rs = (ResultSet)ps.executeQuery();
				
				if(rs.first()) {
					purchaseOrder.setPo_id(rs.getInt("po_id"));
					purchaseOrder.setCreated_date(rs.getDate("created_date"));
					purchaseOrder.setCreated_by(rs.getInt("created_by"));
					
					//get user
					User user = userService.getUserById(purchaseOrder.getCreated_by());
					//set created user
					purchaseOrder.setUser(user);
					
					purchaseOrder.setApproved_by(rs.getInt("approved_by"));
					
					//approved by user 
					User approved_by_user = userService.getUserById(purchaseOrder.getApproved_by());
					//set approved by user
					purchaseOrder.setApproved_by_user(approved_by_user);
					
					purchaseOrder.setSupplier_id(rs.getString("supplier_id"));
					
//					System.out.println(purchaseOrder.getSupplier_id());
					
					//Supplier
					Supplier supplier = supplierService.getSupplierDetails(purchaseOrder.getSupplier_id());
					//set supplier
					purchaseOrder.setSupplier(supplier);
					
					purchaseOrder.setSite_id(rs.getInt("site_id"));
					
					//get Site
					Site site = siteService.getSiteById(purchaseOrder.getSite_id());
					purchaseOrder.setSite(site);
					
					purchaseOrder.setDilivery_date(rs.getDate("dilivery_date"));
					purchaseOrder.setPo_price(rs.getDouble("po_price"));
					purchaseOrder.setStatus(rs.getString("status"));
					purchaseOrder.setDescription(rs.getString("description"));
					
					purchaseOrder.setItems(getPoItems(purchaseOrder.getPo_id()));
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
		
		
		//execute
		return purchaseOrder;
	}

	public boolean deletePo(PurchaseOrder purchaseOrder) {
		
		//delete all related items for PO
		for(Item item : purchaseOrder.getItems()) {
			deleteItemFromPo(purchaseOrder, item.getItem_id());
		}
		
		//delete from notification_po
		deleteFromPoNotification(purchaseOrder.getPo_id());
		//delete from notification
		deleteFromNotification(purchaseOrder.getPo_id());
		
		//ps
		try {
			PreparedStatement ps;
			try {
				ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.DELETE_PO_BY_ID));
				ps.setInt(1, purchaseOrder.getPo_id());
				
				//execute
				int i = ps.executeUpdate();

				if(i!= 0) {
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
		
		return false;
		
	}
	
	public void deleteFromNotification(int po_id) {
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.DELETE_NOTIFICATION));
			ps.setInt(1, po_id);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public void deleteFromPoNotification(int po_id) {
		try {
			PreparedStatement ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.DELETE_PO_NOTIFICATION));
			ps.setInt(1, po_id);
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
	}
	
	public PurchaseOrder deleteItemFromPo(PurchaseOrder purchaseOrder, int item_id) {
		
		//ps
		try {
			PreparedStatement ps;
			try {
				ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.DELETE_PO_ITEMS));
				ps.setInt(1, item_id);
				ps.setInt(2, purchaseOrder.getPo_id());
				
				//execute
				ps.executeUpdate();
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
		
		//updating the total
		double totalPoPrice = purchaseOrder.getPo_price();
		
		//get deleted item quantity
		int quantity = 0;
		double unit_price = 0.0;
		
		for(Item itemQty : purchaseOrder.getItems()) {
			if(item_id == itemQty.getItem_id()) {
				quantity = itemQty.getQuantity();
				unit_price = itemQty.getUnit_price();
				break;
			}
		}
		
		totalPoPrice = totalPoPrice - (unit_price*quantity);
		
		
		//check item status to update the description of PO
		boolean specialRequirement = false;
		for(Item item : getPoItems(purchaseOrder.getPo_id())) {
			if(item.isSpecialApproval()) {
				specialRequirement = true;
				break;
			}
		}
		
		//update purchase_order
		if(!specialRequirement) {
			if(totalPoPrice < 100000) {		
				//execute
				try {
					//ps
					PreparedStatement psPriceUpdate;
					try {
						psPriceUpdate = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.UPDATE_PO_PRICE));
						psPriceUpdate.setDouble(1, totalPoPrice);
						psPriceUpdate.setString(2, "Approved");
						psPriceUpdate.setString(3, "PO is under budget");
						psPriceUpdate.setInt(4, purchaseOrder.getPo_id());
						
						psPriceUpdate.executeUpdate();
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
				
			}else if(totalPoPrice > 100000) {
				//execute
				try {
					//ps
					PreparedStatement psPriceUpdate;
					try {
						psPriceUpdate = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.UPDATE_PO_PRICE));
						psPriceUpdate.setDouble(1, totalPoPrice);
						psPriceUpdate.setString(2, "Pending");
						psPriceUpdate.setString(3, "PO is over the budget");
						psPriceUpdate.setInt(4, purchaseOrder.getPo_id());
						
						psPriceUpdate.executeUpdate();
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
			
		}else {
			//execute
			try {
				//ps
				PreparedStatement psPriceUpdate;
				try {
					psPriceUpdate = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.UPDATE_PO_PRICE));
					psPriceUpdate.setDouble(1, totalPoPrice);
					psPriceUpdate.setString(2, "Pending");
					psPriceUpdate.setString(3, "Some items of the PO need some special Approval!");
					psPriceUpdate.setInt(4, purchaseOrder.getPo_id());
					
					psPriceUpdate.executeUpdate();
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
		
		
		//delete from notification
		boolean prevSpecialCheck = false;
		
		for(Item item : purchaseOrder.getItems()) {
			if(item.isSpecialApproval()) {
				prevSpecialCheck = true;
				break;
			}
		}
		
		
		//first check current po price < 100000 and previously its >100000
		if((totalPoPrice<100000) && purchaseOrder.getPo_price() >= 100000) {
			//if that so need to check do now there is special requirement
			if(!specialRequirement) {
				//then execute
				deleteFromPoNotification(purchaseOrder.getPo_id());
				deleteNotification(purchaseOrder.getPo_id());
			}
		}else if(purchaseOrder.getPo_price() < 100000) {//checking previously there is no notification due to price
			if(prevSpecialCheck && !specialRequirement) {//but there is notification due to items but now it doesnt
				//then execute
				deleteFromPoNotification(purchaseOrder.getPo_id());
				deleteNotification(purchaseOrder.getPo_id());
			}
		}
		
		return getPoById(purchaseOrder.getPo_id());
	}
	
	public PurchaseOrder updatePo(ArrayList<Item> addedItems, PurchaseOrder purchaseOrder) {
		double po_price = 0;
		//check addedItem has purchaseOrder items
		//if there we can append 
		if(!addedItems.isEmpty()) {
			for(Item addItem : addedItems) {
				int quantity = 0;
				boolean matching = false;
				for(Item purchaseItem:purchaseOrder.getItems()) {
					if(addItem.getItem_id() == purchaseItem.getItem_id()) {
						quantity = purchaseItem.getQuantity();
						matching = true;
					}
				}
				
				//adding values of each newly added item to po_price
				po_price += (addItem.getUnit_price() * addItem.getQuantity());
				
				if(matching) {
					quantity += addItem.getQuantity();
					addItem.setQuantity(quantity);
				}
			}			
		}
		
		//check items need special approval
		boolean specialRequirement = false;
		for(Item item : addedItems) {
			if(item.isSpecialApproval()) {
				specialRequirement = true;
				break;
			}
		}
		
		//if not there in added items need to check in purchase order already added items
		if(!specialRequirement) {
			for(Item item : purchaseOrder.getItems()) {
				if(item.isSpecialApproval()) {
					specialRequirement = true;
					break;
				}
			}			
		}
		
		//and add po_price now
		po_price += purchaseOrder.getPo_price();
		
		if(!specialRequirement) {
			//updating po table
			//sql
			//check value whether under or above 100000
			String sql = "";
			if(po_price <100000) {
				try {
					sql = QueryUtil.Q(CommonConstants.UPDATE_PO_BELOW_MARGINAL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.log(Level.SEVERE, e.getMessage());
				}			
			}else {
				try {
					sql = QueryUtil.Q(CommonConstants.UPDATE_PO_OVER_MARGINAL);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Log.log(Level.SEVERE, e.getMessage());
				}
			}
			//ps
			try {
				
				//converting java util date to sql date
				java.util.Date date = purchaseOrder.getDilivery_date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				
				PreparedStatement ps = (PreparedStatement)connection.prepareStatement(sql);
				ps.setInt(1, purchaseOrder.getSite_id());
				ps.setDate(2, sqlDate);
				ps.setDouble(3, po_price);
				
				if(po_price < 100000) {
					ps.setInt(4, purchaseOrder.getPo_id());			
				}else {
					ps.setString(4, "Pending");
					ps.setString(5, "PO is over the budget");
					ps.setInt(6, purchaseOrder.getPo_id());
				}
				
				ps.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.log(Level.SEVERE, e.getMessage());
			}
		}else {
			
			//ps
			try {
				
				//converting java util date to sql date
				java.util.Date date = purchaseOrder.getDilivery_date();
				java.sql.Date sqlDate = new java.sql.Date(date.getTime());
				
				PreparedStatement ps;
				try {
					ps = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.UPDATE_PO_OVER_MARGINAL));
					ps.setInt(1, purchaseOrder.getSite_id());
					ps.setDate(2, sqlDate);
					ps.setDouble(3, po_price);
					ps.setString(4, "Pending");
					ps.setString(5, "Some items of the PO need some special Approval!");
					ps.setInt(6, purchaseOrder.getPo_id());
					
					ps.executeUpdate();
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
		
		
		//updating the po_items table
		//if any new added items are there
		if(!addedItems.isEmpty()) {
			//Sql to update the quantity of items already there
			
			for(Item addItem : addedItems) {
				int quantity = 0;
				boolean matching = false;
				for(Item purchaseItem:purchaseOrder.getItems()) {
					if(addItem.getItem_id() == purchaseItem.getItem_id()) {
						quantity = purchaseItem.getQuantity();
						matching = true;
					}
				}
				
				if(matching) {
					quantity = addItem.getQuantity();
//					String poItemSql = "update po_items set quantity=?, quantity1=? where po_id=? and item_id=?";
					//ps
					try {
						PreparedStatement poItemPs;
						try {
							poItemPs = (PreparedStatement)connection.prepareStatement(QueryUtil.Q(CommonConstants.UPDATE_PO_ITEMS));
							poItemPs.setInt(1, quantity);
							poItemPs.setInt(2, quantity);
							poItemPs.setInt(3, purchaseOrder.getPo_id());
							poItemPs.setInt(4, addItem.getItem_id());
							
							poItemPs.executeUpdate();
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
					
				}else {
					//insert the item to po_items table
					String sqlAddItem = "insert into po_items values(?,?,?,?,?,?)";
					
					//ps
					try {
						PreparedStatement psAddItem = (PreparedStatement)connection.prepareStatement(sqlAddItem);
						psAddItem.setInt(1, purchaseOrder.getPo_id());
						psAddItem.setInt(2, addItem.getItem_id());
						psAddItem.setInt(3, addItem.getQuantity());
						psAddItem.setInt(4, purchaseOrder.getPo_id());
						psAddItem.setString(5, addItem.getItem_name());
						psAddItem.setInt(6, addItem.getQuantity());
						
						psAddItem.executeUpdate();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.log(Level.SEVERE, e.getMessage());
					}
				}
			}			
		}
		
		boolean prevSpecialRequirement = false;
		
		for(Item item : purchaseOrder.getItems()) {
			if(item.isSpecialApproval()) {
				prevSpecialRequirement = true;
				break;
			}
		}
		
		//inserting notification or delete
		//first inserting condition
		if(po_price >= 100000 && purchaseOrder.getPo_price() < 100000) {//means previously there is no notification
			//set po id to purchase order
			purchaseOrder.setPo_id(purchaseOrder.getPo_id());

			//Site manager service instance
			SiteManagerService siteManagerService = new SiteManagerService();

			//Observer design pattern approach
			//attaching the procument Staff
			for(ProcumentStaff procumentStaff : siteManagerService.getAllProcumentStaff()) {
				//assign the id of the purchase order
				procumentStaff.assignPo(purchaseOrder);
				purchaseOrder.attach(procumentStaff);
			}
			
			//add notification
			purchaseOrder.notifyProcumentStaff();
			addNotification(purchaseOrder.getPo_id());// adding to notification
			
		}else if(!prevSpecialRequirement && specialRequirement){//means previously no special requirment but now it has
			
			//set po id to purchase order
			purchaseOrder.setPo_id(purchaseOrder.getPo_id());

			//Site manager service instance
			SiteManagerService siteManagerService = new SiteManagerService();

			//Observer design pattern approach
			//attaching the procument Staff
			for(ProcumentStaff procumentStaff : siteManagerService.getAllProcumentStaff()) {
				//assign the id of the purchase order
				procumentStaff.assignPo(purchaseOrder);
				purchaseOrder.attach(procumentStaff);
			}
			
			//add notification
			purchaseOrder.notifyProcumentStaff();
			addNotification(purchaseOrder.getPo_id());//ading to notification
		
		}else if(purchaseOrder.getDescription().equals("draft_po")) {
			if(specialRequirement || purchaseOrder.getPo_price() >= 100000) {
				addNotification(purchaseOrder.getPo_id());//ading to notification				
			}
		}
		
		//check now do we have any special requirement
		specialRequirement = false;
		for(Item item : getPoById(purchaseOrder.getPo_id()).getItems()) {
			if(item.isSpecialApproval()) {
				specialRequirement = true;
				break;
			}
		}
		//delete notification
		//first check current po price < 100000 and previously its >100000
		if((po_price<100000) && purchaseOrder.getPo_price() >= 100000) {
			//if that so need to check do now there is special requirement
			if(!specialRequirement) {
				//then execute
				deleteFromPoNotification(purchaseOrder.getPo_id());
				deleteNotification(purchaseOrder.getPo_id());
			}
		}else if(purchaseOrder.getPo_price() < 100000) {//checking previously there is no notification due to price
			if(prevSpecialRequirement && !specialRequirement) {//but there is notification due to items but now it doesnt
				//then execute
				deleteFromPoNotification(purchaseOrder.getPo_id());
				deleteNotification(purchaseOrder.getPo_id());
			}
		}
				
		
		return getPoById(purchaseOrder.getPo_id());
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
			Log.log(Level.SEVERE, e.getMessage());
		}
		return succes;
	}
	
	public int getMaxPoId() {
		
		//string sql
		String sql = "select max(po_id) from purchase_order";
		//statement
		try {
			Statement st = (Statement)connection.createStatement();
			ResultSet rs = (ResultSet)st.executeQuery(sql);
			
			if(rs.first()) {
				return rs.getInt("max(po_id)");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.log(Level.SEVERE, e.getMessage());
		}
		//result
		
		return 0;
	}
	
	public boolean deleteNotification(int po_id) {
		boolean success = true;
		//sql for delete from the notification
		String deleteFromNotif = "delete from notification where notifi_type='PO' and notif_rel_id=?";
		try {
			PreparedStatement deleteFromNotiPs = (PreparedStatement)connection.prepareStatement(deleteFromNotif);
			deleteFromNotiPs.setInt(1, po_id);
			deleteFromNotiPs.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return success;
	}
}
