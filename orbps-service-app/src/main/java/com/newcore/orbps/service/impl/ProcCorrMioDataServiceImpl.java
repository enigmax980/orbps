package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcPSInfoUtil;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.CntrBackInBO;
import com.newcore.orbps.models.pcms.bo.CntrBackOutBO;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.taskprmy.TaskType;
import com.newcore.orbps.service.api.InsurApplOperUtils;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.orbps.service.api.ProcCorrMioDataService;
import com.newcore.orbps.service.pcms.api.GrpCntrListBackService;
import com.newcore.orbpsutils.dao.api.MioLogDao;
import com.newcore.orbpsutils.dao.api.OldPrintTaskQueueDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.dao.api.QueryAccInfoDao;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MIO_TYPE;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
/**
 * 关于订正、撤销以及冲正处理服务实现类
 * @author JCC
 * updater LJF
 * 2016年10月27日 09:50:26
 */
@Service("procCorrMioDataService")
public class ProcCorrMioDataServiceImpl implements ProcCorrMioDataService {

	/**
	 * 日志管理工具实例.
	 */
	private Logger logger = LoggerFactory.getLogger(ProcCorrMioDataServiceImpl.class);

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MongoTemplate mongoTemplate;

	//回退、订正、要约撤销接口
	@Autowired 
	GrpCntrListBackService grpCntrListBackService;

	//保单状态
	@Autowired 
	InsurApplOperUtils insurApplLandUtils;

	@Autowired 
	PlnmioRecDao plnmioRecDao;

	@Autowired 
	MioLogDao mioDao;

	@Autowired 
	QueryAccInfoDao queryAccInfoDao;

	@Autowired 
	ProcPSInfoUtil procPSInfoUtil;

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Autowired 
	TaskPrmyDao taskPrmyDao;

	@Autowired
	PolNatureService polNatureService;

	@Autowired
	OldPrintTaskQueueDao oldPrintTaskQueueDao;

	private static final String YSE_FLAG = "1";

	private static final String NO_FLAG = "0";

	private static final String RET_CODE_ONE ="1";

	private static final String RET_CODE_TWO ="2";

	private static final String RET_CODE_THREE ="3";

	private static final String MTN_ITEM_CODE ="29";

	private static final String MIO_ITEM_CODE_AND_MIO_TYPE_FAS ="FAS";

	private static final String MIO_ITEM_CODE_AND_MIO_TYPE_PSS ="PSS";


	/**
	 * 处理投保单订正过程中实收数据冲正流水
	 * @param applNo 投保单号
	 */
	@Override
	public RetInfo setCorrInsurInfo(String applNo,String pclkBranchNo,String pclkNo ,String isProcMio) {

		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(applNo);

		//根据投保单号查询是否有在途应收付记录
		boolean lockFlag = plnmioRecDao.getLockFlagByApplNo(applNo);
		if(lockFlag){
			retInfo.setErrMsg("保单状态为[转账在途]不支持[回退]或[撤销]!");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}

		Map<String,Object> maps = new HashMap<>();
		maps.put("applNo", applNo);
		//保单基本信息
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, maps);
		if(null == grpInsurAppl){
			throw new BusinessException(new Throwable("根据投保单号["+applNo+"]查询保单基本信息失败."));
		}

