package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;

/**
 * 回退/订正/要约撤销 BO
 *
 * @author lijifei
 *
 * @date create on  2017年2月27日下午2:46:31
 */
public class CntrBackInBO implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -5263109885422853726L;

	// 投保单号
	private String applNo;

	// 管理机构号
	private String mgrBranchNo;

	// 险种代码（数组）
	List<String> polCodeList;

	// 处理财务数据标记
	private String isProcMioFlag;

	//总保费
	private Double amnt;

	//主险代码
	private String polCode;

	//操作员代码
	private String backClerkNo;

	//操作员机构号
	private String backBranchNo;

	//数据源
	private String dataSource;

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

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

	public List<String> getPolCodeList() {
		return polCodeList;
	}

	public void setPolCodeList(List<String> polCodeList) {
		this.polCodeList = polCodeList;
	}

	public Double getAmnt() {
		return amnt;
	}

	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public String getIsProcMioFlag() {
		return isProcMioFlag;
	}

	public void setIsProcMioFlag(String isProcMioFlag) {
		this.isProcMioFlag = isProcMioFlag;
	}

	public String getBackClerkNo() {
		return backClerkNo;
	}

	public void setBackClerkNo(String backClerkNo) {
		this.backClerkNo = backClerkNo;
	}

	public String getBackBranchNo() {
		return backBranchNo;
	}

	public void setBackBranchNo(String backBranchNo) {
		this.backBranchNo = backBranchNo;
	}

	public CntrBackInBO() {
		super();
		// TODO Auto-generated constructor stub
	}


}
