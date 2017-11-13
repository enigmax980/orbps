package com.newcore.orbpsutils.dao.api;

import java.util.List;
import java.util.Map;

import com.newcore.orbps.models.banktrans.BuildBanTransBean;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.finance.PlnmioRecGroupBean;
import com.newcore.orbps.models.finance.QueryPauseTransInfoBean;

/**
 * 操作应收付表
 * @author JCC
 * 2017年2月14日 15:21:16
 */
public interface PlnmioRecDao{	
	
	/**
	 * 查询应收付数据
	 * @param 
	 * 	plnmioRecId 主键
	 * @return
	 * 	PlnmioRec
	 */
	PlnmioRec queryPlnmioRec(Long plnmioRecId);
	
	/**
	 * 查询应收付数据集合
	 * @param 
	 * 	plnmioRecIds 多个主键 id1,id2...
	 * @return
	 * 	PlnmioRec集合
	 */
	List<PlnmioRec> queryPlnmioRecList(String plnmioRecIds);
	
	/**
	 * 查询应收付数据集合
	 * @param map 查询条件集合
	 *  key = 表字段
	 *  value = 字段内容
	 * @return 
	 * 	List<PlnmioRec>
	 */
	List<PlnmioRec> queryPlnmioRecList(Map<String,String> param);
	
	/**
	 * 新增应收付数据
	 * @param 
	 * 	plnMioList 数据集合
	 * @return
	 *  成功 true,失败 false
	 */
	public boolean insertPlnmioRec(List<PlnmioRec> plnMioList);
	
	/**
	 * 修改应收数据锁标识
	 * @param 
	 * 	plnmioRecIds 多个主键 id1,id2...
	 * @param 
	 * 	paramMap 需要修改的字段
	 * 	 key  = 字段名，
	 *   value= 修改的值
	 * @return	
	 */
	boolean updatePlnmioRecByIds(String plnmioRecIds,Map<String,String> paramMap);

	/**
	 * 修改投保单对应的应收数据
	 * @param applNo 投保单号
	 * @param pracStat 应收状态
	 * @return
	 */
	boolean updatePlnmioRecByCntrNo(String applNo,String pracStat);
	
	/**
	 * 修改投保单为生效状态
	 * @param applNo 投保单号
	 * @return
	 */
	boolean updatePlnmioRecInforce(String applNo) ;
	
	/**
	 * 修改应收数据的应收状态
	 * @param 
	 * 	plnmioRecId 应收主键
	 * @param 
	 * 	pracStat 应收状态
	 * @return
	 */
	boolean updatePlnmioRecProcStatById(long plnmioRecId, String pracStat);

	/**
	 * 根据请求报表获取分组后的应收数据集合
	 * @param 
	 * 	bean 银行转账请求报文
	 * @return
	 *  PlnmioRecGroupBean集合
	 */
	List<PlnmioRecGroupBean> getPlnmioRecList(BuildBanTransBean bean);
	
	/**
	 * 获取plnmio_Rec_Id自增长序列最新值加1
	 * @return
	 */
	public long getPlnmioRecId();
	
	/**
	 * 获取plnmio_Rec_Id自增长序列最新值加1
	 * @return
	 */
	public long getPlnmioRecBatNId();
	
	/**
	 * 根据投保单号查询是否有在途应收付记录
	 * @param applNo 投保单号
	 * @return true 有转账在途数据   false 无转账在途数据
	 */
	public boolean getLockFlagByApplNo(String applNo);
	
	
	/**
	 * 获取单笔非银行转账的总保费
	 * @return Double
	 */
	Double getPlnmioRecAmnt(String applNo);
	
	/**
	 * 根据暂停送划请求报文查询应收付表中满足条件的数据
	 * 每次查询150条
	 * @param bean 暂停送划查询请求报文解析成的实体类
	 * @return 查询到的应收付数据
	 */
	List<PlnmioRec> queryPlnmioInfoWithPageData(QueryPauseTransInfoBean bean);

	/**
	 * 获取单笔银行转账的应收金额
	 * @param applNo 投保单号
	 * @return
	 */
	double getPlnmioRecAmntBankTrans(String applNo);

	/**
	 * 根据账户ID查询应收数据
	 * @param cntrNo 投保单号
	 * @param type  账号所属人类别
	 * @param key  帐号值
	 * @return
	 * 查询到的条数
	 */
	int getPlnmioRecSize(String cntrNo, String type, String key);

	/**
	 * 删除对应的应收数据
	 * @param 
	 * 	cntrNo 投保单号
	 * @param 
	 * 	accType  账号所属人类别
	 * @param 
	 * 	key 帐号值
	 * @return
	 */
	boolean deletePlnmioRec(String cntrNo, String accType, String key);

	/**
	 * 删除应收数据
	 * @param applNo 投保单号
	 * @return
	 */
	boolean deletePlnmioRecByCntrNo(String applNo);
	
}
