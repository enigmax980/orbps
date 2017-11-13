package com.newcore.orbps.service.business;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbps.models.finance.VatSplitInfo;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwIpsnPolResult;
import com.newcore.orbps.models.service.bo.udw.UdwIpsnResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.*;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.supports.dicts.*;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;

import java.math.BigDecimal;
import java.util.*;

/**
 * 转保费 Created by liushuaifeng on 2017/2/11 0011.
 */
public class PsMiologCreationTasklet implements Tasklet {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ProcMioInfoDao procMioInfoDao;

	@Autowired
	MioLogDao mioDao;

	@Autowired
	QueryAccInfoDao queryAccInfoDao;

	@Autowired
	MioAccInfoLogDao mioAccInfoLogDao;// 修改账户日志表

	@Autowired
	PlnmioRecDao plnmioRecDao;

	@Autowired
	PolNatureService polNatureService;

	private static final double MONEY = 0d;

	private static final String MTN_ITEM_CODE = "0";

	private static final String MIO_PROC_FLAG = "1";

	private static final String MTO_TYPE_NEW = "N";

	private static final Integer INTERGER_NUM_ZERO = 0;

	private static final long LONG_NUM = 0L;

	private static final Integer PRECISION_TWO = 2;
	// 财务拆分ID前缀
	private static final String VAT_ID_PREFIX = "SQ";
	// 契约平台系统号
	private static final String SYS_NO = "Q";

	private static Logger logger = LoggerFactory.getLogger(PsMiologCreationTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		String applNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters()
				.getString("applNo");

		// 根据保费来源产生应:【团单保费来源：
		// 1.团体账户付款(单位缴费产生一笔总应收)；2.个人账户付款(收费组产生应收)；3.团体个人共同付款(每个被保人产生一笔应收)。】
		GrpInsurAppl grpInsurAppl = procMioInfoDao.getGrpInsurAppl(applNo);
		if (null == grpInsurAppl) {
			throw new BusinessException(new Throwable("根据投保单号" + applNo + "查询保单基本信息失败."));
		}

		boolean fund = false;

		//组织报文
		List<String> polCodes = new ArrayList<>();
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			polCodes.add(policy.getPolCode());
		}
		//消息头设置
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		List<JSONObject> polNatureList=polNatureService.getPolNatureInfo(polCodes);
		for(JSONObject json:polNatureList){
			String isFund =json.getString("isFund");     //是否基金险
			if(YES_NO_FLAG.YES.getKey().equals(isFund)){
				fund = true;
			}
		}
		// bigMap存放合格被保人单位缴费总计金额
		Map<String, Double> polCodeAndPremiumMap = new HashMap<>();
		// 总保费
		BigDecimal sumPremium = BigDecimal.ZERO;
		List<MioLog> newMioLogList = new ArrayList<>();
		//判断是否为基金险			
		if(fund){//如果是基金险
			polCodeAndPremiumMap.put(grpInsurAppl.getFirPolCode(), grpInsurAppl.getApplState().getSumPremium());
			sumPremium = BigDecimal.valueOf(grpInsurAppl.getApplState().getSumPremium());
		}else{
			polCodeAndPremiumMap = getSumPolandPremiumWithIpsn(grpInsurAppl);
			for (String polcode : polCodeAndPremiumMap.keySet()) {
				sumPremium = sumPremium.add(BigDecimal.valueOf(polCodeAndPremiumMap.get(polcode)));
			}	
			sumPremium = BigDecimalUtils.keepPrecision(sumPremium,PRECISION_TWO);
		}

		double totalAmnt = sumPremium.doubleValue();// 把总保费留存一份，存储在批作业上下文中
		BigDecimal frozenBalance = BigDecimal.ZERO;
		// 此保单的支付方式非银行转账，无须转保费操作
		if (!ProcMioInfoUtils.isBankTrans(grpInsurAppl)) {
			frozenBalance = BigDecimal.valueOf(plnmioRecDao.getPlnmioRecAmnt(applNo));
		} else {
			frozenBalance = queryAccInfoDao.querySumFrozenBalanceEarnestAccInfoByApplNo(applNo);
		}
		logger.info("投保单[" + grpInsurAppl.getApplNo() + "]被保人，保单累计总保费：sumPremium = " + sumPremium);
		logger.info("投保单[" + grpInsurAppl.getApplNo() + "]冻结总金额：frozenBalance = " + frozenBalance);

