package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.ProPlnmioRecStepService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;
/**
 * 产生分期应收数据服务实现类
 * @author JCC
 * 2016年9月5日 10:37:52
 */
@Service("proPlnmioRecStepService")
public class ProPlnmioRecStepServiceImpl implements ProPlnmioRecStepService{
	/**
	 * 日志管理工具实例.
	 */
	private  Logger logger = LoggerFactory.getLogger(ProPlnmioRecStepServiceImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;  

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	ProcMioInfoDao procMioInfoDao;

	@Override
	public boolean proPlnmioRecStep(GrpInsurAppl grpInsurAppl) {
		String applNo= grpInsurAppl.getApplNo();
		this.logger.info(">>>>>>>>>>>开始产生分期应收数据: " + applNo);
		//判断应收数据是否生成成功
		boolean addFlag = true;
		//收付费相关信息类
		MioPlnmioInfo  mioPln = procMioInfoDao.getMioPlnmioInfo(applNo);	
		//当前投保单号已经产生首期应收的基础上，才可以判断是否需要产生分期应收数据
		if(mioPln == null){
			this.logger.info("此投保单【"+applNo+"】没有对应的首期应收数据,不能分期应收数据！");
		}else{
			int payNum=0;  //根据缴费方式产生获得对应的值
			//moneyinItrvl 缴费方式: M:月缴；Q：季交；H：半年；Y：年；W：趸；X：不定期
			String moneyinItrvl = grpInsurAppl.getPaymentInfo().getMoneyinItrvl();
			if("M".equals(moneyinItrvl)){
				payNum=1;
			}else if("Q".equals(moneyinItrvl)){
				payNum=3;
			}else if("H".equals(moneyinItrvl)){
				payNum=6;
			}
			if(payNum == 0){
				this.logger.error("缴费方式[moneyinItrvl='"+moneyinItrvl+"']没有在规定时间段内[M,Q,H]，不能产生分期应收:【"+applNo+"】 !");
			}else{
				int months;
				//缴费期单位【moneyinDurUnit】 ： 
				String moneyinDurUnit= grpInsurAppl.getPaymentInfo().getMoneyinDurUnit();
				String [] dur=grpInsurAppl.getPaymentInfo().getMoneyinDur().toString().split("\\.");
				int moneyinDur  = Integer.parseInt(dur[0]) ;	//缴费期
				//1、根据缴费期单位得出缴费月数：months
				if ("Y".equals(moneyinDurUnit)){
					months = 12 * moneyinDur;	//Y:缴费年数
				}else if ("W".equals(moneyinDurUnit)){
					months= 1;	//W:一次性缴清 
				}else if ("H".equals(moneyinDurUnit)){
					months= 6 * moneyinDur;		//H:半年缴
				}else if ("Q".equals(moneyinDurUnit)){
					months= 3 * moneyinDur;		//Q:季缴；
				}else if ("M".equals(moneyinDurUnit)){
					months= 1 * moneyinDur; 	// M:按照月缴 
				}else {
					months= 1;
				}
				int i=months/payNum-1; //期数
				this.logger.error("此投保单【"+applNo+"】分为["+i+"]期缴费 !");
				if(i>=1){
					//【团单保费来源： 1.团体账户付款(单位缴费产生一笔总应收)；2.个人账户付款(收费组产生应收)；3.团体个人共同付款(每个被保人产生一笔应收)。】
					String premSource = grpInsurAppl.getPaymentInfo().getPremSource(); 
					//新生成的分期数据List
					List<PlnmioRec> fqList = new ArrayList<>();
					if("1".equals(premSource)){	
						this.logger.info("开始产生【保费来源：团体账户付款】分期应收数据!");	
						List<PlnmioRec> team = getTeamData(grpInsurAppl);
						fqList.addAll(team);						
						this.logger.info("【保费来源：团体账户付款】分期应收数据生成完成["+team.size()+"]!");
					}else if("2".equals(premSource)){		
						this.logger.info("开始产生【保费来源：个人账户付款】分期应收数据!");							
						List<PlnmioRec> personal = getPersonalData(grpInsurAppl);	
						fqList.addAll(personal);						
						this.logger.info("【保费来源：个人账户付款】分期应收数据生成完成["+personal.size()+"]!");
					}else if("3".equals(premSource)){  	
						this.logger.info("开始产生【保费来源：团体个人共同付款】分期应收数据!");							
						List<PlnmioRec> together = getTeamAndPersonalData(grpInsurAppl);
						fqList.addAll(together);						
						this.logger.info("【保费来源：团体个人共同付款】分期应收数据生成完成["+together.size()+"]!");
					}
					try{
						//获取应收集合表中最大的数据
						Long plnmioRecId=procMioInfoDao.getMaxPlnMioRecId();
						//allList=首期应收数据+分期应收数据
						List<PlnmioRec> allList = new ArrayList<>();
						//allList.addAll(mioPln.getPlnmioRecList());
						for(int a=1;a<=i;a++){
							//每一期间隔的月数
							int munths=a*payNum;	 
							//应收付日期[calendar]==生效日期[grpInsurAppl.getInForceDate()] + 月数【munths】
							Date plnmioDate = ProcMioInfoUtils.getMonth(grpInsurAppl.getInForceDate(), munths);
							for(PlnmioRec plnmioRec:fqList){ //TODO
								//重新创建对象
								PlnmioRec newPln = setNewPlnMioRec(plnmioRec);
								plnmioRecId+=1;	
								newPln.setPlnmioRecId(plnmioRecId);	//应收付标识：自动增长序列
								newPln.setPlnmioDate(plnmioDate);	//应收付日期
								newPln.setPlnmioCreateTime(new Date());	 //生成应收记录时间
								String cntrNo = getCntrNo(grpInsurAppl,newPln.getPolCode());
								newPln.setCntrNo(cntrNo); //分保单号
								allList.add(newPln);
							}
						}
						//	mioPln.setPlnmioRecList(allList);
						procMioInfoDao.removeMioPlnmioByApplNo(applNo);	
						mongoBaseDao.insert(mioPln);
						this.logger.info("分期应收数据生成成功："+applNo);
						addFlag=true;
					}catch(RuntimeException rn){
						this.logger.error(">>>>>>>>>>>产生分期应收数据出现异常【"+applNo+"】: " + rn);
						addFlag = false;
					}
				}else{
					this.logger.error("此投保单【"+applNo+"】不需要产生分期数据 ！");
				}
			}
		}
		return addFlag;
	}

	/**
	 * 获取险种分保单号
	 * @param grpInsurAppl
	 * @param polCode 险种代码
	 * @return
	 * 分保单号 cntrNo
	 */
	private String getCntrNo(GrpInsurAppl grpInsurAppl,String polCode) {
		Map<String,String> map = new HashMap<>();
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			map.put(policy.getPolCode(), policy.getCntrNo());
		}
		return map.get(polCode)==null?"":map.get(polCode);
	}

