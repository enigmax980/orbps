package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.halo.core.dao.annotation.Transaction;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.OCLK;
import com.newcore.orbps.models.banktrans.ReceiveTransResultBean;
import com.newcore.orbps.models.finance.BankTrans;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.service.api.ReceiveTransResultService;
import com.newcore.orbpsutils.dao.api.BankTransDao;
import com.newcore.orbpsutils.dao.api.EarnestAccInfoDao;
import com.newcore.orbpsutils.dao.api.MioAccInfoLogDao;
import com.newcore.orbpsutils.dao.api.MioLogDao;
import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.BANK_TRANS_RESULT;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.PLNMIO_STATE;
import com.newcore.supports.dicts.TRAN_STATE;

/**
 * 处理银行转账结果实现
 * @author 贾陈陈
 */
@Service("receiveTransResultService")
public class ReceiveTransResultServiceImpl implements ReceiveTransResultService {
	// 应收付标识：自动增长序列 
	private Logger logger = Logger.getLogger(this.getClass());
	private final String ERROR = "<ERROR>解析XML出错！</ERROR>";
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	BankTransDao bankTransDao;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Autowired
	EarnestAccInfoDao earnestAccInfoDao;
	
	@Autowired
	MioAccInfoLogDao mioAccInfoLogDao;
	
	@Autowired
	MioLogDao mioLogDao;
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	PlnmioCommonDao plnmioCommonDao;
	
	@Override
	public String receive(String msg) {
		logger.info("开始产生实收数据，传入的报文是" + msg);
		List<ReceiveTransResultBean> list;
		OCLK oclk = new OCLK();
		try {
			Document document = DocumentHelper.parseText(msg);
			list = this.readXml(document, oclk);
		} catch (Exception e) {
			this.logger.error("解析XML报文出错！", e);
			return addOutBody(this.ERROR);
		}
		StringBuilder response = new StringBuilder();
		response.append("<BANK_TRANS_LIST>");
		
		for (ReceiveTransResultBean bean : list) {
			response.append("<BANK_TRANS>");
			response.append("<MIOS_TRANS_CODE>" + bean.getMiosTransCode() + "</MIOS_TRANS_CODE>");
			response.append("<TRANS_CODE>" + bean.getTransCode() + "</TRANS_CODE>");
			try {
				// 处理转账结果
				BankTrans bankTrans = receiveBankTrans(bean, oclk);
				if(bankTrans == null){		
					this.logger.error(bean.getTransCode() + "处理失败,无银行转账数据！");
					response.append("<MIO_TX_NO></MIO_TX_NO>");
					response.append("<RESULT>-1</RESULT>");
				}else{
					response.append("<MIO_TX_NO>" + bankTrans.getBtMioTxNo() + "</MIO_TX_NO>");
					response.append("<RESULT>0</RESULT>");
				}
			} catch (Exception e) {
				this.logger.error(bean.getTransCode() + "处理失败！", e);
				response.append("<MIO_TX_NO></MIO_TX_NO>");
				response.append("<RESULT>-1</RESULT>");
			}
			response.append("</BANK_TRANS>");
		}
		response.append("</BANK_TRANS_LIST>");
		return addOutBody(response.toString());
	}

