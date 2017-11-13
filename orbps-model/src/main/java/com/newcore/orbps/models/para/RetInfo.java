package com.newcore.orbps.models.para;

import java.io.Serializable;

/**
 * 团体出单返回信息
 * @author wangxiao
 * 创建时间：2016年7月21日下午5:05:25
 */
public class RetInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8046666365577311792L;

	/*字段名：投保单号，长度：16*/
	private String applNo;

	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;
	
	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;

	/**
	 * 
	 */
	public RetInfo() {
		super();
	}

	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}

	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
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

}
