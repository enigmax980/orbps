package com.newcore.orbps.service.sff.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.WaitBankDelReq;
import com.newcore.orbps.service.sff.api.DeleteBankTransService;
import com.newcore.orbpsutils.bussiness.XMLUtil;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.orbpsutils.dao.api.MioNot2bankDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;

@Service("deleteBankTransService")
public class DeleteBankTransServiceImpl implements DeleteBankTransService {

	private static Logger logger = LoggerFactory.getLogger(DeleteBankTransServiceImpl.class);
	
	@Autowired
	BankTransDao bankTransDao;
	@Autowired
	MioNot2bankDao mioNot2bankDao;
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Override
	public String delete(String xml) {
		System.err.println(xml);
		String flag = "0";
		WaitBankDelReq waitBankDelReq = new WaitBankDelReq();
		if(!StringUtils.isEmpty(xml)){
			waitBankDelReq = (WaitBankDelReq) XMLUtil.convertXmlStrToObject(WaitBankDelReq.class, xml);
			flag = "0";
		}
		if(StringUtils.isEmpty(waitBankDelReq.getSysNo())){
			logger.error("系统号为空");
			flag = "-1";
		}
		if(StringUtils.isEmpty(waitBankDelReq.getTransCode())){
			logger.error("序号为空");
			flag = "-1";
		}
		if(StringUtils.isEmpty(waitBankDelReq.getBankCode())){
			logger.error("银行代码为空");
			flag = "-1";
		}
		if(StringUtils.equals("0", flag)){
			BankTrans bankTrans = null;
			try {
				bankTrans = bankTransDao.queryBankTransByTransCode(Long.valueOf(waitBankDelReq.getTransCode()));
			} catch (EmptyResultDataAccessException e) {
				logger.error(e.getMessage(), e);
				return retXml("-1",0);
			}catch (IncorrectResultSizeDataAccessException e) {
				logger.error(e.getMessage(), e);
				return retXml("-1",e.getActualSize());
			}catch (Exception e) {
				logger.error(e.getMessage(), e);
				return retXml("-1",-1);
			}	
			try {
				if(!bankTransDao.deleteBankTrans(Long.valueOf(waitBankDelReq.getTransCode()))){
					return retXml("-1",0);
				}	
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return retXml("-1",-1);
			}
			MiosNotToBank miosNotToBank = new MiosNotToBank();
			miosNotToBank.setCntrNo(bankTrans.getCntrNo());
			miosNotToBank.setCustNo(bankTrans.getCustNo());
			miosNotToBank.setPlnmioDate(bankTrans.getPlnmioDate());
			miosNotToBank.setMioClass(bankTrans.getMioClass());
			miosNotToBank.setMioItemCode(bankTrans.getMioItemCode());
			miosNotToBank.setBankCode(bankTrans.getBankCode());
			miosNotToBank.setBankAccNo(bankTrans.getBankAccNo());
			miosNotToBank.setAmnt(bankTrans.getTransAmnt());
			miosNotToBank.setMgrBranchNo(bankTrans.getMgrBranchNo());
			miosNotToBank.setPlnmioRecId(bankTrans.getPlnmioRecId());
			miosNotToBank.setMioCustName(bankTrans.getAccCustName());
			miosNotToBank.setStopTransReason("人工删除的待转账数据");
			miosNotToBank.setTransFlag(3);
			miosNotToBank.setInvalidStat(1);
			miosNotToBank.setSysNo("Q");
			miosNotToBank.setEnterTime(new Date());
			miosNotToBank.setStopTransDate(new Date());
			miosNotToBank.setLockFlag(0);
			miosNotToBank.setEnterBranchNo("");
			miosNotToBank.setEnterClerkNo("");
			try {
				if(!mioNot2bankDao.insertMioNot2bank(miosNotToBank)){
					return retXml("-1",0);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return retXml("-1",-1);
			}
			try {
				Map<String,String> map = new HashMap<>();
				map.put("hold_flag", "0");
				map.put("proc_stat", "N");
				map.put("trans_stat", "U");
				map.put("lock_flag", "0");
				String plnRecIdList = null;
				if("".equals(bankTrans.getPlnmioRecIdList()) || bankTrans.getPlnmioRecIdList() == null){
					plnRecIdList = String.valueOf(bankTrans.getPlnmioRecId());
				}else{
					plnRecIdList = bankTrans.getPlnmioRecIdList();
				}
				if(!plnmioRecDao.updatePlnmioRecByIds(plnRecIdList, map)){
					return retXml("-1",0);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return retXml("-1",-1);
			}
		}
		return retXml(flag,1);
	}
	
    private String retXml(String flag,int num){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("<BODY>");
		stringBuffer.append("<RESPONSE>");
		stringBuffer.append("<RESULT>");
		stringBuffer.append(flag);
		stringBuffer.append("</RESULT>");
		stringBuffer.append("<BANK_TRANS_NUM>");
		stringBuffer.append(num);
		stringBuffer.append("</BANK_TRANS_NUM>");
		stringBuffer.append("</RESPONSE>");
		stringBuffer.append("</BODY>");
		return stringBuffer.toString();
    }

}
