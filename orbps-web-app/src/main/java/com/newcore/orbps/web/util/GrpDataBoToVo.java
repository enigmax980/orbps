package com.newcore.orbps.web.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.ApplState;
import com.newcore.orbps.models.service.bo.grpinsurappl.ConstructInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Conventions;
import com.newcore.orbps.models.service.bo.grpinsurappl.FundInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.HealthInsurInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplBaseInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpBusiPrdVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPayInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPrintInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpProposalInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSalesListFormVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSpecialInsurAddInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpVatInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;

/**
 * 
 * @Description: 团单Bo转Vo
 * @ClassName: GrpDataTransfor
 * @author: jinmeina
 * @date: 2016-12-02 15:52:00
 * 
 */
public class GrpDataBoToVo {

    /**
     * 团单Bo转Vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @return 团单基本信息Vo
     */
    public GrpInsurApplVo grpinsurApplBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        // 团单面向前端大Vo
        GrpInsurApplVo grpInsurApplVo = new GrpInsurApplVo();
        // 契约类型
        grpInsurApplVo.setCntrType(grpInsurAppl.getCntrType());
        // 投保单Bo转Vo
        grpInsurApplVo.setApplInfoVo(applInfoBoToVo(grpInsurAppl));
        // 保单Bo转Vo
        grpInsurApplVo.setApplBaseInfoVo(grpApplBaseInfoBoToVo(grpInsurAppl));
        // 交费Bo转Vo
        grpInsurApplVo.setPayInfoVo(grpPayInfoBoToVo(grpInsurAppl));
        // 打印信息Bo转Vo
        grpInsurApplVo.setPrintInfoVo(grpPrintInfoBoToVo(grpInsurAppl));
        // 要约信息Bo转Vo
        grpInsurApplVo.setProposalInfoVo(grpProposalInfoBoToVo(grpInsurAppl));
        // 特殊险种附加信息Bo转Vo
        grpInsurApplVo.setSpecialInsurAddInfoVo(grpSpecialInsurAddInfoBoToVo(grpInsurAppl));
        // 增值税信息Bo转Vo
        grpInsurApplVo.setVatInfoVo(grpVatInfoBoToVo(grpInsurAppl));
        // 被保人分组信息Bo转Vo
        grpInsurApplVo.setInsuredGroupModalVos(insuredGroupModalBoToVo(grpInsurAppl));
        // 组织层次分组信息Bo转Vo
        grpInsurApplVo.setOrganizaHierarModalVos(organizaHierarModalBoToVo(grpInsurAppl));
        // 险种信息Bo转Vo
        grpInsurApplVo.setBusiPrdVos(grpBusiPrdBoToVo(grpInsurAppl));
        // 收付费分组信息Bo转Vo
        grpInsurApplVo.setChargePayGroupModalVos(chargePayGroupModalBoToVo(grpInsurAppl));// 收付费分组信息
        // 进入人工审批的原因
        if(null != grpInsurAppl.getConventions()){
            grpInsurApplVo.setAprovalReason(grpInsurAppl.getConventions().getInputConv());
        }
        return grpInsurApplVo;
    }

    /**
     * 投保单Bo转vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public GrpApplInfoVo applInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpApplInfoVo applInfoVo = new GrpApplInfoVo();
        // 投保单号
        applInfoVo.setApplNo(grpInsurAppl.getApplNo());
        // 报价审批号
        applInfoVo.setQuotaEaNo(grpInsurAppl.getApproNo());
        // 投保日期
        applInfoVo.setApplDate(grpInsurAppl.getApplDate());
        // 共保协议号
        applInfoVo.setAgreementNo(grpInsurAppl.getAgreementNo());
        // 上期保单号
        applInfoVo.setOldApplNo(grpInsurAppl.getRenewedCgNo());
        //合同号
        applInfoVo.setPolNo(grpInsurAppl.getCgNo());
        //生效日期
        applInfoVo.setEffectDate(grpInsurAppl.getInForceDate());
        // 销售相关信息
        List<SalesInfo> salesInfo = grpInsurAppl.getSalesInfoList();
        //展业信息
        List<GrpSalesListFormVo> grpSalesListFormVos = new ArrayList<GrpSalesListFormVo>();
        if (null != salesInfo && salesInfo.size() > 0) {
            for (int i = 0; i < salesInfo.size(); i++) {
                GrpSalesListFormVo grpSalesListFormVo = new GrpSalesListFormVo();
                // 展业比例
                grpSalesListFormVo.setBusinessPct(salesInfo.get(i).getDevelopRate());
                // 销售渠道
                grpSalesListFormVo.setSalesChannel(salesInfo.get(i).getSalesChannel());
                // 销售机构代码
                grpSalesListFormVo.setSalesBranchNo(salesInfo.get(i).getSalesBranchNo());
                // 销售员姓名
                grpSalesListFormVo.setSaleName(salesInfo.get(i).getSalesName());
                // 销售员工号
                grpSalesListFormVo.setSaleCode(salesInfo.get(i).getSalesNo());
                // 代理网点号
                grpSalesListFormVo.setWorksiteNo(salesInfo.get(i).getSalesDeptNo());
                // 网点名称
                grpSalesListFormVo.setWorksiteName(salesInfo.get(i).getDeptName());
                // 销售员主副标记
                grpSalesListFormVo.setJointFieldWorkFlag(StringUtils.equals("1",
                        salesInfo.get(i).getDevelMainFlag()) ? "Y" : "N");
                //代理员工号
                grpSalesListFormVo.setAgencyNo(salesInfo.get(i).getCommnrPsnNo());
                //代理员姓名
                grpSalesListFormVo.setAgencyName(salesInfo.get(i).getCommnrPsnName());
                grpSalesListFormVos.add(grpSalesListFormVo);
            }
            applInfoVo.setGrpSalesListFormVos(grpSalesListFormVos);
        }
        return applInfoVo;
    }

    /**
     * 保单基本信息Bo转Vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public GrpApplBaseInfoVo grpApplBaseInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpApplBaseInfoVo applBaseInfoVo = new GrpApplBaseInfoVo();
        // 业务标志
        applBaseInfoVo.setBusinessFlag(grpInsurAppl.getApplBusiType());
        // 争议处理方式
        applBaseInfoVo.setDisputePorcWay(grpInsurAppl.getArgueType());
        // 团体客户信息
        GrpHolderInfo grpHolderInfo = grpInsurAppl.getGrpHolderInfo();
        if (null != grpHolderInfo) {
            // 单位、团体名称
            applBaseInfoVo.setCompanyName(grpHolderInfo.getGrpName());
            // 企业注册地
            applBaseInfoVo.setRegisterArea(grpHolderInfo.getGrpCountryCode());
            // 证件类型
            applBaseInfoVo.setIdType(grpHolderInfo.getGrpIdType());
            // 证件号码
            applBaseInfoVo.setIdNo(grpHolderInfo.getGrpIdNo());
            // 部门类型
            applBaseInfoVo.setDeptType(grpHolderInfo.getGrpPsnDeptType());
            // 投保人数
            applBaseInfoVo.setApplNum(grpHolderInfo.getIpsnNum());
            // 联系人姓名
            applBaseInfoVo.setConnName(grpHolderInfo.getContactName());
            // 联系人证件类型
            applBaseInfoVo.setConnIdType(grpHolderInfo.getContactIdType());
            // 联系人证件号码
            applBaseInfoVo.setConnIdNo(grpHolderInfo.getContactIdNo());
            // 联系人移动电话
            applBaseInfoVo.setConnPhone(grpHolderInfo.getContactMobile());
            // 联系人邮箱
            applBaseInfoVo.setConnPostcode(grpHolderInfo.getContactEmail());
            // 固定电话
            applBaseInfoVo.setAppHomeTel(grpHolderInfo.getContactTelephone());
            // 传真号码
            applBaseInfoVo.setAppHomeFax(grpHolderInfo.getFax());
            // 职业类别
            applBaseInfoVo.setOccDangerFactor(grpHolderInfo.getOccClassCode());
            // 员工总数
            applBaseInfoVo.setNumOfEmp(grpHolderInfo.getNumOfEnterprise());
            // 在职人数
            applBaseInfoVo.setOjEmpNum(grpHolderInfo.getOnjobStaffNum());
            // 仲裁机构名称
            applBaseInfoVo.setArbOrgName(grpInsurAppl.getArbitration());
            // 性别
            // applBaseInfoVo.setAppGender("");
            // 出生日期
            // applBaseInfoVo.setAppBirthday(new Date());
            // 地址信息
            Address address = grpHolderInfo.getAddress();
            if (null != address) {
                // 省、直辖市
                applBaseInfoVo.setAppAddrProv(address.getProvince());
                // 市、城区
                applBaseInfoVo.setAppAddrCity(address.getCity());
                // 县、地级市
                applBaseInfoVo.setAppAddrTown(address.getTown());
                // 乡镇
                applBaseInfoVo.setAppAddrCountry(address.getCounty());
                // 村、社区
                applBaseInfoVo.setAppAddrValige(address.getVillage());
                // 详细地址
                applBaseInfoVo.setAppAddrHome(address.getHomeAddress());
                // 邮编
                applBaseInfoVo.setAppPost(address.getPostCode());
            }
        }
        return applBaseInfoVo;
    }

    /**
     * 交费信息Bo转Vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public GrpPayInfoVo grpPayInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpPayInfoVo payInfoVo = new GrpPayInfoVo();
        // 交费相关信息
        PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
        if (null != paymentInfo) {
            // 交费方式
            payInfoVo.setMoneyinItrvl(paymentInfo.getMoneyinItrvl());
            // 交费形式
            payInfoVo.setMoneyinType(paymentInfo.getMoneyinType());
            // 保费来源
            payInfoVo.setPremFrom(paymentInfo.getPremSource());
            // 交费开户行
            payInfoVo.setBankCode(paymentInfo.getBankCode());
            // 开户名
            payInfoVo.setBankName(paymentInfo.getBankAccName());
            // 账号
            payInfoVo.setBankAccNo(paymentInfo.getBankAccNo());
            // 结算方式
            payInfoVo.setStlType(paymentInfo.getStlType());
            // 结算限额
            payInfoVo.setStlLimit(paymentInfo.getStlAmnt());
            // 结算比例
            payInfoVo.setSettlementRatio(paymentInfo.getStlRate());
            // 结算日期集合
            List<Date> datalists = new ArrayList<Date>();
            List<Date> dateList = grpInsurAppl.getPaymentInfo().getStlDate();
            if (null != dateList) {
                for (int i = 0; i < dateList.size(); i++) {
                    datalists.add(dateList.get(i));
                }
                // 结算日期
                payInfoVo.setSettlementDate(datalists);
            }
        }
        return payInfoVo;
    }

    /**
     * 打印信息Bo转Vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public GrpPrintInfoVo grpPrintInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpPrintInfoVo printInfoVo = new GrpPrintInfoVo();
        printInfoVo.setCntrType(grpInsurAppl.getCntrPrintType());// 合同打印方式
        printInfoVo.setPrtIpsnLstType(grpInsurAppl.getListPrintType());// 清单打印
        printInfoVo.setIpsnVoucherPrt(grpInsurAppl.getVoucherPrintType());// 个人凭证打印
        printInfoVo.setIpsnlstId(grpInsurAppl.getLstProcType());// 清单标记
        printInfoVo.setGiftFlag(grpInsurAppl.getGiftFlag());// 赠送险标记
        printInfoVo.setApplProperty(grpInsurAppl.getInsurProperty());// 保单性质
        //printInfoVo.setManualCheck(grpInsurAppl.getUdwType());// 人工核保
        printInfoVo.setExceptionInform(grpInsurAppl.getNotificaStat());// 异常告知
        //被保人分组类型
        if(null != grpInsurAppl.getIpsnStateGrpList() && grpInsurAppl.getIpsnStateGrpList().size()>0){
        	if(null != grpInsurAppl.getIpsnStateGrpList().get(0)){
        		printInfoVo.setGroupType(grpInsurAppl.getIpsnStateGrpList().get(0).getIpsnGrpType());
        	}
        }
        // printInfoVo.setFile("");//投保资料影像-档案文件上传
        return printInfoVo;
    }
    /**
     * 要约信息Bo转Vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @return
     */
    public GrpProposalInfoVo grpProposalInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpProposalInfoVo proposalInfoVo = new GrpProposalInfoVo();
        // 交费相关信息
        PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
        if (null != paymentInfo) {
            // 首期扣款截止日期
            proposalInfoVo.setEnstPremDeadline(paymentInfo.getForeExpDate());
        }
        // 投保要约
        ApplState applState = grpInsurAppl.getApplState();
        if (null != applState) {
            // 指定生效日
            proposalInfoVo.setInForceDate(applState.getDesignForceDate());
            // 保险期间单位
            proposalInfoVo.setInsurDurUnit(applState.getInsurDurUnit());
            // 被保险人总数
            proposalInfoVo.setIpsnNum(applState.getIpsnNum());
            // 频次是否生效
            proposalInfoVo.setFrequenceEff(applState.getIsFreForce());
            // 生效频率
            proposalInfoVo.setForceNum(applState.getForceFre());
            // 生效方式
            proposalInfoVo.setForceType(applState.getInforceDateType());
            // 保费合计
            proposalInfoVo.setSumPrem(applState.getSumPremium());
            // 特约信息
            Conventions conventions = grpInsurAppl.getConventions();
            if (null != conventions) {
                // 特别约定
                proposalInfoVo.setSpecialPro(conventions.getPolConv());
            }
        }
        return proposalInfoVo;
    }

    /**
     * 特殊险种附加信息
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @return
     */
    public GrpSpecialInsurAddInfoVo grpSpecialInsurAddInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpSpecialInsurAddInfoVo specialInsurAddInfoVo = new GrpSpecialInsurAddInfoVo();
        // 健康险信息
        HealthInsurInfo healthInsurInfo = grpInsurAppl.getHealthInsurInfo();
        if (null != healthInsurInfo) {
            // 公共保额使用范围
            specialInsurAddInfoVo.setComInsurAmntUse(healthInsurInfo.getComInsurAmntUse());
            // 公共保额类型
            specialInsurAddInfoVo.setComInsurAmntType(healthInsurInfo.getComInsurAmntType());
            // 公共保费
            specialInsurAddInfoVo.setCommPremium(healthInsurInfo.getComInsrPrem());
            // 固定公共保额
            specialInsurAddInfoVo.setFixedComAmnt(healthInsurInfo.getSumFixedAmnt());
            // 人均浮动公共保额
            specialInsurAddInfoVo.setIpsnFloatAmnt(healthInsurInfo.getIpsnFloatAmnt());
            // 人均浮动比例
            specialInsurAddInfoVo.setIpsnFloatPct(healthInsurInfo.getFloatInverse());
        }
        // 基金险信息
        FundInsurInfo fundInsurInfo = grpInsurAppl.getFundInsurInfo();
        if (null != fundInsurInfo) {
            // 管理费计提方式
            specialInsurAddInfoVo.setAdminCalType(fundInsurInfo.getAdminFeeCopuType());
            // 管理费比例
            specialInsurAddInfoVo.setAdminPcent(fundInsurInfo.getAdminFeePct());
            // 个人账户金额
            specialInsurAddInfoVo.setIpsnAccAmnt(fundInsurInfo.getIpsnFundPremium());
            // 计入个人账户金额
            specialInsurAddInfoVo.setInclIpsnAccAmnt(fundInsurInfo.getIpsnFundAmnt());
            // 公共账户交费金额
            specialInsurAddInfoVo.setSumPubAccAmnt(fundInsurInfo.getSumFundPremium());
            // 计入公共交费账户金额
            specialInsurAddInfoVo.setInclSumPubAccAmnt(fundInsurInfo.getSumFundAmnt());
            //基金险收到保费时间
            specialInsurAddInfoVo.setPreMioDate(fundInsurInfo.getPreMioDate());
			//账户余额
            specialInsurAddInfoVo.setAccBalance(fundInsurInfo.getAccBalance());
			//账户管理费金额
            specialInsurAddInfoVo.setAccAdminBalance(fundInsurInfo.getAccAdminBalance());
			//首期管理费总金额
            specialInsurAddInfoVo.setAccSumAdminBalance(fundInsurInfo.getAccSumAdminBalance());
        }
        // 建工险信息
        ConstructInsurInfo constructInsurInfo = grpInsurAppl.getConstructInsurInfo();
        if (null != constructInsurInfo) {
            // 工程名称
            specialInsurAddInfoVo.setProjectName(constructInsurInfo.getIobjName());
            // 工程地址
            specialInsurAddInfoVo.setProjectAddr(constructInsurInfo.getProjLoc());
            // 工程类型
            specialInsurAddInfoVo.setProjectType(constructInsurInfo.getProjType());
            // 保费收取方式
            specialInsurAddInfoVo.setCpnstMioType(constructInsurInfo.getPremCalType());
            // 总造价
            specialInsurAddInfoVo.setTotalCost(constructInsurInfo.getIobjCost());
            // 总面积
            specialInsurAddInfoVo.setTotalArea(constructInsurInfo.getIobjSize());
            // 施工开始日期
            specialInsurAddInfoVo.setConstructionDur(constructInsurInfo.getConstructFrom());
            // 施工结束日期
            specialInsurAddInfoVo.setUntil(constructInsurInfo.getConstructTo());
            // 企业资质
            specialInsurAddInfoVo.setEnterpriseLicence(constructInsurInfo.getEnterpriseLicence());
            // 获奖情况
            specialInsurAddInfoVo.setAwardGrade(constructInsurInfo.getAwardGrade());
            // 是否有安防措施
            specialInsurAddInfoVo.setSafetyFlag(constructInsurInfo.getSafetyFlag());
            // 疾病死亡人数
            specialInsurAddInfoVo.setDiseaDeathNums(constructInsurInfo.getDiseaDeathNums());
            // 疾病伤残人数
            specialInsurAddInfoVo.setDiseaDisableNums(constructInsurInfo.getDiseaDisableNums());
            // 意外死亡人数
            specialInsurAddInfoVo.setAcdntDeathNums(constructInsurInfo.getAcdntDeathNums());
            // 意外伤残人数
            specialInsurAddInfoVo.setAcdntDisableNums(constructInsurInfo.getAcdntDisableNums());
            // 过去二年内是否有四级以上安全事故
            specialInsurAddInfoVo.setSaftyAcdntFlag(constructInsurInfo.getSaftyAcdntFlag());
            // 层高(米)
            specialInsurAddInfoVo.setFloorHeight(constructInsurInfo.getFloorHeight());
            // 工程位置类别
            specialInsurAddInfoVo.setProjLocType(constructInsurInfo.getProjLocType());
        }
        return specialInsurAddInfoVo;
    }

    /**
     * 增值税Bo转vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public GrpVatInfoVo grpVatInfoBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        GrpVatInfoVo vatInfoVo = new GrpVatInfoVo();
        // 纳税人名称
        // vatInfoVo.setCompanyBankAccNo("");
        // 团体客户信息
        GrpHolderInfo grpHolderInfo = grpInsurAppl.getGrpHolderInfo();
        if (null != grpHolderInfo) {
            // 纳税人识别号
            vatInfoVo.setTaxpayerCode(grpHolderInfo.getTaxpayerId());
        }
//        // 交费相关
//        PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
//        if (null != paymentInfo) {
//            // 银行账户
//            vatInfoVo.setTaxpayerCode(paymentInfo.getBankAccNo());
//            // 开户银行
//            vatInfoVo.setCompanyBankName(paymentInfo.getBankCode());
//            // 开户名称
//            vatInfoVo.setTaxpayer(paymentInfo.getBankAccName());
//        }
        return vatInfoVo;
    }

    /**
     * 被保人分组Bo转vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public List<InsuredGroupModalVo> insuredGroupModalBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        // 被保人分组信息集合
        List<InsuredGroupModalVo> list = new ArrayList<InsuredGroupModalVo>();
        // 要约分组集合
        List<IpsnStateGrp> ipsnStateGrpList = grpInsurAppl.getIpsnStateGrpList();
        if (null != ipsnStateGrpList && ipsnStateGrpList.size() > 0) {
            for (int i = 0; i < ipsnStateGrpList.size(); i++) {
                // 险种集合
                List<InsuranceInfoVo> insuranceInfoVoList = new ArrayList<InsuranceInfoVo>();
                // 被保人分组信息
                InsuredGroupModalVo insuredGroupModalVo = new InsuredGroupModalVo();
                // 要约属组编号
                insuredGroupModalVo.setIpsnGrpNo(ipsnStateGrpList.get(i).getIpsnGrpNo());
                // 要约属组名称
                insuredGroupModalVo.setIpsnGrpName(ipsnStateGrpList.get(i).getIpsnGrpName());
                // 行业类别
                insuredGroupModalVo.setOccClassCode(ipsnStateGrpList.get(i).getOccClassCode());
                // 职业类别
                insuredGroupModalVo.setIpsnOccSubclsCode(ipsnStateGrpList.get(i).getIpsnOccCode());
                // 要约属组人数
                insuredGroupModalVo.setIpsnGrpNum(ipsnStateGrpList.get(i).getIpsnGrpNum());
                // 男女比例
                insuredGroupModalVo.setGenderRadio(ipsnStateGrpList.get(i).getGenderRadio());
                // 参加工伤比例
                insuredGroupModalVo.setGsRate(ipsnStateGrpList.get(i).getGsPct());
                // 参加医保比例
                insuredGroupModalVo.setSsRate(ipsnStateGrpList.get(i).getSsPct());
                // 险种信息集合
                List<GrpPolicy> grpPolicy = ipsnStateGrpList.get(i).getGrpPolicyList();
                if (null != grpPolicy && grpPolicy.size() > 0) {
                    for (int j = 0; j < grpPolicy.size(); j++) {
                        // 险种信息
                        InsuranceInfoVo insuranceInfoVo = new InsuranceInfoVo();
                        // 险种代码
                        insuranceInfoVo.setPolCode(grpPolicy.get(j).getPolCode());
                        // 主附险性质
                        insuranceInfoVo.setMrCode(grpPolicy.get(j).getMrCode());
                        // 险种保额
                        insuranceInfoVo.setFaceAmnt(grpPolicy.get(j).getFaceAmnt());
                        // 实际保费
                        insuranceInfoVo.setPremium(grpPolicy.get(j).getPremium());
                        // 标准保费
                        insuranceInfoVo.setStdPremium(grpPolicy.get(j).getStdPremium());
                        // 承保费率
                        insuranceInfoVo.setPremRate(grpPolicy.get(j).getPremRate());
                        // 费率浮动幅度
                        insuranceInfoVo.setRecDisount(grpPolicy.get(j).getPremDiscount());
                        insuranceInfoVoList.add(insuranceInfoVo);
                    }
                    // 险种
                    insuredGroupModalVo.setInsuranceInfoVos(insuranceInfoVoList);
                }
                list.add(insuredGroupModalVo);
            }
        }
        return list;
    }

    /**
     * 组织层次信息Bo转vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public List<OrganizaHierarModalVo> organizaHierarModalBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        // 组织层次信息集合
        List<OrganizaHierarModalVo> list = new ArrayList<OrganizaHierarModalVo>();
        // 组织关系树集合
        List<OrgTree> orgTreeList = grpInsurAppl.getOrgTreeList();
        if (null != orgTreeList && orgTreeList.size() > 0) {
            for (int i = 0; i < orgTreeList.size(); i++) {
                // 组织层次信息
                OrganizaHierarModalVo organizaHierarModalVo = new OrganizaHierarModalVo();
                // 是否交费
                organizaHierarModalVo.setPay(orgTreeList.get(i).getIsPaid());
                // 保全选项
                organizaHierarModalVo.setSecurityOptions(orgTreeList.get(i).getMtnOpt());
                // 服务指数
                organizaHierarModalVo.setServiceAssignment(orgTreeList.get(i).getServiceOpt());
                // 发票选项
                organizaHierarModalVo.setInvoiceOption(orgTreeList.get(i).getReceiptOpt());
                // 层次代码
                organizaHierarModalVo.setLevelCode(orgTreeList.get(i).getLevelCode());
                // 上级层次代码
                organizaHierarModalVo.setPrioLevelCode(orgTreeList.get(i).getPrioLevelCode());
                // 节点类型
                organizaHierarModalVo.setNodeType(orgTreeList.get(i).getNodeType());
                // 是否是根节点
                organizaHierarModalVo.setIsRoot(orgTreeList.get(i).getIsRoot());
                // 节点交费金额
                organizaHierarModalVo.setNodePayAmnt(orgTreeList.get(i).getNodePayAmnt());
                // 开户名称
                organizaHierarModalVo.setBankaccName(orgTreeList.get(i).getBankaccName());
                // 银行账号
                organizaHierarModalVo.setBankaccNo(orgTreeList.get(i).getBankaccNo());
                // 开户银行
                organizaHierarModalVo.setBankCode(orgTreeList.get(i).getBankCode());
                // 增值税号
                // organizaHierarModalVo.setVatNum("");
                // 团体客户信息
                GrpHolderInfo grpHolderInfo = orgTreeList.get(i).getGrpHolderInfo();
                if (null != grpHolderInfo) {
                    // 客户号
                    organizaHierarModalVo.setCustNo(grpHolderInfo.getGrpCustNo());
                    if("0".equals(orgTreeList.get(i).getNodeType())){
                        // 单位、团体名称
                        organizaHierarModalVo.setCompanyName(grpHolderInfo.getGrpName());
                    }else{
                        if(grpHolderInfo.getGrpName().indexOf("~")!=-1){
                            String grpName = grpHolderInfo.getGrpName().split("~")[0];
                            String groupDep = grpHolderInfo.getGrpName().split("~")[1];
                            // 单位、团体名称
                            organizaHierarModalVo.setCompanyName(grpName);
                            // 部门名称
                            organizaHierarModalVo.setGroupDep(groupDep);
                        }else{
                            // 单位、团体名称
                            organizaHierarModalVo.setCompanyName(grpHolderInfo.getGrpName());
                            // 部门名称
                            organizaHierarModalVo.setGroupDep(grpHolderInfo.getGrpName());
                        }
                    }
                    // 曾用名
                    organizaHierarModalVo.setOldName(grpHolderInfo.getFormerGrpName());
                    // 行业性质
                    organizaHierarModalVo.setIndustryClassification(grpHolderInfo.getOccClassCode());
                    // 证件类型
                    organizaHierarModalVo.setDeptType(grpHolderInfo.getGrpIdType());
                    // 成员总数
                    organizaHierarModalVo.setTotalMembers(grpHolderInfo.getNumOfEnterprise());
                    // 证件号码
                    organizaHierarModalVo.setIdCardNo(grpHolderInfo.getGrpIdNo());
                    // 在职人数
                    organizaHierarModalVo.setOjEmpNum(grpHolderInfo.getOnjobStaffNum());
                    // 投保人数
                    organizaHierarModalVo.setApplNum(grpHolderInfo.getIpsnNum());
                    // partyId
                    organizaHierarModalVo.setPartyId(grpHolderInfo.getPartyId());
                    // 企业注册国籍
                    organizaHierarModalVo.setRegisteredNationality(grpHolderInfo.getGrpCountryCode());
                    // 联系人姓名
                    organizaHierarModalVo.setContactName(grpHolderInfo.getContactName());
                    // 手机号码
                    organizaHierarModalVo.setPhoneNum(grpHolderInfo.getContactMobile());
                    // 电子邮件
                    organizaHierarModalVo.setEmail(grpHolderInfo.getContactEmail());
                    // 固定电话
                    organizaHierarModalVo.setFixedPhones(grpHolderInfo.getContactTelephone());
                    // 地址信息
                    Address address = grpHolderInfo.getAddress();
                    if (null != address) {
                        // 省、自治州
                        organizaHierarModalVo.setProvince(address.getProvince());
                        // 市、州
                        organizaHierarModalVo.setCity(address.getCity());
                        // 区、县
                        organizaHierarModalVo.setCounty(address.getCounty());
                        // 镇、乡
                        organizaHierarModalVo.setTown(address.getTown());
                        // 村、社区
                        organizaHierarModalVo.setVillage(address.getVillage());
                        // 地址明细
                        organizaHierarModalVo.setDetailAddress(address.getHomeAddress());
                        // 邮政编码
                        organizaHierarModalVo.setPostCode(address.getPostCode());
                    }
                }
                list.add(organizaHierarModalVo);
            }
        }
        return list;
    }

    /**
     * 险种Bo转vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public List<GrpBusiPrdVo> grpBusiPrdBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        // 险种信息集合
        List<GrpBusiPrdVo> list = new ArrayList<GrpBusiPrdVo>();
        // 险种集合
        List<Policy> policyList = null;
        // 投保要约
        ApplState applState = grpInsurAppl.getApplState();
        if (null != applState) {
            // 险种集合
            policyList = applState.getPolicyList();
        }
        if (null != policyList && policyList.size() > 0) {
            for (int i = 0; i < policyList.size(); i++) {
                // 每次进来清空之前的 责任信息集合
                List<ResponseVo> responseVoList = new ArrayList<ResponseVo>();
                // 险种信息
                GrpBusiPrdVo busiPrdVo = new GrpBusiPrdVo();
                // 投保单ID
                busiPrdVo.setApplId(grpInsurAppl.getApplNo());
                // 险种代码
                busiPrdVo.setBusiPrdCode(policyList.get(i).getPolCode());
                // 险种名称
                busiPrdVo.setBusiPrdCodeName(policyList.get(i).getPolNameChn());
                // 保险期间单位
                busiPrdVo.setInsurDurUnit(policyList.get(i).getInsurDurUnit());
                // 保险期间
                busiPrdVo.setInsurDur(policyList.get(i).getInsurDur());
                // 承保人数
                busiPrdVo.setInsuredNum(policyList.get(i).getPolIpsnNum());
                // 保额
                busiPrdVo.setAmount(policyList.get(i).getFaceAmnt());
                // 保费
                busiPrdVo.setPremium(policyList.get(i).getPremium());
                // 健康险标识
                busiPrdVo.setHealthInsurFlag(policyList.get(i).getSpeciBusinessLogo());
                // 子险种集合
                List<SubPolicy> subPolicy = policyList.get(i).getSubPolicyList();
                if (null != subPolicy && subPolicy.size() > 0) {
                    for (int j = 0; j < subPolicy.size(); j++) {
                        // 责任信息
                        ResponseVo responseVo = new ResponseVo();
                        // 投保单ID
                        responseVo.setApplId(grpInsurAppl.getApplNo());
                        // 责任代码
                        responseVo.setProductCode(subPolicy.get(j).getSubPolCode());
                        // 险种代码
                        responseVo.setBusiPrdCode(policyList.get(i).getPolCode());
                        // 责任名称
                        responseVo.setProductName(subPolicy.get(j).getSubPolName());
                        // 责任号
                        responseVo.setProductNo(subPolicy.get(j).getSubPolCode());
                        // 保额
                        responseVo.setProductNum(subPolicy.get(j).getSubPolAmnt());
                        // 保费
                        responseVo.setProductPremium(subPolicy.get(j).getSubPremium());
                        // 险种标准保费
                        responseVo.setSubStdPremium(subPolicy.get(j).getSubStdPremium());
                        responseVoList.add(responseVo);
                    }
                }
                // 责任信息集合
                busiPrdVo.setResponseVos(responseVoList);
                list.add(busiPrdVo);
            }
        }
        return list;
    }

    /**
     * 收付费分组Bo转vo
     * 
     * @param grpInsurAppl
     *            团单基本信息Bo
     * @author jinmeina
     * @return
     */
    public List<ChargePayGroupModalVo> chargePayGroupModalBoToVo(GrpInsurAppl grpInsurAppl) {
        if (null == grpInsurAppl) {
            return null;
        }
        List<ChargePayGroupModalVo> list = new ArrayList<ChargePayGroupModalVo>();
        // 团体收费组
        List<IpsnPayGrp> ipsnPayGrpList = grpInsurAppl.getIpsnPayGrpList();
        if (null != ipsnPayGrpList && ipsnPayGrpList.size() > 0) {
            for (int i = 0; i < ipsnPayGrpList.size(); i++) {
                ChargePayGroupModalVo chargePayGroupModalVo = new ChargePayGroupModalVo();
                // 组号
                chargePayGroupModalVo.setGroupNo(ipsnPayGrpList.get(i).getFeeGrpNo());
                // 组名
                chargePayGroupModalVo.setGroupName(ipsnPayGrpList.get(i).getFeeGrpName());
                // 人数
                chargePayGroupModalVo.setNum(ipsnPayGrpList.get(i).getIpsnGrpNum());
                // 交费形式
                chargePayGroupModalVo.setMoneyinType(ipsnPayGrpList.get(i).getMoneyinType());
                // 开户银行
                chargePayGroupModalVo.setBankCode(ipsnPayGrpList.get(i).getBankCode());
                // 开户名称
                chargePayGroupModalVo.setBankName(ipsnPayGrpList.get(i).getBankaccName());
                // 银行账号
                chargePayGroupModalVo.setBankAccNo(ipsnPayGrpList.get(i).getBankaccNo());
                list.add(chargePayGroupModalVo);
            }
        }
        return list;
    }
}
