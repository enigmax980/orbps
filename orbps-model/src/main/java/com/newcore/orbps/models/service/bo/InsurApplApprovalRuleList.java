package com.newcore.orbps.models.service.bo;

import java.util.List;

public class InsurApplApprovalRuleList {
	
	/*字段名：执行结果代码，长度：1，说明：0：执行失败；1：新增成功；2：修改成功；3：复核成功；*/
	private String retCode;
	
	/*字段名：执行结果代码，长度：500，说明：IF retCode == 0 THEN 增加错误信息*/
	private String errMsg;

	List<InsurApplApprovalRule> insurApplApprovalRules;

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

	/**
	 * @return the insurApplApprovalRules
	 */
	public List<InsurApplApprovalRule> getInsurApplApprovalRules() {
		return insurApplApprovalRules;
	}

	/**
	 * @param insurApplApprovalRules the insurApplApprovalRules to set
	 */
	public void setInsurApplApprovalRules(List<InsurApplApprovalRule> insurApplApprovalRules) {
		this.insurApplApprovalRules = insurApplApprovalRules;
	}

	
}
