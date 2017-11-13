package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.halo.core.dao.annotation.Transaction;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.banktrans.DateHelper;
import com.newcore.orbps.models.banktrans.ExtractBankTransBean;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.service.api.ExtractBankTransService;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.TRAN_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
/**
 * 抽取银行转账数据功能实现类
 * @author JCC
 * 2016年11月15日 16:28:45
 */
@Service("extractBankTransService")
public class ExtractBankTransServiceImpl implements ExtractBankTransService{
	private Logger logger = Logger.getLogger(this.getClass());
	private final String FAILED = "<RESULT>-1</RESULT><IO_MIOS_BANK_TRANS_COLLECTION_SCXE/>";
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	BankTransDao bankTransDao;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Override
	public String extract(String msg) {
		this.logger.info("【抽取银行转账】传入报文为：" + msg);
		ExtractBankTransBean bean = null;
		try {
			Document document = DocumentHelper.parseText(msg);
			bean = this.readXml(document);
		} catch (Exception e) {
			this.logger.error("【抽取银行转账】解析报文失败！", e);
			return addOutBody(this.FAILED);
		}
		try {
			List<BankTrans> btList = extractBankTrans(bean);
			if(!btList.isEmpty()){
				return this.buildResponse(bean, btList);
			}else{
				return addOutBody(this.FAILED);
			}
		} catch (Exception e) {
			this.logger.error("【抽取银行转账】获取转账数据失败！", e);
			return addOutBody(this.FAILED);
		}
	}

	/**
	 * 解析请求报文
	 * @param document
	 * @return
	 */
	private ExtractBankTransBean readXml(Document document) {
		Element root = document.getRootElement().element("REQUEST").element("IO_MIOS_BANK_TRANS_COLLECTION_SCXE");
		String branchNo = root.element("BRANCH_NO").getTextTrim();
		String bankCode = root.element("BANK_CODE").getTextTrim();
		Integer mioClass = Integer.valueOf(root.element("MIO_CLASS").getTextTrim());
		Integer fcFlag = Integer.valueOf(root.element("FC_FLAG").getTextTrim());
		Long transBatSeq = this.toLong(root.element("TRANS_BAT_SEQ").getTextTrim());
		Integer transCode = Integer.valueOf(root.element("TRANS_CODE").getTextTrim());
		return new ExtractBankTransBean(branchNo, bankCode, mioClass, fcFlag,transBatSeq, transCode);
	}

	/**
	 * 抽取银行转账数据
	 * @param bean
	 * @return
	 */
	@Transaction
	public List<BankTrans> extractBankTrans(ExtractBankTransBean bean) {
		List<BankTrans> bankTransList = getBankTransList(bean);
		if(bankTransList.isEmpty()){
			this.logger.info("【抽取银行转账】未查询出满足条件的银行转账数据！");
			return bankTransList;
		}
		//循环遍历btList中的数据，然后更新状态状态为转账在途。
		StringBuilder sbr = new StringBuilder();
		StringBuilder transCode = new StringBuilder();
		int index =0;
		for (BankTrans bankTrans : bankTransList) {
			if(index > 0){
				transCode.append(",");
				sbr.append(",");
			}
			transCode.append(bankTrans.getTransCode());
			if(bankTrans.getPlnmioRecId()!=0L){
				sbr.append(bankTrans.getPlnmioRecId());
			}else{
				sbr.append(bankTrans.getPlnmioRecIdList());
			}
			index+=1;
		}
		
		Map<String,String> paramMap = new HashMap<>();
		paramMap.put("trans_stat", TRAN_STATE.TAKEN.getKey());
		plnmioRecDao.updatePlnmioRecByIds(sbr.toString(), paramMap);
		long transBatSeq ; 	//转账批次号
		if(bean.getTransBatSeq() == null || bean.getTransBatSeq() == 0){
			//如果为0，则取序列最新值进行赋值
			transBatSeq = bankTransDao.getTransBatSeq();
		}else{
			transBatSeq =bean.getTransBatSeq();
		}
		bankTransDao.updateBankTransTransStat(transCode.toString(),TRAN_STATE.TAKEN.getKey(),transBatSeq);
		return bankTransList;
	}
	
