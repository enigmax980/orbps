package com.newcore.orbps.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.dao.api.InsurRuleMangerDao;
import com.newcore.orbps.models.bo.insurrulemanger.InsurRuleManger;


/**
 * @author wangxiao
 * 创建时间：2016年9月23日下午3:30:49
 */
@Repository("insurRuleMangerDao")
public class InsurRuleMangerDaoImpl implements InsurRuleMangerDao {
	
	@Autowired
    JdbcOperations jdbcTemplate;
	
	/**
     * 日志管理工具实例.
     */
    private static Logger logger = LoggerFactory.getLogger(InsurRuleMangerDaoImpl.class);
	private static final String SELECT_SQL="SELECT * FROM INSUR_RULE_MANGER WHERE MGR_BRANCH_NO=? AND SALES_CHANNEL=? AND RULE_TYPE=? AND CNTR_FORM=? AND END_DATE >= to_date(?,'yyyy/MM/dd')";
	
	@Override
	public InsurRuleManger select(String mgrBranchNo, String salesChannel, String ruleType,String cntrForm) {
		Assert.notNull(mgrBranchNo);
		Assert.notNull(salesChannel);
		Assert.notNull(ruleType);
		Assert.notNull(cntrForm);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		String [] strArray = {mgrBranchNo,salesChannel,ruleType,cntrForm,dateFormat.format(new Date())};
		InsurRuleManger insurRuleManger = null;
		try {
			insurRuleManger = jdbcTemplate.queryForObject(SELECT_SQL,strArray,DaoUtils.createRowMapper(InsurRuleManger.class));			
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return insurRuleManger;
	}
}
