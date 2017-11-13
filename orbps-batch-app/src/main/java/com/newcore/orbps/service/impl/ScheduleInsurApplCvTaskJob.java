package com.newcore.orbps.service.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.halo.core.batch.annotation.Schedule;
import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.InsurApplCvTaskBo;
import com.newcore.orbps.models.service.bo.InsurApplCvTask;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.ArchiveListMonitor;
import com.newcore.orbps.service.pcms.api.InsurApplCvTaskService;
import com.newcore.orbpsutils.validation.TimeUtils;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 回执核销定时任务
 * 
 * @author wangxiao 
 * 创建时间：2017年1月16日上午11:15:08
 */
@Schedule(cron = "0 0/1 * * * ?")
@Service("ScheduleInsurApplCvTask")
@DisallowConcurrentExecution
public class ScheduleInsurApplCvTaskJob implements Job {
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	TaskPrmyDao taskPrmyDao;
	@Autowired
	InsurApplCvTaskService insurApplCvTaskServiceClient;
	@Autowired
	TaskProcessService taskProcessServiceDascClient;
	@Autowired
	JdbcOperations jdbcTemplate;
	@Autowired
	ArchiveListMonitor restfulArchiveListMonitorClient;

	private final static String pattern1 = "yyyy/MM/dd HH:mm:ss";
	private final static String pattern2 = "yyyy-MM-dd HH:mm:ss";

	private final static String STATE_A = "A";
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(ScheduleInsurApplCvTaskJob.class);

