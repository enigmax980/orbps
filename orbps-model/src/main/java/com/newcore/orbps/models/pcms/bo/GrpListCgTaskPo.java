package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * 团单清单转合同任务表Model.
 */
public class GrpListCgTaskPo implements Serializable {

	private static final long serialVersionUID = -8249254334135000418L;

	
	/**
	 * 任务ID
	 */
	private Long taskId;
	
	/**
	 * 组合保单号
	 */
	private String cgNo;
	
	/**
	 * 投保单号
	 */
    private String applNo;
	/**
	 * 管理机构
	 */
	private String mgrBranchNo;
	/**
	 * 险种（第一主险）
	 */
	private String polCode;
	
	/**
	 * 批次号
	 */
	private Long batNo;
	
	/**
	 * 清单总量
	 */
	private Long ListTtlCount;
	
	/**
	 * 总保费
	 */
	private Double SumPremium;
	
	/**
	 * 总保额
	 */
	private Double SumFaceAmnt;
	
	/**
	 * 生成日期
	 */
	private Date createDate;
	
	/**
	 * 处理状态
	 */
	private String dealStatus;
	
	/**
	 * 已处理笔数
	 */
	private Long dealCount;
	
	/**
	 * 失败笔数
	 */
	private Long failCount;
	
	private String dataSource;
	
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}
	public Long getBatNo() {
		return batNo;
	}
	public void setBatNo(Long batNo) {
		this.batNo = batNo;
	}
	
	public Long getListTtlCount() {
		return ListTtlCount;
	}
	public void setListTtlCount(Long listTtlCount) {
		ListTtlCount = listTtlCount;
	}
	public Double getSumPremium() {
		return SumPremium;
	}
	public void setSumPremium(Double sumPremium) {
		SumPremium = sumPremium;
	}
	public Double getSumFaceAmnt() {
		return SumFaceAmnt;
	}
	public void setSumFaceAmnt(Double sumFaceAmnt) {
		SumFaceAmnt = sumFaceAmnt;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getDealStatus() {
		return dealStatus;
	}
	public void setDealStatus(String dealStatus) {
		this.dealStatus = dealStatus;
	}
	public Long getDealCount() {
		return dealCount;
	}
	public void setDealCount(Long dealCount) {
		this.dealCount = dealCount;
	}
	public Long getFailCount() {
		return failCount;
	}
	public void setFailCount(Long failCount) {
		this.failCount = failCount;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
}