	/**
	 * 解析请求报文
	 * @param document
	 * @param oclk
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<ReceiveTransResultBean> readXml(Document document, OCLK oclk) {
		List<ReceiveTransResultBean> list = new ArrayList<ReceiveTransResultBean>();
		Element root = document.getRootElement().element("REQUEST");
		String obn = root.element("OCLK_BRANCH_NO").getTextTrim();
		String ocn = root.element("OCLK_CLERK_NO").getTextTrim();
		oclk.setOclkBranchNo(obn);
		oclk.setOclkClerkNo(ocn);
		Element imsbts = root.element("I_MIOS_SET_BANK_TRANS_SCXE");
		List<Element> btEts = imsbts.elements("BANK_TRANS");
		for (Element btEt : btEts) {
			String miosTransCode = btEt.element("MIOS_TRANS_CODE").getTextTrim();
			Long transCode = Long.valueOf(btEt.element("TRANS_CODE").getTextTrim());
			String transStat = btEt.element("TRANS_STAT").getTextTrim();
			list.add(new ReceiveTransResultBean(miosTransCode, transCode, transStat));
		}
		return list;
	}

	/**
	 * 封装返回报文
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

	/***
	 * 处理银行转账结果
	 * @param bean 请求报文
	 * @param oclk 操作员信息
	 * @param batNo 批次号
	 * @return
	 * @throws Exception
	 */
	@Transaction
	public BankTrans receiveBankTrans(ReceiveTransResultBean bean, OCLK oclk) throws Exception {
		//1.根据请求报文中的transCode获取到BankTrans表中对应的数据
		long transCode = bean.getTransCode();
		BankTrans bankTrans = bankTransDao.queryBankTransByTransCode(transCode);
		//查询出BankTrans中的应收ID，放到plnmioRecIds
		String plnmioRecIds = "";
		if (bankTrans.getPlnmioRecId() == 0L) {
			plnmioRecIds = bankTrans.getPlnmioRecIdList();
		} else {
			plnmioRecIds = ""+bankTrans.getPlnmioRecId();
		}
		
		long batNo = mioLogDao.getBatNo();
		//2. 判断是否转账成功
		Map<String,String> paramMap = new HashMap<>(); //修改应收数据Map
		//TRANSFER_SUCCESS("1", "转帐成功"),
		if (BANK_TRANS_RESULT.TRANSFER_SUCCESS.getKey().equals(bean.getTransStat())) {
			List<PlnmioRec> nowPlnmioList = plnmioRecDao.queryPlnmioRecList(plnmioRecIds);
			logger.info("【转账序号:"+transCode+"】对应的应收数据nowPlnmioList的长度是" +nowPlnmioList.size());
			List<MioLog> mioLogList = new ArrayList<MioLog>();
			for (PlnmioRec plnmioRec : nowPlnmioList) {
				MioLog mioLog = new MioLog();
				// 从应收以及转账中复制数据到实收
				infoMioLogData(mioLog, bankTrans, plnmioRec, oclk, bean.getTransCode());
				mioLog.setMioLogId(mioLogDao.getMioLogIdSeq());   //收付流水标识
				mioLog.setBatNo(batNo);
				mioLogList.add(mioLog);
			}
			//保存实收数据
			mioLogDao.insertMioLogList(mioLogList);
			//判断收费类型，mioClass 1：应收，-1：应付
			if(bankTrans.getMioClass() == 1){
				//生成实收数据后，根据新增的实收数据新增或修改对应的帐号信息
				addEarnestAcc(nowPlnmioList);
			}
			//将生成的实收数据转存到MonogDB的MioLog表中
		    doInsertMongoDBMioLog(mioLogList);
			logger.info("【转账序号:"+transCode+"】实收数据入库完成！" );
			//需要修改的应收数据字段
			paramMap.put("lock_flag", "0"); 	//锁状态[未锁定0]
			paramMap.put("trans_stat", TRAN_STATE.SUCCESS.getKey());	//转账状态[成功S]
			paramMap.put("proc_stat", PLNMIO_STATE.PAID.getKey());	//应收费状态[已实收S]
			paramMap.put("mio_proc_flag","0");	//是否收付处理标记[已处理]
			paramMap.put("hold_flag", "1"); 	//待转账标识[已转账1]
			paramMap.put("remark", ""); 	//备注
		} else {
			logger.info("【转账序号:"+transCode+"】收费平台转账失败！" );
			bankTrans = new BankTrans();
			//转账失败：需要修改的应收数据字段
			paramMap.put("lock_flag", "0"); 	//锁状态[未锁定0]
			paramMap.put("hold_flag", "0"); 	//待转账标识[待转账0]
			paramMap.put("trans_stat", TRAN_STATE.FAILURE.getKey());	//转账状态[失败F]
			String msg = BANK_TRANS_RESULT.valueOfKey(bean.getTransStat()).getDescription();
			if (msg == null || msg.isEmpty()) {
				msg = "未知错误！[" + bean.getTransStat() + "]";
			}
			paramMap.put("remark", msg);
			logger.info("【转账序号:"+transCode+"】转账失败后续处理完成！" );
		}	
		
		//重置应收数据状态
		plnmioRecDao.updatePlnmioRecByIds(plnmioRecIds, paramMap);
		//删除对应的银行转账数据
		bankTransDao.deleteBankTrans(transCode);
		//向实收付流转任务队列表插入数据
		plnmioCommonDao.insertMioInfoRoamTaskQueue(bankTrans.getMgrBranchNo(),batNo);
		return bankTrans;
	}

