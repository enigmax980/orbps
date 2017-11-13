package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @author zhanghui
 *
 * @date create on  2016年9月7日下午2:49:02
 */
public class GuaranteeRateReturnBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3098487207052811018L;
	/**
	 * 执行结果
	 */
	private String  retCode;
	/**
	 * 错误信息
	 */
	private String  errMsg;
	/**
	 * 组合保单号
	 */
	private String  cgNo;
	/**
	 * 帽子入库标志
	 */
	private String  cntrFlag;
	/**
	 * 清单入库标志
	 */
	private String  listFlag;
	/**
	 * 转保费完成标志
	 */
	private String  mioFlag;
	/**
	 * 回执核销完成标志
	 */
	private String  cvFlag;
	/**
	 * 查询时间
	 */
	private Date  createTime;
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
	public String getCgNo() {
		return cgNo;
	}
	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}
	public String getCntrFlag() {
		return cntrFlag;
	}
	public void setCntrFlag(String cntrFlag) {
		this.cntrFlag = cntrFlag;
	}
	public String getListFlag() {
		return listFlag;
	}
	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}
	public String getMioFlag() {
		return mioFlag;
	}
	public void setMioFlag(String mioFlag) {
		this.mioFlag = mioFlag;
	}
	public String getCvFlag() {
		return cvFlag;
	}
	public void setCvFlag(String cvFlag) {
		this.cvFlag = cvFlag;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
