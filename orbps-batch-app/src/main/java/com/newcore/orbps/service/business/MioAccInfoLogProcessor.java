package com.newcore.orbps.service.business;

import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbpsutils.dao.api.QueryAccInfoDao;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据账号信息生成账户明细
 * Created by liushuaifeng on 2017/4/19 0019.
 */
public class MioAccInfoLogProcessor implements ItemProcessor<EarnestAccInfo, Map<Long, MioAccInfoLog>> {

	/**
	 *
	 * @param item 账户信息
	 * @return Map<Long, MioAccInfoLog> 账户ID，账户明细机构
	 * @throws Exception
	 */


	@Autowired
	QueryAccInfoDao queryAccInfoDao;


	@Override
	public Map<Long, MioAccInfoLog> process(EarnestAccInfo item) throws Exception {

		//声明账户轨迹表，记录每个账户的轨迹信息
		MioAccInfoLog	mioAccInfoLog =  new  MioAccInfoLog(); 		
		mioAccInfoLog.setAccId(item.getAccId());//账户标识
		mioAccInfoLog.setAccLogId(queryAccInfoDao.selectMioAccInfoLog());//账户明细标识
		mioAccInfoLog.setAnmt(item.getFrozenBalance());//收付金额，此时收付金额等于账户冻结金额
		mioAccInfoLog.setCreateTime(new Date());//发生日期
		mioAccInfoLog.setMioClass(Integer.valueOf(MIO_CLASS.HANDLE.getKey()));//收付类型： 1：收，-1：付 ；此时为-1
		mioAccInfoLog.setMioItemCode(MIO_ITEM_CODE.PS.getKey());//收付项目代码，此时为[保费]
		mioAccInfoLog.setRemark("暂收转保费");//备注

		//声明map集合作为返回值，其中map的key值代表 账户表账户ID，value为该账户发生的轨迹信息
		Map<Long, MioAccInfoLog> map = new HashMap<>();
		map.put(item.getAccId(), mioAccInfoLog);
		return map;
	}
}
