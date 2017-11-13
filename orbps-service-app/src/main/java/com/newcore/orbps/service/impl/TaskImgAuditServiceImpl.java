package com.newcore.orbps.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.TaskImgAuditService;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;


/**
 * @author zhoushoubo
 * @date 2017年5月16日
 * 内容: 影像资料审核流程服务。
 */
@Service("taskImgAuditService")
public class TaskImgAuditServiceImpl implements TaskImgAuditService{

	private static Logger logger = LoggerFactory.getLogger(TaskImgAuditServiceImpl.class);
	
	@Autowired
	TaskProcessService taskProcessServiceDascClient;

    @Autowired
    TaskPrmyDao taskPrmyDao;
    
	@Override
	@AsynCall
	public RetInfo startTask(Map<String, Object> paraMap) {
		
		RetInfo retInfo = new RetInfo();
		
		if(paraMap == null){
			throw new BusinessException("0018", "入参内容");			
		}
		
		String applNo=(String) paraMap.get("applNo");
		if(applNo == null || StringUtils.isEmpty(applNo)){
			throw new BusinessException("0018", "投保单号");			
		}
		
		String salesBranchNo=(String) paraMap.get("salesBranchNo");
		if(salesBranchNo == null || StringUtils.isEmpty(salesBranchNo)){
			throw new BusinessException("0018", "销售机构号");			
		}
		
		String salesChannel=(String) paraMap.get("salesChannel");
		if(salesChannel == null || StringUtils.isEmpty(salesChannel)){
			throw new BusinessException("0018", "销售渠道");			
		}
		
		String cntrType=(String) paraMap.get("cntrType");
		if(cntrType == null || StringUtils.isEmpty(cntrType)){
			throw new BusinessException("0018", "契约形式");			
		}

		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.CNTR_INFORCE_TASK_QUEUE.toString(), applNo, "C");
		if(null != taskPrmyInfos && !taskPrmyInfos.isEmpty()){
			retInfo.setApplNo(applNo);
			retInfo.setErrMsg("此保单已生效,不允许再重复进行影像资料审核流程.");
			retInfo.setRetCode("0");
			return retInfo;
		}
		
        //影像资料审核流程
		Map<String, String> mapVar = new ConcurrentHashMap<>();
		//受理机构号存销售机构号
        mapVar.put("ACCEPT_BRANCH_NO", salesBranchNo);
		//受理渠道存销售渠道
        mapVar.put("ACCEPT_CHANNEL", salesChannel);
        //业务类别存契约形式
        mapVar.put("CNTR_TASK_SUB_ITEM", cntrType);
        
        TaskProcessRequestBO taskProcessRq=new TaskProcessRequestBO();
        //必填-流程定义ID
        taskProcessRq.setProcessDefKey("ECORBP004");
        //必填-业务流水号
        taskProcessRq.setBusinessKey(applNo);
        taskProcessRq.setBranchNo(salesBranchNo);
        taskProcessRq.setProcessVar(mapVar);
        //发起任务流
        logger.info("影像资料审核流程start");
        taskProcessServiceDascClient.startProcess(taskProcessRq);
        
        retInfo.setApplNo(applNo);
		retInfo.setRetCode("1");
		return retInfo;
	}
}
