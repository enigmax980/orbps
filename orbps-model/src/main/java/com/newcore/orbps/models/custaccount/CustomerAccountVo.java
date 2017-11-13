package com.newcore.orbps.models.custaccount;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiao
 * 创建时间：2016年8月24日上午11:20:40
 */
public class CustomerAccountVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7088489606071463217L;
	//省机构号
	private String provBranchNo;
	//系统来源
	private String srcSys;
	//管理机构
	private String custOacBranchNo;
	//投保单号
	private String applNo;
	//投被保受益人标志
	private String role;
	//被保人顺序号
	private Long ipsnNo;
	//姓名
	private String name;
	//证件类型
	private String idType;
	//证件号码
	private String idNo;
	//性别
	private String sex;
	//出生日期
	private Date birthDate;
	/**
	 * @return the provBranchNo
	 */
	public String getProvBranchNo() {
		return provBranchNo;
	}
	/**
	 * @param provBranchNo the provBranchNo to set
	 */
	public void setProvBranchNo(String provBranchNo) {
		this.provBranchNo = provBranchNo;
	}
	/**
	 * @return the srcSys
	 */
	public String getSrcSys() {
		return srcSys;
	}
	/**
	 * @param srcSys the srcSys to set
	 */
	public void setSrcSys(String srcSys) {
		this.srcSys = srcSys;
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
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the role
	 */
	public String getRole() {
		return role;
	}
	/**
	 * @param role the role to set
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * @return the ipsnNo
	 */
	public Long getIpsnNo() {
		return ipsnNo;
	}
	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(Long ipsnNo) {
		this.ipsnNo = ipsnNo;
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

}
