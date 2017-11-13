package com.newcore.orbps.web.contractreview;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.Conventions;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.util.SgGrpDataVoToBo;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 清汇复核
 * @author 王鸿林
 *
 * @date 2016年11月8日 下午7:38:52
 * @TODO
 */
@Controller
@RequestMapping("/orbps/contractReview/sg")
public class SgGrpInsurApplReviewController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SgGrpInsurApplReviewController.class);

	@Autowired
	InsurApplServer grpInsurApplServer;
	/**
	 * 提交复核信息
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
		// TaskInfo taskInfo = new TaskInfo();
		// taskInfo.setOperState(sgGrpInsurApplVo.getApprovalState());//页面过来的单子状态
		TraceNode traceNode = new TraceNode();
		traceNode.setPclkBranchNo(sessionModel.getBranchNo());// 机构
		traceNode.setPclkName(sessionModel.getName());// 操作员姓名
		LOGGER.info("操作员工号"+sessionModel.getClerkNo()+  "操作员姓名"+sessionModel.getName());
		traceNode.setPclkNo(sessionModel.getClerkNo());//
		traceNode.setProcStat(sgGrpInsurApplVo.getApprovalState());//清汇符合,从页面过来的key
		SgGrpDataVoToBo VoToBo = new SgGrpDataVoToBo(); 
		GrpInsurApplPara sggrpInsurApplPara = new GrpInsurApplPara();
		sggrpInsurApplPara.setGrpInsurAppl(VoToBo.grpInsurApplVoToBo(sgGrpInsurApplVo));
		sggrpInsurApplPara.setTraceNode(traceNode);
		sggrpInsurApplPara.setTaskId(sgGrpInsurApplVo.getTaskInfo().getTaskId());//
		return grpInsurApplServer.infoCheck(sggrpInsurApplPara);
	}
}
