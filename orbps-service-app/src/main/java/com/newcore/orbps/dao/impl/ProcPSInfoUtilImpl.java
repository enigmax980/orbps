package com.newcore.orbps.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import com.alibaba.fastjson.JSONObject;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcPSInfoDao;
import com.newcore.orbps.dao.api.ProcPSInfoUtil;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.YES_NO_FLAG;


@Repository("procPSInfoUtil")
public class ProcPSInfoUtilImpl implements ProcPSInfoUtil {


	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	PlnmioRecDao plnmioRecDao;

	@Autowired
	ProcPSInfoDao procPSInfoDao;

	@Autowired
	JdbcOperations jdbcTemplate;

	private static final String UPDATE_STATUS_AND_APPL_LAND_FLAG_BY_APPL_NO_SQL = 
			"UPDATE TASK_CNTR_DATA_LANDING_QUEUE SET STATUS = ? , INSUR_APPL_LAND_FLAG = ? , EXT_KEY5 = ? WHERE APPL_NO = ?";


	@Override
	public Map<String, Double> procCorrPSData(GrpInsurAppl grpInsurAppl) {

		//声明map作为方法的返回值，其key对应账户信息表的账户号accNo,value为转保费金额。
		Map<String,Double> bigMap = new HashMap<>();

		//1、 根据保费来源产生应:【团单保费来源： 1.团体账户付款(单位缴费产生一笔总应收)；2.个人账户付款(收费组产生应收)；3.团体个人共同付款(每个被保人产生一笔应收)。】
		String premSource = grpInsurAppl.getPaymentInfo().getPremSource();
		if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)){//单位缴费部分			
			bigMap = getTeamData(grpInsurAppl);
		}else if(PREM_SOURCE.PSN_ACC_PAY.getKey().equals(premSource)){//个人缴费部分		
			bigMap = getIpsnData(grpInsurAppl);
		}else if(PREM_SOURCE.GRP_INDIVI_COPAY.getKey().equals(premSource)){  
			//单位缴费部分
			bigMap.putAll(getTeamData(grpInsurAppl));
			//个人缴费部分
			bigMap.putAll(getIpsnData(grpInsurAppl));		
		}
		return bigMap;  
	}


	/**
	 * 根据保费来源为团体缴费，获取组织架构树对应的被保人的保费金额
	 * @param grpInsurAppl 团单信息
	 */
	private  Map<String, Double> getTeamData(GrpInsurAppl grpInsurAppl) {
		String applNo=grpInsurAppl.getApplNo();
		//存放组织架构树对应的合格被保人单位缴费总计金额
		Map<String, Double> teamMap = new HashMap<>();
		//这种情况是保费来源为团体个人共同缴费	
		if(null == grpInsurAppl.getOrgTreeList() || grpInsurAppl.getOrgTreeList().isEmpty()){
			teamMap = getTeamNoTreeMap(grpInsurAppl);
		}else{ 
			//用于存放组织架构树中已经缴费的数据
			List<OrgTree> orgTreeList = new ArrayList<>();
			//orgTreeMap：存放所有组织架构树数据
			Map<String,OrgTree> orgTreeMap = new HashMap<>();
			//noIsPaidMap:存放不需要缴费的组织架构树数据
			Map<String,OrgTree> noIsPaidMap = new HashMap<>();		
			for(OrgTree orgTree :grpInsurAppl.getOrgTreeList()){
				//判断是否需要缴费:ifPay【Y：是；N：否。】
				orgTreeMap.put(orgTree.getLevelCode(), orgTree);
				if(StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())){
					orgTreeList.add(orgTree);
				}else{
					if(!StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsRoot())){
						noIsPaidMap.put(orgTree.getLevelCode(),orgTree);
					}
				}
			}
			if(orgTreeList.isEmpty()){
				//若空， 说明组织架构树节点都无需缴费，那么就只产生一条总险种保费的应收
				teamMap = getTeamNoTreeMap(grpInsurAppl);	
			}else{
				//遍历需要缴费的组织架构树中数据,产生应收

				//获取需要缴费的组织架构树下，不需要缴费的子节点信息
				Map<String,List<String>> levelMap = ProcMioInfoUtils.getChildTree(orgTreeMap,noIsPaidMap);
				//历史险种加费累计值，用于计算最后一条组织架构树的险种加费金额
				double sumPolAddJe=0; 	
				for(OrgTree orgTree :orgTreeList){		
					//组织层次代码
					String levelCode = orgTree.getLevelCode();
					//根据投保单号，组织层次号，获取该组织架构树下有效被保人信息的险种保费数据
					Map<String,Double> subStateMap = new HashMap<>();
					//levelList：查询条件，查询节点下的有效被保人
					List<String> levelList = new ArrayList<>();
					levelList.add(levelCode);
					if(levelMap.containsKey(levelCode)){
						levelList.addAll(levelMap.get(levelCode));
					}
					List<GrpInsured> grpInsuredList = procPSInfoDao.getGrpInsuredList(applNo, levelList);
					if(!grpInsuredList.isEmpty()){
						//当保费来源是单位缴费和个人缴费时 才计算被保人核保结论加费
						double ipsnAddJe=0;  	//被保人加费金额
						if(!PREM_SOURCE.GRP_INDIVI_COPAY.getKey().equals(grpInsurAppl.getPaymentInfo().getPremSource())){
							//此组织架构树的被保人核保结论加费金额总计
							List<Long> ipsnNoList = new ArrayList<>();
							for(GrpInsured grpInsured:grpInsuredList){
								ipsnNoList.add(grpInsured.getIpsnNo());
							}
							ipsnAddJe=procPSInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(),ipsnNoList);
						}	

						double sumLevelAddJe=0; //当前组织架构树针对每个险种的加费金额总计
						//判断当前遍历的是否是List的最后一条数据
						if(orgTreeList.size()>1 && orgTree.equals(orgTreeList.get(orgTreeList.size()-1)) ){
							//最后一条组织架构树险种加费金额=保单险种加费总计  - 历史险种加费累计值【sumPolAddJe】
							double policAddJe =procPSInfoDao.getPolicyAddMoney(applNo,"");
							sumLevelAddJe =policAddJe-sumPolAddJe;
						}else{											
							for(GrpInsured grpInsured:grpInsuredList){
								for(SubState subState:grpInsured.getSubStateList()){
									String polCode=subState.getPolCode().substring(0,3); 
									double premium=subState.getPremium();
									if(subStateMap.containsKey(polCode)){
										subStateMap.put(polCode, premium+subStateMap.get(polCode));
									}else{
										subStateMap.put(polCode, premium);
									}
								}
							}
							//计算当前组织架构树，关于每个险种的加费金额，累计值就是 当前组织架构树的险种加费金额
							for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
								if(subStateMap.containsKey(policy.getPolCode())){
									String polCode= policy.getPolCode(); //当前险种号
									double premium= policy.getPremium(); //当前险种的保费
									double invalidPolicyJe = procPSInfoDao.getInvalidPolicyMoney(applNo,polCode); //无效的险种保费
									//当前组织架构树针对当前险种的保费金额占比
									double bl = subStateMap.get(polCode)/(premium-invalidPolicyJe);
									//当前险种加费金额
									double policyJe=procPSInfoDao.getPolicyAddMoney(applNo,polCode);
									//当前组织架构树针对当前险种的加费金额
									double levelAddJe = policyJe*bl;
									sumLevelAddJe+=levelAddJe;
								}
							}
							sumPolAddJe+=sumLevelAddJe;
						}
						//金额= 险种加费金额【sumLevelAddJe】+ 节点交费金额【codePayMoney】+被保人加费金额【ipsnAddJe】-无效被保人单位缴费金额总计【invalidIpsn】
						double invalidIpsn = procPSInfoDao.getInvalidIpsnMoney(applNo,levelList);
						double amnt=sumLevelAddJe+orgTree.getNodePayAmnt()+ipsnAddJe-invalidIpsn;
						String key=applNo+"O"+levelCode;
						teamMap.put(key, BigDecimalUtils.keepPrecisionDouble(amnt,2));
					}
				}
			}
		}
		return teamMap;
	}



	/**
	 * 根据保费来源为个人缴费，获取被保人或收费组对应的保费金额
	 * @param grpInsurAppl 团单信息
	 */
	private  Map<String, Double> getIpsnData(GrpInsurAppl grpInsurAppl) {
		String applNo=grpInsurAppl.getApplNo();
		//获取投保单号对应的合格被保人数据集合
		List<GrpInsured> grsList = procPSInfoDao.getGrpInsuredList(applNo,null);
		//用于累计收费组号下的个人总的缴费金额
		Map<String,Double> feeGrpNoMap = new HashMap<>();
		//遍历被保人数据集合：根据被保人是否有缴费帐号来处理
		for(GrpInsured grpIn:grsList){
			//被保人核保结论加费总计:
			List<Long> ipsnList = new ArrayList<>();
			ipsnList.add(grpIn.getIpsnNo());
			double ipsnAddJe=procPSInfoDao.getPersonAddMoney(applNo,ipsnList);
			//无缴费帐号
			if(grpIn.getAccInfoList()== null || grpIn.getAccInfoList().isEmpty()){
				//应收类别【O:组织架构树应收,I:被保人应收,P:收费组产生应收】
				String key =applNo+"P"+grpIn.getFeeGrpNo();
				//缴费金额=个人缴费金额+个人加费金额
				double ipsnJe=grpIn.getIpsnPayAmount()+ipsnAddJe;	
				//判断容器中是否保存了 已当前组号为键的记录
				if(feeGrpNoMap.containsKey(key)){
					//若有记录，则把收费组号下个人缴费金额与此次遍历的个人缴费金额相加后，保存到容器中；
					feeGrpNoMap.put(key, ipsnJe+feeGrpNoMap.get(key));
				}else{
					//若没有记录，则把最新的收费组号，个人缴费金额存入容器中；
					feeGrpNoMap.put(key, ipsnJe);
				}					
			}else{//有缴费帐号时
				//被保人对应缴费帐号金额总计
				double sumAccInJe=0; 
				for(AccInfo accInfo :grpIn.getAccInfoList()){
					//不一定有缴费金额，需要根据缴费比例计算 TODO
					if(accInfo.getIpsnPayAmnt()>0){
						sumAccInJe += accInfo.getIpsnPayAmnt();
					}else if(accInfo.getIpsnPayPct()>0 && accInfo.getIpsnPayAmnt()==0 ){
						//需要计算出【缴费帐号的缴费金额】：个人账户交费比例*被保人信息个人缴费金额
						sumAccInJe+=accInfo.getIpsnPayPct()*grpIn.getIpsnPayAmount();
					}
				}
				String key =applNo+"I"+grpIn.getIpsnNo();
				feeGrpNoMap.put(key,BigDecimalUtils.keepPrecisionDouble(sumAccInJe+ipsnAddJe,2));
			}
		}
		return feeGrpNoMap;
	}

	/**
	 * 计算单位缴费无组织架构树收费金额
	 * @param grpInsurAppl
	 * @return
	 */
	private Map<String, Double> getTeamNoTreeMap(GrpInsurAppl grpInsurAppl) {
		String applNo = grpInsurAppl.getApplNo();
		double sumDwJe=0;
		//团单清单标志， L:普通清单，A：档案清单，M：事后补录，N：无清单
		if(!"L".equals(grpInsurAppl.getLstProcType())){
			//单笔无清单
			sumDwJe = plnmioRecDao.getPlnmioRecAmntBankTrans(applNo);
		}else{
			//单位缴费：遍历团单对应的有效被保人数据集合：
			sumDwJe =getSumGrpPayAmount(applNo);

			//判断是否公共保费
			if (null != grpInsurAppl.getHealthInsurInfo() && StringUtils.equals(
					grpInsurAppl.getHealthInsurInfo().getComInsurAmntType(), COMLNSUR_AMNT_TYPE.FIXED_INSURED.getKey())) {
				double tempComInsrPrem = null == grpInsurAppl.getHealthInsurInfo().getComInsrPrem() ? 0.00
						: grpInsurAppl.getHealthInsurInfo().getComInsrPrem();	
				sumDwJe += tempComInsrPrem ;
			}
			
			String cntrType=grpInsurAppl.getCntrType();	//契约形式，G：团单；L：清汇；
			//判断是否多期暂缴费
			if(CNTR_TYPE.LIST_INSUR.getKey().equals(cntrType) && "Y".equals(grpInsurAppl.getPaymentInfo().getIsMultiPay())){
				//金额:取投保要约里面的总保费*多期暂缴年数
				sumDwJe=sumDwJe*grpInsurAppl.getPaymentInfo().getMutipayTimes();
			}
			//计算险种加费
			double addFeeAmntSum=procPSInfoDao.getPolicyAddMoney(applNo, "");
			sumDwJe+=addFeeAmntSum;
			//当保费来源为：团体账户付款,缴费金额=被保人单位交费金额总计 + 险种加费总计被保人加费金额
			if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(grpInsurAppl.getPaymentInfo().getPremSource())){ 
				//无组织架构树的被保人加费
				double addFeeAmnt=procPSInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(),null);
				sumDwJe+=addFeeAmnt;
			}
		}
		String key=applNo+"O0";
		Map<String, Double> teamMap = new HashMap<>();
		teamMap.put(key, sumDwJe);
		return teamMap;
	}

	/**
	 * 计算被保人单位交费金额总计 TODO
	 * @param applNo 投保单号
	 * @return
	 */
	private  double getSumGrpPayAmount(String applNo) {
		double sumFaceAmnt = 0.0;

		Criteria criteria = Criteria.where("applNo").is(applNo).and("procStat").is("E");
		// 保额保费校验
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(criteria),
				Aggregation.group("SumGrpPayAmount").sum("grpPayAmount").as("sumgrpPayAmount"));

		AggregationResults<JSONObject> aggregate = mongoTemplate.aggregate(aggregation, "grpInsured", JSONObject.class);
		List<JSONObject> jsonObjects = aggregate.getMappedResults();

		for (JSONObject jsonObject : jsonObjects) {

			sumFaceAmnt += jsonObject.getDouble("sumgrpPayAmount");
		}

		return sumFaceAmnt;
	}


	@Override
	public Boolean updateStatusAndApplLandFlagByApplNo(String applNo, String status, String statusGrp) {
		Assert.notNull(applNo);
		Assert.notNull(status);
		Assert.notNull(statusGrp);
		jdbcTemplate.update(UPDATE_STATUS_AND_APPL_LAND_FLAG_BY_APPL_NO_SQL, status,statusGrp ,"订正后保单重新落地",applNo);
		return true;
	}




}

