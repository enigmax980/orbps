package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import com.newcore.orbps.dao.api.InsuredErrSelService;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 被保险人错误信息提交服务
 *
 * @author lijifei
 * 创建时间：2016年8月25日20:24:11
 */
@Service("insuredErrSelService")
public class InsuredErrSelServiceImpl implements InsuredErrSelService {


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
	private static Logger logger = LoggerFactory.getLogger(InsuredErrSelServiceImpl.class);


	/*功能说明：被保险人错误信息查询服务。具体流程如下：
	 *	1)	根据组合保单号、被保人序号、修正标志为0或1三个条件，查询批次号。
	 *	2)	根据组合保单号、被保险人序号、批次号+1查询mongodb库。
	 *		i.	如果数据不存在，根据mongodb库中组合保单号、被保险人序号、批次号的数据生成一条新的数据，生成的新数据批次号需要进行+1操作。
	 *	3)	展示组合保单号、被保险人序号、批次号+1的mongodb数据。
	 *参数：
	 * 	   组合保单号 cgNo
	 * 	  被保人序号 ipsnNo
	 * 返回值：GrpInsured 被保人信息
	 */
	@Override
	public GrpInsured doInsuredErrSelService(String cgNo, int ipsnNo){
		//根据组合保单号、被保人序号、修正标志为0或1三个条件，查询ERR_LIST_INFO批次号
		String ERR_LIST_INFO_SQL = "select BATCH_NO from ERR_LIST_INFO where CG_NO = ? and IPSN_NO = ? and ( MODIFY_FLAG = ? or MODIFY_FLAG = ? ) ";
		int batchNo;
		batchNo = jdbcTemplate.queryForObject(ERR_LIST_INFO_SQL, int.class, cgNo,ipsnNo,"0","1");
		//创建sql查询条件
		Map<String, Object> mapM=new HashMap<>();
		mapM.put("cgNo",cgNo);
		mapM.put("batchNo",batchNo);
		mapM.put("ipsnNo",ipsnNo);
		//根据条件查询mongodb-是否有该条数据。此处需 抛异常（如果查不到）
		GrpInsured grpInsuredBase=(GrpInsured) mongoBaseDao.findOne(GrpInsured.class, mapM);
		//如果有该条数据，证明数据没问题。
		if(grpInsuredBase!=null){
			//根据组合保单号、被保险人序号、批次号+1查询mongodb库。
			Map<String, Object> map=new HashMap<>();
			map.put("cgNo",cgNo);
			map.put("batchNo",batchNo+1);
			int ipsnNoTwo=ipsnNo+1;
			map.put("ipsnNo",ipsnNoTwo);
			//根据条件查询mongodb-是否有该条数据。此处需 抛异常（如果查不到）
			GrpInsured grpInsured=(GrpInsured) mongoBaseDao.findOne(GrpInsured.class, map);
			//如果数据不存在，根据mongodb库中组合保单号、被保险人序号、批次号的数据生成一条新的数据，生成的新数据批次号需要进行+1操作。展示组合保单号、被保险人序号、批次号+1的mongodb数据。
			if(grpInsured==null){
				grpInsuredBase.setBatNo(String.valueOf(batchNo+1));
				mongoBaseDao.insert(grpInsuredBase);
				return (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, map);
			}else{
				return grpInsured;
			}
		}else{//如果没有该条数据，证明数据有问题--mongodb不存在对应的数据。
			logger.error("没有该清单！");
			return null;
		}
	}

}
