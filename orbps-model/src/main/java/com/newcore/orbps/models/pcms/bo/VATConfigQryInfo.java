package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
/**
 * 营改增配置信息查询服务入参对象
 * @author xushichao
 * 2016年11月28日
 */
public class VATConfigQryInfo implements Serializable {

	private static final long serialVersionUID = 6963468001963021232L;
	/**
	 * 管理机构号
	 */
	@NotBlank(message="[管理机构号不能为空]")
	@Length(max=6,message="[管理机构号长度不能大于6位]")
	private String mgrBranchNo;
	/**
	 * 营改增查询入参子对象
	 */
	private List<VATConfigQryObj> vATConfigQryList;
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public List<VATConfigQryObj> getvATConfigQryList() {
		return vATConfigQryList;
	}
	public void setvATConfigQryList(List<VATConfigQryObj> vATConfigQryList) {
		this.vATConfigQryList = vATConfigQryList;
	}
}
