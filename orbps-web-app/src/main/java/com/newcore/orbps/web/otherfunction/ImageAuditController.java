package com.newcore.orbps.web.otherfunction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ImageAuditSubmitVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ImageAuditTrailVo;
import com.newcore.orbps.service.api.ImgApprovalService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;


@Controller
@RequestMapping("/orbps/otherfunction/imageAudit")
public class ImageAuditController {
	
	@Autowired
	ImgApprovalService imgApprovalService;
    @Autowired
    InsurApplServer grpInsurApplServer;
	/***
	 * 影像轨迹查询
	 * @return
	 */
	@RequestMapping(value = "/query")
	public @ResponseMessage List<ImageAuditTrailVo> queryImageTrail(@RequestBody String applNo){
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<ImageAuditTrailVo> list = imgApprovalService.search(applNo);
		return list;
		
	}
	
	/**
	 * 查询投保单信息的管理机构及其他信息
	 * @param session
	 * @param applNo
	 * @return
	 */
	@RequestMapping(value = "/serch")
	public @ResponseMessage GrpApplInfoVo serchGrpInsurApp(@CurrentSession Session session,@RequestBody String applNo){
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //查询投保单信息
	    GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(applNo);
	    GrpApplInfoVo grpApplInfoVo = new GrpApplInfoVo();
	    if("0".equals(grpInsurApplBo.getErrCode())){
	    	throw new BusinessException(grpInsurApplBo.getErrMsg());
	    }
  	    GrpDataBoToVo BoToVo = new GrpDataBoToVo();
	    if(grpInsurApplBo != null ){
	  	    //把投保单信息转换成相应 
	  	    grpApplInfoVo = BoToVo.applInfoBoToVo(grpInsurApplBo.getGrpInsurAppl());
	    }
		return grpApplInfoVo;
		
	}
	
	
	
	/**
	 * 影像审批提交
	 * @return
	 */
	@RequestMapping(value = "/submit")
	public @ResponseMessage RetInfo submitAudit(@CurrentSession Session session, @RequestBody ImageAuditSubmitVo imageAuditSubmitVo){
	  	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
		Map<String ,String > map = new HashMap<>();
		map.put("operatorBranchNo", sessionModel.getBranchNo());//操作员机构号
		map.put("operatorNo", sessionModel.getClerkNo());//操作员工号
		map.put("operator", sessionModel.getName());//操作名字
		
// 		map.put("operatorBranchNo", "120000");//操作员机构号
//		map.put("operatorNo", "zhf01");//操作员工号
//		map.put("operator", "zhf01");//操作名字
		
		map.put("applNo", imageAuditSubmitVo.getApplNo());//投保单号
		map.put("notice", imageAuditSubmitVo.getNotice());//审批原因
		map.put("taskId", imageAuditSubmitVo.getTaskId());//任务轨迹
		map.put("procFlag", imageAuditSubmitVo.getProcFlag());//审批是否通过
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
		RetInfo retInfo = imgApprovalService.add(map);
		return retInfo;
		
	}
}
