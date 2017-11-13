package com.newcore.orbps.service.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.service.api.ManualApprovalService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * @author wangxiao 创建时间：2016年9月24日下午4:21:56
 */
@Service("manualApprovalService")
public class ManualApprovalServiceImpl implements ManualApprovalService {
	
	@Autowired
	TaskPrmyDao taskPrmyDao;
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Resource
	DascInsurParaDao dascInsurParaDao;
	
	@Autowired
	ConfigDao configDao;
	
	private static final String PROPERTIE_TYPE= "UDW_OFF" ;  //配置类型
	
	@AsynCall
	@Override
	public RetInfo approval(TraceNodePra traceNodePra) {
		RetInfo retInfo = new RetInfo();
		String applNo = traceNodePra.getApplNo();
		if(StringUtils.isEmpty(applNo)){
			throw new BusinessException("0001");
		}
		String approvalState = traceNodePra.getTraceNode().getProcStat();
		if(StringUtils.isEmpty(approvalState)){
			String [] arr = {"审批状态",""};
			throw new BusinessException("0018",arr);
		}
		String taskId = traceNodePra.getTaskId();
		if(StringUtils.isEmpty(approvalState)){
			String [] arr = {"taskId",""};
			throw new BusinessException("0018",arr);
		}
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if(grpInsurAppl==null){
			retInfo.setErrMsg("保单基本信息不存在");
			retInfo.setRetCode("0");
			retInfo.setApplNo(applNo);
			return retInfo;
		}
		//任务完成
		if (StringUtils.equals(approvalState, NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey())
			||StringUtils.equals(approvalState, NEW_APPL_STATE.LIST_APPROVE_CHECK_SUC.getKey())) {
			
			TaskProcessService taskProcessService = SpringContextHolder.getBean("taskProcessServiceDascClient");// 为DASC配置文件中定义的ID
			TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
			DascInsurParaBo dascInsurParaBo = dascInsurParaDao.select(applNo);
			dascInsurParaBo.getIsInsuredList();
			Map<String,String> mapVar=new HashMap<>();//选填-放入流程运转过程中必须的业务信息~
			mapVar.put("IS_MAN_APPROVE_SUC","1");
			if(StringUtils.equals(grpInsurAppl.getAccessSource(),"GCSS")){
				if(StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(),grpInsurAppl.getLstProcType())){
					mapVar.put("IS_INSURED_LIST","1");
				}else{
					mapVar.put("IS_INSURED_LIST","0");
				}
			}
			taskProcessRequestBO.setProcessVar(mapVar);
			taskProcessRequestBO.setBusinessKey(applNo);
			taskProcessRequestBO.setTaskId(taskId);// 业务服务参数中获取的任务ID
			taskProcessRequestBO.setBranchNo(traceNodePra.getTraceNode().getPclkBranchNo());
			taskProcessRequestBO.setClerkName(traceNodePra.getTraceNode().getPclkName());
			taskProcessRequestBO.setClerkNo(traceNodePra.getTraceNode().getPclkNo());
			taskProcessService.completeTask(taskProcessRequestBO);// 进行任务完成操作
			retInfo.setRetCode("1");
			TraceNode traceNode = traceNodePra.getTraceNode();
			traceNode.setProcDate(new Date());
			traceNode.setProcStat(approvalState);
			mongoBaseDao.updateOperTrace(applNo, traceNode);
			//判断是否要越过核保 如果等于Y 则禁止核保并添加核保结论
			Map<String, Object> applNoMap = new HashMap<String, Object>();
			applNoMap.put("applNo", traceNodePra.getApplNo());
			applNoMap.put("operTraceDeque.procStat", NEW_APPL_STATE.ACCEPTED.getKey());
			InsurApplOperTrace applOperTrace=(InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,  applNoMap);
			 String procSwitch =configDao.queryPropertiesConfigure(PROPERTIE_TYPE,applOperTrace.getOperTraceDeque().getLast().getPclkNo(),null);
             if (StringUtils.equals(procSwitch, YES_NO_FLAG.YES.getKey()) && !StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())){
            	 //判断是否有无清单  无清单 则添加核保结论
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
             }
             //插入核保轨迹
             TraceNode udwTraceNode = new TraceNode();
             udwTraceNode.setProcStat(NEW_APPL_STATE.UNDERWRITING.getKey());
             udwTraceNode.setProcDate(new Date());
             mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), udwTraceNode);
			return retInfo;
		//审批不通过
		} else if(StringUtils.equals(approvalState, NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey())
			||StringUtils.equals(approvalState, NEW_APPL_STATE.LIST_APPROVE_CHECK_FAL.getKey())) {
			TaskProcessService taskProcessService = SpringContextHolder.getBean("taskProcessServiceDascClient");// 为DASC配置文件中定义的ID
			TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
			taskProcessRequestBO.setTaskId(taskId);;// 业务流水号
			taskProcessRequestBO.setBusinessKey(applNo);
			taskProcessRequestBO.setBranchNo(traceNodePra.getTraceNode().getPclkBranchNo());
			taskProcessRequestBO.setClerkName(traceNodePra.getTraceNode().getPclkName());
			taskProcessRequestBO.setClerkNo(traceNodePra.getTraceNode().getPclkNo());
			Map<String, String> varMap = new HashMap<>();
			varMap.put("IS_MAN_APPROVE_SUC","0");
			if(StringUtils.equals(grpInsurAppl.getAccessSource(),"GCSS")){
				if(StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(),grpInsurAppl.getLstProcType())){
					varMap.put("IS_INSURED_LIST","1");
				}else{
					varMap.put("IS_INSURED_LIST","0");
				}
			}
			taskProcessRequestBO.setProcessVar(varMap);
			taskProcessService.completeTask(taskProcessRequestBO);// 进行任务完成操作
			retInfo.setRetCode("1");
			TraceNode traceNode = traceNodePra.getTraceNode();
			traceNode.setProcDate(new Date());
			traceNode.setProcStat(approvalState);
			mongoBaseDao.updateOperTrace(applNo, traceNode);
			return retInfo;
		}else{
			retInfo.setRetCode("0");
			retInfo.setErrMsg("审批状态为空，人工审批失败");
			return retInfo;
		}
	}

}
