<%@page import="com.procument.model.Notification"%>
<%@page import="com.procument.model.PurchaseOrder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.procument.service.UserService"%>
<%@page import="com.procument.dao.DBConnectionUtil"%>
<%@page import="com.procument.model.User"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
  <!-- Font Awesome -->
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
  <!-- Bootstrap core CSS -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
  <!-- Material Design Bootstrap -->
  <link href="css/mdb.min.css" rel="stylesheet">
  <!-- Your custom styles (optional) -->
  <link href="css/style.css" rel="stylesheet">
  <%
		User user = new User();
		user = (User)session.getAttribute("user");
		ArrayList<PurchaseOrder> purchaseOrders = (ArrayList<PurchaseOrder>)session.getAttribute("purchaseOrders");
		ArrayList<Notification> notifications = (ArrayList<Notification>)request.getAttribute("notifications");
		long totalPoCost = (long)session.getAttribute("totalPoCost");
		long poAverage = (long)session.getAttribute("poAverage");
  %>
</head>
<body>
	<%
		if(session.getAttribute("logName") == null){
			response.sendRedirect("login.jsp");
		}
	%>
	<th scope="row">
        <input class="form-check-input" type="checkbox" id="checkbox1">
        <label for="checkbox1" class="label-table form-check-label"></label>
    </th>
    
    <div>
					        <button type="button" class="btn btn-outline-white btn-rounded btn-sm px-2">
					            <i class="fas fa-th-large mt-0"></i>
					        </button>
					        <button type="button" class="btn btn-outline-white btn-rounded btn-sm px-2">
					            <i class="fas fa-columns mt-0"></i>
					        </button>
					    </div>
					
					    <a href="" class="white-text mx-3">Purchase Orders</a>
					
					    <div>
					        <button type="button" class="btn btn-outline-white btn-rounded btn-sm px-2">
					            <i class="fas fa-pencil-alt mt-0"></i>
					        </button>
					        <button type="button" class="btn btn-outline-white btn-rounded btn-sm px-2" data-toggle="modal" data-target="#modalConfirmDelete">
							    <i class="fas fa-times mt-0"></i>
							</button>
					        <button type="button" class="btn btn-outline-white btn-rounded btn-sm px-2">
					            <i class="fas fa-info-circle mt-0"></i>
					        </button>
					    </div>
    
	<!--Navbar -->
	<nav class="mb-1 navbar navbar-expand-lg navbar-dark default-color">
	  <a class="navbar-brand" href="#">
	  	<img src="img\logo2.png" height="30" alt="mdb logo">
	  </a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent-333"
	    aria-controls="navbarSupportedContent-333" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	  <div class="collapse navbar-collapse" id="navbarSupportedContent-333">
	    <ul class="navbar-nav mr-auto">
	      <li class="nav-item active">
	        <a class="nav-link" href="#">
	          Home
	          <span class="sr-only">(current)</span>
	        </a>
	      </li>
	      <% 
	      	if(user.getPrivilage()==1){
	      %>
		      <li class="nav-item dropdown">
		        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		          PO Options
		        </a>
		        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
		          <a class="dropdown-item" onclick = "document.getElementById('purchaseOrderForm').submit();" href="#">Create Purchase Order</a>
		          <a class="dropdown-item" href="#">PO Status Review</a>
		          <div class="dropdown-divider"></div>
		          <a class="dropdown-item" href="#">Something else here</a>
		        </div>
		      </li>
		      
		      <form id="purchaseOrderForm" action="purchaseOrder" method="post">
		      	<input type="hidden" name="type" value="1">
		      </form>
	      <%
	      	}
	      %>
	      
	      <% 
	      	if(user.getPrivilage()==2){
	      %>
		      <li class="nav-item">
		        <a class="nav-link" onclick="document.getElementById('supplierForm').submit();">Supplier Management</a>
		      </li>
		      <form id="supplierForm" action="supplierPanel" method="post">
		      	<input type = "hidden" name="type" value="1">
		      </form>
	      <%
	      	}
	      %>
	      
	      <% 
	      	if(user.getPrivilage()==2){
	      %>
		      <li class="nav-item">
		        <a class="nav-link" onclick="document.getElementById('itemForm').submit();">Item Management</a>
		      </li>
		      <form id="itemForm" action="itemPanel" method="post">
		      	<input type = "hidden" name="type" value="1">
		      </form>
	      <%
	      	}
	      %>  
	      
	      <%
	      	if(user.getPrivilage()==1){
	      %>
		      <li class="nav-item dropdown">
			        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			          Notifications <span class="badge badge-danger ml-2"><%=notifications.size() %></span>
			        </a>
			        <div class="dropdown-menu alert-dark" aria-labelledby="navbarDropdown">
			        	<%
			        		
			        			for(Notification notification : notifications){
			        	%>
			          				<a class="dropdown-item alert-info" href="#"><span class="badge indigo ml-2">Approval</span> for PO id : <%=notification.getNotif_rel_id() %></a>
			          	<%
			        			}	
			          	%>
			        </div>
		      </li>
          <%
	          }else if(user.getPrivilage()==2){
	      
	      %>
	      		<li class="nav-item dropdown">
			        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
			          Notifications <span class="badge badge-danger ml-2"><%=notifications.size() %></span>
			        </a>
			        <div class="dropdown-menu alert-dark" aria-labelledby="navbarDropdown">
			        	<%
			        		
			        			for(Notification notification : notifications){
			        	%>
			          				<a class="dropdown-item alert-info" href="#"><span class="badge badge-info ml-2">PO REQUEST</span> for PO id : <%=notification.getNotif_rel_id() %></a>
			          	<%
			        			}	
			          	%>
			        </div>
		         </li>
		      
		      <%
	          }
		      %>
	    </ul>
	    <ul class="navbar-nav ml-auto nav-flex-icons">
	      
	      
	        <a class="nav-link dropdown-toggle" id="navbarDropdownMenuLink-333" data-toggle="dropdown"
	          aria-haspopup="true" aria-expanded="false">
	          <span class="caret">${logName}</span>
	        </a>
	        <div class="dropdown-menu dropdown-menu-right dropdown-default"
	          aria-labelledby="navbarDropdownMenuLink-333">
	          <a class="dropdown-item" href="#">Profile</a>
	          <a class="dropdown-item" onclick="document.getElementById('logoutForm').submit();">Logout</a>
	          <form id="logoutForm" action="logout" method="post"></form>
	        </div>
	      </li>
	    </ul>
	  </div>
	</nav>
	<!--/.Navbar -->
	

  <!-- SCRIPTS -->
  <!-- JQuery -->
  <script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
  <!-- Bootstrap tooltips -->
  <script type="text/javascript" src="js/popper.min.js"></script>
  <!-- Bootstrap core JavaScript -->
  <script type="text/javascript" src="js/bootstrap.min.js"></script>
  <!-- MDB core JavaScript -->
  <script type="text/javascript" src="js/mdb.min.js"></script>
</body>
</html>