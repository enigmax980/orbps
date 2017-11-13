package com.newcore.orbps.service.impl;

import javax.annotation.Resource;

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
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.models.pcms.bo.GrpListMioTask;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.service.pcms.api.GrpMioNtTaskService;
import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 实收付流水落地清单告知 定时任务
 * @author JCC
 * 2017年3月3日 09:35:14
 */
@Schedule(cron = "0 0/1 * * * ?")
@Service("mioLogDataWriteBackJob")
@DisallowConcurrentExecution
public class MioLogDataWriteBackJob implements Job {

	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	PlnmioCommonDao plnmioCommonDao;
	
	@Resource(name = "grpMioNtTaskServiceClient")
    GrpMioNtTaskService grpMioNtTaskService;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(MioLogDataWriteBackJob.class);

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("实收付流水落地清单告知-正向落地开始......");
		writeMioLogDate();
		logger.info("实收付流水落地清单告知-正向落地结束......");
	}

	/**
	 * 开始业务操作
	 */
	public void writeMioLogDate(){
		// 1.从产生应收付流水任务表查询STATUS N 新增或者 E 错误的数据       
		String [] status ={"N"};
		SqlRowSet row = plnmioCommonDao.queryMioInfoRoamTaskQueue(status);
		while(row.next()){
			String branchNo=row.getString(1);	//管理机构号
			String batNo=row.getString(2); 		//批次号
			Long taskSeq = row.getLong(3);		//业务主键
			logger.info("执行方法[实收付流水落地清单告知定时任务]查询到数据，开始处理!["+taskSeq+"]["+branchNo+"]["+batNo+"]");
			try{
				//数据处理中，此单状态status修改成K
	        	plnmioCommonDao.updateMioInfoRoamTaskQueue(taskSeq,"K");
				//实收付流水任务 BO
		        GrpListMioTask grpListMioTask = new GrpListMioTask();
		        grpListMioTask.setApplNo("######");
		        grpListMioTask.setBatNo(Long.valueOf(batNo));
		        grpListMioTask.setMgrBranchNo(branchNo);
		        grpListMioTask.setMioType("1");
		        grpListMioTask.setPlnBatNo(-1L);
		        //消息头设置
		        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		        HeaderInfoHolder.setOutboundHeader(headerInfo);
		        RetInfo retInfo =grpMioNtTaskService.grpListMioTaskCreate(grpListMioTask); 
		        if("1".equals(retInfo.getRetCode())){
		        	logger.info("执行方法[实收付流水落地清单告知定时任务]:调用成功！["+taskSeq+"]["+branchNo+"]["+batNo+"]");
		        	//处理成功，此单状态status修改成C
		        	plnmioCommonDao.updateMioInfoRoamTaskQueue(taskSeq,"C");
		        }else{
		        	logger.info("执行方法[实收付流水落地清单告知定时任务]:调用失败！["+taskSeq+"]["+branchNo+"]["+batNo+"]");
		        	//处理失败，此单状态status修改成E
		        	plnmioCommonDao.updateMioInfoRoamTaskQueue(taskSeq,"E");
		        }
			}catch(RuntimeException ex){
				logger.info("执行方法[实收付流水落地清单告知定时任务]:出现异常情况！["+taskSeq+"]["+branchNo+"]["+batNo+"]！"+ex);
				plnmioCommonDao.updateMioInfoRoamTaskQueue(taskSeq,"E");
			}
		}
	}
}
