package com.newcore.orbpsutils.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.dao.util.DaoUtils;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbpsutils.dao.api.MioLogDao;
import com.newcore.orbpsutils.math.DateUtils;


@Repository("mioDao")
public class MioLogDaoImpl implements MioLogDao {

	@Autowired
	JdbcOperations jdbcTemplate;

	private final static String TABLE_NAME = "MIO_LOG";
	private final static String GET_MIO_LOG_ID = "select S_MIO_LOG.Nextval from dual";
	private final static String GET_BAT_NO = "select S_MIO_LOG_BAT_NO.Nextval from dual";
	private final static String INSERT_ONE_MIO_LOG_DATA = "insert into mio_log ";
	private final static String UPDATE_CORESTAT_BY_BATNO = "update mio_log set core_stat=? where bat_no=?";


	@Override
	public boolean insertMioLogList(List<MioLog> mlList) {

		StringBuffer sql = new StringBuffer();
		sql.append(INSERT_ONE_MIO_LOG_DATA);
		sql.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		//表中共55个字段，
		int[][] rows = jdbcTemplate.batchUpdate(sql.toString(), mlList, mlList.size(),
				new ParameterizedPreparedStatementSetter<MioLog>() {
			@Override
			public void setValues(PreparedStatement ps, MioLog mioLog) throws SQLException {
				ps.setLong(1, mioLog.getMioLogId());
				ps.setLong(2, mioLog.getPlnmioRecId());
				ps.setString(3, mioLog.getPolCode());
				ps.setString(4, mioLog.getCntrType());
				ps.setString(5, mioLog.getCgNo());
				ps.setString(6, mioLog.getSgNo());
				ps.setString(7, mioLog.getCntrNo());
				ps.setString(8, mioLog.getCurrencyCode());
				ps.setLong(9, mioLog.getMtnId());
				ps.setString(10, mioLog.getMtnItemCode());

				ps.setLong(11, mioLog.getIpsnNo());
				ps.setString(12, mioLog.getLevelCode());
				ps.setLong(13, mioLog.getFeeGrpNo());
				ps.setString(14, mioLog.getMioCustNo());
				ps.setString(15, mioLog.getMioCustName());
				ps.setDate(16, DateUtils.getDate(mioLog.getPlnmioDate()));
				ps.setDate(17, DateUtils.getDate(mioLog.getMioDate()));
				ps.setDate(18, DateUtils.getDate(new Date()));
				ps.setDate(19, DateUtils.getDate(mioLog.getPremDeadlineDate()));
				ps.setString(20, mioLog.getMioItemCode());

				ps.setString(21, mioLog.getMioType());
				ps.setString(22, mioLog.getMgrBranchNo());
				ps.setString(23, mioLog.getPclkBranchNo());
				ps.setString(24, mioLog.getPclkNo());
				ps.setString(25, mioLog.getSalesBranchNo());
				ps.setString(26, mioLog.getSalesChannel());
				ps.setString(27, mioLog.getSalesNo());
				ps.setInt(28, mioLog.getMioClass());
				ps.setBigDecimal(29, mioLog.getAmnt());
				ps.setString(30, mioLog.getAccCustIdType());

				ps.setString(31, mioLog.getAccCustIdNo());
				ps.setString(32, mioLog.getBankCode());
				ps.setString(33, mioLog.getBankAccName());
				ps.setString(34, mioLog.getBankAccNo());
				ps.setInt(35, mioLog.getMioTxClass());
				ps.setLong(36, mioLog.getMioTxNo());
				ps.setDate(37, null);
				ps.setLong(38, mioLog.getCorrMioTxNo());
				ps.setString(39, mioLog.getReceiptNo());
				ps.setString(40, mioLog.getVoucherNo());

				ps.setDate(41, null);
				ps.setString(42, mioLog.getClearingMioTxNo());
				ps.setString(43, mioLog.getMioProcFlag());
				ps.setString(44, mioLog.getRouterNo());
				ps.setLong(45, mioLog.getAccId());
				ps.setString(46, mioLog.getCoreStat());
				ps.setLong(47, mioLog.getTransCode());
				ps.setLong(48, mioLog.getBtMioTxNo());
				ps.setLong(49, mioLog.getBatNo());
				ps.setString(50, mioLog.getRemark());

				ps.setDate(51, DateUtils.getDate(new Date()));
				ps.setDouble(52, mioLog.getNetIncome());
				ps.setDouble(53, mioLog.getVat());
				ps.setString(54, mioLog.getVatId());
				ps.setDouble(55, mioLog.getVatRate());
			}
		});
		return rows.length > 0;
	}

