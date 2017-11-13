package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;

public class ImageAuditSubmitVo  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -109330896660284369L;
	//投保单号
	private String applNo;
	//任务ID
	private String taskId;
	//审批不通过原因
	private String notice;
	//审批flag
	private String procFlag;
	
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	@Override
	public String toString() {
		return "ImageAuditSubmitVo [applNo=" + applNo + ", taskId=" + taskId + ", notice=" + notice + ", procFlag="
				+ procFlag + "]";
	}
	
	
}
