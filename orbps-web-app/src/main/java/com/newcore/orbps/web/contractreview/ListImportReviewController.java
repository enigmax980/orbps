package com.newcore.orbps.web.contractreview;

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

import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.filestore.api.FileStoreService;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.task.models.TaskJumpModel;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.GrpInsurApplBo;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
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
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;
import com.newcore.orbps.service.api.GrpInsuredService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.InsurApplServices;
import com.newcore.orbps.service.api.InsuredFileCreatService;
import com.newcore.orbps.service.api.IpsnImpAppliForInterService;
import com.newcore.orbps.service.api.IpsnImportStateService;
import com.newcore.orbps.service.api.OrbpsInsurApplCvTaskServer;
import com.newcore.orbps.service.api.QueryInsurApplCvTaskServer;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;

/**
 * 清单复合页面
 * 
 * @author xiaoYe
 *
 */
@Controller
@RequestMapping("/orbps/contractReview/offlineList")
public class ListImportReviewController {
    /**
     * 日志对象.
     */
    private static Logger logger = LoggerFactory.getLogger(ListImportReviewController.class);

    @Autowired
    InsurApplServices insurApplServices;

    @Autowired
    InsurApplServer insurApplServer;

    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    OrbpsInsurApplCvTaskServer orbpsInsurApplCvTaskServer;

    @Autowired
    QueryInsurApplCvTaskServer queryInsurApplCvTaskServer;

    @Autowired
    GrpInsuredService restfulGrpInsuredService;

    @Autowired
    IpsnImportStateService ipsnImportStateService;

    @Autowired
    IpsnImpAppliForInterService ipsnImpAppliForInterService;

