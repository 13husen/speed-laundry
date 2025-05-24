package com.speedlaundryapp.userapp.model;

import com.google.gson.annotations.SerializedName;

public class RegisterModel {

	@SerializedName("session_data")
	public SessionData sessionData;

	@SerializedName("client_secret")
	public String clientSecret;

	@SerializedName("notification_id")
	public String notificationId;

	@SerializedName("facebook_token")
	public String fbToken;

	@SerializedName("phone")
	public String phone;

	@SerializedName("password")
	public String password;

	@SerializedName("password_confirmation")
	public String password_confirmation;

	@SerializedName("fcm_token")
	public String fcmToken;

	@SerializedName("email")
	public String email;

	@SerializedName("client_id")
	public String clientId;

	@SerializedName("facebook_id")
	public String facebookId;

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword_confirmation() { return password_confirmation; }

	public void setPassword_confirmation(String password_confirmation) { this.password_confirmation = password_confirmation; }

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getFbToken() {
		return fbToken;
	}

	public void setFbToken(String fbToken) {
		this.fbToken = fbToken;
	}

	public void setSessionData(SessionData sessionData){
		this.sessionData = sessionData;
	}

	public SessionData getSessionData(){
		return sessionData;
	}

	public void setClientSecret(String clientSecret){
		this.clientSecret = clientSecret;
	}

	public String getClientSecret(){
		return clientSecret;
	}

	public void setNotificationId(String notificationId){
		this.notificationId = notificationId;
	}

	public String getNotificationId(){
		return notificationId;
	}
/*
	public void setRegToken(String regToken){
		this.regToken = regToken;
	}

	public String getRegToken(){
		return regToken;
	}*/

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setClientId(String clientId){
		this.clientId = clientId;
	}

	public String getClientId(){
		return clientId;
	}

	public void setFacebookId(String facebookId){
		this.facebookId = facebookId;
	}

	public String getFacebookId(){
		return facebookId;
	}

	@Override
	public String toString() {
		return "RegisterModel{" +
				"sessionData=" + sessionData +
				", clientSecret='" + clientSecret + '\'' +
				", notificationId='" + notificationId + '\'' +
				", fbToken='" + fbToken + '\'' +
				", phone='" + phone + '\'' +
				", password='" + password + '\'' +
				", password_confirmation='" + password_confirmation + '\'' +
				", fcmToken='" + fcmToken + '\'' +
				", email='" + email + '\'' +
				", clientId='" + clientId + '\'' +
				", facebookId='" + facebookId + '\'' +
				'}';
	}
}