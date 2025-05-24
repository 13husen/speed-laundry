package com.speedlaundryapp.userapp.model.laundry.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentItem {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("payment_type")
    @Expose
    private Integer paymentType;
    @SerializedName("shipping_fee")
    @Expose
    private Double shippingFee;
    @SerializedName("total_fee")
    @Expose
    private Double totalFee;
    @SerializedName("discount_fee")
    @Expose
    private Double discountFee;
    @SerializedName("nominal")
    @Expose
    private Double nominal;
    @SerializedName("admin_fee")
    @Expose
    private Double adminFee;
    @SerializedName("confirm_bank")
    @Expose
    private String confirmBank;
    @SerializedName("confirm_image")
    @Expose
    private String confirmImage;
    @SerializedName("confirm_bank_owner")
    @Expose
    private String confirmBankOwner;
    @SerializedName("confirm_bank_number")
    @Expose
    private String confirmBankNumber;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("detail")
    @Expose
    private String detail;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Double getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(Double shippingFee) {
        this.shippingFee = shippingFee;
    }

    public Double getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(Double totalFee) {
        this.totalFee = totalFee;
    }

    public Double getDiscountFee() {
        return discountFee;
    }

    public void setDiscountFee(Double discountFee) {
        this.discountFee = discountFee;
    }

    public Double getNominal() {
        return nominal;
    }

    public void setNominal(Double nominal) {
        this.nominal = nominal;
    }

    public Double getAdminFee() {
        return adminFee;
    }

    public void setAdminFee(Double adminFee) {
        this.adminFee = adminFee;
    }

    public String getConfirmBank() {
        return confirmBank;
    }

    public void setConfirmBank(String confirmBank) {
        this.confirmBank = confirmBank;
    }

    public String getConfirmImage() {
        return confirmImage;
    }

    public void setConfirmImage(String confirmImage) {
        this.confirmImage = confirmImage;
    }

    public String getConfirmBankOwner() {
        return confirmBankOwner;
    }

    public void setConfirmBankOwner(String confirmBankOwner) {
        this.confirmBankOwner = confirmBankOwner;
    }

    public String getConfirmBankNumber() {
        return confirmBankNumber;
    }

    public void setConfirmBankNumber(String confirmBankNumber) {
        this.confirmBankNumber = confirmBankNumber;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }
}
