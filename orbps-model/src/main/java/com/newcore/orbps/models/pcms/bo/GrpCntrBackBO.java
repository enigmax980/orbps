package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public class GrpCntrBackBO implements Serializable {



	/**
	 * 
	 */
	private static final long serialVersionUID = 1270676631310610123L;
	//投保单号
	@NotNull(message="[投保单号不能为空]")
	@Length(max=16,message="[投保单号长度不能大于16位]")
	private String applNo;
	//管理机构号	
	@NotNull(message="[管理机构不能为空]")
	@Length(max=6,message="[管理机构长度不能大于6位]")
	private String mgrBranchNo;
	//险种代码（数组）
	@NotNull(message="[险种不能为空]")
	List<String> polCode;
	//处理财务数据标记	
	@NotNull(message="[处理财务数据标记不能为空]")
	@Length(max=1,message="[处理财务数据标记长度不能大于1位]")
	private String isProcMio;
	//数据源
	private String dataSource;
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
	public List<String> getPolCode() {
		return polCode;
	}
	public void setPolCode(List<String> polCode) {
		this.polCode = polCode;
	}
	public String getIsProcMio() {
		return isProcMio;
	}
	public void setIsProcMio(String isProcMio) {
		this.isProcMio = isProcMio;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
 
	

}

