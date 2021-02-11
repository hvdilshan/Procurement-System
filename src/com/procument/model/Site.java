package com.procument.model;

import java.sql.Date;

public class Site {
	private int site_id;
	private String site_name;
	private String site_address;
	private Date project_start_date;
	private int project_duration;
	private String status;
	private Customer customer;
	private User user;

	public Site() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getSite_name() {
		return site_name;
	}

	public void setSite_name(String site_name) {
		this.site_name = site_name;
	}

	public String getSite_address() {
		return site_address;
	}

	public void setSite_address(String site_address) {
		this.site_address = site_address;
	}

	public Date getProject_start_date() {
		return project_start_date;
	}

	public void setProject_start_date(Date project_start_date) {
		this.project_start_date = project_start_date;
	}

	public int getProject_duration() {
		return project_duration;
	}

	public void setProject_duration(int project_duration) {
		this.project_duration = project_duration;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getSite_id() {
		return site_id;
	}

	public void setSite_id(int site_id) {
		this.site_id = site_id;
	}
	
	
}
