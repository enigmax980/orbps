package com.newcore.orbps.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryInfo;
import com.newcore.orbps.models.pcms.bo.WebsiteQueryReturnBo;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.aa.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.insurapplregist.ApplInfo;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.models.service.bo.insurapplregist.RegistInfo;
import com.newcore.orbps.service.api.InsuredAcceptService;
import com.newcore.orbps.service.business.ValidatorUtils;
import com.newcore.orbps.service.ipms.api.InsurSummaryInfoService;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.service.pcms.api.WebsiteInfoService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.CURRENCY_CODE;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.SALES_TAGET;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * @author wangxiao 创建时间：2016年10月24日下午2:08:53
 */
@Service("insuredAcceptService")
public class InsuredAcceptServiceImpl implements InsuredAcceptService {
	private Logger logger = LoggerFactory.getLogger(InsuredAcceptServiceImpl.class);
	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	TaskProcessService taskProcessServiceDascClient;
	@Autowired
	ConfigDao configDao;
	@Autowired
	SalesmanInfoService resulsalesmanInfoService;
	@Autowired
	WebsiteInfoService resulwebsiteInfoService;
	@Autowired
	PolicyQueryService resulpolicyQueryService;
	@Autowired
	InsurSummaryInfoService resulInsurSummaryInfoService;
	
	private static final String PROPERTIE_TYPE= "UDW_OFF" ;  //配置类型

	/**
	 * 投保受理
	 * 
	 * @param InsurApplRegist
	 * @return RetInfo
	 */
	@Override
	@AsynCall
	public RetInfo accept(RegistInfo registInfo) {
		RetInfo retInfo = new RetInfo();
		if (registInfo == null) {
			throw new BusinessException("0008");
		}
		InsurApplRegist insurApplRegist = registInfo.getInsurApplRegist();
		if (registInfo.getInsurApplRegist() == null) {
			throw new BusinessException("0007");
		}
		if (StringUtils.isEmpty(insurApplRegist.getBillNo())) {
			throw new BusinessException("0009");
		}
		TraceNode traceNode = registInfo.getTraceNode();
		if (registInfo.getTraceNode() == null || StringUtils.isEmpty(traceNode.getPclkBranchNo())
				|| StringUtils.isEmpty(traceNode.getPclkNo())) {
			throw new BusinessException("0019", insurApplRegist.getBillNo());
		}
		Map<String, Object> map = new HashMap<>();
		map.put("billNo", insurApplRegist.getBillNo());
		if (mongoBaseDao.findOne(InsurApplRegist.class, map) != null) {
			throw new BusinessException("0010");
		}
		String error = checkInsurApplRegist(insurApplRegist);
		if (error.length() == 0) {
			traceNode.setProcStat(NEW_APPL_STATE.ACCEPTED.getKey());
			traceNode.setProcDate(new Date());
			InsurApplOperTrace insurApplOperTrace = new InsurApplOperTrace();
			insurApplOperTrace.pushTraceNode(traceNode);
			insurApplOperTrace.setApplNo(insurApplRegist.getBillNo());
			mongoBaseDao.insert(insurApplRegist);
			mongoBaseDao.insert(insurApplOperTrace);
			retInfo.setRetCode("1");
			retInfo.setApplNo(insurApplRegist.getBillNo());
			startProcess(insurApplRegist.getBillNo(), insurApplRegist.getCntrType(), traceNode.getPclkBranchNo(),traceNode.getPclkNo());
		} else {
			retInfo.setErrMsg(error);
			retInfo.setRetCode("0");
		}
		return retInfo;
	}

