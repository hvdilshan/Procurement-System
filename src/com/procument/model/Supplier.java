package com.procument.model;

public class Supplier {
	private String supplier_id;
	private String company_name;
	private String supplier_name;
	private String supplier_address;
	private String supplier_email;
	private String supplier_contact;	
	private int grade;
	private int added_by;
	
	public Supplier() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getAdded_by() {
		return added_by;
	}

	public void setAdded_by(int added_by) {
		this.added_by = added_by;
	}

	public String getSupplier_email() {
		return supplier_email;
	}

	public void setSupplier_email(String supplier_email) {
		this.supplier_email = supplier_email;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}



	public String getSupplier_contact() {
		return supplier_contact;
	}
	
	public void setSupplier_contact(String supplier_contact) {
		this.supplier_contact = supplier_contact;
	}
	
	public String getSupplier_address() {
		return supplier_address;
	}

	public void setSupplier_address(String supplier_address) {
		this.supplier_address = supplier_address;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public String getSupplier_id() {
		return supplier_id;
	}
	public void setSupplier_id(String supplier_id) {
		this.supplier_id = supplier_id;
	}
	public String getSupplier_name() {
		return supplier_name;
	}
	public void setSupplier_name(String supplier_name) {
		this.supplier_name = supplier_name;
	}
	
	
}
