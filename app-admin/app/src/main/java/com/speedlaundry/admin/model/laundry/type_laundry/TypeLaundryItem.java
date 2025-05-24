package com.speedlaundry.admin.model.laundry.type_laundry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TypeLaundryItem implements Parcelable {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
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

    @SerializedName("count_type")
    @Expose
    private Integer countType;
    @SerializedName("count_time")
    @Expose
    private Integer countTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
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

    public Integer getCountType() {
        return countType;
    }

    public void setCountType(Integer countType) {
        this.countType = countType;
    }

    public Integer getCountTime() {
        return countTime;
    }

    public void setCountTime(Integer countTime) {
        this.countTime = countTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.nama);
        dest.writeString(this.deskripsi);
        dest.writeString(this.fee);
        dest.writeValue(this.countType);
        dest.writeValue(this.countTime);
        dest.writeValue(this.status);
        dest.writeString(this.createdAt);
        dest.writeString(this.updatedAt);
        dest.writeString(this.deletedAt);
    }

    public void readFromParcel(Parcel source) {
        this.id = (Integer) source.readValue(Integer.class.getClassLoader());
        this.nama = source.readString();
        this.deskripsi = source.readString();
        this.fee = source.readString();
        this.countType = (Integer) source.readValue(Integer.class.getClassLoader());
        this.countTime = (Integer) source.readValue(Integer.class.getClassLoader());
        this.status = (Integer) source.readValue(Integer.class.getClassLoader());
        this.createdAt = source.readString();
        this.updatedAt = source.readString();
        this.deletedAt = source.readString();
    }

    public TypeLaundryItem() {
    }

    protected TypeLaundryItem(Parcel in) {
        this.id = (Integer) in.readValue(Integer.class.getClassLoader());
        this.nama = in.readString();
        this.deskripsi = in.readString();
        this.fee = in.readString();
        this.countType = (Integer) in.readValue(Integer.class.getClassLoader());
        this.countTime = (Integer) in.readValue(Integer.class.getClassLoader());
        this.status = (Integer) in.readValue(Integer.class.getClassLoader());
        this.createdAt = in.readString();
        this.updatedAt = in.readString();
        this.deletedAt = in.readString();
    }

    public static final Creator<TypeLaundryItem> CREATOR = new Creator<TypeLaundryItem>() {
        @Override
        public TypeLaundryItem createFromParcel(Parcel source) {
            return new TypeLaundryItem(source);
        }

        @Override
        public TypeLaundryItem[] newArray(int size) {
            return new TypeLaundryItem[size];
        }
    };
}