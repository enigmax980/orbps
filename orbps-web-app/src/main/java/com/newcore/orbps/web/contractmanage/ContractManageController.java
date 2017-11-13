package com.newcore.orbps.web.contractmanage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.BeanUtils;
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
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.InsurApplApprovalRule;
import com.newcore.orbps.models.web.vo.contractmanage.contractconfig.ContractOperatingTrackVo;
import com.newcore.orbps.models.web.vo.contractmanage.parent.ContractManageVo;
import com.newcore.orbps.models.web.vo.contractmanage.parent.RuleQueryVo;
import com.newcore.orbps.models.web.vo.contractmanage.parent.ExamineRulesDetailViewVo;
import com.newcore.orbps.service.api.InsurApplApprovalRuleConfigService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.web.util.BranchNoUtils;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 合同管理
 * 
 * @author xiaoYe
 *
 */
@Controller
@RequestMapping("/orbps/contractManage/rule")
public class ContractManageController {

    @Autowired
    InsurApplServices insurApplServices;

    @Autowired
    InsurApplApprovalRuleConfigService insurApplApprovalRuleConfigServiceImpl;

    @Autowired
    InsurApplServer grpInsurApplServer;

    @Autowired
    BranchService branchService;
    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;

    /**
     * 提交规则信息
     * 
     * @author xiaoYe
     * @param contractManageVo
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/submit")
    // @RequiresPermissions("orbps:ruleAdd:submit")
    public @ResponseMessage RetInfo ruleInsurAppl(@CurrentSession Session session,
            @RequestBody ContractManageVo contractManageVo) throws ParseException {
    	RetInfo retInfo = new RetInfo();
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        //获取提交条件中的机构号
        String mgrBranchNo = "";
        if(contractManageVo!=null && contractManageVo.getRuleAddVo()!=null){
        	mgrBranchNo = contractManageVo.getRuleAddVo().getBranchNo();
        }
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(!StringUtils.equals(sessionModel.getBranchNo(), mgrBranchNo)){
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            Branch branch = branchService.findSubBranchAll(sessionModel.getBranchNo());
        	List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
        	boolean b = false;
        	for(String branchNo:branchNos){
        		if(StringUtils.equals(branchNo, mgrBranchNo)){
        			b = true;
        			break;
        		}
        	}
        	if(!b){
        		retInfo.setRetCode("0004");
                retInfo.setErrMsg("提交失败，该操作员只能配置本级及下级机构的规则");
                return retInfo;
        	}
        }
    	
        InsurApplApprovalRule insurApplApprovalRule = new InsurApplApprovalRule();
        insurApplApprovalRule.setOperBranchNo(sessionModel.getBranchNo());
        insurApplApprovalRule.setOperClerkName(sessionModel.getName());
        insurApplApprovalRule.setOperClerkNo(sessionModel.getClerkNo());
        insurApplApprovalRule.setOperState("I");
        Date StartDate = new Date();// 生效日期
        Date EndDate = new Date();// 失效日期
        if(null != contractManageVo){
        	if (null != contractManageVo.getExamineRuleVo()) {
                // 销售渠道
                insurApplApprovalRule.setSalesChannel(contractManageVo.getExamineRuleVo().getSalesChannel());
                // 销售机构代码
                insurApplApprovalRule.setSalesBranchNo(contractManageVo.getExamineRuleVo().getBranchCode());
                // 业务场景
                insurApplApprovalRule.setScene(contractManageVo.getExamineRuleVo().getScene());
                // 契约形式
                insurApplApprovalRule.setCntrForm(contractManageVo.getExamineRuleVo().getCntrForm());
                // 产品类型
                // insurApplApprovalRule.setProductType(contractManageVo.getExamineRuleVo().getProductType());
                // 是否自动审批
                insurApplApprovalRule.setAutoApproveFlag(contractManageVo.getExamineRuleVo().getAutoApproveFlag());
                // 人工审批
                insurApplApprovalRule
                        .setArtificialApproveFlag(contractManageVo.getExamineRuleVo().getArtificialApproveFlag());
                // 生效日往后指定天数
                insurApplApprovalRule.setBeforeEffectiveDate(contractManageVo.getExamineRuleVo().getBeforeEffectiveDate());
                // 生效日往前追溯天数
                insurApplApprovalRule.setAfterEffectiveDate(contractManageVo.getExamineRuleVo().getAfterEffectiveDate());
                // 生效日往前追溯跨越日期
                insurApplApprovalRule
                        .setEffectiveDateBackAcross(contractManageVo.getExamineRuleVo().getEffectiveDateBackAcross());
            }
            if (contractManageVo.getRuleAddVo() != null) {
                // 管理机构号
                if (contractManageVo.getRuleAddVo().getBranchNo() != null) {
                    // 销售机构代码
                	insurApplApprovalRule.setMgrBranchNo(contractManageVo.getRuleAddVo().getBranchNo());
                }
                // 规则状态
                insurApplApprovalRule.setRuleState(contractManageVo.getRuleAddVo().getRuleState());
                // 规则名称
                insurApplApprovalRule.setRuleName(contractManageVo.getRuleAddVo().getRuleName());
                // 规则类型
                insurApplApprovalRule.setRuleType(contractManageVo.getRuleAddVo().getRuleType());
                // 规则变化原因
                insurApplApprovalRule.setRuleChangeReason(contractManageVo.getRuleAddVo().getRuleChangeReason());
                // 启用日期未定义？
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                if (contractManageVo.getExamineRuleVo().getValidDate() != null
                        && contractManageVo.getExamineRuleVo().getValidDate().length() > 0) {
                    StartDate = format.parse(contractManageVo.getExamineRuleVo().getValidDate());
                    insurApplApprovalRule.setStartDate(StartDate);
                }
                // 停用日期
                String InvalidDate = contractManageVo.getExamineRuleVo().getInvalidDate();
                if (InvalidDate != null && InvalidDate.length() != 0) {
                    EndDate = format.parse(contractManageVo.getExamineRuleVo().getInvalidDate());
                    insurApplApprovalRule.setEndDate(EndDate);
                } else {
                    // 如果页面不输入失效日期，默认送这个日期
                    String endDate = "9999-12-31";
                    insurApplApprovalRule.setEndDate(format.parse(endDate));
                }
            }
        }

        // 调用服务
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        retInfo = insurApplApprovalRuleConfigServiceImpl.addRule(insurApplApprovalRule);
        return retInfo;

    }

    /**
     * 修改规则信息
     * 
     * @author xiaoYe
     * @param query
     * @param ruleQueryVo
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/update")
    // @RequiresPermissions("orbps:ruleQuery:update")
    public @ResponseMessage RetInfo update(@CurrentSession Session session,
            @RequestBody ContractManageVo contractManageVo) throws ParseException {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        InsurApplApprovalRule insurApplApprovalRule = new InsurApplApprovalRule();
        insurApplApprovalRule.setOperBranchNo(sessionModel.getBranchNo());
        insurApplApprovalRule.setOperClerkName(sessionModel.getName());
        insurApplApprovalRule.setOperClerkNo(sessionModel.getClerkNo());
        insurApplApprovalRule.setOperState("U");
        if (contractManageVo.getExamineRulesDetailViewVo() != null) {
            // id
            insurApplApprovalRule.setId(contractManageVo.getExamineRulesDetailViewVo().getId());
            // 管理机构号
            insurApplApprovalRule.setMgrBranchNo(contractManageVo.getExamineRulesDetailViewVo().getMgrBranchNo());
            // 规则类型
            insurApplApprovalRule.setRuleType(contractManageVo.getExamineRulesDetailViewVo().getRuleType());
            // 规则名称
            insurApplApprovalRule.setRuleName(contractManageVo.getExamineRulesDetailViewVo().getRuleName());
            // 销售渠道
            insurApplApprovalRule.setSalesChannel(contractManageVo.getExamineRulesDetailViewVo().getSalesChannel());
            // 销售机构代码
            // insurApplApprovalRule.setSalesBranchNo(contractManageVo.getExamineRulesDetailViewVo().getBranchNo());
            // 业务场景
            insurApplApprovalRule.setScene(contractManageVo.getExamineRulesDetailViewVo().getScene());
            // 规则变化原因
            insurApplApprovalRule
                    .setRuleChangeReason(contractManageVo.getExamineRulesDetailViewVo().getRuleChangeReason());
            // 契约形式
            insurApplApprovalRule.setCntrForm(contractManageVo.getExamineRulesDetailViewVo().getCntrForm());
            // 产品类型
            // insurApplApprovalRule.setProductType(contractManageVo.getExamineRulesDetailViewVo().getProductType());
            // 人工审批
            insurApplApprovalRule.setArtificialApproveFlag(
                    contractManageVo.getExamineRulesDetailViewVo().getArtificialApproveFlag());
            // 生效日往前追溯天数
            insurApplApprovalRule
                    .setBeforeEffectiveDate(contractManageVo.getExamineRulesDetailViewVo().getBeforeEffectiveDate());
            // 生效日往后追溯天数
            insurApplApprovalRule
                    .setAfterEffectiveDate(contractManageVo.getExamineRulesDetailViewVo().getAfterEffectiveDate());
            // 生效日往前追溯跨越日期
            insurApplApprovalRule.setEffectiveDateBackAcross(
                    contractManageVo.getExamineRulesDetailViewVo().getEffectiveDateBackAcross());
            // 启用日期
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            insurApplApprovalRule
                    .setStartDate(format.parse(contractManageVo.getExamineRulesDetailViewVo().getValidDate()));
            // 停用日期
            String InvalidDate = contractManageVo.getExamineRulesDetailViewVo().getInvalidDate();
            System.out.println(contractManageVo.getExamineRulesDetailViewVo().getInvalidDate());
            if (InvalidDate != null && InvalidDate.length() != 0) {
                insurApplApprovalRule
                        .setEndDate(format.parse(contractManageVo.getExamineRulesDetailViewVo().getInvalidDate()));
            } else {
                String endDate = "9999-12-31";
                insurApplApprovalRule.setEndDate(format.parse(endDate));
            }
            // 审核意见
            insurApplApprovalRule.setAuditStatus(contractManageVo.getExamineRulesDetailViewVo().getAuditStatus());

        }
        return insurApplApprovalRuleConfigServiceImpl.updateRule(insurApplApprovalRule);
    }

    /**
     * 查询规则信息
     * 
     * @author xiaoYe
     * @param query
     * @param ruleQueryVo
     * @return
     */
    @RequestMapping(value = "/query")
    // @RequiresPermissions("orbps:ruleQuery:query")
    public @ResponseMessage DataTable<RuleQueryVo> getQueryRuleSummaryList(@CurrentSession Session session,
            QueryDt query, RuleQueryVo ruleQueryVo) {
        InsurApplApprovalRule insurApplApprovalRule = new InsurApplApprovalRule();
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        //获取查询条件中的机构号
        String mgrBranchNo = ruleQueryVo.getMgrBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isBlank(mgrBranchNo)||StringUtils.equals(sessionModel.getBranchNo(), mgrBranchNo)){
            ruleQueryVo.setMgrBranchNo(sessionModel.getBranchNo());
        }else{
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            Branch branch = branchService.findSubBranchAll(sessionModel.getBranchNo());
        	List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
        	boolean b = false;
        	for(String branchNo:branchNos){
        		if(StringUtils.equals(branchNo, mgrBranchNo)){
        			b = true;
        			break;
        		}
        	}
        	if(!b){
        		throw new BusinessException("0004", "查询失败，该操作员只能配置本级及下级机构的规则");
        	}
        }
        if(StringUtils.isEmpty(ruleQueryVo.getFindSubBranchNoFlag())){
        	ruleQueryVo.setFindSubBranchNoFlag("Y");
        }
        // vo转bo
        BeanUtils.copyProperties(ruleQueryVo, insurApplApprovalRule);
        insurApplApprovalRule.setMgrBranchNo(ruleQueryVo.getMgrBranchNo());
        
