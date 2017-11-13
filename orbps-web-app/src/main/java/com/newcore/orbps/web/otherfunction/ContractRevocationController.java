package com.newcore.orbps.web.otherfunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.BackResult;
import com.newcore.orbps.models.service.bo.UndoResult;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ContractRevokeVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ContractStateBackVo;
import com.newcore.orbps.models.web.vo.otherfunction.manualapproval.ApprovalVo;
import com.newcore.orbps.service.api.ApplStateUndoService;
import com.newcore.orbps.service.api.CommonQueryService;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.web.util.BranchNoUtils;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 契约撤销界面
 * 
 * @author wangyanjie
 *
 */
@Controller
@RequestMapping("/orbps/otherfunction/revoke")
public class ContractRevocationController {

    @Autowired
    ApplStateUndoService applStateUndoService;

    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    ContractQueryService contractQueryService;

    @Autowired
    CommonQueryService commonQueryService;

    @Autowired
    BranchService branchService;
    /**
     * 查询契约撤销信息
     * 
     * @author wangyanjie
     * @date 2016年9月22日
     */

    @RequestMapping(value = "/revokequery")
    // @RequiresPermissions("orbps:contractRevocation:query")
    public @ResponseMessage DataTable<QueryinfVo> getCorreList(@CurrentSession Session session, QueryDt query,
            CorrectionVo correctionVo) {
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        String mgrBranchNo = correctionVo.getCorrectSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
        	correctionVo.setCorrectSalesBranchNo(sessionModel.getBranchNo());
        }else if(!StringUtils.equals(mgrBranchNo,sessionModel.getBranchNo())){
            //销售机构条件控制
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            Branch branch = branchService.findSubBranchAll(sessionModel.getBranchNo());
            List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
            //校验输入的机构号是否是操作员机构的下级机构号
            boolean b = false;
            for(String branchNo:branchNos){
            	if(StringUtils.equals(mgrBranchNo,branchNo)){
            		b = true;
            		break;
            	}
            }
            if (!b) {
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(correctionVo.getFindSubBranchNoFlag())){
        	correctionVo.setFindSubBranchNoFlag("Y");
        }
        /** 构建PageQuery对象 **/
        PageQuery<CorrectionVo> pageQuery = pageQueryService.tranToPageQuery(query, correctionVo);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<QueryinfVo> result = contractQueryService.query(pageQuery);
        return pageQueryService.tranToDataTable(query.getRequestId(), result);
    }
    //撤销控制
    @RequestMapping(value = "/submit")
    // @RequiresPermissions("orbps:contractRevocation:btnrevoke")
    public @ResponseMessage QueryinfVo revo(@CurrentSession Session session, @RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        TraceNodePra traceNodePra = new TraceNodePra();
        traceNodePra.setApplNo(applNo);
        TraceNode traceNode = new TraceNode();
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNodePra.setTraceNode(traceNode);
        RetInfo flag = applStateUndoService.undo(traceNodePra);
        QueryinfVo queryinfVo = new QueryinfVo();
        queryinfVo.setApplNo(flag.getRetCode());
        return queryinfVo;
    }

