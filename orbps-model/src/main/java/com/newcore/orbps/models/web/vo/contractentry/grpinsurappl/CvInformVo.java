package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CvInformVo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7180961722637276000L;
	/*
	 * 投保单号
	 */
	@Size(min = 16,max = 16 ,message = "投保单号长度必须是16位.")
	private String applNo;
	/*
	 * 合同组号
	 */
	@Size(min = 22,max = 22 ,message = "合同组号长度必须是22位")
	private String cgNo;
	/*
	 * 合同号
	 */
	@Size(min = 22,max = 22 ,message = "合同号长度必须是22位")
	private String cntrNo;
	/*
	 * 汇缴件号
	 */
	private String sgNo;
	/*
	 * 签收日期
	 */
	@NotNull(message="签收日期不允许为空")
    private  Date signDate;
    /*
     * 签收方式
     */
   @Pattern(regexp = "[123]",message = "签收方式不符合规范，目前支持1 电子签收,  2, 纸质签收, 3, 不签收")
   @Size(min = 1,max = 1 ,message = "签收方式长度不能超过一位")
    private String signType;
    
    
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
	public String getCntrNo() {
		return cntrNo;
	}
	public void setCntrNo(String cntrNo) {
		this.cntrNo = cntrNo;
	}
	public String getSgNo() {
		return sgNo;
	}
	public void setSgNo(String sgNo) {
		this.sgNo = sgNo;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
    
    
}
