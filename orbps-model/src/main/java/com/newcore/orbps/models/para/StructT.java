package com.newcore.orbps.models.para;

import java.io.Serializable;

/**
 * 团单后台代码传参
 * @author lijifei
 * 时间 ：2016年8月3日 20:04:22
 */
public class StructT implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8898015868205868748L;

	//参数值，对应原c代码二维数组中a[0]
	private String strBranch; 
	
	//参数值，对应原c代码二维数组中a[1]
	private String strSysdate; 
	
	//参数值，对应原c代码二维数组中a[2]
	private String strAppType; 
	
	//参数值，对应原c代码二维数组中a[3]
	private String strMrType; 
	
	//参数值，对应原c代码二维数组中a[4]
	private String strInsurDurUnit;
	//参数值，
	private int iCount;
	public String getStrBranch() {
		return strBranch;
	}
	public void setStrBranch(String strBranch) {
		this.strBranch = strBranch;
	}
	public String getStrSysdate() {
		return strSysdate;
	}
	public void setStrSysdate(String strSysdate) {
		this.strSysdate = strSysdate;
	}
	public String getStrAppType() {
		return strAppType;
	}
	public void setStrAppType(String strAppType) {
		this.strAppType = strAppType;
	}
	public String getStrMrType() {
		return strMrType;
	}
	public void setStrMrType(String strMrType) {
		this.strMrType = strMrType;
	}
	public String getStrInsurDurUnit() {
		return strInsurDurUnit;
	}
	public void setStrInsurDurUnit(String strInsurDurUnit) {
		this.strInsurDurUnit = strInsurDurUnit;
	}
	public int getiCount() {
		return iCount;
	}
	public void setiCount(int iCount) {
		this.iCount = iCount;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public StructT(String strBranch, String strSysdate, String strAppType, String strMrType, String strInsurDurUnit,
			int iCount) {
		super();
		this.strBranch = strBranch;
		this.strSysdate = strSysdate;
		this.strAppType = strAppType;
		this.strMrType = strMrType;
		this.strInsurDurUnit = strInsurDurUnit;
		this.iCount = iCount;
	}
	public StructT() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

	
	
	
}
