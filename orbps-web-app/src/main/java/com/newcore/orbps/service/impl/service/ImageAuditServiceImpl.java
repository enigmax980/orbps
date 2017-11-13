package com.newcore.orbps.service.impl.service;

import java.io.Serializable;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.task.models.TaskJumpModel;
import com.newcore.task.service.api.TaskBusinessProcessService;

@Service("imageAuditServiceImpl")
@DependsOn("grpInsurApplServer")
public class ImageAuditServiceImpl implements TaskBusinessProcessService<Serializable> {
    @Autowired
    InsurApplServer grpInsurApplServer;
	@Override
	public GrpInsurApplVo invoker(TaskJumpModel taskJumpModel) {
		// TODO Auto-generated method stub
		 GrpInsurApplVo grpInsurApplVo = new GrpInsurApplVo();
		 TaskInfo taskInfo = new TaskInfo();
	     taskInfo.setBizNo(taskJumpModel.getBusinessKey());
	     taskInfo.setTaskId(taskJumpModel.getTaskId());
	     CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
         HeaderInfoHolder.setOutboundHeader(headerInfo);
         GrpDataBoToVo BoToVo = new GrpDataBoToVo();
         //设置中国时区，修改日期显示问题
         TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
         //查询投保单信息
	     GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
	     if("0".equals(grpInsurApplBo.getErrCode())){
	    	 throw new BusinessException(grpInsurApplBo.getErrMsg());
	     }
	     //把投保单信息转换成相应 
	     GrpApplInfoVo grpApplInfoVo = BoToVo.applInfoBoToVo(grpInsurApplBo.getGrpInsurAppl());
	     grpInsurApplVo.setApplInfoVo(grpApplInfoVo);//投保单信息
	     grpInsurApplVo.setTaskInfo(taskInfo);//任务ID
		 return grpInsurApplVo;
	}
}
