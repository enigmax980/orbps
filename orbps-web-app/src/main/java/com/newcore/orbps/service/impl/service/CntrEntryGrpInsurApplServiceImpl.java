package com.newcore.orbps.service.impl.service;

import com.newcore.task.models.TaskJumpModel;
import com.newcore.task.service.api.TaskBusinessProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;

import org.apache.commons.lang3.StringUtils;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.contractentry.GrpInsurApplController;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.util.HeaderInfoUtils;

import java.io.Serializable;
import java.util.List;
import java.util.TimeZone;

/**
 * 团单保单信息查询
 * @author 王鸿林
 *
 * @date 2016年12月19日 下午7:49:57
 * @TODO
 */
@Service("cntrEntryGrpInsurApplService")
@DependsOn({"grpInsurApplServer","contractQueryService"})
public class CntrEntryGrpInsurApplServiceImpl implements TaskBusinessProcessService<Serializable> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CntrEntryGrpInsurApplServiceImpl.class);
    @Autowired
    InsurApplServer grpInsurApplServer;
    @Autowired
    ContractQueryService contractQueryService;
    @Override
    public GrpInsurApplVo invoker(TaskJumpModel taskJumpModel) {
        System.out.println("----------------"+taskJumpModel+"----------------");
        System.out.println("----------------"+taskJumpModel.getBusinessKey()+"----------------");
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.setBizNo(taskJumpModel.getBusinessKey());
        taskInfo.setTaskId(taskJumpModel.getTaskId());
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
        //设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        //调用后台服务查询保单信息
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
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
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurApplVo regrpInsurApplVo = new GrpInsurApplVo();
        GrpDataBoToVo BoToVo = new GrpDataBoToVo();
        //如果后台返回成功，进行BoToVo转换，返回
        if("1".equals(grpInsurApplBo.getErrCode())){
            grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
            regrpInsurApplVo = BoToVo.grpinsurApplBoToVo(grpInsurAppl);
            System.out.println("vo+++++++++++++++++++++++++++"+regrpInsurApplVo.toString());
        }
        regrpInsurApplVo.setTaskInfo(taskInfo);
        regrpInsurApplVo.setApprovalState(postkey);//新单状态
        return regrpInsurApplVo;
    }
}