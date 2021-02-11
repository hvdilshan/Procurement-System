package com.procument.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.procument.model.Notification;
import com.procument.model.Supplier;
import com.procument.model.User;
import com.procument.service.NotificationService;
import com.procument.service.SupplierService;

/**
 * Servlet implementation class supplierAccessServlet
 */
@WebServlet("/supplierAccessServlet")
public class supplierAccessServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public supplierAccessServlet() {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		int type = Integer.parseInt(request.getParameter("type"));
		SupplierService supplierService = SupplierService.getInstance();
		
		//Notifications for create po
		NotificationService notificationService = NotificationService.getInstance();
		//get Notifications
		ArrayList<Notification> notifications = notificationService.getPoNotification("site_manager");
		//set notification settings 
		request.setAttribute("notifications", notifications);
		
		//redirecting to supplier
		if(type == 1) {
			//arraylist fetch
			ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
			//foward to SupplierPanel
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("SupplierPanel.jsp");
			request.setAttribute("suppliers", suppliers);
			request.setAttribute("cNameValidation", "None");
			request.setAttribute("sEmailValidation", "None");
			request.setAttribute("sNameValidation", "None");
			request.setAttribute("sAddressValidation", "None");
			request.setAttribute("sContactValidation", "None");
			requestDispatcher.forward(request, response);
		}else if(type == 2) {
			//session to get user values
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			//arraylist fetch
			ArrayList<Supplier> suppliers = supplierService.getAllSuppliers();
			//get passed values
			String cName = request.getParameter("cName");
			String sName = request.getParameter("contactPerson");
			String sAddress = request.getParameter("sAddress");
			String sEmail = request.getParameter("sEmail");
			String sContact = request.getParameter("contactNumber");
			int grade = Integer.parseInt(request.getParameter("sGrade"));
			//check whether is there empty fields
			if(!cName.trim().isEmpty() && !sName.trim().isEmpty() && !sAddress.trim().isEmpty() && !sEmail.trim().isEmpty() &&
					!sContact.trim().isEmpty()) {
				//insert via supplier service
				boolean success = supplierService.addSupplier(cName, sName, sAddress, sEmail, sContact, grade, user);
				//check success
				if(success) {
					//foward to SupplierPanel
					RequestDispatcher requestDispatcher = request.getRequestDispatcher("SupplierPanel.jsp");
					suppliers = supplierService.getAllSuppliers();
					request.setAttribute("suppliers", suppliers);
					request.setAttribute("cNameValidation", "None");
					request.setAttribute("sEmailValidation", "None");
					request.setAttribute("sNameValidation", "None");
					request.setAttribute("sAddressValidation", "None");
					request.setAttribute("sContactValidation", "None");
					requestDispatcher.forward(request, response);
				}				
			}else {
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("SupplierPanel.jsp");
				
				if(cName.trim().isEmpty()) {
					request.setAttribute("cNameValidation", "Invalid");
				}else {
					request.setAttribute("cName", cName);
					request.setAttribute("cNameValidation", "Valid");
				}
				
				if(sName.trim().isEmpty()) {
					request.setAttribute("sNameValidation", "Invalid");
				}else {
					request.setAttribute("sName", sName);
					request.setAttribute("sNameValidation", "Valid");
				}
				
				if(sAddress.trim().isEmpty()) {
					request.setAttribute("sAddressValidation", "Invalid");
				}else {
					request.setAttribute("sAddress", sAddress);
					request.setAttribute("sAddressValidation", "Valid");
				}
				
				if(sContact.trim().isEmpty()) {
					request.setAttribute("sContactValidation", "Invalid");
				}else {
					request.setAttribute("sContact", sContact);
					request.setAttribute("sContactValidation", "Valid");
				}
				
				request.setAttribute("sEmail", sEmail);
				request.setAttribute("sEmailValidation", "Valid");
				
				request.setAttribute("suppliers", suppliers);
				requestDispatcher.forward(request, response);
			}
			
		}
		
		doGet(request, response);
	}

}
