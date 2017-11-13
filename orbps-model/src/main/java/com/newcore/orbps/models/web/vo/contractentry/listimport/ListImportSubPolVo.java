package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;

/**
 * 子要约信息Vo
 * @author huangyun
 *
 */
public class ListImportSubPolVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080002L;
	
	/** 责任代码 */
	private String subPolCode;
	/** 责任名称 */
	private String subPolName;
	/** 属组代码*/
	private Long groupCode;
	/** 属组名称*/
	private String groupName;
	/** 保额 */
	private Double amount;
	/** 保费 */
	private Double premium;
	/**
	 * @return the subPolCode
	 */
	public String getSubPolCode() {
		return subPolCode;
	}
	/**
	 * @param subPolCode the subPolCode to set
	 */
	public void setSubPolCode(String subPolCode) {
		this.subPolCode = subPolCode;
	}
	/**
	 * @return the subPolName
	 */
	public String getSubPolName() {
		return subPolName;
	}
	/**
	 * @param subPolName the subPolName to set
	 */
	public void setSubPolName(String subPolName) {
		this.subPolName = subPolName;
	}
	/**
	 * @return the groupCode
	 */
	public Long getGroupCode() {
		return groupCode;
	}
	/**
	 * @param groupCode the groupCode to set
	 */
	public void setGroupCode(Long groupCode) {
		this.groupCode = groupCode;
	}
	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}
	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	/**
	 * @return the premium
	 */
	public Double getPremium() {
		return premium;
	}
	/**
	 * @param premium the premium to set
	 */
	public void setPremium(Double premium) {
		this.premium = premium;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListImportSubPolVo [subPolCode=" + subPolCode + ", subPolName=" + subPolName + ", groupCode="
				+ groupCode + ", groupName=" + groupName + ", amount=" + amount + ", premium=" + premium + "]";
	}
	
}
