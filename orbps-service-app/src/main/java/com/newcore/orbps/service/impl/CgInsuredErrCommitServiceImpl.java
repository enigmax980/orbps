package com.newcore.orbps.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import com.newcore.orbps.dao.api.CgInsuredErrCommitService;

/**
 * 被保险人错误信息提交服务
 *
 * @author lijifei
 * 创建时间：2016年8月25日16:24:11
 */
@Service("cgInsuredErrCommitService")
public class CgInsuredErrCommitServiceImpl implements CgInsuredErrCommitService {

	@Autowired
	JdbcOperations jdbcTemplate;



	/*
	 * 功能说明：被保险人错误信息提交服务。具体逻辑如下：
	 *	1)	当一个批次错误清单信息修改完成后，准备发送给保单辅助系统时，将ERR_LIST_INFO表修正标志字段由1置为2,已发送。
	 *	2)	对CNTR_EFFECT_CTRL表中批次号进行加1操作，并更新状态字段为8
	 * 参数：
	 *	  cgNo        组合保单号
	 *	  modifyFlag  状态标志
	 * 返回值：1为成功，0为失败。用于页面提醒用户
	 */
	@Override
	public int doCgInsuredErrCommitService(String cgNo,String modifyFlag){
		//当一个批次错误清单信息修改完成后，准备发送给保单辅助系统时，将ERR_LIST_INFO表修正标志字段由1置为2,已发送
		//修改ERR_LIST_INFO-sql语句
		String	updatesql="update ERR_LIST_INFO SET MODIFY_FLAG = ?  WHERE CG_NO = ? and MODIFY_FLAG = ?";
		//返回修改影响的行数

		int	updatecount=jdbcTemplate.update(updatesql, "2" ,cgNo,modifyFlag);

		//查询CNTR_EFFECT_CTRL当前批次号-sql语句
		String	SEARCH_CG_NO_SQL = "select BATCH_NO from CNTR_EFFECT_CTRL where CG_NO = ?";
		//获得当前批次号

		String	batchNoSrt = jdbcTemplate.queryForObject(SEARCH_CG_NO_SQL, String.class, cgNo);

		//修改CNTR_EFFECT_CTRL-sql语句
		String	updateCntr="update CNTR_EFFECT_CTRL SET BATCH_NO = ? , PROC_STAT = ? ,PROC_CAUSE_DESC = ? WHERE CG_NO = ?";
		//返回修改影响的行数

		int updateCntrCount=jdbcTemplate.update(updateCntr, Long.valueOf(batchNoSrt)+1 ,"8","保单基本信息发送成功，等待下一步处理！",cgNo);

		//判断两个表是否都进行修改操作	
		if(updatecount==1&&updateCntrCount==1){
			//此时，两表都进行修改操作。返回页面值，用于页面判断操作成功有否。1为成功，0为失败。
			return 1;
		}else{
			return 0;
		}
	}

}
