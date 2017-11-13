package com.newcore.orbpsutils.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbpsutils.dao.api.MioAccInfoLogDao;
import com.newcore.orbpsutils.math.DateUtils;
@Repository("mioAccInfoLogDao")
public class MioAccInfoLogDaoImpl implements MioAccInfoLogDao {
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public long queryAccLogId() {
		String sql="select S_MIO_ACC_INFO_LOG.nextval from dual";
		return jdbcTemplate.queryForObject(sql, Long.class);
	}
	
	@Override
	public boolean insertMioAccInfoLogList(List<MioAccInfoLog> accList) {
		String sql="insert into MIO_ACC_INFO_LOG values(?,?,?,?,?,?,?)";
		int[][] rows=jdbcTemplate.batchUpdate(sql.toString(), accList, accList.size(), new ParameterizedPreparedStatementSetter<MioAccInfoLog>(){
			@Override
			public void setValues(PreparedStatement ps, MioAccInfoLog data) throws SQLException {
				ps.setLong(1,data.getAccLogId());	//账户明细标识
				ps.setLong(2,data.getAccId());		//账户标识
				ps.setTimestamp(3,DateUtils.getTimestamp(data.getCreateTime())); //发生日期
				ps.setString(4,data.getMioItemCode());	//收付项目代码
				ps.setInt(5,data.getMioClass());		//收付类型： 1：收，-1：付
				ps.setBigDecimal(6,data.getAnmt());		//收付金额
				ps.setString(7,data.getRemark()); 		//备注
			}
		});
		return rows.length>0;
	}

}
