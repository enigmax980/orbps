package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;
import java.util.Date;

/**
 * 被保人信息Vo
 * @author jincong
 *
 */
public class ListImportIpsnInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080002L;
	
	/** 投保单号 */
	private String applNo;
	/** 投保单位名称 */
	private String applCompanyName;
	/** 被保险人人数 */
	private Long insurIpsnNum;
	/** 已录人数 */
	private Long recordPersonNum;
	/** 客户号 */
	private String custNo;
	/** 姓名 */
	private String name;
	/** 证件类别 */
	private String idType;
	/** 证件号码 */
	private String idNo;
	/** 是否连带被保险人 */
	private String joinIpsnFlag;
	/** 性别 */
	private String ipsursex;
	/** 出生日期 */
	private Date birthDate;
	/** 年龄 */
	private Long age;
	/** 主被保险人编号 */
	private Long mainIpsnNo;
	/** 与主被保险人关系 */
	private String relToIpsn;
	/** 职业代码 */
	private String occupationalCodes;
	/** 职业名称 */
	private String occupationName;
	/** 风险等级 */
	private String riskLevel;
	/** 是否在职 */
	private String onJobFlag;
	/** 是否有异常告知 */
	private String healthFlag;
	/** 医保标识 */
	private String medicalInsurFlag;
	/** 医保代码 */
	private String medicalInsurCode;
	/** 医保编号 */
	private String medicalInsurNo;
	/** 电话 */
	private String phone;
	/** 电子邮箱 */
	private String email;
	/** 工号 */
	private String workNo;
	/** 工作地点 */
	private String workAddress;
	/** 组织层次代码 */
	private String groupLevelCode;
	/** 交费开户银行 */
	private String bankCode;
	/** 开户名 */
	private String accountName;
	/** 交费账号 */
	private String accountNo;
	/** 个人交费金额 */
	private Double charge;
	/** 理赔账户与交费账户是否相同 */
	private String accountSameFlag;
	/** 理赔开户银行 */
	private String clmBankCode;
	/** 理赔开户名 */
	private String clmAccountName;
	/** 交费账号 */
	private String clmAccountNo;
	/** 备注 */
	private String remarks;
	/** 被保险人编号 */
	private Long ipsnNo;
	/**字段名：收费属组号，是否必填：否 */
	private Long feeGrpNo;
	/** 与投保人关系*/
	private String relToHldr;
	/**同业公司人身保险保额合计*/
	private Double tyNetPayAmnt;
	/**单位交费金额*/
	private Double grpPayAmount;
	/**缴费账户*/
	private String bankAccName;
	/***缴费账号*/
	private String bankAccNo;
	/**个人账户交费比例*/
	private Double ipsnPayPct;
	/**个人扣款金额*/
	private Double ipsnPayAmnt;
	/**账户顺序*/
	private Long seqNo;
	/**上一个或者下一个falg*/
	private String flag;
	
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
	 * @return the applCompanyName
	 */
	public String getApplCompanyName() {
		return applCompanyName;
	}
	/**
	 * @param applCompanyName the applCompanyName to set
	 */
	public void setApplCompanyName(String applCompanyName) {
		this.applCompanyName = applCompanyName;
	}
	/**
	 * @return the insurIpsnNum
	 */
	public Long getInsurIpsnNum() {
		return insurIpsnNum;
	}
	/**
	 * @param insurIpsnNum the insurIpsnNum to set
	 */
	public void setInsurIpsnNum(Long insurIpsnNum) {
		this.insurIpsnNum = insurIpsnNum;
	}
	/**
	 * @return the recordPersonNum
	 */
	public Long getRecordPersonNum() {
		return recordPersonNum;
	}
	/**
	 * @param recordPersonNum the recordPersonNum to set
	 */
	public void setRecordPersonNum(Long recordPersonNum) {
		this.recordPersonNum = recordPersonNum;
	}
	/**
	 * @return the custNo
	 */
	public String getCustNo() {
		return custNo;
	}
	/**
	 * @param custNo the custNo to set
	 */
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the idType
	 */
	public String getIdType() {
		return idType;
	}
	/**
	 * @param idType the idType to set
	 */
	public void setIdType(String idType) {
		this.idType = idType;
	}
	/**
	 * @return the idNo
	 */
	public String getIdNo() {
		return idNo;
	}
	/**
	 * @param idNo the idNo to set
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	/**
	 * @return the joinIpsnFlag
	 */
	public String getJoinIpsnFlag() {
		return joinIpsnFlag;
	}
	/**
	 * @param joinIpsnFlag the joinIpsnFlag to set
	 */
	public void setJoinIpsnFlag(String joinIpsnFlag) {
		this.joinIpsnFlag = joinIpsnFlag;
	}
	/**
	 * @return the sex
	 */

	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @return the ipsursex
	 */
	public String getIpsursex() {
		return ipsursex;
	}
	/**
	 * @param ipsursex the ipsursex to set
	 */
	public void setIpsursex(String ipsursex) {
		this.ipsursex = ipsursex;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the age
	 */
	public Long getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Long age) {
		this.age = age;
	}
	/**
	 * @return the mainIpsnNo
	 */
	public Long getMainIpsnNo() {
		return mainIpsnNo;
	}
	/**
	 * @param mainIpsnNo the mainIpsnNo to set
	 */
	public void setMainIpsnNo(Long mainIpsnNo) {
		this.mainIpsnNo = mainIpsnNo;
	}
	/**
	 * @return the relToIpsn
	 */
	public String getRelToIpsn() {
		return relToIpsn;
	}
	/**
	 * @param relToIpsn the relToIpsn to set
	 */
	public void setRelToIpsn(String relToIpsn) {
		this.relToIpsn = relToIpsn;
	}
	/**
	 * @return the occupationalCodes
	 */
	public String getOccupationalCodes() {
		return occupationalCodes;
	}
	/**
	 * @param occupationalCodes the occupationalCodes to set
	 */
	public void setOccupationalCodes(String occupationalCodes) {
		this.occupationalCodes = occupationalCodes;
	}
	/**
	 * @return the occupationName
	 */
	public String getOccupationName() {
		return occupationName;
	}
	/**
	 * @param occupationName the occupationName to set
	 */
	public void setOccupationName(String occupationName) {
		this.occupationName = occupationName;
	}
	/**
	 * @return the riskLevel
	 */
	public String getRiskLevel() {
		return riskLevel;
	}
	/**
	 * @param riskLevel the riskLevel to set
	 */
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	/**
	 * @return the onJobFlag
	 */
	public String getOnJobFlag() {
		return onJobFlag;
	}
	/**
	 * @param onJobFlag the onJobFlag to set
	 */
	public void setOnJobFlag(String onJobFlag) {
		this.onJobFlag = onJobFlag;
	}
	/**
	 * @return the healthFlag
	 */
	public String getHealthFlag() {
		return healthFlag;
	}
	/**
	 * @param healthFlag the healthFlag to set
	 */
	public void setHealthFlag(String healthFlag) {
		this.healthFlag = healthFlag;
	}
	/**
	 * @return the medicalInsurFlag
	 */
	public String getMedicalInsurFlag() {
		return medicalInsurFlag;
	}
	/**
	 * @param medicalInsurFlag the medicalInsurFlag to set
	 */
	public void setMedicalInsurFlag(String medicalInsurFlag) {
		this.medicalInsurFlag = medicalInsurFlag;
	}
	/**
	 * @return the medicalInsurCode
	 */
	public String getMedicalInsurCode() {
		return medicalInsurCode;
	}
	/**
	 * @param medicalInsurCode the medicalInsurCode to set
	 */
	public void setMedicalInsurCode(String medicalInsurCode) {
		this.medicalInsurCode = medicalInsurCode;
	}
	/**
	 * @return the medicalInsurNo
	 */
	public String getMedicalInsurNo() {
		return medicalInsurNo;
	}
	/**
	 * @param medicalInsurNo the medicalInsurNo to set
	 */
	public void setMedicalInsurNo(String medicalInsurNo) {
		this.medicalInsurNo = medicalInsurNo;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the workNo
	 */
	public String getWorkNo() {
		return workNo;
	}
	/**
	 * @param workNo the workNo to set
	 */
	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}
	/**
	 * @return the workAddress
	 */
	public String getWorkAddress() {
		return workAddress;
	}
	/**
	 * @param workAddress the workAddress to set
	 */
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	/**
	 * @return the groupLevelCode
	 */
	public String getGroupLevelCode() {
		return groupLevelCode;
	}
	/**
	 * @param groupLevelCode the groupLevelCode to set
	 */
	public void setGroupLevelCode(String groupLevelCode) {
		this.groupLevelCode = groupLevelCode;
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
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}
	/**
	 * @param accountName the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	/**
	 * @return the accountNo
	 */
	public String getAccountNo() {
		return accountNo;
	}
	/**
	 * @param accountNo the accountNo to set
	 */
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	/**
	 * @return the charge
	 */
	public Double getCharge() {
		return charge;
	}
	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Double charge) {
		this.charge = charge;
	}
	/**
	 * @return the accountSameFlag
	 */
	public String getAccountSameFlag() {
		return accountSameFlag;
	}
	/**
	 * @param accountSameFlag the accountSameFlag to set
	 */
	public void setAccountSameFlag(String accountSameFlag) {
		this.accountSameFlag = accountSameFlag;
	}
	/**
	 * @return the clmBankCode
	 */
	public String getClmBankCode() {
		return clmBankCode;
	}
	/**
	 * @param clmBankCode the clmBankCode to set
	 */
	public void setClmBankCode(String clmBankCode) {
		this.clmBankCode = clmBankCode;
	}
	/**
	 * @return the clmAccountName
	 */
	public String getClmAccountName() {
		return clmAccountName;
	}
	/**
	 * @param clmAccountName the clmAccountName to set
	 */
	public void setClmAccountName(String clmAccountName) {
		this.clmAccountName = clmAccountName;
	}
	/**
	 * @return the clmAccountNo
	 */
	public String getClmAccountNo() {
		return clmAccountNo;
	}
	/**
	 * @param clmAccountNo the clmAccountNo to set
	 */
	public void setClmAccountNo(String clmAccountNo) {
		this.clmAccountNo = clmAccountNo;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the ipsnNo
	 */
	public Long getIpsnNo() {
		return ipsnNo;
	}
	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(Long ipsnNo) {
		this.ipsnNo = ipsnNo;
	}
	/**
	 * @return the feeGrpNo
	 */
	public Long getFeeGrpNo() {
		return feeGrpNo;
	}
	/**
	 * @param feeGrpNo the feeGrpNo to set
	 */
	public void setFeeGrpNo(Long feeGrpNo) {
		this.feeGrpNo = feeGrpNo;
	}

	/**
	 * @return the relToHldr
	 */
	public String getRelToHldr() {
		return relToHldr;
	}
	/**
	 * @param relToHldr the relToHldr to set
	 */
	public void setRelToHldr(String relToHldr) {
		this.relToHldr = relToHldr;
	}
	/**
	 * @return the tyNetPayAmnt
	 */
	public Double getTyNetPayAmnt() {
		return tyNetPayAmnt;
	}
	/**
	 * @param tyNetPayAmnt the tyNetPayAmnt to set
	 */
	public void setTyNetPayAmnt(Double tyNetPayAmnt) {
		this.tyNetPayAmnt = tyNetPayAmnt;
	}
	/**
	 * @return the grpPayAmount
	 */
	public Double getGrpPayAmount() {
		return grpPayAmount;
	}
	/**
	 * @param grpPayAmount the grpPayAmount to set
	 */
	public void setGrpPayAmount(Double grpPayAmount) {
		this.grpPayAmount = grpPayAmount;
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
	 * @return the ipsnPayPct
	 */
	public Double getIpsnPayPct() {
		return ipsnPayPct;
	}
	/**
	 * @param ipsnPayPct the ipsnPayPct to set
	 */
	public void setIpsnPayPct(Double ipsnPayPct) {
		this.ipsnPayPct = ipsnPayPct;
	}
	/**
	 * @return the ipsnPayAmnt
	 */
	public Double getIpsnPayAmnt() {
		return ipsnPayAmnt;
	}
	/**
	 * @param ipsnPayAmnt the ipsnPayAmnt to set
	 */
	public void setIpsnPayAmnt(Double ipsnPayAmnt) {
		this.ipsnPayAmnt = ipsnPayAmnt;
	}
	/**
	 * @return the seqNo
	 */
	public Long getSeqNo() {
		return seqNo;
	}
	/**
	 * @param seqNo the seqNo to set
	 */
	public void setSeqNo(Long seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}
	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListImportIpsnInfoVo [applNo=" + applNo + ", applCompanyName=" + applCompanyName + ", insurIpsnNum="
				+ insurIpsnNum + ", recordPersonNum=" + recordPersonNum + ", custNo=" + custNo + ", name=" + name
				+ ", idType=" + idType + ", idNo=" + idNo + ", joinIpsnFlag=" + joinIpsnFlag + ", ipsursex=" + ipsursex
				+ ", birthDate=" + birthDate + ", age=" + age + ", mainIpsnNo=" + mainIpsnNo + ", relToIpsn="
				+ relToIpsn + ", occupationalCodes=" + occupationalCodes + ", occupationName=" + occupationName
				+ ", riskLevel=" + riskLevel + ", onJobFlag=" + onJobFlag + ", healthFlag=" + healthFlag
				+ ", medicalInsurFlag=" + medicalInsurFlag + ", medicalInsurCode=" + medicalInsurCode
				+ ", medicalInsurNo=" + medicalInsurNo + ", phone=" + phone + ", email=" + email + ", workNo=" + workNo
				+ ", workAddress=" + workAddress + ", groupLevelCode=" + groupLevelCode + ", bankCode=" + bankCode
				+ ", accountName=" + accountName + ", accountNo=" + accountNo + ", charge=" + charge
				+ ", accountSameFlag=" + accountSameFlag + ", clmBankCode=" + clmBankCode + ", clmAccountName="
				+ clmAccountName + ", clmAccountNo=" + clmAccountNo + ", remarks=" + remarks + ", ipsnNo=" + ipsnNo
				+ ", feeGrpNo=" + feeGrpNo + ", relToHldr=" + relToHldr + ", tyNetPayAmnt=" + tyNetPayAmnt
				+ ", grpPayAmount=" + grpPayAmount + ", bankAccName=" + bankAccName + ", bankAccNo=" + bankAccNo
				+ ", ipsnPayPct=" + ipsnPayPct + ", ipsnPayAmnt=" + ipsnPayAmnt + ", seqNo=" + seqNo + ", flag=" + flag
				+ "]";
	}

	

}
