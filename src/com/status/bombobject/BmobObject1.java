package com.status.bombobject;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class BmobObject1 extends BmobObject {
	// ״̬����
	String StatusContent;
	// ״̬����ʱ��
	String AnncounceTime;
	// ״̬�����ĵ�ǰ��γ��
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
