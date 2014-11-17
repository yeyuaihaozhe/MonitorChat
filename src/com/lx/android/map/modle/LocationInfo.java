package com.lx.android.map.modle;

import cn.bmob.v3.BmobObject;

public class LocationInfo extends BmobObject{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String phoneId;
	private String latitude;
	private String longitude;
	private String address;
	public String getPhoneId() {
		return phoneId;
	}
	public void setPhoneId(String phoneId) {
		this.phoneId = phoneId;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
}
