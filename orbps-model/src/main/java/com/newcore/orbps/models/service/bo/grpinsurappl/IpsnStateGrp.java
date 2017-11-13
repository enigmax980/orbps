package com.newcore.orbps.models.service.bo.grpinsurappl;

import java.io.Serializable;
import java.util.List;

/**
 * 要约分组(整体选录)
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:33:09
 */
public class IpsnStateGrp implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4869915201476929588L;

	/* 字段名：分组类型，长度：2，是否必填：是 */
	private String ipsnGrpType;

	/* 字段名：要约属组编号，是否必填：是 */
	private Long ipsnGrpNo;

	/* 字段名：要约属组名称，长度：32，是否必填：是 */
	private String ipsnGrpName;

	/* 字段名：行业类别，长度：2，是否必填：IF ipsnGrpType == 1 THEN 必填 */
	private String occClassCode;

	/* 字段名：职业代码，长度：6，是否必填：IF ipsnGrpType == 1 THEN 必填 */
	private String ipsnOccCode;

	/* 字段名：要约属组人数，是否必填：否 */
	private Long ipsnGrpNum;

	/* 字段名：参加工伤比例，是否必填：否 */
	private Double gsPct;

	/* 字段名：参加医保比例，是否必填：否 */
	private Double ssPct;

	/* 字段名：男女比例，是否必填：否 */
	private Double genderRadio;

	// 险种
	private List<GrpPolicy> grpPolicyList;

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
	 * @return the ipsnOccCode
	 */
	public String getIpsnOccCode() {
		return ipsnOccCode;
	}

	/**
	 * @param ipsnOccCode the ipsnOccCode to set
	 */
	public void setIpsnOccCode(String ipsnOccCode) {
		this.ipsnOccCode = ipsnOccCode;
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
	 * @return the gsPct
	 */
	public Double getGsPct() {
		return gsPct;
	}

	/**
	 * @param gsPct the gsPct to set
	 */
	public void setGsPct(Double gsPct) {
		this.gsPct = gsPct;
	}

	/**
	 * @return the ssPct
	 */
	public Double getSsPct() {
		return ssPct;
	}

	/**
	 * @param ssPct the ssPct to set
	 */
	public void setSsPct(Double ssPct) {
		this.ssPct = ssPct;
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
	 * @return the grpPolicyList
	 */
	public List<GrpPolicy> getGrpPolicyList() {
		return grpPolicyList;
	}

	/**
	 * @param grpPolicyList the grpPolicyList to set
	 */
	public void setGrpPolicyList(List<GrpPolicy> grpPolicyList) {
		this.grpPolicyList = grpPolicyList;
	}

}
