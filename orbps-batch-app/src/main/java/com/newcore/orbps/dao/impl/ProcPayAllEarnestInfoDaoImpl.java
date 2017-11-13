package com.newcore.orbps.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.newcore.orbps.dao.api.ProcPayAllEarnestInfoDao;
import com.newcore.orbps.models.service.bo.ProcEarnestPayTask;


/**
 * 暂缴费支取全部支取
 * @author ljf
 * 2017年3月8日 11:26:52
 */
@Repository("procPayAllEarnestInfoDao")
public class ProcPayAllEarnestInfoDaoImpl implements ProcPayAllEarnestInfoDao {

	@Autowired
	JdbcOperations jdbcTemplate;

	private static Logger logger = LoggerFactory.getLogger(ProcPayAllEarnestInfoDaoImpl.class);

	@Override
	public List<ProcEarnestPayTask> queryProcEarnestPayTask() {

		List<ProcEarnestPayTask> procEarnestPayTaskList = new ArrayList<>();
		String	sql = "select APPL_NO , TASK_SEQ  from PROC_EARNEST_PAY_TASK WHERE STATUS IN ('N','E')";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		while(row.next()){
			ProcEarnestPayTask procEarnestPayTask = new ProcEarnestPayTask();
			procEarnestPayTask.setApplNo(row.getString(1));
			procEarnestPayTask.setTaskSeq(Long.valueOf(row.getString(2)));
			procEarnestPayTaskList.add(procEarnestPayTask);
		}
		return procEarnestPayTaskList;
	}

	@Override
	public long queryProcEarnestPayTaskTaskSql(String applNo) {
		//select * from MIO_ACC_INFO_LOG t where  t.acc_id in ('52','53') and t.create_time =(select max(create_time ) from MIO_ACC_INFO_LOG where  acc_id in ('52','53')) 

		StringBuilder sql = new StringBuilder();
		sql.append("select TASK_SEQ from PROC_EARNEST_PAY_TASK ");
		sql.append(" WHERE APPL_NO = '");
		sql.append(applNo);
		sql.append("'");
		sql.append(" AND CREATE_TIME = (select max(CREATE_TIME) from PROC_EARNEST_PAY_TASK WHERE APPL_NO = '");
		sql.append(applNo);
		sql.append("')");
		return jdbcTemplate.queryForObject(sql.toString(), long.class);
	}

	@Override
	public boolean updateProcEarnestPayTask(String status, long taskSql , String remark , String timeFlag) {

		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE PROC_EARNEST_PAY_TASK SET STATUS = '"); 
		sql.append(status);
		sql.append("'");
		sql.append(" , REMARK = '");
		sql.append(remark);
		sql.append("'");
		if(StringUtils.equals("S", timeFlag)){
			sql.append(" , START_TIME = sysdate");
		}else{
			sql.append(" , END_TIME = sysdate");
		}
		sql.append("  WHERE TASK_SEQ = '");
		sql.append(taskSql);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
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
		sql.append(" AND  INSTR(CNTR_NO, '");
		sql.append(cntrNo);
		sql.append("')>0");
		sql.append(" AND mio_class = 1 ");
		sql.append(" AND mio_item_code='FA' ");
		try {
			sunAmnt = jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
		} catch (EmptyResultDataAccessException e) {
			logger.info(e.getMessage(),e);
			return sunAmnt;
		}
		return sunAmnt;
	}

	@Override
	public BigDecimal getPlnmioRec(String cntrNo, String bankAccNo, String bankCode, String element,
			String elementFlag) {
		BigDecimal sunAmnt = null;
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(AMNT) from PLNMIO_REC");
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
		sql.append(" AND CNTR_NO = '");
		sql.append(cntrNo);
		sql.append("'");
		sql.append(" AND mio_class =-1 ");
		sql.append(" AND mio_item_code = 'FA' ");
		sunAmnt = jdbcTemplate.queryForObject(sql.toString(), BigDecimal.class);
		return sunAmnt;
	}
}
