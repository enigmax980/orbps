package com.newcore.orbps.service.business;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcPayAllEarnestInfoDao;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.service.bo.EarnestPayInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.SalesInfo;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.dao.api.QueryAccInfoDao;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MIO_TYPE;
import com.newcore.supports.dicts.MULTI_PAY_ACC_TYPE;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liushuaifeng on 2017/2/27 0027.
 */
public class EarnestAccItemProcessor implements ItemProcessor<EarnestAccInfo, Map<EarnestAccInfo, List<PlnmioRec>>> {


	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	PlnmioRecDao plnmioRecDao;

	@Autowired
	QueryAccInfoDao queryAccInfoDao;

	@Autowired
	ProcPayAllEarnestInfoDao procPayAllEarnestInfoDao;

	public static final String STRING_FLAG_ZERO = "0";

	public static final String STRING_FLAG_ONE = "1";

	public static final String ACC_NO_TYPE_I = "I";

	public static final String ACC_NO_TYPE_O = "O";

	public static final String ACC_NO_TYPE_P = "P";

	public static final String  PROC_STAT_N  = "N"; 

	public static final String  TRANS_STAT_U  = "U";


	@Override
	public Map<EarnestAccInfo, List<PlnmioRec>> process(EarnestAccInfo item) throws Exception {

		//创建map,用于返回
		Map<EarnestAccInfo, List<PlnmioRec>>  mapPlnmioRecList = new HashMap<>();

		//创建bo，用于封装参数
		EarnestPayInfo earnestPayInfo = new EarnestPayInfo();
		earnestPayInfo.setAccNo(item.getAccNo());
		earnestPayInfo.setAccType(item.getAccType());
		earnestPayInfo.setAccPersonNo(item.getAccPersonNo());
		earnestPayInfo.setApplNo(item.getAccNo().substring(0, 16));
		earnestPayInfo.setBalance(item.getBalance().doubleValue());


		//获取保单基本信息
		Map<String,Object> map = new HashMap<>();
		map.put("applNo", item.getAccNo().substring(0, 16));
		GrpInsurAppl  grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		if (null == grpInsurAppl) {
			throw new BusinessException(new Throwable("根据投保单号查询保单基本信息失败."));
		}

		//1.产生应付数据插入到PLNMIO_REC表中，最后放到map中返回;
		mapPlnmioRecList.put(item, confirmPremSource(grpInsurAppl , earnestPayInfo));
		return mapPlnmioRecList;
	}

	/**
	 * 根据账号信息判断缴费形式。
	 * @param grpInsurAppl 保单数据
	 * @param earnestPayInfo 暂缴费支取信息
	 * @return
	 */
	private  List<PlnmioRec> confirmPremSource(GrpInsurAppl  grpInsurAppl , EarnestPayInfo earnestPayInfo){
		List<PlnmioRec> plnMioList = new ArrayList<>();

		if(org.apache.commons.lang3.StringUtils.equals(ACC_NO_TYPE_I, earnestPayInfo.getAccType())){//有缴费账号
			Map<String, Object> queryMap = new HashMap<>();
			queryMap.put("applNo", earnestPayInfo.getApplNo());
			queryMap.put("ipsnNo", Long.valueOf(earnestPayInfo.getAccPersonNo()));
			GrpInsured grpInsured = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, queryMap);
			if (null == grpInsured) {
				throw new BusinessException(new Throwable("根据投保单号查询被保人基本信息失败."));
			}
			plnMioList = setNewPlnMioRecOfIpson(grpInsurAppl, grpInsured, earnestPayInfo);
		}else if(org.apache.commons.lang3.StringUtils.equals(ACC_NO_TYPE_O, earnestPayInfo.getAccType())){//无缴费账号，按单位缴费
			plnMioList = setNewPlnMioRecOfOrgTree(grpInsurAppl, earnestPayInfo);
		}else if(org.apache.commons.lang3.StringUtils.equals(ACC_NO_TYPE_P, earnestPayInfo.getAccType())){//无缴费账号，按收费组缴费
			plnMioList = setNewPlnMioRecOfIpsnPayGrp(grpInsurAppl, earnestPayInfo);
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

		BigDecimal plnAmnt =BigDecimal.valueOf(earnestPayInfo.getBalance());

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
			BigDecimal sunAmntPlnmioRec =procPayAllEarnestInfoDao.getPlnmioRec(grpInsurAppl.getApplNo(), accInfo.getBankAccNo(), accInfo.getBankCode(),String.valueOf(grpInsured.getIpsnNo()), "I");
			if (null == sunAmntPlnmioRec) {
				sunAmntPlnmioRec = BigDecimal.ZERO;
			}

			BigDecimal sunMioLogAmnt = procPayAllEarnestInfoDao.getSumAmntFromMioLog(grpInsurAppl.getApplNo(), accInfo.getBankAccNo(), accInfo.getBankCode(),String.valueOf(grpInsured.getIpsnNo()), "I");
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
				newPln.setAmnt(BigDecimal.valueOf(earnestPayInfo.getBalance()));
				plnMioList.add(newPln);
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
			newPln.setAmnt(BigDecimal.valueOf(earnestPayInfo.getBalance()));
			plnMioList.add(newPln);
			return plnMioList;
		}


		for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {//有组织架构树

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
				newPln.setAmnt(BigDecimal.valueOf(earnestPayInfo.getBalance()));
				plnMioList.add(newPln);
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
		newPln.setMioClass(Integer.valueOf(MIO_CLASS.HANDLE.getKey()));
		if(org.apache.commons.lang3.StringUtils.isBlank(grpInsurAppl.getPaymentInfo().getMoneyinType()) || 
				org.apache.commons.lang3.StringUtils.equals("", grpInsurAppl.getPaymentInfo().getMoneyinType())){
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
		newPln.setRemark("暂缴费支取应付");			//备注
		newPln.setPlnmioCreateTime(new Date());//生成应收记录时间
		newPln.setTransStat(TRANS_STAT_U);	     //转账状态：U空  N新建 ,W 抽取 ,S 成功 ,F 失败
		newPln.setExtKey1("");		//扩展健1
		newPln.setExtKey2("");		//扩展健2
		newPln.setExtKey3("");		//扩展健3
		newPln.setExtKey4("");		//扩展健4
		newPln.setExtKey5("");		//扩展健5

		return newPln;
	}




}
