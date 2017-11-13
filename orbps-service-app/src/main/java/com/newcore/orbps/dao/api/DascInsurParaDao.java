package com.newcore.orbps.dao.api;

import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;

/**
 * @author wangxiao
 * 创建时间：2016年9月20日下午3:07:18
 */
public interface DascInsurParaDao {
	public DascInsurParaBo add(DascInsurParaBo dascInsurParaBo);
	public String selectApplNo(String businessKey);
	public String selectListPath(String businessKey);
	public DascInsurParaBo select(String businessKey);
	public int delete(String businessKey);
}
