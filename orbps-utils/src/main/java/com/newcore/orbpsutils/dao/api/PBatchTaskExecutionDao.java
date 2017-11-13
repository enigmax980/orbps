package com.newcore.orbpsutils.dao.api;


import com.newcore.orbps.models.taskprmy.PBatchTaskExecutionPO;

/**
 * 批作业任务执行表Dao
 * @author xushichao
 * 2017/2/6
 */
public interface PBatchTaskExecutionDao {
	/**
	 * 添加
	 * @param batchTaskExecutionPo
	 * @return
	 */
	public boolean addBatchTaskExecution(PBatchTaskExecutionPO batchTaskExecutionPo);
	/**
	 * 更改
	 * @param batchTaskExecutionPo
	 * @return
	 */
	public boolean updateBatchTaskExecution(PBatchTaskExecutionPO batchTaskExecutionPo);
}
