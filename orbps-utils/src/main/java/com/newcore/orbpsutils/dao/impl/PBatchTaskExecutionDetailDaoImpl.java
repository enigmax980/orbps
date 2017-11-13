package com.newcore.orbpsutils.dao.impl;


import com.newcore.orbps.models.taskprmy.PBatchTaskExecutionDetailPO;
import com.newcore.orbpsutils.dao.api.PBatchTaskExecutionDetailDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;


@Repository("pBatchTaskExecutionDetailDao")
public class PBatchTaskExecutionDetailDaoImpl implements PBatchTaskExecutionDetailDao {
	
	/**
	 * 插入数据
	 */
	private static final String INSERT_BATCH_TASK_EXECUTION_SQL = "insert into BATCH_TASK_EXECUTION_DETAIL "
			+ "(pkid,task_execution_id,job_instance_id,job_execution_id,job_name) "
			+ "values(?,?,?,?,?)";
	
	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public boolean addBatchTaskExecutionDetail(PBatchTaskExecutionDetailPO batchTaskExecutionDetailPo) {
		jdbcTemplate.update(INSERT_BATCH_TASK_EXECUTION_SQL, batchTaskExecutionDetailPo.getPkId(),
				batchTaskExecutionDetailPo.getTaskExecutionId(),batchTaskExecutionDetailPo.getJobInstanceId(),
				batchTaskExecutionDetailPo.getJobExecutionId(),batchTaskExecutionDetailPo.getJobName());
		return true;
	}

}
