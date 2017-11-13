package com.newcore.orbps.dao.api;

import com.newcore.orbps.models.bo.insurrulemanger.InsurRuleManger;

/**
 * @author wangxiao
 * 创建时间：2016年9月23日下午2:50:58
 */
@FunctionalInterface
public interface InsurRuleMangerDao {
	public InsurRuleManger select(String mgrBranchNo,String salesChannel,String ruleType,String cntrForm);
}
