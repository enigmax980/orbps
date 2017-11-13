package com.newcore.orbps.web.otherfunction;


import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.common.SpringContextHolder;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.ApprovalVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.service.api.ManualApprovalService;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 人工审批
 * 
 * @author jincong
 *
 */
@Controller
@RequestMapping("/orbps/otherfunction/manualapproval")
public class ManualApprovalGrpController {
	
	private GrpDataBoToVo grpDataBoToVo;

    @Autowired
    InsurApplServices insurApplServices;

    @Autowired
    InsurApplServer grpInsurApplServer;
    
    @Autowired
    ManualApprovalService manualApprovalService;

    /**
     * 查询审批任务清单
     * 
     * @author jincong
     * @param listquery
     * @param approvalVo
     * @return
     */
    @RequestMapping(value = "/listquery")
    public @ResponseMessage ApprovalVo listquery(@RequestBody ApprovalVo approvalVo) {
        System.out.println("ceshi");
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //清单查询方法未编写
//        ApprovalVo reApprovalVo = this.grpinsurApplBoToVo(grpInsurAppl);
        // grpInsurApplVo = JSON.parseObject(applContentJson,
        // GrpInsurApplVo.class);
//        return reApprovalVo;
        return approvalVo;
    }
    
    /**
     * 查询团单投保单信息
     * 
     * @author jincong
     * @param applquery
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/grpquery")
    public @ResponseMessage GrpInsurApplVo grpQuery(@RequestBody TaskInfo taskInfo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //通过esb调用本地团单提交服务
        //通过getBean获取grpInsurApplServer服务的上下文
        grpInsurApplServer = SpringContextHolder.getBean("grpInsurApplServer");
        GrpInsurApplVo reGrpInsurApplVo = new GrpInsurApplVo();
        System.out.println("++++++++++++++++++++"+grpInsurApplServer);
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
        System.out.println("++++++++++++++++++++"+grpInsurApplBo);
        grpDataBoToVo = new GrpDataBoToVo();
        if("1".equals(grpInsurApplBo.getErrCode())){
        	reGrpInsurApplVo = grpDataBoToVo.grpinsurApplBoToVo(grpInsurApplBo.getGrpInsurAppl());
        }
        reGrpInsurApplVo.setTaskInfo(taskInfo);
        return reGrpInsurApplVo;
    }
    
    /**
     * 团单投保信息审批提交
     * 
     * @author jincong
     * @param applquery
     * @param productVo
     * @return
     */
    @RequestMapping(value = "/grpapplapproval")
    public @ResponseMessage RetInfo grpApplaAproval(@CurrentSession Session session,@RequestBody GrpInsurApplVo grpInsurApplVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNodePra traceNodePra = new TraceNodePra();
        traceNodePra.setApplNo(grpInsurApplVo.getApplInfoVo().getApplNo());
        traceNodePra.setTaskId(grpInsurApplVo.getTaskInfo().getTaskId());
        TraceNode traceNode =new TraceNode();
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        String flag = grpInsurApplVo.getApprovalState();
        if("Y".equals(flag)){
        	grpInsurApplVo.setApprovalState(NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey());
        }else if("N".equals(flag)){
        	grpInsurApplVo.setApprovalState(NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey());
        }
        traceNode.setProcStat(grpInsurApplVo.getApprovalState());
        traceNode.setDescription(grpInsurApplVo.getNote());
        traceNodePra.setTraceNode(traceNode);
        return manualApprovalService.approval(traceNodePra);
    }

}
