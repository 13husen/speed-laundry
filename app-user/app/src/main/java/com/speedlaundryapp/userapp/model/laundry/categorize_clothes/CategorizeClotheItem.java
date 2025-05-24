package com.speedlaundryapp.userapp.model.laundry.categorize_clothes;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategorizeClotheItem implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("clothe")
    @Expose
    private String clothe;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("fee")
    @Expose
    private String fee;
    @SerializedName("status")
    @Expose
    private Integer status;
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

    public String getClothe() {
        return clothe;
    }

    public void setClothe(String clothe) {
        this.clothe = clothe;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.clothe);
        dest.writeString(this.description);
        dest.writeString(this.fee);
        dest.writeValue(this.status);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Integer) source.readValue(Integer.class.getClassLoader());
        this.clothe = source.readString();
        this.description = source.readString();
        this.fee = source.readString();
        this.status = (Integer) source.readValue(Integer.class.getClassLoader());
        this.createdAt = source.readString();
        this.updatedAt = source.readString();
        this.deletedAt = source.readString();
    }

    public CategorizeClotheItem() {
    }

    protected CategorizeClotheItem(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.clothe = in.readString();
        this.description = in.readString();
        this.fee = in.readString();
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Parcelable.Creator<CategorizeClotheItem> CREATOR = new Parcelable.Creator<CategorizeClotheItem>() {
        @Override
        public CategorizeClotheItem createFromParcel(Parcel source) {
            return new CategorizeClotheItem(source);
        }

        @Override
        public CategorizeClotheItem[] newArray(int size) {
            return new CategorizeClotheItem[size];
        }
    };
}