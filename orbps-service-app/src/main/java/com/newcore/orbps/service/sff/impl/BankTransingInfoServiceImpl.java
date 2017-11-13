package com.newcore.orbps.service.sff.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.BankTransingInfoVo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.service.sff.api.BankTransingInfoService;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.supports.dicts.BANK_TRANS_RESULT;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.LIST_TYPE;

/**
 * @author wangxiao
 * 创建时间：2017年3月2日上午9:06:09
 */
@Service("bankTransingInfoService")
public class BankTransingInfoServiceImpl implements BankTransingInfoService {
	
	@Autowired
	BankTransDao bankTransDao;
	@Autowired
	MongoTemplate mongoTemplate;
	@Override
	public String queryBankTransingInfo(String message) {
		BankTransingInfoVo bankTransingInfoVo = new BankTransingInfoVo();
		bankTransingInfoVo.setCntrNo(subXml(message, "CNTR_NO"));
		bankTransingInfoVo.setAccCustName(subXml(message, "ACC_CUST_NAME"));
		bankTransingInfoVo.setBankaccIdNo(subXml(message, "BANKACC_ID_NO"));
		bankTransingInfoVo.setBankAccNo(subXml(message, "BANK_ACC_NO"));
		bankTransingInfoVo.setBankCode(subXml(message, "BANK_CODE"));
		bankTransingInfoVo.setMioItemCode(subXml(message, "MIO_ITEM_CODE"));
		bankTransingInfoVo.setPlnmioSdate(subXml(message, "PLNMIO_SDATE"));
		bankTransingInfoVo.setPlnmioEdate(subXml(message, "PLNMIO_EDATE"));
		bankTransingInfoVo.setMioSdate(subXml(message, "MIO_SDATE"));
		bankTransingInfoVo.setMioEdate(subXml(message, "MIO_EDATE"));
		bankTransingInfoVo.setSysNo(subXml(message, "SYS_NO"));
		bankTransingInfoVo.setMioClass(subXml(message, "MIO_CLASS"));
		List<BankTrans> bankTransList = bankTransDao.queryBankTransingInfo(bankTransingInfoVo);
		StringBuilder s = new StringBuilder("<BODY><RESPONSE>");
		s.append(addXml(String.valueOf(bankTransList.size()),"BTT_INFO_NUM"));
		for(BankTrans bnBankTrans:bankTransList){
			GrpInsurAppl grpInsurAppl = mongoTemplate.findOne(new Query(new Criteria("cgNo").is(bnBankTrans.getCntrNo())), GrpInsurAppl.class);
			String phrName = "";
			if(grpInsurAppl == null){
				phrName = "";
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.GRP_INSUR.getKey())
				|| (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())
					&& StringUtils.equals(grpInsurAppl.getSgNo(), LIST_TYPE.GRP_SG.getKey()))){
		 		if(grpInsurAppl.getGrpHolderInfo() != null){
					phrName = grpInsurAppl.getGrpHolderInfo().getGrpName();
				}
			}else if(StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())
					&& StringUtils.equals(grpInsurAppl.getSgNo(), LIST_TYPE.PSN_SG.getKey())){
				if(grpInsurAppl.getPsnListHolderInfo() != null){
					phrName = grpInsurAppl.getPsnListHolderInfo().getSgName();
				}
			}
			String agentName = "";
			if(grpInsurAppl == null){
				agentName = "";
			}else if(grpInsurAppl.getSalesInfoList()!=null && !grpInsurAppl.getSalesInfoList().isEmpty()){
				agentName = getMainSales(grpInsurAppl.getSalesInfoList()).getSalesName();
			}
			s.append("<BANK_TRANSDTL_SCXE>");
			s.append(addXml(bnBankTrans.getMgrBranchNo(), "MGR_BRANCH_NO"));
			s.append(addXml(bnBankTrans.getCntrNo(),"CNTR_NO"));
			s.append(addXml(bnBankTrans.getMioItemCode(),"MIO_ITEM_CODE"));
			s.append(addXml(bnBankTrans.getBankCode(),"BANK_CODE"));
			s.append(addXml(bnBankTrans.getBankAccNo(),"BANK_ACC_NO"));
			s.append(addXml(bnBankTrans.getAccCustName(),"ACC_CUST_NAME"));
			s.append(addXml(bnBankTrans.getAccCustIdNo(),"BANKACC_ID_NO"));
			s.append(addXml(phrName,"PHR_NAME"));
			s.append(addXml(bnBankTrans.getPlnmioDate(),"PLNMIO_DATE"));
			s.append(addXml(agentName, "AGENT_NAME"));
			s.append(addXml(bnBankTrans.getCreateDate(), "GENERATE_DATE"));
			s.append(addXml(bnBankTrans.getMioDate(),"MIO_DATE"));
			//转账结果
			s.append(addXml(BANK_TRANS_RESULT.valueOfKey(bnBankTrans.getTransStat()).getDescription(), "TRANS_STAT_CHN"));
			//银行转账结果(赋值为银行错误明细)
			s.append(addXml("", "BANK_STAT"));
			//银行转账结果错误大类
			s.append(addXml("", "BANK_ERR_STAT"));
			//应收付业务受理人
			s.append(addXml("", "OCLK_NAME"));
			//业务核心转账批次号
			s.append(addXml("", "TRANS_BAT_NO"));
			s.append(addXml(bnBankTrans.getMioClass(),"MIO_CLASS"));
			s.append(addXml(bnBankTrans.getTransBatSeq(), "TRANS_BAT_SEQ"));
			s.append(addXml("Q", "SYS_NO"));
			s.append(addXml(bnBankTrans.getTransAmnt(), "TRANS_AMNT"));
			//选择标志
			s.append(addXml(bnBankTrans.getTransAmnt(), "SEL_FLAG"));
			s.append(addXml(bnBankTrans.getTransCode(), "TRANS_CODE"));
			//转账平台转账流水号
			s.append(addXml("", "MIOS_TRANS_CODE"));
			//转账平台转账合并流水号
			s.append(addXml("", "UNITE_TRANS_CODE"));
			s.append("</BANK_TRANSDTL_SCXE>");
		}
		s.append("</RESPONSE></BODY>");
		return s.toString();
	}
	private String subXml(String s,String xml){
		int l1 = xml.length();
		int begin = s.indexOf("<"+xml+">");
		int end = s.indexOf("</"+xml+">");
		if(begin!=-1 && end!=-1){
			s = s.substring(begin+l1+2,end);
		}else{
			s = null;
		};
		return s;
	}
	private String addXml(Object obj,String xml){
		StringBuilder sb = new StringBuilder();
		sb.append("<");
		sb.append(xml);
		sb.append(">");
		if(obj instanceof Date) {
			sb.append(DateFormatUtils.format((Date)obj,"yyyy-MM-dd"));
		}else{
			sb.append(obj==null?"":String.valueOf(obj));
		}
		sb.append("</");
		sb.append(xml);
		sb.append(">");
		return sb.toString();
	}
	//获取主销售员
	private SalesInfo getMainSales(List<SalesInfo> salesInfos){
		SalesInfo salesInfo = salesInfos.get(0);
		for(SalesInfo salesInfo1:salesInfos){
			if(StringUtils.equals(salesInfo1.getDevelMainFlag(),DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey())){
				return salesInfo1;
			}
		}
		return salesInfo;
	}

}
