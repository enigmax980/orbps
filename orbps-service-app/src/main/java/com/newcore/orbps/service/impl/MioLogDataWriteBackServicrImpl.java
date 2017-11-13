package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import com.halo.core.header.HeaderInfoHolder;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.MioLog;
import com.newcore.orbps.models.banktrans.MioLogCode;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.pcms.bo.GrpListMioTask;
import com.newcore.orbps.models.pcms.bo.PolCode;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.MioLogTemp;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.MioLogDataWriteBack;
import com.newcore.orbps.service.pcms.api.GrpMioNtTaskService;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
/**
 * 实收付流水落地清单告知
 * @author LJF
 * 2016年11月04日 09:51:26
 */
@Service("mioLogDataWriteBack")
public class MioLogDataWriteBackServicrImpl implements MioLogDataWriteBack {


	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	GrpMioNtTaskService grpMioNtTaskServiceClient;
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(MioLogDataWriteBackServicrImpl.class);
	/**
	 * 方法说明：根据投保单号查询mongodb库中[管理机构号][险种数组][收付费标识id最小号][收付费标识id最大号]；操作节点集合的[操作员管理机构号][操作员工号]。
	 * 将查出的值作为参数调用 保单辅助系统接口。每调用一次本服务 查询收付费相关信息集合中 [实收付流水类]中 批次号 字段值每次加一。
	 * @param applNo  投保单号
	 * @param keySetMap 
	 */
	@Override
	public String writeMioLogDate(String applNo, Map<String, String> keySetMap,String mioType,String oclkBranchNo,String oclkClerkNo) {//关于MioLogTemp 非空字段   赋值问题
		Map<String,Object> mapOld = new HashMap<>();
		mapOld.put("applNo", applNo);
		//根据投保单号查询该保单下实收流水中收付费标识id最大值和最小值，以及该保单的收付项目代码与收付款方式代码
		GrpInsurAppl grpIns = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, mapOld);
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, mapOld);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("applNo", applNo);
		//是否作废；0-作废；1-正常
		//查询收付费相关信息
		MioPlnmioInfo mioPlnmioInfo = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class, map);
		if(null!=grpIns && null!=insurApplOperTrace && null!=mioPlnmioInfo){
			//获得 管理机构号
			String mgrBranchNo = grpIns.getMgrBranchNo();	//管理机构号
			//获得最大最小ID
			long minMioId;
			long maxMioId;
			//判断是正向落地 或冲正
			if(null==keySetMap){
				//冲正。
				minMioId = 0l;	
				maxMioId = 0l;	
			}else{
				//正向落地 
				Map<String, Long>  aa=	getResultMap(applNo, keySetMap);
				minMioId = aa.get("min");	
				maxMioId = aa.get("max");	
			}
			//操作员管理机构号，操作员工号
			@SuppressWarnings("unused")
			String oclkBranchNoStr = "";
			@SuppressWarnings("unused")
			String oclkClerkNoStr = "";
			long batNo = 0 ;
			//实收类型:1:  正向落地   -1：冲正
			if("-1".equals(mioType)){
				mioType = "-1";
				oclkBranchNoStr=oclkBranchNo;
				oclkClerkNoStr=oclkClerkNo;
			}else{
				mioType = "1";
				//获得操作节点的操作员管理机构号和操作员工号
				TraceNode traceNode = insurApplOperTrace.getCurrentTraceNode();
				oclkBranchNoStr = traceNode.getPclkBranchNo();
				oclkClerkNoStr = traceNode.getPclkNo();
				batNo =getbatNo(keySetMap, mioPlnmioInfo);
			}
			//应保单辅助要求（批作业），每调一次本服务 批次号就加一
			long batNoNew=batNo+1;
			//获得险种集合
			List<PolCode> polCodeList =new ArrayList<>();
			for(Policy policy:grpIns.getApplState().getPolicyList()){
				PolCode polCodeClass =new PolCode();
				polCodeClass.setPolCode(policy.getPolCode());	
				polCodeList.add(polCodeClass);
			}
			//非空判断
			if(null!=mgrBranchNo && !polCodeList.isEmpty()){
				//判断是正向落地还是冲正。
				if("1".endsWith(mioType)){
					//如果是正向落地，将修改后的[批次号]数据插入mongodb。冲正时[批次号]可以不传。详见接口文档。
					updateBatNo(mioPlnmioInfo, mapOld, batNoNew);
					//如果是正向落地，调用pcms接口前，需要将实收数据集合从收付费相关信息类取出放到mongodb中mioLogTemp集合中（方便pcms方批作业调用）。
					extractMioLogToMioLogTemp(applNo, mioPlnmioInfo);
				}
				//实收付流水任务 BO
				GrpListMioTask grpListMioTask  = new GrpListMioTask();
				grpListMioTask.setApplNo(applNo);
				//实收类型:1:  正向落地   -1：冲正
				if("-1".equals(mioType)){
					Date date =new Date();
					String ss = String.valueOf(date.getTime());
					grpListMioTask.setMaxMioID(Long.valueOf(ss.substring(0,6)));
					grpListMioTask.setMinMioID(Long.valueOf(ss.substring(6)));
				}else{
					grpListMioTask.setMaxMioID(maxMioId);
					grpListMioTask.setMinMioID(minMioId);
				}
				grpListMioTask.setBatNo(batNoNew);
				grpListMioTask.setMgrBranchNo(mgrBranchNo);
				grpListMioTask.setMioType(mioType);
				grpListMioTask.setPclkBranchNo(oclkBranchNo);
				grpListMioTask.setPclkNo(oclkClerkNo);
				grpListMioTask.setPolCodeList(polCodeList);
				/**
				 * 本段特别说明，在调用[实收付流水落地清单告知接口]前，更改实收表字段 【实收数据入账状态】  coreState=T；
				 * 调用pcms[实收付流水落地清单告知接口]成功后，更改实收表字段 【实收数据入账状态】  coreState=S；
				 * 调用pcms[实收付流水落地清单告知接口]失败后，更改实收表字段 【实收数据入账状态】  coreState=F。
				 * coreState=T  (N--未送；T--已送；S--入账成功；F--入账失败)
				 * */
				//判断是正向落地还是冲正。
				if("1".endsWith(mioType)){
					//修改实收表 【实收数据入账状态】字段，赋值 coreState=T
					updateCoreStateToT(mioPlnmioInfo, mapOld, MioLogCode.CORE_STATE_TO);
				}
				//消息头设置
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				//				headerInfo.setOrginSystem("ORBPS");					
				//				headerInfo.getRouteInfo().setBranchNo("120000");
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				//调用pcms实收付流水落地清单告知接口
				RetInfo  retInfo =	grpMioNtTaskServiceClient.grpListMioTaskCreate(grpListMioTask);
				//判断是单笔数据还是多笔数据
				int len =getmioPlnmioInfoLen(grpIns);
				//调用pcms接口后，根据上一步调用返回的成功标识符，返回N或者Y。如果retInfo.getRetCode()为1，即为调用成功。
				if("1".equals(retInfo.getRetCode()) && len>1){
					//如果成功，修改实收表 【实收数据入账状态】字段，赋值 coreState=S
					updateCoreState(mioPlnmioInfo, mapOld, MioLogCode.CORE_STATE_SUCCESS,mioType);
					return "Y";
				}else if("1".equals(retInfo.getRetCode()) && len<=1){
					return "Y";
				}else if("0".equals(retInfo.getRetCode()) && len<=1){
					return "N";
				}else{
					//如果失败，修改实收表 【实收数据入账状态】字段，赋值 coreState=F
					updateCoreState(mioPlnmioInfo, mapOld, MioLogCode.CORE_STATE_FAILED,mioType);
					//如果失败，将上一步从MioLog抽取放到mongodb中mioLogTemp集合中的数据 ，从mioLogTemp集合中删除。避免下一次重复抽取。
					//	mongoBaseDao.remove(MioLogTemp.class, mapOld);
					return "N";
				}
			}else{
				logger.error("查询不到投保单号为："+applNo+"下对应的[管理机构号]或[险种集合]或[节点操作员]！");
				return "N";
			}
		}else{
			logger.error("投保单号为："+applNo+"对应的[保单信息]或[轨迹节点]或[收费相关信息]查询不到信息！");
			return "N";
		}
	}
	//判断单笔多笔--判断缴费形式是否为银行转账
	public int getmioPlnmioInfoLen(GrpInsurAppl grpInsurAppl){
		int len = 0;
		/*	1，保费来源为2、3  默认为T
		2，保费来源为1，且缴费相关里面的缴费形式(moneyinType)为T
		3，保费来源为1，且组织架构树里面有缴费节点 默认为 T*/
		String premSource = grpInsurAppl.getPaymentInfo().getPremSource();
		boolean isTrue = true;
		if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)){	
			if(grpInsurAppl.getOrgTreeList() == null || grpInsurAppl.getOrgTreeList().isEmpty()){ 
				//1.1、无组织架构树时
				if(!"T".equals(grpInsurAppl.getPaymentInfo().getMoneyinType())){
					isTrue=false;
				}
			}else{
				//用于存放组织架构树中已经缴费的数据
				List<OrgTree> orgTreeList = new ArrayList<>();
				for(OrgTree orgTree :grpInsurAppl.getOrgTreeList()){
					//判断是否需要缴费:ifPay【Y：是；N：否。】
					if("Y".equals(orgTree.getIsPaid())){
						orgTreeList.add(orgTree);
					}
				}
				if(orgTreeList.isEmpty() && !"T".equals(grpInsurAppl.getPaymentInfo().getMoneyinType())){
					isTrue=false;
				}
			}
		}
		if(isTrue){
			len = 2;
		}else{
			//此处设为2，不代表多笔数据为两笔。只作为区别单笔。
			len = 1;
		}
		return len;
	}
	/** 
	 * 调用pcms接口前，将实收数据集合从收付费相关信息类取出（方便pcms方批作业调用）放到mongodb中mioLogTemp集合中。
	 * 抽取规则：
	 * 		1.实收数据入账状态  coreStat=N/F  (N--未送；T--已送；S--入账成功；F--入账失败)
	 * 		2.收付交易类型          mioTxClass-0    (0 正常交易  1 冲正交易 -1 被冲交易)
	 * */
	public void extractMioLogToMioLogTemp(String applNo,MioPlnmioInfo mioPlnmioInfo ){

		for (MioLog mioLogOld : mioPlnmioInfo.getMioLogList()) {
			Long a = new Long(0);
			Long b = new Long(mioLogOld.getMioTxClass());
			//收付交易类型          mioTxClass-0    (0 正常交易  1 冲正交易 -1 被冲交易)
			boolean fal =(a.longValue()==b.longValue());
			//抽取规则
			if(MioLogCode.CORE_STATE_NO.equals(mioLogOld.getCoreStat()) || MioLogCode.CORE_STATE_FAILED.equals(mioLogOld.getCoreStat()) && fal){
				MioLogTemp mioLogTemp = new MioLogTemp();
				mioLogTemp.setApplNo(applNo);

				mioLogTemp.setMioLogId(mioLogOld.getMioLogId());
				mioLogTemp.setMioTxClass(mioLogOld.getMioTxClass());
				mioLogTemp.setPlnmioRecId(mioLogOld.getPlnmioRecId());
				mioLogTemp.setPolCode(mioLogOld.getPolCode());
				mioLogTemp.setCntrType(mioLogOld.getCntrType());

				mioLogTemp.setCgNo(mioLogOld.getCgNo());
				mioLogTemp.setSgNo(mioLogOld.getSgNo());
				mioLogTemp.setCntrNo(mioLogOld.getCntrNo());
				mioLogTemp.setMtnId(mioLogOld.getMtnId());
				mioLogTemp.setMtnItemCode(mioLogOld.getMtnItemCode());

				mioLogTemp.setIpsnNo(mioLogOld.getIpsnNo());
				mioLogTemp.setMioCustNo(mioLogOld.getMioCustNo());
				mioLogTemp.setMioCustName(mioLogOld.getMioCustName());
				mioLogTemp.setPlnmioDate(mioLogOld.getPlnmioDate());
				mioLogTemp.setMioDate(mioLogOld.getMioDate());

				mioLogTemp.setMioLogUpdTime(mioLogOld.getMioLogUpdTime());
				mioLogTemp.setMioItemCode(mioLogOld.getMioItemCode());
				mioLogTemp.setMioType(mioLogOld.getMioType());
				mioLogTemp.setMgrBranchNo(mioLogOld.getMgrBranchNo());
				mioLogTemp.setPclkBranchNo(mioLogOld.getPclkBranchNo());

				mioLogTemp.setPclkNo(mioLogOld.getPclkNo());
				mioLogTemp.setSalesBranchNo(mioLogOld.getSalesBranchNo());
				mioLogTemp.setSalesChannel(mioLogOld.getSalesChannel());
				mioLogTemp.setSalesNo(mioLogOld.getSalesNo());
				mioLogTemp.setMioClass(mioLogOld.getMioClass());

				mioLogTemp.setAmnt(mioLogOld.getAmnt());
				mioLogTemp.setBankCode(mioLogOld.getBankCode());
				mioLogTemp.setBankAccNo(mioLogOld.getBankAccNo());
				mioLogTemp.setAccId(mioLogOld.getAccId());
				mioLogTemp.setCurrencyCode(mioLogOld.getCurrencyCode());

				mioLogTemp.setCorrMioTxNo(mioLogOld.getCorrMioTxNo());

				mioLogTemp.setVatRate(mioLogOld.getVatRate());
				mioLogTemp.setVatId(mioLogOld.getVatId());
				mioLogTemp.setVat(mioLogOld.getVat());
				mioLogTemp.setNetIncome(mioLogOld.getNetIncome());

				mioLogTemp.setPlnmioCreateTime(mioLogOld.getPlnmioCreateTime());
				mioLogTemp.setRouterNo(mioLogOld.getRouterNo());
				mioLogTemp.setMioProcFlag(mioLogOld.getMioProcFlag());
				mioLogTemp.setBankAccName(mioLogOld.getBankAccName());
				mioLogTemp.setAccCustIdNo(mioLogOld.getAccCustIdNo());

				mioLogTemp.setAccCustIdType(mioLogOld.getAccCustIdType());
				mioLogTemp.setRemark(mioLogOld.getRemark());
				mioLogTemp.setFinPlnmioDate(mioLogOld.getFinPlnmioDate());
				mioLogTemp.setClearingMioTxNo(mioLogOld.getClearingMioTxNo());
				mioLogTemp.setVoucherNo(mioLogOld.getVoucherNo());

				mioLogTemp.setReceiptNo(mioLogOld.getReceiptNo());
				mioLogTemp.setCorrMioDate(mioLogOld.getCorrMioDate());
				mioLogTemp.setPremDeadlineDate(mioLogOld.getPremDeadlineDate());
				mioLogTemp.setMioTxNo(mioLogOld.getMioTxNo());
				mioLogTemp.setBatNo(mioLogOld.getBatNo());
				mongoBaseDao.insert(mioLogTemp);
			}//end if -是否满足抽取规则
		}//end for
	}


	/**
	 * 修改保单批次号。每调用一次本服务批次号自动加一。
	 * */
	public void updateBatNo(MioPlnmioInfo mioPlnmioInfo,Map<String,Object> mapOld,long batNoNew){
		//获取实收数据list
		List<MioLog>  mioLogList1 = mioPlnmioInfo.getMioLogList();
		//新建实收list，用于封装修改状态之后的实收数据
		List<MioLog>  mioLogList2 = new ArrayList<>();
		//循环遍历实收list
		for (MioLog mioLog : mioLogList1) {
			//修改[批次号]
			mioLog.setBatNo(batNoNew);
			mioLogList2.add(mioLog);
		}//end for
		mioPlnmioInfo.setMioLogList(mioLogList2);
		mongoBaseDao.remove(MioPlnmioInfo.class, mapOld);
		mongoBaseDao.insert(mioPlnmioInfo);
	}

	public void updateCoreStateToT(MioPlnmioInfo mioPlnmioInfo,Map<String,Object> mapOld,String successFlag){
		//获取实收数据list
		List<MioLog>  mioLogList1 = mioPlnmioInfo.getMioLogList();
		//新建实收list，用于封装修改状态之后的实收数据
		List<MioLog>  mioLogList2 = new ArrayList<>();
		//循环遍历实收list
		for (MioLog mioLog : mioLogList1) {
			if(MioLogCode.CORE_STATE_NO.equals(mioLog.getCoreStat()) || 
					MioLogCode.CORE_STATE_FAILED.equals(mioLog.getCoreStat())){
				MioLog 	mioLogOld = new MioLog();

				mioLogOld.setMioTxClass(mioLog.getMioTxClass());
				mioLogOld.setMioLogId(mioLog.getMioLogId());
				mioLogOld.setPlnmioRecId(mioLog.getPlnmioRecId());
				mioLogOld.setPolCode(mioLog.getPolCode());
				mioLogOld.setCntrType(mioLog.getCntrType());

				mioLogOld.setCgNo(mioLog.getCgNo());
				mioLogOld.setSgNo(mioLog.getSgNo());
				mioLogOld.setCntrNo(mioLog.getCntrNo());
				mioLogOld.setMtnId(mioLog.getMtnId());
				mioLogOld.setMtnItemCode(mioLog.getMtnItemCode());

				mioLogOld.setIpsnNo(mioLog.getIpsnNo());
				mioLogOld.setMioCustNo(mioLog.getMioCustNo());
				mioLogOld.setMioCustName(mioLog.getMioCustName());
				mioLogOld.setPlnmioDate(mioLog.getPlnmioDate());
				mioLogOld.setMioDate(mioLog.getMioDate());

				mioLogOld.setMioLogUpdTime(mioLog.getMioLogUpdTime());
				mioLogOld.setMioItemCode(mioLog.getMioItemCode());
				mioLogOld.setMioType(mioLog.getMioType());
				mioLogOld.setMgrBranchNo(mioLog.getMgrBranchNo());
				mioLogOld.setPclkBranchNo(mioLog.getPclkBranchNo());

				mioLogOld.setPclkNo(mioLog.getPclkNo());
				mioLogOld.setSalesBranchNo(mioLog.getSalesBranchNo());
				mioLogOld.setSalesChannel(mioLog.getSalesChannel());
				mioLogOld.setSalesNo(mioLog.getSalesNo());
				mioLogOld.setMioClass(mioLog.getMioClass());

				mioLogOld.setAmnt(mioLog.getAmnt());
				mioLogOld.setBankCode(mioLog.getBankCode());
				mioLogOld.setBankAccNo(mioLog.getBankAccNo());
				mioLogOld.setAccId(mioLog.getAccId());
				mioLogOld.setCurrencyCode(mioLog.getCurrencyCode());

				mioLogOld.setBtMioTxNo(mioLog.getBtMioTxNo());
				mioLogOld.setCorrMioTxNo(mioLog.getCorrMioTxNo());
				mioLogOld.setTransCode(mioLog.getTransCode());

				mioLogOld.setVatRate(mioLog.getVatRate());
				mioLogOld.setVatId(mioLog.getVatId());
				mioLogOld.setVat(mioLog.getVat());
				mioLogOld.setNetIncome(mioLog.getNetIncome());

				mioLogOld.setPlnmioCreateTime(mioLog.getPlnmioCreateTime());
				mioLogOld.setRouterNo(mioLog.getRouterNo());
				mioLogOld.setMioProcFlag(mioLog.getMioProcFlag());
				mioLogOld.setBankAccName(mioLog.getBankAccName());
				mioLogOld.setAccCustIdNo(mioLog.getAccCustIdNo());

				mioLogOld.setAccCustIdType(mioLog.getAccCustIdType());
				mioLogOld.setRemark(mioLog.getRemark());
				mioLogOld.setFinPlnmioDate(mioLog.getFinPlnmioDate());
				mioLogOld.setClearingMioTxNo(mioLog.getClearingMioTxNo());
				mioLogOld.setVoucherNo(mioLog.getVoucherNo());

				mioLogOld.setReceiptNo(mioLog.getReceiptNo());
				mioLogOld.setCorrMioDate(mioLog.getCorrMioDate());
				mioLogOld.setPremDeadlineDate(mioLog.getPremDeadlineDate());
				mioLogOld.setMioTxNo(mioLog.getMioTxNo());
				mioLogOld.setBatNo(mioLog.getBatNo());
				mioLogOld.setLevelCode(mioLog.getLevelCode());
				mioLogOld.setFeeGrpNo(mioLog.getFeeGrpNo());
				//修改[实收数据入账状态]
				mioLogOld.setCoreStat(successFlag);
				mioLogList2.add(mioLogOld);
			}else{
				//如果[实收数据入账状态]不是“N”或“F”，则不需要修改数据。
				mioLogList2.add(mioLog);
			}
			mioPlnmioInfo.setMioLogList(mioLogList2);
		}//end for
		mongoBaseDao.remove(MioPlnmioInfo.class, mapOld);
		mongoBaseDao.insert(mioPlnmioInfo);
	}

	/**
	 * 在调用保单辅助[实收付流水落地清单告知接口]前后，修改coreState状态。
	 * coreState=T  (N--未送；T--已送；S--入账成功；F--入账失败)
	 * */
	public void updateCoreState(MioPlnmioInfo mioPlnmioInfo,Map<String,Object> mapOld,String successFlag,String mioType){
		//获取实收数据list
		List<MioLog>  mioLogList1 = mioPlnmioInfo.getMioLogList();
		//新建实收list，用于封装修改状态之后的实收数据
		List<MioLog>  mioLogList2 = new ArrayList<>();
		//判断是正向落地还是冲正，如果是正向落地需要修改coreState为successFlag。
		if("1".endsWith(mioType)){
			//循环遍历实收list
			for (MioLog mioLog : mioLogList1) {
				if(MioLogCode.CORE_STATE_TO.equals(mioLog.getCoreStat())){
					MioLog 	mioLogOld = new MioLog();

					mioLogOld.setMioTxClass(mioLog.getMioTxClass());
					mioLogOld.setMioLogId(mioLog.getMioLogId());
					mioLogOld.setPlnmioRecId(mioLog.getPlnmioRecId());
					mioLogOld.setPolCode(mioLog.getPolCode());
					mioLogOld.setCntrType(mioLog.getCntrType());

					mioLogOld.setCgNo(mioLog.getCgNo());
					mioLogOld.setSgNo(mioLog.getSgNo());
					mioLogOld.setCntrNo(mioLog.getCntrNo());
					mioLogOld.setMtnId(mioLog.getMtnId());
					mioLogOld.setMtnItemCode(mioLog.getMtnItemCode());

					mioLogOld.setIpsnNo(mioLog.getIpsnNo());
					mioLogOld.setMioCustNo(mioLog.getMioCustNo());
					mioLogOld.setMioCustName(mioLog.getMioCustName());
					mioLogOld.setPlnmioDate(mioLog.getPlnmioDate());
					mioLogOld.setMioDate(mioLog.getMioDate());

					mioLogOld.setMioLogUpdTime(mioLog.getMioLogUpdTime());
					mioLogOld.setMioItemCode(mioLog.getMioItemCode());
					mioLogOld.setMioType(mioLog.getMioType());
					mioLogOld.setMgrBranchNo(mioLog.getMgrBranchNo());
					mioLogOld.setPclkBranchNo(mioLog.getPclkBranchNo());

					mioLogOld.setPclkNo(mioLog.getPclkNo());
					mioLogOld.setSalesBranchNo(mioLog.getSalesBranchNo());
					mioLogOld.setSalesChannel(mioLog.getSalesChannel());
					mioLogOld.setSalesNo(mioLog.getSalesNo());
					mioLogOld.setMioClass(mioLog.getMioClass());

					mioLogOld.setAmnt(mioLog.getAmnt());
					mioLogOld.setBankCode(mioLog.getBankCode());
					mioLogOld.setBankAccNo(mioLog.getBankAccNo());
					mioLogOld.setAccId(mioLog.getAccId());
					mioLogOld.setCurrencyCode(mioLog.getCurrencyCode());

					mioLogOld.setBtMioTxNo(mioLog.getBtMioTxNo());
					mioLogOld.setCorrMioTxNo(mioLog.getCorrMioTxNo());
					mioLogOld.setTransCode(mioLog.getTransCode());

					mioLogOld.setVatRate(mioLog.getVatRate());
					mioLogOld.setVatId(mioLog.getVatId());
					mioLogOld.setVat(mioLog.getVat());
					mioLogOld.setNetIncome(mioLog.getNetIncome());

					mioLogOld.setPlnmioCreateTime(mioLog.getPlnmioCreateTime());
					mioLogOld.setRouterNo(mioLog.getRouterNo());
					mioLogOld.setMioProcFlag(mioLog.getMioProcFlag());
					mioLogOld.setBankAccName(mioLog.getBankAccName());
					mioLogOld.setAccCustIdNo(mioLog.getAccCustIdNo());

					mioLogOld.setAccCustIdType(mioLog.getAccCustIdType());
					mioLogOld.setRemark(mioLog.getRemark());
					mioLogOld.setFinPlnmioDate(mioLog.getFinPlnmioDate());
					mioLogOld.setClearingMioTxNo(mioLog.getClearingMioTxNo());
					mioLogOld.setVoucherNo(mioLog.getVoucherNo());

					mioLogOld.setReceiptNo(mioLog.getReceiptNo());
					mioLogOld.setCorrMioDate(mioLog.getCorrMioDate());
					mioLogOld.setPremDeadlineDate(mioLog.getPremDeadlineDate());
					mioLogOld.setMioTxNo(mioLog.getMioTxNo());
					mioLogOld.setBatNo(mioLog.getBatNo());
					mioLogOld.setLevelCode(mioLog.getLevelCode());
					mioLogOld.setFeeGrpNo(mioLog.getFeeGrpNo());
					//修改[实收数据入账状态]
					mioLogOld.setCoreStat(successFlag);
					mioLogList2.add(mioLogOld);
				}else{
					//如果[实收数据入账状态]不是“N”或“F”，则不需要修改数据。
					mioLogList2.add(mioLog);
				}
				mioPlnmioInfo.setMioLogList(mioLogList2);
			}//end for
			mongoBaseDao.remove(MioPlnmioInfo.class, mapOld);
			mongoBaseDao.insert(mioPlnmioInfo);
		}else{
			//如果是正冲正，需要修改[收付交易类型]为-1。
			//循环遍历实收list
			for (MioLog mioLog : mioLogList1) {
				String mioItemCode = mioLog.getMioItemCode();
				String mioTypeCode = mioLog.getMioType();
				//判断需要修改的数据，如果[收付项目代码]+[收付款方式代码]为“FAS”或“PSS”，则需要修改。
				if("FAS".equals(mioItemCode+mioTypeCode) || "PSS".equals(mioItemCode+mioTypeCode) && "S".equals(successFlag)){
					MioLog 	mioLogOld = new MioLog();

					mioLogOld.setMioLogId(mioLog.getMioLogId());
					mioLogOld.setPlnmioRecId(mioLog.getPlnmioRecId());
					mioLogOld.setPolCode(mioLog.getPolCode());
					mioLogOld.setCntrType(mioLog.getCntrType());

					mioLogOld.setCgNo(mioLog.getCgNo());
					mioLogOld.setSgNo(mioLog.getSgNo());
					mioLogOld.setCntrNo(mioLog.getCntrNo());
					mioLogOld.setMtnId(mioLog.getMtnId());
					mioLogOld.setMtnItemCode(mioLog.getMtnItemCode());

					mioLogOld.setIpsnNo(mioLog.getIpsnNo());
					mioLogOld.setMioCustNo(mioLog.getMioCustNo());
					mioLogOld.setMioCustName(mioLog.getMioCustName());
					mioLogOld.setPlnmioDate(mioLog.getPlnmioDate());
					mioLogOld.setMioDate(mioLog.getMioDate());

					mioLogOld.setMioLogUpdTime(mioLog.getMioLogUpdTime());
					mioLogOld.setMioItemCode(mioLog.getMioItemCode());
					mioLogOld.setMioType(mioLog.getMioType());
					mioLogOld.setMgrBranchNo(mioLog.getMgrBranchNo());
					mioLogOld.setPclkBranchNo(mioLog.getPclkBranchNo());

					mioLogOld.setPclkNo(mioLog.getPclkNo());
					mioLogOld.setSalesBranchNo(mioLog.getSalesBranchNo());
					mioLogOld.setSalesChannel(mioLog.getSalesChannel());
					mioLogOld.setSalesNo(mioLog.getSalesNo());
					mioLogOld.setMioClass(mioLog.getMioClass());

					mioLogOld.setAmnt(mioLog.getAmnt());
					mioLogOld.setBankCode(mioLog.getBankCode());
					mioLogOld.setBankAccNo(mioLog.getBankAccNo());
					mioLogOld.setAccId(mioLog.getAccId());
					mioLogOld.setCurrencyCode(mioLog.getCurrencyCode());

					mioLogOld.setBtMioTxNo(mioLog.getBtMioTxNo());
					mioLogOld.setCorrMioTxNo(mioLog.getCorrMioTxNo());

					mioLogOld.setTransCode(mioLog.getTransCode());

					mioLogOld.setVatRate(mioLog.getVatRate());
					mioLogOld.setVatId(mioLog.getVatId());
					mioLogOld.setVat(mioLog.getVat());
					mioLogOld.setNetIncome(mioLog.getNetIncome());

					mioLogOld.setPlnmioCreateTime(mioLog.getPlnmioCreateTime());
					mioLogOld.setRouterNo(mioLog.getRouterNo());
					mioLogOld.setMioProcFlag(mioLog.getMioProcFlag());
					mioLogOld.setBankAccName(mioLog.getBankAccName());
					mioLogOld.setAccCustIdNo(mioLog.getAccCustIdNo());

					mioLogOld.setAccCustIdType(mioLog.getAccCustIdType());
					mioLogOld.setRemark(mioLog.getRemark());
					mioLogOld.setFinPlnmioDate(mioLog.getFinPlnmioDate());
					mioLogOld.setClearingMioTxNo(mioLog.getClearingMioTxNo());
					mioLogOld.setVoucherNo(mioLog.getVoucherNo());

					mioLogOld.setReceiptNo(mioLog.getReceiptNo());
					mioLogOld.setCorrMioDate(mioLog.getCorrMioDate());
					mioLogOld.setPremDeadlineDate(mioLog.getPremDeadlineDate());
					mioLogOld.setMioTxNo(mioLog.getMioTxNo());
					mioLogOld.setBatNo(mioLog.getBatNo());
					mioLogOld.setLevelCode(mioLog.getLevelCode());
					mioLogOld.setFeeGrpNo(mioLog.getFeeGrpNo());
					mioLogOld.setCoreStat(successFlag);
					//修改[收付交易类型] 0 正常交易;1 冲正交易;-1 被冲交易
					mioLogOld.setMioTxClass(mioLog.getMioTxClass());
					mioLogList2.add(mioLogOld);
				}//如果冲正失败，不需要修改[收付交易类型]，只修改[实收数据入账状态]CoreStat
				else if("FAS".equals(mioItemCode+mioTypeCode) || "PSS".equals(mioItemCode+mioTypeCode)  && "F".equals(successFlag)){
					MioLog 	mioLogOld = new MioLog();

					mioLogOld.setMioLogId(mioLog.getMioLogId());
					mioLogOld.setPlnmioRecId(mioLog.getPlnmioRecId());
					mioLogOld.setPolCode(mioLog.getPolCode());
					mioLogOld.setCntrType(mioLog.getCntrType());

					mioLogOld.setCgNo(mioLog.getCgNo());
					mioLogOld.setSgNo(mioLog.getSgNo());
					mioLogOld.setCntrNo(mioLog.getCntrNo());
					mioLogOld.setMtnId(mioLog.getMtnId());
					mioLogOld.setMtnItemCode(mioLog.getMtnItemCode());

					mioLogOld.setIpsnNo(mioLog.getIpsnNo());
					mioLogOld.setMioCustNo(mioLog.getMioCustNo());
					mioLogOld.setMioCustName(mioLog.getMioCustName());
					mioLogOld.setPlnmioDate(mioLog.getPlnmioDate());
					mioLogOld.setMioDate(mioLog.getMioDate());

					mioLogOld.setMioLogUpdTime(mioLog.getMioLogUpdTime());
					mioLogOld.setMioItemCode(mioLog.getMioItemCode());
					mioLogOld.setMioType(mioLog.getMioType());
					mioLogOld.setMgrBranchNo(mioLog.getMgrBranchNo());
					mioLogOld.setPclkBranchNo(mioLog.getPclkBranchNo());

					mioLogOld.setPclkNo(mioLog.getPclkNo());
					mioLogOld.setSalesBranchNo(mioLog.getSalesBranchNo());
					mioLogOld.setSalesChannel(mioLog.getSalesChannel());
					mioLogOld.setSalesNo(mioLog.getSalesNo());
					mioLogOld.setMioClass(mioLog.getMioClass());

					mioLogOld.setAmnt(mioLog.getAmnt());
					mioLogOld.setBankCode(mioLog.getBankCode());
					mioLogOld.setBankAccNo(mioLog.getBankAccNo());
					mioLogOld.setAccId(mioLog.getAccId());
					mioLogOld.setCurrencyCode(mioLog.getCurrencyCode());

					mioLogOld.setBtMioTxNo(mioLog.getBtMioTxNo());
					mioLogOld.setCorrMioTxNo(mioLog.getCorrMioTxNo());

					mioLogOld.setTransCode(mioLog.getTransCode());

					mioLogOld.setVatRate(mioLog.getVatRate());
					mioLogOld.setVatId(mioLog.getVatId());
					mioLogOld.setVat(mioLog.getVat());
					mioLogOld.setNetIncome(mioLog.getNetIncome());

					mioLogOld.setPlnmioCreateTime(mioLog.getPlnmioCreateTime());
					mioLogOld.setRouterNo(mioLog.getRouterNo());
					mioLogOld.setMioProcFlag(mioLog.getMioProcFlag());
					mioLogOld.setBankAccName(mioLog.getBankAccName());
					mioLogOld.setAccCustIdNo(mioLog.getAccCustIdNo());

					mioLogOld.setAccCustIdType(mioLog.getAccCustIdType());
					mioLogOld.setRemark(mioLog.getRemark());
					mioLogOld.setFinPlnmioDate(mioLog.getFinPlnmioDate());
					mioLogOld.setClearingMioTxNo(mioLog.getClearingMioTxNo());
					mioLogOld.setVoucherNo(mioLog.getVoucherNo());

					mioLogOld.setReceiptNo(mioLog.getReceiptNo());
					mioLogOld.setCorrMioDate(mioLog.getCorrMioDate());
					mioLogOld.setPremDeadlineDate(mioLog.getPremDeadlineDate());
					mioLogOld.setMioTxNo(mioLog.getMioTxNo());
					mioLogOld.setBatNo(mioLog.getBatNo());
					mioLogOld.setLevelCode(mioLog.getLevelCode());
					mioLogOld.setFeeGrpNo(mioLog.getFeeGrpNo());
					mioLogOld.setCoreStat(successFlag);
					//修改[收付交易类型] 0 正常交易;1 冲正交易;-1 被冲交易
					mioLogList2.add(mioLogOld);
				}else{
					//如果[实收数据入账状态]不是“N”或“F”，则不需要修改数据。
					mioLogList2.add(mioLog);
				}
				mioPlnmioInfo.setMioLogList(mioLogList2);
			}//end for
			mongoBaseDao.remove(MioPlnmioInfo.class, mapOld);
			mongoBaseDao.insert(mioPlnmioInfo);

		}//end if判断是正向落地还是冲正
	}

	/**
	 * 获取实收数据中某一发送批次中的，批次号值
	 * @param 
	 * 		  keySetMap keySetMap中K值为【收付费项目+收付款方式】 ，V值为null
	 * 		  mioItemCode    收付费项目，
	 * 		  mioTypeCode	  收付款方式
	 * 		  mioPlnmioInfo  缴费相关
	 * @return long 本批次的批次号
	 */
	private long  getbatNo(Map<String, String> keySetMap,MioPlnmioInfo mioPlnmioInfo) {
		long batchNo = 0;
		String mioItemCode =null;
		String mioTypeCode =null;
		for (String key : keySetMap.keySet()) {
			mioItemCode =key.substring(0,2);
			mioTypeCode =key.substring(2);
			String type = mioItemCode+mioTypeCode;
			batchNo=getbatNo2(type, mioPlnmioInfo);
		}//end for
		return batchNo;
	}

	private long  getbatNo2(String type,MioPlnmioInfo mioPlnmioInfo){
		long batchNo = 0;
		for (MioLog mioLog :mioPlnmioInfo.getMioLogList()){
			if(type.equals(mioLog.getMioItemCode()+mioLog.getMioType())){
				try {
					batchNo = mioLog.getBatNo();
				} catch (NullPointerException e) {
					logger.info(e.getMessage(),e);
					batchNo =0l;
				}
			}//end if
		}//end for
		return batchNo;
	}





	/**
	 * 获取实收数据中某一发送批次中的，实收ID最小值,最大值
	 * @param applNo 投保单号
	 * @param 
	 * 		  keySetMap keySetMap中K值为【收付费项目+收付款方式】 ，V值为null
	 * 		  mioItemCode    收付费项目，
	 * 		  mioTypeCode	  收付款方式
	 * @return Map<String, Long> 本批次的最大最小ID
	 */
	private Map<String, Long>  getResultMap(String applNo,Map<String, String> keySetMap) {
		String mioItemCode =null;
		String mioTypeCode =null;
		//创建map，用于返回值
		Map<String, Long> result =new  HashMap<String, Long>();
		//先判断map有几个键值对
		if(keySetMap.keySet().size()==1){
			//假如map集合有一个键值对，获取收付费项目+收付款方式，调用查询方法，获取对应条件下的最大最小ID
			for (String key : keySetMap.keySet()) {
				mioItemCode =key.substring(0,2);
				mioTypeCode =key.substring(2);
			}
			//调用查询方法，获取对应条件下的最大最小ID
			long minMioId =	getMioId(applNo, false, mioItemCode, mioTypeCode);
			long maxMioId =	getMioId(applNo, true, mioItemCode, mioTypeCode);
			result.put("min", minMioId);
			result.put("max", maxMioId);
			return result;
		}else{
			//假如map集合有多个键值对，遍历获取每个键值的 收付费项目+收付款方式，调用查询方法，获取对应条件下的最大最小ID
			List<Long> minList = new ArrayList<>();
			List<Long> maxList = new ArrayList<>();
			for (String key : keySetMap.keySet()) {
				//得到某一个的键值对的最大最小值
				mioItemCode =key.substring(0,2);
				mioTypeCode =key.substring(2);
				//调用查询方法，获取对应条件下的最大最小ID
				long minMioId =	getMioId(applNo, false, mioItemCode, mioTypeCode);
				long maxMioId =	getMioId(applNo, true, mioItemCode, mioTypeCode);
				//将最大最小值放到 最大最小集合中。最终将比较出不同键值对对应的最大最小ID
				minList.add(minMioId);
				maxList.add(maxMioId);
			}
			//将最小值集合按升序排序
			Collections.sort(minList);
			//将最大值集合按升序排序
			Collections.sort(maxList);
			//得到集合中的最大最小值，并放入map集合返回
			result.put("min", minList.get(0));
			result.put("max", maxList.get(maxList.size()-1));
			return result;
		}
	}
	/**
	 * 获取实收数据中，实收ID最小值,最大值
	 * @param applNo 投保单号
	 * @param 
	 * 		  isMax          true： 获取最大值，false:获取最小值 
	 * 		  mioItemCode    收付费项目，
	 * 		  mioType	               收付款方式
	 * 		  mioTxClass     收付交易类型   0 正常交易 1 冲正交易-1 被冲交易
	 * @return mioLogId
	 */
	private long getMioId(String applNo,boolean isMax,String mioItemCode,String mioTypeCode) {
		//获取银行转账数据中transBatSeq最大的数据
		Aggregation aggregation;
		if(isMax){
			aggregation = Aggregation.newAggregation(
					//根据投保单号 和保单状态（有效性）查询
					Aggregation.match(Criteria.where("applNo").is(applNo)),
					Aggregation.unwind("mioLogList"),
					//根据收付费项目，收付款方式查询
					Aggregation.match(Criteria.where("mioLogList.mioItemCode").is(mioItemCode).and
							("mioLogList.mioType").is(mioTypeCode).and("mioLogList.mioTxClass").is(0)),
					Aggregation.group("max").max("mioLogList.mioLogId").as("getMioLogId")
					);
		}else{
			aggregation = Aggregation.newAggregation(
					//根据投保单号 和保单状态（有效性）查询
					Aggregation.match(Criteria.where("applNo").is(applNo)),
					Aggregation.unwind("mioLogList"),
					//根据收付费项目，收付款方式查询
					Aggregation.match(Criteria.where("mioLogList.mioItemCode").is(mioItemCode).and
							("mioLogList.mioType").is(mioTypeCode).and("mioLogList.mioTxClass").is(0)),
					Aggregation.group("max").min("mioLogList.mioLogId").as("getMioLogId")
					);
		}
		AggregationResults<Object> aggregate = mongoTemplate.aggregate(aggregation, "mioPlnmioInfo", Object.class);
		BasicDBList bdbl =(BasicDBList) aggregate.getRawResults().get("result");
		//自动增长序列实现
		long mioLogId=0;
		if(bdbl.isEmpty()){
			mioLogId=0L;
		}else{
			BasicDBObject obj=(BasicDBObject) bdbl.get(0);   
			mioLogId = obj.getLong("getMioLogId");
		}
		return mioLogId;
	}

}
