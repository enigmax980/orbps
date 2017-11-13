package com.newcore.orbps.models.banktrans;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "STOP_TASK_IN")
public class StopTaskIn implements Serializable{
	@XmlElement(name = "TASK_ID")
	private String taskId;
	@XmlElement(name = "START_EXEC_TIME")
	private String startExecTime;
	@XmlElement(name = "RESULT")
	private String result;
	
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getStartExecTime() {
		return startExecTime;
	}
	public void setStartExecTime(String startExecTime) {
		this.startExecTime = startExecTime;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
