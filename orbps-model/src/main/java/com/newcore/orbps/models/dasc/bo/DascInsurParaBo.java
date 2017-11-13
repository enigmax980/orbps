package com.newcore.orbps.models.dasc.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiao
 * 创建时间：2016年9月20日下午5:21:34
 */
public class DascInsurParaBo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2217976031363757922L;
	//业务流水号
	private String businessKey;
	//投保单号
	private String applNo;
	//保单类型
	private String cntrType;
	//清单导入文件路径
	private String listFilePath;
	//创建时间
	private Date createDate;
	//是否有清单
	private String isInsuredList;
	/**
	 * @return the businessKey
	 */
	public String getBusinessKey() {
		return businessKey;
	}

	/**
	 * @param businessKey the businessKey to set
	 */
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
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
	 * @return the cntrType
	 */
	public String getCntrType() {
		return cntrType;
	}

	/**
	 * @param cntrType the cntrType to set
	 */
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	/**
	 * @return the listFilePath
	 */
	public String getListFilePath() {
		return listFilePath;
	}

	/**
	 * @param listFilePath the listFilePath to set
	 */
	public void setListFilePath(String listFilePath) {
		this.listFilePath = listFilePath;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the isInsuredList
	 */
	public String getIsInsuredList() {
		return isInsuredList;
	}

	/**
	 * @param isInsuredList the isInsuredList to set
	 */
	public void setIsInsuredList(String isInsuredList) {
		this.isInsuredList = isInsuredList;
	}
	
}
