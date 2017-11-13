package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;
import java.util.Date;

/**
 * 受益人信息Vo
 * @author jincong
 *
 */
public class ListImportBeneficiaryVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080001L;
	
	/** 受益顺序 */
	private Long benefitOrder;
	/** 姓名 */
	private String name;
	/** 性别 */
	private String sex;
	/** 出生日期 */
	private Date birthDate;
	/** 与被保险人关系 */
	private String relToIpsnTb;
	/** 受益份额 */
	private Double beneAmount;
	/** 证件类别 */
	private String idTypeTb;
	/** 证件号 */
	private String idNo;
	/**
	 * @return the benefitOrder
	 */
	public Long getBenefitOrder() {
		return benefitOrder;
	}
	/**
	 * @param benefitOrder the benefitOrder to set
	 */
	public void setBenefitOrder(Long benefitOrder) {
		this.benefitOrder = benefitOrder;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	/**
	 * @return the birthDate
	 */
	public Date getBirthDate() {
		return birthDate;
	}
	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}
	/**
	 * @return the relToIpsnTb
	 */
	public String getRelToIpsnTb() {
		return relToIpsnTb;
	}
	/**
	 * @param relToIpsnTb the relToIpsnTb to set
	 */
	public void setRelToIpsnTb(String relToIpsnTb) {
		this.relToIpsnTb = relToIpsnTb;
	}
	/**
	 * @return the beneAmount
	 */
	public Double getBeneAmount() {
		return beneAmount;
	}
	/**
	 * @param beneAmount the beneAmount to set
	 */
	public void setBeneAmount(Double beneAmount) {
		this.beneAmount = beneAmount;
	}
	/**
	 * @return the idTypeTb
	 */
	public String getIdTypeTb() {
		return idTypeTb;
	}
	/**
	 * @param idTypeTb the idTypeTb to set
	 */
	public void setIdTypeTb(String idTypeTb) {
		this.idTypeTb = idTypeTb;
	}
	/**
	 * @return the idNo
	 */
	public String getIdNo() {
		return idNo;
	}
	/**
	 * @param idNo the idNo to set
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ListImportBeneficiaryVo [benefitOrder=" + benefitOrder + ", name=" + name + ", sex=" + sex
				+ ", birthDate=" + birthDate + ", relToIpsnTb=" + relToIpsnTb + ", beneAmount=" + beneAmount
				+ ", idTypeTb=" + idTypeTb + ", idNo=" + idNo + "]";
	}
	
	
	
}
