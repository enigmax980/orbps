package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;

/**
 * 收付费分组信息
 * @author xiaoYe
 *
 */
public class ChargePayGroupModalVo  implements Serializable{
	
	private static final long serialVersionUID = 54788442313L;
	/** 组号 */
	private Long groupNo;
	/** 组名 */
	private String groupName;
	/** 人数 */
	private Long num;
	/** 交费形式 */
	private String moneyinType;
	/** 开户银行 */
	private String bankCode;
	/** 开户名称 */
	private String bankName;
	/** 银行帐号 */
	private String bankAccNo;
    /**
     * @return the groupNo
     */
    public Long getGroupNo() {
        return groupNo;
    }
    /**
     * @param groupNo the groupNo to set
     */
    public void setGroupNo(Long groupNo) {
        this.groupNo = groupNo;
    }
    /**
     * @return the groupName
     */
    public String getGroupName() {
        return groupName;
    }
    /**
     * @param groupName the groupName to set
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    /**
     * @return the num
     */
    public Long getNum() {
        return num;
    }
    /**
     * @param num the num to set
     */
    public void setNum(Long num) {
        this.num = num;
    }
    /**
     * @return the moneyinType
     */
    public String getMoneyinType() {
        return moneyinType;
    }
    /**
     * @param moneyinType the moneyinType to set
     */
    public void setMoneyinType(String moneyinType) {
        this.moneyinType = moneyinType;
    }
    /**
     * @return the bankCode
     */
    public String getBankCode() {
        return bankCode;
    }
    /**
     * @param bankCode the bankCode to set
     */
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }
    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    /**
     * @return the bankAccNo
     */
    public String getBankAccNo() {
        return bankAccNo;
    }
    /**
     * @param bankAccNo the bankAccNo to set
     */
    public void setBankAccNo(String bankAccNo) {
        this.bankAccNo = bankAccNo;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ChargePayGroupModalVo [groupNo=" + groupNo + ", groupName=" + groupName + ", num=" + num
                + ", moneyinType=" + moneyinType + ", bankCode=" + bankCode + ", bankName=" + bankName + ", bankAccNo="
                + bankAccNo + "]";
    }

	
}
