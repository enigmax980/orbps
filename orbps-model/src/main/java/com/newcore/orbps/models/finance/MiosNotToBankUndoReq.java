package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author huanglong
 * @date 2017年2月27日
 * @content 撤销暂停送划登记提交请求对象
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "IO_MIOS_BANK_TRANS_NOTTOBANKCHEXT_SCXE")
public class MiosNotToBankUndoReq implements Serializable{

	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -614512757524738905L;
	
	/*字段名称:系统,长度:,是否必填:否*/
	@XmlElement(name = "SYS_NO")
	private String sysNo;
	/*字段名称:合同号,长度:,是否必填:否*/
	@XmlElement(name = "CNTR_NO")
	private String cntrNo;
	/*字段名称:客户号,长度:,是否必填:否*/
	@XmlElement(name = "CUST_NO")
	private String custNo;
	/*字段名称:应收付日期,长度:,是否必填:否*/
	@XmlElement(name = "PLNMIO_DATE")
	private String plnmioDate;
	/*字段名称:收付标志,长度:,是否必填:否*/
	@XmlElement(name = "MIO_CLASS")
	private String mioClass;
	/*字段名称:项目,长度:,是否必填:否*/
	@XmlElement(name = "MIO_ITEM_CODE")
	private String mioItemCode;
	/*字段名称:银行代号,长度:,是否必填:否*/
	@XmlElement(name = "BANK_CODE")
	private String bankCode;
	/*字段名称:帐号,长度:,是否必填:否*/
	@XmlElement(name = "BANK_ACC_NO")
	private String bankAccNo;
	/*字段名称:金额,长度:,是否必填:否*/
	@XmlElement(name = "AMNT")
	private Double amnt;
	/*字段名称:管理机构,长度:,是否必填:否*/
	@XmlElement(name = "MGR_BRANCH_NO")
	private String mgrBranchNo;
	/*字段名称:顺序号,长度:,是否必填:否*/
	@XmlElement(name = "PLNMIO_REC_ID")
	private String plnmioRecId;
	/*字段名称:款项所有人,长度:,是否必填:否*/
	@XmlElement(name = "MIO_CUST_NAME")
	private String mioCustName;
	/*字段名称:生成应收付操作员机构,长度:,是否必填:否*/
	@XmlElement(name = "GCLK_BRANCH_NO")
	private String gclkBranchNo;
	/*字段名称:生成应收付操作员代码,长度:,是否必填:否*/
	@XmlElement(name = "GCLK_CLERK_NO")
	private String gclkClerkNo;
	/*字段名称:不可送划原因,长度:,是否必填:否*/
	@XmlElement(name = "STOP_TRANS_REASON")
	private String stopTransReason;
	/*字段名称:不可送划起日,长度:,是否必填:否*/
	@XmlElement(name = "STOP_TRANS_DATE")
	private String stopTransDate; 
	/*字段名称:恢复送划日期,长度:,是否必填:否*/
	@XmlElement(name = "RE_TRANS_DATE")
	private String reTransDate;
	/*字段名称:是否冻结应收付表 0-不冻结1-冻结,长度:,是否必填:否*/
	@XmlElement(name = "LOCK_FLAG")
	private String lockFlag;
	/*字段名称:是否送划 0-不送1-要送划,长度:,是否必填:否*/
	@XmlElement(name = "TRANS_FLAG")
	private String transFlag;
	/*字段名称:录入时间,长度:,是否必填:否*/
	@XmlElement(name = "ENTER_TIME")
	private String enterTime;
	/*字段名称:录入机构,长度:,是否必填:否*/
	@XmlElement(name = "ENTER_BRANCH_NO")
	private String enterBranchNo;
	/*字段名称:录入工号,长度:,是否必填:否*/
	@XmlElement(name = "ENTER_CLERK_NO")
	private String enterClerkNo;
	/*字段名称:是否撤消 0-未撤1-已撤2-不可撤消,长度:,是否必填:否*/
	@XmlElement(name = "CANCEL_FLAG")
	private String cancelFlag;
	/*字段名称:撤消时间,长度:,是否必填:否*/
	@XmlElement(name = "CANCEL_TIME")
	private String cancelTime;
	/*字段名称:撤消机构,长度:,是否必填:否*/
	@XmlElement(name = "CANCEL_BRANCH_NO")
	private String cancelBranchNo;
	/*字段名称:撤消工号,长度:,是否必填:否*/
	@XmlElement(name = "CANCEL_CLERK_NO")
	private String cancelClerkNo;
	/*字段名称:撤消理由,长度:,是否必填:否*/
	@XmlElement(name = "CANCEL_REASON")
	private String cancelReason;
	/*字段名称:失效标志 0-未失效1-失效,长度:,是否必填:否*/
	@XmlElement(name = "INVALID_STAT")
	private String invalidStat;
	/*字段名称:是否转账途中,长度:,是否必填:否*/
	@XmlElement(name = "HOLD_FLAG")
	private String holdFlag;
	/*字段名称:收付费形式,长度:,是否必填:否*/
	@XmlElement(name = "MIO_TYPE")
	private String mioTypeCode;
	/*字段名称:选择标志,长度:,是否必填:否*/
	@XmlElement(name = "SELFLAG")
	private String selFlag;
	/*字段名称:处理标志 发起查询时，0-相关所有、1-登记、2-回退登记、回退处理时，0-不可操作、1-可操作,长度:,是否必填:否*/
	@XmlElement(name = "BANKREG_FLAG")
	private String bankRegFlag;
	/*字段名称:备注说明,长度:,是否必填:否*/
	@XmlElement(name = "REMARK")
	private String remark;
	public MiosNotToBankUndoReq() {
		super();
	}
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
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
	public String getPlnmioDate() {
		return plnmioDate;
	}
	public void setPlnmioDate(String plnmioDate) {
		this.plnmioDate = plnmioDate;
	}
	public String getMioClass() {
		return mioClass;
	}
	public void setMioClass(String mioClass) {
		this.mioClass = mioClass;
	}
	public String getMioItemCode() {
		return mioItemCode;
	}
	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getBankAccNo() {
		return bankAccNo;
	}
	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}
	public Double getAmnt() {
		return amnt;
	}
	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getPlnmioRecId() {
		return plnmioRecId;
	}
	public void setPlnmioRecId(String plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
	public String getMioCustName() {
		return mioCustName;
	}
	public void setMioCustName(String mioCustName) {
		this.mioCustName = mioCustName;
	}
	public String getGclkBranchNo() {
		return gclkBranchNo;
	}
	public void setGclkBranchNo(String gclkBranchNo) {
		this.gclkBranchNo = gclkBranchNo;
	}
	public String getGclkClerkNo() {
		return gclkClerkNo;
	}
	public void setGclkClerkNo(String gclkClerkNo) {
		this.gclkClerkNo = gclkClerkNo;
	}
	public String getStopTransReason() {
		return stopTransReason;
	}
	public void setStopTransReason(String stopTransReason) {
		this.stopTransReason = stopTransReason;
	}
	public String getStopTransDate() {
		return stopTransDate;
	}
	public void setStopTransDate(String stopTransDate) {
		this.stopTransDate = stopTransDate;
	}
	public String getReTransDate() {
		return reTransDate;
	}
	public void setReTransDate(String reTransDate) {
		this.reTransDate = reTransDate;
	}
	public String getLockFlag() {
		return lockFlag;
	}
	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}
	public String getTransFlag() {
		return transFlag;
	}
	public void setTransFlag(String transFlag) {
		this.transFlag = transFlag;
	}
	public String getEnterTime() {
		return enterTime;
	}
	public void setEnterTime(String enterTime) {
		this.enterTime = enterTime;
	}
	public String getEnterBranchNo() {
		return enterBranchNo;
	}
	public void setEnterBranchNo(String enterBranchNo) {
		this.enterBranchNo = enterBranchNo;
	}
	public String getEnterClerkNo() {
		return enterClerkNo;
	}
	public void setEnterClerkNo(String enterClerkNo) {
		this.enterClerkNo = enterClerkNo;
	}
	public String getCancelFlag() {
		return cancelFlag;
	}
	public void setCancelFlag(String cancelFlag) {
		this.cancelFlag = cancelFlag;
	}
	public String getCancelTime() {
		return cancelTime;
	}
	public void setCancelTime(String cancelTime) {
		this.cancelTime = cancelTime;
	}
	public String getCancelBranchNo() {
		return cancelBranchNo;
	}
	public void setCancelBranchNo(String cancelBranchNo) {
		this.cancelBranchNo = cancelBranchNo;
	}
	public String getCancelClerkNo() {
		return cancelClerkNo;
	}
	public void setCancelClerkNo(String cancelClerkNo) {
		this.cancelClerkNo = cancelClerkNo;
	}
	public String getCancelReason() {
		return cancelReason;
	}
	public void setCancelReason(String cancelReason) {
		this.cancelReason = cancelReason;
	}
	public String getInvalidStat() {
		return invalidStat;
	}
	public void setInvalidStat(String invalidStat) {
		this.invalidStat = invalidStat;
	}
	public String getHoldFlag() {
		return holdFlag;
	}
	public void setHoldFlag(String holdFlag) {
		this.holdFlag = holdFlag;
	}
	public String getMioTypeCode() {
		return mioTypeCode;
	}
	public void setMioTypeCode(String mioTypeCode) {
		this.mioTypeCode = mioTypeCode;
	}
	public String getSelFlag() {
		return selFlag;
	}
	public void setSelFlag(String selFlag) {
		this.selFlag = selFlag;
	}
	public String getBankRegFlag() {
		return bankRegFlag;
	}
	public void setBankRegFlag(String bankRegFlag) {
		this.bankRegFlag = bankRegFlag;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
