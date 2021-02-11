package com.procument.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.procument.model.User;
import com.procument.service.UserService;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
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
		String uname = request.getParameter("userName");
		String pass = request.getParameter("password");
		
		
		if(!uname.trim().isEmpty() && !pass.trim().isEmpty()) {
			UserService employeeService = new UserService();
			if(employeeService.login(uname, pass)) {
				//get employee
				User user = employeeService.getUser(uname, pass);
				//get http session
				HttpSession session = request.getSession();
				//set session
				session.setAttribute("user", user);
				session.setAttribute("logName", user.getFirst_name() + " " +  user.getLast_name());
				
				
				
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("home");
				requestDispatcher.forward(request, response);
				
				System.out.println("Login Success!");
				
			}else {
				String errorMessage = "Login Failed!";
				request.setAttribute("errorMessage", errorMessage);
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
				requestDispatcher.forward(request, response);
			}
			
		}else {
			String errorMessage = "Cannot be empty!";
			request.setAttribute("errorMessage", errorMessage);
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("login.jsp");
			requestDispatcher.forward(request, response);
		}
		
		
		doGet(request, response);
	}

}
