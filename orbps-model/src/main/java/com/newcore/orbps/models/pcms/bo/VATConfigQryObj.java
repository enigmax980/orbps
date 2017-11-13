package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
/**
 * 营改增配置信息查询服务 入参子对象
 * @author xushichao
 * 2016年11月28日
 */
public class VATConfigQryObj implements Serializable {

	
	/*字段名称:,长度:,是否必填:否*/
	private static final long serialVersionUID = -3484353786195947400L;

	@NotBlank(message="[到账确认标识不能为空]")
	@Length(max=1,message="[到账确认标识长度不能大于1位]")
	private String conAccflag;//   到账确认标识
	
	@NotBlank(message="[险种号不能为空]")
	@Length(max=8,message="[险种号长度不能大于8位]")
	private String polCode;//      险种号
	
	@NotBlank(message="[系统号不能为空]")
	@Length(max=1,message="[系统号长度不能大于1位]")
	private String sysNo;//        系统号
	
	@NotBlank(message="[分机构号不能为空]")
	@Length(max=6,message="[分机构号长度不能大于6位]")
	private String branchNo;//     分机构号
	
	@NotBlank(message="[收付项目代码不能为空]")
	@Length(max=2,message="[收付项目代码长度不能大于2位]")
	private String mioItemCode;//  收付项目代码
	
	@NotBlank(message="[收付款方式代码不能为空]")
	@Length(max=1,message="[收付款方式代码长度不能大于1位]")
	private String mioTypeCode;//  收付款方式代码
	
	@NotBlank(message="[收付款方式类别不能为空]")
	@Length(max=1,message="[收付款方式类别长度不能大于1位]")
	private String sourcePayType;//收付款方式类别
	
	@NotNull(message="[收付款项目分类不能为空]")
	private Long itemCodeType;// 收付款项目分类
	
	@NotNull(message="[收付款方式分类不能为空]")
	private Long typeCodeType;// 收付款方式分类
	
	private String dataSource;
	
	public String getConAccflag() {
		return conAccflag;
	}
	public void setConAccflag(String conAccflag) {
		this.conAccflag = conAccflag;
	}
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public String getSysNo() {
		return sysNo;
	}
	public void setSysNo(String sysNo) {
		this.sysNo = sysNo;
	}
	public String getBranchNo() {
		return branchNo;
	}
	public void setBranchNo(String branchNo) {
		this.branchNo = branchNo;
	}
	public String getMioTypeCode() {
		return mioTypeCode;
	}
	public void setMioTypeCode(String mioTypeCode) {
		this.mioTypeCode = mioTypeCode;
	}
	public String getSourcePayType() {
		return sourcePayType;
	}
	public void setSourcePayType(String sourcePayType) {
		this.sourcePayType = sourcePayType;
	}
	public Long getItemCodeType() {
		return itemCodeType;
	}
	public void setItemCodeType(Long itemCodeType) {
		this.itemCodeType = itemCodeType;
	}
	public Long getTypeCodeType() {
		return typeCodeType;
	}
	public void setTypeCodeType(Long typeCodeType) {
		this.typeCodeType = typeCodeType;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getMioItemCode() {
		return mioItemCode;
	}
	public void setMioItemCode(String mioItemCode) {
		this.mioItemCode = mioItemCode;
	}
	@Override
	public String toString() {
		return "VATConfigQryObj [conAccflag=" + conAccflag + ", polCode=" + polCode + ", sysNo=" + sysNo + ", branchNo="
				+ branchNo + ", mioItemCode=" + mioItemCode + ", mioTypeCode=" + mioTypeCode + ", sourcePayType="
				+ sourcePayType + ", itemCodeType=" + itemCodeType + ", typeCodeType=" + typeCodeType + ", dataSource="
				+ dataSource + "]";
	}
	
}
