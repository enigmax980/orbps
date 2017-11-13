package com.newcore.orbps.service.oldpms.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.newcore.orbps.models.oldpms.OldPrintCallBackRequst;
import com.newcore.orbps.models.service.bo.RetInfo;
import com.newcore.orbps.service.oldpms.api.OldPrintCallBackService;
import com.newcore.orbpsutils.dao.api.OldPrintTaskQueueDao;
import com.newcore.orbpsutils.validation.ValidationUtils;

/**
 * 保单辅助打印数据落地反馈服务实现类
 * @author JCC
 * 2017年5月17日 10:03:30
 */
@Service("oldPrintCallBackService")
public class OldPrintCallBackServiceImpl implements OldPrintCallBackService{
	
	/**
	 * 日志管理工具实例.
	 */
	private Logger logger = LoggerFactory.getLogger(OldPrintCallBackServiceImpl.class);

	@Autowired
	OldPrintTaskQueueDao oldPrintTaskQueueDao;
	
	/**
	 * 告知方法
	 */
	@Override
	public RetInfo callBack(OldPrintCallBackRequst oldPrint){ 
		ValidationUtils.validate(oldPrint);		//非空字段校验
		logger.info("开始执行-保单辅助打印数据落地反馈服务实现类！");
		String applNo = oldPrint.getApplNo();
		//打印任务状态改成：D - 需要调用老打印
		int rowid = oldPrintTaskQueueDao.updateDateByApplNo(applNo,"D");
		String retCode ="0";
		String errMsg="通知调用短险打印服务失败！";
		if(rowid ==1){
			retCode ="1";
			errMsg="通知调用短险打印服务成功！";
		}
		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(applNo);
		retInfo.setCgNo(oldPrint.getCgNo());
		retInfo.setRetCode(retCode);
		retInfo.setErrMsg(errMsg);
		return retInfo;
	}
}
