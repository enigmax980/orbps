package com.newcore.orbps.models.pcms.bo;

import java.util.Date;

public class ImBizTask implements java.io.Serializable {

	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 8759286063734869L;
	private Long taskSeq;
	private String status;
	private Integer attachedFlag;
	private Date taskCreateTime;
	private Date alterTime;
	private Date plnRespTime;
	private Date respTime;
	private String askTimes;
	private String relKeyNo;
	private Long relKeyId;
	private String taskFrom;
	private String taskEvent;
	private String taskEventOrder;
	private String relEventNo;
	private Long relEventId;
	private String polCode;
	private String mgBranch;
	private Short serviceDept;
	private String accBranch;
	private Short accDept;
	//mio_date
	private Date accDate;
	private Long oclkClerkId;
	private String oclkBranchNo;
	private String oclkClerkNo;
	private String salesBranchNo;
	private String salesDeptNo;
	private String salesNo;
	private String cntrType;
	private String sysType;
	private Long taskFlowId;
	private String cntrNo;
	private String custNo;
	private String salesChannel;
	private String extKeys;
	private String extKey0;
	private String extKey1;
	private String extKey2;
	private String extKey3;
	private String extKey4;
	private String extKey5;
	private String extKey6;
	private String extKey7;
	private String extKey8;
	private String extKey9;
	private String extKey10;
	private String extKey11;
	private String extKey12;
	private String extKey13;
	private String extKey14;
	private String extKey15;
	private String extKey16;
	private String extKey17;
	private String extKey18;
	private String extKey19;
	private String extKey20;
	private String extKey21;
	private String extKey22;
	private String extKey23;
	private String extKey24;
	private String extKey25;
	private String extKey26;
	private String extKey27;
	private String extKey28;
	private String extKey29;
	private String extKey30;
	private String extKey31;
	private String extKey32;
	private String extKey33;
	private String extKey34;
	private String extKey35;
	private String extKey36;
	private String extKey37;
	private String extKey38;
	private String extKey39;
	private String extKey40;
	
