package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.QueryAccInfoDao;
import com.newcore.orbps.models.service.bo.EarnestAccInfo;
import com.newcore.orbps.models.service.bo.QueryEarnestAccInfoBean;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.web.vo.otherfunction.earnestpay.AccInfoVo;
import com.newcore.orbps.service.api.QueryAccInfoByApplNo;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 暂收费支取功能：查询账户信息
 * @author LJF
 * 2017年2月22日 19:25:45
 */
@Service("queryAccInfoByApplNo")
public class QueryAccInfoByApplNoImpl implements QueryAccInfoByApplNo {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	QueryAccInfoDao queryAccInfoDao;

	private static final Integer INT_NUM_ZERO = 0;

	private static final Integer INT_NUM_ONR = 1;

	private static final Integer INT_NUM_FIVE = 5;

	private static final String CHECK_FLAG_ALL = "A";

	private static final String CHECK_FLAG_EMPTY = "E";

	private static final String CHECK_FLAG_FEE_GRP_NO = "F";

	private static final String CHECK_FLAG_GRP_NAME = "G";

	private static final String IPSN_FLAG_I = "I";

	private static final String IPSN_FLAG_O = "O";

	private static final String IPSN_FLAG_P = "P";

	/**
	 * 1,根据投保单号查询保单是否存在
	 * 2，判断单位名称和收费属组list是否同时不为空，
	 * 3，根据单位名称查询组织层次代码
	 * 4，根据被保人信息以及单位名称或收费组list查询被保人序号
	 * 5，这个时候知道投保单号、层次代码list、收费组编号list，被保人序号
	 * 6，根据上面的条件来查询账户信息
	 */
	@SuppressWarnings("unchecked")
	@Override
	public PageData<AccInfoVo> getEarnestAccInfo(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean) {

		//新建bo，作为返回值
		PageData<AccInfoVo> pageData = new PageData<>();
		//声明前台vo,最后封装到PageData<AccInfoVo>中
		List<AccInfoVo> accInfoVoList = new ArrayList<>();
		//中间值，最后转化为List<AccInfoVo>，bo转vo。
		List<EarnestAccInfo> earnestAccInfoList = new ArrayList<>();

		//根据投保单号查询保单信息
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", queryEarnestAccInfoBean.getCondition().getApplNo());
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);

		//如果团单信息不存在返回失败！
		if (null == grpInsurAppl) {
			pageData.setData(accInfoVoList);
			pageData.setTotalCount(0);
			return pageData;
		}

		//对入参校验：判断【收费组】 【组织架构树节点单位名称】赋值；判断被保人五要素赋值情况
		String sbFeeGrpNoList = checkGrpNameAndFeeGrpNoList(queryEarnestAccInfoBean);
		String sb = checkIpsn(queryEarnestAccInfoBean);

