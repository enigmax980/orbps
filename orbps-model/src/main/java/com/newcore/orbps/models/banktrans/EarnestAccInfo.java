package com.newcore.orbps.models.banktrans;

import java.io.Serializable;
import java.util.List;

/**
 * 保单应收账户信息
 * @author JCC 
 * 2016年10月25日 16:43:05
 */
public class EarnestAccInfo implements Serializable {
	private static final long serialVersionUID = 6927684023060721393L;

	private String applNo; 		// 投保单号
	private String accNo; 		// 帐号
	private String multiPayAccType; // 帐号所属人类别:O-组织架构树应收，I-被保人应收，P-收费组应收
	private String accPersonNo;	// 帐号所属人序号
	private Double balance; 	// 账户余额 ：默认0
	private List<MioAccInfo> mioAccInfoList; // 账户对应操作轨迹
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getAccNo() {
		return accNo;
	}
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	public String getMultiPayAccType() {
		return multiPayAccType;
	}
	public void setMultiPayAccType(String multiPayAccType) {
		this.multiPayAccType = multiPayAccType;
	}
	public String getAccPersonNo() {
		return accPersonNo;
	}
	public void setAccPersonNo(String accPersonNo) {
		this.accPersonNo = accPersonNo;
	}
	public Double getBalance() {
		return balance;
	}
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	public List<MioAccInfo> getMioAccInfoList() {
		return mioAccInfoList;
	}
	public void setMioAccInfoList(List<MioAccInfo> mioAccInfoList) {
		this.mioAccInfoList = mioAccInfoList;
	}

	

}
