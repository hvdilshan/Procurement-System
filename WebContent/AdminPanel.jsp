<%@page import="com.procument.model.Notification"%>
<%@page import="com.procument.model.PurchaseOrder"%>
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
	ArrayList<PurchaseOrder> purchaseOrders = (ArrayList<PurchaseOrder>)session.getAttribute("purchaseOrders");
	ArrayList<Notification> notifications = (ArrayList<Notification>)request.getAttribute("notifications");
	long totalPoCost = (long)session.getAttribute("totalPoCost");
	long poAverage = (long)session.getAttribute("poAverage");
%>

<body>
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
		          <a class="dropdown-item" onclick = "document.getElementById('purchaseOrderDraft').submit();" href="#">Edit Draft Pos</a>
		        </div>
		      </li>
		      
		      <form id="purchaseOrderForm" action="purchaseOrder" method="post">
		      	<input type="hidden" name="type" value="1">
		      </form>
		      
		      <form id="purchaseOrderDraft" action="updatePo" method="post">
		      	<input type="hidden" name="type" value="7">
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
	<!--Main layout-->
	<main class="mt-4">
	  <div class="container-fluid">
	
	      <!--Section: Modals-->
			<section>
			
			    <!--Modal: modalConfirmDelete-->
			    <div class="modal fade" id="modalConfirmDelete" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
			        <div class="modal-dialog modal-sm modal-notify modal-danger" role="document">
			            <!--Content-->
			            <div class="modal-content text-center">
			                <!--Header-->
			                <div class="modal-header d-flex justify-content-center">
			                    <p class="heading">Are you sure?</p>
			                </div>
			
			                <!--Body-->
			                <div class="modal-body">
			
			                    <i class="fas fa-times fa-4x animated rotateIn"></i>
			
			                </div>
			
			                <!--Footer-->
			                <div class="modal-footer flex-center">
			                    <a href="https://mdbootstrap.com/products/jquery-ui-kit/" class="btn btn-danger">Yes</a>
			                    <a type="button" class="btn btn-outline-danger waves-effect" data-dismiss="modal">No</a>
			                </div>
			            </div>
			            <!--/.Content-->
			        </div>
			    </div>
			    <!--Modal: modalConfirmDelete-->
			
			</section>
			<!--Section: Modals-->
	
	     <!--Section: Main panel-->
		<section class="card card-cascade narrower mb-5">
		
		<!--Grid row-->
		<div class="row">
		
		<!--Grid column-->
  		<div class="col-md-5">
		
		  <!--Panel Header-->
		  <div class="view view-cascade py-3 gradient-card-header info-color-dark">
		      <h5 class="mb-0 text-white ml-3 ">PO Summary</h5>
		  </div>
		  <!--/Panel Header-->
		
		  <!--Panel content-->
		  <div class="card-body">
		
		      <!--Grid row-->
		      <div class="row">
		
		          <!--Grid column-->
		          <div class="col-md-6 mb-4">
					<!--Date select-->
					<p class="lead">
					  <span class="badge info-color-dark p-2">Date range</span>
					</p>
					<select class="mdb-select colorful-select dropdown-info">
					  <option value="" disabled>Choose time period</option>
					  <option value="1">Today</option>
					  <option value="2">Yesterday</option>
					  <option value="3">Last 7 days</option>
					  <option value="3">Last 30 days</option>
					  <option value="3">Last week</option>
					  <option value="3">Last month</option>
					</select>	
					<!--Date pickers-->
					<p class="lead my-4">
					  <span class="badge info-color-dark p-2">Custom date</span>
					</p>
					<div class="md-form">
					  <input placeholder="Selected date" type="text" id="from" class="form-control datepicker">
					  
					  <label for="from">From</label>
					</div>
					<div class="md-form">
					  <input placeholder="Selected date" type="text" id="to" class="form-control datepicker">
					  <label for="to">To</label>
					</div>
		
		          </div>
		          <!--Grid column-->
		
		          <!--Grid column-->
		          <div class="col-md-6 mb-4 text-center">
					<!--Summary-->
					<p>Total :
					    <strong>Rs.<%=totalPoCost %></strong>
					    <button type="button" class="btn btn-info btn-sm p-2" data-toggle="tooltip" data-placement="top" title="Total POS in the given period">
					        <i class="fas fa-question"></i>
					    </button>
					</p>
					<p>Average :
					    <strong>Rs.<%=poAverage %></strong>
					    <button type="button" class="btn btn-info btn-sm p-2" data-toggle="tooltip" data-placement="top" title="Average PO Cost in the given period">
					        <i class="fas fa-question"></i>
					    </button>
					</p>
					
		          </div>
		          <!--Grid column-->
		
		      </div>
		      <!--Grid row-->
		
		  </div>
		  <!--Panel content-->
		
		</div>
		<!--Grid column-->
		
		  <!--Grid column-->
			<div class="col-md-7">
			
			    <!--Panel Header-->
			    <div class="view view-cascade py-3 gradient-card-header info-color-dark mb-4">
			
			        <canvas id="lineChart"></canvas>
			
			    </div>
			    <!--/Card image-->
			
			</div>
			<!--Grid column-->
		
		  </div>
		  <!--Grid row-->
		
		</section>
		<!--Section: Main panel-->
	
	      <!--Section: Table-->
			<section class="mb-5">
			
			    <!--Top Table UI-->
			    <div class="card p-2 mb-5">
			
			        <!--Grid row-->
			        <div class="row">
			
			            <!--Grid column-->
			            <div class="col-lg-3 col-md-12">
			
			                <!--Name-->
			                <select class="mdb-select colorful-select dropdown-primary mx-2">
			                    <option value="" disabled selected>Bulk actions</option>
			                    <option value="1">Delate</option>
			                    <option value="2">Export</option>
			                    <option value="3">Change segment</option>
			                </select>
			
			            </div>
			            <!--Grid column-->
			
			            <!--Grid column-->
			            <div class="col-lg-3 col-md-6">
			
			                <!--Blue select-->
			                <select class="mdb-select colorful-select dropdown-primary mx-2">
			                    <option value="" disabled selected>Show only</option>
			                    <option value="1">All
			                        <span> (2000)</span>
			                    </option>
			                    <option value="2">Never opened
			                        <span> (200)</span>
			                    </option>
			                    <option value="3">Opened but unanswered
			                        <span> (1800)</span>
			                    </option>
			                    <option value="4">Answered
			                        <span> (200)</span>
			                    </option>
			                    <option value="5">Unsunscribed
			                        <span> (50)</span>
			                    </option>
			                </select>
			                <!--/Blue select-->
			
			            </div>
			            <!--Grid column-->
			
			            <!--Grid column-->
			            <div class="col-lg-3 col-md-6">
			
			                <!--Blue select-->
			                <select class="mdb-select colorful-select dropdown-primary mx-2">
			                    <option value="" disabled selected>Filter segments</option>
			                    <option value="1">Contacts in no segments
			                        <span> (100)</span>
			                    </option>
			                    <option value="1">Segment 1
			                        <span> (2000)</span>
			                    </option>
			                    <option value="2">Segment 2
			                        <span> (1000)</span>
			                    </option>
			                    <option value="3">Segment 3
			                        <span> (4000)</span>
			                    </option>
			                </select>
			                <!--/Blue select-->
			
			            </div>
			            <!--Grid column-->
			
			            <!--Grid column-->
			            <div class="col-lg-3 col-md-6">
			
			                <form class="form-inline mt-2 ml-2">
			                    <input class="form-control my-0 py-0" type="text" placeholder="Search" style="max-width: 150px;">
			                    <button class="btn btn-sm btn-info ml-2 px-2">
			                        <i class="fas fa-search"></i>
			                    </button>
			                </form>
			
			            </div>
			            <!--Grid column-->
			
			        </div>
			        <!--Grid row-->
			
			    </div>
			    <!--Top Table UI-->
				<!--Card-->
				<div class="card card-cascade narrower">
				
					<!--Card header-->
					<div class="view view-cascade py-3 gradient-card-header info-color-dark mx-4 d-flex justify-content-between align-items-center">					
					    <a href="" class="white-text mx-3">Purchase Orders</a>
					 </div>
					  <!--/Card header-->
				
				    <!--Card content-->
					<div class="card-body">
						<div class="table-responsive">
					    <table class="table text-nowrap">
						    <thead>
						      <tr>
						          <th>po_id</th>
						          <th>created Date</th>
						          <th>Created By</th>
						          <th>Approved By</th>
						          <th>Supplier</th>
						          <th>Site</th>
						          <th>Dilivery Date</th>
						          <th>PO Price</th>
						          <th>Status</th>
						          <th>Description</th>
						          <%
						          	if(user.getPrivilage() == 1){
						          	
						          %>
						          		<th>Update</th>
						          		<th>Delete/Request</th>
						          <%
						          	}else if(user.getPrivilage() == 2){
						          %>
						          		<th>Approve</th>
						          		<th>Deny</th>
						          <%
						          	}
						          %>
						          <th>Print</th>
						      </tr>
						    </thead>
						    <tbody>
						    <%
						    	int poCounter = 0;
						    	for(PurchaseOrder pOrder : purchaseOrders){
						    		poCounter++;
						    %>
						        <tr>
						            
						            <td><%=pOrder.getPo_id() %></td>
						            <td><%=pOrder.getCreated_date() %></td>
						            <td><%=pOrder.getUser().getFirst_name() +" " + pOrder.getUser().getLast_name() %></td>
						            <td><% if(pOrder.getApproved_by() == 0){ if(pOrder.getStatus().equals("Approved")){out.print("PO acceptable");}else{out.print("In Progress");} }else{out.print(pOrder.getApproved_by_user().getFirst_name());} %></td>
						            <td><%=pOrder.getSupplier().getCompany_name() %></td>
						            <td><%=pOrder.getSite().getSite_name() %></td>
						            <td><%=pOrder.getDilivery_date() %></td>
						            <td><%=pOrder.getPo_price() %></td>
						            <td><%=pOrder.getStatus() %></td>
						            <td><%=pOrder.getDescription() %></td>
						            <%
						          		if(user.getPrivilage() == 1){
						          	
						          	%>
						          		<td>
									      	<button class="btn btn-sm btn-dark-green<% if(user.getUser_id() != pOrder.getCreated_by()){out.print("disabled");} %>" name="update" onclick="document.getElementById('poUpdateForm<%=poCounter %>').submit();">UPDATE</button>
								      	  	<form id="poUpdateForm<%=poCounter %>" action="purchaseOrder" method="post">
										      	<input type = "hidden" name="type" value="7">
										      	<input type = "hidden" name="action" value="update">
										      	<input type = "hidden" name="po_id" value="<%=pOrder.getPo_id()%>">
										    </form>
								      	</td>
							            <td>
									      	<button class="btn btn-sm <% if(pOrder.getStatus().equals("Accepted")){out.print("btn-warning");}else{out.print("btn-danger");} %>" name="delete" onclick="document.getElementById('poDeleteForm<%=poCounter%>').submit();"><% if(pOrder.getStatus().equals("Accepted")){out.print("REQUEST");}else{out.print("DELETE");} %></button>
								      	  	<form id="poDeleteForm<%=poCounter%>" action="#" method="post">
										      	<input type = "hidden" name="type" value="2">
										      	<input type = "hidden" name="supplier_id" value="<%=pOrder.getPo_id()%>">
										    </form>
								      	</td>
						          <%
						          	}else if(user.getPrivilage() == 2){
						          %>
						          		<td>
									      	<button class="btn btn-sm btn-dark-green <% if(pOrder.getStatus().equals("Approved")){out.print("disabled");} %>" name="update" onclick="document.getElementById('poAcceptForm<%=poCounter%>').submit();">APPROVE</button>
								      	  	<form id="poAcceptForm<%=poCounter %>" action="#" method="post">
										      	<input type = "hidden" name="type" value="6">
										      	<input type = "hidden" name="action" value="update">
										      	<input type = "hidden" name="item_id" value="<%=pOrder.getPo_id()%>">
										    </form>
								      	</td>
							            <td>
									      	<button class="btn btn-sm btn-danger <% if(pOrder.getStatus().equals("Approved")){out.print("disabled");} %>" name="delete" onclick="document.getElementById('poDenyForm<%=poCounter%>').submit();">DENY</button>
								      	  	<form id="poDenyForm<%=poCounter%>" action="#" method="post">
										      	<input type = "hidden" name="type" value="2">
										      	<input type = "hidden" name="supplier_id" value="<%=pOrder.getPo_id()%>">
										    </form>
								      	</td>
						          <%
						          	}
						          %>
						          
					          		<td>
								      	<button class="btn btn-sm btn-indigo" name="delete" onclick="document.getElementById('poPintForm<%=poCounter%>').submit();">PRINT</button>
							      	  	<form id="poPintForm<%=poCounter%>" action="#" method="post">
									      	<input type = "hidden" name="type" value="2">
									      	<input type = "hidden" name="supplier_id" value="<%=pOrder.getPo_id()%>">
									    </form>
							      	</td>
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
						 <!--Name-->
					    <select class="mdb-select colorful-select dropdown-primary mt-2 d-hidden d-md-block">
					        <option value="" disabled>Rows number</option>
					        <option value="1" selected>10 rows</option>
					        <option value="2">25 rows</option>
					        <option value="3">50 rows</option>
					        <option value="4">100 rows</option>
					    </select>
						<!--Pagination -->
						<nav class="mt-4">
						    <ul class="pagination pagination-circle pg-blue mb-0">
						
						      
						
						        <!--Arrow left-->
						        <li class="page-item disabled">
						            <a class="page-link" aria-label="Previous">
						                <span aria-hidden="true">&laquo;</span>
						                <span class="sr-only">Previous</span>
						            </a>
						        </li>
						
						        <!--Numbers-->
						        <li class="page-item active">
						            <a class="page-link">1</a>
						        </li>
						       
						
						        <!--Arrow right-->
						        <li class="page-item">
						            <a class="page-link" aria-label="Next">
						                <span aria-hidden="true">&raquo;</span>
						                <span class="sr-only">Next</span>
						            </a>
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
	
	      <!--Section: Accordion-->
			<section class="mb-5">
			
				<!--Accordion wrapper-->
			<div class="md-accordion accordion" id="accordionEx" role="tablist" aria-multiselectable="true">
			
			  <!-- Accordion card -->
			  <div class="card">
			
			    <!-- Card header -->
			    <div class="card-header" role="tab" id="headingOne">
			
			      <!--Options-->
			      <div class="dropdown float-left">
			        <button class="btn btn-info btn-sm m-0 mr-3 p-2 dropdown-toggle" type="button" data-toggle="dropdown"
			          aria-haspopup="true" aria-expanded="false">
			          <i class="fas fa-pencil-alt"></i>
			        </button>
			        <div class="dropdown-menu dropdown-info">
			          <a class="dropdown-item" href="#">Add new</a>
			          <a class="dropdown-item" href="#">Rename folder</a>
			          <a class="dropdown-item" href="#">Delete folder</a>
			        </div>
			      </div>
			
			      <!-- Heading -->
			      <a id="folder-1" data-toggle="collapse" data-parent="#accordionEx" href="#collapseOne" aria-expanded="true"
			        aria-controls="collapseOne">
			        <h5 class="mt-1 mb-0">
			          <span>Folder 1</span>
			          <i class="fas fa-angle-down rotate-icon"></i>
			        </h5>
			      </a>
			
			    </div>
			
			    <!-- Card body -->
			    <div id="collapseOne" class="collapse show" role="tabpanel" aria-labelledby="headingOne">
			      <div class="card-body">
			        Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad
			        squid. 3 wolf moon officia aute,
			        non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch
			        3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda
			        shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt
			        sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer
			        farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them
			        accusamus
			        labore sustainable VHS.
			      </div>
			    </div>
			  </div>
			  <!-- Accordion card -->
				<br><br>			
			  <!-- Accordion card -->
			  <div class="card">
			
			    <!-- Card header -->
			    <div class="card-header" role="tab" id="headingTwo">
			
			      <!--Options-->
			      <div class="dropdown float-left">
			        <button class="btn btn-info btn-sm m-0 mr-3 p-2 dropdown-toggle" type="button" data-toggle="dropdown"
			          aria-haspopup="true" aria-expanded="false">
			          <i class="fas fa-pencil-alt"></i>
			        </button>
			        <div class="dropdown-menu dropdown-info">
			          <a class="dropdown-item" href="#">Add new</a>
			          <a class="dropdown-item" href="#">Rename folder</a>
			          <a class="dropdown-item" href="#">Delete folder</a>
			        </div>
			      </div>
			
			      <!-- Heading -->
			      <a id="folder-2" data-toggle="collapse" data-parent="#accordionEx" href="#collapseTwo" aria-expanded="true"
			        aria-controls="collapseTwo">
			        <h5 class="mt-1 mb-0">
			          <span>Folder 2</span>
			          <i class="fas fa-angle-down rotate-icon"></i>
			        </h5>
			      </a>
			
			    </div>
			
			    <!-- Card body -->
			    <div id="collapseTwo" class="collapse" role="tabpanel" aria-labelledby="headingTwo">
			      <div class="card-body">
			        Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad
			        squid. 3 wolf moon officia aute,
			        non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch
			        3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda
			        shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt
			        sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer
			        farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them
			        accusamus
			        labore sustainable VHS.
			      </div>
			    </div>
			  </div>
			  <!-- Accordion card -->
			<br><br>
			  <!-- Accordion card -->
			  <div class="card">
			
			    <!-- Card header -->
			    <div class="card-header" role="tab" id="headingThree">
			
			      <!--Options-->
			      <div class="dropdown float-left">
			        <button class="btn btn-info btn-sm m-0 mr-3 p-2 dropdown-toggle" type="button" data-toggle="dropdown"
			          aria-haspopup="true" aria-expanded="false">
			          <i class="fas fa-pencil-alt"></i>
			        </button>
			        <div class="dropdown-menu dropdown-info">
			          <a class="dropdown-item" href="#">Add new</a>
			          <a class="dropdown-item" href="#">Rename folder</a>
			          <a class="dropdown-item" href="#">Delete folder</a>
			        </div>
			      </div>
			
			      <!-- Heading -->
			      <a id="folder-3" data-toggle="collapse" data-parent="#accordionEx" href="#collapseThree" aria-expanded="true"
			        aria-controls="collapseThree">
			        <h5 class="mt-1 mb-0">
			          <span>Folder 3</span>
			          <i class="fas fa-angle-down rotate-icon"></i>
			        </h5>
			      </a>
			    </div>
			
			    <!-- Card body -->
			    <div id="collapseThree" class="collapse" role="tabpanel" aria-labelledby="headingThree">
			      <div class="card-body">
			        Anim pariatur cliche reprehenderit, enim eiusmod high life accusamus terry richardson ad
			        squid. 3 wolf moon officia aute,
			        non cupidatat skateboard dolor brunch. Food truck quinoa nesciunt laborum eiusmod. Brunch
			        3 wolf moon tempor, sunt aliqua put a bird on it squid single-origin coffee nulla assumenda
			        shoreditch et. Nihil anim keffiyeh helvetica, craft beer labore wes anderson cred nesciunt
			        sapiente ea proident. Ad vegan excepteur butcher vice lomo. Leggings occaecat craft beer
			        farm-to-table, raw denim aesthetic synth nesciunt you probably haven't heard of them
			        accusamus
			        labore sustainable VHS.
			      </div>
			    </div>
			  </div>
			  <!-- Accordion card -->
			</div>
			<!--/.Accordion wrapper-->
			
			</section>
			<!--Section: Accordion-->
	
	  </div>
	</main>
	<!--Main layout-->
	
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
  <script type="text/javascript">
  $(".selector").flatpickr(optional_config);
  </script>
  
  <script type="text/javascript">
//Tooltip Initialization
	  $(function () {
	  $('[data-toggle="tooltip"]').tooltip()
	  })
  </script>
  <script type="text/javascript">
//Minimalist chart
  $(function () {
      $('.min-chart#chart-sales').easyPieChart({
          barColor: "#4caf50",
          onStep: function (from, to, percent) {
              $(this.el).find('.percent').text(Math.round(percent));
          }
      });
  });
  </script>
  <script type="text/javascript">
	//Main chart
	  var ctxL = document.getElementById("lineChart").getContext('2d');
	  var myLineChart = new Chart(ctxL, {
	  type: 'line',
	  data: {
	  labels: ["January", "February", "March", "April", "May", "June", "July"],
	  datasets: [{
	  label: "PO Tracker",
	  fillColor: "#fff",
	  backgroundColor: 'rgba(255, 255, 255, .3)',
	  borderColor: 'rgba(255, 255, 255)',
	  data: [0, 10, 5, 2, 20, 30, 45],
	  }]
	  },
	  options: {
	  legend: {
	  labels: {
	      fontColor: "#fff",
	  }
	  },
	  scales: {
	  xAxes: [{
	      gridLines: {
	          display: true,
	          color: "rgba(255,255,255,.25)"
	      },
	      ticks: {
	          fontColor: "#fff",
	      },
	  }],
	  yAxes: [{
	      display: true,
	      gridLines: {
	          display: true,
	          color: "rgba(255,255,255,.25)"
	      },
	      ticks: {
	          fontColor: "#fff",
	      },
	  }],
	  }
	  }
	  });
  </script>
	
	<script type="text/javascript">
  (function() {
	  'use strict';
	  window.addEventListener('load', function() {
	  // Fetch all the forms we want to apply custom Bootstrap validation styles to
	  var forms = document.getElementsByClassName('needs-validation');
	  // Loop over them and prevent submission
	  var validation = Array.prototype.filter.call(forms, function(form) {
	  form.addEventListener('submit', function(event) {
	  if (form.checkValidity() === false) {
	  event.preventDefault();
	  event.stopPropagation();
	  }
	  form.classList.add('was-validated');
	  }, false);
	  });
	  }, false);
	  })();
  </script>
<script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
	
</body>

</html>
