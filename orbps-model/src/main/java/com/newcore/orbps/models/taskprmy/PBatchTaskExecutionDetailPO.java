package com.newcore.orbps.models.taskprmy;

import java.io.Serializable;
/**
 * 批作业任务执行明细表
 * @author xushichao
 * 2017/2/6
 */
public class PBatchTaskExecutionDetailPO implements Serializable {

	private static final long serialVersionUID = 869862309510414032L;
	private String PkId;
	private String taskExecutionId;
	private Long jobInstanceId;
	private Long jobExecutionId;
	private String jobName;
	public String getPkId() {
		return PkId;
	}
	public void setPkId(String pkId) {
		PkId = pkId;
	}
	public String getTaskExecutionId() {
		return taskExecutionId;
	}
	public void setTaskExecutionId(String taskExecutionId) {
		this.taskExecutionId = taskExecutionId;
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
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	@Override
	public String toString() {
		return "BatchTaskExecutionDetailPo [PkId=" + PkId + ", taskExecutionId=" + taskExecutionId + ", jobInstanceId="
				+ jobInstanceId + ", jobExecutionId=" + jobExecutionId + ", jobName=" + jobName + "]";
	}
	
}