	/**
	 * 修改并保存
	 * 
	 * @param InsurApplRegist
	 * @return RetInfo
	 */
	@Override
	@AsynCall
	public RetInfo update(RegistInfo registInfo) {
		RetInfo retInfo = new RetInfo();
		if (registInfo == null) {
			throw new BusinessException("0008");
		}
		InsurApplRegist insurApplRegist = registInfo.getInsurApplRegist();
		if (registInfo.getInsurApplRegist() == null) {
			throw new BusinessException("0007");
		}
		if (StringUtils.isEmpty(insurApplRegist.getBillNo())) {
			throw new BusinessException("0009");
		}
		TraceNode traceNode = registInfo.getTraceNode();
		if (registInfo.getTraceNode() == null || StringUtils.isEmpty(traceNode.getPclkBranchNo())
				|| StringUtils.isEmpty(traceNode.getPclkNo())) {
			throw new BusinessException("0019", insurApplRegist.getBillNo());
		}
		traceNode.setProcStat(NEW_APPL_STATE.ACCEPTED.getKey());
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", insurApplRegist.getBillNo());
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,
				map);
		if (insurApplOperTrace == null) {
			throw new BusinessException("0011");
		} else if (!(StringUtils.equals(NEW_APPL_STATE.ACCEPTED.getKey(),
				insurApplOperTrace.getCurrentTraceNode().getProcStat())
				|| StringUtils.equals(NEW_APPL_STATE.BACK.getKey(),
						insurApplOperTrace.getCurrentTraceNode().getProcStat()) 
				|| StringUtils.equals(NEW_APPL_STATE.RECORD_TEMPORARILY.getKey(),
						insurApplOperTrace.getCurrentTraceNode().getProcStat()))) {
			throw new BusinessException("0012");
		}

