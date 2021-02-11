<%@page import="com.procument.model.Supplier"%>
<%@page import="com.procument.model.Site"%>
<%@page import="com.procument.model.Item"%>
<%@page import="com.procument.model.PurchaseOrder"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.procument.model.Notification"%>
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
  <%
		User user = new User();
		user = (User)session.getAttribute("user");
		PurchaseOrder purchaseOrder = (PurchaseOrder)session.getAttribute("purchaseOrder");
  %>
</head>
<body 
	onload="<%try{ String promptMsg = (String)request.getAttribute("prompt"); if(promptMsg.equals("deleted")){
			out.print("alert('item deleted');");
		}else if(promptMsg.equals("updated")){
			out.print("alert('po is updated!');");
		}
	}catch(NullPointerException exc){} %>" 

>
	<%
		if(session.getAttribute("logName") == null){
			response.sendRedirect("login.jsp");
		}
	%>
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
		    	<li class="nav-item">
			        <a class="nav-link" onclick="document.getElementById('homeForm').submit();">
			          Back to Home
			        </a>
			        <form action="home" id="homeForm" method="post"></form>
			    </li>
		      <li class="nav-item active">
		        <a class="nav-link" href="#">
		          	Update PO
		          <span class="sr-only">(current)</span>
		        </a>
		      </li>
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
  
  <main class="mt-4">
  		
		<!-- container  -->
		<div class="container-fluid">
			<div class = "row">
				<div class = "col-md-4">
					<!-- Card Dark -->
					<div class="card">
					  <!-- Card content -->
					  <div class="card-body elegant-color white-text rounded-bottom">
					    <!-- Title -->
					    <h4 class="card-title"><% try{out.print("PO For : " + purchaseOrder.getSupplier().getCompany_name());}catch(NullPointerException exc){ out.print("Select a Suplier"); }  %></h4>
					    <hr class="hr-light">
					       <%
						    	try{
						    		ArrayList<Item> addedItems = (ArrayList<Item>)session.getAttribute("addedItems");
						    		int removeID = 0;
							     	
							    	for(Item addItem : addedItems){
							    		int item_id= addItem.getItem_id();
							    		removeID++;
						    %>
								    <div class="row">
									    <div class="col-md-6">
									    	<p class="card-text white-text mb-2 mt-2 ml-4"><% out.print("From " + addItem.getItem_name() + "-" + addItem.getQuantity() + " Items");   %></p>
									    </div>
									    <div class="col-md-6">
									   		<button class="btn btn-sm btn-mdb-color mb-2 ml-4" onclick="document.getElementById('itemRemoveForm<%=removeID%>').submit();">REMOVE</button></p>
									    	<form id="itemRemoveForm<%=removeID%>" action="updatePo" method="post">
										      	<input type = "hidden" name="type" value="4">
										      	<input type = "hidden" name="item_id" value="<%=item_id %>">
										    </form> 	
									    </div>
								    </div>
						     <%
						    		}
						    	}catch(NullPointerException exc){
						    		
						    	}
						     %>
					     <%
				    		int removeID = 0;
					     	
					    	for(Item addItem : purchaseOrder.getItems()){
					    		int item_id= addItem.getItem_id();
					    		removeID++;
					    %>
					    <div class="row">
						    <div class="col-md-6">
						    	<p class="card-text white-text mb-2 mt-2 ml-4"><% out.print("From " + addItem.getItem_name() + "-" + addItem.getQuantity() + " Items");   %></p>
						    </div>
						    <div class="col-md-6">
						   		<%
						   			if(purchaseOrder.getStatus().equals("Accepted")){
						   		%>
						   		
						   		
						   			<button class="btn btn-sm btn-warning mb-2 ml-4" onclick="var result = confirm('Are you sure want to delete?');
									    											if(result){
									    												document.getElementById('itemDeleteForm<%=removeID%>').submit();
								    												}"">REQUEST DELETE</button></p>
							    	<form id="itemDeleteForm<%=removeID%>" action="updatePo" method="post">
								      	<input type = "hidden" name="type" value="1">
								      	<input type="hidden" name="po_id" value="<%=purchaseOrder.getPo_id()%>">
								      	<input type = "hidden" name="item_id" value="<%=item_id %>">
								    </form>
						   		<%
						   			}else{
						   		%>
						   		
							   		<button class="btn btn-sm btn-danger mb-2 ml-4" onclick="var result = confirm('Are you sure want to delete?');
									    											if(result){
									    												document.getElementById('itemDeleteForm<%=removeID%>').submit();
								    												}"">DELETE</button></p>
							    	<form id="itemDeleteForm<%=removeID%>" action="updatePo" method="post">
								      	<input type = "hidden" name="type" value="2">
								      	<input type="hidden" name="po_id" value="<%=purchaseOrder.getPo_id()%>">
								      	<input type = "hidden" name="item_id" value="<%=item_id %>">
								    </form> 							   		
						   		<%
						   			}
						   		%>
						    </div>
					    </div>
					     <%
					    	}
					     %>
					    <hr class="hr-light">
					    <div class="col-md-12">
						    	<p class="card-text white-text mb-2 mt-3 ">Total PO Price : <span class="ml-2">
						    	<% try{long po_price = (long)session.getAttribute("po_price"); out.print(po_price + ".00");}catch(NullPointerException exc){ out.print("0.00"); } %></span></p>
						</div>
					  </div>
					
					</div>
					<!-- Card Dark -->
					
					<form id = "updateForm" class="border border-light p-5" method="post" action="updatePo">
						<input type="hidden" name="type" value="5">
					    <p class="h4 mb-4 text-center">Update PO Form</p>
						
						<label for="textInput">Dilivery Date</label>
					    <input type="date" class="form-control mb-4" value="<%=purchaseOrder.getDilivery_date() %>" name="dilivery_date"  placeholder="Dilivery Date">
						
					    <select class="browser-default custom-select mb-4" name="site_id" id="select" required>
					        <option value="" disabled="" selected="">Select Site</option>
					        <% 
					        	ArrayList<Site> sites = (ArrayList<Site>)session.getAttribute("sites");
					        	for(Site site : sites){
					        %>
					        	<option value="<%out.print(site.getSite_id());%>" <%if(purchaseOrder.getSite_id() == site.getSite_id()){out.print("selected");} %>><%=site.getSite_name()%></option>
				        	<%
					        	}
				        	%>
					    </select>
					    <button class="btn btn-dark-green btn-block my-4" onclick="var result = confirm('Are you sure want to update?'); if(result){document.getElementById('updateForm').submit();}" >Update PO</button>    
					</form>
				</div>
				
				<div class = "col-md-8 ">
					<div class="row">
						<div class="col-md-6">
						  	<form action="" id="searchSupplier" action="purchaseOrder" method="post">
								<div class="input-group md-form form-sm form-1 pl-0">
								  <div class="input-group-prepend">
								    <span class="input-group-text cyan lighten-2" id="basic-text1"><i class="fas fa-search text-white"
								        aria-hidden="true"></i></span>
								  </div>
									<input class="form-control my-0 py-1"
										id="test"
										value="<%  try{ String searchValue = (String)session.getAttribute("searchValue"); if(searchValue != null){out.print(searchValue);}}catch(NullPointerException exc){} %>"
									 	name="search_value" onkeyup="document.getElementById('searchSupplier').submit();" type="text" placeholder="Search" aria-label="Search">
												<input type = "hidden" name="type" value="4">
												<input type = "hidden" name="supplier_id" value="<%=purchaseOrder.getSupplier_id()%>">
								</div>					
							</div>
							<br>
								<div class = "col-md-6 mt-4 mb-4">
									<!-- Small split button group -->
									 <select class="browser-default custom-select col-md-7 mb-4" name="search_option" id="select" required>
								        <option value="" disabled="" selected="">Select Search Option</option>
								        <option value="1" <%  try{ String value = (String)session.getAttribute("value"); if(value.equals("1")){out.print("selected");}}catch(NullPointerException exc){} %>>Item Code</option>
								        <option value="2" <% try{String value = (String)session.getAttribute("value"); if(value.equals("2")){out.print("selected");}}catch(NullPointerException exc){} %>>Item Name</option>
								    </select>
								</div>
							</form>
						</div>
						<!--Section: Table-->
						<section class="mb-2 mt-1">
							<!--Card-->
							<div class="card card-cascade narrower">
							
								 <!--Card content-->
								<div class="card-body">
									<div class="table-responsive">
								    <table class="table text-nowrap">
									    <thead>
									      <tr class = "default-color">
									          <th>Item Code</th>
									          <th>Item Name</th>
									          <th>Unit Price</th>
									          <th>Special</th>
									          <th>Quantity</th>
									          <th>ADD</th>
									          
									      </tr>
									    </thead>
									    <tbody>
									    	<%
									    		ArrayList<Item> items = (ArrayList<Item>)session.getAttribute("items");
									    		int itemCounter = 0;
									    		for(Item item : items){
									    			itemCounter++;
									    		
									    	%>
											        <tr class="table">
											            <td><%=item.getItem_code() %></td>
											            <td><%=item.getItem_name() %></td>
											            <td><%=item.getUnit_price() %></td>
											            <td><%=item.isSpecialApproval() %></td>
											            <form action="updatePo" method="post">
												            <td>
															      	<input type="number" id="qty" name="quantity" value="" class="form-control mb-1" placeholder="Quantity" required>
															      	<input type = "hidden" name="type" value="3">
															      	<input type = "hidden" name="supplier_id" value="<%=purchaseOrder.getSupplier().getSupplier_id()%>">
															      	<input type = "hidden" name="action" value="add">
															      	<input type = "hidden" name="item_id" value="<%=item.getItem_id()%>">
												            </td>
												            <td>
														      	<button type=type="submit" class="btn btn-sm btn-dark-green">ADD</button>
													      	  	
													      	</td>
													    </form>
											            
											        </tr>
										       
									      <%
									      		} 
									      %>
									    </tbody>
									</table>
									
								</div>
								<hr class="my-0">
								
								<!--Bottom Table UI-->
								<div class="d-flex justify-content-between">
									<!--Pagination -->
									<nav class="mt-4">
									    <ul class="pagination pagination-circle pg-blue mb-0">
											<%
												double pageItem = (double)session.getAttribute("pageCount");
												int currentPage = (int)session.getAttribute("pageCurrent");
												
											%>
									        <!--First-->
									        <li class="page-item <%if(currentPage == 1){ out.print("disabled"); }%>">
									            <a class="page-link" onclick="document.getElementById('first').submit();">First</a>
									            <form id="first" action="itemPanel" method="post">
											      	<input type = "hidden" name="type" value="5">
											      	<input type = "hidden" name="page_number" value="<% out.print(1); %>">
											    </form>
									        </li>
									
									        <!--Arrow left-->
									        <li class="page-item <%if(currentPage == 1){ out.print("disabled"); }%>">
									            <a class="page-link" aria-label="Previous" onclick="document.getElementById('previous').submit();">
									                <span aria-hidden="true">&laquo;</span>
									                <span class="sr-only">Previous</span>
									            </a>
									            <form id="previous" action="itemPanel" method="post">
											      	<input type = "hidden" name="type" value="5">
											      	<input type = "hidden" name="page_number" value="<%if(currentPage==1){out.print(1);}else{out.print(currentPage-1);}%>">
											    </form>
									        </li>
											<% 
												for(int pageItems = 1; pageItems <= pageItem; pageItems++){
												
											%>
										        <!--Numbers-->
										        <li class="page-item <% if(currentPage == pageItems){ out.print("active"); } %>">
										            <a class="page-link" onclick="document.getElementById('pageChange<%=pageItems %>').submit();"><%=pageItems %></a>
										        </li>
										        <form id="pageChange<%=pageItems %>" action="itemPanel" method="post">
											      	<input type = "hidden" name="type" value="5">
											      	<input type = "hidden" name="page_number" value="<%=pageItems%>">
											    </form>
									        <%
												}
									        %>
									        <!--Arrow right-->
									        <li class="page-item <%if(currentPage == pageItem){out.print("disabled");}%>">
									            <a class="page-link" aria-label="Next"  onclick="document.getElementById('next').submit();" >
									                <span aria-hidden="true">&raquo;</span>
									                <span class="sr-only">Next</span>
									            </a>
									            
									            <form id="next" action="itemPanel" method="post">
											      	<input type = "hidden" name="type" value="5">
											      	<input type = "hidden" name="page_number" value="<%if(currentPage==pageItem){out.print(currentPage);}else{out.print(currentPage+1);}%>">
											    </form>
									        </li>
									
									        <!--First-->
									        <li class="page-item <%if(currentPage == pageItem){out.print("disabled");}%>">
									            <a class="page-link" onclick="document.getElementById('last').submit();">Last</a>
									            <form id="last" action="itemPanel" method="post">
											      	<input type = "hidden" name="type" value="5">
											      	<input type = "hidden" name="page_number" value="<%=(int)pageItem%>">
											    </form>
									        </li>
									
									    </ul>
									</nav>
									<!--/Pagination -->
								
								</div>
								<!--Bottom Table UI-->
				
								</div>
								<!--/.Card content-->
							
							</div>
							<!--/.Card-->			
						  </section>
						  <!--Section: Table-->
						
						
				</div>
				
			</div>
		</div>
		<!-- /.container -->
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
</body>
</html>