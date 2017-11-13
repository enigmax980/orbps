package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.halo.core.batch.annotation.Schedule;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.pcms.bo.MioFirstPremChkBo;
import com.newcore.orbps.models.pcms.bo.MioFirstPremChkFramBo;
import com.newcore.orbps.models.pcms.bo.MioFirstPremchkRetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.MioFirstPremchkService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.MioLogDao;
import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PLNMIO_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 收费检查定时任务
 * @author JCC
 * 2016年12月3日 10:10:09
 * 每隔一分钟执行一次。
 */
@Schedule(cron = "0 0/1 * * * ?") 
@Service("TaskMoneyinCheckServiceQuartz")
@DisallowConcurrentExecution
public class TaskMoneyinCheckServiceJob implements Job{
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(TaskMoneyinCheckServiceJob.class);

	@Autowired
	private TaskProcessService taskProcessServiceDascClient;
	
	@Autowired
	MioFirstPremchkService resulMioFirstPremchkServiceClient;
	
	@Autowired
	PlnmioCommonDao plnmioCommonDao;
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Autowired
	MioLogDao mioLogDao;
	
	private final String CQ_TYPE="CQ"; 	  //超期类别
	private final String WCQ_TYPE="WCQ";  //未超期类别
	private final String IPSN_TYPE="IPSN";//被保人类别
	private final String FEE_TYPE="FEE";  //收费组类别

