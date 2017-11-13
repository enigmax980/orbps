package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.common.SpringContextHolder;
import com.halo.core.dasc.annotation.AsynCall;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.ErrListInfoBo;
import com.newcore.orbps.models.service.bo.GrpInsurApplArrivalControlBo;
import com.newcore.orbps.models.service.bo.GrpInsuredBatchRecord;
import com.newcore.orbps.models.service.bo.RetInfo;
import com.newcore.orbps.service.api.CgInsuredTreatResult;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 接受清单导入处理结果
 *
 * @author lijifei
 * 创建时间：2016年8月19日09:24:11
 */

@Service("cgInsuredTreatResult")
public class CgInsuredTreatResultImpl implements CgInsuredTreatResult {


	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	MongoBaseDao mongoBaseDao;


	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(CgInsuredTreatResultImpl.class);


	/*流程说明：当pcms将清单数据导入核心库失败时，调用该服务。具体如下:
	 * 	     1.更新保单落地信息控制表  “状态”字段，如果有错误数据更新为6-->清单中存在错误数据，带修正！否则更新为1-->成功
	 * 		 2.更新清单批次信息记录表 “成功笔数”，“失败笔数”字段
	 * */
	@Override
	@AsynCall
	public List<RetInfo> doCgInsuredTreatResult(List<GrpInsuredBatchRecord> grpInsuredList){
		//清单导入详细错误信息
		ErrListInfoBo errListInfo =new ErrListInfoBo();
		//清单数据落地完成情况告知服务-接口返回bo
		List<RetInfo> retInfoList = new ArrayList<>();
		//保单落地控制表-封装参数使用
		GrpInsurApplArrivalControlBo grpApplArrConBo=new GrpInsurApplArrivalControlBo();
		for (Object object:grpInsuredList) {
			//清单批次信息记录
			GrpInsuredBatchRecord grpInsuredBatchRecord;
			if(object instanceof JSONObject){
				JSONObject jsonObject = (JSONObject)object;
				grpInsuredBatchRecord = JSONObject.toJavaObject(jsonObject, GrpInsuredBatchRecord.class);
			}else{
				grpInsuredBatchRecord = (GrpInsuredBatchRecord)object;
			}
			RetInfo retInfo = new RetInfo();
			retInfo.setCgNo(null);
			retInfo.setRetCode("0");
			retInfo.setErrMsg("数据为空！");
			//获得清单批次信息
			//组合保单号 
			String cgNoStr=grpInsuredBatchRecord.getCgNo();
			//批次号
			String batchNoStr=grpInsuredBatchRecord.getBatchNo();
			//成功笔数   
			String succNumStr=grpInsuredBatchRecord.getSuccNum();
			//失败笔数   
			String failNumStr=grpInsuredBatchRecord.getFailNum();
			grpApplArrConBo.setCgNo(cgNoStr);
			grpApplArrConBo.setBatchNo(String.valueOf(batchNoStr));
			//判断参数是否为空
			StringBuilder stb=new StringBuilder();
			if(StringUtils.isBlank(cgNoStr)){
				stb.append("[组合保单号为空+]");
			}
			if(StringUtils.isBlank(batchNoStr)){
				stb.append("[批次号为空+]");
			}
			if(StringUtils.isBlank(failNumStr)){
				stb.append("[失败笔数为空+]");
			}
			if(StringUtils.isBlank(succNumStr)){
				stb.append("[成功笔数为空+]");
			}
			/**
			 * 与保单辅助约定,当保单辅助回调该接口时，succNumStr=0 failNumStr=0, 认为是清单告知时 告知数与mongodb中数据不一致。保单辅助不做清单导入直接返回。
			 * 本处将修改[保单落地控制表]保单对应的 状态字段为‘8’，重新触发，清单告知。
			 * */
			boolean  flag = true;
			if("0".equals(succNumStr) && "0".equals(failNumStr)){
				flag = false;
				String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ? and BATCH_NO=? ";
				jdbcTemplate.update(updatesql, "8" ,"保单落地成功！",grpApplArrConBo.getCgNo(),grpApplArrConBo.getBatchNo());
			}
			String applNoSql = "select APPL_NO from CNTR_EFFECT_CTRL WHERE CG_NO = ? and BATCH_NO=? ";
			String applNo = jdbcTemplate.queryForObject(applNoSql, String.class, cgNoStr,batchNoStr);
			//修改操作轨迹记录
			Map<String,Object> maps = new HashMap<>();
			maps.put("applNo", applNo);
			//查询收付费相关信息类，获得应收数据集合
			MioPlnmioInfo mioPlnmioInfo = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class, maps);
			@SuppressWarnings("unused")
			boolean plnmioFlag = false;
			@SuppressWarnings("unused")
			boolean successFlag = false;
			//如果数据都不为空
			if(StringUtils.isBlank(stb.toString()) && flag && null!=mioPlnmioInfo){
				//查询收费相关信息表，确认有几条应收数据。
				int plnmioNum = mioPlnmioInfo.getPlnmioNum();
				if(plnmioNum>1){
					//设置为true，判断是否转保费使用。
					plnmioFlag = true;
				}
				// 更新清单批次信息记录表 “成功笔数”，“失败笔数”字段
				String	updatBatch="update LIST_BATCH_RECORD SET SUCC_NUM = ? ,FAIL_NUM = ? WHERE CG_NO = ? and BATCH_NO=?";

				jdbcTemplate.update(updatBatch, succNumStr, failNumStr,cgNoStr,batchNoStr);

				String SELECT_ERR_LIST_INFO_SQL = "SELECT COUNT(*) FROM ERR_LIST_INFO WHERE MODIFY_FLAG = ? and CG_NO = ? ";
				errListInfo.setCgNo(cgNoStr);
				errListInfo.setModifyFlag("0");
				int count=jdbcTemplate.queryForObject(SELECT_ERR_LIST_INFO_SQL, int.class,errListInfo.getModifyFlag(),errListInfo.getCgNo());
				//更新保单落地信息控制表  “状态”字段，如果有错误数据更新为6-->清单中存在错误数据，带修正！否则更新为1-->成功
				if(Integer.valueOf(failNumStr)==0 && count==0){
					grpApplArrConBo.setProcStat("1");
					grpApplArrConBo.setProcCauseDesc("成功！");
					//设置为true，判断是否转保费使用。
					successFlag = true;
					//修改操作轨迹记录
					TraceNode traceNode= new TraceNode();
					traceNode.setProcDate(new Date());
					traceNode.setProcStat("16");
					mongoBaseDao.updateOperTrace(applNo,traceNode);

					String selectIdSql = "select ID from CNTR_EFFECT_CTRL WHERE CG_NO = ? and BATCH_NO=? ";
					String selectId = jdbcTemplate.queryForObject(selectIdSql, String.class, grpApplArrConBo.getCgNo(),grpApplArrConBo.getBatchNo());

					String selectIdSql2 = "select APPL_NO from CNTR_EFFECT_CTRL WHERE CG_NO = ? and BATCH_NO=? ";
					String applNo2 = jdbcTemplate.queryForObject(selectIdSql2, String.class, grpApplArrConBo.getCgNo(),grpApplArrConBo.getBatchNo());

					//当都成功时，调用打印流程
					TaskProcessService taskProcessService = SpringContextHolder.getBean("taskProcessServiceDascClient");// 为DASC配置文件中定义的ID
					TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
					taskProcessRequestBO.setBusinessKey(applNo2);
					taskProcessRequestBO.setTaskId(selectId);// 业务服务参数中获取的任务ID
					taskProcessService.completeTask(taskProcessRequestBO);// 进行任务完成操作

				}else if(Integer.valueOf(failNumStr)==0 && count!=0){
					//更改-保单落地信息控制表-处理状态为2（清单中存在错误信息，待修正）
					grpApplArrConBo.setProcStat("2");
					grpApplArrConBo.setProcCauseDesc("清单中存在错误信息，待修正!");
				}else{
					grpApplArrConBo.setProcStat("6");
					grpApplArrConBo.setProcCauseDesc("清单中存在错误数据！");
				}
				String	updatesql="update CNTR_EFFECT_CTRL SET PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ? and BATCH_NO=? ";
				grpApplArrConBo.setCgNo(cgNoStr);
				grpApplArrConBo.setBatchNo(String.valueOf(batchNoStr));
				jdbcTemplate.update(updatesql, grpApplArrConBo.getProcStat() ,grpApplArrConBo.getProcCauseDesc(),
						grpApplArrConBo.getCgNo(),grpApplArrConBo.getBatchNo());
				//反馈正确信息
				retInfo.setCgNo(cgNoStr);
				retInfo.setRetCode("1");
				retInfo.setErrMsg("success");
			}else{
				//反馈错误信息
				retInfo.setCgNo(cgNoStr);
				retInfo.setRetCode("0");
				retInfo.setErrMsg(stb.toString());
			}
			retInfoList.add(retInfo);
		}//end if
		return retInfoList;
	}

}
