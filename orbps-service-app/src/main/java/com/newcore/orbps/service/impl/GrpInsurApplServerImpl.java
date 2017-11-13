package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.InsurRuleMangerDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.bo.insurrulemanger.InsurRuleManger;
import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.Conventions;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.business.GrpInsurApplValidator;
import com.newcore.orbps.service.business.InsurApplProcessor;
import com.newcore.orbps.service.business.InsurApplProcessorFactory;
import com.newcore.orbps.service.cmds.api.CreateGrpCstomerAcountService;
import com.newcore.orbps.service.cmds.api.CreatePsnCstomerAcountService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE_01;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PRD_SALES_CHANNEL_01;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 保单基本信息录入实现
 * 
 * @author wangxiao 创建时间：2016年7月20日下午3:05:24
 */
@Service("grpInsurApplServer")
public class GrpInsurApplServerImpl implements InsurApplServer {

	private static Logger logger = LoggerFactory.getLogger(GrpInsurApplServerImpl.class);

	InsurApplProcessorFactory insurApplProcessorFactory = InsurApplProcessorFactory.getInsurApplProcessorFactory();

	private RetInfo retInfo;

	private StringBuilder errMsg;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	InsurRuleMangerDao insurRuleMangerDao;

	@Autowired
	DascInsurParaDao dascInsurParaDao;

	@Autowired
	TaskProcessService taskProcessServiceDascClient;

	@Autowired
	CreateGrpCstomerAcountService restfulCreateGrpCstomerAcountService;

	@Autowired
	CreatePsnCstomerAcountService restfulBatchCreatePsn;

	@Autowired
	ConfigDao configDao;
	