	/**
	 * 将实收数据插入MongoDB中的mioLog表中
	 * @param mioLogList
	 */
	private void doInsertMongoDBMioLog(List<MioLog> mioLogList) {
		for(MioLog miolog: mioLogList){
			//重新格式化日期，否则无法插入到MongoDB库中
			if(miolog.getPlnmioDate() != null){
				miolog.setPlnmioDate(new Date(miolog.getPlnmioDate().getTime()));
			}
			if(miolog.getMioDate() != null){
				miolog.setMioDate(new Date(miolog.getMioDate().getTime()));		
			}
			if(miolog.getMioLogUpdTime() != null){
				miolog.setMioLogUpdTime(new Date(miolog.getMioLogUpdTime().getTime()));
			}
			if(miolog.getPremDeadlineDate() != null){
				miolog.setPremDeadlineDate(new Date(miolog.getPremDeadlineDate().getTime()));
			}
			if(miolog.getCorrMioDate() != null){
				miolog.setCorrMioDate(new Date(miolog.getCorrMioDate().getTime()));
			}
			if(miolog.getFinPlnmioDate() != null){
				miolog.setFinPlnmioDate(new Date(miolog.getFinPlnmioDate().getTime()));
			}
			if(miolog.getPlnmioCreateTime() != null){
				miolog.setPlnmioCreateTime(new Date(miolog.getPlnmioCreateTime().getTime()));	
			}
		}
		mongoBaseDao.insertAll(mioLogList);
		
	}

	/**
	 * 新增或修改账户信息
	 * @param plnList 应收数据集合
	 */
	private void addEarnestAcc(List<PlnmioRec> plnList) {
		
		List<MioAccInfoLog> accList = new ArrayList<>(); 
		
		for(PlnmioRec pln:plnList){
			this.logger.info("【应收ID:"+pln.getPlnmioRecId()+"】开始修改应收数据对应的账户信息！");
			//帐号=投保单号(cntrNo)+帐号所属人类别(plnmioRecType【O:组织架构树应收,I:被保人应收,P:收费组产生应收】)+ LevelCode/IpsnNo/FeegrpNo
			String accNo = pln.getCntrNo()+pln.getMultiPayAccType();
			if("O".equals(pln.getMultiPayAccType())) {
				accNo += pln.getLevelCode();
			}else if("I".equals(pln.getMultiPayAccType())){
				accNo += pln.getIpsnNo();
			}else if("P".equals(pln.getMultiPayAccType())){
				accNo += pln.getFeeGrpNo();
			}
			//根据帐号查询账户信息表
			EarnestAccInfo bigEarAcc = earnestAccInfoDao.queryEarnestAccInfo(accNo);
			long accId=0L;
			if(bigEarAcc == null){
				bigEarAcc = new EarnestAccInfo();
				accId = earnestAccInfoDao.queryEarnestAccInfoAccId();
				bigEarAcc.setAccId(accId); 	//账户信息
				bigEarAcc.setAccNo(accNo);	// 帐号
				// 帐号所属人类别:O-组织架构树应收，I-被保人应收，P-收费组应收
				bigEarAcc.setAccType(pln.getMultiPayAccType()); 	
				if("O".equals(pln.getMultiPayAccType())) {
					bigEarAcc.setAccPersonNo(pln.getLevelCode());	//组织层次代码
				}else if("I".equals(pln.getMultiPayAccType())){
					bigEarAcc.setAccPersonNo(""+pln.getIpsnNo());	//被保人序号
				}else if("P".equals(pln.getMultiPayAccType())){
					bigEarAcc.setAccPersonNo(""+pln.getFeeGrpNo());	//收费组号
				}
				bigEarAcc.setBalance(pln.getAmnt()); 	//账户余额 ：默认0
				bigEarAcc.setFrozenBalance(new BigDecimal(0));	//冻结金额  默认0
				earnestAccInfoDao.insertEarnestAccInfo(bigEarAcc);
				this.logger.info("新增账户信息【"+accNo+"】：完成！");
			}else{
				//存在账户时
				accId=bigEarAcc.getAccId();
				BigDecimal sumJe= pln.getAmnt().add(bigEarAcc.getBalance()); 		// 账户余额:应收金额+账户余额
				earnestAccInfoDao.updateEarnestAccInfo(accId,sumJe);
				this.logger.info("修改账户信息【"+accNo+"】：完成！");
			}
			//账户对应操作轨迹集合
			MioAccInfoLog mioAccInfoLog = new MioAccInfoLog();
			long accLogId = mioAccInfoLogDao.queryAccLogId();
			mioAccInfoLog.setAccLogId(accLogId);		//账户明细标识
			mioAccInfoLog.setAccId(accId);				//账户标识
			mioAccInfoLog.setCreateTime(new Date());	//发生日期
			mioAccInfoLog.setMioItemCode(MIO_ITEM_CODE.FA.getKey());	//收付项目代码
			mioAccInfoLog.setMioClass(pln.getMioClass()); 				//收付类型： 1：收，-1：付
			mioAccInfoLog.setAnmt(pln.getAmnt());		//收付金额
			mioAccInfoLog.setRemark("");				//备注
			accList.add(mioAccInfoLog);
		}
		
		mioAccInfoLogDao.insertMioAccInfoLogList(accList);
	}

