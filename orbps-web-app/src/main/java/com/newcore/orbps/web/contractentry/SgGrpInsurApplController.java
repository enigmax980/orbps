package com.newcore.orbps.web.contractentry;

import java.util.List;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.commons.lang3.StringUtils;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.web.util.SgGrpDataBoToVo;
import com.newcore.orbps.web.util.SgGrpDataVoToBo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;

/**
 * 清汇团单控制
 * 
 * @author xiaoye
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/sg")
public class SgGrpInsurApplController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SgGrpInsurApplController.class);
	
	@Autowired
	ContractQueryService contractQueryService;
	
	@Autowired
	InsurApplServices insurApplServices;

	@Autowired
	InsurApplServer grpInsurApplServer;

	/**
	 * 分页查询的支持服务
	 */
	@Autowired
	PageQueryService pageQueryService;
	
	@Autowired
	BranchService branchService;

	/**
	 * 查询责任信息
	 * 
	 * @author xiaoye
	 * @param query
	 * @param productVo
	 * @return
	 */
	@RequestMapping(value = "/search")
	public @ResponseMessage DataTable<ResponseVo> getResponseSummaryList(QueryDt query, ResponseVo productVo) {
		/** 构建PageQuery对象 **/
		PageQuery<ResponseVo> pageQuery = pageQueryService.tranToPageQuery(query, productVo);

		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");
		headerInfo.getRouteInfo().setBranchNo("1");
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		PageData<ResponseVo> result = insurApplServices.getProductSummaryList(pageQuery);
		return pageQueryService.tranToDataTable(query.getRequestId(), result);
	}

	/**
	 * 提交清单信息
	 * 
	 * @author xiaoye
	 * @param query
	 * @param grpInsurApplVo
	 * @return
	 */
	@RequestMapping(value = "/submit")
	public @ResponseMessage RetInfo sggrpInsurAppl(@RequestBody SgGrpInsurApplVo sgGrpInsurApplVo,
			@CurrentSession Session session) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");
		headerInfo.getRouteInfo().setBranchNo("1");
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());// 机构
		traceNode.setPclkName(sessionModel.getName());// 操作员姓名
		LOGGER.info("操作员工号"+sessionModel.getClerkNo()+  "操作员姓名"+sessionModel.getName());
		traceNode.setPclkNo(sessionModel.getClerkNo());
		traceNode.setProcStat(NEW_APPL_STATE.LIST_ENTRY.getKey());//操作轨迹
		SgGrpDataVoToBo VoToBo = new SgGrpDataVoToBo(); 
		GrpInsurApplPara sggrpInsurApplPara = new GrpInsurApplPara();
		sggrpInsurApplPara.setGrpInsurAppl(VoToBo.grpInsurApplVoToBo(sgGrpInsurApplVo));		
		sggrpInsurApplPara.setTraceNode(traceNode);
		sggrpInsurApplPara.setTaskId(sgGrpInsurApplVo.getTaskInfo().getTaskId());//
		return grpInsurApplServer.save(sggrpInsurApplPara);
	}

	/**
	 * 查询投保单信息
	 * 
	 * @author xiaoye
	 * @param query
	 * @param productVo
	 * @return
	 */
	@RequestMapping(value = "/query")
	public @ResponseMessage SgGrpInsurApplVo search(@RequestBody TaskInfo taskInfo) {

		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		headerInfo.setOrginSystem("ORBPS");
		headerInfo.getRouteInfo().setBranchNo("1");
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		grpInsurApplServer = SpringContextHolder.getBean("grpInsurApplServer");
		// 调用后台服务根据流水号查询
		GrpInsurApplBo sggrpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
		LOGGER.info("查询的BO" + sggrpInsurApplBo.getGrpInsurAppl());
		GrpInsurAppl sggrpInsurAppl = new GrpInsurAppl();
		SgGrpInsurApplVo regrpInsurApplVo = new SgGrpInsurApplVo();
		LOGGER.info("errorcode是" + sggrpInsurApplBo.getErrCode());
		if (StringUtils.equals(sggrpInsurApplBo.getErrCode(), "1")) {
			LOGGER.info("进入IF判断。。。。");
			sggrpInsurAppl = sggrpInsurApplBo.getGrpInsurAppl();
			SgGrpDataBoToVo BoToVo = new SgGrpDataBoToVo();
			regrpInsurApplVo = BoToVo.sgGrpInsurApplBoToVo(sggrpInsurAppl);
			LOGGER.info("BO转VO的值-----" + regrpInsurApplVo.toString());
		}
		regrpInsurApplVo.setTaskInfo(taskInfo);
		LOGGER.info("========。。。。"+regrpInsurApplVo.toString());
		return regrpInsurApplVo;
	}
	
	/**
     * 根据投保单号查询清汇信息
     * 
     * @author jinmeina
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/querySgGrpInser")   
    public @ResponseMessage SgGrpInsurApplVo getRuleType(@CurrentSession Session session,@RequestBody String applNo) {
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        String branchStr1 = "";
        String branchStr2 = "";
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurApplBo sggrpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(applNo);
        if(sggrpInsurApplBo.getGrpInsurAppl()!=null){
        	branchStr1 = sggrpInsurApplBo.getGrpInsurAppl().getProvBranchNo();
            // 操作员机构操作员代码 从session中获取
            SessionModel sessionModel = SessionUtils.getSessionModel(session);
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            branchStr2 = branchService.findProvinceBranch(sessionModel.getBranchNo());
            if (branchStr1.equals(branchStr2)) {
            } else {
                throw new BusinessException("0004", "查询失败，该操作员只能查询本省机构的保单");
            }
        }
        PageQuery<CorrectionVo> pageQuery = new PageQuery<>();
        CorrectionVo condition = new CorrectionVo();
        condition.setCorrectApplNo(applNo);
        pageQuery.setCondition(condition);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<QueryinfVo> pageData = contractQueryService.query(pageQuery);
        List<QueryinfVo> queryinfVos = pageData.getData();
        String newApplState = null;
        if(queryinfVos!=null && !queryinfVos.isEmpty()){
        	newApplState = queryinfVos.get(0).getPostName();
        }
        SgGrpDataBoToVo BoToVo = new SgGrpDataBoToVo();
        SgGrpInsurApplVo sggrpInsurApplVo =  BoToVo.sgGrpInsurApplBoToVo(sggrpInsurApplBo.getGrpInsurAppl());
        if(null != sggrpInsurApplVo){
        	if(null != sggrpInsurApplVo.getSgGrpApplInfoVo()){
            	sggrpInsurApplVo.getSgGrpApplInfoVo().setPolNoState(newApplState);
            }
        	return sggrpInsurApplVo;
        }else{
        	return new SgGrpInsurApplVo();
        }
    }
	
	/**
	 * 清汇暂存功能
	 * @param session
	 * @param sgGrpInsurApplVo
	 * @return
	 */
    @RequestMapping(value = "/tempSave")
    public @ResponseMessage RetInfo tempSave(@CurrentSession Session session,@RequestBody SgGrpInsurApplVo sgGrpInsurApplVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());// 机构
		traceNode.setPclkName(sessionModel.getName());// 操作员姓名
		LOGGER.info("操作员工号"+sessionModel.getClerkNo()+  "操作员姓名"+sessionModel.getName());
		traceNode.setPclkNo(sessionModel.getClerkNo());
		traceNode.setProcStat(NEW_APPL_STATE.RECORD_TEMPORARILY.getKey());//操作轨迹 新单暂存状态
		SgGrpDataVoToBo VoToBo = new SgGrpDataVoToBo();
        GrpInsurApplPara grpInsurApplPara = new GrpInsurApplPara();
        grpInsurApplPara.setTraceNode(traceNode);
        grpInsurApplPara.setGrpInsurAppl(VoToBo.grpInsurApplVoToBo(sgGrpInsurApplVo));
        RetInfo result = grpInsurApplServer.tempSave(grpInsurApplPara); 
        return result;
    }
}