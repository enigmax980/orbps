package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.newcore.orbps.models.banktrans.MioLog;


public class FinQueueData implements Serializable{

	private static final long serialVersionUID = 5106485440933724572L;

	/** 字段名：投保单号，长度：16，是否必填：是 **/
	@NotNull(message="[投保单号不能为空]")
	@Length(max=16,message="[投保单号长度不能大于16位]") 
	private String applNo;
	
	/**字段名：管理机构号，长度：6，是否必传：是**/
	@NotNull(message="[管理机构号不能为空]")
	@Length(max=6,message="[管理机构号长度不能大于6位]")
	private String mgrBranchNo;
	
	/**字段名：合同组号，长度：25，是否必传：是**/
	@NotNull(message="[合同组号不能为空]")
	@Length(max=25,message="[合同组号长度不能大于25位]")
	private String cgNo;
	
    /**险种集合**/
	@NotNull(message="[险种集合不能为空]")
	private List<String> polCodeList;
	
	 /**实收付流水数组**/
	@NotNull(message="[实收付流水数组不能为空]")
	private List<MioLog> mioLogList;
	
	 /**转财务接口数据数组**/
	@NotNull(message="[转财务接口数据数组不能为空]")
	private List<FinTaskData> finTaskDataList;

	private String dataSource;
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public String getCgNo() {
		return cgNo;
	}

	public void setCgNo(String cgNo) {
		this.cgNo = cgNo;
	}

	public List<String> getPolCodeList() {
		return polCodeList;
	}

	public void setPolCodeList(List<String> polCodeList) {
		this.polCodeList = polCodeList;
	}

	public List<MioLog> getMioLogList() {
		return mioLogList;
	}

	public void setMioLogList(List<MioLog> mioLogList) {
		this.mioLogList = mioLogList;
	}

	public List<FinTaskData> getFinTaskDataList() {
		return finTaskDataList;
	}

	public void setFinTaskDataList(List<FinTaskData> finTaskDataList) {
		this.finTaskDataList = finTaskDataList;
	}
}
