package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.banktrans.PlnmioRec;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.ApplState;
import com.newcore.orbps.models.service.bo.ConstructInsurInfo;
import com.newcore.orbps.models.service.bo.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.GrpInsurApplArrivalControlBo;
import com.newcore.orbps.models.service.bo.OperTrace;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.PaymentInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.TuPolResult;
import com.newcore.orbps.models.service.bo.udw.ApplUdwResult;
import com.newcore.orbps.models.service.bo.udw.UdwPolResult;
import com.newcore.orbps.models.web.vo.sendgrpinsurappl.GrpInsurApplSendVo;
import com.newcore.orbps.service.api.ProcCommonFinQueueRecordService;
import com.newcore.orbps.service.api.ProcNewApplPSInfoService;
import com.newcore.orbps.service.api.TaskCntrPrintService;
import com.newcore.orbps.service.pcms.api.MissionLandingService;
import com.newcore.orbps.service.pcms.api.SetComAgreeService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;



//@Schedule(cron = "0 0/1 * * * ?")
//@Service("grpInsurApplArrivalJop")
//@DisallowConcurrentExecution
public class GrpInsurApplArrivalJop implements Job {



	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MissionLandingService missionLandingServiceClient;

	@Autowired
	SetComAgreeService setComAgreeServiceClient;

	@Autowired
	ProcNewApplPSInfoService  procNewApplPSInfoService;

	@Autowired
	ProcCommonFinQueueRecordService procCommonFinQueueRecordService;