	private String dataSource;

	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public Long getTaskSeq() {
		return taskSeq;
	}
	public void setTaskSeq(Long taskSeq) {
		this.taskSeq = taskSeq;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getAttachedFlag() {
		return attachedFlag;
	}
	public void setAttachedFlag(Integer attachedFlag) {
		this.attachedFlag = attachedFlag;
	}
	public Date getTaskCreateTime() {
		return taskCreateTime;
	}
	public void setTaskCreateTime(Date taskCreateTime) {
		this.taskCreateTime = taskCreateTime;
	}
	public Date getAlterTime() {
		return alterTime;
	}
	public void setAlterTime(Date alterTime) {
		this.alterTime = alterTime;
	}
	public Date getPlnRespTime() {
		return plnRespTime;
	}
	public void setPlnRespTime(Date plnRespTime) {
		this.plnRespTime = plnRespTime;
	}
	public Date getRespTime() {
		return respTime;
	}
	public void setRespTime(Date respTime) {
		this.respTime = respTime;
	}
	public String getAskTimes() {
		return askTimes;
	}
	public void setAskTimes(String askTimes) {
		this.askTimes = askTimes;
	}
	public String getRelKeyNo() {
		return relKeyNo;
	}
	public void setRelKeyNo(String relKeyNo) {
		this.relKeyNo = relKeyNo;
	}
	public Long getRelKeyId() {
		return relKeyId;
	}
	public void setRelKeyId(Long relKeyId) {
		this.relKeyId = relKeyId;
	}
	public String getTaskFrom() {
		return taskFrom;
	}
	public void setTaskFrom(String taskFrom) {
		this.taskFrom = taskFrom;
	}
	public String getTaskEvent() {
		return taskEvent;
	}
	public void setTaskEvent(String taskEvent) {
		this.taskEvent = taskEvent;
	}
	public String getTaskEventOrder() {
		return taskEventOrder;
	}
	public void setTaskEventOrder(String taskEventOrder) {
		this.taskEventOrder = taskEventOrder;
	}
	public String getRelEventNo() {
		return relEventNo;
	}
	public void setRelEventNo(String relEventNo) {
		this.relEventNo = relEventNo;
	}
	public Long getRelEventId() {
		return relEventId;
	}
	public void setRelEventId(Long relEventId) {
		this.relEventId = relEventId;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getMgBranch() {
		return mgBranch;
	}
	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}
	public Short getServiceDept() {
		return serviceDept;
	}
	public void setServiceDept(Short serviceDept) {
		this.serviceDept = serviceDept;
	}
	public String getAccBranch() {
		return accBranch;
	}
	public void setAccBranch(String accBranch) {
		this.accBranch = accBranch;
	}
	public Short getAccDept() {
		return accDept;
	}
	public void setAccDept(Short accDept) {
		this.accDept = accDept;
	}
	public Date getAccDate() {
		return accDate;
	}
	public void setAccDate(Date accDate) {
		this.accDate = accDate;
	}
	public Long getOclkClerkId() {
		return oclkClerkId;
	}
	public void setOclkClerkId(Long oclkClerkId) {
		this.oclkClerkId = oclkClerkId;
	}
	public String getOclkBranchNo() {
		return oclkBranchNo;
	}
	public void setOclkBranchNo(String oclkBranchNo) {
		this.oclkBranchNo = oclkBranchNo;
	}
	public String getOclkClerkNo() {
		return oclkClerkNo;
	}
	public void setOclkClerkNo(String oclkClerkNo) {
		this.oclkClerkNo = oclkClerkNo;
	}
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	public String getSalesDeptNo() {
		return salesDeptNo;
	}
	public void setSalesDeptNo(String salesDeptNo) {
		this.salesDeptNo = salesDeptNo;
	}
	public String getSalesNo() {
		return salesNo;
	}
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}
	public String getCntrType() {
		return cntrType;
	}
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}
	public String getSysType() {
		return sysType;
	}
	public void setSysType(String sysType) {
		this.sysType = sysType;
	}
	public Long getTaskFlowId() {
		return taskFlowId;
	}
	public void setTaskFlowId(Long taskFlowId) {
		this.taskFlowId = taskFlowId;
	}
	public String getCntrNo() {
		return cntrNo;
	}
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getSalesChannel() {
		return salesChannel;
	}
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	public String getExtKeys() {
		return extKeys;
	}
	public void setExtKeys(String extKeys) {
		this.extKeys = extKeys;
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
	public String getExtKey6() {
		return extKey6;
	}
	public void setExtKey6(String extKey6) {
		this.extKey6 = extKey6;
	}
	public String getExtKey7() {
		return extKey7;
	}
	public void setExtKey7(String extKey7) {
		this.extKey7 = extKey7;
	}
	public String getExtKey8() {
		return extKey8;
	}
	public void setExtKey8(String extKey8) {
		this.extKey8 = extKey8;
	}
	public String getExtKey9() {
		return extKey9;
	}
	public void setExtKey9(String extKey9) {
		this.extKey9 = extKey9;
	}
	public String getExtKey10() {
		return extKey10;
	}
	public void setExtKey10(String extKey10) {
		this.extKey10 = extKey10;
	}
	public String getExtKey11() {
		return extKey11;
	}
	public void setExtKey11(String extKey11) {
		this.extKey11 = extKey11;
	}
	public String getExtKey12() {
		return extKey12;
	}
	public void setExtKey12(String extKey12) {
		this.extKey12 = extKey12;
	}
	public String getExtKey13() {
		return extKey13;
	}
	public void setExtKey13(String extKey13) {
		this.extKey13 = extKey13;
	}
	public String getExtKey14() {
		return extKey14;
	}
	public void setExtKey14(String extKey14) {
		this.extKey14 = extKey14;
	}
	public String getExtKey15() {
		return extKey15;
	}
	public void setExtKey15(String extKey15) {
		this.extKey15 = extKey15;
	}
	public String getExtKey16() {
		return extKey16;
	}
	public void setExtKey16(String extKey16) {
		this.extKey16 = extKey16;
	}
	public String getExtKey17() {
		return extKey17;
	}
	public void setExtKey17(String extKey17) {
		this.extKey17 = extKey17;
	}
	public String getExtKey18() {
		return extKey18;
	}
	public void setExtKey18(String extKey18) {
		this.extKey18 = extKey18;
	}
	public String getExtKey19() {
		return extKey19;
	}
	public void setExtKey19(String extKey19) {
		this.extKey19 = extKey19;
	}
	public String getExtKey20() {
		return extKey20;
	}
	public void setExtKey20(String extKey20) {
		this.extKey20 = extKey20;
	}
	public String getExtKey21() {
		return extKey21;
	}
	public void setExtKey21(String extKey21) {
		this.extKey21 = extKey21;
	}
	public String getExtKey22() {
		return extKey22;
	}
	public void setExtKey22(String extKey22) {
		this.extKey22 = extKey22;
	}
	public String getExtKey23() {
		return extKey23;
	}
	public void setExtKey23(String extKey23) {
		this.extKey23 = extKey23;
	}
	public String getExtKey24() {
		return extKey24;
	}
	public void setExtKey24(String extKey24) {
		this.extKey24 = extKey24;
	}
	public String getExtKey25() {
		return extKey25;
	}
	public void setExtKey25(String extKey25) {
		this.extKey25 = extKey25;
	}
	public String getExtKey26() {
		return extKey26;
	}
	public void setExtKey26(String extKey26) {
		this.extKey26 = extKey26;
	}
	public String getExtKey27() {
		return extKey27;
	}
	public void setExtKey27(String extKey27) {
		this.extKey27 = extKey27;
	}
	public String getExtKey28() {
		return extKey28;
	}
	public void setExtKey28(String extKey28) {
		this.extKey28 = extKey28;
	}
	public String getExtKey29() {
		return extKey29;
	}
	public void setExtKey29(String extKey29) {
		this.extKey29 = extKey29;
	}
	public String getExtKey30() {
		return extKey30;
	}
	public void setExtKey30(String extKey30) {
		this.extKey30 = extKey30;
	}
	public String getExtKey31() {
		return extKey31;
	}
	public void setExtKey31(String extKey31) {
		this.extKey31 = extKey31;
	}
	public String getExtKey32() {
		return extKey32;
	}
	public void setExtKey32(String extKey32) {
		this.extKey32 = extKey32;
	}
	public String getExtKey33() {
		return extKey33;
	}
	public void setExtKey33(String extKey33) {
		this.extKey33 = extKey33;
	}
	public String getExtKey34() {
		return extKey34;
	}
	public void setExtKey34(String extKey34) {
		this.extKey34 = extKey34;
	}
	public String getExtKey35() {
		return extKey35;
	}
	public void setExtKey35(String extKey35) {
		this.extKey35 = extKey35;
	}
	public String getExtKey36() {
		return extKey36;
	}
	public void setExtKey36(String extKey36) {
		this.extKey36 = extKey36;
	}
	public String getExtKey37() {
		return extKey37;
	}
	public void setExtKey37(String extKey37) {
		this.extKey37 = extKey37;
	}
	public String getExtKey38() {
		return extKey38;
	}
	public void setExtKey38(String extKey38) {
		this.extKey38 = extKey38;
	}
	public String getExtKey39() {
		return extKey39;
	}
	public void setExtKey39(String extKey39) {
		this.extKey39 = extKey39;
	}
	public String getExtKey40() {
		return extKey40;
	}
	public void setExtKey40(String extKey40) {
		this.extKey40 = extKey40;
	}


}