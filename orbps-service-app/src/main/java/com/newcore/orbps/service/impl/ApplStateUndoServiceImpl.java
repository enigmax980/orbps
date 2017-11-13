package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.QueryAccInfoDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.uwbps.AltBussStatInVO;
import com.newcore.orbps.service.api.ApplStateUndoService;
import com.newcore.orbps.service.api.ProcCorrMioDataService;
import com.newcore.orbps.service.pcms.api.MioFirstPremchkService;
import com.newcore.orbps.service.uwbps.api.UnderWritingService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 要约撤销
 *
 * @author wangxiao 创建时间：2016年9月24日上午10:43:20
 */
@Service("applStateUndoService")
public class ApplStateUndoServiceImpl implements ApplStateUndoService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(InsurApplBackServiceImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	TaskProcessService taskProcessServiceDascClient;

	@Autowired
	MioFirstPremchkService resulMioFirstPremchkServiceClient;

	@Autowired
	UnderWritingService underWritingServiceClient;
	@Autowired
	ProcCorrMioDataService procCorrMioDataService;
	@Autowired
	QueryAccInfoDao queryAccInfoDaoService;
	
	@Autowired
	ConfigDao configDao;
	
	private static final String PROPERTIE_TYPE= "UDW_OFF" ;  //配置类型
	
	@Override
	@AsynCall
	public RetInfo undo(TraceNodePra traceNodePra) {
		
		if (traceNodePra == null || StringUtils.isEmpty(traceNodePra.getApplNo())
				|| traceNodePra.getTraceNode() == null) {
			throw new BusinessException("0018", "入参");
		}
		if (StringUtils.isEmpty(traceNodePra.getTraceNode().getPclkBranchNo())
				|| StringUtils.isEmpty(traceNodePra.getTraceNode().getPclkNo())) {
			throw new BusinessException("0018", "操作员信息为空");
		}
		String applNo = traceNodePra.getApplNo();
		RetInfo retInfo = new RetInfo();
		Map<String, Object> mapVar = new HashMap<>();
		mapVar.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, mapVar);
		if (grpInsurAppl == null) {
			logger.error("保单基本信息不存在: " + applNo);
			String[] arrStr = { applNo, "" };
			throw new BusinessException("0016", arrStr);
		}

		// 查询操作轨迹
		TraceNode traceNodeDb = mongoBaseDao.getPeekTraceNode(applNo);
		if (null == traceNodeDb) {
			throw new BusinessException("0018", "操作轨迹为空");
		}
		if (StringUtils.equals(traceNodeDb.getProcStat(),	NEW_APPL_STATE.OFFER_WITHDRAWN.getKey())) {
			logger.error("已撤销的契约: " + applNo);
			String[] arrstr = { applNo, "已撤销的契约" };
			throw new BusinessException("0020", arrstr);
		}

		// 处理投保单回退过程中实收数据冲正流水
		retInfo = procCorrMioDataService.setCorrInsurInfo(applNo, traceNodePra.getTraceNode().getPclkBranchNo(),
				traceNodePra.getTraceNode().getPclkNo(),"S");
		if ("0".equals(retInfo.getRetCode())) { // 调用回退处理方法失败
			throw new BusinessException("0017", retInfo.getErrMsg());
		}

		// 插入操作轨迹
		TraceNode traceNode = traceNodePra.getTraceNode();
		if (traceNode == null) {
			logger.error("操作节点信息为空: " + applNo);
			String[] arrstr = { applNo, "" };
			throw new BusinessException("0019", arrstr);
		}
		traceNode.setProcStat(NEW_APPL_STATE.OFFER_WITHDRAWN.getKey());
		traceNode.setProcDate(new Date());
		mongoBaseDao.updateOperTrace(applNo, traceNode);

		// 调用任务撤销接口
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		if (StringUtils.equals(grpInsurAppl.getAccessSource(), "GCSS")) {
			taskProcessRequestBO.setProcessDefKey("ECORBP001");// 团体销售系统流程
		} else {
			Map<String, Object> applNoMap = new HashMap<String, Object>();
			applNoMap.put("applNo", traceNodePra.getApplNo());
			applNoMap.put("operTraceDeque.procStat", NEW_APPL_STATE.ACCEPTED.getKey());
			InsurApplOperTrace applOperTrace=(InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, applNoMap);
			//如果service.udw.enabled 配置为N 则需要重新发起流程3  否则发起流程2
			 String procSwitch = configDao.queryPropertiesConfigure(PROPERTIE_TYPE,applOperTrace.getOperTraceDeque().getLast().getPclkNo(),null);
			 if (StringUtils.equals(procSwitch, YES_NO_FLAG.YES.getKey())){
	        	taskProcessRequestBO.setProcessDefKey("ECORBP003");// 越过核保流程
	        }else{
	    		taskProcessRequestBO.setProcessDefKey("ECORBP002");//全流程柜面出单
	        }

		}
		taskProcessRequestBO.setBusinessKey(applNo);// 必填-业务流水号
		taskProcessServiceDascClient.deleteProcessInstance(taskProcessRequestBO);
		if(0 == queryAccInfoDaoService.findProcEarnestPayTask(applNo)){
			//查询所有账户对应的余额，如果存在 余额不为0的的账户，插入任务 进行全部支取动作
			if(0 != queryAccInfoDaoService.queryCountEarnestAccInfoByApplNo(applNo)){
				//当控制表为空时，插入控制表一条任务信息
				queryAccInfoDaoService.insertProcEarnestPayTask(applNo);	
			}
		}
		try {
			// 通知核保，任务已撤销
			AltBussStatInVO altBussStatInVO = new AltBussStatInVO();
			altBussStatInVO.setBusinessKey(applNo);
			altBussStatInVO.setNewApplState("W");
			altBussStatInVO.setPclkBranchNo(traceNode.getPclkBranchNo());
			altBussStatInVO.setPclkName(traceNode.getPclkName());
			altBussStatInVO.setPclkNo(traceNode.getPclkNo());
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			underWritingServiceClient.updateNewApplStat(altBussStatInVO);
		} catch (Exception e) {
			logger.error("通知核保错误", e);
		}
		retInfo.setRetCode("1");
		return retInfo;
	}
}
