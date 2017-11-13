package com.newcore.orbps.web.util;
	 
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpSpecialInsurAddInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import org.apache.commons.lang3.StringUtils;

/** 
 * 
 * @Description: 团单Vo转Bo
 * @ClassName: GrpDataTransfor 
 * @author: jinmeina
 * @date: 2016-12-03 15:58:00 
 * 
 */
public class GrpDataVoToBo {
	/**
	 * 团单Vo转Bo
	 * @param grpInsurApplVo
	 * @return
	 */
	public GrpInsurAppl grpinsurApplVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		//团体出单基本信息
		GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
		//接入来源
		grpInsurAppl.setAccessSource("ORBPS");
		//投保单信息
		GrpApplInfoVo grpApplInfoVo = grpInsurApplVo.getApplInfoVo();
		if(null != grpApplInfoVo){
			//投保单号
			grpInsurAppl.setApplNo(grpApplInfoVo.getApplNo());
			//团险方案审批号
			grpInsurAppl.setApproNo(grpApplInfoVo.getQuotaEaNo());
			//投保日期
			grpInsurAppl.setApplDate(grpApplInfoVo.getApplDate());
			//上期保单号
			grpInsurAppl.setRenewedCgNo(grpApplInfoVo.getOldApplNo());
			//共保协议号
			grpInsurAppl.setAgreementNo(grpApplInfoVo.getAgreementNo());
		}
		//打印信息
		GrpPrintInfoVo grpPrintInfoVo = grpInsurApplVo.getPrintInfoVo();
		if(null != grpPrintInfoVo){
			//团单清单标志
			grpInsurAppl.setLstProcType(grpPrintInfoVo.getIpsnlstId());
			//保单性质
			grpInsurAppl.setInsurProperty(grpPrintInfoVo.getApplProperty());
			//合同打印方式
			grpInsurAppl.setCntrPrintType(grpPrintInfoVo.getCntrType());
			//清单打印方式
			grpInsurAppl.setListPrintType(grpPrintInfoVo.getPrtIpsnLstType());
			//个人凭证类型
			grpInsurAppl.setVoucherPrintType(grpPrintInfoVo.getIpsnVoucherPrt());
			//契约形式
			grpInsurAppl.setCntrType(grpPrintInfoVo.getCntrType());
			//赠送保险标志
			grpInsurAppl.setGiftFlag(grpPrintInfoVo.getGiftFlag());
			//是否异常告知
			grpInsurAppl.setNotificaStat(grpPrintInfoVo.getExceptionInform());
			//人工核保标志
			//grpInsurAppl.setUdwType(grpPrintInfoVo.getManualCheck());
		}
		//保单基本信息
		GrpApplBaseInfoVo grpApplBaseInfoVo = grpInsurApplVo.getApplBaseInfoVo();
		if(null != grpApplBaseInfoVo){
			//契约业务类型
			grpInsurAppl.setApplBusiType(grpApplBaseInfoVo.getBusinessFlag());
			//争议处理方式
			grpInsurAppl.setArgueType(grpApplBaseInfoVo.getDisputePorcWay());
			//仲裁委员会名称
			grpInsurAppl.setArbitration(grpApplBaseInfoVo.getArbOrgName());			
		}
		//团体客户信息Vo转Bo
		grpInsurAppl.setGrpHolderInfo(grpHolderInfoVoToBo(grpInsurApplVo));
		//销售信息Vo转Bo
		grpInsurAppl.setSalesInfoList(salesInfoVoToBo(grpInsurApplVo));
		//交费相关Vo转Bo
		grpInsurAppl.setPaymentInfo(paymentInfoVoToBo(grpInsurApplVo));
		//投保要约Vo转Bo
		grpInsurAppl.setApplState(applStateVoToBo(grpInsurApplVo));
		//要约分组Vo转Bo
		grpInsurAppl.setIpsnStateGrpList(ipsnStateGrpVoToBo(grpInsurApplVo));
		//团体收费组Vo转Bo
		grpInsurAppl.setIpsnPayGrpList(ipsnPayGrpVoToBo(grpInsurApplVo));
		//组织架构树Vo转Bo（团单特有）
		if(grpInsurApplVo.getOrganizaHierarModalVos().size() > 0){
			grpInsurAppl.setOrgTreeList(orgTreeVoToBo(grpInsurApplVo));
		}
		//建工险Vo转Bo（团单特有）
		grpInsurAppl.setConstructInsurInfo(constructInsurInfoVoToBo(grpInsurApplVo));
		//健康险Vo转Bo（团单特有）
		grpInsurAppl.setHealthInsurInfo(healthInsurInfoVoToBo(grpInsurApplVo));
		//基金险Vo转Bo（团单特有）
		grpInsurAppl.setFundInsurInfo(fundInsurInfoVoToBo(grpInsurApplVo));
		//特约（团单特有）
		grpInsurAppl.setConventions(conventionsVoToBo(grpInsurApplVo));
		//接入通道（controller写死，或者取字典值）
		//grpInsurApplVo.setAccessChannel("");
		//团体业务分类（待确认）
		//grpInsurAppl.setGrpBusiType(grpBusiType);
		//销售人员是否共同展业标识（受理界面有此字段，后续业务不再传值）
		//grpInsurAppl.setSalesDevelopFlag(salesDevelopFlag);
		//保单送达方式（待确认）
		//grpInsurAppl.setCntrSendType(cntrSendType);
		//外包录入标志（受理界面有此字段，后续业务不在传值，字典值待确认）
		//grpInsurAppl.setEntChannelFlag(entChannelFlag);
		//汇交人类型（待确认）
		//grpInsurAppl.setSgType(sgType);
		//合同组号（待确认）
		//grpInsurAppl.setCgNo(cgNo);
		//汇交件号（待确认）
		//grpInsurAppl.setSgNo(sgNo);
		//签单日期（待确认）
		//grpInsurAppl.setSignDate(signDate);
		//生效日期（待确认）
		//grpInsurAppl.setInForceDate(inForceDate);
		//续保次数（待确认）
		//grpInsurAppl.setRenewTimes(renewTimes);
		//合同预计满期日期（待确认）
		//grpInsurAppl.setCntrExpiryDate(cntrExpiryDate);
		//第一主险（不传，后台默认取险种列表第一个险种为第一主险）
		//grpInsurAppl.setFirPolCode(firPolCode);
		//指定退保公式（待确认）
		//grpInsurAppl.setSrndAmntCmptType(srndAmntCmptType);
		//退保手续费比例（待确认）
		//grpInsurAppl.setSrdFeeRate(srdFeeRate);
		//省级机构号（待确认）
		//grpInsurAppl.setProvBranchNo(provBranchNo);
		//管理机构号（待确认）
		//grpInsurAppl.setMgrBranchNo(mgrBranchNo);
		//归档机构号（待确认）
		//grpInsurAppl.setArcBranchNo(arcBranchNo);
		//个人汇交人信息（待确认）
		//grpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);
		
