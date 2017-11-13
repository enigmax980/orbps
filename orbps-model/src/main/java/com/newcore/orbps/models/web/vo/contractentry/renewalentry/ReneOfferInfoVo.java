package com.newcore.orbps.models.web.vo.contractentry.renewalentry;

import java.io.Serializable;

/**
 * 要约信息
 * @author wangyupeng
 *
 */
public class ReneOfferInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1149444656L;
	
	/**序号**/
	private String applId;
	/**险种代码**/
	private String busiPrdCode;
	/**份数**/
	private String amount;
	/**保费金额**/
	private String premium;
	/**承费**/
	private String insuredNum;
	/**保险期间**/
	private String validateDate;
	/**
	 * @return the applId
	 */
	public String getApplId() {
		return applId;
	}
	/**
	 * @param applId the applId to set
	 */
	public void setApplId(String applId) {
		this.applId = applId;
	}
	/**
	 * @return the busiPrdCode
	 */
	public String getBusiPrdCode() {
		return busiPrdCode;
	}
	/**
	 * @param busiPrdCode the busiPrdCode to set
	 */
	public void setBusiPrdCode(String busiPrdCode) {
		this.busiPrdCode = busiPrdCode;
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
	public String getValidateDate() {
		return validateDate;
	}
	/**
	 * @param validateDate the validateDate to set
	 */
	public void setValidateDate(String validateDate) {
		this.validateDate = validateDate;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OfferInfoVo [applId=" + applId + ", busiPrdCode=" + busiPrdCode + ", amount=" + amount + ", premium="
				+ premium + ", insuredNum=" + insuredNum + ", validateDate=" + validateDate + "]";
	}

}
