package com.newcore.orbps.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.dao.api.PolNatureInfoDao;
@Repository("polNatureInfoDao")
public class PolNatureInfoDaoImpl implements PolNatureInfoDao {
	
	@Autowired
    JdbcOperations jdbcTemplate;

	private final String SELECT_POL_NATURE_SQL = "SELECT * FROM POL_NATURE_INFO WHERE POL_CODE in ";
	
	@Override
	public List<JSONObject> getPolNatureInfo(List<String> polCodes) {
		List<JSONObject> jsonObjects = new ArrayList<>();
		if(polCodes==null){
			return jsonObjects;
		}
		StringBuilder sql = new StringBuilder(SELECT_POL_NATURE_SQL);
		sql.append("(");
		for(int i=0;i<polCodes.size();i++){
			sql.append("'");
			sql.append(polCodes.get(i));
			sql.append("'");
			if(i+1!=polCodes.size()){
				sql.append(",");
			}
		}
		sql.append(")");
		SqlRowSet rs = jdbcTemplate.queryForRowSet(sql.toString());
		while(rs.next()){
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("polCode", rs.getString("POL_CODE"));
			jsonObject.put("isFund", rs.getString("IS_FUND"));
			jsonObject.put("isHealth", rs.getString("IS_HEALTH"));
			jsonObject.put("isConstruct", rs.getString("IS_CONSTRUCT"));
			jsonObject.put("speciLogoGrpNo", rs.getString("SPECI_LOGO_GRP_NO"));
			jsonObjects.add(jsonObject);
		}
		return jsonObjects;
	}
}
