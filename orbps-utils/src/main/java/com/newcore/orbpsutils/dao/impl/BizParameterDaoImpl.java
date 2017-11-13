package com.newcore.orbpsutils.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.newcore.orbpsutils.dao.api.BizParameterDao;

/**
 * 系统参数配置表
 * @author JCC
 * 2017年5月17日 14:26:31
 */
@Repository("bizParameterDao")
public class BizParameterDaoImpl implements BizParameterDao{

    @Autowired
    JdbcOperations jdbcTemplate;
	
	@Override
	public String findBizParam(String bizParamCode) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select BIZ_PARAM_VAL from biz_parameter t ");
		sql.append(" where SYS_TYPE='#' and MRG_BRANCH_GRP='!!!!!!' ");
		sql.append(" and BIZ_PARAM_CODE ='"+bizParamCode+"'");
		return jdbcTemplate.queryForObject(sql.toString(), String.class);
	}

}
