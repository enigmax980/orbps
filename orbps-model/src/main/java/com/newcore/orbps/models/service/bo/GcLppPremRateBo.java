package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.Date;

/*团体保单一次性缴费多期缴费标准
 * 
 * lijifei
 * 
 * */

public class GcLppPremRateBo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 5738739248581970926L;
	//险种代码
	private String polCode;
	//指定保单生效日期
	private Date inForceDate;
	//保费
	private double premium;
	//保险期间单位（显示/计算）
	private String insurDurUnit;
	//保险期
	private Integer insurDur;
	public GcLppPremRateBo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public Date getInForceDate() {
		return inForceDate;
	}
	public void setInForceDate(Date inForceDate) {
		this.inForceDate = inForceDate;
	}
	public double getPremium() {
		return premium;
	}
	public void setPremium(double premium) {
		this.premium = premium;
	}
	public String getInsurDurUnit() {
		return insurDurUnit;
	}
	public void setInsurDurUnit(String insurDurUnit) {
		this.insurDurUnit = insurDurUnit;
	}
	public Integer getInsurDur() {
		return insurDur;
	}
	public void setInsurDur(Integer insurDur) {
		this.insurDur = insurDur;
	}





}
