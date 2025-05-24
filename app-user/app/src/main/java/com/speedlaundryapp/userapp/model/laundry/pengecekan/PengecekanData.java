package com.speedlaundryapp.userapp.model.laundry.pengecekan;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundryapp.userapp.model.laundry.categorize_clothes.CategorizeClotheItem;
import com.speedlaundryapp.userapp.model.laundry.category.CategoryItem;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryItem;

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
    @SerializedName("type_laundry")
    @Expose
    private TypeLaundryItem typeLaundry;
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

    public TypeLaundryItem getTypeLaundry() {
        return typeLaundry;
    }

    public void setTypeLaundry(TypeLaundryItem typeLaundry) {
        this.typeLaundry = typeLaundry;
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