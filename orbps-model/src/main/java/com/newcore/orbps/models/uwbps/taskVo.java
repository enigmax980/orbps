package com.newcore.orbps.models.uwbps;

import java.io.Serializable;

public class taskVo implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2098720595759899651L;

	private String businessKey;
	
	private String taskDefKey;
	
	private String taskId;
	
	private String taskSuspend;

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getTaskDefKey() {
		return taskDefKey;
	}

	public void setTaskDefKey(String taskDefKey) {
		this.taskDefKey = taskDefKey;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskSuspend() {
		return taskSuspend;
	}

	public void setTaskSuspend(String taskSuspend) {
		this.taskSuspend = taskSuspend;
	}
	
}
