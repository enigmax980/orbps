package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.pcms.bo.ComPolicy;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.ProcCommAgreementInfoService;
import com.newcore.orbps.service.pcms.api.SetComAgreeService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 共保协议落地服务实现类
 * @author JCC
 * 2016年12月14日 16:54:05
 */
@Service("procCommAgreementInfoService")
public class ProcCommAgreementInfoServiceImpl implements ProcCommAgreementInfoService{
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcCommAgreementInfoServiceImpl.class);
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	SetComAgreeService setComAgreeServiceClient;

	@Override
	public RetInfo commonAgreement(GrpInsurAppl grpInsurAppl) {
		return chooseCommonAgreement(grpInsurAppl);
	}
	
	/**
	 * 首席共保落地--获得共保数据
	 * */
	public RetInfo chooseCommonAgreement(GrpInsurAppl grpInsurAppl){
		RetInfo retInfo = new RetInfo();
		//共保协议号非空校验
		if(grpInsurAppl.getAgreementNo() != null){
			logger.info("------------>共保协议号"+grpInsurAppl.getAgreementNo());
			retInfo = commonAgreementForPCMS(grpInsurAppl);
		}else{//参数非空校验
			logger.info("投保单号["+grpInsurAppl.getApplNo()+"]无共保协议号！");
			retInfo.setErrMsg("投保单号["+grpInsurAppl.getApplNo()+"]无共保协议号！");
			retInfo.setRetCode("0");
			retInfo.setApplNo(grpInsurAppl.getApplNo());
		}
		return retInfo;
	}
	
	/**
	 * 首席共保落地--判断是否是首席共保数据-具体调用
	 * @param agreementNo 共保协议号
	 */
	public RetInfo commonAgreementForPCMS(GrpInsurAppl grpInsurAppl){
		RetInfo retInfo = new RetInfo();
		//根据保单的【共保协议号】查询共保基本信息【CommonAgreement】
		String agreementNo=grpInsurAppl.getAgreementNo();
		Map<String, Object> comMap = new HashMap<>();
		comMap.put("agreementNo",agreementNo);
		CommonAgreement commonAgreement = (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class, comMap);
		if(commonAgreement !=null){
			//消息头设置
			CxfHeader headerInfo2 = HeaderInfoUtils.buildDefaultHeaderInfo();
//			headerInfo2.setOrginSystem("ORBPS");					
//			headerInfo2.getRouteInfo().setBranchNo("120000");
			HeaderInfoHolder.setOutboundHeader(headerInfo2);
			com.newcore.orbps.models.pcms.bo.CommonAgreement commonAgreement2 =	getCommonAgreement(commonAgreement,grpInsurAppl);
			logger.info("投保单号【"+grpInsurAppl.getApplNo()+"】:开始调用参与共保协议落地服务！");
			retInfo  =	setComAgreeServiceClient.addCommonAgreement(commonAgreement2);
		}else{//参数非空校验
			logger.info("根据共保协议号【"+agreementNo+"】查询不到对应的共保基本信息【CommonAgreement】");
			retInfo.setErrMsg("根据共保协议号【"+agreementNo+"】查询不到对应的共保基本信息【CommonAgreement】");
			retInfo.setRetCode("0");
			retInfo.setApplNo("共保协议号"+agreementNo);
		}
		return retInfo;
	}
	
	/**
	 * 重新封装共保基本信息,与接口入参对应
	 * @param commonAgreement 共保基本信息
	 * @param grpInsurAppl 投保单号
	 * @return
	 */
	public com.newcore.orbps.models.pcms.bo.CommonAgreement getCommonAgreement(CommonAgreement commonAgreement,GrpInsurAppl grpInsurAppl){
		com.newcore.orbps.models.pcms.bo.CommonAgreement commonAgreement2 = new com.newcore.orbps.models.pcms.bo.CommonAgreement();
		commonAgreement2.setApplNo(grpInsurAppl.getApplNo());
		commonAgreement2.setAgreementName(commonAgreement.getAgreementName());
		commonAgreement2.setAgreementNo(commonAgreement.getAgreementNo());
		commonAgreement2.setApplDate(commonAgreement.getApplDate());
		commonAgreement2.setClerkNo(commonAgreement.getClerkNo());
		commonAgreement2.setAgreementStat(commonAgreement.getAgreementStat());
		List<com.newcore.orbps.models.pcms.bo.ComCompany> comCompanyList = new ArrayList<>();
		for (ComCompany comCompany2 : commonAgreement.getComCompanies()) {
			com.newcore.orbps.models.pcms.bo.ComCompany comCompanyPcms = new com.newcore.orbps.models.pcms.bo.ComCompany();
			comCompanyPcms.setBankAccName(comCompany2.getBankAccName());
			comCompanyPcms.setBankAccNo(comCompany2.getBankAccNo());
			comCompanyPcms.setBankCode(comCompany2.getBankCode());
			comCompanyPcms.setCoinsurAmntPct(comCompany2.getCoinsurAmntPct());
			comCompanyPcms.setCoinsurResponsePct(comCompany2.getCoinsurResponsePct());
			comCompanyPcms.setCoinsurType(comCompany2.getCoinsurType());
			comCompanyPcms.setCompanyCode(comCompany2.getCompanyCode());
			comCompanyPcms.setCompanyFlag(comCompany2.getCompanyFlag());
			comCompanyPcms.setCompanyName(comCompany2.getCompanyName());
			comCompanyList.add(comCompanyPcms);
		}
		commonAgreement2.setComCompanyList(comCompanyList);


		//		com.newcore.orbps.models.pcms.bo.PeriodComRec periodComRec = new com.newcore.orbps.models.pcms.bo.PeriodComRec();
		//		 PeriodComRec  periodComRec3 =commonAgreement.getPeriodComRec();
		//		
		//		periodComRec.setAmnt(periodComRec3.getAmnt());
		//		periodComRec.setClerkNo(periodComRec3.getClerkNo());
		//		periodComRec.setCntrCount(periodComRec3.getCntrCount());
		//		periodComRec.setHandoverEndDate(periodComRec3.getHandoverEndDate());
		//		periodComRec.setHandoverNum(commonAgreement.getPeriodComRec().getHandoverNum());
		//		periodComRec.setHandoverStartDate(commonAgreement.getPeriodComRec().getHandoverStartDate());
		//		periodComRec.setJoinCoinsurStat(commonAgreement.getPeriodComRec().getJoinCoinsurStat());
		//	//	periodComRec.setOclkBranchNo(commonAgreement.getPeriodComRec().getoclkBranchNo);
		//		
		//		periodComRec.setSalesBranchNo(commonAgreement.getPeriodComRec().getSalesBranchNo());
		//		periodComRec.setSalesChannel(commonAgreement.getPeriodComRec().getSalesChannel());
		//		periodComRec.setSalesNo(commonAgreement.getPeriodComRec().getSalesNo());
		//		commonAgreement2.setPeriodComRec(periodComRec);



		//commonAgreement2.setComPolList(commonAgreement.getcomPolList);
		commonAgreement2.setConvention(commonAgreement.getConvention());
		commonAgreement2.setDataSource(null);

		com.newcore.orbps.models.pcms.bo.ComGrpHolderInfo comGrpHolderInfo = new com.newcore.orbps.models.pcms.bo.ComGrpHolderInfo();
		com.newcore.orbps.models.pcms.bo.Address address = new com.newcore.orbps.models.pcms.bo.Address();
		GrpHolderInfo  grpHolderInfo  = commonAgreement.getComCustomer();
		address.setCity(grpHolderInfo.getAddress().getCity());
		address.setCounty(grpHolderInfo.getAddress().getCounty());
		address.setHomeAddress(grpHolderInfo.getAddress().getHomeAddress());
		address.setPostCode(grpHolderInfo.getAddress().getPostCode());
		address.setProvince(grpHolderInfo.getAddress().getProvince());
		address.setTown(grpHolderInfo.getAddress().getTown());
		address.setVillage(grpHolderInfo.getAddress().getVillage());

		comGrpHolderInfo.setAddress(address);
		comGrpHolderInfo.setContactEmail(grpHolderInfo.getContactEmail());
		comGrpHolderInfo.setContactMobile(grpHolderInfo.getContactMobile());
		comGrpHolderInfo.setContactName(grpHolderInfo.getContactName());
		comGrpHolderInfo.setContactTelephone(grpHolderInfo.getContactTelephone());
		comGrpHolderInfo.setCorpRep(grpHolderInfo.getCorpRep());
		comGrpHolderInfo.setFax(grpHolderInfo.getFax());
		comGrpHolderInfo.setGrpCustNo(grpHolderInfo.getGrpCustNo());
		comGrpHolderInfo.setGrpIdNo(grpHolderInfo.getGrpIdNo());
		comGrpHolderInfo.setGrpIdType(grpHolderInfo.getGrpIdType());

		comGrpHolderInfo.setGrpName(grpHolderInfo.getGrpName());
		comGrpHolderInfo.setNatureCode(grpHolderInfo.getNatureCode());
		comGrpHolderInfo.setOccClassCode(grpHolderInfo.getOccClassCode());

		commonAgreement2.setGrpHolderInfo(comGrpHolderInfo);

		commonAgreement2.setInForceDate(commonAgreement.getInForceDate());
		commonAgreement2.setIsScan(commonAgreement.getIsScan());
		commonAgreement2.setMgrBranchNo(commonAgreement.getMgrBranchNo());
		commonAgreement2.setPclkBranchNo(commonAgreement.getPclkBranchNo());

		//		List<com.newcore.orbps.models.pcms.bo.PeriodComInsur> periodComInsurList = new ArrayList<>();
		//		for (PeriodComInsur periodComInsur :commonAgreement.getPeriodComInsurs()) {
		//			com.newcore.orbps.models.pcms.bo.PeriodComInsur periodComInsurPcms = 
		//					new com.newcore.orbps.models.pcms.bo.PeriodComInsur();
		//			periodComInsurPcms.setCntrNo(periodComInsur.getCntrNo());
		//			periodComInsurPcms.setCustName(periodComInsur.getCustName());
		//			periodComInsurPcms.setNum(periodComInsur.getHandoverNum());
		//			periodComInsurPcms.setRemark(periodComInsur.getRemark());
		//			periodComInsurList.add(periodComInsurPcms);
		//		}
		//		
		//		commonAgreement2.setPeriodComInsurList(periodComInsurList);
		List<ComPolicy> comPolList = new ArrayList<>();
		List<Policy> ListPolicy = commonAgreement.getPolicies();
		if(ListPolicy.size()>0){
			for (Policy policy : ListPolicy) {
				ComPolicy comPolicy =new ComPolicy();
				comPolicy.setFaceAmnt(policy.getFaceAmnt());
				comPolicy.setMrCode(policy.getMrCode());
				comPolicy.setPolCode(policy.getPolCode());
				comPolicy.setPremium(policy.getPremium());
				comPolList.add(comPolicy);
			}
		}
		commonAgreement2.setComPolList(comPolList);
		commonAgreement2.setTermDate(commonAgreement.getTermDate());
		commonAgreement2.setVclkBranchNo(commonAgreement.getVclkBranchNo());
		commonAgreement2.setVclkNo(commonAgreement.getVclkNo());
		return commonAgreement2;
	}

}
