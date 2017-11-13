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
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.GrpPrintInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
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
 * @Description: 清汇Vo转Bo
 * @ClassName: SgGrpDataVoToBo 
 * @author: jinmeina
 * @date: 2016-12-07 11:22:00 
 * 
 */
public class SgGrpDataVoToBo {
	/**	
	 * 清汇Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public GrpInsurAppl grpInsurApplVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		//团体出单基本信息
		GrpInsurAppl grpInsurAppl = new GrpInsurAppl();
		//接入来源
		grpInsurAppl.setAccessSource("ORBPS");
		//汇交人类型
		grpInsurAppl.setSgType(sgGrpInsurApplVo.getListType());
		//投保单信息
		SgGrpApplInfoVo sgGrpApplInfoVo = sgGrpInsurApplVo.getSgGrpApplInfoVo();
		if(null != sgGrpApplInfoVo ){
			//投保单号
			grpInsurAppl.setApplNo(sgGrpApplInfoVo.getApplNo());
			//团险方案审批号
			grpInsurAppl.setApproNo(sgGrpApplInfoVo.getQuotaEaNo());
			//投保日期
			grpInsurAppl.setApplDate(sgGrpApplInfoVo.getApplDate());
			//上期保单号
			grpInsurAppl.setRenewedCgNo(sgGrpApplInfoVo.getPreviousPolNo());
			//共保协议号
			grpInsurAppl.setAgreementNo(sgGrpApplInfoVo.getAgreementNo());
			
		}
		//打印信息
		SgGrpPrintInfoVo sgGrpPrintInfoVo = sgGrpInsurApplVo.getSgGrpPrintInfoVo();
		if(null != sgGrpPrintInfoVo){
			//团单清单标志
			grpInsurAppl.setLstProcType(sgGrpPrintInfoVo.getListFlag());
			//保单性质
			grpInsurAppl.setInsurProperty(sgGrpPrintInfoVo.getPolProperty());
			//合同打印方式
			grpInsurAppl.setCntrPrintType(sgGrpPrintInfoVo.getUnderNoticeType());
			//清单打印方式
			grpInsurAppl.setListPrintType(sgGrpPrintInfoVo.getListPrint());
			//个人凭证类型
			grpInsurAppl.setVoucherPrintType(sgGrpPrintInfoVo.getPersonalIdPrint());
			//赠送保险标志
			grpInsurAppl.setGiftFlag(sgGrpPrintInfoVo.getGiftInsFlag());
			//是否异常告知
			grpInsurAppl.setNotificaStat(sgGrpPrintInfoVo.getUnusualFlag());
			//人工核保标志
			//grpInsurAppl.setUdwType(sgGrpPrintInfoVo.getManualUwFlag());
		}
		//团体汇交信息
		SgGrpInsurInfoVo sgGrpInsurInfoVo = sgGrpInsurApplVo.getSgGrpInsurInfoVo();
		if("G".equals(sgGrpInsurApplVo.getListType())){
			if(null != sgGrpInsurInfoVo){			
				//争议处理方式
				grpInsurAppl.setArgueType(sgGrpInsurInfoVo.getgSettleDispute());
				//仲裁委员会名称
				grpInsurAppl.setArbitration(sgGrpInsurInfoVo.getParbOrgName());
			}
		}else if("P".equals(sgGrpInsurApplVo.getListType())){
			//个人汇交信息
			SgGrpPersonalInsurInfoVo sgGrpPersonalInsurInfoVo = sgGrpInsurApplVo.getSgGrpPersonalInsurInfoVo();
			if(null != sgGrpPersonalInsurInfoVo){			
				//个人争议处理方式
				grpInsurAppl.setArgueType(sgGrpPersonalInsurInfoVo.getpSettleDispute());
				//个人仲裁委员会名称
				grpInsurAppl.setArbitration(sgGrpPersonalInsurInfoVo.getJoinParbOrgName());
			}
		}
		//个人汇交人信息Vo转Bo
		grpInsurAppl.setPsnListHolderInfo(psnListHolderInfoVoToBo(sgGrpInsurApplVo));
		//团体客户信息Vo转Bo
		grpInsurAppl.setGrpHolderInfo(grpHolderInfoVoToBo(sgGrpInsurApplVo));
		//销售信息
		grpInsurAppl.setSalesInfoList(salesInfoVoToBo(sgGrpInsurApplVo));
		//交费相关
		grpInsurAppl.setPaymentInfo(paymentInfoVoToBo(sgGrpInsurApplVo));
		//投保要约
		grpInsurAppl.setApplState(applStateVoToBo(sgGrpInsurApplVo));
		//要约分组
		grpInsurAppl.setIpsnStateGrpList(ipsnStateGrpVoToBo(sgGrpInsurApplVo));
		//团体收费组
		grpInsurAppl.setIpsnPayGrpList(ipsnPayGrpVoToBo(sgGrpInsurApplVo));
		//特约
		grpInsurAppl.setConventions(conventionsVoToBo(sgGrpInsurApplVo));
		
		//组织架构树（团单特有）
		//grpInsurAppl.setOrgTreeList(orgTreeList);
		//建工险（团单特有）
		//grpInsurAppl.setConstructInsurInfo(constructInsurInfo);
		//健康险（团单特有）
		//grpInsurAppl.setHealthInsurInfo(healthInsurInfo);
		//基金险（团单特有）
		//grpInsurAppl.setFundInsurInfo(fundInsurInfo);
		
		//团体业务分类
		//grpInsurAppl.setGrpBusiType(grpBusiType);
		//契约业务类型
		//grpInsurAppl.setApplBusiType(applBusiType);
		//销售人员是否共同展业标识
		//grpInsurAppl.setSalesDevelopFlag(salesDevelopFlag);
		//契约形式
		//grpInsurAppl.setCntrType(cntrType);
		//保单送达方式
		//grpInsurAppl.setCntrSendType(cntrSendType);
		//外包录入标志
		//grpInsurAppl.setEntChannelFlag(entChannelFlag);
		//接入通道
		//grpInsurAppl.setAccessChannel(accessChannel);
		//合同组号
		//grpInsurAppl.setCgNo(cgNo);
		//汇交件号
		//grpInsurAppl.setSgNo(sgNo);
		//签单日期
		//grpInsurAppl.setSignDate(signDate);
		//生效日期
		//grpInsurAppl.setInForceDate(inForceDate);
		//续保次数
		//grpInsurAppl.setRenewTimes(renewTimes);
		//合同预计满期日期
		//grpInsurAppl.setCntrExpiryDate(cntrExpiryDate);
		//第一主险
		//grpInsurAppl.setFirPolCode(firPolCode);
		//指定退保公式
		//grpInsurAppl.setSrndAmntCmptType(srndAmntCmptType);
		//退保手续费比例
		//grpInsurAppl.setSrdFeeRate(srdFeeRate);
		//省级机构号
		//grpInsurAppl.setProvBranchNo(provBranchNo);
		//管理机构号
		//grpInsurAppl.setMgrBranchNo(mgrBranchNo);
		//归档机构号
		//grpInsurAppl.setArcBranchNo(arcBranchNo);
		
		return grpInsurAppl;
	}
	
	/**	
	 * 个人汇交信息Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public PsnListHolderInfo psnListHolderInfoVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		//个人汇交信息
		PsnListHolderInfo psnListHolderInfo = new PsnListHolderInfo();
		//个人汇交信息
		SgGrpPersonalInsurInfoVo sgGrpPersonalInsurInfoVo = sgGrpInsurApplVo.getSgGrpPersonalInsurInfoVo();
		if(null != sgGrpPersonalInsurInfoVo){	
			//汇交人姓名
			psnListHolderInfo.setSgName(sgGrpPersonalInsurInfoVo.getJoinName());
			//汇交人性别
			psnListHolderInfo.setSgSex(sgGrpPersonalInsurInfoVo.getJoinSex());
			//汇交人出生日期
			psnListHolderInfo.setSgBirthDate(sgGrpPersonalInsurInfoVo.getJoinBirthDate());
			//汇交人证件类型
			psnListHolderInfo.setSgIdType(sgGrpPersonalInsurInfoVo.getJoinIdType());
			//汇交人证件号码
			psnListHolderInfo.setSgIdNo(sgGrpPersonalInsurInfoVo.getJoinIdNo());
			//汇交人移动电话
			psnListHolderInfo.setSgMobile(sgGrpPersonalInsurInfoVo.getJoinMobile());
			//汇交人邮箱
			psnListHolderInfo.setSgEmail(sgGrpPersonalInsurInfoVo.getJoinEmail());
			//汇交人固定电话
			psnListHolderInfo.setSgTelephone(sgGrpPersonalInsurInfoVo.getJoinTel());
			//地址
			Address address = new Address();
			//省、自治州
			address.setProvince(sgGrpPersonalInsurInfoVo.getProvince());
			//市、州
			address.setCity(sgGrpPersonalInsurInfoVo.getCity());
			//区、县
			address.setCounty(sgGrpPersonalInsurInfoVo.getCounty());
			//镇、乡
			address.setTown(sgGrpPersonalInsurInfoVo.getTown());
			//村、社区
			address.setVillage(sgGrpPersonalInsurInfoVo.getVillage());
			//地址明细
			address.setHomeAddress(sgGrpPersonalInsurInfoVo.getJoinHome());
			//邮政编码
			address.setPostCode(sgGrpPersonalInsurInfoVo.getPostCode());
			//地址
			psnListHolderInfo.setAddress(address);
			//传真
			psnListHolderInfo.setFax(sgGrpPersonalInsurInfoVo.getJoinFaxNo());
		}
		//汇交人CMDS客户号
		//psnListHolderInfo.setSgPartyId(sgPartyId);
		//汇交人客户号
		//psnListHolderInfo.setSgCustNo(sgCustNo);
		return psnListHolderInfo;
 	}
	
	/**	
	 * 团体客户信息Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public GrpHolderInfo grpHolderInfoVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		//团体客户信息
		GrpHolderInfo grpHolderInfo = new GrpHolderInfo();
		//团体汇交信息
		SgGrpInsurInfoVo sgGrpInsurInfoVo = sgGrpInsurApplVo.getSgGrpInsurInfoVo();
		if(null != sgGrpInsurInfoVo){			
			//单位名称
			grpHolderInfo.setGrpName(sgGrpInsurInfoVo.getCompanyName());
			//团体客户证件类别
			grpHolderInfo.setGrpIdType(sgGrpInsurInfoVo.getIdType());
			//团体客户证件号码
			grpHolderInfo.setGrpIdNo(sgGrpInsurInfoVo.getIdNo());
			//企业注册地国籍
			grpHolderInfo.setGrpCountryCode(sgGrpInsurInfoVo.getCompanyRegist());
			//外管局部门类型
			grpHolderInfo.setGrpPsnDeptType(sgGrpInsurInfoVo.getDepartmentType());
			//行业类别
			grpHolderInfo.setOccClassCode(sgGrpInsurInfoVo.getOccClassCode());
			//传真
			grpHolderInfo.setFax(sgGrpInsurInfoVo.getFaxNo());
			//投保人数
			grpHolderInfo.setIpsnNum(sgGrpInsurInfoVo.getGInsuredTotalNum());
			//联系人姓名
			grpHolderInfo.setContactName(sgGrpInsurInfoVo.getContactName());
			//联系人证件类别
			grpHolderInfo.setContactIdType(sgGrpInsurInfoVo.getContactIdType());
			//联系人证件号码
			grpHolderInfo.setContactIdNo(sgGrpInsurInfoVo.getContactIdNo());
			//联系人移动电话
			grpHolderInfo.setContactMobile(sgGrpInsurInfoVo.getContactMobile());
			//联系人固定电话
			grpHolderInfo.setContactTelephone(sgGrpInsurInfoVo.getContactTel());
			//联系人电子邮件
			grpHolderInfo.setContactEmail(sgGrpInsurInfoVo.getContactEmail());
			//地址
			Address address = new Address();
			//省、自治州
			address.setProvince(sgGrpInsurInfoVo.getProvinceCode());
			//市、州
			address.setCity(sgGrpInsurInfoVo.getCityCode());
			//区、县
			address.setCounty(sgGrpInsurInfoVo.getCountyCode());
			//镇、乡
			address.setTown(sgGrpInsurInfoVo.getTownCode());
			//村、社区
			address.setVillage(sgGrpInsurInfoVo.getVillageCode());
			//地址明细
			address.setHomeAddress(sgGrpInsurInfoVo.getHome());
			//邮政编码
			address.setPostCode(sgGrpInsurInfoVo.getZipCode());
			//地址
			grpHolderInfo.setAddress(address);
		}
		//增值税信息
		SgGrpVatInfoVo sgGrpVatInfoVo = sgGrpInsurApplVo.getSgGrpVatInfoVo();
		if(null != sgGrpVatInfoVo){			
			//纳税人识别号
			grpHolderInfo.setTaxpayerId(sgGrpVatInfoVo.getTaxIdNo());
		}
		//投保单位CMDS客户号
		//grpHolderInfo.setPartyId(partyId);
		//团体法人客户号
		//grpHolderInfo.setGrpCustNo(grpCustNo);
		//曾用名
		//grpHolderInfo.setFormerGrpName(formerGrpName);
		//单位性质（经济分类）
		//grpHolderInfo.setNatureCode(natureCode);
		//单位性质(法律分类)
		//grpHolderInfo.setLegalCode(legalCode);
		//法人代表
		//grpHolderInfo.setCorpRep(corpRep);
		//员工总数
		//grpHolderInfo.setNumOfEnterprise(numOfEnterprise);
		//在职人数
		//grpHolderInfo.setOnjobStaffNum(onjobStaffNum);
		return grpHolderInfo;
	}
	
	/**	
	 * 销售相关信息Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<SalesInfo> salesInfoVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		List<SalesInfo> list = new ArrayList<SalesInfo>();
		//投保单信息
		SgGrpApplInfoVo sgGrpApplInfoVo = sgGrpInsurApplVo.getSgGrpApplInfoVo();
		if(sgGrpApplInfoVo.getGrpSalesListFormVos()!=null){
		    for (int i = 0; i < sgGrpApplInfoVo.getGrpSalesListFormVos().size(); i++) {
		        // 销售相关
		        SalesInfo salesInfo = new SalesInfo();
		        // 销售机构代码
		        salesInfo.setSalesBranchNo(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesBranchNo());
		        // 销售渠道
		        salesInfo.setSalesChannel(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesChannel());
		        // 销售机构
		        salesInfo.setSalesBranchNo(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesBranchNo());
		        if ("OA".equals(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getSalesChannel())) {
		            // 代理网点号
		            salesInfo.setSalesDeptNo(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getWorksiteNo());
		            // 网点名称
		            salesInfo.setDeptName(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getWorksiteName());
		            //代理员工号
		            if(StringUtils.isNotEmpty(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyNo())){
		            	salesInfo.setCommnrPsnNo(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyNo());
		            }
		            //代理员工姓名
		            if(StringUtils.isNotEmpty(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyName())){
		            	salesInfo.setCommnrPsnName(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getAgencyName());
		            }
		        } else {
		            // 销售人员代码
		            salesInfo.setSalesNo(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getSaleCode());
		            // 销售人员姓名
		            salesInfo.setSalesName(sgGrpApplInfoVo.getGrpSalesListFormVos().get(i).getSaleName());
		        }
		        list.add(salesInfo);
		    }
		}
		//销售机构名称
		//salesInfo.setSalesBranchName(salesBranchName);
		//销售机构地址
		//salesInfo.setSalesBranchAddr(salesBranchAddr);
		//销售机构邮政编码
		//salesInfo.setSalesBranchPostCode(salesBranchPostCode);
		//网点号
		//salesInfo.setDeptNo(deptNo);
		//网点名称
		//salesInfo.setDeptName(deptName);
		//网点代理员工号
		//salesInfo.setCommnrPsnNo(commnrPsnNo);
		//网点代理员姓名
		//salesInfo.setCommnrPsnName(commnrPsnName);
		//成本中心
		//salesInfo.setCenterCode(centerCode);
		//共同展业主副标记
		//salesInfo.setDevelMainFlag(develMainFlag);
		//展业比例
		//salesInfo.setDevelopRate(developRate);
		return list;
	}
	
	/**	
	 * 交费相关信息Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public PaymentInfo paymentInfoVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		//交费相关
		PaymentInfo paymentInfo = new PaymentInfo();
		//交费信息Vo
		SgGrpPayInfoVo sgGrpPayInfoVo = sgGrpInsurApplVo.getSgGrpPayInfoVo();
		if(null != sgGrpPayInfoVo){
			//团单保费来源
			paymentInfo.setPremSource(sgGrpPayInfoVo.getPremFrom());
			//多团体个人共同付款
			paymentInfo.setMultiPartyScale(sgGrpPayInfoVo.getMultiPartyScale());
			//团体个人共同付款
			paymentInfo.setMultiPartyMoney(sgGrpPayInfoVo.getMultiPartyMoney());
			//交费方式
			paymentInfo.setMoneyinItrvl(sgGrpPayInfoVo.getMoneyItrvl());
			//交费形式
			paymentInfo.setMoneyinType(sgGrpPayInfoVo.getMoneyinType());
			//开户银行
			paymentInfo.setBankCode(sgGrpPayInfoVo.getBankCode());
			//开户名称
			paymentInfo.setBankAccName(sgGrpPayInfoVo.getBankBranchName());
			//银行账号
			paymentInfo.setBankAccNo(sgGrpPayInfoVo.getBankaccNo());
			//结算方式
			paymentInfo.setStlType(sgGrpPayInfoVo.getPaymentType());
			//结算限额
			paymentInfo.setStlAmnt(sgGrpPayInfoVo.getPaymentLimit());
			List<Date> stlDate = new ArrayList<>();
			stlDate.add(sgGrpPayInfoVo.getPaymentDate());
			//结算日期
			paymentInfo.setStlDate(stlDate);
			//是否需要续期扣款
			paymentInfo.setIsRenew(sgGrpPayInfoVo.getRenewalChargeFlag());
			//续期扣款截至日
			paymentInfo.setRenewExpDate(sgGrpPayInfoVo.getChargeDeadline());
			//是否多期暂交
			paymentInfo.setIsMultiPay(sgGrpPayInfoVo.getMultiRevFlag());
			//多期暂交年数
			paymentInfo.setMutipayTimes(sgGrpPayInfoVo.getMultiTempYear());
			
		}
		//要约信息
		SgGrpProposalInfoVo sgGrpProposalInfoVo = sgGrpInsurApplVo.getSgGrpProposalInfoVo();
		if(null != sgGrpProposalInfoVo){			
			//首期扣款截止日期
			paymentInfo.setForeExpDate(sgGrpProposalInfoVo.getFirstChargeDate());
		}
		//交费期
		//paymentInfo.setMoneyinDur(moneyinDur);
		//交费期单位
		//paymentInfo.setMoneyinDurUnit(moneyinDurUnit);
		//结算比例
		//paymentInfo.setStlRate(stlRate);
		//币种
		//paymentInfo.setCurrencyCode(currencyCode);
		return paymentInfo;
	}
	
	/**	
	 * 投保要约信息Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public ApplState applStateVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		//投保要约
		ApplState applState = new ApplState();
		//险种集合
		List<Policy> policyList = new ArrayList<Policy>();
		//要约信息
		SgGrpProposalInfoVo sgGrpProposalInfoVo = sgGrpInsurApplVo.getSgGrpProposalInfoVo();
		if(null != sgGrpProposalInfoVo){
			//投保人数
			applState.setIpsnNum(sgGrpProposalInfoVo.getInsuredTotalNum());
			//总保费
			applState.setSumPremium(sgGrpProposalInfoVo.getSumPrem());
			//保险期类型
			applState.setInsurDurUnit(sgGrpProposalInfoVo.getInsurDurUnit());
			//生效日类型
			applState.setInforceDateType(sgGrpProposalInfoVo.getEffectType());
			//指定生效日
			applState.setDesignForceDate(sgGrpProposalInfoVo.getSpeEffectDate());
			//是否频次生效
			applState.setIsFreForce(sgGrpProposalInfoVo.getFrequencyEffectFlag());
			//生效频率
			applState.setForceFre(sgGrpProposalInfoVo.getEffectFreq());
			//新增险种信息
			List<SgGrpAddinsuranceVo> sgGrpAddinsuranceVoList = sgGrpInsurApplVo.getAddinsuranceVos();
			if(null != sgGrpAddinsuranceVoList && sgGrpAddinsuranceVoList.size() > 0){
				for(int i = 0; i < sgGrpAddinsuranceVoList.size(); i++){
					//子险种集合
					List<SubPolicy> subPolicyList = new ArrayList<SubPolicy>();
					//险种
					Policy policy = new Policy();
					//险种
					policy.setPolCode(sgGrpAddinsuranceVoList.get(i).getBusiPrdCode());
					//险种名称
					policy.setPolNameChn(sgGrpAddinsuranceVoList.get(i).getBusiPrdName());
					//保险期间
					policy.setInsurDur(sgGrpAddinsuranceVoList.get(i).getValidateDate());
					//保险期类型
					policy.setInsurDurUnit(sgGrpAddinsuranceVoList.get(i).getInsurDurUnit());
					//险种保额
					policy.setFaceAmnt(sgGrpAddinsuranceVoList.get(i).getAmount());
					//实际保费
					policy.setPremium(sgGrpAddinsuranceVoList.get(i).getPremium());
					//险种投保人数
					policy.setPolIpsnNum(sgGrpAddinsuranceVoList.get(i).getInsuredNum());
					//专项业务标识
					policy.setSpeciBusinessLogo(sgGrpAddinsuranceVoList.get(i).getHealthInsurFlag());
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
					List<ResponseVo> responseVoList = sgGrpAddinsuranceVoList.get(i).getResponseVos();
					if(null != responseVoList && responseVoList.size() > 0){
						for(int j = 0; j < responseVoList.size(); j++){
							//子险种
							SubPolicy subPolicy = new SubPolicy();
							//子险种
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
					policy.setSubPolicyList(subPolicyList);
					policyList.add(policy);
				}
			}
			//险种
			applState.setPolicyList(policyList);
		}
		//总保额
		//applState.setSumFaceAmnt(sumFaceAmnt);
		//保险期间
		//applState.setInsurDur(insurDur);
		//是否预打印
		//applState.setIsPrePrint(isPrePrint);
		return applState;
	}
	
	/**	
	 * 投保要约信息Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<IpsnStateGrp> ipsnStateGrpVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		List<IpsnStateGrp> list = new ArrayList<IpsnStateGrp>();
		//被保人分组信息集合
		List<InsuredGroupModalVo> insuredGroupModalVoList = sgGrpInsurApplVo.getInsuredGroupModalVos();
		if(null != insuredGroupModalVoList && insuredGroupModalVoList.size() > 0){
			for(int i = 0; i < insuredGroupModalVoList.size(); i++){
				//险种集合
				List<GrpPolicy> grpPolicyList = new ArrayList<GrpPolicy>();
				//要约分组
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
				//被保人分组类型
				SgGrpPrintInfoVo sgGrpPrintInfoVo = sgGrpInsurApplVo.getSgGrpPrintInfoVo();
				if(null != sgGrpPrintInfoVo){
					ipsnStateGrp.setIpsnGrpType(sgGrpPrintInfoVo.getGroupType());
				}
				//男女比例
				ipsnStateGrp.setGenderRadio(insuredGroupModalVoList.get(i).getGenderRadio());
				List<InsuranceInfoVo> insuranceInfoVoList = insuredGroupModalVoList.get(i).getInsuranceInfoVos();
				if(null != insuranceInfoVoList && insuranceInfoVoList.size() > 0){
					for(int j = 0; j < insuranceInfoVoList.size(); j++){						
						//险种信息
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
	 * 团体收费组Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public List<IpsnPayGrp> ipsnPayGrpVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		List<IpsnPayGrp> list = new ArrayList<IpsnPayGrp>();
		//收付费分组信息集合
		List<ChargePayGroupModalVo> chargePayGroupModalVoList = sgGrpInsurApplVo.getChargePayGroupModalVos();
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
	 * 特约Vo转Bo
	 * @param grpInsurAppl 团体出单基本信息
	 * @author jinmeina
	 * @return
	 */
	public Conventions conventionsVoToBo(SgGrpInsurApplVo sgGrpInsurApplVo) {
		if(null == sgGrpInsurApplVo){
			return null;
		}
		//特约
		Conventions conventions = new Conventions();
		//要约信息
		SgGrpProposalInfoVo sgGrpProposalInfoVo = sgGrpInsurApplVo.getSgGrpProposalInfoVo();
		if(null != sgGrpProposalInfoVo){			
			//险种责任特约
			conventions.setPolConv(sgGrpProposalInfoVo.getConvention());
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