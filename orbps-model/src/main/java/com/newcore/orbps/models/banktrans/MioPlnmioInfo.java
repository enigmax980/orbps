package com.newcore.orbps.models.banktrans;

import java.util.List;

/**
 * * 收付费相关信息类
*
* @author:王鸿林 
* 创建时间:2016年9月19日上午9:52:50
 */
/**
 * 
 * @author 王鸿林
 *
 * @date 2016年10月8日 下午5:41:29
 */
public class MioPlnmioInfo  {
	private String applNo;		//投保单号或保单号
	private  double sumPremium;	//总保费
	private int plnmioNum;		//总应收条数
	private int mioNum;			//总实收条数
	private int bankTransNum;   //总银行转账条数
	private List<BankTrans> bankTransList;	//银行转账数据集合
	private List<MioLog> mioLogList;		//实收数据集合
	private List<PlnmioRec> plnmioRecList;	//应收数据集合
	
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public double getSumPremium() {
		return sumPremium;
	}
	public void setSumPremium(double sumPremium) {
		this.sumPremium = sumPremium;
	}
	public int getPlnmioNum() {
		return plnmioNum;
	}
	public void setPlnmioNum(int plnmioNum) {
		this.plnmioNum = plnmioNum;
	}
	public int getMioNum() {
		return mioNum;
	}
	public void setMioNum(int mioNum) {
		this.mioNum = mioNum;
	}
	public int getBankTransNum() {
		return bankTransNum;
	}
	public void setBankTransNum(int bankTransNum) {
		this.bankTransNum = bankTransNum;
	}
	public List<BankTrans> getBankTransList() {
		return bankTransList;
	}
	public void setBankTransList(List<BankTrans> bankTransList) {
		this.bankTransList = bankTransList;
	}
	public List<MioLog> getMioLogList() {
		return mioLogList;
	}
	public void setMioLogList(List<MioLog> mioLogList) {
		this.mioLogList = mioLogList;
	}
	public List<PlnmioRec> getPlnmioRecList() {
		return plnmioRecList;
	}
	public void setPlnmioRecList(List<PlnmioRec> plnmioRecList) {
		this.plnmioRecList = plnmioRecList;
	}
	
	
}
