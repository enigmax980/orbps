package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
/**
 * 保单收费检查body内容Bo
 * @author huangwei
 * @date   20160829
 */
public class MioFirstPremChkFramBo implements Serializable {
	
	private static final long serialVersionUID = 7870682530304413579L;

	//机构号   String(6)
	@NotNull(message="[机构号 不能为空]")
	@Length(max=6,message="[机构号长度不能大于6位]")
	private String mgBranch;
	
	//应收标识id  long（20）
	@Length(max=8,message="[机构号长度不能大于8位]")
	private long plnmioRecId;
	
	//应收金额 double（12,2）
	@NotNull(message="[应收金额 不能为空]")
	private double plmioAmnt;
	//投保单号
	@NotNull(message="[投保单号不能为空]")
	@Length(max=16,message="[投保单号长度不能大于16位]")
	private String applNo;
	//险种
	@NotNull(message="[险种不能为空]")
	@Length(max=8,message="[险种长度不能大于8位]")
	private String polCode;
	
	@NotNull(message="[总金额 不能为空]")
	private double totalAmnt;

	public double getTotalAmnt() {
		return totalAmnt;
	}

	public void setTotalAmnt(double totalAmnt) {
		this.totalAmnt = totalAmnt;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}



	public long getPlnmioRecId() {
		return plnmioRecId;
	}

	public void setPlnmioRecId(long plnmioRecId) {
		this.plnmioRecId = plnmioRecId;
	}

	public double getPlmioAmnt() {
		return plmioAmnt;
	}

	public void setPlmioAmnt(double plmioAmnt) {
		this.plmioAmnt = plmioAmnt;
	}

	public String getMgBranch() {
		return mgBranch;
	}

	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}
	
	
}
