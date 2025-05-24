package com.speedlaundryapp.userapp.model.user.data;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("user")
	private User user;

	@SerializedName("token")
	private Token token;

	public User getUser(){
		return user;
	}

	public Token getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"Data{" + 
			"user = '" + user + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}