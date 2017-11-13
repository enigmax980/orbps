package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import com.mongodb.BasicDBList;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.ProcCorrAccInfoService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.supports.dicts.PREM_SOURCE;
@Service("procCorrAccInfoService")
public class ProcCorrAccInfoServiceImpl implements ProcCorrAccInfoService{
	/**
	 * 日志管理工具实例.
	 */
	private  Logger logger = LoggerFactory.getLogger(ProcCorrAccInfoServiceImpl.class);

	@Autowired
	JdbcOperations jdbcTemplate;

	@Autowired
	ProcMioInfoDao procMioInfoDao;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public Map<String,Double>  procPSInfo(String applNo) {
		logger.info("开始执行转保费功能："+applNo);
		//1、 根据保费来源产生应:【团单保费来源： 1.团体账户付款(单位缴费产生一笔总应收)；2.个人账户付款(收费组产生应收)；3.团体个人共同付款(每个被保人产生一笔应收)。】
		GrpInsurAppl grpInsurAppl = procMioInfoDao.getGrpInsurAppl(applNo);
		String premSource = grpInsurAppl.getPaymentInfo().getPremSource();
		Map<String,Double> bigMap = new HashMap<>();
		if(!ProcMioInfoUtils.isBankTrans(grpInsurAppl)){
			this.logger.info("此保单的支付方式非银行转账，无须转保费操作！【"+applNo+"】!");
			return bigMap;
		}
		if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)){			
			this.logger.info("开始产生【保费来源：团体账户付款】保费计算【"+applNo+"】!");
			bigMap = getTeamData(grpInsurAppl);
			this.logger.info("【保费来源：团体账户付款】保费计算完成【"+bigMap.size()+"】!");
		}else if(PREM_SOURCE.PSN_ACC_PAY.getKey().equals(premSource)){		
			this.logger.info("开始产生【保费来源：个人账户付款】保费计算【"+applNo+"】!");
			bigMap = getIpsnData(grpInsurAppl);
			this.logger.info("【保费来源：个人账户付款】保费计算完成【"+bigMap.size()+"】!");
		}else if(PREM_SOURCE.GRP_INDIVI_COPAY.getKey().equals(premSource)){  
			this.logger.info("开始产生【保费来源：团体个人共同付款】保费计算【"+applNo+"】!");
			//单位缴费部分
			bigMap.putAll(getTeamData(grpInsurAppl));
			//个人缴费部分
			bigMap.putAll(getIpsnData(grpInsurAppl));		
			this.logger.info("【保费来源：团体个人共同付款】保费计算完成【"+bigMap.size()+"】!");
		}
		return bigMap;
	}

	/**
	 * 计算被保人单位交费金额总计 TODO
	 * @param applNo 投保单号
	 * @return
	 */
	private double getSumGrpPayAmount(String applNo) {
		BasicDBList dbd= new BasicDBList();
		dbd.add("N");
		dbd.add("E");
		Query query = new Query();
		query.addCriteria(Criteria.where("applNo").is(applNo).and("procStat").in(dbd));
		List<GrpInsured> list = mongoTemplate.find(query, GrpInsured.class);
		double sum=0;
		for(GrpInsured  grpInsured:list){
			sum+=grpInsured.getGrpPayAmount();
		}
		return sum;
	}
	/**
	 * 根据保费来源为团体缴费，获取组织架构树对应的被保人的保费金额
	 * @param grpInsurAppl 团单信息
	 */
	private Map<String, Double> getTeamData(GrpInsurAppl grpInsurAppl) {
		String applNo=grpInsurAppl.getApplNo();
		//存放组织架构树对应的合格被保人单位缴费总计金额
		Map<String, Double> teamMap = new HashMap<>();
		//这种情况是保费来源为团体个人共同缴费	
		if(grpInsurAppl.getOrgTreeList()==null || grpInsurAppl.getOrgTreeList().isEmpty()){
			double sumDwJe=0;
			//团单清单标志， L:普通清单，A：档案清单，M：事后补录，N：无清单
			if(!"L".equals(grpInsurAppl.getLstProcType())){
				//单笔无清单，取应收总金额
				MioPlnmioInfo mio = procMioInfoDao.getMioPlnmioInfo(applNo);
				sumDwJe = mio.getSumPremium();
			}else{
				//单位缴费：遍历团单对应的有效被保人数据集合：
				sumDwJe =getSumGrpPayAmount(applNo);			
				//计算险种加费
				double addFeeAmntSum=procMioInfoDao.getPolicyAddMoney(applNo, "");
				sumDwJe+=addFeeAmntSum;
			}
			String key=applNo+"O0";
			teamMap.put(key, sumDwJe);
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
				if("Y".equals(orgTree.getIsPaid())){
					orgTreeList.add(orgTree);
				}else{
					if(!"Y".equals(orgTree.getIsRoot())){
						noIsPaidMap.put(orgTree.getLevelCode(),orgTree);
					}
				}
			}
			if(orgTreeList.isEmpty()){
				//若空， 说明组织架构树节点都无需缴费，那么就只产生一条总险种保费的应收
				double sumDwJe =getSumGrpPayAmount(applNo);		//单位缴费	
				double addFeeAmntSum=procMioInfoDao.getPolicyAddMoney(applNo, ""); 	//计算险种加费
				String key=applNo+"O0";
				teamMap.put(key, sumDwJe+addFeeAmntSum);	
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
						logger.info(">>>>>>>缴费节点【levelCode】:"+levelCode+"下，不需要缴费的子节点："+levelMap.get(levelCode));
						levelList.addAll(levelMap.get(levelCode));
					}
					List<GrpInsured> grpInsuredList = procMioInfoDao.getGrpInsuredList(applNo, levelList);
					if(grpInsuredList.isEmpty()){
						logger.info("该组织架构树【"+levelCode+"】下无有效被保人信息【投保单号 "+applNo+"】");
					}else{
						logger.info(">>>>>>>根据【投保单号 "+applNo+"】，【节点 "+levelCode+"】，查询到【被保人数"+grpInsuredList.size()+"】");
						//当保费来源是单位缴费和个人缴费时 才计算被保人核保结论加费
						double ipsnAddJe=0;  	//被保人加费金额
						if(!PREM_SOURCE.GRP_INDIVI_COPAY.getKey().equals(grpInsurAppl.getPaymentInfo().getPremSource())){
							//此组织架构树的被保人核保结论加费金额总计
							List<Long> ipsnNoList = new ArrayList<>();
							for(GrpInsured grpInsured:grpInsuredList){
								ipsnNoList.add(grpInsured.getIpsnNo());
							}
							ipsnAddJe=procMioInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(),ipsnNoList);
						}	

						double sumLevelAddJe=0; //当前组织架构树针对每个险种的加费金额总计
						//判断当前遍历的是否是List的最后一条数据
						if(orgTreeList.size()>1 && orgTree.equals(orgTreeList.get(orgTreeList.size()-1)) ){
							//最后一条组织架构树险种加费金额=保单险种加费总计  - 历史险种加费累计值【sumPolAddJe】
							double policAddJe =procMioInfoDao.getPolicyAddMoney(applNo,"");
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
								String polCode= policy.getPolCode(); //当前险种号
								double premium= policy.getPremium(); //当前险种的保费
								double invalidPolicyJe = procMioInfoDao.getInvalidPolicyMoney(applNo,polCode); //无效的险种保费
								//当前组织架构树针对当前险种的保费金额占比
								double bl = subStateMap.get(polCode)/(premium-invalidPolicyJe);
								//当前险种加费金额
								double policyJe=procMioInfoDao.getPolicyAddMoney(applNo,polCode);
								//当前组织架构树针对当前险种的加费金额
								double levelAddJe = policyJe*bl;
								sumLevelAddJe+=levelAddJe;
							}
							sumPolAddJe+=sumLevelAddJe;
						}
						//金额= 险种加费金额【sumLevelAddJe】+ 节点交费金额【codePayMoney】+被保人加费金额【ipsnAddJe】-无效被保人单位缴费金额总计【invalidIpsn】
						double invalidIpsn = procMioInfoDao.getInvalidIpsnMoney(applNo,levelList);
						double amnt=sumLevelAddJe+orgTree.getNodePayAmnt()+ipsnAddJe-invalidIpsn;
						String key=applNo+"O"+levelCode;
						teamMap.put(key, ProcMioInfoUtils.keepPrecision(amnt,2));
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
	private Map<String, Double> getIpsnData(GrpInsurAppl grpInsurAppl) {
		String applNo=grpInsurAppl.getApplNo();
		//获取投保单号对应的合格被保人数据集合
		List<GrpInsured> grsList = procMioInfoDao.getGrpInsuredList(applNo,null);
		//用于累计收费组号下的个人总的缴费金额
		Map<String,Double> feeGrpNoMap = new HashMap<>();
		//遍历被保人数据集合：根据被保人是否有缴费帐号来处理
		for(GrpInsured grpIn:grsList){
			//被保人核保结论加费总计:
			List<Long> ipsnList = new ArrayList<>();
			ipsnList.add(grpIn.getIpsnNo());
			double ipsnAddJe=procMioInfoDao.getPersonAddMoney(applNo,ipsnList);
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
				feeGrpNoMap.put(key,ProcMioInfoUtils.keepPrecision(sumAccInJe+ipsnAddJe,2));
			}
		}
		return feeGrpNoMap;
	}
}
