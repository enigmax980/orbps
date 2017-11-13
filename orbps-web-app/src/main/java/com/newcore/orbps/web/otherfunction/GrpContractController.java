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
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.ConstructInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Conventions;
import com.newcore.orbps.models.service.bo.grpinsurappl.FundInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.HealthInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplBaseInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpBusiPrdVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPayInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPrintInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpProposalInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSpecialInsurAddInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpVatInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.service.api.InsurApplBackService;
import com.newcore.orbps.service.api.InsurApplContentRevise;
import com.newcore.orbps.web.util.GrpDataVoToBo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;

/**
 * 团单订正功能
 * 
 * @author 
 *
 */
@Controller
@RequestMapping("/orbps/otherfunction/grpInsurAppl")
public class GrpContractController {

   
    
    @Autowired
    InsurApplBackService insurApplBackServiceClient;
    
    @Autowired
    InsurApplContentRevise restfulinsurApplContentRevise;

    
    

    
    /**
     * 提交团单信息
     * 
     * @author wangyutao
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo grpInsurAppl(@CurrentSession Session session,@RequestBody GrpInsurApplVo grpInsurApplVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
//      taskInfo.setBizNo(grpInsurApplVo.getApplInfoVo().getApplNo());
	    traceNode.setPclkBranchNo(sessionModel.getBranchNo());
	    traceNode.setPclkName(sessionModel.getName());
	    traceNode.setPclkNo(sessionModel.getClerkNo());
	    traceNode.setProcStat(NEW_APPL_STATE.REVISE.getKey());
	    
	    GrpDataVoToBo grpDataVoToBo = new GrpDataVoToBo();
	    GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
	    GrpInsurAppl grpInsurAppl = grpDataVoToBo.grpinsurApplVoToBo(grpInsurApplVo);
        grpInsurApplPara.setGrpInsurAppl(grpInsurAppl);
        if(grpInsurApplVo.getTaskInfo()!=null){
        	 grpInsurApplPara.setTaskId(grpInsurApplVo.getTaskInfo().getTaskId());
        }
        grpInsurApplPara.setTraceNode(traceNode);
        RetInfo result = restfulinsurApplContentRevise.reviseGrpBasicContent(grpInsurApplPara);	
        return result;
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