        PageQuery<InsurApplApprovalRule> pageQuery = pageQueryService.tranToPageQuery(query, insurApplApprovalRule);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        // 调用查询方法，
        PageData<InsurApplApprovalRule> queryResult = insurApplApprovalRuleConfigServiceImpl.listPage(pageQuery);
        // 使用.getData()方法
        List<InsurApplApprovalRule> insurApplApprovalRules = queryResult.getData();
        // 分页显示
        PageData<RuleQueryVo> queryRuleQueryVoResult = new PageData<>();
        // bo转vo
        List<RuleQueryVo> ruleQueryVos = new ArrayList<>();
        if (null != insurApplApprovalRules && !insurApplApprovalRules.isEmpty()) {
            for (InsurApplApprovalRule insurApplApprovalRule1 : insurApplApprovalRules) {
                RuleQueryVo ruleQueryVo1 = new RuleQueryVo();
                BeanUtils.copyProperties(insurApplApprovalRule1, ruleQueryVo1);
                if ("1".equals(ruleQueryVo1.getRuleType())) {
                    ruleQueryVo1.setRuleType("人工审批");
                }
                ruleQueryVos.add(ruleQueryVo1);
            }
        }
        // 获取总记录数
        queryRuleQueryVoResult.setTotalCount(queryResult.getTotalCount());
        // 获取当前页号的列表数据
        queryRuleQueryVoResult.setData(ruleQueryVos);
        return pageQueryService.tranToDataTable(query.getRequestId(), queryRuleQueryVoResult);
    }

    /**
     * 查询规则详细信息
     * 
     * @author xiaoYe
     * @param ruleQueryVo
     * @return
     */
    @RequestMapping(value = "/search")
    // @RequiresPermissions("orbps:ruleQuery:search")
    public @ResponseMessage ExamineRulesDetailViewVo search(@CurrentSession Session session,
            @RequestBody RuleQueryVo ruleQueryVo) {
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        //获取查询条件中的机构号
        String mgrBranchNo = ruleQueryVo.getMgrBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isBlank(mgrBranchNo)||StringUtils.equals(sessionModel.getBranchNo(), mgrBranchNo)){
            ruleQueryVo.setMgrBranchNo(sessionModel.getBranchNo());
        }else{
        	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            Branch branch = branchService.findSubBranchAll(sessionModel.getBranchNo());
        	List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
        	boolean b = false;
        	for(String branchNo:branchNos){
        		if(StringUtils.equals(branchNo, mgrBranchNo)){
        			b = true;
        			break;
        		}
        	}
        	if(!b){
        		throw new BusinessException("0004", "查询失败，该操作员只能配置本级及下级机构的规则");
        	}
        }
    	
        if(StringUtils.isEmpty(ruleQueryVo.getFindSubBranchNoFlag())){
        	ruleQueryVo.setFindSubBranchNoFlag("Y");
        }
        InsurApplApprovalRule insurApplApprovalRule = new InsurApplApprovalRule();
        insurApplApprovalRule.setId(ruleQueryVo.getId());
        
        ExamineRulesDetailViewVo examineRulesDetailViewVo = new ExamineRulesDetailViewVo();
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        InsurApplApprovalRule result = insurApplApprovalRuleConfigServiceImpl.queryRule(insurApplApprovalRule);
        // 通过管理机构号进行查询
        // id
        examineRulesDetailViewVo.setId(result.getId());
        // 管理机构号
        examineRulesDetailViewVo.setMgrBranchNo(result.getMgrBranchNo());
        // 规则类型
        examineRulesDetailViewVo.setRuleType(result.getRuleType());
        // 规则名称
        examineRulesDetailViewVo.setRuleName(result.getRuleName());
        // 规则状态
        examineRulesDetailViewVo.setRuleState(result.getRuleState());
        // 启用日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (result.getStartDate() != null) {
            String ValidDate = sdf.format(result.getStartDate());
            examineRulesDetailViewVo.setValidDate(ValidDate);
        }
        if (null != result.getEndDate()) {
            // 停止日期
            String InvalidDate = sdf.format(result.getEndDate());
            examineRulesDetailViewVo.setInvalidDate(InvalidDate);
        }
        // 销售机构代码
        examineRulesDetailViewVo.setBranchNo(result.getSalesBranchNo());
        // 销售渠道
        examineRulesDetailViewVo.setSalesChannel(result.getSalesChannel());
        // 业务场景
        examineRulesDetailViewVo.setScene(result.getScene());
        // 契约形式
        examineRulesDetailViewVo.setCntrForm(result.getCntrForm());
        // 产品类型
        // examineRulesDetailViewVo.setProductType(result.getProductType());
        // 审核意见
        examineRulesDetailViewVo.setAuditStatus(result.getAuditStatus());
        // 是否自动审批
        examineRulesDetailViewVo.setAutoApproveFlag(result.getAutoApproveFlag());
        // 人工审批
        examineRulesDetailViewVo.setArtificialApproveFlag(result.getArtificialApproveFlag());
        // 生效日往后指定天数
        examineRulesDetailViewVo.setBeforeEffectiveDate(result.getBeforeEffectiveDate());
        // 生效日往前指定天数
        examineRulesDetailViewVo.setAfterEffectiveDate(result.getAfterEffectiveDate());
        // 生效日往前追溯跨越日期
        examineRulesDetailViewVo.setEffectiveDateBackAcross(result.getEffectiveDateBackAcross());
        // 生效日往前追溯跨越日期
        examineRulesDetailViewVo.setRuleChangeReason((result.getRuleChangeReason()));
        
        return examineRulesDetailViewVo;
    }

    /**
     * 查询操作轨迹
     * 
     * @author xiaoYe
     * @param query
     * @param contractOperatingTrackVo
     * @return
     */
    @RequestMapping(value = "/accountOperaQuery")
    // @RequiresPermissions("orbps:examineRulesDetailView:accountOperaQuery")
    public @ResponseMessage DataTable<ContractOperatingTrackVo> getOperaAcSummaryList(QueryDt query,
            ContractOperatingTrackVo contractOperatingTrackVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        return pageQueryService.tranToDataTable(query.getRequestId(), null);
    }

    /**
     * 查询操作轨迹
     * 
     * @author xiaoYe
     * @param query
     * @param contractOperatingTrackVo
     * @return
     */
    @RequestMapping(value = "/contractOperaQuery")
    // @RequiresPermissions("orbps:examineRulesDetailView:contractOperaQuery")
    public @ResponseMessage DataTable<ContractOperatingTrackVo> getOperaCoSummaryList(QueryDt query,
            ContractOperatingTrackVo contractOperatingTrackVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        return pageQueryService.tranToDataTable(query.getRequestId(), null);
    }
}