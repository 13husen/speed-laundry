package com.speedlaundry.admin.model;

import com.google.gson.annotations.SerializedName;

public class SessionData{

	@SerializedName("device_id")
	public String deviceId;

	@SerializedName("ip")
	public String ip;

	@SerializedName("imei")
	public String imei;

	@SerializedName("model")
	public String model;

	@SerializedName("brand")
	public String brand;

	public void setDeviceId(String deviceId){
		this.deviceId = deviceId;
	}

	public String getDeviceId(){
		return deviceId;
	}

	public void setIp(String ip){
		this.ip = ip;
	}

	public String getIp(){
		return ip;
	}

	public void setImei(String imei){
		this.imei = imei;
	}

	public String getImei(){
		return imei;
	}

	public void setModel(String model){
		this.model = model;
	}

	public String getModel(){
		return model;
	}

	public void setBrand(String brand){
		this.brand = brand;
	}

	public String getBrand(){
		return brand;
	}

	@Override
 	public String toString(){
		return 
			"SessionData{" + 
			"device_id = '" + deviceId + '\'' + 
			",ip = '" + ip + '\'' + 
			",imei = '" + imei + '\'' + 
			",model = '" + model + '\'' + 
			",brand = '" + brand + '\'' + 
			"}";
		}
}