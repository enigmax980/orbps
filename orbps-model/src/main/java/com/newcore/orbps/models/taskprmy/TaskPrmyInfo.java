package com.newcore.orbps.models.taskprmy;

import java.io.Serializable;

public class TaskPrmyInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 任务ID	
	 */
	private Long taskSeq;
	
	/**
	 * 任务状态
	 */
	private String status;
	/**
	 * 销售机构号
	 */
	private String salesBranchNo;
	/**
	 * 任务创建时间
	 */
	private String createTime;
	/**
	 * 开始执行时间
	 */
	private String startTime;
	/**
	 * 任务完成时间
	 */
	private String endTime;
	/**
	 * 执行次数
	 */
	private Integer askTimes;
	/**
	 * 业务主键
	 */
	private String businessKey;
	
	/**
	 * 业务主键
	 */
	private String taskId;
	/**
	 * 投保单号
	 */
	private String applNo;
	/**
	 * 清单路径
	 */
	private String listPath;
	/**
	 * 批作业实例
	 */
	private Long jobInstanceId;
	/**
	 * 任务队列名称
	 */
	private String taskTable;
	/**
	 * 备注
	 */
	private String remark;
	
	
	private String extKey0;
	
	private String extKey1;
	
	private String extKey2;
	
	private String extKey3;
	private String extKey4;
	
	private String extKey5;
	
	public Long getTaskSeq() {
		return taskSeq;
	}
	public void setTaskSeq(Long taskSeq) {
		this.taskSeq = taskSeq;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	
	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * @return the createTime
	 */
	public String getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	/**
	 * @return the endTime
	 */
	public String getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getAskTimes() {
		return askTimes;
	}
	public void setAskTimes(Integer askTimes) {
		this.askTimes = askTimes;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	/**
	 * @return the taskId
	 */
	public String getTaskId() {
		return taskId;
	}
	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getListPath() {
		return listPath;
	}
	public void setListPath(String listPath) {
		this.listPath = listPath;
	}
	
	public String getTaskTable() {
		return taskTable;
	}
	public void setTaskTable(String taskTable) {
		this.taskTable = taskTable;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	/**
	 * @return the extKey0
	 */
	public String getExtKey0() {
		return extKey0;
	}
	/**
	 * @param extKey0 the extKey0 to set
	 */
	public void setExtKey0(String extKey0) {
		this.extKey0 = extKey0;
	}
	/**
	 * @return the extKey1
	 */
	public String getExtKey1() {
		return extKey1;
	}
	/**
	 * @param extKey1 the extKey1 to set
	 */
	public void setExtKey1(String extKey1) {
		this.extKey1 = extKey1;
	}
	/**
	 * @return the extKey2
	 */
	public String getExtKey2() {
		return extKey2;
	}
	/**
	 * @param extKey2 the extKey2 to set
	 */
	public void setExtKey2(String extKey2) {
		this.extKey2 = extKey2;
	}
	/**
	 * @return the extKey3
	 */
	public String getExtKey3() {
		return extKey3;
	}
	/**
	 * @param extKey3 the extKey3 to set
	 */
	public void setExtKey3(String extKey3) {
		this.extKey3 = extKey3;
	}
	/**
	 * @return the extKey4
	 */
	public String getExtKey4() {
		return extKey4;
	}
	/**
	 * @param extKey4 the extKey4 to set
	 */
	public void setExtKey4(String extKey4) {
		this.extKey4 = extKey4;
	}
	/**
	 * @return the extKey5
	 */
	public String getExtKey5() {
		return extKey5;
	}
	/**
	 * @param extKey5 the extKey5 to set
	 */
	public void setExtKey5(String extKey5) {
		this.extKey5 = extKey5;
	}
	/**
	 * @param jobInstanceId the jobInstanceId to set
	 */
	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}
	
	
	/**
	 * @return the jobInstanceId
	 */
	public Long getJobInstanceId() {
		return jobInstanceId;
	}
	
	@Override
	public String toString() {
		return "TaskPrmyInfo [taskSeq=" + taskSeq + ", status=" + status + ", salesBranchNo=" + salesBranchNo
				+ ", createTime=" + createTime + ", startTime=" + startTime + ", endTime=" + endTime + ", askTimes="
				+ askTimes + ", businessKey=" + businessKey + ", taskId=" + taskId + ", applNo=" + applNo
				+ ", listPath=" + listPath + ", jobInstanceId=" + jobInstanceId + ", taskTable=" + taskTable
				+ ", remark=" + remark + ", extKey0=" + extKey0 + ", extKey1=" + extKey1 + ", extKey2=" + extKey2
				+ ", extKey3=" + extKey3 + ", extKey4=" + extKey4 + ", extKey5=" + extKey5 + "]";
	}
	
	
	
}
