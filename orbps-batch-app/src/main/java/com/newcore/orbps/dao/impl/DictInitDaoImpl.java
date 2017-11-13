package com.newcore.orbps.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.dao.api.DictInitDao;
import com.newcore.orbps.models.distcode.OccCodeInfo;

@Repository("dictInitDaoImpl")
public class DictInitDaoImpl implements DictInitDao {

	@Autowired
	JdbcOperations jdbcTemplate;

	/**
	 * 查询字典集合
	 */
	@Override
	public List<OccCodeInfo> queryDict() {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT OCC_CLASS_CODE,OCC_SUBCLS_CODE,OCC_CODE,OCC_NAME_ENG,OCC_NAME_CHN,OCC_DANGER_FACTOR FROM OCC_CODE ");
		List<OccCodeInfo> list = jdbcTemplate
				.query(sql.toString(),
						new CustomBeanPropertyRowMapper<OccCodeInfo>(
								OccCodeInfo.class));
		return list;
	}

}
