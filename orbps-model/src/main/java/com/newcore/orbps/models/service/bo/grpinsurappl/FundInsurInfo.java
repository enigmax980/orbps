package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

/**
 * 基金险
 * @author wangxiao 
 * 创建时间：2016年7月21日上午10:25:33
 */
public class FundInsurInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1857618579660393077L;

	/* 字段名：管理费计提方式，长度：2，是否必填：基金险必填，团单特有 */
	private String adminFeeCopuType;

	/* 字段名：管理费比例，是否必填：基金险必填，团单特有 */
	private Double adminFeePct;

	/* 字段名：个人账户缴费金额，是否必填：基金险必填，团单特有 */
	private Double ipsnFundPremium;

	/* 字段名：计入个人账户金额，是否必填：基金险必填，团单特有 */
	private Double ipsnFundAmnt;

	/* 字段名：公共账户缴费金额，是否必填：基金险必填，团单特有 */
	private Double sumFundPremium;

	/* 字段名：计入公共账户金额，是否必填：基金险必填，团单特有 */
	private Double sumFundAmnt;

	/* 字段名：基金险收到保费时间，是否必填：基金险必填，团单特有 */
	private Date preMioDate;
	
	/* 字段名：账户余额，是否必填：基金险必填，团单特有 */
	private Double accBalance;

	/* 字段名：账户管理费金额，是否必填：基金险必填，团单特有 */
	private Double accAdminBalance;

	/* 字段名：首期管理费总金额，是否必填：基金险必填，团单特有 */
	private Double accSumAdminBalance;

	/**
	 * 
	 */
	public FundInsurInfo() {
		super();
	}

	/**
	 * @return the adminFeeCopuType
	 */
	public String getAdminFeeCopuType() {
		return adminFeeCopuType;
	}

	/**
	 * @param adminFeeCopuType
	 *            the adminFeeCopuType to set
	 */
	public void setAdminFeeCopuType(String adminFeeCopuType) {
		this.adminFeeCopuType = adminFeeCopuType;
	}


	public Double getAdminFeePct() {
		return adminFeePct;
	}

	public void setAdminFeePct(Double adminFeePct) {
		this.adminFeePct = adminFeePct;
	}

	/**
	 * @return the ipsnFundPremium
	 */
	public Double getIpsnFundPremium() {
		return ipsnFundPremium;
	}

	/**
	 * @param ipsnFundPremium
	 *            the ipsnFundPremium to set
	 */
	public void setIpsnFundPremium(Double ipsnFundPremium) {
		this.ipsnFundPremium = ipsnFundPremium;
	}

	/**
	 * @return the ipsnFundAmnt
	 */
	public Double getIpsnFundAmnt() {
		return ipsnFundAmnt;
	}

	/**
	 * @param ipsnFundAmnt
	 *            the ipsnFundAmnt to set
	 */
	public void setIpsnFundAmnt(Double ipsnFundAmnt) {
		this.ipsnFundAmnt = ipsnFundAmnt;
	}

	/**
	 * @return the sumFundPremium
	 */
	public Double getSumFundPremium() {
		return sumFundPremium;
	}

	/**
	 * @param sumFundPremium
	 *            the sumFundPremium to set
	 */
	public void setSumFundPremium(Double sumFundPremium) {
		this.sumFundPremium = sumFundPremium;
	}

	/**
	 * @return the sumFundAmnt
	 */
	public Double getSumFundAmnt() {
		return sumFundAmnt;
	}

	/**
	 * @param sumFundAmnt
	 *            the sumFundAmnt to set
	 */
	public void setSumFundAmnt(Double sumFundAmnt) {
		this.sumFundAmnt = sumFundAmnt;
	}

	/**
	 * @return the preMioDate
	 */
	public Date getPreMioDate() {
		return preMioDate;
	}

	/**
	 * @param preMioDate the preMioDate to set
	 */
	public void setPreMioDate(Date preMioDate) {
		this.preMioDate = preMioDate;
	}

	/**
	 * @return the accBalance
	 */
	public Double getAccBalance() {
		return accBalance;
	}

	/**
	 * @param accBalance the accBalance to set
	 */
	public void setAccBalance(Double accBalance) {
		this.accBalance = accBalance;
	}

	/**
	 * @return the accAdminBalance
	 */
	public Double getAccAdminBalance() {
		return accAdminBalance;
	}

	/**
	 * @param accAdminBalance the accAdminBalance to set
	 */
	public void setAccAdminBalance(Double accAdminBalance) {
		this.accAdminBalance = accAdminBalance;
	}

	/**
	 * @return the accSumAdminBalance
	 */
	public Double getAccSumAdminBalance() {
		return accSumAdminBalance;
	}

	/**
	 * @param accSumAdminBalance the accSumAdminBalance to set
	 */
	public void setAccSumAdminBalance(Double accSumAdminBalance) {
		this.accSumAdminBalance = accSumAdminBalance;
	}

}
