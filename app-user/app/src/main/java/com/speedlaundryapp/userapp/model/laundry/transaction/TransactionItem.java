package com.speedlaundryapp.userapp.model.laundry.transaction;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.speedlaundryapp.userapp.model.laundry.payment.PaymentItem;
import com.speedlaundryapp.userapp.model.laundry.type_laundry.TypeLaundryItem;
import com.speedlaundryapp.userapp.model.user.data.User;

import java.util.List;

public class TransactionItem {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("payment_id")
    @Expose
    private Integer paymentId;
    @SerializedName("invoice")
    @Expose
    private String invoice;
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("courier_id")
    @Expose
    private Integer courierId;
    @SerializedName("shipment_fix_address")
    @Expose
    private String shipmentFixAddress;
    @SerializedName("shipment_fix_name")
    @Expose
    private String shipmentFixName;
    @SerializedName("shipment_fix_phone")
    @Expose
    private String shipmentFixPhone;
    @SerializedName("type_id")
    @Expose
    private Integer typeId;
    @SerializedName("tipe")
    @Expose
    private TypeLaundryItem tipe;
    @SerializedName("shipment_coordinate_lat")
    @Expose
    private String shipmentCoordinateLat;
    @SerializedName("shipment_coordinate_long")
    @Expose
    private String shipmentCoordinateLong;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("job_done_at")
    @Expose
    private String jobDoneAt;
    @SerializedName("payment")
    @Expose
    private PaymentItem payment;
    @SerializedName("is_handled")
    @Expose
    private Integer isHandled;
    @SerializedName("handled_by")
    @Expose
    private Integer handledBy;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("transaction_detail")
    @Expose
    private List<TransactionDetail> transactionDetail = null;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Integer paymentId) {
        this.paymentId = paymentId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCourierId() {
        return courierId;
    }

    public void setCourierId(Integer courierId) {
        this.courierId = courierId;
    }

    public String getShipmentFixAddress() {
        return shipmentFixAddress;
    }

    public void setShipmentFixAddress(String shipmentFixAddress) {
        this.shipmentFixAddress = shipmentFixAddress;
    }

    public String getShipmentCoordinateLat() {
        return shipmentCoordinateLat;
    }

    public void setShipmentCoordinateLat(String shipmentCoordinateLat) {
        this.shipmentCoordinateLat = shipmentCoordinateLat;
    }

    public String getShipmentCoordinateLong() {
        return shipmentCoordinateLong;
    }

    public void setShipmentCoordinateLong(String shipmentCoordinateLong) {
        this.shipmentCoordinateLong = shipmentCoordinateLong;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public PaymentItem getPayment() {
        return payment;
    }

    public void setPayment(PaymentItem payment) {
        this.payment = payment;
    }

    public Integer getHandled() {
        return isHandled;
    }

    public void setHandled(Integer handled) {
        isHandled = handled;
    }

    public Integer getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(Integer handledBy) {
        this.handledBy = handledBy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<TransactionDetail> getTransactionDetail() {
        return transactionDetail;
    }

    public void setTransactionDetail(List<TransactionDetail> transactionDetail) {
        this.transactionDetail = transactionDetail;
    }

    public String getShipmentFixName() {
        return shipmentFixName;
    }

    public void setShipmentFixName(String shipmentFixName) {
        this.shipmentFixName = shipmentFixName;
    }

    public String getShipmentFixPhone() {
        return shipmentFixPhone;
    }

    public void setShipmentFixPhone(String shipmentFixPhone) {
        this.shipmentFixPhone = shipmentFixPhone;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public TypeLaundryItem getTipe() {
        return tipe;
    }

    public void setTipe(TypeLaundryItem tipe) {
        this.tipe = tipe;
    }

    public String getJobDoneAt() {
        return jobDoneAt;
    }

    public void setJobDoneAt(String jobDoneAt) {
        this.jobDoneAt = jobDoneAt;
    }
}
