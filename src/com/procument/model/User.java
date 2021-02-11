package com.procument.model;

public class User {
	private int user_id;
	private String first_name;
	private String last_name;
	private String department;
	private String designation;
	private String email;
	private String address;
	private String contact_number;
	private String user_picture;
	private int privilage;
	private String user_name;
	private String password;
	
	public User(int user_id, String first_name, String last_name, String department, String designation, String email,
			String address, String contact_number, String user_picture) {
		super();
		this.user_id = user_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.department = department;
		this.designation = designation;
		this.email = email;
		this.address = address;
		this.contact_number = contact_number;
		this.user_picture = user_picture;
	}

	public int getPrivilage() {
		return privilage;
	}

	public void setPrivilage(int privilage) {
		this.privilage = privilage;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContact_number() {
		return contact_number;
	}

	public void setContact_number(String contact_number) {
		this.contact_number = contact_number;
	}

	public String getUser_picture() {
		return user_picture;
	}

	public void setUser_picture(String user_picture) {
		this.user_picture = user_picture;
	}
	
	
	
	
	
}