	private static final String PROPERTIE_TYPE= "UDW_OFF" ;  //配置类型
	/**
	 * 保单暂存
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	public RetInfo tempSave(GrpInsurApplPara grpInsurApplPara) {

		retInfo = new RetInfo();
		if (grpInsurApplPara.getGrpInsurAppl() == null
				|| StringUtils.isEmpty(grpInsurApplPara.getGrpInsurAppl().getApplNo()) ||
				grpInsurApplPara.getTraceNode() == null) {
			throw new BusinessException("0004");
		}

		String applNo = grpInsurApplPara.getGrpInsurAppl().getApplNo();

		if (StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkBranchNo())
				|| StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkNo())
				|| StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getProcStat())) {
			throw new BusinessException("0019", applNo);
		}

		Map<String, Object> seleMap = new HashMap<>();
		seleMap.put("applNo", applNo);

		// 获取操作轨迹
		TraceNode traceNode = mongoBaseDao.getPeekTraceNode(applNo);

		if (!StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.RECORD_TEMPORARILY.getKey())
				&& !StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.ACCEPTED.getKey())
				&& !StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.BACK.getKey())) {

			throw new BusinessException("0019", "当前状态不能做暂存。");
		}

		Map<String, Object> findApplMap = new HashMap<>();
		findApplMap.put("billNo", applNo);
		InsurApplRegist insurApplRegist = (InsurApplRegist) mongoBaseDao.findOne(InsurApplRegist.class, findApplMap);
		if (insurApplRegist == null) {
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("0");
			retInfo.setErrMsg("受理信息不存在，请先执行受理录入");
			return retInfo;
		}

		grpInsurApplPara.getGrpInsurAppl().setCntrType(insurApplRegist.getCntrType());
		grpInsurApplPara.getGrpInsurAppl().setSalesDevelopFlag(insurApplRegist.getSalesDevelopFlag());
		grpInsurApplPara.getGrpInsurAppl().setSalesInfoList(insurApplRegist.getSalesInfos());

		// 如果当前状态是暂存，则需要删除暂存记录
		if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.RECORD_TEMPORARILY.getKey())) {
			mongoBaseDao.remove(GrpInsurAppl.class, seleMap);
		}

		// 插入操作轨迹
		grpInsurApplPara.getTraceNode().setProcDate(new Date());
		mongoBaseDao.updateOperTrace(applNo, grpInsurApplPara.getTraceNode());

		// 保存基本信息
		mongoBaseDao.insert(grpInsurApplPara.getGrpInsurAppl());

		retInfo.setApplNo(applNo);
		retInfo.setRetCode("1");

		return retInfo;
	}

	/**
	 * 保单复核
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	@AsynCall
	public RetInfo infoCheck(GrpInsurApplPara grpInsurApplPara) {

		retInfo = new RetInfo();
		errMsg = new StringBuilder();
		Map<String, String> mapVar = new HashMap<>();
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();

		if (grpInsurApplPara.getGrpInsurAppl() == null
				|| StringUtils.isEmpty(grpInsurApplPara.getGrpInsurAppl().getApplNo())) {
			throw new BusinessException("0004");
		}

		GrpInsurAppl grpInsurAppl = grpInsurApplPara.getGrpInsurAppl();

		if (StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkBranchNo())
				|| StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkNo())) {
			throw new BusinessException("0019", grpInsurAppl.getApplNo());
		}

		// 获取操作轨迹
		Map<String, Object> operTraceMap = new HashMap<>();
		operTraceMap.put("applNo", grpInsurAppl.getApplNo());

		// 获取操作轨迹
		TraceNode historyTraceNode = mongoBaseDao.getPeekTraceNode(grpInsurAppl.getApplNo());
		if (null == historyTraceNode) {
			throw new BusinessException("0017", grpInsurAppl.getApplNo() + "操作轨迹不存在。");
		}

		//如果提交成功，则无需再提交
		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())){
			
			if ( StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_SUC.getKey())
					|| StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.LIST_IPSN_CHECK_FAL.getKey())
					||StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_SUC.getKey())
					||StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())) {

				throw new BusinessException("0017", grpInsurAppl.getApplNo() + "已提交成功，不能重复提交。");
			}
		}else{
			if(StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_CHECK_SUC.getKey())
					|| StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.LIST_INFO_CHECK_FAL.getKey())
					||StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_SUC.getKey())
					||StringUtils.equals(historyTraceNode.getProcStat(), NEW_APPL_STATE.GRP_IPSN_CHECK_FAL.getKey())){
				
				throw new BusinessException("0017", grpInsurAppl.getApplNo() + "已提交成功，不能重复提交。");
			}
			
		}
		
		Map<String, Object> findApplMap = new HashMap<>();
		findApplMap.put("billNo", grpInsurAppl.getApplNo());
		InsurApplRegist insurApplRegist = (InsurApplRegist) mongoBaseDao.findOne(InsurApplRegist.class, findApplMap);
		if (insurApplRegist == null) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("0");
			retInfo.setErrMsg("受理信息不存在，请先执行受理录入");
			return retInfo;
		}

		grpInsurAppl.setSalesDevelopFlag(insurApplRegist.getSalesDevelopFlag());
		grpInsurAppl.setSalesInfoList(insurApplRegist.getSalesInfos());
		
		grpInsurApplPara.getTraceNode().setProcDate(new Date());

		taskProcessRequestBO.setProcessDefKey("ECORBP002");// 必填-流程定义ID
		taskProcessRequestBO.setTaskId(grpInsurApplPara.getTaskId());
		taskProcessRequestBO.setBranchNo(grpInsurApplPara.getTraceNode().getPclkBranchNo());
		taskProcessRequestBO.setClerkNo(grpInsurApplPara.getTraceNode().getPclkNo());
		taskProcessRequestBO.setClerkName(grpInsurApplPara.getTraceNode().getPclkName());
		taskProcessRequestBO.setBusinessKey(grpInsurAppl.getApplNo());
		
		 //复核是否通过判断
		if ((StringUtils.equals(grpInsurApplPara.getTraceNode().getProcStat(),
				NEW_APPL_STATE.GRP_INFO_CHECK_FAL.getKey())
				|| StringUtils.equals(grpInsurApplPara.getTraceNode().getProcStat(),
						NEW_APPL_STATE.LIST_INFO_CHECK_FAL.getKey()))) {
			mapVar.put("IS_INFO_CHECK_SUC", "0");		
			

			taskProcessRequestBO.setProcessVar(mapVar);
			taskProcessServiceDascClient.completeTask(taskProcessRequestBO);
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("1");

			// 保存轨迹
//			grpInsurApplPara.getTraceNode().setProcStat(NEW_APPL_STATE.GRP_INFO_CHECK_FAL.getKey());
			mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), grpInsurApplPara.getTraceNode());

			return retInfo;
		} else {
			mapVar.put("IS_INFO_CHECK_SUC", "1");
		}

		// 已保存的团体出单基本信息取得
		GrpInsurAppl grpInsurApplDb = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, operTraceMap);
		if (null == grpInsurApplDb) {
			throw new BusinessException("0004");
		}
		// 避免复核内容丢失
		grpInsurAppl.setCntrType(grpInsurApplDb.getCntrType());
		grpInsurAppl.setAccessChannel(grpInsurApplDb.getAccessChannel());
		grpInsurAppl.setAccessSource(grpInsurApplDb.getAccessSource());
		grpInsurAppl.setAgreementNo(grpInsurApplDb.getAgreementNo());
		grpInsurAppl.setApproNo(grpInsurApplDb.getApproNo());
		
		// 如果是团销系统柜面出单，保存不存在子险种的险种的标准保费，防止丢失
		getPolicyStdPremium(grpInsurApplDb, grpInsurAppl);
	

		// 保单基本信息校验
		BindException errors = new BindException(grpInsurAppl, "grpInsurAppl");
		GrpInsurApplValidator grpValidator = new GrpInsurApplValidator();
		grpValidator.validate(grpInsurAppl, errors);

		if (errors.hasErrors()) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("0");

			for (FieldError error : (List<FieldError>) errors.getFieldErrors()) {
				errMsg.append(error.getField());
				errMsg.append(", ");
				errMsg.append(error.getCode());
				errMsg.append(" | ");
			}
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}

		PsnListHolderInfo psnListInfo = grpInsurAppl.getPsnListHolderInfo();
		PsnListHolderInfo psnListInfoDb = grpInsurApplDb.getPsnListHolderInfo();
		GrpHolderInfo grpInfo = grpInsurAppl.getGrpHolderInfo();
		GrpHolderInfo grpInfoDb = grpInsurApplDb.getGrpHolderInfo();

		// 清单汇交，汇交人为个人
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {

			if (StringUtils.equals(grpInsurAppl.getSgType(), LIST_TYPE.PSN_SG.getKey())) {
				// 判断个人五要素是否变化
				if (StringUtils.equals(psnListInfo.getSgName(), psnListInfoDb.getSgName())
						&& StringUtils.equals(psnListInfo.getSgIdType(), psnListInfoDb.getSgIdType())
						&& StringUtils.equals(psnListInfo.getSgIdNo(), psnListInfoDb.getSgIdNo())
						&& StringUtils.equals(psnListInfo.getSgSex(), psnListInfoDb.getSgSex()) && StringUtils.equals(
								psnListInfo.getSgBirthDate().toString(), psnListInfoDb.getSgBirthDate().toString())) {

					// 五要素没有变化，无需开户
					psnListInfo.setSgCustNo(psnListInfoDb.getSgCustNo());
					psnListInfo.setSgPartyId(psnListInfoDb.getSgPartyId());
					grpInsurAppl.setPsnListHolderInfo(psnListInfo);

				} else {

					PsnListHolderInfo psnListHolderInfo = getPsnCustNo(grpInsurAppl);
					grpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);
				}

			} else {
				// 判断团体三要素是否变化
				if (StringUtils.equals(grpInfo.getGrpName(), grpInfoDb.getGrpName())
						&& StringUtils.equals(grpInfo.getGrpIdType(), grpInfoDb.getGrpIdType())
						&& StringUtils.equals(grpInfo.getGrpIdNo(), grpInfoDb.getGrpIdNo())) {
					grpInfo.setGrpCustNo(grpInfoDb.getGrpCustNo());
					grpInfo.setPartyId(grpInfoDb.getPartyId());
					grpInsurAppl.setGrpHolderInfo(grpInfo);
				} else {

					GrpHolderInfo grpHolderInfo = getGrpCustNo(grpInsurAppl, grpInsurAppl.getGrpHolderInfo());
					grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
				}

			}
		}

		// 团体客户开户
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {

			// 判断团体三要素是否变化
			if (StringUtils.equals(grpInfo.getGrpName(), grpInfoDb.getGrpName())
					&& StringUtils.equals(grpInfo.getGrpIdType(), grpInfoDb.getGrpIdType())
					&& StringUtils.equals(grpInfo.getGrpIdNo(), grpInfoDb.getGrpIdNo())) {
				grpInfo.setGrpCustNo(grpInfoDb.getGrpCustNo());
				grpInfo.setPartyId(grpInfoDb.getPartyId());
				grpInsurAppl.setGrpHolderInfo(grpInfo);
			} else {

				GrpHolderInfo grpHolderInfo = getGrpCustNo(grpInsurAppl, grpInsurAppl.getGrpHolderInfo());
				grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
			}
		}

		// 保存轨迹
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
			grpInsurApplPara.getTraceNode().setProcStat(NEW_APPL_STATE.GRP_INFO_CHECK_SUC.getKey());
		}		
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			grpInsurApplPara.getTraceNode().setProcStat(NEW_APPL_STATE.LIST_INFO_CHECK_SUC.getKey());			
		}
		mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), grpInsurApplPara.getTraceNode());
		
		retInfo.setApplNo(grpInsurAppl.getApplNo());
		retInfo.setRetCode("1");
		
		//如果有清单，则不走任务流，流程结束
		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())){
			// 更新保单基本信息
			mongoBaseDao.remove(GrpInsurAppl.class, operTraceMap);
			mongoBaseDao.insert(grpInsurAppl);
			return retInfo;
		}
		
		// 是否有清单判断
		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			mapVar.put("IS_INSURED_LIST", "1");
		} else {
			mapVar.put("IS_INSURED_LIST", "0");
		}

		// 判断人工审批
		// 省级机构号
		String proBranchNo = "";
		// 销售渠道
		String salesChannel = "";
		// 规则类型
		String ruleType = "1";//暂时同一为1
		// 契约形式
		String cntrForm = grpInsurAppl.getCntrType();	
		proBranchNo = grpInsurAppl.getProvBranchNo();
		for (SalesInfo salesInfo : grpInsurAppl.getSalesInfoList()) {
			// 取主销售员销售机构和销售渠道
			if (StringUtils.equals(salesInfo.getDevelMainFlag(), "1")) {
				salesChannel = salesInfo.getSalesChannel();
			}
		}
		// 如果没主销售员标记取第一个销售员的销售机构和销售渠道
		if (StringUtils.isEmpty(salesChannel)) {
			salesChannel = grpInsurAppl.getSalesInfoList().get(0).getSalesChannel();
		}
		// 通过管理机构号，销售渠道，销售机构，契约形式查询审批方案
		//flag 为false 不进人工审批，true进人工审批
		boolean flag = false;
		InsurRuleManger insurRuleManger = insurRuleMangerDao.select(proBranchNo, salesChannel,ruleType,cntrForm);	
		if(null == insurRuleManger){
			insurRuleManger = insurRuleMangerDao.select(proBranchNo,PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,cntrForm);					
		}
		if(null == insurRuleManger){
			insurRuleManger = insurRuleMangerDao.select(proBranchNo,PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,CNTR_TYPE_01.ALL.getKey());					
		}
		if(null == insurRuleManger){
			insurRuleManger = insurRuleMangerDao.select(proBranchNo,salesChannel,ruleType,CNTR_TYPE_01.ALL.getKey());					
		}
		if(null == insurRuleManger){
			flag = false;
		}else {
			//人工审批规则判断 兼容多多个审批规则
			List<InsurRuleManger> insurRuleMangers = new ArrayList<>();
			insurRuleMangers.add(insurRuleManger);
			flag = grpInsurAppl.isManApprove(insurRuleMangers);		
		}
		if (flag) {
			mapVar.put("IS_MAN_APPROVE", "1");// 人工审批
			Map<String, Object> map = new HashMap<>();
			map.put("applNo", grpInsurAppl.getApplNo());
			Update update = new Update();
			update.addToSet("conventions.inputConv", grpInsurAppl.getConventions().getInputConv());
			mongoBaseDao.update(GrpInsurAppl.class, map, update);
		} else {
			mapVar.put("IS_MAN_APPROVE", "0");//
		}
		Map<String, Object> applNoMap = new HashMap<String, Object>();
		applNoMap.put("applNo",grpInsurAppl.getApplNo());
		applNoMap.put("operTraceDeque.procStat", NEW_APPL_STATE.ACCEPTED.getKey());
		InsurApplOperTrace applOperTrace=(InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, applNoMap);
		 String procSwitch = configDao.queryPropertiesConfigure(PROPERTIE_TYPE,applOperTrace.getOperTraceDeque().getLast().getPclkNo(),null);
		//判断是否要越过核保 如果等于Y 则越过核保并添加核保结论
         if (StringUtils.equals(procSwitch, YES_NO_FLAG.YES.getKey())){
        	 //判断是否有清单 如果无清单并且非人工审批 增加核保结论 并增加相应轨迹
        	 if(StringUtils.equals("0",mapVar.get("IS_INSURED_LIST")) && !flag){
                 logger.info("applNo={}, 核保开关关闭，准备写核保结论……",grpInsurAppl.getApplNo() );
        		 ApplUdwResult applUdwResult = new ApplUdwResult();
                 applUdwResult.setBusinessKey(grpInsurAppl.getApplNo());
                 applUdwResult.setUdwResult("0");
                 Map<String, Object> businessMap = new HashMap<>();
                 businessMap.put("businessKey", grpInsurAppl.getApplNo());
                 List<UdwPolResult> udwPolResults = new ArrayList<>();
                 for (Policy policy : grpInsurAppl.getApplState().getPolicyList()){
                     UdwPolResult udwPolResult = new UdwPolResult();
                     udwPolResult.setPolCode(policy.getPolCode());
                     udwPolResult.setUdwResult("0");
                     udwPolResult.setAddFeeAmnt(0D);
                     udwPolResult.setAddFeeYear(0);
                     udwPolResults.add(udwPolResult);
                 }
                 applUdwResult.setUdwPolResults(udwPolResults);
                 mongoBaseDao.remove(ApplUdwResult.class, businessMap);
                 mongoBaseDao.insert(applUdwResult);
                  //插入核保轨迹
                 TraceNode udwTraceNode = new TraceNode();
                 udwTraceNode.setProcDate(new Date());     
                 udwTraceNode.setProcStat(NEW_APPL_STATE.UNDERWRITING.getKey());
                 mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), udwTraceNode);
        	 }
         }
             
		// 更新保单基本信息
		mongoBaseDao.remove(GrpInsurAppl.class, operTraceMap);
		mongoBaseDao.insert(grpInsurAppl);

		taskProcessRequestBO.setProcessVar(mapVar);
		taskProcessServiceDascClient.completeTask(taskProcessRequestBO);
		retInfo.setApplNo(grpInsurAppl.getApplNo());
		retInfo.setRetCode("1");

		return retInfo;
	}

	/**
	 * 保单录入
	 * 
	 * @param GrpInsurApplPara
	 * @return
	 */
	@Override
	@AsynCall
	public RetInfo save(GrpInsurApplPara grpInsurApplPara) {
		retInfo = new RetInfo();
		errMsg = new StringBuilder();
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		Map<String, String> mapVar = new HashMap<>();// 选填-放入流程运转过程中必须的业务信息~

		if (grpInsurApplPara.getGrpInsurAppl() == null || grpInsurApplPara.getTaskId() == null
				|| grpInsurApplPara.getTraceNode() == null) {
			throw new BusinessException("0004");
		}
		GrpInsurAppl grpInsurAppl = grpInsurApplPara.getGrpInsurAppl();
		if (StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkBranchNo())
				|| StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkNo())) {
			throw new BusinessException("0019", grpInsurAppl.getApplNo());
		}
		// 获取操作轨迹
		Map<String, Object> operTraceMap = new HashMap<>();
		operTraceMap.put("applNo", grpInsurAppl.getApplNo());
		TraceNode traceNode = mongoBaseDao.getPeekTraceNode(grpInsurAppl.getApplNo());
		if (null == traceNode) {
			throw new BusinessException("0017", grpInsurAppl.getApplNo() + "操作轨迹不存在。");
		}
		//回退状态，修改有清单改为无清单需删除被保险人
		if(StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.BACK.getKey())){
			if (!StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
				//清除被保险人
				mongoBaseDao.remove(GrpInsured.class, operTraceMap);
			}			
		}

		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {			
			if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_IMPORT.getKey()) 
					|| StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_IMPORT.getKey())) {
				
				throw new BusinessException("0017", grpInsurAppl.getApplNo() + "已全部录入完成，不能重复提交");
			}
			mapVar.put("IS_INSURED_LIST","1");
		}else{
			
			if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.GRP_ENTRY.getKey())
					|| StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.LIST_ENTRY.getKey())) {
				throw new BusinessException("0017", grpInsurAppl.getApplNo() + "清单类型的修改，不能重复提交，请回退到录入状态!");
			}
			mapVar.put("IS_INSURED_LIST","0");
		}

		Map<String, Object> findApplMap = new HashMap<>();
		findApplMap.put("billNo", grpInsurAppl.getApplNo());
		InsurApplRegist insurApplRegist = (InsurApplRegist) mongoBaseDao.findOne(InsurApplRegist.class, findApplMap);
		if (insurApplRegist == null) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("0");
			retInfo.setErrMsg("受理信息不存在，请先执行受理录入");
			return retInfo;
		}

		grpInsurAppl.setCntrType(insurApplRegist.getCntrType());
		grpInsurAppl.setSalesDevelopFlag(insurApplRegist.getSalesDevelopFlag());
		grpInsurAppl.setSalesInfoList(insurApplRegist.getSalesInfos());
		grpInsurAppl.setAgreementNo(insurApplRegist.getAgreementNo());
		grpInsurAppl.setApproNo(insurApplRegist.getApproNo());
		
		// 如果存在保单基本信息，取出基本信息中接入来源、接入渠道内容，防止丢失
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", grpInsurAppl.getApplNo());
		GrpInsurAppl grpInsurApplDB = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if(null!=grpInsurApplDB){
			grpInsurAppl.setAccessChannel(grpInsurApplDB.getAccessChannel());
			grpInsurAppl.setAccessSource(grpInsurApplDB.getAccessSource());
		}
		// 如果是团销系统柜面出单，保存不存在子险种的险种的标准保费，防止丢失
		getPolicyStdPremium(grpInsurApplDB, grpInsurAppl);

		// 保单基本信息校验
		BindException errors = new BindException(grpInsurAppl, "grpInsurAppl");
		GrpInsurApplValidator grpValidator = new GrpInsurApplValidator();
		grpValidator.validate(grpInsurAppl, errors);

		if (errors.hasErrors()) {
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setRetCode("0");

			for (FieldError error : (List<FieldError>) errors.getFieldErrors()) {
				errMsg.append(error.getField());
				errMsg.append(", ");
				errMsg.append(error.getCode());
				errMsg.append(" | ");
			}
			retInfo.setErrMsg(errMsg.toString());
			return retInfo;
		}

		// 清单汇交，汇交人为个人
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {

			if (StringUtils.equals(grpInsurAppl.getSgType(), LIST_TYPE.PSN_SG.getKey())) {

				PsnListHolderInfo psnListHolderInfo = getPsnCustNo(grpInsurAppl);
				grpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);

			} else {

				GrpHolderInfo grpHolderInfo = getGrpCustNo(grpInsurAppl, grpInsurAppl.getGrpHolderInfo());
				grpInsurAppl.setGrpHolderInfo(grpHolderInfo);

			}
		}

		// 团体客户开户
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
			GrpHolderInfo grpHolderInfo = getGrpCustNo(grpInsurAppl, grpInsurAppl.getGrpHolderInfo());
			grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
		}
		
		// 更新保单基本信息
		mongoBaseDao.remove(GrpInsurAppl.class, operTraceMap);
		mongoBaseDao.insert(grpInsurAppl);
		retInfo.setRetCode("1");
		// 插入操作轨迹
		grpInsurApplPara.getTraceNode().setProcDate(new Date());
		mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), grpInsurApplPara.getTraceNode());
		//如果普通清单，则流程结束
		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			return retInfo;
		}
		
		taskProcessRequestBO.setTaskId(grpInsurApplPara.getTaskId());// 业务服务参数中获取的任务ID
		taskProcessRequestBO.setBranchNo(grpInsurApplPara.getTraceNode().getPclkBranchNo());
		taskProcessRequestBO.setClerkNo(grpInsurApplPara.getTraceNode().getPclkNo());
		taskProcessRequestBO.setClerkName(grpInsurApplPara.getTraceNode().getPclkName());
		taskProcessRequestBO.setBusinessKey(grpInsurAppl.getApplNo());
		taskProcessRequestBO.setProcessVar(mapVar);
		taskProcessServiceDascClient.completeTask(taskProcessRequestBO);// 进行任务完成操作		

		return retInfo;
	}

	/**
	 * 保单录入
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	@AsynCall
	public RetInfo addSubmit(GrpInsurAppl grpInsurAppl) {
		retInfo = new RetInfo();
		if (grpInsurAppl == null) {
			retInfo.setRetCode("0");
			retInfo.setErrMsg("保单基本信息为空");
			return retInfo;
		}
		//获取系统来源，获取流程ID  AccessSource = GCSS 流程1 AccessSource = GCSS-PRE 流程2
		String ecorbp = "ECORBP001";
		if(!StringUtils.equals(grpInsurAppl.getAccessSource(),"GCSS")){
			ecorbp = "ECORBP002";
		}
		InsurApplProcessor insurApplProcessor = getInsurApplProcessor(grpInsurAppl);
		if (insurApplProcessor != null) {
			retInfo = insurApplProcessor.addInsurAppl(grpInsurAppl, mongoBaseDao);
		}
		if(StringUtils.equals("0", retInfo.getRetCode())){
			return retInfo;
		}
		// 销售渠道
		String salesChannel = "";
		// 销售机构
		String salesBranchNo = "";
		// 规则类型
		String ruleType = "1";//暂时同一为1
		// 契约形式
		String cntrForm = grpInsurAppl.getCntrType();		
		for (SalesInfo salesInfo : grpInsurAppl.getSalesInfoList()) {
			// 取主销售员销售机构和销售渠道
			if (StringUtils.equals(salesInfo.getDevelMainFlag(), "1")) {
				salesChannel = salesInfo.getSalesChannel();
				salesBranchNo = salesInfo.getSalesBranchNo();
			}
		}
		// 如果没主销售员标记取第一个销售员的销售机构和销售渠道
		if (StringUtils.isEmpty(salesChannel)) {
			salesChannel = grpInsurAppl.getSalesInfoList().get(0).getSalesChannel();
			salesBranchNo = grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo();
		}
		// 添加操作轨迹
		TraceNode traceNode = new TraceNode();
		traceNode.setProcDate(new Date());
		traceNode.setProcStat(NEW_APPL_STATE.ACCEPTED.getKey());
		traceNode.setPclkBranchNo(salesBranchNo);
		mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), traceNode);
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			traceNode.setProcStat(NEW_APPL_STATE.LIST_ENTRY.getKey());
		} else {
			traceNode.setProcStat(NEW_APPL_STATE.GRP_ENTRY.getKey());
		}
		mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), traceNode);
		// 流程ECORBP001无清单发起任务流程
		if (StringUtils.equals(ecorbp, "ECORBP001") && StringUtils.equals(retInfo.getRetCode(), "1")){
			if(!StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())){
				Map<String, String> mapVar = new HashMap<>();// 选填-放入流程运转过程中必须的业务信息~
				mapVar.put("IS_INSURED_LIST","0");
				// 通过管理机构号，销售渠道，销售机构，契约形式查询审批方案
				//flag 为false 不进人工审批，true进人工审批
				boolean flag = false;
				InsurRuleManger insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),salesChannel,ruleType,cntrForm);					
				if(null == insurRuleManger){
					insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,cntrForm);		
				}
				if(null == insurRuleManger){
					insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,CNTR_TYPE_01.ALL.getKey());					
				}
				if(null == insurRuleManger){
					insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),salesChannel,ruleType,CNTR_TYPE_01.ALL.getKey());					
				}
				if(null == insurRuleManger){
					flag = false;
				}else {
					//人工审批规则判断 兼容多多个审批规则
					List<InsurRuleManger> insurRuleMangers = new ArrayList<>();
					insurRuleMangers.add(insurRuleManger);
					flag = grpInsurAppl.isManApprove(insurRuleMangers);		
				}
				if (flag) {
					if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
						mapVar.put("IS_MAN_APPROVE", "1");
                        mapVar.put("CNTR_TASK_SUB_ITEM", "GRP");
					} else {
						mapVar.put("IS_MAN_APPROVE", "2");// 人工审批
                        mapVar.put("CNTR_TASK_SUB_ITEM", "LST");
					}
					Map<String, Object> map = new HashMap<>();
					map.put("applNo", grpInsurAppl.getApplNo());
					Update update = new Update();
					update.addToSet("conventions.inputConv", grpInsurAppl.getConventions().getInputConv());
					mongoBaseDao.update(GrpInsurAppl.class, map, update);
				} else {
					mapVar.put("IS_MAN_APPROVE", "0");// 自动审批
				}
                mapVar.put("ACCEPT_BRANCH_NO", salesBranchNo); //受理机构号存销售机构号
				DascInsurParaBo dascInsurParaBo = new DascInsurParaBo();
				dascInsurParaBo.setApplNo(grpInsurAppl.getApplNo());
				dascInsurParaBo.setCntrType(grpInsurAppl.getCntrType());
				dascInsurParaBo.setListFilePath("");
				DascInsurParaBo reDascInsurParaBo = dascInsurParaDao.add(dascInsurParaBo);
				TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
				taskProcessRequestBO.setProcessDefKey("ECORBP001");// 必填-流程定义ID
				taskProcessRequestBO.setBusinessKey(reDascInsurParaBo.getBusinessKey());// 必填-业务流水号
				taskProcessRequestBO.setProcessVar(mapVar);
				taskProcessRequestBO.setBranchNo(salesBranchNo);
				taskProcessServiceDascClient.startProcess(taskProcessRequestBO);
			}
		}
		//流程ECORBP002发起
		if(StringUtils.equals(ecorbp, "ECORBP002") && StringUtils.equals(retInfo.getRetCode(), "1")){
			startProcess(grpInsurAppl.getApplNo(), grpInsurAppl.getCntrType(), salesBranchNo);
		}
		return retInfo;
	}

	/**
	 * 保单修正
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	public RetInfo modifySubmit(GrpInsurAppl grpInsurAppl) {
		InsurApplProcessor insurApplProcessor = getInsurApplProcessor(grpInsurAppl);
		if (insurApplProcessor != null) {
			retInfo = insurApplProcessor.modifyInsurAppl(grpInsurAppl, mongoBaseDao);
		}
		return retInfo;
	}

	/**
	 * 
	 * 根据流水号查询
	 * 
	 * @param applNo
	 * @return
	 */
	@Override
	public ApplState searchApplState(String applNo) {
		// 入参非空判定
		if (StringUtils.isEmpty(applNo)) {
			throw new BusinessException("0001");
		}

		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo);
		// 获取团单基本信息
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
		if (null == grpInsurAppl) {
			throw new BusinessException("0018", "团单基本信息");
		}

		return grpInsurAppl.getApplState();
	}

	/**
	 * 根据流水号查询
	 * 
	 * @param InsurApplString
	 * @return
	 */
	@Override
	public GrpInsurApplBo searchInsurApplByBusinessKey(String applNo) {
		GrpInsurApplBo grpInsurApplVo = new GrpInsurApplBo();
		GrpInsurAppl grpInsurAppl = new GrpInsurAppl();

		// 查询受理
		Map<String, Object> queryRegistMap = new HashMap<>();
		queryRegistMap.put("billNo", applNo);
		Object object1 = mongoBaseDao.findOne(InsurApplRegist.class, queryRegistMap);
		if (object1 == null) {
			grpInsurApplVo.setErrCode("0");
			grpInsurApplVo.setErrMsg("受理信息不存在");
			grpInsurApplVo.setApplNo(applNo);
			return grpInsurApplVo;
		}
		InsurApplRegist insurApplRegist = (InsurApplRegist) object1;

		// 获取操作轨迹
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo);
		TraceNode traceNode = mongoBaseDao.getPeekTraceNode(applNo);
		if (traceNode == null) {
			grpInsurApplVo.setErrCode("0");
			grpInsurApplVo.setErrMsg("操作轨迹不存在");
			grpInsurApplVo.setApplNo(applNo);
			return grpInsurApplVo;
		}

		if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.ACCEPTED.getKey())) {
			grpInsurApplVo.setApplNo(applNo);
			grpInsurAppl.setApplNo(applNo);
			grpInsurAppl.setCntrType(insurApplRegist.getCntrType()); // 保单类型
			grpInsurAppl.setSalesDevelopFlag(insurApplRegist.getSalesDevelopFlag());// 共同展业标记
			grpInsurAppl.setSalesInfoList(insurApplRegist.getSalesInfos()); // 销售员信息
			grpInsurAppl.setApproNo(insurApplRegist.getApproNo()); // 方案审批号
			//增加保单基本信息首次录入时受理信息中投保人信息
			PsnListHolderInfo psnListHolderInfo = new PsnListHolderInfo();
			psnListHolderInfo.setSgName(insurApplRegist.getHldrName());
			GrpHolderInfo grpHolderInfo = new GrpHolderInfo();
			grpHolderInfo.setGrpName(insurApplRegist.getHldrName());
			grpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);
			grpInsurAppl.setGrpHolderInfo(grpHolderInfo);

			ApplState applState = new ApplState();
			applState.setIpsnNum(insurApplRegist.getIpsnNum()); // 投保人数
			applState.setSumPremium(insurApplRegist.getSumPremium()); // 保费合计
			// 判断是否共保
			if (StringUtils.equals(insurApplRegist.getIsCommonAgreement(), YES_NO_FLAG.YES.getKey())) {
				Map<String, Object> commAgreeMap = new HashMap<>();
				commAgreeMap.put("agreementNo", insurApplRegist.getAgreementNo());
				CommonAgreement commonAgreement = (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class,
						commAgreeMap);
				if (null == commonAgreement) {
					throw new BusinessException("0018", "共保协议");
				}
				Conventions conventions = new Conventions();
				conventions.setPolConv(commonAgreement.getConvention());
				grpInsurAppl.setConventions(conventions);
				grpInsurAppl.setAgreementNo(insurApplRegist.getAgreementNo());
				applState.setPolicyList(commonAgreement.getPolicies());

			} else {
				Policy policy = new Policy();
				policy.setPolCode(insurApplRegist.getPolCode());
				policy.setPolNameChn(insurApplRegist.getPolNameChn());

				List<Policy> policies = new ArrayList<>();
				policies.add(policy);
				applState.setPolicyList(policies);
			}
			grpInsurAppl.setApplState(applState);
			grpInsurApplVo.setErrCode("1");
			grpInsurApplVo.setGrpInsurAppl(grpInsurAppl);

			return grpInsurApplVo;
		}

		// 获取团单基本信息
		Object object3 = mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
		if (null == object3) {
			grpInsurApplVo.setErrCode("0");
			grpInsurApplVo.setErrMsg("团单基本信息不存在");
			grpInsurApplVo.setApplNo(applNo);
			return grpInsurApplVo;
		}
		grpInsurAppl = (GrpInsurAppl) object3;

		grpInsurApplVo.setGrpInsurAppl(grpInsurAppl);
		grpInsurApplVo.setErrCode("1");
		grpInsurApplVo.setApplNo(applNo);
		return grpInsurApplVo;
	}

	/**
	 * 获取数据处理器
	 * 
	 * @param InsurApplString
	 * @return
	 */
	public InsurApplProcessor getInsurApplProcessor(GrpInsurAppl grpInsurAppl) {
		String cntrForm = grpInsurAppl.getCntrType();
		InsurApplProcessor insurApplProcessor = null;
		if ("G".equals(cntrForm) || "L".equals(cntrForm)) {
			insurApplProcessor = insurApplProcessorFactory.createProcessor(cntrForm);
		} else {
			retInfo.setRetCode("0");
			retInfo.setErrMsg("契约形式为空");
		}
		return insurApplProcessor;
	}

	/**
	 * @return the retInfo
	 */
	public RetInfo getRetInfo() {
		return retInfo;
	}

	/**
	 * @param retInfo
	 *            the retInfo to set
	 */
	public void setRetInfo(RetInfo retInfo) {
		this.retInfo = retInfo;
	}

	// 个人汇交人开户
	private PsnListHolderInfo getPsnCustNo(GrpInsurAppl grpInsurAppl) {
		Map<String, Object> json = new HashMap<>();
		String provBranchNo = grpInsurAppl.getProvBranchNo();
		PsnListHolderInfo psnListHolderInfo = grpInsurAppl.getPsnListHolderInfo();
		json.put("PROV_BRANCH_NO", provBranchNo == null ? "120000" : provBranchNo);
		json.put("SRC_SYS", "ORBPS");// 系统来源
		String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
		json.put("CUST_OAC_BRANCH_NO", mgrBranchNo == null ? "120000" : mgrBranchNo);// 管理机构
		json.put("APPL_NO", grpInsurAppl.getApplNo());
		json.put("ROLE", "1");
		json.put("IPSN_NO", "");
		json.put("BNFRLEVEL", "");
		json.put("NAME", psnListHolderInfo.getSgName());
		json.put("ID_TYPE", psnListHolderInfo.getSgIdType());
		json.put("ID_NO", psnListHolderInfo.getSgIdNo());
		json.put("SEX", psnListHolderInfo.getSgSex());
		json.put("BIRTH_DATE", DateFormatUtils.format(psnListHolderInfo.getSgBirthDate(), "yyyy-MM-dd"));
		logger.info("个人汇交人开户发送报文：" + JSON.toJSONString(json));
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");
		headerInfo.getRouteInfo().setBranchNo(mgrBranchNo == null ? "120000" : mgrBranchNo);
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		List<Map<String, Object>> list = new ArrayList<>();
		list.add(json);
		String retjson = restfulBatchCreatePsn.createPsnCstomerAcount(list);
		if (StringUtils.isEmpty(retjson)) {
			throw new BusinessException("0006");
		}
		logger.info("个人汇交人开户返回报文：" + retjson);
		JSONObject jsonObject = JSON.parseArray(retjson).getJSONObject(0);
		String custNo = jsonObject.getString("CUST_NO");
		String partyId = jsonObject.getString("PARTY_ID");
		if(StringUtils.isEmpty(custNo)||StringUtils.isEmpty(partyId)){
			String [] arr = {"开户返回客户号"};
			throw new BusinessException("0018",arr);
		}
		psnListHolderInfo.setSgCustNo(custNo);
		psnListHolderInfo.setSgPartyId(partyId);
		return psnListHolderInfo;
	}

	// 法人开户
	private GrpHolderInfo getGrpCustNo(GrpInsurAppl grpInsurAppl, GrpHolderInfo grpHolderInfo) {
		Map<String, Object> json = new HashMap<>();
		String provBranchNo = grpInsurAppl.getProvBranchNo();
		json.put("CUST_NO", "");
		json.put("PROV_BRANCH_NO", provBranchNo == null ? "120000" : provBranchNo);
		json.put("SRC_SYS", "ORBPS");// 系统来源
		String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
		json.put("CUST_OAC_BRANCH_NO", mgrBranchNo == null ? "120000" : mgrBranchNo);// 管理机构
		json.put("NAME", grpHolderInfo.getGrpName());
		json.put("OLD_NAME", grpHolderInfo.getFormerGrpName() == null ? "" : grpHolderInfo.getFormerGrpName());
		json.put("ID_TYPE", grpHolderInfo.getGrpIdType());
		json.put("ID_NO", grpHolderInfo.getGrpIdNo());
		json.put("LEGAL_CODE", grpHolderInfo.getLegalCode() == null ? "" : grpHolderInfo.getLegalCode());
		json.put("NATURE_CODE", grpHolderInfo.getNatureCode() == null ? "" : grpHolderInfo.getNatureCode());
		json.put("OCC_CLASS_CODE", grpHolderInfo.getOccClassCode());
		json.put("CORP_REP", grpHolderInfo.getCorpRep() == null ? "" : grpHolderInfo.getCorpRep());
		json.put("CONTACT_PSN", grpHolderInfo.getContactName() == null ? "" : grpHolderInfo.getContactName());
		json.put("CONTACT_PSN_SEX", "");
		if (null == grpHolderInfo.getNumOfEnterprise()) {
			json.put("NUM_OF_EMP", "0");
		} else {
			json.put("NUM_OF_EMP", grpHolderInfo.getNumOfEnterprise().toString());
		}
		logger.info("法人开户发送报文：" + json);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");
		headerInfo.getRouteInfo().setBranchNo(mgrBranchNo == null ? "120000" : mgrBranchNo);
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String retjson = restfulCreateGrpCstomerAcountService.createGrpCstomerAcount(json);
		if (retjson == null) {
			throw new BusinessException("0006");
		}
		logger.info("法人开户返回报文：" + retjson);
		JSONObject jsonObject = JSON.parseObject(retjson);
		String custNo = jsonObject.getString("CUST_NO");
		String partyId = jsonObject.getString("PARTY_ID");
		if(StringUtils.isEmpty(custNo)||StringUtils.isEmpty(partyId)){
			String [] arr = {"开户返回客户号"};
			throw new BusinessException("0018",arr);
		}
		grpHolderInfo.setGrpCustNo(custNo);
		grpHolderInfo.setPartyId(partyId);
		return grpHolderInfo;
	}

	@Override
	public GrpInsurApplPara searchGrpInsurAppl(Map<String, Object> map) {
		GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
		if (null == map || map.isEmpty()) {
			throw new BusinessException("0018", "入参map");
		}
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if (null == grpInsurAppl) {
			throw new BusinessException("0004");
		}
		TraceNode traceNode = mongoBaseDao.getPeekTraceNode(grpInsurAppl.getApplNo());
		grpInsurApplPara.setGrpInsurAppl(grpInsurAppl);		
		grpInsurApplPara.setTraceNode(traceNode);	
		return grpInsurApplPara;
	}
	//发起任务流
	private void startProcess(String applNo,String cntrType,String acceptBranchNo){
		Map<String,String> mapVar=new HashMap<>();//选填-放入流程运转过程中必须的业务信息~
		if(StringUtils.equals(cntrType,CNTR_TYPE.GRP_INSUR.getKey())){
			mapVar.put("NODE_STATUS","1");
			mapVar.put("ACCEPT_BRANCH_NO",acceptBranchNo);
			mapVar.put("CNTR_TASK_SUB_ITEM","GRP");
		}
		if(StringUtils.equals(cntrType,CNTR_TYPE.LIST_INSUR.getKey())){
			mapVar.put("NODE_STATUS","2");
			mapVar.put("ACCEPT_BRANCH_NO",acceptBranchNo);
			mapVar.put("CNTR_TASK_SUB_ITEM","LST");
		}
		TaskProcessRequestBO taskProcessRequestBO=new TaskProcessRequestBO();
		taskProcessRequestBO.setProcessDefKey("ECORBP002");//必填-流程定义ID  契约团体柜面出单流程ECORBP002
		taskProcessRequestBO.setBusinessKey(applNo);//必填-业务流水号
		taskProcessRequestBO.setProcessVar(mapVar);
		taskProcessServiceDascClient.startProcess(taskProcessRequestBO);
		logger.info("调用任务平台流程发起接口");
	}
	
	//接入来源为GCSS_PRE时，获取数据库中不包含子险种的险种标准保费
	private void getPolicyStdPremium(GrpInsurAppl grpInsurApplDB,GrpInsurAppl grpInsurAppl){
		if(null==grpInsurApplDB)return; 
		if(!StringUtils.equals("GCSS_PRE", grpInsurAppl.getAccessSource()))return;
		if(null==grpInsurApplDB.getApplState())return;
		if(null==grpInsurApplDB.getApplState().getPolicyList())return;
		if(null==grpInsurAppl.getApplState())return;
		if(null==grpInsurAppl.getApplState().getPolicyList())return;
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			if(policy.getSubPolicyList()!=null && !policy.getSubPolicyList().isEmpty()){
				continue;
			}
			for(Policy policyDB:grpInsurApplDB.getApplState().getPolicyList()){
				if(!StringUtils.isEmpty(policy.getPolCode())
					&& null != policyDB.getStdPremium()
					&& StringUtils.equals(policy.getPolCode(), policyDB.getPolCode())){
					policy.setStdPremium(policyDB.getStdPremium());
				}
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		Map<String, String> mapVar = new HashMap<>();
		mapVar.put("IS_INSURED_LIST", "0");
		if (StringUtils.equals("N", LST_PROC_TYPE.ORDINARY_LIST.getKey())) {
			mapVar.put("IS_INSURED_LIST", "1");
		} else {
			mapVar.put("IS_INSURED_LIST", "0");
		}

		boolean flag = false;
		 if(StringUtils.equals("0",mapVar.get("IS_INSURED_LIST")) && !flag){
		System.out.println("dd");
		 }else{
			 System.out.println("dds");
		 }
	}
}
