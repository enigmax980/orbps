package com.newcore.orbps.service.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.ipms.api.PolicyBasicService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.orbpsutils.math.DateUtils;
import com.newcore.supports.dicts.COINSUR_TYPE;
import com.newcore.supports.dicts.MIO_TYPE;
import com.newcore.supports.dicts.MONEYIN_ITRVL;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 如果是多期暂交，生成多期应收
 * Created by liushuaifeng on 2017/2/11 0011.
 */
public class MultiPayCreationTasklet implements Tasklet {

	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired 
	PlnmioRecDao plnmioRecDao;

	@Autowired
	PolicyBasicService policyBasicService;

	private static final double MONEY = 0d;

	private static final int MONEY_IN_TYPE_ZERO = 0;

	private static final int MONEY_IN_TYPE_ONE = 1;

	private static final int MONEY_IN_TYPE_TWO = 2;

	private static final int MONEY_IN_TYPE_THERR = 3;

	private static final int MONEY_IN_TYPE_SIX = 6;

	private static final int MONEY_IN_TYPE_TWELVE = 12;

	private static final String MTN_ITEM_CODE = "29";

	private static final String MTO_ITEM_CODE = "PS";
	
	private static final String MIO_ITEM_CODES = "GF";
	
	private static final String TRANS_STAT = "U";


	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		ExecutionContext jobExecutionContext = chunkContext.getStepContext().getStepExecution().getJobExecution()
				.getExecutionContext();
		String applNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getString("applNo");
		GrpInsurAppl grpInsurAppl = procMioInfoDao.getGrpInsurAppl(applNo);
		if (null == grpInsurAppl) {
			//批作业执行失败落地
			throw new BusinessException(new Throwable("根据投保单号查询保单基本信息失败."));
		}

		//根据缴费方式产生获得对应的值
		int payNum = MONEY_IN_TYPE_ZERO;
		//缴费方式: M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期
		if (StringUtils.equals(MONEYIN_ITRVL.MONTH_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl())) {
			payNum = MONEY_IN_TYPE_ONE;
		} else if (StringUtils.equals(MONEYIN_ITRVL.QUARTER_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl())) {
			payNum = MONEY_IN_TYPE_THERR;
		} else if (StringUtils.equals(MONEYIN_ITRVL.HALF_YEAR_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinItrvl())) {
			payNum = MONEY_IN_TYPE_SIX;
		}
		Long batNo=(long) -1;
		