	@Autowired 
	TaskCntrPrintService taskCntrPrintService;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(GrpInsurApplArrivalJop.class);
	/**
	 * 定时任务调用该方法，去扫描保单落地信息控制表，如果有需要落地的数据则调用保单落地方法。
	 */

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		String	sql = "select CG_NO ,lst_proc_type ,APPL_NO,MIOLOG_DATE from CNTR_EFFECT_CTRL where PROC_STAT = '0'";
		SqlRowSet row = jdbcTemplate.queryForRowSet(sql);
		List<GrpInsurApplArrivalControlBo> listNum= new ArrayList<>();
		boolean statusMio = false;
		while(row.next()){
			logger.info("查询到需要落地的数据，开始处理......");
			String cgNo=row.getString(1);
			String lstProcType=row.getString(2);
			String applNo=row.getString(3);
			String mio=row.getString(4);
			//查询实收付流水控制表，是否存在有需要实收付流水清单告知的数据
			String selectcounter = "select count(*) from MIO_INFO_ROAM_TASK_QUEUE WHERE APPL_NO = ? AND TASK_ID = ? AND STATUS in ('N','E','K')";
			int	counter = jdbcTemplate.queryForObject(selectcounter, int.class, applNo,"FAT");
			if(counter==0){
				//如果没有，状态置为true。此时说明，没有需要实收付流水清单告知的数据或存在数据但已经告知成功，帽子可以直接落地。
				statusMio = true;
			}else{
				//如果有，即实收付流水清单告知存在未送或失败，团单帽子不可以落地，状态置为false。直到告知状态为C。
				statusMio = false;
			}
			//当没有实收付流水清单告知的数据 或者 已经告知成功，将数据放入bo，调用团单落地。
			if(statusMio){
				GrpInsurApplArrivalControlBo grpInsurApplArrivalControlBo = new GrpInsurApplArrivalControlBo();
				grpInsurApplArrivalControlBo.setCgNo(cgNo);
				grpInsurApplArrivalControlBo.setApplNo(applNo);
				grpInsurApplArrivalControlBo.setLstProcType(lstProcType);
				grpInsurApplArrivalControlBo.setMiologDate(mio);
				listNum.add(grpInsurApplArrivalControlBo);
			}
		}
		if(!listNum.isEmpty()){
			logger.info("帽子落地开始......"+listNum.size());
			selectGrp(listNum);
			logger.info("帽子落地结束......");
		}

	}

	public void selectGrp(List<GrpInsurApplArrivalControlBo> listNum){
		String str="";
		@SuppressWarnings("unused")
		String lstProcType="";
		String mio = "";
		String applNo = "";
		RetInfo  retInfo=new RetInfo();
		GrpInsurApplArrivalControlBo grpApplArrConBo=new GrpInsurApplArrivalControlBo();
		for (int i = 0; i <listNum.size(); i++) {
			GrpInsurApplArrivalControlBo grpInsurA=listNum.get(i);
			str = grpInsurA.getCgNo();
			lstProcType =	grpInsurA.getLstProcType();
			mio =	grpInsurA.getMiologDate();
			applNo =grpInsurA.getApplNo();
			//调用pcms团单帽子落地接口，将参数传入map
			Map<String, Object> map = new HashMap<>();
			map.put("cgNo",str);
			logger.info("调用落地服务开始........................................................cgNo="+str);
			retInfo=fromPcmsGrpInsurApplArrival(map);
			logger.info("调用落地服务结束........................................................cgNo="+str);
			//判断调用接口返回信息，若RetCode=1 即程序执行成功。反之则为失败！
			String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
			if(("1").equals(retInfo.getRetCode())){
				//判断是不是订正
				if("S".equals(mio)){
					logger.info("订正后落地成功........................................................cgNo=");
					//更改-保单落地信息控制表-处理状态为1,成功
					grpApplArrConBo.setProcStat("15");
					grpApplArrConBo.setProcCauseDesc("订正后落地成功!");
					//回退后判断是清汇还是团单，之后调用打印。
					doTaskCntrPrintService(applNo);
				}else{
					logger.info("调用落地服务成功落地，待调用清单告知........................................................cgNo="+str);
					//更改-保单落地信息控制表-处理状态为8   基本信息发送成功
					grpApplArrConBo.setProcStat("8");
					grpApplArrConBo.setProcCauseDesc("保单基本信息发送成功!");
				}
			}else{
				//更改-保单落地信息控制表-处理状态为9   基本信息处理失败
				grpApplArrConBo.setProcStat("9");
				grpApplArrConBo.setProcCauseDesc(retInfo.getErrMsg());
			}	
			grpApplArrConBo.setCgNo(str);
			jdbcTemplate.update(updatesql, grpApplArrConBo.getProcStat() ,grpApplArrConBo.getProcCauseDesc(),grpApplArrConBo.getCgNo());
		}//end for
	}//end selectGrp()


	/**
	 * 保单落地。
	 */

	public RetInfo fromPcmsGrpInsurApplArrival(Map<String, Object> map){
		String cgNo=null;
		//遍历map中的值  
		for (Object value : map.values()) { 
			cgNo=(String) value;
		}  
		RetInfo retInfo=new RetInfo();
		//团体出单基本信息,用于下文调用。
		GrpInsurAppl grpInsurAppl=new GrpInsurAppl();
		logger.info("调用落地服务开始........................................................"+cgNo);
		//与mongodb交互时的条件参数
		map.put("cgNo", cgNo);
		//修改状态字段，锁定本条数据。
		String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
		jdbcTemplate.update(updatesql, "16" ,"保单落地-数据处理中！",cgNo);

		//在mongodb中添加字段。因业务需要可能会在原有基础上增加新字段，但不影响实体类。
		Update update=new Update();
		//理赔录入类型-写死
		update.set("applState.clDcType","M");
		mongoBaseDao.update(GrpInsurAppl.class, map, update);
		//根据条件 查询数据
		grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		logger.info("落地生效前校验开始......"+cgNo);
		//非空校验
		String checkResult = checkGrpInsurAppl(grpInsurAppl);
		logger.info("落地生效前校验结束......"+cgNo);
		//判断校验结果，若为空即说明没有空值；若有值即为 空字符返回的提示信息。
		if(StringUtils.isBlank(checkResult)){
			GrpInsurApplSendVo grpInsurApplSendVo = new GrpInsurApplSendVo();
			//转换赋值
			grpInsurApplSendVo.setApplNo(grpInsurAppl.getApplNo());
			grpInsurApplSendVo.setLstProcType(grpInsurAppl.getLstProcType());
			grpInsurApplSendVo.setInsurProperty(grpInsurAppl.getInsurProperty());
			grpInsurApplSendVo.setApproNo(grpInsurAppl.getApproNo());
			grpInsurApplSendVo.setAccessSource(grpInsurAppl.getAccessSource());
			grpInsurApplSendVo.setGrpBusiType(grpInsurAppl.getGrpBusiType());
			grpInsurApplSendVo.setApplBusiType(grpInsurAppl.getApplBusiType());
			grpInsurApplSendVo.setCntrPrintType(grpInsurAppl.getCntrPrintType());
			grpInsurApplSendVo.setListPrintType(grpInsurAppl.getListPrintType());
			grpInsurApplSendVo.setVoucherPrintType(grpInsurAppl.getVoucherPrintType());
			grpInsurApplSendVo.setPrtIpsnlstType(grpInsurAppl.getPrtIpsnlstType());
			grpInsurApplSendVo.setSalesDevelopFlag(grpInsurAppl.getSalesDevelopFlag());
			grpInsurApplSendVo.setCntrType(grpInsurAppl.getCntrType()); 
			grpInsurApplSendVo.setCntrSendType(grpInsurAppl.getCntrSendType());
			grpInsurApplSendVo.setEntChannelFlag(grpInsurAppl.getEntChannelFlag()); 
			grpInsurApplSendVo.setSgType(grpInsurAppl.getSgType());
			grpInsurApplSendVo.setApplDate(grpInsurAppl.getApplDate()); 
			grpInsurApplSendVo.setAccessChannel(grpInsurAppl.getAccessChannel()); 
			grpInsurApplSendVo.setArgueType(grpInsurAppl.getArgueType()); 
			grpInsurApplSendVo.setArbitration(grpInsurAppl.getArbitration());
			grpInsurApplSendVo.setRenewedCgNo(grpInsurAppl.getRenewedCgNo()); 
			grpInsurApplSendVo.setAgreementNo(grpInsurAppl.getAgreementNo()); 
			grpInsurApplSendVo.setGiftFlag(grpInsurAppl.getGiftFlag()); 
			grpInsurApplSendVo.setNotificaStat(grpInsurAppl.getNotificaStat()); 
			grpInsurApplSendVo.setCgNo(grpInsurAppl.getCgNo()); 
			grpInsurApplSendVo.setSgNo(grpInsurAppl.getSgNo()); 
			grpInsurApplSendVo.setSignDate(grpInsurAppl.getSignDate()); 
			grpInsurApplSendVo.setInForceDate(grpInsurAppl.getInForceDate()); 
			grpInsurApplSendVo.setRenewTimes(grpInsurAppl.getRenewTimes()); 
			grpInsurApplSendVo.setCntrExpiryDate(grpInsurAppl.getCntrExpiryDate()); 
			grpInsurApplSendVo.setFirPolCode(grpInsurAppl.getFirPolCode()); 
			grpInsurApplSendVo.setUdwType(grpInsurAppl.getUdwType()); 
			grpInsurApplSendVo.setSrndAmntCmptType(grpInsurAppl.getSrndAmntCmptType()); 
			grpInsurApplSendVo.setSrdFeeRate(grpInsurAppl.getSrdFeeRate()); 
			grpInsurApplSendVo.setPsnListHolderInfo(grpInsurAppl.getPsnListHolderInfo());
	//		grpInsurApplSendVo.setGrpHolderInfo(grpInsurAppl.getGrpHolderInfo()); 
			grpInsurApplSendVo.setSalesInfoList(grpInsurAppl.getSalesInfoList()); 
			 PaymentInfo  paymentInfo =	grpInsurAppl.getPaymentInfo();
			//当缴费相关中缴费形式为空时，赋值C。（个人缴费时默认c）动态赋值，不存入mongodb中。
			if(StringUtils.isBlank(paymentInfo.getMoneyinType())){
				paymentInfo.setMoneyinType("C");
			}
			grpInsurAppl.setPaymentInfo(paymentInfo);
			grpInsurApplSendVo.setPaymentInfo(grpInsurAppl.getPaymentInfo()); 
			//grpInsurApplSendVo.setApplState(grpInsurAppl.getApplState());
			grpInsurApplSendVo.setApplState(getApplUdwResult(grpInsurAppl.getApplNo(), grpInsurAppl));
			//获得要约分组
			List<IpsnStateGrp>   grpPolicyList = getGrpIpsnStateGrpList(cgNo);
			grpInsurApplSendVo.setIpsnStateGrpList(grpPolicyList); 
		//	grpInsurApplSendVo.setIpsnPayGrpList(grpInsurAppl.getIpsnPayGrpList()); 
		//	grpInsurApplSendVo.setOrgTreeList(grpInsurAppl.getOrgTreeList()); 
			OperTrace operTrace = new OperTrace();
			operTrace.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());
			grpInsurApplSendVo.setOperTrace(operTrace); 

			if(null!=grpInsurAppl.getConstructInsurInfo()){
				ConstructInsurInfo constructInsurInfo =new ConstructInsurInfo();
				com.newcore.orbps.models.service.bo.grpinsurappl.ConstructInsurInfo constructInsurInfoOld = grpInsurAppl.getConstructInsurInfo();
				constructInsurInfo.setAcdntDeathNums(constructInsurInfoOld.getAcdntDeathNums());
				constructInsurInfo.setAcdntDisableNums(constructInsurInfoOld.getAcdntDisableNums());
				constructInsurInfo.setAwardGrade(constructInsurInfoOld.getAwardGrade());
				constructInsurInfo.setConstructFrom(constructInsurInfoOld.getConstructFrom());
				constructInsurInfo.setConstructTo(constructInsurInfoOld.getConstructTo());

				constructInsurInfo.setDiseaDeathNums(constructInsurInfoOld.getDiseaDeathNums());
				constructInsurInfo.setDiseaDisableNums(constructInsurInfoOld.getDiseaDisableNums());
				constructInsurInfo.setEnterpriseLicence(constructInsurInfoOld.getEnterpriseLicence());
				constructInsurInfo.setFloorHeight(constructInsurInfoOld.getFloorHeight());
				constructInsurInfo.setIobjCost(constructInsurInfoOld.getIobjCost());
				constructInsurInfo.setIobjName(constructInsurInfoOld.getIobjName());
				constructInsurInfo.setIobjNo((long)1);

				constructInsurInfo.setIobjSize(constructInsurInfoOld.getIobjSize());
				constructInsurInfo.setPremCalType(constructInsurInfoOld.getPremCalType());
				constructInsurInfo.setProjLoc(constructInsurInfoOld.getProjLoc());
				constructInsurInfo.setProjLocType(constructInsurInfoOld.getProjLocType());
				constructInsurInfo.setProjType(constructInsurInfoOld.getProjType());
				constructInsurInfo.setSafetyFlag(constructInsurInfoOld.getSafetyFlag());
				constructInsurInfo.setSaftyAcdntFlag(constructInsurInfoOld.getSaftyAcdntFlag());
				constructInsurInfo.setSafetyFlag(constructInsurInfoOld.getSafetyFlag());
				grpInsurApplSendVo.setConstructInsurInfo(constructInsurInfo);
			}
			//grpInsurApplSendVo.setHealthInsurInfo(grpInsurAppl.getHealthInsurInfo()); 
			grpInsurApplSendVo.setFundInsurInfo(grpInsurAppl.getFundInsurInfo()); 
			//特约中[录入特约]不传给保单辅助。
			if(null!=grpInsurAppl.getConventions()){
				grpInsurAppl.getConventions().setInputConv("");
				grpInsurApplSendVo.setConventions(grpInsurAppl.getConventions()); 
			}
			grpInsurApplSendVo.setGcLppPremRateBoList(grpInsurAppl.getGcLppPremRateBoList());

			//合同打印方式-0：电子保单；1：纸质保单。（默认为0）
			String	cntrPrintType = grpInsurAppl.getCntrPrintType();
			//清单打印方式-0：0：电子清单，1：纸质清单。（默认为0）
			String	listPrintType = grpInsurAppl.getListPrintType();
			//个人凭证类型-0：电子个人凭证；1：纸制个人凭证；2打印家庭险凭证
			String	voucherPrintType = grpInsurAppl.getVoucherPrintType();
			//被保人清单打印方式L、V、N、P、H、F
			String	prtIpsnlstType =null;
			if("0".equals(cntrPrintType) && "1".equals(listPrintType) && "0".equals(voucherPrintType)){
				prtIpsnlstType = "L";
			}
			//			else if("0".equals(cntrPrintType) && "1".equals(listPrintType) && "1".equals(voucherPrintType)){
			//				prtIpsnlstType = "V";
			//			}
			else if("1".equals(cntrPrintType) && "0".equals(listPrintType) && "0".equals(voucherPrintType)){
				prtIpsnlstType = "N";
			}else if("0".equals(cntrPrintType) && "0".equals(listPrintType) && "1".equals(voucherPrintType)){
				prtIpsnlstType = "P";
			}else if("0".equals(cntrPrintType) && "0".equals(listPrintType) && "2".equals(voucherPrintType)){
				prtIpsnlstType = "H";
			}else{
				prtIpsnlstType = "F";
			}
			grpInsurApplSendVo.setPrtIpsnlstType(prtIpsnlstType);
			//判断是否是无清单分期
			int amorFlag = cfcGetAmorFlag(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit(), 
					grpInsurAppl.getPaymentInfo().getMoneyinDur(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl());
			Map<String, Object> mapMioPlnmioInfo = new HashMap<>();
			mapMioPlnmioInfo.put("applNo", grpInsurAppl.getApplNo());
			//应收付数据
			MioPlnmioInfo mioPlnmioInfo = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class, mapMioPlnmioInfo);
			if(amorFlag>1 && null!=mioPlnmioInfo){
				logger.info("..........................................是分期数据");
				List<PlnmioRec> plnmioRecList = new ArrayList<>();
				//遍历应收
				for (PlnmioRec plnmioRec :mioPlnmioInfo.getPlnmioRecList()) {
					//筛选分期应收数据
					if("29".equals(plnmioRec.getMtnItemCode()) && "PS".equals(plnmioRec.getMioItemCode())){
						logger.info("..........................................存在分期数据............................");
						//剔除-组织层次代码	levelCode 收费组属组编号	feeGrpNo
						PlnmioRec plnmioRecNew = new PlnmioRec();
						plnmioRecNew.setAccCustIdType(plnmioRec.getAccCustIdType());
						plnmioRecNew.setAccId(plnmioRec.getAccId());
						plnmioRecNew.setAmnt(plnmioRec.getAmnt());
						plnmioRecNew.setArcBranchNo(plnmioRec.getArcBranchNo());
						plnmioRecNew.setBankaccIdNo(plnmioRec.getBankaccIdNo());

						plnmioRecNew.setBankAccName(plnmioRec.getBankAccName());
						plnmioRecNew.setBankAccNo(plnmioRec.getBankAccNo());
						plnmioRecNew.setBankCode(plnmioRec.getBankCode());
						plnmioRecNew.setCgNo(plnmioRec.getCgNo());
						plnmioRecNew.setClearingMioTxNo(plnmioRec.getClearingMioTxNo());

						plnmioRecNew.setCntrNo(plnmioRec.getCntrNo());
						plnmioRecNew.setCurrencyCode(plnmioRec.getCurrencyCode());
						plnmioRecNew.setCntrType(plnmioRec.getCntrType());
						plnmioRecNew.setFinPlnmioDate(plnmioRec.getFinPlnmioDate());

						plnmioRecNew.setHoldFlag(plnmioRec.getHoldFlag());
						plnmioRecNew.setIpsnNo(plnmioRec.getIpsnNo());
						plnmioRecNew.setLockFlag(plnmioRec.getLockFlag());
						plnmioRecNew.setMgrBranchNo(plnmioRec.getMgrBranchNo());

						plnmioRecNew.setMioClass(plnmioRec.getMioClass());
						plnmioRecNew.setMioCustName(plnmioRec.getMioCustName());
						plnmioRecNew.setMioCustNo(plnmioRec.getMioCustNo());
						plnmioRecNew.setMioItemCode(plnmioRec.getMioItemCode());
						plnmioRecNew.setMioProcFlag(plnmioRec.getMioProcFlag());

						plnmioRecNew.setMioType(plnmioRec.getMioType());
						plnmioRecNew.setMtnId(plnmioRec.getMtnId());
						plnmioRecNew.setMtnItemCode(plnmioRec.getMtnItemCode());
						plnmioRecNew.setMultiPayAccType(plnmioRec.getMultiPayAccType());

						plnmioRecNew.setPlnmioCreateTime(plnmioRec.getPlnmioCreateTime());
						plnmioRecNew.setPlnmioDate(plnmioRec.getPlnmioDate());
						plnmioRecNew.setPlnmioRecId(plnmioRec.getPlnmioRecId());
						plnmioRecNew.setPolCode(plnmioRec.getPolCode());
						plnmioRecNew.setPremDeadlineDate(plnmioRec.getPremDeadlineDate());

						plnmioRecNew.setProcStat(plnmioRec.getProcStat());
						plnmioRecNew.setRemark(plnmioRec.getRemark());
						plnmioRecNew.setRouterNo(plnmioRec.getRouterNo());
						plnmioRecNew.setSalesBranchNo(plnmioRec.getSalesBranchNo());
						plnmioRecNew.setSalesChannel(plnmioRec.getSalesChannel());

						plnmioRecNew.setSalesNo(plnmioRec.getSalesNo());
						plnmioRecNew.setSgNo(plnmioRec.getSgNo());
						plnmioRecNew.setSignYear(plnmioRec.getSignYear());
						plnmioRecNew.setTransStat(plnmioRec.getTransStat());
						plnmioRecNew.setVoucherNo(plnmioRec.getVoucherNo());

						plnmioRecList.add(plnmioRecNew);
					}
				}
				logger.info("..........................................获取分期应收数据并赋值,分期应收条数"+plnmioRecList.size());
				//grpInsurApplSendVo.setPlnmioRecList(plnmioRecList);
			}//enf if 判断是否是分期


			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);	
			logger.info("调用pcms接口开始......"+cgNo);
			//调用PCMS接口，将数据传送过去。/
			retInfo=missionLandingServiceClient.missionLandingCreate(grpInsurApplSendVo);
			logger.info("调用pcms接口结束......"+cgNo);
			return retInfo;
		}else{
			logger.info("落地前非空校验不通过......");
			logger.info("落地前非空校验不通过详情："+checkResult);
			retInfo.setApplNo(grpInsurAppl.getApplNo());
			retInfo.setErrMsg("非空校验不通过："+checkResult);
			retInfo.setRetCode("0");
			return retInfo;
		}
	}

	/**retInfo
	 * 功能说明：校验团单帽子数据 
	 * 
	 */
	private String checkGrpInsurAppl(GrpInsurAppl grpInsurAppl){
		//非空校验
		StringBuilder sb = new StringBuilder();
		if(StringUtils.isBlank(grpInsurAppl.getApplNo())){
			sb.append("[投保单号为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getLstProcType())){
			sb.append("[团体清单标志为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getCntrPrintType())){
			grpInsurAppl.setCntrPrintType("0");
		}
		if(StringUtils.isBlank(grpInsurAppl.getListPrintType())){
			grpInsurAppl.setListPrintType("0");
		}
		if(StringUtils.isBlank(grpInsurAppl.getVoucherPrintType())){
			grpInsurAppl.setVoucherPrintType("0");
		}
		if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getRenewTimes()))){
			grpInsurAppl.setRenewTimes((long)0);
		}
		if(StringUtils.isBlank(grpInsurAppl.getSalesDevelopFlag())){
			grpInsurAppl.setSalesDevelopFlag("N");
		}
		if(StringUtils.isBlank(grpInsurAppl.getCntrSendType())){
			grpInsurAppl.setCntrSendType("1");
		}
		if(StringUtils.isBlank(grpInsurAppl.getEntChannelFlag())){
			grpInsurAppl.setEntChannelFlag("N");
		}
		if(StringUtils.isBlank(grpInsurAppl.getGiftFlag())){
			grpInsurAppl.setGiftFlag("N");
		}
		if(StringUtils.isBlank(grpInsurAppl.getUdwType())){
			grpInsurAppl.setUdwType("N");
		}
		if("1".equals(grpInsurAppl.getInsurProperty())){
			if(StringUtils.isBlank(grpInsurAppl.getRenewedCgNo())){
				sb.append("[上期保单号为空]");
			}
		}
		if(StringUtils.isBlank(grpInsurAppl.getCntrType())){
			sb.append("[契约形式为空]");
		}
		if(null==grpInsurAppl.getApplDate()){
			sb.append("[投保日期为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getArgueType())){
			sb.append("[争议处理方式为空]");
		}
		if("2".equals(grpInsurAppl.getArgueType())){
			if(StringUtils.isBlank(grpInsurAppl.getArbitration())){
				sb.append("[仲裁委员会名称为空]");
			}
		}
		if(StringUtils.isBlank(grpInsurAppl.getNotificaStat())){
			sb.append("[是否异常告知为空]");
		}
		if(StringUtils.isBlank(grpInsurAppl.getCgNo())){
			sb.append("[合同组号为空]");
		}
		if(("L").equals(grpInsurAppl.getCntrType())){
			if(StringUtils.isBlank(grpInsurAppl.getSgNo())){
				sb.append("[汇缴件号为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getSgType())){
				sb.append("[汇交人类型为空]");
			}
		}

		if(StringUtils.isBlank(grpInsurAppl.getFirPolCode())){
			sb.append("[第一主险为空]");
		}                                                                          
		if("P".equals(grpInsurAppl.getSgType()) && "L".equals(grpInsurAppl.getCntrType())){
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgCustNo())){
				sb.append("[汇交人姓名为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgName())){
				sb.append("[汇交人姓名为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgSex())){
				sb.append("[汇交人性别为空]");
			}
			if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPsnListHolderInfo().getSgBirthDate()))){
				sb.append("[汇交人出生日期为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgIdType())){
				sb.append("[汇交人证件类型为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgIdNo())){
				sb.append("[汇交人证件号码为空]");
			}
//			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgMobile())){
//				sb.append("[汇交人移动电话为空]");
//			}
			if("0".equals(grpInsurAppl.getCntrPrintType())){
				if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getSgEmail())){
					sb.append("[汇交人邮箱为空]");
				}
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getAddress().getProvince())){
				sb.append("省/自治州为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getAddress().getCity())){
				sb.append("[市/州为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getAddress().getCounty())){
				sb.append("[区/县为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getAddress().getHomeAddress())){
				sb.append("[地址明细为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getPsnListHolderInfo().getAddress().getPostCode())){
				sb.append("[邮政编码为空]");
			}			
		}

		if(("G".equals(grpInsurAppl.getSgType()) && "L".equals(grpInsurAppl.getCntrType())) || ("G".equals(grpInsurAppl.getSgType()))){
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getGrpName())){
				sb.append("[单位名称为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getGrpCustNo())){
				sb.append("[投保人客户号为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getGrpIdType())){
				sb.append("[团体客户证件类别为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getGrpIdNo())){
				sb.append("[团体客户证件号码为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getGrpCountryCode())){
				sb.append("[企业注册地国籍为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getGrpPsnDeptType())){
				sb.append("[外管局部门类型为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getOccClassCode())){
				sb.append("[行业类别为空]");
			}
			if("G".equals(grpInsurAppl.getSgType())){

				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getGrpHolderInfo().getNumOfEnterprise()))){
					sb.append("[企业员工总数为空]");
				}
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getGrpHolderInfo().getOnjobStaffNum()))){
					sb.append("[企业在职人数为空]");
				}
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getGrpHolderInfo().getIpsnNum()))){
					sb.append("[投保人数为空]");
				}
			}

			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getContactName())){
				sb.append("[联系人姓名为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getContactIdType())){
				sb.append("[联系人证件类别为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getContactIdNo())){
				sb.append("[联系人证件号码为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getContactMobile())&&StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getContactTelephone())){
				sb.append("[联系人移动电话与联系人固定电话为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getAddress().getProvince())){
				sb.append("[省/自治州为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getAddress().getCity())){
				sb.append("[市/州为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getAddress().getCounty())){
				sb.append("[区/县为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getAddress().getHomeAddress())){
				sb.append("[地址明细为空]");
			}
			if(StringUtils.isBlank(grpInsurAppl.getGrpHolderInfo().getAddress().getPostCode())){
				sb.append("[邮政编码为空]");
			}
		}
		int lenSalesInfo=grpInsurAppl.getSalesInfoList().size();
		if(lenSalesInfo>0){
			if("Y".equals(grpInsurAppl.getSalesDevelopFlag())){
				if(lenSalesInfo<=1){
					sb.append("[销售信息应为多条]");
				}else{
					//校验多条信息
					for (int i = 0; i < lenSalesInfo; i++) {
						SalesInfo salesInfo=grpInsurAppl.getSalesInfoList().get(i);
						if(StringUtils.isBlank(salesInfo.getSalesChannel())){
							sb.append("第"+i+"列"+"[销售渠道为空]");
						}
						if(StringUtils.isBlank(salesInfo.getSalesBranchNo())){
							sb.append("第"+i+"列"+"[销售机构为空]");
						}
						if("Y".equals(grpInsurAppl.getSalesDevelopFlag())){
							if(StringUtils.isBlank(salesInfo.getDevelMainFlag())){
								sb.append("第"+i+"列"+"[共同展业主副标记为空]");
							}
							if(StringUtils.isBlank(String.valueOf(salesInfo.getDevelopRate()))){
								sb.append("第"+i+"列"+"[展业比例为空]");
							}
						}
						if(StringUtils.isBlank(salesInfo.getSalesNo())){
							sb.append("第"+i+"列"+"[销售员工号为空]");
						}
						if(StringUtils.isBlank(salesInfo.getSalesName())){
							sb.append("第"+i+"列"+"[销售员姓名为空]");
						}
					}//end for
				}//end if--判断多条信息是否数量是否正确
			}else{//单条信息-校验()
				for (int i = 0; i < lenSalesInfo; i++) {
					SalesInfo salesInfo=grpInsurAppl.getSalesInfoList().get(i);
					if(StringUtils.isBlank(salesInfo.getSalesBranchNo())){
						sb.append("第"+i+"列"+"[销售机构为空]");
					}
					if(StringUtils.isBlank(salesInfo.getSalesNo())){
						sb.append("第"+i+"列"+"[销售员工号为空]");
					}
					if(StringUtils.isBlank(salesInfo.getSalesName())){
						sb.append("第"+i+"列"+"[销售员姓名为空]");
					}
				}//end for
			}//end if--判断多条信息
		}else{
			sb.append("[销售信息为空]");
		}//end if
		if(null==grpInsurAppl.getPaymentInfo()){
			sb.append("[缴费相关源为空]");
		}else{
			if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getPremSource())){
				sb.append("[团单保费来源为空]");
			}
			if("3".equals(grpInsurAppl.getPaymentInfo().getPremSource()) && 
					(null==grpInsurAppl.getPaymentInfo().getMultiPartyMoney() || "0".equals(grpInsurAppl.getPaymentInfo().getMultiPartyMoney()))){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getMultiPartyScale()))){
					sb.append("[团体个人共同付款，团体供款比例为空]");
				}
			}	
			if("3".equals(grpInsurAppl.getPaymentInfo().getPremSource()) && 
					(null==grpInsurAppl.getPaymentInfo().getMultiPartyScale() || "0".equals(grpInsurAppl.getPaymentInfo().getMultiPartyScale()))){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getMultiPartyMoney()))){
					sb.append("[团体个人共同付款，团体供款金额为空]");
				}
			}		
			if("3".equals(grpInsurAppl.getPaymentInfo().getPremSource()) || "2".equals(grpInsurAppl.getPaymentInfo().getPremSource())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getForeExpDate()))){
					sb.append("[团体个人共同付款，团体供款金额为空]");
				}
			}



			if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getMoneyinItrvl())){
				sb.append("[缴费方式为空]");
			}
			//			if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getMoneyinType())){
			//				sb.append("[缴费形式为空]");
			//			}
			if("T".equals(grpInsurAppl.getPaymentInfo().getMoneyinType())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getBankCode()))){
					sb.append("[开户银行为空]");
				}
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getBankAccName()))){
					sb.append("[开户名称为空]");
				}
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getBankAccNo()))){
					sb.append("[银行账号为空]");
				}
			}
			if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getStlType())){
				grpInsurAppl.getPaymentInfo().setStlType("N");
			}
			if("X".equals(grpInsurAppl.getPaymentInfo().getStlType())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getStlAmnt()))){
					sb.append("[结算限额为空]");
				}
			}
			if("X".equals(grpInsurAppl.getPaymentInfo().getStlType()) || "D".equals(grpInsurAppl.getPaymentInfo().getStlType())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getStlDate()))){
					sb.append("[结算日期为空]");
				}
			}
			if("L".equals(grpInsurAppl.getPaymentInfo().getStlType())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getStlRate()))){
					sb.append("[结算比例为空]");
				}
			}
			if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getMoneyinDur()))){
				sb.append("[缴费期为空]");
			}
			if("L".equals(grpInsurAppl.getCntrType())){
				if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getIsRenew())){
					grpInsurAppl.getPaymentInfo().setIsRenew("0");
				}
				if(StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getIsMultiPay())){
					grpInsurAppl.getPaymentInfo().setIsMultiPay("0");
				}
			}
			if("1".equals(grpInsurAppl.getPaymentInfo().getIsRenew())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getRenewExpDate()))){
					sb.append("[续期扣款截至日为空]");
				}
			}
			if("1".equals(grpInsurAppl.getPaymentInfo().getIsMultiPay())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getMutipayTimes()))){
					sb.append("[多期暂缴年数为空]");
				}
			}
			if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getPaymentInfo().getCurrencyCode()))){
				grpInsurAppl.getPaymentInfo().setCurrencyCode("CNY");
			}


		}
		if(null==grpInsurAppl.getApplState()){
			sb.append("[投保要约为空]");
		}else{
			if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getIpsnNum()))){
				sb.append("[投保人数为空]");
			}
			if(0==grpInsurAppl.getApplState().getIpsnNum()){
				sb.append("[投保人数为0]");
			}
			if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getSumFaceAmnt()))){
				sb.append("[总保额为空]");
			}
			if(0==grpInsurAppl.getApplState().getSumFaceAmnt()){
				sb.append("[总保额为0]");
			}
			if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getSumPremium()))){
				sb.append("[总保费为空]");
			}
			if(0==grpInsurAppl.getApplState().getSumPremium()){
				sb.append("[总保费为0]");
			}
			//投保人数，时间，保额，保费是基本数据类型，有默认初始值。故没有非空校验。
			if("G".equals(grpInsurAppl.getCntrType())){
				if(StringUtils.isBlank(grpInsurAppl.getApplState().getInsurDurUnit())){
					sb.append("[保险期类型为空]");
				}
			}
			if(StringUtils.isBlank(grpInsurAppl.getApplState().getInforceDateType())){
				sb.append("[生效日类型为空]");
			}
			if("1".equals(grpInsurAppl.getApplState().getInforceDateType())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getDesignForceDate()))){
					sb.append("[指定生效日为空]");
				}
			}
			if(StringUtils.isBlank(grpInsurAppl.getApplState().getIsPrePrint())){
				grpInsurAppl.getApplState().setIsPrePrint("0");
			}
			if(StringUtils.isBlank(grpInsurAppl.getApplState().getIsFreForce())){
				grpInsurAppl.getApplState().setIsFreForce("0");
			}
			if("1".equals(grpInsurAppl.getApplState().getIsFreForce()) || "Y".equals(grpInsurAppl.getApplState().getIsFreForce())){
				if(StringUtils.isBlank(String.valueOf(grpInsurAppl.getApplState().getForceFre()))){
					sb.append("[生效频率为空]");
				}
			}			
			int lenPolicy=grpInsurAppl.getApplState().getPolicyList().size();
			if(lenPolicy>0){
				int num=0;
				for (int i = 0; i < lenPolicy; i++) {
					Policy policy=grpInsurAppl.getApplState().getPolicyList().get(i);
					if(StringUtils.isBlank(policy.getPolCode())){
						sb.append("第"+i+"列"+"[险种为空]");
					}
					if(StringUtils.isBlank(String.valueOf(policy.getInsurDur()))){
						sb.append("第"+i+"列"+"[保险期间为空]");
					}
					if(StringUtils.isBlank(String.valueOf(policy.getFaceAmnt()))){
						sb.append("第"+i+"列"+"[险种保额为空]");
					}
					if(0==policy.getFaceAmnt()){
						sb.append("第"+i+"列"+"[险种保额为0]");
					}
					if(StringUtils.isBlank(String.valueOf(policy.getPremium()))){
						sb.append("第"+i+"列"+"[实际保费为空]");
					}
					if(0==policy.getPremium()){
						sb.append("第"+i+"列"+"[实际保费为0]");
					}
					if(StringUtils.isBlank(String.valueOf(policy.getStdPremium()))){
						sb.append("第"+i+"列"+"[标准保费为空]");
					}
					if(0==policy.getStdPremium()){
						sb.append("第"+i+"列"+"[标准保费为0]");
					}
					if(StringUtils.isBlank(String.valueOf(policy.getPolIpsnNum()))){
						sb.append("第"+i+"列"+"[险种投保人数为空]");
					}
					if(0==policy.getPolIpsnNum()){
						sb.append("第"+i+"列"+"[险种投保人数为0]");
					}

					if(StringUtils.isBlank(policy.getCntrNo())){
						sb.append("第"+i+"列"+"[分保单号为空]");
					}
					if(StringUtils.isBlank(policy.getMrCode())){
						sb.append("第"+i+"列"+"[主附险性质为空]");
					}
					if("M".equals(policy.getMrCode())){
						num = 1;
					}
					if(StringUtils.isBlank(String.valueOf(policy.getMoneyinItrvlMon()))){
						sb.append("第"+i+"列"+"[缴费间隔月为空]");
					}
					List<SubPolicy> listSubPolicy =	policy.getSubPolicyList();
					if(null!=listSubPolicy){
						for (int j = 0; j < listSubPolicy.size(); j++) {
							SubPolicy subPolicy	= listSubPolicy.get(j);

							if(StringUtils.isBlank(subPolicy.getSubPolCode())){
								sb.append("[子险种为空]");
							}
							if(StringUtils.isBlank(String.valueOf(subPolicy.getSubPolAmnt()))){
								sb.append("[子险种保额为空]");
							}
							if(0==subPolicy.getSubPolAmnt()){
								sb.append("[子险种保额为0]");
							}
							if(StringUtils.isBlank(String.valueOf(subPolicy.getSubPremium()))){
								sb.append("[子险种实际保费为空]");
							}
							if(0==subPolicy.getSubPremium()){
								sb.append("[子险种实际保费为0]");
							}
							if(StringUtils.isBlank(String.valueOf(subPolicy.getSubStdPremium()))){
								sb.append("[子险种标准保费为空]");
							}
							if(0==subPolicy.getSubStdPremium()){
								sb.append("[子险种标准保费不能为0.0]");
							}

						}
					}
				}//end for
				if(0==num){
					sb.append("[没有主险mrCode==M]");
				}
			}else{
				sb.append("[险种（多条）policyList为空]");
			}//end if--PolicyList
		}//end if--ApplState

		List<IpsnStateGrp> listIpsnStateGrp =	grpInsurAppl.getIpsnStateGrpList();
		if(null!=listIpsnStateGrp){
			for (int j = 0; j < listIpsnStateGrp.size(); j++) {
				IpsnStateGrp ipsnStateGrp = listIpsnStateGrp.get(j);
				if(StringUtils.isBlank(ipsnStateGrp.getIpsnGrpType())){
					sb.append("[分组类型为空]");
				}
				if(StringUtils.isBlank(String.valueOf(ipsnStateGrp.getIpsnGrpNo()))){
					sb.append("[要约属组编号为空]");
				}
				if(0==ipsnStateGrp.getIpsnGrpNo()){
					sb.append("[要约属组编号为0]");
				}
				if(StringUtils.isBlank(ipsnStateGrp.getIpsnGrpName())){
					sb.append("[要约属组名称为空]");
				}
				if("1".equals(ipsnStateGrp.getIpsnGrpType())){
					//					if(StringUtils.isBlank(ipsnStateGrp.getOccClassCode())){
					//						sb.append("[行业类别为空]");
					//					}
					//					if(StringUtils.isBlank(ipsnStateGrp.getIpsnOccCode())){
					//						sb.append("[职业代码为空]");
					//					}
					if(StringUtils.isBlank(String.valueOf(ipsnStateGrp.getIpsnGrpNum()))){
						sb.append("[要约属组人数为空]");
					}
				}//end if
				List<GrpPolicy> listGrpPolicy =	ipsnStateGrp.getGrpPolicyList();
				if(null!=listGrpPolicy){
					for (int i = 0; i < listGrpPolicy.size(); i++) {
						GrpPolicy grpPolicy =	listGrpPolicy.get(i);
						if(StringUtils.isBlank(grpPolicy.getPolCode())){
							sb.append("[grpPolicyList险种代码为空]");
						}
						if("1".equals(ipsnStateGrp.getIpsnGrpType())){
							if(StringUtils.isBlank(String.valueOf(grpPolicy.getFaceAmnt()))){
								sb.append("[grpPolicyList险种保额为空]");
							}
							if(0==grpPolicy.getFaceAmnt()){
								sb.append("[grpPolicyList险种保额为0]");
							}
							if(StringUtils.isBlank(String.valueOf(grpPolicy.getPremium()))){
								sb.append("[grpPolicyList实际保费为空]");
							}
							if(0==grpPolicy.getPremium()){
								sb.append("[grpPolicyList实际保费为0]");
							}
						}//end if
					}
				}else{
					sb.append("[grpPolicyList为空]");
				}

			}//end for
		}//end if
		List<IpsnPayGrp> listIpsnPayGrp =	grpInsurAppl.getIpsnPayGrpList();
		if(null!=listIpsnPayGrp){
			for (int i = 0; i < listIpsnPayGrp.size(); i++) {
				IpsnPayGrp IipsnPayGrp	= listIpsnPayGrp.get(i);
				if(StringUtils.isBlank(String.valueOf(IipsnPayGrp.getFeeGrpNo()))){
					sb.append("[收费组属组编号为空]");
				}
				if(StringUtils.isBlank(IipsnPayGrp.getFeeGrpName())){
					sb.append("[收费组属组名称为空]");
				}
				if(StringUtils.isBlank(String.valueOf(IipsnPayGrp.getIpsnGrpNum()))){
					sb.append("[收费属组人数为空]");
				}
				if(StringUtils.isBlank(IipsnPayGrp.getMoneyinType())){
					sb.append("[缴费形式为空]");
				}
				if("T".equals(IipsnPayGrp.getMoneyinType())){
					if(StringUtils.isBlank(IipsnPayGrp.getBankCode())){
						sb.append("[开户银行为空]");
					}
					if(StringUtils.isBlank(IipsnPayGrp.getBankaccName())){
						sb.append("[开户名称为空]");
					}
					if(StringUtils.isBlank(IipsnPayGrp.getBankaccNo())){
						sb.append("[银行账号为空]");
					}
				}
				if("Q".equals(IipsnPayGrp.getMoneyinType())){
					if(StringUtils.isBlank(IipsnPayGrp.getBankChequeNo())){
						sb.append("[支票号为空]");
					}
				}
			}
		}
		if(StringUtils.isBlank(grpInsurAppl.getMgrBranchNo())){
			sb.append("[管理机构号为空]");
		}

		List<OrgTree> listOrgTree=	grpInsurAppl.getOrgTreeList();
		if(null!=listOrgTree){
			for (int j = 0; j < listOrgTree.size(); j++) {
				OrgTree  orgTree = listOrgTree.get(j);
				if(StringUtils.isBlank(String.valueOf(orgTree.getLevelCode()))){
					sb.append("第"+j+"列"+"[层级代码为空]");
				}
				if(StringUtils.isEmpty(orgTree.getLevelCode())){
					sb.append("第"+j+"列"+"[层级代码为0]");
				}
				if(StringUtils.isBlank(String.valueOf(orgTree.getPrioLevelCode()))){
					sb.append("第"+j+"列"+"[上级层级代码为空]");
				}
				if(StringUtils.isEmpty(orgTree.getPrioLevelCode())){
					sb.append("第"+j+"列"+"[上级层级代码为0]");
				}
				if(StringUtils.isBlank(orgTree.getIsPaid())){
					sb.append("第"+j+"列"+"[是否缴费为空]");
				}
				if(StringUtils.isBlank(orgTree.getMtnOpt())){
					sb.append("第"+j+"列"+"[保全选项为空]");
				}
				if(StringUtils.isBlank(orgTree.getServiceOpt())){
					sb.append("第"+j+"列"+"[服务指派为空]");
				}
				if(StringUtils.isBlank(orgTree.getReceiptOpt())){
					sb.append("第"+j+"列"+"[发票选项为空]");
				}

				if(StringUtils.isBlank(orgTree.getNodeType())){
					sb.append("第"+j+"列"+"[节点类型为空]");
				}
				if(StringUtils.isBlank(orgTree.getIsRoot())){
					sb.append("第"+j+"列"+"[是否根节点为空]");
				}
				if("Y".equals(orgTree.getIsPaid())){
					if(StringUtils.isBlank(orgTree.getMoneyinType())){
						sb.append("第"+j+"列"+"[缴费形式为空]");
					}
					if(StringUtils.isBlank(String.valueOf(orgTree.getNodePayAmnt()))){
						sb.append("第"+j+"列"+"[节点交费金额为空]");
					}
					if(StringUtils.isBlank(String.valueOf(orgTree.getBankCode()))){
						sb.append("第"+j+"列"+"[开户银行为空]");
					}
					if(StringUtils.isBlank(String.valueOf(orgTree.getBankaccName()))){
						sb.append("第"+j+"列"+"[开户名称为空]");
					}
					if(StringUtils.isBlank(String.valueOf(orgTree.getBankaccNo()))){
						sb.append("第"+j+"列"+"[银行账号为空]");
					}
				}
				if("0".equals(orgTree.getNodeType())){
					GrpHolderInfo grpHolderInfo  =	orgTree.getGrpHolderInfo();
					if(null==grpHolderInfo){
						sb.append("[隶属于orgTree的团体客户信息为空]");
					}else{
						if(StringUtils.isBlank(grpHolderInfo.getPartyId())){
							sb.append("[隶属于orgTree的[投保单位cmds客户号]为空]");
						}
						if(StringUtils.isBlank(grpHolderInfo.getGrpCustNo())){
							sb.append("[隶属于orgTree的[团体客户号]为空]");
						}
						if(StringUtils.isBlank(grpHolderInfo.getGrpName())){
							sb.append("[隶属于orgTree的[单位名称]为空]");
						}
						if(StringUtils.isBlank(grpHolderInfo.getGrpIdType())){
							sb.append("[隶属于orgTree的[团体客户证件类别]为空]");
						}
						if(StringUtils.isBlank(grpHolderInfo.getGrpIdNo())){
							sb.append("[隶属于orgTree的[团体客户证件号码]为空]");
						}
					}//end if-团体客户信息是否为空

				}//end if-是否有团体客户信息
			}//end for 
		}

		return sb.toString();
	}
	/**
	 * 要约分组-合并其中的分组。
	 * 逻辑：遍历要约分组集合，获得其中的一个分组，遍历该分组的险种集合，把险种集合里的险种代码做截取前3位操作，之后放入map1集合中，
	 * 险种放入map1集合时初始值为1，如果有重复每次加一。遍历完险种之后，调用getIpsnStateGrp方法，获得合并后的一个分组，放入新建的要约分组集合listIpsnStateGrp中。
	 * 按照上面的逻辑遍历剩下的要约分组，最后把新建的listIpsnStateGrp返回，
	 * */
	public List<IpsnStateGrp> getGrpIpsnStateGrpList(String cgNo){
		List<IpsnStateGrp>  listIpsnStateGrp = new ArrayList<>();
		Map<String, Object> map=new HashMap<>();
		map.put("cgNo", cgNo);
		GrpInsurAppl   grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		List<IpsnStateGrp>  listIpsnStateGrpOld  =	grpInsurAppl.getIpsnStateGrpList();
		//非空判断，如果不为空说明有分组
		if(!listIpsnStateGrpOld.isEmpty()){
			//遍历分组
			for (IpsnStateGrp ipsnStateGrp : listIpsnStateGrpOld) {
				Map<String, Integer> map1 = new HashMap<>();
				//遍历分组的险种信息，获得险种
				for (GrpPolicy grpPolicy :ipsnStateGrp.getGrpPolicyList()) {
					String pol =grpPolicy.getPolCode().substring(0, 3);
					if(map1.containsKey(pol)){
						//将险种集合中的险种放入map1集合
						map1.put(pol, map1.get(pol)+1);
					}else{
						map1.put(pol, 1);
					}
				}
				//遍历分组-得到其中某一个分组---判断该分组是否有需要合并，如果需要就合并，不需要就直接返回。
				IpsnStateGrp  ipsnStateGrpNew = getIpsnStateGrp(ipsnStateGrp, map1);
				listIpsnStateGrp.add(ipsnStateGrpNew);
			}//end for-遍历分组
		}//end if-判断是否需要合并
		return listIpsnStateGrp;
	}
	/**
	 * 要约分组-得到其中某一个分组
	 * 逻辑：首先判断该分组是否有需要合并，如果不需要就遍历该条要约分组的险种集合，把险种做截取前3位操作，之后返回该条要约分组；
	 * 如果需要合并，首先遍历map1集合，把需要合并的险种放到list中，把不需要分组的放入list2中，之后遍历list遍历险种把需要合并的险种进行合并，放入新建的险种集合grpPolicyList中，
	 * 遍历list2遍历险种，把险种做截取前3位操作，放入新建的险种集合grpPolicyList中，最后返回该条需要合并的要约分组。
	 * 
	 * */
	public IpsnStateGrp getIpsnStateGrp(IpsnStateGrp ipsnStateGrp,Map<String, Integer> map1){
		List<GrpPolicy> grpPolicyList = new ArrayList<>();
		List<String> list = new ArrayList<>();
		List<String> list2 = new ArrayList<>();
		Double faceAmnt = 0d;
		Double premium = 0d;
		Double stdPremium = 0d;
		//对[承保费率][费率浮动幅度]做精度限定
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		//判断是否有需要合并的险种数组。
		if(map1.size()!=ipsnStateGrp.getGrpPolicyList().size()){
			//此时说明有需要合并的数组，遍历map获得需要合并的险种代码
			for (Entry<String, Integer> entry   :map1.entrySet()) {
				int intValue = Integer.valueOf(entry.getValue());
				//获取需要合并的险种代码
				if(intValue>1){
					list.add(String.valueOf(entry.getKey()));
				}else{
					list2.add(String.valueOf(entry.getKey()));
				}
			}//遍历map1集合
			for (int i = 0; i < list.size(); i++) {
				String mrCode ="";
				//遍历分组的险种信息，进行合并					  
				for (GrpPolicy grpPolicy :ipsnStateGrp.getGrpPolicyList()) {
					String pol =grpPolicy.getPolCode().substring(0, 3);
					if(list.get(i).equals(pol)){
						faceAmnt += grpPolicy.getFaceAmnt();
						premium += grpPolicy.getPremium();
						stdPremium += grpPolicy.getStdPremium();
						mrCode = grpPolicy.getMrCode();
					}//需要合并的某一条数据
				}//end for 需要合并的多条数据
				GrpPolicy grpPolicyNew = new GrpPolicy();
				grpPolicyNew.setFaceAmnt(faceAmnt);
				grpPolicyNew.setMrCode(mrCode);
				grpPolicyNew.setPolCode(list.get(i));
				grpPolicyNew.setPremium(premium);
				grpPolicyNew.setStdPremium(stdPremium);
				//获得[承保费率]值，并赋值。
				String PremRate = dcmFmt.format((premium/faceAmnt)*1000);
				grpPolicyNew.setPremRate(Double.valueOf(PremRate));
				//				//获得[费率浮动幅度]值，并赋值。
				//				String PremDiscount = dcmFmt.format(premium/stdPremium);
				//				grpPolicyNew.setPremDiscount(Double.valueOf(PremDiscount));
				grpPolicyList.add(grpPolicyNew);
			}
			for (int i = 0; i < list2.size(); i++) {
				//遍历分组的险种信息，获得不需要合并的数据
				for (GrpPolicy grpPolicy :ipsnStateGrp.getGrpPolicyList()) {
					String pol =grpPolicy.getPolCode().substring(0, 3);
					if(list2.get(i).equals(pol)){
						grpPolicy.setPolCode(pol);
						faceAmnt = grpPolicy.getFaceAmnt();
						premium = grpPolicy.getPremium();
						//获得[承保费率]值，并赋值。
						String PremRate = dcmFmt.format((premium/faceAmnt)*1000);
						grpPolicy.setPremRate(Double.valueOf(PremRate));
						//将不需要合并的数据放入新的集合中
						grpPolicyList.add(grpPolicy);
					}//end if 
				}//end for 不需要合并的多条数据
			}
			ipsnStateGrp.setGrpPolicyList(grpPolicyList);
			return ipsnStateGrp;
		}else{
			//遍历分组的险种信息，	对不需要合并的把子险种代码改为险种代码			  
			for (GrpPolicy grpPolicy :ipsnStateGrp.getGrpPolicyList()) {
				String pol =grpPolicy.getPolCode().substring(0, 3);
				grpPolicy.setPolCode(pol);
				faceAmnt = grpPolicy.getFaceAmnt();
				premium = grpPolicy.getPremium();
				//获得[承保费率]值，并赋值。
				String PremRate = dcmFmt.format((premium/faceAmnt)*1000);
				grpPolicy.setPremRate(Double.valueOf(PremRate));
				grpPolicyList.add(grpPolicy);
			}//end for 需要合并的多条数据
			ipsnStateGrp.setGrpPolicyList(grpPolicyList);
			return ipsnStateGrp;
		}
	}
	//回退后判断是清汇还是团单，之后调用打印。
	public void doTaskCntrPrintService(String applNo){
		Map<String,Object> maps = new HashMap<>();
		maps.put("applNo", applNo);
		//查询轨迹，获得保单当前状态
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, maps);
		if("G".equals(grpInsurAppl.getCntrType())){
			taskCntrPrintService.cntrGrpPrint(applNo);
		}else{
			taskCntrPrintService.cntrLstPrint(applNo);
		}
	}
	public List<PlnmioRec> getPlnmioRecList(MioPlnmioInfo mioPlnmioInfo){
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//遍历应收
		for (PlnmioRec plnmioRec :mioPlnmioInfo.getPlnmioRecList()) {
			//筛选分期应收数据
			if("29".equals(plnmioRec.getMtnItemCode()) && "PS".equals(plnmioRec.getMioItemCode())){
				plnmioRecList.add(plnmioRec);
			}
		}
		return plnmioRecList;
	}
	//抽取核保结论的险种加费，放到保单要约中
	public ApplState getApplUdwResult(String applNo,GrpInsurAppl grpInsurAppl){
		List<Policy> newPolicyList = new ArrayList<>();
		//查询收付费相关信息类，获得应收数据集合
		Map<String,Object> maps = new HashMap<>();
		maps.put("businessKey", applNo);
		ApplUdwResult applUdwResult = (ApplUdwResult) mongoBaseDao.findOne(ApplUdwResult.class, maps);
		List<UdwPolResult>  udwPolResultList =	applUdwResult.getUdwPolResults();
		List<Policy> policyList =grpInsurAppl.getApplState().getPolicyList();
		for (UdwPolResult udwPolResult : udwPolResultList) {
			for (Policy policy :policyList) {
				if(udwPolResult.getPolCode().equals(policy.getPolCode())){
					TuPolResult  tuPolResult  = new TuPolResult();
					tuPolResult.setAddFeeAmnt(udwPolResult.getAddFeeAmnt());
					tuPolResult.setAddFeeYear(udwPolResult.getAddFeeYear());
					tuPolResult.setUdwOpinion(udwPolResult.getUdwOpinion());
					tuPolResult.setUdwResult(udwPolResult.getUdwResult());
					policy.setTuPolResult(tuPolResult);
					newPolicyList.add(policy);
				}
			}
		}
		//用于本方法返回
		ApplState applState  =	grpInsurAppl.getApplState();
		applState.setPolicyList(newPolicyList);
		//用于修改数据库
		Update updatepolicyList=new Update();
		updatepolicyList.set("applState.policyList", newPolicyList);//
		Map<String,Object> map = new HashMap<>();
		map.put("applNo", applNo);
		mongoBaseDao.update(GrpInsurAppl.class, map, updatepolicyList);
		return applState;
	}



	/*********************************************************
	 *  方法名称:cfcGetAmorFlag
	 *	功能描述:产生分期交费的多期应收
	 *	参数：
	 *		polCode       险种代码
	 * 		insurDurDnit  保险期类型(调产品组获得---Y-年 M-月 D-天 A-岁 C-约定  )
	 * 		insurDur 	      保险期(调产品组获得 )
	 * 		moneyinItrvl  缴费方式(M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期)
	 * 
	 *******************************************************/
	public int cfcGetAmorFlag(String insurDurDnit,  double insurDur ,String moneyinItrvl){
		double moneyinDur =0.0;
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
		double subtr=    b1.multiply(b5).doubleValue();
		//保险期间乘以12
		BigDecimal b6 = new BigDecimal(Double.toString(subtr));
		//判断缴费方式
		if ("O".equals(moneyinItrvl) || "W".equals(moneyinItrvl) ||"Y".equals(moneyinItrvl) || "X".equals(moneyinItrvl)){
			moneyinDur=0.0;
		}else{//保险期类型
			if ("M".equals(insurDurDnit)){
				switch(moneyinItrvl)//缴费方式
				{
				case "M"://保险期间除以1
					moneyinDur=b1.divide(b2).doubleValue();
					break;
				case "Q"://保险期间除以4
					moneyinDur=b1.divide(b3).doubleValue();
					break;
				case "H"://保险期间除以6
					moneyinDur=b1.divide(b4).doubleValue();
					break;
				default:		      
					moneyinDur=0.0;
				}       
			}//保险期类型 
			if ("Y".equals(insurDurDnit)){
				switch(moneyinItrvl)//缴费方式
				{
				case "M"://保险期间乘以12 在除以1
					moneyinDur=b6.divide(b2).doubleValue();
					break;
				case "Q"://保险期间乘以12 在除以4
					moneyinDur=b6.divide(b3).doubleValue();
					break;
				case "H"://保险期间乘以12 在除以6
					moneyinDur=b6.divide(b4).doubleValue();
					break;
				default:
					moneyinDur=0.0;
					break;
				}
			} //end if
		}//end if
		return (int) moneyinDur;
	}

	public	 String cfcComputeDate(String lstrBeginDate,int llPassCount,String lstrFlag){

		//判断日期合法性，默认合法。如果输入的日期之间间隔符不同 或者 月份日期不符合形式 会自动加减，若输入不是日期 字符，程序默认当前日期。
		@SuppressWarnings("unused")
		boolean convertSuccess=true;
		//格式化日期
		SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		//日历
		Calendar c = Calendar.getInstance();
		try {//设置时间为 输入的时间
			c.setTime(df.parse(lstrBeginDate));
		} catch (ParseException e) {
			//若输入不是日期 字符，程序默认当前日期。
			convertSuccess=false;
		} 
		//判断输入数字的长度
		Integer numLen=llPassCount;
		if(numLen.toString().length()<=9){	
			//判断标志，判断输入数据是 年或是 月 或是日。年-"Y"、月-"M"、日-"D"。
			if(lstrFlag.equals("Y")){
				c.add(Calendar.YEAR, llPassCount); // 目前時間加
				return cfcComputeDate(df.format((c.getTime())), 0, "D");    	
			}else if(lstrFlag.equals("M")){
				c.add(Calendar.MONTH, llPassCount); // 目前時間加
				return df.format((c.getTime()));  
			}else if(lstrFlag.equals("D")){
				c.add(Calendar.DAY_OF_WEEK, llPassCount); // 目前時間加
				return df.format((c.getTime()));  
			}else{
				return "标志输入错误，请重新输入！";
			}
		}else{
			return "输入数字长度不能超过10位，请重新输入！";
		}
	}

}

