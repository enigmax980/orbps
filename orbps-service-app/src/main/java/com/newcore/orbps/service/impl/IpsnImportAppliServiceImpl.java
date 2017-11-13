package com.newcore.orbps.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.FileAccessException;
import com.halo.core.filestore.api.FileStoreService;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.InsurRuleMangerDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.bo.insurrulemanger.InsurRuleManger;
import com.newcore.orbps.models.dasc.bo.DascInsurParaBo;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.IpsnImportAppliService;
import com.newcore.orbps.util.DecompressUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.CNTR_TYPE_01;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PRD_SALES_CHANNEL_01;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 清单导入请求接口实现
 * @author wangxiao
 * 创建时间：2016年7月28日下午7:41:09
 * 说明：
 *	入参：applNo、status(1：首次导入；2：继续导入：3：导入完成)， filename（ORBPS：resouce ID， 团销：文件路径）
 *	1.团体销售系统
 *	-1. 首次导入
 * 	--1.1 如果该投保单号存在清单，则删除；如果存在老的任务流，则撤销老的任务流，则从清单导入队列里获取业务流水号，同时删除该投保单号下的清单队列
 *	--1.2 校验是否可清单导入；
 *	--1.3 解压 发起任务流
 *	-2.继续导入
 *	--2.1 解压
 *	--2.2发起任务流
 *	2.ORBPS
 *	-1.首次导入
 *	--1.1 如果该投保单号存在清单，则删除；如果存在老的任务流，则撤销老的任务流，则从清单导入队列里获取业务流水号，同时删除该投保单号下的清单队列
 *	--1.2 校验是否可清单导入；
 *	--1.3 插入任务队列
 *	-2.继续导入
 *	--2.2 插入清单导入队列
 *	-3.导入完成
 *	--3.1 清单导入任务完成
 */
@Service("ipsnImportAppliService")
public class IpsnImportAppliServiceImpl implements IpsnImportAppliService {

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(IpsnImportAppliServiceImpl.class);

	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Resource
    FileStoreService fileStoreService;
	
	@Resource
	DascInsurParaDao dascInsurParaDao;
	
	@Resource
	InsurRuleMangerDao insurRuleMangerDao;
	
	@Resource
	TaskProcessService taskProcessServiceDascClient;
	
	@Resource
	TaskPrmyDao taskPrmyDao;
	
	@Resource
	JdbcOperations jdbcTemplate;
	
