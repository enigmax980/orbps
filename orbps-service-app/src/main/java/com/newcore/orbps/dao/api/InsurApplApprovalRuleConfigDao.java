package com.newcore.orbps.dao.api;

import java.util.List;

import com.newcore.orbps.models.service.bo.InsurApplApprovalRule;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 契约规则管理Dao层接口	
 * @author Administrator
 *
 */
public interface InsurApplApprovalRuleConfigDao {

	/**
	 * 增加
	 * @param nsurApplApprovalRule
	 * @return
	 */
	public Integer addRule(InsurApplApprovalRule nsurApplApprovalRule);

	/**
	 * 查询
	 * @param id
	 * @return
	 */
	InsurApplApprovalRule queryRule(Integer id);

	/**
	 * @author huanglong
	 * @date 2016年12月25日
	 * @param String mgrBranchNo,String salesChannel,String ruleType,String cntrForm
	 * @return InsurRuleManger
	 * @content 根据 mgrBranchNo，salesChannel，ruleType,cntrForm查询规则
	 */
	public int selectForAdd(String mgrBranchNo,String salesChannel,String ruleType,String cntrForm);
	/**
	 * @author huanglong
	 * @date 2017年1月7日
	 * @param InsurApplApprovalRuleConfigDao
	 * @return List<InsurApplApprovalRule>
	 * @content 
	 */
	public List<InsurApplApprovalRule> selectForAdd(String mgrBranchNo,String ruleType,String cntrForm);
	/**
	 * @author huanglong
	 * @date 2017年1月16日
	 * @param InsurApplApprovalRuleConfigDao
	 * @return List<InsurApplApprovalRule>
	 * @content 
	 */
	public List<InsurApplApprovalRule> selectForAdd2(String mgrBranchNo,String salesChannel,String ruleType);
	/**
	 * @author huanglong
	 * @date 2017年1月16日
	 * @param InsurApplApprovalRuleConfigDao
	 * @return List<InsurApplApprovalRule>
	 * @content 
	 */
	public List<InsurApplApprovalRule> selectForAdd3(String mgrBranchNo,String ruleType);
	/**
	 * @author huanglong
	 * @date 2017年1月8日
	 * @param InsurApplApprovalRuleConfigDao
	 * @return InsurApplApprovalRule
	 * @content 
	 */
	public InsurApplApprovalRule selectForObject(String mgrBranchNo,String salesChannel,String ruleType,String cntrForm);
	/**
	 * 更新
	 * @param insurApplApprovalRule
	 * @return
	 */
	public Integer updateRule(InsurApplApprovalRule insurApplApprovalRule);

	/**
	 * 分页查
	 * @param pageQuery
	 * @return
	 */
	public List<InsurApplApprovalRule> getUserList(PageQuery<InsurApplApprovalRule> pageQuery);

	/**
	 * 获取总数
	 * @param pageQuery
	 * @return
	 */
	public long getTotalCount(PageQuery<InsurApplApprovalRule> pageQuery);

}
