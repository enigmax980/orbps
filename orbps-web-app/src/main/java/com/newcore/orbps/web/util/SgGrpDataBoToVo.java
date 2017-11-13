package com.newcore.orbps.web.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

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
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSalesListFormVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpAddinsuranceVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpApplInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurApplVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpInsurInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpPayInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpPersonalInsurInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpPrintInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpProposalInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl.SgGrpVatInfoVo;

/** 
 * 
 * @Description: 清汇Bo转Vo
 * @ClassName: SgGrpDataBoToVo 
 * @author: jinmeina
 * @date: 2016-12-05 22:05:00 
 * 
 */
public class SgGrpDataBoToVo {
	/**	
	 * 清汇Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpInsurApplVo sgGrpInsurApplBoToVo(GrpInsurAppl grpInsurAppl){
		if(null == grpInsurAppl){
			return null;
		}
		//汇交件面向前端大Vo
		SgGrpInsurApplVo sgGrpInsurApplVo = new SgGrpInsurApplVo();
		//汇交人类型
		sgGrpInsurApplVo.setListType(grpInsurAppl.getSgType());
		//契约类型
		sgGrpInsurApplVo.setCntrType(grpInsurAppl.getCntrType());
		//投保单信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpApplInfoVo(sgGrpApplInfoBoToVo(grpInsurAppl));
		//团体汇交信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpInsurInfoVo(sgGrpInsurInfoBoToVo(grpInsurAppl));
		//个人汇交信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpPersonalInsurInfoVo(sgGrpPersonalInsurInfoBoToVo(grpInsurAppl));
		//交费信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpPayInfoVo(sgGrpPayInfoBoToVo(grpInsurAppl));
		//打印信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpPrintInfoVo(sgGrpPrintInfoBoToVo(grpInsurAppl));
		//要约信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpProposalInfoVo(sgGrpProposalInfoBoToVo(grpInsurAppl));
		//增值税信息Bo转Vo
		sgGrpInsurApplVo.setSgGrpVatInfoVo(sgGrpVatInfoBoToVo(grpInsurAppl));
		//被保人分组信息Bo转Vo
		sgGrpInsurApplVo.setInsuredGroupModalVos(insuredGroupModalBoToVo(grpInsurAppl));
		//组织层次分组信息Bo转Vo
		sgGrpInsurApplVo.setOrganizaHierarModalVos(organizaHierarModalBoToVo(grpInsurAppl));
		//险种信息Bo转Vo
		sgGrpInsurApplVo.setAddinsuranceVos(sgGrpAddinsuranceVoBoToVo(grpInsurAppl));
		//收付费分组信息Bo转Vo
		sgGrpInsurApplVo.setChargePayGroupModalVos(chargePayGroupModalVoBoToVo(grpInsurAppl));
		//进入人工审批的原因
		if(null != grpInsurAppl.getConventions()){
			sgGrpInsurApplVo.setAprovalReason(grpInsurAppl.getConventions().getInputConv());
		}
		//新单状态
		//sgGrpInsurApplVo.setApprovalState(grpInsurAppl);
		//接入通道
		//sgGrpInsurApplVo.setAccessChannel(accessChannel);
		//任务轨迹信息
		//sgGrpInsurApplVo.setTaskInfo(taskInfo);
		return sgGrpInsurApplVo;
	}
	
	/**
	 * 投保单信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpApplInfoVo sgGrpApplInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		SgGrpApplInfoVo sgGrpApplInfoVo = new SgGrpApplInfoVo();
		//投保单号
		sgGrpApplInfoVo.setApplNo(grpInsurAppl.getApplNo());
		//报价审批号
		sgGrpApplInfoVo.setQuotaEaNo(grpInsurAppl.getApproNo());
		//投保日期
		sgGrpApplInfoVo.setApplDate(grpInsurAppl.getApplDate());
		//共保协议号
		sgGrpApplInfoVo.setAgreementNo(grpInsurAppl.getAgreementNo());
		//上期保单号
		sgGrpApplInfoVo.setPreviousPolNo(grpInsurAppl.getRenewedCgNo());
		//合同号
		sgGrpApplInfoVo.setPolNo(grpInsurAppl.getCgNo());
		//生效日期
		sgGrpApplInfoVo.setEffectDate(grpInsurAppl.getInForceDate());
		
		//销售相关集合
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
                // 代理员工号
                grpSalesListFormVo.setAgencyNo(salesInfo.get(i).getCommnrPsnNo());
                // 代理员工姓名
                grpSalesListFormVo.setAgencyName(salesInfo.get(i).getCommnrPsnName());
                grpSalesListFormVos.add(grpSalesListFormVo);
            }
            sgGrpApplInfoVo.setGrpSalesListFormVos(grpSalesListFormVos);
        }
		return sgGrpApplInfoVo;
	}
	
	/**
	 * 团体汇交信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpInsurInfoVo sgGrpInsurInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//团体汇交信息
		SgGrpInsurInfoVo sgGrpInsurInfoVo = new SgGrpInsurInfoVo();
		//争议处理方式
		sgGrpInsurInfoVo.setgSettleDispute(grpInsurAppl.getArgueType());
		//仲裁机构名称
		sgGrpInsurInfoVo.setParbOrgName(grpInsurAppl.getArbitration());
		//团体客户信息
		GrpHolderInfo grpHolderInfo = grpInsurAppl.getGrpHolderInfo();
		if(null != grpHolderInfo){
			//单位、团体名称
			sgGrpInsurInfoVo.setCompanyName(grpHolderInfo.getGrpName());
			//证件类型
			sgGrpInsurInfoVo.setIdType(grpHolderInfo.getGrpIdType());
			//证件号码
			sgGrpInsurInfoVo.setIdNo(grpHolderInfo.getGrpIdNo());
			//企业注册地
			sgGrpInsurInfoVo.setCompanyRegist(grpHolderInfo.getGrpCountryCode());
			//部门类型
			sgGrpInsurInfoVo.setDepartmentType(grpHolderInfo.getGrpPsnDeptType());
			//职业类型
			sgGrpInsurInfoVo.setOccClassCode(grpHolderInfo.getOccClassCode());
			//投保人数
			sgGrpInsurInfoVo.setGInsuredTotalNum(grpHolderInfo.getIpsnNum());
			//联系人姓名
			sgGrpInsurInfoVo.setContactName(grpHolderInfo.getContactName());
			//联系人证件类型
			sgGrpInsurInfoVo.setContactIdType(grpHolderInfo.getContactIdType());
			//联系人证件号码
			sgGrpInsurInfoVo.setContactIdNo(grpHolderInfo.getContactIdNo());
			//联系人移动电话
			sgGrpInsurInfoVo.setContactMobile(grpHolderInfo.getContactMobile());
			//联系人邮箱
			sgGrpInsurInfoVo.setContactEmail(grpHolderInfo.getContactEmail());
			//固定电话
			sgGrpInsurInfoVo.setContactTel(grpHolderInfo.getContactTelephone());
			//传真号码
			sgGrpInsurInfoVo.setFaxNo(grpHolderInfo.getFax());
			//地址
			Address address = grpHolderInfo.getAddress();
			if(null != address){				
				//邮编
				sgGrpInsurInfoVo.setZipCode(address.getPostCode());
				//省、直辖市
				sgGrpInsurInfoVo.setProvinceCode(address.getProvince());
				//市、城区
				sgGrpInsurInfoVo.setCityCode(address.getCity());
				//县、地级市
				sgGrpInsurInfoVo.setCountyCode(address.getCounty());
				//乡镇
				sgGrpInsurInfoVo.setTownCode(address.getTown());
				//村、社区
				sgGrpInsurInfoVo.setVillageCode(address.getVillage());
				//汇交人详细信息
				sgGrpInsurInfoVo.setHome(address.getHomeAddress());
			}
			//性别
			//sgGrpInsurInfoVo.setSex(sex);
			//出生日期
			//sgGrpInsurInfoVo.setBirthDate(birthDate);
		}
		return sgGrpInsurInfoVo;
	}
	
	/**
	 * 个人汇交信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpPersonalInsurInfoVo sgGrpPersonalInsurInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		SgGrpPersonalInsurInfoVo sgGrpPersonalInsurInfoVo = new SgGrpPersonalInsurInfoVo();
		//个人汇交信息
		PsnListHolderInfo psnListHolderInfo = grpInsurAppl.getPsnListHolderInfo();
		//争议处理方式
		sgGrpPersonalInsurInfoVo.setpSettleDispute(grpInsurAppl.getArgueType());
		//仲裁机构名称
		sgGrpPersonalInsurInfoVo.setJoinParbOrgName(grpInsurAppl.getArbitration());
		if(null != psnListHolderInfo){
			//汇交人姓名
			sgGrpPersonalInsurInfoVo.setJoinName(psnListHolderInfo.getSgName());
			//汇交人证件类型
			sgGrpPersonalInsurInfoVo.setJoinIdType(psnListHolderInfo.getSgIdType());
			//汇交人证件号码
			sgGrpPersonalInsurInfoVo.setJoinIdNo(psnListHolderInfo.getSgIdNo());
			//汇交人性别
			sgGrpPersonalInsurInfoVo.setJoinSex(psnListHolderInfo.getSgSex());
			//汇交人出生日期
			sgGrpPersonalInsurInfoVo.setJoinBirthDate(psnListHolderInfo.getSgBirthDate());
			//汇交人移动电话
			sgGrpPersonalInsurInfoVo.setJoinMobile(psnListHolderInfo.getSgMobile());
			//汇交人邮箱
			sgGrpPersonalInsurInfoVo.setJoinEmail(psnListHolderInfo.getSgEmail());
			//汇交人固定电话
			sgGrpPersonalInsurInfoVo.setJoinTel(psnListHolderInfo.getSgTelephone());
			//汇交人传真号码
			sgGrpPersonalInsurInfoVo.setJoinFaxNo(psnListHolderInfo.getFax());
			//地址
			Address address = psnListHolderInfo.getAddress();
			if(null != address){				
				//汇交人省
				sgGrpPersonalInsurInfoVo.setProvince(address.getProvince());
				//汇交人城市
				sgGrpPersonalInsurInfoVo.setCity(address.getCity());
				//汇交人县
				sgGrpPersonalInsurInfoVo.setCounty(address.getCounty());
				//汇交人乡
				sgGrpPersonalInsurInfoVo.setTown(address.getTown());
				//汇交人村
				sgGrpPersonalInsurInfoVo.setVillage(address.getVillage());
				//汇交人详细地址
				sgGrpPersonalInsurInfoVo.setJoinHome(address.getHomeAddress());
				//汇交人邮编
				sgGrpPersonalInsurInfoVo.setPostCode(address.getPostCode());
			}
		}
		//职业类别
		//sgGrpPersonalInsurInfoVo.setOccClassCode();
		return sgGrpPersonalInsurInfoVo;
	}
	
	/**
	 * 交费信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpPayInfoVo sgGrpPayInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//交费信息
		SgGrpPayInfoVo sgGrpPayInfoVo = new SgGrpPayInfoVo();
		//交费相关
		PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
		if(null != paymentInfo){
			//交费方式
			sgGrpPayInfoVo.setMoneyItrvl(paymentInfo.getMoneyinItrvl());
			//交费形式
			sgGrpPayInfoVo.setMoneyinType(paymentInfo.getMoneyinType());
			//保费来源
			sgGrpPayInfoVo.setPremFrom(paymentInfo.getPremSource());
			//交费开户行
			sgGrpPayInfoVo.setBankCode(paymentInfo.getBankCode());
			//开户名
			sgGrpPayInfoVo.setBankBranchName(paymentInfo.getBankAccName());
			//账号
			sgGrpPayInfoVo.setBankaccNo(paymentInfo.getBankAccNo());
			//结算方式
			sgGrpPayInfoVo.setPaymentType(paymentInfo.getStlType());
			//结算限额
			sgGrpPayInfoVo.setPaymentLimit(paymentInfo.getStlAmnt());
			//结算日期集合
			List<Date> dateList = paymentInfo.getStlDate();
			if(null != dateList && dateList.size()>0){				
				//结算日期
				sgGrpPayInfoVo.setPaymentDate(dateList.get(0));
			}
			//是否续期扣款
			sgGrpPayInfoVo.setRenewalChargeFlag(paymentInfo.getIsRenew());
			//扣款截止日期
			sgGrpPayInfoVo.setChargeDeadline(paymentInfo.getRenewExpDate());
			//是否多期暂交
			sgGrpPayInfoVo.setMultiRevFlag(paymentInfo.getIsMultiPay());
			//多期暂交年数
			sgGrpPayInfoVo.setMultiTempYear(paymentInfo.getMutipayTimes());
			//供款比例
			sgGrpPayInfoVo.setMultiPartyScale(paymentInfo.getMultiPartyScale());
			//供款金额
			sgGrpPayInfoVo.setMultiPartyMoney(paymentInfo.getMultiPartyMoney());
		}
		//续期扣款期数
		//sgGrpPayInfoVo.setRenewalDebitNum(renewalDebitNum);
		//是否续保
		//sgGrpPayInfoVo.setRenewFlag(renewFlag);
		//续保扣款期数
		//sgGrpPayInfoVo.setRenewChargeNum(renewChargeNum);
		return sgGrpPayInfoVo;
	}
	
	/**
	 * 打印信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpPrintInfoVo sgGrpPrintInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//打印信息
		SgGrpPrintInfoVo sgGrpPrintInfoVo = new SgGrpPrintInfoVo();
		//合同打印方式
		sgGrpPrintInfoVo.setUnderNoticeType(grpInsurAppl.getCntrPrintType());
		//清单打印
		sgGrpPrintInfoVo.setListPrint(grpInsurAppl.getListPrintType());
		//个人凭证打印
		sgGrpPrintInfoVo.setPersonalIdPrint(grpInsurAppl.getVoucherPrintType());
		//清单标记
		sgGrpPrintInfoVo.setListFlag(grpInsurAppl.getLstProcType());
		//赠送险标记
		sgGrpPrintInfoVo.setGiftInsFlag(grpInsurAppl.getGiftFlag());
		//保单性质
		sgGrpPrintInfoVo.setPolProperty(grpInsurAppl.getInsurProperty());
		//是否人工核保
		//sgGrpPrintInfoVo.setManualUwFlag(grpInsurAppl.getUdwType());
		//是否异常告知
		sgGrpPrintInfoVo.setUnusualFlag(grpInsurAppl.getNotificaStat());
		//被保人分组类型
        if(null != grpInsurAppl.getIpsnStateGrpList() && grpInsurAppl.getIpsnStateGrpList().size()>0){
        	if(null != grpInsurAppl.getIpsnStateGrpList().get(0)){
        		sgGrpPrintInfoVo.setGroupType(grpInsurAppl.getIpsnStateGrpList().get(0).getIpsnGrpType());
        	}
        }
		//投保资料影像、档案清单文件上传
		//sgGrpPrintInfoVo.setImageListUpload(imageListUpload);	
		return sgGrpPrintInfoVo;
	}
	
	/**
	 * 要约信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpProposalInfoVo sgGrpProposalInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//要约信息
		SgGrpProposalInfoVo sgGrpProposalInfoVo = new SgGrpProposalInfoVo();
		//投保要约
		ApplState applState = grpInsurAppl.getApplState();
		//新险种信息集合
		List<SgGrpAddinsuranceVo> sgGrpAddinsuranceVoList = new ArrayList<SgGrpAddinsuranceVo>();
		if(null != applState){			
			//被保险人总数
			sgGrpProposalInfoVo.setInsuredTotalNum(applState.getIpsnNum());
			//保险期间
			if(null != applState.getInsurDur()){
				sgGrpProposalInfoVo.setDateType(applState.getInsurDur().intValue());
			}
			//保费合计
			sgGrpProposalInfoVo.setSumPrem(applState.getSumPremium());
			//生效方式
			sgGrpProposalInfoVo.setEffectType(applState.getInforceDateType());
			//指定生效日
			sgGrpProposalInfoVo.setSpeEffectDate(applState.getDesignForceDate());
			//频次是否生效
			sgGrpProposalInfoVo.setFrequencyEffectFlag(applState.getIsFreForce());
			//生效频率
			sgGrpProposalInfoVo.setEffectFreq(applState.getForceFre());
			//保险期间单位
			sgGrpProposalInfoVo.setInsurDurUnit(applState.getInsurDurUnit());
			//险种集合
			List<Policy> polocyList = applState.getPolicyList();
			if(null != polocyList && polocyList.size() > 0){
				for(int i = 0; i < polocyList.size(); i++){
					//责任集合
					List<ResponseVo> responseVoList = new ArrayList<ResponseVo>();
					//新险种信息
					SgGrpAddinsuranceVo addinsuranceVo = new SgGrpAddinsuranceVo();
					//险种代码
					addinsuranceVo.setBusiPrdCode(polocyList.get(i).getPolCode());
					//险种名称
					addinsuranceVo.setBusiPrdName(polocyList.get(i).getPolNameChn());
					//保额
					addinsuranceVo.setAmount(polocyList.get(i).getFaceAmnt());
					//保费
					addinsuranceVo.setPremium(polocyList.get(i).getPremium());
					//承保人数
					addinsuranceVo.setInsuredNum(polocyList.get(i).getPolIpsnNum());
					//保险期间
					addinsuranceVo.setValidateDate(polocyList.get(i).getInsurDur());
					//保险期间类型
					addinsuranceVo.setInsurDurUnit(polocyList.get(i).getInsurDurUnit());
					//健康险专项标识
					addinsuranceVo.setHealthInsurFlag(polocyList.get(i).getSpeciBusinessLogo());
					//子险种
					List<SubPolicy> subPolicyList = polocyList.get(i).getSubPolicyList();
					if (null != subPolicyList && subPolicyList.size() > 0) {
						for (int j = 0; j < subPolicyList.size(); j++) {
							ResponseVo	responseVo = new ResponseVo();
							//投保单号
							responseVo.setApplId(grpInsurAppl.getApplNo());
							//险种代码
							responseVo.setBusiPrdCode(polocyList.get(i).getPolCode());
							//责任代码
							responseVo.setProductCode(subPolicyList.get(j).getSubPolCode());
							//责任名字
							responseVo.setProductName(subPolicyList.get(j).getSubPolName());
							//保额
							responseVo.setProductNum(subPolicyList.get(j).getSubPolAmnt());
							//保费
							responseVo.setProductPremium(subPolicyList.get(j).getSubPremium());
							//险种标准保费
							responseVo.setSubStdPremium(subPolicyList.get(j).getSubStdPremium());
							//责任号
							//responseVo.setProductNo(productNo);
							responseVoList.add(responseVo);
						}
					}
					addinsuranceVo.setResponseVos(responseVoList);
					sgGrpAddinsuranceVoList.add(addinsuranceVo);
				}	
			}
			//新增险种信息
			sgGrpProposalInfoVo.setAddinsuranceVos(sgGrpAddinsuranceVoList);
		}
		//缴费相关
		PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
		if(null != paymentInfo){			
			//首期扣款截止日期
			sgGrpProposalInfoVo.setFirstChargeDate(paymentInfo.getForeExpDate());
		}
		//特约信息
		Conventions conventions = grpInsurAppl.getConventions();
		if(null != conventions){			
			//特别约定
			sgGrpProposalInfoVo.setConvention(conventions.getPolConv());
		}
		return sgGrpProposalInfoVo;
	}
	
	/**
	 * 增值税信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public SgGrpVatInfoVo sgGrpVatInfoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//增值税信息
		SgGrpVatInfoVo sgGrpVatInfoVo = new SgGrpVatInfoVo();
		//团体客户信息
		GrpHolderInfo grpHolderInfo = grpInsurAppl.getGrpHolderInfo();
		if(null != grpHolderInfo){			
			//纳税人识别号
			sgGrpVatInfoVo.setTaxIdNo(grpHolderInfo.getTaxpayerId());
		}
		//交费相关
		PaymentInfo paymentInfo = grpInsurAppl.getPaymentInfo();
		if(null != paymentInfo){			
			//开户银行
			sgGrpVatInfoVo.setBankCode(paymentInfo.getBankCode());
			//开户名称
			sgGrpVatInfoVo.setBankBranchName(paymentInfo.getBankAccName());
			//开户账号
			sgGrpVatInfoVo.setBankaccNo(paymentInfo.getBankAccNo());
		}
		//纳税人名称
		//sgGrpVatInfoVo.setTaxpayerName(taxpayerName);
		return sgGrpVatInfoVo;
	}
	
	/**
	 * 被保人分组信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<InsuredGroupModalVo> insuredGroupModalBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//被保人分组信息集合
		List<InsuredGroupModalVo> list = new ArrayList<InsuredGroupModalVo>();
		//要约分组(整体选录)
		List<IpsnStateGrp> ipsnStateGrpList = grpInsurAppl.getIpsnStateGrpList();
		if(null != ipsnStateGrpList && ipsnStateGrpList.size() > 0){
			for(int i = 0; i < ipsnStateGrpList.size(); i++){
				//险种集合
				List<InsuranceInfoVo> insuranceInfoVoList = new ArrayList<InsuranceInfoVo>();
				//被保人分组信息
				InsuredGroupModalVo insuredGroupModalVo = new InsuredGroupModalVo();
				//要约属组编号
				insuredGroupModalVo.setIpsnGrpNo(ipsnStateGrpList.get(i).getIpsnGrpNo());
				//要约属组名称
				insuredGroupModalVo.setIpsnGrpName(ipsnStateGrpList.get(i).getIpsnGrpName());
				//行业类别
				insuredGroupModalVo.setOccClassCode(ipsnStateGrpList.get(i).getOccClassCode());
				//职业类别
				insuredGroupModalVo.setIpsnOccSubclsCode(ipsnStateGrpList.get(i).getIpsnOccCode());
				//要约属组人数
				insuredGroupModalVo.setIpsnGrpNum(ipsnStateGrpList.get(i).getIpsnGrpNum());
				//男女比例
				insuredGroupModalVo.setGenderRadio(ipsnStateGrpList.get(i).getGenderRadio());
				//参加工伤比例
				insuredGroupModalVo.setGsRate(ipsnStateGrpList.get(i).getGsPct());
				//参加医保比例
				insuredGroupModalVo.setSsRate(ipsnStateGrpList.get(i).getSsPct());
				//险种集合
				List<GrpPolicy> grpPolicyList = ipsnStateGrpList.get(i).getGrpPolicyList();
				if(null != grpPolicyList && grpPolicyList.size() > 0){
					for(int j = 0; j < grpPolicyList.size(); j++){
						//险种
						InsuranceInfoVo insuranceInfoVo = new InsuranceInfoVo();
						//险种代码
						insuranceInfoVo.setPolCode(grpPolicyList.get(j).getPolCode());
						//主附险性质
						insuranceInfoVo.setMrCode(grpPolicyList.get(j).getMrCode());
						//险种保额
						insuranceInfoVo.setFaceAmnt(grpPolicyList.get(j).getFaceAmnt());
						//实际保费
						insuranceInfoVo.setPremium(grpPolicyList.get(j).getPremium());
						//标准保费
						insuranceInfoVo.setStdPremium(grpPolicyList.get(j).getStdPremium());
						//承保费率
						insuranceInfoVo.setPremRate(grpPolicyList.get(j).getPremRate());
						//费率浮动幅度
						insuranceInfoVo.setRecDisount(grpPolicyList.get(j).getPremDiscount());
						insuranceInfoVoList.add(insuranceInfoVo);
					}
				}
				//险种
				insuredGroupModalVo.setInsuranceInfoVos(insuranceInfoVoList);
				list.add(insuredGroupModalVo);
			}
		}
		return list;
	}
	
	/**
	 * 组织层次信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<OrganizaHierarModalVo> organizaHierarModalBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		List<OrganizaHierarModalVo> list = new ArrayList<OrganizaHierarModalVo>();
		//组织关系树集合
		List<OrgTree> orgTreeList = grpInsurAppl.getOrgTreeList();
		if(null != orgTreeList && orgTreeList.size() > 0){
			for(int i = 0; i < orgTreeList.size(); i++){
				//组织层次信息
				OrganizaHierarModalVo organizaHierarModalVo = new OrganizaHierarModalVo();
				//是否缴费
				organizaHierarModalVo.setPay(orgTreeList.get(i).getIsPaid());
				//保全选项
				organizaHierarModalVo.setSecurityOptions(orgTreeList.get(i).getMtnOpt());
				//服务指派
				organizaHierarModalVo.setServiceAssignment(orgTreeList.get(i).getServiceOpt());
				//团体客户信息
				GrpHolderInfo grpHolderInfo = orgTreeList.get(i).getGrpHolderInfo();
				if(null != grpHolderInfo){
					//客户号
					organizaHierarModalVo.setCustNo(grpHolderInfo.getGrpCustNo());
					//单位、团体名称
					organizaHierarModalVo.setCompanyName(grpHolderInfo.getGrpName());
					//曾用名
					organizaHierarModalVo.setOldName(grpHolderInfo.getFormerGrpName());
					//单位性质
					organizaHierarModalVo.setUnitCharacter(grpHolderInfo.getNatureCode());
					//行业类型
					organizaHierarModalVo.setIndustryClassification(grpHolderInfo.getOccClassCode());
					//证件类型
					organizaHierarModalVo.setDeptType(grpHolderInfo.getGrpIdType());
					//成员总数
					organizaHierarModalVo.setTotalMembers(grpHolderInfo.getNumOfEnterprise());
					//证件号码
					organizaHierarModalVo.setIdCardNo(grpHolderInfo.getGrpIdNo());
					//在职人数
					organizaHierarModalVo.setOjEmpNum(grpHolderInfo.getOnjobStaffNum());
					//投保人数
					organizaHierarModalVo.setApplNum(grpHolderInfo.getIpsnNum());
					//企业注册国籍
					organizaHierarModalVo.setRegisteredNationality(grpHolderInfo.getGrpCountryCode());
					//联系人姓名
					organizaHierarModalVo.setContactName(grpHolderInfo.getContactName());
					//手机号码
					organizaHierarModalVo.setPhoneNum(grpHolderInfo.getContactMobile());
					//电子邮件
					organizaHierarModalVo.setEmail(grpHolderInfo.getContactEmail());
					//固定电话
					organizaHierarModalVo.setFixedPhones(grpHolderInfo.getContactTelephone());
					//地址
					Address address = grpHolderInfo.getAddress();
					if(null != address){						
						//省、自治区
						organizaHierarModalVo.setProvince(address.getProvince());
						//市、州
						organizaHierarModalVo.setCity(address.getCity());
						//区、县
						organizaHierarModalVo.setCounty(address.getCounty());
						//镇、乡
						organizaHierarModalVo.setTown(address.getTown());
						//村、社区
						organizaHierarModalVo.setVillage(address.getVillage());
						//地址明细
						organizaHierarModalVo.setDetailAddress(address.getHomeAddress());
						//邮政编码
						organizaHierarModalVo.setPostCode(address.getPostCode());
					}
				}
				//发票选项
				//organizaHierarModalVo.setInvoiceOption(invoiceOption);
				//增值税号
				//organizaHierarModalVo.setVatNum(vatNum);
				list.add(organizaHierarModalVo);
			}
		}
		return list;
	}
	
	/**
	 * 新增险种信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<SgGrpAddinsuranceVo> sgGrpAddinsuranceVoBoToVo(GrpInsurAppl grpInsurAppl) {
		if(null == grpInsurAppl){
			return null;
		}
		//新增险种集合
		List<SgGrpAddinsuranceVo> list = new ArrayList<SgGrpAddinsuranceVo>();
		//险种
		List<Policy> polocyList = grpInsurAppl.getApplState().getPolicyList();
		if(null != polocyList && polocyList.size() > 0){
			for(int i = 0; i < polocyList.size(); i++){
				//责任集合
				List<ResponseVo> responseVoList = new ArrayList<ResponseVo>();
				//新增险种信息
				SgGrpAddinsuranceVo sgGrpAddinsuranceVo = new SgGrpAddinsuranceVo();
				//险种代码
				sgGrpAddinsuranceVo.setBusiPrdCode(polocyList.get(i).getPolCode());
				//险种名称
				sgGrpAddinsuranceVo.setBusiPrdName(polocyList.get(i).getPolNameChn());
				//保额
				sgGrpAddinsuranceVo.setAmount(polocyList.get(i).getFaceAmnt());
				//保费
				sgGrpAddinsuranceVo.setPremium(polocyList.get(i).getPremium());
				//承保人数
				sgGrpAddinsuranceVo.setInsuredNum(polocyList.get(i).getPolIpsnNum());
				//保险期间
				sgGrpAddinsuranceVo.setValidateDate(polocyList.get(i).getInsurDur());
				//保险期类型
				sgGrpAddinsuranceVo.setInsurDurUnit(polocyList.get(i).getInsurDurUnit());
				//健康险专项标识
				sgGrpAddinsuranceVo.setHealthInsurFlag(polocyList.get(i).getSpeciBusinessLogo());
				//子险种
				List<SubPolicy> subPolicyList = polocyList.get(i).getSubPolicyList();
				if(null != subPolicyList && subPolicyList.size() > 0){
					for(int j = 0; j < subPolicyList.size(); j++){						
						//责任信息
						ResponseVo responseVo = new ResponseVo();
						//投保单ID
						responseVo.setApplId(grpInsurAppl.getApplNo());
						//责任代码
						responseVo.setProductCode(subPolicyList.get(j).getSubPolCode());
						//险种代码
						responseVo.setBusiPrdCode(polocyList.get(i).getPolCode());
						//责任名称   
						responseVo.setProductName(subPolicyList.get(j).getSubPolName());
						//保额
						responseVo.setProductNum(subPolicyList.get(j).getSubPolAmnt());
						//保费
						responseVo.setProductPremium(subPolicyList.get(j).getSubPremium());
						//险种标准保费
						responseVo.setSubStdPremium(subPolicyList.get(j).getSubStdPremium());
						//责任号
						//responseVo.setProductNo(productNo);
						responseVoList.add(responseVo);
					}
				}
				//责任列表信息
				sgGrpAddinsuranceVo.setResponseVos(responseVoList);
				list.add(sgGrpAddinsuranceVo);
			}
		}
		return list;
	}
	
	/**
	 * 收付费分组信息Bo转Vo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<ChargePayGroupModalVo> chargePayGroupModalVoBoToVo(GrpInsurAppl grpInsurAppl) {
		List<ChargePayGroupModalVo> list = new ArrayList<ChargePayGroupModalVo>();
		//团体收费组集合
		List<IpsnPayGrp> ipsnPayGrpList = grpInsurAppl.getIpsnPayGrpList();
		if(null != ipsnPayGrpList && ipsnPayGrpList.size() > 0){
			for(int i = 0; i < ipsnPayGrpList.size(); i++){				
				//收付费分组信息
				ChargePayGroupModalVo chargePayGroupModalVo = new ChargePayGroupModalVo();
				//组号
				chargePayGroupModalVo.setGroupNo(ipsnPayGrpList.get(i).getFeeGrpNo());
				//组名
				chargePayGroupModalVo.setGroupName(ipsnPayGrpList.get(i).getFeeGrpName());
				//人数
				chargePayGroupModalVo.setNum(ipsnPayGrpList.get(i).getIpsnGrpNum());
				//交费形式
				chargePayGroupModalVo.setMoneyinType(ipsnPayGrpList.get(i).getMoneyinType());
				//开户银行
				chargePayGroupModalVo.setBankCode(ipsnPayGrpList.get(i).getBankCode());
				//开户名称
				chargePayGroupModalVo.setBankName(ipsnPayGrpList.get(i).getBankaccName());
				//银行账号	
				chargePayGroupModalVo.setBankAccNo(ipsnPayGrpList.get(i).getBankaccNo());
				list.add(chargePayGroupModalVo);
			}
		}
		return list;
	}
}