		String error = checkInsurApplRegist(insurApplRegist);
		if (StringUtils.isEmpty(error)) {

			// 如果是回退后的受理修改，如果修改了保单类型，需要重新发起流程
			if (StringUtils.equals(NEW_APPL_STATE.BACK.getKey(), insurApplOperTrace.getCurrentTraceNode().getProcStat())
					|| StringUtils.equals(NEW_APPL_STATE.ACCEPTED.getKey(),insurApplOperTrace.getCurrentTraceNode().getProcStat()) 
					|| StringUtils.equals(NEW_APPL_STATE.RECORD_TEMPORARILY.getKey(), insurApplOperTrace.getCurrentTraceNode().getProcStat())) {
				Map<String, Object> findApplRegDb = new HashMap<>();
				findApplRegDb.put("billNo", insurApplRegist.getBillNo());
				InsurApplRegist insurApplRegistDB = (InsurApplRegist) mongoBaseDao.findOne(InsurApplRegist.class,
						findApplRegDb);
				if (null == insurApplRegistDB) {
					throw new BusinessException("0017");
				}
				// 保单类型发生了变化，则重新发起流程
				if (!StringUtils.equals(insurApplRegistDB.getCntrType(), insurApplRegist.getCntrType())) {
					logger.info("修改了合同类型，需重新发起流程……");
					TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
					Map<String, String> mapVar = new HashMap<>();
					mapVar.put("ACCEPT_BRANCH_NO", traceNode.getPclkBranchNo());
					if (StringUtils.equals(insurApplRegist.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
						mapVar.put("CNTR_TASK_SUB_ITEM", "GRP");
						mapVar.put("NODE_STATUS", "1");
					}
					if (StringUtils.equals(insurApplRegist.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
						mapVar.put("CNTR_TASK_SUB_ITEM", "LST");
						mapVar.put("NODE_STATUS", "2");
					}
					//读取配置信息
					Map<String, Object> applNoMap = new HashMap<String, Object>();
					applNoMap.put("applNo", insurApplOperTrace.getApplNo());
					applNoMap.put("operTraceDeque.procStat", NEW_APPL_STATE.ACCEPTED.getKey());
					InsurApplOperTrace applOperTrace=(InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,  applNoMap);
					String enabled = configDao.queryPropertiesConfigure(PROPERTIE_TYPE,applOperTrace.getOperTraceDeque().getLast().getPclkNo(),null);
					//如果enabled="Y" 则禁止核保 
					if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),enabled)){
						taskProcessRequestBO.setProcessDefKey("ECORBP003");
					}else{
						taskProcessRequestBO.setProcessDefKey("ECORBP002");// 必填-流程定义ID
					}
					taskProcessRequestBO.setBusinessKey(insurApplRegist.getBillNo());// 必填-业务流水号
					taskProcessRequestBO.setProcessVar(mapVar);
					taskProcessServiceDascClient.restartProcessInstance(taskProcessRequestBO);
					// 增加操作轨迹
					// mongoBaseDao.updateOperTrace(insurApplRegist.getBillNo(),
					// traceNode);
					mongoBaseDao.remove(GrpInsurAppl.class, map);
					mongoBaseDao.remove(GrpInsured.class, map);
				}

				// 增加操作轨迹
				mongoBaseDao.updateOperTrace(insurApplRegist.getBillNo(), traceNode);
				Map<String, Object> acceptMap = new HashMap<>();
				acceptMap.put("billNo", insurApplRegist.getBillNo());
				mongoBaseDao.remove(InsurApplRegist.class, acceptMap);
				mongoBaseDao.insert(insurApplRegist);
				retInfo.setRetCode("1");

			}

		} else {
			retInfo.setErrMsg(error);
			retInfo.setRetCode("0");
		}
		return retInfo;
	}

	/**
	 * 查询
	 * 
	 * @param Map<String,String>
	 * @return InsurApplRegist
	 */
	@Override
	public RegistInfo search(Map<String, String> map) {
		Map<String, Object> mapVar = new HashMap<>();
		mapVar.put("billNo", map.get("billNo"));
		InsurApplRegist insurApplRegist = (InsurApplRegist) mongoBaseDao.findOne(InsurApplRegist.class, mapVar);
		Map<String, Object> mapVar1 = new HashMap<>();
		mapVar1.put("applNo", map.get("billNo"));
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, mapVar1);
		TraceNode traceNode = new TraceNode();
		if(insurApplOperTrace!=null && insurApplOperTrace.getOperTraceDeque()!=null){
			traceNode = insurApplOperTrace.getOperTraceDeque().peekLast();
		}
		RegistInfo registInfo = new RegistInfo();
		registInfo.setInsurApplRegist(insurApplRegist);
		registInfo.setTraceNode(traceNode);
		return registInfo;
	}
	
	/**
	 * 受理删除
	 * 
	 * @param InsurApplRegist
	 * @return RetInfo
	 */
	@Override
	@AsynCall
	public RetInfo delete(Map<String, String> map) {
		RetInfo retInfo = new RetInfo();
		Map<String, Object> map1 = new HashMap<>();
		map1.put("applNo", map.get("billNo"));
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,
				map1);
		if (insurApplOperTrace == null) {
			throw new BusinessException("0011");
		} else if (!(StringUtils.equals(NEW_APPL_STATE.ACCEPTED.getKey(),
				insurApplOperTrace.getCurrentTraceNode().getProcStat())
				|| StringUtils.equals(NEW_APPL_STATE.BACK.getKey(),
						insurApplOperTrace.getCurrentTraceNode().getProcStat()) 
				|| StringUtils.equals(NEW_APPL_STATE.RECORD_TEMPORARILY.getKey(),
						insurApplOperTrace.getCurrentTraceNode().getProcStat()))) {
			throw new BusinessException("0012");
		}
		Map<String, Object> map2 = new HashMap<>();
		map2.put("billNo", map.get("billNo"));
		int i = mongoBaseDao.remove(InsurApplRegist.class, map2);
		mongoBaseDao.remove(GrpInsurAppl.class, map1);
		mongoBaseDao.remove(GrpInsured.class, map1);
		mongoBaseDao.remove(ErrorGrpInsured.class, map1);
		mongoBaseDao.remove(InsurApplOperTrace.class, map1);
		if(i==1){
			retInfo.setApplNo(map.get("billNo"));
			retInfo.setRetCode("1");
			//受理信息删除成功撤销流程
			TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
			taskProcessRequestBO.setProcessDefKey("ECORBP002");// 契约柜面系统流程
			taskProcessRequestBO.setBusinessKey(map.get("billNo"));// 必填-业务流水号
			taskProcessServiceDascClient.deleteProcessInstance(taskProcessRequestBO);
		}else{
			retInfo.setApplNo(map.get("billNo"));
			retInfo.setRetCode("0");
			retInfo.setErrMsg("删除受理信息失败");
		}
		return retInfo;
	}
	//受理校验
	private String checkInsurApplRegist(InsurApplRegist insurApplRegist) {
		StringBuilder error = new StringBuilder();
		if (StringUtils.isEmpty(insurApplRegist.getBillNo())) {
			error.append("单证号码为空|");
		} else if (insurApplRegist.getBillNo().length() != 16) {
			error.append("单证号码长度不符合16位|");
		}
		if (insurApplRegist.getAcceptDate() == null) {
			error.append("申请日期为空|");
		}
		if (ValidatorUtils.testYesNo(insurApplRegist.getIsCommonAgreement())) {
			error.append("是否共保不符合字典检查|");
		}
		if (StringUtils.equals(insurApplRegist.getIsCommonAgreement(), YES_NO_FLAG.YES.getKey())
				&& StringUtils.isEmpty(insurApplRegist.getAgreementNo())) {
			error.append("共保时共保协议号为空|");
		} else if (StringUtils.equals(insurApplRegist.getIsCommonAgreement(), YES_NO_FLAG.YES.getKey())) {
			Map<String, Object> map = new HashMap<>();
			map.put("agreementNo", insurApplRegist.getAgreementNo());
			CommonAgreement commonAgreement = (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class, map);
			if (commonAgreement == null) {
				error.append("共保号对应的共保基本信息不存在|");
				// 共保险种校验
			} else {
				String s = checkCommonAgreement(commonAgreement, insurApplRegist);
				error.append(s);
			}
		}
		if (ValidatorUtils.testCntrType(insurApplRegist.getCntrType())) {
			error.append("契约形式不符合字典检查|");
		}
		if (StringUtils.equals(insurApplRegist.getCntrType(), CNTR_TYPE.PERSON_INSUR.getKey())
				&& StringUtils.isEmpty(insurApplRegist.getComPolCode())) {
			error.append("契约形式为个单时，险种组合代码为空|");
		}
		if (StringUtils.isEmpty(insurApplRegist.getCurrencyCode())) {
			insurApplRegist.setCurrencyCode(CURRENCY_CODE.CNY.getKey());
		}
		if (StringUtils.isEmpty(insurApplRegist.getPolCode())) {
			error.append("险种代码为空|");
		} else {
			checkPolCode(error, insurApplRegist);
		}
		if (StringUtils.isEmpty(insurApplRegist.getPolNameChn())) {
			error.append("险种名称为空|");
		}
		if (insurApplRegist.getIpsnNum() == null || insurApplRegist.getIpsnNum() == 0) {
			error.append("被保险人数为空或为0|");
		}
		if (ValidatorUtils.testCurrencyCode(insurApplRegist.getCurrencyCode())) {
			error.append("币种不符合字典检查|");
		}
		if (insurApplRegist.getSumPremium() == null || insurApplRegist.getSumPremium() < 0.0001) {
			error.append("保费合计为空或为0|");
		}
		if (StringUtils.isEmpty(insurApplRegist.getHldrName())) {
			error.append("投保人/汇交人名称为空|");
		}
		if (insurApplRegist.getBillNumber() == null || insurApplRegist.getBillNumber() == 0) {
			error.append("投保单数量为空或为0|");
		}
		if (StringUtils.isEmpty(insurApplRegist.getShareCustFlag())) {
			error.append("共享客户信息为空|");
		}
		if (ValidatorUtils.testYesNo(insurApplRegist.getEntChanelFlag())) {
			error.append("外包录入标记不符合字典检查|");
		}
		// 个汇投保单号列表 applNoList ArrayList<String> C
		if (StringUtils.isEmpty(insurApplRegist.getSalesDevelopFlag())) {
			insurApplRegist.setSalesDevelopFlag("N");
		} else if (ValidatorUtils.testYesNo(insurApplRegist.getSalesDevelopFlag())) {
			error.append("销售人员是否共同展业标识不符合字典检查|");
		}
		if (insurApplRegist.getApplInfos() != null && !insurApplRegist.getApplInfos().isEmpty()) {
			checkApplInfoType(error, insurApplRegist.getApplInfos());
		}
		// 校验销售信息
		if (insurApplRegist.getSalesInfos() == null || (insurApplRegist.getSalesInfos().isEmpty())) {
			error.append("销售信息为空|");
		} else {
			if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), insurApplRegist.getSalesDevelopFlag())
					&& (insurApplRegist.getSalesInfos().size() < 2)) {
				error.append("共同展业时销售员应为多个|");
			} else if (StringUtils.equals(YES_NO_FLAG.NO.getKey(), insurApplRegist.getSalesDevelopFlag())
					&& (insurApplRegist.getSalesInfos().size() != 1)) {
				error.append("非共同展业时销售员应为一个|");
			}
			double sumDevelopRate = 0.0;
			for (int i = 0; i < insurApplRegist.getSalesInfos().size(); i++) {
				SalesInfo salesInfo = insurApplRegist.getSalesInfos().get(i);
				// 多销售员共同展业，各个销售员需填写主副展业标记
				if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), insurApplRegist.getSalesDevelopFlag())
						&& ValidatorUtils.testDevelMainFlag(salesInfo.getDevelMainFlag())) {
					error.append("共同展业主副展业标记不符合字典检查|");
				}
				if (null != salesInfo) {
					checkSalesInfo(error, salesInfo);
				}
				if (null != salesInfo && salesInfo.getDevelopRate() != null) {
					sumDevelopRate += salesInfo.getDevelopRate();
				}
			}
			if (insurApplRegist.getSalesInfos().size() > 1 && Math.abs(sumDevelopRate - 1) > 0.001) {
				error.append("共同展业展业比例之和不等于1|");
			}
		}
		return error.toString();
	}

	private void checkPolCode(StringBuilder error, InsurApplRegist insurApplRegist) {
		/* 对险种进行校验 */
		// 保单基本信息提交校验-产品编码联调
		// 获取第一个险种，判断是不是主险，.获取第一个险种，判断是不是主险，
		// 校验险种
		// 产品组查询赋值
		Map<String, Object> polMap = new HashMap<>();
		polMap.put("polCode", insurApplRegist.getPolCode());
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Map<String, Object> map1 = resulInsurSummaryInfoService.excute(polMap);
		if (null == map1 || (map1.containsKey("errorCode") && map1.get("errorCode").equals("8888"))) {
			error.append("|查询险种销售对象为空|");
		} else {
			JSONObject insurSummaryInfoBo = (JSONObject) map1.get("insurSummaryInfoBo");
			if (insurSummaryInfoBo == null || insurSummaryInfoBo.isEmpty()) {
				error.append("|查询险种销售对象为空|");
			} else {
				String salesTaget = insurSummaryInfoBo.getString("salesTaget");
				if (StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(), insurApplRegist.getCntrType())
						&& StringUtils.equals(salesTaget, SALES_TAGET.PERSON_SALE.getKey())) {
					error.append("|保单类型为团单，险种[" + insurApplRegist.getPolCode() + "]销售对象为个人|");
				}
				if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), insurApplRegist.getCntrType())
						&& StringUtils.equals(salesTaget, SALES_TAGET.GROUP_SALE.getKey())) {
					error.append("|保单类型为清汇，险种[" + insurApplRegist.getPolCode() + "]销售对象为团体|");
				}				
			}
		}
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Map<String, Object> polBoMap = resulpolicyQueryService.excute(polMap);
		if (polBoMap.get("policyBo") == null) {
			error.append("查询险种概要,没有引用的对象或集合为空.|");
		} else {
			JSONObject obj = JSONObject.parseObject(JSON.toJSONString(polBoMap));
			if (!StringUtils.equals((String) obj.getJSONObject("policyBo").get("mrCode"), MR_CODE.MASTER.getKey())) {
				error.append("险种代码不是主险种|");
			}
			// 2.1获取险种名称polNameChn
			String polNameStr = obj.getJSONObject("policyBo").getString("polNameChn");
			insurApplRegist.setPolNameChn(polNameStr);
			// 获取币种
			String currencyCodeStr = obj.getJSONObject("policyBo").getString("currencyCode");
			// 判断币种是否一致
			if (!StringUtils.equals(insurApplRegist.getCurrencyCode(), currencyCodeStr)) {
				error.append("币种不一致|");
			} // end if-判断币种是否一致
				// 判断险种是否可销售，开售日期<=当前的日期<= 停手日期
			String endTimeStr = obj.getJSONObject("policyBo").getString("polEndDate");
			String beginTimeStr = obj.getJSONObject("policyBo").getString("polStartDate");
			SimpleDateFormat formate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			Date endDate = null;
			Date beginDate = null;
			try {
				endDate = formate.parse(endTimeStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			try {
				beginDate = formate.parse(beginTimeStr);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
			if (!(beginDate.getTime() <= date.getTime() && date.getTime() <= endDate.getTime())) {
				// 停售
				error.append("险种已停售.|");
			}
		}
	}

	private void checkApplInfoType(StringBuilder error, List<ApplInfo> applInfos) {
		List<String> list = new ArrayList<>();
		for (ApplInfo applInfo : applInfos) {
			if (list.contains(applInfo.getApplInfoType())) {
				error.append("投保资料类型重复|");
			} else {
				list.add(applInfo.getApplInfoType());
			}
		}
	}

	private void checkSalesInfo(StringBuilder error, SalesInfo salesInfo) {
		SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();
		WebsiteQueryInfo websiteQueryInfo = new WebsiteQueryInfo();
		if (null != salesInfo) {
			if (StringUtils.isEmpty(salesInfo.getSalesBranchNo())) {
				error.append("销售机构为空|");
			} else {
				salesmanQueryInfo.setSalesBranchNo(salesInfo.getSalesBranchNo());
				websiteQueryInfo.setSalesBranchNo(salesInfo.getSalesBranchNo());
			}
			if (StringUtils.isEmpty(salesInfo.getSalesChannel())) {
				error.append("销售渠道为空|");
			} else {
				salesmanQueryInfo.setSalesChannel(salesInfo.getSalesChannel());
				websiteQueryInfo.setSalesChannel(salesInfo.getSalesChannel());
			}

			if (!StringUtils.isEmpty(salesInfo.getSalesDeptNo()) && !StringUtils.isEmpty(salesInfo.getSalesNo())) {
				salesmanQueryInfo.setSalesNo(salesInfo.getSalesNo());
				//网点与操作员同时存在时，查询业务员，用业务员销售渠道
				salesmanQueryInfo.setSalesChannel("BS");
				websiteQueryInfo.setSalesDeptNo(salesInfo.getSalesDeptNo());
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
				CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo1);
				WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
				if (null == websiteQuery && null == queryReturnBo) {
					throw new BusinessException("0000", "查询销售员，销售网点信息有误");
				}
				if (null != websiteQuery) {// 获得网点信息
					if (StringUtils.equals("1", websiteQuery.getRetCode())
							&& StringUtils.equals("1", websiteQuery.getDeptState())) {
						salesInfo.setDeptName(websiteQuery.getDeptName());
					} else if (StringUtils.equals("1", websiteQuery.getRetCode())
							&& StringUtils.equals("0", websiteQuery.getDeptState())) {
						error.append("查询网点无效|");
					} else {
						error.append("查询网店返回信息：");
						error.append(websiteQuery.getErrMsg());
						error.append("|");
					}

				}
				if (null != queryReturnBo) {// 销售员工信息
					if (StringUtils.equals("1", queryReturnBo.getSaleState())) {
						salesInfo.setSalesName(queryReturnBo.getSalesName());// 销售员姓名
					} else {
						error.append("查询员工返回信息：");
						error.append(queryReturnBo.getErrMsg());
						error.append("|");
					}
				}
				// 如果网点不为空销售员工为空 信息以网点为主
			} else if (!StringUtils.isEmpty(salesInfo.getSalesDeptNo())
					&& StringUtils.isEmpty(salesInfo.getSalesNo())) {
				websiteQueryInfo.setSalesDeptNo(salesInfo.getSalesDeptNo());
				CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo1);
				WebsiteQueryReturnBo websiteQuery = resulwebsiteInfoService.queryWebsite(websiteQueryInfo);
				if (null != websiteQuery) {// 获得网点信息
					if (StringUtils.equals("1", websiteQuery.getRetCode())
							&& StringUtils.equals("1", websiteQuery.getDeptState())) {
						salesInfo.setDeptName(websiteQuery.getDeptName());
					} else if (StringUtils.equals("1", websiteQuery.getRetCode())
							&& StringUtils.equals("0", websiteQuery.getDeptState())) {
						error.append("查询网点无效|");
					} else {
						error.append("查询网点返回信息：");
						error.append(websiteQuery.getErrMsg());
						error.append("|");
					}

				}
			} else if (StringUtils.isEmpty(salesInfo.getSalesDeptNo())
					&& !StringUtils.isEmpty(salesInfo.getSalesNo())) {
				salesmanQueryInfo.setSalesNo(salesInfo.getSalesNo());
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				SalesmanQueryReturnBo queryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
				if (null != queryReturnBo) {// 销售员工信息
					if (StringUtils.equals("1", queryReturnBo.getSaleState())) {
						salesInfo.setSalesName(queryReturnBo.getSalesName()); // 销售员姓名(
					} else {
						error.append("查询员工返回信息：");
						error.append(queryReturnBo.getErrMsg());
						error.append("|");
					}
				}

			} else {
				error.append("网点号或销售员工号必须存在一个|");
			}
		} else {
			error.append("销售信息为空");
		}
	}

	// 发起任务流
	private void startProcess(String applNo, String cntrType, String acceptBranchNo,String pclkNo) {
		Map<String, String> mapVar = new HashMap<>();// 选填-放入流程运转过程中必须的业务信息~
		if (StringUtils.equals(cntrType, CNTR_TYPE.GRP_INSUR.getKey())) {
			mapVar.put("NODE_STATUS", "1");
			mapVar.put("ACCEPT_BRANCH_NO", acceptBranchNo);
			mapVar.put("CNTR_TASK_SUB_ITEM", "GRP");
		}
		if (StringUtils.equals(cntrType, CNTR_TYPE.LIST_INSUR.getKey())) {
			mapVar.put("NODE_STATUS", "2");
			mapVar.put("ACCEPT_BRANCH_NO", acceptBranchNo);
			mapVar.put("CNTR_TASK_SUB_ITEM", "LST");
		}
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		
		//读取配置信息
		String enabled = configDao.queryPropertiesConfigure(PROPERTIE_TYPE,pclkNo,null);
		//如果enabled="Y" 则表示禁止核保 否则正常流程
		if(StringUtils.equals(YES_NO_FLAG.YES.getKey(),enabled)){
			taskProcessRequestBO.setProcessDefKey("ECORBP003");
		}else{
			taskProcessRequestBO.setProcessDefKey("ECORBP002");// 必填-流程定义ID
		}
															// 契约团体柜面出单流程ECORBP002
		taskProcessRequestBO.setBusinessKey(applNo);// 必填-业务流水号
		taskProcessRequestBO.setProcessVar(mapVar);
		taskProcessServiceDascClient.startProcess(taskProcessRequestBO);
		logger.info("调用任务平台流程发起接口");
	}

	private String checkCommonAgreement(CommonAgreement commonAgreement, InsurApplRegist insurApplRegist) {

		StringBuilder s = new StringBuilder();
		// 判断共保协议是否生效
		if (!StringUtils.equals(commonAgreement.getAgreementStat(), "K")) {
			s.append("|该共保协议未生效,不能受理|");
		}
		if (insurApplRegist.getAcceptDate().before(commonAgreement.getInForceDate())
				|| insurApplRegist.getAcceptDate().after(commonAgreement.getTermDate())) {
			s.append("|受理日期已超出共保协议的生效范围|");
		}
		// 是否是首席共保判断
		if (commonAgreement.getComCompanies() != null && !commonAgreement.getComCompanies().isEmpty()) {
			int i = 0;
			for (; i < commonAgreement.getComCompanies().size(); i++) {
				ComCompany comCompany = commonAgreement.getComCompanies().get(i);
				if (comCompany == null) {
					continue;
				}
				if (StringUtils.equals(comCompany.getCoinsurType(), "M")
						&& StringUtils.equals(comCompany.getCompanyFlag(), "Y")) {
					break;
				}
			}
			if (i == commonAgreement.getComCompanies().size()) {
				s.append("|所录共保协议本公司不是首席共保|");
			}
		}
		if (StringUtils.isEmpty(insurApplRegist.getPolCode())) {
			return s.toString();
		}
		// 判断所录险种是否在共保协议内
		String polCode = insurApplRegist.getPolCode();
		if (commonAgreement.getPolicies() != null && !commonAgreement.getPolicies().isEmpty()) {
			for (Policy policy : commonAgreement.getPolicies()) {
				if (StringUtils.equals(polCode, policy.getPolCode())) {
					return s.toString();
				}
			}
		}
		s.append("|所投的险种[");
		s.append(polCode);
		s.append("]不在共保协议内|");
		return s.toString();
	}
}