    @Autowired
    InsuredFileCreatService insuredFileCreatService;
    
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
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsured grpInsured = new GrpInsured();
        HldrInfo hldrInfo = new HldrInfo();
        //投保人信息
        if(listImportPageVo.getHldrInfoVo() != null){
            hldrInfo.setHldrName(listImportPageVo.getHldrInfoVo().getHldrName());
            hldrInfo.setHldrSex(listImportPageVo.getHldrInfoVo().getHldrSex());
            hldrInfo.setHldrIdType(listImportPageVo.getHldrInfoVo().getHldrIdType());
            hldrInfo.setHldrIdNo(listImportPageVo.getHldrInfoVo().getHldrIdNo());
            hldrInfo.setHldrBirthDate(listImportPageVo.getHldrInfoVo().getHldrBirth());
            hldrInfo.setHldrMobilePhone(listImportPageVo.getHldrInfoVo().getHldrPhone());
            grpInsured.setHldrInfo(hldrInfo);
            System.out.println("++++++++++++++++++++++++"+hldrInfo.toString()+"+++++++++++++++++++++++++++");
        }
     // 被保人信息
     		if (listImportPageVo.getIpsnInfoVo() != null) {
     			grpInsured.setApplNo(listImportPageVo.getIpsnInfoVo().getApplNo());
     			grpInsured.setIpsnPartyId(listImportPageVo.getIpsnInfoVo().getCustNo());//客户号
     			grpInsured.setIpsnNo(listImportPageVo.getIpsnInfoVo().getIpsnNo());//被保险人编号
     			grpInsured.setIpsnName(listImportPageVo.getIpsnInfoVo().getName());//被保险人姓名
     			// 被保险人类型
     			if("Y".equals(listImportPageVo.getIpsnInfoVo().getJoinIpsnFlag())){
     				grpInsured.setIpsnType("A");//被保险人类型
     			}else{
     				grpInsured.setIpsnType("I");//被保险人类型
     			}
     			grpInsured.setIpsnIdNo(listImportPageVo.getIpsnInfoVo().getIdNo());//证件号码
     			grpInsured.setIpsnSex(listImportPageVo.getIpsnInfoVo().getIpsursex());//被保人性别
     			grpInsured.setIpsnBirthDate(listImportPageVo.getIpsnInfoVo().getBirthDate());//被保人出生日期
     			grpInsured.setIpsnAge(listImportPageVo.getIpsnInfoVo().getAge());//被保人年龄
     			grpInsured.setIpsnIdType(listImportPageVo.getIpsnInfoVo().getIdType());//被保人证件类型
     			grpInsured.setIpsnOccCode(listImportPageVo.getIpsnInfoVo().getOccupationalCodes());//职业代码
     			grpInsured.setIpsnOccClassLevel(listImportPageVo.getIpsnInfoVo().getRiskLevel());//职业风险等级 
     			grpInsured.setLevelCode(listImportPageVo.getIpsnInfoVo().getGroupLevelCode());//组织层次代码
     			grpInsured.setIpsnCompanyLoc(listImportPageVo.getIpsnInfoVo().getWorkAddress());//被保人工作地点
     			grpInsured.setIpsnRefNo(listImportPageVo.getIpsnInfoVo().getWorkNo());//被保人工号
     			grpInsured.setInServiceFlag(listImportPageVo.getIpsnInfoVo().getOnJobFlag());//是否在职
     			grpInsured.setIpsnSss(listImportPageVo.getIpsnInfoVo().getMedicalInsurFlag());//医保标识
     			grpInsured.setIpsnSsc(listImportPageVo.getIpsnInfoVo().getMedicalInsurCode());//医保代码
     			grpInsured.setIpsnSsn(listImportPageVo.getIpsnInfoVo().getMedicalInsurNo());//医保编号
     			grpInsured.setNotificaStat(listImportPageVo.getIpsnInfoVo().getHealthFlag());//是否有异常告知
     			grpInsured.setIpsnEmail(listImportPageVo.getIpsnInfoVo().getEmail());//被保人邮箱
     			grpInsured.setIpsnPayAmount(listImportPageVo.getIpsnInfoVo().getCharge());//个人交费金额
     			grpInsured.setRemark(listImportPageVo.getIpsnInfoVo().getRemarks());//备注
     			grpInsured.setFeeGrpNo(listImportPageVo.getIpsnInfoVo().getFeeGrpNo());// 被保人属组号
     			grpInsured.setIpsnMobilePhone(listImportPageVo.getIpsnInfoVo().getPhone());//被保人手机号 24
     			grpInsured.setMasterIpsnNo(listImportPageVo.getIpsnInfoVo().getMainIpsnNo());//主被保险人编号
     			grpInsured.setIpsnRtMstIpsn(listImportPageVo.getIpsnInfoVo().getRelToIpsn());//与主被保险人关系
     			grpInsured.setRelToHldr(listImportPageVo.getIpsnInfoVo().getRelToHldr());//与投保人关系
     			grpInsured.setTyNetPayAmnt(listImportPageVo.getIpsnInfoVo().getTyNetPayAmnt());//同业公司人身保险保额合计
     			grpInsured.setGrpPayAmount(listImportPageVo.getIpsnInfoVo().getGrpPayAmount());//单位交费金额
     		}
     		//缴费账户信息
     		if(listImportPageVo.getAccInfoList() != null){
     			List<AccInfo> list = new ArrayList<AccInfo>();
     			AccInfo accInfo = new AccInfo();
     			for(AccInfoVo accInfovo : listImportPageVo.getAccInfoList()){
     				accInfo.setSeqNo(accInfovo.getSeqNo());//账户顺序
     				accInfo.setIpsnPayAmnt(accInfovo.getIpsnPayAmnt());//个人扣款金额
     				accInfo.setIpsnPayPct(accInfovo.getIpsnPayPct());//个人账户交费比例
     				accInfo.setBankAccName(accInfovo.getBankAccName());//缴费账户
     				accInfo.setBankCode(accInfovo.getBankCode());//缴费银行
     				accInfo.setBankAccNo(accInfovo.getBankAccNo());//缴费账号
     				list.add(accInfo);
     			}
     			grpInsured.setAccInfoList(list);
     		}
        //子要约信息
        List<SubState> listSubState = new ArrayList<>();
        if (listImportPageVo.getBsInfoVo() != null) {
            for (int i = 0; i < listImportPageVo.getBsInfoVo().size(); i++) {
            	for (int j = 0; j < listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().size(); j++) {
            		SubState subState = new SubState();
                  	String SubPolCode;
                  	//如果责任代码为空，把险种代码传过去
                	if(StringUtils.isEmpty(listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolCode())){
                		 SubPolCode = listImportPageVo.getBsInfoVo().get(i).getPolCode();
                	}else{
                		 SubPolCode = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolCode();
                	}
                	subState.setPolCode(SubPolCode);
                    Long groupCodeTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getGroupCode();
                    subState.setClaimIpsnGrpNo(groupCodeTb);
                    Double amountTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getAmount();
                    subState.setFaceAmnt(amountTb);
                    Double premiumTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getPremium();
                    subState.setPremium(premiumTb);
                    String coverageTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolName();
                    subState.setPolNameChn(coverageTb);
                    listSubState.add(subState);	
            	}
            
            }
        }
        grpInsured.setSubStateList(listSubState);
        //受益人信息
        List<BnfrInfo> listBnfrInfo = new ArrayList<>();
        BnfrInfo bnfrInfo = new BnfrInfo();
        if (listImportPageVo.getBeneficiaryInfo() != null) {
            for (int i = 0; i < listImportPageVo.getBeneficiaryInfo().size(); i++) {
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

        grpInsured.setBnfrInfoList(listBnfrInfo);
        RetInfo retInfo = restfulGrpInsuredService.addGrpInsured(grpInsured);

        return retInfo;
    }
    
    /**
     * 删除
     * @author xiaoYe
     * @param session , applNo
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/delete")
    public @ResponseMessage RetInfo getDeleteInsuredInfo(@RequestBody ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        
        Map<String,Object> paraMap = new HashMap<String,Object>();
        paraMap.put("applNo", listImportIpsnInfoVo.getApplNo());
        paraMap.put("ipsnNo", listImportIpsnInfoVo.getIpsnNo());
        RetInfo retInfo = restfulGrpInsuredService.removeGrpInsured(paraMap);
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
    public @ResponseMessage ListImportPageVo getQueryInsuredInfo(@RequestBody ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        GrpInsured grpInsured = new GrpInsured();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        grpInsured.setApplNo(listImportIpsnInfoVo.getApplNo());
        if(null != listImportIpsnInfoVo.getIpsnNo()){
            grpInsured.setIpsnNo(listImportIpsnInfoVo.getIpsnNo());
        }
        if(!StringUtils.isEmpty(listImportIpsnInfoVo.getCustNo())){
            grpInsured.setIpsnCustNo(listImportIpsnInfoVo.getCustNo());
        }
        GrpInsured grpInsuredInfo = restfulGrpInsuredService.queryGrpInsured(grpInsured);
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsurApplBo  grpInsurApplBo = insurApplServer.searchInsurApplByBusinessKey(listImportIpsnInfoVo.getApplNo());
        logger.debug("调用后台服务查询的保单类型是"+grpInsurApplBo.getGrpInsurAppl().getCntrType());
        // 投保人信息
        ListImportPageVo listImportPageVo = new ListImportPageVo();
        listImportPageVo.setCntrType(grpInsurApplBo.getGrpInsurAppl().getCntrType());
        if(null != grpInsuredInfo.getHldrInfo()){
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
     			listImportIpsnInfoVo.setBirthDate(grpInsuredInfo.getIpsnBirthDate());
     			listImportIpsnInfoVo.setAge(grpInsuredInfo.getIpsnAge());
     			listImportIpsnInfoVo.setIdType(grpInsuredInfo.getIpsnIdType());
     			listImportIpsnInfoVo.setOccupationalCodes(grpInsuredInfo.getIpsnOccCode());
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
     			listImportIpsnInfoVo.setPhone(grpInsuredInfo.getIpsnMobilePhone());
     			listImportIpsnInfoVo.setFeeGrpNo(grpInsuredInfo.getFeeGrpNo());// 收费属组号
     			listImportIpsnInfoVo.setMainIpsnNo(grpInsuredInfo.getMasterIpsnNo());// 主被保险人编号
     			listImportIpsnInfoVo.setRelToIpsn(grpInsuredInfo.getIpsnRtMstIpsn());// 与主被保险人关系
     			listImportIpsnInfoVo.setRelToHldr(grpInsuredInfo.getRelToHldr());// 与投保人关系
     			listImportIpsnInfoVo.setTyNetPayAmnt(grpInsuredInfo.getTyNetPayAmnt());// 同业公司人身保险保额合计
     			listImportIpsnInfoVo.setGrpPayAmount(grpInsuredInfo.getGrpPayAmount());// 单位交费金额
     	        listImportPageVo.setIpsnInfoVo(listImportIpsnInfoVo);
     		}
     		//缴费账户信息
             if(grpInsuredInfo.getAccInfoList() != null){
     			List<AccInfoVo> list = new ArrayList<>();
     			for(AccInfo ac : grpInsuredInfo.getAccInfoList()){
     				AccInfoVo accInfoVo = new AccInfoVo();
     				accInfoVo.setSeqNo(ac.getSeqNo());//账户顺序
     				accInfoVo.setIpsnPayAmnt(ac.getIpsnPayAmnt());//个人扣款金额
     				accInfoVo.setIpsnPayPct(ac.getIpsnPayPct());//个人账户交费比例
     				accInfoVo.setBankAccName(ac.getBankAccName());//缴费账户
     				accInfoVo.setBankCode(ac.getBankCode());//缴费银行
     				accInfoVo.setBankAccNo(ac.getBankAccNo());//缴费账号
     				list.add(accInfoVo);
     			}
     			listImportPageVo.setAccInfoList(list);//缴费账户信息
     		}
        // 受益人信息
        List<ListImportBeneficiaryVo> listImportBeneficiaryVos = new ArrayList<>();
        ListImportBeneficiaryVo listImportBeneficiaryVo = new  ListImportBeneficiaryVo();
        if(null != grpInsuredInfo.getBnfrInfoList()){
        	for (int i = 0; i < grpInsuredInfo.getBnfrInfoList().size(); i++) {
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
            listImportPageVo.setBeneficiaryInfo(listImportBeneficiaryVos);
        }
        
        // 要约信息
        List<ListImportBsInfoVo> listImportBsInfoVos = new ArrayList<>();
        for (int j = 0; j < grpInsuredInfo.getSubStateList().size(); j++) {
        	ListImportBsInfoVo listImportBsInfoVo;
        	List<ListImportSubPolVo> listImportSubPolVos;
        	if(j==0||!StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0,3), listImportBsInfoVos.get(listImportBsInfoVos.size()-1).getPolCode())){
        		listImportBsInfoVo = new ListImportBsInfoVo();
        		listImportSubPolVos = new ArrayList<>();
        		listImportBsInfoVo.setPolCode(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3));
        		for(Policy policy:grpInsurApplBo.getGrpInsurAppl().getApplState().getPolicyList()){
        			if(StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0, 3),policy.getPolCode())){
        				listImportBsInfoVo.setPolName(policy.getPolNameChn());
        				break;
        			}
        		}
        	}else{
        		listImportBsInfoVo = listImportBsInfoVos.get(listImportBsInfoVos.size()-1);
        		listImportSubPolVos = listImportBsInfoVo.getListImportSubPolVos();
        	}
            ListImportSubPolVo listImportSubPolVo = new ListImportSubPolVo();
            listImportSubPolVo.setSubPolCode(grpInsuredInfo.getSubStateList().get(j).getPolCode());
            listImportSubPolVo.setSubPolName(grpInsuredInfo.getSubStateList().get(j).getPolNameChn());
            listImportSubPolVo.setAmount(grpInsuredInfo.getSubStateList().get(j).getFaceAmnt());
            listImportSubPolVo.setPremium(grpInsuredInfo.getSubStateList().get(j).getPremium());
            listImportSubPolVo.setGroupCode(grpInsuredInfo.getSubStateList().get(j).getClaimIpsnGrpNo());
        	//获取被保人属组名称
        	for(IpsnStateGrp ipsnStateGrp:grpInsurApplBo.getGrpInsurAppl().getIpsnStateGrpList()){
        		if(grpInsuredInfo.getSubStateList().get(j).getClaimIpsnGrpNo()==ipsnStateGrp.getIpsnGrpNo()){
        			listImportSubPolVo.setGroupName(ipsnStateGrp.getIpsnGrpName());
        			break;
        		}
        	}
        	listImportSubPolVos.add(listImportSubPolVo);
        	listImportBsInfoVo.setListImportSubPolVos(listImportSubPolVos);
        	if(j==0||!StringUtils.equals(grpInsuredInfo.getSubStateList().get(j).getPolCode().substring(0,3), listImportBsInfoVos.get(listImportBsInfoVos.size()-1).getPolCode())){
        		listImportBsInfoVos.add(listImportBsInfoVo);
        	}
        }
        listImportPageVo.setBsInfoVo(listImportBsInfoVos);
        return listImportPageVo;
    }

    /**
     * 查询险种表格
     * 
     * @author xiaoYe
     * @param query
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/query")
    public @ResponseMessage DataTable<ListImportVo> getResponseSummaryList(QueryDt queryDt, ListImportVo listImportVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //前台接收的日期字符串，对接后台，做格式转换
        Date taskStartTime = null;
        Date taskEndTime = null;
        
        if(!StringUtils.isEmpty(listImportVo.getTaskStartTime())){
        	try{
        		taskStartTime = DateUtils.parseDate(listImportVo.getTaskStartTime(), "yyyy-MM-dd");
        	}catch(ParseException e){
        		logger.info("日期转换失败，请核对日期格式",e);
        	}
        }
        if(!StringUtils.isEmpty(listImportVo.getTaskEndTime())){
        	try{
        		taskEndTime = DateUtils.parseDate(listImportVo.getTaskEndTime(), "yyyy-MM-dd");
        	}catch(ParseException e){
        		logger.info("日期转换失败，请核对日期格式",e);
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
        ipsnImportInfo.setPageNum(queryDt.getRequestId());
        ipsnImportInfo.setRowNum(queryDt.getRows());
        RetInfoObject<IpsnImportInfo> retInfoObject = new RetInfoObject<>();
        retInfoObject = ipsnImportStateService.GetIpsnImportResult(ipsnImportInfo);
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
            Long errorNum = subpolNameJson.get(i).getErrorNum();
            String operateBranch = subpolNameJson.get(i).getPclkBranchNo();
            String operatorName = subpolNameJson.get(i).getPclkName();
            String operatorNo = subpolNameJson.get(i).getPclkNo();
            //前台接收的日期字符串，对接后台，做格式转换
            String endTime = null;
            String startTime = null;
            if(null != subpolNameJson.get(i).getStartTime()){
			    startTime = DateFormatUtils.format(subpolNameJson.get(i).getStartTime(), "yyyy-MM-dd HH:mm:ss");
			}
            if(null != subpolNameJson.get(i).getEndTime()){
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
        pageData.setTotalCount(subpolNameJson.size());
        pageData.setData(list);
        return pageQueryService.tranToDataTable(queryDt.getRequestId(), pageData);
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
    public @ResponseMessage DataTable<ListImportBeneficiaryVo> getResponseBeneTable(QueryDt queryDt , ListImportIpsnInfoVo listImportIpsnInfoVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<ListImportBeneficiaryVo> listImportBeneficiaryVos = new ArrayList<ListImportBeneficiaryVo>();
        PageData<ListImportBeneficiaryVo> pageData = new PageData<ListImportBeneficiaryVo>();
        GrpInsured grpInsured = new  GrpInsured();
        String applNo = listImportIpsnInfoVo.getApplNo();
        if(applNo!=null){
            grpInsured.setApplNo(applNo);
        }
        Long ipsnNo = listImportIpsnInfoVo.getIpsnNo();
        if(ipsnNo!=null){
            grpInsured.setIpsnNo(ipsnNo);
        }
        String custNo = listImportIpsnInfoVo.getCustNo();
        if(custNo!=null){
            grpInsured.setIpsnCustNo(custNo);
        }
        GrpInsured grpInsuredInfo = restfulGrpInsuredService.queryGrpInsured(grpInsured);
        // 受益人信息
        ListImportBeneficiaryVo listImportBeneficiaryVo = new  ListImportBeneficiaryVo();
        for (int i = 0; i < grpInsuredInfo.getBnfrInfoList().size(); i++) {
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
     * @param businessKey , taskId
     * @return
     */
    @RequestMapping(value = "/comback")
    public @ResponseMessage TaskInfo comback(@RequestBody TaskInfo taskInfo) {

        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        TaskJumpModel taskJumpModel = new TaskJumpModel();
        taskJumpModel.setTaskId(taskInfo.getTaskId());
        taskJumpModel.setBusinessKey(taskInfo.getBizNo());
        System.out.println("++++++++++++++++++++++++++controller test   taskJumpModel++++++++++++++++++++++++++"+taskJumpModel.toString());
        return taskInfo;
    }

    /**
     * 全部复合完成
     * @author xiaoYe
     * @param session , applNo
     * @param listImportVo
     * @return
     */
    @RequestMapping(value = "/submitAll")
    public @ResponseMessage RetInfo getSubmitAllSign(@CurrentSession Session session,@RequestBody String applNo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        headerInfo.setOrginSystem("ORBPS");
        headerInfo.getRouteInfo().setBranchNo("1");
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        //获取session信息
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        TraceNodePra traceNodePra = new TraceNodePra();
        String [] s = applNo.split(",");
        String businessKey = s[0];
        String taskId = s[1];
        String operState = s[2];
        System.out.println("业务流水号++++"+businessKey+ "任务ID+++++"+taskId);
        traceNodePra.setApplNo(businessKey);
        traceNodePra.setTaskId(taskId);
        TraceNode traceNode = new TraceNode();
        traceNode.setPclkBranchNo(sessionModel.getBranchNo());
        traceNode.setPclkName(sessionModel.getName());
        traceNode.setPclkNo(sessionModel.getClerkNo());
        traceNode.setProcStat(operState);//清汇导入完成
        traceNodePra.setTraceNode(traceNode);
        // 调用全部录入完成服务
        RetInfo retInfo = restfulGrpInsuredService.addAllGrpInsured(traceNodePra);
        
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
    public RetInfo upload(@CurrentSession Session session, MultipartFile multipartFile, @PathVariable("applNo") String applNo) {
    	CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        SessionModel sessionModel = SessionUtils.getSessionModel(session);
        String pclkBranchNo = sessionModel.getBranchNo();//机构代码
        String pclkName = sessionModel.getName();//操作员姓名
        String pclkNo = sessionModel.getClerkNo();//操作员工号
        InputStream in = null;
        OutputStream out = null;
        RetInfo retInfo=null;
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
            String [] s = applNo.split(",");
            String status = s [0];
            String businessKey = s[1];
            String taskId = s[2];
            
            if (multipartFile.getOriginalFilename().endsWith("xlsx")){
                //2007
                fileResource = parentfile + File.separator + businessKey + time + ".xlsx";

            } else if (multipartFile.getOriginalFilename().endsWith("xls")){
                //2003
                fileResource = parentfile + File.separator + businessKey + time + ".xls";	
            } else {
            	fileResource = parentfile + File.separator + businessKey + time + ".csv";
            }
            out  = new FileOutputStream(new File(fileResource));
            byte[] b = new byte[1024];
            int i;
            while ((i = in.read(b)) != -1) {
                out.write(b, 0, i);
            }
            out.flush();
//            String fileName = businessKey + time + ".csv";
//            String filePath = parentfile + File.separator + fileName;// 文件名称     
           
//            ExcelToCsv.process(fileResource, filePath);
//            if (multipartFile.getOriginalFilename().endsWith("xlsx")){
//                XLSX2CSV xlsx2csv = new XLSX2CSV(fileResource, filePath);
//                xlsx2csv.process();
//            } else {
//                XLS2CSV xls2csv = new XLS2CSV(fileResource, filePath);
//                xls2csv.process();
//            }
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
        if(retInfo!=null){
        	retInfo.setErrMsg("文件已上传完毕，请稍等。");
        }
		return retInfo;
    }
    /**
     * 清单文件导出
     * @param applNo
     * @return
     * @throws IOException
     */
	@RequestMapping(value = "/download/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<byte []> download(@PathVariable("id") String applNo) throws IOException {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        HttpHeaders headers = new HttpHeaders();
        byte[] body = null;
        HttpStatus httpState = HttpStatus.NOT_FOUND;
		//生成清单文件
        RetInfo retInfo=insuredFileCreatService.creatExcelFile(applNo);
        if (StringUtils.equals("0", retInfo.getRetCode())){
        	return new ResponseEntity<>(body,headers,httpState);
        }
		
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsnlst.ipsn.dir"));
        sb.append(File.separator);
        sb.append(applNo);
        sb.append(".xls");
		File file = new File(sb.toString());
		if(file.exists()){
	        //处理显示中文文件名的问题
	        String fileName=new String(file.getName().getBytes("utf-8"),"ISO-8859-1");
	        //设置请求头内容,告诉浏览器代开下载窗口
	        body=FileUtils.readFileToByteArray(file);
	        httpState=HttpStatus.OK;
	        headers.setContentDispositionFormData("attachment",fileName );
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		}else{
			logger.error("清单文件["+applNo+".xls]不存在!");
		}
        return new ResponseEntity<>(body,headers,httpState);    
    }
	
	/**
     * 修改团单信息
     * 
     * @author JiangBoMeng
     * @param update
     * @param  listImportPageVo
     * @return
     */
    @RequestMapping(value = "/update")
    public @ResponseMessage RetInfo grpInsurApplUpdate(@RequestBody ListImportPageVo listImportPageVo) {
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        GrpInsured grpInsured = new GrpInsured();
        HldrInfo hldrInfo = new HldrInfo();
        //投保人信息
        if(listImportPageVo.getHldrInfoVo() != null){
            hldrInfo.setHldrName(listImportPageVo.getHldrInfoVo().getHldrName());
            hldrInfo.setHldrSex(listImportPageVo.getHldrInfoVo().getHldrSex());
            hldrInfo.setHldrIdType(listImportPageVo.getHldrInfoVo().getHldrIdType());
            hldrInfo.setHldrIdNo(listImportPageVo.getHldrInfoVo().getHldrIdNo());
            hldrInfo.setHldrBirthDate(listImportPageVo.getHldrInfoVo().getHldrBirth());
            hldrInfo.setHldrMobilePhone(listImportPageVo.getHldrInfoVo().getHldrPhone());
            grpInsured.setHldrInfo(hldrInfo);
            System.out.println("++++++++++投保人信息++++++++++++++"+hldrInfo.toString()+"+++++++++++++++++++++++++++");
        }
		// 被保人信息
		if (listImportPageVo.getIpsnInfoVo() != null ) {
			grpInsured.setApplNo(listImportPageVo.getIpsnInfoVo().getApplNo());
			grpInsured.setIpsnPartyId(listImportPageVo.getIpsnInfoVo().getCustNo());//客户号
			grpInsured.setIpsnNo(listImportPageVo.getIpsnInfoVo().getIpsnNo());//被保险人编号
			grpInsured.setIpsnName(listImportPageVo.getIpsnInfoVo().getName());//被保险人姓名
			// 被保险人类型
			if("Y".equals(listImportPageVo.getIpsnInfoVo().getJoinIpsnFlag())){
				grpInsured.setIpsnType("A");//被保险人类型
			}else{
				grpInsured.setIpsnType("I");//被保险人类型
			}
			grpInsured.setIpsnIdNo(listImportPageVo.getIpsnInfoVo().getIdNo());//证件号码
			grpInsured.setIpsnSex(listImportPageVo.getIpsnInfoVo().getIpsursex());//被保人性别
			grpInsured.setIpsnBirthDate(listImportPageVo.getIpsnInfoVo().getBirthDate());//被保人出生日期
			grpInsured.setIpsnAge(listImportPageVo.getIpsnInfoVo().getAge());//被保人年龄
			grpInsured.setIpsnIdType(listImportPageVo.getIpsnInfoVo().getIdType());//被保人证件类型
			grpInsured.setIpsnOccCode(listImportPageVo.getIpsnInfoVo().getOccupationalCodes());//职业代码
			grpInsured.setIpsnOccClassLevel(listImportPageVo.getIpsnInfoVo().getRiskLevel());//职业风险等级 
			grpInsured.setLevelCode(listImportPageVo.getIpsnInfoVo().getGroupLevelCode());//组织层次代码
			grpInsured.setIpsnCompanyLoc(listImportPageVo.getIpsnInfoVo().getWorkAddress());//被保人工作地点
			grpInsured.setIpsnRefNo(listImportPageVo.getIpsnInfoVo().getWorkNo());//被保人工号
			grpInsured.setInServiceFlag(listImportPageVo.getIpsnInfoVo().getOnJobFlag());//是否在职
			grpInsured.setIpsnSss(listImportPageVo.getIpsnInfoVo().getMedicalInsurFlag());//医保标识
			grpInsured.setIpsnSsc(listImportPageVo.getIpsnInfoVo().getMedicalInsurCode());//医保代码
			grpInsured.setIpsnSsn(listImportPageVo.getIpsnInfoVo().getMedicalInsurNo());//医保编号
			grpInsured.setNotificaStat(listImportPageVo.getIpsnInfoVo().getHealthFlag());//是否有异常告知
			grpInsured.setIpsnEmail(listImportPageVo.getIpsnInfoVo().getEmail());//被保人邮箱
			grpInsured.setIpsnPayAmount(listImportPageVo.getIpsnInfoVo().getCharge());//个人交费金额
			grpInsured.setRemark(listImportPageVo.getIpsnInfoVo().getRemarks());//备注
			grpInsured.setFeeGrpNo(listImportPageVo.getIpsnInfoVo().getFeeGrpNo());// 被保人属组号
			grpInsured.setIpsnMobilePhone(listImportPageVo.getIpsnInfoVo().getPhone());//被保人手机号 24
			grpInsured.setMasterIpsnNo(listImportPageVo.getIpsnInfoVo().getMainIpsnNo());//主被保险人编号
			grpInsured.setIpsnRtMstIpsn(listImportPageVo.getIpsnInfoVo().getRelToIpsn());//与主被保险人关系
			grpInsured.setRelToHldr(listImportPageVo.getIpsnInfoVo().getRelToHldr());//与投保人关系
			grpInsured.setTyNetPayAmnt(listImportPageVo.getIpsnInfoVo().getTyNetPayAmnt());//同业公司人身保险保额合计
			grpInsured.setGrpPayAmount(listImportPageVo.getIpsnInfoVo().getGrpPayAmount());//单位交费金额
		}
		//缴费账户信息
		if(listImportPageVo.getAccInfoList() != null){
			List<AccInfo> list = new ArrayList<AccInfo>();
			for(AccInfoVo accInfovo : listImportPageVo.getAccInfoList()){
				AccInfo accInfo = new AccInfo();
				accInfo.setSeqNo(accInfovo.getSeqNo());//账户顺序
				accInfo.setIpsnPayAmnt(accInfovo.getIpsnPayAmnt());//个人扣款金额
				accInfo.setIpsnPayPct(null == accInfovo.getIpsnPayPct() ? null : accInfovo.getIpsnPayPct() / 100);//个人账户交费比例
				accInfo.setBankAccName(accInfovo.getBankAccName());//缴费账户
				accInfo.setBankCode(accInfovo.getBankCode());//缴费银行
				accInfo.setBankAccNo(accInfovo.getBankAccNo());//缴费账号
				list.add(accInfo);
			}
			grpInsured.setAccInfoList(list);
		}
		//要约信息
        List<SubState> listSubState = new ArrayList<>();
        if (listImportPageVo.getBsInfoVo() != null) {
            for (int i = 0; i < listImportPageVo.getBsInfoVo().size(); i++) {
                for (int j = 0; j < listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().size(); j++) {
                  	SubState subState = new SubState();
                  	String SubPolCode;
                  	//如果责任代码为空，把险种代码传过去
                	if(StringUtils.isEmpty(listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolCode())){
                		 SubPolCode = listImportPageVo.getBsInfoVo().get(i).getPolCode();
                	}else{
                		 SubPolCode = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolCode();
                	}
                	subState.setPolCode(SubPolCode);
                	Long groupCodeTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getGroupCode();
                    subState.setClaimIpsnGrpNo(groupCodeTb);
                    Double amountTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getAmount();
                    subState.setFaceAmnt(amountTb);
                    Double premiumTb = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getPremium();
                    subState.setPremium(premiumTb);
                	String SubPolName = listImportPageVo.getBsInfoVo().get(i).getListImportSubPolVos().get(j).getSubPolName();
                	subState.setPolNameChn(SubPolName);
                	listSubState.add(subState);
				}
            }
        }
        grpInsured.setSubStateList(listSubState);
        //受益人信息
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
                Double beneAmount = listImportPageVo.getBeneficiaryInfo().get(i).getBeneAmount() / 100;;
                bnfrInfo.setBnfrProfit(beneAmount);
                String relToIpsn = listImportPageVo.getBeneficiaryInfo().get(i).getRelToIpsnTb();
                bnfrInfo.setBnfrRtIpsn(relToIpsn);
                String sex = listImportPageVo.getBeneficiaryInfo().get(i).getSex();
                bnfrInfo.setBnfrSex(sex);
                listBnfrInfo.add(bnfrInfo);
            }
        }

        grpInsured.setBnfrInfoList(listBnfrInfo);
        RetInfo retInfo = restfulGrpInsuredService.updateGrpInsured(grpInsured);
        return retInfo;
    }
}