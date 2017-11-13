package com.newcore.orbps.web.otherfunction;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplBackService;
import com.newcore.orbps.service.api.InsurApplContentRevise;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.util.SgGrpDataVoToBo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 订正界面
 * 
 * @author wangyanjie
 *
 */

@Controller
@RequestMapping("/orbps/otherfunction/sgGrpInsurAppl")
public class sgGrpInsurController {
    
    @Autowired
    InsurApplServer insurApplServer;
    
    /**
     * 回退服务
     */
    @Autowired
    InsurApplBackService insurApplBackServiceClient;

    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    ContractQueryService contractQueryService;

    @Autowired
    InsurApplContentRevise restfulinsurApplContentRevise;

    /**
     * 清汇提交
     * 
     * @author chang
     * @param submit
     * @param
     * @return
     */
    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo sggrpInsurAppl(@RequestBody SgGrpInsurApplVo sgGrpInsurApplVo,
            @CurrentSession Session session) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //传操作轨迹信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
//      taskInfo.setBizNo(grpInsurApplVo.getApplInfoVo().getApplNo());
	    traceNode.setPclkBranchNo(sessionModel.getBranchNo());
	    traceNode.setPclkName(sessionModel.getName());
	    traceNode.setPclkNo(sessionModel.getClerkNo());
	    traceNode.setProcStat(NEW_APPL_STATE.REVISE.getKey());
	    //调用公共工具进行vo转bo
	    SgGrpDataVoToBo sgGrpDataVoToBo = new SgGrpDataVoToBo();
	    GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
	    grpInsurApplPara.setGrpInsurAppl(sgGrpDataVoToBo.grpInsurApplVoToBo(sgGrpInsurApplVo));
        if(sgGrpInsurApplVo.getTaskInfo()!=null){
       	 	grpInsurApplPara.setTaskId(sgGrpInsurApplVo.getTaskInfo().getTaskId());
        }
        grpInsurApplPara.setTraceNode(traceNode);
        return restfulinsurApplContentRevise.reviseGrpBasicContent(grpInsurApplPara);
    }
    
    /**
     * 回退团单信息
     * 
     * @author jincong
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/back")
    public @ResponseMessage RetInfo back(@CurrentSession Session session,@RequestBody String backInfo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        
        //解析backInfo
        String[] str = backInfo.split(",");
        String applNo = "";
        String backStat = "";
        String backReason = "";
        if(str.length>0){
        	applNo = str[0];
        	backStat = str[1];
        	if(str.length>2){
        		backReason = str[2];
        	}
        }
        //赋值操作信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
	    traceNode.setPclkBranchNo(sessionModel.getBranchNo());
	    traceNode.setPclkName(sessionModel.getName());
	    traceNode.setPclkNo(sessionModel.getClerkNo());
	    traceNode.setProcStat(backStat);
	    traceNode.setDescription(backReason);
	    
	    TraceNodePra traceNodePra = new TraceNodePra();
	    traceNodePra.setApplNo(applNo);
	    traceNodePra.setTraceNode(traceNode);
	    
	    RetInfo result = insurApplBackServiceClient.backDeal(traceNodePra);
       
        return result;
    }
    

}