		/* 如果是清汇多期暂缴 冻结金额是 总保费 乘 期数 */
		if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())
				&& StringUtils.equals(YES_NO_FLAG.YES.getKey(), grpInsurAppl.getPaymentInfo().getIsMultiPay())) {
			if (BigDecimalUtils.compareBigDecimal(frozenBalance,
					sumPremium.multiply(new BigDecimal(grpInsurAppl.getPaymentInfo().getMutipayTimes()))) != 0) {
				throw new BusinessException(
						new Throwable("applNo=" + applNo + ", 冻结金额总和[" + frozenBalance.doubleValue() + "]不等于总保费["
								+ sumPremium.doubleValue() * grpInsurAppl.getPaymentInfo().getMutipayTimes() + "]"));
			}
		} else {
			if (BigDecimalUtils.compareBigDecimal(frozenBalance, sumPremium) != 0) {
				throw new BusinessException(new Throwable(
						"applNo=" + applNo + ", 冻结金额总和[" + frozenBalance + "]不等于总保费[" + sumPremium + "]"));
			}
		}
		// 获取批次号
		long batNo = mioDao.getBatNo();

		// 如果是共保的单子，产生共保转保费记录
		if (!StringUtils.isEmpty(grpInsurAppl.getAgreementNo())) {

			Map<String, Object> comMap = new HashMap<>();
			comMap.put("agreementNo", grpInsurAppl.getAgreementNo());
			CommonAgreement commonAgreement = (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class, comMap);
			if (null == commonAgreement) {
				throw new BusinessException(
						new Throwable("根据共保协议号[" + grpInsurAppl.getAgreementNo() + "]查询共保返回为null."));
			}
			Integer com_count = 0;
			BigDecimal sumPayJe = BigDecimal.ZERO;
			for (ComCompany comCompany : commonAgreement.getComCompanies()) {
				com_count++;
				BigDecimal comComPayJe = BigDecimal.ZERO;
				// 共保比例百分比
				Double gbbl = comCompany.getCoinsurAmntPct() / 100;
				if (com_count == commonAgreement.getComCompanies().size()) {
					comComPayJe = sumPremium.subtract(sumPayJe);
				} else {
					// 共保公司应收金额:保单总金额*共保保费份额比例,保留两位小数
					comComPayJe = BigDecimalUtils.keepPrecision(sumPremium.multiply(new BigDecimal(gbbl.toString())),
							PRECISION_TWO);
				}

				// 2.1 承保人实收数据
				MioLog mioLog = initMioLogData(grpInsurAppl, batNo);
				mioLog.setMioLogId(mioDao.getMioLogIdSeq());
				mioLog.setPolCode(grpInsurAppl.getFirPolCode());
				mioLog.setMioItemCode(MIO_ITEM_CODE.FA.getKey()); // 收付项目代码:FA
				mioLog.setMioType(MIO_TYPE.S.getKey()); // 收付款方式代码:S
				mioLog.setMioClass(StringUtils.stringToInteger(MIO_CLASS.HANDLE.getKey())); // -1
				mioLog.setCntrNo(applNo + grpInsurAppl.getFirPolCode());
				sumPayJe = sumPayJe.add(comComPayJe);
				mioLog.setAmnt(BigDecimalUtils.keepPrecision(comComPayJe, PRECISION_TWO));

				mioLog.setRemark("暂收转保费"); // 备注：PS/S和FA/S 暂收转保费，GF/S
				// 暂收保费转应付参与共保人保费
				newMioLogList.add(mioLog);
				if (StringUtils.equals(COINSUR_TYPE.CHIEF_COIN.getKey(), comCompany.getCoinsurType())) {

					for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
						// 共保公司应收金额:保单总金额*共保保费份额比例
						BigDecimal comComPayJeTemp = BigDecimal.valueOf(polCodeAndPremiumMap.get(policy.getPolCode()))
								.multiply(new BigDecimal(gbbl.toString()));
						// 2.1 承保人实收数据
						MioLog mioLogTem = initMioLogData(grpInsurAppl, batNo);
						mioLogTem.setMioLogId(mioDao.getMioLogIdSeq());
						mioLogTem.setPolCode(policy.getPolCode());
						mioLogTem.setCntrNo(policy.getCntrNo());
						mioLogTem.setAmnt(BigDecimalUtils.keepPrecision(comComPayJeTemp, PRECISION_TWO));

						mioLogTem.setMioItemCode(MIO_ITEM_CODE.PS.getKey()); // 收付项目代码:
						// ps
						mioLogTem.setMioType(MIO_TYPE.S.getKey()); // 首付类型：S
						mioLogTem.setRemark("暂收转保费"); // 备注：PS/S和FA/S 暂收转保费，GF/S
						// 暂收保费转应付参与共保人保费

						// 共保财务拆分
						List<VatSplitInfo> vatSplitInfoList = new ArrayList<>();
						VatSplitInfo vatSplitInfo = new VatSplitInfo();
						vatSplitInfo.setVatId(VAT_ID_PREFIX + grpInsurAppl.getMgrBranchNo() + mioLogTem.getMioLogId());
						vatSplitInfo.setApplNo(grpInsurAppl.getApplNo());
						vatSplitInfo.setSysNo(SYS_NO);
						if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())
								|| StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())
								&& StringUtils.equals(grpInsurAppl.getSgType(), LIST_TYPE.GRP_SG.getKey())) {
							vatSplitInfo.setGrpName(grpInsurAppl.getGrpHolderInfo().getGrpName());
							vatSplitInfo.setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
							vatSplitInfo.setGrpIdNo(grpInsurAppl.getGrpHolderInfo().getGrpIdNo());
							vatSplitInfo.setContactMobile(grpInsurAppl.getGrpHolderInfo().getContactMobile());
							vatSplitInfo.setContactEmail(grpInsurAppl.getGrpHolderInfo().getContactEmail());
						} else {
							vatSplitInfo.setGrpName(grpInsurAppl.getPsnListHolderInfo().getSgName());
							vatSplitInfo.setGrpCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
							vatSplitInfo.setGrpIdNo(grpInsurAppl.getPsnListHolderInfo().getSgIdNo());
							vatSplitInfo.setContactMobile(grpInsurAppl.getPsnListHolderInfo().getSgMobile());
							vatSplitInfo.setContactEmail(grpInsurAppl.getPsnListHolderInfo().getSgEmail());
						}
						// 共保的财务拆分放的是总的保费
						vatSplitInfo.setPremium(polCodeAndPremiumMap.get(policy.getPolCode()).doubleValue());
						vatSplitInfoList.add(vatSplitInfo);
						mioLogTem.setVatSplitInfoList(vatSplitInfoList);

						newMioLogList.add(mioLogTem);
					}

				} else {
					MioLog mioLogTem = initMioLogData(grpInsurAppl, batNo);
					mioLogTem.setMioLogId(mioDao.getMioLogIdSeq());
					mioLogTem.setPolCode(grpInsurAppl.getFirPolCode());
					mioLogTem.setMioItemCode("GF"); // 收付项目代码:GF
					mioLogTem.setMioType(MIO_TYPE.S.getKey()); // 收付款方式代码:S
					mioLogTem.setAmnt(BigDecimalUtils.keepPrecision(comComPayJe, PRECISION_TWO));
					mioLogTem.setRemark("暂收保费转应付参与共保人保费"); // 备注：PS/S和FA/S
					// 暂收转保费，GF/S
					// 暂收保费转应付参与共保人保费
					newMioLogList.add(mioLogTem);

				}

			}

			// 插入mio_log表:-oracel、mongledb
			boolean insertMioLogListResult = mioDao.insertMioLogList(newMioLogList);
			if (!insertMioLogListResult) {
				throw new BusinessException(new Throwable("转保费数据插入oracel数据库失败."));
			}
			mongoBaseDao.insertAll(newMioLogList);
			// 批次号放入批作业上下文
			ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
					.getExecutionContext();
			jobExecutionContext.putLong("finLandBatNo", batNo);
			jobExecutionContext.putDouble("sumPremium", totalAmnt); // 增加总保费
			return RepeatStatus.FINISHED;
		}
		// 非共保单子
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {

			if (StringUtils.equals(policy.getMrCode(), MR_CODE.MASTER.getKey())) {
				MioLog mioLog = initMioLogData(grpInsurAppl, batNo);
				mioLog.setMioLogId(mioDao.getMioLogIdSeq());
				mioLog.setAmnt(BigDecimalUtils.keepPrecision(sumPremium, PRECISION_TWO));
				mioLog.setMioItemCode(MIO_ITEM_CODE.FA.getKey());
				mioLog.setMioType(MIO_TYPE.S.getKey());
				mioLog.setMioClass(StringUtils.stringToInteger(MIO_CLASS.HANDLE.getKey()));
				mioLog.setPolCode(policy.getPolCode()); // 险种代码
				mioLog.setCntrNo(applNo + policy.getPolCode());
				newMioLogList.add(mioLog);
			}
			MioLog mioLog = initMioLogData(grpInsurAppl, batNo);
			mioLog.setMioLogId(mioDao.getMioLogIdSeq());
			mioLog.setAmnt(BigDecimalUtils
					.keepPrecision(BigDecimal.valueOf(polCodeAndPremiumMap.get(policy.getPolCode())), PRECISION_TWO));
			mioLog.setPolCode(policy.getPolCode());
			mioLog.setCntrNo(policy.getCntrNo());
			newMioLogList.add(mioLog);
		}

		// 非组织架构树财务拆分
		if (null == grpInsurAppl.getOrgTreeList() || grpInsurAppl.getOrgTreeList().isEmpty()) {
			splitVatWithoutOrgtree(grpInsurAppl, newMioLogList);
		} else {// 组织架构树财务拆分
			splitVatWithOrgtree(grpInsurAppl, newMioLogList);
		}

		// 插入mio_log表:-oracel、mongledb
		boolean insertMioLogListResult = mioDao.insertMioLogList(newMioLogList);
		if (!insertMioLogListResult) {
			throw new BusinessException(new Throwable("转保费数据插入oracel数据库时失败."));
		}
		mongoBaseDao.insertAll(newMioLogList);

		// 批次号放入批作业上下文
		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();
		jobExecutionContext.putLong("finLandBatNo", batNo);
		jobExecutionContext.putDouble("sumPremium", totalAmnt); // 增加总保费

		return RepeatStatus.FINISHED;
	}

	/**
	 * 初始化实收数据：默认为PS/S,mioClass=1的数据
	 *
	 * @param grpInsurAppl
	 *            团单出单基本信息
	 * @return
	 */

	private MioLog initMioLogData(GrpInsurAppl grpInsurAppl, long batNo) {
		MioLog mioLog = new com.newcore.orbps.models.finance.MioLog();
		mioLog.setPlnmioRecId(LONG_NUM); // 应收付记录标识
		mioLog.setPolCode(""); // 险种代码
		mioLog.setCntrType(grpInsurAppl.getCntrType()); // 合同类型
		mioLog.setCgNo(grpInsurAppl.getCgNo()); // 合同组号
		mioLog.setSgNo(grpInsurAppl.getSgNo()); // 汇缴事件号
		mioLog.setCntrNo(grpInsurAppl.getApplNo()); // 保单号/帐号/投保单号
		mioLog.setCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode()); // 保单币种
		mioLog.setMtnId(LONG_NUM); // 保全批改流水号
		mioLog.setMtnItemCode(null); // 批改保全项目

		mioLog.setIpsnNo(LONG_NUM); // 被保人序号
		mioLog.setLevelCode(""); // 组织层次代码
		mioLog.setFeeGrpNo(LONG_NUM); // 收费组号
		if(null != grpInsurAppl.getGrpHolderInfo()){
			mioLog.setMioCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo()); // 领款人/交款人客户号
			mioLog.setMioCustName(grpInsurAppl.getGrpHolderInfo().getGrpName()); // 领款人/交款人姓名			
		}else {
			mioLog.setMioCustNo(""); // 领款人/交款人客户号
			mioLog.setMioCustName(""); // 领款人/交款人姓名
		}
		mioLog.setPlnmioDate(grpInsurAppl.getInForceDate()); // 应收付日期:团单生效日期
		mioLog.setMioDate(new Date()); // 实收付日期：当前系统日期
		mioLog.setMioLogUpdTime(new Date()); // 写流水时间(系统时间)
		mioLog.setPremDeadlineDate(ProcMioInfoUtils.getDate(60)); // 保费缴费宽限截止日期:当期系统日期+60天
		mioLog.setMioItemCode(MIO_ITEM_CODE.PS.getKey()); // 收付项目代码
		mioLog.setMioType(MIO_TYPE.S.getKey()); // 收付款方式代码
		mioLog.setMgrBranchNo(grpInsurAppl.getMgrBranchNo()); // 管理机构

		mioLog.setPclkBranchNo("!!!!!!"); // 操作员分支机构号
		mioLog.setPclkNo("########"); // 操作员代码

		SalesInfo salesInfo = grpInsurAppl.getSalesInfoList().get(0);
		if (YES_NO_FLAG.YES.getKey().equals(grpInsurAppl.getSalesDevelopFlag())) {
			for(int i =0 ;i< grpInsurAppl.getSalesInfoList().size();i++){
				if(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey().equals(grpInsurAppl.getSalesInfoList().get(i).getDevelMainFlag())){
					salesInfo = grpInsurAppl.getSalesInfoList().get(i);
					break;
				}
			}
			mioLog.setSalesBranchNo(salesInfo.getSalesBranchNo()); // 销售机构号
			mioLog.setSalesChannel(salesInfo.getSalesChannel()); // 销售渠道
			mioLog.setSalesNo(salesInfo.getSalesNo()); // 销售员号
		}else if(GRP_PRD_SALES_CHANNEL.OA.getKey().equals(salesInfo.getSalesChannel())){
			if(null != salesInfo.getSalesNo() && null != salesInfo.getSalesDeptNo()){
				mioLog.setSalesNo(salesInfo.getSalesNo());
				mioLog.setSalesChannel(GRP_PRD_SALES_CHANNEL.BS.getKey());
			}else if(null == salesInfo.getSalesNo() && null != salesInfo.getSalesDeptNo()){
				mioLog.setSalesNo(salesInfo.getSalesDeptNo());
				mioLog.setSalesChannel(GRP_PRD_SALES_CHANNEL.OA.getKey());
			}
			mioLog.setSalesBranchNo(salesInfo.getSalesBranchNo()); // 销售机构号
		}else {
			mioLog.setSalesBranchNo(salesInfo.getSalesBranchNo()); // 销售机构号
			mioLog.setSalesChannel(salesInfo.getSalesChannel()); // 销售渠道
			mioLog.setSalesNo(salesInfo.getSalesNo()); // 销售员号
		}
		mioLog.setMioClass(StringUtils.stringToInteger(MIO_CLASS.RECEIVABLES.getKey())); // 收付类型
		mioLog.setAmnt(BigDecimal.ZERO); // 金额:险种对应保费
		mioLog.setAccCustIdType(""); // 帐户所有人证件类别
		mioLog.setAccCustIdNo(""); // 帐户所有人证件号
		mioLog.setBankCode(""); // 银行代码

		mioLog.setBankAccName(""); // 帐户所有人名称
		mioLog.setBankAccNo(""); // 银行帐号
		mioLog.setMioTxClass(INTERGER_NUM_ZERO); // 收付交易类型
		mioLog.setCorrMioDate(null); // 冲正业务日期
		mioLog.setCorrMioTxNo(LONG_NUM); // 冲正收付交易号
		mioLog.setReceiptNo(""); // 打印发票号
		mioLog.setVoucherNo(""); // 核销凭证号
		mioLog.setFinPlnmioDate(null); // 财务应收付日期
		mioLog.setClearingMioTxNo(""); // 清算交易流水号

		mioLog.setMioProcFlag(MIO_PROC_FLAG);// 是否收付处理标记
		mioLog.setRouterNo(MTN_ITEM_CODE); // 路由号
		mioLog.setAccId(LONG_NUM); // 关联帐户标识
		mioLog.setCoreStat(MTO_TYPE_NEW); // 实收数据入账状态
		mioLog.setBatNo(batNo); // 批次号
		mioLog.setTransCode(LONG_NUM); // 转账编号
		mioLog.setBtMioTxNo(LONG_NUM); // 转账交易号
		mioLog.setRemark("暂收转保费");// 备注
		mioLog.setPlnmioCreateTime(new Date());// 生成应收记录时间
		mioLog.setNetIncome(MONEY);// 净收入

		mioLog.setVat(MONEY);// 税
		mioLog.setVatId("");// ID号，对应vat_flow
		mioLog.setVatRate(MONEY);// 税率
		return mioLog;
	}

	/**
	 * 获取每个险种对应的被保人累计总保费
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	public Map<String, Double> getSumPolandPremiumWithIpsn(GrpInsurAppl grpInsurAppl) {

		Map<String, Double> policyPremiumMap = new HashMap<>();

		// 获取加费信息
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("businessKey", grpInsurAppl.getApplNo());

		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
		if (null == applUdwResult) {
			throw new BusinessException(new Throwable("查询加费信息表失败"));
		}

		List<Policy> policyList = grpInsurAppl.getApplState().getPolicyList();

		// 如果是普通清单，则从被保人算
		if (StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())) {

			Criteria criteria = Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E");
			// 保额保费校验
			Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
					Aggregation.unwind("subStateList"),
					Aggregation.group("$subStateList.polCode").sum("subStateList.premium").as("sumPremium"));

			AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured",
					JSONObject.class);
			if (null == aggregate || aggregate.getMappedResults().isEmpty()) {
				throw new BusinessException(new Throwable("累计有效被保人保费失败"));
			}
			List<JSONObject> jsonObjects = aggregate.getMappedResults();
			for (JSONObject jsonObject : jsonObjects) {
				String polCode = jsonObject.getString("_id").substring(0, 3);
				policyPremiumMap.put(polCode,
						policyPremiumMap.containsKey(polCode)
						? (policyPremiumMap.get(polCode) + jsonObject.getDouble("sumPremium"))
								: jsonObject.getDouble("sumPremium"));

			}
			// 累计被保人加费
			if (null != applUdwResult.getUdwIpsnResults() && !applUdwResult.getUdwIpsnResults().isEmpty()) {
				for (UdwIpsnResult udwIpsnResult : applUdwResult.getUdwIpsnResults()) {
					for (UdwIpsnPolResult udwIpsnPolResult : udwIpsnResult.getUdwIpsnPolResults()) {
						String temPolCode = udwIpsnPolResult.getPolCode().substring(0, 3);
						policyPremiumMap.put(temPolCode,
								udwIpsnPolResult.getAddFeeAmnt() + policyPremiumMap.get(temPolCode));
					}
				}
			}

		} else {
			for (Policy policy : policyList) {
				policyPremiumMap.put(policy.getPolCode(), policy.getPremium());
			}

		}

		// 累计险种加费
		if (null != applUdwResult.getUdwPolResults() && !applUdwResult.getUdwPolResults().isEmpty()) {
			for (UdwPolResult udwPolResult : applUdwResult.getUdwPolResults()) {
				String temPolCode = udwPolResult.getPolCode().substring(0, 3);
				policyPremiumMap.put(temPolCode, udwPolResult.getAddFeeAmnt() + policyPremiumMap.get(temPolCode));
			}
		}

		if (null != grpInsurAppl.getHealthInsurInfo() && StringUtils.equals(
				grpInsurAppl.getHealthInsurInfo().getComInsurAmntType(), COMLNSUR_AMNT_TYPE.FIXED_INSURED.getKey())) {
			double tempComInsrPrem = null == grpInsurAppl.getHealthInsurInfo().getComInsrPrem() ? 0.00
					: grpInsurAppl.getHealthInsurInfo().getComInsrPrem();
			policyPremiumMap.put(grpInsurAppl.getFirPolCode(),
					policyPremiumMap.get(grpInsurAppl.getFirPolCode()) + tempComInsrPrem);
		}
		return policyPremiumMap;
	}

	/**
	 * 团个共同付款，获取每个险种对应的被保人中团体负担的保费部分
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	public Map<String, Double> getSumPolandPremiumWithGrp(GrpInsurAppl grpInsurAppl) {

		Map<String, Double> policyPremiumMap = new HashMap<>();

		// 计算被保人中每个险种团体付款额度
		Criteria criteria = Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E");
		// 保额保费校验
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
				Aggregation.unwind("subStateList"),
				Aggregation.project("subStateList.polCode")
				.andExpression("subStateList.premium * (grpPayAmount /(ipsnPayAmount + grpPayAmount))")
				.as("grpPremium"),
				Aggregation.group("polCode").sum("grpPremium").as("grpPremium"));

		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);
		List<JSONObject> jsonObjects = aggregate.getMappedResults();
		for (JSONObject jsonObject : jsonObjects) {
			String polCode = jsonObject.getString("_id").substring(0, 3);
			policyPremiumMap.put(polCode,
					policyPremiumMap.containsKey(polCode)
					? (policyPremiumMap.get(polCode) + jsonObject.getDouble("grpPremium"))
							: jsonObject.getDouble("grpPremium"));

		}
		// 获取加费信息
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("businessKey", grpInsurAppl.getApplNo());

		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
		if (null == applUdwResult) {
			throw new BusinessException(new Throwable("查询加费信息表失败"));
		}

		if (null == applUdwResult.getUdwPolResults() || applUdwResult.getUdwPolResults().isEmpty()) {
			return policyPremiumMap;
		}

		// 累计险种加费
		for (UdwPolResult udwPolResult : applUdwResult.getUdwPolResults()) {
			String temPolCode = udwPolResult.getPolCode().substring(0, 3);
			policyPremiumMap.put(temPolCode, udwPolResult.getAddFeeAmnt() + policyPremiumMap.get(temPolCode));
		}

		return policyPremiumMap;
	}

	/**
	 * 无组织架构树，财务拆分
	 *
	 * @param grpInsurAppl
	 * @param mioLogList
	 */
	public void splitVatWithoutOrgtree(GrpInsurAppl grpInsurAppl, List<MioLog> mioLogList) {

		for (MioLog mioLog : mioLogList) {
			// 只考虑PS拆分
			if (StringUtils.equals(mioLog.getMioItemCode(), MIO_ITEM_CODE.PS.getKey())) {
				List<VatSplitInfo> vatSplitInfoList = new ArrayList<>();
				VatSplitInfo vatSplitInfo = new VatSplitInfo();
				vatSplitInfo.setVatId(VAT_ID_PREFIX + grpInsurAppl.getMgrBranchNo() + mioLog.getMioLogId());
				vatSplitInfo.setApplNo(grpInsurAppl.getApplNo());
				vatSplitInfo.setSysNo(SYS_NO);
				if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())
						|| StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())
						&& StringUtils.equals(grpInsurAppl.getSgType(), LIST_TYPE.GRP_SG.getKey())) {
					vatSplitInfo.setGrpName(grpInsurAppl.getGrpHolderInfo().getGrpName());
					vatSplitInfo.setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
					vatSplitInfo.setGrpIdNo(grpInsurAppl.getGrpHolderInfo().getGrpIdNo());
					vatSplitInfo.setContactMobile(grpInsurAppl.getGrpHolderInfo().getContactMobile());
					vatSplitInfo.setContactEmail(grpInsurAppl.getGrpHolderInfo().getContactEmail());
				} else {
					vatSplitInfo.setGrpName(grpInsurAppl.getPsnListHolderInfo().getSgName());
					vatSplitInfo.setGrpCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
					vatSplitInfo.setGrpIdNo(grpInsurAppl.getPsnListHolderInfo().getSgIdNo());
					vatSplitInfo.setContactMobile(grpInsurAppl.getPsnListHolderInfo().getSgMobile());
					vatSplitInfo.setContactEmail(grpInsurAppl.getPsnListHolderInfo().getSgEmail());
				}
				vatSplitInfo.setPremium(
						BigDecimalUtils.keepPrecision(mioLog.getAmnt().doubleValue(), PRECISION_TWO).doubleValue());
				vatSplitInfoList.add(vatSplitInfo);
				mioLog.setVatSplitInfoList(vatSplitInfoList);
			}

		}

	}

	/**
	 * 存在组织架构树，财务拆分,(存在组织架构树，则只支持团体交费、团个共同交费) 1. 需考虑险种加费，被保人加费；
	 *
	 * @param grpInsurAppl
	 * @param mioLogList
	 */
	public void splitVatWithOrgtree(GrpInsurAppl grpInsurAppl, List<MioLog> mioLogList) {
		// if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(),
		// PREM_SOURCE.PSN_ACC_PAY.getKey())) {
		// return;
		// }
		Map<String, Map<String, Double>> paidNodeMap = new HashMap<>();
		Map<String, Map<String, Double>> unPaidNodeMap = new HashMap<>();
		List<String> paidNodeList = new ArrayList<>(); // 交费节点层次代码集合
		List<String> unPaidNodeList = new ArrayList<>(); // 非交费节点层次戴拿集合
		Map<String, String> unPaidNodeRelMap = new HashMap<>(); // 非交费节点以及上级交费节点集合

		for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
			if (StringUtils.equals(orgTree.getIsPaid(), YES_NO_FLAG.YES.getKey())) {
				paidNodeList.add(orgTree.getLevelCode());
			} else { // 如果非交费节点是根节点，且存在被保人，则该节点一定是交费节点
				unPaidNodeList.add(orgTree.getLevelCode());
				if (StringUtils.equals(orgTree.getIsRoot(), YES_NO_FLAG.NO.getKey())) {
					unPaidNodeRelMap.put(orgTree.getLevelCode(), orgTree.getPrioLevelCode());
				} else {
					unPaidNodeRelMap.put(orgTree.getLevelCode(), orgTree
							.getPrioLevelCode());/* 根节点的上级层级代码应该是"#" 防止递归死循环 */
				}
			}
		}
		// 不存在交费节点，则按照无组织架构树拆分
		if (paidNodeList.isEmpty()) {
			splitVatWithoutOrgtree(grpInsurAppl, mioLogList);
			return;
		}

		// 后去核保结论
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("businessKey", grpInsurAppl.getApplNo());

		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, queryMap);
		if (null == applUdwResult) {
			throw new BusinessException(new Throwable("查询加费信息表失败"));
		}

		// 如果团体账户付款
		if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())) {
			// 交费节点的计算，交费节点对应险种保费累计
			for (String levelCode : paidNodeList) {
				// 计算被保人中对应节点的险种-保费累计
				Criteria criteria = Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E")
						.and("levelCode").is(levelCode);
				// 保额保费校验
				Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
						Aggregation.unwind("subStateList"),
						Aggregation.group("$subStateList.polCode").sum("subStateList.premium").as("sumPremium"));

				AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured",
						JSONObject.class);
				List<JSONObject> jsonObjects = aggregate.getMappedResults();
				Map<String, Double> policyPremiumMap = new HashMap<>();
				for (JSONObject jsonObject : jsonObjects) {
					String polCode = jsonObject.getString("_id").substring(0, 3);
					policyPremiumMap.put(polCode,
							policyPremiumMap.containsKey(polCode)
							? (policyPremiumMap.get(polCode) + jsonObject.getDouble("sumPremium"))
									: jsonObject.getDouble("sumPremium"));
				}
				paidNodeMap.put(levelCode, policyPremiumMap);
			}
			// 非交费节点的险种累计
			for (String levelCode : unPaidNodeList) {
				// 计算被保人中对应节点的险种-保费累计
				Criteria criteria = Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E")
						.and("levelCode").is(levelCode);
				// 保额保费校验
				Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
						Aggregation.unwind("subStateList"),
						Aggregation.group("$subStateList.polCode").sum("subStateList.premium").as("sumPremium"));

				AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured",
						JSONObject.class);
				List<JSONObject> jsonObjects = aggregate.getMappedResults();
				Map<String, Double> policyPremiumMap = new HashMap<>();
				for (JSONObject jsonObject : jsonObjects) {
					String polCode = jsonObject.getString("_id").substring(0, 3);
					policyPremiumMap.put(polCode,
							policyPremiumMap.containsKey(polCode)
							? (policyPremiumMap.get(polCode) + jsonObject.getDouble("sumPremium"))
									: jsonObject.getDouble("sumPremium"));
				}
				unPaidNodeMap.put(levelCode, policyPremiumMap);
			}

			// 存在被保人加费,根据被保人编号，获取被保人层次代码，把加费险种累计对应险种的保费中
			if (null != applUdwResult.getUdwIpsnResults() && !applUdwResult.getUdwIpsnResults().isEmpty()) {
				for (UdwIpsnResult udwIpsnResult : applUdwResult.getUdwIpsnResults()) {
					queryMap.put("applNo", grpInsurAppl.getApplNo());
					queryMap.put("ipsnNo", udwIpsnResult.getIpsnNo());
					GrpInsured grpInsured = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, queryMap);
					// 如果该被保人是在交费节点
					if (paidNodeList.contains(grpInsured.getLevelCode())) {
						Map<String, Double> onePaiidNodeMap = paidNodeMap.get(grpInsured.getLevelCode());
						for (UdwIpsnPolResult udwIpsnPolResult : udwIpsnResult.getUdwIpsnPolResults()) {
							String temPolCode = udwIpsnPolResult.getPolCode().substring(0, 3);
							onePaiidNodeMap.put(temPolCode,
									udwIpsnPolResult.getAddFeeAmnt() + onePaiidNodeMap.get(temPolCode));
						}

					} else {
						Map<String, Double> oneUnPaidNodeMap = unPaidNodeMap.get(grpInsured.getLevelCode());
						for (UdwIpsnPolResult udwIpsnPolResult : udwIpsnResult.getUdwIpsnPolResults()) {
							String temPolCode = udwIpsnPolResult.getPolCode().substring(0, 3);
							oneUnPaidNodeMap.put(temPolCode,
									udwIpsnPolResult.getAddFeeAmnt() + oneUnPaidNodeMap.get(temPolCode));
						}

					}
				}
			}

			// 把非交费节点中的被保人归并到交费节点中
			for (String levelCode : unPaidNodeMap.keySet()) { // 只关心存在被保人的非交费节点
				// 获取对应的交费父节点
				String paidCodeLevelFather = getfatherPaidNodeLevel(levelCode, unPaidNodeRelMap, paidNodeList);
				// 把该非交费节点中的被保人归并到父交费节点中
				Map<String, Double> unPaidPolAmntMap = unPaidNodeMap.get(levelCode);
				Map<String, Double> paidPolAmntMap = paidNodeMap.get(paidCodeLevelFather);

				for (String polcode : unPaidPolAmntMap.keySet()) {
					if (null != paidPolAmntMap && paidPolAmntMap.keySet().contains(polcode)) {
						paidPolAmntMap.put(polcode, paidPolAmntMap.get(polcode) + unPaidPolAmntMap.get(polcode));
					} else if (null != paidPolAmntMap) {
						paidPolAmntMap.put(polcode, unPaidPolAmntMap.get(polcode));
					}
				}
			}

			// 累计险种加费到各个节点
			// 累计险种加费
			if ((null != applUdwResult.getUdwPolResults()) && !applUdwResult.getUdwPolResults().isEmpty()) {
				for (UdwPolResult udwPolResult : applUdwResult.getUdwPolResults()) {
					String temPolCode = udwPolResult.getPolCode().substring(0, 3);
					Double polSumAmnt = 0D;
					for (MioLog mioLog : mioLogList) {
						if (StringUtils.equals(mioLog.getPolCode(), temPolCode)) {
							polSumAmnt = mioLog.getAmnt().doubleValue();
						}
					}

					int i = 0;
					Double sumAddAmnt = 0D; // 避免几分钱的差异
					for (String levelCode : paidNodeMap.keySet()) {
						Map<String, Double> paidPolAmntMap = paidNodeMap.get(levelCode);
						i++;
						if (paidPolAmntMap.keySet().contains(temPolCode)) {
							// 计算险种分配系数：该节点对应险种的保费/该险种总的保费
							Double ratio = paidPolAmntMap.get(temPolCode) / polSumAmnt;
							if (i == paidNodeMap.keySet().size()) {
								paidPolAmntMap.put(temPolCode,
										paidPolAmntMap.get(temPolCode) + udwPolResult.getAddFeeAmnt() - sumAddAmnt);
							} else {
								Double addAmnt = BigDecimalUtils
										.keepPrecision(udwPolResult.getAddFeeAmnt() * ratio, PRECISION_TWO)
										.doubleValue();
								sumAddAmnt += addAmnt;
								paidPolAmntMap.put(temPolCode, paidPolAmntMap.get(temPolCode) + addAmnt);
							}

						}
					}

				}
			}

		} else { // 团个共同付款--不考虑个人加费情况

			// 交费节点的计算，交费节点对应险种保费累计
			for (String levelCode : paidNodeList) {
				// 计算被保人中对应节点的险种-保费累计
				Criteria criteria = Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E")
						.and("levelCode").is(levelCode);
				// 聚合-获取该节点中团体交费部分
				Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
						Aggregation.unwind("subStateList"),
						Aggregation.project("subStateList.polCode")
						.andExpression("subStateList.premium * (grpPayAmount /(ipsnPayAmount + grpPayAmount))")
						.as("grpPremium"),
						Aggregation.group("polCode").sum("grpPremium").as("grpPremium"));

				AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured",
						JSONObject.class);
				List<JSONObject> jsonObjects = aggregate.getMappedResults();
				Map<String, Double> policyPremiumMap = new HashMap<>();
				for (JSONObject jsonObject : jsonObjects) {
					String polCode = jsonObject.getString("_id").substring(0, 3);
					policyPremiumMap.put(polCode,
							policyPremiumMap.containsKey(polCode)
							? (policyPremiumMap.get(polCode) + jsonObject.getDouble("grpPremium"))
									: jsonObject.getDouble("grpPremium"));
				}
				paidNodeMap.put(levelCode, policyPremiumMap);
			}
			// 非交费节点的险种累计
			for (String levelCode : unPaidNodeList) {
				// 计算被保人中对应节点的险种-保费累计
				Criteria criteria = Criteria.where("applNo").is(grpInsurAppl.getApplNo()).and("procStat").is("E")
						.and("levelCode").is(levelCode);
				// 聚合-获取该节点中团体交费部分
				Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
						Aggregation.unwind("subStateList"),
						Aggregation.project("subStateList.polCode")
						.andExpression("subStateList.premium * (grpPayAmount /(ipsnPayAmount + grpPayAmount))")
						.as("grpPremium"),
						Aggregation.group("polCode").sum("grpPremium").as("grpPremium"));

				AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured",
						JSONObject.class);
				List<JSONObject> jsonObjects = aggregate.getMappedResults();
				Map<String, Double> policyPremiumMap = new HashMap<>();
				for (JSONObject jsonObject : jsonObjects) {
					String polCode = jsonObject.getString("_id").substring(0, 3);
					policyPremiumMap.put(polCode,
							policyPremiumMap.containsKey(polCode)
							? (policyPremiumMap.get(polCode) + jsonObject.getDouble("grpPremium"))
									: jsonObject.getDouble("grpPremium"));
				}
				unPaidNodeMap.put(levelCode, policyPremiumMap);
			}

			// 把非交费节点中的被保人归并到交费节点中
			for (String levelCode : unPaidNodeMap.keySet()) { // 只关心存在被保人的非交费节点
				// 获取对应的交费父节点
				String paidCodeLevelFather = getfatherPaidNodeLevel(levelCode, unPaidNodeRelMap, paidNodeList);
				// 把该非交费节点中的被保人归并到父交费节点中
				Map<String, Double> unPaidPolAmntMap = unPaidNodeMap.get(levelCode);
				Map<String, Double> paidPolAmntMap = paidNodeMap.get(paidCodeLevelFather);

				for (String polcode : unPaidPolAmntMap.keySet()) {
					if (null != paidPolAmntMap && paidPolAmntMap.keySet().contains(polcode)) {
						paidPolAmntMap.put(polcode, paidPolAmntMap.get(polcode) + unPaidPolAmntMap.get(polcode));
					} else if (null != paidPolAmntMap) {
						paidPolAmntMap.put(polcode, unPaidPolAmntMap.get(polcode));
					}
				}
			}

			// 累计险种加费到各个节点
			// 累计险种加费
			if ((applUdwResult.getUdwPolResults() != null) && !applUdwResult.getUdwPolResults().isEmpty()) {
				for (UdwPolResult udwPolResult : applUdwResult.getUdwPolResults()) {
					String temPolCode = udwPolResult.getPolCode().substring(0, 3);
					Double polSumAmnt = 0D;
					for (MioLog mioLog : mioLogList) {
						if (StringUtils.equals(mioLog.getPolCode(), temPolCode)) {
							polSumAmnt = mioLog.getAmnt().doubleValue();
						}
					}

					int i = 0;
					Double sumAddAmnt = 0D; // 避免几分钱的差异
					for (String levelCode : paidNodeMap.keySet()) {
						Map<String, Double> paidPolAmntMap = paidNodeMap.get(levelCode);
						i++;
						if (paidPolAmntMap.keySet().contains(temPolCode)) {
							// 计算险种分配系数：该节点对应险种的保费/该险种总的保费
							Double ratio = paidPolAmntMap.get(temPolCode) / polSumAmnt;
							if (i == paidNodeMap.keySet().size()) {
								paidPolAmntMap.put(temPolCode,
										paidPolAmntMap.get(temPolCode) + udwPolResult.getAddFeeAmnt() - sumAddAmnt);
							} else {
								Double addAmnt = BigDecimalUtils
										.keepPrecision(udwPolResult.getAddFeeAmnt() * ratio, PRECISION_TWO)
										.doubleValue();
								sumAddAmnt += addAmnt;
								paidPolAmntMap.put(temPolCode, paidPolAmntMap.get(temPolCode) + addAmnt);
							}

						}
					}
				}
			}
		}

		// 每个险种对应的层次保费
		Map<String, Map<String, Double>> polLevelAmntMap = new HashMap<>();
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			Map<String, Double> levelAmnt = new HashMap<>();
			for (String level : paidNodeMap.keySet()) {
				if (paidNodeMap.get(level).keySet().contains(policy.getPolCode())) {
					levelAmnt.put(level, paidNodeMap.get(level).get(policy.getPolCode()));
					polLevelAmntMap.put(policy.getPolCode(), levelAmnt);
				}
			}
		}
		// 组织根据层次代码找到组织层次节点
		Map<String, OrgTree> levelOrgTreeMap = findLevelAndOrgTrees(grpInsurAppl.getOrgTreeList());

		// 进行财务拆分(如果是组织架构树的话，一定是多个交费节点)
		for (MioLog mioLog : mioLogList) {
			// 只考虑PS拆分
			if (StringUtils.equals(mioLog.getMioItemCode(), MIO_ITEM_CODE.PS.getKey())) {
				if (polLevelAmntMap.keySet().contains(mioLog.getPolCode())) {
					Map<String, Double> levelAmntMap = polLevelAmntMap.get(mioLog.getPolCode());
					int i = 0;
					Double sumAmnt = 0D;
					List<VatSplitInfo> vatSplitInfoList = new ArrayList<>();
					for (String level : levelAmntMap.keySet()) {
						i++;
						VatSplitInfo vatSplitInfo = new VatSplitInfo();
						vatSplitInfo.setVatId(VAT_ID_PREFIX + grpInsurAppl.getMgrBranchNo() + mioLog.getMioLogId()
						+ getVatSpitSeq(i));
						vatSplitInfo.setApplNo(grpInsurAppl.getApplNo());
						vatSplitInfo.setSysNo("Q");
						vatSplitInfo.setGrpName(levelOrgTreeMap.get(level).getGrpHolderInfo().getGrpName());
						vatSplitInfo.setGrpCustNo(level);
						vatSplitInfo.setGrpIdNo(levelOrgTreeMap.get(level).getGrpHolderInfo().getGrpIdNo());
						vatSplitInfo.setContactMobile(levelOrgTreeMap.get(level).getGrpHolderInfo().getContactMobile());
						vatSplitInfo.setContactEmail(levelOrgTreeMap.get(level).getGrpHolderInfo().getContactEmail());
						// vatSplitInfo.setPremium(BigDecimalUtils.keepPrecision(levelAmntMap.get(level),
						// PRECISION_TWO).doubleValue());

						if (i == levelAmntMap.keySet().size() && StringUtils.equals(
								grpInsurAppl.getPaymentInfo().getPremSource(), PREM_SOURCE.GRP_ACC_PAY.getKey())) {
							vatSplitInfo.setPremium(mioLog.getAmnt().doubleValue() - sumAmnt);
						} else {
							vatSplitInfo.setPremium(BigDecimalUtils
									.keepPrecision(levelAmntMap.get(level), PRECISION_TWO).doubleValue());
						}

						sumAmnt += BigDecimalUtils.keepPrecision(levelAmntMap.get(level), PRECISION_TWO).doubleValue();

						vatSplitInfoList.add(vatSplitInfo);
					}
					i++;// 循环外++
					// 如果团个共同付款则需要拆分，如果存余额，则需要继续进行拆分
					if (StringUtils.equals(grpInsurAppl.getPaymentInfo().getPremSource(),
							PREM_SOURCE.GRP_INDIVI_COPAY.getKey())) {
						if (mioLog.getAmnt().compareTo(new BigDecimal(sumAmnt)) > 0) { // 如果流水里存在余额
							VatSplitInfo vatSplitInfo = new VatSplitInfo();
							vatSplitInfo.setVatId(VAT_ID_PREFIX + grpInsurAppl.getMgrBranchNo() + mioLog.getMioLogId()
							+ getVatSpitSeq(i));
							vatSplitInfo.setApplNo(grpInsurAppl.getApplNo());
							vatSplitInfo.setSysNo("Q");
							if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())
									|| StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())
									&& StringUtils.equals(grpInsurAppl.getSgType(),
											LIST_TYPE.GRP_SG.getKey())) {
								vatSplitInfo.setGrpName(grpInsurAppl.getGrpHolderInfo().getGrpName());
								vatSplitInfo.setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
								vatSplitInfo.setGrpIdNo(grpInsurAppl.getGrpHolderInfo().getGrpIdNo());
								vatSplitInfo.setContactMobile(grpInsurAppl.getGrpHolderInfo().getContactMobile());
								vatSplitInfo.setContactEmail(grpInsurAppl.getGrpHolderInfo().getContactEmail());
							} else {
								vatSplitInfo.setGrpName(grpInsurAppl.getPsnListHolderInfo().getSgName());
								vatSplitInfo.setGrpCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
								vatSplitInfo.setGrpIdNo(grpInsurAppl.getPsnListHolderInfo().getSgIdNo());
								vatSplitInfo.setContactMobile(grpInsurAppl.getPsnListHolderInfo().getSgMobile());
								vatSplitInfo.setContactEmail(grpInsurAppl.getPsnListHolderInfo().getSgEmail());
							}
							vatSplitInfo.setPremium(mioLog.getAmnt().doubleValue() - sumAmnt);
							vatSplitInfoList.add(vatSplitInfo);
						}
					}
					mioLog.setVatSplitInfoList(vatSplitInfoList);
				}

			}
		}

	}

	/**
	 * 根据该节点找到相邻的交费父节点
	 *
	 * @param sourceLevel
	 *            层次代码
	 * @param unPaidNoderelMap
	 *            子、父关系map
	 * @param paidNodeList
	 *            交费节点集合
	 * @return 相邻父交费节点
	 */
	public String getfatherPaidNodeLevel(String sourceLevel, Map<String, String> unPaidNoderelMap,
			List<String> paidNodeList) {
		String tagetLevel = unPaidNoderelMap.get(sourceLevel);
		if (StringUtils.equals("#", tagetLevel) || paidNodeList.contains(tagetLevel)) {
			return tagetLevel;
		}
		return getfatherPaidNodeLevel(tagetLevel, unPaidNoderelMap, paidNodeList);
	}

	/**
	 * 根据组织架构树的层级序号，获得vat拆分的id
	 *
	 * @param seq
	 * @return
	 */
	public String getVatSpitSeq(Integer seq) {

		String seqStr = seq.toString();
		if (seqStr.length() == 1) {
			return "B00" + seqStr;

		} else if (seqStr.length() == 2) {
			return "B0" + seqStr;
		} else {
			return seqStr;
		}

	}

	public Map<String, OrgTree> findLevelAndOrgTrees(List<OrgTree> orgTreeList) {
		Map<String, OrgTree> levelOrgTreeMap = new HashMap<>();
		for (OrgTree orgTree : orgTreeList) {
			levelOrgTreeMap.put(orgTree.getLevelCode(), orgTree);
		}

		return levelOrgTreeMap;
	}

}
