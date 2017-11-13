package com.newcore.orbps.dao.api;

import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.supports.models.service.bo.PageQuery;

import java.util.List;

public interface TaskPrmyDao{	
	/**
	 * 根据任务状态、任务列队名称查询任务队列接口
	 * @param taskTable 任务列队名称
	 * @param status 任务状态
	 * @return List<TaskPrmyInfo> 任务队列实体类
	 */
	public List<TaskPrmyInfo> queryTaskPrmyInfo(PageQuery<TaskPrmyInfo> pageQuery);
	/**
	 * 根据任务ID查询任务队列表的任务状态、任务完成时间和执行次数
	 * @param i 任务ID
	 * @param status 任务状态
	 * @param endTime 任务完成时间
	 * @param askTimes 执行次数
	 * @return boolean true:成功，false:失败
	 * @throws Exception
	 */
	public boolean updateTaskPrmyInfoBytaskSeq(String taskTable, TaskPrmyInfo taskPrmyInfo);
	/**
	 * 根据任务列队名称增加任务队列表信息
	 * @param taskPrmyInfo	
	 * @return
	 */
	public boolean insertTaskPrmyInfoByTaskSeq(String taskTable,TaskPrmyInfo taskPrmyInfo);
	/**
	 * 查询状态为N的任务队列表信息数量
	 * TaskPrmyDao
	 * int
	 */
	public int countForStatusIsN(String taskTable);
	/**
	 * TaskPrmyDao
	 * List<TaskPrmyInfo>
	 */
	List<TaskPrmyInfo> queryTaskPrmyInfoByStatus(String taskTable, String status);
	
	/**
	 * TaskPrmyDao
	 * List<TaskPrmyInfo>
	 */
	TaskPrmyInfo queryTaskPrmyInfoByTaskSeq(String taskTable, Long taskSeq);

	public Long getTaskSeq(String tableName);
	
	public List<Long> queryJobExcutionIdList(String taskTable);
	
	/**
	 * 根据投保单号查询运行中的任务执行ID
	 * @param taskTable
	 * @param applNo
	 * @return
	 */
	public List<Long> queryJobExcutionIdListByApplno(String taskTable, String applNo);
	
	/**
	 * 根据taskseq更新任务表的执行ID
	 * @param tbl
	 * @param taskSeq
	 * @param excuId
	 * @return
	 */
	public boolean updateExcuIdByTaskSeq(String tbl, Long taskSeq, Long excuId);
	
	/**
	 * 根据投保单号更新任务表的状态从K改为E
	 * @param tbl
	 * @param applNo
	 * @return
	 */
	public boolean updateStatusByApplNo(String tbl, String applNo);

	/**
	 * 对批作业执行ID进行过滤，获取正在执行的批作业ID
	 * @param executionIdSet
	 * @return
	 */
    public List<Long> selRuningExexuions(List<Long> executionIds);

	/**
	 * 查询正在停止的批作业数量
	 * @param executionIdSet
	 * @return
	 */
    public Long selStoppingExexuions(List<Long> executionIds);

    /**
     * 查询已经停止的批作业
     * @param executionIdSet
     * @return
     */
    public List<Long> selStoppedExexuions(List<Long> executionIds);

    /**
     * 查询定时任务表中执行数量
     * @param executionIdSet
     * @return
     */
    public Long selRunningScheduleTask(String taskTable);
    
    /**
     * 查询回执核销任务表根据回执核销规则
     * @return
     */
	List<TaskPrmyInfo> queryReceiptVerificaTask();
	
	List<TaskPrmyInfo> queryTaskPrmyInfoByTaskId(String taskTable,String applNo,String taskId);
	
	
}