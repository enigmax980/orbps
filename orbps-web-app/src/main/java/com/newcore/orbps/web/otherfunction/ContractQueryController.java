package com.newcore.orbps.web.otherfunction;

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
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.ContractBusiStateQueryVo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 审批表格
 * 
 * @author xiaoYe
 *
 */
@Controller
@RequestMapping("/orbps/otherfunction/contractQuery")
public class ContractQueryController {

    @Autowired
    ContractQueryService contractQueryService;

    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;
    @Autowired
    InsurApplServer grpInsurApplServer;
    @Autowired
    BranchService branchService;
    /**
     * 查询审批表格
     * 
     * @author xiaoYe
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/query")
    public @ResponseMessage DataTable<ContractBusiStateQueryVo> grpInsurAppls(@CurrentSession Session session, QueryDt query,
            ContractBusiStateQueryVo contractBusiStateQueryVo) {
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String provinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = contractBusiStateQueryVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
            contractBusiStateQueryVo.setSalesBranchNo(provinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String provinceBranchNo1 = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if (!StringUtils.equals(provinceBranchNo, provinceBranchNo1)) {
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(contractBusiStateQueryVo.getIsCotainJuniorSaleBranch())){
        	contractBusiStateQueryVo.setIsCotainJuniorSaleBranch("Y");
        }
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        PageQuery<ContractBusiStateQueryVo> pageQuery = pageQueryService.tranToPageQuery(query, contractBusiStateQueryVo);
        return pageQueryService.tranToDataTable(query.getRequestId(), contractQueryService.queryCheck(pageQuery));
    }

    
    /**
     * 查询审批表格(全量查询，为了导出数据)
     * 
     * @throws Exception
     */
    @RequestMapping(value = "/queryAll")
    public @ResponseMessage List<ContractBusiStateQueryVo> getBusiStateVoResultAll(@CurrentSession Session session,@RequestBody ContractBusiStateQueryVo contractBusiStateQueryVo)
            throws Exception {
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String provinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        
        String mgrBranchNo = contractBusiStateQueryVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            contractBusiStateQueryVo.setSalesBranchNo(provinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String provinceBranchNo1 = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if (!StringUtils.equals(provinceBranchNo, provinceBranchNo1)) {
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(contractBusiStateQueryVo.getIsCotainJuniorSaleBranch())){
        	contractBusiStateQueryVo.setIsCotainJuniorSaleBranch("Y");
        }
        //删除任务当前状态入参中的空元素
        if(contractBusiStateQueryVo.getTaskPresentState()!=null){
        	while(contractBusiStateQueryVo.getTaskPresentState().contains("")){
        		contractBusiStateQueryVo.getTaskPresentState().remove("");
        	}
        }
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        // 调用后台commonQueryService.queryAllForBack方法
        List<ContractBusiStateQueryVo> undoResults = contractQueryService.queryAllCheck(contractBusiStateQueryVo);
        return undoResults;
    }
    /**
     * 投保单信息查询
     * 
     * @author chang
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/check")
    public @ResponseMessage GrpInsurApplVo getRuleType(@CurrentSession Session session,@RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpDataBoToVo BoToVo = new GrpDataBoToVo();
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(applNo);
        PageQuery<CorrectionVo> pageQuery = new PageQuery<>();
        CorrectionVo condition = new CorrectionVo();
        condition.setCorrectApplNo(applNo);
        pageQuery.setCondition(condition);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<QueryinfVo> pageData = contractQueryService.query(pageQuery);
        List<QueryinfVo> queryinfVos = pageData.getData();
        String newApplState = null;
        if (queryinfVos != null && !queryinfVos.isEmpty()) {
            newApplState = queryinfVos.get(0).getPostName();
        }
        GrpInsurApplVo grpInsurApplVo = BoToVo.grpinsurApplBoToVo(grpInsurApplBo.getGrpInsurAppl());
        if(grpInsurApplBo.getGrpInsurAppl()!=null){
            String branch1 = grpInsurApplBo.getGrpInsurAppl().getProvBranchNo();
            SessionModel sessionModel = SessionUtils.getSessionModel(session);
            CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String branch2 = branchService.findProvinceBranch(sessionModel.getBranchNo());
            if(!StringUtils.equals(branch1, branch2)){
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省级机构的保单");
            }
        }
        if(grpInsurApplVo != null){
            if (null != grpInsurApplVo.getApplInfoVo()) {
                grpInsurApplVo.getApplInfoVo().setPolNoState(newApplState);
            }
            return grpInsurApplVo;
        }else{
        	return new GrpInsurApplVo();
        }
    }

}
