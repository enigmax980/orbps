package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;
import java.util.Date;

import com.newcore.orbps.models.service.bo.grpinsurappl.Address;

/**
 * 个人汇交信息
 * @author wangyanjie
 *
 */
public class SgGrpPersonalInsurInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 132423443L;
	/** 汇交人姓名 */
	private String joinName;
	/** 汇交人证件类型  */
	private String joinIdType;
	/** 汇交人证件号码 */
	private String joinIdNo;
	/** 汇交人性别 */
	private String joinSex;
	/** 汇交人出生日期  */
	private Date joinBirthDate;
	/** 汇交人移动电话 */
	private String joinMobile;
	/**汇交人邮箱*/
	private String joinEmail;
	/**汇交人固定电话*/
	private String joinTel;
	/** 汇交人传真号码 */
	private String joinFaxNo;
	/** 争议处理方式 */
	private String pSettleDispute;
	/** 职业类别 */
	private String occClassCode;
	/**汇交人省**/
	private String province;
	/**汇交人城市**/
	private String city;
	/**汇交人县**/
	private String county;
	/**汇交人乡**/
	private String town;
	/**汇交人村**/
	private String village;
	/**汇交人详细地址**/
	private String joinHome;
	/**汇交人邮编****/
	private String postCode;
	/**仲裁机构名称**/
	private String joinParbOrgName;
	/**
	 * @return the joinName
	 */
	public String getJoinName() {
		return joinName;
	}
	/**
	 * @param joinName the joinName to set
	 */
	public void setJoinName(String joinName) {
		this.joinName = joinName;
	}
	/**
	 * @return the joinIdType
	 */
	public String getJoinIdType() {
		return joinIdType;
	}
	/**
	 * @param joinIdType the joinIdType to set
	 */
	public void setJoinIdType(String joinIdType) {
		this.joinIdType = joinIdType;
	}
	/**
	 * @return the joinIdNo
	 */
	public String getJoinIdNo() {
		return joinIdNo;
	}
	/**
	 * @param joinIdNo the joinIdNo to set
	 */
	public void setJoinIdNo(String joinIdNo) {
		this.joinIdNo = joinIdNo;
	}
	/**
	 * @return the joinSex
	 */
	public String getJoinSex() {
		return joinSex;
	}
	/**
	 * @param joinSex the joinSex to set
	 */
	public void setJoinSex(String joinSex) {
		this.joinSex = joinSex;
	}
	/**
	 * @return the joinBirthDate
	 */
	public Date getJoinBirthDate() {
		return joinBirthDate;
	}
	/**
	 * @param joinBirthDate the joinBirthDate to set
	 */
	public void setJoinBirthDate(Date joinBirthDate) {
		this.joinBirthDate = joinBirthDate;
	}
	/**
	 * @return the joinMobile
	 */
	public String getJoinMobile() {
		return joinMobile;
	}
	/**
	 * @param joinMobile the joinMobile to set
	 */
	public void setJoinMobile(String joinMobile) {
		this.joinMobile = joinMobile;
	}
	/**
	 * @return the joinEmail
	 */
	public String getJoinEmail() {
		return joinEmail;
	}
	/**
	 * @param joinEmail the joinEmail to set
	 */
	public void setJoinEmail(String joinEmail) {
		this.joinEmail = joinEmail;
	}
	/**
	 * @return the joinTel
	 */
	public String getJoinTel() {
		return joinTel;
	}
	/**
	 * @param joinTel the joinTel to set
	 */
	public void setJoinTel(String joinTel) {
		this.joinTel = joinTel;
	}
	/**
	 * @return the joinFaxNo
	 */
	public String getJoinFaxNo() {
		return joinFaxNo;
	}
	/**
	 * @param joinFaxNo the joinFaxNo to set
	 */
	public void setJoinFaxNo(String joinFaxNo) {
		this.joinFaxNo = joinFaxNo;
	}
	
	/**
	 * @return the pSettleDispute
	 */
	public String getpSettleDispute() {
		return pSettleDispute;
	}
	/**
	 * @param pSettleDispute the pSettleDispute to set
	 */
	public void setpSettleDispute(String pSettleDispute) {
		this.pSettleDispute = pSettleDispute;
	}
	/**
	 * @return the occClassCode
	 */
	public String getOccClassCode() {
		return occClassCode;
	}
	/**
	 * @param occClassCode the occClassCode to set
	 */
	public void setOccClassCode(String occClassCode) {
		this.occClassCode = occClassCode;
	}
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}
	/**
	 * @param county the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}
	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}
	/**
	 * @param town the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}
	/**
	 * @return the village
	 */
	public String getVillage() {
		return village;
	}
	/**
	 * @param village the village to set
	 */
	public void setVillage(String village) {
		this.village = village;
	}
	

	/**
	 * @return the joinhome
	 */
	public String getJoinHome() {
		return joinHome;
	}
	/**
	 * @param joinhome the joinhome to set
	 */
	public void setJoinHome(String joinHome) {
		this.joinHome = joinHome;
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
	 * @return the parbOrgName
	 */
	public String getJoinParbOrgName() {
		return joinParbOrgName;
	}
	/**
	 * @param parbOrgName the parbOrgName to set
	 */
	public void setJoinParbOrgName(String joinParbOrgName) {
		this.joinParbOrgName = joinParbOrgName;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SgGrpPersonalInsurInfoVo [joinName=" + joinName + ", joinIdType=" + joinIdType + ", joinIdNo="
				+ joinIdNo + ", joinSex=" + joinSex + ", joinBirthDate=" + joinBirthDate + ", joinMobile=" + joinMobile
				+ ", joinEmail=" + joinEmail + ", joinTel=" + joinTel + ", joinFaxNo=" + joinFaxNo + ", pSettleDispute="
				+ pSettleDispute + ", occClassCode=" + occClassCode + ", province=" + province + ", city=" + city
				+ ", county=" + county + ", town=" + town + ", village=" + village + ", joinhome=" + joinHome + ", postCode="
				+ postCode + ", parbOrgName=" + joinParbOrgName + "]";
	}
	
	
	

	
	
}
