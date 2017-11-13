package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.QueryAccInfoDao;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.MioAccInfoLog;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.service.bo.EarnestPayInfo;
import com.newcore.orbps.models.service.bo.EarnestPayList;
import com.newcore.orbps.models.service.bo.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.ProcEarnestPayPlnmioRecInfo;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MIO_TYPE;
import com.newcore.supports.dicts.MULTI_PAY_ACC_TYPE;







/**
 * 暂收费支取功能
 * @author LJF
 * 2016年17月02日 09:51:26
 */
@Service("procEarnestPayPlnmioRecInfo")
public class ProcEarnestPayPlnmioRecInfoImpl implements ProcEarnestPayPlnmioRecInfo {




	@Autowired
	PlnmioRecDao plnmioRecDao;

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	QueryAccInfoDao queryAccInfoDaoService;


	public static final String STRING_FLAG_ZERO = "0";

	public static final String STRING_FLAG_ONE = "1";

	public static final String ACC_NO_TYPE_I = "I";

	public static final String ACC_NO_TYPE_O = "O";

	public static final String ACC_NO_TYPE_P = "P";

	public static final String  PROC_STAT_N  = "N"; 

	public static final String  TRANS_STAT_U  = "U";

	public static final String  TASK_STATUS_SUCCESS  = "C";

	public static final String  PAY_ALL_FLAG  = "Y";


	/**
	 * 功能说明：根据账户信息以及支取金额等产生应付数据，产生应付数据各字段赋值
	 * @param earnestPayList 支取账户信息明细
	 */

	@Transaction
	@Override
	public RetInfo drawEarnestAccInfo(EarnestPayList earnestPayList) {

		RetInfo retInfo = new RetInfo();
		retInfo.setRetCode(STRING_FLAG_ONE);
		retInfo.setErrMsg("成功.");

		Map<String,Object> map = new HashMap<>();
		map.put("applNo", earnestPayList.getApplNo());
		GrpInsurAppl  grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if (null == grpInsurAppl) {
			retInfo.setRetCode(STRING_FLAG_ZERO);
			retInfo.setErrMsg("根据投保单号查询保单基本信息失败.");
			return retInfo;
		}
		//判断是否全部支取，如果是调用全部支取方法并返回结果
		if(StringUtils.equals(PAY_ALL_FLAG, earnestPayList.getPayAllFlag())){//
			return procEarnestPay(earnestPayList.getApplNo());
		}

		for (EarnestPayInfo earnestPayInfo : earnestPayList.getEarnestPayInfoList()) {

			EarnestAccInfo  earnestAccInfo =queryAccInfoDaoService.queryOneEarnestAccInfoByApplNo(earnestPayInfo.getAccNo());

			if(null == earnestAccInfo){
				throw new BusinessException(new Throwable("根据账户号查询账户信息失败."));
			}

			BigDecimal data1 = earnestAccInfo.getBalance();
			BigDecimal data2 = BigDecimal.valueOf(earnestPayInfo.getPlnAmnt());
			//判断支取金额是否大于余额如果是直接返回失败，余额为零直接返回失败
			int result1 = data1.compareTo(BigDecimal.ZERO);
			int result2 = data1.compareTo(data2);
			if(0 == result1 || -1 == result1 || -1 == result2){
				retInfo.setRetCode(STRING_FLAG_ZERO);
				retInfo.setErrMsg("余额为零或支取金额大于余额，不支持支取.");
				return retInfo;
			}
			earnestPayInfo.setBalance(earnestAccInfo.getBalance().doubleValue());
			//1.产生应付数据插入到PLNMIO_REC表中;
			List<PlnmioRec> plnMioList = confirmPremSource(grpInsurAppl , earnestPayInfo);

			plnmioRecDao.insertPlnmioRec(plnMioList);

			// 2.根据 AccNo 修改 EARNEST_ACC_INFO,对账户表中的余额进行扣取;

			Double amnt;
			if(null == earnestPayInfo.getPlnAmnt()){
				amnt = earnestAccInfo.getBalance().doubleValue();
			}else{
				amnt = earnestPayInfo.getPlnAmnt();
			}
			BigDecimal balance=earnestAccInfo.getBalance();
			BigDecimal payInfo=BigDecimal.valueOf(amnt);
			queryAccInfoDaoService.updateEarnestAccInfo(earnestPayInfo.getAccNo(), balance.subtract(payInfo).doubleValue());
			// 3.在MIO_ACC_INFO_LOG表里面添加一条轨迹数据
			MioAccInfoLog	mioAccInfoLog =  new  MioAccInfoLog(); 		
			mioAccInfoLog.setAccId(earnestAccInfo.getAccId());
			mioAccInfoLog.setAccLogId(queryAccInfoDaoService.selectMioAccInfoLog());
			mioAccInfoLog.setAnmt(BigDecimal.valueOf(amnt));
			mioAccInfoLog.setCreateTime(new Date());
			mioAccInfoLog.setMioClass(Integer.valueOf(MIO_CLASS.HANDLE.getKey()));
			mioAccInfoLog.setMioItemCode(MIO_ITEM_CODE.FA.getKey());
			mioAccInfoLog.setRemark("暂缴费支取应付数据");
			queryAccInfoDaoService.insertEarnestAccInfo(mioAccInfoLog);
		}//end for 
		return retInfo;
	}

