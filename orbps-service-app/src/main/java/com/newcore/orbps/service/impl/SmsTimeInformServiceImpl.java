package com.newcore.orbps.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.service.api.SmsTimeInformService;


/**
 * @author zhoushoubo
 * @date 2017年5月8日
 * 内容: 打印系统做成打印数据之后，调用CCS系统向客户发送短信，然后调用本服务记录短信发送时间。
 */
@Service("smsTimeInformService")
public class SmsTimeInformServiceImpl implements SmsTimeInformService{

	@Autowired
	TaskPrmyDao taskPrmyDao;

	@Override
	public RetInfo insSmsTime(Map<String, Object> paraMap) {
		if(paraMap == null){
			throw new BusinessException("0018", "入参内容");			
		}
		
		String applNo=(String) paraMap.get("applNo");
		if(applNo == null || StringUtils.isEmpty(applNo)){
			throw new BusinessException("0018", "投保单号");			
		}
		
		String smsTime=(String) paraMap.get("smsTime");
		if(smsTime == null || StringUtils.isEmpty(smsTime)){
			throw new BusinessException("0018", "SMS短信发送时间");			
		}
		
		RetInfo retInfo=new RetInfo();
		retInfo.setApplNo(applNo);
		
		//调用
		boolean ret=taskPrmyDao.updateSmsTimeByApplNo(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), applNo, smsTime);
		if(ret){
			retInfo.setRetCode("1");
			retInfo.setErrMsg("更新成功.");
		} else {
			retInfo.setRetCode("0");
			retInfo.setErrMsg("没有符合更新条件的数据.");
		}
		return retInfo;
	}
}
