package com.newcore.orbpsutils.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbpsutils.dao.api.OldPrintTaskQueueDao;

/**
 * 打印任务控制表
 * @author JCC
 * 2017年5月17日 16:09:05
 */
@Repository("oldPrintTaskQueueDao")
public class OldPrintTaskQueueDaoImpl implements OldPrintTaskQueueDao{

	@Autowired
    JdbcOperations jdbcTemplate;
	
	@Override
	public int delData(String applNo) {
		String sql ="delete old_print_task_queue where APPL_NO=? ";	
		return jdbcTemplate.update(sql,applNo);
	}

	@Override
	public int insertData(String taskId, String applNo) {
		String	insertsql="insert into old_print_task_queue(TASK_SEQ,STATUS,CREATE_TIME,TASK_ID,BUSINESS_KEY,APPL_NO)"
				+"values(S_OLD_PRINT_TASK_QUEUE.NEXTVAL,'N',sysdate,'"+taskId+"','"+applNo+"','"+applNo+"')";
		return jdbcTemplate.update(insertsql);	
	}

	@Override
	public String findTaskByApplNo(String applNo) {
		String sql = "select task_id from old_print_task_queue where APPL_NO='"+applNo+"'";
		return jdbcTemplate.queryForObject(sql, String.class)	;
	}

	@Override
	public int updateDateByApplNo(String applNo, String status) {
		String sql ="update old_print_task_queue set status=? ,end_time=sysdate where APPL_NO=? ";	
		return jdbcTemplate.update(sql,status,applNo);
	}

	@Override
	public SqlRowSet queryOldPrintQueue(String[] status) {
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
		String sql = "select APPL_NO from old_print_task_queue where STATUS in ("+sbr.toString()+")";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		return row;
	}

	@Override
	public int findCountyApplNo(String applNo) {
		String sql = "select count(appl_no) as appl_no from old_print_task_queue where APPL_NO='"+applNo+"'";
		return jdbcTemplate.queryForObject(sql, Integer.class)	;
	}

	@Override
	public String checkPrintIsOnLanging(String applNo) {
		String sql ="select count(1) from old_print_task_queue t where t.business_key ='"+applNo+"'";	
		int countPrint = jdbcTemplate.queryForObject(sql,Integer.class);
		if(0==countPrint){//0---不存在打印在途数据
			return "0";
		}else{
			String sql2 ="select status from old_print_task_queue t where t.business_key ='"+applNo+"'";
			String printResult =	jdbcTemplate.queryForObject(sql2, String.class)	;
			if(StringUtils.equals("S", printResult)){
				return "0";//0---不存在打印在途数据
			}else{
				return "1";//0---存在打印在途数据
			}//end status
		}//end is exist 
	}


}
