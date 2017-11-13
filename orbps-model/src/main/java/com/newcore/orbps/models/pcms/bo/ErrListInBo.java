package com.newcore.orbps.models.pcms.bo;

/**
 * 错误日志查询
 * 
 */
public class ErrListInBo implements java.io.Serializable{




	private static final long serialVersionUID = -5060105039572983614L;

	//组合保单号  长度25
	private String cgNo;

	//投保单号   长度16
	private String applNo;

	//批次号
	private long batNo;

	//险种号 第一主险  长度8  非必传
	private String polCode;

	//管理机构  长度6 非必传
	private String mgBranch;

	public ErrListInBo() {
		super();
		// TODO Auto-generated constructor stub
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

	public long getBatNo() {
		return batNo;
	}

	public void setBatNo(long batNo) {
		this.batNo = batNo;
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
