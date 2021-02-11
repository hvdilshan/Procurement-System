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
import com.procument.model.PurchaseOrder;
import com.procument.service.NotificationService;
import com.procument.service.PurchaseOrderService;

/**
 * Servlet implementation class homeServelet
 */
@WebServlet("/homeServelet")
public class homeServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public homeServelet() {
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
		
		//purchaseOrderService instance
		PurchaseOrderService purchaseOrderService = PurchaseOrderService.getInstance();
		
		//Purchase order list
		ArrayList<PurchaseOrder> purchaseOrders;
		try {
			purchaseOrders = purchaseOrderService.getAllPurchaseOrders();
			long totalPoCost = purchaseOrderService.getTotalPoCost();
			int totalPoCount = purchaseOrderService.getTotalPoCount();
			long poAverage = purchaseOrderService.calculateAveragePoCost(totalPoCost, totalPoCount);
			
			HttpSession session = request.getSession();
			
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
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("AdminPanel.jsp");
			requestDispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		
		
//		doGet(request, response);
	}

}
