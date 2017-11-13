package com.newcore.orbpsutils.dao.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.service.bo.ProcEarnestPayTask;
import com.newcore.orbpsutils.dao.api.QueryAccInfoDao;



/**
 * 查询账户信息
 * @author LJF
 * 2017年2月22日 20:19:55
 */
@Repository("queryAccInfoDao")
public class QueryAccInfoDaoImpl implements QueryAccInfoDao {

	@Autowired
	JdbcOperations jdbcTemplate;

	private static Logger logger = LoggerFactory.getLogger(QueryAccInfoDaoImpl.class);

	private static final String INSERT_MIO_ACC_INFO_LOG = "insert into MIO_ACC_INFO_LOG";

	private static final String SELECT_S_MIO_ACC_INFO_LOG_SQL = "select S_MIO_ACC_INFO_LOG.nextval from dual";

	private static final String SELECT_PROC_EARNEST_PAY_TASK_SQL = "select * from PROC_EARNEST_PAY_TASK WHERE STATUS IN ('N','E')";

	@Override
	public List<EarnestAccInfo> queryEarnestAccInfo(List<String> accNoList) {

		StringBuilder strAccNoList = new StringBuilder();
		//遍历账号accNoList
		for (String strAccNo : accNoList) {
			strAccNoList.append(" '");
			strAccNoList.append(strAccNo);
			strAccNoList.append("' ,");
		}
		String	str = strAccNoList.substring(0,strAccNoList.length()-1);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM EARNEST_ACC_INFO");
		sql.append(" WHERE ACC_NO in (");
		sql.append(str);
		sql.append(")");
		List<EarnestAccInfo> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		return list;
	}