	/**
	 * 生成团体分期应收数据  TODO
	 * @param grpInsurAppl 团体出单基本信息
	 * @param i 分期期数
	 * @return
	 */
	private List<PlnmioRec> getTeamData(GrpInsurAppl grpInsurAppl) {
		String applNo=grpInsurAppl.getApplNo();
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//1、判断是否有组织架构树数据
		if(grpInsurAppl.getOrgTreeList() == null || grpInsurAppl.getOrgTreeList().isEmpty()){ 
			//1.1、无组织机构数据时，产生分期应收逻辑
			plnmioRecList = getMioPlnmioInfo(grpInsurAppl);
		}else{
			//1.2、有组织机构数据，产生分期应收逻辑
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
				//1.2.1、 若组织架构树中信息都不需要缴费，则按照无组织架构树产生分期应收数据
				plnmioRecList = getMioPlnmioInfo(grpInsurAppl);
			}else{
				//1.2.2、 组织架构树中需要缴费的数据，产生分期应收逻辑

				//获取需要缴费的组织架构树下，不需要缴费的子节点信息
				Map<String,List<String>> levelMap = ProcMioInfoUtils.getChildTree(orgTreeMap,noIsPaidMap);

				for(OrgTree orgTree :orgTreeList){
					String levelCode = orgTree.getLevelCode();
					double sumPolAddJe=0; //险种加费累计值，用于计算最后一比险种加费金额
					List<Policy> policyList = grpInsurAppl.getApplState().getPolicyList();
					for(Policy policy :policyList){
						//初始化【应收付记录】数据
						PlnmioRec plnmioRec = setPlnmioRec(grpInsurAppl);
						plnmioRec.setLevelCode(levelCode);	//组织层次号
						plnmioRec.setBankCode(orgTree.getBankCode());	//银行代码
						plnmioRec.setBankAccNo(orgTree.getBankaccNo());	//银行账号
						plnmioRec.setBankAccName(orgTree.getBankaccName());//银行账号
						plnmioRec.setCntrNo(policy.getCntrNo()); //分保单号
						double ipsnAddJe =0;	//被保人加费金额
						double polDwAddJe=0;	//该险种加费金额：	

						//根据投保单号，组织层次号，获取该组织架构树下有效被保人信息的险种保费数据
						Map<String,Double> subStateMap = new HashMap<>();

						//获取层次代码下的有效被保人数据
						List<String> levelList = new ArrayList<>();
						levelList.add(levelCode);
						if(levelMap.containsKey(levelCode)){
							logger.info(">>>>>>>缴费节点【levelCode】:"+levelCode+"下，不需要缴费的子节点："+levelMap.get(levelCode));
							levelList.addAll(levelMap.get(levelCode));
						}
						List<GrpInsured> grpInsuredList = procMioInfoDao.getGrpInsuredList(grpInsurAppl.getApplNo(), levelList);
						if(grpInsuredList.isEmpty()){
							logger.info("该组织架构树【"+levelCode+"】下无有效被保人信息【投保单号 "+applNo+"】");
						}else{
							logger.info(">>>>>>>根据【投保单号 "+applNo+"】，【节点 "+levelCode+"】，查询到【被保人数"+grpInsuredList.size()+"】");
							for(GrpInsured grpInsured :grpInsuredList){
								//被保人加额保费信息
								ipsnAddJe+=procMioInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(),policy.getPolCode(),grpInsured.getIpsnNo());
								for(SubState subState:grpInsured.getSubStateList()){
									String polCode=subState.getPolCode().substring(0,3); //险种代码
									double premium=subState.getPremium(); //险种保费
									if(subStateMap.containsKey(polCode)){
										subStateMap.put(polCode, premium+subStateMap.get(polCode));
									}else{
										subStateMap.put(polCode, premium);
									}
								}
							}
							//险种总加费金额
							double polJe=procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(),policy.getPolCode());
							//判断当前遍历的是否是List的最后一条数据

							if(policyList.size()>1 && policy.equals(policyList.get(policyList.size()-1)) ){
								//险种加费金额=险种加费金额
								polDwAddJe=(polJe-sumPolAddJe);
							}else{
								//险种加费金额=险种加费金额
								double premium= policy.getPremium(); //当前险种的保费
								double invalidPolicyJe = procMioInfoDao.getInvalidPolicyMoney(grpInsurAppl.getApplNo(),policy.getPolCode()); //无效的险种保费
								double bl = subStateMap.get(policy.getPolCode())/(premium-invalidPolicyJe);
								double levelAddJe = polJe*bl;
								sumPolAddJe+=levelAddJe;
								polDwAddJe=levelAddJe;
							}
							//应收金额=保单单个险种保费【sumPolDwPayJe】 + 险种加费【polDwAddJe】+被保人加费金额【ipsnAddJe】-无效被保人单位缴费金额总计【invalidIpsn】
							double invalidIpsn = procMioInfoDao.getInvalidIpsnMoney(grpInsurAppl.getApplNo(),levelList);
							//	plnmioRec.setAmnt(ProcMioInfoUtils.keepPrecision(orgTree.getNodePayAmnt()+polDwAddJe+ipsnAddJe-invalidIpsn,2));
							plnmioRecList.add(plnmioRec);
						}
					}
				}
			}
		}
		return plnmioRecList;
	}

	/**
	 * 生成分期应收数据:组织架构树 团单特有无组织架构树，生成应收数据
	 * @param grpInsurAppl
	 * @return TODO
	 */
	private List<PlnmioRec> getMioPlnmioInfo(GrpInsurAppl grpInsurAppl) {
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//无组织机构树数据时，金额:取投保要约中险种产生分期数据
		for(Policy policy :grpInsurAppl.getApplState().getPolicyList()){
			//初始化【应收付记录】数据
			PlnmioRec plnmioRec = setPlnmioRec(grpInsurAppl);
			plnmioRec.setPolCode(policy.getPolCode());	//险种代码
			plnmioRec.setCntrNo(policy.getCntrNo()); //分保单号
			plnmioRec.setMioCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());	//领款人/交款人客户号
			plnmioRec.setMioCustName(grpInsurAppl.getGrpHolderInfo().getGrpName());	//领款人/交款人姓名
			plnmioRec.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());		//银行代码
			plnmioRec.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());	//银行账号
			plnmioRec.setBankAccName(grpInsurAppl.getPaymentInfo().getBankAccName());//银行账号
			//保人核保结论加费金额=无组织架构树的被保人核保结论加费金额总计
			double addIpsnJe=procMioInfoDao.getPersonAddMoneyByPolCode(grpInsurAppl.getApplNo(),policy.getPolCode() );
			double ipsnAddje = addIpsnJe;
			//险种加费金额 
			double polAaddJe = procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(),policy.getPolCode());
			double polAddAmnt= polAaddJe;
			//金额:险种的实际保费【policy.getPremium()】 +险种核保结论中的加费金额【polAddAmnt】+被保人加费金额总计【addAmntPerson】
			//	plnmioRec.setAmnt(ProcMioInfoUtils.keepPrecision(policy.getPremium()+polAddAmnt+ipsnAddje,2));
			plnmioRecList.add(plnmioRec);
		}
		return plnmioRecList;
	}

	/**
	 * 生成被保人分期应收数据
	 * @param grpInsurAppl
	 * @param plnmioRec
	 * @return
	 */
	private List<PlnmioRec> getPersonalData(GrpInsurAppl grpInsurAppl) {
		//1、应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		//2、根据投保单号(applNo)查询被保人清单信息
		List<GrpInsured> grsList=procMioInfoDao.getGrpInsuredList(grpInsurAppl.getApplNo(),null);
		//3、此容器用于存放【key为收费组号+险种代码】的map
		Map<String,Double> feeGrpNoMap = new HashMap<>();
		//4、初始化【应收付记录】数据,为收费组Map【bigMap】使用
		PlnmioRec outPlnmioRec;
		//6、循环遍历 - 被保人信息集合
		for(GrpInsured grpInsured:grsList){	
			for(SubState subState: grpInsured.getSubStateList()){
				String polCode = subState.getPolCode().substring(0,3);
				//6.3.1、当期被保人加费金额
				double ipsnje=procMioInfoDao.getPersonAddMoney(grpInsured.getApplNo(), polCode, grpInsured.getIpsnNo());
				double perAddAmnt =ipsnje;
				//6.3.2、判断缴费账号GrpInsured.accInfoList是否有数据
				if(grpInsured.getAccInfoList()==null || grpInsured.getAccInfoList().isEmpty()){
					//1、缴费帐号无数据时，根据收费组产生分期应收数据
					//被保人的收费组号
					long feeGrpNo = grpInsured.getFeeGrpNo();	
					double ipsnPayAmount = grpInsured.getIpsnPayAmount(); //个人缴费金额
					//联合主键：收费组+#+险种代码
					String key = feeGrpNo+"#"+polCode;
					//险种代码及其对应的应缴费金额【个人缴费金额+个人加费金额】存入Map中
					double je=ipsnPayAmount+perAddAmnt;
					if(feeGrpNoMap.containsKey(key)){
						feeGrpNoMap.put(key, feeGrpNoMap.get(key)+je);
					}else{
						feeGrpNoMap.put(key, je);
					}
				}else{
					//1、缴费帐号有数据时，根据缴费帐号产生分期应收数据
					//循环遍历   - 缴费账号
					double sumAccInfoAddJe=0; //缴费帐号的加费金额累计
					for(AccInfo accInfo: grpInsured.getAccInfoList()){
						//初始化【应收付记录】数据  遍历缴费帐号内部使用
						PlnmioRec inPlnmioRec = setPlnmioRec(grpInsurAppl);
						inPlnmioRec.setIpsnNo(grpInsured.getIpsnNo());	//被保人序号
						inPlnmioRec.setMioCustNo(grpInsured.getIpsnCustNo());	//领款人/交款人客户号
						inPlnmioRec.setMioCustName(grpInsured.getIpsnName());	//领款人/交款人姓名
						double accInfoJe =0 ;	//缴费帐号的缴费金额
						double accInfoAddJe=0;  //缴费帐号的加费金额

						//若相等，则说明此时遍历的是最后一条缴费帐号信息,直接用要约信息里面的保费减去已累计的总应收金额。
						int size=grpInsured.getAccInfoList().size();
						if(size>1 && accInfo.equals(grpInsured.getAccInfoList().get(size-1))){
							accInfoAddJe=perAddAmnt-sumAccInfoAddJe;
						}else{
							if(accInfo.getIpsnPayPct()!=null && accInfo.getIpsnPayAmnt()>0){
								//1、当缴费帐号的【个人账户交费比例】没有值，【个人扣款金额】有值时:
								//需要计算出【缴费帐号的缴费比例】：缴费帐号的个人扣款金额【AccInfo.ipsnPayAmnt】/被保人信息个人缴费金额【GrpInsured.ipsnPayAmount】
								double accInfoBL=accInfo.getIpsnPayAmnt()/grpInsured.getIpsnPayAmount();
								//需要计算出【缴费帐号的缴费金额】：
								accInfoJe=accInfo.getIpsnPayAmnt();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计*缴费帐号的缴费比例
								accInfoAddJe =perAddAmnt * accInfoBL;
							}else if(accInfo.getIpsnPayAmnt()!=null && accInfo.getIpsnPayPct()>0){
								//2、当缴费帐号的【个人账户交费比例】有值，【个人扣款金额】没有值时:
								//需要计算出【缴费帐号的缴费金额】：个人账户交费比例*被保人信息个人缴费金额
								accInfoJe=accInfo.getIpsnPayPct()*grpInsured.getIpsnPayAmount();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计   * 个人账户交费比例
								accInfoAddJe= perAddAmnt * accInfo.getIpsnPayPct();
							}
							sumAccInfoAddJe+=accInfoAddJe;
						}

						inPlnmioRec.setPolCode(polCode);	//险种代码
						inPlnmioRec.setBankCode(accInfo.getBankCode());	  //银行代码
						inPlnmioRec.setBankAccNo(accInfo.getBankAccNo()); //银行账号
						inPlnmioRec.setBankAccName(accInfo.getBankAccName());//银行账号
						//应收信息金额 = 加费金额【addAmnt】+ 缴费帐号的缴费金额【accInfoJe】
						//inPlnmioRec.setAmnt(ProcMioInfoUtils.keepPrecision(accInfoAddJe+accInfoJe,2));
						plnmioRecList.add(inPlnmioRec);
					}
				}
			}

			if(grpInsured.getAccInfoList()==null && grpInsured.getFeeGrpNo()==null && grpInsured.getIpsnPayAmount()>0){
				//TODO 这种情况，需要与业务协商
				logger.info("数据出现特殊情况！");
			}
		}
		//7、根据feeGrpNoMap容器，获取保费金额		
		for(Map.Entry<String,Double> entryIn : feeGrpNoMap.entrySet() ){
			//截取收费组
			String[] midKey = entryIn.getKey().split("#");
			long feeGrpNoIn =Long.valueOf(midKey[0]); //收费组
			String polCodeIn=midKey[1];		//险种代码
			double midVal = entryIn.getValue(); //缴费金额
			outPlnmioRec=setPlnmioRec(grpInsurAppl);	
			Map<String,Object> map = new HashMap<>();
			map.put("feeGrpNo",feeGrpNoIn);
			//根据map里面的键(也就是收费组号)查询 团体收费组grpInsurAppl.ipsnPayGrpList的账户信息产生应收
			IpsnPayGrp  ipsnPayGrp = (IpsnPayGrp) mongoBaseDao.findOne(IpsnPayGrp.class, map);
			outPlnmioRec.setPolCode(polCodeIn);	//险种代码
			outPlnmioRec.setFeeGrpNo(feeGrpNoIn);	//收费组号
			outPlnmioRec.setBankCode(ipsnPayGrp.getBankCode());	  //银行代码
			outPlnmioRec.setBankAccNo(ipsnPayGrp.getBankaccNo()); //银行账号
			outPlnmioRec.setBankAccName(ipsnPayGrp.getBankaccName());//银行账号
			//outPlnmioRec.setAmnt(ProcMioInfoUtils.keepPrecision(midVal, 2));//金额
			plnmioRecList.add(outPlnmioRec);
		}
		return plnmioRecList;
	}


	/**
	 * 生成团体，个人共同缴费 模式  产生分期应收
	 * @param grpInsurAppl
	 * @param i 分期期数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PlnmioRec> getTeamAndPersonalData(GrpInsurAppl grpInsurAppl) {
		String applNo = grpInsurAppl.getApplNo();
		//应收数据集合
		List<PlnmioRec> plnmioRecList = new ArrayList<>();	
		boolean orgTreeNotNull =true;
		//判断是否有组织架构树数据 :true有组织架构树    false 没组织架构树
		if(grpInsurAppl.getOrgTreeList() == null || grpInsurAppl.getOrgTreeList().isEmpty()){ 
			orgTreeNotNull=false;
		}

		//1、orgTreeMap:保存组织架构树代码以及对应的险种金额
		Map<String,Map<String, Double>> orgTreeMap = new HashMap<>();
		//2、noOrgTreeMap:没组织架构树时，单位缴纳一笔总保费，险种代码以及险种对应保费分别作为键值对
		Map<String,Double> noOrgTreeMap = new HashMap<>();
		//3、accInfoMap:没个人账号时用保存收费组号以及对应的险种金额
		Map<Long,Object> accInfoMap = new HashMap<>();
		//4、codeMap:险种代码对应的险种加费金额
		Map<String,Double> codeMap = new HashMap<>();
		//5、单位险种加费：通过遍历险种信息，累加险种加费
		for(Policy policy :grpInsurAppl.getApplState().getPolicyList()){
			double polAddje = procMioInfoDao.getPolicyAddMoney(applNo, policy.getPolCode());
			codeMap.put(policy.getPolCode(), polAddje);
		}
		//6、根据投保单号(applNo)查询被保人清单信息
		List<GrpInsured> grpInsuredList=procMioInfoDao.getGrpInsuredList(grpInsurAppl.getApplNo(),null);
		for(GrpInsured grpInsured: grpInsuredList){
			//1、计算个人、单位缴费总计:sumJe=个人缴费金额【ipsnPayAmount】+单位交费金额【grpPayAmount】
			double sumJe=  grpInsured.getIpsnPayAmount()+grpInsured.getGrpPayAmount();
			//2、个人缴费比例：个人缴费金额【ipsnPayAmount】/sumJe
			double personalBL=grpInsured.getIpsnPayAmount()/sumJe;
			//3、单位缴费比例：单位缴费金额grpPayAmount/sumJe
			double teamBL=grpInsured.getGrpPayAmount()/sumJe;
			//4、inPolCodeMap 里面存放险种代码以及金额，该map作为orgTreeMap的值
			Map<String,Double> inPolCodeMap = new HashMap<>();
			//5、里面存放险种代码以及金额，该map作为accInfoMap的值
			Map<String,Double> noAccInfoMap	= new HashMap<>();
			//循环遍历 - 子邀约信息
			for(SubState subState:grpInsured.getSubStateList()){
				//险种代码
				String polCode = subState.getPolCode().substring(0,3);
				//取该险种保费【premium】
				double premium = subState.getPremium();
				//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓单位部分保费计算开始↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				//计算该险种单位应缴纳金额: 单位缴费比例【temBL】*险种保费【premium】 = 单位应缴纳金额【payJe】
				double teamPayJe= teamBL * premium;
				//无组织架构树
				if(noOrgTreeMap.containsKey(polCode)){
					double sum=noOrgTreeMap.get(polCode)+teamPayJe;
					noOrgTreeMap.put(polCode,sum);
				}else{
					noOrgTreeMap.put(polCode,teamPayJe);
				}
				if(orgTreeNotNull){ 		//有组织架构树
					//组织层次代码:levelCode
					String levelCode=grpInsured.getLevelCode();
					if(orgTreeMap.containsKey(levelCode)){
						inPolCodeMap = (Map<String, Double>) orgTreeMap.get(levelCode);
						if(inPolCodeMap.containsKey(polCode)){
							inPolCodeMap.put(polCode, inPolCodeMap.get(polCode)+teamPayJe);
						}else{
							inPolCodeMap.put(polCode, teamPayJe);
						}
					}else{
						inPolCodeMap.put(polCode,teamPayJe);
					}
					orgTreeMap.put(levelCode,inPolCodeMap);
				}
				//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑单位部分保费计算结束↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑				
				//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓个人部分保费计算开始↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
				//个人加费金额
				double perAddAmnt=procMioInfoDao.getPersonAddMoney(grpInsured.getApplNo(), polCode, grpInsured.getIpsnNo());
				//个人在该险种应缴保费【personalJe】 = 险种保费【premium】*个人缴费比例【personalBL】+个人加费金额【personalAddFeeAmnt】
				double personalJe=personalBL*premium+perAddAmnt;
				//判断被保人是否有缴费帐号
				if(grpInsured.getAccInfoList() == null || grpInsured.getAccInfoList().isEmpty() ){  
					//无缴费帐号则根据收费组号产生分期应收
					Long feeGrpNo = grpInsured.getFeeGrpNo();	//收费组号
					if(accInfoMap.containsKey(feeGrpNo)){
						noAccInfoMap=(Map<String, Double>) accInfoMap.get(feeGrpNo);
						if(noAccInfoMap.containsKey(polCode)){
							noAccInfoMap.put(polCode,noAccInfoMap.get(polCode)+personalJe);
						}else{
							noAccInfoMap.put(polCode,personalJe);
						}
					}else{
						noAccInfoMap.put(polCode,personalJe);
					}
					//将最新的数据放入accInfoMap中
					accInfoMap.put(feeGrpNo,noAccInfoMap);
				}else{
					//有帐号时：根据个人缴费账号产生应收

					double sumAccInfoAddJe=0; //缴费帐号的加费金额累计
					for(AccInfo accInfo:grpInsured.getAccInfoList()){
						double accInfoJe =0 ;	//缴费帐号的缴费金额
						double accInfoAddJe=0;  //缴费帐号的加费金额

						int size=grpInsured.getAccInfoList().size();
						if(size>1 && accInfo.equals(grpInsured.getAccInfoList().get(size-1))){
							accInfoAddJe=perAddAmnt-sumAccInfoAddJe;
						}else{
							if(accInfo.getIpsnPayPct()!=null && accInfo.getIpsnPayAmnt()>0){
								//1、当缴费帐号的【个人账户交费比例】没有值，【个人扣款金额】有值时:
								//需要计算出【缴费帐号的缴费比例】：缴费帐号的个人扣款金额【AccInfo.ipsnPayAmnt】/被保人信息个人缴费金额【GrpInsured.ipsnPayAmount】
								double accInfoBL=accInfo.getIpsnPayAmnt()/grpInsured.getIpsnPayAmount();
								//需要计算出【缴费帐号的缴费金额】：
								accInfoJe=accInfo.getIpsnPayAmnt();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计*缴费帐号的缴费比例
								accInfoAddJe = perAddAmnt * accInfoBL;
							}else if(accInfo.getIpsnPayAmnt()!=null && accInfo.getIpsnPayPct()>0){
								//2、当缴费帐号的【个人账户交费比例】有值，【个人扣款金额】没有值时:
								//需要计算出【缴费帐号的缴费金额】：个人账户交费比例*被保人信息个人缴费金额
								accInfoJe=accInfo.getIpsnPayPct()*grpInsured.getIpsnPayAmount();
								//计算出【当前缴费帐号的加费金额】= 被保人核保结论加费金额总计   * 个人账户交费比例
								accInfoAddJe=perAddAmnt * accInfo.getIpsnPayPct();
							}
							sumAccInfoAddJe+=accInfoAddJe;
						}

						//初始化【应收付记录】数据  遍历缴费帐号内部使用
						PlnmioRec inPlnmioRec = setPlnmioRec(grpInsurAppl);
						inPlnmioRec.setPolCode(polCode);
						inPlnmioRec.setBankCode(accInfo.getBankCode());		//银行代码
						inPlnmioRec.setBankAccNo(accInfo.getBankAccNo());	//银行帐号
						inPlnmioRec.setBankAccName(accInfo.getBankAccName());//帐户所有人名称
						inPlnmioRec.setIpsnNo(grpInsured.getIpsnNo());	//被保人序号
						//应收信息金额 = 加费金额【addAmnt】+ 缴费帐号的缴费金额【accInfoJe】
						//inPlnmioRec.setAmnt(accInfoAddJe+accInfoJe);
						plnmioRecList.add(inPlnmioRec);
					}
				}
				//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑个人部分保费计算结束↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
			}
		}

		//获取所有缴费节点下的无需缴费的子节点
		Map<String,List<String>> levelMap = getLevelMap(grpInsurAppl);
		//统计缴费节点及其无需缴费子节点的险种和险种保费数据
		Map<String,Map<String,Double>> sumPolMap = new HashMap<>();
		for(Entry<String, List<String>> entryIn :levelMap.entrySet()){
			String level = entryIn.getKey();  //父节点
			List<String> entryList = entryIn.getValue(); //无需缴费的子节点
			//noPaidList 无需缴费的层次代码
			List<String> noPaidList = new ArrayList<>();
			noPaidList.add(level);
			noPaidList.addAll(entryList);

			Map<String, Double> noPaidMap = new HashMap<>();
			for(String lev: noPaidList){
				logger.info("获取缴费节点及其无需缴费的子节点的险种保费数据【levelCode:"+lev+"】");
				if(orgTreeMap.get(lev) == null){
					logger.info("当前组织架构树节点【levelCode:"+lev+"】下，无有效被保人数据，请查看！");
				}else{
					Map<String, Double> inMap = orgTreeMap.get(lev);
					for(Map.Entry<String,Double> inMapEntry :inMap.entrySet()){
						String inPolCode = inMapEntry.getKey();     //险种代码
						Double inPolJe = inMapEntry.getValue(); 	//险种投保金额
						if(noPaidMap.containsKey(inPolCode)){
							noPaidMap.put(inPolCode, inPolJe+noPaidMap.get(inPolCode));
						}else{
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
		List<PlnmioRec> farPlnMioList = getPlnMioRec(grpInsurAppl,sumPolMap,noOrgTreeMap,codeMap);
		List<PlnmioRec> isPainPlnList = getPlnMioRec(grpInsurAppl,orgTreeMap,noOrgTreeMap,codeMap);
		plnmioRecList.addAll(farPlnMioList);
		plnmioRecList.addAll(isPainPlnList);
		//没组织架构树map:直接去缴费相关【grpInsurAppl.getPaymentInfo()】的银行账户信息产生应收
		if(!orgTreeNotNull){
			for(Map.Entry<String,Double> entry :noOrgTreeMap.entrySet()){
				String key = entry.getKey();
				Double value = entry.getValue();
				//初始化【应收付记录】数据 
				PlnmioRec inPlnmioRecNoOrgMap = setPlnmioRec(grpInsurAppl);
				inPlnmioRecNoOrgMap.setIpsnNo(0L);	//被保人序号
				inPlnmioRecNoOrgMap.setBankCode(grpInsurAppl.getPaymentInfo().getBankCode());	//银行代码
				inPlnmioRecNoOrgMap.setBankAccNo(grpInsurAppl.getPaymentInfo().getBankAccNo());	//银行帐号
				inPlnmioRecNoOrgMap.setBankAccName(grpInsurAppl.getPaymentInfo().getBankAccName());	//帐户所有人名称
				inPlnmioRecNoOrgMap.setPolCode(key);			//险种代码
				//inPlnmioRecNoOrgMap.setAmnt(ProcMioInfoUtils.keepPrecision(value+codeMap.get(key),2));
				plnmioRecList.add(inPlnmioRecNoOrgMap);
			}
		}
		//没个人账号时map:取团体收费组【IpsnPayGrp】中的银行账户信息产生应收
		for(Map.Entry<Long,Object> entry :accInfoMap.entrySet()){
			long key = entry.getKey();
			Object value = entry.getValue();
			//inMap 里面存放险种代码以及金额，该map作为orgTreeMap的值
			Map<String,Double> inMap = (Map<String, Double>) value;
			for(Entry<String,Double> inEntry:inMap.entrySet()){
				String inKey = inEntry.getKey();
				double inVal = inEntry.getValue();
				//初始化【应收付记录】数据 
				PlnmioRec inPlnmioRecAccMap = setPlnmioRec(grpInsurAppl);
				inPlnmioRecAccMap.setFeeGrpNo(key);	//收费组号
				for(IpsnPayGrp ipsnPayGrp:grpInsurAppl.getIpsnPayGrpList()){
					if(ipsnPayGrp.getFeeGrpNo().equals(value)){
						inPlnmioRecAccMap.setBankCode(ipsnPayGrp.getBankCode());	//银行代码
						inPlnmioRecAccMap.setBankAccNo(ipsnPayGrp.getBankaccNo());	//银行帐号
						inPlnmioRecAccMap.setBankAccName(ipsnPayGrp.getBankaccName());//帐户所有人名称
						break;
					}
				}
				inPlnmioRecAccMap.setPolCode(inKey);//险种代码
				//inPlnmioRecAccMap.setAmnt(ProcMioInfoUtils.keepPrecision(inVal,2));	//金额
				plnmioRecList.add(inPlnmioRecAccMap);
			}
		}
		return plnmioRecList;
	}

	/**
	 * 获取单位部分应收数据 
	 * @param grpInsurAppl 投保单号
	 * @param sumPolMap  需要生成应收的Map
	 * @param noOrgTreeMap 险种总保费
	 * @param codeMap 险种加费
	 * @return
	 */
	private List<PlnmioRec> getPlnMioRec(GrpInsurAppl grpInsurAppl,Map<String, Map<String, Double>> sumPolMap,
			Map<String,Double> noOrgTreeMap,Map<String,Double> codeMap ) {
		List<PlnmioRec> plnmioRecList = new ArrayList<>();
		for(Map.Entry<String,Map<String, Double>> levelEntry :sumPolMap.entrySet()){
			String key = levelEntry.getKey();  		//组织层次代码	
			Map<String, Double> inMap = levelEntry.getValue();	//险种及其投保金额
			for(Map.Entry<String,Double> entryIn :inMap.entrySet()){
				String inPolCode = entryIn.getKey();     //险种代码
				Double inPolJe = entryIn.getValue(); 	 //险种投保金额
				double jfZB = inPolJe/noOrgTreeMap.get(inPolCode); //险种缴费占比
				//初始化【应收付记录】数据 
				PlnmioRec inPlnmioRecOrgMap = setPlnmioRec(grpInsurAppl);
				inPlnmioRecOrgMap.setLevelCode(key);	//组织层次代码
				for(OrgTree orgTree:grpInsurAppl.getOrgTreeList()){
					if(StringUtils.equals(key,orgTree.getLevelCode())){
						inPlnmioRecOrgMap.setBankCode(orgTree.getBankCode());  //银行代码
						inPlnmioRecOrgMap.setBankAccNo(orgTree.getBankaccNo());//银行帐号
						inPlnmioRecOrgMap.setBankAccName(orgTree.getBankaccName());//帐户所有人名称
						break;
					}
				}
				inPlnmioRecOrgMap.setPolCode(inPolCode);			//险种代码
				//	inPlnmioRecOrgMap.setAmnt(ProcMioInfoUtils.keepPrecision(inPolJe+codeMap.get(inPolCode)*jfZB,2));	//金额
				plnmioRecList.add(inPlnmioRecOrgMap);
			}
		}
		return plnmioRecList;
	}

	/**
	 * 获取所有缴费节点下无需缴费的子节点
	 * @param grpInsurAppl
	 * @return
	 */
	private Map<String, List<String>> getLevelMap(GrpInsurAppl grpInsurAppl) {
		//orgTreeMap：存放所有组织架构树数据
		Map<String,OrgTree> allOrgTreeMap = new HashMap<>();
		//noIsPaidMap:存放不需要缴费的组织架构树数据
		Map<String,OrgTree> noIsPaidMap = new HashMap<>();		
		for(OrgTree orgTree :grpInsurAppl.getOrgTreeList()){
			//判断是否需要缴费:ifPay【Y：是；N：否。】
			allOrgTreeMap.put(orgTree.getLevelCode(), orgTree);
			if("N".equals(orgTree.getIsPaid()) && !"Y".equals(orgTree.getIsRoot())){
				noIsPaidMap.put(orgTree.getLevelCode(),orgTree);
			}
		}
		//获取需要缴费的组织架构树下，不需要缴费的子节点信息
		Map<String,List<String>> levelMap = ProcMioInfoUtils.getChildTree(allOrgTreeMap,noIsPaidMap);
		return levelMap;
	}

	/**
	 * 初始化分期应收数据
	 * @param grpInsurAppl
	 * @return
	 */
	private PlnmioRec setPlnmioRec(GrpInsurAppl grpInsurAppl) {
		InsurApplOperTrace insurOper = procMioInfoDao.getInsurApplOperTrace(grpInsurAppl.getApplNo());
		PlnmioRec pln = ProcMioInfoUtils.setPlnmioRec(grpInsurAppl, insurOper);
		pln.setCgNo(grpInsurAppl.getCgNo());
		//根据前面计算出的应收日期等计算保费缴费宽限期(从oracle 表中查询宽限日期)
		pln.setPremDeadlineDate(grpInsurAppl.getInForceDate());
		pln.setMtnItemCode("29");  	//批改保全项目:29	
		pln.setMioItemCode("PS");	//收付项目代码:分期应收 PS
		pln.setRemark("分期应收保费");	//备注
		return pln;
	}

	/**
	 * 重新给plnmioRec赋值
	 * @param plnmioRec
	 * @return
	 */
	private PlnmioRec setNewPlnMioRec(PlnmioRec oldPln) {
		PlnmioRec newPln = new PlnmioRec();

		newPln.setCntrType(oldPln.getCntrType());	//合同类型
		newPln.setSgNo(oldPln.getSgNo());	 		//汇缴事件号
		newPln.setArcBranchNo(oldPln.getArcBranchNo());//(路由)归档机构
		newPln.setCgNo(oldPln.getCgNo());		//合同组号
		newPln.setPremDeadlineDate(oldPln.getPremDeadlineDate()); //保费缴费宽限截止日期
		newPln.setPolCode(oldPln.getPolCode());	//险种代码
		newPln.setCntrNo(oldPln.getCntrNo()); 	//保单号/投保单号/帐号
		newPln.setCurrencyCode(oldPln.getCurrencyCode());	//保单币种
		newPln.setMtnId(oldPln.getMtnId());		//保全批改流水号
		newPln.setMtnItemCode(oldPln.getMtnItemCode()); //批改保全项目	

		newPln.setIpsnNo(oldPln.getIpsnNo());		    //被保人序号
		newPln.setLevelCode(oldPln.getLevelCode());		//组织层次代码
		newPln.setFeeGrpNo(oldPln.getFeeGrpNo());		//收费组号
		newPln.setMioCustNo(oldPln.getMioCustNo());		//领款人/交款人客户号 
		newPln.setMioCustName(oldPln.getMioCustName());	//领款人/交款人姓名
		newPln.setMioClass(oldPln.getMioClass());	 	//收付类型
		newPln.setPlnmioDate(oldPln.getPlnmioDate());	//应收付日期：暂时取保单生效日期
		newPln.setSignYear(oldPln.getSignYear());	 	//(路由)签单年度
		newPln.setMioItemCode(oldPln.getMioItemCode());	//收付项目代码:首期暂收 FA
		newPln.setMioType(oldPln.getMioType());			//收付款形式代码		
		newPln.setMgrBranchNo(oldPln.getMgrBranchNo()); 	//管理机构
		newPln.setSalesChannel(oldPln.getSalesChannel());	//销售渠道		
		newPln.setSalesBranchNo(oldPln.getSalesBranchNo());	//销售机构号

		newPln.setSalesNo(oldPln.getSalesNo());	//销售员号		
		newPln.setAmnt(oldPln.getAmnt());		//金额 	
		newPln.setLockFlag(oldPln.getLockFlag());		//锁标志:默认为0  银行转账在途则位1
		newPln.setBankCode(oldPln.getBankCode());		//银行代码 【注：根据不同的保费来源获取对应的银行信息】
		newPln.setBankAccName(oldPln.getBankAccName()); //帐户所有人名称
		newPln.setAccCustIdType(oldPln.getAccCustIdType()); //帐户所有人证件类别
		//newPln.setBankaccIdNo(oldPln.getBankaccIdNo());     //帐户所有人证件号
		newPln.setBankAccNo(oldPln.getBankAccNo());			//银行账号 【注：根据不同的保费来源获取对应的银行信息】

		newPln.setHoldFlag(oldPln.getHoldFlag());			//待转帐标志:默认为0
		newPln.setVoucherNo(oldPln.getVoucherNo());			//核销凭证号
		newPln.setFinPlnmioDate(oldPln.getFinPlnmioDate());	//财务应收付日期
		newPln.setClearingMioTxNo(oldPln.getClearingMioTxNo()); //清算交易号(收据号)
		newPln.setMioProcFlag(oldPln.getMioProcFlag());		//是否收付处理标记:默认是'1'
		newPln.setRouterNo(oldPln.getRouterNo()); 			//路由号:默认是‘0’
		newPln.setAccId(oldPln.getAccId());					//关联帐户标识:默认0
		newPln.setRemark(oldPln.getRemark());				//备注
		newPln.setProcStat(oldPln.getProcStat()); 			//应收状态：N 未收 ,D 作废 ,S 实收，T 在途
		newPln.setTransStat(oldPln.getTransStat());	     	 //转账状态：U空  N新建 ,W 抽取 ,S 成功 ,F 失败
		return newPln;
	}
}
