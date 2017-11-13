package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保人信息Vo
 * @author jincong
 *
 */
public class LoanAddHldrInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030003L;
	
	/** 客户号 */
	private String custNo;
	/** 姓名 */
	private String custName;
	/** 性别 */
	private String sex;
	/** 出生日期 */
	private Date birthDate;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNo;
	/** 证件号码 */
	private String postalAddress;
	/** 职业代码 */
	private String occupationalCodes;
	/** 邮政编码 */
	private String postCode;
	/** 移动电话 */
	private String mobilePhone;
	/** 固定电话 */
	private String lanPhone;
	/** 电子邮箱 */
	private String email;
	/** 传真号码 */
	private String faxNumber;
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
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
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
	 * @return the postalAddress
	 */
	public String getPostalAddress() {
		return postalAddress;
	}
	/**
	 * @param postalAddress the postalAddress to set
	 */
	public void setPostalAddress(String postalAddress) {
		this.postalAddress = postalAddress;
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
	 * @return the postCode
	 */
	public String getPostCode() {
		return postCode;
	}
	/**
	 * @param postCode the postCode to set
	 */
	public void setPostCode(String postCode) {
		this.postCode = postCode;
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
	 * @return the faxNumber
	 */
	public String getFaxNumber() {
		return faxNumber;
	}
	/**
	 * @param faxNumber the faxNumber to set
	 */
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HldrInfoVo [custNo=" + custNo + ", custName=" + custName + ", sex=" + sex + ", birthDate=" + birthDate
				+ ", idType=" + idType + ", idNo=" + idNo + ", postalAddress=" + postalAddress + ", occupationalCodes="
				+ occupationalCodes + ", postCode=" + postCode + ", mobilePhone=" + mobilePhone + ", lanPhone="
				+ lanPhone + ", email=" + email + ", faxNumber=" + faxNumber + "]";
	}
	
}
