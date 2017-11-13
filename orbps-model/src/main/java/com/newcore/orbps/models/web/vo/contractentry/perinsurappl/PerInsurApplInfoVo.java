package com.newcore.orbps.models.web.vo.contractentry.perinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保单信息
 * @author wangyupeng
 *
 */
public class PerInsurApplInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 2149444543L;
	
	/** 投保单号 */
	private String applNo;
	/**销售渠道**/
	private String salesChannel;
	/**销售机构号**/
	private String salesBranchNo;
	/**销售员号**/
	private String salesPersonNo;
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
	/**邮编**/
	private String zipCode;
	/**通讯地址**/
	private String posAddress;
	/**移动电话**/
	private String mobilePhone;
	/**固定电话**/
	private String lanPhone;
	/**电子邮箱**/
	private String email;
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
	 * @return the salesPersonNo
	 */
	public String getSalesPersonNo() {
		return salesPersonNo;
	}
	/**
	 * @param salesPersonNo the salesPersonNo to set
	 */
	public void setSalesPersonNo(String salesPersonNo) {
		this.salesPersonNo = salesPersonNo;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApplInfoForm [applNo=" + applNo + ", salesChannel=" + salesChannel + ", salesBranchNo=" + salesBranchNo
				+ ", salesPersonNo=" + salesPersonNo + ", customerNo=" + customerNo + ", Name=" + Name + ", sex=" + sex
				+ ", birthDate=" + birthDate + ", age=" + age + ", idType=" + idType + ", idNo=" + idNo + ", idTerm="
				+ idTerm + ", occupationType=" + occupationType + ", occupationCode=" + occupationCode
				+ ", nationality=" + nationality + ", zipCode=" + zipCode + ", posAddress=" + posAddress
				+ ", mobilePhone=" + mobilePhone + ", lanPhone=" + lanPhone + ", email=" + email + "]";
	}
	
}
