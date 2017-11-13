package com.newcore.orbps.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.util.PropertiesUtils;
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
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.models.web.vo.contractentry.grpinsurappl.CvInformVo;
import com.newcore.orbps.service.api.ArchiveListMonitor;
import com.newcore.orbps.service.api.InsurApplOperUtils;
import com.newcore.orbps.service.api.OrbpsInsurApplCvTaskServer;
import com.newcore.orbps.service.pcms.api.InsurApplCvTaskService;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.orbpsutils.validation.ValidationUtils;
import com.newcore.supports.dicts.DEVEL_MAIN_FLAG;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;
/**
 * @author huanglong
 * @date 2016年8月24日 内容:回执核销实现类
 */

@Service("orbpsInsurApplCvTaskServer")
public class OrbpsInsurApplCvTaskServerImpl implements OrbpsInsurApplCvTaskServer {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	InsurApplCvTaskService insurApplCvTaskServiceClient;

	@Resource
	TaskProcessService taskProcessServiceDascClient;

	@Resource
	TaskPrmyDao taskPrmyDao;
	
	@Autowired
	InsurApplOperUtils insurApplOperUtils;
	
	@Autowired
    TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;
	
	@Autowired
	ArchiveListMonitor archiveListMonitor;
	
	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(OrbpsInsurApplCvTaskServerImpl.class);

	//所需常量定义
	long checkDay = 15;//超时校验日期
	String[] cvFlag ={"N","C","E","T","F"};//N-未核销，C-已核销,E-批量核销异常,T-统计批量状态,"F"-核销失败
	String[] succFlag ={"0","1","2","3"};//失败成功标记0-核销失败，1-成功,2-校验失败且数据库无记录,3-已核销
	String[] attachedFlag = {"0","1"};//PCMS 0-无清单，1-有清单 
	String[] timeOut = {"N","Y"};//核销超时标记 0-不超时，1-超时
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//时间格式
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//时间格式
	private static final String STATUS= "N" ;  //状态C 核销成功

	@Override
	@AsynCall
	public RetInfo insurApplCvTask(InsurApplCvTask insurApplCvTask) {
		if (null == insurApplCvTask) {
			RetInfo retInfo = new RetInfo();
			logger.error("入参insurApplCvTask为空");
			retInfo.setRetCode(succFlag[0]);
			retInfo.setErrMsg("入参insurApplCvTask为空");
			return retInfo;
		}
		/*对入参进行校验处理*/
		InsurApplCvTaskBo insurApplCvTaskBo = new InsurApplCvTaskBo();		
		RetInfo retInfo = insurApplCvTaskCheck(insurApplCvTask,insurApplCvTaskBo);
		if(StringUtils.equals(succFlag[0], retInfo.getRetCode()) 
				||StringUtils.equals(succFlag[2], retInfo.getRetCode())
				||StringUtils.equals(succFlag[3], retInfo.getRetCode())){
			retInfo.setRetCode("0");
			return retInfo;
		}
		/* 入参处理完成后 对校验完成的 单子  循环调用ESB回执核销服务 */
		RetInfo retInfoRet  = callServer(insurApplCvTaskBo);
		if(retInfoRet == null ){
			retInfoRet = new RetInfo();
			retInfoRet.setApplNo(insurApplCvTaskBo.getApplNo());
			retInfoRet.setRetCode("0");
			retInfoRet.setErrMsg("调用PCMS回执核销 失败！");
		}
		insurApplCvTaskReturn(retInfoRet);//对返回值进行处理
		return retInfoRet;
	}

