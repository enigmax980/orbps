package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.halo.core.header.HeaderInfoHolder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.newcore.orbps.dao.api.UntilPolicyDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.BusiDataInBo;
import com.newcore.orbps.models.pcms.bo.ComEarnestaccQryArgInfo;
import com.newcore.orbps.models.pcms.bo.ComEarnestaccQryRetBo;
import com.newcore.orbps.models.pcms.bo.JoinComMioRecBo;
import com.newcore.orbps.models.pcms.bo.JoinComOutBo;
import com.newcore.orbps.models.pcms.bo.MioLogInBo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryInfo;
import com.newcore.orbps.models.pcms.bo.SalesmanQueryReturnBo;
import com.newcore.orbps.models.pcms.bo.VATConfigQryInfo;
import com.newcore.orbps.models.pcms.bo.VATConfigQryObj;
import com.newcore.orbps.models.pcms.bo.VATConfigQryRetInfo;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.PeriodComRec;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.PeriodComInsur;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.CommonAgreementService;
import com.newcore.orbps.service.business.ValidatorUtils;
import com.newcore.orbps.service.ipms.api.PolicyQueryService;
import com.newcore.orbps.service.ipms.api.SubPolicyService;
import com.newcore.orbps.service.pcms.api.QryJoinComFAAccService;
import com.newcore.orbps.service.pcms.api.QryVATCfgInfoService;
import com.newcore.orbps.service.pcms.api.SalesmanInfoService;
import com.newcore.orbps.service.pcms.api.SetComAgreeService;
import com.newcore.orbps.service.pcms.api.SetJoinComMioRecService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;



@Service("comAgrService")
public class CommonAgreementServiceImpl implements CommonAgreementService{

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	UntilPolicyDao untilPolicy;

	@Autowired
	PolicyQueryService resulpolicyQueryService;

	@Autowired
	SubPolicyService resulsubPolicyService;

	@Autowired
	SalesmanInfoService resulsalesmanInfoService;

	@Autowired
	QryJoinComFAAccService qryJoinComFAAccServiceClient;
	@Autowired	
	SetJoinComMioRecService setJoinComMioRecServiceClient;
	@Autowired
	SetComAgreeService setComAgreeServiceClient;
	@Autowired
	QryVATCfgInfoService qryVATCfgInfoServiceClient;


	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(CommonAgreementServiceImpl.class);

	@Override
	public RetInfo comAgrSerSubmit(CommonAgreement commonAgreement) {
		RetInfo  retInfo = new RetInfo();
		if(null == commonAgreement){
			retInfo.setErrMsg("入参commonAgreement为空！");
			retInfo.setRetCode("0");
			return retInfo;
		}

		retInfo = argCheak(commonAgreement);		
		if("0".equals(retInfo.getRetCode())){
			return retInfo;
		}
		/*若果是 A-共保新增*/
		if("A".equals(commonAgreement.getTransFlag())){
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);

			RetInfo retInfoAgreementNo = untilPolicy.creatAgreementNo(commonAgreement.getMgrBranchNo());
			if(null == retInfoAgreementNo ){
				retInfo.setErrMsg("生成共保号出错！");
				retInfo.setRetCode("0");
				return retInfo;
			}else if ("0".equals(retInfoAgreementNo.getRetCode())) {
				return retInfoAgreementNo;
			}
			commonAgreement.setAgreementNo(retInfoAgreementNo.getApplNo());

			/*共保公司基本信息组comCompanyList中的公司代码赋值*/
			int seqNo = 2;//表示公司的序号
			for(ComCompany comCompany :commonAgreement.getComCompanies()){
				if("M".equals(comCompany.getCoinsurType().trim())){
					comCompany.setCompanyCode(retInfoAgreementNo.getApplNo().trim().substring(0,17) + String.format("%02d", 1));
				}else {
					comCompany.setCompanyCode(retInfoAgreementNo.getApplNo().trim().substring(0,17) + String.format("%02d", seqNo));
					seqNo++;
				}			
			}
			/*对险种主附险性质赋值*/
			commonAgreement.getPolicies().get(0).setMrCode("M");
			for(int i = 1;i<commonAgreement.getPolicies().size();i++){
				commonAgreement.getPolicies().get(i).setMrCode("R");
			}

			/*是否扫描*/
			if(StringUtils.isEmpty(commonAgreement.getIsScan())){
				commonAgreement.setIsScan("N");
			}

			commonAgreement.setAgreementStat("W");//协议录入提交 						
			retInfo.setApplNo(commonAgreement.getAgreementNo());
			mongoTemplate.insert(commonAgreement);					
		}

