package com.newcore.orbps.models.banktrans;

import java.util.ArrayList;
import java.util.List;

/**
 * 存放应收数据分组后的数据实体
 * @author jcc
 * 2016年9月26日 19:38:45
 */
public class PlnmioRecGroupBean {
	private String plnmioRecId; //应收标识ID：根据 id1,id2,id3....的方式进行拼接
	private String applNo;		//投保单号
	private String bankCode;	//银行代码
	private String accCustName;	//帐号所有人
	private String bankAccNo;	//银行帐号
	private Double amnts;		//应收金额总计
	private Integer count;		//条数：用于累计由几条数据Group多得的
	//存放检索出的应收数据，list的条数和count对应，数据信息与plnmioRecId 的对应：例如plnmioRecId是id1,id2,那么plnmioRecList存放两条plnmioRecId分别是id1,id2 的数据
	private List<PlnmioRec> plnmioRecList = new ArrayList<>();
	
	public void addPlnmioRec(PlnmioRec plnmioRec) {
		this.plnmioRecList.add(plnmioRec);
	}

	public String getPlnmioRecId() {
		return plnmioRecId;
	}

	public void setPlnmioRecId(String plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getAccCustName() {
		return accCustName;
	}

	public void setAccCustName(String accCustName) {
		this.accCustName = accCustName;
	}

	public String getBankAccNo() {
		return bankAccNo;
	}

	public void setBankAccNo(String bankAccNo) {
		this.bankAccNo = bankAccNo;
	}

	public Double getAmnts() {
		return amnts;
	}

	public void setAmnts(Double amnts) {
		this.amnts = amnts;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public List<PlnmioRec> getPlnmioRecList() {
		return plnmioRecList;
	}

	public void setPlnmioRecList(List<PlnmioRec> plnmioRecList) {
		this.plnmioRecList = plnmioRecList;
	}
}