package com.newcore.orbpsutils.dao.impl;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;


import java.util.List;

/**
 * 保单落地监控表
 * Created by liushuaifeng on 2017/2/14 0014.
 */
@Repository("TaskCntrDataLandingQueueDao")
public class TaskCntrDataLandingQueueDaoImpl implements TaskCntrDataLandingQueueDao {

	private static Logger logger = LoggerFactory.getLogger(TaskCntrDataLandingQueueDaoImpl.class);
	/**
	 * 插入监控表一条记录
	 */
	//private static final String INSERT_TASK_CNTR_DATA_LANDING_QUEUE_SQL = "INSERT INTO TASK_CNTR_DATA_LANDING_QUEUE VALUES (?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?, ?,?, ?)";

	/**
	 * 监控表
	 */
	private static final String TABLE_NAME = "TASK_CNTR_DATA_LANDING_QUEUE";

	/**
	 * 获取序列
	 */
	private static final String S_TABLE_NAME = "SELECT S_TASK_CNTR_DATA_LANDING_QUEUE.NEXTVAL FROM DUAL";

	/**
	 * The constant SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_TASK_ID_SQL.
	 */
	private static final String SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_TASK_SEQ_SQL = "SELECT * FROM TASK_CNTR_DATA_LANDING_QUEUE WHERE TASK_SEQ = ?";

	/**
	 * The constant SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO_SQL.
	 */
	private static final String SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO_SQL = "SELECT * FROM TASK_CNTR_DATA_LANDING_QUEUE WHERE APPL_NO = ?";

	/**
	 * The constant GET_INSERT_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO_SQL.
	 */
	private static final String SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_STATUS_SQL = "SELECT * FROM TASK_CNTR_DATA_LANDING_QUEUE WHERE STATUS = ?";

	/**
	 * The constant GET_INSERT_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO_SQL.
	 */
	private static final String UPDATE_TASK_CNTR_DATA_LANDING_QUEUE_BY_TASK_SEQ_SQL = "UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET STATUS = ? WHERE TASK_SEQ = ?";

	private static final String UPDATE_FIN_LAND_FLAG_BY_APPL_NO_SQL = "UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET FIN_LAND_FLAG = ? WHERE APPL_NO = ?";

	private static final String UPDATE_IPSN_LAND_FLAG_BY_APPL_NO_SQL = "UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET IPSN_LAND_FLAG = ? WHERE APPL_NO = ?";

	private static final String UPDATE_PLN_LAND_FLAG_BY_APPL_NO_SQL = "UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET PLN_LAND_FLAG = ? WHERE APPL_NO = ?";
	/**
	 * The constant GET_INSERT_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO_SQL.
	 */
	private static final String UPDATE_TASK_CNTR_DATA_LANDING_QUEUE_ALL_BY_TASK_SEQ_SQL =
			"UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET STATUS = ?, SALES_BRANCH_NO = ?, CREATE_TIME = ?,  START_TIME = ?, END_TIME = ?, ASK_TIMES = ?, " +
					"APPL_NO = ?, CG_NO = ?, JOB_INSTANCE_ID = ?, CNTR_TYPE = ?, " +
					"LST_PROC_TYPE = ?, IS_RENEW  = ?, IS_MULTI_PAY = ?, IS_COMMON_AGREEMENT = ?, FIN_LAND_BAT_NO = ?,FIN_LAND_FLAG = ?, IPSN_LAND_FLAG = ?, " +
					"INSUR_APPL_LAND_FLAG = ?, COMMON_AGREEMENT_LAND_FLAG = ?," +
					"REMARK = ?, EXT_KEY0 = ?, EXT_KEY1 = ?, EXT_KEY2 = ?, EXT_KEY3 = ?, EXT_KEY4 = ?, EXT_KEY5 = ? ,PLN_LAND_BAT_NO = ?, PLN_LAND_FLAG = ?, SUM_PREMIUM = ?, IS_STEP_PLNMIO =?  where TASK_SEQ = ?";

	private static final String DELETE_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO = "DELETE FROM TASK_CNTR_DATA_LANDING_QUEUE WHERE APPL_NO = ?";

	private static final String UPDATE_TASK_CNTR_DATA_LANDING_QUEUE_ARCHIVE_LIST_SQL = "UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET STATUS = 'N' , LST_PROC_TYPE = 'L',IPSN_LAND_FLAG = '0',EXT_KEY1 = 'A' WHERE LST_PROC_TYPE ='A' AND APPL_NO = ?";
	
	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	@Override
	public Boolean save(TaskCntrDataLandingQueue taskCntrDataLandingQueue) {
		Assert.notNull(taskCntrDataLandingQueue);

		SimpleJdbcInsert insert = DaoUtils.createJdbcInsert();
		insert.setTableName(TABLE_NAME);
		taskCntrDataLandingQueue.setTaskSeq(getTaskSeq());
		insert.execute(DaoUtils.toColumnMap(taskCntrDataLandingQueue));
		return true;
	}

	@Override
	public TaskCntrDataLandingQueue searchByTaskSeq(Long taskSeq) {
		Assert.notNull(taskSeq);
		TaskCntrDataLandingQueue result = jdbcTemplate.queryForObject(SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_TASK_SEQ_SQL,
				new CustomBeanPropertyRowMapper<TaskCntrDataLandingQueue>(TaskCntrDataLandingQueue.class), taskSeq);

		return result;
	}

