package com.procument.controller;

import java.io.IOException;
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
import com.procument.model.Supplier;
import com.procument.model.User;
import com.procument.service.ItemService;
import com.procument.service.NotificationService;
import com.procument.service.SupplierService;

/**
 * Servlet implementation class itemAccessServlet
 */
@WebServlet("/itemAccessServlet")
public class itemAccessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public itemAccessServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	public double itemCount(HttpServletRequest request, HttpServletResponse response, ArrayList<Item> items) {
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
	
	public ArrayList<Item> getRelatedItemList(HttpServletRequest request, HttpServletResponse response, ArrayList<Item> items, int pageNumber){
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
			newItem.setUnit(item.getUnit());
			newItem.setSpecialApproval(item.isSpecialApproval());
			
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//get type
		int type = Integer.parseInt(request.getParameter("type"));
		SupplierService supplierService = SupplierService.getInstance();
		
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute("user");
		
		double pageCount = 1;
		
		//always get new arrayList
		ArrayList<Supplier> addedSuppliers = new ArrayList<Supplier>();

		//Notifications for create po
		NotificationService notificationService = NotificationService.getInstance();
		//get Notifications
		ArrayList<Notification> notifications = notificationService.getPoNotification("site_manager");
		//set notification settings 
		request.setAttribute("notifications", notifications);
			
		
		//always get new arrayList
		
		//Item service initialization
		ItemService itemService = ItemService.getInstance();
		
		//arraylist fetch suppliers
		ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();

		//arraylist fetch items
		ArrayList<Item> items = new ArrayList<Item>();
		items.clear();
		
		//access item panel
		if(type == 1) {
			
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			
			request.setAttribute("suppliers", suppliers);
			request.setAttribute("items", items);
			
			session.setAttribute("addedSuppliers", addedSuppliers);
			session.removeAttribute("deletedSuppliers");
			session.removeAttribute("updateIName");
			session.removeAttribute("updateUPrice");
			session.removeAttribute("unit");
			session.removeAttribute("specialApproval");
			
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			
			requestDispatcher.forward(request, response);
		}else if(type == 2) {//add suppliers
			
			String supplier_id = request.getParameter("supplier_id");
			addedSuppliers.addAll((Collection<? extends Supplier>) session.getAttribute("addedSuppliers"));
			
			Supplier supplier = supplierService.getSupplierDetails(supplier_id);
			boolean notThere = true;
			for(Supplier addedSupplier : addedSuppliers) {
				if(addedSupplier.getSupplier_id().equals(supplier.getSupplier_id())) {
					notThere = false;
					break;
				}
			}
			
			try {
				ArrayList<Supplier> deletedSuppliers = (ArrayList<Supplier>) session.getAttribute("deletedSuppliers");
				if(notThere) {
					//check adding supplier is available in updation list
					boolean notThereInDeleted = true;
					
					for(Supplier deletedSupplier : deletedSuppliers) {
						if(deletedSupplier.getSupplier_id().equals(supplier.getSupplier_id())) {
							notThereInDeleted = false;
							break;
						}
					}
					
					//if not there add to addedSupplier List
					if(notThereInDeleted) {
						addedSuppliers.add(supplier);						
					}
				}
			}catch(NullPointerException exc) {
				if(notThere) {
					addedSuppliers.add(supplier);
				}
			}
			
			
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			request.setAttribute("suppliers", suppliers);
			session.setAttribute("addedSuppliers", addedSuppliers);
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			request.setAttribute("items", items);
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			requestDispatcher.forward(request, response);
		
		}else if(type == 3) {//remove suppliers
			
			//remove session
			String supplier_id = request.getParameter("supplier_id");
			addedSuppliers.addAll((Collection<? extends Supplier>) session.getAttribute("addedSuppliers"));
			
			Supplier supplier = supplierService.getSupplierDetails(supplier_id);
			
			//use iterator to remove the object
			Iterator<Supplier> iter = addedSuppliers.iterator();
			while (iter.hasNext()) 
			{
			    Supplier supplierRemove = iter.next();
			    if(supplierRemove.getSupplier_id().equals(supplier.getSupplier_id()))
			    {
			        //Use iterator to remove this User object.
			        iter.remove();
			    }
			}
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			request.setAttribute("suppliers", suppliers);
			session.setAttribute("addedSuppliers", addedSuppliers);
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			request.setAttribute("items", items);
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			requestDispatcher.forward(request, response);
		}else if(type == 4) {//add item
			
			//get all added suppliers	
			addedSuppliers.addAll((Collection<? extends Supplier>) session.getAttribute("addedSuppliers")); 

			if(!addedSuppliers.isEmpty()) {
				String item_name = request.getParameter("iName");
				double unit_price = Double.parseDouble(request.getParameter("uPrice"));
				String unitInitial = request.getParameter("unit");
				
				String unit = "";
				
				if(unitInitial.equals("1")) {
					unit = "Meters";
				}else if(unitInitial.equals("2")) {
					unit = "Kg";
				}else if(unitInitial.equals("3")) {
					unit = "Piece";
				}else if(unitInitial.equals("4")) {
					unit = "Liter";
				}else if(unitInitial.equals("5")) {
					unit = "Cube";
				}
				

				String specialApprovalS = request.getParameter("specialApproval");
				boolean specialApproval;
				if(specialApprovalS.equals("1")) {
					specialApproval = false;
				}else {
					specialApproval = true;
				}
				
				//add items
				boolean success = itemService.addItems(item_name, unit_price, unit, specialApproval, addedSuppliers, "");
				
				addedSuppliers.clear();
				
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
				request.setAttribute("suppliers", suppliers);
				session.setAttribute("addedSuppliers", addedSuppliers);
				items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
				request.setAttribute("items", items);
				pageCount = itemCount(request, response, itemService.getAllItems());
				request.setAttribute("pageCount", pageCount);
				request.setAttribute("pageCurrent", 1);
				requestDispatcher.forward(request, response);
				
			}else {
				addedSuppliers.clear();
				
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
				request.setAttribute("suppliers", suppliers);
				session.setAttribute("addedSuppliers", addedSuppliers);
				items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
				request.setAttribute("items", items);
				pageCount = itemCount(request, response, itemService.getAllItems());
				request.setAttribute("pageCount", pageCount);
				request.setAttribute("pageCurrent", 1);
				requestDispatcher.forward(request, response);
			}
			
			
		}else if(type == 5) {//change page
			
			int pageNumber = Integer.parseInt(request.getParameter("page_number"));
			
			items = getRelatedItemList(request, response, itemService.getAllItems(), pageNumber);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			
			request.setAttribute("suppliers", suppliers);
			request.setAttribute("items", items);
			session.setAttribute("addedSuppliers", addedSuppliers);
			session.removeAttribute("deletedSuppliers");
			session.removeAttribute("updateIName");
			session.removeAttribute("updateUPrice");
			session.removeAttribute("unit");
			session.removeAttribute("specialApproval");
			
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", pageNumber);
			
			requestDispatcher.forward(request, response);
		}else if(type == 6) {//update item set details to view
			
			session.removeAttribute("deletedSuppliers");
			session.removeAttribute("updateIName");
			session.removeAttribute("updateUPrice");
			session.removeAttribute("unit");
			session.removeAttribute("specialApproval");
			session.removeAttribute("item_id");
			
			String action = request.getParameter("action");
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			
			Item item = new Item();
			if(action.equals("update")) {
				item = itemService.getItem(item_id);
				ArrayList<Supplier> deletedSuppliers = item.getSuppliers();				
				session.setAttribute("updateIName", item.getItem_name());
				session.setAttribute("updateUPrice", item.getUnit_price());
			
				String unit = item.getUnit();
				
				String specialApprovalS;
				
				if(item.isSpecialApproval()) {
					specialApprovalS = "1";
				}else {
					specialApprovalS = "0";
				}
				
				session.setAttribute("unit", unit);
				session.setAttribute("specialApproval", specialApprovalS);
				session.setAttribute("item_id", item.getItem_id());
				session.setAttribute("deletedSuppliers", deletedSuppliers);
			}
			
			addedSuppliers.clear();
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			request.setAttribute("suppliers", suppliers);
			session.setAttribute("addedSuppliers", addedSuppliers);
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			request.setAttribute("items", items);
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			requestDispatcher.forward(request, response);
		}else if(type == 7) {//update db
			//get all added suppliers	
			addedSuppliers.addAll((Collection<? extends Supplier>) session.getAttribute("addedSuppliers"));
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			
			//get Item
			Item itemDb = itemService.getItem(item_id);
			String item_name = request.getParameter("iName");
			double unit_price = Double.parseDouble(request.getParameter("uPrice"));
			String unitInitial = request.getParameter("unit");
			
			String unit = "";
			
			if(unitInitial.equals("1")) {
				unit = "Meters";
			}else if(unitInitial.equals("2")) {
				unit = "Kg";
			}else if(unitInitial.equals("3")) {
				unit = "Piece";
			}else if(unitInitial.equals("4")) {
				unit = "Liter";
			}else if(unitInitial.equals("5")) {
				unit = "Cube";
			}
			
			//get approval status
			String specialApprovalS = request.getParameter("specialApproval");
			boolean specialApproval;
			if(specialApprovalS.equals("1")) {
				specialApproval = false;
			}else {
				specialApproval = true;
			}
			
			//update database
			itemService.updateItem(itemDb, addedSuppliers, item_name, unit_price, unit, specialApproval);
			
			//go back with updated List
			//removing existing values
			session.removeAttribute("deletedSuppliers");
			session.removeAttribute("updateIName");
			session.removeAttribute("updateUPrice");
			session.removeAttribute("unit");
			session.removeAttribute("item_id");
			session.removeAttribute("specialApproval");
			
			//get item from db after updated
			itemDb = itemService.getItem(item_id);
			//clear added suppliers
			addedSuppliers.clear();
			//and add empty added supplier
			session.setAttribute("addedSuppliers", addedSuppliers);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			request.setAttribute("suppliers", suppliers);
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			request.setAttribute("items", items);
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			
			//add deleted suppliers as db values
			session.setAttribute("deletedSuppliers", itemDb.getSuppliers());
			
			session.setAttribute("updateIName", itemDb.getItem_name());
			session.setAttribute("updateUPrice", itemDb.getUnit_price());
			session.setAttribute("unit", itemDb.getUnit());
			session.setAttribute("item_id", itemDb.getItem_id());
			
			requestDispatcher.forward(request, response);
			
		}else if(type == 8) {//delete supplier
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			String supplier_id = request.getParameter("supplier_id");
			
			Item itemDb = itemService.deleteSupplier(supplier_id, item_id);
			
			//return to the page with updated List
			
			//removing existing values
			session.removeAttribute("deletedSuppliers");
			session.removeAttribute("updateIName");
			session.removeAttribute("updateUPrice");
			session.removeAttribute("unit");
			session.removeAttribute("item_id");
			session.removeAttribute("specialApproval");
			
			//add all added suppliers if any 
			addedSuppliers.addAll((Collection<? extends Supplier>) session.getAttribute("addedSuppliers"));
			
			//and add empty added supplier
			session.setAttribute("addedSuppliers", addedSuppliers);
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			request.setAttribute("items", items);
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			
			request.setAttribute("suppliers", suppliers);
			
			//add deleted suppliers as db values
			session.setAttribute("deletedSuppliers", itemDb.getSuppliers());
			
			session.setAttribute("updateIName", itemDb.getItem_name());
			session.setAttribute("updateUPrice", itemDb.getUnit_price());
			session.setAttribute("unit", itemDb.getUnit());
			session.setAttribute("item_id", itemDb.getItem_id());
			
			requestDispatcher.forward(request, response);
		}else if(type == 9) {//delete item fully
			int item_id = Integer.parseInt(request.getParameter("item_id"));
			Item item = itemService.getItem(item_id);
			
			itemService.deleteItem(item);
			
			//get items
			items = getRelatedItemList(request, response, itemService.getAllItems(), 1);
			
			//go back to itemPanel
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("ItemPanel.jsp");
			
			request.setAttribute("suppliers", suppliers);
			request.setAttribute("items", items);
			session.setAttribute("addedSuppliers", addedSuppliers);
			
			session.removeAttribute("deletedSuppliers");
			session.removeAttribute("updateIName");
			session.removeAttribute("updateUPrice");
			session.removeAttribute("unit");
			session.removeAttribute("specialApproval");
			
			pageCount = itemCount(request, response, itemService.getAllItems());
			request.setAttribute("pageCount", pageCount);
			request.setAttribute("pageCurrent", 1);
			
			requestDispatcher.forward(request, response);
		}
		doGet(request, response);
	}

}
