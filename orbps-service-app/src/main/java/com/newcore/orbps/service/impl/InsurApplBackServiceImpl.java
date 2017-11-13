package com.newcore.orbps.service.impl;

import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.newcore.supports.dicts.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.TaskPrmyDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.opertrace.TraceNodePra;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.taskprmy.QueueType;
import com.newcore.orbps.models.taskprmy.TaskPrmyInfo;
import com.newcore.orbps.models.uwbps.AltBussStatInVO;
import com.newcore.orbps.service.api.InsurApplBackService;
import com.newcore.orbps.service.api.ProcCorrMioDataService;
import com.newcore.orbps.service.uwbps.api.UnderWritingService;
import com.newcore.orbpsutils.dao.api.ConfigDao;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 保单回退
 *
 * @author chenyongcai 创建时间：2016年11月4日上午10:36:28
 */
@Service("insurApplBackService")
public class InsurApplBackServiceImpl implements InsurApplBackService {
    /**
     * 日志管理工具实例.
     */
    private static Logger logger = LoggerFactory.getLogger(InsurApplBackServiceImpl.class);
    // 基本信息录入
    private static final String INFO_INPUT = "1";
    // 复核
    private static final String INFO_REVIEW = "2";

    @Autowired
    TaskPrmyDao taskPrmyDao;

    @Autowired
    TaskProcessService taskProcessServiceDascClient;

    @Autowired
    MongoBaseDao mongoBaseDao;

    @Autowired
    ProcCorrMioDataService procCorrMioDataService;

    @Autowired
    ConfigDao configDao;

    @Autowired
    UnderWritingService underWritingServiceClient;

    private static final String PROPERTIE_TYPE = "UDW_OFF";  //配置类型
    private final static String ACCESS_SOURCE_GCSS = "GCSS";

    @Override
    @AsynCall
    public RetInfo backDeal(TraceNodePra traceNodePra) {
        if (traceNodePra == null || StringUtils.isEmpty(traceNodePra.getApplNo())
                || traceNodePra.getTraceNode() == null) {
            throw new BusinessException("0018", "入参");
        }
        if (StringUtils.isEmpty(traceNodePra.getTraceNode().getPclkBranchNo())
                || StringUtils.isEmpty(traceNodePra.getTraceNode().getProcStat())
                || StringUtils.isEmpty(traceNodePra.getTraceNode().getPclkNo())) {
            throw new BusinessException("0018", "操作员信息为空");
        }
        String applNo = traceNodePra.getApplNo();
        RetInfo retInfo = new RetInfo();

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("applNo", applNo);
        GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, param);
        if (grpInsurAppl == null) {
            logger.error("保单基本信息不存在: " + applNo);
            throw new BusinessException("0016", applNo);
        }

//		if (!StringUtils.equals(grpInsurAppl.getLstProcType(), LST_PROC_TYPE.ORDINARY_LIST.getKey())
//				&& StringUtils.equals(traceNodePra.getTraceNode().getProcStat(), INFO_IMPORT)) {
//
//			throw new BusinessException("0021", "非普通清单，不能回退到清单录入。");
//		}

