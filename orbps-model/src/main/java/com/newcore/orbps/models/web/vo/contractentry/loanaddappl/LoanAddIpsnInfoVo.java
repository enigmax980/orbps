package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 被保人信息Vo
 * @author jincong
 *
 */
public class LoanAddIpsnInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030004L;
	
	/** 与投保人关系 */
	private String relToHldr;
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
	private String id;
	/** 通讯地址 */
	private String postalAddress;
	/** 通讯地址 */
	private String occupationalCodes;
	/** 邮编 */
	private String postCode;
	/** 移动电话 */
	private String mobilePhone;
	/** 电子邮箱 */
	private String email;
	/** 固定电话 */
	private String lanPhone;
	/** 传真号码 */
	private String faxNumber;
	/** 是否覆盖以往信息 */
	private String coverInfoFlag;
	
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
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
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
	/**
	 * @return the coverInfoFlag
	 */
	public String getCoverInfoFlag() {
		return coverInfoFlag;
	}
	/**
	 * @param coverInfoFlag the coverInfoFlag to set
	 */
	public void setCoverInfoFlag(String coverInfoFlag) {
		this.coverInfoFlag = coverInfoFlag;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IpsnInfoVo [relToHldr=" + relToHldr + ", custNo=" + custNo + ", custName=" + custName + ", sex=" + sex
				+ ", birthDate=" + birthDate + ", idType=" + idType + ", id=" + id + ", postalAddress=" + postalAddress
				+ ", occupationalCodes=" + occupationalCodes + ", postCode=" + postCode + ", mobilePhone=" + mobilePhone
				+ ", email=" + email + ", lanPhone=" + lanPhone + ", faxNumber=" + faxNumber + ", coverInfoFlag="
				+ coverInfoFlag + "]";
	}
	
}
