package com.speedlaundryapp.userapp.model.notification;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("notification")
	private Notification notification;

	@SerializedName("data")
	private Data data;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("channel")
	private Object channel;

	@SerializedName("purchase_id")
	private int purchaseId;

	@SerializedName("product_id")
	private int productId;

	public void setNotification(Notification notification){
		this.notification = notification;
	}

	public Notification getNotification(){
		return notification;
	}

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	public void setUserId(int userId){
		this.userId = userId;
	}

	public int getUserId(){
		return userId;
	}

	public void setChannel(Object channel){
		this.channel = channel;
	}

	public Object getChannel(){
		return channel;
	}

	public void setPurchaseId(int purchaseId){
		this.purchaseId = purchaseId;
	}

	public int getPurchaseId(){
		return purchaseId;
	}

	public void setProductId(int productId){
		this.productId = productId;
	}

	public int getProductId(){
		return productId;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"notification = '" + notification + '\'' + 
			",data = '" + data + '\'' + 
			",user_id = '" + userId + '\'' + 
			",channel = '" + channel + '\'' + 
			",purchase_id = '" + purchaseId + '\'' + 
			",product_id = '" + productId + '\'' + 
			"}";
		}
}