		/*若果是 M-共保修改 就先删除 已存在的共保信息*/
		if("M".equals(commonAgreement.getTransFlag())){

			CommonAgreement check = mongoTemplate.findOne(Query.query(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo())), CommonAgreement.class);
			if(null == check){
				retInfo.setErrMsg("共保协议表中无 该共保协议号["+commonAgreement.getAgreementNo() +"]信息");
				retInfo.setRetCode("0");				
				return retInfo;
			}
			if("W".equals(check.getAgreementStat()) || "B".equals(check.getAgreementStat()) || "C".equals(check.getAgreementStat())){
				mongoTemplate.remove(Query.query(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo())), CommonAgreement.class);
			}else {
				retInfo.setErrMsg("该共保协议["+commonAgreement.getAgreementNo() +"]状态为["+check.getAgreementStat()+"]审核成功,不能修改!");
				retInfo.setRetCode("0");				
				return retInfo;
			}

			/*共保公司基本信息组comCompanyList中的公司代码赋值*/
			int seqNo = 2;//表示公司的序号
			for(ComCompany comCompany :commonAgreement.getComCompanies()){
				if("M".equals(comCompany.getCoinsurType().trim())){
					comCompany.setCompanyCode(commonAgreement.getAgreementNo().substring(0,17) + String.format("%02d", 1));
				}else {
					comCompany.setCompanyCode(commonAgreement.getAgreementNo().substring(0,17) + String.format("%02d", seqNo));
					seqNo++;
				}			
			}
			/*对险种主附险性质赋值*/
			commonAgreement.getPolicies().get(0).setMrCode("M");
			for(int i = 1;i<commonAgreement.getPolicies().size();i++){
				commonAgreement.getPolicies().get(i).setMrCode("R");
			}
			/*是否扫描*/
			if(StringUtils.isEmpty(commonAgreement.getIsScan())){
				commonAgreement.setIsScan("N");
			}
			/*修改之后，transFlag重新设置 为A,用来区别是共保单子，还是参与共保的单子*/
			commonAgreement.setTransFlag("A");
			commonAgreement.setAgreementStat("W");
			mongoTemplate.insert(commonAgreement);
			retInfo.setApplNo(commonAgreement.getAgreementNo());
		}
		/*I-参与共保信息录入*/
		if("I".equals(commonAgreement.getTransFlag())){

			/*查询共保协议表,使用数据库中的数据，防止前台修改了原有的数据，造成数据不准确*/
			CommonAgreement queryCommonAgreement  = mongoTemplate.findOne(Query.query(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo())), CommonAgreement.class);			

			/*如果协议审核未通过，则不允许参与共保信息录入*/
			if(!"K".equals(queryCommonAgreement.getAgreementStat())){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该共保协议未审核通过,不允许进行参与共保业务!");
				return retInfo;
			}

			/*防止对同一批次单子多次提交，对已存数据就行校验*/
			PeriodComRec periodComRec = commonAgreement.getPeriodComRecs().get(0);
			if(null != queryCommonAgreement.getPeriodComRecs()){
				for(PeriodComRec periodComRecQuery:queryCommonAgreement.getPeriodComRecs()){
					if(contrastPeriodComRec(periodComRec,periodComRecQuery)){
						retInfo.setRetCode("0");
						retInfo.setErrMsg("该参与共保信息 已存在 或者 已存在相同的保单信息，请检查后再提交以免提交重复数据!");
						return retInfo;
					}
				}
			}

			/*调用PCMS接口，查询参与共保是否暂收费*/			
			ComEarnestaccQryArgInfo comEarnestaccQryArgInfo = new ComEarnestaccQryArgInfo();
			comEarnestaccQryArgInfo.setAgreementNo(queryCommonAgreement.getAgreementNo());
			comEarnestaccQryArgInfo.setMgrBranchNo(queryCommonAgreement.getMgrBranchNo());
			/*配合PCMS系统需要,增加险种list,可为空*/
			List<String> list = new ArrayList<>();		
			list.add(queryCommonAgreement.getPolicies().get(0).getPolCode());
			comEarnestaccQryArgInfo.setPolCode(list);
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			ComEarnestaccQryRetBo  comEarnestaccQryRetBo  = qryJoinComFAAccServiceClient.qryJoinComFAAccService(comEarnestaccQryArgInfo);
			if(null == comEarnestaccQryRetBo ){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("查询参与共保暂收费失败!");
				return retInfo;
			}
			if("0".equals(comEarnestaccQryRetBo.getRetCode())){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("PCMS参与共保暂收费查询返回:"+ comEarnestaccQryRetBo.getErrMsg());
				return retInfo;				
			}


			if(periodComRec.getAmnt() - Double.valueOf(comEarnestaccQryRetBo.getEarnestAcc().getBalance()) > 0.001){
				retInfo.setRetCode("0");
				retInfo.setErrMsg("暂缴费金额["+comEarnestaccQryRetBo.getEarnestAcc().getBalance()+"]不足,所需保费为["+periodComRec.getAmnt() +"]");
				return retInfo;
			}
			/**------查询参与共保是否暂收费--end------**/

			/*参与共保信息录入落地服务*/
			queryCommonAgreement.setTransFlag("I");
			headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			com.newcore.orbps.models.pcms.bo.RetInfo retInfo2 = setComAgreeServiceClient.addCommonAgreement(convertCommonAgreement(queryCommonAgreement,periodComRec));
			if("0".equals(retInfo2.getRetCode())){
				retInfo.setRetCode("0");
				retInfo.setErrMsg(retInfo2.getErrMsg());
				return retInfo;
			}
			/**------参与共保信息录入--end------**/

			/*调用PCMS接口,传送mio_log和busi_data数据*/			
			JoinComMioRecBo jcBo = new JoinComMioRecBo();
			/*组 实收付流水数据  财务接口数据*/
			retInfo = CreateMioLogAndBusiDate(queryCommonAgreement,periodComRec, jcBo);
			if("0".equals(retInfo.getRetCode())){
				return retInfo;
			}
			/*参与共保财务与财务队列落地*/
			headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			JoinComOutBo joinComOutBo = setJoinComMioRecServiceClient.setJoinComMioRecService(jcBo);
			if("0".equals(joinComOutBo.getRetCode())){
				retInfo.setRetCode(joinComOutBo.getRetCode());
				retInfo.setErrMsg("PCMS参与共保财务与财务队列落地返回:"+joinComOutBo.getErrMsg());
				return retInfo;
			}
			/**------参与共保财务与财务队列落地--end------**/

			/*更新 参与共保收费信息  参与承保保单信息信息*/
			periodComRec.setCreateDate(new Date());
			periodComRec.setJoinCoinsurStat("4");//参与共保信息落地成功
			Update update = new Update();
			update.set("transFlag", "I");
			update.push("periodComRecs", periodComRec);
			mongoTemplate.updateFirst(Query.query(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo())), update,CommonAgreement.class);					
		}
		retInfo.setErrMsg("交易成功");
		retInfo.setRetCode("1");
		return retInfo;
	}

	@Override
	public RetInfoObject<CommonAgreement>  comAgrSerQuery(CommonAgreement commonAgreement) {
		RetInfoObject<CommonAgreement> retInfoObject = new RetInfoObject<>();
		if(StringUtils.isEmpty(commonAgreement.getAgreementNo())){
			retInfoObject.setErrMsg("共保协议号为空！");
			retInfoObject.setRetCode("0");
			return retInfoObject;
		}
		List<CommonAgreement> commonAgreements = new ArrayList<>();

		CommonAgreement commonAgreementRet = mongoTemplate.findOne(Query.query(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo())), CommonAgreement.class);
		if(null == commonAgreementRet){
			retInfoObject.setRetCode("0");
			retInfoObject.setErrMsg("没有查询到符合条件数据！");
			return  retInfoObject;
		}
		commonAgreements.add(commonAgreementRet);
		retInfoObject.setRetCode("1");
		retInfoObject.setErrMsg("查询成功！");
		retInfoObject.setListObject(commonAgreements);
		return  retInfoObject;
	}

	@Override
	public RetInfo comAgrSerCheck(CommonAgreement commonAgreement) {
		RetInfo retInfo = new RetInfo();
		if(null == commonAgreement){
			retInfo.setErrMsg("入参commonAgreement为空！");
			retInfo.setRetCode("0");
			return retInfo;
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo()));
		/*查询共保协议表*/
		CommonAgreement queryCommonAgreement  = mongoTemplate.findOne(query, CommonAgreement.class);						
		if(null == queryCommonAgreement){
			retInfo.setErrMsg("该共保协议不存在,请检查协议号是否正确!");
			retInfo.setRetCode("0");
			return retInfo;			
		}

		if("K".equals(queryCommonAgreement.getAgreementStat())){
			retInfo.setErrMsg("该共保协议已审核通过,请勿重复提交");
			retInfo.setRetCode("0");
			return retInfo;
		}	
		/*判断是否是参与共保的单子*/
		int flag = 0;
		List<ComCompany> comCompanies = queryCommonAgreement.getComCompanies();
		comCompanies.removeAll(Collections.singleton(null));		
		for(ComCompany comCompany : comCompanies){
			if("M".equals(comCompany.getCoinsurType()) && "N".equals(comCompany.getCompanyFlag())){
				/*共保协议落地服务*/
				flag = 1;
				break;
			}
		}
		/*共保协议审核通过 且 是参与共保的协议 才能 在审核的时候 落地*/
		if("K".equals(commonAgreement.getAgreementStat()) && flag == 1){
			queryCommonAgreement.setAgreementStat("K");
			com.newcore.orbps.models.pcms.bo.RetInfo retInfo2 = setComAgreeServiceClient.addCommonAgreement(convertCommonAgreement(queryCommonAgreement,null));				
			if(null == retInfo2  || "0".equals(retInfo2.getRetCode())){
				retInfo.setApplNo(null == retInfo2 ? "":retInfo2.getApplNo());
				retInfo.setRetCode( null == retInfo2 ? "0":retInfo2.getRetCode());
				retInfo.setErrMsg( null == retInfo2 ? "参与共保协议落地失败!":retInfo2.getErrMsg());
				return retInfo;
			}
		}

		/*将审核结果存入库中*/
		Update update = new Update();
		update.set("remark",commonAgreement.getRemark());
		update.set("agreementStat", commonAgreement.getAgreementStat());
		update.set("vclkBranchNo", commonAgreement.getVclkBranchNo());
		update.set("vclkNo", commonAgreement.getVclkNo());
		update.set("createDate", new Date());
		mongoTemplate.updateFirst(query, update, CommonAgreement.class);	
		retInfo.setErrMsg("交易成功");
		retInfo.setRetCode("1");
		return retInfo;
	}



	/*校验入参函数*/
	private RetInfo argCheak(CommonAgreement commonAgreement) {

		String transFlag ="AMI";//A--共保新增,M-共保修改，I--参与共保
		RetInfo retInfo = new RetInfo();
		StringBuilder errMsg = new StringBuilder();
		retInfo.setRetCode("1");
		if(StringUtils.isEmpty(commonAgreement.getTransFlag())){
			errMsg.append("交易标志(transFlag)不能为空|");
			retInfo.setRetCode("0");
		}else if(-1 == transFlag.indexOf(commonAgreement.getTransFlag())){
			errMsg.append("交易标志(transFlag) 必须为A--共保新增,M-共保修改，I--参与共保,入参为 ");
			errMsg.append(commonAgreement.getTransFlag());
			errMsg.append("|");
			retInfo.setRetCode("0");
		}

		if(("M".equals(commonAgreement.getTransFlag()) || "I".equals(commonAgreement.getTransFlag()) ) && StringUtils.isEmpty(commonAgreement.getAgreementNo())){
			errMsg.append("协议号(agreementNo)不能为空|");
			retInfo.setRetCode("0");
		}
		/*对共保协议进行校验*/
		if("M".equals(commonAgreement.getTransFlag()) || "A".equals(commonAgreement.getTransFlag())){
			if(null == commonAgreement.getSignDate()){
				errMsg.append("协议签署日期(signDate)不能为空|");
				retInfo.setRetCode("0");
			}
			if(null == commonAgreement.getApplDate()){
				errMsg.append("协议申请日期(applDate)不能为空|");
				retInfo.setRetCode("0");
			}		
			if(null == commonAgreement.getInForceDate()){
				errMsg.append("协议生效日期(inforceDate)不能为空|");
				retInfo.setRetCode("0");
			}		
			if(null == commonAgreement.getTermDate()){
				errMsg.append("协议终止日期(termDate)不能为空|");
				retInfo.setRetCode("0");
			}		
			if(StringUtils.isEmpty(commonAgreement.getMgrBranchNo())){
				errMsg.append("管理机构(mgrBranchNo)不能为空|");
				retInfo.setRetCode("0");
			}else if(commonAgreement.getMgrBranchNo().trim().length() != 6){
				errMsg.append("管理机构长度应该为 6 (mgrBranchNo:[");
				errMsg.append(commonAgreement.getMgrBranchNo());
				errMsg.append("]),所传值长度不符合要求,|");
				retInfo.setRetCode("0");		
			}
			if(StringUtils.isEmpty(commonAgreement.getPclkBranchNo())){
				errMsg.append("操作员机构(pclkBranchNo)不能为空|");
				retInfo.setRetCode("0");
			}			
			if(StringUtils.isEmpty(commonAgreement.getClerkNo())){
				errMsg.append("操作员工号(clerkNo)不能为空|");
				retInfo.setRetCode("0");
			}			


			/*共保公司基本信息组comCompanyList校验*/
			RetInfo ret = checkComCompanyList(commonAgreement.getComCompanies());
			if("0".equals(ret.getRetCode())){
				errMsg.append(ret.getErrMsg());
				retInfo.setRetCode("0");
			}

			/*共保险种信息comPolList校验*/
			ret = checkComPolList(commonAgreement.getPolicies());		
			if("0".equals(ret.getRetCode())){
				errMsg.append(ret.getErrMsg());
				retInfo.setRetCode("0");
			}				

			/*共保协议客户基本信息comCustomer校验*/
			ret = checkComCustomer(commonAgreement.getComCustomer());
			if("0".equals(ret.getRetCode())){
				errMsg.append(ret.getErrMsg());
				retInfo.setRetCode("0");
			}

		}

		/*对参与共保信息校验*/
		if("I".equals(commonAgreement.getTransFlag()) && !StringUtils.isEmpty(commonAgreement.getAgreementNo())){

			CommonAgreement commonAgreementCheck = mongoTemplate.findOne(Query.query(Criteria.where("agreementNo").is(commonAgreement.getAgreementNo())),CommonAgreement.class);
			if(null == commonAgreementCheck){
				errMsg.append("共保协议表中不存在 该共保协议号["+commonAgreement.getAgreementNo()+"]信息|");
				retInfo.setRetCode("0");
				retInfo.setErrMsg(errMsg.toString());
				return retInfo;	
			}
			/*校验保单能否参与共保,只有首席承保公司不为本公司时,才能进行参与共保录入*/
			for(ComCompany comCompany : commonAgreementCheck.getComCompanies()){
				if("M".equals(comCompany.getCoinsurType()) && "Y".equals(comCompany.getCompanyFlag())){
					errMsg.append("该共保协议是首席共保协议,不能进行参与共保信息录入|");
					retInfo.setRetCode("0");
					break;
				}
			}
			commonAgreementCheck.setPeriodComRecs(commonAgreement.getPeriodComRecs());
			RetInfo ret = checkPeriodComRec(commonAgreementCheck);
			if("0".equals(ret.getRetCode())){
				errMsg.append(ret.getErrMsg());
				retInfo.setRetCode("0");
			}
		}
		retInfo.setErrMsg(errMsg.toString());
		return retInfo;		
	}

	private RetInfo checkComCompanyList(List<ComCompany> comCompanies) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		String errMsg = "";
		if(null == comCompanies || comCompanies.isEmpty()){
			errMsg +="共保公司基本信息 (comCompanies)为空|";
			retInfo.setErrMsg(errMsg);
			retInfo.setRetCode("0");
			return retInfo;
		}

		int companyNum=0;//共保公司数量
		int companyFlagNum = 0;//是否本公司
		int coinsurTypeNum =0;//共保类型统计，只允许有一个主承保方
		double coinsurAmntPctSum = 0.00;//共保保费份额比例
		double coinsurResponsePctSum = 0.00;//共保责任份额比例
		comCompanies.removeAll(Collections.singleton(null));
		for(int i=0; i<comCompanies.size();i++){	
			if("M".equals(comCompanies.get(i).getCoinsurType())){
				coinsurTypeNum++;
			}
			if("Y".equals(comCompanies.get(i).getCompanyFlag())){
				companyFlagNum++;
			}
			if(StringUtils.isEmpty(comCompanies.get(i).getCompanyName())){
				errMsg+="共保公司基本信息组第"+ (i+1)+"条数据 公司名称(companName)不能为空|";
				retInfo.setRetCode("0");
			}
			if(StringUtils.isEmpty(comCompanies.get(i).getCoinsurType())){
				errMsg+="共保公司基本信息组第"+ (i+1)+"条数据 共保类型(coinsurType)不能为空|";
				retInfo.setRetCode("0");
			}
			if(StringUtils.isEmpty(comCompanies.get(i).getCompanyFlag())){
				errMsg+="共保公司基本信息组第"+ (i+1)+"条数据 是否本公司(companyFlag)不能为空|";
				retInfo.setRetCode("0");
			}
			if(comCompanies.get(i).getCoinsurAmntPct() <= 0){
				errMsg+="共保公司基本信息组第"+ (i+1)+"条数据 共保保费份额比例(amntPct)不能为零|";
				retInfo.setRetCode("0");
			}
			if(comCompanies.get(i).getCoinsurResponsePct() <= 0){
				errMsg+="共保公司基本信息组第"+ (i+1)+"条数据 共保责任份额比例(responsPct)不能为零|";
				retInfo.setRetCode("0");
			}
			coinsurAmntPctSum += comCompanies.get(i).getCoinsurAmntPct();
			coinsurResponsePctSum += comCompanies.get(i).getCoinsurResponsePct();
			companyNum++;
		}
		if(companyNum <=1){
			errMsg+="参与共保的公司数量 必须大于等于两个|";
			retInfo.setRetCode("0");
		}
		if(companyFlagNum != 1 ){
			errMsg+="共保公司 必须有且只有一个是本公司|";
			retInfo.setRetCode("0");
		}
		if(coinsurTypeNum != 1){
			errMsg+="必须有且只有一个主承保方,该保单不存在主承保方 或者 存在多个主承保方|";
			retInfo.setRetCode("0");
		}
		if(Math.abs(coinsurAmntPctSum - 100) > 0.001){
			errMsg+="共保保费份额比例总额 必须为  100%,该共保保费份额比例总额为["+ coinsurAmntPctSum  + "%]|";
			retInfo.setRetCode("0");			
		}
		if(Math.abs(coinsurResponsePctSum - 100) > 0.001){
			errMsg+="共保责任份额比例总额 必须为 100%,该共保责任份额比例总额为["+ coinsurResponsePctSum  + "%]|";
			retInfo.setRetCode("0");			
		}
		retInfo.setErrMsg(errMsg);
		return retInfo;	
	}


	private RetInfo checkComCustomer(GrpHolderInfo comCustomer) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		String errMsg = "";
		if(null == comCustomer){
			errMsg +="共保协议客户基本信息 (comCustomer)为空|";
			retInfo.setErrMsg(errMsg);
			retInfo.setRetCode("0");
			return retInfo;
		}	
		if(StringUtils.isEmpty(comCustomer.getGrpName())){
			errMsg+="单位名称 (GrpName)不能为空|";
			retInfo.setRetCode("0");
		}			
		if ("I".equals(comCustomer.getGrpIdType())) {
			String ret = ValidatorUtils.validIdNo(comCustomer.getGrpIdNo());
			if(!"OK".equals(ret)){
				errMsg+=ret;
				errMsg+="|";
				retInfo.setRetCode("0");
			}
		}
		if(null == comCustomer.getAddress()){
			errMsg += "地址为空|";
			retInfo.setErrMsg(errMsg);
			retInfo.setRetCode("0");
			return retInfo;
		}
		if (StringUtils.isEmpty(comCustomer.getAddress().getProvince())) {
			errMsg += "客户地址中省/自治州为空|";
			retInfo.setRetCode("0");
		}
		if (StringUtils.isEmpty(comCustomer.getAddress().getCity())) {
			errMsg += "客户地址中地址中市/州为空|";
			retInfo.setRetCode("0");
		}

		if (StringUtils.isEmpty(comCustomer.getAddress().getHomeAddress())) {
			errMsg += "客户地址中地址明细为空|";
			retInfo.setRetCode("0");
		}
		if (StringUtils.isEmpty(comCustomer.getAddress().getCounty())) {
			errMsg += "客户地址中地区/县为空|";
			retInfo.setRetCode("0");
		}
		if (StringUtils.isEmpty(comCustomer.getAddress().getPostCode())) {
			errMsg += "客户地址中邮编为空|";
			retInfo.setRetCode("0");
		}
		/*增加邮编校验*/
		RetInfo retInfo2 =ValidatorUtils.checkPostCode(comCustomer.getAddress().getPostCode(), comCustomer.getAddress().getProvince());
		if(StringUtils.equals(retInfo2.getRetCode(), "0")){
			retInfo.setRetCode(retInfo2.getRetCode());
			errMsg += retInfo2.getErrMsg();
		}
		retInfo.setErrMsg(errMsg);
		return retInfo;		
	}

	private  RetInfo checkComPolList(List<Policy> policies) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		String errMsg = "";
		if(null == policies || policies.isEmpty()){
			errMsg +="共保险种信息 (policies)为空|";
			retInfo.setErrMsg(errMsg);
			retInfo.setRetCode("0");
			return retInfo;
		}
		policies.removeAll(Collections.singleton(null));
		for(int i =0;i < policies.size(); i++){
			if(StringUtils.isEmpty(policies.get(i).getPolCode())){
				errMsg+="共保险种信息第"+ (i+1)+"条数据 险种代码(polCode)不能为空|";
				retInfo.setRetCode("0");
			}
			if (!StringUtils.isEmpty(policies.get(i).getPolCode()) && policies.get(i).getPolCode().trim().length()== 3) {
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Map<String, Object> map = new HashMap<>();
				map.put("polCode", policies.get(i).getPolCode());			
				map = resulpolicyQueryService.excute(map);		
				if(null == map ){
					logger.error("共保险种信息第 "+ (i+1)+" 条数据 险种代码("+policies.get(i).getPolCode()+") 调用产品险种查询服务出错!");
					errMsg+="共保险种信息第 "+ (i+1)+" 条数据 险种代码("+policies.get(i).getPolCode()+") 调用产品险种查询服务出错|";
					retInfo.setRetCode("0");
				}else if("8888".equals(map.get("errorCode"))) {
					errMsg+="共保险种信息第 "+ (i+1)+" 条数据 险种代码("+policies.get(i).getPolCode()+")不存在|";
					retInfo.setRetCode("0");					
				}
			}
			if (!StringUtils.isEmpty(policies.get(i).getPolCode()) && policies.get(i).getPolCode().trim().length() > 3) {
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);			
				Map<String, Object> map = new HashMap<>();
				map.put("polCode", policies.get(i).getPolCode());
				map = resulsubPolicyService.excute(map);
				if(null == map ){
					logger.error("共保险种信息第 "+ (i+1)+" 条数据 险种代码("+policies.get(i).getPolCode()+") 调用产品险种查询服务出错!");
					errMsg+="共保险种信息第 "+ (i+1)+" 条数据 险种代码("+policies.get(i).getPolCode()+") 调用产品险种查询服务出错|";
					retInfo.setRetCode("0");
				}else if("8888".equals(map.get("errorCode"))) {
					errMsg+="共保险种信息第 "+ (i+1)+" 条数据 险种代码("+policies.get(i).getPolCode()+")不存在|";
					retInfo.setRetCode("0");
				}
			}

			if(policies.get(i).getFaceAmnt() <= 0.00){
				errMsg+="共保险种信息第"+ (i+1)+"条数据 保额(faceAmnt)不能小于等于零|";
				retInfo.setRetCode("0");
			}
			if(policies.get(i).getPremium() <= 0.00){
				errMsg+="共保险种信息第"+ (i+1)+"条数据 保费(premium)不能小于等于零|";
				retInfo.setRetCode("0");
			}
		}
		retInfo.setErrMsg(errMsg);
		return retInfo;	
	}

	private RetInfo checkPeriodComInsurList(List<PeriodComInsur> periodComInsurs) {
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		StringBuilder errMsg = new StringBuilder();
		if(null == periodComInsurs || periodComInsurs.isEmpty()){
			errMsg.append("参与承保保单信息 (periodComInsurList)为空|");
			retInfo.setErrMsg(errMsg.toString());
			retInfo.setRetCode("0");
			return retInfo;
		}
		periodComInsurs.removeAll(Collections.singleton(null));
		for(int i =0;i < periodComInsurs.size();i++){
			if(StringUtils.isEmpty(periodComInsurs.get(i).getCntrNo())){
				errMsg.append("参与承保保单信息第"+ (i+1)+"条数据 保单号(cntrNo)不能为空|");
				retInfo.setRetCode("0");
			}
			if(StringUtils.isEmpty(periodComInsurs.get(i).getCustName())){
				errMsg.append("参与承保保单信息第"+ (i+1)+"条数据 单位名称(custName)不能为空|");
				retInfo.setRetCode("0");
			}
			if(periodComInsurs.get(i).getHandoverNum() <= 0){
				errMsg.append("参与承保保单信息第"+ (i+1)+"条数据 单件保单人数(handoverNum)不能小于等于零|");
				retInfo.setRetCode("0");
			}
		}
		retInfo.setErrMsg(errMsg.toString());		
		return retInfo;
	}

	private  RetInfo checkPeriodComRec(CommonAgreement commonAgreement){

		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode("1");
		StringBuilder errMsg = new StringBuilder();
		if(null == commonAgreement.getPeriodComRecs() || commonAgreement.getPeriodComRecs().isEmpty() ){
			errMsg.append("参与共保信息 (periodComRecs)为空|");
			retInfo.setErrMsg(errMsg.toString());
			retInfo.setRetCode("0");
			return retInfo;
		}
		commonAgreement.getPeriodComRecs().removeAll(Collections.singleton(null));
		/*前台录入参与共保信息每次应该是一条数据,故取第一条数据*/
		PeriodComRec periodComRec = commonAgreement.getPeriodComRecs().get(0);
		if(periodComRec.getHandoverNum() <= 0){
			errMsg.append("交接批次总人数(handoverNum)必须存在|");
			retInfo.setRetCode("0");
		}
		if(null == periodComRec.getHandoverStartDate()){
			errMsg.append("交接起始日期(handoverStartDate)不能为空|");
			retInfo.setRetCode("0");
		}
		if(null == periodComRec.getHandoverEndDate()){
			errMsg.append("交接截止日期(handoverEndDate)不能为空|");
			retInfo.setRetCode("0");
		}

		if(null != periodComRec.getHandoverStartDate() && null != periodComRec.getHandoverEndDate()){
			if(periodComRec.getHandoverStartDate().getTime() - periodComRec.getHandoverEndDate().getTime() > 24 * 60 * 60){
				errMsg.append("交接起始日期("+periodComRec.getHandoverStartDate()+")不能大于交接截止日期("+periodComRec.getHandoverEndDate()+")|");
				retInfo.setRetCode("0");
			}
			if(periodComRec.getHandoverStartDate().getTime() - commonAgreement.getInForceDate().getTime() < 0){
				errMsg.append("交接起始日期("+periodComRec.getHandoverStartDate()+")不能小于协议生效日期("+commonAgreement.getInForceDate()+")|");
				retInfo.setRetCode("0");
			}
			if(periodComRec.getHandoverEndDate().getTime() - commonAgreement.getTermDate().getTime() > 0){
				errMsg.append("交接截止日期("+periodComRec.getHandoverEndDate()+")不能大于协议终止日期("+commonAgreement.getTermDate()+")|");
				retInfo.setRetCode("0");
			}
		}


		if(periodComRec.getAmnt() < 0.001){
			errMsg.append("交接金额(amnt)必须大于0|");
			retInfo.setRetCode("0");
		}
		if(StringUtils.isEmpty(periodComRec.getPclkBranchNo())){
			errMsg.append("操作员机构(pclkBranchNo)不能为空|");
			retInfo.setRetCode("0");
		}			
		if(StringUtils.isEmpty(periodComRec.getClerkNo())){
			errMsg.append("操作员工号(clerkNo)不能为空|");
			retInfo.setRetCode("0");
		}

		RetInfo ret = checkPeriodComInsurList(periodComRec.getPeriodComInsurs());
		if("0".equals(ret.getRetCode())){
			errMsg.append(ret.getErrMsg());
			retInfo.setRetCode("0");
		}	
		retInfo.setErrMsg(errMsg.toString());		
		return retInfo;
	}

	private boolean contrastPeriodComRec(PeriodComRec arg,PeriodComRec source){

		int flag = 0;/*相似度*/	
		if(Math.abs(arg.getAmnt() - source.getAmnt()) < 0.0001){
			flag++;
		}
		Map<Integer, String> mapSource = new HashMap<>();
		for(int i=0;i<source.getPeriodComInsurs().size();i++){
			mapSource.put(i, source.getPeriodComInsurs().get(i).getCntrNo());
		}
		for(PeriodComInsur periodComInsur : arg.getPeriodComInsurs()){
			if(mapSource.containsValue(periodComInsur.getCntrNo())){
				flag++;
			}
		}
		if(flag >= 2){
			return true;
		}				
		return false;
	}
	private RetInfo CreateMioLogAndBusiDate(CommonAgreement commonAgreement,PeriodComRec periodComRec,JoinComMioRecBo jcBo){

		SimpleDateFormat  sDateFormat = new SimpleDateFormat("yyyy-MM-dd");

		List<MioLogInBo> comMioLogs = new ArrayList<>();
		List<BusiDataInBo> comBusiDatas = new ArrayList<>();

		MioLogInBo comMioLog = new MioLogInBo();
		comMioLog.setPlnmioRecId("0"); /*应收付记录号*/
		comMioLog.setCntrType("G");/*合同类型*/
		comMioLog.setCurrencyCode("CNY");/*币种*/
		comMioLog.setMtnId("0");/*保全批改流水号*/
		comMioLog.setIpsnNo("0");/*被保人序号*/
		ComCompany comCompany = new ComCompany();
		for(ComCompany comCompanyTemp :commonAgreement.getComCompanies()){
			if(null != comCompanyTemp && "M".equals(comCompanyTemp.getCoinsurType())){
				comCompany = comCompanyTemp;
				break;
			}
		}

		comMioLog.setMioCustName(comCompany.getCompanyName());
		comMioLog.setMioCustNo(comCompany.getCompanyCode());
		comMioLog.setBankCode(comCompany.getBankCode());
		comMioLog.setBankaccName(comCompany.getBankAccName());
		comMioLog.setBankAccNo(comCompany.getBankAccNo());	
		comMioLog.setPlnmioDate(new Date());/*应收付日期*/
		comMioLog.setPremDeadline(new Date());/*保费缴费宽限截止日期*/
		comMioLog.setMioDate(new Date());/*实收付日期*/
		comMioLog.setMioItemCode("FA");
		comMioLog.setMioTypeCode("S");
		comMioLog.setMgrBranchNo(commonAgreement.getMgrBranchNo());
		comMioLog.setOclkBranchNo(commonAgreement.getPclkBranchNo());
		comMioLog.setOclkClerkNo(commonAgreement.getClerkNo());
		comMioLog.setSalesNo(periodComRec.getSalesNo());
		comMioLog.setSalesBranchNo(periodComRec.getSalesBranchNo());
		comMioLog.setSalesChannel(periodComRec.getSalesChannel());
		comMioLog.setMioTxNo(0L);/*收付交易号（收据号）*/
		comMioLog.setMioProcFlag("0");/*是否收付处理标记*/
		comMioLog.setRemark("CYGB");
		comMioLog.setAmnt(String.valueOf(periodComRec.getAmnt()));
		comMioLog.setCgNo(commonAgreement.getAgreementNo());
		comMioLog.setCntrNo(commonAgreement.getAgreementNo());
		comMioLog.setPolCode(commonAgreement.getPolicies().get(0).getPolCode());
		comMioLog.setMioClass(-1);
		comMioLog.setMioLogUpdTime(new Date());
		comMioLog.setPlnmioCreateTime(new Date());
		comMioLog.setMioTxClass(0);
		comMioLog.setRouterNo("0");

		RetInfo retInfo = procMioLog(comMioLog,comMioLogs);//
		if("0".equals(retInfo.getRetCode())){
			return retInfo;
		}
		/*---------------------------------------------------*/


		/*组财务接口一笔记录*/
		BusiDataInBo comBusiData = new BusiDataInBo();
		comBusiData.setTaskSeq(0L); /*数据编号*/
		comBusiData.setSeqNo("0");/*流水标识号*/
		comBusiData.setDataSource("JK");/*数据来源*/
		comBusiData.setTransType("2");/*业务类别*/
		comBusiData.setCreateDate(new Date()); /*产生日期*/
		comBusiData.setInsureNo(commonAgreement.getAgreementNo()); /*投保单号*/
		comBusiData.setTransCode("FA");/*收付费项目*/
		comBusiData.setRiskCode(commonAgreement.getPolicies().get(0).getPolCode());
		comBusiData.setYsfDate(new Date());/*应收付日期*/
		comBusiData.setPayType("S");/*收付费方式*/

		comBusiData.setMoneyNum(BigDecimal.valueOf(periodComRec.getAmnt()));
		comBusiData.setItemDesc(comCompany.getCompanyName());/*投保人/领款人名称*/
		comBusiData.setCheckNo("");/*支票号/缴款单号*/
		comBusiData.setReceiptNo("0");/*收据号*/
		comBusiData.setMgBranch(commonAgreement.getMgrBranchNo());
		comBusiData.setSellChannel(periodComRec.getSalesChannel());/*销售渠道*/
		comBusiData.setPolicyDate(new Date()); /*业务日期*/
		comBusiData.setPolicyNo(commonAgreement.getAgreementNo());/*保单号*/
		comBusiData.setChannelDetail("");/*销售渠道明细*/
		comBusiData.setPayMode("W"); /*缴费方式*/
		comBusiData.setAgeInsure("0");/*岁满险种标志：到多少岁满期*/
		comBusiData.setOperDate(new Date());/*保单生效日*/
		comBusiData.setSingleFlag("G");/*团个单标识*/
		comBusiData.setFmaincode(commonAgreement.getPolicies().get(0).getPolCode());/*附加险主险代码*/
		comBusiData.setAlterNo("");/*批单号*/
		comBusiData.setPayTerm("1");/*保单的缴费年期，需要符合险种条款定义：交费年期*/
		comBusiData.setDamageNo("");/*赔案号*/
		comBusiData.setAvalibleTerm(1L);/*保险期间（有效期数）*/
		try {
			comBusiData.setBirthDay(sDateFormat.parse("1899-12-31")); /*被保人生日*/
		} catch (ParseException e) {
			logger.info(e.getMessage(),e);
		}
		comBusiData.setAgreementYear("");/*承保年度*/
		comBusiData.setAccountCode(comCompany.getBankAccNo());/*承保年度*/
		comBusiData.setPactDate(commonAgreement.getSignDate());/*签单日*/
		comBusiData.setFirstpayDate(new Date()); /*红利的首次领取日期*/
		comBusiData.setAvalibleType("Y");/*有效期类型*/
		comBusiData.setCurrency("001");/*币别*/
		comBusiData.setLastTerm("0"); /*年金/满期标志：最后一期是满期*/
		comBusiData.setSpectrans("3");/*特殊业务类别*/
		comBusiData.setClearno("");/*清算收据号*/
		comBusiData.setOtherBranch(periodComRec.getPclkBranchNo());/*入帐机构*/
		comBusiData.setSellerCode(String.format("%6s%8s", periodComRec.getSalesBranchNo(),periodComRec.getSalesNo()));
		comBusiData.setOperatorCode(periodComRec.getClerkNo());/*操作员编号*/
		comBusiData.setOperatorBranch(periodComRec.getPclkBranchNo());
		comBusiData.setAgencyBranch("");/*中介机构网点号*/
		comBusiData.setAccflag("0");/*到帐确认标识*/
		comBusiData.setEXT03("0000000000");/*用来存储操作员 责任中心代码 如果没有就默认 0000000000 需要查询柜面系统*/
		comBusiData.setEXT01(String.valueOf(periodComRec.getAmnt()));
		
		SalesmanQueryInfo salesmanQueryInfo = new SalesmanQueryInfo();

		salesmanQueryInfo.setSalesBranchNo(periodComRec.getSalesBranchNo());
		salesmanQueryInfo.setSalesChannel(periodComRec.getSalesChannel());
		salesmanQueryInfo.setSalesNo(periodComRec.getSalesNo());
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);		
		SalesmanQueryReturnBo  salesmanQueryReturnBo = resulsalesmanInfoService.querySalesman(salesmanQueryInfo);
		if("1".equals(salesmanQueryReturnBo.getRetCode())){
			comBusiData.setEXT02(salesmanQueryReturnBo.getCenterCode());/*用来存储销售员  责任中心代码  需要查询保单系统*/			
		}
		retInfo = procBusiData(comBusiData,comBusiDatas);
		if("0".equals(retInfo.getRetCode())){
			return retInfo;
		}

		/*需要调用保单辅助系统*/
		/*查询协议号下险种信息循环插入PS/S/1*/
		List<String> polCodeList = new ArrayList<>();
		List<Policy> policies = commonAgreement.getPolicies();
		policies.removeAll(Collections.singleton(null));
		double polSumPremium = 0.00;
		/*计算险种保费在所有险种中所占比例，再用交接金额*比列得出对应金额*/
		for(Policy policy : policies){
			polSumPremium += policy.getPremium();
		}

		for(int i = 0;i<policies.size();i++){

			polCodeList.add(policies.get(i).getPolCode());
			/*生成实收付流水 mio_log数据*/
			MioLogInBo cMioLog = new MioLogInBo();
			cMioLog.setPlnmioRecId("0"); /*应收付记录号*/
			cMioLog.setCntrType("G");/*合同类型*/
			cMioLog.setCurrencyCode("CNY");/*币种*/
			cMioLog.setMtnId("0");/*保全批改流水号*/
			cMioLog.setIpsnNo("0");/*被保人序号*/
			cMioLog.setMioCustName(comCompany.getCompanyName());
			cMioLog.setMioCustNo(comCompany.getCompanyCode());
			cMioLog.setBankCode(comCompany.getBankCode());
			cMioLog.setBankaccName(comCompany.getBankAccName());
			cMioLog.setBankAccNo(comCompany.getBankAccNo());	
			cMioLog.setPlnmioDate(new Date());/*应收付日期*/
			cMioLog.setPremDeadline(new Date());/*保费缴费宽限截止日期*/
			cMioLog.setMioDate(new Date());/*实收付日期*/
			cMioLog.setMioItemCode("PS");
			cMioLog.setMioTypeCode("S");
			cMioLog.setMgrBranchNo(commonAgreement.getMgrBranchNo());
			cMioLog.setOclkBranchNo(commonAgreement.getPclkBranchNo());
			cMioLog.setOclkClerkNo(commonAgreement.getClerkNo());
			cMioLog.setSalesNo(periodComRec.getSalesNo());
			cMioLog.setSalesBranchNo(periodComRec.getSalesBranchNo());
			cMioLog.setSalesChannel(periodComRec.getSalesChannel());
			cMioLog.setMioTxNo(0L);/*收付交易号（收据号）*/
			cMioLog.setMioProcFlag("0");/*是否收付处理标记*/
			cMioLog.setRemark("CYGB");
			double lAmnt = BigDecimal.valueOf(periodComRec.getAmnt() * ( policies.get(i).getPremium() / polSumPremium )).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
			cMioLog.setAmnt(String.valueOf(lAmnt));
			cMioLog.setCgNo(commonAgreement.getAgreementNo());
			cMioLog.setCntrNo(commonAgreement.getAgreementNo());
			cMioLog.setPolCode(policies.get(i).getPolCode());
			cMioLog.setMioClass(1);
			cMioLog.setMioLogUpdTime(new Date());
			cMioLog.setPlnmioCreateTime(new Date());
			cMioLog.setMioTxClass(0);
			cMioLog.setRouterNo("0");

			retInfo = procMioLog(cMioLog,comMioLogs);//
			if("0".equals(retInfo.getRetCode())){
				return retInfo;
			}

			/*生成财务接口 busi_data数据*/
			BusiDataInBo cBusiData = new BusiDataInBo();
			cBusiData.setTaskSeq(0L); /*数据编号*/
			cBusiData.setSeqNo("");/*流水标识号*/
			cBusiData.setDataSource("JK");/*数据来源*/
			cBusiData.setTransType("1");/*业务类别*/
			cBusiData.setCreateDate(new Date());
			cBusiData.setInsureNo(commonAgreement.getAgreementNo()); /*投保单号*/
			cBusiData.setTransCode("PS");/*收付费项目*/
			cBusiData.setRiskCode(commonAgreement.getPolicies().get(i).getPolCode());
			cBusiData.setYsfDate(new Date());
			cBusiData.setPayType("S");/*收付费方式*/
			cBusiData.setItemDesc(comCompany.getCompanyName());/*投保人/领款人名称*/
			cBusiData.setCheckNo("");
			cBusiData.setReceiptNo("0");/*收据号*/
			cBusiData.setMgBranch(commonAgreement.getMgrBranchNo());
			cBusiData.setSellChannel(periodComRec.getSalesChannel());
			cBusiData.setPolicyDate(new Date()); /*业务日期*/
			cBusiData.setPolicyNo(commonAgreement.getAgreementNo());/*保单号*/
			cBusiData.setChannelDetail("");/*销售渠道明细*/
			cBusiData.setPayMode("W"); /*缴费方式*/
			cBusiData.setAgeInsure("0");/*岁满险种标志：到多少岁满期*/
			cBusiData.setOperDate(new Date());/*保单生效日*/
			cBusiData.setSingleFlag("G");/*团个单标识*/
			cBusiData.setFmaincode(commonAgreement.getPolicies().get(i).getPolCode());/*附加险主险代码*/
			cBusiData.setAlterNo("");/*批单号*/
			cBusiData.setPayTerm("1");/*保单的缴费年期，需要符合险种条款定义：交费年期*/
			cBusiData.setDamageNo("");/*赔案号*/
			cBusiData.setAvalibleTerm(1L);/*保险期间（有效期数）*/
			try {
				cBusiData.setBirthDay(sDateFormat.parse("1899-12-31")); /*被保人生日*/
			} catch (ParseException e) {
				logger.info(e.getMessage(),e);
			}
			cBusiData.setAgreementYear("");/*承保年度*/
			cBusiData.setAccountCode(comCompany.getBankAccNo());/*承保年度*/
			cBusiData.setPactDate(commonAgreement.getSignDate());/*签单日*/
			cBusiData.setFirstpayDate(new Date()); /*红利的首次领取日期*/
			cBusiData.setAvalibleType("Y");/*有效期类型*/
			cBusiData.setCurrency("001");/*币别*/
			cBusiData.setLastTerm("0"); /*年金/满期标志：最后一期是满期*/
			cBusiData.setSpectrans("3");/*特殊业务类别*/
			cBusiData.setClearno("");/*清算收据号*/
			cBusiData.setOtherBranch(periodComRec.getPclkBranchNo());/*入帐机构*/
			cBusiData.setSellerCode(String.format("%6s%8s", periodComRec.getSalesBranchNo(),periodComRec.getSalesNo()));
			cBusiData.setOperatorCode(periodComRec.getClerkNo());/*操作员编号*/
			cBusiData.setOperatorBranch(periodComRec.getPclkBranchNo());
			cBusiData.setAgencyBranch("");/*中介机构网点号*/
			cBusiData.setAccflag("0");/*到帐确认标识*/

			BigDecimal bigDecimal1 = BigDecimal.valueOf(lAmnt);	
			cBusiData.setMoneyNum(bigDecimal1);
			cBusiData.setEXT01(String.valueOf(lAmnt));
			cBusiData.setEXT03("0000000000");/*用来存储操作员 责任中心代码 如果没有就默认 0000000000 需要查询柜面系统*/
			if("1".equals(salesmanQueryReturnBo.getRetCode())){
				cBusiData.setEXT02(salesmanQueryReturnBo.getCenterCode());/*用来存储销售员  责任中心代码  需要查询保单系统*/			
			}

			retInfo = procBusiData(cBusiData,comBusiDatas);
			if("0".equals(retInfo.getRetCode())){
				return retInfo;
			}
		}
		/*调用PCMS服务*/

		jcBo.setAgreementNo(commonAgreement.getAgreementNo());
		jcBo.setMgrBranchNo(commonAgreement.getMgrBranchNo());
		jcBo.setPolCodeList(polCodeList);
		jcBo.setMioLogInBoList(comMioLogs);
		jcBo.setBusiDataInBoList(comBusiDatas);

		retInfo.setRetCode("1");
		retInfo.setErrMsg("组数据成功!");
		return retInfo;
	}

	/*单条实收付流水记录的操作*/
	private RetInfo procMioLog(MioLogInBo comMioLog,List<MioLogInBo> comMioLogs){

		RetInfo retInfo = new RetInfo();
		/*查询拆分规则 和 和 税率  需要PCMS提供接口*/		
		VATConfigQryInfo vATConfigQryInfo = new VATConfigQryInfo();
		vATConfigQryInfo.setMgrBranchNo(comMioLog.getMgrBranchNo());

		List<VATConfigQryObj> lsConfigQryObjs = new ArrayList<>();		
		VATConfigQryObj vatConfigQryObj = new VATConfigQryObj();
		vatConfigQryObj.setBranchNo(comMioLog.getMgrBranchNo());
		vatConfigQryObj.setConAccflag("1");
		vatConfigQryObj.setSysNo("2");
		vatConfigQryObj.setMioItemCode(comMioLog.getMioItemCode());
		vatConfigQryObj.setItemCodeType(1L);
		vatConfigQryObj.setMioTypeCode(comMioLog.getMioTypeCode());
		vatConfigQryObj.setTypeCodeType(2L);		
		vatConfigQryObj.setPolCode(comMioLog.getPolCode());		
		if(1 == comMioLog.getMioClass()){
			vatConfigQryObj.setSourcePayType("1");
		}else {
			vatConfigQryObj.setSourcePayType("2");
		}
		lsConfigQryObjs.add(vatConfigQryObj);

		vATConfigQryInfo.setvATConfigQryList(lsConfigQryObjs);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		List<VATConfigQryRetInfo> vatConfigQryRetInfos = qryVATCfgInfoServiceClient.queryQryVATCfgInfo(vATConfigQryInfo);

		if(null == vatConfigQryRetInfos || vatConfigQryRetInfos.isEmpty()){
			retInfo.setRetCode("0");
			retInfo.setRetCode("查询拆分规则  和 税率 失败,服务无返回");
			return retInfo;
		}
		if("0".equals(vatConfigQryRetInfos.get(0).getRetCode())){
			retInfo.setRetCode("0");
			retInfo.setErrMsg(vatConfigQryRetInfos.get(0).getErrMsg());
			return retInfo;
		}
		/*没有查询到 税率*/
		if("0".equals(vatConfigQryRetInfos.get(0).getSuccessFlag())){
			comMioLog.setNetIncome(comMioLog.getAmnt());
			comMioLog.setVat("");
			comMioLog.setVatRate("");
			comMioLogs.add(comMioLog);
		}else {//查询到税率
			/*查询税率  PCMS提供接口*/		
			double vatRate = vatConfigQryRetInfos.get(0).getRate();//获得增值税率
			double temp = (Double.valueOf(comMioLog.getAmnt()) * vatRate) / (1 + vatRate);
			/*保留小数点后两位数字，对第三位进行四舍五入 */
			double vat = BigDecimal.valueOf(temp).setScale(2, RoundingMode.HALF_EVEN).doubleValue();//增值税
			comMioLog.setVat(String.valueOf(vat));
			comMioLog.setNetIncome(String.valueOf(Double.valueOf(comMioLog.getAmnt()) - vat));
			comMioLog.setVatRate(String.valueOf(vatRate));
			comMioLogs.add(comMioLog);
		}
		retInfo.setRetCode("1");
		retInfo.setErrMsg("处理实收付流水记录成功!");
		return retInfo;
	}

	private RetInfo procBusiData(BusiDataInBo comBusiData,List<BusiDataInBo> comBusiDatas){

		RetInfo retInfo = new RetInfo();
		/*查询拆分规则 和 和 税率  需要PCMS提供接口*/		
		VATConfigQryInfo vATConfigQryInfo = new VATConfigQryInfo();
		vATConfigQryInfo.setMgrBranchNo(comBusiData.getMgBranch());

		List<VATConfigQryObj> lsConfigQryObjs = new ArrayList<>();		
		VATConfigQryObj vatConfigQryObj = new VATConfigQryObj();
		vatConfigQryObj.setBranchNo(comBusiData.getMgBranch());
		vatConfigQryObj.setConAccflag("#");
		vatConfigQryObj.setSysNo("2");
		vatConfigQryObj.setMioItemCode(comBusiData.getTransCode());
		vatConfigQryObj.setItemCodeType(1L);
		vatConfigQryObj.setMioTypeCode(comBusiData.getPayType());
		vatConfigQryObj.setTypeCodeType(2L);		
		vatConfigQryObj.setPolCode(comBusiData.getRiskCode());
		vatConfigQryObj.setSourcePayType(comBusiData.getTransType());
		lsConfigQryObjs.add(vatConfigQryObj);

		vATConfigQryInfo.setvATConfigQryList(lsConfigQryObjs);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		List<VATConfigQryRetInfo> vatConfigQryRetInfos = qryVATCfgInfoServiceClient.queryQryVATCfgInfo(vATConfigQryInfo);		

		if(null == vatConfigQryRetInfos || vatConfigQryRetInfos.isEmpty()){
			retInfo.setRetCode("0");
			retInfo.setRetCode("查询拆分规则  和 税率 失败,服务无返回");
			return retInfo;
		}
		if("0".equals(vatConfigQryRetInfos.get(0).getRetCode())){
			retInfo.setRetCode("0");
			retInfo.setErrMsg(vatConfigQryRetInfos.get(0).getErrMsg());
			return retInfo;
		}


		/*没有查到拆分规则 */
		if("0".equals(vatConfigQryRetInfos.get(0).getSuccessFlag()) || StringUtils.isEmpty(vatConfigQryRetInfos.get(0).getFiMioItemCode())){
			comBusiDatas.add(comBusiData);
		}else {//如果有拆分规则
			double vatRate = vatConfigQryRetInfos.get(0).getRate();//获得增值税率
			double temp = (comBusiData.getMoneyNum().doubleValue() * vatRate) / (1 + vatRate);

			double vat = BigDecimal.valueOf(temp).setScale(2, RoundingMode.HALF_EVEN).doubleValue();//增值税

			comBusiData.setMoneyNum( BigDecimal.valueOf(comBusiData.getMoneyNum().doubleValue() - vat));
			comBusiDatas.add(comBusiData);
			String str[] = vatConfigQryRetInfos.get(0).getFiMioItemCode().split(",");/*拆分成的多条科目，用逗号分*/
			for(int i = 0 ; i<str.length;i++){

				BusiDataInBo comBusiDataSplit = JSON.parseObject(JSON.toJSONString(comBusiData), BusiDataInBo.class);
				comBusiDataSplit.setEXT01("");
				
				String strItem[] ={"","",""};
				for(int j=0;j < str[i].split("-").length && j < 3;j++){
					strItem[j] = str[i].split("-")[j];
				}
				comBusiDataSplit.setTransCode(strItem[0]);
				if(!"#".equals(strItem[1])){
					comBusiDataSplit.setPayType(strItem[1]);
				}
				comBusiDataSplit.setTransType(strItem[2]);
				comBusiDataSplit.setAccflag("");
				comBusiDataSplit.setMoneyNum(BigDecimal.valueOf(vat));
				comBusiDatas.add(comBusiDataSplit);
			}		
		}
		retInfo.setRetCode("1");
		retInfo.setErrMsg("处理财务接口数据成功!");
		return retInfo;
	}

	private com.newcore.orbps.models.pcms.bo.CommonAgreement convertCommonAgreement(CommonAgreement commonAgreement,PeriodComRec periodComRec){

		com.newcore.orbps.models.pcms.bo.CommonAgreement commonAgreementPcms = JSON.parseObject(JSON.toJSONString(commonAgreement), com.newcore.orbps.models.pcms.bo.CommonAgreement.class);

		/*转换客户信息*/
		if(null != commonAgreement.getComCustomer()){
			commonAgreementPcms.setGrpHolderInfo(JSON.parseObject(JSON.toJSONString(commonAgreement.getComCustomer()), com.newcore.orbps.models.pcms.bo.ComGrpHolderInfo.class));		
		}
		/*转换共保公司信息*/
		if(null != commonAgreement.getComCompanies() && !commonAgreement.getComCompanies().isEmpty()){

			List<com.newcore.orbps.models.pcms.bo.ComCompany> lComCompanies = new ArrayList<>();
			for(ComCompany comCompany :commonAgreement.getComCompanies()){
				lComCompanies.add(JSON.parseObject(JSON.toJSONString(comCompany), com.newcore.orbps.models.pcms.bo.ComCompany.class));
			}
			commonAgreementPcms.setComCompanyList(lComCompanies);
		}
		/*转换险种信息*/
		if(null != commonAgreement.getPolicies() && !commonAgreement.getPolicies().isEmpty()){
			List<com.newcore.orbps.models.pcms.bo.ComPolicy> lPolicies = new ArrayList<>();
			for(Policy policy:commonAgreement.getPolicies()){				
				com.newcore.orbps.models.pcms.bo.ComPolicy policyPcms = JSON.parseObject(JSON.toJSONString(policy), com.newcore.orbps.models.pcms.bo.ComPolicy.class);
				lPolicies.add(policyPcms);
			}
			commonAgreementPcms.setComPolList(lPolicies);
		}
		/*转换参与共保财务信息*/
		if(null != periodComRec){
			commonAgreementPcms.setPeriodComRec(JSON.parseObject(JSON.toJSONString(periodComRec), com.newcore.orbps.models.pcms.bo.PeriodComRec.class));
			commonAgreementPcms.getPeriodComRec().setJoinCoinsurStat("4");
			commonAgreementPcms.getPeriodComRec().setOclkBranchNo(periodComRec.getPclkBranchNo());
			/*转换参与承保保单信息*/
			if(null != periodComRec.getPeriodComInsurs() && !periodComRec.getPeriodComInsurs().isEmpty()){
				List<com.newcore.orbps.models.pcms.bo.PeriodComInsur> lPeriodComInsurs = new ArrayList<>();
				for(PeriodComInsur periodComInsur:periodComRec.getPeriodComInsurs()){
					com.newcore.orbps.models.pcms.bo.PeriodComInsur periodComInsurPcms = JSON.parseObject(JSON.toJSONString(periodComInsur), com.newcore.orbps.models.pcms.bo.PeriodComInsur.class);
					periodComInsurPcms.setNum(periodComInsur.getHandoverNum());			
					lPeriodComInsurs.add(periodComInsurPcms);
				}
				commonAgreementPcms.setPeriodComInsurList(lPeriodComInsurs);
			}
		}	
		return commonAgreementPcms;	
	}

}
