package com.newcore.orbpsutils.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.models.banktrans.BtBranchContrast;
import com.newcore.orbpsutils.dao.api.BtBranchContrastDao;

@Repository("btBranchContrastDao")
public class BtBranchContrastDaoImpl implements BtBranchContrastDao {
	
	@Autowired
	JdbcOperations jdbcTemplate;

	@Override
	public BtBranchContrast queryBtBranchContrastInfo(BtBranchContrast bt) {
		Assert.notNull(bt);
		
		StringBuilder sql = new StringBuilder();
		sql.append("select * from bt_branch_contrast where bank_code='");
		sql.append(bt.getBankCode());
		sql.append("' and pclk_branch_no='");
		sql.append(bt.getPclkBranchNo());
		sql.append("' and service_dept_no='");
		sql.append(bt.getServiceDeptNo());
		sql.append("' and mio_type='");
		sql.append(bt.getMioType());
		sql.append("' and mio_class=");
		sql.append(bt.getMioClass());
		
		BtBranchContrast btBranch = null;
		btBranch = (BtBranchContrast) jdbcTemplate.queryForObject(sql.toString(), new CustomBeanPropertyRowMapper<BtBranchContrast>(BtBranchContrast.class));
		
		return btBranch;
	}

}
