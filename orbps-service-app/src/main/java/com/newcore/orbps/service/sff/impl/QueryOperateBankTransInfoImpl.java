package com.newcore.orbps.service.sff.impl;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.ProcPSInfoDao;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.BankZzczScxe;
import com.newcore.orbps.models.finance.OperateBankTransInBean;
import com.newcore.orbps.models.finance.OperateBankTransOutBean;
import com.newcore.orbps.service.sff.api.QueryOperateBankTransInfo;
import com.newcore.orbpsutils.bussiness.XMLUtil;


/**
 * 银行转账操作查询
 * @author lijifei
 * 2017年3月3日 19:49:37
 */
@Service("queryOperateBankTransInfo")
public class QueryOperateBankTransInfoImpl implements QueryOperateBankTransInfo {

	@Autowired
	ProcPSInfoDao procPSInfoDao;

	private static final String DEFAULT_UALUE_MINUS_ONR = "-1";

	private static final String DEFAULT_UALUE_ONR = "1";

	private static final String DEFAULT_UALUE_NINE = "Q";


	@Override
	public String queryOperateBankTrans(String message) {

		//将xml转换为java bean
		OperateBankTransInBean operateBankTransInBean =	(OperateBankTransInBean) XMLUtil.convertXmlStrToObject(OperateBankTransInBean.class, message);

		//非空字段校验
		if(StringUtils.isBlank(operateBankTransInBean.getBankCode()) || StringUtils.isBlank(operateBankTransInBean.getOpFlag())){
			throw new BusinessException(new Throwable("银行转账操作查询时：[银行代码]或[操作项目为空]"));
		}

		//根据查询入参查询符合条件的信息
		List<BankTrans> bankTransList = procPSInfoDao.getListBankTrans(operateBankTransInBean);
		if(null == bankTransList || bankTransList.isEmpty()){
			//封装消息头
			return "<BODY><RESPONSE><BTT_INFO_NUM>0</BTT_INFO_NUM></RESPONSE></BODY>";
		}

		//根据查询入参查询符合条件的信息，如果不为空时遍历信息，封装到返回参数bankZzczScxe中
		List<BankZzczScxe> bankZzczScxe = new ArrayList<>();
		BankZzczScxe bankZzcz = new BankZzczScxe();
		bankZzcz.setMaxGenDate(procPSInfoDao.getListBankTransMaxCreateDate(operateBankTransInBean).substring(0, 10));
		bankZzcz.setMinGenDate(procPSInfoDao.getListBankTransMinCreateDate(operateBankTransInBean).substring(0, 10));
		bankZzcz.setOpClass(DEFAULT_UALUE_ONR);
		bankZzcz.setOpCode(DEFAULT_UALUE_ONR);
		bankZzcz.setSysNo(DEFAULT_UALUE_NINE);
		bankZzcz.setTransAmnt(String.valueOf(procPSInfoDao.getListBankTransAmnt(operateBankTransInBean)));
		bankZzcz.setTransBatSeq(DEFAULT_UALUE_MINUS_ONR);
		bankZzcz.setTransCount(String.valueOf(bankTransList.size()));
		bankZzcz.setTransStatChn("");
		for (BankTrans bankTrans : bankTransList) {
			bankZzcz.setBankCode(bankTrans.getBankCode());
			bankZzcz.setMioClass(String.valueOf(bankTrans.getMioClass()));
			bankZzczScxe.add(bankZzcz);
		}
		//封装数据
		OperateBankTransOutBean operateBankTransOutBean = new OperateBankTransOutBean();
		operateBankTransOutBean.setBankZzczScxe(bankZzczScxe);
		operateBankTransOutBean.setBttInfoNum(String.valueOf(bankZzczScxe.size()));

		//转换参数，将java bean转化为 xml类型的字符串
		String returnStr = XMLUtil.convertToXml(operateBankTransOutBean);
		//封装消息头
		StringBuilder sb = new StringBuilder();
		sb.append("<BODY>");
		sb.append(returnStr);
		sb.append("</BODY>");
		return sb.toString();
	}

}