	/**
	 * 清单导入请求
	 * @param filemap
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	@Override
	@Transaction
	@AsynCall
	public RetInfo applyIpsnImport(Map<String, String> filemap){
		RetInfo reInfo = new RetInfo();
		String applNo = filemap.get("applNo");
		String status = filemap.get("status");
		String filename = filemap.get("filename");
		if(StringUtils.isEmpty(applNo)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("投保单号为空");
			return reInfo;
		}
		if(StringUtils.isEmpty(status)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("status为空");
			return reInfo;
		}
		if(StringUtils.isEmpty(filename)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("filename为空");
			return reInfo;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if(grpInsurAppl == null){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("投保单号对应的团单基本信息不存在");
			return reInfo;
		}
		if(!StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ORDINARY_LIST.getKey())){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("团体清单标志不为普通清单，无法执行清单导入");
			return reInfo;
		}
		String sourceDirectory = PropertiesUtils.getProperty("fs.ftp.filedir")+"/"+filename;
		logger.info("文件路径="+sourceDirectory);
		File sourcePath = new File(sourceDirectory);
		if (!sourcePath.exists()){
			logger.info("未找到对应文件");
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("未找到对应文件");
			return reInfo;
		}
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo,null);
		
		// 创建错误文件路径
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
		if (sb.lastIndexOf("/") != sb.length() - 1)
			sb.append("/");
		File errfile = new File(sb.toString() + "/" + applNo + ".xls");
		if (errfile.exists())
			errfile.delete();
		Map<String, Object> operTraceMap = new HashMap<>();
		operTraceMap.put("applNo", applNo);
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
			operTraceMap.put("operTraceDeque.procStat",NEW_APPL_STATE.GRP_IMPORT.getKey());
		} else if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			operTraceMap.put("operTraceDeque.procStat",NEW_APPL_STATE.LIST_IMPORT.getKey());
		}
		long count = mongoBaseDao.count(InsurApplOperTrace.class, operTraceMap);
		if(count!=0){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg(applNo+":清单导入已完成无法再次导入");
			return reInfo;
		}
		//首次导入
		String resourceId = "";
		if(StringUtils.equals(status,"1")){
			if(!taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo,"K").isEmpty()){
				reInfo.setApplNo(applNo);
				reInfo.setRetCode("0");
				reInfo.setErrMsg(applNo+":正在进行清单导入，请稍等...");
				return reInfo;
			}
			//如果该投保单号存在清单，则删除
			mongoBaseDao.remove(GrpInsured.class, map);
			mongoBaseDao.remove(ErrorGrpInsured.class, map);
			dascInsurParaDao.delete(applNo);
			//如果存在老的任务流，则从清单导入队列里获取业务流水号，撤销老的任务流，同时删除该投保单号下的清单队列
			if(taskPrmyInfos!=null && !taskPrmyInfos.isEmpty()){
				TaskProcessRequestBO taskProcessRequestBO=new TaskProcessRequestBO ();
				taskProcessRequestBO.setProcessDefKey("ECORBP001");//必填-流程定义ID
				taskProcessRequestBO.setBusinessKey(taskPrmyInfos.get(0).getBusinessKey().toString());//必填-业务流水号
				//撤销流程
				taskProcessServiceDascClient.deleteProcessInstance(taskProcessRequestBO);
				taskPrmyDao.deletTaskPrmyInfoByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo);
			}
			//解压文件
			String targetDirectory = PropertiesUtils.getProperty("fs.ftp.filedir");
			File file = null;
			try {
				file = DecompressUtils.unZip(sourceDirectory, targetDirectory, applNo);
				resourceId = fileStoreService.addResource(file);
			} catch (FileAccessException e1) {
				logger.error(e1.getMessage());
				throw new RuntimeException(e1);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			//判断是否人工审批，发起任务流
			logger.info("准备开始发起任务流");
			initiateProcess(grpInsurAppl,applNo,resourceId);
		}else if(StringUtils.equals(status,"2")){
			//解压文件
			String targetDirectory = PropertiesUtils.getProperty("fs.ftp.filedir");
			File file = null;
			try {
				file = DecompressUtils.unZip(sourceDirectory, targetDirectory, applNo);
				resourceId = fileStoreService.addResource(file);
			} catch (FileAccessException e1) {
				logger.error(e1.getMessage());
				throw new RuntimeException(e1);
			} catch (Exception e) {
				logger.error(e.getMessage());
				throw new RuntimeException(e);
			}
			//插入清单导入队列
			if(taskPrmyInfos!=null && !taskPrmyInfos.isEmpty()){
				String businessKey = taskPrmyInfos.get(0).getBusinessKey();
				String taskId = taskPrmyInfos.get(0).getTaskId();
				TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
				taskPrmyInfo.setApplNo(applNo);
				String sql = "SELECT S_LIST_IMPORT_TASK_QUEUE.NEXTVAL FROM DUAL";
				Long taskSeq = jdbcTemplate.queryForObject(sql, Long.class);
				taskPrmyInfo.setTaskSeq(taskSeq);
				taskPrmyInfo.setStatus("N");
				taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
				taskPrmyInfo.setBusinessKey(businessKey);
				taskPrmyInfo.setTaskId(taskId);
				taskPrmyInfo.setListPath(resourceId);
				taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo);
			}else{
				reInfo.setApplNo(applNo);
				reInfo.setRetCode("0");
				reInfo.setErrMsg("继续导入时查找之前的任务id失败，请重新导入");
				return reInfo;
			}
		//3.执行清单导入完成
		}else if(StringUtils.equals(status,"3")){
			
		}
		Update update = new Update();
		update.set("operTrace.newApplState", NEW_APPL_STATE.LIST_IMPORT.getKey());
		mongoBaseDao.update(GrpInsurAppl.class, map, update);
        reInfo.setApplNo(applNo);
		reInfo.setRetCode("1");
		return reInfo;
	}
	//判断是否人工审批，发起任务流
	private void initiateProcess(GrpInsurAppl grpInsurAppl,String applNo,String resourceId){
		 Map<String,String> mapVar=new HashMap<>();//选填-放入流程运转过程中必须的业务信息~
	        //销售渠道
	        String salesChannel = "";
	        //销售机构
	        String salesBranchNo = "";
			// 规则类型
			String ruleType = "1";//暂时同一为1
			// 契约形式
			String cntrForm = grpInsurAppl.getCntrType();
	        if(grpInsurAppl.getSalesInfoList()!=null){
	        	for(SalesInfo salesInfo:grpInsurAppl.getSalesInfoList()){
	        		//取主销售员销售机构和销售渠道
	        		if(StringUtils.equals(salesInfo.getDevelMainFlag(),"1")){
	        			salesChannel = salesInfo.getSalesChannel();
	        			salesBranchNo = salesInfo.getSalesBranchNo();
	        		}
	        	}
	        	//如果没主销售员标记取第一个销售员的销售机构和销售渠道
	        	if(StringUtils.isEmpty(salesChannel)){
	        		salesChannel = grpInsurAppl.getSalesInfoList().get(0).getSalesChannel();
	        		salesBranchNo = grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo();
	        	}
	        }
	     // 通过省级机构号，销售渠道，销售机构，契约形式查询审批方案
			//flag 为false 不进人工审批，true进人工审批
			boolean flag;
			InsurRuleManger insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(), salesChannel,ruleType,cntrForm);
			if(null == insurRuleManger){
				insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,cntrForm);		
			}
			if(null == insurRuleManger){
				insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),PRD_SALES_CHANNEL_01.ALL.getKey(),ruleType,CNTR_TYPE_01.ALL.getKey());					
			}
			if(null == insurRuleManger){
				insurRuleManger = insurRuleMangerDao.select(grpInsurAppl.getProvBranchNo(),salesChannel,ruleType,CNTR_TYPE_01.ALL.getKey());					
			}
			if(null == insurRuleManger){
				flag = false;
			}else {
				//人工审批规则判断 兼容多多个审批规则
				List<InsurRuleManger> insurRuleMangers = new ArrayList<>();
				insurRuleMangers.add(insurRuleManger);
				flag = grpInsurAppl.isManApprove(insurRuleMangers);		
			}	        //如果查询结果为空，自动审批
	        if (flag){	        	
				if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
					mapVar.put("IS_MAN_APPROVE", "1");
					mapVar.put("CNTR_TASK_SUB_ITEM", "GRP");
					mapVar.put("ACCEPT_BRANCH_NO", salesBranchNo);
				} else {
					mapVar.put("IS_MAN_APPROVE", "2");// 人工审批
					mapVar.put("CNTR_TASK_SUB_ITEM", "LST");
					mapVar.put("ACCEPT_BRANCH_NO", salesBranchNo);
				}
				Map<String, Object> map = new HashMap<>();
				map.put("applNo", grpInsurAppl.getApplNo());
				Update update = new Update();
				update.addToSet("conventions.inputConv", grpInsurAppl.getConventions().getInputConv());
				mongoBaseDao.update(GrpInsurAppl.class, map, update);
			} else {
				mapVar.put("ACCEPT_BRANCH_NO", salesBranchNo);
				mapVar.put("IS_MAN_APPROVE", "0");// 自动审批
			}
		    mapVar.put("IS_INSURED_LIST", "1");
	        DascInsurParaBo dascInsurParaBo = new DascInsurParaBo();
	        dascInsurParaBo.setApplNo(applNo);
	        dascInsurParaBo.setCntrType(grpInsurAppl.getCntrType());
	        dascInsurParaBo.setListFilePath(resourceId);
	        dascInsurParaDao.add(dascInsurParaBo);
	        TaskProcessRequestBO taskProcessRequestBO=new TaskProcessRequestBO ();
	        taskProcessRequestBO.setProcessDefKey("ECORBP001");//必填-流程定义ID
	        taskProcessRequestBO.setBusinessKey(applNo);//必填-业务流水号
	        taskProcessRequestBO.setBranchNo(salesBranchNo);
	        taskProcessRequestBO.setProcessVar(mapVar);
	        //发起任务流
	        logger.info("任务流start");
	        taskProcessServiceDascClient.startProcess(taskProcessRequestBO);
	}
}
