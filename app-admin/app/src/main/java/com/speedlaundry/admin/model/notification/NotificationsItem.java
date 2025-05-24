package com.speedlaundry.admin.model.notification;

import com.google.gson.annotations.SerializedName;

public class NotificationsItem{

	@SerializedName("data")
	private Data data;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("read_at")
	private Object readAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private String id;

	@SerializedName("notifiable_id")
	private int notifiableId;

	@SerializedName("type")
	private String type;

	@SerializedName("notifiable_type")
	private String notifiableType;

	public void setDataNotification(Data data){
		this.data = data;
	}

	public Data getDataNotification(){
		return data;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setReadAt(Object readAt){
		this.readAt = readAt;
	}

	public Object getReadAt(){
		return readAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setNotifiableId(int notifiableId){
		this.notifiableId = notifiableId;
	}

	public int getNotifiableId(){
		return notifiableId;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setNotifiableType(String notifiableType){
		this.notifiableType = notifiableType;
	}

	public String getNotifiableType(){
		return notifiableType;
	}

	@Override
 	public String toString(){
		return 
			"NotificationsItem{" + 
			"data = '" + data + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",read_at = '" + readAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",notifiable_id = '" + notifiableId + '\'' + 
			",type = '" + type + '\'' + 
			",notifiable_type = '" + notifiableType + '\'' + 
			"}";
		}
}