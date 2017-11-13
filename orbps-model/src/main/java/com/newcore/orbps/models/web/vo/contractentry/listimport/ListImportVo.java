package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;
import java.util.Date;

/**
 * 清单导入信息Vo
 * @author jincong
 *
 */
public class ListImportVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080001L;
	
	/** 投保单号 */
	private String applNo;
	/** 投保人客户号 */
	private String custNo;
	/** 投保人客姓名 */
	private String custName;
	/** 任务类型 */
	private String taskType;
	/** 任务状态 */
	private String taskState;
	/** 销售机构 */
	private String salesBranchNo;
	/** 是否包含下级机构 */
	private String findSubBranchNoFlag;
	/** 销售员代码 */
	private String salesCode;
	/** 操作机构 */
	private String operateBranch;
	/** 操作员工号 */
	private String operatorNo;
	/** 操作员姓名 */
    private String operatorName;
	/** 任务开始时间，使用String类型接收前台数据，传到后台需要做类型转换 */
	private String taskStartTime;
	/** 任务结束时间，使用String类型接收前台数据，传到后台需要做类型转换 */
	private String taskEndTime;
	/** 投保人数 */
	private Long insuredNum;
	/** 导入人数 */
	private Long importNum;
	/** 导入进度 */
	private Double importSp;
	/** 差错数量 */
	private Long errorNum;
	/** 本次导入总人数 */
	private Long thisIpsnNum;
	/** 本次导入正确条数 */
	private Long thisImportNum;
	/** 本次导入错误条数 */
	private Long thisErrorNum;
	/** 日志 */
	private Date listLog;
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
	 * @return the custNo
	 */
	public String getCustNo() {
		return custNo;
	}
	/**
	 * @param custNo the custNo to set
	 */
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	/**
	 * @return the custName
	 */
	public String getCustName() {
		return custName;
	}
	/**
	 * @param custName the custName to set
	 */
	public void setCustName(String custName) {
		this.custName = custName;
	}
	/**
	 * @return the taskType
	 */
	public String getTaskType() {
		return taskType;
	}
	/**
	 * @param taskType the taskType to set
	 */
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	/**
	 * @return the taskState
	 */
	public String getTaskState() {
		return taskState;
	}
	/**
	 * @param taskState the taskState to set
	 */
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	/**
	 * @return the salesBranchNo
	 */
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	/**
	 * @param salesBranchNo the salesBranchNo to set
	 */
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	/**
	 * @return the findSubBranchNoFlag
	 */
	public String getFindSubBranchNoFlag() {
		return findSubBranchNoFlag;
	}
	/**
	 * @param findSubBranchNoFlag the findSubBranchNoFlag to set
	 */
	public void setFindSubBranchNoFlag(String findSubBranchNoFlag) {
		this.findSubBranchNoFlag = findSubBranchNoFlag;
	}
	/**
	 * @return the salesCode
	 */
	public String getSalesCode() {
		return salesCode;
	}
	/**
	 * @param salesCode the salesCode to set
	 */
	public void setSalesCode(String salesCode) {
		this.salesCode = salesCode;
	}
	/**
	 * @return the operateBranch
	 */
	public String getOperateBranch() {
		return operateBranch;
	}
	/**
	 * @param operateBranch the operateBranch to set
	 */
	public void setOperateBranch(String operateBranch) {
		this.operateBranch = operateBranch;
	}
	/**
	 * @return the operatorNo
	 */
	public String getOperatorNo() {
		return operatorNo;
	}
	/**
	 * @param operatorNo the operatorNo to set
	 */
	public void setOperatorNo(String operatorNo) {
		this.operatorNo = operatorNo;
	}
	/**
	 * @return the operatorName
	 */
	public String getOperatorName() {
		return operatorName;
	}
	/**
	 * @param operatorName the operatorName to set
	 */
	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}
	/**
	 * @return the taskStartTime
	 */
	public String getTaskStartTime() {
		return taskStartTime;
	}
	/**
	 * @param taskStartTime the taskStartTime to set
	 */
	public void setTaskStartTime(String taskStartTime) {
		this.taskStartTime = taskStartTime;
	}
	/**
	 * @return the taskEndTime
	 */
	public String getTaskEndTime() {
		return taskEndTime;
	}
	/**
	 * @param taskEndTime the taskEndTime to set
	 */
	public void setTaskEndTime(String taskEndTime) {
		this.taskEndTime = taskEndTime;
	}
	/**
	 * @return the insuredNum
	 */
	public Long getInsuredNum() {
		return insuredNum;
	}
	/**
	 * @param insuredNum the insuredNum to set
	 */
	public void setInsuredNum(Long insuredNum) {
		this.insuredNum = insuredNum;
	}
	/**
	 * @return the importNum
	 */
	public Long getImportNum() {
		return importNum;
	}
	/**
	 * @param importNum the importNum to set
	 */
	public void setImportNum(Long importNum) {
		this.importNum = importNum;
	}
	/**
	 * @return the importSp
	 */
	public Double getImportSp() {
		return importSp;
	}
	/**
	 * @param importSp the importSp to set
	 */
	public void setImportSp(Double importSp) {
		this.importSp = importSp;
	}
	/**
	 * @return the errorNum
	 */
	public Long getErrorNum() {
		return errorNum;
	}
	/**
	 * @param errorNum the errorNum to set
	 */
	public void setErrorNum(Long errorNum) {
		this.errorNum = errorNum;
	}
	/**
	 * @return the thisIpsnNum
	 */
	public Long getThisIpsnNum() {
		return thisIpsnNum;
	}
	/**
	 * @param thisIpsnNum the thisIpsnNum to set
	 */
	public void setThisIpsnNum(Long thisIpsnNum) {
		this.thisIpsnNum = thisIpsnNum;
	}
	/**
	 * @return the thisImportNum
	 */
	public Long getThisImportNum() {
		return thisImportNum;
	}
	/**
	 * @param thisImportNum the thisImportNum to set
	 */
	public void setThisImportNum(Long thisImportNum) {
		this.thisImportNum = thisImportNum;
	}
	/**
	 * @return the thisErrorNum
	 */
	public Long getThisErrorNum() {
		return thisErrorNum;
	}
	/**
	 * @param thisErrorNum the thisErrorNum to set
	 */
	public void setThisErrorNum(Long thisErrorNum) {
		this.thisErrorNum = thisErrorNum;
	}
	/**
	 * @return the listLog
	 */
	public Date getListLog() {
		return listLog;
	}
	/**
	 * @param listLog the listLog to set
	 */
	public void setListLog(Date listLog) {
		this.listLog = listLog;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListImportVo [applNo=" + applNo + ", custNo=" + custNo + ", custName=" + custName + ", taskType="
				+ taskType + ", taskState=" + taskState + ", salesBranchNo=" + salesBranchNo + ", findSubBranchNoFlag="
				+ findSubBranchNoFlag + ", salesCode=" + salesCode + ", operateBranch=" + operateBranch
				+ ", operatorNo=" + operatorNo + ", operatorName=" + operatorName + ", taskStartTime=" + taskStartTime
				+ ", taskEndTime=" + taskEndTime + ", insuredNum=" + insuredNum + ", importNum=" + importNum
				+ ", importSp=" + importSp + ", errorNum=" + errorNum + ", thisIpsnNum=" + thisIpsnNum
				+ ", thisImportNum=" + thisImportNum + ", thisErrorNum=" + thisErrorNum + ", listLog=" + listLog + "]";
	}
	
}
