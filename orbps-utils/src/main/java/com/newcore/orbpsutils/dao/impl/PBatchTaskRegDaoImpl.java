package com.newcore.orbpsutils.dao.impl;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;

import com.newcore.orbps.models.taskprmy.PBatchTaskRegPO;
import com.newcore.orbpsutils.dao.api.PBatchTaskRegDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;


@Repository("pBatchTaskRegDao")
public class PBatchTaskRegDaoImpl implements PBatchTaskRegDao {
	/**
	 * 插入数据
	 */
	private static final String INSERT_BATCH_TASK_REG_SQL = "insert into BATCH_TASK_REG "
			+ "(TASK_ID,BAT_TX_NO,DATA_DEAL_STATUS,CREATE_TIME,UPDATE_TIMESTAMP) "
			+ "values(?,?,?,sysdate,systimestamp)";
	/**
	 * 更改最后一次更新时间
	 */
	private static final String UPDATE_BATCH_TASK_REG_SQL = "update BATCH_TASK_REG "
			+ "set DATA_DEAL_STATUS=?,UPDATE_TIMESTAMP=systimestamp where task_id=?";
	private static final String QUERY_BATCH_TASK_REG_SQL = "select * from BATCH_TASK_REG "
			+ "where task_id=?";

	private static final String BATCH_TASK_REG_BY_STATUS_SQL = "SELECT * FROM BATCH_TASK_REG WHERE DATA_DEAL_STATUS = ?";
	
	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public boolean addBatchTaskReg(PBatchTaskRegPO batchTaskRegPo) {
		jdbcTemplate.update(INSERT_BATCH_TASK_REG_SQL, batchTaskRegPo.getTaskId(),
				batchTaskRegPo.getBatTxNo(),batchTaskRegPo.getDataDealStatus());
		return true;
	}
	
	@Override
	public boolean updateBatchTaskReg(PBatchTaskRegPO batchTaskRegPo) {
		jdbcTemplate.update(UPDATE_BATCH_TASK_REG_SQL,
				batchTaskRegPo.getDataDealStatus(),batchTaskRegPo.getTaskId());
		return true;
	}

	@Override
	public List<PBatchTaskRegPO> queryBatchTaskReg(PBatchTaskRegPO batchTaskRegPo) {
		return jdbcTemplate.query(QUERY_BATCH_TASK_REG_SQL,
				new CustomBeanPropertyRowMapper<PBatchTaskRegPO>(PBatchTaskRegPO.class),
				batchTaskRegPo.getTaskId());
	}

	@Override
	public List<PBatchTaskRegPO> queryBatchTaskRegsByStatus(String status) {
        Assert.notNull(status);
        List<PBatchTaskRegPO> resultList = jdbcTemplate.query(BATCH_TASK_REG_BY_STATUS_SQL,
                new CustomBeanPropertyRowMapper<PBatchTaskRegPO>(PBatchTaskRegPO.class), status);

        return resultList;
	}

}
