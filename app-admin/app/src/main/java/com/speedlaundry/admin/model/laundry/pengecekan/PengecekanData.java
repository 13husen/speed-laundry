package com.speedlaundry.admin.model.laundry.pengecekan;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundry.admin.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundry.admin.model.laundry.category.CategoryItem;

import java.util.List;

public class PengecekanData {

    @SerializedName("item_type")
    @Expose
    private int itemType;
    @SerializedName("weight")
    @Expose
    private int weight;
    @SerializedName("category")
    @Expose
    private CategoryItem category;

    @SerializedName("pcs")
    @Expose
    private int pcs;
    @SerializedName("clothe")
    @Expose
    private List<CategorizeClotheItem> clothe = null;

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public CategoryItem getCategory() {
        return category;
    }

    public void setCategory(CategoryItem category) {
        this.category = category;
    }


    public int getPcs() {
        return pcs;
    }

    public void setPcs(int pcs) {
        this.pcs = pcs;
    }

    public List<CategorizeClotheItem> getClothe() {
        return clothe;
    }

    public void setClothe(List<CategorizeClotheItem> clothe) {
        this.clothe = clothe;
    }

}