	/**
	 * 生成请求报文
	 * @param bean
	 * @param btList
	 * @return
	 */
	private String buildResponse(ExtractBankTransBean bean,	List<BankTrans> btList) {
		StringBuilder content = new StringBuilder();
		content.append("<RESULT>0</RESULT>");
		content.append("<IO_MIOS_BANK_TRANS_COLLECTION_SCXE>");
		content.append("<TRANS_BAT_SEQ>");
		content.append(bean.getTransBatSeq());
		content.append("</TRANS_BAT_SEQ>");	
		//判断表中是否还有可抽取数据
		List<BankTrans> bankTransList = getBankTransList(bean);
		if(bankTransList.isEmpty()){
			content.append("<TRANS_CODE>0</TRANS_CODE>");
		}else{
			content.append("<TRANS_CODE>1</TRANS_CODE>");
		}
		
		content.append("<BANK_TRANS_NUM>");
		content.append(btList.size());
		content.append("</BANK_TRANS_NUM>");
		content.append("<BANK_TRANS_SCXES>");
		for (BankTrans bankTrans : btList) {
			content.append("<BANK_TRANS_SCXE>");
			content.append("<TRANS_CODE>"+toString(bankTrans.getTransCode())+ "</TRANS_CODE>");
			content.append("<TRAN_STAT>"+ toString(bankTrans.getTransStat())+ "</TRAN_STAT>");
			content.append("<PLNMIO_REC_ID>"+toString(bankTrans.getPlnmioRecId())+ "</PLNMIO_REC_ID>");
			content.append("<TRANS_BAT_SEQ>"+ toString(bankTrans.getTransBatSeq())+ "</TRANS_BAT_SEQ>");
			content.append("<BANK_CODE>"+toString(bankTrans.getBankCode())+ "</BANK_CODE>");
			content.append("<BRANCH_BANK_ACC_NO>"+ toString(bankTrans.getBranchBankAccNo())	+ "</BRANCH_BANK_ACC_NO>");
			content.append("<ACC_CUST_NAME>"+ toString(bankTrans.getAccCustName())+ "</ACC_CUST_NAME>");
			content.append("<BANKACC_ID_TYPE></BANKACC_ID_TYPE>");
			content.append("<BANKACC_ID_NO></BANKACC_ID_NO>");
			content.append("<BANK_ACC_NO>"+ toString(bankTrans.getBankAccNo())+ "</BANK_ACC_NO>");
			// 合并后取顶级机构
			content.append("<MGR_BRANCH_NO>"+ toString(bankTrans.getMgrBranchNo())+ "</MGR_BRANCH_NO>");
			content.append("<CNTR_NO>"	+ toString(bankTrans.getCntrNo()) + "</CNTR_NO>");
			content.append("<IPSN_NO>"	+ toString(bankTrans.getIpsnNo  ()) + "</IPSN_NO>");

			content.append("<TRANS_CLASS>" + bankTrans.getTransClass()+ "</TRANS_CLASS>"); 
			content.append("<MIO_CLASS>"+ toString(bankTrans.getMioClass())	+ "</MIO_CLASS>");
			content.append("<MIO_ITEM_CODE>"+ toString(bankTrans.getMioItemCode())+ "</MIO_ITEM_CODE>");
			// 合并后取最小
			content.append("<PLNMIO_DATE>"+ toString(DateHelper.formatDate(bankTrans.getPlnmioDate(), DateHelper.PATTERN_1))+ "</PLNMIO_DATE>");
			content.append("<MIO_DATE>"+ toString(DateHelper.formatDate(bankTrans.getMioDate(), DateHelper.PATTERN_1))+ "</MIO_DATE>");
			content.append("<TRANS_AMNT>"+ toString(bankTrans.getTransAmnt())+ "</TRANS_AMNT>");
			content.append("<CUST_NO>"+ toString(bankTrans.getCustNo()) + "</CUST_NO>");
			content.append("<GENERATE_DATE>"+ toString(DateHelper.formatDate(bankTrans.getCreateDate(), DateHelper.PATTERN_1))+ "</GENERATE_DATE>");
			content.append("<OPEN_BANK_CODE></OPEN_BANK_CODE>");
			content.append("<OPEN_BANK_NAME></OPEN_BANK_NAME>");
			content.append("<BANK_ACC_TYPE></BANK_ACC_TYPE>");
			content.append("<BANK_PROV_CODE></BANK_PROV_CODE>");
			content.append("<BANK_CITY_CODE></BANK_CITY_CODE>");
			content.append("<MIO_CUST_NAME></MIO_CUST_NAME>");
			content.append("<GCLK_BRANCH_NO></GCLK_BRANCH_NO>");
			content.append("<GCLK_CLERK_NO></GCLK_CLERK_NO>");
			content.append("<EXT01></EXT01>");
			content.append("<EXT02></EXT02>");
			content.append("<EXT03></EXT03>");
			content.append("<EXT04></EXT04>");
			content.append("<EXT05></EXT05>");
			content.append("</BANK_TRANS_SCXE>");
		}
		content.append("</BANK_TRANS_SCXES>");
		content.append("</IO_MIOS_BANK_TRANS_COLLECTION_SCXE>");
		return addOutBody(content.toString());
	}
	
	/**
	 * 查询满足条件的银行转账数据
	 * @param bean
	 * @return
	 */
	private List<BankTrans> getBankTransList(ExtractBankTransBean bean) {
		//根据条件查询更新转账状态为转账在途
		if(bean.getFcFlag() == 1){ 
			//FcFlag 是否包含下属机构 ： 1-包含，0-不包含
			String branchNos=getBranchNos(bean.getBranchNo());
			bean.setBranchNo(branchNos);
		}
		return bankTransDao.queryBankTransList(bean);
	}

	/**
	 * 返回报文
	 * @param content
	 * @return
	 */
	public static String addOutBody(String content) {
		StringBuilder body = new StringBuilder();
		body.append("<BODY>");
		body.append("<RESPONSE>");
		body.append("<SERIALNUM>1</SERIALNUM>");
		body.append(content);
		body.append("</RESPONSE>");
		body.append("</BODY>");
		return body.toString();
	}
	
	/**
	 * 格式转换 :String 转 Long
	 * @param value
	 * @return
	 */
	private Long toLong(String value) {
		if (value == null || value.isEmpty())
			return null;
		return Long.valueOf(value);
	}
	
	/**
	 * 格式转换：Object 转 String
	 * @param value
	 * @return
	 */
	public static String toString(Object obj){
		if(obj == null)
			return "";
		return obj.toString();
	}
	
	@Autowired
	BranchService branchService;
	/**
	 * 获取下属机构号
	 * @param branchNo 父级机构号
	 * @return
	 */
	private String getBranchNos(String branchNo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Branch branch = branchService.findSubBranchAll(branchNo);
		List<String> branchNos = BranchNoUtils.getAllSubBranchNo(branch);
		StringBuilder sdr = new StringBuilder();
		sdr.append("'");
		sdr.append(branchNo);
		sdr.append("'");
		for(String branchNo1:branchNos){
			sdr.append(",");
			sdr.append("'");
			sdr.append(branchNo1);
			sdr.append("'");
		}
		return sdr.toString();
	}
}
