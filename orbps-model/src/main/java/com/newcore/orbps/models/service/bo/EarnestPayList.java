package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.List;

/**
 *暂收费支取功能--支取账户明细
 * 
 * @author lijifei 
 * 创建时间：2017年2月23日上午9:46:11
 */
public class EarnestPayList implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/*字段名：投保单号，是否必填：是 */
	private String applNo;

	/*
	 * 字段名：需支取数量，是否必填：是
	 */
	private Integer accNum;

	/*
	 * 是否全部支取标记 1-是，0-否，是否必填：是
	 */
	private String payAllFlag; 

	/*
	 * 字段名：支取信息，是否必填：是
	 */
	private List< EarnestPayInfo>  earnestPayInfoList;



	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public Integer getAccNum() {
		return accNum;
	}

	public void setAccNum(Integer accNum) {
		this.accNum = accNum;
	}

	public String getPayAllFlag() {
		return payAllFlag;
	}

	public void setPayAllFlag(String payAllFlag) {
		this.payAllFlag = payAllFlag;
	}

	public List<EarnestPayInfo> getEarnestPayInfoList() {
		return earnestPayInfoList;
	}

	public void setEarnestPayInfoList(List<EarnestPayInfo> earnestPayInfoList) {
		this.earnestPayInfoList = earnestPayInfoList;
	}

	public EarnestPayList() {
		super();
		// TODO Auto-generated constructor stub
	}






}
