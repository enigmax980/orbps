package com.newcore.orbps.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import com.newcore.orbps.models.service.bo.BackResult;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.ManualApprovalList;
import com.newcore.orbps.models.service.bo.UndoResult;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.ApprovalVo;
import com.newcore.orbps.service.api.CommonQueryService;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * @author huanglong
 * @date 2017年2月16日
 * @content 公共查询服务，包括人工审批清单查询
 */
@Service("commonQueryService")
public class CommonQueryServiceImpl implements CommonQueryService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(CommonQueryServiceImpl.class);
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	BranchService branchService;
	
	/*审核通过标记*/
	String flags[]={"0","1","2"};/*0-表示没有进人工审批，1-表示成功，2表示失败*/	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	@Override
	public PageData<ManualApprovalList> manualApproval(Map<String, Object> map) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		PageData<ManualApprovalList> pageData = new PageData<>();
		List<ManualApprovalList> manualApprovalLists = new ArrayList<>();
		pageData.setTotalCount(0);	
		if(null == map){
			map = new HashMap<>();
		}
		/*分析
		 * 1.如果只有投保单号 按投保单号查询
		 * 2.包含投保单号，按投保单号查询
		 * 3.只有机构号，按机构号查询
		 * 3.不包含投保单号，包含审批元素优先按 审批元素查询
		 * */
		/*分页处理*/
		Query query = new Query();
		if(map.containsKey("applNo")){
			GrpInsurAppl grpInsurAppl;
			if(map.containsKey("salesBranchNo")){
				List<String> salesBranchNos = new ArrayList<>();
				salesBranchNos.add(String.valueOf(map.get("salesBranchNo")));
				if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),String.valueOf(map.get("levelFlag")))){
					CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
					HeaderInfoHolder.setOutboundHeader(headerInfo);
					Branch branch = branchService.findSubBranchAll(String.valueOf(map.get("salesBranchNo")));		
					salesBranchNos.addAll(BranchNoUtils.getAllSubBranchNo(branch));
				}
				grpInsurAppl = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(map.get("applNo")).and("salesInfoList.salesBranchNo").in(salesBranchNos)), GrpInsurAppl.class);
			}else {
				grpInsurAppl = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(map.get("applNo"))), GrpInsurAppl.class);			
			}
			if(null == grpInsurAppl){
				return pageData;
			}
			InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(map.get("applNo"))), InsurApplOperTrace.class);
			TraceNode traceNodeTemp = new TraceNode();
			String flag = isPass(insurApplOperTrace,traceNodeTemp);/*0-表示没有进人工审批，1-表示成功，2表示失败*/
			if(StringUtils.equals(flags[0],flag)){
				return pageData;
			}
			createManualApprovalList(grpInsurAppl,traceNodeTemp,flag,manualApprovalLists);
			pageData.setData(manualApprovalLists);
			pageData.setTotalCount(1);		
			return pageData;
		}else{
			Criteria criteria = new Criteria();
			if(map.containsKey("pclkBranchNo")){
				criteria.and("pclkBranchNo").is(map.get("pclkBranchNo"));
			}
			if(map.containsKey("pclkNo")){
				criteria.and("pclkNo").is(map.get("pclkNo"));
			}
			try {
				if(map.containsKey("startDate") && map.containsKey("endDate")){
					criteria.andOperator(Criteria.where("procDate").gte(dateFormat.parse(String.valueOf(map.get("startDate")))),Criteria.where("procDate").lte(dateFormat.parse(String.valueOf(map.get("endDate")))));
				}else if(map.containsKey("startDate")){
					criteria.and("procDate").gte(dateFormat.parse(String.valueOf(map.get("startDate"))));
				}else if(map.containsKey("endDate")){
					criteria.and("procDate").lte(dateFormat.parse(String.valueOf(map.get("endDate"))));
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("0002","时间格式转换异常");
			}
			if(map.containsKey("result") && StringUtils.equals(flags[1], String.valueOf(map.get("result")))){
				criteria.orOperator(Criteria.where("procStat").is(NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey()),Criteria.where("procStat").is(NEW_APPL_STATE.LIST_APPROVE_CHECK_SUC.getKey()));
			}else if (map.containsKey("result") && StringUtils.equals(flags[2], String.valueOf(map.get("result")))) {
				criteria.orOperator(Criteria.where("procStat").is(NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey()),Criteria.where("procStat").is(NEW_APPL_STATE.LIST_APPROVE_CHECK_FAL.getKey()));
			}else if(!map.containsKey("result")){
				criteria.orOperator(Criteria.where("procStat").is(NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey()),Criteria.where("procStat").is(NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey()),
						Criteria.where("procStat").is(NEW_APPL_STATE.LIST_APPROVE_CHECK_FAL.getKey()),Criteria.where("procStat").is(NEW_APPL_STATE.LIST_APPROVE_CHECK_SUC.getKey()));
			}
			query.addCriteria(Criteria.where("operTraceDeque").elemMatch(criteria));
			List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);	
			if(map.containsKey("salesBranchNo")){
				List<String> salesBranchNos = new ArrayList<>();
				salesBranchNos.add(String.valueOf(map.get("salesBranchNo")));			
				if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),String.valueOf(map.get("levelFlag")))){
					CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
					HeaderInfoHolder.setOutboundHeader(headerInfo);
					Branch branch = branchService.findSubBranchAll(String.valueOf(map.get("salesBranchNo")));
					salesBranchNos.addAll(BranchNoUtils.getAllSubBranchNo(branch));
				}
				createPageDate(insurApplOperTraces,salesBranchNos,map,pageData);	
				return pageData;
			}else {
				List<String> salesBranchNos = new ArrayList<>();
				createPageDate(insurApplOperTraces,salesBranchNos,map,pageData);	
				return pageData;
			}
		}
	}

	@Override
	public PageData<CommonAgreement> commonAgreementQuery(Map<String, Object> map) {
		PageData<CommonAgreement> pageData = new PageData<>();
		pageData.setTotalCount(0);	
		if(null == map){
			map = new HashMap<>();
		}
		Query query = new Query();
		if(map.containsKey("agreementNo")){
			query.addCriteria(Criteria.where("agreementNo").is(map.get("agreementNo")));
		}
		if(map.containsKey("agreementName")){
			query.addCriteria(Criteria.where("agreementName").regex(String.valueOf(map.get("agreementName"))));
		}
		if(map.containsKey("applNo")){
			InsurApplRegist insurApplRegist = mongoTemplate.findOne(Query.query(Criteria.where("billNo").is(map.get("applNo"))), InsurApplRegist.class);
			if(null == insurApplRegist){
				return pageData;
			}
			if(map.containsKey("agreementNo") && !StringUtils.equals(String.valueOf(map.get("agreementNo")), insurApplRegist.getAgreementNo())){
				return pageData;
			}
			query.addCriteria(Criteria.where("agreementNo").is(insurApplRegist.getAgreementNo()));	
		}
		if(map.containsKey("mgrBranchNo")){	
			List<String> mgrBranchNos = new ArrayList<>();		
			mgrBranchNos.add(String.valueOf(map.get("mgrBranchNo")));			
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),String.valueOf(map.get("levelFlag")))){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(String.valueOf(map.get("mgrBranchNo")));
				mgrBranchNos.addAll(BranchNoUtils.getAllSubBranchNo(branch));
			}
			query.addCriteria(Criteria.where("mgrBranchNo").in(mgrBranchNos));
		}	
		try {
			if(map.containsKey("inForceDate")){
				query.addCriteria(Criteria.where("inForceDate").gte(dateFormat.parse(String.valueOf(map.get("inForceDate")))));
			}
			if(map.containsKey("termDate")){
				query.addCriteria(Criteria.where("termDate").lte(dateFormat.parse(String.valueOf(map.get("termDate")))));
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new BusinessException("0002","时间格式转换异常");
		}	
		long sum = mongoTemplate.count(query, CommonAgreement.class);
		if(map.containsKey("page") && map.containsKey("size") ){
			int page = (Integer) map.get("page");
			int size = (Integer) map.get("size");
			query.skip((page <=0?0:(page -1)) * size);
			query.limit(size);
		}
		List<CommonAgreement> commonAgreements = mongoTemplate.find(query, CommonAgreement.class);
		pageData.setData(commonAgreements);
		pageData.setTotalCount(sum);
		return pageData;
	}
	private String isPass(InsurApplOperTrace insurApplOperTrace,TraceNode traceNodeTemp){
		for(TraceNode traceNode: insurApplOperTrace.getOperTraceDeque()){
			if(StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey())
				|| StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_APPROVE_CHECK_SUC.getKey())){
				try {
					PropertyUtils.copyProperties(traceNodeTemp, traceNode);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("0002", "对象赋值异常");
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("0002", "对象赋值异常");
				} catch (NoSuchMethodException e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("0002", "对象赋值异常");
				}
				return flags[1];
			}else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey())
				|| StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_APPROVE_CHECK_FAL.getKey())) {
				try {
					PropertyUtils.copyProperties(traceNodeTemp, traceNode);
				} catch (IllegalAccessException e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("0002", "对象赋值异常");
				} catch (InvocationTargetException e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("0002", "对象赋值异常");
				} catch (NoSuchMethodException e) {
					logger.error(e.getMessage(), e);
					throw new BusinessException("0002", "对象赋值异常");
				}
				return flags[2];
			}
		}
		return flags[0];
	}
	private void createManualApprovalList(GrpInsurAppl grpInsurAppl,TraceNode traceNodeTemp,String flag,List<ManualApprovalList> manualApprovalLists){
		InsurApplRegist insurApplRegist = mongoTemplate.findOne(Query.query(Criteria.where("billNo").is(grpInsurAppl.getApplNo())), InsurApplRegist.class);
		ManualApprovalList manualApprovalList = new ManualApprovalList();
		manualApprovalList.setApplNo(grpInsurAppl.getApplNo());
		manualApprovalList.setAcceptDate(null==insurApplRegist?null:insurApplRegist.getAcceptDate());
		if(null != grpInsurAppl.getPsnListHolderInfo()){
			manualApprovalList.setSgName(grpInsurAppl.getPsnListHolderInfo().getSgName());			
		}else {
			manualApprovalList.setSgName(grpInsurAppl.getGrpHolderInfo().getGrpName());
		}
		manualApprovalList.setNote(traceNodeTemp.getDescription());
		manualApprovalList.setSignDate(grpInsurAppl.getSignDate());
		manualApprovalList.setInForceDate(grpInsurAppl.getInForceDate());
		manualApprovalList.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());
		manualApprovalList.setPclkBranchNo(traceNodeTemp.getPclkBranchNo());
		manualApprovalList.setPclkName(traceNodeTemp.getPclkName());
		manualApprovalList.setPclkNo(traceNodeTemp.getPclkNo());
		manualApprovalList.setPolCode(grpInsurAppl.getFirPolCode());
		manualApprovalList.setProcDate(traceNodeTemp.getProcDate());
		manualApprovalList.setReason(grpInsurAppl.getConventions().getInputConv());
		manualApprovalList.setResult(flag);
		manualApprovalList.setRuleTye("1");	
		manualApprovalLists.add(manualApprovalList);
	}

	private void createPageDate(List<InsurApplOperTrace> insurApplOperTraces,List<String> salesBranchNos,Map<String, Object>map,PageData<ManualApprovalList> pageData){
		List<ManualApprovalList> manualApprovalListSum = new ArrayList<>();
		List<ManualApprovalList> manualApprovalLists = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			GrpInsurAppl grpInsurAppl;
			if(salesBranchNos.isEmpty()){
				grpInsurAppl = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(insurApplOperTrace.getApplNo())), GrpInsurAppl.class);
			}else {
				grpInsurAppl = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(insurApplOperTrace.getApplNo()).and("salesInfoList.salesBranchNo").in(salesBranchNos)), GrpInsurAppl.class);				
			}
			if(null == grpInsurAppl){
				continue;
			}
			TraceNode traceNodeTemp = new TraceNode();
			String flag = isPass(insurApplOperTrace,traceNodeTemp);
			if(map.containsKey("result") && StringUtils.equals(String.valueOf(map.get("result")), flag)){
				createManualApprovalList(grpInsurAppl, traceNodeTemp, flag, manualApprovalListSum);
			}else if (!map.containsKey("result") && (StringUtils.equals(flag, flags[1]) || StringUtils.equals(flag, flags[2]))) {
				createManualApprovalList(grpInsurAppl, traceNodeTemp, flag, manualApprovalListSum);
			}
		}
		if(map.containsKey("page") && map.containsKey("size") ){
			int page = (Integer) map.get("page");
			int size = (Integer) map.get("size");
			for(int i = (page -1) * size; i< page*size && i<manualApprovalListSum.size(); i++){
				manualApprovalLists.add(manualApprovalListSum.get(i));
			}
			pageData.setData(manualApprovalLists);
			pageData.setTotalCount(manualApprovalListSum.size());
		}else {
			pageData.setData(manualApprovalListSum);
			pageData.setTotalCount(manualApprovalListSum.size());
		}
	}
	
	@Override
	public PageData<UndoResult> queryForUndo(PageQuery<ApprovalVo> pageQuery) {
		ApprovalVo approvalVo = pageQuery.getCondition();
		if(null == approvalVo){
		    approvalVo = new ApprovalVo();
		}
		Criteria criteria1 = new Criteria();
		Criteria criteria2 = Criteria.where("operTraceDeque.0.procStat").is(NEW_APPL_STATE.OFFER_WITHDRAWN.getKey());
		if (!StringUtils.isEmpty(approvalVo.getApplNo())){
			criteria1 = Criteria.where("billNo").is(approvalVo.getApplNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getSalesBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),approvalVo.getLevelFlag())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(approvalVo.getSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				salesBranchNos.add(approvalVo.getSalesBranchNo());
				criteria1.and("salesInfos.salesBranchNo").in(salesBranchNos);
			}else{
				criteria1.and("salesInfos.salesBranchNo").is(approvalVo.getSalesBranchNo());
			}
		}
		Aggregation aggregation1 = Aggregation.newAggregation(
				Aggregation.match(criteria1),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> result1 = mongoTemplate.aggregate(aggregation1, "insurApplRegist", JSONObject.class);
		List<JSONObject> jsonObjects = result1.getMappedResults();
		List<String> applNos1 = new ArrayList<>();
		for(JSONObject jsonObject:jsonObjects){
			applNos1.add(jsonObject.getString("_id"));
		}
		criteria2.and("applNo").in(applNos1);
		if (!StringUtils.isEmpty(approvalVo.getPclkBranchNo())){
			criteria2.and("operTraceDeque.0.pclkBranchNo").is(approvalVo.getPclkBranchNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getPclkNo())){
			criteria2.and("operTraceDeque.0.pclkNo").is(approvalVo.getPclkNo());
		}
		Criteria criteria3 = new Criteria();
		if(!StringUtils.isEmpty(approvalVo.getStartDate()) || !StringUtils.isEmpty(approvalVo.getEndDate())){
			criteria3 = Criteria.where("operTraceDeque.0.procDate");
			if (!StringUtils.isEmpty(approvalVo.getStartDate())){
				criteria3.gte(stringToDate(approvalVo.getStartDate()));
			}
			if (!StringUtils.isEmpty(approvalVo.getEndDate())){
				criteria3.lte(DateUtils.addDays(stringToDate(approvalVo.getEndDate()),1));
			}
		}
		Query query = new Query(criteria2);
		query.addCriteria(criteria3);
		long num = mongoTemplate.count(query, InsurApplOperTrace.class);
		query.skip((int) pageQuery.getPageStartNo());
		query.limit(pageQuery.getPageSize());
		List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);
		List<UndoResult> undoResults = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			UndoResult undoResult = new UndoResult();
			undoResult.setApplNo(insurApplOperTrace.getApplNo());
			undoResult.setPclkBranchNo(insurApplOperTrace.getCurrentTraceNode().getPclkBranchNo());
			undoResult.setPclkNo(insurApplOperTrace.getCurrentTraceNode().getPclkNo());
			undoResult.setPclkDate(insurApplOperTrace.getCurrentTraceNode().getProcDate());
			undoResult.setPclkName(insurApplOperTrace.getCurrentTraceNode().getPclkName());
			insurApplOperTrace.getOperTraceDeque().poll();
			undoResult.setPreStat(insurApplOperTrace.getOperTraceDeque().peek()==null?null:insurApplOperTrace.getOperTraceDeque().peek().getProcStat());
			InsurApplRegist insurApplRegist = mongoTemplate.findOne(new Query(Criteria.where("billNo").is(insurApplOperTrace.getApplNo())), InsurApplRegist.class);
			undoResult.setHldrName(insurApplRegist.getHldrName());
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(Criteria.where("applNo").is(insurApplOperTrace.getApplNo())), GrpInsurAppl.class);
			if(null!=grpInsurAppl){
				if(StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(),grpInsurAppl.getCntrType())
					|| (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(),grpInsurAppl.getCntrType())
					   && StringUtils.equals(LIST_TYPE.GRP_SG.getKey(),grpInsurAppl.getSgType()))){
					undoResult.setHldrName(grpInsurAppl.getGrpHolderInfo()==null?null:grpInsurAppl.getGrpHolderInfo().getGrpName());
				}
				if(StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(),grpInsurAppl.getCntrType())
					&& StringUtils.equals(LIST_TYPE.PSN_SG.getKey(),grpInsurAppl.getSgType())){
					undoResult.setHldrName(grpInsurAppl.getPsnListHolderInfo()==null?null:grpInsurAppl.getPsnListHolderInfo().getSgName());
				}
			}
			undoResult.setSaleBranchNo(getMainSales(insurApplRegist.getSalesInfos()).getSalesBranchNo());
			undoResults.add(undoResult);
		}
		PageData<UndoResult> pageData = new PageData<>();
		pageData.setTotalCount(num);
		pageData.setData(undoResults);
		return pageData;
	}
	
	@Override
	public List<UndoResult> queryAllForUndo(ApprovalVo approvalVo) {
		if(null == approvalVo){
			approvalVo = new ApprovalVo();
		}
		Criteria criteria1 = new Criteria();
		Criteria criteria2 = Criteria.where("operTraceDeque.0.procStat").is(NEW_APPL_STATE.OFFER_WITHDRAWN.getKey());
		if (!StringUtils.isEmpty(approvalVo.getApplNo())){
			criteria1 = Criteria.where("billNo").is(approvalVo.getApplNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getSalesBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),approvalVo.getLevelFlag())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(approvalVo.getSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				salesBranchNos.add(approvalVo.getSalesBranchNo());
				criteria1.and("salesInfos.salesBranchNo").in(salesBranchNos);
			}else{
				criteria1.and("salesInfos.salesBranchNo").is(approvalVo.getSalesBranchNo());
			}
		}
		Aggregation aggregation1 = Aggregation.newAggregation(
				Aggregation.match(criteria1),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> result1 = mongoTemplate.aggregate(aggregation1, "insurApplRegist", JSONObject.class);
		List<JSONObject> jsonObjects = result1.getMappedResults();
		List<String> applNos1 = new ArrayList<>();
		for(JSONObject jsonObject:jsonObjects){
			applNos1.add(jsonObject.getString("_id"));
		}
		criteria2.and("applNo").in(applNos1);
		if (!StringUtils.isEmpty(approvalVo.getPclkBranchNo())){
			criteria2.and("operTraceDeque.0.pclkBranchNo").is(approvalVo.getPclkBranchNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getPclkNo())){
			criteria2.and("operTraceDeque.0.pclkNo").is(approvalVo.getPclkNo());
		}
		Criteria criteria3 = new Criteria();
		if(!StringUtils.isEmpty(approvalVo.getStartDate()) || !StringUtils.isEmpty(approvalVo.getEndDate())){
			criteria3 = Criteria.where("operTraceDeque.0.procDate");
			if (!StringUtils.isEmpty(approvalVo.getStartDate())){
				criteria3.gte(stringToDate(approvalVo.getStartDate()));
			}
			if (!StringUtils.isEmpty(approvalVo.getEndDate())){
				criteria3.lte(DateUtils.addDays(stringToDate(approvalVo.getEndDate()),1));
			}
		}
		Query query = new Query(criteria2);
		query.addCriteria(criteria3);
		List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);
		List<UndoResult> undoResults = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			UndoResult undoResult = new UndoResult();
			undoResult.setApplNo(insurApplOperTrace.getApplNo());
			undoResult.setPclkBranchNo(insurApplOperTrace.getCurrentTraceNode().getPclkBranchNo());
			undoResult.setPclkNo(insurApplOperTrace.getCurrentTraceNode().getPclkNo());
			undoResult.setPclkDate(insurApplOperTrace.getCurrentTraceNode().getProcDate());
			undoResult.setPclkName(insurApplOperTrace.getCurrentTraceNode().getPclkName());
			insurApplOperTrace.getOperTraceDeque().poll();
			undoResult.setPreStat(insurApplOperTrace.getOperTraceDeque().peek()==null?null:insurApplOperTrace.getOperTraceDeque().peek().getProcStat());
			InsurApplRegist insurApplRegist = mongoTemplate.findOne(new Query(Criteria.where("billNo").is(insurApplOperTrace.getApplNo())), InsurApplRegist.class);
			undoResult.setHldrName(insurApplRegist.getHldrName());
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(Criteria.where("applNo").is(insurApplOperTrace.getApplNo())), GrpInsurAppl.class);
			if(null!=grpInsurAppl){
				if(StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(),grpInsurAppl.getCntrType())
					|| (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(),grpInsurAppl.getCntrType())
					   && StringUtils.equals(LIST_TYPE.GRP_SG.getKey(),grpInsurAppl.getSgType()))){
					undoResult.setHldrName(grpInsurAppl.getGrpHolderInfo()==null?null:grpInsurAppl.getGrpHolderInfo().getGrpName());
				}
				if(StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(),grpInsurAppl.getCntrType())
					&& StringUtils.equals(LIST_TYPE.PSN_SG.getKey(),grpInsurAppl.getSgType())){
					undoResult.setHldrName(grpInsurAppl.getPsnListHolderInfo()==null?null:grpInsurAppl.getPsnListHolderInfo().getSgName());
				}
			}
			undoResult.setSaleBranchNo(getMainSales(insurApplRegist.getSalesInfos()).getSalesBranchNo());
			undoResults.add(undoResult);
		}
		return undoResults;
	}
	
	@Override
	public PageData<BackResult> queryForBack(PageQuery<ApprovalVo> pageQuery) {
		ApprovalVo approvalVo = pageQuery.getCondition();
		if(null == approvalVo){
			approvalVo = new ApprovalVo();
		}
		Criteria criteria1 = new Criteria();
		Criteria criteria2 = Criteria.where("procStat").is(NEW_APPL_STATE.BACK.getKey());
		if (!StringUtils.isEmpty(approvalVo.getApplNo())){
			criteria1 = Criteria.where("billNo").is(approvalVo.getApplNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getSalesBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),approvalVo.getLevelFlag())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(approvalVo.getSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				salesBranchNos.add(approvalVo.getSalesBranchNo());
				criteria1.and("salesInfos.salesBranchNo").in(salesBranchNos);
			}else{
				criteria1.and("salesInfos.salesBranchNo").is(approvalVo.getSalesBranchNo());
			}
		}
		Aggregation aggregation1 = Aggregation.newAggregation(
				Aggregation.match(criteria1),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> result1 = mongoTemplate.aggregate(aggregation1, "insurApplRegist", JSONObject.class);
		List<JSONObject> jsonObjects = result1.getMappedResults();
		List<String> applNos1 = new ArrayList<>();
		for(JSONObject jsonObject:jsonObjects){
			applNos1.add(jsonObject.getString("_id"));
		}
		Criteria criteria3 = Criteria.where("applNo").in(applNos1);
		if (!StringUtils.isEmpty(approvalVo.getPclkBranchNo())){
			criteria2.and("pclkBranchNo").is(approvalVo.getPclkBranchNo());
		}
		Criteria criteria4 = new Criteria();
		if (!StringUtils.isEmpty(approvalVo.getStartDate()) || !StringUtils.isEmpty(approvalVo.getEndDate())){
			criteria4 = Criteria.where("procDate");
			if (!StringUtils.isEmpty(approvalVo.getStartDate())){
				criteria4.gte(stringToDate(approvalVo.getStartDate()));
			}
			if (!StringUtils.isEmpty(approvalVo.getEndDate())){
				criteria4.lte(stringToDate(approvalVo.getEndDate()));
			}
		}
		criteria2.andOperator(criteria4);
		Query query = new Query(criteria3.and("operTraceDeque").elemMatch(criteria2));
		long num = mongoTemplate.count(query, InsurApplOperTrace.class);
		query.skip((int) pageQuery.getPageStartNo());
		query.limit(pageQuery.getPageSize());
		List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);
		List<BackResult> backResults = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			TraceNode traceNode;
			while((traceNode = insurApplOperTrace.getOperTraceDeque().poll())!=null){
				String procStat = traceNode.getProcStat();
				InsurApplRegist insurApplRegist = mongoTemplate.findOne(new Query(Criteria.where("billNo").is(insurApplOperTrace.getApplNo())), InsurApplRegist.class);
				if(StringUtils.equals(NEW_APPL_STATE.BACK.getKey(), traceNode.getProcStat())){
					BackResult backResult = new BackResult();
					backResult.setApplNo(insurApplOperTrace.getApplNo());
					backResult.setStat(procStat);
					backResult.setPclkBranchNo(traceNode.getPclkBranchNo());
					backResult.setPclkNo(traceNode.getPclkNo());
					backResult.setPclkDate(traceNode.getProcDate());
					backResult.setPclkName(traceNode.getPclkName());
					backResult.setBackReason(traceNode.getDescription());
					backResult.setPreStat(insurApplOperTrace.getOperTraceDeque().peek()==null?null:insurApplOperTrace.getOperTraceDeque().peek().getProcStat());
					backResult.setHldrName(insurApplRegist.getHldrName());
					backResult.setSaleBranchNo(getMainSales(insurApplRegist.getSalesInfos()).getSalesBranchNo());
					backResults.add(backResult);
				}
			}
		}
		PageData<BackResult> pageData = new PageData<>();
		pageData.setTotalCount(num);
		pageData.setData(backResults);
		return pageData;
	}
	
	@Override
	public List<BackResult> queryAllForBack(ApprovalVo approvalVo) {
		if(null == approvalVo){
			approvalVo = new ApprovalVo();
		}
		Criteria criteria1 = new Criteria();
		Criteria criteria2 = Criteria.where("procStat").is(NEW_APPL_STATE.BACK.getKey());
		if (!StringUtils.isEmpty(approvalVo.getApplNo())){
			criteria1 = Criteria.where("billNo").is(approvalVo.getApplNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getSalesBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),approvalVo.getLevelFlag())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(approvalVo.getSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				salesBranchNos.add(approvalVo.getSalesBranchNo());
				criteria1.and("salesInfos.salesBranchNo").in(salesBranchNos);
			}else{
				criteria1.and("salesInfos.salesBranchNo").is(approvalVo.getSalesBranchNo());
			}
		}
		Aggregation aggregation1 = Aggregation.newAggregation(
				Aggregation.match(criteria1),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> result1 = mongoTemplate.aggregate(aggregation1, "insurApplRegist", JSONObject.class);
		List<JSONObject> jsonObjects = result1.getMappedResults();
		List<String> applNos1 = new ArrayList<>();
		for(JSONObject jsonObject:jsonObjects){
			applNos1.add(jsonObject.getString("_id"));
		}
		Criteria criteria3 = Criteria.where("applNo").in(applNos1);
		if (!StringUtils.isEmpty(approvalVo.getPclkBranchNo())){
			criteria2.and("pclkBranchNo").is(approvalVo.getPclkBranchNo());
		}
		if (!StringUtils.isEmpty(approvalVo.getPclkNo())){
			criteria2.and("pclkNo").is(approvalVo.getPclkNo());
		}
		Criteria criteria4 = new Criteria();
		if (!StringUtils.isEmpty(approvalVo.getStartDate()) || !StringUtils.isEmpty(approvalVo.getEndDate())){
			criteria4 = Criteria.where("procDate");
			if (!StringUtils.isEmpty(approvalVo.getStartDate())){
				criteria4.gte(stringToDate(approvalVo.getStartDate()));
			}
			if (!StringUtils.isEmpty(approvalVo.getEndDate())){
				criteria4.lte(stringToDate(approvalVo.getEndDate()));
			}
		}
		criteria2.andOperator(criteria4);
		Query query = new Query(criteria3.and("operTraceDeque").elemMatch(criteria2));
		List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);
		List<BackResult> backResults = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			TraceNode traceNode;
			while((traceNode = insurApplOperTrace.getOperTraceDeque().poll())!=null){
				if(!StringUtils.isEmpty(approvalVo.getPclkNo()) && !StringUtils.equals(approvalVo.getPclkNo(), traceNode.getPclkNo())){
					continue;
				}
				String procStat = traceNode.getProcStat();
				InsurApplRegist insurApplRegist = mongoTemplate.findOne(new Query(Criteria.where("billNo").is(insurApplOperTrace.getApplNo())), InsurApplRegist.class);
				if(StringUtils.equals(NEW_APPL_STATE.BACK.getKey(), traceNode.getProcStat())){
					BackResult backResult = new BackResult();
					backResult.setApplNo(insurApplOperTrace.getApplNo());
					backResult.setStat(NEW_APPL_STATE.valueOfKey(procStat).getDescription());
					backResult.setPclkBranchNo(traceNode.getPclkBranchNo());
					backResult.setPclkNo(traceNode.getPclkNo());
					backResult.setPclkDate(traceNode.getProcDate());
					backResult.setPclkName(traceNode.getPclkName());
					backResult.setBackReason(traceNode.getDescription());
					backResult.setPreStat(insurApplOperTrace.getOperTraceDeque().peek()==null?null:NEW_APPL_STATE.valueOfKey(insurApplOperTrace.getOperTraceDeque().peek().getProcStat()).getDescription());
					backResult.setHldrName(insurApplRegist.getHldrName());
					backResult.setSaleBranchNo(getMainSales(insurApplRegist.getSalesInfos()).getSalesBranchNo());
					backResults.add(backResult);
				}
			}
		}
		return backResults;
	}
	
	//获取主销售员
	private SalesInfo getMainSales(List<SalesInfo> salesInfos){
		SalesInfo salesInfo = salesInfos.get(0);
		for(SalesInfo salesInfo1:salesInfos){
			if(StringUtils.equals(salesInfo1.getDevelMainFlag(),DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey())){
				return salesInfo1;
			}
		}
		return salesInfo;
	}
	//string类型转换date
	private Date stringToDate(String sdate){
		Date date = null;
		try {
			date = DateUtils.parseDate(sdate, "yyyy-MM-dd");
		} catch (ParseException e) {
			logger.error("日期解析错误",e);
			throw new BusinessException("0002", "日期解析错误");
		}
		return date;
	}
}
