package com.newcore.orbps.models.web.vo.contractentry.perinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 要约信息
 * @author wangyupeng
 *
 */
public class PerInsurOfferInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1149444556L;
	
	/**序号 **/
	private String applId;
	/**险种名称**/
	private String polName;
	/**保额**/
	private String amount;
	/**保费**/
	private String premium;
	/**承保人数**/
	private String insuredNum;
	/**保险期间**/
	private String validateDate;
	/**保费合计**/
	private String totalPremium;
	/**首期交费方式**/
	private String firstPaymentForm;
	/**交费日期**/
	private Date paymentDate;
	/**交费方式**/
	private String paymentForm;
	/**银行代码**/
	private String bankCode;
	/**银行账户**/
	private String bankAccount;
	/**银行账号**/
	private Long BankNo;
	/**原保险公司名称**/
	private String originalInsuranceName;
	/**原税优保单号**/
	private String originalTaxInsurance;
	/**是否连续续保**/
	private Double contRenewal;
	/** 特别约定 */
	private String specialPro;
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
	public String getValidateDate() {
		return validateDate;
	}
	/**
	 * @param validateDate the validateDate to set
	 */
	public void setValidateDate(String validateDate) {
		this.validateDate = validateDate;
	}
	/**
	 * @return the totalPremium
	 */
	public String getTotalPremium() {
		return totalPremium;
	}
	/**
	 * @param totalPremium the totalPremium to set
	 */
	public void setTotalPremium(String totalPremium) {
		this.totalPremium = totalPremium;
	}
	/**
	 * @return the firstPaymentForm
	 */
	public String getFirstPaymentForm() {
		return firstPaymentForm;
	}
	/**
	 * @param firstPaymentForm the firstPaymentForm to set
	 */
	public void setFirstPaymentForm(String firstPaymentForm) {
		this.firstPaymentForm = firstPaymentForm;
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
	 * @return the paymentForm
	 */
	public String getPaymentForm() {
		return paymentForm;
	}
	/**
	 * @param paymentForm the paymentForm to set
	 */
	public void setPaymentForm(String paymentForm) {
		this.paymentForm = paymentForm;
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
	 * @return the bankAccount
	 */
	public String getBankAccount() {
		return bankAccount;
	}
	/**
	 * @param bankAccount the bankAccount to set
	 */
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	/**
	 * @return the bankNo
	 */
	public Long getBankNo() {
		return BankNo;
	}
	/**
	 * @param bankNo the bankNo to set
	 */
	public void setBankNo(Long bankNo) {
		BankNo = bankNo;
	}
	/**
	 * @return the originalInsuranceName
	 */
	public String getOriginalInsuranceName() {
		return originalInsuranceName;
	}
	/**
	 * @param originalInsuranceName the originalInsuranceName to set
	 */
	public void setOriginalInsuranceName(String originalInsuranceName) {
		this.originalInsuranceName = originalInsuranceName;
	}
	/**
	 * @return the originalTaxInsurance
	 */
	public String getOriginalTaxInsurance() {
		return originalTaxInsurance;
	}
	/**
	 * @param originalTaxInsurance the originalTaxInsurance to set
	 */
	public void setOriginalTaxInsurance(String originalTaxInsurance) {
		this.originalTaxInsurance = originalTaxInsurance;
	}
	/**
	 * @return the contRenewal
	 */
	public Double getContRenewal() {
		return contRenewal;
	}
	/**
	 * @param contRenewal the contRenewal to set
	 */
	public void setContRenewal(Double contRenewal) {
		this.contRenewal = contRenewal;
	}
	/**
	 * @return the specialPro
	 */
	public String getSpecialPro() {
		return specialPro;
	}
	/**
	 * @param specialPro the specialPro to set
	 */
	public void setSpecialPro(String specialPro) {
		this.specialPro = specialPro;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OfferInfo [applId=" + applId + ", polName=" + polName + ", amount=" + amount + ", premium=" + premium
				+ ", insuredNum=" + insuredNum + ", validateDate=" + validateDate + ", totalPremium=" + totalPremium
				+ ", firstPaymentForm=" + firstPaymentForm + ", paymentDate=" + paymentDate + ", paymentForm="
				+ paymentForm + ", bankCode=" + bankCode + ", bankAccount=" + bankAccount + ", BankNo=" + BankNo
				+ ", originalInsuranceName=" + originalInsuranceName + ", originalTaxInsurance=" + originalTaxInsurance
				+ ", contRenewal=" + contRenewal + ", specialPro=" + specialPro + "]";
	}
	
}