	@Override
	public List<EarnestAccInfo> queryEarnestAccInfoByApplNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM EARNEST_ACC_INFO");
		sql.append(" WHERE ACC_NO like '");
		sql.append(applNo+"%");
		sql.append("'");
		List<EarnestAccInfo> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		return list;
	}

	@Override
	public BigDecimal querySumBalanceEarnestAccInfoByApplNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(balance) from EARNEST_ACC_INFO ");
		sql.append(" WHERE ACC_NO like'");
		sql.append(applNo+"%");
		sql.append("'");
		return (BigDecimal) jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
	}

	@Override
	public boolean getProcStat(String cntrNo, String lockFlag, String procStat) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from PLNMIO_REC");
		sql.append(" WHERE PROC_STAT='");
		sql.append(procStat);
		sql.append("'");
		sql.append(" AND LOCK_FLAG ='");
		sql.append(lockFlag);
		sql.append("'");
		sql.append(" AND CNTR_NO like '");
		sql.append(cntrNo);
		sql.append("%'");
		List<PlnmioRec> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<PlnmioRec>(PlnmioRec.class));
		return list.size()>0;
	}

	@Override
	public boolean updateEarnestAccInfo(String accNo , Double amnt){
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE EARNEST_ACC_INFO SET BALANCE = '"); 
		sql.append(amnt);
		sql.append("'  WHERE ACC_NO='");
		sql.append(accNo);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
	}

	@Override
	public boolean updateEarnestAccInfo(long accId) {
		StringBuilder sql = new StringBuilder();
		sql.append("update EARNEST_ACC_INFO set frozen_balance = 0");
		sql.append(" where acc_id ='");
		sql.append(accId);
		sql.append("'");
		int i = jdbcTemplate.update(sql.toString());
		return i>0;
	}

	@Override
	public boolean insertEarnestAccInfo(MioAccInfoLog mioAccInfoLog) {
		StringBuilder sql = new StringBuilder();
		sql.append(INSERT_MIO_ACC_INFO_LOG);
		sql.append(" values(?,?,?,?,?,?,?)");
		int row=jdbcTemplate.update(sql.toString(),	
				mioAccInfoLog.getAccLogId(),mioAccInfoLog.getAccId(),mioAccInfoLog.getCreateTime(),mioAccInfoLog.getMioItemCode(),	
				mioAccInfoLog.getMioClass(),mioAccInfoLog.getAnmt(),mioAccInfoLog.getRemark());
		return row>0;
	}

	@Override
	public BigDecimal getMioLogSumAmnt(String cntrNo, String bankAccNo , String bankCode , String element , String elementFlag) {
		BigDecimal sunAmnt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(AMNT) from mio_log");
		sql.append(" WHERE BANK_CODE='");
		sql.append(bankCode);
		sql.append("'");
		sql.append(" AND BANK_ACC_NO ='");
		sql.append(bankAccNo);
		sql.append("'");
		if(StringUtils.equals("I", elementFlag)){
			sql.append(" AND IPSN_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else if(StringUtils.equals("P", elementFlag)){
			sql.append(" AND FEE_GRP_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else{
			sql.append(" AND LEVEL_CODE ='");
			sql.append(element);
			sql.append("'");
		}
		sql.append(" AND CNTR_NO like '");
		sql.append(cntrNo);
		sql.append("%'");
		try {
			sunAmnt = jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return sunAmnt;
		}
		return sunAmnt;
	}

	@Override
	public EarnestAccInfo queryOneEarnestAccInfoByApplNo(String accNo) {
		EarnestAccInfo earnestAccInfo = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM EARNEST_ACC_INFO");
		sql.append(" WHERE ACC_NO = '");
		sql.append(accNo);
		sql.append("'");
		try {
			earnestAccInfo = jdbcTemplate.queryForObject(sql.toString(), 
					new CustomBeanPropertyRowMapper<EarnestAccInfo>(EarnestAccInfo.class));
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return earnestAccInfo;
		}
		return earnestAccInfo;
	}

	@Override
	public BigDecimal querySumFrozenBalanceEarnestAccInfoByApplNo(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(frozen_balance) from EARNEST_ACC_INFO ");
		sql.append(" WHERE ACC_NO like'");
		sql.append(applNo+"%");
		sql.append("'");
		return (BigDecimal) jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
	}

	@Override
	public long selectMioAccInfoLog() {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(SELECT_S_MIO_ACC_INFO_LOG_SQL, long.class);
	}

	@Override
	public List<ProcEarnestPayTask> queryProcEarnestPayTask() {
		List<ProcEarnestPayTask> list = jdbcTemplate.query(SELECT_PROC_EARNEST_PAY_TASK_SQL, 
				new CustomBeanPropertyRowMapper<ProcEarnestPayTask>(ProcEarnestPayTask.class));
		return list;
	}

	@Override
	public boolean updateProcEarnestPayTask(String status, long taskSql , String remark , String timeFlag) {
		StringBuilder sql = new StringBuilder();
		sql.append("UPDATE PROC_EARNEST_PAY_TASK SET STATUS = '"); 
		sql.append(status);
		sql.append("'");
		sql.append(" , REMARK = '");
		sql.append(remark);
		sql.append("'");
		if(StringUtils.equals("S", timeFlag)){
			sql.append(" , START_TIME = '");
		}else{
			sql.append(" , END_TIME = '");
		}
		sql.append(new Date());
		sql.append("'");
		sql.append("'  WHERE TASK_SEQ = '");
		sql.append(taskSql);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
	}

	@Override
	public long queryProcEarnestPayTaskTaskSql(String applNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select TASK_SEQ from PROC_EARNEST_PAY_TASK ");
		sql.append(" WHERE APPL_NO = '");
		sql.append(applNo);
		sql.append("'");
		sql.append(" AND STATUS = 'K' ");
		return jdbcTemplate.queryForObject(sql.toString(), long.class);
	}

	@Override
	public boolean removePlnmioRecList(String cgNo) {

		StringBuilder sql = new StringBuilder();
		sql.append(" delete plnmio_rec where CG_NO = '");
		sql.append(cgNo);
		sql.append("' and mio_item_code='PS' and mio_class= '1' and mtn_item_code= '29' ");
		int size = jdbcTemplate.update(sql.toString());
		return size>0;
	}

	@Override
	public BigDecimal getSumAmntFromMioLog(String cntrNo, String bankAccNo, String bankCode, String element,
			String elementFlag) {
		BigDecimal sunAmnt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(AMNT) from mio_log");
		sql.append(" WHERE BANK_CODE='");
		sql.append(bankCode);
		sql.append("'");
		sql.append(" AND BANK_ACC_NO ='");
		sql.append(bankAccNo);
		sql.append("'");
		if(StringUtils.equals("I", elementFlag)){
			sql.append(" AND IPSN_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else if(StringUtils.equals("P", elementFlag)){
			sql.append(" AND FEE_GRP_NO ='");
			sql.append(Long.valueOf(element));
			sql.append("'");
		}else{
			sql.append(" AND LEVEL_CODE ='");
			sql.append(element);
			sql.append("'");
		}
		sql.append(" AND CNTR_NO like '");
		sql.append(cntrNo);
		sql.append("%'");
		try {
			sunAmnt = jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return sunAmnt;
		}
		return sunAmnt;
	}



}
