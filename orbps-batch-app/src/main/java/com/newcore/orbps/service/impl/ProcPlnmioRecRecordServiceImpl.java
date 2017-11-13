package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.dao.annotation.Transaction;
import com.halo.core.dasc.annotation.AsynCall;
import com.halo.core.header.HeaderInfoHolder;
import com.mongodb.BasicDBList;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.finance.EarnestAccInfo;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.orbps.service.api.ProcPlnmioRecRecordService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
import com.newcore.orbpsutils.dao.api.EarnestAccInfoDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.COMLNSUR_AMNT_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;
import com.newcore.tsms.models.service.bo.task.TaskProcessRequestBO;
import com.newcore.tsms.service.api.task.TaskProcessService;

/**
 * 产生应收数据接口实现
 * @author JCC
 * 2016-08-23 16:32:48
 */
@Service("procPlnmioRecRecordService")
public class ProcPlnmioRecRecordServiceImpl implements ProcPlnmioRecRecordService{

	/**
     * 日志管理工具实例.
     */
    private  Logger logger = LoggerFactory.getLogger(ProcPlnmioRecRecordServiceImpl.class);
	
	@Autowired
	PlnmioRecDao plnmioRecDao;
	
	@Autowired
	MongoBaseDao mongoBaseDao;
	
	@Autowired
	EarnestAccInfoDao earnestAccInfoDao;
	
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
    @Autowired
    PolNatureService polNatureService;
	
	@Autowired
	private TaskProcessService taskProcessServiceDascClient;

