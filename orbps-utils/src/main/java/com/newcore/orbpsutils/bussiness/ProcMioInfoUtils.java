package com.newcore.orbpsutils.bussiness;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.finance.PlnmioRec;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.OrgTree;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.supports.dicts.MIO_CLASS;
import com.newcore.supports.dicts.MIO_ITEM_CODE;
import com.newcore.supports.dicts.MIO_TYPE;
import com.newcore.supports.dicts.MONEYIN_TYPE;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.dicts.PLNMIO_STATE;
import com.newcore.supports.dicts.PREM_SOURCE;
import com.newcore.supports.dicts.YES_NO_FLAG;

/**
 * 处理应收付数据工具类isBankTrans
 *
 * @author JCC
 *         2016年11月30日 14:54:09
 */
public class ProcMioInfoUtils {

	/**
	 * 保存精确度
	 * @param number  数据
	 * @param precision 保存的小数点位数
	 * @return
	 */
	public static double keepPrecision(double number, int precision) {  
		BigDecimal bg = BigDecimal.valueOf(number);  
		return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();  
	} 


	/**
	 * 获取日期年份
	 * @param date 日期
	 * @return YYYY
	 */
	public static int getYear(Date date){
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}


	/**
	 * 比较两个double类型数据大小
	 * @param a 
	 * @param b
	 * @return a>b:1, a<b:-1,a==b:0
	 */
	public static int compareDouble(double a,double b){
		BigDecimal bigA = BigDecimal.valueOf(a);
		BigDecimal bigB = BigDecimal.valueOf(b);
		return bigA.compareTo(bigB);
	}

