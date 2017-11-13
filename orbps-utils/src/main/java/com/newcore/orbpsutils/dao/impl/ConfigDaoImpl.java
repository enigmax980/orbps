package com.newcore.orbpsutils.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import com.newcore.orbpsutils.dao.api.ConfigDao;

@Repository("ConfigDao")
public class ConfigDaoImpl implements ConfigDao {
	@Autowired
	JdbcOperations jdbcTemplate;

	@Override
	public String queryPropertiesConfigure(String propertieType, String pavaVal, String brcnchNo) {

		StringBuilder sbr = new StringBuilder();
		SqlRowSet row;
		String str = "N";
		sbr.append("SELECT p.IS_VALID as isValid");
		sbr.append(" FROM PROPERTIES_CONFIGURE p,CONFIG_BRANCH_NO cbn,CONFIG_VAL cv");
		sbr.append(" WHERE p.id=cbn.id and p.id=cv.id");
		sbr.append(" AND p.PROPERTIE_TYPE=?");
		if (StringUtils.isBlank(brcnchNo)) {
			brcnchNo = "!!!!!!";
		}
		sbr.append(" AND cbn.BRANCH_NO=?");
		if (!StringUtils.isBlank(pavaVal)) {
			sbr.append(" AND cv.PARA_VAL=?");
			 row = jdbcTemplate.queryForRowSet(sbr.toString(), propertieType, brcnchNo, pavaVal);
		}else{
			 row = jdbcTemplate.queryForRowSet(sbr.toString(), propertieType, brcnchNo);
		}
		while (row.next()) {
			str = row.getString("isValid");
		}
		return str;
	}

}
