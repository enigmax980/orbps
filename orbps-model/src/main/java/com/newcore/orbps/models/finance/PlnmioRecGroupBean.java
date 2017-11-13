package com.newcore.orbps.models.finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 存放应收数据分组后的数据实体
 * @author jcc
 * 2016年9月26日 19:38:45
 */
public class PlnmioRecGroupBean {
	private String plnmioRecId; //应收标识ID：根据 id1,id2,id3....的方式进行拼接
	private String cntrNo;		//投保单号
	private int mioClass;		//收付类型
	private Date plnmioDate;	//应收付日期
	private String mioItemCode;	//收付项目代码
	private String mgrBranchNo;	//管理机构
	private String bankCode;	//银行代码
	private String bankAccNo;	//银行帐号
	private String bankAccName;	//帐号所有人
	private BigDecimal amnts;		//应收金额总计
	private int count;		//条数：用于累计由几条数据Group多得的
	//存放检索出的应收数据，list的条数和count对应，数据信息与plnmioRecId 的对应：例如plnmioRecId是id1,id2,那么plnmioRecList存放两条plnmioRecId分别是id1,id2 的数据
	private List<PlnmioRec> plnmioRecList = new ArrayList<>();
	
	public int getMioClass() {
		return mioClass;
	}

	public void setMioClass(int mioClass) {
		this.mioClass = mioClass;
	}

	public Date getPlnmioDate() {
		return plnmioDate;
	}

	public void setPlnmioDate(Date plnmioDate) {
		this.plnmioDate = plnmioDate;
	}

	public String getMioItemCode() {
		return mioItemCode;
	}

	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public void addPlnmioRec(PlnmioRec plnmioRec) {
		this.plnmioRecList.add(plnmioRec);
	}

	public String getPlnmioRecId() {
		return plnmioRecId;
	}

	public void setPlnmioRecId(String plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}
	
	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public BigDecimal getAmnts() {
		return amnts;
	}

	public void setAmnts(BigDecimal amnts) {
		this.amnts = amnts;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<PlnmioRec> getPlnmioRecList() {
		return plnmioRecList;
	}

	public void setPlnmioRecList(List<PlnmioRec> plnmioRecList) {
		this.plnmioRecList = plnmioRecList;
	}

	public String getCntrNo() {
		return cntrNo;
	}

	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankaccName() {
		return bankAccName;
	}

	public void setBankaccName(String bankAccName) {
		this.bankAccName = bankAccName;
	}
}