package com.newcore.orbps.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dao.util.DaoUtils;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.InsurApplApprovalRuleConfigDao;
import com.newcore.orbps.dao.api.InsurRuleMangerDao;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.InsurApplApprovalRule;
import com.newcore.orbps.service.api.InsurApplApprovalRuleConfigService;
import com.newcore.supports.dicts.CNTR_TYPE_01;
import com.newcore.supports.dicts.PRD_SALES_CHANNEL_01;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.OrderInfo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 
 * @author nzh 功能：审批规则操作 创建时间：2016年9月8日上午9:28:58
 */
@Service("ApprovalRuleConfigService")
public class InsurApplApprovalRuleConfigServiceImpl implements
InsurApplApprovalRuleConfigService {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory
			.getLogger(InsurApplApprovalRuleConfigServiceImpl.class);

	@Autowired
	InsurApplApprovalRuleConfigDao insurApplApprovalRuleConfigDao;

	@Autowired
	BranchService branchService;

	@Autowired
	InsurRuleMangerDao insurRuleMangerDao;

	boolean addorupRule;

	/**
	 * 
	 * 审批添加功能
	 */
	@Override
	public RetInfo addRule(InsurApplApprovalRule insurApplApprovalRule) {
		Assert.notNull(insurApplApprovalRule);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RetInfo retInfo = new RetInfo();
		if (StringUtils.isEmpty(insurApplApprovalRule.getMgrBranchNo())
				|| StringUtils.isEmpty(insurApplApprovalRule.getSalesChannel())
				|| StringUtils.isEmpty(insurApplApprovalRule.getCntrForm())
				|| StringUtils.isEmpty(insurApplApprovalRule.getRuleType())
				|| StringUtils.isEmpty(insurApplApprovalRule.getOperBranchNo())) {
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("管理机构、销售渠道、契约形式、规则类型、操作员机构等，有值存在空.");
			return retInfo;
		}
		if(null == insurApplApprovalRule.getStartDate() || null == insurApplApprovalRule.getEndDate()){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("规则启用日期和规则终止日期均不能为空.");
			return retInfo;
		}
		if(insurApplApprovalRule.getStartDate().after(insurApplApprovalRule.getEndDate())){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("规则启用日期["+dateFormat.format(insurApplApprovalRule.getStartDate())+"]不应该大于规则终止日期["+dateFormat.format(insurApplApprovalRule.getEndDate())+"].");
			return retInfo;
		}
		//查询省级机构号
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String proBranchno=branchService.findProvinceBranch(insurApplApprovalRule.getOperBranchNo());
		if (!StringUtils.equals(proBranchno, insurApplApprovalRule.getMgrBranchNo())){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("当前省级机构号["+insurApplApprovalRule.getMgrBranchNo()+"]与操作员机构号的省级机构号["+proBranchno+"]不匹配.");
			return retInfo;
		}
		/*根据省级机构号、销售渠道、规则类型、契约形式查询规则是否存在*/
		int count = insurApplApprovalRuleConfigDao.selectForAdd(proBranchno, insurApplApprovalRule.getSalesChannel(), insurApplApprovalRule.getRuleType(),insurApplApprovalRule.getCntrForm());
		if(count > 0){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("已存在管理机构、销售渠道、规则类型、契约形式相同的规则.");
			return retInfo;
		}
		/*对于销售渠道为AA,契约形式为A时，根据省级机构号、规则类型查询规则*/
		if(StringUtils.equals(insurApplApprovalRule.getSalesChannel(), PRD_SALES_CHANNEL_01.ALL.getKey()) && StringUtils.equals(insurApplApprovalRule.getCntrForm(),CNTR_TYPE_01.ALL.getKey())){
			List<InsurApplApprovalRule> insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd3(proBranchno, insurApplApprovalRule.getRuleType());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}			
		}else if(StringUtils.equals(insurApplApprovalRule.getSalesChannel(), PRD_SALES_CHANNEL_01.ALL.getKey())){/*对于销售渠道为AA时，根据省级机构号、规则类型、契约形式查询规则*/	
			InsurApplApprovalRule insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() ,insurApplApprovalRule.getRuleType(),CNTR_TYPE_01.ALL.getKey());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
			List<InsurApplApprovalRule> insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd(proBranchno, insurApplApprovalRule.getRuleType(), insurApplApprovalRule.getCntrForm());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}
			insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd(proBranchno, insurApplApprovalRule.getRuleType(), CNTR_TYPE_01.ALL.getKey());
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}
		}else if(StringUtils.equals(insurApplApprovalRule.getCntrForm(),CNTR_TYPE_01.ALL.getKey())){/*根据省级机构号、规则类型、销售渠道，契约形式为A*/
			InsurApplApprovalRule insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() ,insurApplApprovalRule.getRuleType(),CNTR_TYPE_01.ALL.getKey());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
			List<InsurApplApprovalRule> insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd2(proBranchno, insurApplApprovalRule.getSalesChannel(), insurApplApprovalRule.getRuleType());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}
			insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd2(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() , insurApplApprovalRule.getRuleType());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}
		}else {
			InsurApplApprovalRule insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() ,insurApplApprovalRule.getRuleType(), insurApplApprovalRule.getCntrForm());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
			insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() ,insurApplApprovalRule.getRuleType(),CNTR_TYPE_01.ALL.getKey());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
			insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,insurApplApprovalRule.getSalesChannel(),insurApplApprovalRule.getRuleType(),CNTR_TYPE_01.ALL.getKey());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
		}	
		Integer sqlRet = insurApplApprovalRuleConfigDao.addRule(insurApplApprovalRule);
		if (0 == sqlRet) {
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("更新审核规则表失败.");
			logger.error("更新审核规则表失败");
		}
		retInfo.setRetCode("1");
		return retInfo;
	}

	/**
	 * 
	 * 审批查询功能
	 */
	@Override
	public InsurApplApprovalRule queryRule(InsurApplApprovalRule insurApplRule) {
		InsurApplApprovalRule insurApplApprovalRule = insurApplApprovalRuleConfigDao
				.queryRule(insurApplRule.getId());
		if (null == insurApplApprovalRule) {
			insurApplApprovalRule = new InsurApplApprovalRule();
			insurApplApprovalRule.setRetCode("0");
			insurApplApprovalRule.setRetCode("查询结果为空");
			logger.error("查询结果为空");
			return insurApplApprovalRule;
		}
		insurApplApprovalRule.setRetCode("1");
		return insurApplApprovalRule;
	}

	/**
	 * 
	 * 审批修改功能
	 */
	@Override
	public RetInfo updateRule(InsurApplApprovalRule insurApplApprovalRule) {
		Assert.notNull(insurApplApprovalRule);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		RetInfo retInfo = new RetInfo();
		if (StringUtils.isEmpty(insurApplApprovalRule.getMgrBranchNo())
				|| StringUtils.isEmpty(insurApplApprovalRule.getSalesChannel())
				|| StringUtils.isEmpty(insurApplApprovalRule.getCntrForm())
				|| StringUtils.isEmpty(insurApplApprovalRule.getRuleType())
				|| StringUtils.isEmpty(insurApplApprovalRule.getOperBranchNo())) {
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("管理机构、销售渠道、契约形式、规则类型、操作员机构等，有值存在空.");
			return retInfo;
		}

		if(null == insurApplApprovalRule.getStartDate() || null == insurApplApprovalRule.getEndDate()){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("规则启用日期和规则终止日期均不能为空.");
			return retInfo;
		}
		if(insurApplApprovalRule.getStartDate().after(insurApplApprovalRule.getEndDate())){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("规则启用日期["+dateFormat.format(insurApplApprovalRule.getStartDate())+"]不应该大于规则终止日期["+dateFormat.format(insurApplApprovalRule.getEndDate())+"].");
			return retInfo;
		}
		//查询省级机构号
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String proBranchno=branchService.findProvinceBranch(insurApplApprovalRule.getOperBranchNo());
		if (!StringUtils.equals(proBranchno, insurApplApprovalRule.getMgrBranchNo())){
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("当前省级机构号["+insurApplApprovalRule.getMgrBranchNo()+"]与操作员机构号的省级机构号["+proBranchno+"]不匹配.");
			return retInfo;
		}		

		/*对于销售渠道为All时，根据省级机构号、规则类型、契约形式查询规则*/
		if(StringUtils.equals(insurApplApprovalRule.getSalesChannel(), PRD_SALES_CHANNEL_01.ALL.getKey())){
			List<InsurApplApprovalRule> insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd(proBranchno, insurApplApprovalRule.getRuleType(), insurApplApprovalRule.getCntrForm());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(StringUtils.equals(insurApplApprovalRule.getMgrBranchNo(), insurApplApprovalRules.get(i).getMgrBranchNo()) &&
						StringUtils.equals(insurApplApprovalRule.getSalesChannel(), insurApplApprovalRules.get(i).getSalesChannel()) &&
						StringUtils.equals(insurApplApprovalRule.getRuleType(), insurApplApprovalRules.get(i).getRuleType()) &&
						StringUtils.equals(insurApplApprovalRule.getCntrForm(), insurApplApprovalRules.get(i).getCntrForm())){
					continue;
				}				
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}
			}
			insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd(proBranchno, insurApplApprovalRule.getRuleType(), CNTR_TYPE_01.ALL.getKey());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(StringUtils.equals(insurApplApprovalRule.getMgrBranchNo(), insurApplApprovalRules.get(i).getMgrBranchNo()) &&
						StringUtils.equals(insurApplApprovalRule.getSalesChannel(), insurApplApprovalRules.get(i).getSalesChannel()) &&
						StringUtils.equals(insurApplApprovalRule.getRuleType(), insurApplApprovalRules.get(i).getRuleType()) &&
						StringUtils.equals(insurApplApprovalRule.getCntrForm(), insurApplApprovalRules.get(i).getCntrForm())){
					continue;
				}
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}
			}
		}else if (StringUtils.equals(insurApplApprovalRule.getCntrForm(), CNTR_TYPE_01.ALL.getKey())) {/*根据省级机构号、规则类型、销售渠道，契约形式为A*/
			List<InsurApplApprovalRule> insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd2(proBranchno, insurApplApprovalRule.getSalesChannel(), insurApplApprovalRule.getRuleType());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(StringUtils.equals(insurApplApprovalRule.getMgrBranchNo(), insurApplApprovalRules.get(i).getMgrBranchNo()) &&
						StringUtils.equals(insurApplApprovalRule.getSalesChannel(), insurApplApprovalRules.get(i).getSalesChannel()) &&
						StringUtils.equals(insurApplApprovalRule.getRuleType(), insurApplApprovalRules.get(i).getRuleType()) &&
						StringUtils.equals(insurApplApprovalRule.getCntrForm(), insurApplApprovalRules.get(i).getCntrForm())){
					continue;
				}
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}
			insurApplApprovalRules = insurApplApprovalRuleConfigDao.selectForAdd2(proBranchno, PRD_SALES_CHANNEL_01.ALL.getKey(), insurApplApprovalRule.getRuleType());			
			for(int i = 0;null != insurApplApprovalRules && i<insurApplApprovalRules.size();i++){
				if(StringUtils.equals(insurApplApprovalRule.getMgrBranchNo(), insurApplApprovalRules.get(i).getMgrBranchNo()) &&
						StringUtils.equals(insurApplApprovalRule.getSalesChannel(), insurApplApprovalRules.get(i).getSalesChannel()) &&
						StringUtils.equals(insurApplApprovalRule.getRuleType(), insurApplApprovalRules.get(i).getRuleType()) &&
						StringUtils.equals(insurApplApprovalRule.getCntrForm(), insurApplApprovalRules.get(i).getCntrForm())){
					continue;
				}
				if(!(insurApplApprovalRule.getStartDate().after(insurApplApprovalRules.get(i).getEndDate())||
						insurApplApprovalRule.getEndDate().before(insurApplApprovalRules.get(i).getStartDate()))){
					retInfo.setApplNo("");
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRules.get(i).getMgrBranchNo()+"]、销售渠道为["+
							insurApplApprovalRules.get(i).getSalesChannel()+"]、规则类型为["+insurApplApprovalRules.get(i).getRuleType()+
							"]、契约形式为["+insurApplApprovalRules.get(i).getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRules.get(i).getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRules.get(i).getEndDate())+"]存在交集!");
					return retInfo;
				}	
			}
		}else {
			InsurApplApprovalRule insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() ,insurApplApprovalRule.getRuleType(), insurApplApprovalRule.getCntrForm());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
			insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,PRD_SALES_CHANNEL_01.ALL.getKey() ,insurApplApprovalRule.getRuleType(),CNTR_TYPE_01.ALL.getKey());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
			insurApplApprovalRuleRET = insurApplApprovalRuleConfigDao.selectForObject(proBranchno,insurApplApprovalRule.getSalesChannel(),insurApplApprovalRule.getRuleType(),CNTR_TYPE_01.ALL.getKey());			
			if(null != insurApplApprovalRuleRET && !(insurApplApprovalRule.getStartDate().after(insurApplApprovalRuleRET.getEndDate())||
					insurApplApprovalRule.getEndDate().before(insurApplApprovalRuleRET.getStartDate()))){
				retInfo.setApplNo("");
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该规则的启用时间和终止时间与机构号为["+insurApplApprovalRuleRET.getMgrBranchNo()+"]、销售渠道为["+
						insurApplApprovalRuleRET.getSalesChannel()+"]、规则类型为["+insurApplApprovalRuleRET.getRuleType()+
						"]、契约形式为["+insurApplApprovalRuleRET.getCntrForm()+"]的规则的启用时间["+dateFormat.format(insurApplApprovalRuleRET.getStartDate())+
							"]和终止时间["+dateFormat.format(insurApplApprovalRuleRET.getEndDate())+"]存在交集!");
				return retInfo;
			}
		}
		Integer sqlRet = insurApplApprovalRuleConfigDao.updateRule(insurApplApprovalRule);
		if (0 == sqlRet) {
			retInfo.setApplNo("");
			retInfo.setRetCode("0");
			retInfo.setErrMsg("更新审核规则表失败.");
			logger.error("更新审核规则表失败");
			return retInfo;
		}
		retInfo.setRetCode("1");
		return retInfo;
	}

	/**
	 * 分页查
	 */
	@Override
	public PageData<InsurApplApprovalRule> listPage(
			PageQuery<InsurApplApprovalRule> pageQuery) {
		// Map<String, Long> totalCountMap = new HashMap<>();
		PageData<InsurApplApprovalRule> pageData = new PageData<>();
		try {
			Assert.notNull(pageQuery);
			for (OrderInfo orderInfo : pageQuery.getOrderInfoList()) {
				orderInfo.setColumnName(DaoUtils.toColumnName(orderInfo
						.getColumnName()));
			}
			pageData.setData(insurApplApprovalRuleConfigDao
					.getUserList(pageQuery));
			pageData.setTotalCount(insurApplApprovalRuleConfigDao
					.getTotalCount(pageQuery));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return pageData;
	}
}