	@Override
	@AsynCall
	public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("回执核销批作业开始");
//		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.queryReceiptVerificaTask();
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.queryTaskPrmyInfoByStatus(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), STATE_A);
		if (taskPrmyInfos == null || taskPrmyInfos.isEmpty()) {
			logger.info("回执核销队列暂无符合条件任务，回执核销批作业结束。");
			return;
		}
		for (TaskPrmyInfo taskPrmyInfo : taskPrmyInfos) {
			StringBuilder sql = new StringBuilder("SELECT * FROM CNTR_INFORCE_TASK_QUEUE WHERE APPL_NO = '");
			sql.append(taskPrmyInfo.getApplNo());
			sql.append("'");
			String flag;

			Map<String, Object> mongoMap = new HashMap<>();
			mongoMap.put("applNo", taskPrmyInfo.getApplNo());
			GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, mongoMap);
			if (null == grpInsurAppl) {
				throw new BusinessException(new Throwable("查询保单基本信息失败。applNo= " + taskPrmyInfo.getApplNo()));
			}

			//如果是档案清单，那么当前系统时间-生效日 <14天，则不回执核销
			if(com.alibaba.druid.util.StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ARCHIVES_LIST.getKey())){
				if( TimeUtils.getIntervalDays(grpInsurAppl.getInForceDate(),new Date()) < 14 ){
					logger.info("该电子保单处于生效日十五天之内，不能自动回执核销：applNo="+taskPrmyInfo.getApplNo());
					continue;
				}
			}
			
			// 获取落地监控表
			TaskCntrDataLandingQueue taskCntrDataLandingQueue = null;
			try {
				taskCntrDataLandingQueue = jdbcTemplate.queryForObject(
						"SELECT * FROM TASK_CNTR_DATA_LANDING_QUEUE WHERE APPL_NO = ?",
						new CustomBeanPropertyRowMapper<TaskCntrDataLandingQueue>(TaskCntrDataLandingQueue.class),
						taskPrmyInfo.getApplNo());
			} catch (EmptyResultDataAccessException e) { // 如果查询结果为空，则返回Null
				logger.info(e.getMessage(), e);
				logger.info("查询保单落地无结果：table:applNo=" + taskPrmyInfo.getApplNo());
			}
			List<TaskPrmyInfo> taskPrmyInfoList = jdbcTemplate.query(sql.toString(),
					new CustomBeanPropertyRowMapper<TaskPrmyInfo>(TaskPrmyInfo.class));
			if (null == taskPrmyInfoList || taskPrmyInfoList.isEmpty()) {
				flag = "1";
			} else if (null == taskCntrDataLandingQueue) {
				flag = "2";
			} else if (StringUtils.equals("C", taskCntrDataLandingQueue.getStatus()) // 落地批作业已经完成
					&& StringUtils.equals("3", taskCntrDataLandingQueue.getFinLandFlag()) // 财务落地完成
					&& StringUtils.equals("3", taskCntrDataLandingQueue.getInsurApplLandFlag())) {// 帽子落地完成
				// 如果是普通清单,被保人须落地告知
				if (StringUtils.equals(taskCntrDataLandingQueue.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())
						&& !StringUtils.equals("3", taskCntrDataLandingQueue.getIpsnLandFlag())) {
					flag = "2";
					// 如果是共保
				} else if (StringUtils.equals(taskCntrDataLandingQueue.getIsCommonAgreement(), YES_NO_FLAG.YES.getKey())
						&& !StringUtils.equals("3", taskCntrDataLandingQueue.getCommonAgreementLandFlag())) {
					flag = "2";
				} else {
					flag = "3";
				}
			} else {
				flag = "2";
			}
			Map<String, Object> mapUpdate = new HashMap<>();
			mapUpdate.put("applNo", taskPrmyInfo.getApplNo());
			if (StringUtils.equals(flag, "1")) {
				taskPrmyInfo.setRemark("保单未生效");
				taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), taskPrmyInfo);
			} else if (StringUtils.equals(flag, "2")) {
				taskPrmyInfo.setRemark("保单生效、落地在途");
				taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), taskPrmyInfo);
			} else if (StringUtils.equals(flag, "3")) {
				InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
				insurApplCvTask.setApplNo(taskPrmyInfo.getApplNo());
//				if (null == taskPrmyInfo.getExtKey1() || taskPrmyInfo.getExtKey1().isEmpty()) {
					insurApplCvTask.setSignDate(new Date());
//				} else {
//					Date signDate = null;
//					try {
//						signDate = DateUtils.parseDate(taskPrmyInfo.getExtKey1(), pattern1, pattern2);
//					} catch (ParseException e) {
//						logger.info(e.getMessage(), e);
//						throw new RuntimeException(e.getCause());
//					}
//					insurApplCvTask.setSignDate(signDate);
//				}
				RetInfo retInfo = insurApplCvTask(insurApplCvTask, taskPrmyInfo);
				if (StringUtils.equals(retInfo.getRetCode(), "1") || StringUtils.equals(retInfo.getRetCode(), "2")) {
					taskPrmyInfo.setStatus("C");
					taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(),
							taskPrmyInfo);
				}
			}
		}

        logger.info("回执核销批作业结束");
	}

	private RetInfo insurApplCvTask(InsurApplCvTask insurApplCvTask, TaskPrmyInfo taskPrmyInfo) {
		/* 对入参进行校验处理 */
		InsurApplCvTaskBo insurApplCvTaskBo = new InsurApplCvTaskBo();
		RetInfo retInfo = insurApplCvTaskCheck(insurApplCvTask, insurApplCvTaskBo);
        logger.info("insurApplCvTaskCheck"+insurApplCvTask.getApplNo()+retInfo.getRetCode());
		if (StringUtils.equals("0", retInfo.getRetCode()) || StringUtils.equals("2", retInfo.getRetCode())) {
			retInfo.setRetCode("0");
			return retInfo;
		}
		if (StringUtils.equals("3", retInfo.getRetCode())) {
			retInfo.setRetCode("1");
			return retInfo;
		}
		/* 入参处理完成后 对校验完成的 单子 ,调用ESB回执核销服务 */
		RetInfo retInfoRet = callServer(insurApplCvTaskBo, taskPrmyInfo);
		if (retInfoRet == null) {
			retInfoRet = new RetInfo();
			retInfoRet.setApplNo(insurApplCvTaskBo.getApplNo());
			retInfoRet.setRetCode("0");
			retInfoRet.setErrMsg("调用PCMS回执核销 失败！");
		}
		insurApplCvTaskReturn(retInfoRet);// 对返回值进行处理
		return retInfoRet;
	}

	/* 校验入参，并组织发往PCMS系统的数据 */
	private RetInfo insurApplCvTaskCheck(InsurApplCvTask insurApplCvTask, InsurApplCvTaskBo insurApplCvTaskBo) {

		Map<String, Object> map = new HashMap<>();
		/* 根据投保单号查询保单信息 */
		map.put("applNo", insurApplCvTask.getApplNo());
		RetInfo retInfo = new RetInfo();
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		Map<String, Object> applMap = new HashMap<>();
		applMap.put("applNo", insurApplCvTask.getApplNo());
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,
				applMap);
		// 校验回执核销表是否已存数据
		InsurApplCvTask queryInsurApplCvTask = (InsurApplCvTask) mongoBaseDao.findOne(InsurApplCvTask.class, map);
		if (null != queryInsurApplCvTask && queryInsurApplCvTask.getStatus().equals("C")) {
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			retInfo.setRetCode("3");
			retInfo.setErrMsg("合同号[" + queryInsurApplCvTask.getCgNo() + "]:已核销");
			logger.error("合同号[" + queryInsurApplCvTask.getCgNo() + "]:已核销");
			return retInfo;
		}
		/*
		 * 对于档案清单的处理，如果有补导入，二次落地成功或者无档案清单补导入才能允许回执核销
		 * 档案清单已开户，第一次落地完毕时-A,二次落地成功-S,二次落地失败-F,无档案清单补导入-N,正在开户中-K,清单落地异常-"E"
		 */
		if (StringUtils.endsWith(LST_PROC_TYPE.ARCHIVES_LIST.getKey(), grpInsurAppl.getLstProcType())){
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			String flag = restfulArchiveListMonitorClient.query(insurApplCvTask.getApplNo());
			if (!("S".equals(flag) || "N".equals(flag))) {
				retInfo.setRetCode("0");
				retInfo.setErrMsg("该保单" + insurApplCvTask.getApplNo() + ":为档案清单补导入的单子,补导入流程未完成不允许回执核销,状态为[" + flag + "]!");
				retInfo.setApplNo(insurApplCvTask.getApplNo());
				return retInfo;
			}

		}

		/* 对insurApplCvTask进行赋值 */
		insurApplCvTask.setCgNo(grpInsurAppl.getCgNo());
		insurApplCvTask.setSgNo(grpInsurAppl.getSgNo());
		if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.NONE_LIST.getKey())) {// 是否有清单
			insurApplCvTask.setAttachedFlag("0");
		} else {
			insurApplCvTask.setAttachedFlag("1");
		}
		insurApplCvTask.setCntrType(grpInsurAppl.getCntrType());// 保单类型
		insurApplCvTask.setPolCode(grpInsurAppl.getFirPolCode());// 主险
		insurApplCvTask.setRespDate(new Date());// 核销日期
		insurApplCvTask.setStatus("N");// 核销状态-N-未核销或核销失败
		if (StringUtils.isBlank(insurApplCvTask.getMgBranch())) {
			insurApplCvTask.setMgBranch(grpInsurAppl.getMgrBranchNo());// 管理机构
		}
		/* 受理机构赋值 */
		for (TraceNode traceNode : insurApplOperTrace.getOperTraceDeque()) {
			if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.ACCEPTED.getKey())
					&& StringUtils.isBlank(insurApplCvTask.getAccBranch())) {
				insurApplCvTask.setAccBranch(traceNode.getPclkBranchNo());// 受理机构
			}
		}
		insurApplCvTask.setInForceDate(grpInsurAppl.getInForceDate());// 合同生效日期
		insurApplCvTask.setCntrExpiryDate(grpInsurAppl.getCntrExpiryDate());// 合同预计满期日期
		if (null != grpInsurAppl.getSalesInfoList()) {
			SalesInfo salesInfo = getSalesInfo(grpInsurAppl);
			if (StringUtils.isBlank(insurApplCvTask.getSalesName())) {
				insurApplCvTask.setSalesName(salesInfo.getSalesName());// 销售员姓名
			}
			if (StringUtils.isBlank(insurApplCvTask.getSalesNo())) {
				insurApplCvTask.setSalesNo(salesInfo.getSalesNo());// 销售员号
			}
			insurApplCvTask.setSalesBranchNo(salesInfo.getSalesBranchNo());// 销售机构号
		}
		/* 将回执核销数据插入数据库 */
		if (null == queryInsurApplCvTask) {
			mongoBaseDao.insert(insurApplCvTask);
		} else {
			mongoBaseDao.remove(InsurApplCvTask.class, map);
			mongoBaseDao.insert(insurApplCvTask);
		}
		/* 组 PCMS InsurApplCvTaskService 回执核销数据 */
		insurApplCvTaskBo.setCgNo(insurApplCvTask.getCgNo());// 组合保单号
		insurApplCvTaskBo.setApplNo(insurApplCvTask.getApplNo());// 投保单号
		insurApplCvTaskBo.setAccBranch(StringUtils.isBlank(insurApplCvTask.getAccBranch())
				? insurApplCvTask.getMgBranch() : insurApplCvTask.getAccBranch());// 受理机构
		insurApplCvTaskBo.setAttachedFlag(insurApplCvTask.getAttachedFlag());// 是否附有清单
		insurApplCvTaskBo.setMgBranch(insurApplCvTask.getMgBranch());// 管理机构
		insurApplCvTaskBo.setPolCode(insurApplCvTask.getPolCode());// 险种代码(第一主险)
		insurApplCvTaskBo.setRespDate(insurApplCvTask.getRespDate());// 核销日期
		insurApplCvTaskBo.setCntrType(insurApplCvTask.getCntrType());// 保单类别
		retInfo.setRetCode("1");
		retInfo.setApplNo(insurApplCvTask.getApplNo());
		retInfo.setErrMsg("检查通过");
		return retInfo;
	}

	/* 调用PCMS服务 */
	private RetInfo callServer(InsurApplCvTaskBo insurapplcvtaskbo, TaskPrmyInfo taskPrmyInfo) {
		RetInfo retInfoRet = new RetInfo();
		try {
	        logger.info("callServer"+insurapplcvtaskbo.getApplNo());
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			com.newcore.orbps.models.pcms.bo.RetInfo retInfo = insurApplCvTaskServiceClient
					.addInsurApplCvTask(insurapplcvtaskbo);
			logger.debug("调用PCMS回执核销返回值:" + JSONObject.toJSONString(retInfo));
			// 添加回执核销任务完成调用
			if (StringUtils.equals(retInfo.getRetCode(), "1") || StringUtils.equals(retInfo.getRetCode(), "2")) {
				completeTask(insurapplcvtaskbo, taskPrmyInfo);
			}
			retInfoRet.setApplNo(insurapplcvtaskbo.getApplNo());
			retInfoRet.setErrMsg(retInfo.getErrMsg());
			retInfoRet.setRetCode(retInfo.getRetCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retInfoRet.setApplNo(insurapplcvtaskbo.getApplNo());
			retInfoRet.setErrMsg("投保单" + insurapplcvtaskbo.getApplNo() + "回执核销时出现异常:" + e.getMessage());
			retInfoRet.setRetCode("0");
		}
		return retInfoRet;
	}

	/* 对返回值进行处理 */
	private void insurApplCvTaskReturn(RetInfo retInfoRet) {
		/* 对返回参数进行处理，组返回数据 */
		if (null == retInfoRet) {
			return;
		}
		Map<String, Object> mapUpdate = new HashMap<String, Object>();
		mapUpdate.put("applNo", retInfoRet.getApplNo());
		Update update = new Update();
		if (StringUtils.equals(retInfoRet.getRetCode(), "1") || StringUtils.equals(retInfoRet.getRetCode(), "2")) {
			/* 回执核销表 status-C 回执核销完成 */
			mongoBaseDao.updateAll(InsurApplCvTask.class, mapUpdate,
					update.set("status", "C").set("description", "核销成功"));
			/* 团体出单表 完成回执核销 */
			InsurApplCvTask insurApplCvTask = (InsurApplCvTask) mongoBaseDao.findOne(InsurApplCvTask.class, mapUpdate);
			/* 核保节点 */
			/* 对轨迹操作InsurApplOperTrace 增加核保节点 */
			TraceNode traceNode = new TraceNode();
			traceNode.setProcStat(NEW_APPL_STATE.RECEIPT_VERIFICA.getKey());// 回执核销
			if (null != insurApplCvTask) {
				traceNode.setPclkBranchNo(insurApplCvTask.getOclkBranchNo());// 回执核销操作员机构
				traceNode.setPclkName(insurApplCvTask.getOclkClerkName());// 回执核销操作员姓名
				traceNode.setPclkNo(insurApplCvTask.getOclkClerkNo());// 回执核销操作员代码
				traceNode.setProcDate(insurApplCvTask.getRespDate());// 回执核销完成日期
			}
			traceNode.setPclkName("自动核销");
			mongoBaseDao.updateOperTrace(retInfoRet.getApplNo(), traceNode);
		} else if (StringUtils.equals(retInfoRet.getRetCode(), "0")) {
			/* 回执核销失败，status-N 回执核销失败 */
			logger.error("保单" + retInfoRet.getApplNo() + "调用PCMS回执核销失败：" + retInfoRet.getErrMsg());
			mongoBaseDao.updateAll(InsurApplCvTask.class, mapUpdate,
					update.set("status", "N").set("description", retInfoRet.getErrMsg()));
		}
	}

	private SalesInfo getSalesInfo(GrpInsurAppl grpInsurAppl) {
		for (SalesInfo salesInfo1 : grpInsurAppl.getSalesInfoList()) {
			if (StringUtils.equals(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey(), salesInfo1.getDevelMainFlag())) {
				return salesInfo1;
			}
		}
		return grpInsurAppl.getSalesInfoList().get(0);
	}

	// 回执核销调用任务完成方法
	private void completeTask(InsurApplCvTaskBo insurapplcvtaskbo, TaskPrmyInfo taskPrmyInfo) throws ParseException {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		TaskProcessRequestBO taskProcessbo = new TaskProcessRequestBO();
		taskProcessbo.setTaskId(taskPrmyInfo.getTaskId());
		taskProcessServiceDascClient.completeTask(taskProcessbo);
		taskPrmyInfo.setStatus("C");
		Date createTime = null;
		String createTimestr = null;
		if (!StringUtils.isEmpty(taskPrmyInfo.getCreateTime())) {
			createTime = DateUtils.parseDate(taskPrmyInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss");
		}
		if (createTime != null) {
			createTimestr = DateFormatUtils.format(createTime, pattern);
		}
		taskPrmyInfo.setCreateTime(createTimestr);
		taskPrmyInfo.setEndTime(DateFormatUtils.format(new Date(), pattern));
		taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), taskPrmyInfo);
	}
}
