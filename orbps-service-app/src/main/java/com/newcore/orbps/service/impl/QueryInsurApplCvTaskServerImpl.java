package com.newcore.orbps.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.InsurApplCvTask;
import com.newcore.orbps.models.service.bo.QueryReceipt;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.QueryInsurApplCvTaskServer;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.supports.dicts.CNTR_SEND_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * @author huanglong
 * @date 2016年9月7日
 * 内容:查询回执核销
 */

@Service("queryInsurApplCvTaskServer")
public class QueryInsurApplCvTaskServerImpl implements QueryInsurApplCvTaskServer{

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	BranchService branchService;
	
	
	private static Logger logger = LoggerFactory.getLogger(QueryInsurApplCvTaskServerImpl.class);
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");	
	@Override
	public PageData<QueryReceipt> queryInsurApplCvTask(Map<String, Object> map){
		PageData<QueryReceipt> pageData = new PageData<>();
		List<QueryReceipt> queryReceipts = new ArrayList<>();
		if(null == map){
			map = new HashMap<>();
		}
		/*根据条件从回执核销表中查询相应数据 */		
		Query query = new Query();			
		for (Map.Entry<String, Object> key : map.entrySet()) {
			if ("page".equals(key.getKey())|| "size".equals(key.getKey()) || "startDate".equals(key.getKey())
					|| "endDate".equals(key.getKey())
					|| "salesBranchNo".equals(key.getKey())
					|| "status".equals(key.getKey())
					|| "levelFlag".equals(key.getKey())) continue;
			query.addCriteria(Criteria.where(key.getKey()).is(key.getValue()));				
		}
		/*机构号赋值*/
		if(map.containsKey("salesBranchNo")){
			List<String> salesBranchNos = new ArrayList<>();	
			salesBranchNos.add(String.valueOf(map.get("salesBranchNo")));			
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),String.valueOf(map.get("levelFlag")))){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(String.valueOf(map.get("salesBranchNo")));
				salesBranchNos.addAll(BranchNoUtils.getAllSubBranchNo(branch));
			}
			query.addCriteria(Criteria.where("salesBranchNo").in(salesBranchNos));
		}
		
		try {
			/*按核销时间查询*/
			if (map.containsKey("startDate") && map.containsKey("endDate")) {
				query.addCriteria(new Criteria().andOperator(Criteria.where("respDate").gte(dateFormat.parse(String.valueOf(map.get("startDate")))),Criteria.where("respDate").lte(dateFormat.parse(String.valueOf(map.get("endDate"))))));
			}else if(map.containsKey("startDate")){
				query.addCriteria(Criteria.where("respDate").gte(dateFormat.parse(String.valueOf(map.get("startDate")))));
			}else if (map.containsKey("endDate")){
				query.addCriteria(Criteria.where("respDate").lte(dateFormat.parse(String.valueOf(map.get("endDate")))));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","时间类型转换异常!");
		}
		if(map.containsKey("status")){
			query.addCriteria(Criteria.where("status").is(map.get("status")));
		}else {
			List<String> status = new ArrayList<>();
			status.add("N");
			status.add("C");
			query.addCriteria(Criteria.where("status").in(status));
		}		
		long recordSum = mongoTemplate.count(query, InsurApplCvTask.class);
		pageData.setTotalCount(recordSum);
		/*分页查询*/
		if(map.containsKey("page") && map.containsKey("size") ){
			int page = (Integer) map.get("page");
			int size = (Integer) map.get("size");
			query.skip((page <=0?0:(page -1)) * size);
			query.limit(size);
		}
		List<InsurApplCvTask> insurApplCvTasks = mongoTemplate.find(query, InsurApplCvTask.class);
		for (InsurApplCvTask insurApplCvTask : insurApplCvTasks) {
			QueryReceipt queryReceipt = new QueryReceipt();
			assignment(insurApplCvTask,queryReceipt);
			queryReceipts.add(queryReceipt);		
		}
		pageData.setData(queryReceipts);
		return pageData;
	}

	@Override
	public Map<String, Object> queryExecuteState(Map<String, String> map) {
		if(null == map || map.isEmpty()){
			throw new BusinessException("0002", "查询批量回执核销入参为空。");
		}
		if(StringUtils.isEmpty(map.get("batNo"))){
			throw new BusinessException("0002", "查询批量回执核销入参批次号为空。");
		}
		Map<String, Object> mapRet = new HashMap<>();
		
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("batNo").is(map.get("batNo"))),
				Aggregation.project("status","description"),
				Aggregation.group("status").count().as("sum").push("description").as("description")
				);
		AggregationResults<JSONObject> aggregationResults = mongoTemplate.aggregate(aggregation, InsurApplCvTask.class, JSONObject.class);		
		List<JSONObject> resultList = aggregationResults.getMappedResults();		
		if(resultList.isEmpty()){
			return mapRet;
		}
		long checkFail = 0;/*校验失败数*/
		long succ = 0;
		long fail = 0;
		for(JSONObject jsonObject:resultList){
			if(StringUtils.equals("E", jsonObject.getString("_id"))){
				mapRet.put("fail", jsonObject.getJSONArray("description").getString(0));
				return mapRet;
			}
			if(StringUtils.equals("T", jsonObject.getString("_id"))){
				String num[] = jsonObject.getJSONArray("description").getString(0).split("\\|");
				mapRet.put("sum", Long.valueOf(num[0]));
				checkFail = Integer.valueOf(num[1]);
			}
			if(StringUtils.equals("C", jsonObject.getString("_id"))){
				succ = jsonObject.getIntValue("sum");
			}
			if(StringUtils.equals("F", jsonObject.getString("_id"))){/*回执核销失败*/
				fail = jsonObject.getIntValue("sum");
			}	
		}
		mapRet.put("succNum", succ);
		mapRet.put("failNum", fail + checkFail);
		return mapRet;
	}
	
	private void assignment(InsurApplCvTask insurApplCvTask,QueryReceipt queryReceipt){
		queryReceipt.setApplNo(insurApplCvTask.getApplNo());
		queryReceipt.setOclkBranchNo(insurApplCvTask.getOclkBranchNo());
		queryReceipt.setOclkClerkName(insurApplCvTask.getOclkClerkName());
		queryReceipt.setOclkClerkNo(insurApplCvTask.getOclkClerkNo());
		queryReceipt.setRespDate(insurApplCvTask.getRespDate());
		queryReceipt.setSalesBranchNo(insurApplCvTask.getSalesBranchNo());
		queryReceipt.setSignDate(insurApplCvTask.getSignDate());
		queryReceipt.setDescription(insurApplCvTask.getDescription());
		GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(insurApplCvTask.getApplNo())), GrpInsurAppl.class);
		if(null != grpInsurAppl){
			queryReceipt.setApplDate(grpInsurAppl.getApplDate());
			queryReceipt.setCntrSendType(grpInsurAppl.getCntrSendType()==null?"":CNTR_SEND_TYPE.valueOfKey(grpInsurAppl.getCntrSendType()).getDescription());			
			if(null != grpInsurAppl.getPsnListHolderInfo()){
				queryReceipt.setSgName(grpInsurAppl.getPsnListHolderInfo().getSgName());
			}else if (null != grpInsurAppl.getGrpHolderInfo()) {
				queryReceipt.setSgName(grpInsurAppl.getGrpHolderInfo().getGrpName());
			}
		}
		InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(insurApplCvTask.getApplNo())), InsurApplOperTrace.class);
		if(null != insurApplOperTrace && null != insurApplOperTrace.getOperTraceDeque()){
			for(TraceNode traceNode:insurApplOperTrace.getOperTraceDeque()){
				if(StringUtils.equals(NEW_APPL_STATE.PRINT.getKey(), traceNode.getProcStat())){
					queryReceipt.setPrintDate(traceNode.getProcDate());
				}
			}
		}
	}
}
