package com.newcore.orbps.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcMioInfoDao;
import com.newcore.orbps.models.banktrans.MioLog;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.banktrans.PlnmioRec;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.ProcCommonPlnmioRecRecordService;
import com.newcore.orbpsutils.bussiness.ProcMioInfoUtils;


/**
 *  产生首席共保应付功能服务实现类
 * @author JCC
 * 2016年11月19日 14:22:53
 */

@Service("procCommonPlnmioRecRecordService")
public class ProcCommonPlnmioRecRecordServiceImpl implements ProcCommonPlnmioRecRecordService {

	/**
     * 日志管理工具实例.
     */
    private  Logger logger = LoggerFactory.getLogger(ProcCommonPlnmioRecRecordServiceImpl.class);
    
	@Autowired
	private MongoBaseDao mongoBaseDao;
	@Autowired
	ProcMioInfoDao procMioInfoDao;
	@Autowired
	JdbcOperations jdbcTemplate;
	
	@Override
	public boolean procCommonPlnmioRecRecord(GrpInsurAppl grpInsurAppl) {
		logger.info("开始执行产生首席共保应付功能服务！");
		//1. 根据保单中协议号，获取共保基本信息
		String applNo = grpInsurAppl.getApplNo();
		Map<String,Object> comMap = new HashMap<>();
		comMap.put("agreementNo", grpInsurAppl.getAgreementNo());
		CommonAgreement  comAg =  (CommonAgreement) mongoBaseDao.findOne(CommonAgreement.class, comMap);
		if(comAg != null){
			//获取应付最大值
			Long plnmioRecId = procMioInfoDao.getMaxPlnMioRecId();
			//实收数据最大值
			Long mioLogId = procMioInfoDao.getMixMioLogId();
			logger.info("执行产生首席共保应付功能服务:[应付最大值="+plnmioRecId+",实收数据最大值="+mioLogId+"]");
			//应付数据集合:GF/Q
			List<PlnmioRec> plnmioRecList = new ArrayList<>();
			//非主承保人:GF/S实收数据集合
			List<MioLog> mioLogNList = new ArrayList<>();
			//承保人:FA/S实收数据集合
			List<MioLog> mioLogMList = new ArrayList<>();
			//PS/S实收数据集合
			List<MioLog> mioLogPList = new ArrayList<>();
			//团单总保费
			double sumPremium = getSumPremium(grpInsurAppl);
			//险种代码对应总险种保费
			Map<String,Double> polCodeMap = getSumPolJe(grpInsurAppl);
			//2. 根据共保公司基本信息，险种信息生成对应的应付，实收数据：【FA/S 承保人实收】【GF/Q 非主承保人应付】【GF/S 非主承保人】【PS/S 主承保人】
			for(ComCompany comCompany: comAg.getComCompanies()){
				//共保比例百分比
				double gbbl =comCompany.getCoinsurAmntPct()/100;
				//共保公司应收金额:保单总金额*共保保费份额比例
				double comComPayJe=sumPremium*gbbl;
				//2.1 承保人实收数据
				MioLog mioLogM = createMioLog(comCompany,grpInsurAppl);
				mioLogId+=1;
				mioLogM.setMioLogId(mioLogId); //自增长
				for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
					if("M".equals(policy.getMrCode())){
						mioLogM.setPolCode(policy.getPolCode());
						break;
					}
				}
				mioLogM.setMioItemCode("FA");  //收付项目代码:FA
				mioLogM.setMioType("S");   //收付款方式代码:S
				mioLogM.setAmnt(comComPayJe);
				mioLogM.setRemark("暂收转保费");   //备注：PS/S和FA/S  暂收转保费，GF/S  暂收保费转应付参与共保人保费
				mioLogMList.add(mioLogM);
				//【M主承保方  || N参与承保方】
				if(!"M".equals(comCompany.getCoinsurType())){
					//2.2  产生应付[GF/Q]条数:为非主承保人个数*险种个数
					for(Entry<String,Double> entry :polCodeMap.entrySet()){
						String polCode= entry.getKey();
						double polJe = entry.getValue();
						//此参与承保方应收金额:共保保费份额比例
						double comCoJe =  polJe*gbbl;		
						PlnmioRec plnmioRec = setPlnmioRecData(grpInsurAppl,comCompany);
						plnmioRecId+=1;
						plnmioRec.setPlnmioRecId(plnmioRecId);
						plnmioRec.setPolCode(polCode);
						plnmioRec.setAmnt(comCoJe);
						plnmioRecList.add(plnmioRec);
					}
					//2.3 非主承保人[GF/S]实收数据:数据只针对非主承保人产生
					MioLog mioLogN = createMioLog(comCompany,grpInsurAppl);
					mioLogId++;
					mioLogN.setMioLogId(mioLogId);  //自增长
					for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
						if("M".equals(policy.getMrCode())){
							mioLogN.setPolCode(policy.getPolCode());
							break;
						}
					}
					mioLogN.setMioItemCode("GF");  //收付项目代码:GF
					mioLogN.setMioType("S");  //收付款方式代码:S
					mioLogN.setAmnt(comComPayJe);
					mioLogN.setRemark("暂收保费转应付参与共保人保费");   //备注：PS/S和FA/S  暂收转保费，GF/S  暂收保费转应付参与共保人保费
					mioLogNList.add(mioLogN);
				}else{
					//2.4 保单信息产生PS/S数据:PS数据还是根据险种来产生，但是产生PS数据保费合计金额需要与主承保人保费相等
					for(Entry<String,Double> entry :polCodeMap.entrySet()){
						String polCode = entry.getKey();
						double polJe = entry.getValue();
						//此参与承保方应收金额:共保保费份额比例
						double comCoJe =  polJe*gbbl;		
						MioLog mioLogPS = createMioLog(comCompany,grpInsurAppl);
						mioLogId++;
						mioLogPS.setMioLogId(mioLogId); //自增长
						mioLogPS.setPolCode(polCode);
						String cntrNo="";
						for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
							if(polCode.equals(policy.getPolCode())){
								cntrNo=policy.getCntrNo();
								break;
							}
						}
						mioLogPS.setCntrNo(cntrNo);
						mioLogPS.setMioItemCode("PS");  //收付项目代码:
						mioLogPS.setMioType("S");   //收付款方式代码:S
						mioLogPS.setAmnt(comCoJe);
						mioLogPS.setRemark("暂收转保费");   //备注：PS/S和FA/S  暂收转保费，GF/S  暂收保费转应付参与共保人保费
						mioLogPList.add(mioLogPS);
					}
				}
			}
			
			//主承保人，非主承保人,PS/S 实收数据集合
			List<MioLog> mioLogList = new ArrayList<>();
			mioLogList.addAll(mioLogNList);
			mioLogList.addAll(mioLogMList);
			mioLogList.addAll(mioLogPList);
			
			
			//收付费相关信息类
			MioPlnmioInfo oldMioPln = procMioInfoDao.getMioPlnmioInfo(applNo);			
			//应付数据集合
			List<PlnmioRec> plnList = new ArrayList<>();
			plnList.addAll(oldMioPln.getPlnmioRecList());
			plnList.addAll(plnmioRecList);
			oldMioPln.setPlnmioRecList(plnList);	
			//实收数据集合
			List<MioLog> mioList = new ArrayList<>();
			if(oldMioPln.getMioLogList()!=null){
				mioList.addAll(oldMioPln.getMioLogList());
			}
			mioList.addAll(mioLogList);
			oldMioPln.setMioLogList(mioList); 	
			procMioInfoDao.removeMioPlnmioByApplNo(applNo);
			mongoBaseDao.insert(oldMioPln);
			return true;
		}else{
			logger.error("执行产生首席共保应付服务失败：未查询到共保公司数据！");
			return false;
		}
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
	 * 获取团单总保费
	 * @param grpInsurAppl
	 * @return
	 */
	private double getSumPremium(GrpInsurAppl grpInsurAppl) {
		//团单总保费
		double sumPremium=grpInsurAppl.getApplState().getSumPremium();
		//总险种加费
		double sumPolAddJe = procMioInfoDao.getPolicyAddMoney(grpInsurAppl.getApplNo(), "");
		//总被保人加费
		double sumManAddJe = procMioInfoDao.getPersonAddMoney(grpInsurAppl.getApplNo(),null);
		return sumPremium+sumPolAddJe+sumManAddJe;
	}

	/**
	 * 初始化【应付数据】 TODO
	 * @param grpInsurAppl 团体出单基本信息
	 * @param comCompany 共保公司基本信息
	 * @param polCode 险种代码
	 * @return PlnmioRec 应付数据
	 */
	private PlnmioRec setPlnmioRecData(GrpInsurAppl grpInsurAppl, ComCompany comCo) {
		PlnmioRec plnmioRec = new PlnmioRec();
		plnmioRec.setPlnmioRecId(0L);	//应收付标识：自动增长序列
		plnmioRec.setCntrType(grpInsurAppl.getCntrType());	//合同类型
		plnmioRec.setSgNo("");								//汇缴事件号
		plnmioRec.setArcBranchNo(grpInsurAppl.getArcBranchNo());//(路由)归档机构
		plnmioRec.setCgNo(grpInsurAppl.getApplNo());//合同组号=applNo+第1险种
		plnmioRec.setPolCode("");		//险种代码
		plnmioRec.setCntrNo(grpInsurAppl.getApplNo()); 	//保单号/投保单号/帐号
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
		plnmioRec.setMioItemCode("GF");	//收付项目代码: GF
		plnmioRec.setMioType("Q");	//收付款形式代码		
		plnmioRec.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());		//管理机构
		
		plnmioRec.setSalesChannel(grpInsurAppl.getSalesInfoList().get(0).getSalesChannel());	//销售渠道		
		plnmioRec.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());	//销售机构号
		plnmioRec.setSalesNo(grpInsurAppl.getSalesInfoList().get(0).getSalesNo());	//销售员号		
		plnmioRec.setAmnt((double) 0);		//金额  【注：根据不同的保费来源获计算应缴金额】		
		plnmioRec.setLockFlag(0);			//锁标志:默认为0  银行转账在途则位1
		plnmioRec.setBankCode(comCo.getBankCode());			//银行代码
		plnmioRec.setBankAccName(comCo.getBankAccName());   //帐户所有人名称
		plnmioRec.setAccCustIdType(""); 	//帐户所有人证件类别
		plnmioRec.setBankaccIdNo("");       //帐户所有人证件号
		plnmioRec.setBankAccNo(comCo.getBankAccNo());	//银行账号
		
		plnmioRec.setHoldFlag(0);			//待转帐标志:默认为0	
		plnmioRec.setVoucherNo("");			//核销凭证号
		plnmioRec.setFinPlnmioDate(new Date()); //财务应收付日期
		plnmioRec.setClearingMioTxNo("");   //清算交易号(收据号)
		plnmioRec.setMioProcFlag("1");		//是否收付处理标记:1
		plnmioRec.setRouterNo("0"); 		//路由号:默认是‘0’
		plnmioRec.setAccId((long)0);		//关联帐户标识:默认0
		plnmioRec.setRemark(grpInsurAppl.getAgreementNo());	 //备注:存放共保协议号grpInsurAppl.agreementNo
		plnmioRec.setProcStat("N");		    //应收状态：N未收  D作废  S实收/成功，F失败
		plnmioRec.setTransStat("U");		//转账状态：N新建  U空  F失败  W抽取 S成功
		plnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
		plnmioRec.setMultiPayAccType("");	//账号所属人类别
		return plnmioRec;
	}
	
	/**
	 * 生成实收数据
	 * @param comCompany  共保公司基本信息
	 * @param grpInsurAppl 团单
	 * @return
	 */
	private MioLog createMioLog(ComCompany comCompany,GrpInsurAppl grpInsurAppl) {
		MioLog mioLog = new MioLog();
		mioLog.setMioLogId(0L); //收付流水标识:自动增长
		mioLog.setPlnmioRecId(0L); //应收付记录标识:	0
		mioLog.setPolCode("");	 //险种代码:对应险种代码
		mioLog.setCntrType(grpInsurAppl.getCntrType());  //合同类型:团单
		mioLog.setCgNo(grpInsurAppl.getCgNo());      //合同组号:保单的cg_no
		mioLog.setSgNo(grpInsurAppl.getSgNo());   	 //汇缴事件号:团单
		String polCode ="";
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			if("M".equals(policy.getMrCode())){
				polCode=policy.getPolCode();
				mioLog.setCntrNo(grpInsurAppl.getApplNo()+polCode);  //保单号/帐号/投保单号:applNo+主险种
				break;
			}
		}
		mioLog.setCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode()); //保单币种:团单
		mioLog.setMtnId(0L);       //保全批改流水号:0
		mioLog.setMtnItemCode("0");//批改保全项目:0
		mioLog.setIpsnNo(0L);  	   //被保人序号:0
		mioLog.setMioCustNo("");   //领款人/交款人客户号	       	
		mioLog.setMioCustName(""); //领款人/交款人姓名      	
		mioLog.setPlnmioDate(grpInsurAppl.getInForceDate()); //应收付日期:团单生效日期
		mioLog.setMioDate(new Date());        //实收付日期:当前系统日期
		mioLog.setMioLogUpdTime(new Date());  //写流水时间(系统时间):当前系统日期 具体到时分秒
		mioLog.setPremDeadlineDate(grpInsurAppl.getPaymentInfo().getForeExpDate());  //保费缴费宽限截止日期	      	
		mioLog.setMioItemCode("");  //收付项目代码:GF
		mioLog.setMioType("S");  //收付款方式代码:S
		mioLog.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());   //管理机构:团单
		mioLog.setPclkBranchNo("!!!!!!");  //操作员分支机构号:!!!!!!
		mioLog.setPclkNo("######"); 	 //操作员代码:######
		
		mioLog.setSalesChannel(grpInsurAppl.getSalesInfoList().get(0).getSalesChannel());	//销售渠道		
		mioLog.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());	//销售机构号
		mioLog.setSalesNo(grpInsurAppl.getSalesInfoList().get(0).getSalesNo());	//销售员号		
		
		mioLog.setMioClass(1);    //收付类型:1
		mioLog.setAmnt(0D);       //金额:该副承保公司保费
		mioLog.setAccCustIdType(""); //帐户所有人证件类别:空
		mioLog.setAccCustIdNo("");   //帐户所有人证件号:空
		mioLog.setBankCode("");      //银行代码:空
		mioLog.setBankAccName("");   //帐户所有人名称:空
		mioLog.setBankAccNo("");     //银行帐号:空
		mioLog.setMioTxClass(0);     //收付交易类型:0
		mioLog.setMioTxNo(0L);       //收付交易号(7版收据号):自动增长
		mioLog.setCorrMioDate(new Date());   //冲正业务日期:空
		mioLog.setCorrMioTxNo(0L);   //冲正收付交易号:空
		mioLog.setReceiptNo("");     //打印发票号:空
		mioLog.setVoucherNo("");     //核销凭证号:空
		mioLog.setFinPlnmioDate(new Date()); //财务应收付日期:空
		mioLog.setClearingMioTxNo(""); //清算交易流水号:空
		mioLog.setMioProcFlag("1");   //是否收付处理标记:1
		mioLog.setRouterNo("0");       //路由号:0
		mioLog.setAccId(0L);         //关联帐户标识:0
		mioLog.setCoreStat("");	 	//实收数据入账状态	
		mioLog.setBatNo(0L);         //批次号		
		mioLog.setTransCode(0L);  //转账编号		
		mioLog.setBtMioTxNo(0L);  //转账交易号		
		mioLog.setRemark("");   //备注：PS/S和FA/S  暂收转保费，GF/S  暂收保费转应付参与共保人保费
		mioLog.setPlnmioCreateTime(new Date());  //生成应收记录时间:当期系统时间，具体到时分秒
		mioLog.setNetIncome(0D);  //净收入	        	
		mioLog.setVat(0D);    //税	               	
		mioLog.setVatId("");  //ID号，对应vat_flow	             	
		mioLog.setVatRate(0D); //税率	           	
		return mioLog;
	}
	
	
	/**
	 * 保存精确度
	 * @param number  数据
	 * @param precision 保存的小数点位数
	 * @return
	 */
	public  double keepPrecision(double number, int precision) {  
        BigDecimal bg = BigDecimal.valueOf(number);  
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();  
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
