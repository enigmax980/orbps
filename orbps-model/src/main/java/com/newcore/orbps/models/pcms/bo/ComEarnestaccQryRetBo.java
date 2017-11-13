package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

/**
 * 参与共保暂交保费查询服务  出参
 *
 * @author zhanghui
 *
 * @date create on  2016年11月28日下午4:02:09
 */
public class ComEarnestaccQryRetBo implements Serializable{

	private static final long serialVersionUID = -7175416972505474141L;

	/**协议号**/
	private String agreementNo;
	
	/**执行结果代码**/
	private String retCode;
	
	/**错误信息**/
	private String errMsg;
	
	/**首期暂收费账户对象**/
	private EarnestAcc earnestAcc;

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
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

	public EarnestAcc getEarnestAcc() {
		return earnestAcc;
	}

	public void setEarnestAcc(EarnestAcc earnestAcc) {
		this.earnestAcc = earnestAcc;
	}
}
