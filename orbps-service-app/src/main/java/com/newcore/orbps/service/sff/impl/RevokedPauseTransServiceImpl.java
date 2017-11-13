package com.newcore.orbps.service.sff.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.MiosNotToBankUndoReq;
import com.newcore.orbps.service.sff.api.RevokedPauseTransService;
import com.newcore.orbpsutils.bussiness.XMLUtil;
import com.newcore.orbpsutils.dao.api.MiosNotToBackDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.PLNMIO_STATE;


@Service("revokedPauseTransService")
public class RevokedPauseTransServiceImpl implements RevokedPauseTransService {

	@Autowired
	MiosNotToBackDao miosNotToBackDao;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	private final static String FORMAT_DATE="yyyy-MM-dd";
	private final static String FORMAT_TIME="yyyy-MM-dd HH:mm:ss";
	
	private static Logger logger = LoggerFactory.getLogger(RevokedPauseTransServiceImpl.class);
	
	@Override
	public String repeal(String xml) {
		
		logger.info(xml);
		String flag = "0";
		MiosNotToBankUndoReq miosNotToBankUndoReq = new MiosNotToBankUndoReq();
		if(!StringUtils.isEmpty(xml)){
			miosNotToBankUndoReq = (MiosNotToBankUndoReq) XMLUtil.convertXmlStrToObject(MiosNotToBankUndoReq.class, xml);
			flag = "0";
		}
		MiosNotToBank miosNotToBank = new MiosNotToBank();	
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getInvalidStat())){
			logger.error("失效标志为空");
			flag = "-1";
		}else {
			miosNotToBank.setInvalidStat(Integer.valueOf(miosNotToBankUndoReq.getInvalidStat()));
		}
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getTransFlag())){
			logger.error("是否送划标志为空");
			flag = "-1";
		}else {
			miosNotToBank.setTransFlag(Integer.valueOf(miosNotToBankUndoReq.getTransFlag()));
		}
		if(null == miosNotToBankUndoReq.getStopTransDate()){
			logger.error("不可送划起日为空");
			flag = "-1";
		}else {
			miosNotToBank.setStopTransDate(procFormatDate(miosNotToBankUndoReq.getStopTransDate(),FORMAT_DATE));
		}
		if(null == miosNotToBankUndoReq.getReTransDate()){
			logger.error("恢复送划日期为空");
			flag = "-1";
		}else {
			miosNotToBank.setReTransDate(procFormatDate(miosNotToBankUndoReq.getReTransDate(),FORMAT_DATE));
		}
		if(null == miosNotToBankUndoReq.getCancelBranchNo()){
			logger.error("撤消机构为空");
			flag = "-1";
		}else {
			miosNotToBank.setCancelBranchNo(miosNotToBankUndoReq.getCancelBranchNo());
		}
		if(null == miosNotToBankUndoReq.getCancelClerkNo()){
			logger.error("撤消工号为空");
			flag = "-1";
		}else {
			miosNotToBank.setCancelClerkNo(miosNotToBankUndoReq.getCancelClerkNo());
		}
		if(null == miosNotToBankUndoReq.getCancelTime()){
			logger.error("撤消时间为空");
			flag = "-1";
		}else {
			miosNotToBank.setCancelTime(procFormatDate(miosNotToBankUndoReq.getCancelTime(),FORMAT_TIME));
		}
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getCancelFlag())){
			logger.error("是否撤消标记为空");
			flag = "-1";
		}else {
			miosNotToBank.setCancelFlag(Integer.valueOf(miosNotToBankUndoReq.getCancelFlag()));
		}
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getCancelReason())){
			logger.error("撤消理由为空");
			flag = "-1";
		}else {
			miosNotToBank.setCancelReason(miosNotToBankUndoReq.getCancelReason());
		}
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getPlnmioRecId())){
			logger.error("顺序号为空");
			flag = "-1";
		}else {
			miosNotToBank.setPlnmioRecId(Long.valueOf(miosNotToBankUndoReq.getPlnmioRecId()));
		}
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getCntrNo())){
			logger.error("合同号为空");
			flag = "-1";
		}else {
			miosNotToBank.setCntrNo(miosNotToBankUndoReq.getCntrNo());
		}
		if(null == miosNotToBankUndoReq.getPlnmioDate()){
			logger.error("应收付日期为空");
			flag = "-1";
		}else {
			
			miosNotToBank.setPlnmioDate(procFormatDate(miosNotToBankUndoReq.getPlnmioDate(),FORMAT_DATE));
		}
		if(StringUtils.isEmpty(miosNotToBankUndoReq.getMioItemCode())){
			logger.error("项目代码为空");
			flag = "-1";
		}else {
			miosNotToBank.setMioItemCode(miosNotToBankUndoReq.getMioItemCode());
		}
		if(StringUtils.equals("0", flag)){
			flag = true == miosNotToBackDao.revoMiosNotToBanks(miosNotToBank)?"0":"-1";
			if(StringUtils.equals("0", flag)){
				//修改对应的应收付数据状态为N-未收
				plnmioRecDao.updatePlnmioRecProcStatById(miosNotToBank.getPlnmioRecId(), PLNMIO_STATE.UNCOLLECTED.getKey());
			}
		}
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<BODY>");
		stringBuffer.append("<RESPONSE>");
		stringBuffer.append("<SUCCESS_FLAG>");
		stringBuffer.append(flag);
		stringBuffer.append("</SUCCESS_FLAG>");
		stringBuffer.append("</RESPONSE>");
		stringBuffer.append("</BODY>");
		return stringBuffer.toString();
	}
	
	/**
	 * 日期格式转换
	 * @param 
	 * 	dateString 
	 * @return
	 */
	private Date procFormatDate(String dateString,String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		if(StringUtils.isEmpty(dateString) || dateString==null){
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
}