	@Override
	@AsynCall
	public RetInfoObject<RetInfo> insurApplCvTasks(Map<String, String> map) {
		RetInfoObject<RetInfo> retInfoObject = new RetInfoObject<>();
		List<RetInfo> retInfos = new ArrayList<>();
		if (null == map || map.isEmpty()) {
			logger.error("入参map为空");
			retInfoObject.setRetCode(succFlag[0]);
			retInfoObject.setErrMsg("入参map为空");
			retInfoObject.setListObject(retInfos);
			return retInfoObject;
		}
		StringBuilder errMsg = new StringBuilder();
		if (!map.containsKey("batNo") || StringUtils.isEmpty(map.get("batNo"))) {
			retInfoObject.setRetCode(succFlag[0]);
			logger.error("批次号为空!");
			return retInfoObject;
		}
		if(!map.containsKey("fileName") || StringUtils.isEmpty(map.get("fileName"))){
			retInfoObject.setRetCode(succFlag[0]);
			errMsg.append("文件名为空!");
			errMsg.append("|");
		}
		if(!map.containsKey("oclkBranchNo") || StringUtils.isEmpty(map.get("oclkBranchNo"))){
			retInfoObject.setRetCode(succFlag[0]);
			errMsg.append("操作员机构号为空!");
			errMsg.append("|");
		}
		if(!map.containsKey("oclkClerkNo") || StringUtils.isEmpty(map.get("oclkClerkNo"))){
			retInfoObject.setRetCode(succFlag[0]);
			errMsg.append("操作员工号为空!");
			errMsg.append("|");
		}
		if(!map.containsKey("oclkClerkName") || StringUtils.isEmpty(map.get("oclkClerkName"))){
			retInfoObject.setRetCode(succFlag[0]);
			errMsg.append("操作员姓名为空!");
			errMsg.append("|");
		}	
		if(map.containsKey("fileName") && !map.get("fileName").matches("\\d+(\\.xls|\\.xlsx)$")){
			retInfoObject.setRetCode(succFlag[0]);
			errMsg.append("文件名不符合格式:'数字'+'.xls|.xlsx'!");
			errMsg.append("|");
		}
		
		if(StringUtils.equals(succFlag[0], retInfoObject.getRetCode()) && map.containsKey("batNo") && !StringUtils.isEmpty(map.get("batNo"))){
			InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
			insurApplCvTask.setBatNo(map.get("batNo"));
			insurApplCvTask.setStatus(cvFlag[2]);//异常
			insurApplCvTask.setDescription(errMsg.toString());
			Map<String, Object> mapSel = new HashMap<>();
			mapSel.put("batNo", map.get("batNo"));
			mongoBaseDao.remove(InsurApplCvTask.class, mapSel);
			mongoBaseDao.insert(insurApplCvTask);
			return retInfoObject;
		}
		/*去重*/
		List<InsurApplCvTask> insurApplCvTasksTemp = new ArrayList<>();	
		analysisFile(map.get("fileName"),insurApplCvTasksTemp,map.get("batNo"));
		Map<String, InsurApplCvTask> mapIndex = new HashMap<>();
		Map<String, Integer> mapIndex2 = new HashMap<>();
		for(int i = 0; i< insurApplCvTasksTemp.size();i++){
			InsurApplCvTask insurApplCvTask = insurApplCvTasksTemp.get(i);
			if(StringUtils.isEmpty(insurApplCvTask.getApplNo())){
				RetInfo retInfo = new RetInfo();
				retInfo.setApplNo(insurApplCvTask.getApplNo());
				retInfo.setRetCode(succFlag[0]);
				retInfo.setErrMsg("存在投保单号为空的记录!");
				retInfos.add(retInfo);
				continue;
			}	
			if(mapIndex.containsKey(insurApplCvTask.getApplNo())){
				mapIndex2.put(insurApplCvTask.getApplNo(), i);
				mapIndex.remove(insurApplCvTask.getApplNo());
				RetInfo retInfo = new RetInfo();
				retInfo.setApplNo(insurApplCvTask.getApplNo());
				retInfo.setRetCode(succFlag[0]);
				retInfo.setErrMsg("投保单存在重复!");
				retInfos.add(retInfo);	
			}else if (!mapIndex2.containsKey(insurApplCvTask.getApplNo())) {
				mapIndex.put(insurApplCvTask.getApplNo(), insurApplCvTask);
			}
		}
		List<InsurApplCvTask> insurApplCvTasks = new ArrayList<>();
		for(Map.Entry<String, InsurApplCvTask> entry:mapIndex.entrySet()){
			insurApplCvTasks.add(entry.getValue());
		}
		/*对入参进行处理*/
		List<InsurApplCvTaskBo> insurApplCvTaskBos = new ArrayList<>();
		int passCheck = 0;
		for(InsurApplCvTask insurApplCvTask : insurApplCvTasks){
			InsurApplCvTaskBo insurApplCvTaskBo = new InsurApplCvTaskBo();
			insurApplCvTask.setBatNo(String.valueOf(map.get("batNo")));
			insurApplCvTask.setOclkBranchNo(map.get("oclkBranchNo"));
			insurApplCvTask.setOclkClerkName(map.get("oclkClerkName"));
			insurApplCvTask.setOclkClerkNo(map.get("oclkClerkNo"));
			RetInfo retInfo = insurApplCvTaskCheck(insurApplCvTask,insurApplCvTaskBo);
			if (StringUtils.equals(succFlag[1], retInfo.getRetCode())) {
				insurApplCvTaskBos.add(insurApplCvTaskBo);
				passCheck++;
			}else if(StringUtils.equals(succFlag[3], retInfo.getRetCode())){/*已核销的单子重新赋值批次号*/
				Map<String, Object> map2 = new HashMap<>();
				map2.put("applNo", insurApplCvTask.getApplNo());
				Update update = new Update();
				update.set("batNo", String.valueOf(map.get("batNo")));
				mongoBaseDao.updateAll(InsurApplCvTask.class, map2, update);
				passCheck++;
				retInfos.add(retInfo);
			}else {
				retInfos.add(retInfo);
			}
		}
		if(insurApplCvTaskBos.isEmpty()){
			retInfoObject.setRetCode(succFlag[0]);
			retInfoObject.setErrMsg("没有符合需要回执核销的单子");
			logger.error("没有符合需要回执核销的单子");
			InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
			insurApplCvTask.setBatNo(map.get("batNo"));
			insurApplCvTask.setStatus(cvFlag[2]);//异常
			insurApplCvTask.setDescription("没有符合需要回执核销的单子");
			Map<String, Object> mapSel = new HashMap<>();
			mapSel.put("batNo", map.get("batNo"));
			mapSel.put("status", cvFlag[2]);//异常
			if(mongoBaseDao.count(InsurApplCvTask.class, mapSel) <= 0){
				mongoBaseDao.insert(insurApplCvTask);
			}			
		}else {
			InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
			insurApplCvTask.setBatNo(map.get("batNo"));
			insurApplCvTask.setStatus(cvFlag[3]);//统计批量状态
			insurApplCvTask.setDescription(insurApplCvTasksTemp.size()+"|"+(insurApplCvTasksTemp.size() - passCheck));
			mongoBaseDao.insert(insurApplCvTask);
		}
		/* 入参处理完成后 对校验完成的 单子  循环调用ESB回执核销服务 */
		for (InsurApplCvTaskBo insurApplCvTaskBo : insurApplCvTaskBos) {
			RetInfo retInfoRet  = callServer(insurApplCvTaskBo);
			if(retInfoRet == null ){
				retInfoRet = new RetInfo();
				retInfoRet.setApplNo(insurApplCvTaskBo.getApplNo());
				retInfoRet.setRetCode("0");
				retInfoRet.setErrMsg("调用PCMS回执核销 失败！");
			}
			insurApplCvTaskReturn(retInfoRet);//对返回值进行处理
			retInfos.add(retInfoRet);
		}
		/*生成错误文件*/
		createErrFile(retInfos,map.get("batNo"));
		retInfoObject.setListObject(retInfos);
		return retInfoObject;
	}	

