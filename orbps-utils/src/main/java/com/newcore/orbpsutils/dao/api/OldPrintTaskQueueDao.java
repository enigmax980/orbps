package com.newcore.orbpsutils.dao.api;

import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * 打印任务控制表
 * @author JCC
 * 2017年5月17日 14:22:49
 */
public interface OldPrintTaskQueueDao {
	/**
	 * 删除数据
	 * @param applNo 投保单号
	 * @return
	 */
	int delData(String applNo);
	
	/**
	 * 插入数据
	 * @param taskId 任务ID
	 * @param applNo 投保单号
	 * @return
	 */
	int insertData(String taskId,String applNo);

	/**
	 * 查询任务ID
	 * @param applNo 投保单号
	 * @return
	 */
	String findTaskByApplNo(String applNo);

	/**
	 * 修改数据信息
	 * @param applNo 投保单号
	 * @param status 状态
	 * @return
	 */
	int updateDateByApplNo(String applNo, String status);

	/**
	 * 查询满足条件的数据
	 * @param status 数据状态
	 * @return
	 */
	SqlRowSet queryOldPrintQueue(String[] status);

	/**
	 * 查询投保单是否存在
	 * @param applNo
	 * @return
	 */
	int findCountyApplNo(String applNo);
	/**
	 * 判断是否打印在途
	 * @param applNo 投保单号
	 * @return
	 */
	String checkPrintIsOnLanging(String applNo);
}
