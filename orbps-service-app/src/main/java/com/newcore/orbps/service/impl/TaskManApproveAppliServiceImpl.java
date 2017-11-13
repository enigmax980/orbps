package com.newcore.orbps.service.impl;

import java.util.Date;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.halo.core.dasc.annotation.AsynCall;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.ManualApprovalService;
import com.newcore.orbps.service.api.TaskManApproveAppliService;

/**
 * @ClassName: TaskManApproveAppliServiceImpl
 * @Description: 人工审核
 * @author jiangbomeng
 * @date 2016年9月1日 上午11:06:50
 * 
 */
@Service("taskManApproveAppliService")
public class TaskManApproveAppliServiceImpl implements TaskManApproveAppliService {
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(TaskManApproveAppliServiceImpl.class);

	@Autowired
	ManualApprovalService manualApprovalService;
	
	@Autowired
	DascInsurParaDao dascInsurParaDao;
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Autowired
	TaskPrmyDao taskPrmyDao;
	
	@Autowired
	MongoBaseDao mongoBaseDao;

	@Override
	@AsynCall
	public String manualApprove(String args) {
		String[] argsArr = args.split("_");

		// 业务逻辑开始
		logger.info("执行方法:人工审核" + argsArr[0] + "_" + argsArr[1]);

		try {
			/**
			 * 业务逻辑处理开始
			 */
			TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
			DascInsurParaBo dascInsurParaBo = dascInsurParaDao.select(argsArr[0]);
			String applNo = dascInsurParaBo.getApplNo();
			String listPath = dascInsurParaBo.getListFilePath();
			taskPrmyInfo.setApplNo(applNo);
			String sql = "SELECT S_MANUAL_APPROVE_TASK_QUEUE.NEXTVAL FROM DUAL";
			Long taskSeq = jdbcTemplate.queryForObject(sql, Long.class);
			taskPrmyInfo.setTaskSeq(taskSeq);
			taskPrmyInfo.setStatus("N");
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			taskPrmyInfo.setBusinessKey(argsArr[0]);
			taskPrmyInfo.setTaskId(argsArr[1]);
			taskPrmyInfo.setListPath(listPath);
			taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.MANUAL_APPROVE_TASK_QUEUE.toString(), taskPrmyInfo);
			/**
			 * 业务逻辑处理结束
			 */			
			return null;
			
		} catch (Exception e) {
			/**
			 * 业务系统异常处理逻辑在这里增加
			 * 
			 */
			throw e;
		}
	}
}