    /**
     * 契约撤销清单查询
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/search")
    public @ResponseMessage DataTable<ContractRevokeVo> getManualVoResult(
            @CurrentSession Session session,QueryDt query, ApprovalVo approvalVo)
            throws Exception {
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = approvalVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
        	approvalVo.setSalesBranchNo(sessionProvinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String salesProvinceBranchNo = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if(!StringUtils.equals(sessionProvinceBranchNo, salesProvinceBranchNo)){
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(approvalVo.getLevelFlag())){
        	approvalVo.setLevelFlag("Y");
        }
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        PageQuery<ApprovalVo> pageQuery = pageQueryService.tranToPageQuery(query);
        pageQuery.setCondition(approvalVo);
        // 调用后台commonQueryService.manualApproval方法
        PageData<UndoResult> undoResults = commonQueryService.queryForUndo(pageQuery);
        // 创建PageData
        PageData<ContractRevokeVo> pageData = new PageData<ContractRevokeVo>();
        // 创建arrayList，接收数据
        List<ContractRevokeVo> contractRevokeVos = new ArrayList<ContractRevokeVo>();
        // yyyy-MM-dd HH:mm
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(undoResults.getData().size()>0){
            for (UndoResult undoResult : undoResults.getData()) {
                ContractRevokeVo contractRevokeVo = new ContractRevokeVo();
                // 投保单号
                contractRevokeVo.setApplNo(undoResult.getApplNo());
                // 销售机构代码
                contractRevokeVo.setSalesBranchNo(undoResult.getSaleBranchNo());
                // 撤销前保单任务环节（保单状态）
                contractRevokeVo.setApplState(undoResult.getPreStat());
                // 契约撤销人员姓名
                contractRevokeVo.setPclkName(undoResult.getPclkName());
                // 投保人（汇交人）姓名
                contractRevokeVo.setApplName(undoResult.getHldrName());
                // 契约撤销时间
                if (undoResult.getPreStat() != null) {
                    contractRevokeVo.setPclkDate(simpleDateFormat.format(undoResult.getPclkDate()));
                }
                // 契约撤销人员工号
                contractRevokeVo.setPclkNo(undoResult.getPclkNo());
                // 契约撤销操作人员机构号
                contractRevokeVo.setPclkBranchNo(undoResult.getPclkBranchNo());
                contractRevokeVos.add(contractRevokeVo);
            }
        }
		// 当前页号的列表数据
		pageData.setData(contractRevokeVos);
		// 总记录数
		pageData.setTotalCount(undoResults.getTotalCount());
        return pageQueryService.tranToDataTable(query.getRequestId(), pageData);
    }

    /**
     * 契约状态回退查询(全量查询)
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/searchAll")
    public @ResponseMessage List<ContractRevokeVo> getManualVoResultAll(@CurrentSession Session session,@RequestBody ApprovalVo approvalVo)
            throws Exception {
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = approvalVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
        	approvalVo.setSalesBranchNo(sessionProvinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String salesProvinceBranchNo = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if(!StringUtils.equals(sessionProvinceBranchNo, salesProvinceBranchNo)){
            	throw new BusinessException("0004", "导出失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(approvalVo.getLevelFlag())){
        	approvalVo.setLevelFlag("Y");
        }
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        // 调用后台commonQueryService.queryAllForBack方法
        List<UndoResult> undoResults = commonQueryService.queryAllForUndo(approvalVo);
        // 创建arrayList，接收数据
        List<ContractRevokeVo> contractRevokeVos = new ArrayList<ContractRevokeVo>();
        if (null != undoResults) {
            // yyyy-MM-dd HH:mm
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (UndoResult undoResult : undoResults) {
                ContractRevokeVo contractRevokeVo = new ContractRevokeVo();
                // 投保单号
                contractRevokeVo.setApplNo(undoResult.getApplNo());
                // 销售机构代码
                contractRevokeVo.setSalesBranchNo(undoResult.getSaleBranchNo());
                // 撤销前保单任务环节（保单状态）
                contractRevokeVo.setApplState(undoResult.getPreStat());
                // 契约撤销人员姓名
                contractRevokeVo.setPclkName(undoResult.getPclkName());
                // 投保人（汇交人）姓名
                contractRevokeVo.setApplName(undoResult.getHldrName());
                // 契约撤销时间
                if (undoResult.getPreStat() != null) {
                    contractRevokeVo.setPclkDate(simpleDateFormat.format(undoResult.getPclkDate()));
                }
                // 契约撤销人员工号
                contractRevokeVo.setPclkNo(undoResult.getPclkNo());
                // 契约撤销操作人员机构号
                contractRevokeVo.setPclkBranchNo(undoResult.getPclkBranchNo());
                contractRevokeVos.add(contractRevokeVo);
            }
        }
        return contractRevokeVos;
    }

    /**
     * 契约状态回退查询
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/back")
    public @ResponseMessage List<ContractStateBackVo> contractBackQuery(
            @CurrentSession Session session, @RequestBody ApprovalVo approvalVo)
            throws Exception {
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = approvalVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
        	approvalVo.setSalesBranchNo(sessionProvinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String salesProvinceBranchNo = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if(!StringUtils.equals(sessionProvinceBranchNo, salesProvinceBranchNo)){
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(approvalVo.getLevelFlag())){
        	approvalVo.setLevelFlag("Y");
        }
        // 调用后台commonQueryService.queryForBack方法
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        List<BackResult> backResults = commonQueryService.queryAllForBack(approvalVo);
    	// 创建arrayList，接收数据
    	List<ContractStateBackVo> arrayList = new ArrayList<ContractStateBackVo>();
		// yyyy-MM-dd HH:mm
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(backResults != null && backResults.size()>0){
        	for(int i=0; i<backResults.size(); i++){
        		ContractStateBackVo contractStateBackVo = new ContractStateBackVo();
        		if(null != backResults.get(i)){
        			// 投保单号
        			contractStateBackVo.setApplNo(backResults.get(i).getApplNo());
        			// 销售机构代码
        			contractStateBackVo.setSalesBranchNo(backResults.get(i).getSaleBranchNo());
        			// 目前保单所处任务环节
        			contractStateBackVo.setApplState(backResults.get(i).getStat());
        			// 回退原因
        			contractStateBackVo.setCnBackReason(backResults.get(i).getBackReason());
        			if (backResults.get(i).getPclkDate() != null) {
        				// 契约回退操作时间
        				contractStateBackVo.setCnDate(simpleDateFormat.format(backResults.get(i).getPclkDate()));
        			}
        			// 回退操作员姓名
        			contractStateBackVo.setCnName(backResults.get(i).getPclkName());
        			// 回退操作员工号
        			contractStateBackVo.setCnNo(backResults.get(i).getPclkNo());
        			// 回退前保单任务环节（保单状态）
        			contractStateBackVo.setPreApplState(backResults.get(i).getPreStat());
        			// 回退操作员机构号
        			contractStateBackVo.setCnBranchNo(backResults.get(i).getPclkBranchNo());
        			// 投保人（汇交人）姓名
        			contractStateBackVo.setApplName(backResults.get(i).getHldrName());
        			arrayList.add(contractStateBackVo);
        		}
        	}
        }
        return arrayList;
    }

    /**
     * 契约状态回退查询(全量查询)
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/backAll")
    public @ResponseMessage List<ContractStateBackVo> contractBackExcelQuery(@CurrentSession Session session,@RequestBody ApprovalVo approvalVo)
            throws Exception {
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String sessionProvinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = approvalVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
        	approvalVo.setSalesBranchNo(sessionProvinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String salesProvinceBranchNo = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if(!StringUtils.equals(sessionProvinceBranchNo, salesProvinceBranchNo)){
            	throw new BusinessException("0004", "导出失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(approvalVo.getLevelFlag())){
        	approvalVo.setLevelFlag("Y");
        }
    	
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        // 调用后台commonQueryService.queryAllForBack方法
        List<BackResult> backResults = commonQueryService.queryAllForBack(approvalVo);
        // 创建arrayList，接收数据
        List<ContractStateBackVo> arrayList = new ArrayList<ContractStateBackVo>();
        if (null != backResults) {
            // yyyy-MM-dd HH:mm
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            for (BackResult backResult : backResults) {
                ContractStateBackVo contractStateBackVo = new ContractStateBackVo();
                // 投保单号
                contractStateBackVo.setApplNo(backResult.getApplNo());
                // 销售机构代码
                contractStateBackVo.setSalesBranchNo(backResult.getSaleBranchNo());
                // 目前保单所处任务环节
                contractStateBackVo.setApplState(backResult.getStat());
                // 回退原因
                contractStateBackVo.setCnBackReason(backResult.getBackReason());
                if (backResult.getPclkDate() != null) {
                    // 契约回退操作时间
                    contractStateBackVo.setCnDate(simpleDateFormat.format(backResult.getPclkDate()));
                }
                // 回退操作员姓名
                contractStateBackVo.setCnName(backResult.getPclkName());
                // 回退操作员工号
                contractStateBackVo.setCnNo(backResult.getPclkNo());
                // 回退前保单任务环节（保单状态）
                contractStateBackVo.setPreApplState(backResult.getPreStat());
                // 回退操作员机构号
                contractStateBackVo.setCnBranchNo(backResult.getPclkBranchNo());
                // 投保人（汇交人）姓名
                contractStateBackVo.setApplName(backResult.getHldrName());
                arrayList.add(contractStateBackVo);
            }
        }
        return arrayList;
    }
}
