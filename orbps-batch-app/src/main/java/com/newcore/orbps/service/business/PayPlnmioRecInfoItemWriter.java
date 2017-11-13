package com.newcore.orbps.service.business;

import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcPayAllEarnestInfoDao;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.dao.api.QueryAccInfoDao;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liushuaifeng on 2017/2/27 0027.
 */
public class PayPlnmioRecInfoItemWriter implements ItemWriter<Map<EarnestAccInfo, List<PlnmioRec>>>{


	@Autowired
	PlnmioRecDao plnmioRecDao;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	QueryAccInfoDao queryAccInfoDao;


	private final static String STATE_E = "E";

	private final static String STATE_C = "C";

	private final static String TIME_FLAG_END = "E";

	@Override
	public void write(List<? extends Map<EarnestAccInfo, List<PlnmioRec>>> items) throws Exception {

		for (Map<EarnestAccInfo, List<PlnmioRec>> itemss : items) {

			int count = 0;
			String applNo = "";
			for (EarnestAccInfo earnestAccInfo : itemss.keySet()) {

				//1.插入应付记录
				plnmioRecDao.insertPlnmioRec(itemss.get(earnestAccInfo));
				mongoBaseDao.insertAll(itemss.get(earnestAccInfo));

				// 2.根据 AccNo 修改 EARNEST_ACC_INFO,对账户表中的余额进行扣取;
				queryAccInfoDao.updateEarnestAccInfo(earnestAccInfo.getAccNo(), 0D);
				// 3.在MIO_ACC_INFO_LOG表里面添加一条轨迹数据
				MioAccInfoLog	mioAccInfoLog =  new  MioAccInfoLog(); 		
				mioAccInfoLog.setAccId(earnestAccInfo.getAccId());
				mioAccInfoLog.setAccLogId(queryAccInfoDao.selectMioAccInfoLog());
				mioAccInfoLog.setAnmt(earnestAccInfo.getBalance());
				mioAccInfoLog.setCreateTime(new Date());
				mioAccInfoLog.setMioClass(Integer.valueOf(MIO_CLASS.HANDLE.getKey()));
				mioAccInfoLog.setMioItemCode(MIO_ITEM_CODE.FA.getKey());
				mioAccInfoLog.setRemark("暂缴费支取应付数据");
				queryAccInfoDao.insertEarnestAccInfo(mioAccInfoLog);
				applNo = earnestAccInfo.getAccNo().substring(0, 16);
				count++;
			}//end for 

		}//end for 
	}
}
