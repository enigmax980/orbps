package com.newcore.orbps.service.impl.service;

import java.io.Serializable;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import com.halo.core.common.SpringContextHolder;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.contractentry.SgGrpInsurApplController;
import com.newcore.orbps.web.util.SgGrpDataBoToVo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.task.models.TaskJumpModel;
import com.newcore.task.service.api.TaskBusinessProcessService;
/**
 * 清汇保单信息查询   
 * @author 王鸿林
 *
 * @date 2016年11月2日 下午4:12:15
 * @TODO
 */
@Service("cntrEntrySgGrpInsurApplService")
@DependsOn({"grpInsurApplServer","contractQueryService"})
public class CntrEntrySgGrpInsurApplServiceImpl implements TaskBusinessProcessService<Serializable>{
	 private static final Logger LOGGER = LoggerFactory.getLogger(CntrEntrySgGrpInsurApplServiceImpl.class);
	@Autowired
	InsurApplServer grpInsurApplServer;
    @Autowired
    ContractQueryService contractQueryService;
	@Override
	public SgGrpInsurApplVo invoker(TaskJumpModel taskJumpModel) {
		LOGGER.info("契约受理   从任务平台传过来数据:"+taskJumpModel.toString());
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setBizNo(taskJumpModel.getBusinessKey());//业务流水号 || 投保单号
        taskInfo.setTaskId(taskJumpModel.getTaskId());//任务ID
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		//设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
		// 调用后台服务根据流水号查询
		GrpInsurApplBo sggrpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
		//调用服务报文头
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        //构建查询参数，以投保单号作为查询条件
        PageQuery<CorrectionVo> pageQuery = new PageQuery<CorrectionVo>();
        CorrectionVo correctionVo = new CorrectionVo();
        correctionVo.setCorrectApplNo(taskInfo.getBizNo());
        pageQuery.setCondition(correctionVo);
        //调用后台服务查询新单状态。
        PageData<QueryinfVo> pageData = contractQueryService.query(pageQuery);
        String postName = "";
        String postkey = "";
        if(null != pageData.getData()){
        	//新单状态
        	 postName = pageData.getData().get(0).getPostName();
        }
        //由于postName是字典的value值 所以进去匹配所对应的key
        NEW_APPL_STATE[] sL=NEW_APPL_STATE.values();
		for(int i=0;i<sL.length;i++){
			if(StringUtils.equals(sL[i].getDescription(), postName)){
				postkey = sL[i].getKey();
				break;
			}
		}
		GrpInsurAppl sggrpInsurAppl = new GrpInsurAppl();
		SgGrpInsurApplVo regrpInsurApplVo = new SgGrpInsurApplVo();
		if (StringUtils.equals(sggrpInsurApplBo.getErrCode(), "1")) {
			sggrpInsurAppl = sggrpInsurApplBo.getGrpInsurAppl();
			SgGrpDataBoToVo BoToVo = new SgGrpDataBoToVo();
			regrpInsurApplVo = BoToVo.sgGrpInsurApplBoToVo(sggrpInsurAppl);
		}
		regrpInsurApplVo.setTaskInfo(taskInfo);
		regrpInsurApplVo.setApprovalState(postkey);
		return regrpInsurApplVo;
	}

}
