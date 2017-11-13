package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;

/**
 * 清单上传信息Vo
 * @author jincong
 *
 */
public class ListImportUploadVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080002L;
	
	/** 投保单号 */
	private String applNo;
	/** 投保人客户号 */
	private String custNo;
	/** 投保总人数 */
	private Long insuredTotalNum;
	/** 清单文件 */
	private String listFile;
	
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
	 * @return the insuredTotalNum
	 */
	public Long getInsuredTotalNum() {
		return insuredTotalNum;
	}
	/**
	 * @param insuredTotalNum the insuredTotalNum to set
	 */
	public void setInsuredTotalNum(Long insuredTotalNum) {
		this.insuredTotalNum = insuredTotalNum;
	}
	/**
	 * @return the listFile
	 */
	public String getListFile() {
		return listFile;
	}
	/**
	 * @param listFile the listFile to set
	 */
	public void setListFile(String listFile) {
		this.listFile = listFile;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListUploadVo [applNo=" + applNo + ", custNo=" + custNo + ", insuredTotalNum=" + insuredTotalNum
				+ ", listFile=" + listFile + "]";
	}
	
}
