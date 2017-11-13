package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author huanglong
 * @date 2016年11月17日
 * @content: 参与共保信息类
 */
public class PeriodComRec implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9041951642276185609L;

	/*字段名称:参与共保信息创建时间,长度:,是否必填:否*/
	private Date createDate;
	/*字段名称:交接批次总人数,长度:,是否必填:否*/
	private long handoverNum;
	/*字段名称:交接起始日期,长度:,是否必填:否*/
	private Date handoverStartDate;
	/*字段名称:交接截止日期,长度:,是否必填:否*/
	private Date handoverEndDate;
	/*字段名称:交接金额,长度:,是否必填:否*/
	private double amnt;
	/*字段名称:销售渠道,长度:,是否必填:否*/
	private String salesChannel;
	/*字段名称:销售机构,长度:,是否必填:否*/
	private String salesBranchNo;
	/*字段名称:销售工号,长度:,是否必填:否*/
	private String salesNo;
	/*字段名称:参与共保状态,长度:,是否必填:否*/
	private String joinCoinsurStat;
	/*字段名称:交接件数 ,长度:,是否必填:否*/
	private long CntrCount;
	/*字段名称:操作员机构,长度:6,是否必填:Y*/
	private String pclkBranchNo;
	/*字段名称:操作员工号,长度:8,是否必填:Y*/
	private String clerkNo;	
	/*字段名称:参与承保保单信息组,长度:,是否必填:否*/
	private List<PeriodComInsur> periodComInsurs;
	public PeriodComRec() {
		super();
	}
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the handoverNum
	 */
	public long getHandoverNum() {
		return handoverNum;
	}
	/**
	 * @param handoverNum the handoverNum to set
	 */
	public void setHandoverNum(long handoverNum) {
		this.handoverNum = handoverNum;
	}

	/**
	 * @return the handoverStartDate
	 */
	public Date getHandoverStartDate() {
		return handoverStartDate;
	}

	/**
	 * @param handoverStartDate the handoverStartDate to set
	 */
	public void setHandoverStartDate(Date handoverStartDate) {
		this.handoverStartDate = handoverStartDate;
	}

	/**
	 * @return the handoverEndDate
	 */
	public Date getHandoverEndDate() {
		return handoverEndDate;
	}

	/**
	 * @param handoverEndDate the handoverEndDate to set
	 */
	public void setHandoverEndDate(Date handoverEndDate) {
		this.handoverEndDate = handoverEndDate;
	}

	/**
	 * @return the amnt
	 */
	public double getAmnt() {
		return amnt;
	}

	/**
	 * @param amnt the amnt to set
	 */
	public void setAmnt(double amnt) {
		this.amnt = amnt;
	}

	/**
	 * @return the salesChannel
	 */
	public String getSalesChannel() {
		return salesChannel;
	}

	/**
	 * @param salesChannel the salesChannel to set
	 */
	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
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
	 * @return the salesNo
	 */
	public String getSalesNo() {
		return salesNo;
	}

	/**
	 * @param salesNo the salesNo to set
	 */
	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	/**
	 * @return the joinCoinsurStat
	 */
	public String getJoinCoinsurStat() {
		return joinCoinsurStat;
	}

	/**
	 * @param joinCoinsurStat the joinCoinsurStat to set
	 */
	public void setJoinCoinsurStat(String joinCoinsurStat) {
		this.joinCoinsurStat = joinCoinsurStat;
	}

	/**
	 * @return the cntrCount
	 */
	public long getCntrCount() {
		return CntrCount;
	}

	/**
	 * @param cntrCount the cntrCount to set
	 */
	public void setCntrCount(long cntrCount) {
		CntrCount = cntrCount;
	}

	/**
	 * @return the pclkBranchNo
	 */
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}

	/**
	 * @param pclkBranchNo the pclkBranchNo to set
	 */
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}

	/**
	 * @return the clerkNo
	 */
	public String getClerkNo() {
		return clerkNo;
	}

	/**
	 * @param clerkNo the clerkNo to set
	 */
	public void setClerkNo(String clerkNo) {
		this.clerkNo = clerkNo;
	}
	/**
	 * @return the periodComInsurs
	 */
	public List<PeriodComInsur> getPeriodComInsurs() {
		return periodComInsurs;
	}
	/**
	 * @param periodComInsurs the periodComInsurs to set
	 */
	public void setPeriodComInsurs(List<PeriodComInsur> periodComInsurs) {
		this.periodComInsurs = periodComInsurs;
	}
}