	/**
	 * 产生应收数据操作
	 * @param grpInsurAppl
	 * @return boolean
	 */
	@AsynCall
	@Override
	public boolean procPlnmioRecRecord(GrpInsurAppl grpInsurAppl,String taskId) {
		String applNo= grpInsurAppl.getApplNo();
		this.logger.info(">>>>>>>>>>>>>>>>开始产生首期应收数据:"+applNo);
		//判断应收数据是否生成成功
		boolean addFlag=true;
		//1、 根据保费来源产生应收【团单保费来源： 1.团体账户付款；2.个人账户付款；3.团体个人共同付款。】
		String premSource = grpInsurAppl.getPaymentInfo().getPremSource();
		//应收数据：根据团队信息生成的应收
		List<PlnmioRec> plnmioRecList= new ArrayList<>(); 
		if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)){	
			this.logger.info("开始产生【保费来源：团体账户付款】首期应收数据!");
			List<PlnmioRec> team = getTeamData(grpInsurAppl,premSource);
			plnmioRecList.addAll(team);
			this.logger.info("【保费来源：团体账户付款】首期应收数据生成完成!");
		}else if(PREM_SOURCE.PSN_ACC_PAY.getKey().equals(premSource)){		
			this.logger.info("开始产生【保费来源：个人账户付款】首期应收数据!");
			List<PlnmioRec> personal = getPersonalData(grpInsurAppl);	
			plnmioRecList.addAll(personal);
			this.logger.info("【保费来源：个人账户付款】首期应收数据生成完成!");
		}else if(PREM_SOURCE.GRP_INDIVI_COPAY.getKey().equals(premSource)){  	
			this.logger.info("开始产生【保费来源：团体个人共同付款】首期应收数据!");
			//单位应收集合			
			List<PlnmioRec> team  = getTeamData(grpInsurAppl,premSource);
			plnmioRecList.addAll(team);
			//个人应收集合
			List<PlnmioRec> personal = getPersonalData(grpInsurAppl);	
			plnmioRecList.addAll(personal);
			this.logger.info("【保费来源：团体个人共同付款】首期应收数据生成完成!");
		}
		if(plnmioRecList.isEmpty()){
			this.logger.info("首期应收数据生成失败：无应收数据生成plnmioRecList.isEmpty！ 【"+applNo+"】");
			return false;
		}
		try{
			addFlag = insertData(plnmioRecList,grpInsurAppl);
			//成功生成应收数据后的业务操作
			if(addFlag){
				//添加操作轨迹
				updateOperTrace(applNo);
				//调用任务完成接口
				doNext(applNo,taskId);
			}
		}catch(Exception ex){
			this.logger.info("首期应收数据生成失败【"+applNo+"】"+ex);
			addFlag=false;
		}
		return addFlag;
	}
	

	/**
	 * 生成入库应收数据
	 * @param plnmioRecList
	 * @param grpInsurAppl
	 * @return
	 */
	@Transaction
	private boolean insertData(List<PlnmioRec> plnmioRecList, GrpInsurAppl grpInsurAppl) {
		boolean addFlag= true;
		//判断是否是回退单子，若回退，则在原有数据的基础上添加应收明细，若不是，则直接新增一条数据applNo
		String applNo=grpInsurAppl.getApplNo();
		Map<String,String> paramQuery = new HashMap<>();
		paramQuery.put("cntr_no", applNo);	//投保单号
		paramQuery.put("mio_class", MIO_CLASS.RECEIVABLES.getKey());	//应收1
		paramQuery.put("mio_Item_Code", MIO_ITEM_CODE.FA.getKey());	//应收FA
		List<PlnmioRec>  plnmioList = plnmioRecDao.queryPlnmioRecList(paramQuery);	
		if(plnmioList.isEmpty()){
			addFlag=plnmioRecDao.insertPlnmioRec(plnmioRecList);
		}else {
			//判断支付方式是否为银行转账：非银行转账则生成一比应收数据
			if(!ProcMioInfoUtils.isBankTrans(grpInsurAppl)){
				plnmioRecDao.deletePlnmioRecByCntrNo(applNo);
				addFlag=plnmioRecDao.insertPlnmioRec(plnmioRecList);
			}else{
				//应收数据集合				
				//与账户比较后的应收数据
				List<PlnmioRec> newAccInfoList = getAccInfoList(plnmioRecList);
				if(newAccInfoList.isEmpty()){
					this.logger.info("首期应收数据：根据账户余额比较后，无应收数据生成newAccInfoList.isEmpty！ 【"+applNo+"】");
				}else if(newAccInfoList.size() >= 1){
					addFlag=plnmioRecDao.insertPlnmioRec(newAccInfoList);
				}
			}
		}
		return addFlag;
	}


	/**
	 * 生成保单应收账户信息 TODO
	 * @param plnmioRecList 应收数据
	 * @return
	 */
	private List<PlnmioRec> getAccInfoList(List<PlnmioRec> plnmioRecList) {
		//ispnMap:保存被保人对应的应收金额总计
		Map<Long,BigDecimal> ispnMap = new HashMap<>();
		//accMap:保存被保人对应的账户余额
		Map<String,BigDecimal> accMap = new HashMap<>();
		//ispsList:用于保存应收类别为被保人【I】的应收数据
		List<PlnmioRec> ispsList = new ArrayList<>();
		//noIspsList:用于保存应收类别非被保人【组织架构树O,收费组P】的应收数据
		List<PlnmioRec> noIspsList = new ArrayList<>();
		for(PlnmioRec pln :plnmioRecList){
			//帐号=投保单号(cntrNo)+帐号所属人类别(plnmioRecType【O:组织架构树应收,I:被保人应收,P:收费组产生应收】)+ LevelCode/IpsnNo/FeegrpNo
			String accNo = pln.getCntrNo()+pln.getMultiPayAccType();
			String key="";
			if("O".equals(pln.getMultiPayAccType())) {
				accNo += pln.getLevelCode();
				key=pln.getLevelCode();
			}else if("I".equals(pln.getMultiPayAccType())){
				accNo += pln.getIpsnNo();
				key=pln.getIpsnNo()+"";
			}else if("P".equals(pln.getMultiPayAccType())){
				accNo += pln.getFeeGrpNo();
				key=pln.getFeeGrpNo()+"";
			}
			//根据帐号查询账户信息表
			EarnestAccInfo eAcc =earnestAccInfoDao.queryEarnestAccInfo(accNo);
			if(eAcc == null){
				//没帐号数据，筛选到一个新的List集合中
				int num  = plnmioRecDao.getPlnmioRecSize(pln.getCntrNo(),pln.getMultiPayAccType(),key);
				if(num>0){
					//删除表中记录
					plnmioRecDao.deletePlnmioRec(pln.getCntrNo(),pln.getMultiPayAccType(),key);
				}
				noIspsList.add(pln);
			}else{
				//应收类别【O:组织架构树应收,I:被保人应收,P:收费组产生应收】
				if("I".equals(pln.getMultiPayAccType())){//若是被保人帐号信息，
					//保存被保人对应的账户信息
					accMap.put(accNo, eAcc.getBalance());
					//保存并累计被保人的应收金额
					if(ispnMap.containsKey(pln.getIpsnNo())){
						ispnMap.put(pln.getIpsnNo(), pln.getAmnt().add(ispnMap.get(pln.getIpsnNo())));
					}else{
						ispnMap.put(pln.getIpsnNo(), pln.getAmnt());
					}
					ispsList.add(pln);
				}else{
					//收费组和组织架构树只有一个账户信息：判断应收金额与账户余额是否相同:当应收金额小于或者等于账户信息余额时，不产生应收数据
					int num=BigDecimalUtils.compareBigDecimal(pln.getAmnt(),eAcc.getBalance());
					if(num == 1){
						//当应收金额大于账户信息余额时，修改当前应收金额值=应收金额-账户金额
						pln.setAmnt(pln.getAmnt().subtract(eAcc.getBalance()));
						noIspsList.add(pln);
					}
				}
			}
		}
	
		//被保人应缴金额
		Map<String,BigDecimal> hostroyDataMap = new HashMap<>();
		//统计个数
		Map<String,Integer> accSizeMap = new HashMap<>();
		int index;
		//被保人帐号需要特殊处理：
		for(PlnmioRec isps:ispsList){
			//帐号=投保单号(cntrNo)+帐号所属人类别(plnmioRecType)+帐号所属人序(spsnNo)	
			String accNo = isps.getCntrNo()+isps.getMultiPayAccType()+isps.getIpsnNo();
			BigDecimal balance = accMap.get(accNo);		//帐号信息余额
			BigDecimal ispnJe=ispnMap.get(isps.getIpsnNo()); //被保人对应的应收金额总计
			//判断应收金额与账户余额是否相同:当应收金额小于或者等于账户信息余额时，不产生应收数据
			int num=BigDecimalUtils.compareBigDecimal(ispnJe,balance);
			if(num == 1){ 
				//当应收金额大于账户信息余额时，修改当前应收金额值 【accInfoJe】= （应收金总计【ispnJe】-帐号信息余额【balance】） * 被保人缴费账户里面得账户比例
				BigDecimal accInfoJe=new BigDecimal(0);
				Map<String,Object> maps = new HashMap<>();
				maps.put("applNo", isps.getCntrNo());
				maps.put("ipsnNo", isps.getIpsnNo());				
				GrpInsured grpInsured= (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, maps);
				//当前应收数据对应的缴费帐号信息
				Map<String,AccInfo> accInfoMap = new HashMap<>();
				for(AccInfo acc: grpInsured.getAccInfoList()){
					accInfoMap.put(acc.getBankAccNo(), acc);
				}
				//获取缴费帐号对应的缴费占比
				if(accInfoMap.containsKey(isps.getBankAccNo())){
					AccInfo accInfo =accInfoMap.get(isps.getBankAccNo());
					double accInfoBL = 0;			//被保人缴费占比
					BigDecimal mayJe=ispnJe.subtract(balance);	//被保人应缴金额
					if(accInfo.getIpsnPayPct() !=null && accInfo.getIpsnPayAmnt()>0){
						//1、当缴费帐号的【个人账户交费比例】没有值，【个人扣款金额】有值时:
						//需要计算出【缴费帐号的缴费比例】：缴费帐号的个人扣款金额【AccInfo.ipsnPayAmnt】/被保人信息个人缴费金额【GrpInsured.ipsnPayAmount】
						accInfoBL=accInfo.getIpsnPayAmnt()/grpInsured.getIpsnPayAmount();
						accInfoJe =mayJe.multiply(BigDecimal.valueOf(accInfoBL));
					}else if(accInfo.getIpsnPayPct() != null && accInfo.getIpsnPayPct()>0){
						//2、当缴费帐号的【个人账户交费比例】有值
						accInfoJe=mayJe.multiply(BigDecimal.valueOf(accInfo.getIpsnPayPct()));
					}
					//判断当前遍历的对象，是否是被保人的另一个缴费帐号信息【】				
					String key=isps.getCntrNo()+isps.getIpsnNo();
					index = grpInsured.getAccInfoList().size();
					accSizeMap.put(key, index);
					if(hostroyDataMap.containsKey(key)){
						//是最后一条：应收金额 = 被保人缴费总计【ispnJe】- 另一个缴费帐号缴费金额
						if(accSizeMap.get(key)>1){
							hostroyDataMap.put(key, accInfoJe.add(hostroyDataMap.get(key)));
							accSizeMap.put(key, accSizeMap.get(key)-1);
						}else if(accSizeMap.get(key) == 1){
							accInfoJe=ispnJe.subtract(hostroyDataMap.get(key));
						}
					}else{
						hostroyDataMap.put(key, accInfoJe);
						accSizeMap.put(key, accSizeMap.get(key)-1);
					}
					isps.setAmnt(BigDecimalUtils.keepPrecision(accInfoJe, 2));
				}
				//将修改金额后的数据重新组装
				noIspsList.add(isps);
			}
		}
		return noIspsList;
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
	 * 生成团体缴费应收数据
	 * @param grpInsurAppl 团体出单基本信息
	 * @param ipsnPayAmount 个人缴费金额总和
	 * @param premSource 缴费方式，1：团单，3：团体个人共同缴费
	 * @return
	 */
	private List<PlnmioRec> getTeamData(GrpInsurAppl grpInsurAppl,String premSource) {
		String applNo=grpInsurAppl.getApplNo();
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//1、判断是否有 组织架构树
		if(grpInsurAppl.getOrgTreeList() == null || grpInsurAppl.getOrgTreeList().isEmpty()){ 
			//1.1、无组织架构树时
			plnmioRecList = getMioPlnmioInfo(grpInsurAppl,premSource);		
		}else{
			//1.2、有组织架构树时
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
				//若无缴费信息：则根据无组织架构树模式，生成一笔应收数据
				plnmioRecList = getMioPlnmioInfo(grpInsurAppl,premSource);				
			}else{
				//获取需要缴费的组织架构树下，不需要缴费的子节点信息
				Map<String,List<String>> levelMap = ProcMioInfoUtils.getChildTree(orgTreeMap,noIsPaidMap);
				//历史险种加费累计值，用于计算最后一条组织架构树的险种加费金额
				double sumPolAddJe=0; 	
				for(OrgTree orgTree :orgTreeList){		
					//组织层次代码
					String levelCode = orgTree.getLevelCode();
					//初始化【应收付记录】数据
					PlnmioRec plnmioRec = setPlnmioRec(grpInsurAppl);	
					plnmioRec.setLevelCode(levelCode); 		//组织层次代码
					plnmioRec.setBankCode(orgTree.getBankCode());		//银行代码
					plnmioRec.setBankAccNo(orgTree.getBankaccNo());		//银行账号
					plnmioRec.setBankAccName(orgTree.getBankaccName()); //开户名称
					plnmioRec.setMioCustNo(orgTree.getGrpHolderInfo().getGrpCustNo());	//领款人/交款人客户号
					plnmioRec.setMioCustName(orgTree.getGrpHolderInfo().getGrpName());	//领款人/交款人姓名
					
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
						
						double ipsnAddJe=0;  	//被保人加费金额
						//当保费来源是单位缴费 才计算被保人核保结论加费
						if(PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)){
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
								if(subStateMap.containsKey(polCode)){
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
							}
							sumPolAddJe+=sumLevelAddJe;
						}
						//金额= 险种加费金额【sumLevelAddJe】+ 节点交费金额【codePayMoney】+被保人加费金额【ipsnAddJe】-无效被保人单位缴费金额总计【invalidIpsn】
						double invalidIpsn = procMioInfoDao.getInvalidIpsnMoney(applNo,levelList);
						double amnt=sumLevelAddJe+orgTree.getNodePayAmnt()+ipsnAddJe-invalidIpsn;
						plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(amnt,2));
						plnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
						plnmioRec.setMultiPayAccType("O");	
						plnmioRecList.add(plnmioRec);
					}
				}
			}
		}
		return plnmioRecList;
	}

	/**
	 * 生成应收数据:组织架构树 团单特有无组织架构树，生成一笔应收数据
	 * @param grpInsurAppl
	 * @param premSource 保费来源
	 * @return TODO
	 */
	private List<PlnmioRec> getMioPlnmioInfo(GrpInsurAppl grpInsurAppl,String premSource) {
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//初始化【应收付记录】数据
		PlnmioRec plnmioRec = setPlnmioRec(grpInsurAppl);	
		//T：银行转账
		if(MONEYIN_TYPE.TRANSFER.getKey().equals(grpInsurAppl.getPaymentInfo().getMoneyinType())){
			plnmioRec.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());			//银行代码
			plnmioRec.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());		//银行账号
			plnmioRec.setBankAccName(grpInsurAppl.getPaymentInfo().getBankAccName());   //开户名称
		};
		
		//契约形式，G：团单；L：清汇；
		String cntrType=grpInsurAppl.getCntrType();
		//汇交人类型 :P 个人汇交，G 单位汇交
		String sgType=grpInsurAppl.getSgType();
		if(CNTR_TYPE.GRP_INSUR.getKey().equals(cntrType) || 
				(CNTR_TYPE.LIST_INSUR.getKey().equals(cntrType) && LIST_TYPE.GRP_SG.getKey().equals(sgType))){
			plnmioRec.setMioCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());		//领款人/交款人客户号
			plnmioRec.setMioCustName(grpInsurAppl.getGrpHolderInfo().getGrpName());		//领款人/交款人姓名
		}else if(CNTR_TYPE.LIST_INSUR.getKey().equals(cntrType) && LIST_TYPE.PSN_SG.getKey().equals(sgType)){
			plnmioRec.setMioCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());	//领款人/交款人客户号
			plnmioRec.setMioCustName(grpInsurAppl.getPsnListHolderInfo().getSgName());	//领款人/交款人姓名
		}

		//总应收金额
		double sumJE=0;
		//险种加费总计 
		double policyAddJe =procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(),"");	
		//判断保费来源：当保费来源是团体个人帐号共同交费时，缴费金额=被保人单位交费金额总计 + 险种加费总计， 不需要加上被保人加费金额
		if(PREM_SOURCE.GRP_INDIVI_COPAY.getKey().equals(premSource)){ 
			//被保人单位交费金额总计
			sumJE=getSumGrpPayAmount(grpInsurAppl.getApplNo());
			plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(sumJE+policyAddJe,2));
		}else{
			//基金险功模块业务：在生效的时候，需要计算公共与个人账户金额以及管理费金额
	        boolean isFund = checkPolCodeIsFund(grpInsurAppl.getApplState().getPolicyList());
	        if(isFund){ //若是基金险，在直接取投保要约总保费
				plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(grpInsurAppl.getApplState().getSumPremium(),2));
	        }else{
	        	double ipsnaddAmnt=0; 	//无组织架构树的被保人加费
				//团单清单标志， L:普通清单，A：档案清单，M：事后补录，N：无清单
				if(!"L".equals(grpInsurAppl.getLstProcType())){
					//单笔无清单
					sumJE =grpInsurAppl.getApplState().getSumPremium();
				}else{
					//单位缴费：遍历团单对应的有效被保人数据集合：
					sumJE =getSumGrpPayAmount(grpInsurAppl.getApplNo());
					ipsnaddAmnt=procMioInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(),null);
				}
				
				//判断是否公共保费
				if (null != grpInsurAppl.getHealthInsurInfo() && StringUtils.equals(
						grpInsurAppl.getHealthInsurInfo().getComInsurAmntType(), COMLNSUR_AMNT_TYPE.FIXED_INSURED.getKey())) {
					double tempComInsrPrem = null == grpInsurAppl.getHealthInsurInfo().getComInsrPrem() ? 0.00
							: grpInsurAppl.getHealthInsurInfo().getComInsrPrem();	
					sumJE += tempComInsrPrem ;
				}
				
				//判断是否多期暂缴费,若是多期暂缴费，金额 = sumJE * 多期暂缴年数
				if(CNTR_TYPE.LIST_INSUR.getKey().equals(cntrType) && "Y".equals(grpInsurAppl.getPaymentInfo().getIsMultiPay())){
					sumJE=sumJE*grpInsurAppl.getPaymentInfo().getMutipayTimes();
				}
				//金额:总保费+险种加费+被保人加费
				plnmioRec.setAmnt(BigDecimalUtils.keepPrecision(sumJE+policyAddJe+ipsnaddAmnt,2));
			}
		}
		plnmioRec.setMioType(grpInsurAppl.getPaymentInfo().getMoneyinType());//收付款形式代码
		plnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
		plnmioRec.setMultiPayAccType("O"); 	  	  //账户所属类别 
		plnmioRec.setLevelCode("0"); 
		plnmioRecList.add(plnmioRec);
		return plnmioRecList;
	}

	/**
	 * 生成个人缴费应收数据
	 * @param grpInsurAppl 团体出单基本信息
	 * @return
	 */
	private List<PlnmioRec> getPersonalData(GrpInsurAppl grpInsurAppl) {
		String applNo=grpInsurAppl.getApplNo();
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//根据投保单号(applNo)查询被保人清单信息
		List<GrpInsured> grsList=procMioInfoDao.getGrpInsuredList(applNo,null);
		//添加一个保存收费组号，和个人缴费金额的容器，用于累计收费组号下的个人总的缴费金额
		Map<Long,Double> feeGrpNoMap = new HashMap<>();
		//被保人信息
		for(GrpInsured grpInsured:grsList){			
			//*被保人核保结论加费总计:
			List<Long> ipsnNoList = new ArrayList<>();
			ipsnNoList.add(grpInsured.getIpsnNo());
			double addJePersonal=procMioInfoDao.getPersonAddMoney(applNo,ipsnNoList);
			//判断缴费账号是否有数据
			if(grpInsured.getAccInfoList()== null || grpInsured.getAccInfoList().isEmpty()){
				long feeGrpNo=grpInsured.getFeeGrpNo();			//收费组号		
				double ipsnPayAmount=grpInsured.getIpsnPayAmount();	//个人缴费金额	
				//判断容器中是否保存了 已当前组号为键的记录
				if(feeGrpNoMap.containsKey(feeGrpNo)){
					//若有记录，则把收费组号下个人缴费金额与此次遍历的个人缴费金额相加后，保存到容器中；
					feeGrpNoMap.put(feeGrpNo, addJePersonal+ipsnPayAmount+feeGrpNoMap.get(feeGrpNo));
				}else{
					//若没有记录，则把最新的收费组号，个人缴费金额存入容器中；
					feeGrpNoMap.put(feeGrpNo, ipsnPayAmount+addJePersonal);
				}					
			}else{
				//有数据时，循环遍历这个被保人的缴费账户，根据里面的个人扣款金额(ipsnPayAmnt)或个人交费比例(ipsnPayRate)以及账户信息产生应收
				double sumAccInfoAddJe=0; //缴费帐号的加费金额累计
				for(AccInfo accInfo :grpInsured.getAccInfoList()){
					//初始化【应收付记录】数据  遍历缴费帐号内部使用
					PlnmioRec inPlnmioRec = setPlnmioRec(grpInsurAppl);
					inPlnmioRec.setBankCode(accInfo.getBankCode());		//银行代码
					inPlnmioRec.setBankAccNo(accInfo.getBankAccNo());	//银行账号	
					inPlnmioRec.setBankAccName(accInfo.getBankAccName());//帐户所有人名称
					inPlnmioRec.setIpsnNo(grpInsured.getIpsnNo());		 //被保人序号
					inPlnmioRec.setMioCustNo(grpInsured.getIpsnCustNo());	//领款人/交款人客户号
					inPlnmioRec.setMioCustName(grpInsured.getIpsnName());	//领款人/交款人姓名
					double accInfoJe =0 ;	//缴费帐号的缴费金额
					double accInfoAddJe=0;  //缴费帐号的加费金额
					double ipsnPayAmount = grpInsured.getIpsnPayAmount(); //被保人信息个人缴费金额
					if(accInfo.getIpsnPayAmnt() !=null && accInfo.getIpsnPayAmnt()>0){
						//1、当【个人扣款金额】有值时:
						//缴费帐号的缴费比例=缴费帐号的个人扣款金额【AccInfo.ipsnPayAmnt】/被保人信息个人缴费金额【ipsnPayAmount】
						double accInfoBL=accInfo.getIpsnPayAmnt()/ipsnPayAmount;
						//当前缴费帐号的加费金额 = 被保人加费金额总计   * 缴费帐号的缴费比例
						accInfoAddJe = addJePersonal * accInfoBL;
						//缴费帐号的缴费金额
						accInfoJe=accInfo.getIpsnPayAmnt();
					}else if( accInfo.getIpsnPayPct() !=null && accInfo.getIpsnPayPct()>0 ){
						//当缴费帐号的【个人账户交费比例】有值时:
						//缴费帐号的缴费金额：个人账户交费比例  * 被保人信息个人缴费金额
						accInfoJe=accInfo.getIpsnPayPct()*ipsnPayAmount;
						//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计   * 个人账户交费比例
						accInfoAddJe=addJePersonal * accInfo.getIpsnPayPct();
					}
					//判断当前遍历的对象，是否是当前被保人的最后一个缴费帐号信息
					int size=grpInsured.getAccInfoList().size();
					if(size>1&& accInfo.equals(grpInsured.getAccInfoList().get(size-1))){
						//是最后一条：加费金额 = 被保人核保结论加费总计【addJePersonal】-已入库的加费金额总计【sumAccInfoAddJe】
						accInfoAddJe=addJePersonal-sumAccInfoAddJe;
					}else{
						sumAccInfoAddJe+=accInfoAddJe;
					}
					//应收信息金额 = 加费金额【addAmnt】+ 缴费帐号的缴费金额【accInfoJe】
					inPlnmioRec.setAmnt(BigDecimalUtils.keepPrecision(accInfoAddJe+accInfoJe,2));
					inPlnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
					inPlnmioRec.setMultiPayAccType("I"); 		//账户所属类别	
					plnmioRecList.add(inPlnmioRec);
				}
			}
			if(grpInsured.getAccInfoList()==null && grpInsured.getFeeGrpNo()==null && grpInsured.getIpsnPayAmount()>0){
				//特殊情况需要确认 TODO
				logger.info("特殊情况需要确认!");
			}
		}
		//根据feeGrpNoMap容器，获取保费金额【没缴费帐号，根据收费组产生应收】
		for(Map.Entry<Long,Double> entry : feeGrpNoMap.entrySet()){
			long feeGrpNo = entry.getKey();
			double value = entry.getValue();
			//初始化【应收付记录】数据,为收费组Map【feeGrpNoMap】使用
			PlnmioRec outPlnmioRec = setPlnmioRec(grpInsurAppl);
			outPlnmioRec.setFeeGrpNo(feeGrpNo);	 //收费组号
			for(IpsnPayGrp ipsnPayGrp:grpInsurAppl.getIpsnPayGrpList()){
				if(ipsnPayGrp.getFeeGrpNo() == feeGrpNo){
					outPlnmioRec.setBankCode(ipsnPayGrp.getBankCode());		//银行代码
					outPlnmioRec.setBankAccNo(ipsnPayGrp.getBankaccNo());	//银行账号
					outPlnmioRec.setBankAccName(ipsnPayGrp.getBankaccName());//银行账号
					break;
				}
			}
			outPlnmioRec.setAmnt(BigDecimalUtils.keepPrecision(value,2)); //金额
			outPlnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
			outPlnmioRec.setMultiPayAccType("P");        //账户所属类别
			plnmioRecList.add(outPlnmioRec);
		}
		return plnmioRecList;
	}


	/**
	 * 初始化应收数据
	 * @param grpInsurAppl
	 * @return
	 */
	private PlnmioRec setPlnmioRec(GrpInsurAppl grpInsurAppl) {
		InsurApplOperTrace insurOper = procMioInfoDao.getInsurApplOperTrace(grpInsurAppl.getApplNo());
		PlnmioRec pln = ProcMioInfoUtils.setPlnmioRec(grpInsurAppl, insurOper);
		pln.setPlnmioRecId(plnmioRecDao.getPlnmioRecId());
		return pln;
	}
	
	
	/**
	 * 任务完成调度接口
	 */
	private void doNext(String applNo,String taskId){
		logger.info("执行方法[产生应收数据操作定时任务]：开始执行任务完成接口！【"+taskId+"】【"+applNo+"】");
		// 所有收费完成，调用任务成功
		TaskProcessRequestBO taskProcessRequestBO = new TaskProcessRequestBO();
		taskProcessRequestBO.setBusinessKey(applNo);
		taskProcessRequestBO.setTaskId(taskId);// 业务服务参数中获取的任务ID
		taskProcessServiceDascClient.completeTask(taskProcessRequestBO);// 进行任务完成操作
		logger.info("执行方法[产生应收数据操作定时任务]：任务完成接口执行完成！【"+taskId+"】【"+applNo+"】");
	}
	
	/**
	 * 插入操作轨迹数据
	 * @param businessKey 投保单号
	 */
	private void updateOperTrace(String businessKey){
		//新增一条操作轨迹记录:生成应收
		TraceNode traceNode= new TraceNode();
		traceNode.setPclkBranchNo("");
		traceNode.setPclkNo("");
		traceNode.setPclkName("");
		traceNode.setProcDate(new Date());
		traceNode.setProcStat(NEW_APPL_STATE.GENERATE_RECEIV.getKey());
		boolean isTrue =mongoBaseDao.updateOperTrace(businessKey,traceNode);
		logger.info("执行方法[产生应收数据操作定时任务]:新增一条操作轨迹记录,返回状态【"+isTrue+"】【"+businessKey+"】");
	}
	
    /**
     * 判断险种是否是基金险
     * @param policyList 险种
     * @return 
     * 	包含基金险-true
     *  无基金险 -false
     */
    private boolean checkPolCodeIsFund(List<Policy> policyList) {
    	List<String> polCodes = new ArrayList<>();
        for(Policy policy:policyList){
        	polCodes.add(policy.getPolCode());
        }
        //消息头设置
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        List<JSONObject> polNatureList=polNatureService.getPolNatureInfo(polCodes);
        boolean isTrue = false;
        for(JSONObject json:polNatureList){
        	String isFund =json.getString("isFund");     //是否基金险
        	if("Y".equals(isFund)){
        		isTrue = true;
        		break;
        	}
        }
        return isTrue;
	}
}
