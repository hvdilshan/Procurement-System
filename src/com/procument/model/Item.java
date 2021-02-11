package com.procument.model;

import java.util.ArrayList;

public class Item {
	private int item_id;
	private String item_code;
	private String item_name;
	private double unit_price;
	private String unit;
	private Supplier supplier;
	protected ArrayList<Supplier> suppliers;
	private int quantity;
	private boolean specialApproval;
	private String url;
	
	public Item() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public boolean isSpecialApproval() {
		return specialApproval;
	}

	public void setSpecialApproval(boolean specialApproval) {
		this.specialApproval = specialApproval;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public ArrayList<Supplier> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(ArrayList<Supplier> suppliers) {
		this.suppliers = suppliers;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public String getItem_code() {
		return item_code;
	}

	public void setItem_code(String item_code) {
		this.item_code = item_code;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	public double getUnit_price() {
		return unit_price;
	}

	public void setUnit_price(double unit_price) {
		this.unit_price = unit_price;
	}

	
}
