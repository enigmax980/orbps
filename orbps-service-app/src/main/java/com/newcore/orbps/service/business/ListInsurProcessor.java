package com.newcore.orbps.service.business;

import org.springframework.jdbc.core.JdbcOperations;

import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 清汇数据处理器实现
 * @author wangxiao
 * 创建时间：2016年7月20日下午3:59:29
 */
public class ListInsurProcessor implements InsurApplProcessor {

	@Override
	public RetInfo addInsurAppl(GrpInsurAppl grpInsurAppl,MongoBaseDao mongoBaseDao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object searchInsurAppl(String applNo,MongoBaseDao mongoBaseDao) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RetInfo modifyInsurAppl(GrpInsurAppl grpInsurAppl,MongoBaseDao mongoBaseDao) {
		// TODO Auto-generated method stub
		return null;
	}
}
