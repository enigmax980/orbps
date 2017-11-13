package com.newcore.orbps.models.web.vo.contractentry.renewalentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 	其他信息
 * @author wangyupeng
 *
 */
public class ReneOtherInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1444454666L;
	
	/**争议处理方式**/
	private String disputeHandling;
	/**姓名**/
	private String Name;
	/**生效频率**/
	private String effectiveFrequency;
	/**有效日期**/
	private Date Term;
	/**保险期间**/
	private String insurancePeriod;
	/**结算方式**/
	private String settlementMethod;
	/**结算限额**/
	private String settlementQuota;
	/**结算日期**/
	private Date settlementlDate;
	/**缴费形式**/
	private String paymentForm;
	/**缴费方式**/
	private String paymentMethod;
	/**首期扣款日**/
	private Date initialStart;
	/**扣款截止日**/
	private Date deadlineDay;
	/**保费来源**/
	private String premiumSource;
	/**银行代码**/
	private String bankCode;
	/**银行名称**/
	private String bankName;
	/**银行账户**/
	private String bankAccount;
	/**异常报告**/
	private String exceptionReport;
	/**清单标志**/
	private String listFlag;
	/**清单打印**/
	private String listPrint;
	/**个人凭证**/
	private String personalDigitalID;
	/**保单类型**/
	private String policyType;
	/**赠送保险标志**/
	private String giftSign;
	/**投保资料影像/档案文件上传**/
	private String fileTal;
	/**清单导入**/
	private String fileVal;
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
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the effectiveFrequency
	 */
	public String getEffectiveFrequency() {
		return effectiveFrequency;
	}
	/**
	 * @param effectiveFrequency the effectiveFrequency to set
	 */
	public void setEffectiveFrequency(String effectiveFrequency) {
		this.effectiveFrequency = effectiveFrequency;
	}
	/**
	 * @return the term
	 */
	public Date getTerm() {
		return Term;
	}
	/**
	 * @param term the term to set
	 */
	public void setTerm(Date term) {
		Term = term;
	}
	/**
	 * @return the insurancePeriod
	 */
	public String getInsurancePeriod() {
		return insurancePeriod;
	}
	/**
	 * @param insurancePeriod the insurancePeriod to set
	 */
	public void setInsurancePeriod(String insurancePeriod) {
		this.insurancePeriod = insurancePeriod;
	}
	/**
	 * @return the settlementMethod
	 */
	public String getSettlementMethod() {
		return settlementMethod;
	}
	/**
	 * @param settlementMethod the settlementMethod to set
	 */
	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}
	/**
	 * @return the settlementQuota
	 */
	public String getSettlementQuota() {
		return settlementQuota;
	}
	/**
	 * @param settlementQuota the settlementQuota to set
	 */
	public void setSettlementQuota(String settlementQuota) {
		this.settlementQuota = settlementQuota;
	}
	/**
	 * @return the settlementlDate
	 */
	public Date getSettlementlDate() {
		return settlementlDate;
	}
	/**
	 * @param settlementlDate the settlementlDate to set
	 */
	public void setSettlementlDate(Date settlementlDate) {
		this.settlementlDate = settlementlDate;
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
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}
	/**
	 * @param paymentMethod the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	/**
	 * @return the initialStart
	 */
	public Date getInitialStart() {
		return initialStart;
	}
	/**
	 * @param initialStart the initialStart to set
	 */
	public void setInitialStart(Date initialStart) {
		this.initialStart = initialStart;
	}
	/**
	 * @return the deadlineDay
	 */
	public Date getDeadlineDay() {
		return deadlineDay;
	}
	/**
	 * @param deadlineDay the deadlineDay to set
	 */
	public void setDeadlineDay(Date deadlineDay) {
		this.deadlineDay = deadlineDay;
	}
	/**
	 * @return the premiumSource
	 */
	public String getPremiumSource() {
		return premiumSource;
	}
	/**
	 * @param premiumSource the premiumSource to set
	 */
	public void setPremiumSource(String premiumSource) {
		this.premiumSource = premiumSource;
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
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * @param bankName the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	 * @return the exceptionReport
	 */
	public String getExceptionReport() {
		return exceptionReport;
	}
	/**
	 * @param exceptionReport the exceptionReport to set
	 */
	public void setExceptionReport(String exceptionReport) {
		this.exceptionReport = exceptionReport;
	}
	/**
	 * @return the listFlag
	 */
	public String getListFlag() {
		return listFlag;
	}
	/**
	 * @param listFlag the listFlag to set
	 */
	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}
	/**
	 * @return the listPrint
	 */
	public String getListPrint() {
		return listPrint;
	}
	/**
	 * @param listPrint the listPrint to set
	 */
	public void setListPrint(String listPrint) {
		this.listPrint = listPrint;
	}
	/**
	 * @return the personalDigitalID
	 */
	public String getPersonalDigitalID() {
		return personalDigitalID;
	}
	/**
	 * @param personalDigitalID the personalDigitalID to set
	 */
	public void setPersonalDigitalID(String personalDigitalID) {
		this.personalDigitalID = personalDigitalID;
	}
	/**
	 * @return the policyType
	 */
	public String getPolicyType() {
		return policyType;
	}
	/**
	 * @param policyType the policyType to set
	 */
	public void setPolicyType(String policyType) {
		this.policyType = policyType;
	}
	/**
	 * @return the giftSign
	 */
	public String getGiftSign() {
		return giftSign;
	}
	/**
	 * @param giftSign the giftSign to set
	 */
	public void setGiftSign(String giftSign) {
		this.giftSign = giftSign;
	}
	/**
	 * @return the fileTal
	 */
	public String getFileTal() {
		return fileTal;
	}
	/**
	 * @param fileTal the fileTal to set
	 */
	public void setFileTal(String fileTal) {
		this.fileTal = fileTal;
	}
	/**
	 * @return the fileVal
	 */
	public String getFileVal() {
		return fileVal;
	}
	/**
	 * @param fileVal the fileVal to set
	 */
	public void setFileVal(String fileVal) {
		this.fileVal = fileVal;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "OtherInfoVo [disputeHandling=" + disputeHandling + ", Name=" + Name + ", effectiveFrequency="
				+ effectiveFrequency + ", Term=" + Term + ", insurancePeriod=" + insurancePeriod + ", settlementMethod="
				+ settlementMethod + ", settlementQuota=" + settlementQuota + ", settlementlDate=" + settlementlDate
				+ ", paymentForm=" + paymentForm + ", paymentMethod=" + paymentMethod + ", initialStart=" + initialStart
				+ ", deadlineDay=" + deadlineDay + ", premiumSource=" + premiumSource + ", bankCode=" + bankCode
				+ ", bankName=" + bankName + ", bankAccount=" + bankAccount + ", exceptionReport=" + exceptionReport
				+ ", listFlag=" + listFlag + ", listPrint=" + listPrint + ", personalDigitalID=" + personalDigitalID
				+ ", policyType=" + policyType + ", giftSign=" + giftSign + ", fileTal=" + fileTal + ", fileVal="
				+ fileVal + "]";
	}
	
	
}
