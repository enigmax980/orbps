package com.newcore.orbps.models.custaccount;

import java.io.Serializable;

/**
 * @author wangxiao
 * 创建时间：2016年8月24日下午3:25:55
 */
public class ReCustomerAccountVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6608913497106729374L;
	
	//PARTY_ID
	private String partyId;
	
	//客户号
	private String custNo;
	
	//省机构号
	private String provBranchNo;
	
	//系统来源
	private String srcSys;
	
	//投保单号
	private String applNo;
	
	//投被保受益人标志
	private String role;
	
	//被保人顺序号
	private String ipsnNo;
	
	//返回代码
	private String reNum;

	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
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
	public String getIpsnNo() {
		return ipsnNo;
	}

	/**
	 * @param ipsnNo the ipsnNo to set
	 */
	public void setIpsnNo(String ipsnNo) {
		this.ipsnNo = ipsnNo;
	}

	/**
	 * @return the reNum
	 */
	public String getReNum() {
		return reNum;
	}

	/**
	 * @param reNum the reNum to set
	 */
	public void setReNum(String reNum) {
		this.reNum = reNum;
	}
	
	
}
