package com.newcore.orbps.service.sff.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.halo.core.dao.annotation.Transaction;
import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.MiosNotToBankXML;
import com.newcore.orbps.service.sff.api.SubmitPauseTransInfoService;
import com.newcore.orbpsutils.bussiness.XMLUtil;
import com.newcore.orbpsutils.dao.api.MioNot2bankDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.PLNMIO_STATE;

@Service("submitPauseTransInfoService")
public class SubmitPauseTransInfoServiceImpl implements SubmitPauseTransInfoService {

	/**
     * 日志管理工具实例.
     */
    private  Logger logger = LoggerFactory.getLogger(SubmitPauseTransInfoServiceImpl.class);
	
	@Autowired
	MioNot2bankDao mioNot2bankDao;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	private final String SUCCESS = "0";
	private final String FAILED  = "-1";
	
	@Transaction
	@Override
	public String checkPauseTransInfo(String msg) {
		//1.解析xml请求报文
		MiosNotToBankXML mioToBankXML = (MiosNotToBankXML) XMLUtil.convertXmlStrToObject(MiosNotToBankXML.class, msg);
		String content="";
		if(mioToBankXML == null){
			logger.info("请求报文解析错误，返回失败!");
			content=this.FAILED;
		}else{
			MiosNotToBank mioToBank = setMiosNotToBank(mioToBankXML);
			//1.首先做一个到期的处理，处理逻辑是直接更新mios_not2bank表
			boolean isSuccess = mioNot2bankDao.updateMioNot2bank(mioToBank);
			//2.如果更新失败，直接返回失败,如果更新成功，则继续走后面的逻辑，
			//2.1 检查是否存在未到期有效记录
			int rows = mioNot2bankDao.queryMioNot2bankRow(mioToBank);
			if(rows == 0){
				//2.1.1 如果不存在到期数据，则把入参直接插入到mios_not2bank表中
				boolean isInsert =mioNot2bankDao.insertMioNot2bank(mioToBank);
				if(isInsert){
					logger.info("数据入库成功!");
					//把对应的应收付状态改为D-作废,不允许
					plnmioRecDao.updatePlnmioRecProcStatById(mioToBank.getPlnmioRecId(), PLNMIO_STATE.INVALID.getKey());
					content=this.SUCCESS;
				}else{
					logger.info("数据入库失败!");
					content=this.SUCCESS;
				}
			}else{
				logger.info("存在未到期登记信息，请重新检查!");
				content=this.FAILED;
			}
		}
		return addOutBody(content);
	}
	
	/**
	 * 银行转账暂停送划封装
	 * @param 
	 * 	mioToBankXML 
	 * @return
	 * 	MiosNotToBank
	 */
	private MiosNotToBank setMiosNotToBank(MiosNotToBankXML mioXML) {
		MiosNotToBank mio = new MiosNotToBank();
		mio.setPlnmioRecId(Long.valueOf(mioXML.getPlnmioRecId())) ; //应收唯一标识
		mio.setSysNo(mioXML.getSysNo()); 		//系统号
		mio.setCntrNo(mioXML.getCntrNo());		//保单号码
		mio.setCustNo(mioXML.getCustNo());		//客户号
		mio.setPlnmioDate(formatDate(mioXML.getPlnmioDate()));      //应收付日期
		mio.setMioClass(mioXML.getMioClass());			//收付费类别
		mio.setMioItemCode(mioXML.getMioItemCode()); 	//收付费项目
		mio.setBankCode(mioXML.getBankCode()); 			//银行代号
		mio.setBankAccNo(mioXML.getBankAccNo()); 		//帐号
		mio.setAmnt(new BigDecimal(mioXML.getAmnt()));  //金额
		mio.setMgrBranchNo(mioXML.getMgrBranchNo()); 	//管理机构号
		mio.setMioCustName(mioXML.getMioCustName()); 	//款项所有人
		mio.setGclkBranchNo(mioXML.getGclkBranchNo()); 	//生成应收付操作员机构
		mio.setGclkClerkNo(mioXML.getGclkClerkNo()); 		//生成应收付操作员代码
		mio.setStopTransReason(mioXML.getStopTransReason());//不可送划原因
		mio.setStopTransDate(formatDate(mioXML.getStopTransDate())); 	//不可送划起日期
		mio.setReTransDate(formatDate(mioXML.getReTransDate())); 		//恢复送划日期
		mio.setLockFlag(mioXML.getLockFlag());				//是否冻结应收付表标识
		mio.setTransFlag(mioXML.getTransFlag()); 			//是否送划标记
		mio.setEnterTime(formatDate(mioXML.getEnterTime())); 			//录入时间
		mio.setEnterBranchNo(mioXML.getEnterBranchNo()); 	//录入机构
		mio.setEnterClerkNo(mioXML.getEnterClerkNo()); 		//录入工号
		mio.setCancelFlag(mioXML.getCancelFlag()); 			//是否撤消标记
		mio.setCancelTime(formatDate(mioXML.getCancelTime())); 			//撤消时间
		mio.setCancelBranchNo(mioXML.getCancelBranchNo()); 	//撤消机构号
		mio.setCancelClerkNo(mioXML.getCancelClerkNo()); 	//撤消工号
		mio.setCancelReason(mioXML.getCancelReason()); 		//撤消理由
		mio.setInvalidStat(mioXML.getInvalIdStat()); 		//失效标志
		return mio;
	}

	/**
	 * 日期格式转换
	 * @param 
	 * 	dateString 
	 * @return
	 */
	private Date formatDate(String dateString) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if("".equals(dateString) || dateString==null){
			return null;
		}
		try {
			Date date = sdf.parse(dateString);
			return date;
		} catch (ParseException e) {
			logger.info("日期格式转换异常！", e);   
			return null;
		}
	}

	/**
	 * 返回报文
	 * @param content
	 * @return
	 */
	private String addOutBody(String content) {
		StringBuilder body = new StringBuilder();
		body.append("<BODY>");
		body.append("<RESPONSE>");
		body.append("<LL_SUCCESS_FLAG>");
		body.append(content);
		body.append("</LL_SUCCESS_FLAG>");
		body.append("</RESPONSE>");
		body.append("</BODY>");
		return body.toString();
	}

}
