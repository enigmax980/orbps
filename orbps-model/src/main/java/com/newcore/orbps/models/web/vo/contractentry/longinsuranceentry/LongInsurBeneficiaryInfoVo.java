package com.newcore.orbps.models.web.vo.contractentry.longinsuranceentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 受益人信息
 * @author wangyupeng
 *
 */
public class LongInsurBeneficiaryInfoVo  implements Serializable{
	
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
	/**证件类型**/
	private String idType;
	/**证件号码**/
	private Long idNo;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BeneficiaryInfo [applId=" + applId + ", Name=" + Name + ", sex=" + sex + ", shareMolecule="
				+ shareMolecule + ", relToIpsn=" + relToIpsn + ", birthDate=" + birthDate + ", idType=" + idType
				+ ", idNo=" + idNo + "]";
	}
	
}
