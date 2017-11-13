package com.newcore.orbps.dao.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.service.api.PageQueryService;

@Repository("taskPrmyDao")
public class TaskPrmyDaoImpl implements TaskPrmyDao {
	
	@Autowired
	JdbcOperations jdbcTemplate;
	/**
	 * 分页辅助工具
	 */
	@Autowired
	PageQueryService pageQueryService;

	private static Logger logger = LoggerFactory.getLogger(TaskPrmyDaoImpl.class);

	private final static String pattern = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 根据任务状态、任务列队名称查询任务队列接口
	 * 
	 * @param pageQuery
	 *            任务列队名称
	 *            任务状态
	 * @return List<TaskPrmyInfo> 任务队列实体类
	 */
	@Override
	public List<TaskPrmyInfo> queryTaskPrmyInfo(PageQuery<TaskPrmyInfo> pageQuery) {
		Assert.notNull(pageQuery);
		StringBuilder sql = new StringBuilder();
		if (pageQuery.getCondition() != null
			&& pageQuery.getCondition().getStatus() != null){
			sql.append(
				"select task_seq,status,sales_branch_no,cteate_time,start_time,end_time,ask_times,business_key,task_id,appl_no,list_path,job_instance_id from ");
			sql.append(pageQuery.getCondition().getTaskTable());
			sql.append(" where status='");
			sql.append(pageQuery.getCondition().getStatus());
			sql.append("'");
		}
		String pageSql = pageQueryService.buildPageQuerySql(sql + "", pageQuery);
		List<TaskPrmyInfo> list = jdbcTemplate.query(pageSql,
				new CustomBeanPropertyRowMapper<TaskPrmyInfo>(TaskPrmyInfo.class));
		return list;
	}

	/**
	 * 根据任务ID修改任务队列表的任务状态、任务完成时间和执行次数
	 * 
	 * @param taskTable
	 *            任务ID
	 * @param taskPrmyInfo
	 *            执行次数
	 * @return boolean true:成功，false:失败
	 * @throws ParseException
	 * @throws Exception
	 */
	@Override
	public boolean updateTaskPrmyInfoBytaskSeq(String taskTable, TaskPrmyInfo taskPrmyInfo) throws ParseException {
		
		Date createDate = null, startDate = null, endDate = null;
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(taskTable);
		sql.append(
				" set status=?,create_time=?,start_time=?,end_time=?,business_key=?,job_instance_id=?,remark=?,ask_times=?,ext_key0=?,ext_key1=?,ext_key2=?,ext_key3=?,ext_key4=?,ext_key5=? where task_seq=?");

		logger.info("sql:" + sql);
		if (!StringUtils.isEmpty(taskPrmyInfo.getCreateTime())) {
			createDate = new SimpleDateFormat(pattern).parse(taskPrmyInfo.getCreateTime());
		}

		if (!StringUtils.isEmpty(taskPrmyInfo.getStartTime())) {
			startDate = new SimpleDateFormat(pattern).parse(taskPrmyInfo.getStartTime());
		}

		if (!StringUtils.isEmpty(taskPrmyInfo.getEndTime())) {
			endDate = new SimpleDateFormat(pattern).parse(taskPrmyInfo.getEndTime());
		}	
		Integer num = jdbcTemplate.update(sql.toString(),taskPrmyInfo.getStatus(), createDate, startDate, 
				             endDate, taskPrmyInfo.getBusinessKey(),taskPrmyInfo.getJobInstanceId(),
				             taskPrmyInfo.getRemark(), taskPrmyInfo.getAskTimes(), taskPrmyInfo.getExtKey0(), 
				             taskPrmyInfo.getExtKey1(), taskPrmyInfo.getExtKey2(),  taskPrmyInfo.getExtKey3(),  taskPrmyInfo.getExtKey4(),
				             taskPrmyInfo.getExtKey5(), taskPrmyInfo.getTaskSeq());
		if (num > 0) {
			return true;
		} else {
			return false;
		}

	}
	
	/**
	 * 根据任务列队名称增加任务队列表信息	
	 * @param taskPrmyInfo
	 * @return
	 */
	@Override
	public boolean insertTaskPrmyInfoByTaskSeq(String taskTable,
			TaskPrmyInfo taskPrmyInfo) {
		StringBuilder sql = new StringBuilder(); 
		sql.append("insert into ");
		sql.append(taskTable);
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		int i = jdbcTemplate.update(sql.toString(),taskPrmyInfo.getTaskSeq(),taskPrmyInfo.getStatus()
				,taskPrmyInfo.getSalesBranchNo(),new Date(),null
				,null,0,taskPrmyInfo.getTaskId(),taskPrmyInfo.getBusinessKey()
				,taskPrmyInfo.getApplNo(),taskPrmyInfo.getListPath(),taskPrmyInfo.getJobInstanceId()
				,taskPrmyInfo.getRemark(),taskPrmyInfo.getExtKey0(),taskPrmyInfo.getExtKey1()
				,taskPrmyInfo.getExtKey2(),taskPrmyInfo.getExtKey3(),taskPrmyInfo.getExtKey4()
				,taskPrmyInfo.getExtKey5());
		if(i>0){
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 根据投保单号查询任务基本信息
	 * @param applNo	
	 * @return
	 */
	@Override
	public List<TaskPrmyInfo> selectTaskPrmyInfoByApplNo(String taskTable, String applNo,String status) {
		StringBuilder sql = new StringBuilder("SELECT * FROM ");
		sql.append(taskTable);
		sql.append(" WHERE APPL_NO = '");
		sql.append(applNo);
		sql.append("'");
		if(status != null){
			sql.append(" AND STATUS = '");
			sql.append(status);
			sql.append("'");
		}
		logger.info(sql.toString());
		List<TaskPrmyInfo> list  =  jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<TaskPrmyInfo>(TaskPrmyInfo.class));
		return list;
	}
	/**
	 * 根据投保单号删除任务基本信息
	 * @param applNo	
	 * @return
	 */
	@Override
	public Boolean deletTaskPrmyInfoByApplNo(String taskTable,String applNo){
		StringBuilder sql = new StringBuilder();
		if(null != taskTable && !("").equals(taskTable) && null != applNo && !("").equals(applNo)){
			sql.append("DELETE FROM " + taskTable + " WHERE APPL_NO =?");
		}
		Integer num = jdbcTemplate.update(sql.toString(),applNo);
		if (num > 0) {
			return true;
    } else {
			return false;
		}
	}

	@Override
	public Long getTaskSeq(String tableName) {
        Assert.notNull(tableName);
		String sequeTableName = "S_"+tableName;
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("SELECT ");
        sqlBuffer.append(sequeTableName);
		sqlBuffer.append(".NEXTVAL");
        sqlBuffer.append(" FROM DUAL");

        return jdbcTemplate.queryForObject(sqlBuffer.toString(), Long.class);
	}

	@Override
	public boolean updateSmsTimeByApplNo(String taskTable, String applNo, String extKey0) {
		Assert.notNull(taskTable);
		Assert.notNull(applNo);
		Assert.notNull(extKey0);
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(taskTable);
		sql.append(" set ext_key0=? where APPL_NO=? ");

		logger.info("sql:" + sql);
		Integer num = jdbcTemplate.update(sql.toString() ,extKey0 ,applNo);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}
}
