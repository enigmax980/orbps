package com.newcore.orbps.service.business;

import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;

/**
 * 数据处理器接口
 * @author wangxiao
 * 创建时间：2016年7月20日下午3:54:32
 */
public interface InsurApplProcessor {
	/**
	 * 插入处理
	 * @param mongoBaseDao 
	 * @param InsurApplString
	 * @return
	 */
	public RetInfo addInsurAppl(GrpInsurAppl grpInsurAppl, MongoBaseDao mongoBaseDao);
	/**
	 * 查询处理
	 * @param InsurApplString
	 * @return
	 */
	public Object searchInsurAppl(String applNo, MongoBaseDao mongoBaseDao);
	/**
	 * 修改处理
	 * @param InsurApplString
	 * @return
	 */
	public RetInfo modifyInsurAppl(GrpInsurAppl grpInsurAppl, MongoBaseDao mongoBaseDao);
}
