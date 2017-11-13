package com.newcore.orbps.web.contractentry;

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
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry.CoInsurAgreementEntryVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry.CoInsurAgreementInfVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry.CoInsurComVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry.CoInsurCustomerVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceagreemententry.CoInsurVo;
import com.newcore.orbps.service.api.CommonAgreementService;
import com.newcore.orbps.web.util.BranchNoUtils;
import com.newcore.supports.dicts.LEGAL_CODE;
import com.newcore.supports.dicts.NATURE_CODE;
import com.newcore.supports.models.cxf.CxfHeader;

/**
 * 共保錄入
 * 
 * @author CHANG
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/coi")
public class CoinsuranceAgreeController {
    @Autowired
    CommonAgreementService comAgrService;
    @Autowired
    BranchService branchService;

    /**
     * 供保信息
     * 
     * @author chang
     * @param submit
     * @param
     * @return
     */
    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo commonAgreement(@CurrentSession Session session,
            @RequestBody CoInsurAgreementEntryVo coInsurAgreementEntryVo) {
        // 共保协议信息
        CommonAgreement commonAgreement = new CommonAgreement();
        RetInfo retInfo = new RetInfo();
        String branchStr1 = "";
        String branchStr2 = "";
        // 共保协议约定
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getAppAddrCountry())) {
            commonAgreement.setConvention(coInsurAgreementEntryVo.getAppAddrCountry());
        }
        // 共保协议号
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getAgreementInfVo().getAgreementNo())) {
            commonAgreement.setAgreementNo(coInsurAgreementEntryVo.getAgreementInfVo().getAgreementNo());
        }
        // 协议名称
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getAgreementInfVo().getAgreementName())) {
            commonAgreement.setAgreementName(coInsurAgreementEntryVo.getAgreementInfVo().getAgreementName());
        }
        // 管理机构
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getAgreementInfVo().getMgrBranchNo())) {
            commonAgreement.setMgrBranchNo(coInsurAgreementEntryVo.getAgreementInfVo().getMgrBranchNo());
            branchStr1 = coInsurAgreementEntryVo.getAgreementInfVo().getMgrBranchNo();
        }
        // 协议签署日期
        if (coInsurAgreementEntryVo.getAgreementInfVo().getApplDate() != null) {
            commonAgreement.setSignDate(coInsurAgreementEntryVo.getAgreementInfVo().getApplDate());
        }
        // 协议有效开始时间
        if (coInsurAgreementEntryVo.getAgreementInfVo().getTermDate() != null) {
            commonAgreement.setInForceDate(coInsurAgreementEntryVo.getAgreementInfVo().getInforceDate());
        }
        // 交易标致
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getAgreementInfVo().getTransFlag())) {
            commonAgreement.setTransFlag(coInsurAgreementEntryVo.getAgreementInfVo().getTransFlag());
        } else {
            commonAgreement.setTransFlag("A");
        }

        // 协议申请日期
        if (coInsurAgreementEntryVo.getAgreementInfVo().getApplDate() != null) {
            commonAgreement.setApplDate(coInsurAgreementEntryVo.getAgreementInfVo().getApplDate());
        }
        // 协议结束日期
        if (coInsurAgreementEntryVo.getAgreementInfVo().getTermDate() != null) {
            commonAgreement.setTermDate(coInsurAgreementEntryVo.getAgreementInfVo().getTermDate());
        }
        // 客户信息
        GrpHolderInfo comCustomer = new GrpHolderInfo();
        // 客户名称
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getName())) {
            comCustomer.setGrpName(coInsurAgreementEntryVo.getCustomerVo().getName());
        }
        // 证件类别
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getIdType())) {
            comCustomer.setGrpIdType(coInsurAgreementEntryVo.getCustomerVo().getIdType());
        }
        // 证件号码
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getIdNo())) {
            comCustomer.setGrpIdNo(coInsurAgreementEntryVo.getCustomerVo().getIdNo());
        }
        // 行业性质
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getEnterpriseNature())) {
        	NATURE_CODE[] natureCodes = NATURE_CODE.values();
        	LEGAL_CODE[] legalCodes = LEGAL_CODE.values();
        	//判断行业性质是否属于经济分类
        	for(int i=0; i<natureCodes.length; i++){
        		if(natureCodes[i].getKey().equals(coInsurAgreementEntryVo.getCustomerVo().getEnterpriseNature())){
            		//经济分类，赋值行业性质
            		comCustomer.setNatureCode(coInsurAgreementEntryVo.getCustomerVo().getEnterpriseNature());
            	}
        	}
        	//判断行业性质是否属于法律分类
        	for(int i=0; i<legalCodes.length; i++){
        		if(legalCodes[i].getKey().equals(coInsurAgreementEntryVo.getCustomerVo().getEnterpriseNature())){
        			//法律分类，赋值行业性质
            		comCustomer.setLegalCode(coInsurAgreementEntryVo.getCustomerVo().getEnterpriseNature());
            	}
        	}
        }
        // 职业类别
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getOccClassCode())) {
            comCustomer.setOccClassCode(coInsurAgreementEntryVo.getCustomerVo().getOccClassCode());
        }
        // 传真
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getFax())) {
            comCustomer.setFax(coInsurAgreementEntryVo.getCustomerVo().getFax());
        }
        // 联系人姓名
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getContactPsn())) {
            comCustomer.setContactName(coInsurAgreementEntryVo.getCustomerVo().getContactPsn());
        }
        // 联系人电话
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getContactTel())) {
            comCustomer.setContactMobile(coInsurAgreementEntryVo.getCustomerVo().getContactTel());
        }
        // 邮箱
        if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getCustomerVo().getEmail())) {
            comCustomer.setContactEmail(coInsurAgreementEntryVo.getCustomerVo().getEmail());
        }

        // 邮政编码
        Address address = new Address();
        address.setPostCode(coInsurAgreementEntryVo.getCustomerVo().getZipCode());
        // 通讯地址
        address.setProvince(coInsurAgreementEntryVo.getCustomerVo().getProvince());
        address.setCity(coInsurAgreementEntryVo.getCustomerVo().getCity());
        address.setCounty(coInsurAgreementEntryVo.getCustomerVo().getCounty());
        address.setTown(coInsurAgreementEntryVo.getCustomerVo().getTown());
        address.setVillage(coInsurAgreementEntryVo.getCustomerVo().getVillage());
        address.setHomeAddress(coInsurAgreementEntryVo.getCustomerVo().getHomeAddress());
        comCustomer.setAddress(address);

        // 放入CommonAgreement類
        commonAgreement.setComCustomer(comCustomer);

        // 共保公司信息
        List<ComCompany> comCompanyList = new ArrayList<ComCompany>();
        for (int i = 0; i < coInsurAgreementEntryVo.getInsuranceComVos().size(); i++) {
            // 共保公司名称
            ComCompany comCompany = new ComCompany();
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getCompanyName())) {
                comCompany.setCompanyName(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getCompanyName());
            }
            // 是否本公司
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getCompanyFlag())) {
                comCompany.setCompanyFlag(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getCompanyFlag());
            }
            // 共保身份
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getConnIdType())) {
                comCompany.setCoinsurType(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getConnIdType());
            }
            // 共保保费份额占比
            if (coInsurAgreementEntryVo.getInsuranceComVos().get(i).getAmntPct() != null) {
                comCompany.setCoinsurAmntPct(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getAmntPct());
            }
            // 共保责任份额占比
            if (coInsurAgreementEntryVo.getInsuranceComVos().get(i).getResponsibilityPct() != null) {
                comCompany.setCoinsurResponsePct(
                        coInsurAgreementEntryVo.getInsuranceComVos().get(i).getResponsibilityPct());
            }
            // 开户银行
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getBankCode())) {
                comCompany.setBankCode(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getBankCode());
            }
            // 开户名称
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getAccCustName())) {
                comCompany.setBankAccName(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getAccCustName());
            }
            // 银行账号
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getBankAccNo())) {
                comCompany.setBankAccNo(coInsurAgreementEntryVo.getInsuranceComVos().get(i).getBankAccNo());
            }

            comCompanyList.add(comCompany);
        }
        // 放入CommonAgreement類
        commonAgreement.setComCompanies(comCompanyList);

        // 险种信息
        List<Policy> policies = new ArrayList<>();
        for (int i = 0; i < coInsurAgreementEntryVo.getInsuranceVos().size(); i++) {
            Policy policy = new Policy();
            // 险种代码
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceVos().get(i).getPolCode())) {
                policy.setPolCode(coInsurAgreementEntryVo.getInsuranceVos().get(i).getPolCode());
            }
            // 险种名称
            if (StringUtils.isNotEmpty(coInsurAgreementEntryVo.getInsuranceVos().get(i).getPolName())) {
                policy.setPolNameChn(coInsurAgreementEntryVo.getInsuranceVos().get(i).getPolName());
            }
            // 保额
            if (coInsurAgreementEntryVo.getInsuranceVos().get(i).getFaceAmnt() != null) {
                policy.setFaceAmnt(coInsurAgreementEntryVo.getInsuranceVos().get(i).getFaceAmnt());
            }
            // 保费
            if (coInsurAgreementEntryVo.getInsuranceVos().get(i).getPremium() != null) {
                policy.setPremium(coInsurAgreementEntryVo.getInsuranceVos().get(i).getPremium());
            }
            policies.add(policy);
        }
        // 放入commonAgreement類中
        commonAgreement.setPolicies(policies);

        // 调用服务
        // 操作员机构操作员代码 从session中获取
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        commonAgreement.setPclkBranchNo(sessionModel.getBranchNo());
        commonAgreement.setClerkNo(sessionModel.getClerkNo());
        branchStr2 = sessionModel.getBranchNo();
        if (!StringUtils.equals(branchStr1, branchStr2)) {
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            Branch branch = branchService.findSubBranchAll(branchStr2);
            List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
            //校验输入的机构号是否是操作员机构的下级机构号
            boolean b = false;
            for(String branchNo:branchNos){
            	if(StringUtils.equals(branchStr1,branchNo)){
            		b = true;
            		break;
            	}
            }
            if (!b) {
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本级及下级机构的保单");
            }
        }
        //调用服务，插入共保信息
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        retInfo = comAgrService.comAgrSerSubmit(commonAgreement);

        return retInfo;
    }

    /**
     * 共保查询 chang
     * 
     * @throws Exception
     */
    // @chang
    @RequestMapping(value = "/query")
    public @ResponseMessage CoInsurAgreementEntryVo getQueryResult(@CurrentSession Session session,
            @RequestBody CoInsurAgreementInfVo coInsurAgreementInfVo) throws Exception {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String agreementNo = coInsurAgreementInfVo.getAgreementNo();
        // 创建vo对象
        CoInsurAgreementEntryVo coInsurAgreementEntryVo = new CoInsurAgreementEntryVo();
        // 创建bo对象
        CommonAgreement commonAgreement = new CommonAgreement();
        // vo转bo
        commonAgreement.setAgreementNo(agreementNo);
        // 调用服务方法
        RetInfoObject<CommonAgreement> comAgrSerQuery = comAgrService.comAgrSerQuery(commonAgreement);
        //获取session对应的省级机构信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        String branch1 = sessionModel.getBranchNo();
        // 创建-VO对象
        /** 共保协议信息 */
        coInsurAgreementInfVo = new CoInsurAgreementInfVo();
        /** 客户信息 */
        CoInsurCustomerVo coInsurCustomerVo = new CoInsurCustomerVo();
        // 创建-共保公司信息-集合
        List<CoInsurComVo> coInsurComVoList = new ArrayList<CoInsurComVo>();
        // 创建-险种信息-集合
        List<CoInsurVo> CoInsurVoList = new ArrayList<CoInsurVo>();
        /* 根据共保查询，有且只有一条数据 */
    	if (null == comAgrSerQuery.getListObject()
    			|| comAgrSerQuery.getListObject().isEmpty()) {
    		return coInsurAgreementEntryVo;
    	}
        CommonAgreement commonAgreementRet = comAgrSerQuery.getListObject().get(0);
        if (null != commonAgreementRet) {
            // 共保协议信息
            // 共保协议号
            // vo转bo
            coInsurAgreementInfVo.setAgreementNo(commonAgreementRet.getAgreementNo());
            // 协议名称
            coInsurAgreementInfVo.setAgreementName(commonAgreementRet.getAgreementName());
            // 管理机构
            coInsurAgreementInfVo.setMgrBranchNo(commonAgreementRet.getMgrBranchNo());
            //判断查询的共保协议的机构是否与session对应的机构相同
            String branch2 = commonAgreementRet.getMgrBranchNo();
            boolean b = false;
            if (StringUtils.equals(branch1, branch2)) {
            	b = true;
            }
            CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            Branch branch = branchService.findSubBranchAll(branch1);
            List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
            for(String branchNo:branchNos){
            	if(StringUtils.equals(branchNo, branch2)){
            		b = true;
            		break;
            	}
            }
            if(!b){
            	throw new BusinessException("0004", "查询失败，该操作员只能查看本级机构及下级机构的保单");
            }
            // 协议签署日期
            coInsurAgreementInfVo.setApplDate(commonAgreementRet.getApplDate());
            // 协议有效开始时间
            coInsurAgreementInfVo.setInforceDate(commonAgreementRet.getInForceDate());
            // 协议有效结束时间
            coInsurAgreementInfVo.setTermDate(commonAgreementRet.getTermDate());
            if (null != commonAgreementRet.getComCustomer()) {
                // 客户信息
                // 客户名称
                coInsurCustomerVo.setName(commonAgreementRet.getComCustomer().getGrpName());
                // 证件类别
                coInsurCustomerVo.setIdType(commonAgreementRet.getComCustomer().getGrpIdType());
                // 证件号码
                coInsurCustomerVo.setIdNo(commonAgreementRet.getComCustomer().getGrpIdNo());
                // 行业性质
                //如果行业性质为经济分类，则赋值为经济分类的行业
                if(StringUtils.isNotEmpty(commonAgreementRet.getComCustomer().getNatureCode())){
                	coInsurCustomerVo.setEnterpriseNature(commonAgreementRet.getComCustomer().getNatureCode());
                }
                //如果行业性质为法律分类，则赋值为法律分类的行业
                if(StringUtils.isNotEmpty(commonAgreementRet.getComCustomer().getLegalCode())){
                	coInsurCustomerVo.setEnterpriseNature(commonAgreementRet.getComCustomer().getLegalCode());
                }
                // 职业类别
                coInsurCustomerVo.setOccClassCode(commonAgreementRet.getComCustomer().getOccClassCode());
                // 传真
                coInsurCustomerVo.setFax(commonAgreementRet.getComCustomer().getFax());
                // 联系人姓名
                coInsurCustomerVo.setContactPsn(commonAgreementRet.getComCustomer().getContactName());
                // 联系人电话
                coInsurCustomerVo.setContactTel(commonAgreementRet.getComCustomer().getContactMobile());
                // 邮箱
                coInsurCustomerVo.setEmail(commonAgreementRet.getComCustomer().getContactEmail());
                // 通讯地址
                coInsurCustomerVo.setProvince(commonAgreementRet.getComCustomer().getAddress().getProvince());
                coInsurCustomerVo.setCity(commonAgreementRet.getComCustomer().getAddress().getCity());
                coInsurCustomerVo.setCounty(commonAgreementRet.getComCustomer().getAddress().getCounty());
                coInsurCustomerVo.setTown(commonAgreementRet.getComCustomer().getAddress().getTown());
                coInsurCustomerVo.setVillage(commonAgreementRet.getComCustomer().getAddress().getVillage());
                coInsurCustomerVo.setHomeAddress(commonAgreementRet.getComCustomer().getAddress().getHomeAddress());

                // 邮政编码
                if (null != commonAgreementRet.getComCustomer().getAddress()) {
                    coInsurCustomerVo.setZipCode(commonAgreementRet.getComCustomer().getAddress().getPostCode());
                }

                coInsurAgreementEntryVo.setCustomerVo(coInsurCustomerVo);
            }

            // 共保公司信息
            // 共保公司名称
            if (null != commonAgreementRet.getComCompanies() && !commonAgreementRet.getComCompanies().isEmpty()) {
                for (ComCompany comCompany : commonAgreementRet.getComCompanies()) {
                    /** 共保公司信息 */
                    CoInsurComVo coInsurComVo = new CoInsurComVo();
                    coInsurComVo.setCompanyName(comCompany.getCompanyName());
                    // 是否本公司
                    coInsurComVo.setCompanyFlag(comCompany.getCompanyFlag());
                    // 共保身份
                    coInsurComVo.setConnIdType(comCompany.getCoinsurType());
                    // 共保保费份额占比
                    coInsurComVo.setAmntPct(comCompany.getCoinsurAmntPct());
                    // 共保责任份额占比
                    coInsurComVo.setResponsibilityPct(comCompany.getCoinsurResponsePct());
                    // 开户银行
                    coInsurComVo.setBankCode(comCompany.getBankCode());
                    // 开户名称
                    coInsurComVo.setAccCustName(comCompany.getBankAccName());
                    // 银行账号
                    coInsurComVo.setBankAccNo(comCompany.getBankAccNo());

                    coInsurComVoList.add(coInsurComVo);
                }
                coInsurAgreementEntryVo.setInsuranceComVos(coInsurComVoList);
            }
            // 险种信息
            // 险种名称
            if (null != commonAgreementRet.getPolicies() && !commonAgreementRet.getPolicies().isEmpty()) {
                for (Policy policy : commonAgreementRet.getPolicies()) {
                    /** 险种信息 */
                    CoInsurVo coInsurVo = new CoInsurVo();
                    coInsurVo.setPolCode(policy.getPolCode());
                    coInsurVo.setPolName(policy.getPolNameChn());
                    // 保额
                    coInsurVo.setFaceAmnt(policy.getFaceAmnt());
                    // 保费
                    coInsurVo.setPremium(policy.getPremium());
                    CoInsurVoList.add(coInsurVo);
                }
            }
        }
        coInsurAgreementEntryVo.setInsuranceVos(CoInsurVoList);
        coInsurAgreementEntryVo.setAgreementInfVo(coInsurAgreementInfVo);
        if (null != commonAgreementRet) {
            coInsurAgreementEntryVo.setAppAddrCountry(commonAgreementRet.getConvention());
        }
        System.out.println("客户信息+++++++++++++++++++++++" + coInsurAgreementEntryVo.toString());
        return coInsurAgreementEntryVo;
    }

    @RequestMapping(value = "/queryListTb")
    public @ResponseMessage CoInsurComVo getQueryListTb(@RequestBody String agreementNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        // 创建-共保公司信息-集合
        List<CoInsurComVo> coInsurComVoList = new ArrayList<CoInsurComVo>();
        // 创建bo对象
        CommonAgreement commonAgreement = new CommonAgreement();
        commonAgreement.setAgreementNo(agreementNo);
        RetInfoObject<CommonAgreement> comAgrSerQuery = comAgrService.comAgrSerQuery(commonAgreement);

        /** 共保公司信息 */
        CoInsurComVo coInsurComVo = new CoInsurComVo();
        for (int i = 0; i < comAgrSerQuery.getListObject().size(); i++) {
            // 共保公司信息
            // 共保公司名称
            coInsurComVo
                    .setCompanyName(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getCompanyName());
            // 是否本公司
            coInsurComVo
                    .setCompanyName(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getCompanyName());
            // 共保身份
            coInsurComVo.setConnIdType(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getCoinsurType());
            // 共保保费份额占比
            coInsurComVo.setAmntPct(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getCoinsurAmntPct());
            // 共保责任份额占比
            coInsurComVo.setResponsibilityPct(
                    comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getCoinsurResponsePct());
            // 开户银行
            coInsurComVo.setBankCode(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getBankCode());
            // 开户名称
            coInsurComVo
                    .setAccCustName(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getBankAccName());
            // 银行账号
            coInsurComVo.setBankAccNo(comAgrSerQuery.getListObject().get(i).getComCompanies().get(i).getBankAccNo());

            coInsurComVoList.add(coInsurComVo);
        }
        return null;
    }
}