package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;
 
/**
 * 首期暂收费账户表Model.
 */
@Component
public class EarnestAcc implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -6395630955360998984L;

	/**
	 * 账户标识
	 */
	private Long accId;
	
	/**
	 * 管理机构
	 */
	private String mgrBranchNo;
	
	/**
	 * 账号
	 */
	private String accNo;
	
	/**
	 * 子账号
	 */
	private String accNoSeq;
	
	/**
	 * 账户所有人客户号
	 */
	private String custNo;
	
	/**
	 * 开户日期
	 */
	private Date accOpenDate;
	
	/**
	 * 到期日
	 */
	private Date maturity;
	
	/**
	 * 销户日
	 */
	private Date accCloseDate;
	
	/**
	 * 账户类型
	 */
	private String accTypeCode;
	
	/**
	 * 现金收付方式
	 */
	private String cashType;
	
	/**
	 * 账户状态
	 */
	private String accStat;
	
	/**
	 * 固定利率
	 */
	private String settledIntRate;
	
	/**
	 * 账户变动日期
	 */
	private Date lastUpdDate;
	
	/**
	 * 余额
	 */
	private String balance;
	
	/**
	 * 冻结余额
	 */
	private String frozenBalance;
	
	/**
	 * 未结算利息
	 */
	private String unsettledInt;
	
	/**
	 * 积数
	 */
	private String intCalBase;
	
	/**
	 * 上次结算日期
	 */
	private Date lastSettleDate;
	
	/**
	 * 上次结算余额
	 */
	private String lastSettleBalance;
	
	/**
	 * 计息类型
	 */
	private String intCalType;
	
	/**
	 * 结算周期
	 */
	private String settleCycle;
	
	/**
	 * 结算时点
	 */
	private String settleDay;
	
	/**
	 * 结算类型
	 */
	private String settleType;
	
	/**
	 * 财务本金抽取日期
	 */
	private Date accExtractDate;
	
	/**
	 * 财务利息最近计提日期
	 */
	private Date unsettledDate;
	
	/**
	 * 保单币种
	 */
	private String currencyCode;
	
	/**
	 * 备注
	 */
	private String remark;
	
	private String dataSource;
	
	
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public Long getAccId() {
		return accId;
	}

	public void setAccId(Long accId) {
		this.accId = accId;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getAccNo() {
		return accNo;
	}

	public void setAccNo(String accNo) {
		this.accNo = accNo;
	}

	public String getAccNoSeq() {
		return accNoSeq;
	}

	public void setAccNoSeq(String accNoSeq) {
		this.accNoSeq = accNoSeq;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public Date getAccOpenDate() {
		return accOpenDate;
	}

	public void setAccOpenDate(Date accOpenDate) {
		this.accOpenDate = accOpenDate;
	}

	public Date getMaturity() {
		return maturity;
	}

	public void setMaturity(Date maturity) {
		this.maturity = maturity;
	}

	public Date getAccCloseDate() {
		return accCloseDate;
	}

	public void setAccCloseDate(Date accCloseDate) {
		this.accCloseDate = accCloseDate;
	}

	public String getAccTypeCode() {
		return accTypeCode;
	}

	public void setAccTypeCode(String accTypeCode) {
		this.accTypeCode = accTypeCode;
	}

	public String getCashType() {
		return cashType;
	}

	public void setCashType(String cashType) {
		this.cashType = cashType;
	}

	public String getAccStat() {
		return accStat;
	}

	public void setAccStat(String accStat) {
		this.accStat = accStat;
	}

	public String getSettledIntRate() {
		return settledIntRate;
	}

	public void setSettledIntRate(String settledIntRate) {
		this.settledIntRate = settledIntRate;
	}

	public Date getLastUpdDate() {
		return lastUpdDate;
	}

	public void setLastUpdDate(Date lastUpdDate) {
		this.lastUpdDate = lastUpdDate;
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getFrozenBalance() {
		return frozenBalance;
	}

	public void setFrozenBalance(String frozenBalance) {
		this.frozenBalance = frozenBalance;
	}

	public String getUnsettledInt() {
		return unsettledInt;
	}

	public void setUnsettledInt(String unsettledInt) {
		this.unsettledInt = unsettledInt;
	}

	public String getIntCalBase() {
		return intCalBase;
	}

	public void setIntCalBase(String intCalBase) {
		this.intCalBase = intCalBase;
	}

	public Date getLastSettleDate() {
		return lastSettleDate;
	}

	public void setLastSettleDate(Date lastSettleDate) {
		this.lastSettleDate = lastSettleDate;
	}

	public String getLastSettleBalance() {
		return lastSettleBalance;
	}

	public void setLastSettleBalance(String lastSettleBalance) {
		this.lastSettleBalance = lastSettleBalance;
	}

	public String getIntCalType() {
		return intCalType;
	}

	public void setIntCalType(String intCalType) {
		this.intCalType = intCalType;
	}

	public String getSettleCycle() {
		return settleCycle;
	}

	public void setSettleCycle(String settleCycle) {
		this.settleCycle = settleCycle;
	}

	public String getSettleDay() {
		return settleDay;
	}

	public void setSettleDay(String settleDay) {
		this.settleDay = settleDay;
	}

	public String getSettleType() {
		return settleType;
	}

	public void setSettleType(String settleType) {
		this.settleType = settleType;
	}

	public Date getAccExtractDate() {
		return accExtractDate;
	}

	public void setAccExtractDate(Date accExtractDate) {
		this.accExtractDate = accExtractDate;
	}

	public Date getUnsettledDate() {
		return unsettledDate;
	}

	public void setUnsettledDate(Date unsettledDate) {
		this.unsettledDate = unsettledDate;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

    
}
