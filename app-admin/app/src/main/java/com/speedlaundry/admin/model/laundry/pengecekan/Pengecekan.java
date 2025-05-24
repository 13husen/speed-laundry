package com.speedlaundry.admin.model.laundry.pengecekan;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pengecekan {
        @SerializedName("order")
        @Expose
        private List<PengecekanData> order = null;

        public List<PengecekanData> getOrder() {
            return order;
        }

        public void setOrder(List<PengecekanData> order) {
            this.order = order;
        }
}
