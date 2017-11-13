package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.model.service.gcss.vo.AppRinfo;
import com.newcore.orbps.model.service.gcss.vo.ImagInformVo;
import com.newcore.orbps.model.service.gcss.vo.NotifyApplDataAuditRet;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ImageAuditTrailVo;
import com.newcore.orbps.service.api.ImgApprovalService;
import com.newcore.orbps.service.gcss.api.NotifyApplDataAudit;
import com.newcore.supports.models.cxf.AuthInfo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.cxf.PageInfo;
import com.newcore.supports.models.cxf.ProcessInfo;
import com.newcore.supports.models.cxf.RouteInfo;
import com.newcore.supports.models.cxf.SecurityInfo;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 影像审批结果信息接口
 * 
 * @author wangxiao
 * @date 2017-05-15
 */
@Service("imgApprovalService")
public class ImgApprovalServiceImpl implements ImgApprovalService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(ImgApprovalServiceImpl.class);
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	TaskPrmyDao taskPrmyDao;
	@Autowired
	TaskProcessService taskProcessServiceDascClient;
	@Autowired
	NotifyApplDataAudit notifyApplDataAudit;
	@Override
	@AsynCall
	public RetInfo add(Map<String, String> map) {
		String applNo = map.get("applNo");
		String taskId = map.get("taskId");
		RetInfo retInfo = new RetInfo();
		if(StringUtils.isEmpty(applNo)){
			retInfo.setRetCode("0");
			retInfo.setErrMsg("投保单号不能为空");
			return retInfo;
		}
		//判断保单是否生效
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), applNo, "C");
		if(taskPrmyInfos != null && !taskPrmyInfos.isEmpty()){
			retInfo.setRetCode("0");
			retInfo.setApplNo(applNo);
			retInfo.setErrMsg("操作失败:投保单号对应保单已生效");
			return retInfo;
		}
		String procStat = map.get("procFlag");
		String pclkBranchNo = map.get("operatorBranchNo");
		String pclkNo = map.get("operatorNo");
		String pclkName = map.get("operator");
		String description = map.get("notice");
		TraceNode traceNode = new TraceNode();
		traceNode.setProcStat(procStat);
		traceNode.setPclkBranchNo(pclkBranchNo);
		traceNode.setPclkNo(pclkNo);
		traceNode.setPclkName(pclkName);
		traceNode.setDescription(description);
		if(StringUtils.equals("Y",procStat)){
			traceNode.setDescription("审批通过");
			description = "审批通过";
		}
		traceNode.setProcDate(new Date());
		Map<String, Object> mapVar = new HashMap<>();
		mapVar.put("applNo",applNo);
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,mapVar);
		if(insurApplOperTrace == null){
			retInfo.setRetCode("0");
			retInfo.setApplNo(applNo);
			retInfo.setErrMsg("操作失败:投保单号对应的操作轨迹不存在");
			return retInfo;
		}
		//将影像审批轨迹添加到数据库
		boolean b = mongoBaseDao.updateImgApprovalTrace(applNo, traceNode);
		if(!b){
			retInfo.setApplNo(applNo);
			retInfo.setErrMsg("操作失败");
			retInfo.setRetCode("0");
			return retInfo;
		}
		//如果taskId不为空，调用完成任务方法
		if(!StringUtils.isEmpty(taskId)){
			completeTask(taskId,applNo);
		}
		ImagInformVo imagInformVo = new ImagInformVo();
		imagInformVo.setAPPLNO(applNo);
		AppRinfo apprInfo = new AppRinfo();
		apprInfo.setOPERATORNAME(pclkName);
		apprInfo.setOPERATORNO(pclkNo);
		apprInfo.setOPTBRANCH(pclkBranchNo);
		imagInformVo.setAPPRINFO(apprInfo);
		imagInformVo.setNOTICE(description);
		imagInformVo.setOPTTIME(DateFormatUtils.format(traceNode.getProcDate(),"yyyy-MM-dd HH:mm:ss.SSS"));
		imagInformVo.setPROCFLAG(procStat);
		imagInformVo.setTYPE("01");
		try {
			//消息头设置
			setOutboundHeader();
			NotifyApplDataAuditRet ret = notifyApplDataAudit.notifyApplDataAudit(imagInformVo);
			if(StringUtils.equals(ret.getRetCode(),"1")){
				retInfo.setApplNo(applNo);
				retInfo.setRetCode("1");
			}else{
				retInfo.setApplNo(applNo);
				retInfo.setRetCode("0");
				retInfo.setErrMsg("通知团销系统失败，失败原因:"+ret.getRemark());
			}
		} catch (Exception e) {
			logger.error("调用团销系统接口失败",e);
			retInfo.setApplNo(applNo);
			retInfo.setRetCode("0");
			retInfo.setErrMsg("调用团销系统接口失败");
		}
		return retInfo;
	}
	
	@Override
	public List<ImageAuditTrailVo> search(String applNo) {
		List<ImageAuditTrailVo> imageAuditTrailVos = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		//判断保单是否生效
		String isInforce = "未生效";
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), applNo, "C");
		if(taskPrmyInfos != null && !taskPrmyInfos.isEmpty()){
			isInforce = "已生效";
		}		
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, map);
		if(insurApplOperTrace == null){
			throw new BusinessException("0015");
		}
		Deque<TraceNode> imgApprovalTraceDeque = insurApplOperTrace.getImgApprovalTraceDeque();
		if(imgApprovalTraceDeque == null || imgApprovalTraceDeque.isEmpty()){
			ImageAuditTrailVo imageAuditTrailVo = new ImageAuditTrailVo();
			imageAuditTrailVo.setApplNo(applNo);
			imageAuditTrailVo.setApprovalState("未审批");
			imageAuditTrailVo.setProcFlag("");
			imageAuditTrailVo.setNotice("");
			imageAuditTrailVo.setOperator("");
			imageAuditTrailVo.setOptTime("");
			imageAuditTrailVo.setIsInforce(isInforce);
			imageAuditTrailVos.add(imageAuditTrailVo);
			return imageAuditTrailVos;
		}
		for(TraceNode traceNode:imgApprovalTraceDeque){
			ImageAuditTrailVo imageAuditTrailVo = new ImageAuditTrailVo();
			imageAuditTrailVo.setApplNo(applNo);
			imageAuditTrailVo.setApprovalState("已审批");
			if(StringUtils.equals("Y", traceNode.getProcStat())){
				imageAuditTrailVo.setProcFlag("通过");
			}else{
				imageAuditTrailVo.setProcFlag("不通过");
			}
			imageAuditTrailVo.setNotice(traceNode.getDescription());
			imageAuditTrailVo.setOperator(traceNode.getPclkName());
			imageAuditTrailVo.setOptTime(DateFormatUtils.format(traceNode.getProcDate(),"yyyy/MM/dd HH:mm:dd"));
			imageAuditTrailVo.setIsInforce(isInforce);
			imageAuditTrailVos.add(imageAuditTrailVo);
		}
		return imageAuditTrailVos;
	}
	
	// 调用任务完成方法
	private void completeTask(String taskId,String applNo){
		TaskProcessRequestBO taskProcessbo = new TaskProcessRequestBO();
		taskProcessbo.setTaskId(taskId);
		taskProcessbo.setBusinessKey(applNo);
		taskProcessServiceDascClient.completeTask(taskProcessbo);
	}
	
	/**
	 * 自定义报文头
	 * @return
	 */
	private void setOutboundHeader() {
		//消息头设置
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");					
		headerInfo.setServiceCode("");
		headerInfo.setServiceName("NotifyApplDataAudit");
		headerInfo.setServiceVersion("1.0");
		//登录认证信息部分
		AuthInfo authInfo= new AuthInfo();
		authInfo.setBranchNo("");
		authInfo.setTokenId("");
		authInfo.setUserId("");
		headerInfo.setAuthInfo(authInfo);
		//安全信息部分
		SecurityInfo security = new SecurityInfo();
		security.setSignature("");
		headerInfo.setSecurityInfo(security);
		//路由信息
		RouteInfo routeInfo = new RouteInfo();
		routeInfo.setBranchNo("000000");
		routeInfo.setDestSystem("GCSS");
		headerInfo.setRouteInfo(routeInfo);
		//控制信息
		ProcessInfo  processInfo = new ProcessInfo();
		processInfo.setPageInfo(new PageInfo());
		processInfo.setProcessMode("S");
		processInfo.setProcessNum("1");
		headerInfo.setProcessInfo(processInfo);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
	}
}
