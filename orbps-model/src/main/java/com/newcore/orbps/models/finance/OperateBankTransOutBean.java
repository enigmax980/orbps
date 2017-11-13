package com.newcore.orbps.models.finance;

import java.io.Serializable;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 银行转账数据
 * 
 * @author lijifei 2017年3月3日 17:26:45
 */
@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RESPONSE")
public class OperateBankTransOutBean implements Serializable {

	/**
	 * 返回条数
	 */
	@XmlElement(name = "BTT_INFO_NUM")
	private String bttInfoNum;


	/**
	 * 返回条数对应的银行信息
	 */
	@XmlElement(name = "BANK_ZZCZ_SCXE")
	private List<BankZzczScxe> bankZzczScxe;


	public String getBttInfoNum() {
		return bttInfoNum;
	}


	public void setBttInfoNum(String bttInfoNum) {
		this.bttInfoNum = bttInfoNum;
	}


	public List<BankZzczScxe> getBankZzczScxe() {
		return bankZzczScxe;
	}


	public void setBankZzczScxe(List<BankZzczScxe> bankZzczScxe) {
		this.bankZzczScxe = bankZzczScxe;
	}


	public OperateBankTransOutBean() {
		super();
		// TODO Auto-generated constructor stub
	}





























}
