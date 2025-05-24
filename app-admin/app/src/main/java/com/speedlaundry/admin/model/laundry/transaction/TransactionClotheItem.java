package com.speedlaundry.admin.model.laundry.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundry.admin.model.laundry.categorize_clothes.CategorizeClotheItem;

public class TransactionClotheItem {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("transaction_detail_id")
    @Expose
    private Integer transactionDetailId;
    @SerializedName("clothe_id")
    @Expose
    private Integer clotheId;
    @SerializedName("categorize_clothe")
    @Expose
    private CategorizeClotheItem categorizeClothe;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTransactionDetailId() {
        return transactionDetailId;
    }

    public void setTransactionDetailId(Integer transactionDetailId) {
        this.transactionDetailId = transactionDetailId;
    }

    public Integer getClotheId() {
        return clotheId;
    }

    public void setClotheId(Integer clotheId) {
        this.clotheId = clotheId;
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

    public CategorizeClotheItem getCategorizeClothe() {
        return categorizeClothe;
    }

    public void setCategorizeClothe(CategorizeClotheItem categorizeClothe) {
        this.categorizeClothe = categorizeClothe;
    }
}