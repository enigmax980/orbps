package com.newcore.orbps.dao.api;

import java.text.ParseException;
import java.util.List;

import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.supports.models.service.bo.PageQuery;

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
	public boolean updateTaskPrmyInfoBytaskSeq(String taskTable, TaskPrmyInfo taskPrmyInfo) throws ParseException;
	/**
	 * 根据任务列队名称增加任务队列表信息
	 * @param taskPrmyInfo	
	 * @return
	 */
	public boolean insertTaskPrmyInfoByTaskSeq(String taskTable,TaskPrmyInfo taskPrmyInfo);
	/**
	 * 根据投保单号查询任务基本信息
	 * @param applNo	
	 * @return
	 */
	public List<TaskPrmyInfo> selectTaskPrmyInfoByApplNo(String taskTable,String applNo,String status);
	/**
	 * 根据投保单号删除任务基本信息
	 * @param applNo	
	 * @return
	 */
	public Boolean deletTaskPrmyInfoByApplNo(String taskTable,String applNo);

	/**
	 * 
	 * @param tableName
	 * @return
	 */
	public Long getTaskSeq(String tableName);
	
	/**
	 * 
	 * @param taskTable
	 * @param applNo
	 * @param extKey0
	 * @return
	 */
	boolean updateSmsTimeByApplNo(String taskTable, String applNo, String extKey0);
}
