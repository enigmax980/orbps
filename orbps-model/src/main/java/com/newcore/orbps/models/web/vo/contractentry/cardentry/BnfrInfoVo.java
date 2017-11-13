package com.newcore.orbps.models.web.vo.contractentry.cardentry;

import java.io.Serializable;
import java.util.Date;

/**
 * 要约信息Vo
 * @author jincong
 *
 */
public class BnfrInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146020003L;
	/** 姓名 */
	private String applName;
	/** 性别 */
	private String sex;
	/** 出生日期 */
	private Date birthDate;
	/** 与被保险人关系 */
	private String relationship;
	/** 份额分子 */
	private String molecule;
	/** 份额分母 */
	private String denominator;
	/** 证件类型 */
	private String idType;
	/** 证件号码 */
	private String idNum;
	
	/**
	 * @return the applName
	 */
	public String getApplName() {
		return applName;
	}
	/**
	 * @param applName the applName to set
	 */
	public void setApplName(String applName) {
		this.applName = applName;
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
	 * @return the relationship
	 */
	public String getRelationship() {
		return relationship;
	}
	/**
	 * @param relationship the relationship to set
	 */
	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}
	/**
	 * @return the molecule
	 */
	public String getMolecule() {
		return molecule;
	}
	/**
	 * @param molecule the molecule to set
	 */
	public void setMolecule(String molecule) {
		this.molecule = molecule;
	}
	/**
	 * @return the denominator
	 */
	public String getDenominator() {
		return denominator;
	}
	/**
	 * @param denominator the denominator to set
	 */
	public void setDenominator(String denominator) {
		this.denominator = denominator;
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
	 * @return the idNum
	 */
	public String getIdNum() {
		return idNum;
	}
	/**
	 * @param idNum the idNum to set
	 */
	public void setIdNum(String idNum) {
		this.idNum = idNum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "bnfrInfoVo [applName=" + applName + ", sex=" + sex + ", birthDate=" + birthDate + ", relationship="
				+ relationship + ", molecule=" + molecule + ", denominator=" + denominator + ", idType=" + idType
				+ ", idNum=" + idNum + "]";
	}
	
}
