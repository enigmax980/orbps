package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.TaskReceiptVerificaService;
import com.newcore.supports.dicts.CNTR_PRINT_TYPE;
import com.newcore.tsms.service.api.task.TaskProcessService;

@Service("taskReceiptVerificaService")
public class TaskReceiptVerificaServiceImpl implements TaskReceiptVerificaService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(TaskCntrPrintServiceImpl.class);

	@Autowired
	TaskProcessService taskProcessServiceDascClient;
	
	@Autowired
	TaskPrmyDao taskPrmyDao;
	
	@Autowired
	DascInsurParaDao dascInsurParaDao;
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;	
	
	@Override
	@AsynCall
	public void receiptVerifica(String args) {
		String[] argsArr = args.split("_");
		// 业务逻辑开始
		logger.info("执行方法:回执核销" + "业务流水号:" + argsArr[0] + ",任务ID:" + argsArr[1]);
		
		try {
			/**
			 * 业务逻辑处理开始
			 */
			String applNo = argsArr[0];
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("applNo", applNo);
			GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
			if (null == grpInsurAppl) {
				throw new BusinessException("0019", applNo);
			}
			
			TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
			DascInsurParaBo dascInsurParaBo = dascInsurParaDao.select(applNo);
			String listPath = dascInsurParaBo.getListFilePath();
			taskPrmyInfo.setApplNo(applNo);
			String sql = "SELECT S_RECEIPT_VERIFICA_TASK_QUEUE.NEXTVAL FROM DUAL";
			Long taskSeq = jdbcTemplate.queryForObject(sql, Long.class);
			taskPrmyInfo.setTaskSeq(taskSeq);
			
			if(grpInsurAppl.getCntrPrintType().equals(CNTR_PRINT_TYPE.ELEC_INSUR.getKey()))
			{
				taskPrmyInfo.setStatus("A");
			}
			else
			{
				taskPrmyInfo.setStatus("N");
			}
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			taskPrmyInfo.setBusinessKey(applNo);
			taskPrmyInfo.setApplNo(applNo);
			taskPrmyInfo.setTaskId(argsArr[1]);
			taskPrmyInfo.setListPath(listPath);
			taskPrmyDao.deletTaskPrmyInfoByApplNo(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), applNo);
			taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), taskPrmyInfo);
			/**
			 * 业务逻辑处理结束
			 */			
			
			/**
			 * 调用任务管理平台的完成任务接口。只有业务逻辑处理正常，才需要调用任务管理平台的完成任务接口。
			 * 调用任务调度平台提供的任务完成服务，来驱动当前自动节点往下进行。
			 */
			return;
			
		} catch (Exception e) {
			/**
			 * 业务系统异常处理逻辑在这里增加
			 * 
			 */
			throw e;
		}		
	}

}
