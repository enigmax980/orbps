package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.cache.api.CacheMetaInfoFactory;
import com.halo.core.cache.api.CacheService;
import com.halo.core.cache.models.CacheMetaInfo;
import com.halo.core.cache.models.ExpireConfig;
import com.halo.core.cache.models.ExpireStrategy;
import com.halo.core.cache.support.redis.holder.MapHolder;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.common.TransactionCodeHolder;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.ipms.api.PsnInfoService;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 每次导入处理结束后，对险种保额保费，被保人总保额保费进行校验，如果校验失败把失败记录存在错误表中。
 * 
 * @author liushuaifeng
 *
 *         创建时间：2016年8月19日上午10:40:15
 */

public class IpsnListImportListener implements JobExecutionListener {

	private static Logger logger = LoggerFactory.getLogger(IpsnListImportListener.class);
	private final static String pattern = "yyyy-MM-dd HH:mm:ss";
	private final static String STATE_C = "C";
	private final static String STATE_E = "E";
	private final static String ACCESS_SOURCE_GCSS = "GCSS";

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	TaskPrmyDao taskPrmyDao;

	@Autowired
	PsnInfoService resulPsnInfoService;

	@Resource
	CacheService cacheService;

	@Resource
	CacheMetaInfoFactory cacheMetaInfoFactory;

//	@Autowired
//	BatchJobMonitorService batchJobMonitorService;
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		//批作业开始执行前
//		batchJobMonitorService.beginExcution(jobExecution);

		String mapName = jobExecution.getJobParameters().getString("mapName");
		String applNo = jobExecution.getJobParameters().getString("applNo");
		// MapHolder<String> mapHolder = cacheService.getMap(mapName,
		// String.class);
		Map<String, String> cacheMap = new HashMap<>();
		ExpireConfig expireConfig = new ExpireConfig();
		expireConfig.setExpireStrategy(ExpireStrategy.EXPIRE_ON_OWNER_EXIT);
		// expireConfig.setReadCountLimit(2);
		CacheMetaInfo metaInfo = cacheMetaInfoFactory.generateMapMetaInfo(mapName, String.class, expireConfig);

		// mapHolder = cacheService.getMap(mapName, String.class);

		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		String jsonStr = JSON.toJSONString(grpInsurAppl);
		// 保存保单基本信息
		cacheMap.put(applNo + "grpInsurAppl", jsonStr);
		MapHolder<String> mapHolder = cacheService.createMap(metaInfo, cacheMap);

