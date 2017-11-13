package com.newcore.orbps.models.web.vo.contractentry.registrationacceptance;

import java.io.Serializable;
import java.util.Date;

/**
 * 投保信息
 * @author jincong
 *
 */
public class RegUsualAcceptVo implements Serializable{

	private static final long serialVersionUID = 13242344405L;
	
	/**字段名：投保单号*/
	private String applNo;
	
	/**字段名：申请日期*/
	private Date applDate;
	
	/**字段名：团险定价审批号*/
	private String groupPriceNo;

	/**字段名：外包录入标记*/
	private String entChannelFlag;
	
	/**字段名：被保险人数*/
	private Long insuredNum;
	
	/**字段名：契约型式*/
	private String cntrType;
	
	/**字段名：险种*/
	private String polCode;
	
	/**字段名：保费合计*/
	private Double sumPremium;
	
	/**字段名：投保人/汇缴人*/
	private String hldrName;
	
	/**字段名：投保单数量*/
	private Integer applNum;
		
	/**字段名：是否共保*/
	private String agreementFlag;
	
	/**字段名：共保协议号*/
	private String agreementNum;

	/**字段名：保单类型*/
    private String policyType;
    
    /**字段名：共保协议号*/
    private String agreementShow;
    
    /**字段名：险种代码*/
    private String polName;
    
    /**字段名：币种*/
    private String currencyCode;
    
    /**字段名：共享客户信息*/
    private String paraVal;
    
    /* 字段名：销售人员是否共同展业标识，长度：2，是否必填：否*/
	private String salesDevelopFlag;
	
	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public Date getApplDate() {
		return applDate;
	}

	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}

	public String getGroupPriceNo() {
		return groupPriceNo;
	}

	public void setGroupPriceNo(String groupPriceNo) {
		this.groupPriceNo = groupPriceNo;
	}

	public String getEntChannelFlag() {
		return entChannelFlag;
	}

	public void setEntChannelFlag(String entChannelFlag) {
		this.entChannelFlag = entChannelFlag;
	}

	public Long getInsuredNum() {
		return insuredNum;
	}

	public void setInsuredNum(Long insuredNum) {
		this.insuredNum = insuredNum;
	}

	public String getCntrType() {
		return cntrType;
	}

	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public Double getSumPremium() {
		return sumPremium;
	}

	public void setSumPremium(Double sumPremium) {
		this.sumPremium = sumPremium;
	}

	public String getHldrName() {
		return hldrName;
	}

	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}

	public Integer getApplNum() {
		return applNum;
	}

	public void setApplNum(Integer applNum) {
		this.applNum = applNum;
	}

	public String getAgreementFlag() {
		return agreementFlag;
	}

	public void setAgreementFlag(String agreementFlag) {
		this.agreementFlag = agreementFlag;
	}

	public String getAgreementNum() {
		return agreementNum;
	}

	public void setAgreementNum(String agreementNum) {
		this.agreementNum = agreementNum;
	}

	
	
	/**
     * @return the polName
     */
    public String getPolName() {
        return polName;
    }

    /**
     * @param polName the polName to set
     */
    public void setPolName(String polName) {
        this.polName = polName;
    }

    /**
     * @return the currencyCode
     */
    public String getCurrencyCode() {
        return currencyCode;
    }

    /**
     * @param currencyCode the currencyCode to set
     */
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    /**
     * @return the paraVal
     */
    public String getParaVal() {
        return paraVal;
    }

    /**
     * @param paraVal the paraVal to set
     */
    public void setParaVal(String paraVal) {
        this.paraVal = paraVal;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((agreementFlag == null) ? 0 : agreementFlag.hashCode());
		result = prime * result + ((agreementNum == null) ? 0 : agreementNum.hashCode());
		result = prime * result + ((applDate == null) ? 0 : applDate.hashCode());
		result = prime * result + ((applNo == null) ? 0 : applNo.hashCode());
		result = prime * result + ((applNum == null) ? 0 : applNum.hashCode());
		result = prime * result + ((cntrType == null) ? 0 : cntrType.hashCode());
		result = prime * result + ((entChannelFlag == null) ? 0 : entChannelFlag.hashCode());
		result = prime * result + ((groupPriceNo == null) ? 0 : groupPriceNo.hashCode());
		result = prime * result + ((hldrName == null) ? 0 : hldrName.hashCode());
		result = prime * result + ((insuredNum == null) ? 0 : insuredNum.hashCode());
		result = prime * result + ((polCode == null) ? 0 : polCode.hashCode());
		result = prime * result + ((sumPremium == null) ? 0 : sumPremium.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegUsualAcceptVo other = (RegUsualAcceptVo) obj;
		if (agreementFlag == null) {
			if (other.agreementFlag != null)
				return false;
		} else if (!agreementFlag.equals(other.agreementFlag))
			return false;
		if (agreementNum == null) {
			if (other.agreementNum != null)
				return false;
		} else if (!agreementNum.equals(other.agreementNum))
			return false;
		if (applDate == null) {
			if (other.applDate != null)
				return false;
		} else if (!applDate.equals(other.applDate))
			return false;
		if (applNo == null) {
			if (other.applNo != null)
				return false;
		} else if (!applNo.equals(other.applNo))
			return false;
		if (applNum == null) {
			if (other.applNum != null)
				return false;
		} else if (!applNum.equals(other.applNum))
			return false;
		if (cntrType == null) {
			if (other.cntrType != null)
				return false;
		} else if (!cntrType.equals(other.cntrType))
			return false;
		if (entChannelFlag == null) {
			if (other.entChannelFlag != null)
				return false;
		} else if (!entChannelFlag.equals(other.entChannelFlag))
			return false;
		if (groupPriceNo == null) {
			if (other.groupPriceNo != null)
				return false;
		} else if (!groupPriceNo.equals(other.groupPriceNo))
			return false;
		if (hldrName == null) {
			if (other.hldrName != null)
				return false;
		} else if (!hldrName.equals(other.hldrName))
			return false;
		if (insuredNum == null) {
			if (other.insuredNum != null)
				return false;
		} else if (!insuredNum.equals(other.insuredNum))
			return false;
		if (polCode == null) {
			if (other.polCode != null)
				return false;
		} else if (!polCode.equals(other.polCode))
			return false;
		if (sumPremium == null) {
			if (other.sumPremium != null)
				return false;
		} else if (!sumPremium.equals(other.sumPremium))
			return false;
		return true;
	}

    /**
     * @return the policyType
     */
    public String getPolicyType() {
        return policyType;
    }

    /**
     * @param policyType the policyType to set
     */
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    /**
     * @return the agreementShow
     */
    public String getAgreementShow() {
        return agreementShow;
    }

    public String getSalesDevelopFlag() {
		return salesDevelopFlag;
	}

	public void setSalesDevelopFlag(String salesDevelopFlag) {
		this.salesDevelopFlag = salesDevelopFlag;
	}

	/**
     * @param agreementShow the agreementShow to set
     */
    public void setAgreementShow(String agreementShow) {
        this.agreementShow = agreementShow;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RegUsualAcceptVo [applNo=" + applNo + ", applDate=" + applDate + ", groupPriceNo=" + groupPriceNo
                + ", entChannelFlag=" + entChannelFlag + ", insuredNum=" + insuredNum + ", cntrType=" + cntrType
                + ", polCode=" + polCode + ", sumPremium=" + sumPremium + ", hldrName=" + hldrName + ", applNum="
                + applNum + ", agreementFlag=" + agreementFlag + ", agreementNum=" + agreementNum + ", policyType="
                + policyType + ", agreementShow=" + agreementShow + "]";
    }

	
	
	}
