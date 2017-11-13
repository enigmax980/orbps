 package com.newcore.orbps.web.otherfunction;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.common.SpringContextHolder;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.ManualApprovalList;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.ApprovalListVo;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.ApprovalVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.CommonQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.service.api.ManualApprovalService;
import com.newcore.orbps.web.util.SgGrpDataBoToVo;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;
/**
 * 清汇人工审批
 * @author wanghonglin
 *
 * @date 2016年11月10日 下午2:53:54
 * @TODO
 */
@Controller
@RequestMapping("/orbps/otherfunction/sgmanualapproval")
public class ManualApprovalSgController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ManualApprovalSgController.class);
    
	@Autowired
    ManualApprovalService manualApprovalService;
	@Autowired
	InsurApplServices insurApplServices;
	@Autowired
	InsurApplServer grpInsurApplServer;
	@Autowired
	CommonQueryService commonQueryService;
	@Autowired
	PageQueryService pageQueryService;
	@Autowired
	BranchService branchService;
	
	@RequestMapping(value = "/submit")
	public @ResponseMessage RetInfo sggrpInsurAppl(@RequestBody SgGrpInsurApplVo sgGrpInsurApplVo,
			@CurrentSession Session session) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		// TaskInfo taskInfo = new TaskInfo();
		// taskInfo.setOperState(sgGrpInsurApplVo.getApprovalState());//页面过来的单子状态
		TraceNode traceNode = new TraceNode();
		TraceNodePra traceNodePra = new TraceNodePra();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());// 机构
		traceNode.setPclkName(sessionModel.getName());// 操作员姓名
		LOGGER.info("操作员工号"+sessionModel.getClerkNo()+  "操作员姓名"+sessionModel.getName());
		traceNode.setPclkNo(sessionModel.getClerkNo());//
		String flag = sgGrpInsurApplVo.getApprovalState();
		if("Y".equals(flag)){
			sgGrpInsurApplVo.setApprovalState(NEW_APPL_STATE.GRP_APPROVE_CHECK_SUC.getKey());
        }else if("N".equals(flag)){
        	sgGrpInsurApplVo.setApprovalState(NEW_APPL_STATE.GRP_APPROVE_CHECK_FAL.getKey());
        }
		traceNode.setProcStat(sgGrpInsurApplVo.getApprovalState());
		traceNode.setDescription(sgGrpInsurApplVo.getNote());
		traceNodePra.setTraceNode(traceNode);
		traceNodePra.setApplNo(sgGrpInsurApplVo.getTaskInfo().getBizNo());
		traceNodePra.setTaskId(sgGrpInsurApplVo.getTaskInfo().getTaskId());
		return manualApprovalService.approval(traceNodePra);
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
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		grpInsurApplServer = SpringContextHolder.getBean("grpInsurApplServer");
		// 调用后台服务根据流水号查询
		GrpInsurApplBo sggrpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
		LOGGER.info("查询的BO" + sggrpInsurApplBo.getGrpInsurAppl());
		SgGrpInsurApplVo resgGrpInsurApplVo = new SgGrpInsurApplVo();
		LOGGER.info("errorcode是" + sggrpInsurApplBo.getErrCode());
		if ("1".equals(sggrpInsurApplBo.getErrCode())){
			SgGrpDataBoToVo sgGrpDataBoToVo = new SgGrpDataBoToVo();
			resgGrpInsurApplVo = sgGrpDataBoToVo.sgGrpInsurApplBoToVo(sggrpInsurApplBo.getGrpInsurAppl());
		}
		resgGrpInsurApplVo.setTaskInfo(taskInfo);
		return resgGrpInsurApplVo;
	}
	/**
	 * 人工审批查询 
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/search")
	public @ResponseMessage DataTable<ApprovalListVo> getManualVoResult(@CurrentSession Session session,
			QueryDt query,  ApprovalVo approvalVo) throws Exception {
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		//获取session中的机构的省级机构号
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
		Map<String, Object> map = new HashMap<String, Object>();
		// 销售机构号
		if (StringUtils.isBlank(approvalVo.getSalesBranchNo())) {
			map.put("salesBranchNo", sessionProvinceBranchNo);
			//判断机构号权限
		}else{
			CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo1);
			String salesProvinceBranch = branchService.findProvinceBranch(approvalVo.getSalesBranchNo());
			if(!StringUtils.equals(sessionProvinceBranchNo, salesProvinceBranch)){
				throw new BusinessException("0004","操作员只能查询本省及以下机构信息");
			}
			map.put("salesBranchNo", approvalVo.getSalesBranchNo());
		}
		// 投保单号
		if (!StringUtils.isBlank(approvalVo.getApplNo())) {
			map.put("applNo", approvalVo.getApplNo());
		}
		// 是否包含下级机构
		if (!StringUtils.isBlank(approvalVo.getLevelFlag())) {
			map.put("levelFlag", approvalVo.getLevelFlag());
		}else{
			map.put("levelFlag", YES_NO_FLAG.YES.getKey());
		}
		// 审批操作人员机构号
		if (!StringUtils.isBlank(approvalVo.getPclkBranchNo())) {
			map.put("pclkBranchNo", approvalVo.getPclkBranchNo());
		}
		// 审批操作员工号
		if (!StringUtils.isBlank(approvalVo.getPclkNo())) {
			map.put("pclkNo", approvalVo.getPclkNo());
		}
		// 审批类型
		if (!StringUtils.isBlank(approvalVo.getRuleTye())) {
			map.put("ruleTye", approvalVo.getRuleTye());
		}
		// 审批操作起期
		//将后台的日期字符串转成日期送给后台。
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isBlank(approvalVo.getStartDate())) {
			map.put("startDate", sdf.parseObject(approvalVo.getStartDate()));
		}
		// 审批操作止期
		if (!StringUtils.isBlank(approvalVo.getEndDate())) {
			map.put("endDate", sdf.parse(approvalVo.getEndDate()));
		}
		// 是否审批通过
		if (!StringUtils.isBlank(approvalVo.getResult())) {
			String result ="";
			if("Y".equals(approvalVo.getResult())){
				result = "1";
			}else{
				result = "2";
			}
			map.put("result", result);
		}	
			int page = Integer.valueOf(String.valueOf(query.getPage()));
			int size = query.getRows();
			page = (page/size)+1;
			map.put("page",page);
			map.put("size", query.getRows());

		// 调用后台commonQueryService.manualApproval方法。
		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo1);
		PageData<ManualApprovalList> manualApprovalList = commonQueryService.manualApproval(map);
		// 创建PageData。
		PageData<ApprovalListVo> pageData = new PageData<ApprovalListVo>();
		if ( null != manualApprovalList && manualApprovalList.getTotalCount() != 0) {
			// 创建arrayList，接收数据。
			List<ApprovalListVo> arrayList = new ArrayList<>();
			// yyyy-MM-dd HH:mm
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			//yyyy-MM-dd
			SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
			for (ManualApprovalList  manualApproval:manualApprovalList.getData()) {
				ApprovalListVo approvalListVo = new ApprovalListVo();
				//投保单号
				approvalListVo.setApplNo(manualApproval.getApplNo());
				//投保人（姓名）
				approvalListVo.setSgName(manualApproval.getSgName());
				//销售机构代码
				approvalListVo.setSalesBranchNo(manualApproval.getSalesBranchNo());
				//首个主险种代码
				approvalListVo.setPolCode(manualApproval.getPolCode());
				//新单受理时间
				if(manualApproval.getAcceptDate() != null){
					approvalListVo.setAcceptDate(simpleDateFormat.format(manualApproval.getAcceptDate()));
				}
				//生效时间
				if(manualApproval.getSignDate() != null){
					approvalListVo.setSignDate(simpleDateFormat.format(manualApproval.getSignDate()));
				}
				//生效时日
				if(manualApproval.getInForceDate() != null){
					approvalListVo.setInForceDate(simpleDateFormats.format(manualApproval.getInForceDate()));
				}
				//审批类型
				if("1".equals(manualApproval.getRuleTye())){
					approvalListVo.setRuleTye("生效日审批");
				}
				//进入人工审批原因
				approvalListVo.setReason(manualApproval.getReason());
				//审批结果
				if("1".equals(manualApproval.getResult())){
					approvalListVo.setResult("通过");
				}else{
					approvalListVo.setResult("不通过");
				}
				
				//审批操作人员机构号
				approvalListVo.setPclkBranchNo(manualApproval.getPclkBranchNo());
				//审批操作人员工号
				approvalListVo.setPclkNo(manualApproval.getPclkNo());
				//审批操作人员姓名
				approvalListVo.setPclkName(manualApproval.getPclkName());
				//审批操作时间
				if(manualApproval.getProcDate() != null){
					approvalListVo.setProcDate(simpleDateFormat.format(manualApproval.getProcDate()));
				}
				//审批不通过原因
				approvalListVo.setNote(manualApproval.getNote());
				arrayList.add(approvalListVo);
			}
			// 当前页号的列表数据
			pageData.setData(arrayList);
			// 总记录数
			pageData.setTotalCount(manualApprovalList.getTotalCount());
		}
		return pageQueryService.tranToDataTable(query.getRequestId(), pageData);
	}
	@RequestMapping(value = "/outExcel")
	public  @ResponseMessage List<ApprovalListVo> outExcel(@CurrentSession Session session,@RequestBody ApprovalVo approvalVo) throws ParseException{
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		//获取session中的机构号
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
		Map<String, Object> map = new HashMap<String, Object>();
		// 销售机构号
		if (StringUtils.isBlank(approvalVo.getSalesBranchNo())) {
			map.put("salesBranchNo", sessionProvinceBranchNo);
			//判断机构号权限
		}else{
			CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo1);
			String salesProvinceBranchNo = branchService.findProvinceBranch(approvalVo.getSalesBranchNo());
			if(!StringUtils.equals(sessionProvinceBranchNo, salesProvinceBranchNo)){
				throw new BusinessException("0004","操作员只能查询本省机构下信息");
			}
			map.put("salesBranchNo", approvalVo.getSalesBranchNo());
		}
		// 投保单号
		if (!StringUtils.isBlank(approvalVo.getApplNo())) {
			map.put("applNo", approvalVo.getApplNo());
		}
		// 是否包含下级机构
		if (!StringUtils.isBlank(approvalVo.getLevelFlag())) {
			map.put("levelFlag", approvalVo.getLevelFlag());
		}else{
			map.put("levelFlag", YES_NO_FLAG.YES.getKey());
		}
		// 审批操作人员机构号
		if (!StringUtils.isBlank(approvalVo.getPclkBranchNo())) {
			map.put("pclkBranchNo", approvalVo.getPclkBranchNo());
		}
		// 审批操作员工号
		if (!StringUtils.isBlank(approvalVo.getPclkNo())) {
			map.put("pclkNo", approvalVo.getPclkNo());
		}
		// 审批类型
		if (!StringUtils.isBlank(approvalVo.getRuleTye())) {
			map.put("ruleTye", approvalVo.getRuleTye());
		}
		// 审批操作起期
		//将后台的日期字符串转成日期送给后台。
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (!StringUtils.isBlank(approvalVo.getStartDate())) {
			map.put("startDate", sdf.parseObject(approvalVo.getStartDate()));
		}
		// 审批操作止期
		if (!StringUtils.isBlank(approvalVo.getEndDate())) {
			map.put("endDate", sdf.parse(approvalVo.getEndDate()));
		}
		// 是否审批通过
		if (!StringUtils.isBlank(approvalVo.getResult())) {
			map.put("result", approvalVo.getResult());
		}	
		// 调用后台commonQueryService.manualApproval方法。
		CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo1);
		PageData<ManualApprovalList> manualApprovalList = commonQueryService.manualApproval(map);
		// 创建arrayList，接收数据。
		List<ApprovalListVo> arrayList = new ArrayList<>();
		if ( null != manualApprovalList && manualApprovalList.getTotalCount() != 0) {
			// yyyy-MM-dd HH:mm
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			//yyyy-MM-dd
			SimpleDateFormat simpleDateFormats = new SimpleDateFormat("yyyy-MM-dd");
			for (ManualApprovalList  manualApproval:manualApprovalList.getData()) {
				ApprovalListVo approvalListVo = new ApprovalListVo();
				//投保单号
				approvalListVo.setApplNo(manualApproval.getApplNo());
				//投保人姓名
				approvalListVo.setSgName(manualApproval.getSgName());
				//销售机构代码
				approvalListVo.setSalesBranchNo(manualApproval.getSalesBranchNo());
				//首个主险种代码
				approvalListVo.setPolCode(manualApproval.getPolCode());
				//新单受理时间
				if(manualApproval.getAcceptDate() != null){
					approvalListVo.setAcceptDate(simpleDateFormat.format(manualApproval.getAcceptDate()));
				}
				//生效时间
				if(manualApproval.getSignDate() != null){
					approvalListVo.setSignDate(simpleDateFormat.format(manualApproval.getSignDate()));
				}
				//生效时间
				if(manualApproval.getInForceDate() != null){
					approvalListVo.setInForceDate(simpleDateFormats.format(manualApproval.getInForceDate()));
				}
				//审批类型
				if("1".equals(manualApproval.getRuleTye())){
					approvalListVo.setRuleTye("生效日审批");
				}
				//进入人工审批原因
				approvalListVo.setReason(manualApproval.getReason());
				//审批结果
				if("1".equals(manualApproval.getResult())){
					approvalListVo.setResult("通过");
				}else{
					approvalListVo.setResult("不通过");
				}
				
				//审批操作人员机构号
				approvalListVo.setPclkBranchNo(manualApproval.getPclkBranchNo());
				//审批操作人员工号
				approvalListVo.setPclkNo(manualApproval.getPclkNo());
				//审批操作人员姓名
				approvalListVo.setPclkName(manualApproval.getPclkName());
				//审批操作时间
				if(manualApproval.getProcDate() != null){
					approvalListVo.setProcDate(simpleDateFormat.format(manualApproval.getProcDate()));
				}
				//审批不通过原因
				approvalListVo.setNote(manualApproval.getNote());
				arrayList.add(approvalListVo);
			}
		}
		return arrayList;
		
	}
}
