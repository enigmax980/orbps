package com.newcore.orbpsutils.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbpsutils.dao.api.ProductDao;
import com.newcore.orbps.models.service.bo.Policy;
import com.newcore.supports.service.api.PageQueryService;

/**
 * 
 * @author zhoushoubo
 *
 */
@Repository("productDao")
public class ProductDaoImpl implements ProductDao {
	
	private static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

	private static final String QUERY_SQL_PRODUCT  = "SELECT SALES_TAGET FROM PRODUCT WHERE PRODUCT_CODE=?";
	
	private static final String QUERY_SQL_ULTRASHORT_PREM  = "SELECT COUNT(*) FROM ULTRASHORT_PREM WHERE POL_CODE like ?"
			+ " AND INSUR_DUR_UNIT=? AND INSUR_DUR_FROM<=? AND INSUR_DUR_TO>=?" ;
	
	private static final String QUERY_SQL_POLICY  = "SELECT * FROM POLICY WHERE POL_CODE=?";
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
	 * 
	 */
	@Override
	public String getSalesTaget(String productCode) {
		Assert.notNull(productCode);
		String [] sarr = {productCode};
		String salesTaget = null;
		try {
			salesTaget = jdbcTemplate.queryForObject(QUERY_SQL_PRODUCT, sarr, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error("查询结果不存在",e);
		}
		return salesTaget;
	}

	/**
	 * 
	 */
	@Override
	public int getInsurDurCnt(String polCode,Integer insurDur,String insurDurUnit) {
		Assert.notNull(polCode);
		Assert.notNull(insurDur);
		Assert.notNull(insurDurUnit);
		Object [] sarr = {polCode+"%",insurDurUnit,insurDur,insurDur};
		int cnt = jdbcTemplate.queryForObject(QUERY_SQL_ULTRASHORT_PREM, sarr, Integer.class);
		return cnt;
	}

	/**
	 * 
	 */
	@Override
	public Policy getPolicyInfo(String polCode) {
		Assert.notNull(polCode);
		String [] sarr = {polCode};
		Policy policy = null;
		try {
			policy = jdbcTemplate.queryForObject(QUERY_SQL_POLICY, sarr, DaoUtils.createRowMapper(Policy.class));
		} catch (EmptyResultDataAccessException e) {
			logger.error("查询结果不存在",e);
		}
		return policy;
	}
}
