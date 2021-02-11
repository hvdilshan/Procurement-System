package com.procument.model;

public class SiteManager extends User {
	private String site_id;
	
	public SiteManager() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SiteManager(int user_id, String first_name, String last_name, String department, String designation,
			String email, String address, String contact_number, String user_picture, String site_id) {
		super(user_id, first_name, last_name, department, designation, email, address, contact_number, user_picture);
		// TODO Auto-generated constructor stub
		this.site_id = site_id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	
}
