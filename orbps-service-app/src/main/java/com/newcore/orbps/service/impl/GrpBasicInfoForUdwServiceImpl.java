package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.newcore.supports.dicts.PRD_SALES_CHANNEL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.uwbps.GrpHolderInfoVO;
import com.newcore.orbps.models.uwbps.PolicyVO;
import com.newcore.orbps.models.uwbps.PsnListHolderInfoVO;
import com.newcore.orbps.models.uwbps.SalesInfoVO;
import com.newcore.orbps.models.uwbps.SubPolicyVO;
import com.newcore.orbps.models.uwbps.WebGroupVO;
import com.newcore.orbps.service.api.GrpBasicInfoForUdwService;

/**
 * 核保平台接口实现
 *
 * @author jiachenchen
 *         创建时间：2016年8月1日 19:31:00
 */
@Service("grpBasicInfoForUdwService")
public class GrpBasicInfoForUdwServiceImpl implements GrpBasicInfoForUdwService {

    @Autowired
    MongoBaseDao mongoBaseDao;

    @Override
    public WebGroupVO getGrpBasicInfo(Map<String, Object> applNo) {
        WebGroupVO grpBasicInfo = new WebGroupVO();
        if (null == applNo || applNo.isEmpty()) {
            throw new BusinessException("0018", "入参");
        }
        if (StringUtils.isEmpty((String) applNo.get("applNo"))) {
            throw new BusinessException("0018", "入参的投保单号");
        }
        GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, applNo);
        if (grpInsurAppl == null) {
            throw new BusinessException("0018", "查询的团单基本信息");
        }
        //契约形式类型  - G：团单；L：清汇
        String cntrForm = grpInsurAppl.getCntrType();
        //汇交人类型 - 1.个人汇交； 2.单位汇交
        String listType = grpInsurAppl.getSgType();
        //2.给核保平台接口返回类GrpBasicInfo 赋值
        grpBasicInfo = setGrpInsurApplVO(grpInsurAppl, grpBasicInfo);
        /**
         * 当 cntrForm=="G" || （cntrForm=="L" && listType=="2"）  时，给团体客户信息 grpHolderInfoVO 赋值；
         * 当 cntrForm=="L" && listType=="1" 时， 给个人汇交人信息（psnListHolderInfoVO）赋值  ；
         */
        if (StringUtils.equals("G", cntrForm) || (StringUtils.equals("L", cntrForm) && StringUtils.equals("G", listType))) {
            //2.2  团体客户信息 grpHolderInfoVO 赋值；
            GrpHolderInfoVO grpHolderInfoVO = setGrpHolderInfoVO(grpInsurAppl);
            grpBasicInfo.setGrpHolderInfoVO(grpHolderInfoVO);
        } else if (StringUtils.equals("L", cntrForm) && StringUtils.equals("P", listType)) {
            //2.3 给个人汇交人信息（psnListHolderInfoVO）赋值
            PsnListHolderInfoVO psnListHolderInfoVO = setPsnListHolderInfoVO(grpInsurAppl);
            grpBasicInfo.setPsnListHolderInfoVO(psnListHolderInfoVO);
        }
        //2.4 给销售信息（多条）(salesInfoVOList)赋值
        List<SalesInfoVO> salesInfoVOList = setSalesInfoVOList(grpInsurAppl);
        grpBasicInfo.setSalesInfoVOList(salesInfoVOList);
        //2.5 给险种（多条）(policyVOList)赋值
        List<PolicyVO> policyVOList = setPolicyVOList(grpInsurAppl);
        grpBasicInfo.setPolicyVOList(policyVOList);
        return grpBasicInfo;
    }

    /**
     * 给团单基本信息 GrpInsurAppVO 赋值
     *
     * @param grpInsurAppl
     * @return GrpInsurApplVO
     */
    private WebGroupVO setGrpInsurApplVO(GrpInsurAppl grpInsurAppl, WebGroupVO grpBasicInfo) {
        grpBasicInfo.setApplNo(grpInsurAppl.getApplNo());            //投保单号
        grpBasicInfo.setLstProcType(grpInsurAppl.getLstProcType());        //清单标志
        grpBasicInfo.setRenewFlag(grpInsurAppl.getInsurProperty());        //保单性质
        grpBasicInfo.setApproNo(grpInsurAppl.getApproNo());        //团单方案审批号
        grpBasicInfo.setCntrType(grpInsurAppl.getCntrType());        //契约形式
        grpBasicInfo.setListType(grpInsurAppl.getSgType());        //汇交人类型
        grpBasicInfo.setApplDate(grpInsurAppl.getApplDate());        //投保日期
        grpBasicInfo.setAccessSource(grpInsurAppl.getAccessSource()); //投保单来源
        grpBasicInfo.setGiftFlag(grpInsurAppl.getGiftFlag());        //赠送保险标志
        grpBasicInfo.setNotificaStat(grpInsurAppl.getNotificaStat());//是否异常告知
        grpBasicInfo.setSumFaceAmnt(grpInsurAppl.getApplState().getSumFaceAmnt());    //总保额
        grpBasicInfo.setSumPremium(grpInsurAppl.getApplState().getSumPremium());    //总保费
        if (null != grpInsurAppl.getConstructInsurInfo()) {
            grpBasicInfo.setIobjName(grpInsurAppl.getConstructInsurInfo().getIobjName());
            grpBasicInfo.setProjType(grpInsurAppl.getConstructInsurInfo().getProjType());
            grpBasicInfo.setProjLoc(grpInsurAppl.getConstructInsurInfo().getProjLoc());
            grpBasicInfo.setIobjSize(grpInsurAppl.getConstructInsurInfo().getIobjSize());
            grpBasicInfo.setIobjCost(grpInsurAppl.getConstructInsurInfo().getIobjCost());

        }
        grpBasicInfo.setMoneyinItrvl(grpInsurAppl.getPaymentInfo().getMoneyinItrvl());    //缴费方式
        grpBasicInfo.setInForceDate(grpInsurAppl.getInForceDate());//指定生效日期
        grpBasicInfo.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());//管理机构号
        if (grpInsurAppl.getConstructInsurInfo() != null) {
            grpBasicInfo.setIobjName(grpInsurAppl.getConstructInsurInfo().getIobjName());//工程名称
        }
        return grpBasicInfo;
    }

    /**
     * 给团体客户信息 grpHolderInfoVO 赋值
     *
     * @param grpInsurAppl
     * @return grpHolderInfoVO
     */
    private GrpHolderInfoVO setGrpHolderInfoVO(GrpInsurAppl grpInsurAppl) {
        GrpHolderInfoVO grpHolderInfoVO = new GrpHolderInfoVO();
        String grpPartyId = grpInsurAppl.getGrpHolderInfo().getPartyId();
        grpHolderInfoVO.setGrpPartyId(grpPartyId == null ? 0l : Long.parseLong(grpPartyId));
        grpHolderInfoVO.setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());    //团体法人客户号
        grpHolderInfoVO.setGrpName(grpInsurAppl.getGrpHolderInfo().getGrpName());   //单位名称
        grpHolderInfoVO.setGrpIdType(grpInsurAppl.getGrpHolderInfo().getGrpIdType());    //证件类别
        grpHolderInfoVO.setGrpIdNo(grpInsurAppl.getGrpHolderInfo().getGrpIdNo());        //证件号码
        grpHolderInfoVO.setGrpCountryCode(grpInsurAppl.getGrpHolderInfo().getGrpCountryCode()); //企业注册地
        grpHolderInfoVO.setGrpPsnDeptType(grpInsurAppl.getGrpHolderInfo().getGrpPsnDeptType()); //部门类型
        grpHolderInfoVO.setOccClassCode(grpInsurAppl.getGrpHolderInfo().getOccClassCode()); //行业类别
        grpHolderInfoVO.setPostCode(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode()); //邮政编码
        grpHolderInfoVO.setFax(grpInsurAppl.getGrpHolderInfo().getFax());        //传真
        grpHolderInfoVO.setNumOfEnterprise(grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise()); //员工总数
        grpHolderInfoVO.setOnjobStaffNum(grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum()); //在职人数
        grpHolderInfoVO.setIpsnNum(null == grpInsurAppl.getApplState() ? grpInsurAppl.getGrpHolderInfo().getIpsnNum() : grpInsurAppl.getApplState().getIpsnNum());    //投保人数
        grpHolderInfoVO.setContactName(grpInsurAppl.getGrpHolderInfo().getContactName()); //联系人姓名
        grpHolderInfoVO.setContactIdType(grpInsurAppl.getGrpHolderInfo().getContactIdType()); //联系人证件类别
        grpHolderInfoVO.setContactIdNo(grpInsurAppl.getGrpHolderInfo().getContactIdNo());    //联系人证件号码
        grpHolderInfoVO.setContactMobile(grpInsurAppl.getGrpHolderInfo().getContactMobile()); //联系人移动电话
        grpHolderInfoVO.setContactTelephone(grpInsurAppl.getGrpHolderInfo().getContactTelephone()); //联系人固定电话
        grpHolderInfoVO.setContactEmail(grpInsurAppl.getGrpHolderInfo().getContactEmail());    //联系人电子邮件
        grpHolderInfoVO.setProvince(grpInsurAppl.getGrpHolderInfo().getAddress().getProvince());
        grpHolderInfoVO.setCity(grpInsurAppl.getGrpHolderInfo().getAddress().getCity());        //市/州
        grpHolderInfoVO.setCounty(grpInsurAppl.getGrpHolderInfo().getAddress().getCounty());    //区/县
        grpHolderInfoVO.setTown(grpInsurAppl.getGrpHolderInfo().getAddress().getTown());        //镇/乡
        grpHolderInfoVO.setVillage(grpInsurAppl.getGrpHolderInfo().getAddress().getVillage());    //村/社区
        grpHolderInfoVO.setHomeAddress(grpInsurAppl.getGrpHolderInfo().getAddress().getHomeAddress());//地址明细
        return grpHolderInfoVO;
    }

    /**
     * 给个人汇交人信息（psnListHolderInfoVO）赋值
     *
     * @param grpInsurAppl
     * @return psnListHolderInfoVO
     */
    private PsnListHolderInfoVO setPsnListHolderInfoVO(GrpInsurAppl grpInsurAppl) {
        PsnListHolderInfoVO psnListHolderInfoVO = new PsnListHolderInfoVO();
        String sgPartyId = grpInsurAppl.getPsnListHolderInfo().getSgPartyId();
        psnListHolderInfoVO.setSgPartyId(sgPartyId == null ? 0l : Long.parseLong(sgPartyId));
        psnListHolderInfoVO.setSgCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo()); //汇交人客户号
        psnListHolderInfoVO.setSgName(grpInsurAppl.getPsnListHolderInfo().getSgName());        //汇交人姓名
        psnListHolderInfoVO.setSgSex(grpInsurAppl.getPsnListHolderInfo().getSgSex());        //汇交人性别
        psnListHolderInfoVO.setSgBirthDate(grpInsurAppl.getPsnListHolderInfo().getSgBirthDate());    //汇交人出生日期
        psnListHolderInfoVO.setSgIdType(grpInsurAppl.getPsnListHolderInfo().getSgIdType());    //汇交人证件类型
        psnListHolderInfoVO.setSgIdNo(grpInsurAppl.getPsnListHolderInfo().getSgIdNo());        //汇交人证件号码
        psnListHolderInfoVO.setPostCode(grpInsurAppl.getPsnListHolderInfo().getAddress().getPostCode());    //汇交人邮政编码
        psnListHolderInfoVO.setSgMobile(grpInsurAppl.getPsnListHolderInfo().getSgMobile());    //汇交人移动电话
        psnListHolderInfoVO.setSgEmail(grpInsurAppl.getPsnListHolderInfo().getSgEmail());    //汇交人邮箱
        psnListHolderInfoVO.setSgTelephone(grpInsurAppl.getPsnListHolderInfo().getSgTelephone()); //汇交人固定电话
        psnListHolderInfoVO.setFax(grpInsurAppl.getPsnListHolderInfo().getFax());            //传真
        psnListHolderInfoVO.setProvince(grpInsurAppl.getPsnListHolderInfo().getAddress().getProvince());
        psnListHolderInfoVO.setCity(grpInsurAppl.getPsnListHolderInfo().getAddress().getCity());        //市/州
        psnListHolderInfoVO.setCounty(grpInsurAppl.getPsnListHolderInfo().getAddress().getCounty());    //区/县
        psnListHolderInfoVO.setTown(grpInsurAppl.getPsnListHolderInfo().getAddress().getTown());        //镇/乡
        psnListHolderInfoVO.setVillage(grpInsurAppl.getPsnListHolderInfo().getAddress().getVillage());    //村/社区
        psnListHolderInfoVO.setHomeAddress(grpInsurAppl.getPsnListHolderInfo().getAddress().getHomeAddress());        //地址明细
        return psnListHolderInfoVO;
    }

    /**
     * 给销售信息（多条）(salesInfoVOList)赋值
     * 修改by刘帅锋，当银保机构代理的时候，赋值网点信息；当团单机构代理，赋值直销销售员信息。
     *
     * @param grpInsurAppl
     * @return psnListHolderInfoVO
     */
    private List<SalesInfoVO> setSalesInfoVOList(GrpInsurAppl grpInsurAppl) {
        List<SalesInfoVO> SalesInfoVO = new ArrayList<>();
        for (SalesInfo salesInfo : grpInsurAppl.getSalesInfoList()) {
            SalesInfoVO sv = new SalesInfoVO();
            /* 字段名：销售渠道，长度：2，是否必填：否 */
            sv.setSalesChannel(salesInfo.getSalesChannel());
			/* 字段名：销售机构，长度：6，是否必填：是 */
            sv.setSalesBranchNo(salesInfo.getSalesBranchNo());
            //如果是机构代理
            if (StringUtils.equals(salesInfo.getSalesChannel(), PRD_SALES_CHANNEL.OA.getKey())) {
                //如果销售员工号为空，则赋值网点
                if (StringUtils.isEmpty(salesInfo.getSalesNo())) {
			        /* 字段名：销售员工号，长度：8，是否必填：是 */
                    sv.setSalesNo(salesInfo.getSalesDeptNo());
			        /* 字段名：销售员姓名，长度：200，是否必填：是 */
                    sv.setSaleName(salesInfo.getDeptName());
                }else {
                    /* 字段名：销售员工号，长度：8，是否必填：是 */
                    sv.setSalesNo(salesInfo.getSalesNo());
			        /* 字段名：销售员姓名，长度：200，是否必填：是 */
                    sv.setSaleName(salesInfo.getSalesName());
                }

            }else {
                /* 字段名：销售员工号，长度：8，是否必填：是 */
                sv.setSalesNo(salesInfo.getSalesNo());
			    /* 字段名：销售员姓名，长度：200，是否必填：是 */
                sv.setSaleName(salesInfo.getSalesName());
            }

			/*
			 * 字段名：共同展业主副标记，长度：1，是否必填：IF salesDevelopFlag == 1 THEN 非空" 
			 * 1：主销售员；2：附销售员
			 */
            sv.setDevelMainFlag(salesInfo.getDevelMainFlag());
			/* 字段名：展业比例，是否必填：IF salesDevelopFlag == 1 THEN 非空 */
            sv.setDevelopRate(salesInfo.getDevelopRate());
            SalesInfoVO.add(sv);
        }
        return SalesInfoVO;
    }


    /**
     * 给险种（多条）(policyVOList)赋值
     *
     * @param grpInsurAppl
     * @return psnListHolderInfoVO
     */
    private List<PolicyVO> setPolicyVOList(GrpInsurAppl grpInsurAppl) {
        List<PolicyVO> policyVOList = new ArrayList<>();
        for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
            PolicyVO pv = new PolicyVO();
			/* 字段名：险种，长度：8，是否必填：是 */
            pv.setPolCode(policy.getPolCode());
            pv.setPolNameChn(policy.getPolNameChn());
			/* 字段名：保险期间，是否必填：是 */
            pv.setInsurDur(policy.getInsurDur().intValue());
			/*保险期间单位   A:保至年龄   Y：保险年数   W 终身   M保险月数   D 保险天数   O一次性*/
            pv.setInsurDurUnit(policy.getInsurDurUnit());
			/* 字段名：险种保额，是否必填：是 */
            pv.setFaceAmnt(policy.getFaceAmnt());
			/* 字段名：实际保费，是否必填：是 */
            pv.setPremium(policy.getPremium());
			/* 字段名：标准保费，是否必填：是 */
            pv.setStdPremium(policy.getStdPremium());
			/* 字段名：险种投保人数，是否必填：是 */
            pv.setPolIpsnNum(policy.getPolIpsnNum());

            if (grpInsurAppl.getPaymentInfo() != null) {
                if (null != grpInsurAppl.getPaymentInfo().getMoneyinDur()) {
					/*缴费期*/
                    pv.setMoneyinDur(grpInsurAppl.getPaymentInfo().getMoneyinDur().intValue());
                }
				/*缴费期间单位   A 保至年龄  Y 缴费年数*/
                pv.setMoneyinDurUnit(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit());

            }
			
			/*主附险性质    M 主险   R附险（被保人）  E  附险（投保人）*/
            pv.setMrCode(policy.getMrCode());
			/*保费折扣*/
            if (policy.getPremDiscount() != null) {
                pv.setPremDiscount(policy.getPremDiscount());
            }
            if (policy.getSubPolicyList() != null && !policy.getSubPolicyList().isEmpty()) {
                List<SubPolicyVO> subPolicyVOList = new ArrayList<>();
                // 子险种 存在子险种必填
                for (SubPolicy subPolicy : policy.getSubPolicyList()) {
                    SubPolicyVO sv = new SubPolicyVO();
					/* 字段名：子险种，长度：8，是否必填：存在子险种必填 */
                    sv.setSubPolCode(subPolicy.getSubPolCode());
                    sv.setSubPolName(subPolicy.getSubPolName());
					/* 字段名：险种保额，是否必填：存在子险种必填 */
                    sv.setSubPolAmnt(subPolicy.getSubPolAmnt());
					/* 字段名：险种实际保费，是否必填：存在子险种必填 */
                    sv.setSubPremium(subPolicy.getSubPremium());
					/* 字段名：险种标准保费，是否必填：存在子险种必填 */
                    sv.setSubStdPremium(subPolicy.getSubStdPremium());
                    subPolicyVOList.add(sv);
                }
                pv.setSubPolicyList(subPolicyVOList);
            }
            policyVOList.add(pv);
        }
        return policyVOList;
    }

}