	/**
	 * 格式化日期格式
	 * @return
	 *   yy-MM-dd HH:mm:ss
	 */
	public static String getFormatDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Date date = new Date();
		return sdf.format(date);
	}

	/**
	 * 月+月数
	 * @param date 日期
	 * @param munths 月数
	 * @return
	 */
	public static Date getMonth(Date date,int munths){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH,munths);
		return calendar.getTime();
	}




	/**
	 * 初始化【应收付记录】数据 TODO
	 *
	 * @param grpInsurAppl       保单信息
	 * @param insurOper 操作轨迹
	 * @return
	 */
	public static PlnmioRec setPlnmioRec(GrpInsurAppl grpInsurAppl, InsurApplOperTrace insurOper) {
		PlnmioRec plnmioRec = new PlnmioRec();
		plnmioRec.setPlnmioRecId(0L);    //应收付标识：自动增长序列
		plnmioRec.setCntrType(grpInsurAppl.getCntrType());    //合同类型
		plnmioRec.setSgNo("");            //汇缴事件号
		plnmioRec.setArcBranchNo(grpInsurAppl.getArcBranchNo());   //(路由)归档机构
		String polCode = "";
		for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
			if (MR_CODE.MASTER.getKey().equals(policy.getMrCode())) {
				polCode = policy.getPolCode();
				break;
			}
		}
		plnmioRec.setCgNo(grpInsurAppl.getApplNo() + polCode);     //合同组号=applNo+第1险种

		plnmioRec.setPolCode(polCode);    //险种代码
		plnmioRec.setPremDeadlineDate(new Date()); //保费缴费宽限截止日期
		plnmioRec.setCntrNo(grpInsurAppl.getApplNo());    //单号
		plnmioRec.setCurrencyCode(grpInsurAppl.getPaymentInfo().getCurrencyCode());    //保单币种
		plnmioRec.setMtnId(0L);        	 //保全批改流水号:默认为0
		plnmioRec.setMtnItemCode("0");    //批改保全项目
		//如果是单位总缴费，则赋值为0，如果是组织架构树缴费，则赋值为组织架构层次代码，如果是收费组缴费，则赋值为收费组号，如果是被别人个人缴费，则赋值为被别人序号
		plnmioRec.setIpsnNo(0L);         //被保人序号:默认为0
		plnmioRec.setLevelCode("");          //组织层次代码
		plnmioRec.setFeeGrpNo(0L);           //收费组:默认为0
		plnmioRec.setMioCustNo("");      //领款人/交款人客户号 【注：①如果是一笔总应收，则使用汇交人客户号，②如果是每个被保人一笔的话，则用被保人客户号】
		plnmioRec.setMioCustName("");    //领款人/交款人姓名     【注：①如果是一笔总应收，则使用汇交人客户号，②如果是每个被保人一笔的话，则用被保人客户号】
		plnmioRec.setMioClass(Integer.parseInt(MIO_CLASS.RECEIVABLES.getKey()));    //收付类型:0-应收付；  1-应收； -1-应付
		for (TraceNode traceNode : insurOper.getOperTraceDeque()) {
			if (NEW_APPL_STATE.UNDERWRITING.getKey().equals(traceNode.getProcStat())) {
				plnmioRec.setPlnmioDate(traceNode.getProcDate());      //应收付日期：暂时取保单生效日期
				plnmioRec.setSignYear(getYear(traceNode.getProcDate()));//(路由)签单年度
				break;
			}
		}
		plnmioRec.setMioItemCode(MIO_ITEM_CODE.FA.getKey());          //收付项目代码:首期暂收 FA
		plnmioRec.setMioType(MIO_TYPE.T.getKey());      //收付款形式代码
		plnmioRec.setMgrBranchNo(grpInsurAppl.getMgrBranchNo()); //管理机构
		plnmioRec.setSalesChannel(grpInsurAppl.getSalesInfoList().get(0).getSalesChannel());    //销售渠道
		plnmioRec.setSalesBranchNo(grpInsurAppl.getSalesInfoList().get(0).getSalesBranchNo());  //销售机构号
		plnmioRec.setSalesNo(grpInsurAppl.getSalesInfoList().get(0).getSalesNo());    //销售员号
		plnmioRec.setAmnt(BigDecimal.ZERO); //金额  【注：根据不同的保费来源获计算应缴金额】
		plnmioRec.setLockFlag("0");           //锁标志:默认为0  银行转账在途则位1
		plnmioRec.setBankCode("");            //银行代码 【注：根据不同的保费来源获取对应的银行信息】
		plnmioRec.setBankAccName("");         //帐户所有人名称
		plnmioRec.setAccCustIdType("");       //帐户所有人证件类别
		plnmioRec.setAccCustIdNo("");         //帐户所有人证件号
		plnmioRec.setBankAccNo("");           //银行账号 【注：根据不同的保费来源获取对应的银行信息】
		plnmioRec.setHoldFlag("0");           //待转帐标志:默认为0
		plnmioRec.setVoucherNo("");           //核销凭证号
		plnmioRec.setFinPlnmioDate(new Date());//财务应收付日期
		plnmioRec.setClearingMioTxNo("");     //清算交易号(收据号)
		plnmioRec.setMioProcFlag("1");        //是否收付处理标记:默认是'1'
		plnmioRec.setRouterNo("0");           //路由号:默认是‘0’
		plnmioRec.setAccId(0L);               //关联帐户标识:默认0
		plnmioRec.setRemark("");              //备注
		plnmioRec.setPlnmioCreateTime(new Date());//生成应收记录时间
		plnmioRec.setTransStat("U");          //转账状态：U空  N新建 ,W 抽取 ,S 成功 ,F 失败
		plnmioRec.setProcStat(PLNMIO_STATE.UNCOLLECTED.getKey()); //应收状态：N 未收 ,D 作废 ,S 实收，T 在途


		plnmioRec.setMultiPayAccType("");    //账号所属人类别
		plnmioRec.setIpsnName("");            //被保人姓名
		plnmioRec.setIpsnSex("");             //被保人性别
		plnmioRec.setIpsnBirthDate(null);	  //被保人出生日期
		plnmioRec.setIpsnIdType("");          //被保人证件类别
		plnmioRec.setIpsnIdNo("");            //被保人证件号
		plnmioRec.setExtKey1("");        //扩展健1
		plnmioRec.setExtKey2("");        //扩展健2
		plnmioRec.setExtKey3("");        //扩展健3
		plnmioRec.setExtKey4("");        //扩展健4
		plnmioRec.setExtKey5("");        //扩展健5
		return plnmioRec;
	}


	/**
	 * 获取需要缴费节点下所有不需要缴费的层次代码
	 *
	 * @param orgTreeMap  所有组织架构数据
	 * @param noIsPaidMap 不需要缴费的组织架构数据
	 * @return
	 */
	public static Map<String, List<String>> getChildTree(Map<String, OrgTree> orgTreeMap, Map<String, OrgTree> noIsPaidMap) {
		//levelMap 存放不需要缴费的子节点
		Map<String, List<String>> levelMap = new HashMap<>();
		for (Entry<String, OrgTree> entry : noIsPaidMap.entrySet()) {
			List<String> isHaveList = new ArrayList<>();
			for (Entry<String, List<String>> entryIn : levelMap.entrySet()) {
				List<String> entryList = entryIn.getValue();
				isHaveList.addAll(entryList);
			}
			String levelCode = entry.getKey();
			if (!isHaveList.contains(levelCode)) {
				Map<String, List<String>> ipsnMap = new HashMap<>();
				OrgTree orgTree = entry.getValue();
				//父级组织架构树新
				OrgTree priOrgTree = orgTreeMap.get(orgTree.getPrioLevelCode());
				levelMap.putAll(getDg(levelCode, priOrgTree, orgTreeMap, ipsnMap, levelMap));
			}
		}
		return levelMap;
	}

	/**
	 * 递归算法
	 *
	 * @param levelCode  当前层级代码
	 * @param orgTree    父级组织架构树
	 * @param orgTreeMap 保单中的组织机构树
	 * @return
	 */
	private static Map<String, List<String>> getDg(String levelCode, OrgTree orgTree, Map<String, OrgTree> orgTreeMap,
			Map<String, List<String>> ipsnMap, Map<String, List<String>> levelMap) {
		//父级组织架构树层级代码
		String personCode = orgTree.getLevelCode();
		//若需要缴费，则需要记录当前节点的子节点:
		List<String> levelList = new ArrayList<>();
		//判断父级组织架构树，是否需要缴费
		if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())) {
			//判断levelMap中是否存在【key = personCode】的数据
			if (levelMap.containsKey(personCode)) {
				//整合节点中的value，防止重复保存
				levelList.addAll(levelMap.get(personCode));
				if (!levelMap.get(personCode).contains(levelCode)) {
					levelList.add(levelCode);
				}
			} else {
				//将当前子节点存入集合
				levelList.add(levelCode);
			}

			if (ipsnMap.containsKey(levelCode)) {
				//将当前子节点的子节点信息存入集合
				levelList.addAll(ipsnMap.get(levelCode));
			}
			levelMap.put(personCode, levelList);
		} else {
			//当前节点，不需要缴费，则继续获取上级节点，进行递归操作，并记录次此节点
			OrgTree priOrgTree = orgTreeMap.get(orgTree.getPrioLevelCode());
			levelList.add(levelCode);
			//ipsnMap存放当前父级节点下的所有不需要缴费的子节点
			ipsnMap.put(personCode, levelList);
			getDg(personCode, priOrgTree, orgTreeMap, ipsnMap, levelMap);
		}
		return levelMap;
	}

	/**
	 * 判断缴费形式是否为银行转账
	 *
	 * @param grpInsurAppl
	 * @return 是-true 非-false
	 */
	public static boolean isBankTrans(GrpInsurAppl grpInsurAppl) {
		/*	1，保费来源为2、3  默认为T
			2，保费来源为1，且缴费相关里面的缴费形式(moneyinType)为T
			3，保费来源为1，且组织架构树里面有缴费节点 默认为 T*/
		String premSource = grpInsurAppl.getPaymentInfo().getPremSource();
		boolean isTrue = true;
		if (PREM_SOURCE.GRP_ACC_PAY.getKey().equals(premSource)) {
			if (null == grpInsurAppl.getOrgTreeList() || grpInsurAppl.getOrgTreeList().isEmpty()) {
				//1.1、无组织架构树时
				if (!StringUtils.equals(MONEYIN_TYPE.TRANSFER.getKey(), grpInsurAppl.getPaymentInfo().getMoneyinType())) {
					isTrue = false;
				}
			} else {
				//用于存放组织架构树中已经缴费的数据
				List<OrgTree> orgTreeList = new ArrayList<>();
				for (OrgTree orgTree : grpInsurAppl.getOrgTreeList()) {
					//判断是否需要缴费:ifPay【Y：是；N：否。】
					if (StringUtils.equals(YES_NO_FLAG.YES.getKey(), orgTree.getIsPaid())) {
						orgTreeList.add(orgTree);
					}
				}
				if (orgTreeList.isEmpty() && !StringUtils.equals(MONEYIN_TYPE.TRANSFER.getKey(), 
						grpInsurAppl.getPaymentInfo().getMoneyinType())) {
					isTrue = false;
				}
			}
		}
		return isTrue;
	}



	/**
	 * 当前日期+天数
	 * @param day
	 * @return
	 */
	public static Date getDate(int day){
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		calendar.setTime(date);
		calendar.add(Calendar.DATE,day);
		return calendar.getTime();
	}
}
