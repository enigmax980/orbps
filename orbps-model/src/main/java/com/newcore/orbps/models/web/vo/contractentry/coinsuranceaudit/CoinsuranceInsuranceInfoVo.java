package com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit;

import java.io.Serializable;

/**
 * 共保审批险种信息
 * @author wangyanjie
 */
public class CoinsuranceInsuranceInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 11464432434L;
	/** 险种代码 */
	private String busiPrdCode;
	/** 险种名称 */
	private String busiPrdCodeName;
	/** 保额 */
	private Double amount;
	/** 保费 */
	private Double premium;
	

    /**
     * @return the busiPrdCode
     */
    public String getBusiPrdCode() {
        return busiPrdCode;
    }
    /**
     * @param busiPrdCode the busiPrdCode to set
     */
    public void setBusiPrdCode(String busiPrdCode) {
        this.busiPrdCode = busiPrdCode;
    }
    /**
     * @return the busiPrdCodeName
     */
    public String getBusiPrdCodeName() {
        return busiPrdCodeName;
    }
    /**
     * @param busiPrdCodeName the busiPrdCodeName to set
     */
    public void setBusiPrdCodeName(String busiPrdCodeName) {
        this.busiPrdCodeName = busiPrdCodeName;
    }
    /**
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }
    /**
     * @param amount the amount to set
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }
    /**
     * @return the premium
     */
    public Double getPremium() {
        return premium;
    }
    /**
     * @param premium the premium to set
     */
    public void setPremium(Double premium) {
        this.premium = premium;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CoinsuranceInsuranceInfoVo [busiPrdCode=" + busiPrdCode + ", busiPrdCodeName=" + busiPrdCodeName
                + ", amount=" + amount + ", premium=" + premium + "]";
    }
}
