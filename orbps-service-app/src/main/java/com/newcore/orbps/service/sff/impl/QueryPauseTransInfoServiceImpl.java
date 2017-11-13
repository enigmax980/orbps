package com.newcore.orbps.service.sff.impl;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.PauseTransData;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.finance.QueryPauseTransInfoBean;
import com.newcore.orbps.models.finance.QueryPauseTransInfoRespBean;
import com.newcore.orbps.service.sff.api.QueryPauseTransInfoService;
import com.newcore.orbpsutils.bussiness.XMLUtil;
import com.newcore.orbpsutils.dao.api.MioNot2bankDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.models.service.bo.NullValue;
import com.newcore.supports.models.service.bo.PageQuery;


/**
 * 暂停送划登记查询服务功能实现类
 * @author LiSK
 *
 */
@Service("queryPauseTransInfoService")
public class QueryPauseTransInfoServiceImpl implements QueryPauseTransInfoService {
	
	
	private Logger logger = LoggerFactory.getLogger(QueryPauseTransInfoServiceImpl.class);
	
	private static final int  QUERY_NUM = 150;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Autowired
	MioNot2bankDao mioNot2bankDao;
	
	@Override
	public String queryPauseTrans(String message) {
		/**
		 * 当BankReg_flag==0，则查询满足条件的所有暂停送划历史记录,需翻页；
		当BankReg_flag==1，则先查询应收付记录表(plnmio_rec)中满足条件的数据，再循环遍历查询到的应收付数据，fetch到每条数据，
		并把应收付数据值赋值到一个对象里面去，再通过这个对象的值查询mios_not2bank表。把最后查询到的数据返回给收付费平台。
		当BankReg_flag==2，则先查询mios_not2bank中满足条件的记录，循环遍历查询到的数据，根据里面的是否可撤销以及是否送划标
		记来给该条数据的remark字段赋值。再根据该条数据的值查询应收付表，获取lock_flag、hold_flag与mio_type_code值赋值到
		该条数据中。最后把数据返回给收付费平台
		 */
		
		this.logger.info("查询暂停送划登记开始:"+message);
		//1,解析请求报文到QueryPauseTransInfoBean类对象中
		QueryPauseTransInfoBean bean = (QueryPauseTransInfoBean) XMLUtil.convertXmlStrToObject(QueryPauseTransInfoBean.class, message);
		
		//2,根据BankReg_flag的不同调用不同的方法进行查询
		QueryPauseTransInfoRespBean respBean = new QueryPauseTransInfoRespBean();
		//暂停送划历史查询
		if(bean.getBankRegFlag()==0){ 
			respBean = queryMiosNot2BankHistoryInfo(bean);
		}
		//暂停送划登记查询
		if(bean.getBankRegFlag()==1){
			respBean = queryPlnmioRecInfoNotToTrans(bean);
		}
		//撤销暂停送划查询
		if(bean.getBankRegFlag()==2){
			respBean = queryCancelMiosNot2BankInfo(bean);
		}
		
		
		//3,把查询到的数据组织报文传给收付费平台
		String str = XMLUtil.convertToXml(respBean);
				
		StringBuilder body = new StringBuilder();
		body.append("<BODY>");
		body.append(str);
		body.append("</BODY>");
		
		
		return body.toString();
	}
	/**
	 * 1，根据条件查询plnmio_rec表，查询成功后，把查询到的数据组织到mios_notToBank对象里面去
	 * 2，根据mios_notToBank对象查询mios_not2bank表，看是否存在数据
	 * 3，如果没查询到数据，则把mios_notToBank对象中BankReg_flag设置为1，remark字段添加内容”无预约登记信息，允许登记“
	 * 4，如果查询到数据，则判断恢复送划日期是否大于系统日期，如果大于则把BankReg_flag设置为0，remark字段添加内容”不允许登记“
	 * 5，如果还没过期，则判断mios_notToBank对象中trans_flag的值，
	 *           如果trans_flag in（2，4，5），则把BankReg_flag设置为0，remark字段添加内容”不允许登记“
	 *           如果trans_flag=1，把BankReg_flag设置为1，remark字段添加内容”允许登记“
	 * 6，把处理完成的数据组织放到返回对象中
	 * 7，这里一次查询只查询150条
	 * @param bean
	 */
	private QueryPauseTransInfoRespBean queryPlnmioRecInfoNotToTrans(QueryPauseTransInfoBean bean){
		
		//根据投保单号(保单号)、客户号、应收付日期、收付标志、账号以及收付费项目查询plnmio_rec表，查询的时候只查询银行转账数据
		List<PlnmioRec> plnList =  plnmioRecDao.queryPlnmioInfoWithPageData(bean);
		QueryPauseTransInfoRespBean respBean = new QueryPauseTransInfoRespBean();
		List<PauseTransData> pauseTransList = new ArrayList<>();
		this.logger.info("size:"+plnList.size());
		for(PlnmioRec pln : plnList){
			PauseTransData bank = new PauseTransData();
			bank.setPlnmioRecId(pln.getPlnmioRecId());
			bank.setMgrBranchNo(pln.getMgrBranchNo());
			bank.setMioClass(pln.getMioClass());
			bank.setCntrNo(pln.getCntrNo());
			bank.setCustNo(pln.getMioCustNo()==null?"":pln.getMioCustNo());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String plnmioDate = sdf.format(pln.getPlnmioDate());
			bank.setPlnmioDate(plnmioDate);
			bank.setMioItemCode(pln.getMioItemCode());
			bank.setBankCode(pln.getBankCode());
			bank.setBankAccNo(pln.getBankAccNo());
			bank.setMioCustName(pln.getMioCustName());
			bank.setLockFlag(pln.getLockFlag());
			bank.setHoldFlag(pln.getHoldFlag());
			bank.setMioType(pln.getMioType());
			bank.setBankRegFlag(String.valueOf(bean.getBankRegFlag()));
			bank.setAmnt(pln.getAmnt());
			bank.setSysNo("Q");
			
			//根据PauseTransData对象查询mios_not2bank表中是否有对应数据
			MiosNotToBank  mios=  mioNot2bankDao.queryMioNot2bankInfo(bank);
			//查询为空，则允许登记
			if(mios==null){
				bank.setBankRegFlag("1");
				bank.setRemark("无预约登记信息，允许登记");
				bank.setTransFlag("");
				bank.setReTransDate("");
				bank.setCancelFlag("");
				bank.setEnterTime("");
				bank.setEnterBranchNo("");
				bank.setEnterClerkNo("");
				bank.setStopTransDate("");
				bank.setStopTransReason("");
				bank.setCancelTime("");
				bank.setCancelBranchNo("");
				bank.setCancelClerkNo("");
				bank.setCancelReason("");
			}else{
				//把查询到得对象部分值赋值都bank对象中
				bank.setTransFlag(String.valueOf(mios.getTransFlag()));
				if(null == mios.getReTransDate()){
					bank.setReTransDate("");
				}else{
					bank.setReTransDate(new SimpleDateFormat("yyyy-MM-dd").format(mios.getReTransDate()));
				}
				bank.setCancelFlag(String.valueOf(mios.getCancelFlag()));
				if(null == mios.getEnterTime()){
					bank.setEnterTime("");
				}else{
					bank.setEnterTime(new SimpleDateFormat("yyyy-MM-dd").format(mios.getEnterTime()));
				}
				bank.setEnterBranchNo(mios.getEnterBranchNo());
				bank.setEnterClerkNo(mios.getEnterClerkNo());
				if(null == mios.getStopTransDate()){
					bank.setStopTransDate("");
				}else{
					bank.setStopTransDate(new SimpleDateFormat("yyyy-MM-dd").format(mios.getStopTransDate()));
				}
				bank.setStopTransReason(mios.getStopTransReason());
				if(null == mios.getCancelTime()){
					bank.setCancelTime("");
				}else{
					bank.setCancelTime(new SimpleDateFormat("yyyy-MM-dd").format(mios.getCancelTime()));
				}
				bank.setCancelBranchNo(mios.getCancelBranchNo());
				bank.setCancelClerkNo(mios.getCancelClerkNo());
				bank.setCancelReason(mios.getCancelReason());
				
				//恢复送划日期大于系统日期
				if(mios.getReTransDate().getTime()>new Date().getTime()){
					bank.setBankRegFlag("0");
					bank.setRemark("不允许登记");
				}else{
					if("2".equals(bank.getTransFlag()) || "5".equals(bank.getTransFlag()) || "4".equals(bank.getTransFlag())){
						bank.setBankRegFlag("0");
						bank.setRemark("不允许登记");
					}else{
						bank.setBankRegFlag("1");
						bank.setRemark("允许登记");
					}
				}
			}
			
			bank.setGclkBranchNo("");
			bank.setGclkClerkNo("");
			bank.setInvalidStat("");
			bank.setSelflag("");
			
			//把查询组织得数据赋值到返回报文中
			pauseTransList.add(bank);
			
		}
		respBean.setPauseTransDataList(pauseTransList);
		respBean.setNbNum(pauseTransList.size());
		respBean.setReason("");
		respBean.setReTransDate("");
		respBean.setStopTransDate("");
			
		return respBean;
	}
	/**
	 * 根据条件查询mios_not2bank表，循环获取并处理查询到的每一条数据
	 * @param bean
	 * @return
	 */
	private QueryPauseTransInfoRespBean queryMiosNot2BankHistoryInfo(QueryPauseTransInfoBean bean){
		
		QueryPauseTransInfoRespBean respBean = new QueryPauseTransInfoRespBean();
		List<PauseTransData> pauseList = new ArrayList<>();
		
		//组织分页查询条件  查询起始条数和查询总条数
		PageQuery<NullValue> page = new PageQuery<>();
		page.setPageStartNo(bean.getMaxId());
		page.setPageSize(QUERY_NUM);
		
		//调用DAO函数进行查询
		List<MiosNotToBank> bankList = mioNot2bankDao.queryHistoryNotTransInfo(bean, page);
		
		for(MiosNotToBank notBank : bankList){
			PauseTransData bank = new PauseTransData();
			bank.setPlnmioRecId(notBank.getPlnmioRecId());
			bank.setMgrBranchNo(notBank.getMgrBranchNo());
			bank.setMioClass(notBank.getMioClass());
			bank.setCntrNo(notBank.getCntrNo());
			bank.setCustNo(notBank.getCustNo());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String plnmioDate = sdf.format(notBank.getPlnmioDate());
			bank.setPlnmioDate(plnmioDate);
			bank.setMioItemCode(notBank.getMioItemCode());
			bank.setBankCode(notBank.getBankCode());
			bank.setBankAccNo(notBank.getBankAccNo());
			bank.setMioCustName(notBank.getMioCustName());
			bank.setLockFlag(String.valueOf(notBank.getLockFlag()));
			bank.setMioType("T");
			bank.setBankRegFlag(String.valueOf(bean.getBankRegFlag()));
			bank.setTransFlag(String.valueOf(notBank.getTransFlag()));
			if(null == notBank.getReTransDate()){
				bank.setReTransDate("");
			}else{
				bank.setReTransDate(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getReTransDate()));
			}
			bank.setCancelFlag(String.valueOf(notBank.getCancelFlag()));
			if(null == notBank.getEnterTime()){
				bank.setEnterTime("");
			}else{
				bank.setEnterTime(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getEnterTime()));
			}
			bank.setEnterBranchNo(notBank.getEnterBranchNo());
			bank.setEnterClerkNo(notBank.getEnterClerkNo());
			if(null == notBank.getStopTransDate()){
				bank.setStopTransDate("");
			}else{
				bank.setStopTransDate(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getStopTransDate()));
			}
			bank.setStopTransReason(notBank.getStopTransReason());
			if(null == notBank.getCancelTime()){
				bank.setCancelTime("");
			}else{
				bank.setCancelTime(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getCancelTime()));
			}
			bank.setCancelBranchNo(notBank.getCancelBranchNo());
			bank.setCancelClerkNo(notBank.getCancelClerkNo());
			bank.setCancelReason(notBank.getCancelReason());
			bank.setAmnt(notBank.getAmnt());
			bank.setSysNo("Q");
			
			//根据查询出来数据各标识的不同，给remark字段赋值不同的值
			StringBuilder strRemark = new StringBuilder();
			if(null == notBank.getReTransDate()){
				strRemark.append("已到期，");
			}else{
				if(notBank.getReTransDate().before(new Date())){
					strRemark.append("未到期，");
				}else{
					strRemark.append("已到期，");
				}
			}
			
			switch(notBank.getTransFlag()){
				case 0:
					strRemark.append("柜面登记不传送");
					break;
				case 1:
					strRemark.append("可传送，");
					break;
				case 2:
					strRemark.append("系统自检不传送，");
					break;
				case 4:
				case 5:
					strRemark.append("");
					break;
				case 6:
					strRemark.delete(0, strRemark.length());
					strRemark.append("在线支付登记不送达");
					break;
				default:
					strRemark.append("");
					break;
			}
			if(notBank.getCancelFlag()==0){
				strRemark.append("未撤销，");
			}else if(notBank.getCancelFlag()==1){
				strRemark.append("已撤销，");
			}else if(notBank.getCancelFlag()==2){
				strRemark.append("不可撤销，");
			}
			
			if(notBank.getInvalidStat()==0){
				strRemark.append("有效");
			}else if(notBank.getInvalidStat()==1){
				strRemark.append("无效");
			}
			bank.setRemark(strRemark.toString());
			
			//根据应收付标识id查询应收付信息
			PlnmioRec pln = plnmioRecDao.queryPlnmioRec(notBank.getPlnmioRecId());
			if(null != pln ){
				bank.setLockFlag(pln.getLockFlag());
				bank.setHoldFlag(pln.getHoldFlag());
				bank.setMioType(pln.getMioType());
			}else{
				bank.setHoldFlag("");
			}
			pauseList.add(bank);
		}
		
		respBean.setPauseTransDataList(pauseList);
		respBean.setNbNum(pauseList.size());
		respBean.setReason("");
		respBean.setReTransDate("");
		respBean.setStopTransDate("");
		
		return respBean;
	}
	/**
	 * 1，根据条件查询mios_not2bank表，循环获取并处理查询到的每一条数据
	 * 2，如果查询到的数据cancel_flag=2，则BankReg_flag=0，remark=“不允许回退”，
	 *    否则BankReg_flag=1，如果trans_flag=0，则remark=“柜面登记不送划，允许撤销原登记信息”，
	 *    如果trans_flag=1，则remark=“已登记可送划，允许撤销原登记信息”，
	 *    如果trans_flag=2，4，5，则remark=“自检不送划，允许回退为可送划”，
	 * 3，再根据处理完的暂停送划数据查询应收付表，如果没查询到数据，并且trans_flag=2，4，5，则BankReg_flag=0，remark=“查无对应应收付信息”，
	 * 4，把处理的数据组织赋值到返回对象中
	 * 5，这里一次查询只查询150条
	 */
	private QueryPauseTransInfoRespBean queryCancelMiosNot2BankInfo(QueryPauseTransInfoBean bean){
		
		
		QueryPauseTransInfoRespBean respBean = new QueryPauseTransInfoRespBean();
		List<PauseTransData> pauseList = new ArrayList<>();
		
		//组织分页查询条件  查询起始条数和查询总条数
		PageQuery<NullValue> page = new PageQuery<>();
		page.setPageStartNo(bean.getMaxId());
		page.setPageSize(QUERY_NUM);
		
		//调用DAO函数进行查询
		List<MiosNotToBank> bankList = mioNot2bankDao.queryHistoryNotTransInfo(bean, page);
		
		for(MiosNotToBank notBank : bankList){
			PauseTransData bank = new PauseTransData();
			bank.setPlnmioRecId(notBank.getPlnmioRecId());
			bank.setMgrBranchNo(notBank.getMgrBranchNo());
			bank.setMioClass(notBank.getMioClass());
			bank.setCntrNo(notBank.getCntrNo());
			bank.setCustNo(notBank.getCustNo());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String plnmioDate = sdf.format(notBank.getPlnmioDate());
			bank.setPlnmioDate(plnmioDate);
			bank.setMioItemCode(notBank.getMioItemCode());
			bank.setBankCode(notBank.getBankCode());
			bank.setBankAccNo(notBank.getBankAccNo());
			bank.setMioCustName(notBank.getMioCustName());
			bank.setLockFlag(String.valueOf(notBank.getLockFlag()));
			bank.setMioType("T");
			bank.setBankRegFlag(String.valueOf(bean.getBankRegFlag()));
			bank.setTransFlag(String.valueOf(notBank.getTransFlag()));
			if(null == notBank.getReTransDate()){
				bank.setReTransDate("");
			}else{
				bank.setReTransDate(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getReTransDate()));
			}
			bank.setCancelFlag(String.valueOf(notBank.getCancelFlag()));
			if(null == notBank.getEnterTime()){
				bank.setEnterTime("");
			}else{
				bank.setEnterTime(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getEnterTime()));
			}
			bank.setEnterBranchNo(notBank.getEnterBranchNo());
			bank.setEnterClerkNo(notBank.getEnterClerkNo());
			if(null == notBank.getStopTransDate()){
				bank.setStopTransDate("");
			}else{
				bank.setStopTransDate(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getStopTransDate()));
			}
			bank.setStopTransReason(notBank.getStopTransReason());
			if(null == notBank.getCancelTime()){
				bank.setCancelTime("");
			}else{
				bank.setCancelTime(new SimpleDateFormat("yyyy-MM-dd").format(notBank.getCancelTime()));
			}
			bank.setCancelBranchNo(notBank.getCancelBranchNo());
			bank.setCancelClerkNo(notBank.getCancelClerkNo());
			bank.setCancelReason(notBank.getCancelReason());
			bank.setAmnt(notBank.getAmnt());
			bank.setSysNo("Q");
			
			//根据查询出来数据各标识的不同，给remark字段赋值不同的值
			if(notBank.getCancelFlag()==2){
				bank.setBankRegFlag("0");
				bank.setRemark("不允许回退");
			}else{
				bank.setBankRegFlag("1");
				if(notBank.getTransFlag()==0){
					bank.setRemark("柜面登记不送划，允许撤销原登记信息");
				}else if(notBank.getTransFlag()==1){
					bank.setRemark("柜面登记不送划，允许撤销原登记信息");
				}else if(notBank.getTransFlag()==2 || notBank.getTransFlag()==4 || notBank.getTransFlag()==5){
					bank.setRemark("自检不送划，允许回退为可送划");
				}
			}
			
			//根据应收付标识id查询应收付信息
			PlnmioRec pln = plnmioRecDao.queryPlnmioRec(notBank.getPlnmioRecId());
			if(null != pln ){
				bank.setLockFlag(pln.getLockFlag());
				bank.setHoldFlag(pln.getHoldFlag());
				bank.setMioType(pln.getMioType());
			}else{
				if(notBank.getTransFlag()==2 || notBank.getTransFlag()==4 || notBank.getTransFlag()==5){
					bank.setBankRegFlag("0");
					bank.setRemark("查无对应应收付信息");
				}
				bank.setHoldFlag("");
			}
			
			pauseList.add(bank);
		}
		
		respBean.setPauseTransDataList(pauseList);
		respBean.setNbNum(pauseList.size());
		respBean.setReason("");
		respBean.setReTransDate("");
		respBean.setStopTransDate("");
		
		return respBean;
	}
	
	
}











