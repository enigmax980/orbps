package com.newcore.orbps.service.impl;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
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
import com.newcore.orbps.dao.api.ContractQueryDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ContractBusiStateQueryVo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.supports.dicts.APPL_BUSI_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.MONEYIN_ITRVL;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PRD_SALES_CHANNEL;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.util.HeaderInfoUtils;
/**
 * 
 * @author nzh
 *	 
 * 创建时间：2016年9月12日下午4:03:31
 */
@Service("contractQueryService")
public class ContractQueryServiceImpl implements ContractQueryService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(ContractQueryServiceImpl.class);

	@Autowired
	ContractQueryDao contractQueryDaoImpl;
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	BranchService branchService;
	@Override
	public PageData<ContractBusiStateQueryVo> queryCheck(PageQuery<ContractBusiStateQueryVo> pageQuery) {
		ContractBusiStateQueryVo contractBusiStateQueryVo = pageQuery.getCondition();
		if(null == contractBusiStateQueryVo){
			contractBusiStateQueryVo = new ContractBusiStateQueryVo();
		}
		Criteria insurApplRegistCriteria = new Criteria();
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getApplNo())){
			insurApplRegistCriteria = Criteria.where("billNo").is(contractBusiStateQueryVo.getApplNo());
		}
		Criteria criteria1 = new Criteria();
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getSalesBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),contractBusiStateQueryVo.getIsCotainJuniorSaleBranch())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch allBranch = branchService.findSubBranchAll(contractBusiStateQueryVo.getSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(allBranch);
				salesBranchNos.add(contractBusiStateQueryVo.getSalesBranchNo());
				criteria1.and("salesBranchNo").in(salesBranchNos);
			}else{
				criteria1.and("salesBranchNo").is(contractBusiStateQueryVo.getSalesBranchNo());
			}
		}
		if(!StringUtils.isEmpty(contractBusiStateQueryVo.getSalesChannel())){
			criteria1.and("salesChannel").is(contractBusiStateQueryVo.getSalesChannel());
		}
		insurApplRegistCriteria.and("salesInfos").elemMatch(criteria1);
		Criteria criteria2 = new Criteria();
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getStartDate()) || !StringUtils.isEmpty(contractBusiStateQueryVo.getEndDate())){
			criteria2 = Criteria.where("acceptDate");
			if (!StringUtils.isEmpty(contractBusiStateQueryVo.getStartDate())){
				criteria2.gte(stringToDate(contractBusiStateQueryVo.getStartDate()));
			}
			if (!StringUtils.isEmpty(contractBusiStateQueryVo.getEndDate())){
				criteria2.lte(stringToDate(contractBusiStateQueryVo.getEndDate()));
			}
		}
		insurApplRegistCriteria.andOperator(criteria2);
		if(!StringUtils.isEmpty(contractBusiStateQueryVo.getContractBusiForm())){
			insurApplRegistCriteria.and("cntrType").is(contractBusiStateQueryVo.getContractBusiForm());
		}
		Aggregation aggregation1 = Aggregation.newAggregation(
				Aggregation.match(insurApplRegistCriteria),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> result1 = mongoTemplate.aggregate(aggregation1, "insurApplRegist", JSONObject.class);
		List<String> applNos1 = new ArrayList<>();
		for(JSONObject jsonObject:result1.getMappedResults()){
			applNos1.add(jsonObject.getString("_id"));
		}
		Criteria insurApplOperTraceCriteria = new Criteria();
		insurApplOperTraceCriteria.and("applNo").in(applNos1);
		Criteria criteria3 = Criteria.where("procStat").is(NEW_APPL_STATE.ACCEPTED.getKey());
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getBizzBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),contractBusiStateQueryVo.getIsCotainJuniorBizzBranch())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(contractBusiStateQueryVo.getBizzBranchNo());
				List<String> bizzBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				bizzBranchNos.add(contractBusiStateQueryVo.getBizzBranchNo());
				criteria3.and("pclkBranchNo").in(bizzBranchNos);
			}else{
				criteria3.and("pclkBranchNo").is(contractBusiStateQueryVo.getBizzBranchNo());
			}
		}
		if(!StringUtils.isEmpty(contractBusiStateQueryVo.getBizzNo())){
			criteria3.and("pclkNo").is(contractBusiStateQueryVo.getBizzNo());
		}
		insurApplOperTraceCriteria.and("operTraceDeque").elemMatch(criteria3);
		if(contractBusiStateQueryVo.getTaskPresentState()!=null && !contractBusiStateQueryVo.getTaskPresentState().isEmpty()){
			insurApplOperTraceCriteria.and("operTraceDeque.0.procStat").in(contractBusiStateQueryVo.getTaskPresentState());
		}
		long num = mongoTemplate.count(new Query(insurApplOperTraceCriteria), InsurApplOperTrace.class);
		Query query = new Query(insurApplOperTraceCriteria);
		query.skip((int) pageQuery.getPageStartNo());
		query.limit(pageQuery.getPageSize());
		List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);
		List<ContractBusiStateQueryVo> contractBusiStateQueryVos = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			if(insurApplOperTrace==null){
				continue;
			}
			ContractBusiStateQueryVo reContractBusiStateQueryVo = new ContractBusiStateQueryVo();
			if(insurApplOperTrace.getOperTraceDeque()!=null && !insurApplOperTrace.getOperTraceDeque().isEmpty()){
				reContractBusiStateQueryVo.setBizzBranchNo(insurApplOperTrace.getOperTraceDeque().peekLast().getPclkBranchNo());
				reContractBusiStateQueryVo.setBizzNo(insurApplOperTrace.getOperTraceDeque().peekLast().getPclkNo());
				reContractBusiStateQueryVo.setBizzName(insurApplOperTrace.getOperTraceDeque().peekLast().getPclkName());
				reContractBusiStateQueryVo.setTaskPresentStates(getState(insurApplOperTrace.getCurrentTraceNode().getProcStat()));
			}
			reContractBusiStateQueryVo.setApplNo(insurApplOperTrace.getApplNo());
			InsurApplRegist insurApplRegist = mongoTemplate.findOne(new Query(Criteria.where("billNo").is(insurApplOperTrace.getApplNo())), InsurApplRegist.class);
			if(insurApplRegist!=null){
				reContractBusiStateQueryVo.setBizzDate(DateFormatUtils.format(insurApplRegist.getAcceptDate(),"yyyy/MM/dd HH:mm"));
				reContractBusiStateQueryVo.setContractBusiForm(StringUtils.isEmpty(insurApplRegist.getCntrType())?"":CNTR_TYPE.valueOfKey(insurApplRegist.getCntrType()).getDescription());
				reContractBusiStateQueryVo.setPolCode(insurApplRegist.getPolCode());
				reContractBusiStateQueryVo.setTotalPremium(String.valueOf(insurApplRegist.getSumPremium()));
				if(insurApplRegist.getSalesInfos()!=null){
					SalesInfo salesInfo = getSalesInfo(insurApplRegist);
					reContractBusiStateQueryVo.setSalesBranchNo(salesInfo.getSalesBranchNo());
					reContractBusiStateQueryVo.setSalesChannel(StringUtils.isEmpty(salesInfo.getSalesChannel())?"":PRD_SALES_CHANNEL.valueOfKey(salesInfo.getSalesChannel()).getDescription());
					reContractBusiStateQueryVo.setSalesNo(salesInfo.getSalesNo());
					reContractBusiStateQueryVo.setSalesName(salesInfo.getSalesName());
				}
				reContractBusiStateQueryVo.setApplName(insurApplRegist.getHldrName());
			}
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(Criteria.where("applNo").is(insurApplOperTrace.getApplNo())), GrpInsurAppl.class);
			if(grpInsurAppl != null){
				if(grpInsurAppl.getPaymentInfo()!=null){
					reContractBusiStateQueryVo.setPayForm(StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())?"":MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription());
				}
				String applName = "";
				if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())
					|| (StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey()) 
						&& StringUtils.equals(grpInsurAppl.getSgType(),LIST_TYPE.GRP_SG.getKey()))){
					applName = grpInsurAppl.getGrpHolderInfo()==null?"":grpInsurAppl.getGrpHolderInfo().getGrpName();
				}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey()) 
						&& StringUtils.equals(grpInsurAppl.getSgType(),LIST_TYPE.PSN_SG.getKey())){
					applName = grpInsurAppl.getPsnListHolderInfo()==null?"":grpInsurAppl.getPsnListHolderInfo().getSgName();
				}
				reContractBusiStateQueryVo.setApplName(applName);
				reContractBusiStateQueryVo.setBusiType(StringUtils.isEmpty(grpInsurAppl.getApplBusiType())?"":APPL_BUSI_TYPE.valueOfKey(grpInsurAppl.getApplBusiType()).getDescription());
			}
			StringBuilder string = new StringBuilder();
			string.append("<a data-toggle='tooltip' data-placement='bottom' title='");
			for (TraceNode traceNode : insurApplOperTrace.getOperTraceDeque()) {// 受理岗
                if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.ACCEPTED.getKey())) {
                    string.append("受理岗--进入受理岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 业务操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 受理状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } // 暂存岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.RECORD_TEMPORARILY.getKey())) {
                    string.append("暂存岗--进入暂存岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 业务操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 暂存状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } // 录入岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_ENTRY.getKey())
                        || StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_ENTRY.getKey())) {
                    string.append("录入岗--进入录入岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 录入操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 录入状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } // 导入岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IMPORT.getKey())
                        || StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IMPORT.getKey())) {
                    string.append("导入岗--进入导入岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 导入操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 导入状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (isReview(traceNode)) {
                    string.append("复核岗--进入复核岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 复核操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 复核状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                }
                // 人工审批岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_MANUAL_APPROVAL.getKey())
                        || StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_MANUAL_APPROVAL.getKey())
                        || isListInfoCheck(traceNode)) {
                    string.append("人工审批岗--进入人工审批岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 人工审批操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 人工审批状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.INSURED_ACCOUNT.getKey())) {
                    string.append("被保人开户岗--进入被保人开户岗位时间:");
                    string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    string.append(" 业务操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 被保人状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.UNDERWRITING.getKey())
                        || isUnderwriting(traceNode)) {
                    string.append("核保岗--进入核保岗位时间:");

                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 核保操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 核保状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GENERATE_RECEIV.getKey())) {
                    string.append("应收岗--进入应收岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 应收操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 应收状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.PAY_CHECK.getKey())) {
                    string.append("收费检查岗--进入收费检查岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 收费检查操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 收费检查状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.POLICY_INFORCE.getKey())) {
                    string.append("保单生效岗--进入保单生效岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 保单生效操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 保单生效状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.PRINT.getKey())) {
                    string.append("打印岗--进入打印岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 打印操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 打印状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.REVISE.getKey())) {
                    string.append("订正岗--进入订正岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 订正操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 订正状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.RECEIPT_VERIFICA.getKey())) {
                    string.append("核销岗--进入核销岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 核销操作人员号:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 核销状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.BACK.getKey())) {
                    string.append("回退岗--进入回退岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 回退操作人员号:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 回退状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                }
            }
            string.append("当前业务状态:");
            if (insurApplOperTrace.getCurrentTraceNode() != null && insurApplOperTrace.getCurrentTraceNode().getProcStat() != null) {
                string.append(getState(insurApplOperTrace.getCurrentTraceNode().getProcStat())+"，操作时间：");
                string.append(insurApplOperTrace.getCurrentTraceNode().getProcDate()==null?" ":DateFormatUtils.format(insurApplOperTrace.getCurrentTraceNode().getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                string.append("，操作人员：");
                string.append(insurApplOperTrace.getCurrentTraceNode().getPclkName()==null?" " : insurApplOperTrace.getCurrentTraceNode().getPclkName());
            }
            string.append("'>详细业务情况</a></td></tr>");
			reContractBusiStateQueryVo.setDetailBusiCondition(string.toString());
			contractBusiStateQueryVos.add(reContractBusiStateQueryVo);
		}
		PageData<ContractBusiStateQueryVo> pageData = new PageData<>();
		pageData.setTotalCount(num);
		pageData.setData(contractBusiStateQueryVos);
		return pageData;
	}
	
	@Override
	public List<ContractBusiStateQueryVo> queryAllCheck(ContractBusiStateQueryVo contractBusiStateQueryVo) {
		if(null == contractBusiStateQueryVo){
			contractBusiStateQueryVo = new ContractBusiStateQueryVo();
		}
		Criteria insurApplRegistCriteria = new Criteria();
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getApplNo())){
			insurApplRegistCriteria = Criteria.where("billNo").is(contractBusiStateQueryVo.getApplNo());
		}
		Criteria criteria1 = new Criteria();
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getSalesBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),contractBusiStateQueryVo.getIsCotainJuniorSaleBranch())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(contractBusiStateQueryVo.getSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				salesBranchNos.add(contractBusiStateQueryVo.getSalesBranchNo());
				criteria1.and("salesBranchNo").in(salesBranchNos);
			}else{
				criteria1.and("salesBranchNo").is(contractBusiStateQueryVo.getSalesBranchNo());
			}
		}
		if(!StringUtils.isEmpty(contractBusiStateQueryVo.getSalesChannel())){
			criteria1.and("salesChannel").is(contractBusiStateQueryVo.getSalesChannel());
		}
		insurApplRegistCriteria.and("salesInfos").elemMatch(criteria1);
		Criteria criteria2 = new Criteria();
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getStartDate()) || !StringUtils.isEmpty(contractBusiStateQueryVo.getEndDate())){
			criteria2 = Criteria.where("acceptDate");
			if (!StringUtils.isEmpty(contractBusiStateQueryVo.getStartDate())){
				criteria2.gte(stringToDate(contractBusiStateQueryVo.getStartDate()));
			}
			if (!StringUtils.isEmpty(contractBusiStateQueryVo.getEndDate())){
				criteria2.lte(stringToDate(contractBusiStateQueryVo.getEndDate()));
			}
		}
		insurApplRegistCriteria.andOperator(criteria2);
		if(!StringUtils.isEmpty(contractBusiStateQueryVo.getContractBusiForm())){
			insurApplRegistCriteria.and("cntrType").is(contractBusiStateQueryVo.getContractBusiForm());
		}
		Aggregation aggregation1 = Aggregation.newAggregation(
				Aggregation.match(insurApplRegistCriteria),
				Aggregation.group("billNo")
				);
		AggregationResults<JSONObject> result1 = mongoTemplate.aggregate(aggregation1, "insurApplRegist", JSONObject.class);
		List<String> applNos1 = new ArrayList<>();
		for(JSONObject jsonObject:result1.getMappedResults()){
			applNos1.add(jsonObject.getString("_id"));
		}
		Criteria insurApplOperTraceCriteria = new Criteria();
		insurApplOperTraceCriteria.and("applNo").in(applNos1);
		Criteria criteria3 = Criteria.where("procStat").is(NEW_APPL_STATE.ACCEPTED.getKey());
		if (!StringUtils.isEmpty(contractBusiStateQueryVo.getBizzBranchNo())) {
			if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),contractBusiStateQueryVo.getIsCotainJuniorBizzBranch())){
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch branch = branchService.findSubBranchAll(contractBusiStateQueryVo.getBizzBranchNo());
				List<String> bizzBranchNos = BranchNoUtils.getAllSubBranchNo(branch);
				bizzBranchNos.add(contractBusiStateQueryVo.getBizzBranchNo());
				criteria3.and("pclkBranchNo").in(bizzBranchNos);
			}else{
				criteria3.and("pclkBranchNo").is(contractBusiStateQueryVo.getBizzBranchNo());
			}
		}
		if(!StringUtils.isEmpty(contractBusiStateQueryVo.getBizzNo())){
			criteria3.and("pclkNo").is(contractBusiStateQueryVo.getBizzNo());
		}
		insurApplOperTraceCriteria.and("operTraceDeque").elemMatch(criteria3);
		if(contractBusiStateQueryVo.getTaskPresentState()!=null && !contractBusiStateQueryVo.getTaskPresentState().isEmpty()){
			insurApplOperTraceCriteria.and("operTraceDeque.0.procStat").in(contractBusiStateQueryVo.getTaskPresentState());
		}
		Query query = new Query(insurApplOperTraceCriteria);
		List<InsurApplOperTrace> insurApplOperTraces = mongoTemplate.find(query, InsurApplOperTrace.class);
		List<ContractBusiStateQueryVo> contractBusiStateQueryVos = new ArrayList<>();
		for(InsurApplOperTrace insurApplOperTrace:insurApplOperTraces){
			if(insurApplOperTrace==null){
				continue;
			}
			ContractBusiStateQueryVo reContractBusiStateQueryVo = new ContractBusiStateQueryVo();
			if(insurApplOperTrace.getOperTraceDeque()!=null && !insurApplOperTrace.getOperTraceDeque().isEmpty()){
				reContractBusiStateQueryVo.setBizzBranchNo(insurApplOperTrace.getOperTraceDeque().peekLast().getPclkBranchNo());
				reContractBusiStateQueryVo.setBizzNo(insurApplOperTrace.getOperTraceDeque().peekLast().getPclkNo());
				reContractBusiStateQueryVo.setBizzName(insurApplOperTrace.getOperTraceDeque().peekLast().getPclkName());
				reContractBusiStateQueryVo.setTaskPresentStates(getState(insurApplOperTrace.getCurrentTraceNode().getProcStat()));
			}
			reContractBusiStateQueryVo.setApplNo(insurApplOperTrace.getApplNo());
			InsurApplRegist insurApplRegist = mongoTemplate.findOne(new Query(Criteria.where("billNo").is(insurApplOperTrace.getApplNo())), InsurApplRegist.class);
			if(insurApplRegist!=null){
				reContractBusiStateQueryVo.setBizzDate(DateFormatUtils.format(insurApplRegist.getAcceptDate(),"yyyy/MM/dd HH:mm"));
				reContractBusiStateQueryVo.setContractBusiForm(StringUtils.isEmpty(insurApplRegist.getCntrType())?"":CNTR_TYPE.valueOfKey(insurApplRegist.getCntrType()).getDescription());
				reContractBusiStateQueryVo.setPolCode(insurApplRegist.getPolCode());
				reContractBusiStateQueryVo.setTotalPremium(String.valueOf(insurApplRegist.getSumPremium()));
				if(insurApplRegist.getSalesInfos()!=null){
					SalesInfo salesInfo = getSalesInfo(insurApplRegist);
					reContractBusiStateQueryVo.setSalesBranchNo(salesInfo.getSalesBranchNo());
					reContractBusiStateQueryVo.setSalesChannel(StringUtils.isEmpty(salesInfo.getSalesChannel())?"":PRD_SALES_CHANNEL.valueOfKey(salesInfo.getSalesChannel()).getDescription());
					reContractBusiStateQueryVo.setSalesNo(salesInfo.getSalesNo());
					reContractBusiStateQueryVo.setSalesName(salesInfo.getSalesName());
				}
				reContractBusiStateQueryVo.setApplName(insurApplRegist.getHldrName());
			}
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(Criteria.where("applNo").is(insurApplOperTrace.getApplNo())), GrpInsurAppl.class);
			if(grpInsurAppl != null){
				if(grpInsurAppl.getPaymentInfo()!=null){
					reContractBusiStateQueryVo.setPayForm(StringUtils.isEmpty(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())?"":MONEYIN_ITRVL.valueOfKey(grpInsurAppl.getPaymentInfo().getMoneyinItrvl()).getDescription());
				}
				String applName = "";
				if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())
					|| (StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey()) 
						&& StringUtils.equals(grpInsurAppl.getSgType(),LIST_TYPE.GRP_SG.getKey()))){
					applName = grpInsurAppl.getGrpHolderInfo()==null?"":grpInsurAppl.getGrpHolderInfo().getGrpName();
				}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey()) 
						&& StringUtils.equals(grpInsurAppl.getSgType(),LIST_TYPE.PSN_SG.getKey())){
					applName = grpInsurAppl.getPsnListHolderInfo()==null?"":grpInsurAppl.getPsnListHolderInfo().getSgName();
				}
				reContractBusiStateQueryVo.setApplName(applName);
				reContractBusiStateQueryVo.setBusiType(StringUtils.isEmpty(grpInsurAppl.getApplBusiType())?"":APPL_BUSI_TYPE.valueOfKey(grpInsurAppl.getApplBusiType()).getDescription());
			}
			StringBuilder string = new StringBuilder();
			string.append("<a data-toggle='tooltip' data-placement='bottom' title='");
			for (TraceNode traceNode : insurApplOperTrace.getOperTraceDeque()) {// 受理岗
                if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.ACCEPTED.getKey())) {
                    string.append("受理岗--进入受理岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 业务操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 受理状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } // 暂存岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.RECORD_TEMPORARILY.getKey())) {
                    string.append("暂存岗--进入暂存岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 业务操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 暂存状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } // 录入岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_ENTRY.getKey())
                        || StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_ENTRY.getKey())) {
                    string.append("录入岗--进入录入岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 录入操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 录入状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } // 导入岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IMPORT.getKey())
                        || StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IMPORT.getKey())) {
                    string.append("导入岗--进入导入岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 导入操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 导入状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (isReview(traceNode)) {
                    string.append("复核岗--进入复核岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 复核操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 复核状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                }
                // 人工审批岗
                else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_MANUAL_APPROVAL.getKey())
                        || StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_MANUAL_APPROVAL.getKey())
                        || isListInfoCheck(traceNode)) {
                    string.append("人工审批岗--进入人工审批岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 人工审批操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 人工审批状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.INSURED_ACCOUNT.getKey())) {
                    string.append("被保人开户岗--进入被保人开户岗位时间:");
                    string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    string.append(" 业务操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 被保人状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.UNDERWRITING.getKey())
                        || isUnderwriting(traceNode)) {
                    string.append("核保岗--进入核保岗位时间:");

                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 核保操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 核保状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GENERATE_RECEIV.getKey())) {
                    string.append("应收岗--进入应收岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 应收操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 应收状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.PAY_CHECK.getKey())) {
                    string.append("收费检查岗--进入收费检查岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 收费检查操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 收费检查状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.POLICY_INFORCE.getKey())) {
                    string.append("保单生效岗--进入保单生效岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 保单生效操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 保单生效状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.PRINT.getKey())) {
                    string.append("打印岗--进入打印岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 打印操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 打印状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.REVISE.getKey())) {
                    string.append("订正岗--进入订正岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 订正操作人员姓名:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 订正状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.RECEIPT_VERIFICA.getKey())) {
                    string.append("核销岗--进入核销岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 核销操作人员号:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 核销状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.BACK.getKey())) {
                    string.append("回退岗--进入回退岗位时间:");
                    if (null != traceNode.getProcDate()) {
                        string.append(DateFormatUtils.format(traceNode.getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                    }
                    string.append(" 回退操作人员号:");
                    string.append(StringUtils.isEmpty(traceNode.getPclkName()) ? " " : traceNode.getPclkName());
                    string.append(" 回退状态:");
                    string.append(getState(traceNode.getProcStat()));
                    string.append("\n");
                }
            }
            string.append("当前业务状态:");
            if (insurApplOperTrace.getCurrentTraceNode() != null && insurApplOperTrace.getCurrentTraceNode().getProcStat() != null) {
                string.append(getState(insurApplOperTrace.getCurrentTraceNode().getProcStat())+"，操作时间：");
                string.append(insurApplOperTrace.getCurrentTraceNode().getProcDate()==null?"":DateFormatUtils.format(insurApplOperTrace.getCurrentTraceNode().getProcDate(), "yyyy-MM-dd HH:mm:ss"));
                string.append("，操作人员：");
                string.append(insurApplOperTrace.getCurrentTraceNode().getPclkName()==null?"" : insurApplOperTrace.getCurrentTraceNode().getPclkName());
            }
            string.append("'>详细业务情况</a></td></tr>");
			reContractBusiStateQueryVo.setDetailBusiCondition(string.toString());
			contractBusiStateQueryVos.add(reContractBusiStateQueryVo);
		}
		return contractBusiStateQueryVos;
	}

	@Override
	public  PageData<QueryinfVo> query(PageQuery<CorrectionVo> pageQuery) {
		if(null == pageQuery){
			throw new BusinessException("0000","查询条件为空");
		}
		PageData<QueryinfVo> pageData = new PageData<>();
		Map<String, Object> resultMap = contractQueryDaoImpl.query(pageQuery);
		List<InsurApplRegist> query = (List<InsurApplRegist>) resultMap.get("list");
		List<QueryinfVo> arrayList = new  ArrayList<>();
		if(null != query && !query.isEmpty()){
			for (InsurApplRegist insurApplRegist : query) {
				Map<String, Object> map = new HashMap<>();
				map.put("applNo", insurApplRegist.getBillNo());
				QueryinfVo queryinfVo = new QueryinfVo();
				InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, map);
				if(null!=insurApplOperTrace && insurApplOperTrace.getCurrentTraceNode()!=null){
					TraceNode traceNode = insurApplOperTrace.getCurrentTraceNode();
					queryinfVo.setCurrentOperatorCode(traceNode.getPclkNo());
					queryinfVo.setCurrentOperatorName(traceNode.getPclkName());
					queryinfVo.setCurrentOperatorNum(traceNode.getPclkBranchNo());
					queryinfVo.setPostName(StringUtils.isEmpty(traceNode.getProcStat())?null:NEW_APPL_STATE.valueOfKey(traceNode.getProcStat()).getDescription());
					queryinfVo.setProcTime(traceNode.getProcDate()==null?null:DateFormatUtils.format(traceNode.getProcDate(),"yyyy-MM-dd"));
				}
				SalesInfo salesInfo = getSalesInfo(insurApplRegist);
				queryinfVo.setMgrBranchNo(salesInfo.getSalesBranchNo());
				queryinfVo.setApplNo(insurApplRegist.getBillNo());
				queryinfVo.setHldrName(insurApplRegist.getHldrName());
				queryinfVo.setApplDate(DateFormatUtils.format(insurApplRegist.getAcceptDate(),"yyyy-MM-dd"));
				queryinfVo.setCntrForm(CNTR_TYPE.valueOfKey(insurApplRegist.getCntrType()).getDescription());
				arrayList.add(queryinfVo);
			}
		}
		pageData.setData(arrayList);
		pageData.setTotalCount((long) resultMap.get("num"));
		return pageData;
	}

	@Override
	public GrpInsurAppl query(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			throw new BusinessException("0000","查询条件为空");
		}
		return contractQueryDaoImpl.queryone(grpInsurAppl);
	}
	
	private SalesInfo getSalesInfo(InsurApplRegist insurApplRegist){
		for(SalesInfo salesInfo1:insurApplRegist.getSalesInfos()){
			if(StringUtils.equals(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey(),salesInfo1.getDevelMainFlag())){
				return salesInfo1;
			}
		}
		return insurApplRegist.getSalesInfos().get(0);
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
	
	private boolean isReview(TraceNode traceNode) {
        if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_INFO_REVIEW.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_REVIEW.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_INFO_CHECK_SUC.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_CHECK_SUC.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_INFO_CHECK_FAL.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_CHECK_FAL.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_SUC.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_SUC.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_FAL.getKey())) {
            return true;
        } else {
            return false;
        }
    }

    // 人工审核验证
    private boolean isListInfoCheck(TraceNode traceNode) {
        if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_APPROVE_CHECK_SUC.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_APPROVE_CHECK_FAL.getKey())) {
            return true;
        } else {
            return false;
        }

    }

    // 核保岗状态验证
    private boolean isUnderwriting(TraceNode traceNode) {
        if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.INSURANCE_COVERAGE.getKey())) {
            return true;
        } else if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.EXTENSION.getKey())) {
            return true;
        } else {
            return false;
        }
    }
    
    //获取前台要显示的状态值
    private String getState(String procStat){
    	String states = "";
    	if(StringUtils.equals(procStat,NEW_APPL_STATE.ACCEPTED.getKey())){
			states = "受理完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.RECORD_TEMPORARILY.getKey())){
			states = "基本信息正录入";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_ENTRY.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_ENTRY.getKey())
		){
			states = "基本信息录入完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_IMPORT.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_IMPORT.getKey())
		){
			states = "清单导入/录入完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_INFO_REVIEW.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_INFO_REVIEW.getKey())
		){
			states = "基本信息复核完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_MANUAL_APPROVAL.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_MANUAL_APPROVAL.getKey())
		){
			states = "人工审批完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_IPSN_REVIEW.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_IPSN_REVIEW.getKey())
		){
			states = "被保人清单复核完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.INSURED_ACCOUNT.getKey())){
			states = "被保人开户完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.UNDERWRITING.getKey())){
			states = "核保完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GENERATE_RECEIV.getKey())){
			states = "生成应收";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.PAY_CHECK.getKey())){
			states = "收费检查";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.POLICY_INFORCE.getKey())){
			states = "保单生效";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.PRINT.getKey())){
			states = "打印完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.REVISE.getKey())){
			states = "订正完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_INFO_CHECK_SUC.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_INFO_CHECK_SUC.getKey())
		){
			states = "基本信息复核通过";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_INFO_CHECK_FAL.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_INFO_CHECK_FAL.getKey())
		){
			states = "基本信息复核不通过(基本信息正录入)";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_IPSN_CHECK_FAL.getKey())
		){
			states = "被保人清单复核不通过(基本信息正录入)";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_APPROVE_CHECK_SUC.getKey())
		){
			states = "人工审批通过";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey())
			|| StringUtils.equals(procStat,NEW_APPL_STATE.LIST_APPROVE_CHECK_FAL.getKey())
		){
			states = "人工审批不通过(基本信息复核)";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.INSURANCE_COVERAGE.getKey())){
			states = "拒保";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.OFFER_WITHDRAWN.getKey())){
			states = "撤销";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.RECEIPT_VERIFICA.getKey())){
			states = "回执核销完成";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.EXTENSION.getKey())){
			states = "延期";
		}else if(StringUtils.equals(procStat,NEW_APPL_STATE.BACK.getKey())){
			states = "回退完成";
		}
    	return states;
    }
}