	@AsynCall
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//从产生应收付流水任务表STATUS:K=检查处理中， N=新建，E=检查失败，D=保单作废，C=检查通过，S=冻结成功，W=冻结处理中，F=冻结处理失败
		logger.info("执行方法[收费检查定时任务]:开始运行！");
		// 1.从产生应收付流水任务表查询STATUS = N新增或者E错误的数据
		String [] status = {"N","E"};
		SqlRowSet row = plnmioCommonDao.queryMoneyInCheckQueue(status);
		while(row.next()){
			String taskId=row.getString(1);		 //任务ID
			String businessKey=row.getString(2); //业务流水号
			try{
				logger.info("执行方法[收费检查定时任务]:【任务ID："+taskId+"】>>>>【业务流水号:"+businessKey+"】");
				//锁定当前处理数据 status改成K
				plnmioCommonDao.updateMoneyInCheckStatus("K",taskId);
				//取出应收数据集合
				Map<String,String> param =  new HashMap<>();
				param.put("cntr_no", businessKey);	//投保单号
				param.put("mio_class", MIO_CLASS.RECEIVABLES.getKey());	//应收1
				param.put("mio_Item_Code", MIO_ITEM_CODE.FA.getKey());	//应收FA
				List<PlnmioRec> plnmioRecList = plnmioRecDao.queryPlnmioRecList(param);
				GrpInsurAppl grp = procMioInfoDao.getGrpInsurAppl(businessKey);
				//判断该保单是银行转账
				if(!ProcMioInfoUtils.isBankTrans(grp)){	
					logger.info("执行方法[收费检查定时任务]:单笔非银行转账业务！["+taskId+"]["+businessKey+"]");
					//处理单笔非银行转账业务
					PlnmioRec plnmioRec = plnmioRecList.get(0);
					doNonBankTransData(plnmioRec,taskId,businessKey);
				}else{ //银行转账业务
					logger.info("执行方法[收费检查定时任务]:银行转账业务！["+taskId+"]["+businessKey+"]");
					int isOverTime = 0;
					if(grp.getPaymentInfo().getForeExpDate() != null){
						Date foreExpDate= grp.getPaymentInfo().getForeExpDate();//首期扣款日期
						Date nowTime = new Date(); 	//当前系统时间 
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						String data1 = dateFormat.format(nowTime); 
						String data2 = dateFormat.format(foreExpDate);
						isOverTime = data1.compareTo(data2); 
						logger.info("执行方法[收费检查定时任务]:首期扣款日期是否超期["+(isOverTime<1)+"]!["+taskId+"]["+businessKey+"]");
					}
					//实收数据集合
					List<MioLog> mioLogList = mioLogDao.queryMioLogList(param);
					//判断首期扣款日期是否超期
					if(isOverTime < 1){
						//未超期业务处理
						if(mioLogList.isEmpty()){
							logger.error("执行方法[收费检查定时任务]:无实收完成的数据!["+taskId+"]["+businessKey+"]");
							plnmioCommonDao.updateMoneyInCheckStatus("E",taskId);
							continue;
						}
						//处理未超期:判断应收数据是否全部实收
						doNoOverTimeData(plnmioRecList,taskId,businessKey,mioLogList,grp);
					}else{
						//已超期业务处理		
						doOverTimeDate(plnmioRecList,taskId,businessKey,mioLogList,grp);
					}
				}
			}catch(Exception ex){
				logger.error("【执行方法:收费检查定时任务失败["+taskId+"]["+businessKey+"]】"+ex);
				plnmioCommonDao.updateMoneyInCheckStatus("E",taskId);
			}
		}
	}
		
	/**
	 * 处理超期银行转账数据
	 * @param plnmioRecList 应收数据
	 * @param taskId 任务流ID
	 * @param businessKey 投保单号
	 * @param mioList 实收数据
	 * @param grp 保单信息
	 * @return 
	 * 若存在应收数据为在途的，返回false
	 */
	private boolean doOverTimeDate(List<PlnmioRec> plnmioRecList, String taskId, String businessKey,
			List<MioLog> mioLogList, GrpInsurAppl grp) {
		List<PlnmioRec> ztPlnList = new ArrayList<>(); //在途应收数据集合：ztPlnList
		List<PlnmioRec> ssPlnList = new ArrayList<>(); //完成实收的应收数据集合：ssPlnList
		List<PlnmioRec> wsPlnList = new ArrayList<>(); //未完成实收的应收数据集合：wsPlnList
		List<PlnmioRec> levelPlnList = new ArrayList<>(); //单位、组织架构树应收数据集合：levelPlnList
		List<PlnmioRec> ipsnPlnList = new ArrayList<>(); //被保人，收费组应收数据集合：ipsnPlnList
		Map<Long,BigDecimal> ipsnMap = new HashMap<>();//被保人缴费实收Map,key=应收ID
		Map<Long,BigDecimal> feeMap = new HashMap<>(); //收费组缴费实收Map，key=收费组号					
		for(PlnmioRec plnmioRec:plnmioRecList){
			if(PLNMIO_STATE.ON_THE_WAY.getKey().equals(plnmioRec.getProcStat())){
				//"T", "在途"
				ztPlnList.add(plnmioRec);
			}else if(PLNMIO_STATE.PAID.getKey().equals(plnmioRec.getProcStat())){
				//"S", "实收"
				ssPlnList.add(plnmioRec);
				if("I".equals(plnmioRec.getMultiPayAccType())){ //被保人缴费
					ipsnMap.put(plnmioRec.getPlnmioRecId(), plnmioRec.getAmnt());
				}else if("P".equals(plnmioRec.getMultiPayAccType())){//收费组缴费
					feeMap.put(plnmioRec.getFeeGrpNo(), plnmioRec.getAmnt());
				}
			}else if(PLNMIO_STATE.UNCOLLECTED.getKey().equals(plnmioRec.getProcStat())){
				//"N", "未收"
				wsPlnList.add(plnmioRec);
			}
			//账户所属类别【O:组织架构树应收,I:被保人应收,P:收费组产生应收】
			if("O".equals(plnmioRec.getMultiPayAccType())){
				levelPlnList.add(plnmioRec);
			}else{
				ipsnPlnList.add(plnmioRec);
			}
		}
		//判断此单是否满足，无在途，且有部分数据已经实收
		boolean isTrue = false;
		String message="此保单存在的在途或者无完成实收的应收数据！";
		if(ztPlnList.isEmpty() && !ssPlnList.isEmpty()){
			//判断保费来源：
			if(!levelPlnList.isEmpty() && !ipsnPlnList.isEmpty()){
				//团体个人共同缴费
				isTrue = checkMioLogAmntIsAmple(levelPlnList, mioLogList, grp,ipsnMap,feeMap);
			}else if(levelPlnList.isEmpty() && !ipsnPlnList.isEmpty()){
				//个人缴费
				isTrue = checkMioLogAmntIsAmpleByPersonal(mioLogList, grp,ipsnMap,feeMap);
			}else{
				//单位或个人缴费操作逻辑
				isTrue =checkMioLogAmntIsAmple(CQ_TYPE,ssPlnList,mioLogList,grp);
			}
			message="不存在实收金额大于等于应收收金额的数据，此保单作废";
		}else if(!ztPlnList.isEmpty()){
			logger.info("执行方法[收费检查定时任务]：存在应收数据为在途状态，结束处理，执行下一条保单！["+taskId+"]["+businessKey+"]");
			return false;
		}
		if(isTrue){
			//经比较后，收费检查通过，进行通后的相应业务处理
			//1.修改未收的应收数据作废D，应收数据对应的被保人作废O
			changePlnmioRecProcStat(wsPlnList,grp,PLNMIO_STATE.INVALID.getKey());
			//2.修改保单收费检查日期，添加收费检查操作轨迹
			doUpdateStat(businessKey,taskId,"C");
			logger.info("执行方法[收费检查定时任务]：执行成功！["+taskId+"]["+businessKey+"]");
		}else{
			//1.修改收费检查表中状态D  1:N新建，2：E失败，3：D保费，4：C成功
			plnmioCommonDao.updateMoneyInCheckStatus("D",taskId);
			//2.修改应收数据作废
			StringBuilder sbr = new StringBuilder();
			for(PlnmioRec pln: plnmioRecList){
				sbr.append(pln.getPlnmioRecId()+",");
			}
			//1.应收记录的状态都改为实收:N未收  D作废  S实收，S成功，F失败
			Map<String,String> paramMap = new HashMap<String,String>();
			paramMap.put("proc_stat", PLNMIO_STATE.INVALID.getKey());
			plnmioRecDao.updatePlnmioRecByIds(sbr.toString().substring(0, sbr.length()-1), paramMap);
			//3保单对应被保人作废
	    	Map<String,Object> upMap = new HashMap<>();
	    	upMap.put("applNo", businessKey);
	    	upMap.put("procStat", "N");
	    	Update gupInsUpdate = new Update();
	    	gupInsUpdate.set("procStat", "O");
    		gupInsUpdate.set("remark","扣款超过截止日期，扣款失败");
	    	mongoBaseDao.updateAll(GrpInsured.class, upMap, gupInsUpdate); 
	    	logger.error("执行方法[收费检查定时任务]:"+message+"!["+taskId+"]["+businessKey+"]");
		}
		return true;
	}

	

	/**
	 * 处理未超期银行转账数据
	 * @param plnmioRecList 应收数据
	 * @param taskId 任务流ID
	 * @param businessKey 投保单号
	 * @param mioList 实收数据
	 * @param grp 保单信息
	 */
	private void doNoOverTimeData(List<PlnmioRec> plnmioRecList, String taskId, String businessKey, List<MioLog> mioList,GrpInsurAppl grp) {
		//实收应收数据集合：ssPlnList
		List<PlnmioRec> ssPlnList = new ArrayList<>();
		for(PlnmioRec plnmioRec:plnmioRecList){
			// PAID("S", "实收")
			if(PLNMIO_STATE.PAID.getKey().equals(plnmioRec.getProcStat())){
				ssPlnList.add(plnmioRec);
			}
		}
		//每一笔应收数据对应的实收金额都大于等于应收金额,则isTrue=true
		boolean isTrue = false;
		String message="此保单对应的应收数据未全部实收！";
		if(plnmioRecList.size() == ssPlnList.size()){
			message="存在实收金额小于应收收金额的数据!";
			isTrue =checkMioLogAmntIsAmple(WCQ_TYPE,ssPlnList,mioList,grp);
		}
		if(isTrue){
			//经比较后，每一笔应收数据对应的实收金额都大于等于应收金额，则收费检查通过，进行通后的相应业务处理
			//1.修改该保单对应的有效被保人状态为E
			Map<String,Object> ipsnMap = new HashMap<>();
	    	ipsnMap.put("applNo", businessKey);
	    	ipsnMap.put("procStat", "N");
	    	Update gupInsUpdate = new Update();
	    	gupInsUpdate.set("procStat", "E");
    		gupInsUpdate.set("remark","完成实收");
	    	mongoBaseDao.updateAll(GrpInsured.class, ipsnMap, gupInsUpdate);
	    	//2.修改保单收费检查日期，添加收费检查操作轨迹:STATUS=4：C检查通过
	    	doUpdateStat(businessKey,taskId,"C");
			logger.info("执行方法[收费检查定时任务]：执行成功！["+taskId+"]["+businessKey+"]");
		}else{
			plnmioCommonDao.updateMoneyInCheckStatus("E",taskId);
			logger.error("执行方法[收费检查定时任务]:"+message+"!["+taskId+"]["+businessKey+"]");
		}
	}

	/**
	 * 处理单笔非银行转账业务
	 * @param plnmioRec 应收数据
	 * @param taskId 任务流ID
	 * @param businessKey 投保单号
	 */
	private void doNonBankTransData(PlnmioRec plnmioRec, String taskId, String businessKey) {
		//调用保单辅助平台接口mioFirstPremchkService进行收费检查
		MioFirstPremChkBo mioFirstPremChkBo = setMioFirstPremChkBo(plnmioRec);
		MioFirstPremchkRetInfo infos = resulMioFirstPremchkServiceClient.chMioFirstPremchkService(mioFirstPremChkBo);
		//检查成功后：修改保单的收费检查日期【signDate】，新增一条状态为【收费检查】操作轨迹，修改应收状态【成功S】
		if ("1".equals(infos.getAllMiFinMark())) {
			logger.info("执行方法[收费检查定时任务]:调应接口返回成功，开始处理具体业务["+taskId+"]["+businessKey+"]！");
			String miologDate=getFormatDate(infos.getRetInfo().get(0).getMioLogUpdTime());
			logger.info("执行方法[收费检查定时任务]:调应接口返回成功，开始处理具体业务["+miologDate+"]["+businessKey+"]！");
			//1.修改收费检查表中打印日期
			plnmioCommonDao.updateMoneyInCheckExtKey(miologDate,taskId);
			//2.修改应收状态：实收完成S
			plnmioRecDao.updatePlnmioRecProcStatById(plnmioRec.getPlnmioRecId(), PLNMIO_STATE.PAID.getKey());
			//3.修改保单收费检查日期，添加收费检查操作轨迹:单笔收费检查，不需要冻结账户操作，status改成S
			doUpdateStat(businessKey,taskId,"S");
			//4.调用任务调度平台任务完成接口
			logger.info("执行方法[收费检查定时任务]：收费检查完成，调用流程完成接口！["+taskId+"]["+businessKey+"]");
			TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
			taskProcessRequestBO.setBusinessKey(businessKey);
			taskProcessRequestBO.setTaskId(taskId);// 业务服务参数中获取的任务ID
			taskProcessServiceDascClient.completeTask(taskProcessRequestBO);// 进行任务完成操作
			logger.info("执行方法[收费检查定时任务]：流程完成接口调用结束,执行成功！["+taskId+"]["+businessKey+"]");
		}else{
			logger.info("执行方法[收费检查定时任务]:["+taskId+"]["+businessKey+"]收费检查失败【"+infos.getErrMsg()+"】");
			//处理失败：status改成E
			plnmioCommonDao.updateMoneyInCheckStatus("E",taskId);
		}
	}

	/**
	 * 修改应收数据状态
	 * @param PlnList 应收数据
	 * @param grp 保单信息
	 * @param procStat 
	 * 应收状态【N未收  D作废  S实收，S成功，F失败】
	 */
	private void changePlnmioRecProcStat(List<PlnmioRec> plnmioRecList,GrpInsurAppl grp,String procStat) {
		StringBuilder sbr = new StringBuilder();
		for(PlnmioRec pln: plnmioRecList){
			changeGrpInsuredState(pln, grp,"O");
			sbr.append(pln.getPlnmioRecId()+",");
		}
		Map<String,String> paramMap = new HashMap<String,String>();
		paramMap.put("proc_stat", procStat);
		plnmioRecDao.updatePlnmioRecByIds(sbr.toString().substring(0, sbr.length()-1), paramMap);
	}

	/**
	 * 修改保单的收费检查日期并添加操作轨迹
	 * @param businessKey 投保单号
	 * @param taskId 任务流ID
	 * @param status 状态
	 */
	private void doUpdateStat(String businessKey, String taskId,String status) {	
		//1.修改保单的收费检查日期
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", businessKey);
		Update upAppl = new Update();
		upAppl.set("signDate", new Date());
		mongoBaseDao.update(GrpInsurAppl.class, map, upAppl);
		//2.修改操作轨迹记录
		TraceNode traceNode= new TraceNode();
		traceNode.setPclkBranchNo("");
		traceNode.setPclkNo("");
		traceNode.setPclkName("");
		traceNode.setProcDate(new Date());
		traceNode.setProcStat(NEW_APPL_STATE.PAY_CHECK.getKey());
		mongoBaseDao.updateOperTrace(businessKey,traceNode);
		//3.修改表状态
		plnmioCommonDao.updateMoneyInCheckStatus(status, taskId);
	}

	/**
	 * 判断每一笔应收数据对应的实收金额是否大于等于应收金额
	 * @param types 类型 
	 * 未超期-WCQ,超期且保费来源是1或2-CQ
	 * @param ssPlnList 已经实收的应收数据
	 * @param mioList 实收数据
	 * @param grp 保单信息
	 * @return
	 */
	private boolean checkMioLogAmntIsAmple(String types,List<PlnmioRec> ssPlnList, List<MioLog> mioList,GrpInsurAppl grp) {
		//完成校验的条数
		int sussNum = 0; 	
		//若全部实收完成，则判断每一笔的实收金额是否大于等于应收金额
		for(PlnmioRec ssPln :ssPlnList){
			//ssJe:应收数据对应的实收金额
			BigDecimal ssJe = getMioLogAmnt(ssPln.getPlnmioRecId(),mioList);
			int num=BigDecimalUtils.compareBigDecimal(ssJe,ssPln.getAmnt());
			if(num<0 && WCQ_TYPE.equals(types)){	
				logger.error("执行方法[收费检查定时任务]:["+ssPln.getCntrNo()+"]["+ssPln.getPlnmioRecId()+"]该笔应收的实收金额小于应收金额，请继续收费！");
				return false;
			}else if(num>=0 && WCQ_TYPE.equals(types)) {
				sussNum +=1 ;
			}else if(num<0 && CQ_TYPE.equals(types)) {
				logger.error("执行方法[收费检查定时任务]:["+ssPln.getCntrNo()+"]["+ssPln.getPlnmioRecId()+"]该笔应收的实收金额小于应收金额,此比应收作废！");
				changeGrpInsuredState(ssPln, grp,"O");
			}else if(num >=0 && CQ_TYPE.equals(types)){
				sussNum +=1 ;
				changeGrpInsuredState(ssPln, grp,"E");
			}
		}
		return sussNum>0;
	}
	
	/**
	 * 判断每一笔应收数据对应的实收金额是否大于等于应收金额[超期且保费来源3]
	 * @param levelPlnList 组织架构树信息
	 * @param mioList 实收数据
	 * @param grp 保单数据
	 * @param ipsnMap 被保人缴费实收Map
	 * @param feeMap  收费组缴费实收Map
	 * @return  TODO
	 */
	private boolean checkMioLogAmntIsAmple(List<PlnmioRec> levelPlnList, List<MioLog> mioList,GrpInsurAppl grp, 
			Map<Long, BigDecimal> ipsnMap, Map<Long, BigDecimal> feeMap) {
		//单位，个人共同缴费操作逻辑
		int rows =0;
		List<PlnmioRec> ssLevelList  = new ArrayList<>();
		//筛选出已经实收成功的组织架构树节点
		for(PlnmioRec pln:levelPlnList){
			if(PLNMIO_STATE.PAID.getKey().equals(pln.getProcStat())){
				ssLevelList.add(pln);
			}
		}
		//若无实收完成的组织架构树节点，则此单作废
		if(ssLevelList.isEmpty()){
			logger.info("执行方法[收费检查定时任务]:["+grp.getApplNo()+"]无实收完成的组织架构树节点，则此单作废！");
			return false;
		}
		//遍历完成实收的组织架构树节点，校验架构树下有效被保人是否部分完成实收
		for(PlnmioRec pln:ssLevelList){
			//获取组织架构树下有效被保人信息
			List<GrpInsured> grpList = new ArrayList<>();
			if("0".equals(pln.getLevelCode())){
				grpList= procMioInfoDao.getGrpInsuredList(grp.getApplNo(), null);
			}else{
				List<String> levelList = getLevelList(grp,pln);
				grpList = procMioInfoDao.getGrpInsuredList(grp.getApplNo(), levelList);
			}
			//校验当前组织架构树节点下，是否有完成实收的被保人，有返回ture,无返回false
			boolean isSuss =checkMioLogAmntIsAmple(grp.getApplNo(),grpList,mioList,ipsnMap,feeMap);
			if(isSuss){
				rows++;
			}
		}	
		return rows>0;
	}
	
	/**
	 * 校验个人缴费所收保费是否充足
	 * @param mioList 实收数据
	 * @param grp	保单信息
	 * @param ipsnMap 被保人缴费实收Map
	 * @param feeMap 收费组缴费实收Map
	 * @return
	 */
	private boolean checkMioLogAmntIsAmpleByPersonal( List<MioLog> mioList, GrpInsurAppl grp,
			Map<Long, BigDecimal> ipsnMap, Map<Long, BigDecimal> feeMap) {
		//获取组织架构树下有效被保人信息
		List<GrpInsured> grpList = procMioInfoDao.getGrpInsuredList(grp.getApplNo(), null);
		return checkMioLogAmntIsAmple(grp.getApplNo(),grpList,mioList,ipsnMap,feeMap);
	}
	
	/**
	 * 校验在超期场景下，个人保费是否收取充足
	 * @param applNo 投保单号
	 * @param grpList 被保人信息
	 * @param mioList 实收数据
	 * @param ipsnMap 被保人缴费实收Map
	 * @param feeMap  收费组缴费实收Map
	 * @return TODO
	 */
	private boolean checkMioLogAmntIsAmple(String applNo,List<GrpInsured> grpList, List<MioLog> mioList,
			Map<Long, BigDecimal> ipsnMap, Map<Long, BigDecimal> feeMap) {
		Map<Long,Long> sussFeeMap = new HashMap<>(); 	//收费充足的收费组号
		Map<Long,Long> failFeeMap = new HashMap<>(); 	//收费不充足或者未收费的收费组号
		Map<Long,Long> sussIpsnMap = new HashMap<>(); 	//收费充足的被保人序号
		Map<Long,Long> failIpsnMap = new HashMap<>(); 	//收费不充足或者未收费的被保人序号
		//组织架构树节点对应的被保人数据
		for(GrpInsured grpInsured: grpList){
			if(grpInsured.getAccInfoList() == null || grpInsured.getAccInfoList().isEmpty()){ //收费组缴费
				//若当前被保人的收费组号，不在sussFeeMap和failFeeMap中，则继续校验
				if(!sussFeeMap.containsKey(grpInsured.getFeeGrpNo()) && !failFeeMap.containsKey(grpInsured.getFeeGrpNo())){
					//根据该被保人所属收费组号查询对应的应收是否已经实收
	  				if(feeMap.containsKey(grpInsured.getFeeGrpNo())){ 
	  					//收费组号对应的已经完成实收应收数据的应收金额
	  					BigDecimal ysJe= feeMap.get(grpInsured.getFeeGrpNo());
	  					//收费组对应的应收数据ID
					    long plnID = getPlnmioRecID(FEE_TYPE,grpInsured.getFeeGrpNo(),grpInsured.getApplNo()).get(0);
					    BigDecimal ssJe = getMioLogAmnt(plnID,mioList);
					    //实收金额 与应收金额比较
	  					int num = BigDecimalUtils.compareBigDecimal(ssJe,ysJe);
	  					if(num<0){
	  						//未收取足够保费，则将此条收费组记录保存到对应的Map集合中
	  						failFeeMap.put(grpInsured.getFeeGrpNo(), grpInsured.getFeeGrpNo());			  						
	  					}else{
	  						//收取足够保费，则将此条收费组记录保存到对应的Map集合中
	  						sussFeeMap.put(grpInsured.getFeeGrpNo(), grpInsured.getFeeGrpNo());
	  					}
	  				}else{
	  					//未收取保费时，则将此条收费组记录保存到对应的Map集合中
	  					failFeeMap.put(grpInsured.getFeeGrpNo(), grpInsured.getFeeGrpNo());
	  				}
				}
			}else{//被保人缴费帐号缴费：根据该被保人序号查询到对应的应收数据是否已经实收
				//根据被保人序号查询出对应的应收数据ID,多个缴费帐号对应多条应收数据，有一笔应收数据未实收，此单作废
				List<Long> plnIDList = getPlnmioRecID(IPSN_TYPE,grpInsured.getIpsnNo(),grpInsured.getApplNo());
				int ssNum = plnIDList.size(); //实收条数
				if(ssNum == 0){
					//该被保人未收费,此被保人作废
					failIpsnMap.put(grpInsured.getIpsnNo(), grpInsured.getIpsnNo()); 
				}else if(grpInsured.getAccInfoList().size() == ssNum){
					//该被保人对应的缴费帐号，都收费了
					int rows=0;
					for(long plnRecId: plnIDList){
						if(ipsnMap.containsKey(plnRecId)){  
		  					BigDecimal ysJe = ipsnMap.get(plnRecId);
	  					    BigDecimal ssJe = getMioLogAmnt(plnRecId,mioList);
		  					int num = BigDecimalUtils.compareBigDecimal(ssJe,ysJe);
		  					if(num<0){	
		  						//该被保人下的缴费帐号未收取足够保费,此被保人作废
		  						rows++;
		  						failIpsnMap.put(grpInsured.getIpsnNo(), grpInsured.getIpsnNo());
								break;
		  					}
		  				}else{
		  					//该被保人下的缴费帐号未全部收费,此被保人作废
		  					rows++;
		  					failIpsnMap.put(grpInsured.getIpsnNo(), grpInsured.getIpsnNo());
		  					break;
		  				}
					}
					if(rows == 0){
						//此被保人的缴费帐号下的所有应收数据，都已经实收后，此被保人生效
						sussIpsnMap.put(grpInsured.getIpsnNo(), grpInsured.getIpsnNo());
					}
				}else{
					//该被保人下的缴费帐号未全部收费,此被保人作废
  					failIpsnMap.put(grpInsured.getIpsnNo(), grpInsured.getIpsnNo());
				}
			}
		}
		int sussFeeNum = sussFeeMap.size();
		int sussIpsnNum= sussIpsnMap.size();
		if((sussFeeNum+sussIpsnNum)>0){
			//收费成功的收费组，修改收费组下所有被保人状态：E 生效
			for(Entry<Long,Long> enrtry:sussFeeMap.entrySet()){
				long feeid = enrtry.getKey(); 	//收费组号
				changeGrpInsuredState(FEE_TYPE,feeid, applNo,"E");
			}
			//未收费成功的收费组，修改收费组下所有被保人状态：O 生效
			for(Entry<Long,Long> enrtry:failFeeMap.entrySet()){
				long feeid = enrtry.getKey(); 	//收费组号
				logger.error("执行方法[收费检查定时任务]:["+applNo+"]["+feeid+"]该收费组号未成功收费，此收费组下所有被保人作废！");
				changeGrpInsuredState(FEE_TYPE,feeid, applNo,"O");
			}
			//收费成功的被保人，修改被保人下所有被保人状态：E 生效
			for(Entry<Long,Long> enrtry:sussIpsnMap.entrySet()){
				long ipsnid = enrtry.getKey(); 	//收费组号
				changeGrpInsuredState(IPSN_TYPE,ipsnid, applNo,"E");
			}
			//未收费成功被保人，修改被保人下所有被保人状态：O 生效
			for(Entry<Long,Long> enrtry:failIpsnMap.entrySet()){
				long ipsnid = enrtry.getKey(); 	//收费组号
				logger.error("执行方法[收费检查定时任务]:["+applNo+"]["+ipsnid+"]该收费组号未成功收费，此收费组下所有被保人作废！");
				changeGrpInsuredState(IPSN_TYPE,ipsnid, applNo,"O");
			}
		}
		return (sussFeeNum+sussIpsnNum)>0;
	}
	
	/**
	 * 获取完成实收的应收ID
	 * @param type 类别：IPSN-被保人，FEE-收费组
	 * @param no 被保人序号/收费组号
	 * @param applNo 投保单号
	 * @return
	 */
	private List<Long> getPlnmioRecID(String type,Long no,String applNo) {
		Map<String,String> param = new HashMap<>();
		param.put("cntr_no", applNo);	//投保单号
		param.put("mio_class", MIO_CLASS.RECEIVABLES.getKey());	//应收1
		param.put("mio_Item_Code", MIO_ITEM_CODE.FA.getKey());	//应收FA
		param.put("proc_stat", PLNMIO_STATE.PAID.getKey());	//应收状态：S完成实收
		if(IPSN_TYPE.equals(type)){
			param.put("ipsn_no", no.toString());
		}else if(FEE_TYPE.equals(type)){
			param.put("fee_Grp_No", no.toString());
		}
		List<PlnmioRec> plnmioRecList = plnmioRecDao.queryPlnmioRecList(param);
		List<Long> longList = new ArrayList<>();
		for(PlnmioRec pln:plnmioRecList){
			longList.add(pln.getPlnmioRecId());
		}
		return longList;	
	}

	/**
	 * 获取应收对应的实收金额
	 * @param plnmioRecId 应收ID
	 * @param mioList 实收数据
	 * @return
	 */
	private BigDecimal getMioLogAmnt(Long plnmioRecId, List<MioLog> mioList) {
		//mioLogMap：存放应收ID对应的实收金额   key=应收ID,value=实收金额
		Map<Long,BigDecimal> mioLogMap = new HashMap<>();
		for(MioLog mioLog:mioList){
			mioLogMap.put(mioLog.getPlnmioRecId(), mioLog.getAmnt());
		}
		return mioLogMap.get(plnmioRecId);
	}

	/**
	 * 获取缴费组织架构节点及其不缴费的子节点数据
	 * @param grp 保单信息
	 * @param pln 应收数据
	 * @return
	 */
	private List<String> getLevelList(GrpInsurAppl grp, PlnmioRec pln) {
		Map<String,OrgTree> orgTreeMap = new HashMap<>();
		//noIsPaidMap:存放不需要缴费的组织架构树数据
		Map<String,OrgTree> noIsPaidMap = new HashMap<>();		
		for(OrgTree orgTree :grp.getOrgTreeList()){
			orgTreeMap.put(orgTree.getLevelCode(), orgTree);
			//不需要缴费且不是根节点的组织层次信息 【是否需要缴费ifPay  Y：是；N：否。】 【是否根节点 isRoot Y：是；N：否。】
			if("N".equals(orgTree.getIsPaid()) && "N".equals(orgTree.getIsRoot())){
				noIsPaidMap.put(orgTree.getLevelCode(),orgTree);
			}
		}
		//获取需要缴费的组织架构树下，不需要缴费的子节点信息
		Map<String,List<String>> levelMap = ProcMioInfoUtils.getChildTree(orgTreeMap,noIsPaidMap);
		List<String> levelList = new ArrayList<>();
		levelList.add(pln.getLevelCode());
		if(levelMap.containsKey(pln.getLevelCode())){
			logger.info(">>>>>>>缴费节点【levelCode】:"+pln.getLevelCode()+"下，不需要缴费的子节点："+levelMap.get(pln.getLevelCode()));
			levelList.addAll(levelMap.get(pln.getLevelCode()));
		}
		return levelList;
	}

	/**
	 * 修改被保人状态
	 * @param type 类别  FEE-收费组 ，IPSN-被保人
	 * @param id 被保人序号/收费组号
	 * @param applNo 投保单号
	 * @param procStat 状态
	 */
	private int changeGrpInsuredState(String type,Long id, String applNo, String procStat) {
		Map<String,Object> ipsnIDMap = new HashMap<>();
    	Update gupInsUpdate = new Update();
    	gupInsUpdate.set("procStat", procStat);
    	if("O".equals(procStat)){
    		//remark字段新增内容-扣款超过截止日期，扣款失败。这里需要注意remark里面的值只能进行拼接，不能直接替换
	    	gupInsUpdate.set("remark","扣款超过截止日期，扣款失败!");
    	}else if("E".equals(procStat)){
    		gupInsUpdate.set("remark","完成实收");
    	}
    	ipsnIDMap.put("applNo", applNo);
    	int num=0;
    	if(IPSN_TYPE.equals(type)){
    		ipsnIDMap.put("ipsnNo", id);
    		num = mongoBaseDao.update(GrpInsured.class, ipsnIDMap, gupInsUpdate); 
    	}else if(FEE_TYPE.equals(type)){
    		ipsnIDMap.put("feeGrpNo", id);
    		ipsnIDMap.put("accInfoList", null);
    		num= mongoBaseDao.updateAll(GrpInsured.class, ipsnIDMap, gupInsUpdate); 
    	}
    	return num;
	}

	/**
	 * 修改被保人状态
	 * @param plnmioRec 应收数据
	 * @param grp 保单信息
	 * @param procStat 被保人状态：N新增  E-生效，O-作废
	 * @return
	 */
	private int changeGrpInsuredState(PlnmioRec plnmioRec, GrpInsurAppl grp,String procStat) {
		//账户所属类别【O:组织架构树应收,I:被保人应收,P:收费组产生应收】
		String multiPayAccType = plnmioRec.getMultiPayAccType();
		//组织架构层次号、被保人序号、收费组号
		String applNo = plnmioRec.getCntrNo();
		Map<String,Object> grpInsMap = new HashMap<>();
		grpInsMap.put("applNo", applNo);
		grpInsMap.put("procStat","N");
		List<GrpInsured> grpInsuredList =  new ArrayList<>();
		if("O".equals(multiPayAccType) && !"0".equals(plnmioRec.getLevelCode())){
			List<String> levelList = getLevelList(grp,plnmioRec);
			grpInsuredList =  procMioInfoDao.getGrpInsuredList(applNo, levelList);
		}else if("I".equals(multiPayAccType)){
			grpInsMap.put("ipsnNo", plnmioRec.getIpsnNo());
			grpInsuredList = mongoBaseDao.find(GrpInsured.class, grpInsMap);
		}else if("P".equals(multiPayAccType)){
			grpInsMap.put("feeGrpNo", plnmioRec.getFeeGrpNo());
			grpInsuredList = mongoBaseDao.find(GrpInsured.class, grpInsMap);
		}else{
			grpInsuredList = mongoBaseDao.find(GrpInsured.class, grpInsMap);
		}
		int rows =0;
	    for(GrpInsured grpInsured:grpInsuredList){
	    	rows+=changeGrpInsuredState(IPSN_TYPE,grpInsured.getIpsnNo(),applNo,procStat);
	    }
	    return rows;
	}

	
	/**
	 * 封装请求数据
	 * @return
	 */
	private MioFirstPremChkBo setMioFirstPremChkBo(PlnmioRec plnmioRec){
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);	
		
		MioFirstPremChkBo mioChkBo = new MioFirstPremChkBo();
		//检查方式不能为空
		mioChkBo.setCheckFlag("1");
		//传递内容主体
		List<MioFirstPremChkFramBo> mioChkBolist = new ArrayList<>();
		MioFirstPremChkFramBo mioFramo = new MioFirstPremChkFramBo();
		mioFramo.setMgBranch(plnmioRec.getMgrBranchNo());		// 系统号
		mioFramo.setApplNo(plnmioRec.getCntrNo().substring(0, 16));//投保单号
		
		//判断是否多期暂缴费
		GrpInsurAppl grp = procMioInfoDao.getGrpInsurAppl(plnmioRec.getCntrNo());
		if(CNTR_TYPE.LIST_INSUR.getKey().equals(grp.getCntrType()) && "Y".equals(grp.getPaymentInfo().getIsMultiPay())){
			mioFramo.setPlmioAmnt(grp.getApplState().getSumPremium());	//应收金额
		}else{
			mioFramo.setPlmioAmnt(plnmioRec.getAmnt().doubleValue());	//应收金额
		}
		mioFramo.setPlnmioRecId(plnmioRec.getPlnmioRecId());	//应收标识id
		mioFramo.setPolCode(plnmioRec.getPolCode());			//险种代码
		mioFramo.setTotalAmnt(plnmioRec.getAmnt().doubleValue());//总金额
		mioChkBolist.add(mioFramo);
		mioChkBo.setList(mioChkBolist);
		return  mioChkBo;
	}
	
	/**
	 * 日期格式化
	 * @param date
	 * @return
	 */
	public String getFormatDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
		return sdf.format(date);
	}
}
