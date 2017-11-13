package com.newcore.orbps.models.web.vo.contractentry.longinsuranceentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 要约信息
 * @author wangyupeng
 *
 */
public class LongInsurOfferInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1149444553L;
	
	/**序号 **/
	private String applId;
	/**险种名称**/
	private String polName;
	/**保额**/
	private String insurSum;
	/**保费**/
	private String premium;
	/**承保人数**/
	private String insuredNum;
	/**保险期间**/
	private String insurDur;
	/**保费合计**/
	private String sumPrem;
	/**首期交费形式**/
	private String firstPaymentForm;
	/**交费日期**/
	private Date paymentDate;
	/**银行代码**/
	private String bankCode;
	/**转投保单单号**/
	private String reprintInsuredNo;
	/**银行账号**/
	private Long BankNo;
	/**投保日期**/
	private Date insureDate;
	/**续保保费计算方式**/
	private String renewalCalculationMethod;
	/**是否连续续保**/
	private Double contRenewal;
	/**保单性质**/
	private String policyNature;
	/**指定生效日**/
	private Date emergencyPerson;
	/**紧急联络人电话**/
	private String designatedEffectiveDate;
	/**人工核保**/
	private Double manualUnderwriting;
	/**合同争议处理方式 **/
	private String disputeHandling;
	/**仲裁委员会名称**/
	private String arbitrationName;
	/**同业公司人身保险金额合计**/
	private String totalInsuranceAmount;
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
	 * @return the insurSum
	 */
	public String getInsurSum() {
		return insurSum;
	}
	/**
	 * @param insurSum the insurSum to set
	 */
	public void setInsurSum(String insurSum) {
		this.insurSum = insurSum;
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
	 * @return the insurDur
	 */
	public String getInsurDur() {
		return insurDur;
	}
	/**
	 * @param insurDur the insurDur to set
	 */
	public void setInsurDur(String insurDur) {
		this.insurDur = insurDur;
	}
	/**
	 * @return the sumPrem
	 */
	public String getSumPrem() {
		return sumPrem;
	}
	/**
	 * @param sumPrem the sumPrem to set
	 */
	public void setSumPrem(String sumPrem) {
		this.sumPrem = sumPrem;
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
	 * @return the reprintInsuredNo
	 */
	public String getReprintInsuredNo() {
		return reprintInsuredNo;
	}
	/**
	 * @param reprintInsuredNo the reprintInsuredNo to set
	 */
	public void setReprintInsuredNo(String reprintInsuredNo) {
		this.reprintInsuredNo = reprintInsuredNo;
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
	 * @return the insureDate
	 */
	public Date getInsureDate() {
		return insureDate;
	}
	/**
	 * @param insureDate the insureDate to set
	 */
	public void setInsureDate(Date insureDate) {
		this.insureDate = insureDate;
	}
	/**
	 * @return the renewalCalculationMethod
	 */
	public String getRenewalCalculationMethod() {
		return renewalCalculationMethod;
	}
	/**
	 * @param renewalCalculationMethod the renewalCalculationMethod to set
	 */
	public void setRenewalCalculationMethod(String renewalCalculationMethod) {
		this.renewalCalculationMethod = renewalCalculationMethod;
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
	 * @return the policyNature
	 */
	public String getPolicyNature() {
		return policyNature;
	}
	/**
	 * @param policyNature the policyNature to set
	 */
	public void setPolicyNature(String policyNature) {
		this.policyNature = policyNature;
	}
	/**
	 * @return the emergencyPerson
	 */
	public Date getEmergencyPerson() {
		return emergencyPerson;
	}
	/**
	 * @param emergencyPerson the emergencyPerson to set
	 */
	public void setEmergencyPerson(Date emergencyPerson) {
		this.emergencyPerson = emergencyPerson;
	}
	/**
	 * @return the designatedEffectiveDate
	 */
	public String getDesignatedEffectiveDate() {
		return designatedEffectiveDate;
	}
	/**
	 * @param designatedEffectiveDate the designatedEffectiveDate to set
	 */
	public void setDesignatedEffectiveDate(String designatedEffectiveDate) {
		this.designatedEffectiveDate = designatedEffectiveDate;
	}
	/**
	 * @return the manualUnderwriting
	 */
	public Double getManualUnderwriting() {
		return manualUnderwriting;
	}
	/**
	 * @param manualUnderwriting the manualUnderwriting to set
	 */
	public void setManualUnderwriting(Double manualUnderwriting) {
		this.manualUnderwriting = manualUnderwriting;
	}
	/**
	 * @return the disputeHandling
	 */
	public String getDisputeHandling() {
		return disputeHandling;
	}
	/**
	 * @param disputeHandling the disputeHandling to set
	 */
	public void setDisputeHandling(String disputeHandling) {
		this.disputeHandling = disputeHandling;
	}
	/**
	 * @return the arbitrationName
	 */
	public String getArbitrationName() {
		return arbitrationName;
	}
	/**
	 * @param arbitrationName the arbitrationName to set
	 */
	public void setArbitrationName(String arbitrationName) {
		this.arbitrationName = arbitrationName;
	}
	/**
	 * @return the totalInsuranceAmount
	 */
	public String getTotalInsuranceAmount() {
		return totalInsuranceAmount;
	}
	/**
	 * @param totalInsuranceAmount the totalInsuranceAmount to set
	 */
	public void setTotalInsuranceAmount(String totalInsuranceAmount) {
		this.totalInsuranceAmount = totalInsuranceAmount;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OfferInfo [applId=" + applId + ", polName=" + polName + ", insurSum=" + insurSum + ", premium="
				+ premium + ", insuredNum=" + insuredNum + ", insurDur=" + insurDur + ", sumPrem=" + sumPrem
				+ ", firstPaymentForm=" + firstPaymentForm + ", paymentDate=" + paymentDate + ", bankCode=" + bankCode
				+ ", reprintInsuredNo=" + reprintInsuredNo + ", BankNo=" + BankNo + ", insureDate=" + insureDate
				+ ", renewalCalculationMethod=" + renewalCalculationMethod + ", contRenewal=" + contRenewal
				+ ", policyNature=" + policyNature + ", emergencyPerson=" + emergencyPerson
				+ ", designatedEffectiveDate=" + designatedEffectiveDate + ", manualUnderwriting=" + manualUnderwriting
				+ ", disputeHandling=" + disputeHandling + ", arbitrationName=" + arbitrationName
				+ ", totalInsuranceAmount=" + totalInsuranceAmount + "]";
	}
	
}
