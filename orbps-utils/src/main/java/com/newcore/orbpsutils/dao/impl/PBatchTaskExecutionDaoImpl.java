package com.newcore.orbpsutils.dao.impl;


import com.newcore.orbps.models.taskprmy.PBatchTaskExecutionPO;
import com.newcore.orbpsutils.dao.api.PBatchTaskExecutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;


@Repository("pBatchTaskExecutionDao")
public class PBatchTaskExecutionDaoImpl implements PBatchTaskExecutionDao {
	
	/**
	 * 插入数据
	 */
	private static final String INSERT_BATCH_TASK_EXECUTION_SQL = "insert into BATCH_TASK_EXECUTION "
			+ "(TASK_EXECUTION_ID,TASK_ID,START_TIME,END_TIME) "
			+ "values(?,?,sysdate,null)";
	/**
	 * 更新结束时间
	 */
	private static final String UPDATE_BATCH_TASK_EXECUTION_SQL = "update BATCH_TASK_EXECUTION "
			+ "set END_TIME=sysdate where TASK_EXECUTION_ID=?";
	
	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public boolean addBatchTaskExecution(PBatchTaskExecutionPO batchTaskExecutionPo) {
		jdbcTemplate.update(INSERT_BATCH_TASK_EXECUTION_SQL, 
				batchTaskExecutionPo.getTaskExecutionId(),batchTaskExecutionPo.getTaskId());
		return true;
	}

	@Override
	public boolean updateBatchTaskExecution(PBatchTaskExecutionPO batchTaskExecutionPo) {
		jdbcTemplate.update(UPDATE_BATCH_TASK_EXECUTION_SQL, batchTaskExecutionPo.getTaskExecutionId());
		return true;
	}

}
