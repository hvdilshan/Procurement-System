package com.procument.model;

public class Management extends User {
	
	public boolean generatePOSummaryReport() {
		
		return true;
	}
	
	public boolean grantApproval() {
		
		return true;
	}
	
	public boolean requestApproval(String id) {
		
		return true;
	}
	
	public void notifi(String id) {
		
		
	}
}
