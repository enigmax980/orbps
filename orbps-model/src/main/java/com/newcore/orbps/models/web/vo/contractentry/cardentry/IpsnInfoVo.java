package com.newcore.orbps.models.web.vo.contractentry.cardentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 被保人信息Vo
 * @author jincong
 *
 */
public class IpsnInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146020002L;
	/** 与投保人关系 */
	private String relToHldr;
	/** 是否为连带被保人 */
	private String jointInsured;
	/** 客户号 */
	private String custNo;
	/** 证件类别 */
	private String ipsnIdType;
	/** 证件号码 */
	private String ipsnIdCode;
	/** 姓名 */
	private String name;
	/** 性别 */
	private String sex;
	/** 出生日期 */
	private Date birthDate;
	/** 年龄 */
	private Integer age;
	/** 工作单位 */
	private String companyName;
	/** 职业代码 */
	private String ipsnOccClassCod;
	/** 通讯地址 */
	private String communicateAddr;
	/** 职业类别 */
	private String occupationCategory;
	/** 邮编 */
	private String postCode;
	/** 联系电话 */
	private String appOfficeTel;
	/** 手机号 */
	private String mPhoneNo;
	/** 电子邮箱 */
	private String email;
	/** 是否有异常告知 */
	private String healthStatFlag;
	/** 职务 */
	private String post;
	/** 收入来源 */
	private String incomeSource;
	/** 与主被保人关系 */
	private String relToIpsn;
	/** 国籍 */
	private String nationality;
	/** 医保身份 */
	private String ipsnSss;
	/** 办公电话 */
	private String officeTelephone;
	/** 家庭电话 */
	private String hPhoneNo;
	
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
	 * @return the jointInsured
	 */
	public String getJointInsured() {
		return jointInsured;
	}
	/**
	 * @param jointInsured the jointInsured to set
	 */
	public void setJointInsured(String jointInsured) {
		this.jointInsured = jointInsured;
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
	 * @return the ipsnIdType
	 */
	public String getIpsnIdType() {
		return ipsnIdType;
	}
	/**
	 * @param ipsnIdType the ipsnIdType to set
	 */
	public void setIpsnIdType(String ipsnIdType) {
		this.ipsnIdType = ipsnIdType;
	}
	/**
	 * @return the ipsnIdCode
	 */
	public String getIpsnIdCode() {
		return ipsnIdCode;
	}
	/**
	 * @param ipsnIdCode the ipsnIdCode to set
	 */
	public void setIpsnIdCode(String ipsnIdCode) {
		this.ipsnIdCode = ipsnIdCode;
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
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the ipsnOccClassCod
	 */
	public String getIpsnOccClassCod() {
		return ipsnOccClassCod;
	}
	/**
	 * @param ipsnOccClassCod the ipsnOccClassCod to set
	 */
	public void setIpsnOccClassCod(String ipsnOccClassCod) {
		this.ipsnOccClassCod = ipsnOccClassCod;
	}
	/**
	 * @return the communicateAddr
	 */
	public String getCommunicateAddr() {
		return communicateAddr;
	}
	/**
	 * @param communicateAddr the communicateAddr to set
	 */
	public void setCommunicateAddr(String communicateAddr) {
		this.communicateAddr = communicateAddr;
	}
	/**
	 * @return the occupationCategory
	 */
	public String getOccupationCategory() {
		return occupationCategory;
	}
	/**
	 * @param occupationCategory the occupationCategory to set
	 */
	public void setOccupationCategory(String occupationCategory) {
		this.occupationCategory = occupationCategory;
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
	 * @return the appOfficeTel
	 */
	public String getAppOfficeTel() {
		return appOfficeTel;
	}
	/**
	 * @param appOfficeTel the appOfficeTel to set
	 */
	public void setAppOfficeTel(String appOfficeTel) {
		this.appOfficeTel = appOfficeTel;
	}
	/**
	 * @return the mPhoneNo
	 */
	public String getmPhoneNo() {
		return mPhoneNo;
	}
	/**
	 * @param mPhoneNo the mPhoneNo to set
	 */
	public void setmPhoneNo(String mPhoneNo) {
		this.mPhoneNo = mPhoneNo;
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
	 * @return the healthStatFlag
	 */
	public String getHealthStatFlag() {
		return healthStatFlag;
	}
	/**
	 * @param healthStatFlag the healthStatFlag to set
	 */
	public void setHealthStatFlag(String healthStatFlag) {
		this.healthStatFlag = healthStatFlag;
	}
	/**
	 * @return the post
	 */
	public String getPost() {
		return post;
	}
	/**
	 * @param post the post to set
	 */
	public void setPost(String post) {
		this.post = post;
	}
	/**
	 * @return the incomeSource
	 */
	public String getIncomeSource() {
		return incomeSource;
	}
	/**
	 * @param incomeSource the incomeSource to set
	 */
	public void setIncomeSource(String incomeSource) {
		this.incomeSource = incomeSource;
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
	 * @return the ipsnSss
	 */
	public String getIpsnSss() {
		return ipsnSss;
	}
	/**
	 * @param ipsnSss the ipsnSss to set
	 */
	public void setIpsnSss(String ipsnSss) {
		this.ipsnSss = ipsnSss;
	}
	/**
	 * @return the officeTelephone
	 */
	public String getOfficeTelephone() {
		return officeTelephone;
	}
	/**
	 * @param officeTelephone the officeTelephone to set
	 */
	public void setOfficeTelephone(String officeTelephone) {
		this.officeTelephone = officeTelephone;
	}
	/**
	 * @return the hPhoneNo
	 */
	public String gethPhoneNo() {
		return hPhoneNo;
	}
	/**
	 * @param hPhoneNo the hPhoneNo to set
	 */
	public void sethPhoneNo(String hPhoneNo) {
		this.hPhoneNo = hPhoneNo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ipsnInfoVo [relToHldr=" + relToHldr + ", jointInsured=" + jointInsured + ", custNo=" + custNo
				+ ", ipsnIdType=" + ipsnIdType + ", ipsnIdCode=" + ipsnIdCode + ", name=" + name + ", sex=" + sex
				+ ", birthDate=" + birthDate + ", age=" + age + ", companyName=" + companyName + ", ipsnOccClassCod="
				+ ipsnOccClassCod + ", communicateAddr=" + communicateAddr + ", occupationCategory="
				+ occupationCategory + ", postCode=" + postCode + ", appOfficeTel=" + appOfficeTel + ", mPhoneNo="
				+ mPhoneNo + ", email=" + email + ", healthStatFlag=" + healthStatFlag + ", post=" + post
				+ ", incomeSource=" + incomeSource + ", relToIpsn=" + relToIpsn + ", nationality=" + nationality
				+ ", ipsnSss=" + ipsnSss + ", officeTelephone=" + officeTelephone + ", hPhoneNo=" + hPhoneNo + "]";
	}
}
