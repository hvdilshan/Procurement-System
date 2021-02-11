package com.procument.model;

import com.procument.service.ProcumentStaffService;
import com.procument.service.PurchaseOrderService;

public class ProcumentStaff extends User {
	
	private PurchaseOrder purchaseOrder = new PurchaseOrder();
	private ProcumentStaffService procumentStaffService;
	
	public ProcumentStaff() {
		// TODO Auto-generated constructor stub
		procumentStaffService = ProcumentStaffService.getInstance();
	}
	
	public void update() {
		System.out.println(getFirst_name() +" A new Po is created need attention for " + purchaseOrder.getPo_id());
		procumentStaffService.addNotification(purchaseOrder.getPo_id(), getUser_id());
	}
	
	//assign related po into instance
	public void assignPo(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}
}
