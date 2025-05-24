
package com.speedlaundry.admin.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LinkedTreeMap;

import java.util.ArrayList;

public class ResponseError {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("code")
    @Expose
    private Integer code = 0;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("errors")
    @Expose
    private LinkedTreeMap<String, ArrayList<String>> errors;
    @SerializedName("data")
    @Expose
    private LinkedTreeMap<String, ArrayList<String>> data;

    public LinkedTreeMap<String, ArrayList<String>> getData() {
        return data;
    }

    public void setData(LinkedTreeMap<String, ArrayList<String>> data) {
        this.data = data;
    }

    public LinkedTreeMap<String, ArrayList<String>> getErrors() {
        return errors;
    }

    public void setErrors(LinkedTreeMap<String, ArrayList<String>> errors) {
        this.errors = errors;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
