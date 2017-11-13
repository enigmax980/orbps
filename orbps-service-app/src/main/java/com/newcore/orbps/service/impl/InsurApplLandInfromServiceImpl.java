package com.newcore.orbps.service.impl;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.dao.annotation.Transaction;
import com.newcore.orbps.models.finance.MioLog;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.vo.LandRetVo;
import com.newcore.orbps.models.taskprmy.TaskCntrDataLandingQueue;
import com.newcore.orbps.service.api.ArchiveListMonitor;
import com.newcore.orbps.service.api.InsurApplLandInfromService;
import com.newcore.orbpsutils.dao.api.MioLogDao;
import com.newcore.orbpsutils.dao.api.TaskCntrDataLandingQueueDao;
import com.newcore.orbpsutils.validation.ValidationUtils;
import com.newcore.supports.dicts.BT_SEND_CORE_STAT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * 保单落地告知服务:
 * Created by liushuaifeng on 2017/2/22 0022.
 */
@Service("insurApplLandInfromService")
public class InsurApplLandInfromServiceImpl implements InsurApplLandInfromService {

	@Autowired
	TaskCntrDataLandingQueueDao taskCntrDataLandingQueueDao;

	@Autowired
	MioLogDao mioLogDao;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	ArchiveListMonitor archiveListMonitor;

	private final static String LAND_TYPE_FIN = "1";
	private final static String LAND_TYPE_INSURED = "2";
	private final static String LAND_TYPE_PLN = "3";
	private final static String SUCESS_LAND_FLAG = "3";
	private final static String FAIL_LAND_FLAG = "4";
	private final static String SUCESS = "1";

	/**
	 * 保单辅助系统告知保单基本信息异步落地是否成功
	 *
	 * @param landRetVo
	 * @return
	 */
	@Transaction
	@Override
	public RetInfo infromLandResult(LandRetVo landRetVo) {
		//入参检查
		ValidationUtils.validate(landRetVo);
		//初始化返回参数
		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode(SUCESS);
		retInfo.setApplNo(landRetVo.getApplNo());

		//财务落地结果告知
		if (StringUtils.equals(LAND_TYPE_FIN, landRetVo.getType())) {

			if (StringUtils.equals(SUCESS, landRetVo.getRetCode())) {

				//根据批次号更新财务数据
				mioLogDao.updateMioLogStat(landRetVo.getBatNo(), BT_SEND_CORE_STAT.SUCCESSED_SEND.getKey());
				//更新mongodb里的财务数据状态
				Query query = new Query();
				query.addCriteria(Criteria.where("batNo").is(landRetVo.getBatNo()));//根据批次号进行更新
				Update update = new Update();
				update.set("coreStat", BT_SEND_CORE_STAT.SUCCESSED_SEND.getKey());
				mongoTemplate.updateMulti(query, update, MioLog.class);


			} else {
				//根据批次号更新财务数据
				mioLogDao.updateMioLogStat(landRetVo.getBatNo(), BT_SEND_CORE_STAT.FAILED_SEND.getKey());
				//更新mongodb里的财务数据状态
				Query query = new Query();
				query.addCriteria(Criteria.where("batNo").is(landRetVo.getBatNo()));//根据批次号进行更新
				Update update = new Update();
				update.set("coreStat", BT_SEND_CORE_STAT.FAILED_SEND.getKey());
				mongoTemplate.updateMulti(query, update, MioLog.class);

			}

		}

		TaskCntrDataLandingQueue taskCntrDataLandingQueue = taskCntrDataLandingQueueDao.searchByApplNo(landRetVo.getApplNo());
		if (null != taskCntrDataLandingQueue) { //说明监控表存在
			//财务落地成功，并且和监控表中的批次号相同
			if (StringUtils.equals(LAND_TYPE_FIN, landRetVo.getType()) &&
					(landRetVo.getBatNo().longValue() == taskCntrDataLandingQueue.getFinLandBatNo().longValue())) {

				if (StringUtils.equals(SUCESS, landRetVo.getRetCode())) {
					taskCntrDataLandingQueueDao.updateFinLandFlagByApplNo(landRetVo.getApplNo(), SUCESS_LAND_FLAG);

				}else {
					taskCntrDataLandingQueueDao.updateFinLandFlagByApplNo(landRetVo.getApplNo(), FAIL_LAND_FLAG);
				}
				archiveListMonitor.send(landRetVo.getApplNo());
			}
			//被保人落地告知
			if (StringUtils.equals(LAND_TYPE_INSURED, landRetVo.getType())){
				if (StringUtils.equals(SUCESS, landRetVo.getRetCode())){
					taskCntrDataLandingQueueDao.updateIpsnLandFlagByApplNo(landRetVo.getApplNo(), SUCESS_LAND_FLAG);
				}else {
					taskCntrDataLandingQueueDao.updateIpsnLandFlagByApplNo(landRetVo.getApplNo(), FAIL_LAND_FLAG);
				}
			}
			 //应收付落地告知
            if (StringUtils.equals(LAND_TYPE_PLN, landRetVo.getType())){
            	  if (StringUtils.equals(SUCESS, landRetVo.getRetCode())){
            		  taskCntrDataLandingQueueDao.updatePlnLandFlagByApplNo(landRetVo.getApplNo(), SUCESS_LAND_FLAG);
                  }else {
                      taskCntrDataLandingQueueDao.updatePlnLandFlagByApplNo(landRetVo.getApplNo(), FAIL_LAND_FLAG);
                  }
            }
		}
		return retInfo;
	}
}
