package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.halo.core.batch.annotation.Schedule;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.pcms.bo.GrpListCgTaskBo;
import com.newcore.orbps.models.service.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.GrpListPriTaskService;
import com.newcore.orbpsutils.dao.api.OldPrintTaskQueueDao;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 打印批作业摘要信息接收服务定时任务
 * @author JCC
 * 2017年5月18日 20:00:45
 * 每隔1分执行一次。
 */
@Schedule(cron = "0 0/1 * * * ?")   
@Service("GrpListPriTaskServiceQuartz")
@DisallowConcurrentExecution
public class GrpListPriTaskServiceJob implements Job {
	/**
	 * 日志记录
	 */
	private final Logger logger = LoggerFactory.getLogger(GrpListPriTaskServiceJob.class);

	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	OldPrintTaskQueueDao oldPrintTaskQueueDao;
	
	@Autowired
	GrpListPriTaskService grpListPriTaskServiceClient;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		//STATUS: N=新建告知，K=告知处理中，E=告知失败，C=告知成功，D=需要调用老打印，S=老打印处理成功成功，W=老打印处理成功处理中，F=老打印处理成功失败
		logger.info("执行方法[打印批作业摘要信息接收服务定时任务]:开始运行！");
		// 1.从产生应收付流水任务表查询[STATUS=C]检查通过的数据
		String [] status = {"N","E"};
		SqlRowSet row = oldPrintTaskQueueDao.queryOldPrintQueue(status);
		while(row.next()){
			String applNo=row.getString(1); //投保单号
			try{
				logger.info("执行方法[打印批作业摘要信息接收服务定时任务]:开始业务处理 ！["+applNo+"]");
				//锁定当然处理的投保单 status改成K
				oldPrintTaskQueueDao.updateDateByApplNo(applNo, "K");
				GrpInsurAppl grpInsurAppl = procMioInfoDao.getGrpInsurAppl(applNo);
				RetInfo retInfo = getRetInfo(grpInsurAppl);
				if("1".equals(retInfo.getRetCode())){
					logger.info("执行方法[打印批作业摘要信息接收服务定时任务]:业务处理完成 ！["+applNo+"]");
					oldPrintTaskQueueDao.updateDateByApplNo(applNo, "C");
				}else{
					logger.info("执行方法[打印批作业摘要信息接收服务定时任务]:业务处理失败 ！["+applNo+"]");
					oldPrintTaskQueueDao.updateDateByApplNo(applNo, "E");
				}
			}catch(Exception ex){
				logger.info("执行方法[打印批作业摘要信息接收服务定时任务]操作失败！["+applNo+"]"+ex);
				oldPrintTaskQueueDao.updateDateByApplNo(applNo, "E");
			}
		}
	}

	/**
	 * 调用保单辅助接口
	 * @param grpInsurAppl
	 * @return
	 */
	private RetInfo getRetInfo(GrpInsurAppl grpInsurAppl) {
		//知保单辅助对该保单进行纸质打印数据的抽取
		GrpListCgTaskBo grpListCgTaskVo = new GrpListCgTaskBo();
		grpListCgTaskVo.setMgBranch(grpInsurAppl.getMgrBranchNo());   //管理机构号
		grpListCgTaskVo.setPolCode(grpInsurAppl.getFirPolCode());	  //险种代码
		grpListCgTaskVo.setApplNo(grpInsurAppl.getApplNo());          //投保单号
		grpListCgTaskVo.setCgNo(grpInsurAppl.getCgNo());    //组合保单号
		long ipsnNum= findIpsnNum(grpInsurAppl.getApplNo());
		grpListCgTaskVo.setBatchIpsnNum(ipsnNum);       	//批次总人数
		
		grpListCgTaskVo.setBatNo(0L);        				//批次号
		grpListCgTaskVo.setBatchSumFaceAmnt(0.0);           //批次总保额
		grpListCgTaskVo.setBatchSumPremium(0.0);            //批次总保费
		
		//调用保单落地服务
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		RetInfo retInfo = grpListPriTaskServiceClient.add(grpListCgTaskVo);
		return retInfo;
	}
	
	/**
	 * 获取有效被保人数量
	 * @param applNo
	 * @return
	 */
	private long findIpsnNum(String applNo) {
		//组织发送报文
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo);
		queryMap.put("procStat", "E");
		return mongoBaseDao.count(GrpInsured.class, queryMap);
	}
	
}

