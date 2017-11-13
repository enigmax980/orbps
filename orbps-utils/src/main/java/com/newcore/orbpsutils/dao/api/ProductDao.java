package com.newcore.orbpsutils.dao.api;

import com.newcore.orbps.models.service.bo.Policy;

/**
 * 契约规则管理Dao层接口	
 * @author Administrator
 *
 */
public interface ProductDao {

	/**
	 * 获取销售对象
	 * @param productCode
	 * @return
	 */
	public String getSalesTaget(String productCode);

	/**
	 * 获取保险期类型
	 * @param polCode
	 * @return
	 */
	public int getInsurDurCnt(String polCode,Integer insurDur,String insurDurUnit);

	/**
	 * 获取险种信息
	 * @param polCode
	 * @return
	 */
	public Policy getPolicyInfo(String polCode);

}
