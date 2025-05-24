package com.speedlaundryapp.userapp.model.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;

public class NotificationsItem{

	@SerializedName("data")
	@Expose
	private Data data;

	@SerializedName("updated_at")
	@Expose
	private String updatedAt;
	@SerializedName("created_at")
	@Expose
	private String createdAt;

	@SerializedName("id")
	@Expose
	private String id;

	@SerializedName("user_id")
	@Expose
	private Integer userId;

	@SerializedName("transaction_id")
	@Expose
	private Integer transactionId;
	@SerializedName("user_type")
	@Expose
	private Integer userType;

	@SerializedName("type")
	@Expose
	private String type;

	@SerializedName("message")
	@Expose
	private String message;

	@SerializedName("transaction")
	@Expose
	private TransactionItem transaction;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Integer transactionId) {
		this.transactionId = transactionId;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public TransactionItem getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionItem transaction) {
		this.transaction = transaction;
	}
}