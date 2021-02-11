<%@page import="com.procument.model.Notification"%>
<%@page import="com.procument.model.Site"%>
<%@page import="com.procument.model.Item"%>
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
	
	try{
		
		
	}catch(NullPointerException exc){
		
	}

	double price = 0.00;
	//take price
	try{
		price = (double)request.getAttribute("price");
		
	}catch(NullPointerException exc){
		price = 0.00;
	}
	
	Supplier selectedSupplier = new Supplier();
	ArrayList<Item> addedItems = (ArrayList<Item>)session.getAttribute("addedItems");
	ArrayList<Notification> notifications = (ArrayList<Notification>)request.getAttribute("notifications");
	
	try{
		selectedSupplier = (Supplier)session.getAttribute("supplier");
	}catch(NullPointerException exc){
		
	}
	
%>

<body
	onload="<%try{ String promptMsg = (String)request.getAttribute("prompt");
		if(promptMsg.equals("created")){
			out.print("alert('PO is successfully Created!');");
		}else if(promptMsg.equals("draft")){
			out.print("alert('PO is successfully Saved as a draft!');");
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
		      <li class="nav-item dropdown active">
		        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
		          PO Options
		          <span class="sr-only">(current)</span>
		        </a>
		        <div class="dropdown-menu" aria-labelledby="navbarDropdown">
		          <a class="dropdown-item" onclick = "document.getElementById('purchaseOrderForm').submit();" href="#">Create Purchase Order</a>
		          <a class="dropdown-item" href="#">PO Status Review</a>
		          <div class="dropdown-divider"></div>
		          <a class="dropdown-item" href="#">Edit Draft POs</a>
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
	
<div class="container mt-3">
	<div class="row">
		<div class="col-md-12">
			<%
				try{
					String errorMessage = (String)request.getAttribute("errorMessage");	
				
					if(errorMessage.equals("Please Add Items!") || errorMessage.equals("Please Select a supplier!")){	
			%>
						<div class="alert alert-danger alert-dismissible fade show" role="alert">
						  <strong><%=errorMessage %></strong> 
						  <button type="button" class="close" data-dismiss="alert" aria-label="Close">
						    <span aria-hidden="true">&times;</span>
						  </button>
						</div>
			<%
					}
				}catch(NullPointerException exc){
					
				}
			%>
		</div>
	</div>
</div>

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
					    <h4 class="card-title"><% try{out.print("PO For : " + selectedSupplier.getCompany_name());}catch(NullPointerException exc){ out.print("Select a Suplier"); }  %></h4>
					    <hr class="hr-light">
					    <!-- Text -->
					     <%
				    		int removeID = 0;
					     	if(addedItems.isEmpty()){
					     		out.print("No item is Selected");
					     	}
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
						    	<form id="itemRemoveForm<%=removeID%>" action="purchaseOrder" method="post">
							      	<input type = "hidden" name="type" value="5">
							      	<input type = "hidden" name="item_id" value="<%=item_id %>">
							    </form> 	
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
					
					<form class="border border-light p-5" method="post" action="purchaseOrder">
						<input type="hidden" id="hiddenId" name="type" value="6">
					    <p class="h4 mb-4 text-center">Create PO Form</p>
						
						<label for="textInput">Dilivery Date</label>
					    <input type="date" class="form-control mb-4" name="dilivery_date"  placeholder="Dilivery Date">
						
					    <select class="browser-default custom-select mb-4" name="site_id" id="select" required>
					        <option value="" disabled="" selected="">Select Site</option>
					        <% 
					        	ArrayList<Site> sites = (ArrayList<Site>)session.getAttribute("sites");
					        	for(Site site : sites){
					        		out.print("site : " + site.getSite_id());
					        %>
					        	<option value="<%out.print(site.getSite_id());%>"><%=site.getSite_name()%></option>
				        	<%
					        	}
				        	%>
					    </select>
					
					    
					    <button class="btn btn-default btn-block my-4 <% if(addedItems.isEmpty()){out.print("disable");} %>"  type="submit" >Create PO</button>
					    <button class="btn btn btn-blue-grey btn-block my-4 <% if(addedItems.isEmpty()){out.print("disable");} %>" onclick="return changeHidden();" type="submit">Save as Draft PO</button>
					</form>
				</div>
				
				<script type="text/javascript">
					function changeHidden(){  
					   var hid = document.getElementById('hiddenId');    
					   hid.value = '8';
					   return true;
					}
				</script>
				
				<div class = "col-md-8 ">
					<%
							try{
								ArrayList<Supplier> suppliers = (ArrayList<Supplier>)session.getAttribute("suppliers");
								
								if(suppliers != null){//if supliers null
								
					%>
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
									      	<button class="btn btn-sm btn-cyan" name="add<%=counter %>" onclick="document.getElementById('itemForm<%=counter%>').submit();">SELECT</button>
								      	  	<form id="itemForm<%=counter%>" action="purchaseOrder" method="post">
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
						<%
								}else{
									//else items will display
									ArrayList<Item> items = (ArrayList<Item>)session.getAttribute("items");
								
						%>			
						
									<div class="row">
										<div class="col-md-6">
										  	<form action="" id="searchSupplier" action="purchaseOrder" method="post">
												<div class="input-group md-form form-sm form-1 pl-0">
												  <div class="input-group-prepend">
												    <span class="input-group-text cyan lighten-2" id="basic-text1"><i class="fas fa-search text-white"
												        aria-hidden="true"></i></span>
												  </div>
													<input class="form-control my-0 py-1"
														autofocus="autofocus" id="test"
														onfocus="this.value = this.value;"
														value="<%  try{ String searchValue = (String)session.getAttribute("searchValue"); if(searchValue != null){out.print(searchValue);}}catch(NullPointerException exc){} %>"
													 	name="search_value" onkeyup="document.getElementById('searchSupplier').submit();" type="text" placeholder="Search" aria-label="Search">
   													<input type = "hidden" name="type" value="4">
   													<input type = "hidden" name="supplier_id" value="<%=selectedSupplier.getSupplier_id()%>">
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
													    		int itemCounter = 0;
													    		for(Item item : items){
													    			itemCounter++;
													    		
													    	%>
															        <tr class="table">
															            <td><%=item.getItem_code() %></td>
															            <td><%=item.getItem_name() %></td>
															            <td><%=item.getUnit_price() %></td>
															            <td><%=item.isSpecialApproval() %></td>
															            <form action="purchaseOrder" method="post">
																            <td>
																			      	<input type="number" id="qty" name="quantity" value="" class="form-control mb-1" placeholder="Quantity" required>
																			      	<input type = "hidden" name="type" value="3">
																			      	<input type = "hidden" name="supplier_id" value="<%=selectedSupplier.getSupplier_id()%>">
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
						
						<%	
								}
						%>
						<%	
							}catch(NullPointerException exc){
								
						%>
								
						<%
							}
					
						%>
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
  <script type="text/javascript">
	  $(document).ready(function() {
		  
		    var input = $("#test");
		    var len = input.val().length;
		    input[0].focus();
		    input[0].setSelectionRange(len, len);
	
		});
  </script>	
</body>
</html>