package com.speedlaundryapp.userapp.model.laundry.pengecekan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PengecekanClothe {
    @SerializedName("clothe_id")
    @Expose
    private Integer clotheId;

    public Integer getClotheId() {
        return clotheId;
    }

    public void setClotheId(Integer clotheId) {
        this.clotheId = clotheId;
    }
}
