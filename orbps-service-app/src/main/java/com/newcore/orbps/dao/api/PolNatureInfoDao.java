package com.newcore.orbps.dao.api;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public interface PolNatureInfoDao {
	
	public List<JSONObject> getPolNatureInfo(List<String> polCodes);
}
