package com.newcore.orbps.models.web.vo.contractentry.combinedpolicy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 被保人信息  
 * @author wangyanjie
 *
 */
public class ComBinedRecognizeelVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000009L;
	/** 是投保人的 */
	private String IpsnRelationCode;
	/** 是否连带被保人 */
	private String relIpsnFlag;
	/** 客户号 */
	private String custNo;
	/** 姓名 */
	private String name;
	/** 证件类别*/
	private String ipsnIdType;
	/** 证件号码*/
	private String ipsnIdCode;
	/** 性别*/
	private String sex;
	/** 出生日期 */
	private Date birthDate ;
	/** 年龄*/
	private Integer age;
	/** 工作单位*/
	private String companyName;
	/** 职业代码*/
	private String IpsnOccClassCod;
	/** 通讯地址*/
	private String communicateAddr;
	/** 职业类别*/
	private String occupationCategory;
	/** 邮编 */
	private String postCode;
	/** 手机号*/
	private String mPhneNO;
	/** 电子邮件 */
	private String mailBox;
	/** 被保人异常告知*/
	private String healthStatFlag;
	/** 职务 */
	private String post;
	/** 收入来源*/
	private String incomeSource;
	/** 与主被保人关系*/
	private String  relToMstIpsn;
	/** 国籍*/
	private String nationality;
	/** 医保身份*/
	private String ipsnSss;
	/** 办公电话*/
	private String officeTelephone;
	/** 家庭电话*/
	private String hPhoneNo;
	/** 平均年收入*/
	private String avgEarning;
	/** 受益人信息 */
	private List<ComBinedBeneficiaryVo> beneficiaryVo = new ArrayList<ComBinedBeneficiaryVo>();
	/**
	 * @return the ipsnRelationCode
	 */
	public String getIpsnRelationCode() {
		return IpsnRelationCode;
	}
	/**
	 * @param ipsnRelationCode the ipsnRelationCode to set
	 */
	public void setIpsnRelationCode(String ipsnRelationCode) {
		IpsnRelationCode = ipsnRelationCode;
	}
	/**
	 * @return the relIpsnFlag
	 */
	public String getRelIpsnFlag() {
		return relIpsnFlag;
	}
	/**
	 * @param relIpsnFlag the relIpsnFlag to set
	 */
	public void setRelIpsnFlag(String relIpsnFlag) {
		this.relIpsnFlag = relIpsnFlag;
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
		return IpsnOccClassCod;
	}
	/**
	 * @param ipsnOccClassCod the ipsnOccClassCod to set
	 */
	public void setIpsnOccClassCod(String ipsnOccClassCod) {
		IpsnOccClassCod = ipsnOccClassCod;
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
	 * @return the mPhneNO
	 */
	public String getmPhneNO() {
		return mPhneNO;
	}
	/**
	 * @param mPhneNO the mPhneNO to set
	 */
	public void setmPhneNO(String mPhneNO) {
		this.mPhneNO = mPhneNO;
	}
	/**
	 * @return the mailBox
	 */
	public String getMailBox() {
		return mailBox;
	}
	/**
	 * @param mailBox the mailBox to set
	 */
	public void setMailBox(String mailBox) {
		this.mailBox = mailBox;
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
	 * @return the relToMstIpsn
	 */
	public String getRelToMstIpsn() {
		return relToMstIpsn;
	}
	/**
	 * @param relToMstIpsn the relToMstIpsn to set
	 */
	public void setRelToMstIpsn(String relToMstIpsn) {
		this.relToMstIpsn = relToMstIpsn;
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
	/**
	 * @return the avgEarning
	 */
	public String getAvgEarning() {
		return avgEarning;
	}
	/**
	 * @param avgEarning the avgEarning to set
	 */
	public void setAvgEarning(String avgEarning) {
		this.avgEarning = avgEarning;
	}
	/**
	 * @return the beneficiaryVo
	 */
	public List<ComBinedBeneficiaryVo> getBeneficiaryVo() {
		return beneficiaryVo;
	}
	/**
	 * @param beneficiaryVo the beneficiaryVo to set
	 */
	public void setBeneficiaryVo(List<ComBinedBeneficiaryVo> beneficiaryVo) {
		this.beneficiaryVo = beneficiaryVo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RecognizeelVo [IpsnRelationCode=" + IpsnRelationCode + ", relIpsnFlag=" + relIpsnFlag + ", custNo="
				+ custNo + ", name=" + name + ", ipsnIdType=" + ipsnIdType + ", ipsnIdCode=" + ipsnIdCode + ", sex="
				+ sex + ", birthDate=" + birthDate + ", age=" + age + ", companyName=" + companyName
				+ ", IpsnOccClassCod=" + IpsnOccClassCod + ", communicateAddr=" + communicateAddr
				+ ", occupationCategory=" + occupationCategory + ", postCode=" + postCode + ", mPhneNO=" + mPhneNO
				+ ", mailBox=" + mailBox + ", healthStatFlag=" + healthStatFlag + ", post=" + post + ", incomeSource="
				+ incomeSource + ", relToMstIpsn=" + relToMstIpsn + ", nationality=" + nationality + ", ipsnSss="
				+ ipsnSss + ", officeTelephone=" + officeTelephone + ", hPhoneNo=" + hPhoneNo + ", avgEarning="
				+ avgEarning + ", beneficiaryVo=" + beneficiaryVo + "]";
	}
	
}
