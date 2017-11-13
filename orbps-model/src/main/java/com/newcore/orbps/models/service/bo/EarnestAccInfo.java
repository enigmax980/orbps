package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 保单应收账户信息
 * @author LJF 
 * 2017年2月25日 10:42:47
 */
public class EarnestAccInfo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -2965610312249545911L;
	private long accId; 		//账户信息
	private String accNo; 		// 帐号
	private String accType; 	// 帐号所属人类别:O-组织架构树应收，I-被保人应收，P-收费组应收
	private String accPersonNo;	// 帐号所属人序号
	private BigDecimal balance; // 账户余额 ：默认0
	private BigDecimal frozenBalance;  //冻结金额  默认0
	private	BigDecimal sumAnmt;  //保单账号总余额


	public BigDecimal getSumAnmt() {
		return sumAnmt;
	}
	public void setSumAnmt(BigDecimal sumAnmt) {
		this.sumAnmt = sumAnmt;
	}
	public long getAccId() {
		return accId;
	}
	public void setAccId(long accId) {
		this.accId = accId;
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
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	public BigDecimal getFrozenBalance() {
		return frozenBalance;
	}
	public void setFrozenBalance(BigDecimal frozenBalance) {
		this.frozenBalance = frozenBalance;
	}
}
