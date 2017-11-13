package com.newcore.orbps.models.taskprmy;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;

public class PBatchErrLogNewPO implements Serializable {

	private static final long serialVersionUID = -6478011703028083026L;
	
	private String pkid;
	private String taskId;
	private String taskExecutionId;
	private Long jobInstanceId;
	private Long jobExecutionId;
	private String businessType;
	private String businessKey;
	private String errorCode;
	private String errorInfo;
	private Date createDate;
	public String getPkid() {
		return pkid;
	}
	public void setPkid(String pkid) {
		this.pkid = pkid;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getTaskExecutionId() {
		return taskExecutionId;
	}
	public void setTaskExecutionId(String taskExecutionId) {
		this.taskExecutionId = taskExecutionId;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorInfo() {
		return errorInfo;
	}
	public void setErrorInfo(String errorInfo) {
		this.errorInfo = errorInfo;
	}
	public Long getJobInstanceId() {
		return jobInstanceId;
	}
	public void setJobInstanceId(Long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}
	public Long getJobExecutionId() {
		return jobExecutionId;
	}
	public void setJobExecutionId(Long jobExecutionId) {
		this.jobExecutionId = jobExecutionId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	@Override
	public String toString() {
		return "BatchErrLogNewPo [pkid=" + pkid + ", taskId=" + taskId + ", taskExecutionId=" + taskExecutionId
				+ ", jobInstanceId=" + jobInstanceId + ", jobExecutionId=" + jobExecutionId + ", businessType="
				+ businessType + ", businessKey=" + businessKey + ", errorCode=" + errorCode + ", errorInfo="
				+ errorInfo + ", createDate=" + createDate + "]";
	}
		
}
