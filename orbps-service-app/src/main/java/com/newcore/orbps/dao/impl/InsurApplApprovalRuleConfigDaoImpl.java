package com.newcore.orbps.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.dao.api.InsurApplApprovalRuleConfigDao;
import com.newcore.orbps.models.service.bo.InsurApplApprovalRule;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.service.api.PageQueryService;

@Repository("insurApplApprovalRuleConfigDaoImpl")
public class InsurApplApprovalRuleConfigDaoImpl implements
InsurApplApprovalRuleConfigDao {

	private static Logger logger = LoggerFactory
			.getLogger(InsurApplApprovalRuleConfigDaoImpl.class);

	private static final String INSERT_SQL = "INSERT INTO INSUR_RULE_MANGER (MGR_BRANCH_NO,SALES_CHANNEL,SALES_BRANCH_NO,SCENE,CNTR_FORM,PRODUCT_TYPE,AUTO_APPROVE_FLAG,ARTIFICIAL_APPROVE_FLAG,BEFORE_EFFECTIVE_DATE,AFTER_EFFECTIVE_DATE,EFFECTIVE_DATE_BACK_ACROSS,RULE_TYPE,RULE_CHANGE_REASON,RULE_STATE,RULE_NAME,START_DATE,END_DATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	private static final String UPDATE_SQL = "UPDATE INSUR_RULE_MANGER SET MGR_BRANCH_NO = ?,SALES_CHANNEL = ?,SALES_BRANCH_NO = ?,SCENE = ?,CNTR_FORM = ?,PRODUCT_TYPE = ?,AUTO_APPROVE_FLAG = ?,ARTIFICIAL_APPROVE_FLAG = ?,BEFORE_EFFECTIVE_DATE = ?,AFTER_EFFECTIVE_DATE = ?,EFFECTIVE_DATE_BACK_ACROSS = ?,RULE_TYPE = ?,RULE_CHANGE_REASON = ?,RULE_NAME = ?,START_DATE = ?,END_DATE = ? WHERE MGR_BRANCH_NO=? AND SALES_CHANNEL=? AND RULE_TYPE=? AND CNTR_FORM=?";

	private static final String PAGE_SQL = "SELECT ID,MGR_BRANCH_NO,SALES_CHANNEL,SALES_BRANCH_NO,SCENE,CNTR_FORM,PRODUCT_TYPE,AUTO_APPROVE_FLAG,ARTIFICIAL_APPROVE_FLAG,BEFORE_EFFECTIVE_DATE,AFTER_EFFECTIVE_DATE,EFFECTIVE_DATE_BACK_ACROSS,RULE_TYPE,RULE_CHANGE_REASON,RULE_STATE,RULE_NAME,START_DATE,END_DATE FROM INSUR_RULE_MANGER";

	private static final String COUNT_SQL = "SELECT COUNT(*) FROM INSUR_RULE_MANGER";

	private static final String SELECTFORADD_SQL="SELECT COUNT(*) FROM INSUR_RULE_MANGER WHERE MGR_BRANCH_NO=? AND SALES_CHANNEL=? AND RULE_TYPE=? AND CNTR_FORM=?";	

	private static final String SELECTFORADD_SQL2="SELECT * FROM INSUR_RULE_MANGER WHERE MGR_BRANCH_NO=? AND RULE_TYPE=? AND CNTR_FORM=?";	

	private static final String SELECTFORADD_SQL3="SELECT * FROM INSUR_RULE_MANGER WHERE MGR_BRANCH_NO=? AND SALES_CHANNEL=? AND RULE_TYPE=?";	

	private static final String SELECTFORADD_SQL4="SELECT * FROM INSUR_RULE_MANGER WHERE MGR_BRANCH_NO=? AND RULE_TYPE=?";	

	private static final String SELECTFOROBJECT_SQL="SELECT * FROM INSUR_RULE_MANGER WHERE MGR_BRANCH_NO=? AND SALES_CHANNEL=? AND RULE_TYPE=? AND CNTR_FORM=?";

	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	/**
	 * 分页辅助工具
	 */
	@Autowired
	PageQueryService pageQueryService;

	/**
	 * 更新契约规则
	 */
	@Override
	public Integer addRule(InsurApplApprovalRule insurApplApprovalRule) {
		// 新增
		Integer sqlRet = jdbcTemplate.update(INSERT_SQL,
				insurApplApprovalRule.getMgrBranchNo(),
				insurApplApprovalRule.getSalesChannel(),
				insurApplApprovalRule.getSalesBranchNo(),
				insurApplApprovalRule.getScene(),
				insurApplApprovalRule.getCntrForm(),
				insurApplApprovalRule.getProductType(),
				insurApplApprovalRule.getAutoApproveFlag(),
				insurApplApprovalRule.getArtificialApproveFlag(),
				insurApplApprovalRule.getBeforeEffectiveDate(),
				insurApplApprovalRule.getAfterEffectiveDate(),
				insurApplApprovalRule.getEffectiveDateBackAcross(),
				insurApplApprovalRule.getRuleType(),
				insurApplApprovalRule.getRuleChangeReason(),
				insurApplApprovalRule.getRuleState(),
				insurApplApprovalRule.getRuleName(),
				insurApplApprovalRule.getStartDate(),
				insurApplApprovalRule.getEndDate());
		logger.info(sqlRet.toString());
		return sqlRet;
	}

	/**
	 * 根据ID查询契约规则
	 */
	@Override
	public InsurApplApprovalRule queryRule(Integer id) {
		InsurApplApprovalRule insurApplApprovalRule = (InsurApplApprovalRule) jdbcTemplate
				.queryForObject(
						"SELECT ID,MGR_BRANCH_NO,SALES_CHANNEL,SALES_BRANCH_NO,SCENE,CNTR_FORM,PRODUCT_TYPE,AUTO_APPROVE_FLAG,ARTIFICIAL_APPROVE_FLAG,BEFORE_EFFECTIVE_DATE,AFTER_EFFECTIVE_DATE,EFFECTIVE_DATE_BACK_ACROSS,RULE_TYPE,RULE_CHANGE_REASON,RULE_STATE,RULE_NAME,START_DATE,END_DATE FROM INSUR_RULE_MANGER WHERE ID = "
								+ id,
								DaoUtils.createRowMapper(InsurApplApprovalRule.class));
		return insurApplApprovalRule;
	}

	/**
	 * 更新契约规则
	 */
	@Override
	public Integer updateRule(InsurApplApprovalRule insurApplApprovalRule) {
		Integer sqlRet = jdbcTemplate.update(UPDATE_SQL,
				insurApplApprovalRule.getMgrBranchNo(),
				insurApplApprovalRule.getSalesChannel(),
				insurApplApprovalRule.getSalesBranchNo(),
				insurApplApprovalRule.getScene(),
				insurApplApprovalRule.getCntrForm(),
				insurApplApprovalRule.getProductType(),
				insurApplApprovalRule.getAutoApproveFlag(),
				insurApplApprovalRule.getArtificialApproveFlag(),
				insurApplApprovalRule.getBeforeEffectiveDate(),
				insurApplApprovalRule.getAfterEffectiveDate(),
				insurApplApprovalRule.getEffectiveDateBackAcross(),
				insurApplApprovalRule.getRuleType(),
				insurApplApprovalRule.getRuleChangeReason(),
				insurApplApprovalRule.getRuleName(),
				insurApplApprovalRule.getStartDate(),
				insurApplApprovalRule.getEndDate(),
				insurApplApprovalRule.getMgrBranchNo(),
				insurApplApprovalRule.getSalesChannel(),
				insurApplApprovalRule.getRuleType(),
				insurApplApprovalRule.getCntrForm()
				);
		return sqlRet;
	}

	/**
	 * 分页获取契约规则
	 */
	@Override
	public List<InsurApplApprovalRule> getUserList(
			PageQuery<InsurApplApprovalRule> pageQuery) {
		StringBuilder sql = new StringBuilder();
		sql.append(" WHERE 1 = 1");
		if (pageQuery.getCondition() != null) {
			if (pageQuery.getCondition().getMgrBranchNo() != null
					&& pageQuery.getCondition().getMgrBranchNo().length() != 0) {
				sql.append(" AND mgr_branch_no ="
						+ pageQuery.getCondition().getMgrBranchNo());
			}
			if (pageQuery.getCondition().getRuleType() != null
					&& pageQuery.getCondition().getRuleType().length() != 0) {
				sql.append(" AND rule_type = "
						+ pageQuery.getCondition().getRuleType());
			}
			if (pageQuery.getCondition().getRuleState() != null
					&& pageQuery.getCondition().getRuleState().length() != 0) {
				sql.append(" AND rule_state ="
						+ pageQuery.getCondition().getRuleState());
			}
		}
		String pageSql = pageQueryService.buildPageQuerySql(PAGE_SQL + sql,
				pageQuery);
		return jdbcTemplate.query(pageSql,
				new CustomBeanPropertyRowMapper<InsurApplApprovalRule>(
						InsurApplApprovalRule.class));
	}

	/**
	 * 获取总数
	 */
	@Override
	public long getTotalCount(PageQuery<InsurApplApprovalRule> pageQuery) {
		StringBuilder sql = new StringBuilder();
		sql.append(" WHERE 1 = 1");
		if (pageQuery.getCondition() != null) {
			if (pageQuery.getCondition().getMgrBranchNo() != null
					&& pageQuery.getCondition().getMgrBranchNo().length() != 0) {
				sql.append(" AND mgr_branch_no ="
						+ pageQuery.getCondition().getMgrBranchNo());
			}
			if (pageQuery.getCondition().getRuleType() != null
					&& pageQuery.getCondition().getRuleType().length() != 0) {
				sql.append(" AND rule_type = "
						+ pageQuery.getCondition().getRuleType());
			}
			if (pageQuery.getCondition().getRuleState() != null
					&& pageQuery.getCondition().getRuleState().length() != 0) {
				sql.append(" AND rule_state ="
						+ pageQuery.getCondition().getRuleState());
			}
		}
		long sqlCount = jdbcTemplate
				.queryForObject(COUNT_SQL + sql, Long.class);
		return sqlCount;
	}

	@Override
	public int selectForAdd(String mgrBranchNo, String salesChannel, String ruleType,String cntrForm) {
		Assert.notNull(mgrBranchNo);
		Assert.notNull(salesChannel);
		Assert.notNull(ruleType);
		Assert.notNull(cntrForm);
		String [] strArray = {mgrBranchNo,salesChannel,ruleType,cntrForm};
		return jdbcTemplate.queryForObject(SELECTFORADD_SQL,strArray, Integer.class);
	}

	@Override
	public List<InsurApplApprovalRule> selectForAdd(String mgrBranchNo, String ruleType, String cntrForm) {
		Assert.notNull(mgrBranchNo);
		Assert.notNull(ruleType);
		Assert.notNull(cntrForm);
		String [] strArray = {mgrBranchNo,ruleType,cntrForm};
		return jdbcTemplate.query(SELECTFORADD_SQL2,strArray, new CustomBeanPropertyRowMapper<InsurApplApprovalRule>(InsurApplApprovalRule.class));
	}

	@Override
	public InsurApplApprovalRule selectForObject(String mgrBranchNo, String salesChannel, String ruleType,
			String cntrForm) {
		Assert.notNull(mgrBranchNo);
		Assert.notNull(salesChannel);
		Assert.notNull(ruleType);
		Assert.notNull(cntrForm);
		String [] strArray = {mgrBranchNo,salesChannel,ruleType,cntrForm};
		InsurApplApprovalRule insurApplApprovalRule = null;
		try {
			insurApplApprovalRule = jdbcTemplate.queryForObject(SELECTFOROBJECT_SQL,strArray,DaoUtils.createRowMapper(InsurApplApprovalRule.class));			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return insurApplApprovalRule;
	}

	@Override
	public List<InsurApplApprovalRule> selectForAdd2(String mgrBranchNo, String salesChannel, String ruleType) {
		Assert.notNull(mgrBranchNo);
		Assert.notNull(salesChannel);
		Assert.notNull(ruleType);
		String [] strArray = {mgrBranchNo,salesChannel,ruleType};
		return jdbcTemplate.query(SELECTFORADD_SQL3,strArray, new CustomBeanPropertyRowMapper<InsurApplApprovalRule>(InsurApplApprovalRule.class));
	}

	@Override
	public List<InsurApplApprovalRule> selectForAdd3(String mgrBranchNo, String ruleType) {
		Assert.notNull(mgrBranchNo);
		Assert.notNull(ruleType);
		String [] strArray = {mgrBranchNo,ruleType};
		return jdbcTemplate.query(SELECTFORADD_SQL4,strArray, new CustomBeanPropertyRowMapper<InsurApplApprovalRule>(InsurApplApprovalRule.class));
	}
}
