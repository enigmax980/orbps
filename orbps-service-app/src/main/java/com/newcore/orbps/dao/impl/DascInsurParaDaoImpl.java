package com.newcore.orbps.dao.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;

/**
 * @author wangxiao
 * 创建时间：2016年9月20日下午3:11:26
 */
@Repository("dascInsurParaDao")
public class DascInsurParaDaoImpl implements DascInsurParaDao {
	private static final String INSERT_SQL = "INSERT INTO  DASC_INSUR_PARA(BUSINESS_KEY,APPL_NO,CNTR_TYPE,LIST_FILE_PATH,IS_INSURED_LIST,CREATE_DATE) VALUES (?,?,?,?,?,?)";
	private static final String SELECT_APPLNO_SQL = "SELECT APPL_NO FROM DASC_INSUR_PARA WHERE BUSINESS_KEY =?";
	private static final String SELECT_LISTPATH_SQL = "SELECT LIST_FILE_PATH FROM DASC_INSUR_PARA WHERE BUSINESS_KEY =?";
	private static final String SELECT_SQL = "SELECT * FROM DASC_INSUR_PARA WHERE BUSINESS_KEY =?";
	private static final String DELETE_SQL = "DELETE FROM DASC_INSUR_PARA WHERE BUSINESS_KEY =?";
	/**
     *  JDBC操作工具
     */
    @Autowired
    JdbcOperations jdbcTemplate;
    
	@Override
	public DascInsurParaBo add(DascInsurParaBo dascInsurParaBo) {
		Date date = new Date();
		dascInsurParaBo.setBusinessKey(dascInsurParaBo.getApplNo());
		dascInsurParaBo.setCreateDate(date);
		int i = jdbcTemplate.update(INSERT_SQL,dascInsurParaBo.getApplNo(),dascInsurParaBo.getApplNo(),dascInsurParaBo.getCntrType(),dascInsurParaBo.getListFilePath(),dascInsurParaBo.getIsInsuredList(),date);
		if(i==1){
			return dascInsurParaBo;
		}else{
			return null;
		}
	}
	@Override
	public String selectApplNo(String businessKey){
		String [] sarr = {businessKey};
		String applNo = jdbcTemplate.queryForObject(SELECT_APPLNO_SQL,sarr, String.class);
		return applNo;
	}
	@Override
	public String selectListPath(String businessKey) {
		String [] sarr = {businessKey};
		String listPath = jdbcTemplate.queryForObject(SELECT_LISTPATH_SQL,sarr, String.class);
		return listPath;
	}
	@Override
	public DascInsurParaBo select(String businessKey) {
		SqlRowSet row = jdbcTemplate.queryForRowSet(SELECT_SQL,businessKey);
		DascInsurParaBo dascInsurParaBo = new DascInsurParaBo();
		if(row.next()){
			dascInsurParaBo.setBusinessKey(row.getString(1));
			dascInsurParaBo.setApplNo(row.getString(2));
			dascInsurParaBo.setCntrType(row.getString(3));
			dascInsurParaBo.setListFilePath(row.getString(4));
			dascInsurParaBo.setIsInsuredList(row.getString(5));
			dascInsurParaBo.setCreateDate(row.getDate(6));
		}
		return dascInsurParaBo;
	}
	@Override
	public int delete(String businessKey) {
		int i = jdbcTemplate.update(DELETE_SQL, businessKey);
		return i;
	}
	
}
