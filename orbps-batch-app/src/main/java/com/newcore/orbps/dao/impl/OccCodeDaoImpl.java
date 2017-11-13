package com.newcore.orbps.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import com.newcore.orbps.dao.api.OccCodeDao;

/**
 * @author wangxiao
 * 创建时间：2016年9月8日下午3:47:41
 */
@Repository("occCodeDao")
public class OccCodeDaoImpl implements OccCodeDao {

	/**
     * ; JDBC操作工具
     */
    @Autowired
    JdbcOperations jdbcTemplate;
    
	@Override
	public String searchIpsnOccClassLevel(String ipsnOccClassCode) {
		// TODO Auto-generated method stub
		String occClassCode = ipsnOccClassCode.substring(0,2);
		String occSubclsCode = ipsnOccClassCode.substring(2,4);
		String occCode = ipsnOccClassCode.substring(4,6);
		StringBuilder sql = new StringBuilder("SELECT OCC_DANGER_FACTOR FROM OCC_CODE WHERE OCC_CLASS_CODE = '");
		sql.append(occClassCode);
		sql.append("'");
		sql.append(" AND OCC_SUBCLS_CODE = '");
		sql.append(occSubclsCode);
		sql.append("'");
		sql.append(" AND OCC_CODE = '");
		sql.append(occCode);
		sql.append("'");
		String occDangerFactor = jdbcTemplate.queryForObject(sql.toString(), String.class);
		return occDangerFactor;
	}

}
