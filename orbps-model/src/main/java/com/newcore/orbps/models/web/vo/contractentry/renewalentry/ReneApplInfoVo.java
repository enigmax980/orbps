package com.newcore.orbps.models.web.vo.contractentry.renewalentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保信息
 * @author wangyupeng
 *
 */
public class ReneApplInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 2149444548L;
	
	/** 续保通知书**/
	private String renewalNotice;
	/**上期保单号**/
	private String previousPolicyNo;
	/**单位/团体名称**/
	private String unitName;
	/**职业类别**/
	private String occupationType;
	/**证件类型**/
	private String idType;
	/**证件号码**/
	private Long idNo;
	/**通讯地址**/
	private String posAddress;
	/**邮编**/
	private String zipCode;
	/**联系人姓名**/
	private String contactsName;
	/**联系人证件类型**/
	private String contactsIdType;
	/**联系人证件号码**/
	private String contactsIdNo;
	/**性别**/
	private String contactsSex;
	/**联系人出生日期**/
	private Date contactsBirthlDate;
	/**联系人移动电话**/
	private String contactsMobPhone;
	/**联系人邮箱**/
	private String contactsEmail;
	/**固定电话**/
	private String lanPhone;	
	/**传真号码**/
	private String faxNo;
	/**投保人数**/
	private Long insureNumber;
	/**总保费**/
	private String totalPremium;
	/**
	 * @return the renewalNotice
	 */
	public String getRenewalNotice() {
		return renewalNotice;
	}
	/**
	 * @param renewalNotice the renewalNotice to set
	 */
	public void setRenewalNotice(String renewalNotice) {
		this.renewalNotice = renewalNotice;
	}
	/**
	 * @return the previousPolicyNo
	 */
	public String getPreviousPolicyNo() {
		return previousPolicyNo;
	}
	/**
	 * @param previousPolicyNo the previousPolicyNo to set
	 */
	public void setPreviousPolicyNo(String previousPolicyNo) {
		this.previousPolicyNo = previousPolicyNo;
	}
	/**
	 * @return the unitName
	 */
	public String getUnitName() {
		return unitName;
	}
	/**
	 * @param unitName the unitName to set
	 */
	public void setUnitName(String unitName) {
		this.unitName = unitName;
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
	 * @return the contactsName
	 */
	public String getContactsName() {
		return contactsName;
	}
	/**
	 * @param contactsName the contactsName to set
	 */
	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}
	/**
	 * @return the contactsIdType
	 */
	public String getContactsIdType() {
		return contactsIdType;
	}
	/**
	 * @param contactsIdType the contactsIdType to set
	 */
	public void setContactsIdType(String contactsIdType) {
		this.contactsIdType = contactsIdType;
	}
	/**
	 * @return the contactsIdNo
	 */
	public String getContactsIdNo() {
		return contactsIdNo;
	}
	/**
	 * @param contactsIdNo the contactsIdNo to set
	 */
	public void setContactsIdNo(String contactsIdNo) {
		this.contactsIdNo = contactsIdNo;
	}
	/**
	 * @return the contactsSex
	 */
	public String getContactsSex() {
		return contactsSex;
	}
	/**
	 * @param contactsSex the contactsSex to set
	 */
	public void setContactsSex(String contactsSex) {
		this.contactsSex = contactsSex;
	}
	/**
	 * @return the contactsBirthlDate
	 */
	public Date getContactsBirthlDate() {
		return contactsBirthlDate;
	}
	/**
	 * @param contactsBirthlDate the contactsBirthlDate to set
	 */
	public void setContactsBirthlDate(Date contactsBirthlDate) {
		this.contactsBirthlDate = contactsBirthlDate;
	}
	/**
	 * @return the contactsMobPhone
	 */
	public String getContactsMobPhone() {
		return contactsMobPhone;
	}
	/**
	 * @param contactsMobPhone the contactsMobPhone to set
	 */
	public void setContactsMobPhone(String contactsMobPhone) {
		this.contactsMobPhone = contactsMobPhone;
	}
	/**
	 * @return the contactsEmail
	 */
	public String getContactsEmail() {
		return contactsEmail;
	}
	/**
	 * @param contactsEmail the contactsEmail to set
	 */
	public void setContactsEmail(String contactsEmail) {
		this.contactsEmail = contactsEmail;
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
	 * @return the faxNo
	 */
	public String getFaxNo() {
		return faxNo;
	}
	/**
	 * @param faxNo the faxNo to set
	 */
	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}
	/**
	 * @return the insureNumber
	 */
	public Long getInsureNumber() {
		return insureNumber;
	}
	/**
	 * @param insureNumber the insureNumber to set
	 */
	public void setInsureNumber(Long insureNumber) {
		this.insureNumber = insureNumber;
	}
	/**
	 * @return the totalPremium
	 */
	public String getTotalPremium() {
		return totalPremium;
	}
	/**
	 * @param totalPremium the totalPremium to set
	 */
	public void setTotalPremium(String totalPremium) {
		this.totalPremium = totalPremium;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ApplInfoVo [renewalNotice=" + renewalNotice + ", previousPolicyNo=" + previousPolicyNo + ", unitName="
				+ unitName + ", occupationType=" + occupationType + ", idType=" + idType + ", idNo=" + idNo
				+ ", posAddress=" + posAddress + ", zipCode=" + zipCode + ", contactsName=" + contactsName
				+ ", contactsIdType=" + contactsIdType + ", contactsIdNo=" + contactsIdNo + ", contactsSex="
				+ contactsSex + ", contactsBirthlDate=" + contactsBirthlDate + ", contactsMobPhone=" + contactsMobPhone
				+ ", contactsEmail=" + contactsEmail + ", lanPhone=" + lanPhone + ", faxNo=" + faxNo + ", insureNumber="
				+ insureNumber + ", totalPremium=" + totalPremium + "]";
	}
	
	
}
