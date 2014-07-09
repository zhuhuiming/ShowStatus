package com.status.bombobject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class BmobObject1 extends BmobObject {
	// 状态内容
	String StatusContent;
	// 状态发布时间
	String AnncounceTime;
	// 状态发布的当前经纬度
	BmobGeoPoint geopoint;

	public String getStatusContent() {
		return StatusContent;
	}

	public void setStatusContent(String strcontent) {
		this.StatusContent = strcontent;
	}

	public String getAnncounceTime() {
		return AnncounceTime;
	}

	public void setAnncounceTime(String strtime) {
		this.AnncounceTime = strtime;
	}

	public BmobGeoPoint getgeopoint() {
		return geopoint;
	}

	public void setgeopoint(BmobGeoPoint geo) {
		this.geopoint = geo;
	}
}
