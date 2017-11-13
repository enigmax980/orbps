package com.newcore.orbps.service.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 生效批作业收尾工作：1.插入落地监控表；2.插入短信表 Created by liushuaifeng on 2017/5/10 0010.
 */
public class MakeInsurFinishTasklet implements Tasklet {

	@Resource(name = "taskProcessServiceDascClient")
	TaskProcessService taskProcessService;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Autowired
	TaskPrmyDao taskPrmyDao;
	
	@Autowired
	JdbcOperations jdbcTemplate;

	// 状态类型 生效短信息
	private static final String TASK_ID = "P";

	private final static String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static Logger logger = LoggerFactory.getLogger(MakeInsurFinishTasklet.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		String applNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters()
				.getString("applNo");
		long finLandBatNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
				.getLong("finLandBatNo");
		Double sumPremium = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
				.getDouble("sumPremium");
		long plnLandBatNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext()
				.getLong("plnLandBatNo");
		
		logger.info("生效完成，准备进行插入落地监控表开始，applNo=[{}], finLandBatNo=[{}], sumPremium=[{}], plnLandBatNo=[{}]", applNo, finLandBatNo, sumPremium, plnLandBatNo);
		
		Map<String, Object> mongoMap = new HashMap<>();
		mongoMap.put("applNo", applNo);
		//查询保单基本信息
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, mongoMap);
		if (null == grpInsurAppl) {
			throw new BusinessException(new Throwable("获取保单基本信息失败"));
		}
		//查看是否分期
		int amorFlag = getAmorFlag(grpInsurAppl.getPaymentInfo().getMoneyinDurUnit(),
				grpInsurAppl.getPaymentInfo().getMoneyinDur(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl());
		TaskCntrDataLandingQueue taskCntrDataLandingQueue = new TaskCntrDataLandingQueue();
		// 分期条数amorFlag>1则是分期 否则不是分期
		taskCntrDataLandingQueue.setIsStepPlnmio(amorFlag > 1 ? "Y" : "N");
		taskCntrDataLandingQueue.setStatus("N");
		taskCntrDataLandingQueue.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());
		taskCntrDataLandingQueue.setCreateTime(new Date());
		taskCntrDataLandingQueue.setAskTimes(0);
		taskCntrDataLandingQueue.setApplNo(grpInsurAppl.getApplNo());
		taskCntrDataLandingQueue.setCgNo(grpInsurAppl.getCgNo());
		taskCntrDataLandingQueue.setJobInstanceId(
				chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getInstanceId());
		taskCntrDataLandingQueue.setCntrType(grpInsurAppl.getCntrType());
		taskCntrDataLandingQueue.setLstProcType(grpInsurAppl.getLstProcType());
		taskCntrDataLandingQueue.setIsRenew(grpInsurAppl.getPaymentInfo().getIsRenew());
		taskCntrDataLandingQueue.setIsMultiPay(grpInsurAppl.getPaymentInfo().getIsMultiPay());

