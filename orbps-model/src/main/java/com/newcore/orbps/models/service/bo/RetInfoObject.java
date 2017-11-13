package com.newcore.orbps.models.service.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RetInfoObject<T> implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3156010132506438234L;

	/*字段名：投保单号，长度：16*/
	private String applNo;

	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;
	
	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;
	/*传入list对象*/
	
	private ArrayList<T> listObject;
	
	public RetInfoObject() {
		super();
	}
	public String getApplNo() {
		return applNo;
	}
	public void setApplNo(String applNo) {
		this.applNo = applNo;
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
	public List<T> getListObject() {
		return listObject;
	}
	public void setListObject(List<T> listObject) {
		this.listObject = (ArrayList<T>) listObject;
	}
}
