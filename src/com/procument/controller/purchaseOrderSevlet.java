package com.procument.controller;

import java.io.IOException;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.procument.model.Item;
import com.procument.model.Notification;
import com.procument.model.PurchaseOrder;
import com.procument.model.Site;
import com.procument.model.SiteManager;
import com.procument.model.Supplier;
import com.procument.model.User;
import com.procument.service.ItemService;
import com.procument.service.NotificationService;
import com.procument.service.SiteManagerService;
import com.procument.service.SiteService;
import com.procument.service.SupplierService;

/**
 * Servlet implementation class purchaseOrderSevlet
 */
@WebServlet("/purchaseOrderSevlet")
public class purchaseOrderSevlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public purchaseOrderSevlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    private double itemCount(HttpServletRequest request, HttpServletResponse response, ArrayList<Item> items) {
		int count = 0;
		
		for(Item item : items) {
			for(Supplier supplier : item.getSuppliers()) {
				count++;
			}
		}
		
		double pageCount = count / 10.0;
		pageCount = Math.ceil(pageCount);
		
		return pageCount;
	}
	
    //to check adding item
    private boolean checkNewAdding(ArrayList<Item> addedItems, Item item) {
    	boolean notThere = true;
		for(Item addedItem : addedItems) {
			if(addedItem.getItem_id() == item.getItem_id()) {
				if(addedItem.getQuantity() == item.getQuantity()) {
					notThere = false;
					break;						
				}else {
					//delete from the list and update
					deleteFromArrayList(addedItems, addedItem);
					addedItems.add(item);
					notThere = false;
					break;
				}
			}
			
		}
		return notThere;
    }
    
    public void deleteFromArrayList(ArrayList<Item> addedItems, Item addedItem) {
    	//use iterator to remove the object
		Iterator<Item> iter = addedItems.iterator();
		while (iter.hasNext()) 
		{
		    Item itemRemove = iter.next();
		    if(itemRemove.getItem_id() ==addedItem.getItem_id())
		    {
		        //Use iterator to remove this User object.
		        iter.remove();
		        break;
		    }
		}
    }
    
    private ArrayList<Item> getRelatedItemList(HttpServletRequest request, HttpServletResponse response, ArrayList<Item> items, int pageNumber){
		ArrayList<Item> relatedItems = new ArrayList<Item>();
		ArrayList<Supplier> suppliers;
		int pageCounter = 0;
		int counter = 0;
		
		//looping all items
		for(Item item : items) {
			//add item details to new Item
			Item newItem = new Item();
			newItem.setItem_id(item.getItem_id());
			newItem.setItem_code(item.getItem_code());
			newItem.setItem_name(item.getItem_name());
			newItem.setUnit_price(item.getUnit_price());
			
			//supplier arrayLits to get page oriented suppliers
			suppliers = new ArrayList<Supplier>();
			
			for(Supplier supplier : item.getSuppliers()) {
				counter++;
				suppliers.add(supplier);
				
				//10 by 10 new page occurs
				if((counter % 10) == 0) {
					pageCounter++;
					//check page is equal to asked page
					if(pageCounter == pageNumber) {
						newItem.setSuppliers(suppliers);
						relatedItems.add(newItem);	
						return relatedItems;
					}
					
					relatedItems = new ArrayList<Item>(); 
				}
				
			}
			
			if((counter % 10) != 0) {
				newItem.setSuppliers(suppliers);
				relatedItems.add(newItem);				
			}
			
		}
		
		return relatedItems;
	}
	
	private long calculateTotal(ArrayList<Item> items) {
		long po_price = (long) 0.00;
		
		for(Item item : items) {
			po_price += (item.getUnit_price() * item.getQuantity());
		}
		
		return po_price;
	}
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int type = Integer.parseInt(request.getParameter("type"));
		
		//arraylist fetch items
		ArrayList<Item> addedItems = new ArrayList<Item>();
		
		//get supplier service
		SupplierService supplierService = SupplierService.getInstance();
		
		//Notifications for create po
		NotificationService notificationService = NotificationService.getInstance();

		//get Notifications
		ArrayList<Notification> notifications = notificationService.getPoNotification("site_manager");
		
		//Item service initialization
		ItemService itemService = ItemService.getInstance();
		
		HttpSession session = request.getSession();
		
		//get user from the session
		User user = (User) session.getAttribute("user");
		
		//check type
		if(type == 1) {//access the po page
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
			
			//site list
			ArrayList<Site> sites = new ArrayList<Site>();
			
			//site Service instance
			SiteService siteService = SiteService.getInstance();
			
			//get related Sites
			sites = siteService.getRelatedSites(user);
			
			//create a session to load sites
			session.setAttribute("sites", sites);
			
			//set notification settings 
			request.setAttribute("notifications", notifications);
			
			//arraylist fetch suppliers
			ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
			//set suppliers to session
			session.setAttribute("suppliers", suppliers);
			
			//set session addedItems
			session.setAttribute("addedItems", addedItems);
			
			//removing suppliers from session
			session.removeAttribute("items");
			//removing supplier from session
			session.removeAttribute("supplier");
			//removing pageCount from session
			session.removeAttribute("pageCount");
			//removing pageCurrent from session
			session.removeAttribute("pageCurrent");
			//remove session value and search value
			session.removeAttribute("value");
			session.removeAttribute("searchValue");
			//removing po_price session
			session.removeAttribute("po_price");
			
			requestDispatcher.forward(request, response);
			
		}else if(type == 2) {//supplier selection type == 2
			//get Supplier details
			String supplier_id = request.getParameter("supplier_id");
			Supplier supplier = supplierService.getSupplierDetails(supplier_id);
			
			//initiate item list
			ArrayList<Item> items = new ArrayList<Item>();
			
			//get supplier related Items
			items = itemService.getSupplierRelatedItems(supplier);
			
			//removing suppliers from session
			session.removeAttribute("suppliers");
			//removing value and search value
			session.removeAttribute("value");
			session.removeAttribute("searchValue");
			//removing po_price  from session
			session.removeAttribute("po_price");
			
			//add items to the session
			session.setAttribute("items", items);
			//supplier selected setting to attribute supplier
			session.setAttribute("supplier", supplier);
			//add addedItems to session
			session.setAttribute("addedItems", addedItems);
			//set notification settings 
			request.setAttribute("notifications", notifications);
			
			//pagination settings
			double pageCount = 1;
			pageCount = itemCount(request, response, items);
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("pageCurrent", 1);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
			requestDispatcher.forward(request, response);
		}else if(type == 3) {//adding items to po
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			int item_id = Integer.parseInt(request.getParameter("item_id"));

			//adding all inside the addedItems session
			addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
			
			Item item = ItemService.getInstance().getItem(item_id);
			item.setQuantity(quantity);
			
			//to check adding item is already added
			boolean notThere = checkNewAdding(addedItems, item);
			
			//if updation of quantity or actually not there item will be added to the list
			if(notThere) {
				addedItems.add(item);
			}
			
			//calculating the 
			long po_price = calculateTotal(addedItems);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
			
			session.removeAttribute("value");
			
			session.setAttribute("addedItems", addedItems);
			session.setAttribute("po_price", po_price);
			//set notification settings 
			request.setAttribute("notifications", notifications);
			
			requestDispatcher.forward(request, response);

		}else if(type == 4) {
			String search_value = request.getParameter("search_value");
			String value = "0";
			value = request.getParameter("search_option");
			
			if(value == null) {
				value = "0";
			}
			
			//get Supplier details
			String supplier_id = request.getParameter("supplier_id");
			Supplier supplier = supplierService.getSupplierDetails(supplier_id);
			
			//initiate item list
			ArrayList<Item> items = new ArrayList<Item>();
			
			String searchKey;
			
			if(value.equals("0") || value.equals("2")) {
				searchKey = "item_name";
				value = "2";
			}else {
				searchKey = "item_code";
			}
			
			//get supplier related Items
			items = itemService.searchItemWithSupplier(searchKey, search_value, supplier);
			
			session.setAttribute("value", value);
			session.setAttribute("searchValue", search_value);
			//set notification settings 
			request.setAttribute("notifications", notifications);
			
			//add items to the session
			session.setAttribute("items", items);
			
			//pagination settings
			double pageCount = 1;
			pageCount = itemCount(request, response, items);
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("pageCurrent", 1);
			
			
			//adding all inside the addedItems session
			addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
			//set session addedItems
			session.setAttribute("addedItems", addedItems);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
			requestDispatcher.forward(request, response);
			
		}else if(type == 5) {//remove item from added list
			//to remove 
			//get item id
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			Item item = itemService.getItem(item_id);
			
			//adding all items in the session
			addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
			
			//iterator will remove item from the list
			//use iterator to remove the object
			Iterator<Item> iter = addedItems.iterator();
			while (iter.hasNext()) 
			{
			    Item itemRemove = iter.next();
			    if(itemRemove.getItem_id() == item.getItem_id())
			    {
			        //Use iterator to remove this User object.
			        iter.remove();
			    }
			}
			
			
			//calculating the 
			long po_price = calculateTotal(addedItems);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
			
			session.setAttribute("addedItems", addedItems);
			session.setAttribute("po_price", po_price);
			//set notification settings 
			request.setAttribute("notifications", notifications);
			
			requestDispatcher.forward(request, response);
			
		}else if(type == 6) {//add PO
			//check is there supplier selected
			//get supplier from the session
			Supplier supplier = (Supplier)session.getAttribute("supplier");
			//set notification settings 
			request.setAttribute("notifications", notifications);

			if(supplier == null) {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
				String errorMessage = "Please Select a supplier!";
				request.setAttribute("errorMessage", errorMessage);
				requestDispatcher.forward(request, response);
			}else {
				//adding all items in the session
				addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
				
				if(addedItems.size() < 1) {
					
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
					String errorMessage = "Please Add Items!";
					request.setAttribute("errorMessage", errorMessage);
					requestDispatcher.forward(request, response);
					
				}else {
					//get selected site id
					int site_id = Integer.parseInt(request.getParameter("site_id"));
					//get site by id
					SiteService siteService = SiteService.getInstance();
					Site site = siteService.getSiteById(site_id);
					
					//add po
					SiteManagerService siteManagerService = new SiteManagerService();
					
					
					//add values to purchase order
					PurchaseOrder purchaseOrder = new PurchaseOrder();
					purchaseOrder.setUser(user);
					purchaseOrder.setItems(addedItems);
					purchaseOrder.setSupplier(supplier);
					purchaseOrder.setSite(site);
					purchaseOrder.setDescription("");
					
					//dilivery date String converting to java util date 
					String dateStr = request.getParameter("dilivery_date");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					Date date;
					try {
						date = sdf.parse(dateStr);
						purchaseOrder.setDilivery_date(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					//add to db using the method
					siteManagerService.makeNewPurchaseOrder(purchaseOrder);
					
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
					
					//site list
					ArrayList<Site> sites = new ArrayList<Site>();
					
					//get related Sites
					sites = siteService.getRelatedSites(user);
					
					//create a session to load sites
					session.setAttribute("sites", sites);
					
					//set notification settings 
					request.setAttribute("notifications", notifications);
					
					//arraylist fetch suppliers
					ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
					//set suppliers to session
					session.setAttribute("suppliers", suppliers);
					
					//set session addedItems
					session.setAttribute("addedItems", addedItems);
					
					//removing suppliers from session
					session.removeAttribute("items");
					//removing supplier from session
					session.removeAttribute("supplier");
					//removing pageCount from session
					session.removeAttribute("pageCount");
					//removing pageCurrent from session
					session.removeAttribute("pageCurrent");
					//remove session value and search value
					session.removeAttribute("value");
					session.removeAttribute("searchValue");
					//removing po_price session
					session.removeAttribute("po_price");
					//add empty added items
					session.setAttribute("addedItems", new ArrayList<Item>());
					
					//po created prompt
					request.setAttribute("prompt", "created");
					
					requestDispatcher.forward(request, response);
					
				}
				
				
			}
			
			
		}else if(type == 7) {
			String po_id = request.getParameter("po_id");
			type = 1;
			
			session.setAttribute("po_id", po_id);
			
			response.sendRedirect("updatePo");
		}else if(type == 8) {
			//adding all items in the session
			addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
			
			
			//get supplier from the session
			Supplier supplier = (Supplier)session.getAttribute("supplier");
			
			//get selected site id
			int site_id = Integer.parseInt(request.getParameter("site_id"));
			//get site by id
			SiteService siteService = SiteService.getInstance();
			Site site = siteService.getSiteById(site_id);
		
			//add po
			SiteManagerService siteManagerService = new SiteManagerService();
			
			
			//add values to purchase order
			PurchaseOrder purchaseOrder = new PurchaseOrder();
			purchaseOrder.setUser(user);
			purchaseOrder.setItems(addedItems);
			purchaseOrder.setSupplier(supplier);
			purchaseOrder.setSite(site);
			purchaseOrder.setDescription("draft_po");
			
			//dilivery date String converting to java util date 
			String dateStr = request.getParameter("dilivery_date");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date;
			try {
				date = sdf.parse(dateStr);
				purchaseOrder.setDilivery_date(date);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//add to db using the method
			siteManagerService.makeNewPurchaseOrder(purchaseOrder);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("CreatePO.jsp");
			
			//site list
			ArrayList<Site> sites = new ArrayList<Site>();
			
			//get related Sites
			sites = siteService.getRelatedSites(user);
			
			//create a session to load sites
			session.setAttribute("sites", sites);
			
			//set notification settings 
			request.setAttribute("notifications", notifications);
			
			//arraylist fetch suppliers
			ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
			//set suppliers to session
			session.setAttribute("suppliers", suppliers);
			
			//set session addedItems
			session.setAttribute("addedItems", addedItems);
			
			//removing suppliers from session
			session.removeAttribute("items");
			//removing supplier from session
			session.removeAttribute("supplier");
			//removing pageCount from session
			session.removeAttribute("pageCount");
			//removing pageCurrent from session
			session.removeAttribute("pageCurrent");
			//remove session value and search value
			session.removeAttribute("value");
			session.removeAttribute("searchValue");
			//removing po_price session
			session.removeAttribute("po_price");
			//add empty added items
			session.setAttribute("addedItems", new ArrayList<Item>());
			
			//po created prompt
			request.setAttribute("prompt", "draft");
			
			requestDispatcher.forward(request, response);
		}
		
		doGet(request, response);
	}

}
