package com.newcore.orbpsutils.dao.api;


import com.newcore.orbps.models.taskprmy.PBatchTaskExecutionDetailPO;

/**
 * 批作业任务执行明细表
 * @author xushichao
 * 2017/2/6
 */
public interface PBatchTaskExecutionDetailDao {
	public boolean addBatchTaskExecutionDetail(PBatchTaskExecutionDetailPO batchTaskExecutionDetailPo);
}
