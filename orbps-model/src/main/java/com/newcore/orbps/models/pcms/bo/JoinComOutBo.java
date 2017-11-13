
package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

/**
* <p>参与共保财务与财务队列落地服务出参 </p>
* @author zhangyuan
* @date 2016年11月28日
*/
public class JoinComOutBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2672661878451289936L;
	/**
	 * 执行结果代码
	 * 0: 执行失败1：执行成功
	 */
	private String retCode;
	/**
	 * 错误信息
	 */
	private String errMsg;
	/**
	 * 共保协议号
	 */
	private String agreementNo;
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
	public String getAgreementNo() {
		return agreementNo;
	}
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	
}
