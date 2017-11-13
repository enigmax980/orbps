package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 缴费信息
 * @author wangyanjie
 *
 */
public class SgGrpPayInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1145488233L;
	
	/** 缴费方式 */
	private String moneyItrvl;
	/** 缴费形式 */
	private String moneyinType;
	/** 保费来源 */
	private String premFrom;
	/** 缴费开户行 */
	private String bankCode;
	/** 开户名 */
	private String bankBranchName;
	/** 账号 */
	private String bankaccNo;
	/** 结算方式 */
	private String paymentType;
	/** 结算限额 */
	private Double paymentLimit;
	/** 结算日期 */
	private Date paymentDate;
	/** 是否续期扣款 */
	private String renewalChargeFlag;
	/** 续期扣款期数 */
	private String renewalDebitNum ;
	/** 扣款截止日期  */
	private Date chargeDeadline;
	/** 是否多期暂交 */
	private String multiRevFlag;
	/** 多期暂交年数 */
	private Integer multiTempYear;
	/** 是否续保*/
	private String renewFlag;
	/** 续保扣款期数 */
	private String renewChargeNum;
	/**供款比例*/
	private Double multiPartyScale;
	/**供款金额*/
	private Double multiPartyMoney;
	
	
	/**
	 * @return the moneyItrvl
	 */
	public String getMoneyItrvl() {
		return moneyItrvl;
	}
	/**
	 * @param moneyItrvl the moneyItrvl to set
	 */
	public void setMoneyItrvl(String moneyItrvl) {
		this.moneyItrvl = moneyItrvl;
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
	 * @return the premFrom
	 */
	public String getPremFrom() {
		return premFrom;
	}
	/**
	 * @param premFrom the premFrom to set
	 */
	public void setPremFrom(String premFrom) {
		this.premFrom = premFrom;
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
	 * @return the bankBranchName
	 */
	public String getBankBranchName() {
		return bankBranchName;
	}
	/**
	 * @param bankBranchName the bankBranchName to set
	 */
	public void setBankBranchName(String bankBranchName) {
		this.bankBranchName = bankBranchName;
	}
	/**
	 * @return the bankaccNo
	 */
	public String getBankaccNo() {
		return bankaccNo;
	}
	/**
	 * @param bankaccNo the bankaccNo to set
	 */
	public void setBankaccNo(String bankaccNo) {
		this.bankaccNo = bankaccNo;
	}
	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}
	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}
	/**
	 * @return the paymentLimit
	 */
	public Double getPaymentLimit() {
		return paymentLimit;
	}
	/**
	 * @param paymentLimit the paymentLimit to set
	 */
	public void setPaymentLimit(Double paymentLimit) {
		this.paymentLimit = paymentLimit;
	}
	/**
	 * @return the paymentDate
	 */
	public Date getPaymentDate() {
		return paymentDate;
	}
	/**
	 * @param paymentDate the paymentDate to set
	 */
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	/**
	 * @return the renewalChargeFlag
	 */
	public String getRenewalChargeFlag() {
		return renewalChargeFlag;
	}
	/**
	 * @param renewalChargeFlag the renewalChargeFlag to set
	 */
	public void setRenewalChargeFlag(String renewalChargeFlag) {
		this.renewalChargeFlag = renewalChargeFlag;
	}
	/**
	 * @return the renewalDebitNum
	 */
	public String getRenewalDebitNum() {
		return renewalDebitNum;
	}
	/**
	 * @param renewalDebitNum the renewalDebitNum to set
	 */
	public void setRenewalDebitNum(String renewalDebitNum) {
		this.renewalDebitNum = renewalDebitNum;
	}

	/**
     * @return the chargeDeadline
     */
    public Date getChargeDeadline() {
        return chargeDeadline;
    }
    /**
     * @param chargeDeadline the chargeDeadline to set
     */
    public void setChargeDeadline(Date chargeDeadline) {
        this.chargeDeadline = chargeDeadline;
    }
    /**
	 * @return the multiRevFlag
	 */
	public String getMultiRevFlag() {
		return multiRevFlag;
	}
	/**
	 * @param multiRevFlag the multiRevFlag to set
	 */
	public void setMultiRevFlag(String multiRevFlag) {
		this.multiRevFlag = multiRevFlag;
	}
	
	/**
     * @return the multiTempYear
     */
    public Integer getMultiTempYear() {
        return multiTempYear;
    }
    /**
     * @param multiTempYear the multiTempYear to set
     */
    public void setMultiTempYear(Integer multiTempYear) {
        this.multiTempYear = multiTempYear;
    }
    /**
	 * @return the renewFlag
	 */
	public String getRenewFlag() {
		return renewFlag;
	}
	/**
	 * @param renewFlag the renewFlag to set
	 */
	public void setRenewFlag(String renewFlag) {
		this.renewFlag = renewFlag;
	}
	/**
	 * @return the renewChargeNum
	 */
	public String getRenewChargeNum() {
		return renewChargeNum;
	}
	/**
	 * @param renewChargeNum the renewChargeNum to set
	 */
	public void setRenewChargeNum(String renewChargeNum) {
		this.renewChargeNum = renewChargeNum;
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
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SgGrpPayInfoVo [moneyItrvl=" + moneyItrvl + ", moneyinType=" + moneyinType + ", premFrom=" + premFrom
                + ", bankCode=" + bankCode + ", bankBranchName=" + bankBranchName + ", bankaccNo=" + bankaccNo
                + ", paymentType=" + paymentType + ", paymentLimit=" + paymentLimit + ", paymentDate=" + paymentDate
                + ", renewalChargeFlag=" + renewalChargeFlag + ", renewalDebitNum=" + renewalDebitNum
                + ", chargeDeadline=" + chargeDeadline + ", multiRevFlag=" + multiRevFlag + ", multiTempYear="
                + multiTempYear + ", renewFlag=" + renewFlag + ", renewChargeNum=" + renewChargeNum + ",multiPartyScale="
                + multiPartyScale +",multiPartyMoney=" + multiPartyMoney +"]";
    }
}