	@Override
	public TaskCntrDataLandingQueue searchByApplNo(String applNo) {
		Assert.notNull(applNo);
		try {
			TaskCntrDataLandingQueue result = jdbcTemplate.queryForObject(SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO_SQL,
					new CustomBeanPropertyRowMapper<TaskCntrDataLandingQueue>(TaskCntrDataLandingQueue.class), applNo);
			return result;

		}catch (EmptyResultDataAccessException e){ //如果查询结果为空，则返回Null
			logger.info(e.getMessage(),e);
			logger.info("查询保单落地无结果：table:applNo="+applNo);
			return null;
		}

	}

	@Override
	public List<TaskCntrDataLandingQueue> searchByStatus(String status) {
		Assert.notNull(status);
		List<TaskCntrDataLandingQueue> resultList = jdbcTemplate.query(SELECT_TASK_CNTR_DATA_LANDING_QUEUE_BY_STATUS_SQL,
				new CustomBeanPropertyRowMapper<TaskCntrDataLandingQueue>(TaskCntrDataLandingQueue.class), status);

		return resultList;
	}

	@Override
	public Boolean updateByTaskSeq(Long taskSeq, TaskCntrDataLandingQueue taskCntrDataLandingQueue) {
		Assert.notNull(taskSeq);
		Assert.notNull(taskCntrDataLandingQueue);
		jdbcTemplate.update(UPDATE_TASK_CNTR_DATA_LANDING_QUEUE_ALL_BY_TASK_SEQ_SQL, taskCntrDataLandingQueue.getStatus(),
				taskCntrDataLandingQueue.getSalesBranchNo(), taskCntrDataLandingQueue.getCreateTime(), taskCntrDataLandingQueue.getStartTime(),
				taskCntrDataLandingQueue.getEndTime(), taskCntrDataLandingQueue.getAskTimes(), taskCntrDataLandingQueue.getApplNo(), taskCntrDataLandingQueue.getCgNo(),
				taskCntrDataLandingQueue.getJobInstanceId(), taskCntrDataLandingQueue.getCntrType(), taskCntrDataLandingQueue.getLstProcType(), taskCntrDataLandingQueue.getIsRenew(),
				taskCntrDataLandingQueue.getIsMultiPay(), taskCntrDataLandingQueue.getIsCommonAgreement(), taskCntrDataLandingQueue.getFinLandBatNo(), taskCntrDataLandingQueue.getFinLandFlag(),
				taskCntrDataLandingQueue.getIpsnLandFlag(), taskCntrDataLandingQueue.getInsurApplLandFlag(), taskCntrDataLandingQueue.getCommonAgreementLandFlag(),
				taskCntrDataLandingQueue.getRemark(), taskCntrDataLandingQueue.getExtKey0(), taskCntrDataLandingQueue.getExtKey1(), taskCntrDataLandingQueue.getExtKey2(),
				taskCntrDataLandingQueue.getExtKey3(), taskCntrDataLandingQueue.getExtKey4(), taskCntrDataLandingQueue.getExtKey5(),taskCntrDataLandingQueue.getPlnLandBatNo(),
				taskCntrDataLandingQueue.getPlnLandFlag(),taskCntrDataLandingQueue.getSumPremium(),taskCntrDataLandingQueue.getIsStepPlnmio(),
				taskSeq);
		return true;
	}

	@Override
	public Boolean updateStatusByTaskSeq(Long taskSeq, String status) {
		Assert.notNull(taskSeq);
		Assert.notNull(status);

		jdbcTemplate.update(UPDATE_TASK_CNTR_DATA_LANDING_QUEUE_BY_TASK_SEQ_SQL, status, taskSeq);
		return true;
	}

	@Override
	public Boolean updateFinLandFlagByApplNo(String applNo, String status) {
		Assert.notNull(applNo);
		Assert.notNull(status);
		jdbcTemplate.update(UPDATE_FIN_LAND_FLAG_BY_APPL_NO_SQL, status, applNo);

		return true;
	}

	@Override
	public Boolean updateIpsnLandFlagByApplNo(String applNo, String status) {
		Assert.notNull(applNo);
		Assert.notNull(status);
		jdbcTemplate.update(UPDATE_IPSN_LAND_FLAG_BY_APPL_NO_SQL, status, applNo);

		return true;
	}

	@Override
	public Boolean deleteByApplNo(String applNo) {
		Assert.notNull(applNo);
		jdbcTemplate.update(DELETE_TASK_CNTR_DATA_LANDING_QUEUE_BY_APPL_NO, applNo);

		return true;
	}

	private Long getTaskSeq() {
		return jdbcTemplate.queryForObject(S_TABLE_NAME, Long.class);
	}

	@Override
	public Boolean updateArchiveList(String applNo) {		
		Assert.notNull(applNo);
		jdbcTemplate.update(UPDATE_TASK_CNTR_DATA_LANDING_QUEUE_ARCHIVE_LIST_SQL, applNo);	
		return true;
	}

	@Override
	public Boolean updatePlnLandFlagByApplNo(String applNo, String status) {
		Assert.notNull(applNo);
		Assert.notNull(status);
	    jdbcTemplate.update(UPDATE_PLN_LAND_FLAG_BY_APPL_NO_SQL, status, applNo);
		return true;
	}
}