	/**
	 * 根据账号信息判断缴费形式。
	 * @param grpInsurAppl 保单数据
	 * @param earnestPayInfo 暂缴费支取信息
	 * @return
	 */
	private  List<PlnmioRec> confirmPremSource(GrpInsurAppl  grpInsurAppl , EarnestPayInfo earnestPayInfo){
		List<PlnmioRec> plnMioList = new ArrayList<>();

		if(StringUtils.equals(ACC_NO_TYPE_I, earnestPayInfo.getAccType())){//有缴费账号
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("applNo", earnestPayInfo.getApplNo());
			queryMap.put("ipsnNo", Long.valueOf(earnestPayInfo.getAccPersonNo()));
			GrpInsured grpInsured = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, queryMap);
			if (null == grpInsured) {
				throw new BusinessException(new Throwable("根据投保单号查询被保人基本信息失败."));
			}
			plnMioList = setNewPlnMioRecOfIpson(grpInsurAppl, grpInsured, earnestPayInfo);
		}else if(StringUtils.equals(ACC_NO_TYPE_O, earnestPayInfo.getAccType())){//无缴费账号，按单位缴费
			plnMioList = setNewPlnMioRecOfOrgTree(grpInsurAppl, earnestPayInfo);
		}else if(StringUtils.equals(ACC_NO_TYPE_P, earnestPayInfo.getAccType())){//无缴费账号，按收费组缴费
			plnMioList = setNewPlnMioRecOfIpsnPayGrp(grpInsurAppl, earnestPayInfo);
		}else{
			throw new BusinessException(new Throwable("页面[账户所属人类别]赋值错误."));
		}
		if(plnMioList.isEmpty()){
			throw new BusinessException(new Throwable("产生应付数据失败。投保单号["+earnestPayInfo.getApplNo()+"]"+"账户号["+earnestPayInfo.getAccNo()+"]"));
		}
		return plnMioList;
	}



	/**
	 * 重新给plnmioRec赋值:有缴费账号时
	 * @param grpInsurAppl 保单数据
	 * @param grpInsured   被保人信息
	 * @param earnestPayInfo 暂缴费支取信息
	 * @return
	 */
	private List<PlnmioRec> setNewPlnMioRecOfIpson(GrpInsurAppl  grpInsurAppl ,GrpInsured grpInsured , EarnestPayInfo earnestPayInfo) {
		List<PlnmioRec> plnMioList = new ArrayList<>();


		BigDecimal plnAmnt;

		if(null == earnestPayInfo.getPlnAmnt()){
			plnAmnt =BigDecimal.valueOf(earnestPayInfo.getBalance());
		}else{
			plnAmnt =BigDecimal.valueOf(earnestPayInfo.getPlnAmnt());
		}

		//新建集合用于下文排序
		List<AccInfo> listAccInfo = new ArrayList<>();
		listAccInfo.addAll(grpInsured.getAccInfoList());

		//从小到大排序
		Collections.sort(listAccInfo, new Comparator<AccInfo>(){

			/*
			 * int compare(AccInfo arg0, AccInfo arg1) 返回一个基本类型的整型，
			 * 返回负数表示：arg0 小于arg1，
			 * 返回正数表示：arg0大于arg1。
			 */
			@Override
			public int compare(AccInfo arg0, AccInfo arg1) {
				//按照执行序列排序
				if(arg0.getSeqNo() > arg1.getSeqNo()){
					return 1;
				}else{
					return -1;
				}

			}
		}); 

		//按照排序后的优先级进行支取操作
		for (AccInfo accInfo : listAccInfo) {

			PlnmioRec newPln = getIniPlnmioRec(grpInsurAppl);
			newPln.setLevelCode("");
			newPln.setFeeGrpNo(0L);
			newPln.setBankCode(accInfo.getBankCode());		//银行代码
			newPln.setBankAccNo(accInfo.getBankAccNo());	//银行账号	
			newPln.setBankAccName(accInfo.getBankAccName());//帐户所有人名称
			newPln.setIpsnNo(grpInsured.getIpsnNo());		 //被保人序号
			newPln.setMioCustNo(grpInsured.getIpsnCustNo());	//领款人/交款人客户号
			newPln.setMioCustName(grpInsured.getIpsnName());	//领款人/交款人姓名
			newPln.setMultiPayAccType(MULTI_PAY_ACC_TYPE.IPSN_ACC.getKey());	//账号所属人类别      

			//查询该银行账号对应的已支取的金额
			BigDecimal sunAmntPlnmioRec =queryAccInfoDaoService.getPlnmioRec(grpInsurAppl.getApplNo(), accInfo.getBankAccNo(), accInfo.getBankCode(),String.valueOf(grpInsured.getIpsnNo()), "I");
			if (null == sunAmntPlnmioRec) {
				sunAmntPlnmioRec = BigDecimal.ZERO;
			}
			//查询该银行账号对应所有实收金额
			BigDecimal sunMioLogAmnt =  queryAccInfoDaoService.getMioLog(grpInsurAppl.getApplNo(), accInfo.getBankAccNo(), accInfo.getBankCode(),String.valueOf(grpInsured.getIpsnNo()), "I");
			if (null == sunMioLogAmnt) {
				throw new BusinessException(new Throwable("根据投保单号查询实收数据基本信息失败."));
			}

			//比较该银行账号下对应的已支取金额与总实收金额
			int result = sunAmntPlnmioRec.compareTo(sunMioLogAmnt);
			if(0 == result){//如果已支取金额 等于 总实收金额，那么说明该银行账号里的钱已支取完，需要支取下一个银行账号的金额。
				continue;
			}else if(-1 == result){//如果已支取金额 小于 总实收金额，那么说明该银行账号里的钱还没有支取完，可以支取该账户。
				//获取该账号下剩余可支取金额
				BigDecimal balancePlnmioRec = sunMioLogAmnt.subtract(sunAmntPlnmioRec);
				int re =plnAmnt.compareTo(balancePlnmioRec);
				if(re == 1){//支付金额大于 该账户号下金额，扣除该银行账号下所有金额。继续循环，扣除下一个账户。
					newPln.setAmnt(balancePlnmioRec);
					plnMioList.add(newPln);
					//计算下一个账户需要扣的钱。
					plnAmnt = plnAmnt.subtract(balancePlnmioRec);
				}else{//支付金额小于 该账户号下金额，扣除该银行账号下的 支付金额，同时跳出循环，不再扣除其他账户的钱。
					newPln.setAmnt(plnAmnt);
					plnMioList.add(newPln);
					return plnMioList;
				}	
			}else{//此时，已支取金额 大于 总实收金额，不能出现这样的场景。数据异常。
				throw new BusinessException(new Throwable("暂收费支取异常，请立即维护！银行卡号为["+accInfo.getBankAccNo()+"]，投保单号为["+grpInsurAppl.getApplNo()+"]"));
			}
		}
		return plnMioList;
	}

	/**
	 * 重新给plnmioRec赋值:有缴费账号时
	 * @param grpInsurAppl 保单数据
	 * @param earnestPayInfo 暂缴费支取信息
	 * @return
	 */
	private List<PlnmioRec> setNewPlnMioRecOfIpsnPayGrp(GrpInsurAppl  grpInsurAppl ,EarnestPayInfo earnestPayInfo) {
		List<PlnmioRec> plnMioList = new ArrayList<>();


		BigDecimal plnAmnt;

		if(null == earnestPayInfo.getPlnAmnt()){
			plnAmnt =BigDecimal.valueOf(earnestPayInfo.getBalance());
		}else{
			plnAmnt =BigDecimal.valueOf(earnestPayInfo.getPlnAmnt());
		}

		for (IpsnPayGrp ipsnPayGrp : grpInsurAppl.getIpsnPayGrpList()) {

			if(StringUtils.equals(earnestPayInfo.getAccPersonNo(), String.valueOf(ipsnPayGrp.getFeeGrpNo()))){
				PlnmioRec newPln = getIniPlnmioRec(grpInsurAppl);
				newPln.setIpsnNo(0L);
				newPln.setLevelCode("");
				newPln.setFeeGrpNo(ipsnPayGrp.getFeeGrpNo());
				newPln.setBankCode(ipsnPayGrp.getBankCode());
				newPln.setBankAccName(ipsnPayGrp.getBankaccName());      //帐户所有人名称
				newPln.setMioCustNo("");
				newPln.setMioCustName("");
				newPln.setBankAccNo(ipsnPayGrp.getBankaccNo());
				newPln.setMultiPayAccType(MULTI_PAY_ACC_TYPE.CHARGE_GROUP_ACC.getKey());	//账号所属人类别

				//查询该银行账号对应的已支取的金额
				BigDecimal sunAmntPlnmioRec =queryAccInfoDaoService.getPlnmioRec(grpInsurAppl.getApplNo(), ipsnPayGrp.getBankaccNo(), ipsnPayGrp.getBankCode(), String.valueOf(ipsnPayGrp.getFeeGrpNo()), "P");
				if (null == sunAmntPlnmioRec) {
					sunAmntPlnmioRec = BigDecimal.ZERO;
				}

				BigDecimal sunMioLogAmnt = queryAccInfoDaoService.getMioLog(grpInsurAppl.getApplNo(), ipsnPayGrp.getBankaccNo(), ipsnPayGrp.getBankCode(), String.valueOf(ipsnPayGrp.getFeeGrpNo()), "P");
				if (null == sunMioLogAmnt) {
					throw new BusinessException(new Throwable("根据投保单号查询实收数据基本信息失败."));
				}

				//比较该银行账号下对应的已支取金额与总实收金额
				int result = sunAmntPlnmioRec.compareTo(sunMioLogAmnt);
				if(0 == result){//如果已支取金额 等于 总实收金额，那么说明该银行账号里的钱已支取完，需要支取下一个银行账号的金额。
					continue;
				}else if(-1 == result){//如果已支取金额 小于 总实收金额，那么说明该银行账号里的钱还没有支取完，可以支取该账户。
					//获取该账号下剩余可支取金额
					BigDecimal balancePlnmioRec = sunMioLogAmnt.subtract(sunAmntPlnmioRec);
					int re =plnAmnt.compareTo(balancePlnmioRec);
					if(re == 1){//支付金额大于 该账户号下金额，扣除该银行账号下所有金额。继续循环，扣除下一个账户。
						throw new BusinessException(new Throwable("暂收费支取异常，支取金额大于可支取金额."));
					}else{//支付金额小于 该账户号下金额，扣除该银行账号下的 支付金额，同时跳出循环，不再扣除其他账户的钱。
						newPln.setAmnt(plnAmnt);
						plnMioList.add(newPln);
						return plnMioList;
					}	
				}else{//此时，已支取金额 大于 总实收金额，不能出现这样的场景。数据异常。
					throw new BusinessException(new Throwable("暂收费支取异常，请立即维护！银行卡号为["+ipsnPayGrp.getBankaccNo()+"]，投保单号为["+grpInsurAppl.getApplNo()+"]"));
				}
			}
		}
		return plnMioList;
	}


	/**
	 * 重新给plnmioRec赋值:有缴费账号时
	 * @param grpInsurAppl 保单数据
	 * @param earnestPayInfo 暂缴费支取信息
	 * @return
	 */
	private List<PlnmioRec> setNewPlnMioRecOfOrgTree(GrpInsurAppl  grpInsurAppl ,EarnestPayInfo earnestPayInfo) {
		List<PlnmioRec> plnMioList = new ArrayList<>();


		BigDecimal plnAmnt;

		if(null == earnestPayInfo.getPlnAmnt()){
			plnAmnt =BigDecimal.valueOf(earnestPayInfo.getBalance());
		}else{
			plnAmnt =BigDecimal.valueOf(earnestPayInfo.getPlnAmnt());
		}

		if(null == grpInsurAppl.getOrgTreeList() || grpInsurAppl.getOrgTreeList().isEmpty()){//无组织架构树

			PlnmioRec newPln = getIniPlnmioRec(grpInsurAppl);

			newPln.setIpsnNo(0L);
			newPln.setLevelCode(STRING_FLAG_ZERO);
			newPln.setFeeGrpNo(0L);

			//契约形式，G：团单；L：清汇；
			String cntrType=grpInsurAppl.getCntrType();
			//汇交人类型 :P 个人汇交，G 单位汇交
			String sgType=grpInsurAppl.getSgType();
			if(CNTR_TYPE.GRP_INSUR.getKey().equals(cntrType) || 
					(CNTR_TYPE.LIST_INSUR.getKey().equals(cntrType) && LIST_TYPE.GRP_SG.getKey().equals(sgType))){
				newPln.setMioCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());		//领款人/交款人客户号
				newPln.setMioCustName(grpInsurAppl.getGrpHolderInfo().getGrpName());		//领款人/交款人姓名
			}else if(CNTR_TYPE.LIST_INSUR.getKey().equals(cntrType) && LIST_TYPE.PSN_SG.getKey().equals(sgType)){
				newPln.setMioCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());	//领款人/交款人客户号
				newPln.setMioCustName(grpInsurAppl.getPsnListHolderInfo().getSgName());	//领款人/交款人姓名
			}
			newPln.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());
			newPln.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());
			newPln.setBankAccName(grpInsurAppl.getPaymentInfo().getBankAccName());
			newPln.setMultiPayAccType(MULTI_PAY_ACC_TYPE.ORGANIZATION_TREE_ACC.getKey());	//账号所属人类别

			//查询该银行账号对应的已支取的金额
			BigDecimal sunAmntPlnmioRec =queryAccInfoDaoService.getPlnmioRec(grpInsurAppl.getApplNo(), 
					grpInsurAppl.getPaymentInfo().getBankAccNo(), grpInsurAppl.getPaymentInfo().getBankCode(), "0", "O");
			if (null == sunAmntPlnmioRec) {
				sunAmntPlnmioRec = BigDecimal.ZERO;
			}

			BigDecimal sunMioLogAmnt = queryAccInfoDaoService.getMioLog(grpInsurAppl.getApplNo(), 
					grpInsurAppl.getPaymentInfo().getBankAccNo(), grpInsurAppl.getPaymentInfo().getBankCode(), "0", "O");
			if (null == sunMioLogAmnt) {
				throw new BusinessException(new Throwable("根据投保单号查询实收数据基本信息失败."));
			}

			//比较该银行账号下对应的已支取金额与总实收金额
			int result = sunAmntPlnmioRec.compareTo(sunMioLogAmnt);
			if(0 == result){//如果已支取金额 等于 总实收金额，那么说明该银行账号里的钱已支取完，需要支取下一个银行账号的金额。
				throw new BusinessException(new Throwable("暂收费支取异常，可支取金额为0"));
			}else if(-1 == result){//如果已支取金额 小于 总实收金额，那么说明该银行账号里的钱还没有支取完，可以支取该账户。
				//获取该账号下剩余可支取金额
				BigDecimal balancePlnmioRec = sunMioLogAmnt.subtract(sunAmntPlnmioRec);
				int re =plnAmnt.compareTo(balancePlnmioRec);
				if(re == 1){//支付金额大于 该账户号下金额，扣除该银行账号下所有金额。继续循环，扣除下一个账户。
					throw new BusinessException(new Throwable("暂收费支取异常，支取金额大于可支取金额."));
				}else{//支付金额小于 该账户号下金额，扣除该银行账号下的 支付金额，同时跳出循环，不再扣除其他账户的钱。
					newPln.setAmnt(plnAmnt);
					plnMioList.add(newPln);
					return plnMioList;
				}	
			}else{//此时，已支取金额 大于 总实收金额，不能出现这样的场景。数据异常。
				throw new BusinessException(new Throwable("暂收费支取异常，请立即维护！银行卡号为["+grpInsurAppl.getPaymentInfo().getBankAccNo()+"]，投保单号为["+grpInsurAppl.getApplNo()+"]"));
			}
		}


		for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
			if(StringUtils.equals(earnestPayInfo.getAccPersonNo(), orgTree.getLevelCode())){

				PlnmioRec newPln = getIniPlnmioRec(grpInsurAppl);

				newPln.setIpsnNo(0L);
				newPln.setLevelCode(orgTree.getLevelCode());
				newPln.setFeeGrpNo(0L);
				newPln.setBankCode(orgTree.getBankCode());
				newPln.setBankAccNo(orgTree.getBankaccNo());
				newPln.setBankAccName(orgTree.getBankaccName()); //开户名称
				newPln.setMioCustNo(orgTree.getGrpHolderInfo().getGrpCustNo());	//领款人/交款人客户号
				newPln.setMioCustName(orgTree.getGrpHolderInfo().getGrpName());	//领款人/交款人姓名
				newPln.setMultiPayAccType(MULTI_PAY_ACC_TYPE.ORGANIZATION_TREE_ACC.getKey());	//账号所属人类别

				//查询该银行账号对应的已支取的金额
				BigDecimal sunAmntPlnmioRec =queryAccInfoDaoService.getPlnmioRec(grpInsurAppl.getApplNo(), 
						grpInsurAppl.getPaymentInfo().getBankAccNo(), grpInsurAppl.getPaymentInfo().getBankCode(), "0", "O");
				if (null == sunAmntPlnmioRec) {
					sunAmntPlnmioRec = BigDecimal.ZERO;
				}

				BigDecimal sunMioLogAmnt = queryAccInfoDaoService.getMioLog(grpInsurAppl.getApplNo(), orgTree.getBankaccNo(), orgTree.getBankCode(), orgTree.getLevelCode(), "O");
				if (null == sunMioLogAmnt) {
					throw new BusinessException(new Throwable("根据投保单号查询实收数据基本信息失败."));
				}

				//比较该银行账号下对应的已支取金额与总实收金额
				int result = sunAmntPlnmioRec.compareTo(sunMioLogAmnt);
				if(0 == result){//如果已支取金额 等于 总实收金额，那么说明该银行账号里的钱已支取完，需要支取下一个银行账号的金额。
					continue;
				}else if(-1 == result){//如果已支取金额 小于 总实收金额，那么说明该银行账号里的钱还没有支取完，可以支取该账户。
					//获取该账号下剩余可支取金额
					BigDecimal balancePlnmioRec = sunMioLogAmnt.subtract(sunAmntPlnmioRec);
					int re =plnAmnt.compareTo(balancePlnmioRec);
					if(re == 1){//支付金额大于 该账户号下金额，扣除该银行账号下所有金额。继续循环，扣除下一个账户。
						throw new BusinessException(new Throwable("暂收费支取异常，支取金额大于可支取金额."));
					}else{//支付金额小于 该账户号下金额，扣除该银行账号下的 支付金额，同时跳出循环，不再扣除其他账户的钱。
						newPln.setAmnt(plnAmnt);
						plnMioList.add(newPln);
						return plnMioList;
					}	
				}else{//此时，已支取金额 大于 总实收金额，不能出现这样的场景。数据异常。
					throw new BusinessException(new Throwable("暂收费支取异常，请立即维护！银行卡号为["+orgTree.getBankaccNo()+"]，投保单号为["+grpInsurAppl.getApplNo()+"]"));
				}
			}
		}
		return plnMioList;
	}

	public PlnmioRec getIniPlnmioRec(GrpInsurAppl  grpInsurAppl){
		PlnmioRec newPln = new PlnmioRec();
		newPln.setPlnmioRecId(plnmioRecDao.getPlnmioRecId());
		newPln.setCntrType(grpInsurAppl.getCntrType());
		newPln.setSgNo("");
		newPln.setCgNo(grpInsurAppl.getApplNo()+grpInsurAppl.getFirPolCode());
		newPln.setArcBranchNo(grpInsurAppl.getArcBranchNo());
		newPln.setCntrNo(grpInsurAppl.getApplNo());
		newPln.setSignYear(Integer.valueOf(grpInsurAppl.getApplNo().substring(0, 4)));
		newPln.setPolCode(grpInsurAppl.getFirPolCode());
		newPln.setCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode());
		newPln.setMtnId(0L);
		newPln.setMioItemCode(MIO_ITEM_CODE.FA.getKey());
		newPln.setProcStat(PROC_STAT_N);
		newPln.setMioCustNo("");
		newPln.setMioCustName("");
		newPln.setMioClass(Integer.valueOf(MIO_CLASS.HANDLE.getKey()));
		if(org.apache.commons.lang3.StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getMoneyinType()) || 
				StringUtils.equals("", grpInsurAppl.getPaymentInfo().getMoneyinType())){
			newPln.setMioType(MIO_TYPE.T.getKey());
		}else{
			newPln.setMioType(grpInsurAppl.getPaymentInfo().getMoneyinType());
		}

		newPln.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());
		if(1 == grpInsurAppl.getSalesInfoList().size()){
			newPln.setSalesChannel(grpInsurAppl.getSalesInfoList().get(0).getSalesChannel());
			newPln.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());
			newPln.setSalesNo(grpInsurAppl.getSalesInfoList().get(0).getSalesNo());
		}else{
			for (SalesInfo salesInfo : grpInsurAppl.getSalesInfoList()) {
				if(StringUtils.equals(STRING_FLAG_ONE,salesInfo.getDevelMainFlag())){
					newPln.setSalesChannel(salesInfo.getSalesChannel());
					newPln.setSalesBranchNo(salesInfo.getSalesBranchNo());
					newPln.setSalesNo(salesInfo.getSalesNo());
				}// end if
			}//end for 
		}// end if

		newPln.setLockFlag(STRING_FLAG_ZERO);
		newPln.setHoldFlag(STRING_FLAG_ZERO);
		newPln.setMioProcFlag(STRING_FLAG_ONE);
		newPln.setPlnmioDate(new Date());
		newPln.setMtnItemCode("");  		
		newPln.setPlnmioDate(new Date());	 	//应收付日期：暂时取保单生效日期
		newPln.setBankAccName("");      //帐户所有人名称
		newPln.setAccCustIdType(""); 	//帐户所有人证件类别
		newPln.setAccCustIdNo(""); 		//帐户所有人证件号
		newPln.setVoucherNo("");		//核销凭证号
		newPln.setPremDeadlineDate(null);
		newPln.setFinPlnmioDate(new Date());	//财务应收付日期
		newPln.setClearingMioTxNo("");   //清算交易号(收据号)
		newPln.setRouterNo(STRING_FLAG_ZERO); 		//路由号:默认是‘0’
		newPln.setAccId(0);				//关联帐户标识:默认0
		newPln.setIpsnName("");			//被保人姓名
		newPln.setIpsnSex("");			//被保人性别
		newPln.setIpsnIdType("");	//被保人证件类别
		newPln.setIpsnIdNo("");		//被保人证件号
		newPln.setRemark("");			//备注
		newPln.setPlnmioCreateTime(new Date());//生成应收记录时间
		newPln.setTransStat(TRANS_STAT_U);	     //转账状态：U空  N新建 ,W 抽取 ,S 成功 ,F 失败
		newPln.setExtKey1("");		//扩展健1
		newPln.setExtKey2("");		//扩展健2
		newPln.setExtKey3("");		//扩展健3
		newPln.setExtKey4("");		//扩展健4
		newPln.setExtKey5("");		//扩展健5

		return newPln;
	}



	/**全部支取逻辑：
	 *1.查询控制表是否存在状态为 N K E，如果有，直接返回----支取处理中
	 *2.如果查询不到 或 状态为C，查询所有账户对应的余额，如果存在 余额不为0的的账户，插入任务 进行全部支取动作，返回----支取处理中；如果所有余额都为0，返回失败。
	 */
	public RetInfo procEarnestPay(String applNo){

		RetInfo retInfo = new RetInfo();
		retInfo.setApplNo(applNo);

		//查询控制表是否已存在对应信息，如果有状态为(N-新建  E-失败     K-处理中)的，直接返回，否则插入任务信息。
		if(0 == queryAccInfoDaoService.findProcEarnestPayTask(applNo)){
			//查询所有账户对应的余额，如果存在 余额不为0的的账户，插入任务 进行全部支取动作
			if(0 != queryAccInfoDaoService.queryCountEarnestAccInfoByApplNo(applNo)){
				//当控制表为空时，插入控制表一条任务信息
				queryAccInfoDaoService.insertProcEarnestPayTask(applNo);	
			}else{
				retInfo.setRetCode(STRING_FLAG_ZERO);
				retInfo.setErrMsg("账户中没足够的余额进行支取");
				return retInfo;
			}
		}
		retInfo.setRetCode(STRING_FLAG_ONE);
		retInfo.setErrMsg("成功");
		return retInfo;
	}

}





