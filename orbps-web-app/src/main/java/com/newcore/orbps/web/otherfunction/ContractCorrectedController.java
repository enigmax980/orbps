package com.newcore.orbps.web.otherfunction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
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
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.QueryinfVo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.web.util.GrpDataBoToVo;
import com.newcore.orbps.web.util.SgGrpDataBoToVo;
import com.newcore.orbps.web.util.SgGrpDataVoToBo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 订正界面
 * 
 * @author wangyanjie
 *
 */
@Controller
@RequestMapping("/orbps/otherfunction/Correction")
public class ContractCorrectedController {

    @Autowired
    InsurApplServer insurApplServer;
    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    ContractQueryService contractQueryService;
    
    @Autowired
    BranchService branchService;

    /**
     * 查询订正信息
     * 
     * @author wangyanjie
     * @date 2016年9月22日
     */

    @RequestMapping(value = "/correctquery")
    public @ResponseMessage DataTable<QueryinfVo> getCorreList(@CurrentSession Session session, QueryDt query,
            CorrectionVo correctionVo) {
        //获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        //获取session中机构的省级机构
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String provinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        // 如果机构号为空,则取session中机构的省级机构作为省级机构号的入参
        if(StringUtils.isEmpty(correctionVo.getCorrectSalesBranchNo())){
            correctionVo.setCorrectSalesBranchNo(provinceBranchNo);
        }else{
        	 CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
             HeaderInfoHolder.setOutboundHeader(headerInfo1);
             String provinceBranchNo1 = branchService.findProvinceBranch(correctionVo.getCorrectSalesBranchNo());
             if(!StringUtils.equals(provinceBranchNo, provinceBranchNo1)){
            	 throw new BusinessException("0004","操作员只能查询本省机构下信息");
             }
        }
        //默认查询包含下级机构信息
        if(StringUtils.isEmpty(correctionVo.getFindSubBranchNoFlag())){
        	correctionVo.setFindSubBranchNoFlag("Y");
        }
        /** 构建PageQuery对象 **/
        PageQuery<CorrectionVo> pageQuery = pageQueryService.tranToPageQuery(query, correctionVo);
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        PageData<QueryinfVo> result = contractQueryService.query(pageQuery);
        return pageQueryService.tranToDataTable(query.getRequestId(), result);
    }

    /**
     * 订正界面下——提交清汇录入信息
     * 
     * @author wangyanjie
     * @date 2016年9月6日
     */
    @RequestMapping(value = "/sgsubmit")
    // @RequiresPermissions("orbps:correcSgGrpInsurAppl:btnSubmit")
    public @ResponseMessage RetInfo sgInsurAppl(@RequestBody SgGrpInsurApplVo sgGrpInsurApplVo) {
        System.out.println("-------------------sssssssssssssssssssssssss-------------------" + sgGrpInsurApplVo);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SgGrpDataVoToBo voToBo = new SgGrpDataVoToBo();
        GrpInsurAppl grpInsurAppl = voToBo.grpInsurApplVoToBo(sgGrpInsurApplVo);
        grpInsurAppl.setCntrType("L");
        // 设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        return insurApplServer.modifySubmit(grpInsurAppl);
    }

    /**
     * 订正功能下查询清汇投保单信息
     * 
     * @author 王彦杰
     * @date 2016.9.7
     */

    @RequestMapping(value = "/sgquery")
    // @RequiresPermissions("orbps:correctSgGrpInsurAppl:revise")
    public @ResponseMessage SgGrpInsurApplVo sgsearch(@RequestBody QueryinfVo queryinfVo) {

        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
        String applNo = queryinfVo.getApplNo();
        SgGrpInsurApplVo resgGrpInsurApplVo = new SgGrpInsurApplVo();
        // String applNo = queryinfVo.getApplNo();
        // String cntrForm = queryinfVo.getCntrForm();
        GrpInsurApplBo grpInsurApplBo = insurApplServer.searchInsurApplByBusinessKey(applNo);
        SgGrpDataBoToVo BoToVo = new SgGrpDataBoToVo();
        if (grpInsurApplBo.getErrCode().equals("1")) {
            grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
            resgGrpInsurApplVo = BoToVo.sgGrpInsurApplBoToVo(grpInsurAppl);
        }
        return resgGrpInsurApplVo;
    }

