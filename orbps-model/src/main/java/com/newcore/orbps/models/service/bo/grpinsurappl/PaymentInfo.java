package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 缴费相关
 * 
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:20:14
 */
public class PaymentInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7379151567100574447L;

	/*
	 * 字段名：团单保费来源，长度：2，是否必填：是 1.团体账户付款；2.个人账户付款；3.团体个人共同付款。
	 */
	private String premSource;
	
	/* 字段名：多团体个人共同付款，团体供款比例*/
	private Double multiPartyScale;

	/* 字段名：团体个人共同付款，团体供款金额*/
	private Double multiPartyMoney;

	/* 字段名：首期扣款截至日期，是否必填：IF premSource == 3 THEN 非空 */
	private Date foreExpDate;

	/*
	 * 字段名：缴费方式，长度：2，是否必填：是 M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期
	 */
	private String moneyinItrvl;

	/*
	 * 字段名：缴费形式，长度：2，是否必填：否 C:现金；T：银行转账；R：银行交款单；W：贷款凭证；Q：支票；P：POS机；D：社保结算
	 */
	private String moneyinType;

	/* 
	 * 字段名：缴费期，是否必填：是
	 */
	private Integer moneyinDur;

	/* 
	 * 字段名：缴费期单位，长度：2，是否必填：是
	 */
	private String moneyinDurUnit;

	/* 字段名：开户银行，长度：4，是否必填：IF moneyInType == T THEN 必填 */
	private String bankCode;

	/* 字段名：开户名称，长度：48，是否必填：IF moneyInType == T THEN 必填 */
	private String bankAccName;

	/* 字段名：银行账号，长度：25，是否必填：IF moneyInType == T THEN 必填 */
	private String bankAccNo;

	/*
	 * 字段名：结算方式，长度：2，是否必填：是 N:即时结算X:组合结算D:指定日期结算L：比例结算
	 */
	private String stlType;

	/* 字段名：结算限额，是否必填：IF stlType == X THEN 必填 */
	private Double stlAmnt;

	/* 字段名：结算日期，是否必填：IF stlType == X || stlType == D THEN 必填 */
	private List<Date> stlDate;

	/* 字段名：结算比例，是否必填：IF stlType == L THEN 必填 */
	private Double stlRate;

	/*
	 * 字段名：是否需要续期扣款，长度：2，是否必填：是，说明：清汇特有 0：否；1：是。（默认为0）
	 */
	private String isRenew;

	/* 字段名：续期扣款截至日，是否必填：IF isRenew == 1 THEN 必填，说明：清汇特有 */
	private Date renewExpDate;

	/*
	 * 字段名：是否多期暂缴，长度：2，是否必填：是，说明：清汇特有 0：否；1：是。（默认为0）
	 */
	private String isMultiPay;

	/* 字段名：多期暂缴年数，是否必填：IF isMultiPay == 1 THEN 必填，说明：清汇特有 */
	private Integer mutipayTimes;

	/* 字段名：币种，长度：3，是否必填：否*/
	private String currencyCode;

	/**
	 * @return the premSource
	 */
	public String getPremSource() {
		return premSource;
	}

	/**
	 * @param premSource the premSource to set
	 */
	public void setPremSource(String premSource) {
		this.premSource = premSource;
	}

	/**
	 * @return the multiPartyScale
	 */
	public Double getMultiPartyScale() {
		return multiPartyScale;
	}

	/**
	 * @param multiPartyScale the multiPartyScale to set
	 */
	public void setMultiPartyScale(Double multiPartyScale) {
		this.multiPartyScale = multiPartyScale;
	}

	/**
	 * @return the multiPartyMoney
	 */
	public Double getMultiPartyMoney() {
		return multiPartyMoney;
	}

	/**
	 * @param multiPartyMoney the multiPartyMoney to set
	 */
	public void setMultiPartyMoney(Double multiPartyMoney) {
		this.multiPartyMoney = multiPartyMoney;
	}

	/**
	 * @return the foreExpDate
	 */
	public Date getForeExpDate() {
		return foreExpDate;
	}

	/**
	 * @param foreExpDate the foreExpDate to set
	 */
	public void setForeExpDate(Date foreExpDate) {
		this.foreExpDate = foreExpDate;
	}

	/**
	 * @return the moneyinItrvl
	 */
	public String getMoneyinItrvl() {
		return moneyinItrvl;
	}

	/**
	 * @param moneyinItrvl the moneyinItrvl to set
	 */
	public void setMoneyinItrvl(String moneyinItrvl) {
		this.moneyinItrvl = moneyinItrvl;
	}

	/**
	 * @return the moneyinType
	 */
	public String getMoneyinType() {
		return moneyinType;
	}

	/**
	 * @param moneyinType the moneyinType to set
	 */
	public void setMoneyinType(String moneyinType) {
		this.moneyinType = moneyinType;
	}

	/**
	 * @return the moneyinDur
	 */
	public Integer getMoneyinDur() {
		return moneyinDur;
	}

	/**
	 * @param moneyinDur the moneyinDur to set
	 */
	public void setMoneyinDur(Integer moneyinDur) {
		this.moneyinDur = moneyinDur;
	}

	/**
	 * @return the moneyinDurUnit
	 */
	public String getMoneyinDurUnit() {
		return moneyinDurUnit;
	}

	/**
	 * @param moneyinDurUnit the moneyinDurUnit to set
	 */
	public void setMoneyinDurUnit(String moneyinDurUnit) {
		this.moneyinDurUnit = moneyinDurUnit;
	}

	/**
	 * @return the bankCode
	 */
	public String getBankCode() {
		return bankCode;
	}

	/**
	 * @param bankCode the bankCode to set
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	/**
	 * @return the bankAccName
	 */
	public String getBankAccName() {
		return bankAccName;
	}

	/**
	 * @param bankAccName the bankAccName to set
	 */
	public void setBankAccName(String bankAccName) {
		this.bankAccName = bankAccName;
	}

	/**
	 * @return the bankAccNo
	 */
	public String getBankAccNo() {
		return bankAccNo;
	}

	/**
	 * @param bankAccNo the bankAccNo to set
	 */
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	/**
	 * @return the stlType
	 */
	public String getStlType() {
		return stlType;
	}

	/**
	 * @param stlType the stlType to set
	 */
	public void setStlType(String stlType) {
		this.stlType = stlType;
	}

	/**
	 * @return the stlAmnt
	 */
	public Double getStlAmnt() {
		return stlAmnt;
	}

	/**
	 * @param stlAmnt the stlAmnt to set
	 */
	public void setStlAmnt(Double stlAmnt) {
		this.stlAmnt = stlAmnt;
	}

	/**
	 * @return the stlDate
	 */
	public List<Date> getStlDate() {
		return stlDate;
	}

	/**
	 * @param stlDate the stlDate to set
	 */
	public void setStlDate(List<Date> stlDate) {
		this.stlDate = stlDate;
	}

	/**
	 * @return the stlRate
	 */
	public Double getStlRate() {
		return stlRate;
	}

	/**
	 * @param stlRate the stlRate to set
	 */
	public void setStlRate(Double stlRate) {
		this.stlRate = stlRate;
	}

	/**
	 * @return the isRenew
	 */
	public String getIsRenew() {
		return isRenew;
	}

	/**
	 * @param isRenew the isRenew to set
	 */
	public void setIsRenew(String isRenew) {
		this.isRenew = isRenew;
	}

	/**
	 * @return the renewExpDate
	 */
	public Date getRenewExpDate() {
		return renewExpDate;
	}

	/**
	 * @param renewExpDate the renewExpDate to set
	 */
	public void setRenewExpDate(Date renewExpDate) {
		this.renewExpDate = renewExpDate;
	}

	/**
	 * @return the isMultiPay
	 */
	public String getIsMultiPay() {
		return isMultiPay;
	}

	/**
	 * @param isMultiPay the isMultiPay to set
	 */
	public void setIsMultiPay(String isMultiPay) {
		this.isMultiPay = isMultiPay;
	}

	/**
	 * @return the mutipayTimes
	 */
	public Integer getMutipayTimes() {
		return mutipayTimes;
	}

	/**
	 * @param mutipayTimes the mutipayTimes to set
	 */
	public void setMutipayTimes(Integer mutipayTimes) {
		this.mutipayTimes = mutipayTimes;
	}

	/**
	 * @return the currencyCode
	 */
	public String getCurrencyCode() {
		return currencyCode;
	}

	/**
	 * @param currencyCode the currencyCode to set
	 */
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

}
