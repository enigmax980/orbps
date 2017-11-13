package com.newcore.orbps.web.contractentry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.newcore.orbps.model.service.para.GrpInsuredPara;
import com.newcore.orbps.service.api.InsurApplContentRevise;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.exception.BusinessException;
import com.halo.core.filestore.api.FileStoreService;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.HldrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.IpsnImportInfo;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.models.web.vo.contractentry.listimport.AccInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportBeneficiaryVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportBsInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportHldrInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportIpsnInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportPageVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportSubPolVo;
import com.newcore.orbps.models.web.vo.contractentry.listimport.ListImportVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.BatchJobControlService;
import com.newcore.orbps.service.api.GrpInsuredService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.service.api.IpsnImpAppliForInterService;
import com.newcore.orbps.service.api.IpsnImportStateService;
import com.newcore.orbps.service.api.OrbpsInsurApplCvTaskServer;
import com.newcore.orbps.service.api.QueryInsurApplCvTaskServer;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;

/**
 * 团单控制
 * 
 * @author xiaoYe
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/offlineList")
public class OfflineListImportController {
    /**
     * 日志对象.
     */
    private static Logger logger = LoggerFactory.getLogger(OfflineListImportController.class);

    @Autowired
    InsurApplServices insurApplServices;

    @Autowired
    InsurApplServer grpInsurApplServer;

    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    OrbpsInsurApplCvTaskServer orbpsInsurApplCvTaskServer;

    @Autowired
    QueryInsurApplCvTaskServer queryInsurApplCvTaskServer;

    @Autowired
    GrpInsuredService grpInsuredService;

    @Autowired
    InsurApplContentRevise restfulinsurApplContentRevise;

    @Autowired
    IpsnImportStateService ipsnImportStateService;

    @Autowired
    IpsnImpAppliForInterService ipsnImpAppliForInterService;
    
    @Autowired
    BranchService branchService;
    /**
     * 批作业控制服务
     */
    @Autowired
    BatchJobControlService batchJobControlService;

    @Resource
    FileStoreService fileStoreService;

    RetInfo retInfo = new RetInfo();

    LinkedList<GrpInsured> files = null;

    GrpInsured grpInsured = null;

    List<String> lPolCodes = new ArrayList<>();

    /**
     * 提交团单信息
     * 
     * @author xiaoYe
     * @param query
     * @param grpInsurApplVo
     * @return
     */
    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo grpInsurAppl(@RequestBody ListImportPageVo listImportPageVo) {

        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsured grpInsured = new GrpInsured();
        HldrInfo hldrInfo = new HldrInfo();
        // 投保人信息
        if (listImportPageVo.getHldrInfoVo() != null) {
            hldrInfo.setHldrName(listImportPageVo.getHldrInfoVo().getHldrName());
            hldrInfo.setHldrSex(listImportPageVo.getHldrInfoVo().getHldrSex());
            hldrInfo.setHldrIdType(listImportPageVo.getHldrInfoVo().getHldrIdType());
            hldrInfo.setHldrIdNo(listImportPageVo.getHldrInfoVo().getHldrIdNo());
            hldrInfo.setHldrBirthDate(listImportPageVo.getHldrInfoVo().getHldrBirth());
            hldrInfo.setHldrMobilePhone(listImportPageVo.getHldrInfoVo().getHldrPhone());
            grpInsured.setHldrInfo(hldrInfo);
        }
        // 被保人信息
        if (listImportPageVo.getIpsnInfoVo() != null) {
            grpInsured.setApplNo(listImportPageVo.getIpsnInfoVo().getApplNo());
            grpInsured.setIpsnPartyId(listImportPageVo.getIpsnInfoVo().getCustNo());// 客户号
            grpInsured.setIpsnNo(listImportPageVo.getIpsnInfoVo().getIpsnNo());// 被保险人编号
            grpInsured.setIpsnName(listImportPageVo.getIpsnInfoVo().getName());// 被保险人姓名
            // 被保险人类型
            if ("Y".equals(listImportPageVo.getIpsnInfoVo().getJoinIpsnFlag())) {
                grpInsured.setIpsnType("A");// 被保险人类型
            } else {
                grpInsured.setIpsnType("I");// 被保险人类型
            }
            grpInsured.setIpsnIdNo(listImportPageVo.getIpsnInfoVo().getIdNo());// 证件号码
            grpInsured.setIpsnSex(listImportPageVo.getIpsnInfoVo().getIpsursex());// 被保人性别
            grpInsured.setIpsnBirthDate(listImportPageVo.getIpsnInfoVo().getBirthDate());// 被保人出生日期
            grpInsured.setIpsnAge(listImportPageVo.getIpsnInfoVo().getAge());// 被保人年龄
            grpInsured.setIpsnIdType(listImportPageVo.getIpsnInfoVo().getIdType());// 被保人证件类型
            grpInsured.setIpsnOccCode(listImportPageVo.getIpsnInfoVo().getOccupationalCodes());// 职业代码
            grpInsured.setIpsnOccClassLevel(listImportPageVo.getIpsnInfoVo().getRiskLevel());// 职业风险等级
            grpInsured.setLevelCode(listImportPageVo.getIpsnInfoVo().getGroupLevelCode());// 组织层次代码
            grpInsured.setIpsnCompanyLoc(listImportPageVo.getIpsnInfoVo().getWorkAddress());// 被保人工作地点
            grpInsured.setIpsnRefNo(listImportPageVo.getIpsnInfoVo().getWorkNo());// 被保人工号
            grpInsured.setInServiceFlag(listImportPageVo.getIpsnInfoVo().getOnJobFlag());// 是否在职
            grpInsured.setIpsnSss(listImportPageVo.getIpsnInfoVo().getMedicalInsurFlag());// 医保标识
            grpInsured.setIpsnSsc(listImportPageVo.getIpsnInfoVo().getMedicalInsurCode());// 医保代码
            grpInsured.setIpsnSsn(listImportPageVo.getIpsnInfoVo().getMedicalInsurNo());// 医保编号
            grpInsured.setNotificaStat(listImportPageVo.getIpsnInfoVo().getHealthFlag());// 是否有异常告知
            grpInsured.setIpsnEmail(listImportPageVo.getIpsnInfoVo().getEmail());// 被保人邮箱
            grpInsured.setIpsnPayAmount(listImportPageVo.getIpsnInfoVo().getCharge());// 个人交费金额
            grpInsured.setRemark(listImportPageVo.getIpsnInfoVo().getRemarks());// 备注
            grpInsured.setFeeGrpNo(listImportPageVo.getIpsnInfoVo().getFeeGrpNo());// 被保人属组号
            grpInsured.setIpsnMobilePhone(listImportPageVo.getIpsnInfoVo().getPhone());// 被保人手机号
            grpInsured.setMasterIpsnNo(listImportPageVo.getIpsnInfoVo().getMainIpsnNo());// 主被保险人编号
            grpInsured.setIpsnRtMstIpsn(listImportPageVo.getIpsnInfoVo().getRelToIpsn());// 与主被保险人关系
            grpInsured.setRelToHldr(listImportPageVo.getIpsnInfoVo().getRelToHldr());// 与投保人关系
            grpInsured.setTyNetPayAmnt(listImportPageVo.getIpsnInfoVo().getTyNetPayAmnt());// 同业公司人身保险保额合计
            grpInsured.setGrpPayAmount(listImportPageVo.getIpsnInfoVo().getGrpPayAmount());// 单位交费金额
        }
        // 缴费账户信息
        if (listImportPageVo.getAccInfoList() != null) {
            List<AccInfo> list = new ArrayList<AccInfo>();
            for (AccInfoVo accInfovo : listImportPageVo.getAccInfoList()) {
            	AccInfo accInfo = new AccInfo();
                accInfo.setSeqNo(accInfovo.getSeqNo());// 账户顺序
                accInfo.setIpsnPayAmnt(accInfovo.getIpsnPayAmnt());// 个人扣款金额
                accInfo.setIpsnPayPct(null == accInfovo.getIpsnPayPct() ? null : accInfovo.getIpsnPayPct() / 100);// 个人账户交费比例
                accInfo.setBankAccName(accInfovo.getBankAccName());// 缴费账户
                accInfo.setBankCode(accInfovo.getBankCode());// 缴费银行
                accInfo.setBankAccNo(accInfovo.getBankAccNo());// 缴费账号
                list.add(accInfo);
            }
            grpInsured.setAccInfoList(list);
        }
        // 要约信息
        List<SubState> listSubState = new ArrayList<>();
        if (listImportPageVo.getBsInfoVo() != null) {
            for (int i = 0; i < listImportPageVo.getBsInfoVo().size(); i++) {
                for (int j = 0; j < listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().size(); j++) {
                    SubState subState = new SubState();
                    String SubPolCode;
                    // 如果责任代码为空，把险种代码传过去
                    if (StringUtils.isEmpty(
                            listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolCode())) {
                        SubPolCode = listImportPageVo.getBsInfoVo().get(i).getPolCode();
                    } else {
                        SubPolCode = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                                .getSubPolCode();
                    }
                    subState.setPolCode(SubPolCode);
                    Long groupCodeTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getGroupCode();
                    subState.setClaimIpsnGrpNo(groupCodeTb);
                    Double amountTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getAmount();
                    subState.setFaceAmnt(amountTb);
                    Double premiumTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getPremium();
                    subState.setPremium(premiumTb);
                    String SubPolName = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getSubPolName();
                    subState.setPolNameChn(SubPolName);
                    listSubState.add(subState);
                }
            }
        }
        grpInsured.setSubStateList(listSubState);
        // 受益人信息
        List<BnfrInfo> listBnfrInfo = new ArrayList<>();
        if (listImportPageVo.getBeneficiaryInfo() != null) {
            for (int i = 0; i < listImportPageVo.getBeneficiaryInfo().size(); i++) {
                BnfrInfo bnfrInfo = new BnfrInfo();
                Date birthDate = listImportPageVo.getBeneficiaryInfo().get(i).getBirthDate();
                bnfrInfo.setBnfrBirthDate(birthDate);
                String idNo = listImportPageVo.getBeneficiaryInfo().get(i).getIdNo();
                bnfrInfo.setBnfrIdNo(idNo);
                String idType = listImportPageVo.getBeneficiaryInfo().get(i).getIdTypeTb();
                bnfrInfo.setBnfrIdType(idType);
                Long benefitOrder = listImportPageVo.getBeneficiaryInfo().get(i).getBenefitOrder();
                bnfrInfo.setBnfrLevel(benefitOrder);
                String name = listImportPageVo.getBeneficiaryInfo().get(i).getName();
                bnfrInfo.setBnfrName(name);
                // 受益份额
                Double beneAmount = listImportPageVo.getBeneficiaryInfo().get(i).getBeneAmount() / 100;
                bnfrInfo.setBnfrProfit(beneAmount);
                String relToIpsn = listImportPageVo.getBeneficiaryInfo().get(i).getRelToIpsnTb();
                bnfrInfo.setBnfrRtIpsn(relToIpsn);
                String sex = listImportPageVo.getBeneficiaryInfo().get(i).getSex();
                bnfrInfo.setBnfrSex(sex);
                listBnfrInfo.add(bnfrInfo);
            }
        }
        grpInsured.setBnfrInfoList(listBnfrInfo);
        RetInfo retInfo = grpInsuredService.addGrpInsured(grpInsured);

        return retInfo;
    }

    /**
     * 删除
     * 
     * @author xiaoYe
     * @param session
     *            , applNo
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/delete")
    public @ResponseMessage RetInfo getDeleteInsuredInfo(@RequestBody ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);

        Map<String, Object> paraMap = new HashMap<String, Object>();
        paraMap.put("applNo", listImportIpsnInfoVo.getApplNo());
        paraMap.put("ipsnNo", listImportIpsnInfoVo.getIpsnNo());
        RetInfo retInfo = grpInsuredService.removeGrpInsured(paraMap);
        return retInfo;
    }

    /**
     * 被保险人信息查询
     * 
     * @author xiaoYe
     * @param listImportIpsnInfoVo
     * @return
     */
    @RequestMapping(value = "/queryInsured")
    public @ResponseMessage ListImportPageVo getQueryInsuredInfo(
            @RequestBody ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        GrpInsured grpInsured = new GrpInsured();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        grpInsured.setApplNo(listImportIpsnInfoVo.getApplNo());
        if (null != listImportIpsnInfoVo.getIpsnNo()) {
            grpInsured.setIpsnNo(listImportIpsnInfoVo.getIpsnNo());
        }
        if (!StringUtils.isEmpty(listImportIpsnInfoVo.getCustNo())) {
            grpInsured.setIpsnCustNo(listImportIpsnInfoVo.getCustNo());
        }
        GrpInsured grpInsuredInfo = grpInsuredService.queryGrpInsured(grpInsured);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer
                .searchInsurApplByBusinessKey(listImportIpsnInfoVo.getApplNo());
        logger.debug("调用后台服务查询的保单类型是" + grpInsurApplBo.getGrpInsurAppl().getCntrType());
        // 投保人信息
        ListImportPageVo listImportPageVo = new ListImportPageVo();
        listImportPageVo.setCntrType(grpInsurApplBo.getGrpInsurAppl().getCntrType());
        if (null != grpInsuredInfo.getHldrInfo()) {
            HldrInfo hldrInfo = grpInsuredInfo.getHldrInfo();
            ListImportHldrInfoVo hldrInfoVo = new ListImportHldrInfoVo();
            hldrInfoVo.setHldrName(hldrInfo.getHldrName());
            hldrInfoVo.setHldrSex(hldrInfo.getHldrSex());
            hldrInfoVo.setHldrIdType(hldrInfo.getHldrIdType());
            hldrInfoVo.setHldrIdNo(hldrInfo.getHldrIdNo());
            hldrInfoVo.setHldrBirth(hldrInfo.getHldrBirthDate());
            hldrInfoVo.setHldrPhone(hldrInfo.getHldrMobilePhone());
            listImportPageVo.setHldrInfoVo(hldrInfoVo);
        }
        // 被保险人信息
        if (grpInsuredInfo != null) {
            listImportIpsnInfoVo.setApplNo(grpInsuredInfo.getApplNo());
            listImportIpsnInfoVo.setCustNo(grpInsuredInfo.getIpsnPartyId());
            listImportIpsnInfoVo.setName(grpInsuredInfo.getIpsnName());
            listImportIpsnInfoVo.setRelToIpsn(grpInsuredInfo.getRelToHldr());
            if ("A".equals(grpInsuredInfo.getIpsnType())) {
                listImportIpsnInfoVo.setJoinIpsnFlag("Y");
            } else {
                listImportIpsnInfoVo.setJoinIpsnFlag("N");
            }
            listImportIpsnInfoVo.setIdNo(grpInsuredInfo.getIpsnIdNo());
            listImportIpsnInfoVo.setIpsursex(grpInsuredInfo.getIpsnSex());
            ;
            listImportIpsnInfoVo.setBirthDate(grpInsuredInfo.getIpsnBirthDate());
            listImportIpsnInfoVo.setAge(grpInsuredInfo.getIpsnAge());
            listImportIpsnInfoVo.setIdType(grpInsuredInfo.getIpsnIdType());
            listImportIpsnInfoVo.setOccupationalCodes(grpInsuredInfo.getIpsnOccCode());
            listImportIpsnInfoVo.setOccupationName(grpInsuredInfo.getIpsnName());
            listImportIpsnInfoVo.setRiskLevel(grpInsuredInfo.getIpsnOccClassLevel());// 职业风险等级
            listImportIpsnInfoVo.setGroupLevelCode(grpInsuredInfo.getLevelCode());// 组织层次代码
            listImportIpsnInfoVo.setWorkAddress(grpInsuredInfo.getIpsnCompanyLoc());
            listImportIpsnInfoVo.setWorkNo(grpInsuredInfo.getIpsnRefNo());
            listImportIpsnInfoVo.setOnJobFlag(grpInsuredInfo.getInServiceFlag());
            listImportIpsnInfoVo.setMedicalInsurCode(grpInsuredInfo.getIpsnSsc());
            listImportIpsnInfoVo.setMedicalInsurFlag(grpInsuredInfo.getIpsnSss());
            listImportIpsnInfoVo.setMedicalInsurNo(grpInsuredInfo.getIpsnSsn());
            listImportIpsnInfoVo.setHealthFlag(grpInsuredInfo.getNotificaStat());
            listImportIpsnInfoVo.setEmail(grpInsuredInfo.getIpsnEmail());
            listImportIpsnInfoVo.setCharge(grpInsuredInfo.getIpsnPayAmount());
            listImportIpsnInfoVo.setRemarks(grpInsuredInfo.getRemark());
            listImportIpsnInfoVo.setPhone(grpInsuredInfo.getIpsnMobilePhone());// 被保险人手机号码
            listImportIpsnInfoVo.setFeeGrpNo(grpInsuredInfo.getFeeGrpNo());// 收费属组号
            listImportIpsnInfoVo.setMainIpsnNo(grpInsuredInfo.getMasterIpsnNo());// 主被保险人编号
            listImportIpsnInfoVo.setRelToIpsn(grpInsuredInfo.getIpsnRtMstIpsn());// 与主被保险人关系
            listImportIpsnInfoVo.setRelToHldr(grpInsuredInfo.getRelToHldr());// 与投保人关系
            listImportIpsnInfoVo.setTyNetPayAmnt(grpInsuredInfo.getTyNetPayAmnt());// 同业公司人身保险保额合计
            listImportIpsnInfoVo.setGrpPayAmount(grpInsuredInfo.getGrpPayAmount());// 单位交费金额
            listImportPageVo.setIpsnInfoVo(listImportIpsnInfoVo);
        }
        // 缴费账户信息
        if (grpInsuredInfo.getAccInfoList() != null) {
            List<AccInfoVo> list = new ArrayList<>();
            for (AccInfo ac : grpInsuredInfo.getAccInfoList()) {
                AccInfoVo accInfoVo = new AccInfoVo();
                accInfoVo.setSeqNo(ac.getSeqNo());// 账户顺序
                accInfoVo.setIpsnPayAmnt(ac.getIpsnPayAmnt());// 个人扣款金额
                accInfoVo.setIpsnPayPct(ac.getIpsnPayPct() * 100);// 个人账户交费比例
                accInfoVo.setBankAccName(ac.getBankAccName());// 缴费账户
                accInfoVo.setBankCode(ac.getBankCode());// 缴费银行
                accInfoVo.setBankAccNo(ac.getBankAccNo());// 缴费账号
                list.add(accInfoVo);
            }
            listImportPageVo.setAccInfoList(list);// 缴费账户信息
        }
        List<ListImportBeneficiaryVo> listImportBeneficiaryVos = new ArrayList<>();
        if (null != grpInsuredInfo.getBnfrInfoList()) {
            for (int i = 0; i < grpInsuredInfo.getBnfrInfoList().size(); i++) {
                ListImportBeneficiaryVo listImportBeneficiaryVo = new ListImportBeneficiaryVo();
                listImportBeneficiaryVo.setBirthDate(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrBirthDate());
                listImportBeneficiaryVo.setIdNo(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrIdNo());
                listImportBeneficiaryVo.setIdTypeTb(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrIdType());
                listImportBeneficiaryVo.setBenefitOrder(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrLevel());
                listImportBeneficiaryVo.setName(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrName());
                listImportBeneficiaryVo.setBeneAmount(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrProfit() * 100);
                listImportBeneficiaryVo.setRelToIpsnTb(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrRtIpsn());
                listImportBeneficiaryVo.setSex(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrSex());
                listImportBeneficiaryVos.add(listImportBeneficiaryVo);
            }
            listImportPageVo.setBeneficiaryInfo(listImportBeneficiaryVos);
        }
        // 要约信息
        List<ListImportBsInfoVo> listImportBsInfoVos = new ArrayList<>();
        for (int j = 0; j < grpInsuredInfo.getSubStateList().size(); j++) {
            ListImportBsInfoVo listImportBsInfoVo;
            List<ListImportSubPolVo> listImportSubPolVos;
            if (j == 0 || !StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),
                    listImportBsInfoVos.get(listImportBsInfoVos.size() - 1).getPolCode())) {
                listImportBsInfoVo = new ListImportBsInfoVo();
                listImportSubPolVos = new ArrayList<>();
                listImportBsInfoVo.setPolCode(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3));
                for (Policy policy : grpInsurApplBo.getGrpInsurAppl().getApplState().getPolicyList()) {
                    if (StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),
                            policy.getPolCode())) {
                        listImportBsInfoVo.setPolName(policy.getPolNameChn());
                        break;
                    }
                }
            } else {
                listImportBsInfoVo = listImportBsInfoVos.get(listImportBsInfoVos.size() - 1);
                listImportSubPolVos = listImportBsInfoVo.getListImportSubPolVos();
            }
            ListImportSubPolVo listImportSubPolVo = new ListImportSubPolVo();
            listImportSubPolVo.setSubPolCode(grpInsuredInfo.getSubStateList().get(j).getPolCode());
            listImportSubPolVo.setSubPolName(grpInsuredInfo.getSubStateList().get(j).getPolNameChn());
            listImportSubPolVo.setAmount(grpInsuredInfo.getSubStateList().get(j).getFaceAmnt());
            listImportSubPolVo.setPremium(grpInsuredInfo.getSubStateList().get(j).getPremium());
            listImportSubPolVo.setGroupCode(grpInsuredInfo.getSubStateList().get(j).getClaimIpsnGrpNo());
            // 获取被保人属组名称
            for (IpsnStateGrp ipsnStateGrp : grpInsurApplBo.getGrpInsurAppl().getIpsnStateGrpList()) {
                if (grpInsuredInfo.getSubStateList().get(j).getClaimIpsnGrpNo() == ipsnStateGrp.getIpsnGrpNo()) {
                    listImportSubPolVo.setGroupName(ipsnStateGrp.getIpsnGrpName());
                    break;
                }
            }
            listImportSubPolVos.add(listImportSubPolVo);
            listImportBsInfoVo.setListImportSubPolVos(listImportSubPolVos);
            if (j == 0 || !StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),
                    listImportBsInfoVos.get(listImportBsInfoVos.size() - 1).getPolCode())) {
                listImportBsInfoVos.add(listImportBsInfoVo);
            }
        }
        listImportPageVo.setBsInfoVo(listImportBsInfoVos);

        return listImportPageVo;
    }

    /**
     * 查询表格
     * 
     * @author xiaoYe
     * @param query
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/query")
    public @ResponseMessage DataTable<ListImportVo> getResponseSummaryList(@CurrentSession Session session,
            QueryDt queryDt, ListImportVo listImportVo) {
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        String provinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = listImportVo.getSalesBranchNo();
        // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isBlank(mgrBranchNo)){
            listImportVo.setSalesBranchNo(provinceBranchNo);
        }else{
        	//销售机构条件控制
        	CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo2);
            String provinceBranchNo1 = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if (!StringUtils.equals(provinceBranchNo, provinceBranchNo1)) {
            	throw new BusinessException("0004", "查询失败，该操作员只能查看并操作本省级及下级机构的保单");
            }
        }
        if(StringUtils.isEmpty(listImportVo.getFindSubBranchNoFlag())){
        	listImportVo.setFindSubBranchNoFlag("Y");
        }
        // 前台接收的日期字符串，对接后台，做格式转换
        Date taskStartTime = null;
        Date taskEndTime = null;
        if (!StringUtils.isEmpty(listImportVo.getTaskStartTime())) {
            try {
                taskStartTime = DateUtils.parseDate(listImportVo.getTaskStartTime(), "yyyy-MM-dd");
            } catch (ParseException e) {
                logger.info("日期转换失败，请核对日期格式", e);
            }
        }
        if (!StringUtils.isEmpty(listImportVo.getTaskEndTime())) {
            try {
                taskEndTime = DateUtils.parseDate(listImportVo.getTaskEndTime(), "yyyy-MM-dd");
            } catch (ParseException e) {
                logger.info("日期转换失败，请核对日期格式", e);
            }
        }

        List<ListImportVo> list = new ArrayList<ListImportVo>();
        PageData<ListImportVo> pageData = new PageData<ListImportVo>();
        IpsnImportInfo ipsnImportInfo = new IpsnImportInfo();
        ipsnImportInfo.setApplNo(listImportVo.getApplNo());
        ipsnImportInfo.setStatus(listImportVo.getTaskState());
        ipsnImportInfo.setEndTime(taskEndTime);
        ipsnImportInfo.setStartTime(taskStartTime);
        ipsnImportInfo.setSalesBranchNo(listImportVo.getSalesBranchNo());
        ipsnImportInfo.setSalesNo(listImportVo.getSalesCode());
        ipsnImportInfo.setPclkNo(listImportVo.getOperatorNo());// 操作员工号
        ipsnImportInfo.setPclkBranchNo(listImportVo.getOperateBranch());// 操作机构号
        ipsnImportInfo.setFindSubBranchNoFlag(listImportVo.getFindSubBranchNoFlag());
        // 要查询的页数
        String pageNum = String.valueOf(queryDt.getPage() / queryDt.getRows());

        ipsnImportInfo.setPageNum(Integer.parseInt(pageNum));
        ipsnImportInfo.setRowNum(queryDt.getRows());
        RetInfoObject<IpsnImportInfo> retInfoObject = new RetInfoObject<>();
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        retInfoObject = ipsnImportStateService.GetIpsnImportResult(ipsnImportInfo);

        if (retInfoObject.getListObject().size() > 0) {
            // 获取总条数
            int sumNo = retInfoObject.getListObject().get(0).getSumPage();
            List<IpsnImportInfo> subpolNameJson = retInfoObject.getListObject();
            for (int i = 0; i < subpolNameJson.size(); i++) {
                ListImportVo listImportVo2 = new ListImportVo();
                String applNo = subpolNameJson.get(i).getApplNo();
                String hldrCustNo = subpolNameJson.get(i).getHldrCustNo();
                String hldrName = subpolNameJson.get(i).getHldrName();
                String salesBranchNo = subpolNameJson.get(i).getSalesBranchNo();
                String salesNo = subpolNameJson.get(i).getSalesNo();
                Long ipsnNum = subpolNameJson.get(i).getIpsnNum();
                Double importProgress = subpolNameJson.get(i).getImportProgress();
                String status = subpolNameJson.get(i).getStatus();
                if (StringUtils.equals("C", status)) {
                    status = "成功";
                } else if (StringUtils.equals("E", status)) {
                    status = "失败";
                } else if (StringUtils.equals("N", status)) {
                    status = "新建";
                }

                Long errorNum = subpolNameJson.get(i).getErrorNum();
                String operateBranch = subpolNameJson.get(i).getPclkBranchNo();
                String operatorName = subpolNameJson.get(i).getPclkName();
                String operatorNo = subpolNameJson.get(i).getPclkNo();
                // 前台接收的日期字符串，对接后台，做格式转换
                String endTime = null;
                String startTime = null;
                if (null != subpolNameJson.get(i).getStartTime()) {
                    startTime = DateFormatUtils.format(subpolNameJson.get(i).getStartTime(), "yyyy-MM-dd HH:mm:ss");
                }
                if (null != subpolNameJson.get(i).getEndTime()) {
                    endTime = DateFormatUtils.format(subpolNameJson.get(i).getEndTime(), "yyyy-MM-dd  HH:mm:ss");
                }

                listImportVo2.setApplNo(applNo);
                listImportVo2.setCustNo(hldrCustNo);
                listImportVo2.setCustName(hldrName);
                listImportVo2.setSalesBranchNo(salesBranchNo);
                listImportVo2.setSalesCode(salesNo);
                listImportVo2.setInsuredNum(ipsnNum);
                listImportVo2.setImportSp(importProgress);
                listImportVo2.setTaskState(status);
                listImportVo2.setErrorNum(errorNum);
                listImportVo2.setTaskStartTime(startTime);
                listImportVo2.setTaskEndTime(endTime);
                listImportVo2.setOperateBranch(operateBranch);
                listImportVo2.setOperatorName(operatorName);
                listImportVo2.setOperatorNo(operatorNo);
                list.add(listImportVo2);
            }
            pageData.setTotalCount(sumNo);
            pageData.setData(list);
        }
        return pageQueryService.tranToDataTable(queryDt.getRequestId(), pageData);
    }

    /**
     * 查询返回导出数据
     * 
     * @author jincong
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/dataExport")
    public @ResponseMessage List<ListImportVo> dataExport(@CurrentSession Session session,@RequestBody ListImportVo listImportVo) {
    	// 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        String provinceBranchNo = branchService.findProvinceBranch(sessionModel.getBranchNo());
        String mgrBranchNo = listImportVo.getSalesBranchNo();
       // 如果机构号为空,则去session里取到当前机构代码和是否包含下级机构，然后作为入参
        if(StringUtils.isEmpty(mgrBranchNo)){
            listImportVo.setSalesBranchNo(provinceBranchNo);
        }else{
            //销售机构条件控制
        	CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo1);
            String provinceBranchNo1 = branchService.findProvinceBranch(mgrBranchNo);
            //校验输入的机构号是否是操作员机构的下级机构号
            if (!StringUtils.equals(provinceBranchNo, provinceBranchNo1)) {
            	throw new BusinessException("0004", "导出失败，该操作员只能操作导出本省级及下级机构的保单");
            }
        }
        // 前台接收的日期字符串，对接后台，做格式转换
        Date taskStartTime = null;
        Date taskEndTime = null;
        if (!StringUtils.isEmpty(listImportVo.getTaskStartTime())) {
            try {
                taskStartTime = DateUtils.parseDate(listImportVo.getTaskStartTime(), "yyyy-MM-dd");
            } catch (ParseException e) {
                logger.info("日期转换失败，请核对日期格式", e);
            }
        }
        if (!StringUtils.isEmpty(listImportVo.getTaskEndTime())) {
            try {
                taskEndTime = DateUtils.parseDate(listImportVo.getTaskEndTime(), "yyyy-MM-dd");
            } catch (ParseException e) {
                logger.info("日期转换失败，请核对日期格式", e);
            }
        }

        List<ListImportVo> list = new ArrayList<ListImportVo>();
        IpsnImportInfo ipsnImportInfo = new IpsnImportInfo();
        ipsnImportInfo.setApplNo(listImportVo.getApplNo());
        ipsnImportInfo.setStatus(listImportVo.getTaskState());
        ipsnImportInfo.setEndTime(taskEndTime);
        ipsnImportInfo.setStartTime(taskStartTime);
        ipsnImportInfo.setSalesBranchNo(listImportVo.getSalesBranchNo());
        ipsnImportInfo.setSalesNo(listImportVo.getSalesCode());
        ipsnImportInfo.setPclkNo(listImportVo.getOperatorNo());// 操作员工号
        ipsnImportInfo.setPclkBranchNo(listImportVo.getOperateBranch());// 操作机构号
        ipsnImportInfo.setFindSubBranchNoFlag(listImportVo.getFindSubBranchNoFlag());

        RetInfoObject<IpsnImportInfo> retInfoObject = new RetInfoObject<>();
        CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo1);
        retInfoObject = ipsnImportStateService.getAllIpsnImportResult(ipsnImportInfo);

        if (retInfoObject.getListObject().size() > 0) {
            // 获取总条数
            List<IpsnImportInfo> subpolNameJson = retInfoObject.getListObject();
            for (int i = 0; i < subpolNameJson.size(); i++) {
                ListImportVo listImportVo2 = new ListImportVo();
                String applNo = subpolNameJson.get(i).getApplNo();
                String hldrCustNo = subpolNameJson.get(i).getHldrCustNo();
                String hldrName = subpolNameJson.get(i).getHldrName();
                String salesBranchNo = subpolNameJson.get(i).getSalesBranchNo();
                String salesNo = subpolNameJson.get(i).getSalesNo();
                Long ipsnNum = subpolNameJson.get(i).getIpsnNum();
                Double importProgress = subpolNameJson.get(i).getImportProgress();
                String status = subpolNameJson.get(i).getStatus();
                if (StringUtils.equals("C", status)) {
                    status = "成功";
                } else if (StringUtils.equals("E", status)) {
                    status = "失败";
                } else if (StringUtils.equals("N", status)) {
                    status = "新建";
                }

                Long errorNum = subpolNameJson.get(i).getErrorNum();
                String operateBranch = subpolNameJson.get(i).getPclkBranchNo();
                String operatorName = subpolNameJson.get(i).getPclkName();
                String operatorNo = subpolNameJson.get(i).getPclkNo();
                // 前台接收的日期字符串，对接后台，做格式转换
                String endTime = null;
                String startTime = null;
                if (null != subpolNameJson.get(i).getStartTime()) {
                    startTime = DateFormatUtils.format(subpolNameJson.get(i).getStartTime(), "yyyy-MM-dd HH:mm:ss");
                }
                if (null != subpolNameJson.get(i).getEndTime()) {
                    endTime = DateFormatUtils.format(subpolNameJson.get(i).getEndTime(), "yyyy-MM-dd  HH:mm:ss");
                }

                listImportVo2.setApplNo(applNo);
                listImportVo2.setCustNo(hldrCustNo);
                listImportVo2.setCustName(hldrName);
                listImportVo2.setSalesBranchNo(salesBranchNo);
                listImportVo2.setSalesCode(salesNo);
                listImportVo2.setInsuredNum(ipsnNum);
                listImportVo2.setImportSp(importProgress);
                listImportVo2.setTaskState(status);
                listImportVo2.setErrorNum(errorNum);
                listImportVo2.setTaskStartTime(startTime);
                listImportVo2.setTaskEndTime(endTime);
                listImportVo2.setOperateBranch(operateBranch);
                listImportVo2.setOperatorName(operatorName);
                listImportVo2.setOperatorNo(operatorNo);
                list.add(listImportVo2);
            }
        }
        return list;
    }

    /**
     * 查询受益人表格
     * 
     * @author xiaoYe
     * @param query
     * @param listImportIpsnInfoVo
     * @return
     */
    @RequestMapping(value = "/queryBene")
    public @ResponseMessage DataTable<ListImportBeneficiaryVo> getResponseBeneTable(QueryDt queryDt,
            ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<ListImportBeneficiaryVo> listImportBeneficiaryVos = new ArrayList<ListImportBeneficiaryVo>();
        PageData<ListImportBeneficiaryVo> pageData = new PageData<ListImportBeneficiaryVo>();
        GrpInsured grpInsured = new GrpInsured();
        String applNo = listImportIpsnInfoVo.getApplNo();
        if (applNo != null) {
            grpInsured.setApplNo(applNo);
        }
        Long ipsnNo = listImportIpsnInfoVo.getIpsnNo();
        if (ipsnNo != null) {
            grpInsured.setIpsnNo(ipsnNo);
        }
        String custNo = listImportIpsnInfoVo.getCustNo();
        if (custNo != null) {
            grpInsured.setIpsnCustNo(custNo);
        }
        GrpInsured grpInsuredInfo = grpInsuredService.queryGrpInsured(grpInsured);
        // 受益人信息
        for (int i = 0; i < grpInsuredInfo.getBnfrInfoList().size(); i++) {
            ListImportBeneficiaryVo listImportBeneficiaryVo = new ListImportBeneficiaryVo();
            listImportBeneficiaryVo.setBirthDate(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrBirthDate());
            listImportBeneficiaryVo.setIdNo(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrIdNo());
            listImportBeneficiaryVo.setIdTypeTb(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrIdType());
            listImportBeneficiaryVo.setBenefitOrder(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrLevel());
            listImportBeneficiaryVo.setName(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrName());
            listImportBeneficiaryVo.setBeneAmount(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrProfit());
            listImportBeneficiaryVo.setRelToIpsnTb(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrRtIpsn());
            listImportBeneficiaryVo.setSex(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrSex());
            listImportBeneficiaryVos.add(listImportBeneficiaryVo);
        }
        pageData.setTotalCount(listImportBeneficiaryVos.size());
        pageData.setData(listImportBeneficiaryVos);
        return pageQueryService.tranToDataTable(queryDt.getRequestId(), pageData);
    }

    /**
     * 获取taskID,businessKey返回到前台
     * 
     * @author xiaoYe
     * @param query
     * @param businessKey
     *            , taskId
     * @return
     */
    @RequestMapping(value = "/comback")
    public @ResponseMessage ListImportPageVo comback(@RequestBody TaskInfo taskInfo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);

        // 获取grpInsurApplServer服务的上下文
        grpInsurApplServer = SpringContextHolder.getBean("grpInsurApplServer");
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer.searchInsurApplByBusinessKey(taskInfo.getBizNo());
        // GrpInsurApplBo grpInsurApplBo =
        // grpInsurApplServer.searchInsurApplByBusinessKey("7896321478521473");
        ListImportPageVo listImportPageVo = new ListImportPageVo();
        List<ListImportBsInfoVo> bsInfoVos = new ArrayList<ListImportBsInfoVo>();
        for (Policy policy : grpInsurApplBo.getGrpInsurAppl().getApplState().getPolicyList()) {
            List<ListImportSubPolVo> listImportSubPolVos = new ArrayList<ListImportSubPolVo>();
            ListImportBsInfoVo bsInfoVo = new ListImportBsInfoVo();
            bsInfoVo.setPolCode(policy.getPolCode());
            bsInfoVo.setPolName(policy.getPolNameChn());
            // 责任信息
            if (null != policy.getSubPolicyList()) {
                for (SubPolicy subPolicy : policy.getSubPolicyList()) {
                    ListImportSubPolVo listImportSubPolVo = new ListImportSubPolVo();
                    listImportSubPolVo.setSubPolCode(subPolicy.getSubPolCode());
                    listImportSubPolVo.setSubPolName(subPolicy.getSubPolName());
                    listImportSubPolVos.add(listImportSubPolVo);
                }
            }
            bsInfoVo.setListImportSubPolVos(listImportSubPolVos);
            bsInfoVos.add(bsInfoVo);
        }
        List<InsuredGroupModalVo> insuredGroupModalVos = new ArrayList<>();
        for (IpsnStateGrp ipsnStateGrp : grpInsurApplBo.getGrpInsurAppl().getIpsnStateGrpList()) {
            InsuredGroupModalVo insuredGroupModalVo = new InsuredGroupModalVo();
            insuredGroupModalVo.setIpsnGrpName(ipsnStateGrp.getIpsnGrpName());
            insuredGroupModalVo.setIpsnGrpNo(ipsnStateGrp.getIpsnGrpNo());
            insuredGroupModalVo.setIpsnGrpType(ipsnStateGrp.getIpsnGrpType());
            insuredGroupModalVo.setIpsnGrpNum(ipsnStateGrp.getIpsnGrpNum());
            insuredGroupModalVo.setGenderRadio(ipsnStateGrp.getGenderRadio());
            insuredGroupModalVo.setGsRate(ipsnStateGrp.getGsPct());
            insuredGroupModalVo.setSsRate(ipsnStateGrp.getSsPct());
            insuredGroupModalVo.setIpsnOccSubclsCode(ipsnStateGrp.getIpsnOccCode());
            insuredGroupModalVo.setOccClassCode(ipsnStateGrp.getOccClassCode());
            if (ipsnStateGrp.getGrpPolicyList() == null) {
                continue;
            }
            List<InsuranceInfoVo> insuranceInfoVos = new ArrayList<>();
            for (GrpPolicy grpPolicy : ipsnStateGrp.getGrpPolicyList()) {
                InsuranceInfoVo insuranceInfoVo = new InsuranceInfoVo();
                insuranceInfoVo.setFaceAmnt(grpPolicy.getFaceAmnt());
                insuranceInfoVo.setMrCode(grpPolicy.getMrCode());
                insuranceInfoVo.setPolCode(grpPolicy.getPolCode());
                insuranceInfoVo.setPremium(grpPolicy.getPremium());
                insuranceInfoVo.setPremRate(grpPolicy.getPremRate());
                insuranceInfoVo.setRecDisount(grpPolicy.getPremDiscount());
                insuranceInfoVo.setStdPremium(grpPolicy.getStdPremium());
                insuranceInfoVos.add(insuranceInfoVo);
            }
            insuredGroupModalVo.setInsuranceInfoVos(insuranceInfoVos);
            insuredGroupModalVos.add(insuredGroupModalVo);
        }
        listImportPageVo.setInsuredGroupModalVos(insuredGroupModalVos);
        listImportPageVo.setBsInfoVo(bsInfoVos);
        listImportPageVo.setTaskInfo(taskInfo);
        listImportPageVo.setCntrType(grpInsurApplBo.getGrpInsurAppl().getCntrType());// 保单类型
        listImportPageVo.setPremSource(grpInsurApplBo.getGrpInsurAppl().getPaymentInfo().getPremSource());// 保费来源
        return listImportPageVo;

    }

    /**
     * 全部录入完成
     * 
     * @author xiaoYe
     * @param session
     *            , applNo
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/submitAll")
    public @ResponseMessage RetInfo getSubmitAllSign(@CurrentSession Session session, @RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        // 获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNodePra traceNodePra = new TraceNodePra();
        String[] s = applNo.split(",");
        String businessKey = s[0];
        String taskId = s[1];
        String procStat = s[2];
        traceNodePra.setApplNo(businessKey);
        traceNodePra.setTaskId(taskId);
        TraceNode traceNode = new TraceNode();
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNode.setProcStat(procStat);// 清汇or团单 导入完成
        traceNodePra.setTraceNode(traceNode);
        // 调用全部录入完成服务
        RetInfo retInfo = grpInsuredService.addAllGrpInsured(traceNodePra);

        return retInfo;
    }

    /**
     * 清单文件导入
     * 
     * @param request
     * @author xiaoYe
     * @return files
     * @date 2016-9-22 19 :17:33
     */
    @RequestMapping(value = "/upload/{applNo}", method = RequestMethod.POST)
    @ResponseMessage
    public RetInfo upload(@CurrentSession Session session, MultipartFile multipartFile,
            @PathVariable("applNo") String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        String pclkBranchNo = sessionModel.getBranchNo();// 机构代码
        String pclkName = sessionModel.getName();// 操作员姓名
        String pclkNo = sessionModel.getClerkNo();// 操作员工号
        InputStream in = null;
        OutputStream out = null;
        RetInfo retInfo = null;
        String sourceDirectory = PropertiesUtils.getProperty("fs.file.exceldir");
        try {
            in = multipartFile.getInputStream();
            File parentfile = new File(sourceDirectory);
            if (!parentfile.exists()) {
                parentfile.mkdirs();
            }
            Date date = new Date();
            DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            String time = format.format(date);
            String fileResource = null;
            String[] s = applNo.split(",");
            String status = s[0];
            String businessKey = s[1];
            String taskId = s[2];
            // 获取上传的文件名
            String fileName = multipartFile.getOriginalFilename();

            if (fileName.endsWith("xlsx")) {
                // 2007
                fileResource = parentfile + File.separator + businessKey + "-" + time + ".xlsx";

            } else if (fileName.endsWith("xls")) {
                // 2003
                fileResource = parentfile + File.separator + businessKey + "-" + time + ".xls";
            } else {
                fileResource = parentfile + File.separator + businessKey + "-" + time + ".csv";
            }

            out = new FileOutputStream(new File(fileResource));
            byte[] b = new byte[1024];
            int i;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, i);
            }
            out.flush();
            String resourceId = fileStoreService.addResource(new File(fileResource));
            Map<String, String> fileMap = new HashMap<>();
            fileMap.put("filename", resourceId);
            fileMap.put("applNo", businessKey);
            fileMap.put("status", status);
            fileMap.put("taskId", taskId);
            fileMap.put("pclkBranchNo", pclkBranchNo);
            fileMap.put("pclkName", pclkName);
            fileMap.put("pclkNo", pclkNo);
            retInfo = ipsnImpAppliForInterService.applyIpsnImport(fileMap);
        } catch (Exception e) {
            logger.error("异常", e);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    logger.error("IO异常", e);
                }
            }
        }
        return retInfo;
    }

    /**
     * 清单导入进度查询
     * 
     * @author jincong
     * @param session
     *            , applNo
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/progressQuery")
    public @ResponseMessage ListImportVo progressQuery(@RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        IpsnImportInfo ipsnImportInfo = new IpsnImportInfo();
        ipsnImportInfo.setApplNo(applNo);
        RetInfoObject<IpsnImportInfo> retInfoObject = ipsnImportStateService.GetIpsnImportResult(ipsnImportInfo);
        if (null != retInfoObject.getListObject() && retInfoObject.getListObject().size() > 0) {
            ipsnImportInfo = retInfoObject.getListObject().get(0);
        }
        ListImportVo listImportVo = new ListImportVo();
        listImportVo.setInsuredNum(ipsnImportInfo.getIpsnNum());
        listImportVo.setImportNum(ipsnImportInfo.getImportNum());
        listImportVo.setThisErrorNum(ipsnImportInfo.getErrorNum());
        listImportVo.setThisImportNum(ipsnImportInfo.getThisImportNum());
        listImportVo.setThisIpsnNum(ipsnImportInfo.getThisIpsnNum());
        listImportVo.setTaskState(ipsnImportInfo.getStatus());
        return listImportVo;
    }

    /**
     * 提交团单信息
     * 
     * @author
     * @param query
     * @param grpInsurApplVo
     * @return reviseGrpInsuredContent
     */
    @RequestMapping(value = "/reviseGrpInsured")
    public @ResponseMessage RetInfo reviseGrpInsuredContent(@CurrentSession Session session,
            @RequestBody ListImportPageVo listImportPageVo) {
        // 报文头
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        // 被保人信息
        GrpInsured grpInsured = new GrpInsured();
        // 投保人信息
        HldrInfo hldrInfo = new HldrInfo();
        // 投保人信息
        if (listImportPageVo.getHldrInfoVo() != null) {
            hldrInfo.setHldrName(listImportPageVo.getHldrInfoVo().getHldrName());
            hldrInfo.setHldrSex(listImportPageVo.getHldrInfoVo().getHldrSex());
            hldrInfo.setHldrIdType(listImportPageVo.getHldrInfoVo().getHldrIdType());
            hldrInfo.setHldrIdNo(listImportPageVo.getHldrInfoVo().getHldrIdNo());
            hldrInfo.setHldrBirthDate(listImportPageVo.getHldrInfoVo().getHldrBirth());
            hldrInfo.setHldrMobilePhone(listImportPageVo.getHldrInfoVo().getHldrPhone());
            grpInsured.setHldrInfo(hldrInfo);
        }
        // 被保人信息
        grpInsured.setApplNo(listImportPageVo.getIpsnInfoVo().getApplNo());
        grpInsured.setIpsnPartyId(listImportPageVo.getIpsnInfoVo().getCustNo());
        grpInsured.setIpsnNo(listImportPageVo.getIpsnInfoVo().getIpsnNo());
        grpInsured.setIpsnName(listImportPageVo.getIpsnInfoVo().getName());
        grpInsured.setIpsnRtMstIpsn(listImportPageVo.getIpsnInfoVo().getRelToIpsn());
        grpInsured.setIpsnType(listImportPageVo.getIpsnInfoVo().getJoinIpsnFlag());
        grpInsured.setIpsnIdNo(listImportPageVo.getIpsnInfoVo().getIdNo());
        grpInsured.setIpsnSex(listImportPageVo.getIpsnInfoVo().getIpsursex());
        grpInsured.setIpsnBirthDate(listImportPageVo.getIpsnInfoVo().getBirthDate());
        grpInsured.setIpsnAge(listImportPageVo.getIpsnInfoVo().getAge());
        grpInsured.setIpsnIdType(listImportPageVo.getIpsnInfoVo().getIdType());
        grpInsured.setIpsnOccCode(listImportPageVo.getIpsnInfoVo().getOccupationalCodes());
        grpInsured.setIpsnOccClassLevel(listImportPageVo.getIpsnInfoVo().getRiskLevel());
        grpInsured.setLevelCode(listImportPageVo.getIpsnInfoVo().getGroupLevelCode());
        grpInsured.setIpsnCompanyLoc(listImportPageVo.getIpsnInfoVo().getWorkAddress());
        grpInsured.setIpsnRefNo(listImportPageVo.getIpsnInfoVo().getWorkNo());
        grpInsured.setInServiceFlag(listImportPageVo.getIpsnInfoVo().getOnJobFlag());
        grpInsured.setIpsnSss(listImportPageVo.getIpsnInfoVo().getMedicalInsurFlag());
        grpInsured.setIpsnSsc(listImportPageVo.getIpsnInfoVo().getMedicalInsurCode());
        grpInsured.setIpsnSsn(listImportPageVo.getIpsnInfoVo().getMedicalInsurNo());
        grpInsured.setNotificaStat(listImportPageVo.getIpsnInfoVo().getHealthFlag());
        grpInsured.setIpsnEmail(listImportPageVo.getIpsnInfoVo().getEmail());
        grpInsured.setIpsnPayAmount(listImportPageVo.getIpsnInfoVo().getCharge());
        grpInsured.setRemark(listImportPageVo.getIpsnInfoVo().getRemarks());
        // 子要约
        List<SubState> listSubState = new ArrayList<>();
        if (listImportPageVo.getBsInfoVo() != null) {
            for (int i = 0; i < listImportPageVo.getBsInfoVo().size(); i++) {
                for (int j = 0; j < listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().size(); j++) {
                    SubState subState = new SubState();
                    Long groupCodeTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getGroupCode();
                    subState.setClaimIpsnGrpNo(groupCodeTb);
                    Double amountTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getAmount();
                    subState.setFaceAmnt(amountTb);
                    Double premiumTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getPremium();
                    subState.setPremium(premiumTb);
                    String polCodeTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getSubPolCode();
                    subState.setPolCode(polCodeTb);
                    String coverageTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getSubPolName();
                    subState.setPolNameChn(coverageTb);
                    listSubState.add(subState);
                }
            }
        }
        grpInsured.setSubStateList(listSubState);
        // 受益人
        List<BnfrInfo> listBnfrInfo = new ArrayList<>();
        if (listImportPageVo.getBeneficiaryInfo() != null) {
            for (int i = 0; i < listImportPageVo.getBeneficiaryInfo().size(); i++) {
                BnfrInfo bnfrInfo = new BnfrInfo();
                Date birthDate = listImportPageVo.getBeneficiaryInfo().get(i).getBirthDate();
                bnfrInfo.setBnfrBirthDate(birthDate);
                String idNo = listImportPageVo.getBeneficiaryInfo().get(i).getIdNo();
                bnfrInfo.setBnfrIdNo(idNo);
                String idType = listImportPageVo.getBeneficiaryInfo().get(i).getIdTypeTb();
                bnfrInfo.setBnfrIdType(idType);
                Long benefitOrder = listImportPageVo.getBeneficiaryInfo().get(i).getBenefitOrder();
                bnfrInfo.setBnfrLevel(benefitOrder);
                String name = listImportPageVo.getBeneficiaryInfo().get(i).getName();
                bnfrInfo.setBnfrName(name);
                Double beneAmount = listImportPageVo.getBeneficiaryInfo().get(i).getBeneAmount();
                bnfrInfo.setBnfrProfit(beneAmount);
                String relToIpsn = listImportPageVo.getBeneficiaryInfo().get(i).getRelToIpsnTb();
                bnfrInfo.setBnfrRtIpsn(relToIpsn);
                String sex = listImportPageVo.getBeneficiaryInfo().get(i).getSex();
                bnfrInfo.setBnfrSex(sex);
                listBnfrInfo.add(bnfrInfo);
            }
        }
        // 操作轨迹
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNode traceNode = new TraceNode();
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNode.setProcStat(NEW_APPL_STATE.REVISE.getKey());// 清汇导入完成
        GrpInsuredPara grpInsuredPara = new GrpInsuredPara();
        grpInsuredPara.setGrpInsured(grpInsured);
        grpInsuredPara.setTraceNode(traceNode);

        RetInfo retInfo = restfulinsurApplContentRevise.reviseGrpInsuredContent(grpInsuredPara);
        return retInfo;
    }

    /**
     * 导入模板下载
     * 
     * @author zhoushoubo
     * @param flg
     * @param id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/downloadTemplate/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> download(@PathVariable("id") String flg) throws IOException {
        StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsnlst.template.dir"));
        File file;
        // 需要考虑容错机制，如果flg不为g或者s，怎么处理
        if ("g".equals(flg)) {
            file = new File(sb + File.separator + "团单清单导入模版.xlsx");
        } else {
            file = new File(sb + File.separator + "清汇清单导入模版.xlsx");
        }
        // 处理显示中文文件名的问题
        String fileName = new String(file.getName().getBytes("utf-8"), "ISO-8859-1");
        // 设置请求头内容,告诉浏览器代开下载窗口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.CREATED);
    }
    
    /**
     * 停止导清单任务
     * 
     * @author jincong
     * @param str
     * @return RetInfo
     */
    @RequestMapping(value = "/stopImport")
    public @ResponseMessage RetInfo getDeleteInsuredInfo(@RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        RetInfo retInfo = new RetInfo();
        try{
        	retInfo = batchJobControlService.stopImportIpsnListBatchJob(applNo);	
        }catch(Exception e){
            logger.info(e.getMessage(), e);
            retInfo.setErrMsg("批作业停止失败！");
        }
        return retInfo;
    }

    /**
     * 修改团单信息
     * 
     * @author JiangBoMeng
     * @param update
     * @param listImportPageVo
     * @return
     */
    @RequestMapping(value = "/update")
    public @ResponseMessage RetInfo grpInsurApplUpdate(@RequestBody ListImportPageVo listImportPageVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsured grpInsured = new GrpInsured();
        HldrInfo hldrInfo = new HldrInfo();
        // 投保人信息
        if (listImportPageVo.getHldrInfoVo() != null) {
            hldrInfo.setHldrName(listImportPageVo.getHldrInfoVo().getHldrName());
            hldrInfo.setHldrSex(listImportPageVo.getHldrInfoVo().getHldrSex());
            hldrInfo.setHldrIdType(listImportPageVo.getHldrInfoVo().getHldrIdType());
            hldrInfo.setHldrIdNo(listImportPageVo.getHldrInfoVo().getHldrIdNo());
            hldrInfo.setHldrBirthDate(listImportPageVo.getHldrInfoVo().getHldrBirth());
            hldrInfo.setHldrMobilePhone(listImportPageVo.getHldrInfoVo().getHldrPhone());
            grpInsured.setHldrInfo(hldrInfo);
        }
        // 被保人信息
        if (listImportPageVo.getIpsnInfoVo() != null) {
            grpInsured.setApplNo(listImportPageVo.getIpsnInfoVo().getApplNo());
            grpInsured.setIpsnPartyId(listImportPageVo.getIpsnInfoVo().getCustNo());// 客户号
            grpInsured.setIpsnNo(listImportPageVo.getIpsnInfoVo().getIpsnNo());// 被保险人编号
            grpInsured.setIpsnName(listImportPageVo.getIpsnInfoVo().getName());// 被保险人姓名
            // 被保险人类型
            if ("Y".equals(listImportPageVo.getIpsnInfoVo().getJoinIpsnFlag())) {
                grpInsured.setIpsnType("A");// 被保险人类型
            } else {
                grpInsured.setIpsnType("I");// 被保险人类型
            }
            grpInsured.setIpsnIdNo(listImportPageVo.getIpsnInfoVo().getIdNo());// 证件号码
            grpInsured.setIpsnSex(listImportPageVo.getIpsnInfoVo().getIpsursex());// 被保人性别
            grpInsured.setIpsnBirthDate(listImportPageVo.getIpsnInfoVo().getBirthDate());// 被保人出生日期
            grpInsured.setIpsnAge(listImportPageVo.getIpsnInfoVo().getAge());// 被保人年龄
            grpInsured.setIpsnIdType(listImportPageVo.getIpsnInfoVo().getIdType());// 被保人证件类型
            grpInsured.setIpsnOccCode(listImportPageVo.getIpsnInfoVo().getOccupationalCodes());// 职业代码
            grpInsured.setIpsnOccClassLevel(listImportPageVo.getIpsnInfoVo().getRiskLevel());// 职业风险等级
            grpInsured.setLevelCode(listImportPageVo.getIpsnInfoVo().getGroupLevelCode());// 组织层次代码
            grpInsured.setIpsnCompanyLoc(listImportPageVo.getIpsnInfoVo().getWorkAddress());// 被保人工作地点
            grpInsured.setIpsnRefNo(listImportPageVo.getIpsnInfoVo().getWorkNo());// 被保人工号
            grpInsured.setInServiceFlag(listImportPageVo.getIpsnInfoVo().getOnJobFlag());// 是否在职
            grpInsured.setIpsnSss(listImportPageVo.getIpsnInfoVo().getMedicalInsurFlag());// 医保标识
            grpInsured.setIpsnSsc(listImportPageVo.getIpsnInfoVo().getMedicalInsurCode());// 医保代码
            grpInsured.setIpsnSsn(listImportPageVo.getIpsnInfoVo().getMedicalInsurNo());// 医保编号
            grpInsured.setNotificaStat(listImportPageVo.getIpsnInfoVo().getHealthFlag());// 是否有异常告知
            grpInsured.setIpsnEmail(listImportPageVo.getIpsnInfoVo().getEmail());// 被保人邮箱
            grpInsured.setIpsnPayAmount(listImportPageVo.getIpsnInfoVo().getCharge());// 个人交费金额
            grpInsured.setRemark(listImportPageVo.getIpsnInfoVo().getRemarks());// 备注
            grpInsured.setFeeGrpNo(listImportPageVo.getIpsnInfoVo().getFeeGrpNo());// 被保人属组号
            grpInsured.setIpsnMobilePhone(listImportPageVo.getIpsnInfoVo().getPhone());// 被保人手机号
            grpInsured.setMasterIpsnNo(listImportPageVo.getIpsnInfoVo().getMainIpsnNo());// 主被保险人编号
            grpInsured.setIpsnRtMstIpsn(listImportPageVo.getIpsnInfoVo().getRelToIpsn());// 与主被保险人关系
            grpInsured.setRelToHldr(listImportPageVo.getIpsnInfoVo().getRelToHldr());// 与投保人关系
            grpInsured.setTyNetPayAmnt(listImportPageVo.getIpsnInfoVo().getTyNetPayAmnt());// 同业公司人身保险保额合计
            grpInsured.setGrpPayAmount(listImportPageVo.getIpsnInfoVo().getGrpPayAmount());// 单位交费金额
        }
        // 缴费账户信息
        if (listImportPageVo.getAccInfoList() != null) {
            List<AccInfo> list = new ArrayList<AccInfo>();
            for (AccInfoVo accInfovo : listImportPageVo.getAccInfoList()) {
            	AccInfo accInfo = new AccInfo();
                accInfo.setSeqNo(accInfovo.getSeqNo());// 账户顺序
                accInfo.setIpsnPayAmnt(accInfovo.getIpsnPayAmnt());// 个人扣款金额
                accInfo.setIpsnPayPct(null == accInfovo.getIpsnPayPct() ? null : accInfovo.getIpsnPayPct() / 100);// 个人账户交费比例
                accInfo.setBankAccName(accInfovo.getBankAccName());// 缴费账户
                accInfo.setBankCode(accInfovo.getBankCode());// 缴费银行
                accInfo.setBankAccNo(accInfovo.getBankAccNo());// 缴费账号
                list.add(accInfo);
            }
            grpInsured.setAccInfoList(list);
        }
        // 要约信息
        List<SubState> listSubState = new ArrayList<>();
        if (listImportPageVo.getBsInfoVo() != null) {
            for (int i = 0; i < listImportPageVo.getBsInfoVo().size(); i++) {
                for (int j = 0; j < listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().size(); j++) {
                    SubState subState = new SubState();
                    String SubPolCode;
                    // 如果责任代码为空，把险种代码传过去
                    if (StringUtils.isEmpty(
                            listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolCode())) {
                        SubPolCode = listImportPageVo.getBsInfoVo().get(i).getPolCode();
                    } else {
                        SubPolCode = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                                .getSubPolCode();
                    }
                    subState.setPolCode(SubPolCode);
                    Long groupCodeTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getGroupCode();
                    subState.setClaimIpsnGrpNo(groupCodeTb);
                    Double amountTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getAmount();
                    subState.setFaceAmnt(amountTb);
                    Double premiumTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getPremium();
                    subState.setPremium(premiumTb);
                    String SubPolName = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j)
                            .getSubPolName();
                    subState.setPolNameChn(SubPolName);
                    listSubState.add(subState);
                }
            }
        }
        grpInsured.setSubStateList(listSubState);
        // 受益人信息
        List<BnfrInfo> listBnfrInfo = new ArrayList<>();
        if (listImportPageVo.getBeneficiaryInfo() != null) {
            for (int i = 0; i < listImportPageVo.getBeneficiaryInfo().size(); i++) {
                BnfrInfo bnfrInfo = new BnfrInfo();
                Date birthDate = listImportPageVo.getBeneficiaryInfo().get(i).getBirthDate();
                bnfrInfo.setBnfrBirthDate(birthDate);
                String idNo = listImportPageVo.getBeneficiaryInfo().get(i).getIdNo();
                bnfrInfo.setBnfrIdNo(idNo);
                String idType = listImportPageVo.getBeneficiaryInfo().get(i).getIdTypeTb();
                bnfrInfo.setBnfrIdType(idType);
                Long benefitOrder = listImportPageVo.getBeneficiaryInfo().get(i).getBenefitOrder();
                bnfrInfo.setBnfrLevel(benefitOrder);
                String name = listImportPageVo.getBeneficiaryInfo().get(i).getName();
                bnfrInfo.setBnfrName(name);
                Double beneAmount = listImportPageVo.getBeneficiaryInfo().get(i).getBeneAmount() / 100;
                bnfrInfo.setBnfrProfit(beneAmount);
                String relToIpsn = listImportPageVo.getBeneficiaryInfo().get(i).getRelToIpsnTb();
                bnfrInfo.setBnfrRtIpsn(relToIpsn);
                String sex = listImportPageVo.getBeneficiaryInfo().get(i).getSex();
                bnfrInfo.setBnfrSex(sex);
                listBnfrInfo.add(bnfrInfo);
            }
        }
        grpInsured.setBnfrInfoList(listBnfrInfo);
        RetInfo retInfo = grpInsuredService.updateGrpInsured(grpInsured);
        return retInfo;
    }

    /**
     * 被保险人信息查询（上一人 ， 下一人）
     * 
     * @author xiaoYe
     * @param listImportIpsnInfoVo
     * @return
     */
    @RequestMapping(value = "/queryNextInsured")
    public @ResponseMessage ListImportPageVo getNextInsured(@RequestBody ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("applNo", listImportIpsnInfoVo.getApplNo());
        if (null != listImportIpsnInfoVo.getIpsnNo()) {
            map.put("ipsnNo", listImportIpsnInfoVo.getIpsnNo());
        }
        // 上一个或者下一个的标示
        map.put("flag", listImportIpsnInfoVo.getFlag());

        GrpInsured grpInsuredInfo = grpInsuredService.queryPreOrNextGrpInsured(map);
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurApplBo grpInsurApplBo = grpInsurApplServer
                .searchInsurApplByBusinessKey(listImportIpsnInfoVo.getApplNo());
        logger.debug("调用后台服务查询的保单类型是" + grpInsurApplBo.getGrpInsurAppl().getCntrType());
        // 投保人信息
        ListImportPageVo listImportPageVo = new ListImportPageVo();
        listImportPageVo.setCntrType(grpInsurApplBo.getGrpInsurAppl().getCntrType());
        if (null != grpInsuredInfo) {
            if (null != grpInsuredInfo.getHldrInfo()) {
                HldrInfo hldrInfo = grpInsuredInfo.getHldrInfo();
                ListImportHldrInfoVo hldrInfoVo = new ListImportHldrInfoVo();
                hldrInfoVo.setHldrName(hldrInfo.getHldrName());
                hldrInfoVo.setHldrSex(hldrInfo.getHldrSex());
                hldrInfoVo.setHldrIdType(hldrInfo.getHldrIdType());
                hldrInfoVo.setHldrIdNo(hldrInfo.getHldrIdNo());
                hldrInfoVo.setHldrBirth(hldrInfo.getHldrBirthDate());
                hldrInfoVo.setHldrPhone(hldrInfo.getHldrMobilePhone());
                listImportPageVo.setHldrInfoVo(hldrInfoVo);
            }
        }

        // 被保险人信息
        if (grpInsuredInfo != null) {
            listImportIpsnInfoVo.setIpsnNo(grpInsuredInfo.getIpsnNo());// 被保险人编号
            listImportIpsnInfoVo.setApplNo(grpInsuredInfo.getApplNo());
            listImportIpsnInfoVo.setCustNo(grpInsuredInfo.getIpsnPartyId());
            listImportIpsnInfoVo.setName(grpInsuredInfo.getIpsnName());
            listImportIpsnInfoVo.setRelToIpsn(grpInsuredInfo.getRelToHldr());
            if ("A".equals(grpInsuredInfo.getIpsnType())) {
                listImportIpsnInfoVo.setJoinIpsnFlag("Y");
            } else {
                listImportIpsnInfoVo.setJoinIpsnFlag("N");
            }
            listImportIpsnInfoVo.setIdNo(grpInsuredInfo.getIpsnIdNo());
            listImportIpsnInfoVo.setIpsursex(grpInsuredInfo.getIpsnSex());
            listImportIpsnInfoVo.setBirthDate(grpInsuredInfo.getIpsnBirthDate());
            listImportIpsnInfoVo.setAge(grpInsuredInfo.getIpsnAge());
            listImportIpsnInfoVo.setIdType(grpInsuredInfo.getIpsnIdType());
            listImportIpsnInfoVo.setOccupationalCodes(grpInsuredInfo.getIpsnOccCode());
            listImportIpsnInfoVo.setOccupationName(grpInsuredInfo.getIpsnName());
            listImportIpsnInfoVo.setRiskLevel(grpInsuredInfo.getIpsnOccClassLevel());// 职业风险等级
            listImportIpsnInfoVo.setGroupLevelCode(grpInsuredInfo.getLevelCode());// 组织层次代码
            listImportIpsnInfoVo.setWorkAddress(grpInsuredInfo.getIpsnCompanyLoc());
            listImportIpsnInfoVo.setWorkNo(grpInsuredInfo.getIpsnRefNo());
            listImportIpsnInfoVo.setOnJobFlag(grpInsuredInfo.getInServiceFlag());
            listImportIpsnInfoVo.setMedicalInsurCode(grpInsuredInfo.getIpsnSsc());
            listImportIpsnInfoVo.setMedicalInsurFlag(grpInsuredInfo.getIpsnSss());
            listImportIpsnInfoVo.setMedicalInsurNo(grpInsuredInfo.getIpsnSsn());
            listImportIpsnInfoVo.setHealthFlag(grpInsuredInfo.getNotificaStat());
            listImportIpsnInfoVo.setEmail(grpInsuredInfo.getIpsnEmail());
            listImportIpsnInfoVo.setCharge(grpInsuredInfo.getIpsnPayAmount());
            listImportIpsnInfoVo.setRemarks(grpInsuredInfo.getRemark());
            listImportIpsnInfoVo.setPhone(grpInsuredInfo.getIpsnMobilePhone());// 被保险人手机号码
            listImportIpsnInfoVo.setFeeGrpNo(grpInsuredInfo.getFeeGrpNo());// 收费属组号
            listImportIpsnInfoVo.setMainIpsnNo(grpInsuredInfo.getMasterIpsnNo());// 主被保险人编号
            listImportIpsnInfoVo.setRelToIpsn(grpInsuredInfo.getIpsnRtMstIpsn());// 与主被保险人关系
            listImportIpsnInfoVo.setRelToHldr(grpInsuredInfo.getRelToHldr());// 与投保人关系
            listImportIpsnInfoVo.setTyNetPayAmnt(grpInsuredInfo.getTyNetPayAmnt());// 同业公司人身保险保额合计
            listImportIpsnInfoVo.setGrpPayAmount(grpInsuredInfo.getGrpPayAmount());// 单位交费金额
            listImportPageVo.setIpsnInfoVo(listImportIpsnInfoVo);
            // 缴费账户信息
            if (grpInsuredInfo.getAccInfoList() != null) {
                List<AccInfoVo> list = new ArrayList<>();
                for (AccInfo ac : grpInsuredInfo.getAccInfoList()) {
                    AccInfoVo accInfoVo = new AccInfoVo();
                    accInfoVo.setSeqNo(ac.getSeqNo());// 账户顺序
                    accInfoVo.setIpsnPayAmnt(ac.getIpsnPayAmnt());// 个人扣款金额
                    accInfoVo.setIpsnPayPct(ac.getIpsnPayPct() * 100);// 个人账户交费比例
                    accInfoVo.setBankAccName(ac.getBankAccName());// 缴费账户
                    accInfoVo.setBankCode(ac.getBankCode());// 缴费银行
                    accInfoVo.setBankAccNo(ac.getBankAccNo());// 缴费账号
                    list.add(accInfoVo);
                }
                listImportPageVo.setAccInfoList(list);// 缴费账户信息
            }
            List<ListImportBeneficiaryVo> listImportBeneficiaryVos = new ArrayList<>();
            if (null != grpInsuredInfo.getBnfrInfoList()) {
                for (int i = 0; i < grpInsuredInfo.getBnfrInfoList().size(); i++) {
                    ListImportBeneficiaryVo listImportBeneficiaryVo = new ListImportBeneficiaryVo();
                    listImportBeneficiaryVo.setBirthDate(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrBirthDate());
                    listImportBeneficiaryVo.setIdNo(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrIdNo());
                    listImportBeneficiaryVo.setIdTypeTb(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrIdType());
                    listImportBeneficiaryVo.setBenefitOrder(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrLevel());
                    listImportBeneficiaryVo.setName(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrName());
                    listImportBeneficiaryVo
                            .setBeneAmount(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrProfit() * 100);
                    listImportBeneficiaryVo.setRelToIpsnTb(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrRtIpsn());
                    listImportBeneficiaryVo.setSex(grpInsuredInfo.getBnfrInfoList().get(i).getBnfrSex());
                    listImportBeneficiaryVos.add(listImportBeneficiaryVo);
                }
                listImportPageVo.setBeneficiaryInfo(listImportBeneficiaryVos);
            }
            // 要约信息
            List<ListImportBsInfoVo> listImportBsInfoVos = new ArrayList<>();
            for (int j = 0; j < grpInsuredInfo.getSubStateList().size(); j++) {
                ListImportBsInfoVo listImportBsInfoVo;
                List<ListImportSubPolVo> listImportSubPolVos;
                if (j == 0 || !StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),
                        listImportBsInfoVos.get(listImportBsInfoVos.size() - 1).getPolCode())) {
                    listImportBsInfoVo = new ListImportBsInfoVo();
                    listImportSubPolVos = new ArrayList<>();
                    listImportBsInfoVo.setPolCode(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3));
                    for (Policy policy : grpInsurApplBo.getGrpInsurAppl().getApplState().getPolicyList()) {
                        if (StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),
                                policy.getPolCode())) {
                            listImportBsInfoVo.setPolName(policy.getPolNameChn());
                            break;
                        }
                    }
                } else {
                    listImportBsInfoVo = listImportBsInfoVos.get(listImportBsInfoVos.size() - 1);
                    listImportSubPolVos = listImportBsInfoVo.getListImportSubPolVos();
                }
                ListImportSubPolVo listImportSubPolVo = new ListImportSubPolVo();
                listImportSubPolVo.setSubPolCode(grpInsuredInfo.getSubStateList().get(j).getPolCode());
                listImportSubPolVo.setSubPolName(grpInsuredInfo.getSubStateList().get(j).getPolNameChn());
                listImportSubPolVo.setAmount(grpInsuredInfo.getSubStateList().get(j).getFaceAmnt());
                listImportSubPolVo.setPremium(grpInsuredInfo.getSubStateList().get(j).getPremium());
                listImportSubPolVo.setGroupCode(grpInsuredInfo.getSubStateList().get(j).getClaimIpsnGrpNo());
                // 获取被保人属组名称
                for (IpsnStateGrp ipsnStateGrp : grpInsurApplBo.getGrpInsurAppl().getIpsnStateGrpList()) {
                    if (grpInsuredInfo.getSubStateList().get(j).getClaimIpsnGrpNo() == ipsnStateGrp.getIpsnGrpNo()) {
                        listImportSubPolVo.setGroupName(ipsnStateGrp.getIpsnGrpName());
                        break;
                    }
                }
                listImportSubPolVos.add(listImportSubPolVo);
                listImportBsInfoVo.setListImportSubPolVos(listImportSubPolVos);
                if (j == 0 || !StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),
                        listImportBsInfoVos.get(listImportBsInfoVos.size() - 1).getPolCode())) {
                    listImportBsInfoVos.add(listImportBsInfoVo);
                }
            }
            listImportPageVo.setBsInfoVo(listImportBsInfoVos);
        }
        return listImportPageVo;
    }
}
