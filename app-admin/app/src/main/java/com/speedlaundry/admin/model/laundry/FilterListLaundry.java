package com.speedlaundry.admin.model.laundry;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilterListLaundry {
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getNama() {
        return nama;
    }
    public void setNama(String nama) {
        this.nama = nama;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


}