        // 根据投保单号获取操作轨迹信息
        InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,
                param);

        if ((StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(), NEW_APPL_STATE.ACCEPTED.getKey())
                || StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(),
                NEW_APPL_STATE.RECORD_TEMPORARILY.getKey())
                || StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(),
                NEW_APPL_STATE.GRP_ENTRY.getKey())
                || StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(),
                NEW_APPL_STATE.GRP_IMPORT.getKey())
                || StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(),
                NEW_APPL_STATE.LIST_ENTRY.getKey())
                || StringUtils.equals(insurApplOperTrace.getCurrentTraceNode().getProcStat(),
                NEW_APPL_STATE.LIST_IMPORT.getKey()))
                && StringUtils.equals(traceNodePra.getTraceNode().getProcStat(), INFO_REVIEW)) {
            throw new BusinessException("0021", "不能回退到复核。");
        }

        // 插入操作轨迹
        TraceNode traceNode = traceNodePra.getTraceNode();
        if (traceNode == null || (!StringUtils.equals(traceNode.getProcStat(), INFO_INPUT)
                && !StringUtils.equals(traceNode.getProcStat(), INFO_REVIEW))) {
            logger.error("操作节点信息为空: " + applNo);
            throw new BusinessException("0019", applNo + "入参不合法");
        }

        // 电子保单
        if (StringUtils.equals(grpInsurAppl.getCntrPrintType(), CNTR_PRINT_TYPE.ELEC_INSUR.getKey())) {

            List<TaskPrmyInfo> list = taskPrmyDao
                    .selectTaskPrmyInfoByApplNo(QueueType.RECEIPT_VERIFICA_TASK_QUEUE.toString(), applNo, "A");
            if (list != null && !list.isEmpty()) {

                throw new BusinessException("0018", "回执在途,不能回退");
            }
        }

        // 处理投保单回退过程中实收数据冲正流水
        retInfo = procCorrMioDataService.setCorrInsurInfo(applNo, traceNode.getPclkBranchNo(), traceNode.getPclkNo(), "Y");
        if ("0".equals(retInfo.getRetCode())) { // 调用回退处理方法失败
            return retInfo;
        }

        TraceNode traceNodeTmp = new TraceNode();
        traceNodeTmp.setPclkBranchNo(traceNode.getPclkBranchNo());
        traceNodeTmp.setPclkName(traceNode.getPclkName());
        traceNodeTmp.setPclkNo(traceNode.getPclkNo());
        traceNodeTmp.setProcStat(NEW_APPL_STATE.BACK.getKey());
        traceNodeTmp.setProcDate(new Date());
        traceNodeTmp.setDescription(traceNode.getDescription());
        mongoBaseDao.updateOperTrace(applNo, traceNodeTmp);

        // 调用回退处理方法成功，撤销任务流程
        TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
        Map<String, String> mapVar = new HashMap<>();
        if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
            mapVar.put("CNTR_TASK_SUB_ITEM", "GRP");
        }
        if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
            mapVar.put("CNTR_TASK_SUB_ITEM", "LST");
        }
        String node_status = "";
        if (StringUtils.equals(INFO_INPUT, traceNodePra.getTraceNode().getProcStat())
                && StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
            node_status = "2";
        } else if (StringUtils.equals(INFO_REVIEW, traceNodePra.getTraceNode().getProcStat())
                && StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
            node_status = "4";
        } else if (StringUtils.equals(INFO_INPUT, traceNodePra.getTraceNode().getProcStat())
                && StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
            node_status = "1";
        } else if (StringUtils.equals(INFO_REVIEW, traceNodePra.getTraceNode().getProcStat())
                && StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
            node_status = "3";
        }
        mapVar.put("NODE_STATUS", node_status);

        Map<String, Object> applNoMap = new HashMap<String, Object>();
        applNoMap.put("applNo", traceNodePra.getApplNo());
        applNoMap.put("operTraceDeque.procStat", NEW_APPL_STATE.ACCEPTED.getKey());
        InsurApplOperTrace applOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class, applNoMap);
        if (StringUtils.equals(grpInsurAppl.getAccessSource(), ACCESS_SOURCE_GCSS)) {
            mapVar.put("ACCEPT_BRANCH_NO", grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());
        } else {
            mapVar.put("ACCEPT_BRANCH_NO", applOperTrace.getCurrentTraceNode().getPclkBranchNo());
        }

        //如果service.udw.enabled 配置为Y 则需要重新发起流程3  否则发起流程2
        String procSwitch = configDao.queryPropertiesConfigure(PROPERTIE_TYPE, applOperTrace.getOperTraceDeque().getLast().getPclkNo(), null);
        if (StringUtils.equals(procSwitch, YES_NO_FLAG.YES.getKey())) {
            taskProcessRequestBO.setProcessDefKey("ECORBP003");// 越过核保出单流程
        } else {
            taskProcessRequestBO.setProcessDefKey("ECORBP002");//全流程柜面出单
        }

        taskProcessRequestBO.setBusinessKey(applNo);// 必填-业务流水号
        taskProcessRequestBO.setProcessVar(mapVar);
        //判断是否二次回退
        boolean b = false;
        Deque<TraceNode> traceNodes = insurApplOperTrace.getOperTraceDeque();
        if (traceNodes != null) {
            for (TraceNode traceNode2 : traceNodes) {
                if (StringUtils.equals(traceNode2.getProcStat(), NEW_APPL_STATE.BACK.getKey())) {
                    b = true;
                    break;
                }
            }
        }
        if (StringUtils.equals(grpInsurAppl.getAccessSource(), "GCSS") && !b) {
            TaskProcessRequestBO taskProcessRequestBO1 = new TaskProcessRequestBO();
            taskProcessRequestBO1.setProcessDefKey("ECORBP001");
            taskProcessRequestBO1.setBusinessKey(applNo);
            taskProcessServiceDascClient.deleteProcessInstance(taskProcessRequestBO1);
            taskProcessServiceDascClient.startProcess(taskProcessRequestBO);
        } else {
            taskProcessServiceDascClient.restartProcessInstance(taskProcessRequestBO);
        }
        logger.info("回退处理，调用任务管理平台的restartProcessInstance接口");

        try {
            // 通知核保，任务已撤销
            AltBussStatInVO altBussStatInVO = new AltBussStatInVO();
            altBussStatInVO.setBusinessKey(applNo);
            altBussStatInVO.setNewApplState("W");
            altBussStatInVO.setPclkBranchNo(traceNode.getPclkBranchNo());
            altBussStatInVO.setPclkName(traceNode.getPclkName());
            altBussStatInVO.setPclkNo(traceNode.getPclkNo());
            CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
            HeaderInfoHolder.setOutboundHeader(headerInfo);
            underWritingServiceClient.updateNewApplStat(altBussStatInVO);
        } catch (Exception e) {
            logger.error("通知核保出错", e);
        }
        retInfo.setRetCode("1");
        return retInfo;
    }
}
