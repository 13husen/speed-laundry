package com.speedlaundry.admin.model.laundry.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundry.admin.model.laundry.category.CategoryItem;

import java.util.List;

public class TransactionDetail {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("item_type")
    @Expose
    private Integer itemType;
    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;
    @SerializedName("type_id")
    @Expose
    private Integer typeId;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("category_id")
    @Expose
    private Integer categoryId;
    @SerializedName("amount_fee")
    @Expose
    private String amountFee;
    @SerializedName("pcs")
    @Expose
    private Integer pcs;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("kategori")
    @Expose
    private CategoryItem kategori;
    @SerializedName("pakaian")
    @Expose
    private List<TransactionClotheItem> pakaian = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getAmountFee() {
        return amountFee;
    }

    public void setAmountFee(String amountFee) {
        this.amountFee = amountFee;
    }

    public Integer getPcs() {
        return pcs;
    }

    public void setPcs(Integer pcs) {
        this.pcs = pcs;
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

    public CategoryItem getKategori() {
        return kategori;
    }

    public void setKategori(CategoryItem kategori) {
        this.kategori = kategori;
    }

    public List<TransactionClotheItem> getPakaian() {
        return pakaian;
    }

    public void setPakaian(List<TransactionClotheItem> pakaian) {
        this.pakaian = pakaian;
    }
}