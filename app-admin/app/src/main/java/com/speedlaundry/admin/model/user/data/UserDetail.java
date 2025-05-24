package com.speedlaundry.admin.model.user.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetail {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("plate_number")
    @Expose
    private Object plateNumber;
    @SerializedName("shipment_address")
    @Expose
    private Object shipmentAddress;
    @SerializedName("saved_coordinate_lat")
    @Expose
    private Object savedCoordinateLat;
    @SerializedName("saved_coordinate_long")
    @Expose
    private Object savedCoordinateLong;
    @SerializedName("map_address_url")
    @Expose
    private Object mapAddressUrl;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(Object plateNumber) {
        this.plateNumber = plateNumber;
    }

    public Object getShipmentAddress() {
        return shipmentAddress;
    }

    public void setShipmentAddress(Object shipmentAddress) {
        this.shipmentAddress = shipmentAddress;
    }

    public Object getSavedCoordinateLat() {
        return savedCoordinateLat;
    }

    public void setSavedCoordinateLat(Object savedCoordinateLat) {
        this.savedCoordinateLat = savedCoordinateLat;
    }

    public Object getSavedCoordinateLong() {
        return savedCoordinateLong;
    }

    public void setSavedCoordinateLong(Object savedCoordinateLong) {
        this.savedCoordinateLong = savedCoordinateLong;
    }

    public Object getMapAddressUrl() {
        return mapAddressUrl;
    }

    public void setMapAddressUrl(Object mapAddressUrl) {
        this.mapAddressUrl = mapAddressUrl;
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

}