		//首席共保需产生一条应付 如果是共保，生成落地批次号（S_PLNMIO_REC_BAT_NO）
			if(!StringUtils.isEmpty(grpInsurAppl.getAgreementNo())){
				//1. 根据保单中协议号，获取共保基本信息
				Map<String,Object> comMap = new HashMap<>();
				comMap.put("agreementNo", grpInsurAppl.getAgreementNo());
				CommonAgreement  comAg =  (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class, comMap);
				if(comAg != null){
					String mainPolCode = grpInsurAppl.getFirPolCode();			 //主险险种代码
					Date inForceDate = grpInsurAppl.getInForceDate();
					Map<String, String> polMap = getPremDeadlineDate(mainPolCode) ;
					 int premExtAmnt =Integer.parseInt(polMap.get("premExtAmnt"));//需要相加的数值 
					String premExtUnit = polMap.get("premExtUnit"); 	//保费宽限期单位  Y-年 M-月 D-天
					 Date premDeadlineDate =DateUtils.getNewDate(inForceDate,premExtAmnt,premExtUnit);
					//应付数据集合:GF/Q
					List<PlnmioRec> plnmioRecList = new ArrayList<>();
					//险种代码对应总险种保费
					Map<String,Double> polCodeMap = getSumPolJe(grpInsurAppl);
					//生成批次号
					batNo=plnmioRecDao.getPlnmioRecBatNId();
					//2. 根据共保公司基本信息，险种信息生成对应的应付【GF/Q 非主承保人应付】
					for(ComCompany comCompany: comAg.getComCompanies()){
						//共保比例百分比
						double gbbl =comCompany.getCoinsurAmntPct()/100;
						//共保公司应收金额:保单总金额*共保保费份额比例
						if(!COINSUR_TYPE.CHIEF_COIN.getKey().equals(comCompany.getCoinsurType())){
							//2.2  产生应付[GF/Q]条数:为非主承保人个数*险种个数
							for(Entry<String,Double> entry :polCodeMap.entrySet()){
								String polCode= entry.getKey();
								double polJe = entry.getValue();
								//此参与承保方应收金额:共保保费份额比例
								double comCoJe = polJe*gbbl;		
								PlnmioRec plnmioRec = setPlnmioRecData(grpInsurAppl,comCompany,polCode);
								plnmioRec.setPlnmioRecId(plnmioRecDao.getPlnmioRecId());
								plnmioRec.setPolCode(polCode);
								plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(comCoJe, 2));
								plnmioRec.setBatNo(batNo);
								plnmioRec.setPremDeadlineDate(premDeadlineDate);
								plnmioRecList.add(plnmioRec);
								
							}
						}
					}
					mongoBaseDao.insertAll(plnmioRecList);
				//把batNo放到生效批作业，生效批作业放到落地控制表里
				}else{
					throw new BusinessException(new Throwable("根据共保协议号查询共保协议失败."));
				}
			}
			//如果payNum = 0，即说明不是分期应收数据，则无需操作直接返回成功，否则需要生成分期应收数据。
		if (MONEY_IN_TYPE_ZERO == payNum) {
			jobExecutionContext.putLong("plnLandBatNo", batNo);
			return RepeatStatus.FINISHED;
		}
		BigDecimal result;
		BigDecimal moneyinDurUnitBigD;
		BigDecimal moneyinDurBigD = new BigDecimal(String.valueOf(grpInsurAppl.getPaymentInfo().getMoneyinDur()));
		//1、根据缴费期单位得出缴费月数：result
		if (StringUtils.equals(MONEYIN_ITRVL.YEAR_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinDurUnit())) {
			moneyinDurUnitBigD = new BigDecimal(String.valueOf(MONEY_IN_TYPE_TWELVE));
			result = moneyinDurUnitBigD.multiply(moneyinDurBigD);    //Y:缴费年数
		} else if (StringUtils.equals(MONEYIN_ITRVL.HALF_YEAR_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinDurUnit())) {
			moneyinDurUnitBigD = new BigDecimal(String.valueOf(MONEY_IN_TYPE_SIX));
			result = moneyinDurUnitBigD.multiply(moneyinDurBigD);        //H:半年缴
		} else if (StringUtils.equals(MONEYIN_ITRVL.QUARTER_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinDurUnit())) {
			moneyinDurUnitBigD = new BigDecimal(String.valueOf(MONEY_IN_TYPE_THERR));
			result = moneyinDurUnitBigD.multiply(moneyinDurBigD);    //Q:季缴；
		} else if (StringUtils.equals(MONEYIN_ITRVL.MONTH_PAY.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinDurUnit())) {
			moneyinDurUnitBigD = new BigDecimal(String.valueOf(MONEY_IN_TYPE_ONE));
			result = moneyinDurUnitBigD.multiply(moneyinDurBigD);    // M:按照月缴
		} else {
			result = new BigDecimal(String.valueOf(MONEY_IN_TYPE_ONE));
		}
		int months = result.intValue();
		//期数
		int i = months / payNum - MONEY_IN_TYPE_ONE;
		//如果期数小于1，则不需要产生分期数据 ，否则产生分期应收数据。
		if (i >= MONEY_IN_TYPE_ONE) {
			//新生成的分期数据List
			List<PlnmioRec> fqList = new ArrayList<>();
			//保费来源：团体账户付款
			if (StringUtils.equals(PREM_SOURCE.GRP_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())) {
				//保费来源：团体账户付款,获得分期应收数据。
				List<PlnmioRec> team = getTeamData(grpInsurAppl);
				fqList.addAll(team);
			} else if (StringUtils.equals(PREM_SOURCE.PSN_ACC_PAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())) {
				//保费来源：个人账户付款，获得分期应收数据。
				List<PlnmioRec> personal = getPersonalData(grpInsurAppl);
				fqList.addAll(personal);
			} else if (StringUtils.equals(PREM_SOURCE.GRP_INDIVI_COPAY.getKey(), grpInsurAppl.getPaymentInfo().getPremSource())) {
				//保费来源：团体个人共同付款，获得分期应收数据。
				List<PlnmioRec> together = getTeamAndPersonalData(grpInsurAppl);
				fqList.addAll(together);
			}
			//allList=分期应收数据
			List<PlnmioRec> allList = new ArrayList<>();
			String mainPolCode = getMainPolCode(grpInsurAppl);			 //主险险种代码
			Map<String,String> polMap =getPremDeadlineDate(mainPolCode); 
			int premExtAmnt =Integer.parseInt(polMap.get("premExtAmnt"));//需要相加的数值 
			String premExtUnit = polMap.get("premExtUnit"); 	//保费宽限期单位  Y-年 M-月 D-天
			for (int a = 1; a <= i; a++) {
				//每一期间隔的月数
				int munths = a * payNum;
				//应收付日期[calendar]==生效日期[grpInsurAppl.getInForceDate()] + 月数【MUNTHS】
				Date plnmioDate = DateUtils.getNewDate(grpInsurAppl.getInForceDate(), munths,"M");
				Date premDeadlineDate =DateUtils.getNewDate(plnmioDate,premExtAmnt,premExtUnit); //获取保费缴费宽限日期
				for (PlnmioRec plnmioRec : fqList) {
					//重新创建对象
					PlnmioRec newPln = setNewPlnMioRec(plnmioRec);
					newPln.setPlnmioRecId(plnmioRecDao.getPlnmioRecId());
					newPln.setPlnmioDate(plnmioDate);    //应收付日期
					newPln.setPremDeadlineDate(premDeadlineDate);//保费缴费宽限期
					newPln.setPlnmioCreateTime(new Date());     //生成应收记录时间
					String cntrNo = getCntrNo(grpInsurAppl, newPln.getPolCode());

					String mioType = grpInsurAppl.getPaymentInfo().getMoneyinType();
					if(!org.apache.commons.lang3.StringUtils.isBlank(mioType)){
						newPln.setMioType(mioType);
					}
					newPln.setFinPlnmioDate(null);
					newPln.setCntrNo(cntrNo); //分保单号
					allList.add(newPln);
				}
			}
			//生成分期应收数据后插入oracle 及 mongodb数据库。
			boolean insertPlnmioRecResult =	plnmioRecDao.insertPlnmioRec(allList);
			if(!insertPlnmioRecResult){
				throw new BusinessException(new Throwable("分期应收数据插入oracle失败."));
			}
			//如果batNo=-1 获取plnmio_Rec_Id自增长序列最新值加1; 否则等于原值
			batNo=(long) -1==batNo?plnmioRecDao.getPlnmioRecBatNId():batNo;
			for (PlnmioRec plnmioRec : allList) {
				plnmioRec.setBatNo(batNo);
			}
			mongoBaseDao.insertAll(allList);
		}//end if 判断分期数据的期数
		jobExecutionContext.putLong("plnLandBatNo", batNo);
		return RepeatStatus.FINISHED;

	}

	/**
	 * 获取险种对应保费缴费截至宽限日期
	 * @param polCode 险种代码
	 * @return
	 */
	private Map<String,String> getPremDeadlineDate(String polCode) {
		//消息头设置
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		Map<String , Object> map = new HashMap<>();
		map.put("polCode", polCode);
		Map<String , Object> resultMap=policyBasicService.excute(map);
		JSONObject json = (JSONObject) JSONObject.toJSON(resultMap.get("policyBasicBo"));
		//需要相加的数值
		String premExtAmnt =json.getString("premExtAmnt") ; 
		//保费宽限期单位  Y-年 M-月 D-天
		String premExtUnit = json.getString("premExtUnit"); 
		Map<String , String>  returnMap = new HashMap<>();
		returnMap.put("premExtAmnt", premExtAmnt);
		returnMap.put("premExtUnit", premExtUnit);
		return returnMap;
	}

	/**
	 * 保费来源：团体账户付款，生成团体分期应收数据  TODO
	 *
	 * @param grpInsurAppl 团体出单基本信息
	 * @return
	 */
	private List<PlnmioRec> getTeamData(GrpInsurAppl grpInsurAppl) {
		//应收数据集合,作为本方法返回值
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//1、判断是否有组织架构树数据
		if (null == grpInsurAppl.getOrgTreeList() || grpInsurAppl.getOrgTreeList().isEmpty()) {
			//1.1、无组织机构数据时，产生分期应收逻辑
			plnmioRecList = getMioPlnmioInfo(grpInsurAppl);
		} else {
			//1.2、有组织机构数据，产生分期应收逻辑
			//用于存放组织架构树中已经缴费的数据
			List<OrgTree> orgTreeList = new ArrayList<>();
			//orgTreeMap：存放所有组织架构树数据
			Map<String, OrgTree> orgTreeMap = new HashMap<>();
			//noIsPaidMap:存放不需要缴费的组织架构树数据
			Map<String, OrgTree> noIsPaidMap = new HashMap<>();
			for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
				//判断是否需要缴费:ifPay【Y：是；N：否。】
				orgTreeMap.put(orgTree.getLevelCode(), orgTree);
				if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())) {
					orgTreeList.add(orgTree);
				} else {
					if (!StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsRoot())) {
						noIsPaidMap.put(orgTree.getLevelCode(), orgTree);
					}
				}
			}
			if (orgTreeList.isEmpty()) {
				//1.2.1、 若组织架构树中信息都不需要缴费，则按照无组织架构树产生分期应收数据
				plnmioRecList = getMioPlnmioInfo(grpInsurAppl);
			} else {
				//1.2.2、 组织架构树中需要缴费的数据，产生分期应收逻辑
				//获取需要缴费的组织架构树下，不需要缴费的子节点信息
				Map<String, List<String>> levelMap = ProcMioInfoUtils.getChildTree(orgTreeMap, noIsPaidMap);

				for (OrgTree orgTree : orgTreeList) {
					String levelCode = orgTree.getLevelCode();
					double sumPolAddJe = MONEY; //险种加费累计值，用于计算最后一比险种加费金额
					List<Policy> policyList = grpInsurAppl.getApplState().getPolicyList();
					for (Policy policy : policyList) {
						//初始化【应收付记录】数据
						PlnmioRec plnmioRec = setPlnmioRec(grpInsurAppl);
						plnmioRec.setLevelCode(levelCode);    //组织层次号
						plnmioRec.setBankCode(orgTree.getBankCode());    //银行代码
						plnmioRec.setBankAccNo(orgTree.getBankaccNo());    //银行账号
						plnmioRec.setBankAccName(orgTree.getBankaccName());//银行账号
						plnmioRec.setCntrNo(policy.getCntrNo()); //分保单号
						double ipsnAddJe = MONEY;    //被保人加费金额
						double polDwAddJe = MONEY;    //该险种加费金额：

						//根据投保单号，组织层次号，获取该组织架构树下有效被保人信息的险种保费数据
						Map<String, Double> subStateMap = new HashMap<>();

						//获取层次代码下的有效被保人数据
						List<String> levelList = new ArrayList<>();
						levelList.add(levelCode);
						if (levelMap.containsKey(levelCode)) {
							//缴费节点下，不需要缴费的子节点
							levelList.addAll(levelMap.get(levelCode));
						}
						List<GrpInsured> grpInsuredList = procMioInfoDao.getGrpInsuredList(grpInsurAppl.getApplNo(), levelList);
						if (null != grpInsuredList && !grpInsuredList.isEmpty()) {
							for (GrpInsured grpInsured : grpInsuredList) {
								//被保人加额保费信息
								ipsnAddJe += procMioInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(), policy.getPolCode(), grpInsured.getIpsnNo());
								for (SubState subState : grpInsured.getSubStateList()) {
									String polCode = subState.getPolCode().substring(0, 3); //险种代码
									double premium = subState.getPremium(); //险种保费
									if (subStateMap.containsKey(polCode)) {
										subStateMap.put(polCode, premium + subStateMap.get(polCode));
									} else {
										subStateMap.put(polCode, premium);
									}
								}
							}
							//险种总加费金额
							double polJe = procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(), policy.getPolCode());
							//判断当前遍历的是否是List的最后一条数据

							if (policyList.size() > MONEY_IN_TYPE_ONE && policy.equals(policyList.get(policyList.size() - MONEY_IN_TYPE_ONE))) {
								//险种加费金额=险种加费金额
								polDwAddJe = (polJe - sumPolAddJe);
							} else {
								//险种加费金额=险种加费金额
								double premium = policy.getPremium(); //当前险种的保费
								double invalidPolicyJe = procMioInfoDao.getInvalidPolicyMoney(grpInsurAppl.getApplNo(), policy.getPolCode()); //无效的险种保费
								double bl = subStateMap.get(policy.getPolCode()) / (premium - invalidPolicyJe);
								double levelAddJe = polJe * bl;
								sumPolAddJe += levelAddJe;
								polDwAddJe = levelAddJe;
							}
							//应收金额=保单单个险种保费【sumPolDwPayJe】 + 险种加费【polDwAddJe】+被保人加费金额【ipsnAddJe】-无效被保人单位缴费金额总计【invalidIpsn】
							double invalidIpsn = procMioInfoDao.getInvalidIpsnMoney(grpInsurAppl.getApplNo(), levelList);
							plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(orgTree.getNodePayAmnt() + polDwAddJe + ipsnAddJe - invalidIpsn, 2));
							plnmioRecList.add(plnmioRec);
						}
					}
				}
			}
		}
		return plnmioRecList;
	}

	/**
	 * 保费来源：个人账户付款,生成被保人分期应收数据
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	private List<PlnmioRec> getPersonalData(GrpInsurAppl grpInsurAppl) {
		//1、应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//2、根据投保单号(applNo)查询被保人清单信息
		List<GrpInsured> grsList = procMioInfoDao.getGrpInsuredList(grpInsurAppl.getApplNo(), null);
		if (null == grsList || grsList.isEmpty()) {
			throw new BusinessException(new Throwable("根据投保单号查询被保人清单基本信息失败."));
		}
		//3、此容器用于存放【key为收费组号+险种代码】的map
		Map<String, Double> feeGrpNoMap = new HashMap<>();
		//4、初始化【应收付记录】数据,为收费组Map【bigMap】使用
		PlnmioRec outPlnmioRec;
		//6、循环遍历 - 被保人信息集合
		for (GrpInsured grpInsured : grsList) {
			for (SubState subState : grpInsured.getSubStateList()) {
				String polCode = subState.getPolCode().substring(0, 3);
				//6.3.1、当期被保人加费金额
				double ipsnje = procMioInfoDao.getPersonAddMoney(grpInsured.getApplNo(), polCode, grpInsured.getIpsnNo());
				double perAddAmnt = ipsnje;
				//6.3.2、判断缴费账号GrpInsured.accInfoList是否有数据
				if (null == grpInsured.getAccInfoList() || grpInsured.getAccInfoList().isEmpty()) {
					//1、缴费帐号无数据时，根据收费组产生分期应收数据
					//被保人的收费组号
					long feeGrpNo = grpInsured.getFeeGrpNo();
					double ipsnPayAmount = grpInsured.getIpsnPayAmount(); //个人缴费金额
					//联合主键：收费组+#+险种代码
					String key = feeGrpNo + "#" + polCode;
					//险种代码及其对应的应缴费金额【个人缴费金额+个人加费金额】存入Map中
					double je = ipsnPayAmount + perAddAmnt;
					if (feeGrpNoMap.containsKey(key)) {
						feeGrpNoMap.put(key, feeGrpNoMap.get(key) + je);
					} else {
						feeGrpNoMap.put(key, je);
					}
				} else {
					//1、缴费帐号有数据时，根据缴费帐号产生分期应收数据
					//循环遍历   - 缴费账号
					double sumAccInfoAddJe = MONEY; //缴费帐号的加费金额累计
					for (AccInfo accInfo : grpInsured.getAccInfoList()) {
						//初始化【应收付记录】数据  遍历缴费帐号内部使用
						PlnmioRec inPlnmioRec = setPlnmioRec(grpInsurAppl);
						inPlnmioRec.setIpsnNo(grpInsured.getIpsnNo());    //被保人序号
						inPlnmioRec.setMioCustNo(grpInsured.getIpsnCustNo());    //领款人/交款人客户号
						inPlnmioRec.setMioCustName(grpInsured.getIpsnName());    //领款人/交款人姓名
						double accInfoJe = MONEY;    //缴费帐号的缴费金额
						double accInfoAddJe = MONEY;  //缴费帐号的加费金额

						//若相等，则说明此时遍历的是最后一条缴费帐号信息,直接用要约信息里面的保费减去已累计的总应收金额。
						int size = grpInsured.getAccInfoList().size();
						if (size > MONEY_IN_TYPE_ONE && accInfo.equals(grpInsured.getAccInfoList().get(size - MONEY_IN_TYPE_ONE))) {
							accInfoAddJe = perAddAmnt - sumAccInfoAddJe;
						} else {
							int retval1 = accInfo.getIpsnPayAmnt().compareTo(new Double("0.0"));
							int retval2 = accInfo.getIpsnPayPct().compareTo(new Double("0.0"));
							if (null != accInfo.getIpsnPayPct() && retval1 > MONEY_IN_TYPE_ZERO) {
								//1、当缴费帐号的【个人账户交费比例】没有值，【个人扣款金额】有值时:
								//需要计算出【缴费帐号的缴费比例】：缴费帐号的个人扣款金额【AccInfo.ipsnPayAmnt】/被保人信息个人缴费金额【GrpInsured.ipsnPayAmount】
								double accInfoBL = accInfo.getIpsnPayAmnt() / grpInsured.getIpsnPayAmount();
								//需要计算出【缴费帐号的缴费金额】：
								accInfoJe = accInfo.getIpsnPayAmnt();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计*缴费帐号的缴费比例
								accInfoAddJe = perAddAmnt * accInfoBL;
							} else if (null != accInfo.getIpsnPayAmnt() && retval2 > MONEY_IN_TYPE_ZERO) {
								//2、当缴费帐号的【个人账户交费比例】有值，【个人扣款金额】没有值时:
								//需要计算出【缴费帐号的缴费金额】：个人账户交费比例*被保人信息个人缴费金额
								accInfoJe = accInfo.getIpsnPayPct() * grpInsured.getIpsnPayAmount();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计   * 个人账户交费比例
								accInfoAddJe = perAddAmnt * accInfo.getIpsnPayPct();
							}
							sumAccInfoAddJe += accInfoAddJe;
						}

						inPlnmioRec.setPolCode(polCode);    //险种代码
						inPlnmioRec.setBankCode(accInfo.getBankCode());      //银行代码
						inPlnmioRec.setBankAccNo(accInfo.getBankAccNo()); //银行账号
						inPlnmioRec.setBankAccName(accInfo.getBankAccName());//银行账号
						//应收信息金额 = 加费金额【addAmnt】+ 缴费帐号的缴费金额【accInfoJe】
						inPlnmioRec.setAmnt(BigDecimalUtils.keepPrecision(accInfoAddJe + accInfoJe, 2));
						plnmioRecList.add(inPlnmioRec);
					}
				}
			}
			int retval3 = grpInsured.getIpsnPayAmount().compareTo(new Double("0.0"));
			if (null == grpInsured.getAccInfoList() && null == grpInsured.getFeeGrpNo() && retval3 > MONEY_IN_TYPE_ZERO) {
				//TODO 这种情况，需要与业务协商
				throw new BusinessException(new Throwable("数据出现特殊情况！需要与业务协商."));
			}
		}
		//7、根据feeGrpNoMap容器，获取保费金额
		for (Map.Entry<String, Double> entryIn : feeGrpNoMap.entrySet()) {
			//截取收费组
			String[] midKey = entryIn.getKey().split("#");
			long feeGrpNoIn = Long.valueOf(midKey[0]); //收费组
			String polCodeIn = midKey[MONEY_IN_TYPE_ONE];        //险种代码
			double midVal = entryIn.getValue(); //缴费金额
			outPlnmioRec = setPlnmioRec(grpInsurAppl);
			Map<String, Object> map = new HashMap<>();
			map.put("feeGrpNo", feeGrpNoIn);
			//根据map里面的键(也就是收费组号)查询 团体收费组grpInsurAppl.ipsnPayGrpList的账户信息产生应收
			IpsnPayGrp ipsnPayGrp = (IpsnPayGrp) mongoBaseDao.findOne(IpsnPayGrp.class, map);
			outPlnmioRec.setPolCode(polCodeIn);    //险种代码
			outPlnmioRec.setFeeGrpNo(feeGrpNoIn);    //收费组号
			outPlnmioRec.setBankCode(ipsnPayGrp.getBankCode());      //银行代码
			outPlnmioRec.setBankAccNo(ipsnPayGrp.getBankaccNo()); //银行账号
			outPlnmioRec.setBankAccName(ipsnPayGrp.getBankaccName());//银行账号
			outPlnmioRec.setAmnt(BigDecimalUtils.keepPrecision(midVal, MONEY_IN_TYPE_TWO));//金额
			plnmioRecList.add(outPlnmioRec);
		}
		return plnmioRecList;
	}

	/**
	 * 保费来源：团体个人共同付款,生成团体、个人共同缴费 模式  产生分期应收
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PlnmioRec> getTeamAndPersonalData(GrpInsurAppl grpInsurAppl) {
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		boolean orgTreeNotNull = true;
		//判断是否有组织架构树数据 :true有组织架构树    false 没组织架构树
		if (null == grpInsurAppl.getOrgTreeList() || grpInsurAppl.getOrgTreeList().isEmpty()) {
			orgTreeNotNull = false;
		}
		//1、orgTreeMap:保存组织架构树代码以及对应的险种金额
		Map<String, Map<String, Double>> orgTreeMap = new HashMap<>();
		//2、noOrgTreeMap:没组织架构树时，单位缴纳一笔总保费，险种代码以及险种对应保费分别作为键值对
		Map<String, Double> noOrgTreeMap = new HashMap<>();
		//3、accInfoMap:没个人账号时用保存收费组号以及对应的险种金额
		Map<Long, Object> accInfoMap = new HashMap<>();
		//4、codeMap:险种代码对应的险种加费金额
		Map<String, Double> codeMap = new HashMap<>();
		//5、单位险种加费：通过遍历险种信息，累加险种加费
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			double polAddje = procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(), policy.getPolCode());
			codeMap.put(policy.getPolCode(), polAddje);
		}
		//6、根据投保单号(applNo)查询被保人清单信息
		List<GrpInsured> grpInsuredList = procMioInfoDao.getGrpInsuredList(grpInsurAppl.getApplNo(), null);
		if (null == grpInsuredList || grpInsuredList.isEmpty()) {
			//批作业执行失败落地
			throw new BusinessException(new Throwable("根据投保单号查询被保人清单基本信息失败."));
		}
		for (GrpInsured grpInsured : grpInsuredList) {
			//1、计算个人、单位缴费总计:sumJe=个人缴费金额【ipsnPayAmount】+单位交费金额【grpPayAmount】
			double sumJe = grpInsured.getIpsnPayAmount() + grpInsured.getGrpPayAmount();
			//2、个人缴费比例：个人缴费金额【ipsnPayAmount】/sumJe
			double personalBL = grpInsured.getIpsnPayAmount() / sumJe;
			//3、单位缴费比例：单位缴费金额grpPayAmount/sumJe
			double teamBL = grpInsured.getGrpPayAmount() / sumJe;
			//4、inPolCodeMap 里面存放险种代码以及金额，该map作为orgTreeMap的值
			Map<String, Double> inPolCodeMap = new HashMap<>();
			//5、里面存放险种代码以及金额，该map作为accInfoMap的值
			Map<String, Double> noAccInfoMap = new HashMap<>();
			//循环遍历 - 子邀约信息
			for (SubState subState : grpInsured.getSubStateList()) {
				//险种代码
				String polCode = subState.getPolCode().substring(MONEY_IN_TYPE_ZERO, MONEY_IN_TYPE_THERR);
				//取该险种保费【premium】
				double premium = subState.getPremium();
				//单位部分保费计算开始--计算该险种单位应缴纳金额: 单位缴费比例【temBL】*险种保费【premium】 = 单位应缴纳金额【payJe】
				double teamPayJe = teamBL * premium;
				//无组织架构树
				if (noOrgTreeMap.containsKey(polCode)) {
					double sum = noOrgTreeMap.get(polCode) + teamPayJe;
					noOrgTreeMap.put(polCode, sum);
				} else {
					noOrgTreeMap.put(polCode, teamPayJe);
				}
				//有组织架构树
				if (orgTreeNotNull) {
					//组织层次代码:levelCode
					String levelCode = grpInsured.getLevelCode();
					if (orgTreeMap.containsKey(levelCode)) {
						inPolCodeMap = (Map<String, Double>) orgTreeMap.get(levelCode);
						if (inPolCodeMap.containsKey(polCode)) {
							inPolCodeMap.put(polCode, inPolCodeMap.get(polCode) + teamPayJe);
						} else {
							inPolCodeMap.put(polCode, teamPayJe);
						}
					} else {
						inPolCodeMap.put(polCode, teamPayJe);
					}
					orgTreeMap.put(levelCode, inPolCodeMap);
				}
				//个人部分保费计算开始--个人加费金额
				double perAddAmnt = procMioInfoDao.getPersonAddMoney(grpInsured.getApplNo(), polCode, grpInsured.getIpsnNo());
				//个人在该险种应缴保费【personalJe】 = 险种保费【premium】*个人缴费比例【personalBL】+个人加费金额【personalAddFeeAmnt】
				double personalJe = personalBL * premium + perAddAmnt;
				//判断被保人是否有缴费帐号
				if (null == grpInsured.getAccInfoList() || grpInsured.getAccInfoList().isEmpty()) {
					//无缴费帐号则根据收费组号产生分期应收
					Long feeGrpNo = grpInsured.getFeeGrpNo();    //收费组号
					if (accInfoMap.containsKey(feeGrpNo)) {
						noAccInfoMap = (Map<String, Double>) accInfoMap.get(feeGrpNo);
						if (noAccInfoMap.containsKey(polCode)) {
							noAccInfoMap.put(polCode, noAccInfoMap.get(polCode) + personalJe);
						} else {
							noAccInfoMap.put(polCode, personalJe);
						}
					} else {
						noAccInfoMap.put(polCode, personalJe);
					}
					//将最新的数据放入accInfoMap中
					accInfoMap.put(feeGrpNo, noAccInfoMap);
				} else {
					//有帐号时：根据个人缴费账号产生应收
					double sumAccInfoAddJe = MONEY; //缴费帐号的加费金额累计
					for (AccInfo accInfo : grpInsured.getAccInfoList()) {
						double accInfoJe = MONEY;    //缴费帐号的缴费金额
						double accInfoAddJe = MONEY;  //缴费帐号的加费金额

						int size = grpInsured.getAccInfoList().size();
						if (size > MONEY_IN_TYPE_ONE && accInfo.equals(grpInsured.getAccInfoList().get(size - MONEY_IN_TYPE_ONE))) {
							accInfoAddJe = perAddAmnt - sumAccInfoAddJe;
						} else {
							int retval1 = accInfo.getIpsnPayAmnt().compareTo(new Double("0.0"));
							int retval2 = accInfo.getIpsnPayPct().compareTo(new Double("0.0"));
							if (null != accInfo.getIpsnPayPct() && retval1 > MONEY_IN_TYPE_ZERO) {
								//1、当缴费帐号的【个人账户交费比例】没有值，【个人扣款金额】有值时:
								//需要计算出【缴费帐号的缴费比例】：缴费帐号的个人扣款金额【AccInfo.ipsnPayAmnt】/被保人信息个人缴费金额【GrpInsured.ipsnPayAmount】
								double accInfoBL = accInfo.getIpsnPayAmnt() / grpInsured.getIpsnPayAmount();
								//需要计算出【缴费帐号的缴费金额】：
								accInfoJe = accInfo.getIpsnPayAmnt();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计*缴费帐号的缴费比例
								accInfoAddJe = perAddAmnt * accInfoBL;
							} else if (null != accInfo.getIpsnPayAmnt() && retval2 > MONEY_IN_TYPE_ZERO) {
								//2、当缴费帐号的【个人账户交费比例】有值，【个人扣款金额】没有值时:
								//需要计算出【缴费帐号的缴费金额】：个人账户交费比例*被保人信息个人缴费金额
								accInfoJe = accInfo.getIpsnPayPct() * grpInsured.getIpsnPayAmount();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计   * 个人账户交费比例
								accInfoAddJe = perAddAmnt * accInfo.getIpsnPayPct();
							}
							sumAccInfoAddJe += accInfoAddJe;
						}

						//初始化【应收付记录】数据  遍历缴费帐号内部使用
						PlnmioRec inPlnmioRec = setPlnmioRec(grpInsurAppl);
						inPlnmioRec.setPolCode(polCode);
						inPlnmioRec.setBankCode(accInfo.getBankCode());        //银行代码
						inPlnmioRec.setBankAccNo(accInfo.getBankAccNo());    //银行帐号
						inPlnmioRec.setBankAccName(accInfo.getBankAccName());//帐户所有人名称
						inPlnmioRec.setIpsnNo(grpInsured.getIpsnNo());    //被保人序号
						//应收信息金额 = 加费金额【addAmnt】+ 缴费帐号的缴费金额【accInfoJe】
						inPlnmioRec.setAmnt(BigDecimal.valueOf(accInfoAddJe + accInfoJe));
						plnmioRecList.add(inPlnmioRec);
					}
				}
			}
		}

		//获取所有缴费节点下的无需缴费的子节点
		Map<String, List<String>> levelMap = getLevelMap(grpInsurAppl);
		//统计缴费节点及其无需缴费子节点的险种和险种保费数据
		Map<String, Map<String, Double>> sumPolMap = new HashMap<>();
		for (Entry<String, List<String>> entryIn : levelMap.entrySet()) {
			String level = entryIn.getKey();  //父节点
			List<String> entryList = entryIn.getValue(); //无需缴费的子节点
			//noPaidList 无需缴费的层次代码
			List<String> noPaidList = new ArrayList<>();
			noPaidList.add(level);
			noPaidList.addAll(entryList);
			Map<String, Double> noPaidMap = new HashMap<>();
			for (String lev : noPaidList) {
				if (null == orgTreeMap.get(lev)) {
					throw new BusinessException(new Throwable("当前组织架构树节点【levelCode:" + lev + "】下，无有效被保人数据，请查看."));
				} else {
					Map<String, Double> inMap = orgTreeMap.get(lev);
					for (Map.Entry<String, Double> inMapEntry : inMap.entrySet()) {
						String inPolCode = inMapEntry.getKey();     //险种代码
						Double inPolJe = inMapEntry.getValue();    //险种投保金额
						if (noPaidMap.containsKey(inPolCode)) {
							noPaidMap.put(inPolCode, inPolJe + noPaidMap.get(inPolCode));
						} else {
							noPaidMap.put(inPolCode, inPolJe);
						}
					}
					//处理完成后，将当前组织架构数据信息从orgTreeMap中移除，保证当levelMap遍历完成后，orgTreeMap中只存在正常需要缴费的数据
					orgTreeMap.remove(lev);
				}
			}
			sumPolMap.put(level, noPaidMap);
		}
		//缴费父级节点及其无需缴费子节点应收数据
		List<PlnmioRec> farPlnMioList = getPlnMioRec(grpInsurAppl, sumPolMap, noOrgTreeMap, codeMap);
		List<PlnmioRec> isPainPlnList = getPlnMioRec(grpInsurAppl, orgTreeMap, noOrgTreeMap, codeMap);
		plnmioRecList.addAll(farPlnMioList);
		plnmioRecList.addAll(isPainPlnList);
		//没组织架构树map:直接去缴费相关【grpInsurAppl.getPaymentInfo()】的银行账户信息产生应收
		if (!orgTreeNotNull) {
			for (Map.Entry<String, Double> entry : noOrgTreeMap.entrySet()) {
				String key = entry.getKey();
				Double value = entry.getValue();
				//初始化【应收付记录】数据
				PlnmioRec inPlnmioRecNoOrgMap = setPlnmioRec(grpInsurAppl);
				inPlnmioRecNoOrgMap.setIpsnNo(0L);    //被保人序号
				inPlnmioRecNoOrgMap.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());    //银行代码
				inPlnmioRecNoOrgMap.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());    //银行帐号
				inPlnmioRecNoOrgMap.setBankAccName(grpInsurAppl.getPaymentInfo().getBankAccName());    //帐户所有人名称
				inPlnmioRecNoOrgMap.setPolCode(key);            //险种代码
				inPlnmioRecNoOrgMap.setAmnt(BigDecimalUtils.keepPrecision(value + codeMap.get(key), MONEY_IN_TYPE_TWO));
				plnmioRecList.add(inPlnmioRecNoOrgMap);
			}
		}
		//没个人账号时map:取团体收费组【IpsnPayGrp】中的银行账户信息产生应收
		for (Map.Entry<Long, Object> entry : accInfoMap.entrySet()) {
			long key = entry.getKey();
			Object value = entry.getValue();
			//inMap 里面存放险种代码以及金额，该map作为orgTreeMap的值
			Map<String, Double> inMap = (Map<String, Double>) value;
			for (Entry<String, Double> inEntry : inMap.entrySet()) {
				String inKey = inEntry.getKey();
				double inVal = inEntry.getValue();
				//初始化【应收付记录】数据
				PlnmioRec inPlnmioRecAccMap = setPlnmioRec(grpInsurAppl);
				inPlnmioRecAccMap.setFeeGrpNo(key);    //收费组号
				for (IpsnPayGrp ipsnPayGrp : grpInsurAppl.getIpsnPayGrpList()) {
					if (ipsnPayGrp.getFeeGrpNo().equals(value)) {
						inPlnmioRecAccMap.setBankCode(ipsnPayGrp.getBankCode());    //银行代码
						inPlnmioRecAccMap.setBankAccNo(ipsnPayGrp.getBankaccNo());    //银行帐号
						inPlnmioRecAccMap.setBankAccName(ipsnPayGrp.getBankaccName());//帐户所有人名称
						break;
					}
				}
				inPlnmioRecAccMap.setPolCode(inKey);//险种代码
				inPlnmioRecAccMap.setAmnt(BigDecimalUtils.keepPrecision(inVal, MONEY_IN_TYPE_TWO));    //金额
				plnmioRecList.add(inPlnmioRecAccMap);
			}
		}
		return plnmioRecList;
	}

	/**
	 * 生成分期应收数据:组织架构树 团单特有无组织架构树，生成应收数据
	 *
	 * @param grpInsurAppl
	 * @return TODO
	 */
	private List<PlnmioRec> getMioPlnmioInfo(GrpInsurAppl grpInsurAppl) {
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//无组织机构树数据时，金额:取投保要约中险种产生分期数据
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			//初始化【应收付记录】数据
			PlnmioRec plnmioRec = setPlnmioRec(grpInsurAppl);
			plnmioRec.setPolCode(policy.getPolCode());    //险种代码
			plnmioRec.setCntrNo(policy.getCntrNo()); //分保单号
			plnmioRec.setMioCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());    //领款人/交款人客户号
			plnmioRec.setMioCustName(grpInsurAppl.getGrpHolderInfo().getGrpName());    //领款人/交款人姓名
			plnmioRec.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());        //银行代码
			plnmioRec.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());    //银行账号
			plnmioRec.setBankAccName(grpInsurAppl.getPaymentInfo().getBankAccName());//银行账号
			//保人核保结论加费金额=无组织架构树的被保人核保结论加费金额总计
			double addIpsnJe = procMioInfoDao.getPersonAddMoneyByPolCode(grpInsurAppl.getApplNo(), policy.getPolCode());
			double ipsnAddje = addIpsnJe;
			//险种加费金额
			double polAaddJe = procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(), policy.getPolCode());
			double polAddAmnt = polAaddJe;
			//金额:险种的实际保费【policy.getPremium()】 +险种核保结论中的加费金额【polAddAmnt】+被保人加费金额总计【addAmntPerson】
			plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(policy.getPremium() + polAddAmnt + ipsnAddje, 2));
			plnmioRecList.add(plnmioRec);
		}
		return plnmioRecList;
	}

	/**
	 * 初始化分期应收数据
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	private PlnmioRec setPlnmioRec(GrpInsurAppl grpInsurAppl) {
		InsurApplOperTrace insurOper = procMioInfoDao.getInsurApplOperTrace(grpInsurAppl.getApplNo());
		PlnmioRec pln = ProcMioInfoUtils.setPlnmioRec(grpInsurAppl, insurOper);
		pln.setCgNo(grpInsurAppl.getCgNo());
		pln.setMtnItemCode(MTN_ITEM_CODE);    //批改保全项目:29
		pln.setMioItemCode(MTO_ITEM_CODE);    //收付项目代码:分期应收 PS
		pln.setRemark("分期应收保费");    //备注
		return pln;
	}

	/**
	 * 获取所有缴费节点下无需缴费的子节点
	 *
	 * @param grpInsurAppl
	 * @return
	 */
	private Map<String, List<String>> getLevelMap(GrpInsurAppl grpInsurAppl) {
		//orgTreeMap：存放所有组织架构树数据
		Map<String, OrgTree> allOrgTreeMap = new HashMap<>();
		//noIsPaidMap:存放不需要缴费的组织架构树数据
		Map<String, OrgTree> noIsPaidMap = new HashMap<>();
		for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
			//判断是否需要缴费:ifPay【Y：是；N：否。】
			allOrgTreeMap.put(orgTree.getLevelCode(), orgTree);
			if (StringUtils.equals(YES_NO_FLAG.NO.getKey(), orgTree.getIsPaid()) && !StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsRoot())) {
				noIsPaidMap.put(orgTree.getLevelCode(), orgTree);
			}
		}
		//获取需要缴费的组织架构树下，不需要缴费的子节点信息
		Map<String, List<String>> levelMap = ProcMioInfoUtils.getChildTree(allOrgTreeMap, noIsPaidMap);
		return levelMap;
	}

	/**
	 * 获取单位部分应收数据
	 *
	 * @param grpInsurAppl 投保单号
	 * @param sumPolMap    需要生成应收的Map
	 * @param noOrgTreeMap 险种总保费
	 * @param codeMap      险种加费
	 * @return
	 */
	private List<PlnmioRec> getPlnMioRec(GrpInsurAppl grpInsurAppl, Map<String, Map<String, Double>> sumPolMap,
			Map<String, Double> noOrgTreeMap, Map<String, Double> codeMap) {
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		for (Map.Entry<String, Map<String, Double>> levelEntry : sumPolMap.entrySet()) {
			String key = levelEntry.getKey();        //组织层次代码
			Map<String, Double> inMap = levelEntry.getValue();    //险种及其投保金额
			for (Map.Entry<String, Double> entryIn : inMap.entrySet()) {
				String inPolCode = entryIn.getKey();     //险种代码
				Double inPolJe = entryIn.getValue();     //险种投保金额
				double jfZB = inPolJe / noOrgTreeMap.get(inPolCode); //险种缴费占比
				//初始化【应收付记录】数据
				PlnmioRec inPlnmioRecOrgMap = setPlnmioRec(grpInsurAppl);
				inPlnmioRecOrgMap.setLevelCode(key);    //组织层次代码
				for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
					if (StringUtils.equals(key, orgTree.getLevelCode())) {
						inPlnmioRecOrgMap.setBankCode(orgTree.getBankCode());  //银行代码
						inPlnmioRecOrgMap.setBankAccNo(orgTree.getBankaccNo());//银行帐号
						inPlnmioRecOrgMap.setBankAccName(orgTree.getBankaccName());//帐户所有人名称
						break;
					}
				}
				inPlnmioRecOrgMap.setPolCode(inPolCode);            //险种代码
				inPlnmioRecOrgMap.setAmnt(BigDecimalUtils.keepPrecision(inPolJe + codeMap.get(inPolCode) * jfZB,MONEY_IN_TYPE_TWO));//金额
				plnmioRecList.add(inPlnmioRecOrgMap);
			}
		}
		return plnmioRecList;
	}

	/**
	 * 重新给plnmioRec赋值
	 *
	 * @param oldPln
	 * @return
	 */
	private PlnmioRec setNewPlnMioRec(PlnmioRec oldPln) {
		PlnmioRec newPln = new PlnmioRec();

		newPln.setCntrType(oldPln.getCntrType());    //合同类型
		newPln.setSgNo(oldPln.getSgNo());            //汇缴事件号
		newPln.setArcBranchNo(oldPln.getArcBranchNo());//(路由)归档机构
		newPln.setCgNo(oldPln.getCgNo());        //合同组号
		newPln.setPremDeadlineDate(oldPln.getPremDeadlineDate()); //保费缴费宽限截止日期
		newPln.setPolCode(oldPln.getPolCode());    //险种代码
		newPln.setCntrNo(oldPln.getCntrNo());    //保单号/投保单号/帐号
		newPln.setCurrencyCode(oldPln.getCurrencyCode());    //保单币种
		newPln.setMtnId(oldPln.getMtnId());        //保全批改流水号
		newPln.setMtnItemCode(oldPln.getMtnItemCode()); //批改保全项目
		newPln.setIpsnNo(oldPln.getIpsnNo());            //被保人序号
		newPln.setLevelCode(oldPln.getLevelCode());        //组织层次代码
		newPln.setFeeGrpNo(oldPln.getFeeGrpNo());        //收费组号
		newPln.setMioCustNo(oldPln.getMioCustNo());        //领款人/交款人客户号
		newPln.setMioCustName(oldPln.getMioCustName());    //领款人/交款人姓名
		newPln.setMioClass(oldPln.getMioClass());        //收付类型
		newPln.setPlnmioDate(oldPln.getPlnmioDate());    //应收付日期：暂时取保单生效日期
		newPln.setSignYear(oldPln.getSignYear());        //(路由)签单年度
		newPln.setMioItemCode(oldPln.getMioItemCode());    //收付项目代码:首期暂收 FA
		newPln.setMioType(oldPln.getMioType());            //收付款形式代码
		newPln.setMgrBranchNo(oldPln.getMgrBranchNo());    //管理机构
		newPln.setSalesChannel(oldPln.getSalesChannel());    //销售渠道
		newPln.setSalesBranchNo(oldPln.getSalesBranchNo());    //销售机构号
		newPln.setSalesNo(oldPln.getSalesNo());    //销售员号
		newPln.setAmnt(oldPln.getAmnt());        //金额
		newPln.setLockFlag(oldPln.getLockFlag());        //锁标志:默认为0  银行转账在途则位1
		newPln.setBankCode(oldPln.getBankCode());        //银行代码 【注：根据不同的保费来源获取对应的银行信息】
		newPln.setBankAccName(oldPln.getBankAccName()); //帐户所有人名称
		newPln.setAccCustIdType(oldPln.getAccCustIdType()); //帐户所有人证件类别
		newPln.setAccCustIdNo(oldPln.getAccCustIdNo());     //帐户所有人证件号
		newPln.setBankAccNo(oldPln.getBankAccNo());            //银行账号 【注：根据不同的保费来源获取对应的银行信息】
		newPln.setHoldFlag(oldPln.getHoldFlag());            //待转帐标志:默认为0
		newPln.setVoucherNo(oldPln.getVoucherNo());            //核销凭证号
		newPln.setFinPlnmioDate(oldPln.getFinPlnmioDate());    //财务应收付日期
		newPln.setClearingMioTxNo(oldPln.getClearingMioTxNo()); //清算交易号(收据号)
		newPln.setMioProcFlag(oldPln.getMioProcFlag());        //是否收付处理标记:默认是'1'
		newPln.setRouterNo(oldPln.getRouterNo());            //路由号:默认是‘0’
		newPln.setAccId(oldPln.getAccId());                    //关联帐户标识:默认0
		newPln.setRemark(oldPln.getRemark());                //备注
		newPln.setProcStat(oldPln.getProcStat());            //应收状态：N 未收 ,D 作废 ,S 实收，T 在途
		newPln.setTransStat(oldPln.getTransStat());            //转账状态：U空  N新建 ,W 抽取 ,S 成功 ,F 失败
		return newPln;
	}

	/**
	 * 获取险种分保单号
	 *
	 * @param grpInsurAppl
	 * @param polCode      险种代码
	 * @return 分保单号 cntrNo
	 */
	private String getCntrNo(GrpInsurAppl grpInsurAppl, String polCode) {
		Map<String, String> map = new HashMap<>();
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			map.put(policy.getPolCode(), policy.getCntrNo());
		}
		return map.get(polCode) == null ? "" : map.get(polCode);
	}


	/**
	 * 获取主险代码
	 * @param grpInsurAppl
	 * @return
	 */
	private String getMainPolCode(GrpInsurAppl grpInsurAppl) {
		String polCode="";
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			if (MR_CODE.MASTER.getKey().equals(policy.getMrCode())) {
				polCode = policy.getPolCode();
				break;
			}
		}
		return polCode;
	}
	
	/**
	 * 获取险种总保费
	 * @param grpInsurAppl
	 * @return
	 */
	private Map<String, Double> getSumPolJe(GrpInsurAppl grpInsurAppl) {
		Map<String,Double> polCodeMap = new HashMap<>();
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			//险种加费金额
			double polAddJe = procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(), policy.getPolCode());
			//被保人加费金额
			double manAddJe = procMioInfoDao.getPersonAddMoneyByPolCode(grpInsurAppl.getApplNo(), policy.getPolCode());
			//险种保费
			double premium = policy.getPremium();
			//此参与承保方应收金额:共保保费份额比例
			double comCoJe =  polAddJe+manAddJe+premium;
			polCodeMap.put(policy.getPolCode(), comCoJe);
		}
		return polCodeMap;
	}
	
	/**
	 * 初始化【应付数据】 TODO
	 * @param grpInsurAppl 团体出单基本信息
	 * @param comCompany 共保公司基本信息
	 * @param polCode 险种代码
	 * @return PlnmioRec 应付数据
	 */
	private PlnmioRec setPlnmioRecData(GrpInsurAppl grpInsurAppl, ComCompany comCo,String polCode) {
		PlnmioRec plnmioRec = new PlnmioRec();
		plnmioRec.setPlnmioRecId(0L);	//应收付标识：自动增长序列
		plnmioRec.setCntrType(grpInsurAppl.getCntrType());	//合同类型
		plnmioRec.setSgNo("");								//汇缴事件号
		plnmioRec.setArcBranchNo(grpInsurAppl.getArcBranchNo());//(路由)归档机构
		plnmioRec.setCgNo(grpInsurAppl.getCgNo());//合同组号=applNo+第1险种
		plnmioRec.setPolCode("");		//险种代码
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			if(StringUtils.equals(polCode, policy.getPolCode())){
				plnmioRec.setCntrNo(policy.getCntrNo()); 	//保单号/投保单号/帐号
			}
		}
		plnmioRec.setCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode());	//保单币种
		plnmioRec.setMtnId((long) 0);		//保全批改流水号:默认为0
		
		plnmioRec.setMtnItemCode("0");  	//批改保全项目	
		plnmioRec.setIpsnNo(0L);		    //被保人序号:默认为0 
		plnmioRec.setMioCustNo(comCo.getCompanyCode());		 //领款人/交款人客户号 
		plnmioRec.setMioCustName(comCo.getCompanyName());//领款人/交款人姓名   
		plnmioRec.setMioClass(-1);			//收付类型:0-应收付；  1-应收； -1-应付
		plnmioRec.setPlnmioDate(grpInsurAppl.getInForceDate());	  //应收付日期：生效日期
		plnmioRec.setSignYear(getYear(grpInsurAppl.getInForceDate()));	//(路由)签单年度
		
		
		
		plnmioRec.setPremDeadlineDate(ProcMioInfoUtils.getDate(60)); //保单生效日期+宽限期设置天数
		plnmioRec.setMioItemCode(MIO_ITEM_CODES);	//收付项目代码: GF
		plnmioRec.setMioType(MIO_TYPE.Q.getKey());	//收付款形式代码		
		plnmioRec.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());		//管理机构
		
		plnmioRec.setSalesChannel(grpInsurAppl.getSalesInfoList().get(0).getSalesChannel());	//销售渠道		
		plnmioRec.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());	//销售机构号
		plnmioRec.setSalesNo(grpInsurAppl.getSalesInfoList().get(0).getSalesNo());	//销售员号		
		plnmioRec.setAmnt(BigDecimal.ZERO);		//金额  【注：根据不同的保费来源获计算应缴金额】		
		plnmioRec.setLockFlag("0");			//锁标志:默认为0  银行转账在途则位1
		plnmioRec.setBankCode(comCo.getBankCode());		
		plnmioRec.setBankAccName(comCo.getBankAccName());   //帐户所有人名称
		plnmioRec.setAccCustIdType(""); 	//帐户所有人证件类别
		plnmioRec.setAccCustIdNo("");       //帐户所有人证件号
		plnmioRec.setBankAccNo(comCo.getBankAccNo());	//银行账号
		
		plnmioRec.setHoldFlag("0");			//待转帐标志:默认为0	
		plnmioRec.setVoucherNo("");			//核销凭证号
		plnmioRec.setFinPlnmioDate(null); //财务应收付日期
		plnmioRec.setClearingMioTxNo("");   //清算交易号(收据号)
		plnmioRec.setMioProcFlag("1");		//是否收付处理标记:1
		plnmioRec.setRouterNo("0"); 		//路由号:默认是‘0’
		plnmioRec.setAccId((long)0);		//关联帐户标识:默认0
		plnmioRec.setRemark(grpInsurAppl.getAgreementNo());	 //备注:存放共保协议号grpInsurAppl.agreementNo
		plnmioRec.setProcStat("N");		    //应收状态：N未收  D作废  S实收/成功，F失败
		plnmioRec.setTransStat(TRANS_STAT);	//转账状态：N新建  U空  F失败  W抽取 S成功
		plnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
		plnmioRec.setMultiPayAccType("");	//账号所属人类别
		return plnmioRec;
	}
	/**
	 * 获取日期年份
	 * @param date 日期
	 * @return YYYY
	 */
	private int getYear(Date date){
		 Calendar c = Calendar.getInstance();
	     c.setTime(date);
	     c.get(Calendar.YEAR);
	     return c.get(Calendar.YEAR);
	}
	
}
