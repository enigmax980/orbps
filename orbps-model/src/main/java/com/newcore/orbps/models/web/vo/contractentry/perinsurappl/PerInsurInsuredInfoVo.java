package com.newcore.orbps.models.web.vo.contractentry.perinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 被保人信息
 * @author wangyupeng
 *
 */
public class PerInsurInsuredInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 2159444543L;
	
	/** 与投保人关系 */
	private String relToHldr;
	/**客户号**/
	private String customerNo;
	/**姓名**/
	private String Name;	
	/**性别**/
	private String sex;
	/**出生日期**/
	private Date birthDate;
	/**国籍**/
	private String nationality;
	/**证件类型**/
	private String idType;
	/**证件号码**/
	private Long idNo;
	/**证件有效期**/
	private String idTerm;
	/**职业类别**/
	private String occupationType;
	/**职业代码**/
	private String occupationCode;
	/**婚姻状况**/
	private Double maritalStatus;
	/**兼职类别**/
	private String ptJobType;
	/**兼职代码**/
	private String ptJobCode;
	/**工作单位**/
	private String workUnit;
	/**办公室电话**/
	private String officePhone;
	/**通讯地址**/
	private String posAddress;
	/**电子邮箱**/
	private String email;	
	/**移动电话**/
	private String mobilePhone;
	/**邮编**/
	private String zipCode;
	/**紧急联络人**/
	private String emergencyPhone;
	/**补充医疗**/
	private String supplementaryMedical;
	/**社会保险/公费医疗**/
	private String socialInsurance;
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
	 * @return the customerNo
	 */
	public String getCustomerNo() {
		return customerNo;
	}
	/**
	 * @param customerNo the customerNo to set
	 */
	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
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
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return nationality;
	}
	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
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
	public Long getIdNo() {
		return idNo;
	}
	/**
	 * @param idNo the idNo to set
	 */
	public void setIdNo(Long idNo) {
		this.idNo = idNo;
	}
	/**
	 * @return the idTerm
	 */
	public String getIdTerm() {
		return idTerm;
	}
	/**
	 * @param idTerm the idTerm to set
	 */
	public void setIdTerm(String idTerm) {
		this.idTerm = idTerm;
	}
	/**
	 * @return the occupationType
	 */
	public String getOccupationType() {
		return occupationType;
	}
	/**
	 * @param occupationType the occupationType to set
	 */
	public void setOccupationType(String occupationType) {
		this.occupationType = occupationType;
	}
	/**
	 * @return the occupationCode
	 */
	public String getOccupationCode() {
		return occupationCode;
	}
	/**
	 * @param occupationCode the occupationCode to set
	 */
	public void setOccupationCode(String occupationCode) {
		this.occupationCode = occupationCode;
	}
	/**
	 * @return the maritalStatus
	 */
	public Double getMaritalStatus() {
		return maritalStatus;
	}
	/**
	 * @param maritalStatus the maritalStatus to set
	 */
	public void setMaritalStatus(Double maritalStatus) {
		this.maritalStatus = maritalStatus;
	}
	/**
	 * @return the ptJobType
	 */
	public String getPtJobType() {
		return ptJobType;
	}
	/**
	 * @param ptJobType the ptJobType to set
	 */
	public void setPtJobType(String ptJobType) {
		this.ptJobType = ptJobType;
	}
	/**
	 * @return the ptJobCode
	 */
	public String getPtJobCode() {
		return ptJobCode;
	}
	/**
	 * @param ptJobCode the ptJobCode to set
	 */
	public void setPtJobCode(String ptJobCode) {
		this.ptJobCode = ptJobCode;
	}
	/**
	 * @return the workUnit
	 */
	public String getWorkUnit() {
		return workUnit;
	}
	/**
	 * @param workUnit the workUnit to set
	 */
	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}
	/**
	 * @return the officePhone
	 */
	public String getOfficePhone() {
		return officePhone;
	}
	/**
	 * @param officePhone the officePhone to set
	 */
	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}
	/**
	 * @return the posAddress
	 */
	public String getPosAddress() {
		return posAddress;
	}
	/**
	 * @param posAddress the posAddress to set
	 */
	public void setPosAddress(String posAddress) {
		this.posAddress = posAddress;
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
	 * @return the mobilePhone
	 */
	public String getMobilePhone() {
		return mobilePhone;
	}
	/**
	 * @param mobilePhone the mobilePhone to set
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the emergencyPhone
	 */
	public String getEmergencyPhone() {
		return emergencyPhone;
	}
	/**
	 * @param emergencyPhone the emergencyPhone to set
	 */
	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}
	/**
	 * @return the supplementaryMedical
	 */
	public String getSupplementaryMedical() {
		return supplementaryMedical;
	}
	/**
	 * @param supplementaryMedical the supplementaryMedical to set
	 */
	public void setSupplementaryMedical(String supplementaryMedical) {
		this.supplementaryMedical = supplementaryMedical;
	}
	/**
	 * @return the socialInsurance
	 */
	public String getSocialInsurance() {
		return socialInsurance;
	}
	/**
	 * @param socialInsurance the socialInsurance to set
	 */
	public void setSocialInsurance(String socialInsurance) {
		this.socialInsurance = socialInsurance;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InsuredInfo [relToHldr=" + relToHldr + ", customerNo=" + customerNo + ", Name=" + Name + ", sex=" + sex
				+ ", birthDate=" + birthDate + ", nationality=" + nationality + ", idType=" + idType + ", idNo=" + idNo
				+ ", idTerm=" + idTerm + ", occupationType=" + occupationType + ", occupationCode=" + occupationCode
				+ ", maritalStatus=" + maritalStatus + ", ptJobType=" + ptJobType + ", ptJobCode=" + ptJobCode
				+ ", workUnit=" + workUnit + ", officePhone=" + officePhone + ", posAddress=" + posAddress + ", email="
				+ email + ", mobilePhone=" + mobilePhone + ", zipCode=" + zipCode + ", emergencyPhone=" + emergencyPhone
				+ ", supplementaryMedical=" + supplementaryMedical + ", socialInsurance=" + socialInsurance + "]";
	}
	
}