	/**
	 * 从应收以及转账中复制数据到实收
	 * @param mioLog 实收
	 * @param bankTrans 转账
	 * @param plnmioRec 应收
	 * @param oclk
	 */
	private void infoMioLogData(MioLog mioLog, BankTrans bankTrans, PlnmioRec plnmioRec, OCLK oclk, Long transCode) {
		mioLog.setPlnmioRecId(plnmioRec.getPlnmioRecId());	//应收付记录标识
		mioLog.setPolCode(plnmioRec.getPolCode());			//险种代码
		mioLog.setCntrType(plnmioRec.getCntrType());		//合同类型
		mioLog.setCgNo(plnmioRec.getCgNo());				//合同组号
		mioLog.setSgNo(plnmioRec.getSgNo());				//汇缴事件号
		mioLog.setCntrNo(plnmioRec.getCgNo());			//保单号/投保单号
		mioLog.setCurrencyCode(plnmioRec.getCurrencyCode());//币种		
		mioLog.setMtnId(plnmioRec.getMtnId());				//保全批改流水号/赔案号
		mioLog.setMtnItemCode(plnmioRec.getMtnItemCode());	//批改保全项目
		
		mioLog.setIpsnNo(plnmioRec.getIpsnNo());			//被保人序号
		mioLog.setLevelCode(plnmioRec.getLevelCode());		//组织层次代码
		mioLog.setFeeGrpNo(plnmioRec.getFeeGrpNo());		//收费组号
		mioLog.setMioCustNo(plnmioRec.getMioCustNo());		//领款人/交款人客户号
		mioLog.setMioCustName(plnmioRec.getMioCustName());	//领款人/交款人姓名
		mioLog.setPlnmioDate(plnmioRec.getPlnmioDate());	//应收付日期
		mioLog.setMioDate(new Date());						//实收付日期	
		mioLog.setMioLogUpdTime(new Date());				//写流水日期
		mioLog.setPremDeadlineDate(null); 					//保费缴费宽限截止日期
		mioLog.setMioItemCode(bankTrans.getMioItemCode());	//收付项目代码
		
		mioLog.setMioType(plnmioRec.getMioType());			//收付方式代码
		mioLog.setMgrBranchNo(plnmioRec.getMgrBranchNo());	//管理机构
		mioLog.setPclkBranchNo(oclk.getOclkBranchNo());		//操作员分支机构号
		mioLog.setPclkNo(oclk.getOclkClerkNo());			//操作员代码
		mioLog.setSalesBranchNo(plnmioRec.getSalesBranchNo());//销售机构号
		mioLog.setSalesChannel(plnmioRec.getSalesChannel());  //销售渠道
		mioLog.setSalesNo(plnmioRec.getSalesNo());			//销售员代码
		mioLog.setMioClass(plnmioRec.getMioClass());		//收付类型
		mioLog.setAmnt(plnmioRec.getAmnt());				//金额
		mioLog.setAccCustIdType(plnmioRec.getAccCustIdType());//帐户所有人证件类别
		
		mioLog.setAccCustIdNo(plnmioRec.getAccCustIdNo());    //帐户所有人证件号
		mioLog.setBankCode(plnmioRec.getBankCode()); 		  //银行代码
		mioLog.setBankAccName(plnmioRec.getBankAccName());    //帐户所有人名称
		mioLog.setBankAccNo(plnmioRec.getBankAccNo());        //银行帐号
		mioLog.setMioTxClass(0);    	//收付交易类型
		mioLog.setMioTxNo(0L);    		//收付交易号
		mioLog.setCorrMioDate(null);    //冲正业务日期
		mioLog.setCorrMioTxNo(0L);      //冲正收付交易号
		mioLog.setReceiptNo("");        //打印发票号
		mioLog.setVoucherNo("");        //核销凭证号
		
		mioLog.setFinPlnmioDate(null);  //财务应收付日期
		mioLog.setClearingMioTxNo("");  //清算交易流水号
		mioLog.setMioProcFlag(plnmioRec.getMioProcFlag());//是否收付处理标记
		mioLog.setRouterNo("0");    //路由号
		mioLog.setAccId(0L);        //关联帐户标识
		mioLog.setCoreStat("N");    //实收数据入账状态
		mioLog.setTransCode(transCode); //转账编号
		mioLog.setBtMioTxNo(0000001L);  //转账交易号
		mioLog.setBatNo(0L);        	//批次号
		mioLog.setRemark("银行转账");		//备注
		
		mioLog.setPlnmioCreateTime(plnmioRec.getPlnmioCreateTime());//生成应收记录时间
		mioLog.setNetIncome(0D);	//净收入		
		mioLog.setVat(0D);			//税
		mioLog.setVatId("");		//ID号，对应vat_flow
		mioLog.setVatRate(0D);		//税率
	}
}
