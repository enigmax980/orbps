package com.newcore.orbps.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.QueryGrpInsurApplService;

@Service("queryGrpInsurApplServices")
public class QueryGrpInsurApplServicesImpl implements QueryGrpInsurApplService {
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<GrpInsurAppl> getGrpInsurAppls(Map<String, Object> map) {
		/*根据条件从回执核销表中查询相应数据 */
		Query query = new Query();
		for (Map.Entry<String, Object> key : map.entrySet()) {
			if ("page".equals(key.getKey()) || "size".equals(key.getKey())) continue;
			query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));				
		}
		if(map.containsKey("page") && map.containsKey("size") ){
			query.skip(((Integer) map.get("page") - 1) * (Integer) map.get("size"));
			query.limit((Integer) map.get("size"));
		}
		return (List<GrpInsurAppl>) mongoBaseDao.find(GrpInsurAppl.class, query);
	}
}
