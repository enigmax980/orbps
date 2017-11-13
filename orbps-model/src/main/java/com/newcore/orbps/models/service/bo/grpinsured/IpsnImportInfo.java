package com.newcore.orbps.models.service.bo.grpinsured;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wangxiao
 * 创建时间：2016年10月25日下午6:51:30
 */
public class IpsnImportInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -741978981867119342L;
	
	/* 字段名：投保单号，长度：16，是否必填：是 */
	private String applNo;
	/* 字段名：投保人客户号 */
	private String hldrCustNo;
	/* 字段名：投保人姓名，长度：200，是否必填：IF 清单汇交 && 被保人为未成年人 THEN 必填 */
	private String hldrName;
	/* 字段名：任务类型 */
	private String type;
	/* 字段名：任务状态 */
	private String status;
	/* 字段名：销售机构，长度：6，是否必填：是 */
	private String salesBranchNo;
	/* 字段名：是否查询下级销售机构信息 ，长度：2，是否必填：是 */
	private String findSubBranchNoFlag;
	/* 字段名：销售员工号，长度：8，是否必填：是 */
	private String salesNo;
	/* 字段名：任务开始时间*/
	private Date startTime;
	/* 字段名：任务结束时间*/
	private Date endTime;
	/* 字段名：投保人数，是否必填：是 */
	private Long ipsnNum;
	/* 字段名：本次导入总人数 */
	private Long thisIpsnNum;
	/* 字段名：本次导入正确数量 */
	private Long thisImportNum;
	/* 字段名：差错数量，是否必填：是 */
	private Long errorNum;
	/* 字段名：总导入正确数量，是否必填：是 */
	private Long importNum;
	/* 字段名：导入进度，是否必填：是 */
	private Double importProgress;
	/* 字段名：当前页数*/
	private Integer pageNum;
	/* 字段名：行数*/
	private Integer rowNum;
	/*字段名：总页数*/
	private Integer sumPage;
	/*字段名：操作机构号*/
	private String pclkBranchNo;
	/*字段名：操作员工号*/
	private String pclkNo;
	/*字段名：操作员工名*/
	private String  pclkName;
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
	 * @return the hldrCustNo
	 */
	public String getHldrCustNo() {
		return hldrCustNo;
	}
	/**
	 * @param hldrCustNo the hldrCustNo to set
	 */
	public void setHldrCustNo(String hldrCustNo) {
		this.hldrCustNo = hldrCustNo;
	}
	/**
	 * @return the hldrName
	 */
	public String getHldrName() {
		return hldrName;
	}
	/**
	 * @param hldrName the hldrName to set
	 */
	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
	 * @return the findSubBranchNoFlag
	 */
	public String getFindSubBranchNoFlag() {
		return findSubBranchNoFlag;
	}
	/**
	 * @param findSubBranchNoFlag the findSubBranchNoFlag to set
	 */
	public void setFindSubBranchNoFlag(String findSubBranchNoFlag) {
		this.findSubBranchNoFlag = findSubBranchNoFlag;
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
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the ipsnNum
	 */
	public Long getIpsnNum() {
		return ipsnNum;
	}
	/**
	 * @param ipsnNum the ipsnNum to set
	 */
	public void setIpsnNum(Long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}
	/**
	 * @return the importNum
	 */
	public Long getImportNum() {
		return importNum;
	}
	/**
	 * @param importNum the importNum to set
	 */
	public void setImportNum(Long importNum) {
		this.importNum = importNum;
	}
	/**
	 * @return the importProgress
	 */
	public Double getImportProgress() {
		return importProgress;
	}
	/**
	 * @return the thisIpsnNum
	 */
	public Long getThisIpsnNum() {
		return thisIpsnNum;
	}
	/**
	 * @param thisIpsnNum the thisIpsnNum to set
	 */
	public void setThisIpsnNum(Long thisIpsnNum) {
		this.thisIpsnNum = thisIpsnNum;
	}
	/**
	 * @return the thisImportNum
	 */
	public Long getThisImportNum() {
		return thisImportNum;
	}
	/**
	 * @param thisImportNum the thisImportNum to set
	 */
	public void setThisImportNum(Long thisImportNum) {
		this.thisImportNum = thisImportNum;
	}
	/**
	 * @return the errorNum
	 */
	public Long getErrorNum() {
		return errorNum;
	}
	/**
	 * @param errorNum the errorNum to set
	 */
	public void setErrorNum(Long errorNum) {
		this.errorNum = errorNum;
	}
	/**
	 * @param importProgress the importProgress to set
	 */
	public void setImportProgress(Double importProgress) {
		this.importProgress = importProgress;
	}
	/**
	 * @return the pageNum
	 */
	public Integer getPageNum() {
		return pageNum;
	}
	/**
	 * @param pageNum the pageNum to set
	 */
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	/**
	 * @return the rowNum
	 */
	public Integer getRowNum() {
		return rowNum;
	}
	/**
	 * @param rowNum the rowNum to set
	 */
	public void setRowNum(Integer rowNum) {
		this.rowNum = rowNum;
	}
	/**
	 * @return the sumPage
	 */
	public Integer getSumPage() {
		return sumPage;
	}
	/**
	 * @param sumPage the sumPage to set
	 */
	public void setSumPage(Integer sumPage) {
		this.sumPage = sumPage;
	}
	/**
	 * @return the pclkBranchNo
	 */
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	/**
	 * @param pclkBranchNo the pclkBranchNo to set
	 */
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	/**
	 * @return the pclkNo
	 */
	public String getPclkNo() {
		return pclkNo;
	}
	/**
	 * @param pclkNo the pclkNo to set
	 */
	public void setPclkNo(String pclkNo) {
		this.pclkNo = pclkNo;
	}
	/**
	 * @return the pclkName
	 */
	public String getPclkName() {
		return pclkName;
	}
	/**
	 * @param pclkName the pclkName to set
	 */
	public void setPclkName(String pclkName) {
		this.pclkName = pclkName;
	}

}
