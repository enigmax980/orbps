package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
/**
 * @author lijifei
 * @date 2016年12月01日
 * @content: 清单数据落地完成情况告知服务-返回bo
 */
public class RetInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -259264521990152279L;

	/*字段名：投保单号，长度：16*/
	private String applNo;

	/*字段名：组合保单号，长度：25*/
	private String cgNo;

	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;

	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;

	public RetInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}




}
