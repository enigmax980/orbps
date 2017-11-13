package com.newcore.orbps.models.banktrans;
/**
 * 
 * @author 王鸿林
 *
 * @date 2016年10月21日 下午3:09:01
 * @TODO 业务收付对应银行转帐号及所属机构
 */
public class BtBranchContrast {
	private String  bankCode;//银行代码
	private String pclkBranchNo;//操作员分支机构号
	private long serviceDeptNo;//服务网点
	private String polCode;//险种代码
	private String mioType;//首付款方式代码
	private long mioClass;//收付类型
	private String branchBankAccNo;//财务系统银行账号编码
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	public long getServiceDeptNo() {
		return serviceDeptNo;
	}
	public void setServiceDeptNo(long serviceDeptNo) {
		this.serviceDeptNo = serviceDeptNo;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getMioType() {
		return mioType;
	}
	public void setMioType(String mioType) {
		this.mioType = mioType;
	}
	public long getMioClass() {
		return mioClass;
	}
	public void setMioClass(long mioClass) {
		this.mioClass = mioClass;
	}
	public String getBranchBankAccNo() {
		return branchBankAccNo;
	}
	public void setBranchBankAccNo(String branchBankAccNo) {
		this.branchBankAccNo = branchBankAccNo;
	}
	
}
