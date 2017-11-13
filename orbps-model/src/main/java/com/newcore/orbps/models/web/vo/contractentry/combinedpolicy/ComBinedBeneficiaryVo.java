package com.newcore.orbps.models.web.vo.contractentry.combinedpolicy;

import java.io.Serializable;
import java.util.Date;
/**
 * 受益人信息 
 * @author wangyanjie 
 *
 */
public class ComBinedBeneficiaryVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000012L;

	/** 受益人姓名*/
	private String bnfrName;
	/** 受益人性别*/
	private String bnfrSex;
	/** 受益人出生日期*/
	private Date bnfrBirthDate;
	/** 受益人与被保险人关系*/
	private String bnfrRelationship;
	/** 份额分子*/
	private Integer bnfrMolecule;
	/** 份额分母*/
	private Integer bnfrDenominator;
	/** 受益人证件类型*/
	private String bnfrIdType;
	/** 受益人证件号码*/
	private String bnfrIdNum;
	/**
	 * @return the bnfrName
	 */
	public String getBnfrName() {
		return bnfrName;
	}
	/**
	 * @param bnfrName the bnfrName to set
	 */
	public void setBnfrName(String bnfrName) {
		this.bnfrName = bnfrName;
	}
	/**
	 * @return the bnfrSex
	 */
	public String getBnfrSex() {
		return bnfrSex;
	}
	/**
	 * @param bnfrSex the bnfrSex to set
	 */
	public void setBnfrSex(String bnfrSex) {
		this.bnfrSex = bnfrSex;
	}
	
	/**
	 * @param bnfrRelationship the bnfrRelationship to set
	 */
	public void setBnfrRelationship(String bnfrRelationship) {
		this.bnfrRelationship = bnfrRelationship;
	}
	
	/**
	 * @return the bnfrBirthDate
	 */
	public Date getBnfrBirthDate() {
		return bnfrBirthDate;
	}
	/**
	 * @param bnfrBirthDate the bnfrBirthDate to set
	 */
	public void setBnfrBirthDate(Date bnfrBirthDate) {
		this.bnfrBirthDate = bnfrBirthDate;
	}
	/**
	 * @return the bnfrMolecule
	 */
	public Integer getBnfrMolecule() {
		return bnfrMolecule;
	}
	/**
	 * @param bnfrMolecule the bnfrMolecule to set
	 */
	public void setBnfrMolecule(Integer bnfrMolecule) {
		this.bnfrMolecule = bnfrMolecule;
	}
	/**
	 * @return the bnfrDenominator
	 */
	public Integer getBnfrDenominator() {
		return bnfrDenominator;
	}
	/**
	 * @param bnfrDenominator the bnfrDenominator to set
	 */
	public void setBnfrDenominator(Integer bnfrDenominator) {
		this.bnfrDenominator = bnfrDenominator;
	}
	/**
	 * @return the bnfrRelationship
	 */
	public String getBnfrRelationship() {
		return bnfrRelationship;
	}
	/**
	 * @return the bnfrIdType
	 */
	public String getBnfrIdType() {
		return bnfrIdType;
	}
	/**
	 * @param bnfrIdType the bnfrIdType to set
	 */
	public void setBnfrIdType(String bnfrIdType) {
		this.bnfrIdType = bnfrIdType;
	}
	/**
	 * @return the bnfrIdNum
	 */
	public String getBnfrIdNum() {
		return bnfrIdNum;
	}
	/**
	 * @param bnfrIdNum the bnfrIdNum to set
	 */
	public void setBnfrIdNum(String bnfrIdNum) {
		this.bnfrIdNum = bnfrIdNum;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BeneficiaryVo [bnfrName=" + bnfrName + ", bnfrSex=" + bnfrSex + ", bnfrBirthDate=" + bnfrBirthDate
				+ ", bnfrRelationship=" + bnfrRelationship + ", bnfrMolecule=" + bnfrMolecule + ", bnfrDenominator="
				+ bnfrDenominator + ", bnfrIdType=" + bnfrIdType + ", bnfrIdNum=" + bnfrIdNum + "]";
	}
		
}
