package com.newcore.orbps.models.web.vo.contractentry.loanaddappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保信息Vo
 * @author jincong
 *
 */
public class LoanAddApplInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1146030001L;
	
	/** 投保单号 */
	private String applNo;
	/** 投保日期 */
	private Date applDate;
	/** 销售渠道 */
	private String salesChannel;
	/** 销售机构 */
	private String salesBranch;
	/** 销售员工号 */
	private String salesNo;
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
	 * @return the applDate
	 */
	public Date getApplDate() {
		return applDate;
	}
	/**
	 * @param applDate the applDate to set
	 */
	public void setApplDate(Date applDate) {
		this.applDate = applDate;
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
	 * @return the salesBranch
	 */
	public String getSalesBranch() {
		return salesBranch;
	}
	/**
	 * @param salesBranch the salesBranch to set
	 */
	public void setSalesBranch(String salesBranch) {
		this.salesBranch = salesBranch;
	}
	/**
	 * @return the salesNo
	 */
	public String getSalesNo() {
		return salesNo;
	}
	/**
	 * @param salesNo the salesNo to set
	 */
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LoanAddForm [applNo=" + applNo + ", applDate=" + applDate + ", salesChannel=" + salesChannel
				+ ", salesBranch=" + salesBranch + ", salesNo=" + salesNo + "]";
	}
	
}
