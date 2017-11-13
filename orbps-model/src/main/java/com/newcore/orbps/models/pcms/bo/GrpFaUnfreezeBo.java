package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 实收冲正结果
 * 
 * @author maruifu 
 * 创建时间：2016年11月01日
 */
public class GrpFaUnfreezeBo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2012490322160361150L;

	/* 字段名：投保单号，长度：16，是否必填：是 */
	@NotEmpty(message="[投保单号不能为空]")
	private String applNo;

	/* 字段名：管理机构号，长度：6，是否必填：是 */
	@NotEmpty(message="[管理机构号不能为空]")
	private String mgrBranchNo;
	
	//数据源
	private String dataSource;
	
	@NotEmpty(message="[主险险种不能为空]")
	private String polCode;

	
	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	
	
}

