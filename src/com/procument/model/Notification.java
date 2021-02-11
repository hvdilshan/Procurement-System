package com.procument.model;

public class Notification {
	private int id_notif;
	private String notification_type;
	private String description;
	private String notifi_type;
	private String notifi_for;
	private int notif_rel_id;
	private int status; 
	
	public Notification() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getId_notif() {
		return id_notif;
	}

	public void setId_notif(int id_notif) {
		this.id_notif = id_notif;
	}

	public String getNotifi_for() {
		return notifi_for;
	}

	public void setNotifi_for(String notifi_for) {
		this.notifi_for = notifi_for;
	}

	public String getNotifi_type() {
		return notifi_type;
	}

	public void setNotifi_type(String notifi_type) {
		this.notifi_type = notifi_type;
	}

	public int getNotif_rel_id() {
		return notif_rel_id;
	}

	public void setNotif_rel_id(int notif_rel_id) {
		this.notif_rel_id = notif_rel_id;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getNotification_type() {
		return notification_type;
	}
	
	public void setNotification_type(String notification_type) {
		this.notification_type = notification_type;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
