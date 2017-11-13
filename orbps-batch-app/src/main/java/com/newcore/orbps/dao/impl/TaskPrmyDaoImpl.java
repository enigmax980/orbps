package com.newcore.orbps.dao.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.business.TaskPrmyRowMapper;
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

	private final static String RECEIPT_VERIFICA_SQL ="select t1.* from RECEIPT_VERIFICA_TASK_QUEUE t1,task_cntr_data_landing_queue t2 "
			+ "WHERE t1.STATUS ='A' and  t1.appl_no=t2.appl_no and ( t1.ext_key1 is not null or ( t1.ext_key0 is not null and "
			+ "( ( t2.lst_proc_type in ('L','N') and sysdate-to_date(t1.ext_key0,'yyyy/mm/dd,hh24:mi:ss') >3) or (t2.lst_proc_type = 'A' and sysdate-to_date(t1.ext_key0,'yyyy/mm/dd,hh24:mi:ss') >60 ))))";
	/**
	 * 根据任务状态、任务列队名称查询任务队列接口
	 * 
	 * @param taskTable
	 *            任务列队名称
	 * @param status
	 *            任务状态
	 * @return List<TaskPrmyInfo> 任务队列实体类
	 */
	@Override
	public List<TaskPrmyInfo> queryTaskPrmyInfo(PageQuery<TaskPrmyInfo> pageQuery) {
		Assert.notNull(pageQuery);
		StringBuilder sql = new StringBuilder();
		if (null != pageQuery.getCondition() && null != pageQuery.getCondition().getStatus()) {
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
	 * @param taskSeq
	 *            任务ID
	 * @param status
	 *            任务状态
	 * @param endTime
	 *            任务完成时间
	 * @param askTimes
	 *            执行次数
	 * @return boolean true:成功，false:失败
	 * @throws ParseException
	 * @throws Exception
	 */

	public boolean updateTaskPrmyInfoBytaskSeq(String taskTable, TaskPrmyInfo taskPrmyInfo){

		Assert.notNull(taskTable);
		Assert.notNull(taskPrmyInfo);
		Date createDate = null, startDate = null, endDate = null;
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(taskTable);
		sql.append(
				" set status=?,create_time=?,start_time=?,end_time=?,business_key=?,job_instance_id=?,remark=?,ask_times=?,ext_key0=?,ext_key1=?,ext_key2=?,ext_key3=?,ext_key4=?,ext_key5=? where task_seq=?");

		logger.info("sql:" + sql);
		if (!StringUtils.isEmpty(taskPrmyInfo.getCreateTime())) {
			try {
				createDate = DateUtils.parseDate(taskPrmyInfo.getCreateTime(), pattern);
			} catch (ParseException e) {
				logger.info(e.getMessage(), e);
				throw new RuntimeException(e.getCause());
			}
		}

		if (!StringUtils.isEmpty(taskPrmyInfo.getStartTime())) {
			try {
				startDate = DateUtils.parseDate(taskPrmyInfo.getStartTime(), pattern);
			} catch (ParseException e) {
				logger.info(e.getMessage(), e);
			}
		}

		if (!StringUtils.isEmpty(taskPrmyInfo.getEndTime())) {
			try {
				endDate = DateUtils.parseDate(taskPrmyInfo.getEndTime(), pattern);
			} catch (ParseException e) {
				logger.info(e.getMessage(), e);
				throw new RuntimeException(e.getCause());
			}
		}
		Integer num = jdbcTemplate.update(sql.toString(), taskPrmyInfo.getStatus(), createDate, startDate, endDate,
				taskPrmyInfo.getBusinessKey(), taskPrmyInfo.getJobInstanceId(), taskPrmyInfo.getRemark(),
				taskPrmyInfo.getAskTimes(), taskPrmyInfo.getExtKey0(), taskPrmyInfo.getExtKey1(),
				taskPrmyInfo.getExtKey2(), taskPrmyInfo.getExtKey3(), taskPrmyInfo.getExtKey4(),
				taskPrmyInfo.getExtKey5(), taskPrmyInfo.getTaskSeq());
		if (num > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 根据任务列队名称增加任务队列表信息
	 * 
	 * @param taskPrmyInfo
	 * @return
	 */
	@Override
	public boolean insertTaskPrmyInfoByTaskSeq(String taskTable, TaskPrmyInfo taskPrmyInfo) {
		Assert.notNull(taskTable);
		Assert.notNull(taskPrmyInfo);
		StringBuffer sql = new StringBuffer();
		sql.append("insert into ");
		sql.append(taskTable);
		sql.append(" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		int i = jdbcTemplate.update(sql.toString(), taskPrmyInfo.getTaskSeq(), taskPrmyInfo.getStatus(),
				taskPrmyInfo.getSalesBranchNo(), new Date(), null, null, 0, taskPrmyInfo.getTaskId(),
				taskPrmyInfo.getBusinessKey(), taskPrmyInfo.getApplNo(), taskPrmyInfo.getListPath(),
				taskPrmyInfo.getJobInstanceId(), taskPrmyInfo.getRemark(), taskPrmyInfo.getExtKey0(),
				taskPrmyInfo.getExtKey1(), taskPrmyInfo.getExtKey2(), taskPrmyInfo.getExtKey3(),
				taskPrmyInfo.getExtKey4(), taskPrmyInfo.getExtKey5());
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int countForStatusIsN(String taskTable) {
		Assert.notNull(taskTable);
		StringBuilder sql = new StringBuilder();
		sql.append("select count(*) from ");
		sql.append(taskTable);
		sql.append(" where status = 'N'");
		int i = jdbcTemplate.queryForObject(sql.toString(), int.class);
		return i;
	}

	@Override
	public List<TaskPrmyInfo> queryTaskPrmyInfoByStatus(String taskTable, String status) {
		Assert.notNull(taskTable);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(taskTable);
		sql.append(" WHERE STATUS ='");
		sql.append(status);
		sql.append("'");
		RowMapper<TaskPrmyInfo> rowMapper = new TaskPrmyRowMapper();
		List<TaskPrmyInfo> list = jdbcTemplate.query(sql.toString(), rowMapper);
		return list;
	}
	
	@Override
	public List<TaskPrmyInfo> queryReceiptVerificaTask() {
		
		RowMapper<TaskPrmyInfo> rowMapper = new TaskPrmyRowMapper();
		List<TaskPrmyInfo> list = jdbcTemplate.query(RECEIPT_VERIFICA_SQL, rowMapper);
		return list;
	}
	
	@Override
	public TaskPrmyInfo queryTaskPrmyInfoByTaskSeq(String taskTable, Long taskSeq) {
		Assert.notNull(taskTable);
		Assert.notNull(taskSeq);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(taskTable);
		sql.append(" WHERE TASK_SEQ =");
		sql.append(taskSeq);
		logger.info(sql.toString());
		RowMapper<TaskPrmyInfo> rowMapper = new TaskPrmyRowMapper();
		TaskPrmyInfo taskPrmyInfo = (TaskPrmyInfo) jdbcTemplate.queryForObject(sql.toString(), rowMapper);
		return taskPrmyInfo;
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
	public List<Long> queryJobExcutionIdList(String taskTable) {
		Assert.notNull(taskTable);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT JOB_INSTANCE_ID FROM ");
        sql.append(taskTable);
        sql.append(" WHERE STATUS ='K' AND JOB_INSTANCE_ID > 0");
        
        List<Long> jobExcutionIdList = jdbcTemplate.queryForList(sql.toString(),Long.class);

		return jobExcutionIdList;
	}

	@Override
	public List<Long> queryJobExcutionIdListByApplno(String taskTable, String applNo) {
		Assert.notNull(taskTable);
		Assert.notNull(applNo);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT JOB_INSTANCE_ID FROM ");
        sql.append(taskTable);
        sql.append(" WHERE STATUS ='K'");
        sql.append(" AND APPL_NO ='");
        sql.append(applNo);
		sql.append("'");
        
        List<Long> jobExcutionIdList = jdbcTemplate.queryForList(sql.toString(),Long.class);

		return jobExcutionIdList;
	}

	@Override
	public boolean updateExcuIdByTaskSeq(String taskTable, Long taskSeq, Long excuId) {

		Assert.notNull(taskTable);
		Assert.notNull(taskSeq);
		Assert.notNull(excuId);
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(taskTable);
		sql.append(" set JOB_INSTANCE_ID=? where TASK_SEQ=? ");

		logger.info("sql:" + sql);
		Integer num = jdbcTemplate.update(sql.toString(), excuId, taskSeq);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateStatusByApplNo(String taskTable, String applNo) {

		Assert.notNull(taskTable);
		Assert.notNull(applNo);
		StringBuilder sql = new StringBuilder();
		sql.append("update ");
		sql.append(taskTable);
		sql.append(" set STATUS='E',end_time=sysdate where APPL_NO=? and STATUS='K'");

		logger.info("sql:" + sql);
		Integer num = jdbcTemplate.update(sql.toString(), applNo);
		if (num > 0) {
			return true;
		} else {
			return false;
		}
	}
    public List<Long> selRuningExexuions(List<Long> executionIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION WHERE END_TIME IS NULL AND JOB_EXECUTION_ID IN (");
        for (Long executionId : executionIds) {
            sql.append(executionId);
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        logger.info("sql:" + sql);
        List<Long> runingExecutionIds = jdbcTemplate.queryForList(sql.toString(), Long.class);

        return runingExecutionIds;
    }

    public Long selStoppingExexuions(List<Long> executionIds){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM BATCH_JOB_EXECUTION  WHERE STATUS ='STOPPING' AND JOB_EXECUTION_ID IN (");
        for (Long executionId : executionIds) {
            sql.append(executionId);
            sql.append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(")");
        logger.info("sql:" + sql);

        return  jdbcTemplate.queryForObject(sql.toString(), Long.class);
    }
	public List<Long> selStoppedExexuions(List<Long> executionIds){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT JOB_EXECUTION_ID FROM BATCH_JOB_EXECUTION  WHERE EXIT_CODE ='STOPPED' AND JOB_EXECUTION_ID IN (");
		for (Long executionId : executionIds) {
			sql.append(executionId);
			sql.append(",");
		}
		sql.deleteCharAt(sql.length() - 1);
		sql.append(")");
		logger.info("sql:" + sql);
		List<Long> runingExecutionIds = jdbcTemplate.queryForList(sql.toString(), Long.class);

		return runingExecutionIds;
	}
    public Long selRunningScheduleTask(String taskTable){
		Assert.notNull(taskTable);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ");
        sql.append(taskTable);
        sql.append(" WHERE STATUS in ('K', 'W')");

        return jdbcTemplate.queryForObject(sql.toString(), Long.class);

    }
    
    @Override
	public List<TaskPrmyInfo> queryTaskPrmyInfoByTaskId(String taskTable, String applNo, String taskId) {
		Assert.notNull(taskTable);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ");
		sql.append(taskTable);
		sql.append(" WHERE APPL_NO ='");
		sql.append(applNo);
		sql.append("'");
		sql.append(" AND TASK_ID = '");
		sql.append(taskId);
		sql.append("'");
		RowMapper<TaskPrmyInfo> rowMapper = new TaskPrmyRowMapper();
		List<TaskPrmyInfo> list = jdbcTemplate.query(sql.toString(), rowMapper);
		return list;
	}
}