		//如果保单为【为清单】类型，但入参存对被保人五要素赋值，直接返回失败。
		if(!StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(), grpInsurAppl.getLstProcType()) && StringUtils.equals(IPSN_FLAG_O, sb)){
			pageData.setData(accInfoVoList);
			pageData.setTotalCount(0);
			return pageData;
		}
		Object [] objs = {earnestAccInfoList,0l};
		//判断【收费组】 【组织架构树节点单位名称】的传值情况
		if(StringUtils.equals(CHECK_FLAG_FEE_GRP_NO, sbFeeGrpNoList)){
			//【收费组】不为空，【组织架构树节点单位名称】为空时，获得对应账户信息。
			objs = getRetInfoEarnestAccInfoByFeeGrpNoList(queryEarnestAccInfoBean , grpInsurAppl.getLstProcType() , sb);
		}else if(StringUtils.equals(CHECK_FLAG_GRP_NAME, sbFeeGrpNoList)){
			//【收费组】为空，【组织架构树节点单位名称】不为空时，获得对应账户信息。
			objs = getRetInfoEarnestAccInfoByGrpName(queryEarnestAccInfoBean , grpInsurAppl.getLstProcType() , grpInsurAppl , sb);
		}else if(StringUtils.equals(CHECK_FLAG_ALL, sbFeeGrpNoList)){
			//【收费组】 【组织架构树节点单位名称】都不为空时，直接返回错误（没有该场景）。
			pageData.setData(accInfoVoList);
			pageData.setTotalCount(0);
			return pageData;
		}else{
			//【收费组】 【组织架构树节点单位名称】都为空时，获得对应账户信息。
			objs = getRetInfoEarnestAccInfoByApplNo(queryEarnestAccInfoBean , grpInsurAppl.getLstProcType() , sb);
		}

		pageData.setData(getAccInfoVoByEarnestAccInfo((List<EarnestAccInfo>)objs[0] , queryEarnestAccInfoBean.getCondition().getApplNo()));
		pageData.setTotalCount(Long.valueOf(objs[1].toString()));
		return pageData;
	}

	/**
	 * 【收费组】不为空，【组织架构树节点单位名称】为空时
	 * @param queryEarnestAccInfoBean 查询账户信息入参bo
	 * @param lstProcType 团体清单标志
	 * @param sb 被保人五要素是否为空 
	 * @return
	 */
	public Object [] getRetInfoEarnestAccInfoByFeeGrpNoList(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean , String lstProcType , String sb){

		//新建bo，作为返回值
		List<EarnestAccInfo> earnestAccInfoList = new ArrayList<>();

		//声明账户号集合
		List<String> accNoList = new ArrayList<>();

		if(StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(), lstProcType) && StringUtils.equals(IPSN_FLAG_O, sb)){//普通清单
			//查询被保人信息，获得被保人序号
			Long ipsnNo = queryAccInfoDao.getIpsnNoList(queryEarnestAccInfoBean);

			if(null == ipsnNo){
				Object [] objs = {earnestAccInfoList,0l};
				return objs;
			}

			// 拼接 applNo+I+ipsoNo
			String accNo = queryEarnestAccInfoBean.getCondition().getApplNo()+IPSN_FLAG_I+String.valueOf(ipsnNo);
			accNoList.add(accNo);
		}else{
			//遍历收费属组号集合
			for (Long feeGrpNo : queryEarnestAccInfoBean.getCondition().getFeeGrpNoList()) {
				// 拼接 applNo+I+ipsoNo
				String accNo = queryEarnestAccInfoBean.getCondition().getApplNo()+IPSN_FLAG_P+String.valueOf(feeGrpNo);
				accNoList.add(accNo);
			}
		}
		return 	getEarnestAccInfoList(accNoList ,queryEarnestAccInfoBean.getCondition().getApplNo(),queryEarnestAccInfoBean.getPageSize() , queryEarnestAccInfoBean.getPageStartNo());
	}

	/**
	 * 【收费组】为空，【组织架构树节点单位名称】不为空时
	 * @param queryEarnestAccInfoBean 查询账户信息入参bo
	 * @param lstProcType 团体清单标志
	 * @param sb 被保人五要素是否为空 
	 * @return
	 */
	public Object [] getRetInfoEarnestAccInfoByGrpName(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean , String lstProcType ,GrpInsurAppl grpInsurAppl , String sb){

		//新建bo，作为返回值
		List<EarnestAccInfo> earnestAccInfoList = new ArrayList<>();

		//声明账户号集合
		List<String> accNoList = new ArrayList<>();

		//根据applNo及组织架构树节点单位名称查询对应的组织层次代码集合
		List<String> levelCodeList = new ArrayList<>();
		List<OrgTree> orgTreeList = grpInsurAppl.getOrgTreeList();
		if(null == orgTreeList){
			Object [] objs = {earnestAccInfoList,0l};
			return objs;
		}
		for (OrgTree orgTree : orgTreeList) {
			if(orgTree.getGrpHolderInfo().getGrpName().contains(queryEarnestAccInfoBean.getCondition().getGrpName())){
				levelCodeList.add(orgTree.getLevelCode());
			}
		}

		if(StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(), lstProcType) && StringUtils.equals(IPSN_FLAG_O, sb)){//普通清单
			//查询被保人信息，获得被保人序号
			Long ipsnNo = queryAccInfoDao.getIpsnNoListBy(queryEarnestAccInfoBean , levelCodeList);

			if(null == ipsnNo){
				Object [] objs = {earnestAccInfoList,0l};
				return objs;
			}

			// 拼接 applNo+I+ipsoNo
			String accNo = queryEarnestAccInfoBean.getCondition().getApplNo()+IPSN_FLAG_I+String.valueOf(ipsnNo);
			accNoList.add(accNo);
		}else{
			//遍历层次代码集合
			for (String levelCode : levelCodeList) {
				// 拼接 applNo+I+ipsoNo
				String accNo = queryEarnestAccInfoBean.getCondition().getApplNo()+IPSN_FLAG_O+levelCode;
				accNoList.add(accNo);
			}
		}
		return  getEarnestAccInfoList(accNoList , queryEarnestAccInfoBean.getCondition().getApplNo(),queryEarnestAccInfoBean.getPageSize() , queryEarnestAccInfoBean.getPageStartNo());
	}

	/**
	 * 【收费组】为空，【组织架构树节点单位名称】为空时
	 * @param queryEarnestAccInfoBean 查询账户信息入参bo
	 * @param lstProcType 团体清单标志
	 * @param sb 被保人五要素是否为空 
	 * @return
	 */
	public Object [] getRetInfoEarnestAccInfoByApplNo(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean  , String lstProcType , String sb){

		//新建bo，作为返回值
		List<EarnestAccInfo> earnestAccInfoList = new ArrayList<>();
		//声明账户号集合
		List<String> accNoList = new ArrayList<>();
		Object [] objs = {earnestAccInfoList,0};
		if(StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(), lstProcType) && StringUtils.equals(IPSN_FLAG_O, sb)){//普通清单
			//查询被保人信息，获得被保人序号
			List<Long> ipsnNoList = queryAccInfoDao.getIpsnNoListByQueryEarnestAccInfoBean(queryEarnestAccInfoBean);

			if(ipsnNoList.isEmpty()){
				return objs;
			}

			//遍历被保人序号集合
			for (Long ipsnNo : ipsnNoList) {
				// 拼接 applNo+I+ipsoNo
				String accNo = queryEarnestAccInfoBean.getCondition().getApplNo()+IPSN_FLAG_I+String.valueOf(ipsnNo);
				accNoList.add(accNo);
			}
			objs = getEarnestAccInfoList(accNoList , queryEarnestAccInfoBean.getCondition().getApplNo(),queryEarnestAccInfoBean.getPageSize() , queryEarnestAccInfoBean.getPageStartNo());
		}else{
			List<com.newcore.orbps.models.finance.EarnestAccInfo> earnestAccInfoOldList = queryAccInfoDao.queryEarnestAccInfoByApplNo
					(queryEarnestAccInfoBean.getCondition().getApplNo(),queryEarnestAccInfoBean.getPageSize() , queryEarnestAccInfoBean.getPageStartNo());
			if(!earnestAccInfoOldList.isEmpty()){
				BigDecimal sumBalance = queryAccInfoDao.querySumBalanceEarnestAccInfoByApplNo(queryEarnestAccInfoBean.getCondition().getApplNo());
				objs[1] = queryAccInfoDao.queryNumAccInfoByApplNo(queryEarnestAccInfoBean.getCondition().getApplNo());
				for (com.newcore.orbps.models.finance.EarnestAccInfo earnestAccInfoOld : earnestAccInfoOldList) {
					earnestAccInfoList.add(getNewEarnestAccInfo(earnestAccInfoOld, sumBalance));
				}
			}
		}
		return objs;
	}

	/**
	 * 校验【收费组】，【组织架构树节点单位名称】
	 * @param queryEarnestAccInfoBean 查询账户信息入参bo
	 * @return
	 */
	public String checkGrpNameAndFeeGrpNoList(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean){

		String strFlag = "";
		if(StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getGrpName()) && 
				(null == queryEarnestAccInfoBean.getCondition().getFeeGrpNoList() || queryEarnestAccInfoBean.getCondition().getFeeGrpNoList().isEmpty())){
			//都为空时
			strFlag = CHECK_FLAG_EMPTY ;
		}else if( !StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getGrpName()) && 
				(null == queryEarnestAccInfoBean.getCondition().getFeeGrpNoList() || queryEarnestAccInfoBean.getCondition().getFeeGrpNoList().isEmpty())){
			//组织架构树节点单位名称不为空
			strFlag = CHECK_FLAG_GRP_NAME ;
		}else if(StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getGrpName()) && 
				(!queryEarnestAccInfoBean.getCondition().getFeeGrpNoList().isEmpty())){
			//收费组 不为空
			strFlag = CHECK_FLAG_FEE_GRP_NO ;
		}else{
			//都有值
			strFlag = CHECK_FLAG_ALL ;
		}
		return strFlag;
	}

	/**
	 * 校验被保人五要素
	 * @param queryEarnestAccInfoBean 查询账户信息入参bo
	 * @return
	 */
	public String checkIpsn(PageQuery<QueryEarnestAccInfoBean> queryEarnestAccInfoBean){

		int sb = INT_NUM_ZERO;
		if(StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdNo())){//被保人五要素：被保人证件号码为空
			sb+=INT_NUM_ONR;
		}
		if(StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnIdType())){//被保人五要素：被保人证件类别为空
			sb+=INT_NUM_ONR;
		}
		if(StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnName())){//被保人五要素：被保人为空
			sb+=INT_NUM_ONR;
		}
		if(StringUtils.isBlank(queryEarnestAccInfoBean.getCondition().getIpsnSex())){//被保人五要素：被保险人性别为空
			sb+=INT_NUM_ONR;
		}
		if(null == queryEarnestAccInfoBean.getCondition().getIpsnBirthDate()){//被保人五要素：被保人出生日期为空
			sb+=INT_NUM_ONR;
		}
		if(sb == INT_NUM_FIVE){//都为空
			return CHECK_FLAG_EMPTY;
		}else{//被保人五要素至少有一个属性赋值
			return IPSN_FLAG_O;
		}
	}

	/**
	 * 转换bo，主要是将SumAnmt这个字段放到earnestAccInfoOld中，生成新的bo返回。
	 * @param earnestAccInfoOld 查询账户信息入参bo
	 * @param applNo 投保单号
	 * @return
	 */
	public EarnestAccInfo  getNewEarnestAccInfo(com.newcore.orbps.models.finance.EarnestAccInfo earnestAccInfoOld , BigDecimal 	sumBalance){
		EarnestAccInfo earnestAccInfo = new EarnestAccInfo();
		earnestAccInfo.setAccId(earnestAccInfoOld.getAccId());
		earnestAccInfo.setAccNo(earnestAccInfoOld.getAccNo());
		earnestAccInfo.setAccPersonNo(earnestAccInfoOld.getAccPersonNo());
		earnestAccInfo.setAccType(earnestAccInfoOld.getAccType());
		earnestAccInfo.setBalance(earnestAccInfoOld.getBalance());
		earnestAccInfo.setFrozenBalance(earnestAccInfoOld.getFrozenBalance());
		earnestAccInfo.setSumAnmt(sumBalance);
		return earnestAccInfo;
	}


	public Object [] getEarnestAccInfoList(List<String> accNoList , String applNo , Integer pageSize , long pageStartNo){
		//新建bo，作为返回值
		List<EarnestAccInfo> earnestAccInfoList = new ArrayList<>();
		BigDecimal num = queryAccInfoDao.queryNumAccInfoByApplNo(applNo);
		if(!accNoList.isEmpty()){
			List<com.newcore.orbps.models.finance.EarnestAccInfo> earnestAccInfoOldList = 
					queryAccInfoDao.queryEarnestAccInfo(accNoList ,pageSize , pageStartNo);
			BigDecimal 	sumBalance = queryAccInfoDao.querySumBalanceEarnestAccInfoByApplNo(applNo);
			if(!earnestAccInfoOldList.isEmpty()){
				for (com.newcore.orbps.models.finance.EarnestAccInfo earnestAccInfoOld : earnestAccInfoOldList) {
					earnestAccInfoList.add(getNewEarnestAccInfo(earnestAccInfoOld, sumBalance));
				}
			}
		}
		Object [] objs = {earnestAccInfoList,num};
		return objs;
	}

	/**
	 * bo转vo
	 * @param earnestAccInfoOld 查询账户信息入参bo
	 * @return
	 */
	public List<AccInfoVo> getAccInfoVoByEarnestAccInfo(List<EarnestAccInfo> earnestAccInfoList,String applNo){

		List<AccInfoVo> accInfoVoList = new ArrayList<>();

		if( null == earnestAccInfoList || earnestAccInfoList.isEmpty() ){
			return accInfoVoList;
		}else{
			BigDecimal 	sumBalance = queryAccInfoDao.querySumBalanceEarnestAccInfoByApplNo(applNo);
			for (EarnestAccInfo earnestAccInfo : earnestAccInfoList) {
				AccInfoVo accInfoVo = new AccInfoVo();
				accInfoVo.setAccNo(earnestAccInfo.getAccNo());
				accInfoVo.setApplNo(earnestAccInfo.getAccNo().substring(0, 16));
				accInfoVo.setAccType(earnestAccInfo.getAccType());
				accInfoVo.setAccPersonNo(earnestAccInfo.getAccPersonNo());
				accInfoVo.setBalance(earnestAccInfo.getBalance().doubleValue());
				accInfoVo.setPayAmount(earnestAccInfo.getBalance().doubleValue());
				accInfoVo.setSumBalance(sumBalance.doubleValue());
				accInfoVoList.add(accInfoVo);
			}
		}
		return accInfoVoList;
	}

}