    /**
     * 订正功能下提交团单信息
     * 
     * @author wangyanjie
     * @date 2016.9.20
     */
    @RequestMapping(value = "/submit")
    // @RequiresPermissions("orbps:correctGrpInsurApplForm:btnSubmit")
    public @ResponseMessage RetInfo grpInsurAppl(@RequestBody GrpInsurApplVo grpInsurApplVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurAppl grpInsurAppl = this.grpinsurApplVoToBo(grpInsurApplVo);
        return insurApplServer.modifySubmit(grpInsurAppl);
    }

    /**
     * 订正功能下查询团体投保单信息
     * 
     * @author wangyanjie
     * @date 2016.9.20
     */
    @RequestMapping(value = "/query")
    // @RequiresPermissions("orbps:correctGrpInsurApplForm:revise")
    public @ResponseMessage GrpInsurApplVo search(@RequestBody QueryinfVo queryinfVo) {

        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
        String applNo = queryinfVo.getApplNo();
        GrpInsurApplVo regrpInsurApplVo = new GrpInsurApplVo();
        // String applNo = grpInsurApplVo.getApplInfoVo().getApplNo();
        // 设置中国时区，修改日期显示问题
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+08"));
        GrpInsurApplBo grpInsurApplBo = insurApplServer.searchInsurApplByBusinessKey(applNo);
        GrpDataBoToVo BoToVo = new GrpDataBoToVo();
        // System.out.println(JSONObject.toJSONString("==============================="+grpInsurApplBo+"==============================="));
        if (grpInsurApplBo.getErrCode().equals("1")) {
            // System.out.println(grpInsurApplBo.getErrCode());
            // grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
            // regrpInsurApplVo = this.grpinsurApplBoToVo(grpInsurAppl);
            grpInsurAppl = grpInsurApplBo.getGrpInsurAppl();
            regrpInsurApplVo = BoToVo.grpinsurApplBoToVo(grpInsurAppl);
            // regrpInsurApplVo.setTaskInfo(taskInfo);
        }

        // grpInsurApplVo = JSON.parseObject(applContentJson,
        // GrpInsurApplVo.class);
        return regrpInsurApplVo;
    }

