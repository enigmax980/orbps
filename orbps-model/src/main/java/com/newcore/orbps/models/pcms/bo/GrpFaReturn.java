package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
/**
 * 暂交费还回服务 调用参数bo
 * @author xushichao
 *
 */
public class GrpFaReturn implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2957846480240913617L;

	/**
	 * 投保单号
	 */
	@NotNull(message="[投保单号不能为空]")
	@Length(max=16,message="[投保单号长度不能大于16位]")
	private String applNo;
	
	/**
	 * 管理机构号
	 */
	@NotNull(message="[管理机构号不能为空]")
	@Length(max=6,message="[管理机构号长度不能大于6位]")
	private String mgrBranchNo;
	
	/**
	 * 主险代码
	 */
	@NotNull(message="[主险代码不能为空]")
	@Length(max=8,message="[主险代码长度不能大于8位]")
	private String polCode;
	
	/**
	 * 总保费
	 */
	@NotNull(message="[总保费不能为空]")
	private Double amnt;
	
	/**
	 * 动态路由指定
	 */
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
	public String getPolCode() {
		return polCode;
	}
	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}
	public Double getAmnt() {
		return amnt;
	}
	public void setAmnt(Double amnt) {
		this.amnt = amnt;
	}
	@Override
	public String toString() {
		return "GrpFaReturn [applNo=" + applNo + ", mgrBranchNo=" + mgrBranchNo + ", polCode=" + polCode + ", amnt="
				+ amnt + "]";
	}
}