		Map<String, String> polSex = new HashMap<>();
		Map<String, String> polMinAge = new HashMap<>();
		Map<String, String> polMaxAge = new HashMap<>();

		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {

			Map<String, Object> mapQuery = new HashMap<>();
			mapQuery.put("polCode", policy.getPolCode());
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			Map<String, Object> mapQueryResult = resulPsnInfoService.excute(mapQuery);
			// 存在不限制年龄的情况，则赋默认值
			polMinAge.put(policy.getPolCode(), "A0");
			polMaxAge.put(policy.getPolCode(), "A999");
			polSex.put(policy.getPolCode(), "B");
			if (null == mapQueryResult || (mapQueryResult.containsKey("errorCode") && mapQueryResult.get("errorCode").equals("8888"))) {
				continue;
			}
			JSONArray jsonArray = (JSONArray) JSONObject
					.parseObject(JSONObject.toJSONString(mapQueryResult.get("psnInfoBo"))).get("psnCtrlList");
			if (null == jsonArray) {
				continue;
			}

			for (int i = 0; null != jsonArray && i < jsonArray.size(); i++) {
				if (!StringUtils.equals("M0", jsonArray.getJSONObject(i).getString("psnTypeCode"))) {
					continue;
				}
				String maxAgeUnit = jsonArray.getJSONObject(i).getString("maxAgeUnit");
				Integer maxAge = jsonArray.getJSONObject(i).getIntValue("maxAge");
				if (StringUtils.isEmpty(maxAgeUnit) || null == maxAge) {
					maxAgeUnit = "A";
					maxAge = 999;
				}
				String minAgeUnit = jsonArray.getJSONObject(i).getString("minAgeUnit");
				Integer minAge = jsonArray.getJSONObject(i).getIntValue("minAge");
				if (StringUtils.isEmpty(minAgeUnit) || null == minAge) {
					minAgeUnit = "A";
					minAge = 0;
				}
				String sex = jsonArray.getJSONObject(i).getString("sex");
				if (StringUtils.isEmpty(sex)) {
					sex = "B";					
				}
				polMinAge.put(policy.getPolCode(), minAgeUnit + String.valueOf(minAge));
				polMaxAge.put(policy.getPolCode(), maxAgeUnit + String.valueOf(maxAge));
				polSex.put(policy.getPolCode(), sex);
			}
		}
		String jsonStr1 = JSON.toJSONString(polMinAge);
		String jsonStr2 = JSON.toJSONString(polMaxAge);
		String jsonStr3 = JSON.toJSONString(polSex);
		mapHolder.add(applNo + "polMinAge", jsonStr1);
		mapHolder.add(applNo + "polMaxAge", jsonStr2);
		mapHolder.add(applNo + "polSex", jsonStr3);

	}

	@AsynCall
	@Transaction
	public synchronized void afterJob(JobExecution jobExecution) {

		long taskSeq = jobExecution.getJobParameters().getLong("taskSeq");
		String mapName = jobExecution.getJobParameters().getString("mapName");

		MapHolder<String> mapHolder = cacheService.getMap(mapName, String.class);
		if (null != mapHolder) {
			mapHolder.delete();
		}
		
//		if (jobExecution.getStatus() == BatchStatus.STOPPED || jobExecution.getStatus() == BatchStatus.STOPPING) {
//			return;
//		}
		
		TaskPrmyInfo taskPrmyInfo = taskPrmyDao.queryTaskPrmyInfoByTaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(),
				taskSeq);
		if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_C)
				|| StringUtils.equals(taskPrmyInfo.getStatus(), STATE_E)) {
			return;
		}

		Map<String, Object> mongoMap = new HashMap<>();
		mongoMap.put("applNo", taskPrmyInfo.getApplNo());
		Long count = mongoBaseDao.count(ErrorGrpInsured.class, mongoMap);
		
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {

			GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, mongoMap);
			if (null == grpInsurAppl) {
				throw new BusinessException("0004");
			}

			logger.info("applNo: " + taskPrmyInfo.getApplNo() + ",jobStatus: " + jobExecution.getStatus());
			logger.info("applNo: " + taskPrmyInfo.getApplNo() + ",清单导入错误数量：" + count);

			if (count == 0) {

				taskPrmyInfo.setStatus(STATE_C);
				if (StringUtils.equals(grpInsurAppl.getAccessSource(), ACCESS_SOURCE_GCSS) && !StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ARCHIVES_LIST.getKey())) {

					// 添加操作轨迹
					TraceNode traceNode = new TraceNode();
					traceNode.setProcDate(new Date());

					if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
						traceNode.setProcStat(NEW_APPL_STATE.GRP_IMPORT.getKey());
					} else if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
						traceNode.setProcStat(NEW_APPL_STATE.LIST_IMPORT.getKey());
					}

					mongoBaseDao.updateOperTrace(taskPrmyInfo.getApplNo(), traceNode);

					// 进行任务完成操作
					logger.info("通知任务平台完成任务：applNo: " + taskPrmyInfo.getApplNo());
					TaskProcessService taskProcessService = SpringContextHolder.getBean("taskProcessServiceDascClient");// 为DASC配置文件中定义的ID
					TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
					taskProcessRequestBO.setTaskId(taskPrmyInfo.getTaskId());// 业务服务参数中获取的任务ID
					taskProcessRequestBO.setBusinessKey(taskPrmyInfo.getApplNo());
					taskProcessService.completeTask(taskProcessRequestBO);//

				}

			} else {
				taskPrmyInfo.setStatus(STATE_E);
			}

		} else {
			taskPrmyInfo.setStatus(STATE_E);
		}

//		//批作业执行结束后
//		if (StringUtils.equals(taskPrmyInfo.getStatus(), STATE_C)) {
//			batchJobMonitorService.afterExcution(true, jobExecution);
//		} else {
//			batchJobMonitorService.afterExcution(false, jobExecution);
//		}
		
		// 任务结束时间。
		taskPrmyInfo.setEndTime((DateFormatUtils.format(new Date(), pattern)));
		taskPrmyInfo.setAskTimes(taskPrmyInfo.getAskTimes() + 1);
		String err = jobExecution.getAllFailureExceptions().toString();

		if (err.length() >= 512) {
			err = "transNo:" + TransactionCodeHolder.get() + "," + err.substring(0, 480);
		}
		taskPrmyInfo.setRemark(err);
		taskPrmyInfo.setJobInstanceId(jobExecution.getJobInstance().getInstanceId());
		taskPrmyInfo.setExtKey3(count.toString());
		logger.info(taskPrmyInfo.toString());
		taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo);
		logger.info("applNo: " + "[" + taskPrmyInfo.getApplNo() + "]" + "清单导入结束");
	}
}
