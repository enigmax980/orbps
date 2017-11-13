package com.newcore.orbpsutils.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;

@Repository("plnmioCommonDao")
public class PlnmioCommonDaoImpl implements PlnmioCommonDao {
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public SqlRowSet queryMoneyInCheckQueue(String[] status) {
		StringBuilder sbr = new StringBuilder();
		int index =0;
		for(String statu: status){
			if(index > 0){
				sbr.append(",");
			}
			sbr.append("'");
			sbr.append(statu);
			sbr.append("'");
			index++;
		}
		// 1.从产生应收付流水任务表查询STATUS N 新增或者 E 错误的数据       
		String sql = "select TASK_ID,APPL_NO from MONEY_IN_CHECK_TASK_QUEUE where STATUS in ("+sbr.toString()+")";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		return row;
	}
	
	@Override
	public boolean updateMoneyInCheckStatus(String status, String taskId) {
		//从产生应收付流水任务表STATUS:K=检查处理中， N=新建，E=检查失败，D=保单作废，C=检查通过，S=冻结成功，W=冻结处理中，F=冻结处理失败
		String sqlDate="";
		if("K".equals(status) ){ 
			//开始检查处理执行时间
			sqlDate=" , START_TIME =sysdate ";
		}else if("D".equals(status) || "C".equals(status) ){
			//检查通过或保单作废时间
			sqlDate=" , END_TIME =sysdate ";
		}else if("S".equals(status)){
			//账户冻结成功时间
			sqlDate=" , EXT_KEY4 =to_char(sysdate,'YYYY/MM/DD HH24:mi:ss') ";
		}
		String sql="update MONEY_IN_CHECK_TASK_QUEUE set  STATUS='"+status+"' "+sqlDate+"  where TASK_ID='"+taskId+"'";
		int rows = jdbcTemplate.update(sql);
		return rows>0;
	}

	@Override
	public boolean updateMoneyInCheckExtKey(String miologDate, String taskId) {	
		StringBuilder sbr = new StringBuilder();
		sbr.append(" update MONEY_IN_CHECK_TASK_QUEUE set ");
		sbr.append(" EXT_KEY0  ='");
		sbr.append(miologDate);
		sbr.append("' where TASK_ID ='");
		sbr.append(taskId);
		sbr.append("'");
		int rows = jdbcTemplate.update(sbr.toString());
		return rows>0;
	}
	
	@Override
	public boolean updateMoneyInCheckInforce(String applNo) {	
		StringBuilder sbr = new StringBuilder();
		sbr.append(" update MONEY_IN_CHECK_TASK_QUEUE set ");
		sbr.append(" EXT_KEY5 =to_char(sysdate,'YYYY/MM/DD HH24:mi:ss')");
		sbr.append(" , STATUS ='N'");
		sbr.append(" where APPL_NO ='");
		sbr.append(applNo);
		sbr.append("' AND STATUS ='D'");
		int rows = jdbcTemplate.update(sbr.toString());
		return rows>0;
	}
	@Override
	public boolean insertMioInfoRoamTaskQueue(String branchNo,long batNo ) {
		String sql=" insert into MIO_INFO_ROAM_TASK_QUEUE(TASK_SEQ,STATUS,CREATE_TIME,TASK_ID,APPL_NO,REMARK,EXT_KEY0,EXT_KEY1)"
				  +" values(S_MIO_INFO_ROAM_TASK_QUEUE.NEXTVAL,'N',sysdate,'FAT','######','产生实收数据服务','"+branchNo+"','"+batNo+"')";
		int rows=jdbcTemplate.update(sql);	
		return rows > 0;
	}

	@Override
	public SqlRowSet queryMioInfoRoamTaskQueue(String[] status) {
		StringBuilder sbr = new StringBuilder();
		int index =0;
		for(String statu: status){
			if(index > 0){
				sbr.append(",");
			}
			sbr.append("'");
			sbr.append(statu);
			sbr.append("'");
			index++;
		}
		// 1.从产生应收付流水任务表查询STATUS N 新增或者 E 错误的数据       
		String sql = "select EXT_KEY0,EXT_KEY1,TASK_SEQ from MIO_INFO_ROAM_TASK_QUEUE where STATUS in ("+sbr.toString()+")";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		return row;
	}

	@Override
	public boolean updateMioInfoRoamTaskQueue(Long taskSeq, String state) {
		String sqlDate="";
		if("K".equals(state)){ 
			//开始执行时间
			sqlDate=" , START_TIME =sysdate ";
		}else if("C".equals(state)){
			//任务完成时间
			sqlDate=" , END_TIME =sysdate ";
		}
		String sql="update MIO_INFO_ROAM_TASK_QUEUE set  STATUS='"+state+"' "+sqlDate+"  where TASK_SEQ='"+taskSeq+"'";
		int rows= jdbcTemplate.update(sql);
		return rows >0;
	}

	@Override
	public SqlRowSet queryProcPlnmioTaskQueue(String status) {
		String sql = "select TASK_ID,APPL_NO from PROC_PLNMIO_TASK_QUEUE where STATUS =?";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql,status);
		return row;
	}

	@Override
	public boolean updateProcPlnmioTaskQueue(String status, String taskId) {
		String sqlDate="";
		if("K".equals(status)){ 
			//开始执行时间
			sqlDate=" , START_TIME =sysdate ";
		}else if("C".equals(status)){
			//任务完成时间
			sqlDate=" , END_TIME =sysdate ";
		}
		int rows= jdbcTemplate.update("update PROC_PLNMIO_TASK_QUEUE set STATUS='"+status+"' "+sqlDate+" where TASK_ID='"+taskId+"'");
		return rows>0;
	}
}
