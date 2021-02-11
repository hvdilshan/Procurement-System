package com.procument.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;

import com.procument.model.Item;
import com.procument.model.Notification;
import com.procument.model.PurchaseOrder;
import com.procument.model.Site;
import com.procument.model.Supplier;
import com.procument.service.ItemService;
import com.procument.service.NotificationService;
import com.procument.service.PurchaseOrderService;
import com.procument.service.SiteService;
import com.procument.service.SupplierService;

/**
 * Servlet implementation class updatePoServlet
 */
@WebServlet("/updatePoServlet")
public class updatePoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public updatePoServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
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
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		int po_id = Integer.parseInt((String) session.getAttribute("po_id"));
		
		//remove Added Items from session
		session.removeAttribute("addedItems");
		//adding all items now inside in puchase order
		ArrayList<Item> addedItems = new ArrayList<Item>();
		session.setAttribute("addedItems", addedItems);
		PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();
		
		PurchaseOrder purchaseOrder = purchaseOrderService.getPoById(po_id);
		
		//site list
		ArrayList<Site> sites = new ArrayList<Site>();
		
		//site Service instance
		SiteService siteService = SiteService.getInstance();
		
		//get related Sites
		sites = siteService.getRelatedSites(purchaseOrder.getUser());
			
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("UpdatePo.jsp");
		session.setAttribute("purchaseOrder", purchaseOrder);
		//create a session to load sites
		session.setAttribute("sites", sites);
		
		ItemService itemService = ItemService.getInstance();
		//get supplier related Items
		ArrayList<Item> items = itemService.getSupplierRelatedItems(purchaseOrder.getSupplier());
		//add items to the session
		session.setAttribute("items", items);
		
		//get supplier service
		SupplierService supplierService = SupplierService.getInstance();
		//arraylist fetch suppliers
		ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
		//set suppliers to session
		session.setAttribute("suppliers", suppliers);
		
		//pagination settings
		double pageCount = 1;
		pageCount = itemCount(request, response, items);
		session.setAttribute("pageCount", pageCount);
		session.setAttribute("pageCurrent", 1);
		
		//calculating the 
		long po_price = calculateTotal(purchaseOrder.getItems());
		System.out.println(purchaseOrder.getItems());
		session.setAttribute("po_price", po_price);
		
		requestDispatcher.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//get main values
		int type = Integer.parseInt(request.getParameter("type"));
		//po service instance
		PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();
		
		//adding all items now inside in puchase order
		ArrayList<Item> addedItems = new ArrayList<Item>();
		
		//http session
		HttpSession session = request.getSession();
		if(type == 1) {
			
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			int po_id = Integer.parseInt(request.getParameter("item_id"));
			session.setAttribute("addedItems", addedItems);
			
		}else if(type==2) {//delete an item of approved but not yet accepted by supplier

			//adding all inside the addedItems session
			try {
				addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
				
			}catch(NullPointerException exc) {
				
			}
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			int po_id = Integer.parseInt(request.getParameter("item_id"));
			
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.getAttribute("purchaseOrder");
			
			purchaseOrder = purchaseOrderService.deleteItemFromPo(purchaseOrder, item_id);

			//to calculate the total
			ArrayList<Item> allItems = new ArrayList<Item>();
			//add all items in the purchaseOrder to calculate the totalPrice
			allItems.addAll(purchaseOrder.getItems());
			allItems.addAll(addedItems);
			
			//calculating the 
			long po_price = calculateTotal(allItems);
			
			session.setAttribute("po_price", po_price);
			session.setAttribute("addedItems", addedItems);
			session.setAttribute("purchaseOrder", purchaseOrder);
			
			request.setAttribute("prompt", "deleted");
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("UpdatePo.jsp");
			requestDispatcher.forward(request, response);
		}else if(type == 3) {//add new Item
			
			//get Passing values
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			int item_id = Integer.parseInt(request.getParameter("item_id"));

			Item item = ItemService.getInstance().getItem(item_id);
			item.setQuantity(quantity);
			
			//adding all inside the addedItems session
			addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
			
			//to check adding item is already added
			boolean notThere = checkNewAdding(addedItems, item);
			
			//if updation of quantity or actually not there item will be added to the list
			if(notThere) {
				addedItems.add(item);
			}
			
			//getting purchase orders
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.getAttribute("purchaseOrder");
			
			//to calculate price
			ArrayList<Item> allItems = new ArrayList<Item>();
			allItems.addAll(addedItems);
			allItems.addAll(purchaseOrder.getItems());
			//calculating the 
			long po_price = calculateTotal(allItems);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("UpdatePo.jsp");
			
			session.removeAttribute("value");	
			session.setAttribute("addedItems", addedItems);
			session.setAttribute("po_price", po_price);

			requestDispatcher.forward(request, response);

		}else if(type == 4) {//remove added list from items
			//to remove 
			//get item by its id
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			Item item = ItemService.getInstance().getItem(item_id);
			
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
			
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.getAttribute("purchaseOrder");
			
			//to calculate price
			ArrayList<Item> allItems = new ArrayList<Item>();
			allItems.addAll(addedItems);
			allItems.addAll(purchaseOrder.getItems());
			//calculating the 
			long po_price = calculateTotal(allItems);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("UpdatePo.jsp");
			
			session.removeAttribute("value");	
			session.setAttribute("addedItems", addedItems);
			session.setAttribute("po_price", po_price);

			requestDispatcher.forward(request, response);
		}else if(type == 5) {//update add to db
			//adding all inside the addedItems session
			addedItems.addAll((Collection<? extends Item>) session.getAttribute("addedItems"));
			//get po details
			PurchaseOrder purchaseOrder = (PurchaseOrder) session.getAttribute("purchaseOrder");
			
			//get selected site id
			int site_id = Integer.parseInt(request.getParameter("site_id"));
			//get site by id
			SiteService siteService = SiteService.getInstance();
			Site site = siteService.getSiteById(site_id);
			
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
			
			//set site details
			purchaseOrder.setSite(site);
			purchaseOrder.setSite_id(site_id);
			
			purchaseOrder = purchaseOrderService.updatePo(addedItems, purchaseOrder);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("UpdatePo.jsp");
			session.setAttribute("purchaseOrder", purchaseOrder);
			
			addedItems.clear();
			session.setAttribute("addedItems", addedItems);
			
			//calculating the 
			long po_price = calculateTotal(purchaseOrder.getItems());
			System.out.println(purchaseOrder.getItems());
			session.setAttribute("po_price", po_price);
			
			request.setAttribute("prompt", "updated");
			
			requestDispatcher.forward(request, response);
		}else if(type == 6) {
			// TODO Auto-generated method stub
			int po_id = Integer.parseInt((String) session.getAttribute("po_id"));
			
			//remove Added Items from session
			session.removeAttribute("addedItems");
			//adding all items now inside in puchase order
			addedItems = new ArrayList<Item>();
			session.setAttribute("addedItems", addedItems);
			
			PurchaseOrder purchaseOrder = purchaseOrderService.getPoById(po_id);
			
			//site list
			ArrayList<Site> sites = new ArrayList<Site>();
			
			//site Service instance
			SiteService siteService = SiteService.getInstance();
			
			//get related Sites
			sites = siteService.getRelatedSites(purchaseOrder.getUser());
				
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("UpdatePo.jsp");
			session.setAttribute("purchaseOrder", purchaseOrder);
			//create a session to load sites
			session.setAttribute("sites", sites);
			
			ItemService itemService = ItemService.getInstance();
			//get supplier related Items
			ArrayList<Item> items = itemService.getSupplierRelatedItems(purchaseOrder.getSupplier());
			//add items to the session
			session.setAttribute("items", items);
			
			//get supplier service
			SupplierService supplierService = SupplierService.getInstance();
			//arraylist fetch suppliers
			ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
			//set suppliers to session
			session.setAttribute("suppliers", suppliers);
			
			//pagination settings
			double pageCount = 1;
			pageCount = itemCount(request, response, items);
			session.setAttribute("pageCount", pageCount);
			session.setAttribute("pageCurrent", 1);
			
			//calculating the 
			long po_price = calculateTotal(purchaseOrder.getItems());
			System.out.println(purchaseOrder.getItems());
			session.setAttribute("po_price", po_price);
			
			requestDispatcher.forward(request, response);
		}else if(type==7) {
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("DraftPOPanel.jsp");
			
			//Purchase order list
			ArrayList<PurchaseOrder> purchaseOrders = purchaseOrderService.getAllDraftPurchaseOrders();
			long totalPoCost = purchaseOrderService.getTotalPoCost();
			int totalPoCount = purchaseOrderService.getTotalPoCount();
			long poAverage = purchaseOrderService.calculateAveragePoCost(totalPoCost, totalPoCount);
			
			
			//set purchase orders to the session
			session.setAttribute("purchaseOrders", purchaseOrders);
			session.setAttribute("poAverage", poAverage);
			session.setAttribute("totalPoCost", totalPoCost);
			
			//Notifications for create po
			NotificationService notificationService = NotificationService.getInstance();
			//get Notifications
			ArrayList<Notification> notifications = notificationService.getPoNotification("site_manager");
			//set notification settings 
			request.setAttribute("notifications", notifications);
				
			requestDispatcher.forward(request, response);
		}
//		doGet(request, response);
	}

}
