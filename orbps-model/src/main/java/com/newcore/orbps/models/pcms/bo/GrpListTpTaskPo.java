package com.newcore.orbps.models.pcms.bo;


import java.io.Serializable;
import java.util.Date;

/**
 * 团单清单转保费任务表
 *
 * @author zhanghui
 *
 * @date create on  2016年9月7日下午7:19:30
 */
public class GrpListTpTaskPo implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 6559634333388860065L;

	private Long taskId;
	
	private String applNo;
	
	private String cgNo;
	
	private String mgrBranchNo;
	
	private Date createDate;
	
	private String dealStatus;
	
	private String dataSource;

	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
}