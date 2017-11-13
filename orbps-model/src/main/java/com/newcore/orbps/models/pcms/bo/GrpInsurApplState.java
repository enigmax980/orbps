package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

public class GrpInsurApplState implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1985757951073006371L;

	private String newApplStat; //新单状态
	
	private String cgNo;       //组合保单号
	
	private String cntrFlag;   // 帽子入库标志
	
	private String listFlag;    //清单入库标志
	
	private String mioFlag;    //转保费完成标志
	
	private String cvFlag;      //回执核销完成标志
	
	private Date careteTime;   //查询时间
	
	private String retCode;    //查询结果  0 成功 1 失败
	
	private String errMsg;     //错误详细信息
	
	
	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getNewApplStat() {
		return newApplStat;
	}

	public void setNewApplStat(String newApplStat) {
		this.newApplStat = newApplStat;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getCntrFlag() {
		return cntrFlag;
	}

	public void setCntrFlag(String cntrFlag) {
		this.cntrFlag = cntrFlag;
	}

	public String getListFlag() {
		return listFlag;
	}

	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}

	public String getMioFlag() {
		return mioFlag;
	}

	public void setMioFlag(String mioFlag) {
		this.mioFlag = mioFlag;
	}

	public String getCvFlag() {
		return cvFlag;
	}

	public void setCvFlag(String cvFlag) {
		this.cvFlag = cvFlag;
	}


	public Date getCareteTime() {
		return careteTime;
	}

	public void setCareteTime(Date careteTime) {
		this.careteTime = careteTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}
