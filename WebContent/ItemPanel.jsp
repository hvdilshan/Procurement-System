<%@page import="com.procument.model.Notification"%>
<%@page import="com.procument.model.Item"%>
<%@page import="com.procument.model.User"%>
<%@page import="com.procument.model.Supplier"%>
<%@page import="java.util.ArrayList"%>
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
		
		ArrayList<Supplier> suppliers = (ArrayList<Supplier>)request.getAttribute("suppliers");
		ArrayList<Item> items = (ArrayList<Item>)request.getAttribute("items");
		ArrayList<Supplier> addedSuppliers = (ArrayList<Supplier>)session.getAttribute("addedSuppliers");
		ArrayList<Notification> notifications = (ArrayList<Notification>)request.getAttribute("notifications");
		ArrayList<Supplier> deletedSuppliers = new ArrayList<Supplier>();
		try{
			deletedSuppliers = (ArrayList<Supplier>)session.getAttribute("deletedSuppliers");
			
		}catch(NullPointerException exc){
			
		}
		
  %>
</head>
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
	
	<!--/.Main -->
	<main class="mt-4">
		<div class="container-fluid">
			<div class="row">
				<div class = "col-md-4">
					<!-- Card Dark -->
					<div class="card">
					  <!-- Card content -->
					  <div class="card-body elegant-color white-text rounded-bottom">
					    <!-- Title -->
					    <h4 class="card-title">Suppliers</h4>
					    <hr class="hr-light">
					    <!-- Text -->
					    <%
				    		int removeID = 0;
					    	for(Supplier addSupplier : addedSuppliers){
					    		String supId= addSupplier.getSupplier_id();
					    		removeID++;
					    %>
					    <div class="row">
						    <div class="col-md-6">
						    	<p class="card-text white-text mb-2 mt-2 ml-4"><%=addSupplier.getCompany_name() %>
						    </div>
						    <div class="col-md-6">
						    	<button class="btn btn-sm btn-mdb-color mb-2 ml-4" onclick="document.getElementById('itemRemoveForm<%=removeID%>').submit();">REMOVE</button></p>
						    	<form id="itemRemoveForm<%=removeID%>" action="itemPanel" method="post">
							      	<input type = "hidden" name="type" value="3">
							      	<input type = "hidden" name="supplier_id" value="<%=supId %>">
							    </form>
						    </div>
					    </div>
					    	 
					    <%
					    	}
					    %>
					    
					    <%
				    		try{
						    	int updateID = 0;
						    	for(Supplier delSupplier : deletedSuppliers){
						    		String supId= delSupplier.getSupplier_id();
						    		updateID++;
					    %>
								    <div class="row">
									    <div class="col-md-6">
									    	<p class="card-text white-text mb-2 mt-2 ml-4"><%=delSupplier.getCompany_name() %>
									    </div>
									    <div class="col-md-6">
									    	<button class="btn btn-sm btn-danger mb-2 ml-4 <% if(deletedSuppliers.size() < 2){out.print("disabled");} %>" onclick="
									    											var result = confirm('Are you sure want to delete?');
									    											if(result){
									    												document.getElementById('itemDeleteSupplierForm<%=updateID%>').submit();
								    												}" >DELETE</button></p>
									    	<form id="itemDeleteSupplierForm<%=updateID%>" action="itemPanel" method="post">
										      	<input type = "hidden" name="type" value="8">
										      	<input type = "hidden" name="action" value="supplierUpdate">
										      	<input type="hidden" name="item_id" value="<% int item_id = (int)session.getAttribute("item_id"); out.print(item_id); %>">
										      	<input type = "hidden" name="supplier_id" value="<%=supId %>">
										    </form>
									    </div>
								    </div>
					    	 
					    <%
					    		}
				    		}catch(NullPointerException exc){
				    			
				    		}
					    %>
					  </div>
					
					</div>
					<!-- Card Dark -->
					<%
						try{
							if(!deletedSuppliers.isEmpty()){
					%>	
						<button class="btn btn-indigo btn-block my-4" onclick="document.getElementById('itemForm').submit();" >ADD NEW ITEM</button>
						<form id="itemForm" action="itemPanel" method="post">
					      	<input type = "hidden" name="type" value="1">
					    </form>
						<button class="btn btn-danger btn-block my-4" onclick=
							"var result = confirm('Are you sure want to delete item?');if(result){document.getElementById('deleteItemForm').submit();}">DELETE ITEM</button>
						<form id="deleteItemForm" action="itemPanel" method="post">
					      	<input type = "hidden" name="type" value="9">
					      	<input type="hidden" name="item_id" value="<% int item_id = (int)session.getAttribute("item_id"); out.print(item_id); %>">
					    </form>
					<%	
							} 
						}catch(NullPointerException exc){}
					%>
					<form class="border border-light p-5" method="post" action="itemPanel">
						<%
							try{
								String sessionUnit = (String)session.getAttribute("unit");
								if(sessionUnit.isEmpty()){
								
						%>
					    
					    <%
								}else{
								
						%>
									<input type="hidden" name="type" value="7">
									<input type="hidden" name="item_id" value="<% int item_id = (int)session.getAttribute("item_id"); out.print(item_id); %>">
								    <p class="h4 mb-4 text-center">Update Item</p>
						<%
								}
							}catch(NullPointerException exc){
						%>	
						    <p class="h4 mb-4 text-center">Add Item</p>
							<input type="hidden" name="type" value="4">
						<%		
							}
					    %>
			
					    <label for="textInput">Item Name</label>
					    <input type="text" id="textInput" name="iName" value="<% try{String updateIName = (String)session.getAttribute("updateIName"); if(!updateIName.isEmpty()){out.print(updateIName);}}catch(NullPointerException exc){} %>" placeholder="Item Name" class="form-control mb-4" required>
					
					    <label for="textInput">Unit Price</label>
					    <input type="text" id="textInput" name="uPrice" value="<% try{String updateUPrice = Double.toString((double)session.getAttribute("updateUPrice")); if(!updateUPrice.isEmpty()){out.print(updateUPrice);} }catch(NullPointerException exc){} %>" placeholder="Unit Price" class="form-control mb-4" required>
					    
					    <label for="textInput">Unit</label>
					    <select class="browser-default custom-select mb-4" name="unit" id="select" required>
					        <option value="" disabled="" selected="">Select Unit</option>
					        <option value="1" <% try{String unit = (String)session.getAttribute("unit"); if((!unit.isEmpty()) && (unit.equals("Meters"))){out.print("selected");} }catch(NullPointerException exc){}%>>Meters</option>
					        <option value="2" <% try{String unit = (String)session.getAttribute("unit"); if((!unit.isEmpty()) && (unit.equals("Kg"))){out.print("selected");} }catch(NullPointerException exc){}%>>Kg</option>
					        <option value="3" <% try{String unit = (String)session.getAttribute("unit"); if((!unit.isEmpty()) && (unit.equals("Piece"))){out.print("selected");} }catch(NullPointerException exc){}%>>Piece</option>
					        <option value="4" <% try{String unit = (String)session.getAttribute("unit"); if((!unit.isEmpty()) && (unit.equals("Liter"))){out.print("selected");} }catch(NullPointerException exc){}%>  >Liter</option>
					        <option value="5" <% try{String unit = (String)session.getAttribute("unit"); if((!unit.isEmpty()) && (unit.equals("Cube"))){out.print("selected");} }catch(NullPointerException exc){}%> >Cube</option>
				        </select>

					    <label for="textInput">Special Approval</label>
					    <select class="browser-default custom-select mb-4" name="specialApproval" id="select" required>
					        <option value="" disabled="" selected="">Select Choice</option>
					        <option value="1" <% try{String specialApproval = (String)session.getAttribute("specialApproval"); if((!specialApproval.isEmpty()) && (specialApproval.equals("0"))){out.print("selected");} }catch(NullPointerException exc){}%>>No</option>
					        <option value="2" <% try{String specialApproval = (String)session.getAttribute("specialApproval"); if((!specialApproval.isEmpty()) && (specialApproval.equals("1"))){out.print("selected");} }catch(NullPointerException exc){}%>>Yes</option>
				        </select>
					    <button class="btn <%try{if(!deletedSuppliers.isEmpty()){out.print("btn-dark-green");} }catch(NullPointerException exc){out.print("btn-default");}%> btn-block my-4" type="submit"><%try{if(!deletedSuppliers.isEmpty()){out.print("UPDATE ITEM");} }catch(NullPointerException exc){out.print("ADD ITEM");}%></button>
					</form>
						
				</div>
				<div class = "col-md-8 ">
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
							    
							  </div>
							</div>
						</div>
					</div>
					<!--Table-->
					<table id="tablePreview" class="table table-responsive">
					<!--Table head-->
					  <thead>
					    <tr class="text-center default-color text-white">
					      <th class="th-lg">Supplier Id</th>
					      <th class="th-lg">Company Name</th>
					      <th class="th-lg">Contact Person Name</th>
					      <th class="th-lg">Supplier Contact</th>
					      <th class="th-lg">Supplier Address</th>
					      <th class="th-lg">Supplier Email</th>
					      <th class="th-lg">Grade</th>
					      <th class="th-lg">Add</th>
					    </tr>
					  </thead>
					  <!--Table head-->
					  <!--Table body-->
					  <tbody>
					  <%
					  	int counter = 0;
					  	for(Supplier supplier : suppliers){
					  		String supId= supplier.getSupplier_id();
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
						      <td>
						      	<button class="btn btn-sm btn-cyan" name="add<%=counter %>" onclick="document.getElementById('itemForm<%=counter%>').submit();">ADD</button>
					      	  	<form id="itemForm<%=counter%>" action="itemPanel" method="post">
							      	<input type = "hidden" name="type" value="2">
							      	<input type = "hidden" name="supplier_id" value="<%=supId %>">
							    </form>
					      	  </td>
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
		<!--Section: Table-->
		<section class="mb-5 mt-5">
			<!--Card-->
			<div class="card card-cascade narrower">
			
				<!--Card header-->
				<div class="view view-cascade py-3 gradient-card-header info-color-dark mx-4 d-flex justify-content-between align-items-center">					
				    <a href="" class="white-text mx-3">Items</a>
				 </div>
				  <!--/Card header-->
			
			    <!--Card content-->
				<div class="card-body">
					<div class="table-responsive">
				    <table class="table text-nowrap">
					    <thead>
					      <tr class = "default-color">
					          <th>Item Code</th>
					          <th>Item Name</th>
					          <th>Unit Price</th>
					          <th>Special Approval</th>
					          <th>Supplier Company</th>
					          <th>Update</th>
					      </tr>
					    </thead>
					    <tbody>
					    	<%
					    		int itemCounter = 0;
					    		for(Item item : items){
					    	%>
					    		<%
					    			for(Supplier supplier : item.getSuppliers()){
					    				itemCounter++;
					    		%>
							        <tr class="table">
							            <td><%=item.getItem_code() %></td>
							            <td><%=item.getItem_name() %></td>
							            <td><%=item.getUnit_price() %></td>
							            <td><%=item.isSpecialApproval() %></td>
							            <td><%=supplier.getCompany_name() %></td>
							            <td>
									      	<button class="btn btn-sm btn-dark-green" name="update" onclick="document.getElementById('itemUpdateForm<%=itemCounter %>').submit();">UPDATE</button>
								      	  	<form id="itemUpdateForm<%=itemCounter %>" action="itemPanel" method="post">
										      	<input type = "hidden" name="type" value="6">
										      	<input type = "hidden" name="action" value="update">
										      	<input type = "hidden" name="item_id" value="<%=item.getItem_id()%>">
										    </form>
								      	</td>
							           
							        </tr>
						        <%
					    			}
						        %>
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
								double pageItem = (double)request.getAttribute("pageCount");
								int currentPage = (int)request.getAttribute("pageCurrent");
								
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
	</main>
	<!--/.Main -->
  
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