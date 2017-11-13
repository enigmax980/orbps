package com.newcore.orbps.service.business;

import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbpsutils.dao.api.QueryAccInfoDao;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Map;

/**
 * 写入账户明细
 * Created by liushuaifeng on 2017/4/19 0019.
 */
public class MioAccInfoLogWriter implements ItemWriter<Map<Long, MioAccInfoLog>> {
	/**
	 * Map<Long, MioAccInfoLog>>  <账户accId, 账户明细>
	 * 1.根据accId做该条账户冻结金额清零
	 * 2.插入账户明细
	 * @param items
	 * @throws Exception
	 */

	@Autowired
	QueryAccInfoDao queryAccInfoDao;


	@Override
	public void write(List<? extends Map<Long, MioAccInfoLog>> items) throws Exception {

		//遍历list集合信息
		for (Map<Long, MioAccInfoLog> item : items) {
			//遍历map集合key值
			for (Long accId : item.keySet()) {

				//1.根据accId做该条账户冻结金额清零
				queryAccInfoDao.updateEarnestAccInfo(accId);
				//2.插入账户明细
				queryAccInfoDao.insertEarnestAccInfo(item.get(accId));
			}
		}


	}
}
