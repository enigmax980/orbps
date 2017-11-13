package com.newcore.orbps.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.StringUtils;

import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;

public class TaskPrmyRowMapper implements RowMapper<TaskPrmyInfo>{
	
	private final static String pattern = "yyyy-MM-dd HH:mm:ss";

	@Override
	public TaskPrmyInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
		taskPrmyInfo.setTaskSeq(rs.getLong("TASK_SEQ"));
		taskPrmyInfo.setStatus(rs.getString("STATUS"));
		taskPrmyInfo.setSalesBranchNo(rs.getString("SALES_BRANCH_NO"));
		if (!StringUtils.isEmpty(rs.getString("CREATE_TIME"))) {
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(rs.getDate("CREATE_TIME"), pattern));
		}
		if (!StringUtils.isEmpty(rs.getString("START_TIME"))) {
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(rs.getDate("START_TIME"), pattern));
		}
		if (!StringUtils.isEmpty(rs.getString("END_TIME"))) {
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(rs.getDate("END_TIME"), pattern));
		}
		if (!StringUtils.isEmpty(rs.getString("ASK_TIMES"))) {
			taskPrmyInfo.setAskTimes(rs.getInt("ASK_TIMES"));
		}
		if (!StringUtils.isEmpty(rs.getString("BUSINESS_KEY"))) {
			taskPrmyInfo.setBusinessKey(rs.getString("BUSINESS_KEY"));
		}
		taskPrmyInfo.setTaskId(rs.getString("TASK_ID"));
		taskPrmyInfo.setApplNo(rs.getString("APPL_NO"));
		taskPrmyInfo.setListPath(rs.getString("LIST_PATH"));
		taskPrmyInfo.setJobInstanceId(rs.getLong("JOB_INSTANCE_ID"));
		taskPrmyInfo.setRemark(rs.getString("REMARK"));
		taskPrmyInfo.setExtKey0(rs.getString("EXT_KEY0"));
		taskPrmyInfo.setExtKey1(rs.getString("EXT_KEY1"));
		taskPrmyInfo.setExtKey2(rs.getString("EXT_KEY2"));
		taskPrmyInfo.setExtKey3(rs.getString("EXT_KEY3"));
		taskPrmyInfo.setExtKey4(rs.getString("EXT_KEY4"));
		taskPrmyInfo.setExtKey4(rs.getString("EXT_KEY5"));
		
		return taskPrmyInfo;
	}

}
