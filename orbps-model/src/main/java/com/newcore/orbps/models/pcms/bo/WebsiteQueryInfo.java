package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class WebsiteQueryInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9217331793340746253L;

	/**
	 * 销售渠道
	 */
	@NotNull(message="[销售渠道不能为空]")
	@Length(max=2,message="[销售渠道长度不能大于2位]")
	private String salesChannel;
	
	/**
	 * 销售机构号
	 */
	@NotNull(message="[销售机构号不能为空]")
	@Length(max=6,message="[销售机构号长度不能大于6位]")
	private String salesBranchNo;
	
	/**
	 * 网点号
	 */
	@NotNull(message="[网点号不能为空]")
	@Length(max=8,message="[网点号长度不能大于8位]")
	private String salesDeptNo;

	/**
	 * 数据源
	 */
	private String dataSource;
	
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getSalesChannel() {
		return salesChannel;
	}

	public void setSalesChannel(String salesChannel) {
		this.salesChannel = salesChannel;
	}

	public String getSalesBranchNo() {
		return salesBranchNo;
	}

	public void setSalesBranchNo(String salesBranchNo) {
		this.salesBranchNo = salesBranchNo;
	}

	public String getSalesDeptNo() {
		return salesDeptNo;
	}

	public void setSalesDeptNo(String salesDeptNo) {
		this.salesDeptNo = salesDeptNo;
	}
	
}
