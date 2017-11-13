package com.newcore.orbps.service.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.udw.UdwIpsnPolResult;
import com.newcore.orbps.models.service.bo.udw.UdwIpsnResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.service.api.UdwResultService;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.UDW_RESULT;
import com.newcore.tsms.service.api.task.TaskProcessService;


/**
 * @author huanglong
 * @date 2016年9月5日
 * 内容:人工核保服务
 */
@Service("udwResultService")
public class UdwResultServiceImpl implements UdwResultService{

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	TaskProcessService taskProcessServiceDascClient;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(UdwResultServiceImpl.class);

	@Override
	public RetInfo setUdwResult(ApplUdwResult udwResult) {
		RetInfo retInfo = new RetInfo();//返回类
		String[] succFlag ={"0","1"};//成功标记
		if(udwResult == null){
			retInfo.setRetCode(succFlag[0]);
			retInfo.setErrMsg("入参不能都为空 ");
			logger.error("入参不能都为空 ");
			return retInfo;
		}
		if(StringUtils.isEmpty(udwResult.getBusinessKey())){
			retInfo.setRetCode(succFlag[0]);
			retInfo.setErrMsg("业务流水号businessKey不能为空 ");
			logger.error("业务流水号businessKey不能为空 ");
			return retInfo;
		}
		/*对GrpInsruAppl进行核保信息插入*/
		/*操作mongodb数据库*/
		Map< String, Object> map = new HashMap<>();
		map.put("applNo", udwResult.getBusinessKey());
		/*操作轨迹  核保节点 存储*/
		TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(udwResult.getPclkBranchNo());//核保操作员机构
		traceNode.setPclkName(udwResult.getPclkName());//核保操作员姓名
		traceNode.setPclkNo(udwResult.getPclkNo());//核保操作员代码
		traceNode.setProcDate(new Date());//核保完成日期		

		/*如果整单核保结论为  拒保 延期  撤销  脱落 将调保单撤销流程*/
		if(UDW_RESULT.REJECT.getKey().equals(udwResult.getUdwResult()) ||
				UDW_RESULT.DELAY.getKey().equals(udwResult.getUdwResult()) ||
				UDW_RESULT.WITHDRAW.getKey().equals(udwResult.getUdwResult()) ||
				UDW_RESULT.CASE_OFF.getKey().equals(udwResult.getUdwResult()) ){
			traceNode.setProcStat(NEW_APPL_STATE.INSURANCE_COVERAGE.getKey());//核保结论--拒保
			/*对轨迹操作InsurApplOperTrace 增加核保节点*/
			mongoBaseDao.updateOperTrace(udwResult.getBusinessKey(), traceNode);
			/*对核保结论插入数据库*/
			/*对于核保结论落地先删后插*/
			map.clear();
			map.put("businessKey", udwResult.getBusinessKey());
			mongoBaseDao.remove(ApplUdwResult.class, map);
			mongoBaseDao.insert(udwResult);
			retInfo.setApplNo(udwResult.getBusinessKey());//投保单号
			retInfo.setRetCode(succFlag[1]);

			return retInfo;
		}
		/*防止核保平台对整单核保结论没有进行赋值，  需要对险种核保结论进行校验 有一个或者一个以上的险种是 拒保 延期  撤销  脱落 该保单应被拒保*/
		String udwConvStr ="";
		List<UdwPolResult> udwPolResults = udwResult.getUdwPolResults();
		udwPolResults.removeAll(Collections.singleton(null));
		for(UdwPolResult udwPolResult:udwPolResults){
			if(UDW_RESULT.REJECT.getKey().equals(udwPolResult.getUdwResult()) ||
					UDW_RESULT.DELAY.getKey().equals(udwPolResult.getUdwResult()) ||
					UDW_RESULT.WITHDRAW.getKey().equals(udwPolResult.getUdwResult()) ||
					UDW_RESULT.CASE_OFF.getKey().equals(udwPolResult.getUdwResult()) ){
				udwResult.setUdwResult(udwPolResult.getUdwResult());
				traceNode.setProcStat(NEW_APPL_STATE.INSURANCE_COVERAGE.getKey());//核保结论--拒保
				/*对轨迹操作InsurApplOperTrace 增加核保节点*/
				mongoBaseDao.updateOperTrace(udwResult.getBusinessKey(), traceNode);
				/*对核保结论插入数据库*/
				/*对于核保结论落地先删后插*/
				map.clear();
				map.put("businessKey", udwResult.getBusinessKey());
				mongoBaseDao.remove(ApplUdwResult.class, map);
				mongoBaseDao.insert(udwResult);
				retInfo.setApplNo(udwResult.getBusinessKey());//投保单号
				retInfo.setRetCode(succFlag[1]);
				return retInfo;
			}
			//有条件承保
			if(UDW_RESULT.CONDITION.getKey().equals(udwPolResult.getUdwResult()) && !StringUtils.isEmpty(udwPolResult.getUdwOpinion())){
				udwConvStr += "险种  "+udwPolResult.getPolCode()+":"+udwPolResult.getUdwOpinion()+"\n";
			}
		}
		/*对特约进行赋值*/
		if(!StringUtils.isEmpty(udwConvStr)){
			Update update = new Update();
			update.set("conventions.udwConv",udwConvStr);
			mongoBaseDao.update(GrpInsurAppl.class, map, update);			
		}
		traceNode.setProcStat(NEW_APPL_STATE.UNDERWRITING.getKey());//核保结论--核保
		/*对轨迹操作InsurApplOperTrace 增加核保节点*/
		mongoBaseDao.updateOperTrace(udwResult.getBusinessKey(), traceNode);	

		/*对被保人核保结论进行处理*/
		List<UdwIpsnResult> udwIpsnResults = udwResult.getUdwIpsnResults();
		if(null != udwIpsnResults){
			udwIpsnResults.removeAll(Collections.singleton(null));
			for(UdwIpsnResult udwIpsnResult:udwIpsnResults){
				//对被保人核保结论 进行 存储
				udwIpsnToGrpInsured(udwIpsnResult,udwResult.getBusinessKey());
			}      	
		}

		/*对核保结论插入数据库*/
		/*对于核保结论落地先删后插*/
		map.clear();
		map.put("businessKey", udwResult.getBusinessKey());
		mongoBaseDao.remove(ApplUdwResult.class, map);
		mongoBaseDao.insert(udwResult);
		retInfo.setApplNo(udwResult.getBusinessKey());//投保单号
		retInfo.setRetCode(succFlag[1]);
		retInfo.setErrMsg("交易成功");
		return retInfo;
	}