	@Override
	public boolean insertOneMioLog(MioLog ml) {
		// TODO Auto-generated method stub
		Assert.notNull(ml);
		SimpleJdbcInsert insert = DaoUtils.createJdbcInsert();
		insert.setTableName(TABLE_NAME);
		ml.setMioLogId(getMioLogIdSeq());
		ml.setBatNo(getBatNo());
		int i = insert.execute(DaoUtils.toColumnMap(ml));
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public long getMioLogIdSeq() {
		return jdbcTemplate.queryForObject(GET_MIO_LOG_ID, long.class);
	}

	@Override
	public long getBatNo() {
		return jdbcTemplate.queryForObject(GET_BAT_NO, long.class);
	}

	@Override
	public boolean updateMioLogStat(long batNo, String coreStat) {
		Assert.notNull(batNo);
		Assert.notNull(coreStat);
		int i = jdbcTemplate.update(UPDATE_CORESTAT_BY_BATNO, coreStat, batNo);
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<MioLog> queryMioLogList(Map<String, String> param) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from mio_log ");
		sql.append(" WHERE ");
		int index = 0;
		for (Entry<String, String> entry : param.entrySet()) {
			String key = entry.getKey();
			String val = entry.getValue();
			if (index > 0) {
				//第一次遍历的时候 查询条件不需要 and
				sql.append(" and ");
			}
			if("cntr_no".equals(key)){
				sql.append("instr("+key+" ,'" + val + "') >0 ");
			}else{
				sql.append(key + " = '" + val + "'");
			}
			index++;
		}
		List<MioLog> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<MioLog>(MioLog.class));
		return list;
	}

	@Override
	public boolean updateMioLog(long mioLogId , Integer mioTxClass) {
		StringBuffer sql = new StringBuffer();
		sql.append("UPDATE mio_log SET MIO_TX_CLASS = '"); 
		sql.append(mioTxClass);
		sql.append("'  WHERE MIO_LOG_ID = '");
		sql.append(mioLogId);
		sql.append("'");
		int row=jdbcTemplate.update(sql.toString());
		return row>0;
	}

	@Override
	public Double getMioLogSumAmnt(Integer mioClass , String mioItemCode , String mioType , String applNo , Integer mioTxClass) {
		StringBuilder sql = new StringBuilder();
		sql.append("select sum(amnt) from mio_log  ");
		sql.append(" WHERE instr(cntr_No,'");
		sql.append(applNo);
		sql.append("') >0 ");
		sql.append(" AND mio_class ='");
		sql.append(mioClass);
		sql.append("'");
		sql.append(" AND mio_item_code ='");
		sql.append(mioItemCode);
		sql.append("'");
		sql.append(" AND mio_type ='");
		sql.append(mioType);
		sql.append("'");
		sql.append(" AND MIO_TX_CLASS ='");
		sql.append(mioTxClass);
		sql.append("'");
		return (Double) jdbcTemplate.queryForObject(sql.toString(), Double.class);
	}

	@Override
	public List<MioLog> queryMioLogList(String cgNo) {
		StringBuilder sql = new StringBuilder();
		sql.append("select * from mio_log  ");
		sql.append(" WHERE cg_no ='");
		sql.append(cgNo);
		sql.append("'");
		List<MioLog> list = jdbcTemplate.query(sql.toString(), new CustomBeanPropertyRowMapper<MioLog>(MioLog.class));
		return list;
	}

	@Override
	public Date getMioLogUpdTime(String no,String type) {
		StringBuilder sql = new StringBuilder();
		if("APPL".equals(type)){
			sql.append("select MIN(mio_log_upd_time) minDate from mio_log  ");
			sql.append(" WHERE instr(cntr_No,'");
			sql.append(no);
			sql.append("') >0 ");
			sql.append(" AND mio_class ='-1'");	
			sql.append(" AND mio_type ='S'");
		}else if("CGNO".equals(type)){
			sql.append("select MAX(mio_log_upd_time) maxDate from mio_log  ");
			sql.append(" WHERE cg_no='");
			sql.append(no);
			sql.append("'");
			sql.append(" AND mio_class ='1'");	
			sql.append(" AND mio_type <>'S'");
		}
		sql.append(" AND mio_item_code ='FA'");	
		sql.append(" AND MIO_TX_CLASS ='0'");
		return jdbcTemplate.queryForObject(sql.toString(), Date.class);
	}

}
