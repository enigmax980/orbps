package com.newcore.orbps.service.impl;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.common.util.PropertiesUtils;
import com.halo.core.exception.FileAccessException;
import com.halo.core.filestore.api.FileStoreService;
import com.newcore.orbps.dao.api.DascInsurParaDao;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.service.api.InsurApplOperUtils;
import com.newcore.orbps.service.api.IpsnImpAppliForInterService;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;

/**
 * @author wangxiao
 * 创建时间：2016年10月31日下午4:32:39
 */
@Service("ipsnImpAppliForInterService")
public class IpsnImpAppliForInterServiceImpl implements IpsnImpAppliForInterService{

	/**
	 * 日志记录
	 */
	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	TaskPrmyDao taskPrmyDao;
	@Autowired
	JdbcOperations jdbcTemplate;
	@Resource
	DascInsurParaDao dascInsurParaDao;
	@Autowired
	MongoTemplate mongoTemplate;
	@Resource
    FileStoreService fileStoreService;
	@Resource
	InsurApplOperUtils insurApplOperUtils;
	@Override
	public RetInfo applyIpsnImport(Map<String, String> filemap) throws FileAccessException {
		RetInfo reInfo = new RetInfo();
		String applNo = filemap.get("applNo");
		String status = filemap.get("status");
		String resourceId = filemap.get("filename");
		String taskId = filemap.get("taskId");
		String pclkBranchNo = filemap.get("pclkBranchNo");
		String pclkNo = filemap.get("pclkNo");
		String pclkName = filemap.get("pclkName");
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
		if(StringUtils.isEmpty(resourceId)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("filename为空");
			return reInfo;
		}
		if(StringUtils.isEmpty(pclkBranchNo)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("pclkBranchNo为空");
			return reInfo;
		}
		if(StringUtils.isEmpty(pclkNo)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("pclkNo为空");
			return reInfo;
		}
		if(StringUtils.isEmpty(pclkName)){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("pclkName为空");
			return reInfo;
		}
		// 创建错误文件路径
		StringBuilder sb = new StringBuilder(PropertiesUtils.getProperty("fs.ipsn.err.path"));
		if (sb.lastIndexOf("/") != sb.length() - 1)
			sb.append("/");
		File file = new File(sb.toString() + "/" + applNo + ".xls");
		if (file.exists())
			file.delete();
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if(grpInsurAppl == null){
			reInfo.setApplNo(applNo);
			reInfo.setRetCode("0");
			reInfo.setErrMsg("投保单号对应的团单基本信息不存在");
			return reInfo;
		}
		if(StringUtils.equals(grpInsurAppl.getLstProcType(),LST_PROC_TYPE.ARCHIVES_LIST.getKey())){
			List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.EV_CUST_SET_TASK_QUEUE.toString(), grpInsurAppl.getApplNo(),null);
			if(null != taskPrmyInfos && !taskPrmyInfos.isEmpty()){
				reInfo.setApplNo(applNo);
				reInfo.setErrMsg("档案清单补导入清单已经导入过，请不要重复导入");
				reInfo.setRetCode("0");
				return reInfo;
			}
			/*核保后、核销之前都可以做补到*/
			InsurApplOperTrace insurApplOperTrace = mongoTemplate.findOne(Query.query(Criteria.where("applNo").is(applNo)), InsurApplOperTrace.class);
			if(Integer.valueOf(insurApplOperTrace.getCurrentTraceNode().getProcStat()) < Integer.valueOf(NEW_APPL_STATE.UNDERWRITING.getKey())
					|| Integer.valueOf(insurApplOperTrace.getCurrentTraceNode().getProcStat()) >= Integer.valueOf(NEW_APPL_STATE.RECEIPT_VERIFICA.getKey())){
				reInfo.setApplNo(applNo);
				reInfo.setRetCode("0");
				reInfo.setErrMsg("保单当前状态为"+NEW_APPL_STATE.valueOfKey(insurApplOperTrace.getCurrentTraceNode().getProcStat()).getDescription()+",不处于核保与回执核销之间,不允许档案清单补导入!");
				return reInfo;
			}
		}
		long oldRightNum = mongoBaseDao.count(GrpInsured.class, map);
		if(StringUtils.equals(status,"1")){
			List<TaskPrmyInfo> list = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo,"K");
			if(list != null && !list.isEmpty()){
				reInfo.setApplNo(applNo);
				reInfo.setRetCode("0");
				reInfo.setErrMsg(applNo+":正在进行清单导入，请稍等...");
				return reInfo;
			}
			list = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), applNo,"N");
			if(list != null && !list.isEmpty()){
				reInfo.setApplNo(applNo);
				reInfo.setRetCode("0");
				reInfo.setErrMsg(applNo+":已存在待导入清单，请稍后...!");
				return reInfo;
			}
			//如果该投保单号存在清单，则删除
			mongoBaseDao.remove(GrpInsured.class, map);
			mongoBaseDao.remove(ErrorGrpInsured.class, map);
			dascInsurParaDao.delete(applNo);
			//插入任务队列
			TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
			taskPrmyInfo.setApplNo(applNo);
			String sql = "SELECT S_LIST_IMPORT_TASK_QUEUE.NEXTVAL FROM DUAL";
			Long taskSeq = jdbcTemplate.queryForObject(sql, Long.class);
			taskPrmyInfo.setTaskSeq(taskSeq);
			taskPrmyInfo.setStatus("N");
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			taskPrmyInfo.setBusinessKey(applNo);
			taskPrmyInfo.setTaskId(taskId);
			taskPrmyInfo.setListPath(resourceId);
			taskPrmyInfo.setSalesBranchNo(getSalesInfo(grpInsurAppl).getSalesBranchNo());
			taskPrmyInfo.setExtKey0(pclkNo);
			taskPrmyInfo.setExtKey1(pclkBranchNo);
			taskPrmyInfo.setExtKey2(pclkName);
			//之前已导入的正确条数
			taskPrmyInfo.setExtKey4("0");
			taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo);
		}else if(StringUtils.equals(status,"2")){
			TaskPrmyInfo taskPrmyInfo = new TaskPrmyInfo();
			taskPrmyInfo.setApplNo(applNo);
			String sql = "SELECT S_LIST_IMPORT_TASK_QUEUE.NEXTVAL FROM DUAL";
			Long taskSeq = jdbcTemplate.queryForObject(sql, Long.class);
			taskPrmyInfo.setTaskSeq(taskSeq);
			taskPrmyInfo.setStatus("N");
			taskPrmyInfo.setCreateTime(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
			taskPrmyInfo.setBusinessKey(applNo);
			taskPrmyInfo.setTaskId(taskId);
			taskPrmyInfo.setListPath(resourceId);
			taskPrmyInfo.setSalesBranchNo(getSalesInfo(grpInsurAppl).getSalesBranchNo());
			taskPrmyInfo.setExtKey0(pclkNo);
			taskPrmyInfo.setExtKey1(pclkBranchNo);
			taskPrmyInfo.setExtKey2(pclkName);
			//之前已导入的正确条数
			taskPrmyInfo.setExtKey4(Long.toString(oldRightNum));
			taskPrmyDao.insertTaskPrmyInfoByTaskSeq(QueueType.LIST_IMPORT_TASK_QUEUE.toString(), taskPrmyInfo);
		}
		reInfo.setApplNo(applNo);
		reInfo.setRetCode("1");
		return reInfo;
	}
	private SalesInfo getSalesInfo(GrpInsurAppl grpInsurAppl){
		SalesInfo salesInfo = grpInsurAppl.getSalesInfoList().get(0);
		for(SalesInfo salesInfo1:grpInsurAppl.getSalesInfoList()){
			if(StringUtils.equals(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey(), salesInfo1.getDevelMainFlag())){
				return salesInfo1;
			}
		}
		return salesInfo;
	}
}
