package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 契约收费检查返回List的Bo
 */
public class MioFirstPremchkRetInfoList implements Serializable {

	
	private static final long serialVersionUID = 1619291926606783536L;
	
	// 系统号
	private String mgBranch;
	// 投保单号
	private String applNo;
	//险种
	private String polCode;
	//应收标识id
	private String plnmioRecId;
	//应收金额
	private double plmioAmnt;
	//实收金额
	private double  mioAmnt;
	//收费完成标记
	private String miFinMark;
	//失败原因
	private String failCode;
	//流水时间
	private Date mioLogUpdTime ;
	
	public Date getMioLogUpdTime() {
		return mioLogUpdTime;
	}
	public void setMioLogUpdTime(Date mioLogUpdTime) {
		this.mioLogUpdTime = mioLogUpdTime;
	}
	
	
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(String plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
	public double getPlmioAmnt() {
		return plmioAmnt;
	}
	public void setPlmioAmnt(double plmioAmnt) {
		this.plmioAmnt = plmioAmnt;
	}
	public double getMioAmnt() {
		return mioAmnt;
	}
	public void setMioAmnt(double mioAmnt) {
		this.mioAmnt = mioAmnt;
	}
	public String getMiFinMark() {
		return miFinMark;
	}
	public void setMiFinMark(String miFinMark) {
		this.miFinMark = miFinMark;
	}
	public String getFailCode() {
		return failCode;
	}
	public void setFailCode(String failCode) {
		this.failCode = failCode;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMgBranch() {
		return mgBranch;
	}
	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}
	
}
