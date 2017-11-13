package com.newcore.orbps.service.api;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * @author wangxiao
 * 创建时间：2016年8月24日下午3:12:54
 */
public interface CustomerAccountService {
	/**
	 * 客户开户，获取客户号
	 * CustomerAccountService
	 * List<JSONObject>
	 */
	public List<JSONObject> custaccount(List<Map<String, Object>> jsonObjects) throws Exception;
	/**
	 * 客户号回写
	 * CustomerAccountService
	 * void
	 */
	public void update(Map<Long, GrpInsured> grpInsuredMap, List<JSONObject> retJSONObjects);
}
