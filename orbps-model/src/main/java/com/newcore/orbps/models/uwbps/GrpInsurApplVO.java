package com.newcore.orbps.models.uwbps;

import java.io.Serializable;
import java.util.Date;

/**
 * 团体出单基本信息
 * @author huanghaiyang
 * 创建时间：2016年7月29日
 */
public class GrpInsurApplVO implements Serializable{
	private static final long serialVersionUID = 7208753141791917280L;
	/**
	 * 
	 */

	//投保单号
	private String applNo;

	//清单标志
	private String listFlag;

	//保单性质
	private String applFlag;

	//团单方案审批号
	private String approNo;

	//契约形式
	private String cntrForm;

	//汇交人类型
	private String listType;

	//投保日期
	private Date applDate;

	//投保单来源
	private String insurSour;

	//赠送保险标志
	private String giftFlag;

	//是否异常告知
	private String  noticeStat;

	//投保人数
	private long ipsnNum;

	//总保额
	private Double sumFaceAmnt;

	//总保费
	private Double sumPremiu;

	//个人账户缴费金额
	private Double ipsnFundPremium;

	//计入个人账户金额
	private Double ipsnFundAmt;

	//指定生效日期
	private Date inForceDate;

	//管理机构号
	private String mgrBranchNo;

	//工程名称
	private String iobjName;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getListFlag() {
		return listFlag;
	}

	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}

	public String getApplFlag() {
		return applFlag;
	}

	public void setApplFlag(String applFlag) {
		this.applFlag = applFlag;
	}

	public String getApproNo() {
		return approNo;
	}

	public void setApproNo(String approNo) {
		this.approNo = approNo;
	}

	public String getCntrForm() {
		return cntrForm;
	}

	public void setCntrForm(String cntrForm) {
		this.cntrForm = cntrForm;
	}

	public String getListType() {
		return listType;
	}

	public void setListType(String listType) {
		this.listType = listType;
	}

	public Date getApplDate() {
		return applDate;
	}

	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}

	public String getInsurSour() {
		return insurSour;
	}

	public void setInsurSour(String insurSour) {
		this.insurSour = insurSour;
	}

	public String getGiftFlag() {
		return giftFlag;
	}

	public void setGiftFlag(String giftFlag) {
		this.giftFlag = giftFlag;
	}

	public String getNoticeStat() {
		return noticeStat;
	}

	public void setNoticeStat(String noticeStat) {
		this.noticeStat = noticeStat;
	}

	public long getIpsnNum() {
		return ipsnNum;
	}

	public void setIpsnNum(long ipsnNum) {
		this.ipsnNum = ipsnNum;
	}

	public Double getSumFaceAmnt() {
		return sumFaceAmnt;
	}

	public void setSumFaceAmnt(Double sumFaceAmnt) {
		this.sumFaceAmnt = sumFaceAmnt;
	}

	public Double getSumPremiu() {
		return sumPremiu;
	}

	public void setSumPremiu(Double sumPremiu) {
		this.sumPremiu = sumPremiu;
	}

	public Double getIpsnFundPremium() {
		return ipsnFundPremium;
	}

	public void setIpsnFundPremium(Double ipsnFundPremium) {
		this.ipsnFundPremium = ipsnFundPremium;
	}

	public Double getIpsnFundAmt() {
		return ipsnFundAmt;
	}

	public void setIpsnFundAmt(Double ipsnFundAmt) {
		this.ipsnFundAmt = ipsnFundAmt;
	}

	public Date getInForceDate() {
		return inForceDate;
	}

	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getIobjName() {
		return iobjName;
	}

	public void setIobjName(String iobjName) {
		this.iobjName = iobjName;
	}
}
