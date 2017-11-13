package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

/**
 * 实收冲正结果查询服务返回信息
 * @author maruifu-lijifei
 * 创建时间：2016年11月01日下午5:05:25
 */
public class MioUndoRetInfo implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -4051362156139821596L;

	/*字段名：投保单号，长度：16*/
	private String applNo;

	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;
	
	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;
	
	/*是否冲正成功   长度：2， */
	private String isSucceed;
	
	/*字段名 查询结果，长度：500，*/
	private String qryResult;

	/**
	 * 
	 */
	public MioUndoRetInfo() {
		super();
	}

	/**
	 * @param applNo
	 * @param retCode
	 * @param errMsg
	 * @param isSucceed
	 */
	public MioUndoRetInfo(String applNo, String retCode, String errMsg,String isSucceed) {
		super();
		this.applNo = applNo;
		this.retCode = retCode;
		this.errMsg = errMsg;
		this.isSucceed = isSucceed;
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

	public String getIsSucceed() {
		return isSucceed;
	}

	public void setIsSucceed(String isSucceed) {
		this.isSucceed = isSucceed;
	}

	public String getQryResult() {
		return qryResult;
	}

	public void setQryResult(String qryResult) {
		this.qryResult = qryResult;
	}
	

}
