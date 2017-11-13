package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
/**
 * 团单摘要信息接受服务返回信息
 * @author huangwei
 * 创建时间：2016-08-24
 */
public class GrpListCgTaskRetInfo implements Serializable{


	private static final long serialVersionUID = -8046666365577311792L;
	
	/**
	 * 投保单号
	 */
    private String applNo;

	/*字段名：组合保单号，长度：25*/
	private String cgNo;
	
	/**
	 * 批次号
	 */
	private Long batNo;


	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;
	
	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;

	/**
	 * 
	 */
	public GrpListCgTaskRetInfo() {
		super();
	}

	/**
	 * @param applNo
	 * @param retCode
	 * @param errMsg
	 */
	public GrpListCgTaskRetInfo(String cgNo, String retCode, String errMsg) {
		super();
		this.cgNo = cgNo;
		this.retCode = retCode;
		this.errMsg = errMsg;
	}

	
	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	/**
	 * @return the retCode
	 */
	public String getRetCode() {
		return retCode;
	}

	/**
	 * @param retCode the retCode to set
	 */
	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	/**
	 * @return the errMsg
	 */
	public String getErrMsg() {
		return errMsg;
	}

	/**
	 * @param errMsg the errMsg to set
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public Long getBatNo() {
		return batNo;
	}

	public void setBatNo(Long batNo) {
		this.batNo = batNo;
	}
}
