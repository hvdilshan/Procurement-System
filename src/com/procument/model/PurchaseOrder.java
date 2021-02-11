package com.procument.model;

import java.util.Date;
import java.util.ArrayList;

public class PurchaseOrder {
	private int po_id;
	private Date created_date;
	private int created_by;
	private int approved_by;
	private String supplier_id;
	private int site_id;
	private Date dilivery_date;
	private double po_price;
	private String status;
	private ArrayList<Item> items;
	private User user;
	private Supplier supplier;
	private Site site;
	private User approved_by_user;
	private String description;
	private ArrayList<ProcumentStaff> procumentStaffs;
	
	
	public PurchaseOrder(int po_id, Date created_date, int created_by, int approved_by, String supplier_id, int site_id,
			Date dilivery_date, double po_price, String status, ArrayList<Item> items, User user, Supplier supplier,
			Site site, User approved_by_user, String description) {
		super();
		this.po_id = po_id;
		this.created_date = created_date;
		this.created_by = created_by;
		this.approved_by = approved_by;
		this.supplier_id = supplier_id;
		this.site_id = site_id;
		this.dilivery_date = dilivery_date;
		this.po_price = po_price;
		this.status = status;
		this.items = items;
		this.user = user;
		this.supplier = supplier;
		this.site = site;
		this.approved_by_user = approved_by_user;
		this.description = description;
		procumentStaffs = new ArrayList<ProcumentStaff>();
	}

	public PurchaseOrder() {
		super();
		// TODO Auto-generated constructor stub
		procumentStaffs = new ArrayList<ProcumentStaff>();
	}
	
	//observer design pattern begings
	public void attach(ProcumentStaff procumentStaff) {
		procumentStaffs.add(procumentStaff);
	}
	
	public void deAttach(ProcumentStaff procumentStaff) {
		procumentStaffs.remove(procumentStaff);
	}

	public void notifyProcumentStaff() {
		for(ProcumentStaff procumentStaff : procumentStaffs) {
			procumentStaff.update();
		}
	}
	//observer design pattern ends
	
	public User getApproved_by_user() {
		return approved_by_user;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setApproved_by_user(User approved_by_user) {
		this.approved_by_user = approved_by_user;
	}



	public int getPo_id() {
		return po_id;
	}


	public void setPo_id(int po_id) {
		this.po_id = po_id;
	}


	public Date getCreated_date() {
		return created_date;
	}


	public void setCreated_date(Date created_date) {
		this.created_date = created_date;
	}


	public int getCreated_by() {
		return created_by;
	}


	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}


	public int getApproved_by() {
		return approved_by;
	}


	public void setApproved_by(int approved_by) {
		this.approved_by = approved_by;
	}


	public String getSupplier_id() {
		return supplier_id;
	}


	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}


	public int getSite_id() {
		return site_id;
	}


	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}


	public Date getDilivery_date() {
		return dilivery_date;
	}


	public void setDilivery_date(Date dilivery_date) {
		this.dilivery_date = dilivery_date;
	}


	public double getPo_price() {
		return po_price;
	}


	public void setPo_price(double po_price) {
		this.po_price = po_price;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public ArrayList<Item> getItems() {
		return items;
	}


	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Supplier getSupplier() {
		return supplier;
	}


	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}


	public Site getSite() {
		return site;
	}


	public void setSite(Site site) {
		this.site = site;
	}
	
	
	
	
}
