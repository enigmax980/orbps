package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;

/**
 * 要约信息Vo
 * @author jincong
 *
 */
public class LoanAddBsInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030007L;
	
	/** 险种名称 */
	private String polName;
	/** 保额 */
	private String amount;
	/** 保费 */
	private String premium;
	/** 承保人数 */
	private String insuredNum;
	/** 保险期间 */
	private Double validateDate;
	
	/**
	 * @return the polName
	 */
	public String getPolName() {
		return polName;
	}
	/**
	 * @param polName the polName to set
	 */
	public void setPolName(String polName) {
		this.polName = polName;
	}
	/**
	 * @return the amount
	 */
	public String getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(String amount) {
		this.amount = amount;
	}
	/**
	 * @return the premium
	 */
	public String getPremium() {
		return premium;
	}
	/**
	 * @param premium the premium to set
	 */
	public void setPremium(String premium) {
		this.premium = premium;
	}
	/**
	 * @return the insuredNum
	 */
	public String getInsuredNum() {
		return insuredNum;
	}
	/**
	 * @param insuredNum the insuredNum to set
	 */
	public void setInsuredNum(String insuredNum) {
		this.insuredNum = insuredNum;
	}
	/**
	 * @return the validateDate
	 */
	public Double getValidateDate() {
		return validateDate;
	}
	/**
	 * @param validateDate the validateDate to set
	 */
	public void setValidateDate(Double validateDate) {
		this.validateDate = validateDate;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BsInfoVo [polName=" + polName + ", amount=" + amount + ", premium=" + premium + ", insuredNum="
				+ insuredNum + ", validateDate=" + validateDate + "]";
	}
	
}
