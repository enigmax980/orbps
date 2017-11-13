package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import com.newcore.orbps.dao.api.InsuredErrModifyService;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * 被保险人错误信息修改服务
 *
 * @author lijifei
 * 创建时间：2016年8月25日16:28:11
 */
@Service("insuredErrModifyService")
public class InsuredErrModifyServiceImpl implements InsuredErrModifyService {

	@Autowired
	MongoBaseDao mongoBaseDao;

	/**
	 * JDBC操作工具
	 */
	@Autowired
	JdbcOperations jdbcTemplate;



	/*功能说明：被保险人错误信息修改服务。具体步骤如下：
	 *	1)	对被保险人错误信息查询服务（InsuredErrSelService）查询出的数据进行修改。
	 *	2)	将修改的信息保存到当前批次mongodb数据库中。
	 *	3)	将ERR_LIST_INFO表中的修正标志字段置为1,暂存。
	 *参数：
	 *	 GrpInsured 被保人信息
	 *返回值：1为成功，0为失败。用于页面提醒用户
	 */
	@Override
	public int doInsuredErrModifyService(GrpInsured grpInsured){
		//获取-被保人信息-组合保单号，批次号，被保险人编号
		String cgNo=grpInsured.getCgNo();
		String batchNo =grpInsured.getBatNo();
		long ipsnNo=grpInsured.getIpsnNo();
		//判断组合保单号，批次号，被保险人编号是否为空
		if(StringUtils.isBlank(cgNo)||StringUtils.isBlank(batchNo)||StringUtils.isBlank(String.valueOf(ipsnNo))){
			//数据为空。向页面返回0，提示用户操作失败。
			return 0;
		}else{
			//创建mongodb查询条件
			Map<String, Object> map=new HashMap<>();
			map.put("cgNo",cgNo);
			map.put("batchNo",batchNo);
			map.put("ipsnNo",ipsnNo);
			//将修改的信息保存到当前批次mongodb数据库中.
			int removeCount=mongoBaseDao.remove(GrpInsured.class, map);
			//判断是否成功
			if(removeCount==1){
				mongoBaseDao.insert(GrpInsured.class);
				String	updatBatch = "update ERR_LIST_INFO SET MODIFY_FLAG = ?  WHERE CG_NO = ? ADN BATCH_NO=? ADN IPSN_NO = ?";
				int	updateCount;
					updateCount	=jdbcTemplate.update(updatBatch, "1" ,cgNo,batchNo,ipsnNo);
				if(removeCount==1&&updateCount==1){
					//向页面返回1，提示用户操作成功。
					return 1;
				}else{
					//删除失败。向页面返回0，提示用户操作失败。
					return 0;
				}
			}else{
				//删除失败。向页面返回0，提示用户操作失败。
				return 0;
			}
		}
	}

}
