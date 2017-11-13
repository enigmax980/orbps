package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
/**
 * @author zhanghui
 *
 * @date create on  2016年9月7日下午2:43:51
 */

public class GuaranteeRateInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 352128688047488726L;

	/**
	 * 保单组合号
	 */
	@NotNull(message="[组合保单号不能为空]")
	@Length(max=25,message="[组合保单号长度不能大于25位]")
	private String cgNo;
	
	/**
	 * 投保单号
	 */
	@NotNull(message="[投保单号不能为空]")
	@Length(max=16,message="[投保单号长度不能大于16位]")
	private String  applNo;
	/**
	 * 险种代码（第一主险）
	 */
	@NotNull(message="[险种代码（第一主险）不能为空]")
	@Length(max=8,message="[险种代码（第一主险）长度不能大于8位]")
	private String  polCode;
	/**
	 * 管理机构
	 */
	@NotNull(message="[管理机构不能为空]")
	@Length(max=6,message="[管理机构长度不能大于6位]")
	private String mgBranch;
	
	private String dataSource;
	
	
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
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
	public String getMgBranch() {
		return mgBranch;
	}
	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}
	
}