		if (StringUtils.isEmpty(grpInsurAppl.getAgreementNo())) {
			taskCntrDataLandingQueue.setIsCommonAgreement(YES_NO_FLAG.NO.getKey());
		} else {
			taskCntrDataLandingQueue.setIsCommonAgreement(YES_NO_FLAG.YES.getKey());
		}
		taskCntrDataLandingQueue.setFinLandBatNo(finLandBatNo);
		taskCntrDataLandingQueue.setSumPremium(BigDecimalUtils.keepPrecisionDouble(sumPremium, 2)); // 把总保费
		// taskCntrDataLandingQueue.setExtKey0(sumPremium.toString());
		// //把总保费存放在扩展键0里
		taskCntrDataLandingQueue.setFinLandFlag("0");
		taskCntrDataLandingQueue.setIpsnLandFlag("0");
		taskCntrDataLandingQueue.setInsurApplLandFlag("0");
		taskCntrDataLandingQueue.setCommonAgreementLandFlag("0");
		taskCntrDataLandingQueue.setPlnLandFlag("0");
		taskCntrDataLandingQueue.setPlnLandBatNo(plnLandBatNo);
		// 此处插入监控表
		taskCntrDataLandingQueueDao.deleteByApplNo(taskCntrDataLandingQueue.getApplNo());
		taskCntrDataLandingQueueDao.save(taskCntrDataLandingQueue);
		logger.info("插入落地监控表完成，准备开始进行插入短信息监控表，applNo=[{}]", applNo);
//		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.queryTaskPrmyInfoByTaskId(QueueType.SMS_TASK_QUEUE.toString(), applNo,TASK_ID);
//		if(null == taskPrmyInfos || taskPrmyInfos.isEmpty()){
//			TaskPrmyInfo taskPrmyInfoSMS = new TaskPrmyInfo();
//			Long taskSeq = taskPrmyDao.getTaskSeq("MIO_LOG");
//			taskPrmyInfoSMS.setTaskSeq(taskSeq); 
//			taskPrmyInfoSMS.setTaskId(TASK_ID);  //taskId 短信类型  目前仅有生效完成短信   值为P
//			taskPrmyInfoSMS.setStatus("N");  
//			taskPrmyInfoSMS.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());
//			taskPrmyInfoSMS.setCreateTime(DateFormatUtils.format(new Date(), DATA_PATTERN));
//			taskPrmyInfoSMS.setAskTimes(0);
//			taskPrmyInfoSMS.setApplNo(grpInsurAppl.getApplNo());
//			taskPrmyInfoSMS.setBusinessKey(grpInsurAppl.getApplNo());
//			taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.SMS_TASK_QUEUE.toString(), taskPrmyInfoSMS);
//			logger.info("插入短信息监控表完成,applNo=[{}]", applNo);
//		}
		return RepeatStatus.FINISHED;

	}

	/**
	 * 判断是否分期应收
	 *
	 * @param insurDurDnit
	 * @param insurDur
	 * @param moneyinItrvl
	 * @return
	 */
	public int getAmorFlag(String insurDurDnit, double insurDur, String moneyinItrvl) {
		double moneyinDur = 0.0;
		// 保险期间
		BigDecimal b1 = new BigDecimal(Double.toString(insurDur));
		// 1
		BigDecimal b2 = new BigDecimal(Double.toString(1));
		// 4
		BigDecimal b3 = new BigDecimal(Double.toString(4));
		// 6
		BigDecimal b4 = new BigDecimal(Double.toString(6));
		// 12
		BigDecimal b5 = new BigDecimal(Double.toString(12));
		// 保险期间乘以12
		BigDecimal b6 = b1.multiply(b5);
		// 判断缴费方式
		if ("O".equals(moneyinItrvl) || "W".equals(moneyinItrvl) || "Y".equals(moneyinItrvl)
				|| "X".equals(moneyinItrvl)) {
			moneyinDur = 0.0;
		} else {// 保险期类型
			if ("M".equals(insurDurDnit)) {
				switch (moneyinItrvl)// 缴费方式
				{
				case "M":// 保险期间除以1
					moneyinDur = b1.divide(b2).doubleValue();
					break;
				case "Q":// 保险期间除以4
					moneyinDur = b1.divide(b3).doubleValue();
					break;
				case "H":// 保险期间除以6
					moneyinDur = b1.divide(b4).doubleValue();
					break;
				default:
					moneyinDur = 0.0;
				}
			} // 保险期类型
			if ("Y".equals(insurDurDnit)) {
				switch (moneyinItrvl)// 缴费方式
				{
				case "M":// 保险期间乘以12 在除以1
					moneyinDur = b6.divide(b2).doubleValue();
					break;
				case "Q":// 保险期间乘以12 在除以4
					moneyinDur = b6.divide(b3).doubleValue();
					break;
				case "H":// 保险期间乘以12 在除以6
					moneyinDur = b6.divide(b4).doubleValue();
					break;
				default:
					moneyinDur = 0.0;
					break;
				}
			} // end if
		} // end if
		return (int) moneyinDur;
	}
}
