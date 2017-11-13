package com.newcore.orbpsutils.dao.api;

import com.newcore.orbps.models.taskprmy.PBatchTaskRegPO;

import java.util.List;


/**
 * 批作业任务注册表DAO
 * @author xushichao
 * 2017年2月6日
 */
public interface PBatchTaskRegDao {
	/**
	 * 添加数据
	 * @param batchTaskRegPo
	 * @return
	 */
	public boolean addBatchTaskReg(PBatchTaskRegPO batchTaskRegPo);
	/**
	 * 更改状态及最后一次更新时间
	 * @param batchTaskRegPo
	 * @return
	 */
	public boolean updateBatchTaskReg(PBatchTaskRegPO batchTaskRegPo);
	/**
	 * 依据任务id查询任务所有数据
	 * @param batchTaskRegPo
	 * @return
	 */
	public List<PBatchTaskRegPO> queryBatchTaskReg(PBatchTaskRegPO batchTaskRegPo);

    /**
     * 根据执行状态获取任务列表
     * @param status
     * @return
     */
	public List<PBatchTaskRegPO> queryBatchTaskRegsByStatus(String status);
}