    private GrpInsurAppl grpinsurApplVoToBo(GrpInsurApplVo grpInsurApplVo) {
        GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
        List<SalesInfo> salesInfoList = new ArrayList<>();
        GrpHolderInfo grpHolderInfo = new GrpHolderInfo();
        Address address = new Address();
        PaymentInfo paymentInfo = new PaymentInfo();
        List<Date> stlDate = new ArrayList<>();
        ApplState applState = new ApplState();
        HealthInsurInfo healthInsurInfo = new HealthInsurInfo();
        FundInsurInfo fundInsurInfo = new FundInsurInfo();
        ConstructInsurInfo constructInsurInfo = new ConstructInsurInfo();
        List<IpsnStateGrp> ipsnStateGrpList = new ArrayList<>();
        List<Policy> policyList = new ArrayList<>();
        Policy policy = new Policy();
        List<OrgTree> orglist = new ArrayList<>();
        OrgTree orgTree = new OrgTree();
        List<SubPolicy> subPolicyList = new ArrayList<>();
        SubPolicy subPolicy = new SubPolicy();
        grpInsurAppl.setCntrType("G");
        grpInsurAppl.setApplNo(grpInsurApplVo.getApplInfoVo().getApplNo());
        grpInsurAppl.setApproNo(grpInsurApplVo.getApplInfoVo().getQuotaEaNo());
        grpInsurAppl.setApplDate(grpInsurApplVo.getApplInfoVo().getApplDate());
        grpInsurAppl.setAgreementNo(grpInsurApplVo.getApplInfoVo().getAgreementNo());
        grpInsurAppl.setRenewedCgNo(grpInsurApplVo.getApplInfoVo().getOldApplNo());
        grpInsurAppl.setEntChannelFlag(grpInsurApplVo.getApplBaseInfoVo().getBusinessFlag());
        grpInsurAppl.setArgueType(grpInsurApplVo.getApplBaseInfoVo().getDisputePorcWay());
        grpInsurAppl.setUdwType(grpInsurApplVo.getPrintInfoVo().getManualCheck());
        grpInsurAppl.setListPrintType(grpInsurApplVo.getPrintInfoVo().getPrtIpsnLstType());
        grpInsurAppl.setVoucherPrintType(grpInsurApplVo.getPrintInfoVo().getIpsnVoucherPrt());
        grpInsurAppl.setGiftFlag(grpInsurApplVo.getPrintInfoVo().getGiftFlag());
        grpInsurAppl.setNotificaStat(grpInsurApplVo.getPrintInfoVo().getExceptionInform());
        grpInsurAppl.setLstProcType(grpInsurApplVo.getPrintInfoVo().getIpsnlstId());
        grpInsurAppl.setInsurProperty(grpInsurApplVo.getPrintInfoVo().getApplProperty());
        grpInsurAppl.setApplBusiType(grpInsurApplVo.getApplBaseInfoVo().getBusinessFlag());
        for (int i = 0; i < grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().size(); i++) {
            // 销售相关
            SalesInfo salesInfo = new SalesInfo();
            // 销售机构代码
            salesInfo.setSalesBranchNo(
                    grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getSalesBranchNo());
            // 销售渠道
            salesInfo.setSalesChannel(grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getSalesChannel());
            if ("OA".equals(grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getSalesChannel())) {
                // 代理网点号
                salesInfo
                        .setSalesDeptNo(grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getWorksiteNo());
                // 网点名称
                salesInfo.setDeptName(grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getWorksiteName());
            } else {
                // 销售人员代码
                salesInfo.setSalesNo(grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getSaleCode());
                // 销售人员姓名
                salesInfo.setSalesName(grpInsurApplVo.getApplInfoVo().getGrpSalesListFormVos().get(i).getSaleName());
            }
            salesInfoList.add(salesInfo);
        }
        grpHolderInfo.setTaxpayerId(grpInsurApplVo.getVatInfoVo().getTaxpayerCode());
        grpHolderInfo.setGrpName(grpInsurApplVo.getApplBaseInfoVo().getCompanyName());
        grpHolderInfo.setGrpCountryCode(grpInsurApplVo.getApplBaseInfoVo().getRegisterArea());
        grpHolderInfo.setGrpIdType(grpInsurApplVo.getApplBaseInfoVo().getIdType());
        grpHolderInfo.setGrpIdNo(grpInsurApplVo.getApplBaseInfoVo().getIdNo());
        grpHolderInfo.setGrpPsnDeptType(grpInsurApplVo.getApplBaseInfoVo().getDeptType());
        grpHolderInfo.setIpsnNum(grpInsurApplVo.getApplBaseInfoVo().getApplNum());
        grpHolderInfo.setOccClassCode(grpInsurApplVo.getApplBaseInfoVo().getOccDangerFactor());
        grpHolderInfo.setNumOfEnterprise(grpInsurApplVo.getApplBaseInfoVo().getNumOfEmp());
        grpHolderInfo.setOnjobStaffNum(grpInsurApplVo.getApplBaseInfoVo().getOjEmpNum());
        address.setProvince(grpInsurApplVo.getApplBaseInfoVo().getAppAddrProv());
        address.setCity(grpInsurApplVo.getApplBaseInfoVo().getAppAddrCity());
        address.setCounty(grpInsurApplVo.getApplBaseInfoVo().getAppAddrTown());
        address.setTown(grpInsurApplVo.getApplBaseInfoVo().getAppAddrCountry());
        address.setVillage(grpInsurApplVo.getApplBaseInfoVo().getAppAddrValige());
        address.setHomeAddress(grpInsurApplVo.getApplBaseInfoVo().getAppAddrHome());
        address.setPostCode(grpInsurApplVo.getApplBaseInfoVo().getAppPost());
        grpHolderInfo.setAddress(address);
        grpHolderInfo.setContactName(grpInsurApplVo.getApplBaseInfoVo().getConnName());
        grpHolderInfo.setContactIdType(grpInsurApplVo.getApplBaseInfoVo().getConnIdType());
        grpHolderInfo.setContactIdNo(grpInsurApplVo.getApplBaseInfoVo().getConnIdNo());
        // grpHolderInfo.setContactBirthDate(grpInsurApplVo.getApplBaseInfoVo().getAppBirthday());
        // grpHolderInfo.setContactSex(grpInsurApplVo.getApplBaseInfoVo().getAppGender());
        grpHolderInfo.setContactMobile(grpInsurApplVo.getApplBaseInfoVo().getConnPhone());
        grpHolderInfo.setContactEmail(grpInsurApplVo.getApplBaseInfoVo().getConnPostcode());
        grpHolderInfo.setContactTelephone(grpInsurApplVo.getApplBaseInfoVo().getAppHomeTel());
        grpHolderInfo.setFax(grpInsurApplVo.getApplBaseInfoVo().getAppHomeFax());
        paymentInfo.setMoneyinItrvl(grpInsurApplVo.getPayInfoVo().getMoneyinItrvl());
        paymentInfo.setMoneyinType(grpInsurApplVo.getPayInfoVo().getMoneyinType());
        paymentInfo.setPremSource(grpInsurApplVo.getPayInfoVo().getPremFrom());
        paymentInfo.setBankCode(grpInsurApplVo.getPayInfoVo().getBankCode());
        paymentInfo.setBankAccName(grpInsurApplVo.getPayInfoVo().getBankName());
        paymentInfo.setBankAccNo(grpInsurApplVo.getPayInfoVo().getBankAccNo());
        paymentInfo.setStlType(grpInsurApplVo.getPayInfoVo().getStlType());
        paymentInfo.setStlAmnt(grpInsurApplVo.getPayInfoVo().getStlLimit());
        paymentInfo.setStlRate(grpInsurApplVo.getPayInfoVo().getSettlementRatio());
        paymentInfo.setForeExpDate(grpInsurApplVo.getProposalInfoVo().getEnstPremDeadline());
        if (null != grpInsurApplVo.getPayInfoVo().getSettlementDate()) {
            for (int i = 0; i < grpInsurApplVo.getPayInfoVo().getSettlementDate().size(); i++) {
                stlDate.add(grpInsurApplVo.getPayInfoVo().getSettlementDate().get(i));
            }
        }
        paymentInfo.setStlDate(stlDate);
        applState.setDesignForceDate(grpInsurApplVo.getProposalInfoVo().getInForceDate());
        applState.setIpsnNum(grpInsurApplVo.getProposalInfoVo().getIpsnNum());
        applState.setIsFreForce(grpInsurApplVo.getProposalInfoVo().getFrequenceEff());
        applState.setForceFre(grpInsurApplVo.getProposalInfoVo().getForceNum());
        applState.setInforceDateType(grpInsurApplVo.getProposalInfoVo().getForceType());
        applState.setSumPremium(grpInsurApplVo.getProposalInfoVo().getSumPrem());
        Conventions conventions = new Conventions();
        conventions.setPolConv(grpInsurApplVo.getProposalInfoVo().getSpecialPro());
        grpInsurAppl.setConventions(conventions);
        applState.setInsurDurUnit(grpInsurApplVo.getProposalInfoVo().getInsurDurUnit());
        policyList.clear();
        for (int i = 0; i < grpInsurApplVo.getBusiPrdVos().size(); i++) {
            policy.setPolCode(null);
            policy.setInsurDur(null);
            policy.setInsurDurUnit(null);
            policy.setPolIpsnNum(null);
            policy.setFaceAmnt(null);
            policy.setPremium(null);
            String polCode = grpInsurApplVo.getBusiPrdVos().get(i).getBusiPrdCode();
            policy.setPolCode(polCode);
            policy.setInsurDur(grpInsurApplVo.getBusiPrdVos().get(i).getInsurDur());
            policy.setInsurDurUnit(grpInsurApplVo.getBusiPrdVos().get(i).getInsurDurUnit());
            policy.setSpeciBusinessLogo(grpInsurApplVo.getBusiPrdVos().get(i).getHealthInsurFlag());
            policy.setPolIpsnNum(grpInsurApplVo.getBusiPrdVos().get(i).getInsuredNum());
            policy.setFaceAmnt(grpInsurApplVo.getBusiPrdVos().get(i).getAmount());
            policy.setPremium(grpInsurApplVo.getBusiPrdVos().get(i).getPremium());
            for (int j = 0; j < grpInsurApplVo.getResponseVos().size(); j++) {
                if (polCode.equals(grpInsurApplVo.getResponseVos().get(j).getBusiPrdCode())) {
                    subPolicy.setSubPolCode(null);
                    subPolicy.setSubPolAmnt(null);
                    subPolicy.setSubPremium(null);
                    subPolicy.setSubPolCode(grpInsurApplVo.getResponseVos().get(j).getProductCode());
                    subPolicy.setSubPolAmnt(grpInsurApplVo.getResponseVos().get(j).getProductNum());
                    subPolicy.setSubPremium(grpInsurApplVo.getResponseVos().get(j).getProductPremium());
                    subPolicyList.add(subPolicy);
                }
            }
            policy.setSubPolicyList(subPolicyList);
            policyList.add(policy);
        }
        applState.setPolicyList(policyList);
        healthInsurInfo.setComInsurAmntUse(grpInsurApplVo.getSpecialInsurAddInfoVo().getComInsurAmntUse());
        healthInsurInfo.setComInsurAmntType(grpInsurApplVo.getSpecialInsurAddInfoVo().getComInsurAmntType());
        healthInsurInfo.setComInsrPrem(grpInsurApplVo.getSpecialInsurAddInfoVo().getCommPremium());
        healthInsurInfo.setSumFixedAmnt(grpInsurApplVo.getSpecialInsurAddInfoVo().getFixedComAmnt());
        healthInsurInfo.setIpsnFloatAmnt(grpInsurApplVo.getSpecialInsurAddInfoVo().getIpsnFloatAmnt());
        healthInsurInfo.setFloatInverse(grpInsurApplVo.getSpecialInsurAddInfoVo().getIpsnFloatPct());
        fundInsurInfo.setAdminFeeCopuType(grpInsurApplVo.getSpecialInsurAddInfoVo().getAdminCalType());
        fundInsurInfo.setAdminFeePct(grpInsurApplVo.getSpecialInsurAddInfoVo().getAdminPcent());
        fundInsurInfo.setIpsnFundPremium(grpInsurApplVo.getSpecialInsurAddInfoVo().getIpsnAccAmnt());
        fundInsurInfo.setIpsnFundAmnt(grpInsurApplVo.getSpecialInsurAddInfoVo().getInclIpsnAccAmnt());
        fundInsurInfo.setSumFundPremium(grpInsurApplVo.getSpecialInsurAddInfoVo().getSumPubAccAmnt());
        fundInsurInfo.setSumFundAmnt(grpInsurApplVo.getSpecialInsurAddInfoVo().getInclSumPubAccAmnt());
        constructInsurInfo.setIobjName(grpInsurApplVo.getSpecialInsurAddInfoVo().getProjectName());
        constructInsurInfo.setProjLoc(grpInsurApplVo.getSpecialInsurAddInfoVo().getProjectAddr());
        constructInsurInfo.setProjType(grpInsurApplVo.getSpecialInsurAddInfoVo().getProjectType());
        constructInsurInfo.setPremCalType(grpInsurApplVo.getSpecialInsurAddInfoVo().getCpnstMioType());
        constructInsurInfo.setIobjCost(grpInsurApplVo.getSpecialInsurAddInfoVo().getTotalCost());
        constructInsurInfo.setIobjSize(grpInsurApplVo.getSpecialInsurAddInfoVo().getTotalArea());
        constructInsurInfo.setConstructFrom(grpInsurApplVo.getSpecialInsurAddInfoVo().getConstructionDur());
        constructInsurInfo.setConstructTo(grpInsurApplVo.getSpecialInsurAddInfoVo().getUntil());
        List<InsuredGroupModalVo> insuredGroupModalVos = grpInsurApplVo.getInsuredGroupModalVos();
        if (insuredGroupModalVos != null) {
            for (int i = 0; i < insuredGroupModalVos.size(); i++) {
                IpsnStateGrp ipsnStateGrp = new IpsnStateGrp();
                ipsnStateGrp.setIpsnGrpName(insuredGroupModalVos.get(i).getIpsnGrpName());
                ipsnStateGrp.setIpsnGrpNo(insuredGroupModalVos.get(i).getIpsnGrpNo());
                ipsnStateGrp.setIpsnGrpNum(insuredGroupModalVos.get(i).getIpsnGrpNum());
                ipsnStateGrp.setOccClassCode(insuredGroupModalVos.get(i).getOccClassCode());
                ipsnStateGrp.setIpsnOccCode(insuredGroupModalVos.get(i).getIpsnOccSubclsCode());
                ipsnStateGrp.setGenderRadio(insuredGroupModalVos.get(i).getGenderRadio());
                ipsnStateGrp.setGsPct(insuredGroupModalVos.get(i).getGsRate());
                ipsnStateGrp.setSsPct(insuredGroupModalVos.get(i).getSsRate());
                List<GrpPolicy> grpPolicyList = new ArrayList<>();
                if (insuredGroupModalVos.get(i).getInsuranceInfoVos() != null) {
                    for (int j = 0; j < insuredGroupModalVos.get(i).getInsuranceInfoVos().size(); j++) {
                        GrpPolicy grpPolicy = new GrpPolicy();
                        grpPolicy.setPolCode(insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getPolCode());
                        grpPolicy.setFaceAmnt(insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getFaceAmnt());
                        grpPolicy.setPremium(insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getFaceAmnt());
                        grpPolicy.setMrCode(insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getMrCode());
                        grpPolicy.setStdPremium(
                                insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getStdPremium());
                        grpPolicy.setPremRate(insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getPremRate());
                        grpPolicy.setPremDiscount(
                                insuredGroupModalVos.get(i).getInsuranceInfoVos().get(j).getRecDisount());
                        grpPolicyList.add(grpPolicy);
                    }
                    ipsnStateGrp.setGrpPolicyList(grpPolicyList);
                }
                ipsnStateGrpList.add(ipsnStateGrp);
            }
        }
        grpInsurAppl.setIpsnStateGrpList(ipsnStateGrpList);
        if (grpInsurApplVo.getOrganizaHierarModalVos() != null) {
            for (int i = 0; i < grpInsurApplVo.getOrganizaHierarModalVos().size(); i++) {
                Address address2 = new Address();
                GrpHolderInfo grpHolderInfo2 = new GrpHolderInfo();
                grpHolderInfo2.setGrpCustNo(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getCustNo());
                grpHolderInfo2.setGrpName(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getCompanyName());
                grpHolderInfo2.setFormerGrpName(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getOldName());
                grpHolderInfo2.setNatureCode(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getUnitCharacter());
                grpHolderInfo2
                        .setOccClassCode(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getIndustryClassification());
                grpHolderInfo2.setNumOfEnterprise(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getTotalMembers());
                grpHolderInfo2.setGrpIdType(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getDeptType());
                grpHolderInfo2.setGrpIdNo(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getIdCardNo());
                grpHolderInfo2.setOnjobStaffNum(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getOjEmpNum());
                grpHolderInfo2.setIpsnNum(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getApplNum());
                address2.setProvince(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getProvince());
                address2.setCity(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getCity());
                address2.setCounty(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getCounty());
                address2.setTown(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getTown());
                address2.setVillage(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getVillage());
                address2.setHomeAddress(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getDetailAddress());
                address2.setPostCode(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getPostCode());
                grpHolderInfo2.setGrpCountryCode(
                        grpInsurApplVo.getOrganizaHierarModalVos().get(i).getRegisteredNationality());
                grpHolderInfo2.setContactName(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getContactName());
                grpHolderInfo2.setContactMobile(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getPhoneNum());
                grpHolderInfo2.setContactEmail(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getEmail());
                grpHolderInfo2.setContactTelephone(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getFixedPhones());
                orgTree.setIsPaid(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getPay());
                orgTree.setMtnOpt(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getSecurityOptions());
                orgTree.setServiceOpt(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getServiceAssignment());
                orgTree.setReceiptOpt(grpInsurApplVo.getOrganizaHierarModalVos().get(i).getDeptType());

                grpHolderInfo2.setAddress(address2);
                orgTree.setGrpHolderInfo(grpHolderInfo2);
                orglist.add(orgTree);
            }
        }
        grpInsurAppl.setSalesInfoList(salesInfoList);
        grpInsurAppl.setGrpHolderInfo(grpHolderInfo);
        grpInsurAppl.setPaymentInfo(paymentInfo);
        grpInsurAppl.setApplState(applState);
        grpInsurAppl.setHealthInsurInfo(healthInsurInfo);
        grpInsurAppl.setFundInsurInfo(fundInsurInfo);
        grpInsurAppl.setConstructInsurInfo(constructInsurInfo);
        grpInsurAppl.setIpsnStateGrpList(ipsnStateGrpList);
        grpInsurAppl.setOrgTreeList(orglist);
        return grpInsurAppl;
    }

}
