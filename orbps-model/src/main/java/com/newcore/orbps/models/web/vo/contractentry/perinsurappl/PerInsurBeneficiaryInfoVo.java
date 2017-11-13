package com.newcore.orbps.models.web.vo.contractentry.perinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 受益人信息
 * @author wangyupeng
 *
 */
public class PerInsurBeneficiaryInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1149444544L;
	
	/**序号**/
	private String applId;
	/**姓名**/
	private String Name;
	/**性别**/
	private String sex;
	/** 份额分子 **/
	private String shareMolecule;
	/** 与被保人关系 **/
	private String relToIpsn;
	/**出生日期**/
	private Date birthDate;
	/**份额分母**/
	private String Share;
	/**证件类型**/
	private String idType;
	/**证件号码**/
	private Long idNo;
	/**联系方式**/
	private String contactInfo;
	/**
	 * @return the applId
	 */
	public String getApplId() {
		return applId;
	}
	/**
	 * @param applId the applId to set
	 */
	public void setApplId(String applId) {
		this.applId = applId;
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
	 * @return the shareMolecule
	 */
	public String getShareMolecule() {
		return shareMolecule;
	}
	/**
	 * @param shareMolecule the shareMolecule to set
	 */
	public void setShareMolecule(String shareMolecule) {
		this.shareMolecule = shareMolecule;
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
	 * @return the share
	 */
	public String getShare() {
		return Share;
	}
	/**
	 * @param share the share to set
	 */
	public void setShare(String share) {
		Share = share;
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
	 * @return the contactInfo
	 */
	public String getContactInfo() {
		return contactInfo;
	}
	/**
	 * @param contactInfo the contactInfo to set
	 */
	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BeneficiaryInfo [applId=" + applId + ", Name=" + Name + ", sex=" + sex + ", shareMolecule="
				+ shareMolecule + ", relToIpsn=" + relToIpsn + ", birthDate=" + birthDate + ", Share=" + Share
				+ ", idType=" + idType + ", idNo=" + idNo + ", contactInfo=" + contactInfo + "]";
	}

	
}
