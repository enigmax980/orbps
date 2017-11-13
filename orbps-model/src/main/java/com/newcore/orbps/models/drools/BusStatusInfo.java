package com.newcore.orbps.models.drools;

import java.io.Serializable;

/**
 * 
 * @Description 
 * @author zhoushoubo
 * @date 2016年10月28日下午2:25:45
 */
public class BusStatusInfo implements Serializable{

	private static final long serialVersionUID = -7572543547867535250L;
	
	private String curBusStatus;
	private String nextBusStatus;
	private String chkResult = "0";
	private String message;
	
	public String getCurBusStatus() {
		return curBusStatus;
	}
	public void setCurBusStatus(String curBusStatus) {
		this.curBusStatus = curBusStatus;
	}
	public String getNextBusStatus() {
		return nextBusStatus;
	}
	public void setNextBusStatus(String nextBusStatus) {
		this.nextBusStatus = nextBusStatus;
	}
	public String getChkResult() {
		return chkResult;
	}
	public void setChkResult(String chkResult) {
		this.chkResult = chkResult;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
