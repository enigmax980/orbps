package com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry;

import java.io.Serializable;
/**
 * 险种信息 
 * @author wangyanjie 
 *
 */

public class CoInsurVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000004L;
	/** 险种代码*/
	private String polCode;
	/** 险种名称 */
	private String polName;
	/**  保额  */
	private Double faceAmnt;
	/** 保费*/
	private Double premium;


	/**
     * @return the polCode
     */
    public String getPolCode() {
        return polCode;
    }
    /**
     * @param polCode the polCode to set
     */
    public void setPolCode(String polCode) {
        this.polCode = polCode;
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
	 * @return the faceAmnt
	 */
	public Double getFaceAmnt() {
		return faceAmnt;
	}
	/**
	 * @param faceAmnt the faceAmnt to set
	 */
	public void setFaceAmnt(Double faceAmnt) {
		this.faceAmnt = faceAmnt;
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
		return "insuranceVo [polName=" + polName + ", faceAmnt=" + faceAmnt + ", premium=" + premium + "  ]";
	}
	

}
