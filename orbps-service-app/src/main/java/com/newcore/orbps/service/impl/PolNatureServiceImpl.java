package com.newcore.orbps.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.dao.api.PolNatureInfoDao;
import com.newcore.orbps.service.api.PolNatureService;

@Service("polNatureService")
public class PolNatureServiceImpl implements PolNatureService {

	@Autowired
	PolNatureInfoDao polNatureInfoDao;

	@Override
	public List<JSONObject> getPolNatureInfo(List<String> polCodes) {
		return polNatureInfoDao.getPolNatureInfo(polCodes);
	}
	
}
