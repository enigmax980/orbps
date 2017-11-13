package com.newcore.orbps.models.service.bo;

import java.io.Serializable;

/**
 * @author huanglong
 * @date 2016年10月24日
 * 内容:参与承保单位信息
 */
public class PeriodComInsur implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -74388932524765493L;
	/*
	 * 保单号
	 */
	private String cntrNo;
	/*
	 * 单位名称
	 */
	private String custName;
	/*
	 * 单件保单人数
	 */
	private long handoverNum;
	/*
	 * 备注
	 */
	private String remark;
	public PeriodComInsur() {
		super();
	}
	/**
	 * @return cntrNo
	 */
	public String getCntrNo() {
		return cntrNo;
	}
	/**
	 * @param cntrNo 要设置的 cntrNo
	 */
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	/**
	 * @return custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * @param custName 要设置的 custName
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}

	/**
	 * @return the handoverNum
	 */
	public long getHandoverNum() {
		return handoverNum;
	}
	/**
	 * @param handoverNum the handoverNum to set
	 */
	public void setHandoverNum(long handoverNum) {
		this.handoverNum = handoverNum;
	}
	/**
	 * @return remark
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark 要设置的 remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
