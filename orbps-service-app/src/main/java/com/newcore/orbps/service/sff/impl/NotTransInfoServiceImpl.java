package com.newcore.orbps.service.sff.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.NotTransInfoVo;
import com.newcore.orbps.service.sff.api.NotTransInfoService;
import com.newcore.orbpsutils.dao.api.MiosNotToBackDao;

/**
 * @author wangxiao
 * 创建时间：2017年2月27日下午3:21:17
 */
@Service("notTransInfoService")
public class NotTransInfoServiceImpl implements NotTransInfoService {
	
	@Autowired
	MiosNotToBackDao miosNotToBackDao;
	@Override
	public String queryNotTransInfo(String message) {
		NotTransInfoVo notTransInfoVo = new NotTransInfoVo();
		notTransInfoVo.setCntrNo(subXml(message,"CNTR_NO"));
		notTransInfoVo.setBeginDate(subXml(message,"BEGIN_DATE"));
		notTransInfoVo.setFaFlag(subXml(message,"FA_FLAG"));
		notTransInfoVo.setMioClass(subXml(message,"MIO_CLASS"));
		notTransInfoVo.setMioItemCode(subXml(message,"MIO_ITEM_CODE"));
		notTransInfoVo.setPlnmioSdate(subXml(message, "PLNMIO_SDATE"));
		notTransInfoVo.setPlnmioEdate(subXml(message, "PLNMIO_EDATE"));
		notTransInfoVo.setSysNo(subXml(message,"SYS_NO"));
		List<MiosNotToBank> miosNotToBanks = miosNotToBackDao.getMiosNotToBanks(notTransInfoVo);
		StringBuilder s = new StringBuilder("<BODY><RESPONSE>");
		s.append(addXml(String.valueOf(miosNotToBanks.size()),"BTT_INFO_NUM"));
		for(MiosNotToBank miosNotToBank:miosNotToBanks){
			s.append("<BANK_NOT2TRANSBTT_SCXE>");
			s.append(addXml(miosNotToBank.getCntrNo(), "CNTR_NO"));
			s.append(addXml(miosNotToBank.getSysNo(), "SYS_NO"));
			s.append(addXml(miosNotToBank.getMioClass(), "MIO_CLASS"));
			s.append(addXml(miosNotToBank.getMioItemCode(), "MIO_ITEM_CODE"));
			s.append(addXml(miosNotToBank.getPlnmioDate(), "PLNMIO_DATE"));
			s.append(addXml(String.valueOf(miosNotToBank.getAmnt()), "AMNT"));
			s.append(addXml("", "GEN_BRANCH_NO"));
			s.append(addXml("", "GEN_CLERK_CODE"));
			s.append(addXml(miosNotToBank.getTransFlag()==3?"删除数据":"暂停送划", "OP_CLASS"));
			s.append(addXml(miosNotToBank.getEnterTime(), "OP_DATE"));
			s.append(addXml(miosNotToBank.getStopTransReason(), "REASON"));
			StringBuilder remark = new StringBuilder("自");
			remark.append(miosNotToBank.getStopTransDate()==null?" ":DateFormatUtils.format(miosNotToBank.getStopTransDate(),"yyyy-MM-dd"));
			remark.append("起停止送划,");
			if(miosNotToBank.getReTransDate()!=null){
				remark.append("至");
				remark.append(DateFormatUtils.format(miosNotToBank.getStopTransDate(),"yyyy-MM-dd"));
				remark.append("止,");
			}
			if(miosNotToBank.getCancelFlag()==0){
				remark.append("未撤销");
			}else if(miosNotToBank.getCancelFlag()==1){
				remark.append("已撤销");
			}else if(miosNotToBank.getCancelFlag()==2){
				remark.append("不可撤销");
			}
			s.append(addXml(remark,"REMARK"));
			s.append(addXml(miosNotToBank.getEnterBranchNo(),"OCLK_BRANCH_NO"));
			s.append(addXml(miosNotToBank.getEnterClerkNo(),"OCLK_CLERK_CODE"));
			s.append(addXml("","OCLK_NAME"));
			s.append(addXml(miosNotToBank.getMgrBranchNo(),"MGR_BRANCH_NO"));
			s.append(addXml(miosNotToBank.getBankCode(),"BANK_CODE"));
			s.append("</BANK_NOT2TRANSBTT_SCXE>");
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
