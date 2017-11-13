package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 缴费信息
 * @author xiaoye
 *
 */
public class GrpPayInfoVo  implements Serializable{
	
	private static final long serialVersionUID = 1145488233L;
	/** 缴费方式 */
	private String moneyinItrvl;
	/** 缴费形式 */
	private String moneyinType;
	/** 保费来源 */
	private String premFrom;
	/** 缴费开户行 */
	private String bankCode;
	/** 开户名 */
	private String bankName;
	/** 账号 */
	private String bankAccNo;
	/** 结算方式 */
	private String stlType;
	/** 结算限额 */
	private Double stlLimit;
	/** 结算比例 */
	private Double settlementRatio;
	/** 结算日期 */
	private List<Date> settlementDate;
	/**供款比例*/
	private Double multiPartyScale;
	/**供款金额*/
	private Double multiPartyMoney;
    /**
     * @return the moneyinItrvl
     */
    public String getMoneyinItrvl() {
        return moneyinItrvl;
    }
    /**
     * @param moneyinItrvl the moneyinItrvl to set
     */
    public void setMoneyinItrvl(String moneyinItrvl) {
        this.moneyinItrvl = moneyinItrvl;
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
     * @return the premFrom
     */
    public String getPremFrom() {
        return premFrom;
    }
    /**
     * @param premFrom the premFrom to set
     */
    public void setPremFrom(String premFrom) {
        this.premFrom = premFrom;
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
    /**
     * @return the stlType
     */
    public String getStlType() {
        return stlType;
    }
    /**
     * @param stlType the stlType to set
     */
    public void setStlType(String stlType) {
        this.stlType = stlType;
    }
    /**
     * @return the stlLimit
     */
    public Double getStlLimit() {
        return stlLimit;
    }
    /**
     * @param stlLimit the stlLimit to set
     */
    public void setStlLimit(Double stlLimit) {
        this.stlLimit = stlLimit;
    }
    /**
     * @return the settlementRatio
     */
    public Double getSettlementRatio() {
        return settlementRatio;
    }
    /**
     * @param settlementRatio the settlementRatio to set
     */
    public void setSettlementRatio(Double settlementRatio) {
        this.settlementRatio = settlementRatio;
    }
    /**
     * @return the settlementDate
     */
    public List<Date> getSettlementDate() {
        return settlementDate;
    }
    /**
     * @param settlementDate the settlementDate to set
     */
    public void setSettlementDate(List<Date> settlementDate) {
        this.settlementDate = settlementDate;
    }
    /**
     * @return the multiPartyScale
     */
    public Double getMultiPartyScale() {
		return multiPartyScale;
	}
    /**
     * @param multiPartyScale the multiPartyScale to set
     */
	public void setMultiPartyScale(Double multiPartyScale) {
		this.multiPartyScale = multiPartyScale;
	}
	/**
     * @return the multiPartyMoney
     */
	public Double getMultiPartyMoney() {
		return multiPartyMoney;
	}
	/**
     * @param multiPartyMoney the multiPartyMoney to set
     */
	public void setMultiPartyMoney(Double multiPartyMoney) {
		this.multiPartyMoney = multiPartyMoney;
	}
	/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GrpPayInfoVo [moneyinItrvl=" + moneyinItrvl + ", moneyinType=" + moneyinType + ", premFrom=" + premFrom
                + ", bankCode=" + bankCode + ", bankName=" + bankName + ", bankAccNo=" + bankAccNo + ", stlType="
                + stlType + ", stlLimit=" + stlLimit + ", settlementRatio=" + settlementRatio + ", settlementDate="
                + settlementDate + ",multiPartyScale=" + multiPartyScale +",multiPartyMoney=" + multiPartyMoney +"]";
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bankAccNo == null) ? 0 : bankAccNo.hashCode());
        result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
        result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
        result = prime * result + ((moneyinItrvl == null) ? 0 : moneyinItrvl.hashCode());
        result = prime * result + ((moneyinType == null) ? 0 : moneyinType.hashCode());
        result = prime * result + ((premFrom == null) ? 0 : premFrom.hashCode());
        result = prime * result + ((settlementDate == null) ? 0 : settlementDate.hashCode());
        result = prime * result + ((settlementRatio == null) ? 0 : settlementRatio.hashCode());
        result = prime * result + ((stlLimit == null) ? 0 : stlLimit.hashCode());
        result = prime * result + ((stlType == null) ? 0 : stlType.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GrpPayInfoVo other = (GrpPayInfoVo) obj;
        if (bankAccNo == null) {
            if (other.bankAccNo != null)
                return false;
        } else if (!bankAccNo.equals(other.bankAccNo))
            return false;
        if (bankCode == null) {
            if (other.bankCode != null)
                return false;
        } else if (!bankCode.equals(other.bankCode))
            return false;
        if (bankName == null) {
            if (other.bankName != null)
                return false;
        } else if (!bankName.equals(other.bankName))
            return false;
        if (moneyinItrvl == null) {
            if (other.moneyinItrvl != null)
                return false;
        } else if (!moneyinItrvl.equals(other.moneyinItrvl))
            return false;
        if (moneyinType == null) {
            if (other.moneyinType != null)
                return false;
        } else if (!moneyinType.equals(other.moneyinType))
            return false;
        if (premFrom == null) {
            if (other.premFrom != null)
                return false;
        } else if (!premFrom.equals(other.premFrom))
            return false;
        if (settlementDate == null) {
            if (other.settlementDate != null)
                return false;
        } else if (!settlementDate.equals(other.settlementDate))
            return false;
        if (settlementRatio == null) {
            if (other.settlementRatio != null)
                return false;
        } else if (!settlementRatio.equals(other.settlementRatio))
            return false;
        if (stlLimit == null) {
            if (other.stlLimit != null)
                return false;
        } else if (!stlLimit.equals(other.stlLimit))
            return false;
        if (stlType == null) {
            if (other.stlType != null)
                return false;
        } else if (!stlType.equals(other.stlType))
            return false;
        return true;
    }
}
