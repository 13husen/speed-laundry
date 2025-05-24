package com.speedlaundry.admin.model.notification;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NotificationModel{

	@SerializedName("notifications")
	private List<NotificationsItem> notifications;

	public void setNotifications(List<NotificationsItem> notifications){
		this.notifications = notifications;
	}

	public List<NotificationsItem> getNotifications(){
		return notifications;
	}

	@Override
 	public String toString(){
		return 
			"NotificationModel{" + 
			"notifications = '" + notifications + '\'' + 
			"}";
		}
}