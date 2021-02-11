<%@page import="java.util.ArrayList"%>
<%@page import="com.procument.model.PurchaseOrder"%>
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
		ArrayList<PurchaseOrder> purchaseOrders = (ArrayList<PurchaseOrder>)session.getAttribute("purchaseOrders");
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
		          	Draft PO
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
	<main>
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
					          		<td>
								      	<button class="btn btn-sm btn-dark-green<% if(user.getUser_id() != pOrder.getCreated_by()){out.print("disabled");} %>" name="update" onclick="document.getElementById('poUpdateForm<%=poCounter %>').submit();">EDIT</button>
							      	  	<form id="poUpdateForm<%=poCounter %>" action="purchaseOrder" method="post">
									      	<input type = "hidden" name="type" value="7">
									      	<input type = "hidden" name="action" value="update">
									      	<input type = "hidden" name="po_id" value="<%=pOrder.getPo_id()%>">
									    </form>
							      	</td>
						            <td>
								      	<button class="btn btn-sm <% if(pOrder.getStatus().equals("Accepted")){out.print("btn-warning");}else{out.print("btn-danger");} %>" name="delete" onclick="document.getElementById('poDeleteForm<%=poCounter%>').submit();">DELETE</button>
							      	  	<form id="poDeleteForm<%=poCounter%>" action="#" method="post">
									      	<input type = "hidden" name="type" value="2">
									      	<input type = "hidden" name="supplier_id" value="<%=pOrder.getPo_id()%>">
									    </form>
							      	</td>
							      	
							      	<td>
								      	<button class="btn btn-sm btn-indigo" name="delete" onclick="document.getElementById('poPintForm<%=poCounter%>').submit();">PRINT</button>
							      	  	<form id="poPintForm<%=poCounter%>" action="#" method="post">
									      	<input type = "hidden" name="type" value="2">
									      	<input type = "hidden" name="supplier_id" value="<%=pOrder.getPo_id()%>">
									    </form>
							      	</td>
							      	
						        </tr>
					        <%} %>
						    </tbody>
						</table>
						
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