	/*校验入参，并组织发往PCMS系统的数据*/
	private  RetInfo insurApplCvTaskCheck(InsurApplCvTask insurApplCvTask,InsurApplCvTaskBo insurApplCvTaskBo) {

		Map<String, Object> map = new HashMap<>();
		/* 根据投保单号合同号，合同组号，汇缴号，投保申请号查询保单信息 */
		if (!StringUtils.isBlank(insurApplCvTask.getApplNo())) {
			map.put("applNo", insurApplCvTask.getApplNo());
		}
		if (!StringUtils.isBlank(insurApplCvTask.getCgNo())) {
			map.put("cgNo", insurApplCvTask.getCgNo());
		}
		if (!StringUtils.isBlank(insurApplCvTask.getCntrNo())) {
			map.put("cntrNo", insurApplCvTask.getCntrNo());
		}
		if (!StringUtils.isBlank(insurApplCvTask.getSgNo())) {
			map.put("sgNo", insurApplCvTask.getSgNo());
		}
		RetInfo retInfo = new RetInfo();
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if(grpInsurAppl == null){
			retInfo.setRetCode(succFlag[2]);
			retInfo.setErrMsg("该保单["+JSON.toJSONString(map)+"]:无保单信息");
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			logger.error("该保单-"+JSON.toJSONString(map)+":无保单信息");
			return retInfo;
		}
		insurApplCvTask.setApplNo(grpInsurAppl.getApplNo());
		/*查询操作轨迹,果然操作轨迹最新操作不为打印，则不允许回执核销*/
		Map<String, Object> applMap = new HashMap<>();
		applMap.put("applNo",insurApplCvTask.getApplNo());
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, applMap);			
		if(null == insurApplOperTrace || null == insurApplOperTrace.getCurrentTraceNode()){
			retInfo.setRetCode(succFlag[2]);
			retInfo.setErrMsg("该保单["+insurApplCvTask.getApplNo()+"]:无操作轨迹信息,不允许回执核销!");
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			logger.error("该保单["+insurApplCvTask.getApplNo()+"]:无操作轨迹信息,不允许回执核销!");
			return retInfo;
		}
		TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByApplNo(insurApplCvTask.getApplNo());
		if(null == taskCntrDataLandingQueue){
			retInfo.setRetCode(succFlag[2]);
			retInfo.setErrMsg("该投保单["+insurApplCvTask.getApplNo()+"]:在落地数据监控表中无该保单数据,不允许回执核销!");
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			logger.error("该投保单["+insurApplCvTask.getApplNo()+"]:在落地数据监控表中无该保单数据,不允许回执核销!");
			return retInfo;
		}
//		if(!StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(), NEW_APPL_STATE.PRINT.getKey())
//			&& !StringUtils.equals("Y", taskCntrDataLandingQueue.getExtKey1())){
//			retInfo.setRetCode(succFlag[2]);
//			retInfo.setErrMsg("该保单["+insurApplCvTask.getApplNo()+"]:操作轨迹最新轨迹不是打印,不允许回执核销!");
//			retInfo.setApplNo(insurApplCvTask.getApplNo());
//			logger.error("该保单["+insurApplCvTask.getApplNo()+"]:操作轨迹最新轨迹是打印,不允许回执核销!");
//			return retInfo;
//		}
		//校验签收日期
		try {
			if(insurApplCvTask.getSignDate() == null){
				retInfo.setRetCode(succFlag[2]);
				retInfo.setErrMsg("该保单["+insurApplCvTask.getApplNo()+"]:签收日期为空!");
				retInfo.setApplNo(insurApplCvTask.getApplNo());
				logger.error("该保单["+insurApplCvTask.getApplNo()+"]:签收日期为空!");
				return retInfo;
			}else if(null != insurApplOperTrace.getCurrentTraceNode().getProcDate() && (insurApplCvTask.getSignDate().getTime() - df.parse(df.format(insurApplOperTrace.getCurrentTraceNode().getProcDate())).getTime() < 0)){
				retInfo.setRetCode(succFlag[2]);
				retInfo.setErrMsg("该保单["+insurApplCvTask.getApplNo()+"]:签收日期["+ df.format(insurApplCvTask.getSignDate()) +"]应该大于或等于保单打印日期["+df.format(insurApplOperTrace.getCurrentTraceNode().getProcDate()) +"]!");
				retInfo.setApplNo(insurApplCvTask.getApplNo());
				logger.error("该保单["+insurApplCvTask.getApplNo()+"]:签收日期["+ df.format(insurApplCvTask.getSignDate()) +"]应该大于或等于保单打印日期["+df.format(insurApplOperTrace.getCurrentTraceNode().getProcDate()) +"]!");
				return retInfo;
			}		
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retInfo.setRetCode(succFlag[2]);
			retInfo.setErrMsg("时间格式转换异常:"+e.getMessage());
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			return retInfo;
		}

