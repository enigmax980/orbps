package com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit;

import java.io.Serializable;

/**
 * 共保审批公司信息  
 * @author wangyanjie
 *
 */
public class CoInsurComInFoVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000003L;
	/** 共保公司名称 */
	private String companyName;
	/**  是否本公司  */
	private String companyFlag;
	/** 共保身份*/
	private String connIdType;
	/** 共保保费份额占比   */
	private double amntPct;
	/** 共保责任份额占比 */
	private double responsibilityPct;
	/** 开户银行 */
	private String bankCode;
	/** 开户名称  */
	private String accCustName;
	/** 银行账号 */
	private String bankAccNo;
	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}
	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	/**
	 * @return the companyFlag
	 */
	public String getCompanyFlag() {
		return companyFlag;
	}
	/**
	 * @param companyFlag the companyFlag to set
	 */
	public void setCompanyFlag(String companyFlag) {
		this.companyFlag = companyFlag;
	}
	/**
	 * @return the connIdType
	 */
	public String getConnIdType() {
		return connIdType;
	}
	/**
	 * @param connIdType the connIdType to set
	 */
	public void setConnIdType(String connIdType) {
		this.connIdType = connIdType;
	}

	/**
     * @return the amntPct
     */
    public double getAmntPct() {
        return amntPct;
    }
    /**
     * @param amntPct the amntPct to set
     */
    public void setAmntPct(double amntPct) {
        this.amntPct = amntPct;
    }
    /**
     * @return the responsibilityPct
     */
    public double getResponsibilityPct() {
        return responsibilityPct;
    }
    /**
     * @param responsibilityPct the responsibilityPct to set
     */
    public void setResponsibilityPct(double responsibilityPct) {
        this.responsibilityPct = responsibilityPct;
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
	 * @return the accCustName
	 */
	public String getAccCustName() {
		return accCustName;
	}
	/**
	 * @param accCustName the accCustName to set
	 */
	public void setAccCustName(String accCustName) {
		this.accCustName = accCustName;
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
		return "insuranceComVo [companyName=" + companyName + ", companyFlag=" + companyFlag + ", connIdType="
				+ connIdType + ", amntPct=" + amntPct + ", responsibilityPct=" + responsibilityPct + ", bankCode="
				+ bankCode + ", accCustName=" + accCustName + ", bankAccNo=" + bankAccNo + "]";
	}

}
