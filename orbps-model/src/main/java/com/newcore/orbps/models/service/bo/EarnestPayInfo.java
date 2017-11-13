package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 *暂收费支取功能--账户明细
 * 
 * @author lijifei 
 * 创建时间：2017年2月23日上午9:46:11
 */
public class EarnestPayInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1751982116076322552L;

	/*字段名：投保单号，是否必填：是 */
	private String applNo;

	/*字段名：账户，是否必填：是 */
	private String accNo;

	/*字段名：账户所属人类别，是否必填：是 */
	private String accType;

	/*字段名：账户所属人序号，是否必填：是 */
	private String accPersonNo;

	/*字段名：收付款方式，是否必填：是 */
	private String mioType;

	/*字段名：账户余额，是否必填：是 */
	private Double balance;

	/*字段名：支取金额，是否必填：是 */
	private Double plnAmnt;

	/*字段名：应付日期，是否必填：是 */
	private Date plnmioDate;

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

	public String getAccType() {
		return accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}

	public String getAccPersonNo() {
		return accPersonNo;
	}

	public void setAccPersonNo(String accPersonNo) {
		this.accPersonNo = accPersonNo;
	}

	public String getMioType() {
		return mioType;
	}

	public void setMioType(String mioType) {
		this.mioType = mioType;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Double getPlnAmnt() {
		return plnAmnt;
	}

	public void setPlnAmnt(Double plnAmnt) {
		this.plnAmnt = plnAmnt;
	}

	public Date getPlnmioDate() {
		return plnmioDate;
	}

	public void setPlnmioDate(Date plnmioDate) {
		this.plnmioDate = plnmioDate;
	}

	public EarnestPayInfo() {
		super();
		// TODO Auto-generated constructor stub
	}








}