		/*对于档案清单的处理，如果有补导入，二次落地成功或者无档案清单补导入才能允许回执核销
		 *档案清单已开户，第一次落地完毕时-A,二次落地成功-S,二次落地失败-F,无档案清单补导入-N,正在开户中-K,清单落地异常-"E"*/
		if (StringUtils.equals(LST_PROC_TYPE.ARCHIVES_LIST.getKey(),  grpInsurAppl.getLstProcType())){
			String flag = archiveListMonitor.query(insurApplCvTask.getApplNo());
			if(!("S".equals(flag) || "N".equals(flag))){
				retInfo.setRetCode(succFlag[2]);
				retInfo.setErrMsg("该保单"+insurApplCvTask.getApplNo()+":为档案清单补导入的单子,补导入流程未完成不允许回执核销,状态为["+flag+"]!");
				retInfo.setApplNo(insurApplCvTask.getApplNo());
				return retInfo;
			}

		}

	     /* 是否落地在途，即财务，被保人、保单基本信息、共保协议等是否落地完成
	     * 1.未生效
	     * 2.生效、落地在途
	     * 3.已落地
	     */
		RetInfo ret = insurApplOperUtils.getIsInsurApplLanding(insurApplCvTask.getApplNo());
		if(StringUtils.equals("1", ret.getRetCode())){
			retInfo.setRetCode(succFlag[2]);
			retInfo.setErrMsg("该保单"+insurApplCvTask.getApplNo()+":未生效,不允许回执核销!");
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			return retInfo;
		}else if(StringUtils.equals("2", ret.getRetCode())){
			retInfo.setRetCode(succFlag[2]);
			retInfo.setErrMsg("该保单"+insurApplCvTask.getApplNo()+":生效、落地在途,不允许回执核销!");
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			return retInfo;
		}
		//校验回执核销表是否已存数据
		InsurApplCvTask queryInsurApplCvTask = (InsurApplCvTask)mongoBaseDao.findOne(InsurApplCvTask.class,map);
		if (null != queryInsurApplCvTask && queryInsurApplCvTask.getStatus().equals(cvFlag[1])) {
			retInfo.setApplNo(insurApplCvTask.getApplNo());
			retInfo.setRetCode(succFlag[3]);
			retInfo.setErrMsg("合同号[" + queryInsurApplCvTask.getCgNo() + "]:已核销");
			logger.error("合同号[" + queryInsurApplCvTask.getCgNo() + "]:已核销");
			return retInfo;
		}
		/* 对insurApplCvTask进行赋值 */
		insurApplCvTask.setCgNo(grpInsurAppl.getCgNo());
		insurApplCvTask.setSgNo(grpInsurAppl.getSgNo());
		if (grpInsurAppl.getLstProcType().equals(LST_PROC_TYPE.NONE_LIST.getKey())) {//是否有清单
			insurApplCvTask.setAttachedFlag(attachedFlag[0]);
		} else {
			insurApplCvTask.setAttachedFlag(attachedFlag[1]);
		}
		insurApplCvTask.setCntrType(grpInsurAppl.getCntrType());//保单类型
		insurApplCvTask.setPolCode(grpInsurAppl.getFirPolCode());//主险
		insurApplCvTask.setRespDate(new Date());//核销日期
		insurApplCvTask.setStatus(cvFlag[0]);//核销状态-N-未核销或核销失败
		if(StringUtils.isBlank(insurApplCvTask.getMgBranch())){
			insurApplCvTask.setMgBranch(grpInsurAppl.getMgrBranchNo());//管理机构
		} 
		/*受理机构赋值*/
		for(TraceNode traceNode:insurApplOperTrace.getOperTraceDeque()){
			if(StringUtils.equals(traceNode.getProcStat(),NEW_APPL_STATE.ACCEPTED.getKey())
					&& StringUtils.isBlank(insurApplCvTask.getAccBranch())){
				insurApplCvTask.setAccBranch(traceNode.getPclkBranchNo());//受理机构
			}
		}			
		insurApplCvTask.setInForceDate(grpInsurAppl.getInForceDate());//合同生效日期
		insurApplCvTask.setCntrExpiryDate(grpInsurAppl.getCntrExpiryDate());//合同预计满期日期
		if(null != grpInsurAppl.getSalesInfoList()){
			SalesInfo salesInfo = getSalesInfo(grpInsurAppl);
			if(StringUtils.isBlank(insurApplCvTask.getSalesName())){
				insurApplCvTask.setSalesName(salesInfo.getSalesName());//销售员姓名
			} 
			if(StringUtils.isBlank(insurApplCvTask.getSalesNo())){
				insurApplCvTask.setSalesNo(salesInfo.getSalesNo());//销售员号	
			}		
			insurApplCvTask.setSalesBranchNo(salesInfo.getSalesBranchNo());//销售机构号
		}
		insurApplCvTask.setTimeOutDayLimit(checkDay);//允许超时天数	默认为15天
		/*将回执核销数据插入数据库*/
		if (null == queryInsurApplCvTask) {
			mongoBaseDao.insert(insurApplCvTask);
		}else {
			mongoBaseDao.remove(InsurApplCvTask.class, map);
			mongoBaseDao.insert(insurApplCvTask);
		}
		/*组  PCMS InsurApplCvTaskService 回执核销数据*/
		insurApplCvTaskBo.setCgNo(insurApplCvTask.getCgNo());//组合保单号
		insurApplCvTaskBo.setApplNo(insurApplCvTask.getApplNo());//投保单号
		insurApplCvTaskBo.setAccBranch(StringUtils.isBlank(insurApplCvTask.getAccBranch())?insurApplCvTask.getMgBranch():insurApplCvTask.getAccBranch());//受理机构
		insurApplCvTaskBo.setAttachedFlag(insurApplCvTask.getAttachedFlag());//是否附有清单
		insurApplCvTaskBo.setMgBranch(insurApplCvTask.getMgBranch());//管理机构
		insurApplCvTaskBo.setPolCode(insurApplCvTask.getPolCode());//险种代码(第一主险)
		insurApplCvTaskBo.setRespDate(insurApplCvTask.getRespDate());//核销日期
		insurApplCvTaskBo.setCntrType(insurApplCvTask.getCntrType());//保单类别
		retInfo.setRetCode(succFlag[1]);
		retInfo.setApplNo(insurApplCvTask.getApplNo());
		retInfo.setErrMsg("检查通过");
		return retInfo;
	}
	/*对返回值进行处理*/
	private void insurApplCvTaskReturn(RetInfo retInfoRet) {
		/*对返回参数进行处理，组返回数据*/
		if(null == retInfoRet){
			return;
		}
		Map<String, Object> mapUpdate = new HashMap<String, Object>();
		mapUpdate.put("applNo", retInfoRet.getApplNo());
		Update update = new Update();
		if (StringUtils.equals(retInfoRet.getRetCode(), succFlag[1])) {
			/* 回执核销表 status-C 回执核销完成*/
			mongoBaseDao.updateAll(InsurApplCvTask.class, mapUpdate, update.set("status", cvFlag[1]).set("description", "核销成功"));
			/* 团体出单表  完成回执核销*/
			InsurApplCvTask insurApplCvTask = (InsurApplCvTask) mongoBaseDao.findOne(InsurApplCvTask.class, mapUpdate);
			/*核保节点*/
			/*对轨迹操作InsurApplOperTrace 增加核保节点*/
			TraceNode traceNode = new TraceNode();
			traceNode.setProcStat(NEW_APPL_STATE.RECEIPT_VERIFICA.getKey());//回执核销
			if(null != insurApplCvTask){												
				traceNode.setPclkBranchNo(insurApplCvTask.getOclkBranchNo());//回执核销操作员机构
				traceNode.setPclkName(insurApplCvTask.getOclkClerkName());//回执核销操作员姓名
				traceNode.setPclkNo(insurApplCvTask.getOclkClerkNo());//回执核销操作员代码
				traceNode.setProcDate(insurApplCvTask.getRespDate());//回执核销完成日期
			}
			mongoBaseDao.updateOperTrace(retInfoRet.getApplNo(), traceNode);
		}else if(StringUtils.equals(retInfoRet.getRetCode(), succFlag[0])){
			/*回执核销失败，status-F 回执核销失败*/
			logger.error("保单" + retInfoRet.getApplNo() + "调用PCMS回执核销失败：" +retInfoRet.getErrMsg());
			mongoBaseDao.updateAll(InsurApplCvTask.class, mapUpdate, update.set("status", cvFlag[4]).set("description", retInfoRet.getErrMsg()));
		}
	}
	/*调用PCMS服务*/
	private  RetInfo callServer(InsurApplCvTaskBo insurapplcvtaskbo ) {
		RetInfo retInfoRet = new RetInfo();
		try {
			List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), insurapplcvtaskbo.getApplNo(), "N");
			if(taskPrmyInfos==null||taskPrmyInfos.isEmpty()){
				retInfoRet.setApplNo(insurapplcvtaskbo.getApplNo());
				retInfoRet.setRetCode(succFlag[0]);
				retInfoRet.setErrMsg("投保单"+insurapplcvtaskbo.getApplNo()+":当前流程不处于回执核销!");
				return retInfoRet;
			}
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);			
			com.newcore.orbps.models.pcms.bo.RetInfo retInfo = insurApplCvTaskServiceClient.addInsurApplCvTask(insurapplcvtaskbo);
			logger.debug("调用PCMS回执核销返回值:"+ JSONObject.toJSONString(retInfo));
			//添加回执核销任务完成调用
			if(StringUtils.equals(retInfo.getRetCode(),"1")){
				completeTask(insurapplcvtaskbo,taskPrmyInfos);
			}
			retInfoRet.setApplNo(insurapplcvtaskbo.getApplNo());
			retInfoRet.setErrMsg(retInfo.getErrMsg());
			retInfoRet.setRetCode(retInfo.getRetCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			retInfoRet.setApplNo(insurapplcvtaskbo.getApplNo());
			retInfoRet.setErrMsg("投保单"+insurapplcvtaskbo.getApplNo()+"回执核销时出现异常:"+e.getMessage());
			retInfoRet.setRetCode(succFlag[0]);
		}
		return retInfoRet;
	}
	private SalesInfo getSalesInfo(GrpInsurAppl grpInsurAppl){
		for(SalesInfo salesInfo1:grpInsurAppl.getSalesInfoList()){
			if(StringUtils.equals(DEVEL_MAIN_FLAG.MASTER_SALESMAN.getKey(),salesInfo1.getDevelMainFlag())){
				return salesInfo1;
			}
		}
		return grpInsurAppl.getSalesInfoList().get(0);
	}
	//回执核销调用任务完成方法
	private void completeTask(InsurApplCvTaskBo insurapplcvtaskbo,List<TaskPrmyInfo> taskPrmyInfos) throws ParseException{
		String pattern = "yyyy-MM-dd HH:mm:ss";
		for(TaskPrmyInfo taskPrmyInfo:taskPrmyInfos){
			TaskProcessRequestBO taskProcessbo = new TaskProcessRequestBO();
			taskProcessbo.setTaskId(taskPrmyInfo.getTaskId());
			taskProcessServiceDascClient.completeTask(taskProcessbo);
			taskPrmyInfo.setStatus("C");
			Date createTime = null;
			String createTimestr = null;
			if(!StringUtils.isEmpty(taskPrmyInfo.getCreateTime())){
				createTime = DateUtils.parseDate(taskPrmyInfo.getCreateTime(), "yyyy-MM-dd HH:mm:ss.SSS");
			}
			if(createTime!=null){
				createTimestr = DateFormatUtils.format(createTime, pattern);
			}
			taskPrmyInfo.setCreateTime(createTimestr);
			taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), taskPrmyInfo);
		}
	}
	private void analysisFile(String fileName,List<InsurApplCvTask> insurApplCvTasks,String batNo){	
		String filePath =  PropertiesUtils.getProperty("fs.ipsnlst.ipsn.dir") +File.separator+fileName;
		/*POI 03*/
		try {
			if(fileName.matches("\\d+.xls$")){
				readXLS(filePath,insurApplCvTasks);
			}else if(fileName.matches("\\d+.xlsx$")){/*POI 07*/
				readXLSX(filePath,insurApplCvTasks);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
			insurApplCvTask.setBatNo(batNo);
			insurApplCvTask.setStatus(cvFlag[2]);//异常
			insurApplCvTask.setDescription("文件处理异常:"+e.getMessage());
			mongoBaseDao.insert(insurApplCvTask);
		}

	}
	private  void createErrFile(List<RetInfo> retInfos,String batNo){		
		String filePath =  PropertiesUtils.getProperty("fs.ipsn.err.path") +File.separator+batNo+"_Fail.xlsx";
		XSSFWorkbook workbook = new XSSFWorkbook();
		File file = new File(filePath);
		try {
			int sheetNum = 0;
			FileOutputStream out =new FileOutputStream(file);
			XSSFSheet sheet=workbook.createSheet();
			workbook.setSheetName(sheetNum, "回执核销错误清单");
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			XSSFRow headrow = sheet.createRow(0);
			createXCell(headrow, 0, "投保单号", cellStyle);
			createXCell(headrow, 1, "回执核销状态", cellStyle);
			createXCell(headrow, 2, "回执核销失败原因", cellStyle);
			int rowNum = 1;
			for(RetInfo retInfo:retInfos){
				if(StringUtils.equals(succFlag[0], retInfo.getRetCode()) || StringUtils.equals(succFlag[2], retInfo.getRetCode())){
					XSSFRow row = sheet.createRow(rowNum++);
					createXCell(row, 0, retInfo.getApplNo(), cellStyle);
					createXCell(row, 1, "失败", cellStyle);
					createXCell(row, 2, retInfo.getErrMsg(), cellStyle);
				}
			}
			workbook.write(out);
			out.flush();
			IOUtils.closeQuietly(out);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}finally{		
			IOUtils.closeQuietly(workbook);
		}
	}
	public static void createXCell(XSSFRow row,int index, String value, XSSFCellStyle cellStyle) {
		// 创建单元格（左上端）
		XSSFCell cell = row.createCell(index);  
		// 定义单元格为字符串类型  
		cell.setCellType(CellType.STRING);
		// 格式
		cell.setCellStyle(cellStyle);
		// 在单元格中输入一些内容 
		cell.setCellValue(value);
	}
	private void readXLS(String path,List<InsurApplCvTask> insurApplCvTasks) throws IOException{
		HSSFWorkbook hssfWorkbook = null;
		try (InputStream in = new FileInputStream(path)){
			hssfWorkbook = new HSSFWorkbook(in);
			/*获取第一个表单*/
			HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0);
			// 得到总行数
			int rowNum = hssfSheet.getLastRowNum();
			for(int i = 1;i<=rowNum;i++){
				HSSFRow hssfRow = hssfSheet.getRow(i);
				if(null != hssfRow){
					HSSFCell cell = hssfRow.getCell(0);
					String applNo = String.valueOf(null == cell?"":cell.getRichStringCellValue());
					if(!StringUtils.isEmpty(applNo)){
						InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
						insurApplCvTask.setApplNo(applNo);
						insurApplCvTask.setSignDate(hssfRow.getCell(1).getDateCellValue());
						insurApplCvTasks.add(insurApplCvTask);
					}
				}
			}
			IOUtils.closeQuietly(in);
		} catch (IOException e) {
			throw e;
		} finally{
			IOUtils.closeQuietly(hssfWorkbook);
		}

	}
	private void readXLSX(String path,List<InsurApplCvTask> insurApplCvTasks) throws IOException{
		XSSFWorkbook xssfWorkbook = null;
		try (InputStream in = new FileInputStream(path)){
			xssfWorkbook = new XSSFWorkbook(in);
			/*获取第一个表单*/
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
			// 得到总行数
			int rowNum = xssfSheet.getLastRowNum();
			for(int i = 1;i<=rowNum;i++){
				XSSFRow xssfRow = xssfSheet.getRow(i);
				if(null != xssfRow){
					XSSFCell cell = xssfRow.getCell(0);
					String applNo = String.valueOf(null == cell?"":cell.getRichStringCellValue());
					if(!StringUtils.isEmpty(applNo)){
						InsurApplCvTask insurApplCvTask = new InsurApplCvTask();
						insurApplCvTask.setApplNo(applNo);
						insurApplCvTask.setSignDate(xssfRow.getCell(1).getDateCellValue());
						insurApplCvTasks.add(insurApplCvTask);
					}
				}
			}
			IOUtils.closeQuietly(in);
	
		} catch (IOException e) {
			throw e;
		}finally {
			IOUtils.closeQuietly(xssfWorkbook);
		}
	}

	public RetInfo inForm(CvInformVo cvInformVo) {
		RetInfo retInfo = new RetInfo();  //返回值定义
		//判断入参是否为空
		if (null == cvInformVo) { 
			logger.error("入参cvInformVo为空");
			retInfo.setRetCode(succFlag[0]);
			retInfo.setErrMsg("入参cvInformVo为空");
			return retInfo;
		}
		try{
			ValidationUtils.validate(cvInformVo); //验证
		}catch(BusinessException e){
			logger.error(e.getMessage(), e);
			retInfo.setRetCode(succFlag[0]);
			retInfo.setErrMsg("入参校检失败:"+e.getMessage());
			retInfo.setApplNo(cvInformVo.getApplNo());
			return retInfo;
		}
		/* 如果投保单号为空  则需要根据投保单号合同号，合同组号，汇缴号，投保申请号查询保单信息 获得投保单号*/
		if (StringUtils.isBlank(cvInformVo.getApplNo())) {
			Map<String, Object> map = new HashMap<>();//入参
			if (!StringUtils.isBlank(cvInformVo.getCgNo())) {
				map.put("cgNo", cvInformVo.getCgNo());
			}
			if (!StringUtils.isBlank(cvInformVo.getCntrNo())) {
				map.put("applState.policyList.cntrNo", cvInformVo.getCntrNo());  //cntrNo没在投保单下
			}			 
			if (!StringUtils.isBlank(cvInformVo.getSgNo())) {
				map.put("sgNo", cvInformVo.getSgNo());
			}
			if(map.isEmpty()){
				retInfo.setRetCode(succFlag[0]);
				retInfo.setErrMsg("投保单号为空时,投保单合同号，合同组号，汇缴号必传其中一个");
				retInfo.setApplNo(cvInformVo.getApplNo());
				logger.error("该保单-"+JSON.toJSONString(map)+":无保单信息");
				return retInfo;
			}
			GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
			if(grpInsurAppl == null){
				retInfo.setRetCode(succFlag[0]);
				retInfo.setErrMsg("该保单["+JSON.toJSONString(map)+"]:无保单信息");
				retInfo.setApplNo(cvInformVo.getApplNo());
				logger.error("该保单-"+JSON.toJSONString(map)+":无保单信息");
				return retInfo;
			}
			cvInformVo.setApplNo(grpInsurAppl.getApplNo());
		}
		//查询当前任务是否已到回执核销
		List<TaskPrmyInfo> taskPrmyInfos = taskPrmyDao.selectTaskPrmyInfoByApplNo(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), cvInformVo.getApplNo(), STATUS);
		if(taskPrmyInfos==null||taskPrmyInfos.isEmpty()){
			retInfo.setApplNo(cvInformVo.getApplNo());
			retInfo.setRetCode(succFlag[0]);
			retInfo.setErrMsg("投保单"+cvInformVo.getApplNo()+":当前流程不处于回执核销||或者已经完成回执核销");
			return retInfo;
		}
		for(TaskPrmyInfo taskPrmyInfo:taskPrmyInfos){
			//如果当前状态不为C 则需要回执核销
				try {
					//回执核销增加签收日期， 自动回执核销会根据有无事件进行核销
					System.out.println(dateFormat.format(cvInformVo.getSignDate().getTime()));
					taskPrmyInfo.setExtKey1(dateFormat.format(cvInformVo.getSignDate().getTime()));
					boolean flag = taskPrmyDao.updateTaskPrmyInfoBytaskSeq(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(),taskPrmyInfo);
					if(!flag){
						retInfo.setRetCode(succFlag[0]);
						retInfo.setApplNo(cvInformVo.getApplNo());
						retInfo.setErrMsg("回执核销失败");
					}
				} catch (ParseException e) {
						logger.error(e.getMessage(), e);
						retInfo.setRetCode(succFlag[0]);
						retInfo.setErrMsg("sql失败:"+e.getMessage());
						retInfo.setApplNo(cvInformVo.getApplNo());
						return retInfo;
				}
			}
		retInfo.setRetCode(succFlag[1]);
		retInfo.setApplNo(cvInformVo.getApplNo());
		retInfo.setErrMsg("回执核销成功");
		return retInfo;
	}
}
