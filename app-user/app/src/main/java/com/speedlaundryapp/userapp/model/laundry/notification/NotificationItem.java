package com.speedlaundryapp.userapp.model.laundry.notification;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundryapp.userapp.model.laundry.transaction.TransactionItem;

public class NotificationItem {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("request_data")
    @Expose
    private String requestData;
    @SerializedName("response_data")
    @Expose
    private String responseData;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_type")
    @Expose
    private String userType;

    @SerializedName("transaction")
    @Expose
    private TransactionItem transaction;

    public TransactionItem getTransaction() {
        return transaction;
    }

    public void setTransaction(TransactionItem transaction) {
        this.transaction = transaction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getResponseData() {
        return responseData;
    }

    public void setResponseData(String responseData) {
        this.responseData = responseData;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserType() {
        return userType;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
}