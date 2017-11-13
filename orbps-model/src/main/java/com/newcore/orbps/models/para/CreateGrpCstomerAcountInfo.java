package com.newcore.orbps.models.para;

import java.io.Serializable;

/**
 * 法人开户请求类
 * @author wangxiao
 * 创建时间：2016年7月25日下午5:40:52
 */
public class CreateGrpCstomerAcountInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1725762938030981968L;

	private String flag;
	
	private String custNo;
	
	private String custOacBranchNo;
	
	private String name;
	
	private String oldName;
	
	private String idType;
	
	private String idNo;
	
	private String legalCode;
	
	private String natureCode;
	
	private String occClassCode;
	
	private String corpRep;
	
	private String contactPsn;
	
	private String contactPsnSex;
	
	private String numOfEmp;

	/**
	 * 
	 */
	public CreateGrpCstomerAcountInfo() {
		super();
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
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
	 * @return the custOacBranchNo
	 */
	public String getCustOacBranchNo() {
		return custOacBranchNo;
	}

	/**
	 * @param custOacBranchNo the custOacBranchNo to set
	 */
	public void setCustOacBranchNo(String custOacBranchNo) {
		this.custOacBranchNo = custOacBranchNo;
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
	 * @return the oldName
	 */
	public String getOldName() {
		return oldName;
	}

	/**
	 * @param oldName the oldName to set
	 */
	public void setOldName(String oldName) {
		this.oldName = oldName;
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
	 * @return the legalCode
	 */
	public String getLegalCode() {
		return legalCode;
	}

	/**
	 * @param legalCode the legalCode to set
	 */
	public void setLegalCode(String legalCode) {
		this.legalCode = legalCode;
	}

	/**
	 * @return the natureCode
	 */
	public String getNatureCode() {
		return natureCode;
	}

	/**
	 * @param natureCode the natureCode to set
	 */
	public void setNatureCode(String natureCode) {
		this.natureCode = natureCode;
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
	 * @return the corpRep
	 */
	public String getCorpRep() {
		return corpRep;
	}

	/**
	 * @param corpRep the corpRep to set
	 */
	public void setCorpRep(String corpRep) {
		this.corpRep = corpRep;
	}

	/**
	 * @return the contactPsn
	 */
	public String getContactPsn() {
		return contactPsn;
	}

	/**
	 * @param contactPsn the contactPsn to set
	 */
	public void setContactPsn(String contactPsn) {
		this.contactPsn = contactPsn;
	}

	/**
	 * @return the contactPsnSex
	 */
	public String getContactPsnSex() {
		return contactPsnSex;
	}

	/**
	 * @param contactPsnSex the contactPsnSex to set
	 */
	public void setContactPsnSex(String contactPsnSex) {
		this.contactPsnSex = contactPsnSex;
	}

	/**
	 * @return the numOfEmp
	 */
	public String getNumOfEmp() {
		return numOfEmp;
	}

	/**
	 * @param numOfEmp the numOfEmp to set
	 */
	public void setNumOfEmp(String numOfEmp) {
		this.numOfEmp = numOfEmp;
	}
	
	
}