		return grpInsurAppl;
	}
	
	/**
	 * 团体客户信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public GrpHolderInfo grpHolderInfoVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		//团体客户信息
		GrpHolderInfo holderInfo = new GrpHolderInfo();
		//增值税信息
		if(grpInsurApplVo.getVatInfoVo() != null){
			holderInfo.setTaxpayerId(grpInsurApplVo.getVatInfoVo().getTaxpayerCode());
		}
		//保单基本信息
		GrpApplBaseInfoVo grpApplBaseInfoVo = grpInsurApplVo.getApplBaseInfoVo();
		if(null != grpApplBaseInfoVo){
			//单位名称
			holderInfo.setGrpName(grpApplBaseInfoVo.getCompanyName());
			//团体客户证件类型
			holderInfo.setGrpIdType(grpApplBaseInfoVo.getIdType());
			//团体客户证件号码
			holderInfo.setGrpIdNo(grpApplBaseInfoVo.getIdNo());
			//企业注册地国籍
			holderInfo.setGrpCountryCode(grpApplBaseInfoVo.getRegisterArea());
			//外管局部门类型
			holderInfo.setGrpPsnDeptType(grpApplBaseInfoVo.getDeptType());
			//行业类别
			holderInfo.setOccClassCode(grpApplBaseInfoVo.getOccDangerFactor());
			//传真
			holderInfo.setFax(grpApplBaseInfoVo.getAppHomeFax());
			//员工总数
			holderInfo.setNumOfEnterprise(grpApplBaseInfoVo.getNumOfEmp());
			//在职人数
			holderInfo.setOnjobStaffNum(grpApplBaseInfoVo.getOjEmpNum());
			//投保人数
			holderInfo.setIpsnNum(grpApplBaseInfoVo.getApplNum());
			//联系人姓名
			holderInfo.setContactName(grpApplBaseInfoVo.getConnName());
			//联系人证件类别
			holderInfo.setContactIdType(grpApplBaseInfoVo.getConnIdType());
			//联系人证件号码
			holderInfo.setContactIdNo(grpApplBaseInfoVo.getConnIdNo());
			//联系人移动电话
			holderInfo.setContactMobile(grpApplBaseInfoVo.getConnPhone());
			//联系人固定电话
			holderInfo.setContactTelephone(grpApplBaseInfoVo.getAppHomeTel());
			//联系人电子邮件
			holderInfo.setContactEmail(grpApplBaseInfoVo.getConnPostcode());
			//地址信息
			Address address = new Address();
			//省、自治州
			address.setProvince(grpApplBaseInfoVo.getAppAddrProv());
			//市、州
			address.setCity(grpApplBaseInfoVo.getAppAddrCity());
			//区、县
			address.setCounty(grpApplBaseInfoVo.getAppAddrCountry());
			//镇、乡
			address.setTown(grpApplBaseInfoVo.getAppAddrTown());
			//社、社区
			address.setVillage(grpApplBaseInfoVo.getAppAddrValige());
			//地址明细
			address.setHomeAddress(grpApplBaseInfoVo.getAppAddrHome());
			//邮政编码
			address.setPostCode(grpApplBaseInfoVo.getAppPost());
			//地址
			holderInfo.setAddress(address);
		}
		
		//投保单位CMDS客户号（后台接口查询）
		//holderInfo.setPartyId(partyId);
		//单位性质（法律分类）（待确认）
		//holderInfo.setLegalCode(legalCode);
		//法人代表（待确认）
		//holderInfo.setCorpRep(corpRep);
		//团体法人客户号（后台接口查询）
		//holderInfo.setGrpCustNo(grpCustNo);
		//曾用名（待确认）
		//holderInfo.setFormerGrpName(formerGrpName);
		//单位性质（经济分类）（待确认）
		//holderInfo.setNatureCode(natureCode);
		
		return holderInfo;
	}
	
	/**
	 * 销售相关信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public List<SalesInfo> salesInfoVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		// 销售相关信息
		List<SalesInfo> salesInfos = new ArrayList<SalesInfo>();
		//投保单信息
		GrpApplInfoVo grpApplInfoVo = grpInsurApplVo.getApplInfoVo();
		if(grpApplInfoVo.getGrpSalesListFormVos()!=null){
		    for (int i = 0; i < grpApplInfoVo.getGrpSalesListFormVos().size(); i++) {
		        // 销售相关
		        SalesInfo salesInfo = new SalesInfo();
		        // 销售机构代码
		        salesInfo.setSalesBranchNo(grpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesBranchNo());
		        // 销售渠道
		        salesInfo.setSalesChannel(grpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesChannel());
		        // 销售机构
		        salesInfo.setSalesBranchNo(grpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesBranchNo());
		        if ("OA".equals(grpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesChannel())) {
		            // 代理网点号
		            salesInfo.setSalesDeptNo(grpApplInfoVo.getGrpSalesListFormVos().get(i).getWorksiteNo());
		            // 网点名称
		            salesInfo.setDeptName(grpApplInfoVo.getGrpSalesListFormVos().get(i).getWorksiteName());
		            //代理员工号
		            if(StringUtils.isNotEmpty(grpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyNo())){
		            	salesInfo.setCommnrPsnNo(grpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyNo());
		            }
		            //代理员工姓名
		            if(StringUtils.isNotEmpty(grpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyName())){
		            	salesInfo.setCommnrPsnName(grpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyName());
		            }
		            
		        } else {
		            // 销售人员代码
		            salesInfo.setSalesNo(grpApplInfoVo.getGrpSalesListFormVos().get(i).getSaleCode());
		            // 销售人员姓名
		            salesInfo.setSalesName(grpApplInfoVo.getGrpSalesListFormVos().get(i).getSaleName());
		        }
		        salesInfos.add(salesInfo);
		    }
		}
		
		//销售机构地址（待确认）
		//salesInfo.setSalesBranchAddr(salesBranchAddr);
		//销售机构邮政编码（待确认）
		//salesInfo.setSalesBranchPostCode(salesBranchPostCode);
		//网点名（受理界面添加此字段，后续业务不再传值）
		//salesInfo.setDeptNo(deptNo);
		//网点名称（待确认）
		//salesInfo.setDeptName(deptName);
		//网点代理员工号（受理界面添加此字段，后续业务不再传值）
		//salesInfo.setCommnrPsnNo(commnrPsnNo);
		//网点代理员姓名（待确认）
		//salesInfo.setCommnrPsnName(commnrPsnName);
		//成本中心（待确认）
		//salesInfo.setCenterCode(centerCode);
		//共同展业主副标记（受理界面有此字段，后续业务不再传值）
		//salesInfo.setDevelMainFlag(develMainFlag);
		//展业比例（受理界面有此字段，后续业务不再传值）
		//salesInfo.setDevelopRate(developRate);

		return salesInfos;
	}
	
	/**
	 * 交费相关信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public PaymentInfo paymentInfoVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		PaymentInfo paymentInfo = new PaymentInfo();
		//交费信息
		GrpPayInfoVo grpPayInfoVo = grpInsurApplVo.getPayInfoVo();
		if(null != grpPayInfoVo){
			//团体保费来源
			paymentInfo.setPremSource(grpPayInfoVo.getPremFrom());
			//交费方式
			paymentInfo.setMoneyinItrvl(grpPayInfoVo.getMoneyinItrvl());
			//交费形式
			paymentInfo.setMoneyinType(grpPayInfoVo.getMoneyinType());
			//开户银行
			paymentInfo.setBankCode(grpPayInfoVo.getBankCode());
			//开户名称
			paymentInfo.setBankAccName(grpPayInfoVo.getBankName());
			//银行账号
			paymentInfo.setBankAccNo(grpPayInfoVo.getBankAccNo());
			//结算方式
			paymentInfo.setStlType(grpPayInfoVo.getStlType());
			//结算限额
			paymentInfo.setStlAmnt(grpPayInfoVo.getStlLimit());
			//结算日期集合
			List<Date> stlDate = new ArrayList<Date>();
			if(grpPayInfoVo.getSettlementDate().size()>0){
				for(int i = 0;i <grpPayInfoVo.getSettlementDate().size();i++){
					stlDate.add(grpPayInfoVo.getSettlementDate().get(i));
				}
			}
			// 结算日期
			paymentInfo.setStlDate(stlDate);
			//结算比例
			paymentInfo.setStlRate(grpPayInfoVo.getSettlementRatio());
		}
		//要约信息
		GrpProposalInfoVo grpProposalInfoVo = grpInsurApplVo.getProposalInfoVo();
		if(null != grpProposalInfoVo){
			//首期扣款截止日期
			paymentInfo.setForeExpDate(grpProposalInfoVo.getEnstPremDeadline());
		}
		
		//多团体个人共同付款（待确认）
		//paymentInfo.setMultiPartyScale(multiPartyScale);
		//团体个人共同付款（待确认）
		//paymentInfo.setMultiPartyMoney(multiPartyMoney);
		//交费期（待确认）
		//paymentInfo.setMoneyinDur(moneyinDur);
		//交费期单位（清汇信息）（待确认）
		//paymentInfo.setMoneyinDurUnit(moneyinDurUnit);
		//是否需要续期扣款（清汇信息）
		//paymentInfo.setIsRenew(isRenew);
		//续期扣款截至日（清汇信息）
		//paymentInfo.setRenewExpDate(renewExpDate);
		//是否多期暂缴（清汇信息）
		//paymentInfo.setIsMultiPay(isMultiPay);
		//多期暂缴年数（清汇信息）
		//paymentInfo.setMutipayTimes(mutipayTimes);
		//币种（待确认）
		//paymentInfo.setCurrencyCode(currencyCode);
		
		return paymentInfo;
	}
	
	/**
	 * 投保要约信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public ApplState applStateVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		ApplState applState = new ApplState();
		//要约信息
		GrpProposalInfoVo grpProposalInfoVo = grpInsurApplVo.getProposalInfoVo();
		if(null != grpProposalInfoVo){
			//投保人数
			applState.setIpsnNum(grpProposalInfoVo.getIpsnNum());
			//总保费
			applState.setSumPremium(grpProposalInfoVo.getSumPrem());
			//生效日类型
			applState.setInforceDateType(grpProposalInfoVo.getForceType());
			//指定生效日
			applState.setDesignForceDate(grpProposalInfoVo.getInForceDate());
			//是否频次生效
			applState.setIsFreForce(grpProposalInfoVo.getFrequenceEff());
			//生效频率
			applState.setForceFre(grpProposalInfoVo.getForceNum());
		}
		//险种集合Bo
		List<Policy> policyList = new ArrayList<Policy>();
		//险种集合Vo
		List<GrpBusiPrdVo> grpBusiPrdVoList = grpInsurApplVo.getBusiPrdVos();
		if(null != grpBusiPrdVoList && grpBusiPrdVoList.size() > 0){
			for(int i = 0; i < grpBusiPrdVoList.size(); i++){
				//险种
				Policy policy = new Policy();
				//险种
				policy.setPolCode(grpBusiPrdVoList.get(i).getBusiPrdCode());
				//险种名称
				policy.setPolNameChn(grpBusiPrdVoList.get(i).getBusiPrdCodeName());
				//保险期间
				policy.setInsurDur(grpBusiPrdVoList.get(i).getInsurDur());
				//保险期类型
				policy.setInsurDurUnit(grpBusiPrdVoList.get(i).getInsurDurUnit());
				//险种保额
				policy.setFaceAmnt(grpBusiPrdVoList.get(i).getAmount());
				//实际保费
				policy.setPremium(grpBusiPrdVoList.get(i).getPremium());
				//险种投保人数
				policy.setPolIpsnNum(grpBusiPrdVoList.get(i).getInsuredNum());
				//专项业务标识
				policy.setSpeciBusinessLogo(grpBusiPrdVoList.get(i).getHealthInsurFlag());
				//子险种集合Bo
				List<SubPolicy> subPolicyList = new ArrayList<SubPolicy>();
				//责任信息集合
				List<ResponseVo> responseVoList = grpBusiPrdVoList.get(i).getResponseVos();
				if(null != responseVoList && responseVoList.size() > 0){
					for(int j = 0; j < responseVoList.size(); j++){
						//子险种
						SubPolicy subPolicy = new SubPolicy();
						//子险种号
						subPolicy.setSubPolCode(responseVoList.get(j).getProductCode());
						//子险种名称
						subPolicy.setSubPolName(responseVoList.get(j).getProductName());
						//险种保额
						subPolicy.setSubPolAmnt(responseVoList.get(j).getProductNum());
						//险种实际保费
						subPolicy.setSubPremium(responseVoList.get(j).getProductPremium());
						//险种标准保费
						subPolicy.setSubStdPremium(responseVoList.get(j).getSubStdPremium());
						subPolicyList.add(subPolicy);
					}
				}
				
				//子险种
				policy.setSubPolicyList(subPolicyList);
				policyList.add(policy);
				
				//标准保费
				//policy.setStdPremium(stdPremium);
				//分保单号
				//policy.setCntrNo(cntrNo);
				//主附险性质
				//policy.setMrCode(mrCode);
				//交费间隔月
				//policy.setMoneyinItrvlMon(moneyinItrvlMon);
				//保费折扣
				//policy.setPremDiscount(premDiscount);
				//核保结论
				//policy.setTuPolResult(tuPolResult);
			}
			
		}
		//险种
		applState.setPolicyList(policyList);
		
		//总保额
		//applState.setSumFaceAmnt(sumFaceAmnt);
		//保险期间
		//applState.setInsurDur(insurDur);
		//保险期类型
		//applState.setInsurDurUnit(insurDurUnit);
		//是否预打印
		//applState.setIsPrePrint(isPrePrint);
		
		return applState;
	}
	
	/**
	 * 要约分组信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public List<IpsnStateGrp> ipsnStateGrpVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		List<IpsnStateGrp> list = new ArrayList<IpsnStateGrp>();
		//被保人分组集合
		List<InsuredGroupModalVo> insuredGroupModalVoList = grpInsurApplVo.getInsuredGroupModalVos();
		if(null != insuredGroupModalVoList && insuredGroupModalVoList.size() > 0){
			for(int i = 0;i < insuredGroupModalVoList.size(); i++){
				//要约分组（整体选录）
				IpsnStateGrp ipsnStateGrp = new IpsnStateGrp();
				//分组类型
				//ipsnStateGrp.setIpsnGrpType(ipsnGrpType);
				//要约属组编号
				ipsnStateGrp.setIpsnGrpNo(insuredGroupModalVoList.get(i).getIpsnGrpNo());
				//要约属组名称
				ipsnStateGrp.setIpsnGrpName(insuredGroupModalVoList.get(i).getIpsnGrpName());
				//行业类别
				ipsnStateGrp.setOccClassCode(insuredGroupModalVoList.get(i).getOccClassCode());
				//职业代码
				ipsnStateGrp.setIpsnOccCode(insuredGroupModalVoList.get(i).getIpsnOccSubclsCode());
				//要约属组人数
				ipsnStateGrp.setIpsnGrpNum(insuredGroupModalVoList.get(i).getIpsnGrpNum());
				//参加工伤比例
				ipsnStateGrp.setGsPct(insuredGroupModalVoList.get(i).getGsRate());
				//参加医保比例
				ipsnStateGrp.setSsPct(insuredGroupModalVoList.get(i).getSsRate());
				//男女比例
				ipsnStateGrp.setGenderRadio(insuredGroupModalVoList.get(i).getGenderRadio());
				//被保人分组类型
				GrpPrintInfoVo grpPrintInfoVo = grpInsurApplVo.getPrintInfoVo();
				if(null != grpPrintInfoVo){
					ipsnStateGrp.setIpsnGrpType(grpPrintInfoVo.getGroupType());
				}
				//险种集合Bo
				List<GrpPolicy> grpPolicyList = new ArrayList<GrpPolicy>();
				//险种集合Vo
				List<InsuranceInfoVo> insuranceInfoVoList = insuredGroupModalVoList.get(i).getInsuranceInfoVos();
				if(null != insuranceInfoVoList && insuranceInfoVoList.size() > 0){
					for(int j = 0; j < insuranceInfoVoList.size(); j++){
						//险种
						GrpPolicy grpPolicy = new GrpPolicy();
						//险种
						grpPolicy.setPolCode(insuranceInfoVoList.get(j).getPolCode());
						//险种
						grpPolicy.setMrCode(insuranceInfoVoList.get(j).getMrCode());
						//险种保额
						grpPolicy.setFaceAmnt(insuranceInfoVoList.get(j).getFaceAmnt());
						//实际保费
						grpPolicy.setPremium(insuranceInfoVoList.get(j).getPremium());
						//标准保费
						grpPolicy.setStdPremium(insuranceInfoVoList.get(j).getStdPremium());
						//承保费率
						grpPolicy.setPremRate(insuranceInfoVoList.get(j).getPremRate());
						//费率浮动幅度
						grpPolicy.setPremDiscount(insuranceInfoVoList.get(j).getRecDisount());
						grpPolicyList.add(grpPolicy);
					}
				}
				//险种
				ipsnStateGrp.setGrpPolicyList(grpPolicyList);
				list.add(ipsnStateGrp);
			}
		}
		return list;
	}
	
	/**
	 * 团体收费组信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public List<IpsnPayGrp> ipsnPayGrpVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		List<IpsnPayGrp> list = new ArrayList<IpsnPayGrp>();
		//收费费分组信息
		List<ChargePayGroupModalVo> chargePayGroupModalVoList = grpInsurApplVo.getChargePayGroupModalVos();
		if(null != chargePayGroupModalVoList && chargePayGroupModalVoList.size() > 0){
			for(int i = 0; i < chargePayGroupModalVoList.size(); i++){
				//团体收费组
				IpsnPayGrp ipsnPayGrp = new IpsnPayGrp();
				//收费组属组编号
				ipsnPayGrp.setFeeGrpNo(chargePayGroupModalVoList.get(i).getGroupNo());
				//收费组属组名称
				ipsnPayGrp.setFeeGrpName(chargePayGroupModalVoList.get(i).getGroupName());
				//收费属组人数
				ipsnPayGrp.setIpsnGrpNum(chargePayGroupModalVoList.get(i).getNum());
				//交费形式
				ipsnPayGrp.setMoneyinType(chargePayGroupModalVoList.get(i).getMoneyinType());
				//开户银行
				ipsnPayGrp.setBankCode(chargePayGroupModalVoList.get(i).getBankCode());
				//开户名称
				ipsnPayGrp.setBankaccName(chargePayGroupModalVoList.get(i).getBankName());
				//银行账号
				ipsnPayGrp.setBankaccNo(chargePayGroupModalVoList.get(i).getBankAccNo());
				//支票号
				//ipsnPayGrp.setBankChequeNo(bankChequeNo);
				list.add(ipsnPayGrp);
			}
		}
		return list;
	}

	/**
	 * 组织架构树信息Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public List<OrgTree> orgTreeVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		List<OrgTree> list = new ArrayList<OrgTree>();
		//组织层次信息集合
		List<OrganizaHierarModalVo> organizaHierarModalVoList = grpInsurApplVo.getOrganizaHierarModalVos();
		if(null != organizaHierarModalVoList && organizaHierarModalVoList.size() > 0){
			for(int i = 0; i < organizaHierarModalVoList.size(); i++){
				//组织关系树
				OrgTree orgTree = new OrgTree();
				//是否交费
				orgTree.setIsPaid(organizaHierarModalVoList.get(i).getPay());
				//保全选项
				orgTree.setMtnOpt(organizaHierarModalVoList.get(i).getSecurityOptions());
				//服务指派
				orgTree.setServiceOpt(organizaHierarModalVoList.get(i).getServiceAssignment());
				//发票选项
				orgTree.setReceiptOpt(organizaHierarModalVoList.get(i).getInvoiceOption());
				//团体客户信息
				GrpHolderInfo grpHolderInfo = new GrpHolderInfo();
				//开户名称
				orgTree.setBankaccName(organizaHierarModalVoList.get(i).getBankaccName());
				//开户银行
				orgTree.setBankCode(organizaHierarModalVoList.get(i).getBankCode());
				//银行账号
				orgTree.setBankaccNo(organizaHierarModalVoList.get(i).getBankaccNo());
				//层级代码
                orgTree.setLevelCode(organizaHierarModalVoList.get(i).getLevelCode());
				//上级层级代码
                orgTree.setPrioLevelCode(organizaHierarModalVoList.get(i).getPrioLevelCode());
				//节点类型
                orgTree.setNodeType(organizaHierarModalVoList.get(i).getNodeType());
				//是否根节点
                orgTree.setIsRoot(organizaHierarModalVoList.get(i).getIsRoot());
				//交费形式
				//orgTree.setMoneyinType(moneyinType);
				//节点交费金额
				orgTree.setNodePayAmnt(organizaHierarModalVoList.get(i).getNodePayAmnt());
				//支票号
				//orgTree.setBankChequeNo(bankChequeNo);
				//投保单位CMDS客户号
				grpHolderInfo.setPartyId(organizaHierarModalVoList.get(i).getPartyId());
				//团体法人客户号
				grpHolderInfo.setGrpCustNo(organizaHierarModalVoList.get(i).getCustNo());
				if("0".equals(organizaHierarModalVoList.get(i).getNodeType())){
				    //单位名称
				    grpHolderInfo.setGrpName(organizaHierarModalVoList.get(i).getCompanyName());
				}else{
				    //单位名称
                    grpHolderInfo.setGrpName(organizaHierarModalVoList.get(i).getCompanyName()+"~"+organizaHierarModalVoList.get(i).getGroupDep());
				}
				//曾用名
				grpHolderInfo.setFormerGrpName(organizaHierarModalVoList.get(i).getOldName());
				//团体客户证件类别
				grpHolderInfo.setGrpIdType(organizaHierarModalVoList.get(i).getDeptType());
				//团体客户证件号码
				grpHolderInfo.setGrpIdNo(organizaHierarModalVoList.get(i).getIdCardNo());
				//单位性质
				grpHolderInfo.setOccClassCode(organizaHierarModalVoList.get(i).getIndustryClassification());
				//在职人数
				grpHolderInfo.setOnjobStaffNum(organizaHierarModalVoList.get(i).getOjEmpNum());
				//地址
				Address address = new Address();
				//省、自治州
				address.setProvince(organizaHierarModalVoList.get(i).getProvince());
				//市、州
				address.setCity(organizaHierarModalVoList.get(i).getCity());
				//区、县
				address.setCounty(organizaHierarModalVoList.get(i).getCounty());
				//镇、乡
				address.setTown(organizaHierarModalVoList.get(i).getTown());
				//村、社区
				address.setVillage(organizaHierarModalVoList.get(i).getVillage());
				//地址明细
				address.setHomeAddress(organizaHierarModalVoList.get(i).getDetailAddress());
				//邮政编码
				address.setPostCode(organizaHierarModalVoList.get(i).getPostCode());
				//地址
				grpHolderInfo.setAddress(address);
				//保单基本信息
				//企业注册地国籍
				grpHolderInfo.setGrpCountryCode(organizaHierarModalVoList.get(i).getRegisteredNationality());
				//外管局部门类别
				grpHolderInfo.setGrpPsnDeptType(organizaHierarModalVoList.get(i).getDeptType());
				//行业类别
				grpHolderInfo.setOccClassCode(organizaHierarModalVoList.get(i).getIndustryClassification());
				//员工总数
				grpHolderInfo.setNumOfEnterprise(organizaHierarModalVoList.get(i).getTotalMembers());
				//投保人数
				grpHolderInfo.setIpsnNum(organizaHierarModalVoList.get(i).getApplNum());
				//联系人姓名
				grpHolderInfo.setContactName(organizaHierarModalVoList.get(i).getContactName());
				//联系人证件类别
				grpHolderInfo.setContactIdType(organizaHierarModalVoList.get(i).getDeptType());
				//联系人证件号码
				grpHolderInfo.setContactIdNo(organizaHierarModalVoList.get(i).getIdCardNo());
				//联系人移动电话
				grpHolderInfo.setContactMobile(organizaHierarModalVoList.get(i).getPhoneNum());
				//联系人固定电话
				grpHolderInfo.setContactTelephone(organizaHierarModalVoList.get(i).getFixedPhones());
				//联系人电子邮件
				grpHolderInfo.setContactEmail(organizaHierarModalVoList.get(i).getEmail());
				//团体客户信息
				orgTree.setGrpHolderInfo(grpHolderInfo);
				list.add(orgTree);
			}
		}
		return list;
	}
	
	/**
	 * 建工险Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public ConstructInsurInfo constructInsurInfoVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		ConstructInsurInfo constructInsurInfo = new ConstructInsurInfo();
		GrpSpecialInsurAddInfoVo grpSpecialInsurAddInfoVo = grpInsurApplVo.getSpecialInsurAddInfoVo();
		if(null != grpSpecialInsurAddInfoVo){
			//工程名称
			constructInsurInfo.setIobjName(grpSpecialInsurAddInfoVo.getProjectName());
			//工程地址
			constructInsurInfo.setProjLoc(grpSpecialInsurAddInfoVo.getProjectAddr());
			//工程类型
			constructInsurInfo.setProjType(grpSpecialInsurAddInfoVo.getProjectType());
			//工程总造价
			constructInsurInfo.setIobjCost(grpSpecialInsurAddInfoVo.getTotalCost());
			//工程总面积
			constructInsurInfo.setIobjSize(grpSpecialInsurAddInfoVo.getTotalArea());
			//保费收取方式
			constructInsurInfo.setPremCalType(grpSpecialInsurAddInfoVo.getCpnstMioType());
			//施工期间始
			constructInsurInfo.setConstructFrom(grpSpecialInsurAddInfoVo.getConstructionDur());
			//施工期间止
			constructInsurInfo.setConstructTo(grpSpecialInsurAddInfoVo.getUntil());
			//层高
			constructInsurInfo.setFloorHeight(grpSpecialInsurAddInfoVo.getFloorHeight());
			//企业资质
			constructInsurInfo.setEnterpriseLicence(grpSpecialInsurAddInfoVo.getEnterpriseLicence());
			//获奖情况
			constructInsurInfo.setAwardGrade(grpSpecialInsurAddInfoVo.getAwardGrade());
			//是否有安防措施
			constructInsurInfo.setSafetyFlag(grpSpecialInsurAddInfoVo.getSafetyFlag());
			//疾病死亡人数
			constructInsurInfo.setDiseaDeathNums(grpSpecialInsurAddInfoVo.getDiseaDeathNums());
			//疾病伤残人数
			constructInsurInfo.setDiseaDisableNums(grpSpecialInsurAddInfoVo.getDiseaDisableNums());
			//意外死亡人数
			constructInsurInfo.setAcdntDeathNums(grpSpecialInsurAddInfoVo.getAcdntDeathNums());
			//意外伤残人数
			constructInsurInfo.setAcdntDisableNums(grpSpecialInsurAddInfoVo.getAcdntDisableNums());
			//过去两年内是否有四级以上安全事故
			constructInsurInfo.setSaftyAcdntFlag(grpSpecialInsurAddInfoVo.getSaftyAcdntFlag());
			//工程位置类别
			constructInsurInfo.setProjLocType(grpSpecialInsurAddInfoVo.getProjLocType());
		}
		if(StringUtils.equals("", constructInsurInfo.getIobjName()) 
				&& StringUtils.equals("", constructInsurInfo.getProjLoc()) 
				&& StringUtils.equals("", constructInsurInfo.getProjType()) 
				&& StringUtils.equals("", constructInsurInfo.getPremCalType()) 
				&& ( null == constructInsurInfo.getIobjCost()) 
				&& ( null == constructInsurInfo.getIobjSize()) 
				&& ( null == constructInsurInfo.getConstructFrom()) 
				&& ( null == constructInsurInfo.getConstructTo())){
			return null;
		}else{
			return constructInsurInfo;
		}
	}
	
	/**
	 * 健康险Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public HealthInsurInfo healthInsurInfoVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		HealthInsurInfo healthInsurInfo = new HealthInsurInfo();
		//特殊险种附加信息
		GrpSpecialInsurAddInfoVo grpSpecialInsurAddInfoVo = grpInsurApplVo.getSpecialInsurAddInfoVo();
		if(null != grpSpecialInsurAddInfoVo){
			if("".equals(grpSpecialInsurAddInfoVo.getComInsurAmntUse()) || "0".equals(grpSpecialInsurAddInfoVo.getComInsurAmntUse())){
				return null;
			}
			//公共保额使用范围
			healthInsurInfo.setComInsurAmntUse(grpSpecialInsurAddInfoVo.getComInsurAmntUse());
			//公共保额使用形式
			healthInsurInfo.setComInsurAmntType(grpSpecialInsurAddInfoVo.getComInsurAmntType());
			//固定公共保额
			healthInsurInfo.setSumFixedAmnt(grpSpecialInsurAddInfoVo.getFixedComAmnt());
			//人均浮动公共保额
			healthInsurInfo.setIpsnFloatAmnt(grpSpecialInsurAddInfoVo.getIpsnFloatAmnt());
			//公共保费
			healthInsurInfo.setComInsrPrem(grpSpecialInsurAddInfoVo.getCommPremium());
			//人均浮动比例
			healthInsurInfo.setFloatInverse(grpSpecialInsurAddInfoVo.getIpsnFloatPct());
		} if(StringUtils.equals("", healthInsurInfo.getComInsurAmntUse()) 
				&& StringUtils.equals("", healthInsurInfo.getComInsurAmntType()) 
				&& ( null == healthInsurInfo.getComInsrPrem()) 
        		&& ( null == healthInsurInfo.getSumFixedAmnt()) 
        		&& ( null == healthInsurInfo.getIpsnFloatAmnt()) 
        		&& ( null == healthInsurInfo.getFloatInverse())){
        	return null;
        }else{
        	return healthInsurInfo;
        }
	}
	
	/**
	 * 基金险Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public FundInsurInfo fundInsurInfoVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		FundInsurInfo fundInsurInfo = new FundInsurInfo();
		//特殊险种附加信息
		GrpSpecialInsurAddInfoVo grpSpecialInsurAddInfoVo = grpInsurApplVo.getSpecialInsurAddInfoVo();
		if(null != grpSpecialInsurAddInfoVo){
			//管理费计提方式
			fundInsurInfo.setAdminFeeCopuType(grpSpecialInsurAddInfoVo.getAdminCalType());
			//管理费比例
			fundInsurInfo.setAdminFeePct(grpSpecialInsurAddInfoVo.getAdminPcent());
			//个人账户交费金额
			fundInsurInfo.setIpsnFundPremium(grpSpecialInsurAddInfoVo.getIpsnAccAmnt());
			//个人账户金额
			fundInsurInfo.setIpsnFundAmnt(grpSpecialInsurAddInfoVo.getInclIpsnAccAmnt());
			//公共账户交费金额
			fundInsurInfo.setSumFundPremium(grpSpecialInsurAddInfoVo.getSumPubAccAmnt());
			//计入公共账户金额
			fundInsurInfo.setSumFundAmnt(grpSpecialInsurAddInfoVo.getInclSumPubAccAmnt());	
			//基金险收到保费时间
			fundInsurInfo.setPreMioDate(grpSpecialInsurAddInfoVo.getPreMioDate());
			//账户余额
			fundInsurInfo.setAccBalance(grpSpecialInsurAddInfoVo.getAccBalance());
			//账户管理费金额
			fundInsurInfo.setAccAdminBalance(grpSpecialInsurAddInfoVo.getAccAdminBalance());
			//首期管理费总金额
			fundInsurInfo.setAccSumAdminBalance(grpSpecialInsurAddInfoVo.getAccSumAdminBalance());
		}
		if(StringUtils.equals("", fundInsurInfo.getAdminFeeCopuType())
				&& ("".equals(fundInsurInfo.getAdminFeePct()) || null == fundInsurInfo.getAdminFeePct())  
				&& ("".equals(fundInsurInfo.getIpsnFundPremium()) || null == fundInsurInfo.getIpsnFundPremium()) 
        		&& ("".equals(fundInsurInfo.getIpsnFundAmnt()) || null == fundInsurInfo.getIpsnFundAmnt()) 
        		&& ("".equals(fundInsurInfo.getSumFundPremium()) || null == fundInsurInfo.getSumFundPremium()) 
        		&& ("".equals(fundInsurInfo.getSumFundAmnt()) || null == fundInsurInfo.getSumFundAmnt())){
        	return null;
        }else{
        	return fundInsurInfo;
        }
	}
	
	/**
	 * 基金险Vo转Bo
	 * @param grpInsurApplVo 团单面向前端大Vo
	 * @author jinmeina
	 * @return
	 */
	public Conventions conventionsVoToBo(GrpInsurApplVo grpInsurApplVo) {
		if(null == grpInsurApplVo){
			return null;
		}
		Conventions conventions = new Conventions();
		//要约信息
		GrpProposalInfoVo grpProposalInfoVo = grpInsurApplVo.getProposalInfoVo();
		if(null != grpProposalInfoVo){			
			//险种责任特约
			conventions.setPolConv(grpProposalInfoVo.getSpecialPro());
		}
		//录入特约
		//conventions.setInputConv(inputConv);
		//契约设定特约
		//conventions.setOrSetConv(orSetConv);
		//核保特约
		//conventions.setUdwConv(udwConv);
		//未成年人特约
		//conventions.setMinorConv(minorConv);
		return conventions;
	}
}