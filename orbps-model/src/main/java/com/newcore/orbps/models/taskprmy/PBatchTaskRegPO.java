package com.newcore.orbps.models.taskprmy;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
/**
 * 批作业任务注册表PO
 * @author xushichao
 * 2017年2月6日
 */
public class PBatchTaskRegPO implements Serializable {

	private static final long serialVersionUID = -8652637111082930918L;
	
	private String taskId;
	private String batTxNo;
	private String dataDealStatus;
	private Date createTime;
	private Timestamp updateTimestamp;
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getBatTxNo() {
		return batTxNo;
	}
	public void setBatTxNo(String batTxNo) {
		this.batTxNo = batTxNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Timestamp getUpdateTimestamp() {
		return updateTimestamp;
	}

	public void setUpdateTimestamp(Timestamp updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}

	public String getDataDealStatus() {
		return dataDealStatus;
	}
	public void setDataDealStatus(String dataDealStatus) {
		this.dataDealStatus = dataDealStatus;
	}
	@Override
	public String toString() {
		return "PBatchTaskRegPo [taskId=" + taskId + ", batTxNo=" + batTxNo + ", dataDealStatus=" + dataDealStatus
				+ ", createTime=" + createTime + ", lastUpdatedTimestamp=" + updateTimestamp + "]";
	}
}
