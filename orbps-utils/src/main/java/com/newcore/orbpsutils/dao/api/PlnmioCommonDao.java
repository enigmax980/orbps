package com.newcore.orbpsutils.dao.api;

import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 收付费模块通用DAO
 * @author JCC
 * 2017年2月28日 16:56:56
 */
public interface PlnmioCommonDao {

	/**
	 * 修改产生应收付流水任务表状态
	 * @param 
	 * 	status 状态
	 * @param 
	 * 	taskId 主键
	 * @return
	 */
	boolean updateMoneyInCheckStatus(String status, String taskId);
	

	/**
	 * 修改保单为生效
	 * @param 
	 * 	applNo 投保单号
	 * @return
	 */
	public boolean updateMoneyInCheckInforce(String applNo);
	
	/**
	 * 修改产生应收付流水任务表打印日期
	 * @param 
	 * 	miologDate 打印日期
	 * @param 
	 * 	taskId  主键
	 * @return
	 */
	boolean updateMoneyInCheckExtKey(String miologDate, String taskId);

	/**
	 * 向实收付流转任务队列表插入数据
	 * @param branch 管理机构号
	 * @param batNo 批次号
	 * @return
	 */
	boolean insertMioInfoRoamTaskQueue(String branchNo,long batNo);

	/**
	 * 查询满足条件的应收付流水任务表信息
	 * @param status 状态
	 * @return
	 */
	SqlRowSet queryMioInfoRoamTaskQueue(String[] status);

	/**
	 * 修改应收付流水任务表数据状态
	 * @param taskSeq 主键
	 * @param status 状态 
	 * @return
	 */
	boolean updateMioInfoRoamTaskQueue(Long taskSeq, String status);

	/**
	 * 查询收费检查流程表信息
	 * @param status 状态
	 * @return
	 */
	SqlRowSet queryMoneyInCheckQueue(String[] status);

	/**
	 * 查询应收流水任务表数据
	 * @param status 状态
	 * @return
	 */
	SqlRowSet queryProcPlnmioTaskQueue(String status);

	/**
	 * 修改应收流水任务表数据
	 * @param status 状态
	 * @param taskId 主键
	 * @return
	 */
	boolean updateProcPlnmioTaskQueue(String status, String taskId);

}
