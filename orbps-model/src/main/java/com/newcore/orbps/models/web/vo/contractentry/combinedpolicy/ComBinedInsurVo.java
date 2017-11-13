package com.newcore.orbps.models.web.vo.contractentry.combinedpolicy;

import java.io.Serializable;
/**
 * 共保协议信息  
 * @author wangyanjie
 *
 */
public class ComBinedInsurVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000006L;
	/** 投保单号 */
	private String applNo;
	/**  险种组合代码  */
	private String combProdCode;
	/** 份数 */
	private Long portionNum;
	/** 销售渠道 */
	private String salesChannel;
	/** 销售员机构号 */
	private String salesBranchNo;
	/** 销售员工号 */
	private String salesCode;
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
	 * @return the combProdCode
	 */
	public String getCombProdCode() {
		return combProdCode;
	}
	/**
	 * @param combProdCode the combProdCode to set
	 */
	public void setCombProdCode(String combProdCode) {
		this.combProdCode = combProdCode;
	}
	/**
	 * @return the portionNum
	 */
	public Long getPortionNum() {
		return portionNum;
	}
	/**
	 * @param portionNum the portionNum to set
	 */
	public void setPortionNum(Long portionNum) {
		this.portionNum = portionNum;
	}
	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}
	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	/**
	 * @return the salesCode
	 */
	public String getSalesCode() {
		return salesCode;
	}
	/**
	 * @param salesCode the salesCode to set
	 */
	public void setSalesCode(String salesCode) {
		this.salesCode = salesCode;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "combinedPolicyVo [applNo=" + applNo + ", combProdCode=" + combProdCode + ", portionNum=" + portionNum
				+ ", salesChannel=" + salesChannel + ", salesBranchNo=" + salesBranchNo + ", salesCode=" + salesCode
				+ "]";
	}
	

	
}
