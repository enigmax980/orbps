package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

/**
 * 销售员查询服务入参
 *
 * @author zhanghui
 *
 * @date create on  2016年9月8日下午4:21:14
 */
public class SalesmanQueryInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4883651682187514391L;

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
	 * 销售员代码
	 */
	@NotNull(message="[销售员代码不能为空]")
	@Length(max=8,message="[销售员代码长度不能大于8位]")
	private String salesNo;
	
	/**
	 * 代理网点号
	 */
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

	public String getSalesNo() {
		return salesNo;
	}

	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	/**
	 * @return the salesDeptNo
	 */
	public String getSalesDeptNo() {
		return salesDeptNo;
	}

	/**
	 * @param salesDeptNo the salesDeptNo to set
	 */
	public void setSalesDeptNo(String salesDeptNo) {
		this.salesDeptNo = salesDeptNo;
	}
	
}
