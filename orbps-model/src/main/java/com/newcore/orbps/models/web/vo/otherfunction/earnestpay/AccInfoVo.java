package com.newcore.orbps.models.web.vo.otherfunction.earnestpay;

import java.io.Serializable;
/**
 * 账户信息
 * @author jincong 
 *
 */

public class AccInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 12224560000012L;
	
	/** 投保单号 */
	private String applNo;
	/** 账户号 */
	private String accNo;
	/** 账户所属人类型 */
	private String accType;
	/** 账户所属人序号 */
	private String accPersonNo;
	/** 余额*/
	private Double balance;
	/** 支取金额*/
	private Double payAmount;
	/** 总余额*/
	private Double sumBalance;
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the accNo
	 */
	public String getAccNo() {
		return accNo;
	}
	/**
	 * @param accNo the accNo to set
	 */
	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}
	/**
	 * @return the accType
	 */
	public String getAccType() {
		return accType;
	}
	/**
	 * @param accType the accType to set
	 */
	public void setAccType(String accType) {
		this.accType = accType;
	}
	/**
	 * @return the accPersonNo
	 */
	public String getAccPersonNo() {
		return accPersonNo;
	}
	/**
	 * @param accPersonNo the accPersonNo to set
	 */
	public void setAccPersonNo(String accPersonNo) {
		this.accPersonNo = accPersonNo;
	}
	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}
	/**
	 * @return the payAmount
	 */
	public Double getPayAmount() {
		return payAmount;
	}
	/**
	 * @param payAmount the payAmount to set
	 */
	public void setPayAmount(Double payAmount) {
		this.payAmount = payAmount;
	}
	
	public Double getSumBalance() {
		return sumBalance;
	}
	public void setSumBalance(Double sumBalance) {
		this.sumBalance = sumBalance;
	}
	@Override
	public String toString() {
		return "AccInfoVo [applNo=" + applNo + ", accNo=" + accNo + ", accType=" + accType + ", accPersonNo="
				+ accPersonNo + ", balance=" + balance + ", payAmount=" + payAmount + ", sumBalance=" + sumBalance
				+ "]";
	}
	
	
}
