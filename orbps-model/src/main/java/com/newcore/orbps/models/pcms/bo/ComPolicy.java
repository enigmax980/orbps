package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;


/**
 * 险种
 * @author wangxiao 
 * 创建时间：2016年7月21日上午9:21:09
 */
public class ComPolicy implements Serializable{

	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -921313141395570206L;

	/* 字段名：险种，长度：8，是否必填：是 */
	@NotNull(message="[险种不能为空]")
	@Length(max=8,message="[险种长度不能大于8位]")
	private String polCode;

	/* 字段名：险种保额，是否必填：是 */
	@NotNull(message="[险种保额不能为空]")
	private Double faceAmnt;

	/* 字段名：实际保费，是否必填：是 */
	@NotNull(message="[实际保费不能为空]")
	private Double premium;

	/*
	 * 字段名：主附险性质，长度：1，是否必填：是
	 * M 主险、R 附险(被保人)、E 附险(投保人)
	 */
	@NotNull(message="[主附险性质不能为空]")
	@Length(max=2,message="[主附险性质不能大于2位]")
	private String mrCode;
	
	/*
	 * 字段名：备注
	 */
	private String remark;

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public Double getFaceAmnt() {
		return faceAmnt;
	}

	public void setFaceAmnt(Double faceAmnt) {
		this.faceAmnt = faceAmnt;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public String getMrCode() {
		return mrCode;
	}

	public void setMrCode(String mrCode) {
		this.mrCode = mrCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "ComPolicy [polCode=" + polCode + ", faceAmnt=" + faceAmnt + ", premium=" + premium + ", mrCode="
				+ mrCode + ", remark=" + remark + "]";
	}
}
