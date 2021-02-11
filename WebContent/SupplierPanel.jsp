<%@page import="com.procument.model.Notification"%>
<%@page import="java.awt.print.Printable"%>
<%@page import="com.procument.model.Supplier"%>
<%@page import="java.util.ArrayList"%>

<%@page import="com.procument.model.User"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Panel</title>
<!-- Font Awesome -->
  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.8.2/css/all.css">
  <!-- Bootstrap core CSS -->
  <link href="css/bootstrap.min.css" rel="stylesheet">
  
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
  <!-- Material Design Bootstrap -->
  <link href="css/mdb.min.css" rel="stylesheet">
  <!-- Your custom styles (optional) -->
  <link href="css/style.css" rel="stylesheet">
</head>
	<%
		User user = new User();
		user = (User)session.getAttribute("user");
		
		ArrayList<Supplier> suppliers = (ArrayList<Supplier>)request.getAttribute("suppliers");
		ArrayList<Notification> notifications = (ArrayList<Notification>)request.getAttribute("notifications");
	%>
<body>
	
	<%
		if(session.getAttribute("logName") == null){
			response.sendRedirect("login.jsp");
		}
	%>

	<!--Navbar -->
	<nav class="mb-1 navbar navbar-expand-lg navbar-dark default-color">
	  <a class="navbar-brand" href="#"><img src="img\logo2.png" height="30" alt="mdb logo"></a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent-333"
	    aria-controls="navbarSupportedContent-333" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	  <div class="collapse navbar-collapse" id="navbarSupportedContent-333">
	    <ul class="navbar-nav mr-auto">
	      <li class="nav-item">
	        <a class="nav-link" onclick="document.getElementById('homeForm').submit();">
	          Home
	        </a>
	        <form action="home" id="homeForm" method="post"></form>
	      </li>
	      <% 
	      	if(user.getPrivilage()==1){
	      %>
		      <li class="nav-item">
		        <a class="nav-link" href="#">Create New PO</a>
		      </li>
	      <%
	      	}
	      %>
	      
	      <% 
	      	if(user.getPrivilage()==2){
	      %>
		      <li class="nav-item active">
		        <a class="nav-link" href="#">
		        	Supplier Management
		        	<span class="sr-only">(current)</span>
		        </a>
		      </li>
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
	<br>
	<!--Main-->
	<main>
		<div class="container-fluid">
			<div class="row">
				<div class = "col-md-6">
					
					<form class="border border-light p-5" method="post" action="supplierPanel">
						<input type="hidden" name="type" value="2">
					    <p class="h4 mb-4 text-center">Register Supplier</p>
						
					    <label for="textInput">Company Name</label>
					    <input type="text" id="textInput" name="cName" placeholder="Company Name" class="form-control mb-4  <% if(request.getAttribute("cNameValidation").equals("Valid")){ out.print("is-valid"); }else if(request.getAttribute("cNameValidation").equals("Invalid")){ out.print("is-invalid"); } %>" value = "<% if(request.getAttribute("cNameValidation").equals("Valid")){out.print(request.getAttribute("cName"));} %>">
					
					    <label for="textInput">Contact Person Name</label>
					    <input type="text" id="textInput" class="form-control mb-4 <% if(request.getAttribute("sNameValidation").equals("Valid")){ out.print("is-valid"); }else if(request.getAttribute("sNameValidation").equals("Invalid")){ out.print("is-invalid"); } %>" name="contactPerson" placeholder="Contact Person Name" value = "<% if(request.getAttribute("sNameValidation").equals("Valid")){out.print(request.getAttribute("sName"));} %>">
					
					    <label for="textInput">Contact Number</label>
					    <input type="text" id="textInput" class="form-control mb-4 <% if(request.getAttribute("sContactValidation").equals("Valid")){ out.print("is-valid"); }else if(request.getAttribute("sContactValidation").equals("Invalid")){ out.print("is-invalid"); } %>" name="contactNumber"  placeholder="Contact Number"  value = "<% if(request.getAttribute("sContactValidation").equals("Valid")){out.print(request.getAttribute("sContact"));} %>">
					
					    <label for="textarea">Supplier Address</label>
					    <textarea id="textarea" class="form-control mb-4 <% if(request.getAttribute("sAddressValidation").equals("Valid")){ out.print("is-valid"); }else if(request.getAttribute("sAddressValidation").equals("Invalid")){ out.print("is-invalid"); } %>" name="sAddress" placeholder="Supplier Address" value = "<% if(request.getAttribute("sAddressValidation").equals("Valid")){out.print(request.getAttribute("sAddress"));} %>"></textarea>
					    
					    <label for="emailInput" class="active">Email Address</label>
    					<input type="email" id="emailInput" class="form-control mb-4 <% if(request.getAttribute("sEmailValidation").equals("Valid")){ out.print("is-valid"); }%>" name="sEmail" placeholder="Email Address" value = "<% if(request.getAttribute("sEmailValidation").equals("Valid")){out.print(request.getAttribute("sEmail"));} %>" required>
					
					    <select class="browser-default custom-select mb-4" name="sGrade" id="select" required>
					        <option value="" disabled="" selected="">Supplier Grade</option>
					        <option value="1">1</option>
					        <option value="2">2</option>
					        <option value="3">3</option>
					        <option value="4">4</option>
					        <option value="5">5</option>
					    </select>
					    <button class="btn btn-default btn-block my-4" type="submit">Register</button>
					</form>			
				</div>
				<div class = "col-md-6 ">
					<div class="row">
						<div class="col-md-6">
							<div class="input-group md-form form-sm form-1 pl-0">
							  <div class="input-group-prepend">
							    <span class="input-group-text cyan lighten-2" id="basic-text1"><i class="fas fa-search text-white"
							        aria-hidden="true"></i></span>
							  </div>
							  <input class="form-control my-0 py-1" type="text" placeholder="Search" aria-label="Search">
							</div>					
						</div>
						<br>
						<div class = "col-md-6 mt-4 mb-4">
							<!-- Small split button group -->
							<div class="btn-group">
							  <button type="button" class="btn btn-default btn-sm">Choose Search Option</button>
							  <button type="button" class="btn btn-default btn-sm dropdown-toggle px-3" data-toggle="dropdown"
							    aria-haspopup="true" aria-expanded="false">
							    <span class="sr-only">Toggle Dropdown</span>
							  </button>
							  <div class="dropdown-menu">
							    <a class="dropdown-item" href="#">By Supplier Id</a>
							    <a class="dropdown-item" href="#">By Supplier Company</a>
							    <a class="dropdown-item" href="#">By Supplier Name</a>
							    <a class="dropdown-item" href="#">By Grade</a>
							    <div class="dropdown-divider"></div>
							    <a class="dropdown-item" href="#">By Added User</a>
							  </div>
							</div>
						</div>
					</div>
					<!--Table-->
					<table id="tablePreview" class="table table-responsive">
					<!--Table head-->
					  <thead>
					    <tr class="text-center default-color text-white">
					      <th>Supplier Id</th>
					      <th>Company Name</th>
					      <th>Contact Person Name</th>
					      <th>Supplier Contact</th>
					      <th>Supplier Address</th>
					      <th>Supplier Email</th>
					      <th>Grade</th>
					      <th>Added By</th>
					    </tr>
					  </thead>
					  <!--Table head-->
					  <!--Table body-->
					  <tbody>
					  <%
					  	int counter = 0;
					  	for(Supplier supplier : suppliers){
					  	
					  		counter++;
					  %>
						    <tr>
						      <td><%=supplier.getSupplier_id() %></td>
						      <td><%=supplier.getCompany_name() %></td>
						      <td><%=supplier.getSupplier_name() %></td>
						      <td><%=supplier.getSupplier_contact() %></td>						      
						      <td><%=supplier.getSupplier_address() %></td>
						      <td><%=supplier.getSupplier_email() %></td>
						      <td><%=supplier.getGrade() %></td>
						      <td><%=supplier.getAdded_by() %></td>
						    </tr>
					    <%
					    		if(counter == 10){
					    			break;
					    		}
					  		}
					    %>
					  </tbody>
					  <!--Table body-->
					</table>
					<!--Table-->	
					<!--Pagination -->
						<nav class="mt-4">
						    <ul class="pagination pagination-circle pg-blue mb-0">
						
						        <!--First-->
						        <li class="page-item disabled">
						            <a class="page-link">First</a>
						        </li>
						
						        <!--Arrow left-->
						        <li class="page-item disabled">
						            <a class="page-link" aria-label="Previous">
						                <span aria-hidden="true">&laquo;</span>
						                <span class="sr-only">Previous</span>
						            </a>
						        </li>
								<%
									double pageCount = suppliers.size() / 10.0;
									pageCount = Math.ceil(pageCount);
									
									for(int p = 1; p <= pageCount; p++){
								%>
							        <!--Numbers-->
							        <li class="page-item active">
							            <a class="page-link"><%= p %></a>
							        </li>
						        <%
						        	}
						        %>
						        
						        <!--Arrow right-->
						        <li class="page-item">
						            <a class="page-link" aria-label="Next">
						                <span aria-hidden="true">&raquo;</span>
						                <span class="sr-only">Next</span>
						            </a>
						        </li>
						
						        <!--First-->
						        <li class="page-item">
						            <a class="page-link">Last</a>
						        </li>
						
						    </ul>
						</nav>
						<!--/Pagination -->
				</div>
			</div>
		</div>
	</main>
	
	<!-- Footer -->
	<footer class="page-footer font-small default-color pt-4 mt-4">
	
	  <!-- Footer Links -->
	  <div class="container text-center text-md-left">
	
	    <!-- Grid row -->
	    <div class="row">
	
	      <!-- Grid column -->
	      <div class="col-md-6 mt-md-0 mt-3">
	
	        <!-- Content -->
	        <h5 class="text-uppercase">PROCUMENT SYSTEM</h5>
	        <p>Organize and Manage Easy!.</p>
	
	      </div>
	      <!-- Grid column -->
	
	      <hr class="clearfix w-100 d-md-none pb-3">
	
	      <!-- Grid column -->
	      <div class="col-md-3 mb-md-0 mb-3">
	
	          <!-- Links -->
	          <h5 class="text-uppercase">Links</h5>
	
	          <ul class="list-unstyled">
	            <li>
	              <a href="#!">Link 1</a>
	            </li>
	            <li>
	              <a href="#!">Link 2</a>
	            </li>
	            <li>
	              <a href="#!">Link 3</a>
	            </li>
	            <li>
	              <a href="#!">Link 4</a>
	            </li>
	          </ul>
	
	        </div>
	        <!-- Grid column -->
	
	        <!-- Grid column -->
	        <div class="col-md-3 mb-md-0 mb-3">
	
	          <!-- Links -->
	          <h5 class="text-uppercase">Links</h5>
	
	          <ul class="list-unstyled">
	            <li>
	              <a href="#!">Link 1</a>
	            </li>
	            <li>
	              <a href="#!">Link 2</a>
	            </li>
	            <li>
	              <a href="#!">Link 3</a>
	            </li>
	            <li>
	              <a href="#!">Link 4</a>
	            </li>
	          </ul>
	
	        </div>
	        <!-- Grid column -->
	
	    </div>
	    <!-- Grid row -->
	
	  </div>
	  <!-- Footer Links -->
	
	  <!-- Copyright -->
	  <div class="footer-copyright text-center py-3">© 2019 Copyright:
	    <a href="#"> ProcumentSystem.com</a>
	  </div>
	  <!-- Copyright -->
	
	</footer>

	 <!-- SCRIPTS -->
	 <!-- JQuery -->
	 <script type="text/javascript" src="js/jquery-3.4.1.min.js"></script>
	 <!-- Bootstrap tooltips -->
	 <script type="text/javascript" src="js/popper.min.js"></script>
	 <!-- Bootstrap core JavaScript -->
	 <script type="text/javascript" src="js/bootstrap.min.js"></script>
	 <!-- MDB core JavaScript -->
	 <script type="text/javascript" src="js/mdb.min.js"></script>
	 <script type="text/javascript">
	 	// Material Select Initialization
	$(document).ready(function () {
	$('.mdb-select').material_select();
	});
	 </script>
	 
</body>
</html>