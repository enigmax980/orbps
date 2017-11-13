package com.newcore.orbps.models.web.vo.otherfunction.contractquery;

import java.io.Serializable;

/***
 * 影像轨迹返回实体类
 * @author 王鸿林
 *
 */
public class ImageAuditTrailVo  implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6919906332266726492L;
	//投保单号
	private String applNo;
	//影像审批状态
	private String approvalState;
	//影像审批结果
	private String procFlag;
	//审批操作员
	private String operator;
	//审批日期
	private String optTime;
	//保单生效状态
	private String isInforce;
	//审批原因
	private String notice;
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public String getApprovalState() {
		return approvalState;
	}
	public void setApprovalState(String approvalState) {
		this.approvalState = approvalState;
	}
	public String getProcFlag() {
		return procFlag;
	}
	public void setProcFlag(String procFlag) {
		this.procFlag = procFlag;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getOptTime() {
		return optTime;
	}
	public void setOptTime(String optTime) {
		this.optTime = optTime;
	}
	public String getIsInforce() {
		return isInforce;
	}
	public void setIsInforce(String isInforce) {
		this.isInforce = isInforce;
	}
	public String getNotice() {
		return notice;
	}
	public void setNotice(String notice) {
		this.notice = notice;
	}
	@Override
	public String toString() {
		return "ImageAuditTrailVo [applNo=" + applNo + ", approvalState=" + approvalState + ", procFlag=" + procFlag
				+ ", operator=" + operator + ", optTime=" + optTime + ", isInforce=" + isInforce + ", notice=" + notice
				+ "]";
	}
	
	
	
	
}