	/*对被保人核保结论 进行 存储  如果被保人 下的险种结论有一个 是 拒保 ，延期，撤销，脱落  该被保人即 无效*/
	private void udwIpsnToGrpInsured(UdwIpsnResult udwIpsnResult,String applNo){
		if(null == udwIpsnResult || null == udwIpsnResult.getUdwIpsnPolResults() ||udwIpsnResult.getUdwIpsnPolResults().isEmpty()){
			return;
		}
		//被保人是否有效  E：有效；O：作废
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		map.put("ipsnNo", udwIpsnResult.getIpsnNo());
		Update update = new Update();

		List<UdwIpsnPolResult> udwIpsnPolResults = udwIpsnResult.getUdwIpsnPolResults();
		udwIpsnPolResults.removeAll(Collections.singleton(null));
		for(UdwIpsnPolResult udwIpsnPolResult:udwIpsnPolResults){
			String updateString = "procStat";
			if( UDW_RESULT.REJECT.getKey().equals(udwIpsnPolResult.getUdwResult())){
				update.set(updateString, "O");
				update.set("remark",UDW_RESULT.REJECT.getDescription());
				mongoBaseDao.update(GrpInsured.class, map, update);
				return;
			}
			if (UDW_RESULT.DELAY.getKey().equals(udwIpsnPolResult.getUdwResult())) {
				update.set(updateString, "O");
				update.set("remark",UDW_RESULT.DELAY.getDescription());
				mongoBaseDao.update(GrpInsured.class, map, update);
				return;
			}
			if (UDW_RESULT.WITHDRAW.getKey().equals(udwIpsnPolResult.getUdwResult())) {
				update.set(updateString, "O");
				update.set("remark",UDW_RESULT.WITHDRAW.getDescription());
				mongoBaseDao.update(GrpInsured.class, map, update);
				return;
			}
			if (UDW_RESULT.CASE_OFF.getKey().equals(udwIpsnPolResult.getUdwResult())) {
				update.set(updateString, "O");
				update.set("remark",UDW_RESULT.CASE_OFF.getDescription());
				mongoBaseDao.update(GrpInsured.class, map, update);
				return;
			}
			if(Math.abs(udwIpsnPolResult.getAddFeeAmnt()) > 0.001){
				/*更新子要约加费后保费*/
				Query query = new Query();		
				query.addCriteria(Criteria.where("applNo").is(applNo));
				query.addCriteria(Criteria.where("ipsnNo").is(udwIpsnResult.getIpsnNo()));
				query.addCriteria(Criteria.where("subStateList.polCode").is(udwIpsnPolResult.getPolCode()));								
				update.inc("subStateList.$.sumPremium", udwIpsnPolResult.getAddFeeAmnt());
				mongoTemplate.updateFirst(query, update, GrpInsured.class);
				/*更新被保人要约 加费后保费*/
				Query query2 = new Query();
				query2.addCriteria(Criteria.where("applNo").is(applNo));
				query2.addCriteria(Criteria.where("ipsnNo").is(udwIpsnResult.getIpsnNo()));
				query2.addCriteria(Criteria.where("ipsnStateList.polCode").is(udwIpsnPolResult.getPolCode().trim().substring(0,3)));
				Update update2 = new Update();
				update2.inc("ipsnStateList.$.sumPremium", udwIpsnPolResult.getAddFeeAmnt());
				mongoTemplate.updateFirst(query2, update2, GrpInsured.class);
			}
		}
	}
}