		//查询轨迹，获得保单当前状态
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, maps);
		if(null == insurApplOperTrace){
			throw new BusinessException(new Throwable("根据投保单号["+applNo+"]查询轨迹基本信息失败."));
		}

		if(!org.apache.commons.lang3.StringUtils.isBlank(grpInsurAppl.getAgreementNo())){
			retInfo.setErrMsg("保单类型为[共保]不支持[回退]或[撤销]!");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}

		//获得该节点下的 当前操作节点
		String newApplState = insurApplOperTrace.getCurrentTraceNode().getProcStat();
		//获取最新轨迹，将不支持[回退][撤销]的直接返回失败。
		if (StringUtils.equals(NEW_APPL_STATE.INSURANCE_COVERAGE.getKey(), newApplState)) {
			retInfo.setErrMsg("保单状态为[拒保]不支持[回退]或[撤销]!");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}else if (StringUtils.equals(NEW_APPL_STATE.OFFER_WITHDRAWN.getKey(), newApplState)) {
			retInfo.setErrMsg("保单状态为[撤销]不支持[回退]或[撤销]！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}else if (StringUtils.equals(NEW_APPL_STATE.RECEIPT_VERIFICA.getKey(), newApplState)) {
			retInfo.setErrMsg("保单状态为[回执核销]不支持[回退]或[撤销]！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}else if (StringUtils.equals(NEW_APPL_STATE.EXTENSION.getKey(), newApplState)) {
			retInfo.setErrMsg("保单状态为[延期]不支持[回退]或[撤销]！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}

		//判断是否打印在途（老打印）
		String printLang = oldPrintTaskQueueDao.checkPrintIsOnLanging(applNo);
		if(StringUtils.equals("1", printLang)){
			retInfo.setErrMsg("保单[纸质打印在途 ]不支持[回退]或[撤销]!");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}

		//判断保单状态
		RetInfo retInfoLand  =	insurApplLandUtils.getIsInsurApplLanding(applNo);
		if (StringUtils.equals(RET_CODE_ONE, retInfoLand.getRetCode())) {//未生效，直接返回成功
			retInfo.setRetCode(YSE_FLAG);
			retInfo.setErrMsg("保单[回退]或[撤销]成功!");
			return retInfo;
		}else if (StringUtils.equals(RET_CODE_TWO, retInfoLand.getRetCode())) {//生效、落地在途,不支持回退
			retInfo.setErrMsg("保单状态为[生效落地在途]不支持[回退]或[撤销]！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}else{//已落地，如果是基金险，则在保单生效之后就不能进行订正回退,其他需要调用保单辅助--回退服务


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
					retInfo.setErrMsg("保单为[基金险]并且已生效，不支持[回退]或[撤销]！");
					retInfo.setRetCode(NO_FLAG);
					return retInfo;
				}
			}

			//组织参数
			List<String> polCodeList = new ArrayList<>();
			for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
				polCodeList.add(policy.getPolCode());
			}
			//封装参数
			CntrBackInBO cntrBackInBO = new CntrBackInBO();
			cntrBackInBO.setApplNo(applNo);//必填
			cntrBackInBO.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());//必填
			cntrBackInBO.setPolCodeList(polCodeList);//必填
			cntrBackInBO.setIsProcMioFlag(isProcMio);//必填
			cntrBackInBO.setAmnt(mioDao.getMioLogSumAmnt(-1, MIO_ITEM_CODE.FA.getKey() ,MIO_TYPE.S.getKey(), applNo ,0));//总转保费金额
			cntrBackInBO.setPolCode(grpInsurAppl.getFirPolCode());
			cntrBackInBO.setBackClerkNo(pclkNo);
			cntrBackInBO.setBackBranchNo(pclkBranchNo);
			//1.调用[保单辅助]订正接口
			CxfHeader headerInfo1 = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo1);
			CntrBackOutBO cntrBackOutBO =grpCntrListBackService.setGrpCntrListBack(cntrBackInBO);
			if (StringUtils.equals(RET_CODE_ONE, cntrBackOutBO.getRetCode())) {//保单辅助-回退成功
				//2.调用本地冲正服务
				prodCorrectInfo(grpInsurAppl.getCgNo(), pclkBranchNo , pclkNo);
				//3.金额回写
				doOneTableEarnestAccInfo(grpInsurAppl);
				//4.删除分期应收数据
				removePlnmioRec(grpInsurAppl);
				//删除生效表
				taskPrmyDao.deletTaskPrmyInfoByApplNo(TaskType.CNTR_INFORCE.getTaskType(), applNo);
				//删除落地控制表对应的信息
				taskCntrDataLandingQueueDao.deleteByApplNo(applNo);
			}else{//保单辅助-回退失败
				retInfo.setErrMsg(cntrBackOutBO.getErrMsg());
				retInfo.setRetCode(NO_FLAG);
				return retInfo;
			}
		}//end 

		retInfo.setRetCode(YSE_FLAG);
		retInfo.setErrMsg("保单[回退]或[撤销]成功!");
		return retInfo;
	}

	/**
	 * 订正:判断是否可以订正
	 * @param applNo 投保单号
	 */
	@Override
	public RetInfo correctDate(String applNo) {

		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(applNo);

		Map<String,Object> maps = new HashMap<>();
		maps.put("applNo", applNo);
		//保单基本信息
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, maps);
		if(null == grpInsurAppl){
			throw new BusinessException(new Throwable("根据投保单号["+applNo+"]查询保单基本信息失败."));
		}

		//查询轨迹，获得保单当前状态
		Map<String,Object> map = new HashMap<>();
		map.put("applNo", applNo);
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, map);
		if(null == insurApplOperTrace){
			throw new BusinessException(new Throwable("根据投保单号["+applNo+"]查询轨迹基本信息失败."));
		}

		//判断是否打印在途（老打印）
		String printLang = oldPrintTaskQueueDao.checkPrintIsOnLanging(applNo);
		if(StringUtils.equals("1", printLang)){
			retInfo.setErrMsg("保单[纸质打印在途 ],不满足订正条件！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}


		//判断保单状态
		RetInfo retInfoLand  =	insurApplLandUtils.getIsInsurApplLanding(applNo);
		if (StringUtils.equals(RET_CODE_TWO, retInfoLand.getRetCode())) {//生效在途
			retInfo.setErrMsg("保单在[保单生效落地]状态，不满足订正条件！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}else if(StringUtils.equals(RET_CODE_THREE, retInfoLand.getRetCode())){
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
					retInfo.setErrMsg("保单为[基金险]并且已生效，不支持[订正]！");
					retInfo.setRetCode(NO_FLAG);
					return retInfo;
				}
			}
		}

		//将不支持订正的节点放入map中，用于下文判断
		Map<String, String> mapApplState = new HashMap<>();
		mapApplState.put(NEW_APPL_STATE.OFFER_WITHDRAWN.getKey() , "撤销");
		mapApplState.put(NEW_APPL_STATE.INSURANCE_COVERAGE.getKey() , "拒保");
		mapApplState.put(NEW_APPL_STATE.EXTENSION.getKey() , "延期");
		mapApplState.put(NEW_APPL_STATE.RECEIPT_VERIFICA.getKey() , "回执核销");

		//获得该节点下的 当前操作节点
		String newApplState = insurApplOperTrace.getCurrentTraceNode().getProcStat();
		//判断是否有不支持订正的节点，如果只有直接返回失败
		if(map.containsKey(newApplState)){
			retInfo.setErrMsg("保单在["+map.get(newApplState)+"]状态，不满足订正条件！");
			retInfo.setRetCode(NO_FLAG);
			return retInfo;
		}

		retInfo.setErrMsg("可以订正，请调用订正方法！");
		retInfo.setRetCode(YSE_FLAG);
		return retInfo;
	}


	/**
	 * 修改清单被保人中被保人状态字段为N（新建）。保单回退成功时调用该方法。
	 * */
	public void updateProcStatOfGrpInsured(String applNo){
		Map<String, Object> map=new HashMap<>();
		map.put("applNo", applNo);
		Update update=new Update();
		update.set("procStat","N");
		mongoBaseDao.updateAll(GrpInsured.class, map, update);
	}


	//PS/S、FA/S的数据进行冲正
	public void prodCorrectInfo(String cgNo , String branchNo ,String clerkNo){

		List<MioLog> mioLogList = mioDao.queryMioLogList(cgNo);
		if(null == mioLogList || mioLogList.isEmpty()){
			throw new BusinessException(new Throwable("根据cgNo["+cgNo+"]未获取到实收数据相关信息."));
		}

		List<MioLog> newList = new ArrayList<>();
		for(MioLog mioLogOut:mioLogList){
			String mioItemCode = mioLogOut.getMioItemCode();
			String mioTypeCode = mioLogOut.getMioType();
			//判断需要修改的数据，如果[收付项目代码]+[收付款方式代码]为“FAS”+收付类型为-1     或“PSS”+收付类型为1，同时收付交易类型为0， 则需要修改。
			if(((MIO_ITEM_CODE_AND_MIO_TYPE_FAS.equals(mioItemCode+mioTypeCode) && mioLogOut.getMioClass()==-1)  
					|| (MIO_ITEM_CODE_AND_MIO_TYPE_PSS.equals(mioItemCode+mioTypeCode) && mioLogOut.getMioClass()==1)) 
					&&  0 == mioLogOut.getMioTxClass()){
				//修改原有数据MioTxClass为1
				mioLogOut.setMioTxClass(-1);
				newList.add(mioLogOut);
				logger.info("生成一条新数据，用于抵消老数据！cgNo="+cgNo);
				//生成一条新数据，用于抵消老数据
				MioLog  mioLogIn = new MioLog();
				mioLogIn.setMioLogId(mioDao.getMioLogIdSeq());
				mioLogIn.setMioTxClass(1);
				mioLogIn.setCorrMioTxNo(mioLogOut.getMioTxNo());
				//当前时间
				mioLogIn.setMioDate(new Date());
				//添加操作节点
				mioLogIn.setPclkBranchNo(branchNo);
				mioLogIn.setPclkNo(clerkNo);	
				mioLogIn.setAmnt(mioLogOut.getAmnt().multiply(new BigDecimal("-1")));
				mioLogIn.setPlnmioRecId(mioLogOut.getPlnmioRecId());
				mioLogIn.setPolCode(mioLogOut.getPolCode());
				mioLogIn.setCntrType(mioLogOut.getCntrType());
				mioLogIn.setCgNo(mioLogOut.getCgNo());
				mioLogIn.setSgNo(mioLogOut.getSgNo());
				mioLogIn.setCntrNo(mioLogOut.getCntrNo());
				mioLogIn.setMtnId(mioLogOut.getMtnId());
				mioLogIn.setMtnItemCode(mioLogOut.getMtnItemCode());
				mioLogIn.setIpsnNo(mioLogOut.getIpsnNo());
				mioLogIn.setMioCustNo(mioLogOut.getMioCustNo());
				mioLogIn.setMioCustName(mioLogOut.getMioCustName());
				mioLogIn.setMioItemCode(mioLogOut.getMioItemCode());
				mioLogIn.setMioType(mioLogOut.getMioType());
				mioLogIn.setMgrBranchNo(mioLogOut.getMgrBranchNo());
				mioLogIn.setSalesBranchNo(mioLogOut.getSalesBranchNo());
				mioLogIn.setSalesChannel(mioLogOut.getSalesChannel());
				mioLogIn.setSalesNo(mioLogOut.getSalesNo());
				mioLogIn.setMioClass(mioLogOut.getMioClass());
				mioLogIn.setBankCode(mioLogOut.getBankCode());
				mioLogIn.setBankAccNo(mioLogOut.getBankAccNo());
				mioLogIn.setAccId(mioLogOut.getAccId());
				mioLogIn.setCurrencyCode(mioLogOut.getCurrencyCode());
				mioLogIn.setBtMioTxNo(mioLogOut.getBtMioTxNo());
				mioLogIn.setTransCode(mioLogOut.getTransCode());
				mioLogIn.setVatRate(mioLogOut.getVatRate());
				mioLogIn.setVatId(mioLogOut.getVatId());
				mioLogIn.setVat(mioLogOut.getVat());
				mioLogIn.setNetIncome(mioLogOut.getNetIncome());
				mioLogIn.setRouterNo(mioLogOut.getRouterNo());
				mioLogIn.setMioProcFlag(mioLogOut.getMioProcFlag());
				mioLogIn.setBankAccName(mioLogOut.getBankAccName());
				mioLogIn.setAccCustIdNo(mioLogOut.getAccCustIdNo());
				mioLogIn.setAccCustIdType(mioLogOut.getAccCustIdType());
				mioLogIn.setRemark(mioLogOut.getRemark());
				mioLogIn.setVoucherNo(mioLogOut.getVoucherNo());
				mioLogIn.setReceiptNo(mioLogOut.getReceiptNo());
				mioLogIn.setMioTxNo(mioLogOut.getMioTxNo());
				mioLogIn.setBatNo(mioLogOut.getBatNo());
				mioLogIn.setCoreStat(mioLogOut.getCoreStat());

				//时间类型操作
				if(null != mioLogOut.getPremDeadlineDate()){
					mioLogIn.setPremDeadlineDate(new Date(mioLogOut.getPremDeadlineDate().getTime()));
				}
				if(null != mioLogOut.getFinPlnmioDate()){
					mioLogIn.setFinPlnmioDate(new Date(mioLogOut.getFinPlnmioDate().getTime()));
				}
				if(null != mioLogOut.getPlnmioCreateTime()){
					mioLogIn.setPlnmioCreateTime(new Date(mioLogOut.getPlnmioCreateTime().getTime()));
				}
				if(null != mioLogOut.getMioLogUpdTime()){
					mioLogIn.setMioLogUpdTime(new Date(mioLogOut.getMioLogUpdTime().getTime()));
				}
				if(null != mioLogOut.getPlnmioDate()){
					mioLogIn.setPlnmioDate(new Date(mioLogOut.getPlnmioDate().getTime()));
				}

				newList.add(mioLogIn);
				//修改原有数据
				mioDao.updateMioLog(mioLogOut.getMioLogId(), -1);

				Query queryMioLog = new Query();
				queryMioLog.addCriteria(Criteria.where("mioLogId").is(mioLogOut.getMioLogId()));
				Update updatequeryMioLog = new Update();
				updatequeryMioLog.set("mioTxClass", -1);
				mongoTemplate.updateMulti(queryMioLog, updatequeryMioLog, MioLog.class);

				//插入新生成的一条数据
				mioDao.insertOneMioLog(mioLogIn);
				mongoBaseDao.insert(mioLogIn);
			}//end if
		}//end for
	}
	/**
	 * 如果是多笔，则需要循环遍历实收完成的应收数据，针对每条应收数据在EarnestAccInfo表中找到对应的账户，再把这条应收的金额加到找到的账户里面的账户余额balance中，
	 * 并在EarnestAccInfo. mioAccInfoList里面新增一条收付类型为1、收付金额为FA/S数据中的金额的操作轨迹记录;
	 * 如果是多笔，则需要循环遍历实收完成的应收数据，针对每条应收数据在EarnestAccInfo表中找到对应的账户，
	 * 再把这条应收的金额加到找到的账户里面的账户余额balance中，并在EarnestAccInfo. mioAccInfoList里面新增一条收付类型为1、收付金额为FA/S数据中的金额的操作轨迹记录。
	 * */
	public void doOneTableEarnestAccInfo(GrpInsurAppl grpInsurAppl){

		//判断是否是银行转账，如果是现金则直接返回。
		if(!isBankTrans(grpInsurAppl)){
			return;
		}
		//获得账户回写金额
		Map<String ,Double> map  = procPSInfoUtil.procCorrPSData(grpInsurAppl);
		/**
		 * 回填账户金额（备注：压力测试时，复测此方法）
		 */
		for(Entry<String, Double> entry:map.entrySet()){
			String key = entry.getKey();
			double val = entry.getValue();

			//查询-保单应收账户信息
			EarnestAccInfo earnestAccInfo = queryAccInfoDao.queryOneEarnestAccInfoByApplNo(key);
			if(null == earnestAccInfo){
				throw new BusinessException(new Throwable("根据投保单号["+grpInsurAppl.getApplNo()+"]未获取到账户信息."));
			}

			//金额回滚
			queryAccInfoDao.updateEarnestAccInfo(key, (earnestAccInfo.getBalance().add(BigDecimal.valueOf(val)).doubleValue()));

			// 3.在MIO_ACC_INFO_LOG表里面添加一条轨迹数据
			MioAccInfoLog	mioAccInfoLog =  new  MioAccInfoLog(); 		
			mioAccInfoLog.setAccId(earnestAccInfo.getAccId());
			mioAccInfoLog.setAccLogId(queryAccInfoDao.selectMioAccInfoLog());
			mioAccInfoLog.setAnmt(BigDecimal.valueOf(val));
			mioAccInfoLog.setCreateTime(new Date());
			mioAccInfoLog.setMioClass(Integer.valueOf(MIO_CLASS.RECEIVABLES.getKey()));
			mioAccInfoLog.setMioItemCode(MIO_ITEM_CODE.FA.getKey());
			mioAccInfoLog.setRemark("回退数据");
			queryAccInfoDao.insertEarnestAccInfo(mioAccInfoLog);
		}//end for
	}

	/**
	 * 判断缴费形式是否为银行转账（备注：临时方法。当后期缴费来源为2、3时也有可能是现金缴费，故到时需完善本方法20170303！！！）
	 * @param grpInsurAppl
	 * @return
	 * 是-true 非-false
	 */
	public  boolean isBankTrans(GrpInsurAppl grpInsurAppl) {
		/*	1，保费来源为2、3  默认为T
			2，保费来源为1，且缴费相关里面的缴费形式(moneyinType)为T
			3，保费来源为1，且组织架构树里面有缴费节点 默认为 T*/
		String premSource = grpInsurAppl.getPaymentInfo().getPremSource();
		boolean isTrue = true;
		if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)){	
			if(grpInsurAppl.getOrgTreeList() == null || grpInsurAppl.getOrgTreeList().isEmpty()){ 
				//1.1、无组织架构树时
				if(!MONEYIN_TYPE.TRANSFER.getKey().equals(grpInsurAppl.getPaymentInfo().getMoneyinType())){
					isTrue=false;

				}
			}else{
				//用于存放组织架构树中已经缴费的数据
				List<OrgTree> orgTreeList = new ArrayList<>();
				for(OrgTree orgTree :grpInsurAppl.getOrgTreeList()){
					//判断是否需要缴费:ifPay【Y：是；N：否。】
					if(YES_NO_FLAG.YES.getKey().equals(orgTree.getIsPaid())){
						orgTreeList.add(orgTree);
					}
				}
				if(orgTreeList.isEmpty() && !MONEYIN_TYPE.TRANSFER.getKey().equals(grpInsurAppl.getPaymentInfo().getMoneyinType())){
					isTrue=false;
				}
			}
		}
		return isTrue;
	}

	//回退时，删除分期应收数据
	public void removePlnmioRec(GrpInsurAppl grpInsurAppl){

		//判断是否多期
		int amorFlag = getAmorFlag(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit(),
				grpInsurAppl.getPaymentInfo().getMoneyinDur(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl());

		//amorFlag > 1 表示存在分期应收,删除对应分期应收数据
		if (amorFlag > 1){
			//oracle
			queryAccInfoDao.removePlnmioRecList(grpInsurAppl.getCgNo());

			//mongodb 
			Map<String, Object> map = new HashMap<>();
			map.put("mioItemCode", MIO_ITEM_CODE.PS.getKey());
			map.put("mioClass", 1);
			map.put("mtnItemCode", MTN_ITEM_CODE);
			map.put("cgNo", grpInsurAppl.getCgNo());
			mongoBaseDao.remove(PlnmioRec.class, map);
		}
	}


	/**
	 * 判断是否无清单分期应收
	 *
	 * @param insurDurDnit
	 * @param insurDur
	 * @param moneyinItrvl
	 * @return
	 */
	public int getAmorFlag(String insurDurDnit, double insurDur, String moneyinItrvl) {
		double moneyinDur = 0.0;
		//保险期间
		BigDecimal b1 = new BigDecimal(Double.toString(insurDur));
		//1
		BigDecimal b2 = new BigDecimal(Double.toString(1));
		//4
		BigDecimal b3 = new BigDecimal(Double.toString(4));
		//6
		BigDecimal b4 = new BigDecimal(Double.toString(6));
		//12
		BigDecimal b5 = new BigDecimal(Double.toString(12));
		//保险期间乘以12
		BigDecimal b6 = b1.multiply(b5);
		//判断缴费方式
		if ("O".equals(moneyinItrvl) || "W".equals(moneyinItrvl) || "Y".equals(moneyinItrvl) || "X".equals(moneyinItrvl)) {
			moneyinDur = 0.0;
		} else {//保险期类型
			if ("M".equals(insurDurDnit)) {
				switch (moneyinItrvl)//缴费方式
				{
				case "M"://保险期间除以1
					moneyinDur = b1.divide(b2).doubleValue();
					break;
				case "Q"://保险期间除以4
					moneyinDur = b1.divide(b3).doubleValue();
					break;
				case "H"://保险期间除以6
					moneyinDur = b1.divide(b4).doubleValue();
					break;
				default:
					moneyinDur = 0.0;
				}
			}//保险期类型
			if ("Y".equals(insurDurDnit)) {
				switch (moneyinItrvl)//缴费方式
				{
				case "M"://保险期间乘以12 在除以1
					moneyinDur = b6.divide(b2).doubleValue();
					break;
				case "Q"://保险期间乘以12 在除以4
					moneyinDur = b6.divide(b3).doubleValue();
					break;
				case "H"://保险期间乘以12 在除以6
					moneyinDur = b6.divide(b4).doubleValue();
					break;
				default:
					moneyinDur = 0.0;
					break;
				}
			} //end if
		}//end if
		return (int) moneyinDur;
	}

}
