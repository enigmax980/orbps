package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.QueryGrpInsurApplOperTraceService;



/**
 * @author huanglong
 * @date 2016年9月24日
 * 内容:保单操作轨迹查询服务实现类
 */
@Service("queryOrbpsGrpInsurApplOperTraceService")
public class QueryGrpInsurApplOperTraceServiceImpl implements QueryGrpInsurApplOperTraceService {

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public RetInfoObject<InsurApplOperTrace> queryGrpInsurApplOperTrace(Map<String, Object> map) {

		RetInfoObject<InsurApplOperTrace> retInfoObject = new RetInfoObject<>();
		if(map == null || map.isEmpty()){
			retInfoObject.setRetCode("0");
			retInfoObject.setErrMsg("入参为空!");
			return retInfoObject;
		}
		/*组查询条件语句*/				
		Criteria criteria = new Criteria();
		/*投保单号*/
		if(map.containsKey("applNo")){
			criteria.and("applNo").is(map.get("applNo"));
		}
		/*销售渠道*/
		if(map.containsKey("salesChannel")){
			criteria.and("salesInfoList.salesChannel").is(map.get("salesChannel"));
		}
		/*销售机构号*/
		if(map.containsKey("salesBranchNo")){
			criteria.and("salesInfoList.salesBranchNo").is(map.get("salesBranchNo"));
		}
		/*销售员代码*/
		if(map.containsKey("salesNo")){
			criteria.and("salesInfoList.salesNo").is(map.get("salesNo"));
		}
		if(map.containsKey("beginDate") && map.containsKey("endDate")){
			criteria.andOperator(Criteria.where("applDate").gt(new Date(Long.parseLong(JSONObject.toJSONString(map.get("beginDate"))))),Criteria.where("applDate").lt(new Date(Long.parseLong(JSONObject.toJSONString(map.get("endDate"))))));
		}else if(map.containsKey("beginDate")){
			criteria.and("applDate").gt(new Date(Long.parseLong(JSONObject.toJSONString(map.get("beginDate")))));
		}else if(map.containsKey("endDate")){
			criteria.and("applDate").lt(new Date(Long.parseLong(JSONObject.toJSONString(map.get("endDate")))));
		}


		List<GrpInsurAppl> grpInsurAppls = mongoTemplate.find(Query.query(criteria), GrpInsurAppl.class);
		if(grpInsurAppls.isEmpty()){
			retInfoObject.setRetCode("0");
			retInfoObject.setErrMsg("没有查询到相关信息");
			return retInfoObject;
		}
		List<InsurApplOperTrace> insurApplOperTraces = new ArrayList<>();
		for (GrpInsurAppl grpInsurAppl : grpInsurAppls){
			InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(grpInsurAppl.getApplNo())), InsurApplOperTrace.class);
			/*如果有新单状态 newApplState 找出所处 newApplState 的单子*/
			if(map.containsKey("newApplState") && !StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(),(String)map.get("newApplState"))){
				continue;
			}
			insurApplOperTraces.add(insurApplOperTrace);
		}
		retInfoObject.setListObject(insurApplOperTraces);
		retInfoObject.setRetCode("1");
		retInfoObject.setErrMsg("查询成功!");
		return retInfoObject;
	}
}
