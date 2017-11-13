package com.newcore.orbps.service.sff.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.BankTransInfoDetailsVo;
import com.newcore.orbps.service.sff.api.OperateBankTransInfoDetailsService;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * @author wangxiao
 * 创建时间：2017年3月4日下午5:02:10
 */
@Service("operateBankTransInfoDetailsService")
public class OperateBankTransInfoDetailsServiceImpl implements OperateBankTransInfoDetailsService {
	
	@Autowired
	BranchService branchService;
	
	@Autowired
	BankTransDao bankTransDao;
	
	@Override
	public String queryOperateBankTransInfoDetails(String message) {
		BankTransInfoDetailsVo bankTransInfoDetailsVo = new BankTransInfoDetailsVo();
		bankTransInfoDetailsVo.setBankCode(subXml(message,"BANK_CODE"));
		bankTransInfoDetailsVo.setMioClass(subXml(message,"MIO_CLASS"));
		bankTransInfoDetailsVo.setMinGenDate(subXml(message,"MIN_GEN_DATE"));
		bankTransInfoDetailsVo.setMaxGenDate(subXml(message,"MAX_GEN_DATE"));
		List<String> mgrBranchNos = new ArrayList<>();
		if(!"".equals(subXml(message,"MGR_BRANCH_NO"))){
			mgrBranchNos.add(subXml(message,"MGR_BRANCH_NO"));
		}
		if(StringUtils.equals(subXml(message,"BR_DOWN"),"1")){
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			Branch branch = branchService.findSubBranchAll(subXml(message,"MGR_BRANCH_NO"));
			mgrBranchNos.addAll(BranchNoUtils.getAllSubBranchNo(branch));
		}
		bankTransInfoDetailsVo.setMgrBranchNo(mgrBranchNos);
		List<BankTrans> bankTransList = bankTransDao.queryOperateBankTransInfoDetails(bankTransInfoDetailsVo);
		StringBuilder s = new StringBuilder("<BODY><RESPONSE>");
		s.append(addXml(String.valueOf(bankTransList.size()),"BANK_TRANS_NUM"));
		for(BankTrans bnBankTrans:bankTransList){
			s.append("<BANK_BTTGETTRANS_SCXE>");
			s.append(addXml("Q", "SYS_NO"));
			s.append(addXml(bnBankTrans.getBankCode(),"BANK_CODE"));
			s.append(addXml(bnBankTrans.getCntrNo(),"CNTR_NO"));
			s.append(addXml(bnBankTrans.getAccCustName(),"ACC_CUST_NAME"));
			s.append(addXml(bnBankTrans.getMioClass(),"MIO_CLASS"));
			s.append(addXml(bnBankTrans.getMioItemCode(),"MIO_ITEM_CODE"));
			s.append(addXml(bnBankTrans.getPlnmioDate(),"PLNMIO_DATE"));
			s.append(addXml(bnBankTrans.getTransAmnt(), "TRANS_AMNT"));
			s.append(addXml(bnBankTrans.getCreateDate(), "GENERATE_DATE"));
			s.append(addXml("","GCLK_BRANCH_NO"));
			s.append(addXml("","GCLK_CLERK_NO"));
			s.append(addXml(bnBankTrans.getBankAccNo(),"BANK_ACC_NO"));
			s.append(addXml(bnBankTrans.getAccCustIdNo(),"BANKACC_ID_NO"));
			s.append(addXml(bnBankTrans.getMgrBranchNo(), "MGR_BRANCH_NO"));
			s.append("</BANK_BTTGETTRANS_SCXE>");
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
}
