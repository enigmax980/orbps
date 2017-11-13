package com.newcore.orbpsutils.dao.api;


import com.newcore.orbps.models.taskprmy.PBatchErrLogNewPO;

/**
 * 批作业错误日志表
 * @author xushichao
 * 2017/2/6
 */
public interface PBatchErrLogNewDao {
	public boolean addBatchErrLogNew(PBatchErrLogNewPO batchErrLogNewPo);
}
