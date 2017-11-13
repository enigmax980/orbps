package com.newcore.orbpsutils.dao.impl;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbpsutils.dao.api.EarnestAccInfoDao;
@Repository("earnestAccInfoDao")
public class EarnestAccInfoDaoImpl implements EarnestAccInfoDao {
	private static Logger logger = LoggerFactory.getLogger(EarnestAccInfoDaoImpl.class);
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	/**
	 * 查询所以字段SQL
	 */
	private static final String TABLE_NAME="EARNEST_ACC_INFO";
	private static final String SELECT_ALL_DATA_SQL = "select * from EARNEST_ACC_INFO";
	
	@Override
	public EarnestAccInfo queryEarnestAccInfo(String accNo) {
		StringBuilder sql = new StringBuilder();
		sql.append(SELECT_ALL_DATA_SQL);
		sql.append(" WHERE ACC_NO ='");
		sql.append(accNo);
		sql.append("'");
		try {
			EarnestAccInfo accInfo = jdbcTemplate.queryForObject(sql.toString(), new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
			return accInfo;	
		} catch (EmptyResultDataAccessException e) {
			logger.info("【未查询到满足条件的账户信息】"+e);
			return null;
		}
	}

	@Override
	public long queryEarnestAccInfoAccId() {
		String sql ="select S_EARNEST_ACC_INFO.nextval from dual ";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}

	@Override
	public boolean insertEarnestAccInfo(EarnestAccInfo bigEarAcc) {
		SimpleJdbcInsert insert = DaoUtils.createJdbcInsert();
        insert.setTableName(TABLE_NAME);
        int i = insert.execute(DaoUtils.toColumnMap(bigEarAcc));
		return i>0;
	}

	@Override
	public boolean updateEarnestAccInfo(long accId, BigDecimal sumAmnt) {
		String sql = "update "+TABLE_NAME+" set BALANCE = ? where acc_Id=? ";
		int row = jdbcTemplate.update(sql,sumAmnt,accId);
		return row>0 ;
	}

	@Override
	public boolean updateFrozenBalance(String applNo) {
		Assert.notNull(applNo);
		
		StringBuilder sql = new StringBuilder();
		sql.append("update EARNEST_ACC_INFO set frozen_balance = 0");
		sql.append(" where acc_no like '");
		sql.append(applNo);
		sql.append("%'");
		
		int i = jdbcTemplate.update(sql.toString());
		
		return i>0;
	}

	@Override
	public boolean updateEarnestAccInfo(List<EarnestAccInfo> earList) {
		String sql =" update EARNEST_ACC_INFO set balance =?,frozen_balance=? where acc_no=? ";
		int[][] rows =jdbcTemplate.batchUpdate(sql, earList, earList.size() ,
		new ParameterizedPreparedStatementSetter<EarnestAccInfo>(){
			@Override
			public void setValues(PreparedStatement ps, EarnestAccInfo argument) throws SQLException {
				ps.setBigDecimal(1, argument.getBalance());
				ps.setBigDecimal(2, argument.getFrozenBalance());
				ps.setString(3, argument.getAccNo());
			}
		});
		return rows.length>0;
	}

	@Override
	public List<EarnestAccInfo> queryEarnestAccInfoList(String applNo) {
		Assert.notNull(applNo);
		
		StringBuilder sql = new StringBuilder();
		sql.append("select * from  EARNEST_ACC_INFO where acc_no like '");
		sql.append(applNo);
		sql.append("%'");
		
		List<EarnestAccInfo> list = jdbcTemplate.query(sql.toString(),
					new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		return list;
	}

}
