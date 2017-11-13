package com.newcore.orbpsutils.dao.impl;


import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.taskprmy.PBatchErrLogNewPO;
import com.newcore.orbpsutils.dao.api.PBatchErrLogNewDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;


@Repository("pBatchErrLogNewDao")
public class PBatchErrLogNewDaoImpl implements PBatchErrLogNewDao {
	
	/**
	 * 插入数据
	 */
	private static final String INSERT_BATCH_ERR_LOG_SQL = "insert into BATCH_ERR_LOG "
			+ "(PKID,TASK_ID,TASK_EXECUTION_ID,JOB_INSTANCE_ID,JOB_EXECUTION_ID,BUSINESS_TYPE,BUSINESS_KEY,ERROR_CODE,ERROR_INFO,ERROR_DETAIL,CREATE_DATE) "
			+ "values(?,?,?,?,?,?,?,?,?,?,sysdate)";
	
	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public boolean addBatchErrLogNew(PBatchErrLogNewPO batchErrLogNewPo) {
		String errorInfo=batchErrLogNewPo.getErrorInfo();
		if(!StringUtils.isEmpty(errorInfo) && errorInfo.length()>200)errorInfo=errorInfo.substring(0, 200);
		jdbcTemplate.update(INSERT_BATCH_ERR_LOG_SQL,
				batchErrLogNewPo.getPkid(),batchErrLogNewPo.getTaskId(),
				batchErrLogNewPo.getTaskExecutionId(),batchErrLogNewPo.getJobInstanceId(),
				batchErrLogNewPo.getJobExecutionId(),batchErrLogNewPo.getBusinessType(),
				batchErrLogNewPo.getBusinessKey(),batchErrLogNewPo.getErrorCode(),
				errorInfo,batchErrLogNewPo.getErrorInfo());
		return true;
	}

}
