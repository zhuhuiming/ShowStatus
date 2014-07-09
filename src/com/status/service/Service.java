package com.status.service;

public interface Service {
	// 登录,1表示成功,2表示已经存在该用户,其他表示失败
	public abstract int LogIn(String strPersonName,// 用户名称
			String strPhoneNum,// 电话号码
			String strMac);// 设备mac地址

	// 将自己的状态插入状态表中,失败返回0,成功返回1
	public abstract int InsertStatus(String strPersonName,// 用户名
			String strContent,// 状态内容
			double dLongitude,// 经度
			double dLatitude);// 纬度

	// 插入状态查看记录数据,失败返回0,成功返回1
	public abstract int InsertSearchRecord(String strPersonName,// 用户名
			String strContent,// 状态内容
			double dLongitude,// 经度
			double dLatitude);// 纬度

	// 查看与自己状态相同的人数
	public abstract long GetSameStatusPersonNum(String strPersonName,
			String strContent, long lSeconds, double dLongitude,
			double dLatitude,int nType);//nType为1表示只查不插入数据2表示查同时插入数据

	// 获取正在查看某个状态的人数
	public abstract long GetIsSearchingPersonNum(String strPersonName,
			String strContent, long lSeconds, double dLongitude,
			double dLatitude,int nType);//nType为1表示只查不插入数据2表示查同时插入数据
    
	//获取系统时间
	public abstract String GetServiceTime();
	
	//根据mac获取账号
	public abstract String GetAccountName(String strMac);
	
	//获取注册人数
	public abstract int GetUsingPersonNum();
	
}
