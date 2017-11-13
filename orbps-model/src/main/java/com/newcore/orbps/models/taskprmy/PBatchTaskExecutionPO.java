package com.newcore.orbps.models.taskprmy;

import java.io.Serializable;
import java.util.Date;
/**
 * 批作业任务执行表
 * @author xushichao
 * 2017/2/6
 */
public class PBatchTaskExecutionPO implements Serializable {

	private static final long serialVersionUID = -1329166193631809742L;
	
	private String taskExecutionId;
	private String taskId;
	private Date startTime;
	private Date endTime;
	public String getTaskExecutionId() {
		return taskExecutionId;
	}
	public void setTaskExecutionId(String taskExecutionId) {
		this.taskExecutionId = taskExecutionId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	@Override
	public String toString() {
		return "BatchTaskExecutionPo [taskExecutionId=" + taskExecutionId + ", taskId=" + taskId + ", startTime="
				+ startTime + ", endTime=" + endTime + "]";
	}
}
