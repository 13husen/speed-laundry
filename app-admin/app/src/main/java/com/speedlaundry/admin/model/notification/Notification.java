package com.speedlaundry.admin.model.notification;

import com.google.gson.annotations.SerializedName;

public class Notification{

	@SerializedName("title")
	private String title;

	@SerializedName("body")
	private String body;

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setBody(String body){
		this.body = body;
	}

	public String getBody(){
		return body;
	}

	@Override
 	public String toString(){
		return 
			"Notification{" + 
			"title = '" + title + '\'' + 
			",body = '" + body + '\'' + 
			"}";
		}
}