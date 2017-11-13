package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class PeriodComRec implements Serializable {

	private static final long serialVersionUID = -1835271528512994789L;
	
	/*
	 * 交接批次总人数
	 */
	@NotNull(message="[交接批次总人数不能为空]")
	//@Length(max=1,message="[是否扫描长度不能大于1位]")
	private Long handoverNum;
	private Date handoverStartDate;
	private Date handoverEndDate;
	/*
	 * 交接金额
	 */
	@NotNull(message="[交接金额不能为空]")
	private Double amnt;
	private String salesChannel;
	private String salesBranchNo;
	private String salesNo;
	private String joinCoinsurStat;
	/*
	 * 交接件数
	 */
	@NotNull(message="[交接件数不能为空]")
	private Long CntrCount;
	private String oclkBranchNo;
	/*
	 * 操作员工号
	 */
	@NotNull(message="[操作员工号不能为空]")
	@Length(max=6,message="[操作员工号长度不能大于6位]")
	private String clerkNo;
	public Long getHandoverNum() {
		return handoverNum;
	}
	public void setHandoverNum(Long handoverNum) {
		this.handoverNum = handoverNum;
	}
	public Date getHandoverStartDate() {
		return handoverStartDate;
	}
	public void setHandoverStartDate(Date handoverStartDate) {
		this.handoverStartDate = handoverStartDate;
	}
	public Date getHandoverEndDate() {
		return handoverEndDate;
	}
	public void setHandoverEndDate(Date handoverEndDate) {
		this.handoverEndDate = handoverEndDate;
	}
	public Double getAmnt() {
		return amnt;
	}
	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}
	public String getSalesChannel() {
		return salesChannel;
	}
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}
	public String getSalesBranchNo() {
		return salesBranchNo;
	}
	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}
	public String getSalesNo() {
		return salesNo;
	}
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}
	public String getJoinCoinsurStat() {
		return joinCoinsurStat;
	}
	public void setJoinCoinsurStat(String joinCoinsurStat) {
		this.joinCoinsurStat = joinCoinsurStat;
	}
	public Long getCntrCount() {
		return CntrCount;
	}
	public void setCntrCount(Long cntrCount) {
		CntrCount = cntrCount;
	}
	public String getOclkBranchNo() {
		return oclkBranchNo;
	}
	public void setOclkBranchNo(String oclkBranchNo) {
		this.oclkBranchNo = oclkBranchNo;
	}
	public String getClerkNo() {
		return clerkNo;
	}
	public void setClerkNo(String clerkNo) {
		this.clerkNo = clerkNo;
	}
	@Override
	public String toString() {
		return "PeriodComRec [handoverNum=" + handoverNum + ", handoverStartDate=" + handoverStartDate
				+ ", handoverEndDate=" + handoverEndDate + ", amnt=" + amnt + ", salesChannel=" + salesChannel
				+ ", salesBranchNo=" + salesBranchNo + ", salesNo=" + salesNo + ", joinCoinsurStat=" + joinCoinsurStat
				+ ", CntrCount=" + CntrCount + ", oclkBranchNo=" + oclkBranchNo + ", clerkNo=" + clerkNo + "]";
	}

}
