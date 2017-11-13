package com.newcore.orbps.service.impl;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.halo.core.batch.annotation.Schedule;
import com.halo.core.dasc.annotation.AsynCall;
import com.newcore.orbps.service.api.ProcNewApplPSInfoService;
import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 账户余额冻结功能定时任务
 * @author JCC
 * 2016年12月15日 09:15:24
 * 每隔1分执行一次。
 */
@Schedule(cron = "0 0/1 * * * ?")   
@Service("ProcNewApplPSInfoQuartz")
@DisallowConcurrentExecution
public class ProcNewApplPSInfoJob implements Job {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(ProcNewApplPSInfoJob.class);
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	PlnmioCommonDao plnmioCommonDao;
	
	@Autowired
	private TaskProcessService taskProcessServiceDascClient;
	
	@Autowired
	ProcNewApplPSInfoService procNewApplPSInfoService;
	
	@AsynCall
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//从产生应收付流水任务表STATUS:K=检查处理中， N=新建，E=检查失败，D=保单作废，C=检查通过，S=冻结成功，W=冻结处理中，F=冻结处理失败
		logger.info("执行方法[账户余额冻结功能定时任务]:开始运行！");
		// 1.从产生应收付流水任务表查询[STATUS=C]检查通过的数据
		String [] status = {"C","F"};
		SqlRowSet row = plnmioCommonDao.queryMoneyInCheckQueue(status);
		while(row.next()){
			String taskId=row.getString(1);		 //任务ID
			String businessKey=row.getString(2); //业务流水号
			try{
				logger.info("执行方法[账户余额冻结功能定时任务]:开始业务处理 ！["+taskId+"]["+businessKey+"]");
				//锁定当然处理的投保单 status改成W
				plnmioCommonDao.updateMoneyInCheckStatus("W",taskId);
				boolean isTrue = procNewApplPSInfoService.procPSInfo(businessKey);
				if(isTrue){
					//1.账户冻结成功，修改产生应收付流水任务表状态：冻结成功  status改成S
					plnmioCommonDao.updateMoneyInCheckStatus("S",taskId);
					//2.调用任务完成接口，流转流程
					doNext(businessKey,taskId);
				}else{
					logger.info("执行方法[账户余额冻结功能定时任务]:冻结账户余额操作失败！["+taskId+"]["+businessKey+"]");
					plnmioCommonDao.updateMoneyInCheckStatus("F",taskId);
				}
			}catch(Exception ex){
				logger.info("执行方法[账户余额冻结功能定时任务]:冻结账户余额操作失败！["+taskId+"]["+businessKey+"]"+ex);
				plnmioCommonDao.updateMoneyInCheckStatus("F",taskId);
			}
		}
	}

	/**
	 * 任务完成调度接口
	 */
	private void doNext(String applNo,String taskId){
		logger.info("执行方法[账户余额冻结功能定时任务]：账户余额冻结成功，调用流程完成接口！["+taskId+"]["+applNo+"]");
		// 所有收费完成，调用任务成功
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		taskProcessRequestBO.setBusinessKey(applNo);
		taskProcessRequestBO.setTaskId(taskId);// 业务服务参数中获取的任务ID
		taskProcessServiceDascClient.completeTask(taskProcessRequestBO);// 进行任务完成操作
		logger.info("执行方法[账户余额冻结功能定时任务]:流程完成接口调用结束,账户余额冻结功能定时任务完成！["+taskId+"]["+applNo+"]");
	}
	
}

