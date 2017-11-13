package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/**
 *暂缴费支取-全部支取产生应付控制表
 * @author LJF 
 * 2017年3月6日 10:42:47
 */
public class ProcEarnestPayTask implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4826013866093676240L;

	//任务id  必填
	private long taskSeq; 		

	//任务状态  必填
	private String status;

	//销售机构号
	private String salesBranchNo;

	//任务创建时间   必填
	private Date createTime;

	//开始执行时间
	private Date startTime;

	//任务完成时间
	private Date endTime;

	//执行次数
	private Integer	askTimes;

	//任务ID
	private String taskId;

	//业务主键
	private String businessKey;

	//投保单号
	private String applNo;

	//清单路径
	private String listPath;

	//批作业实例ID
	private long jobInstanceId;

	//备注
	private String remark;

	//扩展键0
	private String extKey0;

	//扩展键1
	private String extKey1;

	//扩展键2
	private String extKey2;

	//扩展键3
	private String extKey3;

	//扩展键4
	private String extKey4;

	//扩展键5
	private String extKey5;



	public long getTaskSeq() {
		return taskSeq;
	}



	public void setTaskSeq(long taskSeq) {
		this.taskSeq = taskSeq;
	}



	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public String getSalesBranchNo() {
		return salesBranchNo;
	}



	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}



	public Date getCreateTime() {
		return createTime;
	}



	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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



	public Integer getAskTimes() {
		return askTimes;
	}



	public void setAskTimes(Integer askTimes) {
		this.askTimes = askTimes;
	}



	public String getTaskId() {
		return taskId;
	}



	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}



	public String getBusinessKey() {
		return businessKey;
	}



	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}



	public String getApplNo() {
		return applNo;
	}



	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}



	public String getListPath() {
		return listPath;
	}



	public void setListPath(String listPath) {
		this.listPath = listPath;
	}



	public long getJobInstanceId() {
		return jobInstanceId;
	}



	public void setJobInstanceId(long jobInstanceId) {
		this.jobInstanceId = jobInstanceId;
	}



	public String getRemark() {
		return remark;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getExtKey0() {
		return extKey0;
	}



	public void setExtKey0(String extKey0) {
		this.extKey0 = extKey0;
	}



	public String getExtKey1() {
		return extKey1;
	}



	public void setExtKey1(String extKey1) {
		this.extKey1 = extKey1;
	}



	public String getExtKey2() {
		return extKey2;
	}



	public void setExtKey2(String extKey2) {
		this.extKey2 = extKey2;
	}



	public String getExtKey3() {
		return extKey3;
	}



	public void setExtKey3(String extKey3) {
		this.extKey3 = extKey3;
	}



	public String getExtKey4() {
		return extKey4;
	}



	public void setExtKey4(String extKey4) {
		this.extKey4 = extKey4;
	}



	public String getExtKey5() {
		return extKey5;
	}



	public void setExtKey5(String extKey5) {
		this.extKey5 = extKey5;
	}



	public ProcEarnestPayTask() {
		super();
		// TODO Auto-generated constructor stub
	}



}
