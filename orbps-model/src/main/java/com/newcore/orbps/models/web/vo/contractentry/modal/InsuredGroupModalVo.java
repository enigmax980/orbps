package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;
import java.util.List;

/**
 * 被保人分组信息
 * @author xiaoYe
 *
 */
public class InsuredGroupModalVo  implements Serializable{
	
	private static final long serialVersionUID = 5478844233L;
	/** 要约属组编号 */
    private Long ipsnGrpNo;
    /** 要约属组名称 */
    private String ipsnGrpName;
    /** 要约属组分组类型 */
    private String ipsnGrpType;
    /** 行业类别 */
    private String occClassCode;
    /** 职业类别 */
    private String ipsnOccSubclsCode;
    /** 要约属组人数 */
    private Long ipsnGrpNum;
    /** 男女比例 */
    private Double genderRadio;
    /** 参加工伤比例 */
    private Double gsRate;
    /** 参加医保比例 */
    private Double ssRate;
	
    /** 险种list */
	private List<InsuranceInfoVo> insuranceInfoVos ;

	/**
	 * @return the ipsnGrpNo
	 */
	public Long getIpsnGrpNo() {
		return ipsnGrpNo;
	}

	/**
	 * @param ipsnGrpNo the ipsnGrpNo to set
	 */
	public void setIpsnGrpNo(Long ipsnGrpNo) {
		this.ipsnGrpNo = ipsnGrpNo;
	}

	/**
	 * @return the ipsnGrpName
	 */
	public String getIpsnGrpName() {
		return ipsnGrpName;
	}

	/**
	 * @param ipsnGrpName the ipsnGrpName to set
	 */
	public void setIpsnGrpName(String ipsnGrpName) {
		this.ipsnGrpName = ipsnGrpName;
	}

	/**
	 * @return the ipsnGrpType
	 */
	public String getIpsnGrpType() {
		return ipsnGrpType;
	}

	/**
	 * @param ipsnGrpType the ipsnGrpType to set
	 */
	public void setIpsnGrpType(String ipsnGrpType) {
		this.ipsnGrpType = ipsnGrpType;
	}

	/**
	 * @return the occClassCode
	 */
	public String getOccClassCode() {
		return occClassCode;
	}

	/**
	 * @param occClassCode the occClassCode to set
	 */
	public void setOccClassCode(String occClassCode) {
		this.occClassCode = occClassCode;
	}

	/**
	 * @return the ipsnOccSubclsCode
	 */
	public String getIpsnOccSubclsCode() {
		return ipsnOccSubclsCode;
	}

	/**
	 * @param ipsnOccSubclsCode the ipsnOccSubclsCode to set
	 */
	public void setIpsnOccSubclsCode(String ipsnOccSubclsCode) {
		this.ipsnOccSubclsCode = ipsnOccSubclsCode;
	}

	/**
	 * @return the ipsnGrpNum
	 */
	public Long getIpsnGrpNum() {
		return ipsnGrpNum;
	}

	/**
	 * @param ipsnGrpNum the ipsnGrpNum to set
	 */
	public void setIpsnGrpNum(Long ipsnGrpNum) {
		this.ipsnGrpNum = ipsnGrpNum;
	}

	/**
	 * @return the genderRadio
	 */
	public Double getGenderRadio() {
		return genderRadio;
	}

	/**
	 * @param genderRadio the genderRadio to set
	 */
	public void setGenderRadio(Double genderRadio) {
		this.genderRadio = genderRadio;
	}

	/**
	 * @return the gsRate
	 */
	public Double getGsRate() {
		return gsRate;
	}

	/**
	 * @param gsRate the gsRate to set
	 */
	public void setGsRate(Double gsRate) {
		this.gsRate = gsRate;
	}

	/**
	 * @return the ssRate
	 */
	public Double getSsRate() {
		return ssRate;
	}

	/**
	 * @param ssRate the ssRate to set
	 */
	public void setSsRate(Double ssRate) {
		this.ssRate = ssRate;
	}

	/**
	 * @return the insuranceInfoVos
	 */
	public List<InsuranceInfoVo> getInsuranceInfoVos() {
		return insuranceInfoVos;
	}

	/**
	 * @param insuranceInfoVos the insuranceInfoVos to set
	 */
	public void setInsuranceInfoVos(List<InsuranceInfoVo> insuranceInfoVos) {
		this.insuranceInfoVos = insuranceInfoVos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InsuredGroupModalVo [ipsnGrpNo=" + ipsnGrpNo + ", ipsnGrpName=" + ipsnGrpName + ", ipsnGrpType="
				+ ipsnGrpType + ", occClassCode=" + occClassCode + ", ipsnOccSubclsCode=" + ipsnOccSubclsCode
				+ ", ipsnGrpNum=" + ipsnGrpNum + ", genderRadio=" + genderRadio + ", gsRate=" + gsRate + ", ssRate="
				+ ssRate + ", insuranceInfoVos=" + insuranceInfoVos + "]";
	}

}
