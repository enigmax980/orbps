package com.newcore.orbps.models.web.vo.contractentry.participateininsurance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



/**
 * 参与共保基本信息    
 * @author wangyanjie
 *
 */
public class ParticipateInInsuranceVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000010L;
	
	/** 协议号 */
	private String agreementNo;
	/**  协议名称  */
	private String agreementName;
	/** 交接批次总人数*/
	private Integer num;
	/** 交接开始期间  */
	private Date startPeriod;
	/** 交接终止期间  */
	private Date endPeriod;
	/** 交接金额 */
	private Double transferAmnt;
	/** 交接件数 */
	private Integer insurNum;
	/** 销售渠道 */
	private String salesChannel;
	/** 销售机构 */
	private String salesBranchNo;
	/** 销售员代码 */
	private String salesCode;
	/** 销售员姓名 */
	private String saleName;
	
	/** 新增保单信息 */
	private List<ParticipateInfoVo> participateInfoVos;
	/**
	 * @return the agreementNo
	 */
	public String getAgreementNo() {
		return agreementNo;
	}
	/**
	 * @param agreementNo the agreementNo to set
	 */
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	/**
	 * @return the agreementName
	 */
	public String getAgreementName() {
		return agreementName;
	}
	/**
	 * @param agreementName the agreementName to set
	 */
	public void setAgreementName(String agreementName) {
		this.agreementName = agreementName;
	}
	/**
	 * @return the num
	 */
	public Integer getNum() {
		return num;
	}
	/**
	 * @param num the num to set
	 */
	public void setNum(Integer num) {
		this.num = num;
	}
	/**
	 * @return the startPeriod
	 */
	public Date getStartPeriod() {
		return startPeriod;
	}
	/**
	 * @param startPeriod the startPeriod to set
	 */
	public void setStartPeriod(Date startPeriod) {
		this.startPeriod = startPeriod;
	}
	/**
	 * @return the endPeriod
	 */
	public Date getEndPeriod() {
		return endPeriod;
	}
	/**
	 * @param endPeriod the endPeriod to set
	 */
	public void setEndPeriod(Date endPeriod) {
		this.endPeriod = endPeriod;
	}
	/**
	 * @return the transferAmnt
	 */
	public Double getTransferAmnt() {
		return transferAmnt;
	}
	/**
	 * @param transferAmnt the transferAmnt to set
	 */
	public void setTransferAmnt(Double transferAmnt) {
		this.transferAmnt = transferAmnt;
	}
	/**
	 * @return the insurNum
	 */
	public Integer getInsurNum() {
		return insurNum;
	}
	/**
	 * @param insurNum the insurNum to set
	 */
	public void setInsurNum(Integer insurNum) {
		this.insurNum = insurNum;
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
	public String getSaleName() {
		return saleName;
	}
	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}
	/**
	 * @return the participateInfoVos
	 */
	public List<ParticipateInfoVo> getParticipateInfoVos() {
		return participateInfoVos;
	}
	/**
	 * @param participateInfoVos the participateInfoVos to set
	 */
	public void setParticipateInfoVos(List<ParticipateInfoVo> participateInfoVos) {
		this.participateInfoVos = participateInfoVos;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParticipateInInsuranceVo [agreementNo=" + agreementNo + ", agreementName=" + agreementName + ", num="
				+ num + ", startPeriod=" + startPeriod + ", endPeriod=" + endPeriod + ", transferAmnt=" + transferAmnt
				+ ", insurNum=" + insurNum + ", salesChannel=" + salesChannel + ", salesBranchNo=" + salesBranchNo
				+ ", salesCode=" + salesCode + ", saleName=" + saleName + ", participateInfoVos=" + participateInfoVos
				+ "]";
	}
	
}
