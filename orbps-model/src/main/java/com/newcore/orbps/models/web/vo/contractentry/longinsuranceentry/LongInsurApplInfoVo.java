package com.newcore.orbps.models.web.vo.contractentry.longinsuranceentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保人信息
 * @author wangyupeng
 *
 */
public class LongInsurApplInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1149444543L;
	
	/** 投保单号 */
	private String applNo;
	/**销售渠道**/
	private String salesChannel;
	/**销售机构号**/
	private String salesBranchNo;
	/**客户号**/
	private String customerNo;
	/**姓名**/
	private String Name;
	/**性别**/
	private String sex;
	/**出生日期**/
	private Date birthDate;
	/**年龄**/
	private Integer age;
	/**婚姻状况**/
	private Double maritalStatus;
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
	/**国籍**/
	private String nationality;
	/**兼职类别**/
	private String ptJobType;
	/**兼职代码**/
	private String ptJobCode;
	/**备用移动电话**/
	private String sparePhone;
	/**紧急联络人**/
	private String emergencyPerson;
	/**紧急联络人电话**/
	private String emergencyPhone;
	/**通讯地址**/
	private String posAddress;
	/**移动电话**/
	private String mobilePhone;
	/**固定电话**/
	private String lanPhone;
	/**电子邮箱**/
	private String email;
	/**个人保单外部转移人员**/
	private String externalTransfer;
	/**补充医疗**/
	private String supplementaryMedical;
	/**社会保险/公费医疗**/
	private String socialInsurance;
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
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}
	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
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
	 * @return the age
	 */
	public Integer getAge() {
		return age;
	}
	/**
	 * @param age the age to set
	 */
	public void setAge(Integer age) {
		this.age = age;
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
	 * @return the sparePhone
	 */
	public String getSparePhone() {
		return sparePhone;
	}
	/**
	 * @param sparePhone the sparePhone to set
	 */
	public void setSparePhone(String sparePhone) {
		this.sparePhone = sparePhone;
	}
	/**
	 * @return the emergencyPerson
	 */
	public String getEmergencyPerson() {
		return emergencyPerson;
	}
	/**
	 * @param emergencyPerson the emergencyPerson to set
	 */
	public void setEmergencyPerson(String emergencyPerson) {
		this.emergencyPerson = emergencyPerson;
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
	 * @return the lanPhone
	 */
	public String getLanPhone() {
		return lanPhone;
	}
	/**
	 * @param lanPhone the lanPhone to set
	 */
	public void setLanPhone(String lanPhone) {
		this.lanPhone = lanPhone;
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
	 * @return the externalTransfer
	 */
	public String getExternalTransfer() {
		return externalTransfer;
	}
	/**
	 * @param externalTransfer the externalTransfer to set
	 */
	public void setExternalTransfer(String externalTransfer) {
		this.externalTransfer = externalTransfer;
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
		return "ApplIlnfo [applNo=" + applNo + ", salesChannel=" + salesChannel + ", salesBranchNo=" + salesBranchNo
				+ ", customerNo=" + customerNo + ", Name=" + Name + ", sex=" + sex + ", birthDate=" + birthDate
				+ ", age=" + age + ", maritalStatus=" + maritalStatus + ", idType=" + idType + ", idNo=" + idNo
				+ ", idTerm=" + idTerm + ", occupationType=" + occupationType + ", occupationCode=" + occupationCode
				+ ", nationality=" + nationality + ", ptJobType=" + ptJobType + ", ptJobCode=" + ptJobCode
				+ ", sparePhone=" + sparePhone + ", emergencyPerson=" + emergencyPerson + ", emergencyPhone="
				+ emergencyPhone + ", posAddress=" + posAddress + ", mobilePhone=" + mobilePhone + ", lanPhone="
				+ lanPhone + ", email=" + email + ", externalTransfer=" + externalTransfer + ", supplementaryMedical="
				+ supplementaryMedical + ", socialInsurance=" + socialInsurance + "]";
	}
	
}
