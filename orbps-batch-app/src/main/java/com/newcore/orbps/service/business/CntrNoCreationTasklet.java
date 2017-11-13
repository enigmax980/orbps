package com.newcore.orbps.service.business;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.mongodb.BasicDBList;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.opertrace.InsurApplOperTrace;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.api.PolNatureService;
import com.newcore.orbps.service.ipms.api.IforceRuleService;
import com.newcore.orbpsutils.dao.api.UntilPolicyDao;
import com.newcore.orbpsutils.math.BigDecimalUtils;
import com.newcore.orbpsutils.math.DateUtils;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.INFORCE_DATE_TYPE;
import com.newcore.supports.dicts.INSUR_DUR_TYPE;
import com.newcore.supports.dicts.LST_PROC_TYPE;
import com.newcore.supports.dicts.MR_CODE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 生成合同号
 * Created by liushuaifeng on 2017/2/11 0011.
 */
public class CntrNoCreationTasklet implements Tasklet {

    @Autowired
    MongoBaseDao mongoBaseDao;

    @Resource(name = "resuliforceRuleService")
    IforceRuleService iforceRuleService; //查询产品平台生效时点，计算保单保单基本信息签单日

    @Autowired
    UntilPolicyDao untilPolicyDao;

    @Autowired
    MongoTemplate mongoTemplate;
    
    @Autowired
    PolNatureService polNatureService;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        String applNo = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobParameters().getString("applNo");
        String enforcePoint = "0";
        Map<String, Object> queryMap = new HashMap<>();
        queryMap.put("applNo", applNo);
        GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
        if (null == grpInsurAppl) {
            //批作业执行失败落地
            throw new BusinessException(new Throwable("根据投保单号查询保单基本信息失败."));
        }

        //获得生效日期
        Date inForceDate = getInForceDate(grpInsurAppl.getApplState().getInforceDateType(), grpInsurAppl.getApplState().getDesignForceDate(), grpInsurAppl.getSignDate());
        grpInsurAppl.setInForceDate(inForceDate);

        Calendar calendarInforce = Calendar.getInstance();
        calendarInforce.setTime(inForceDate);

		Date signDate = null;
		InsurApplOperTrace insurApplOperTrace = (InsurApplOperTrace) mongoBaseDao.findOne(InsurApplOperTrace.class,queryMap);

		if (null == insurApplOperTrace) {
			//查询操作轨迹
			throw new BusinessException(new Throwable("根据投保单号查询操作轨迹信息失败."));
		}

		for (TraceNode traceNode : insurApplOperTrace.getOperTraceDeque()) {
			if (StringUtils.equals(traceNode.getProcStat(), NEW_APPL_STATE.UNDERWRITING.getKey())) {
				//计算前签单日期--从操作轨迹表中取核保通过日期
				signDate = traceNode.getProcDate();
			}
		}

		grpInsurAppl.setSignDate(signDate);
        //生成合同号
        String cgNo = String.valueOf(calendarInforce.get(Calendar.YEAR)) + grpInsurAppl.getMgrBranchNo() + grpInsurAppl.getFirPolCode() + getCounterCgNo(grpInsurAppl.getMgrBranchNo(), signDate);
        cgNo = untilPolicyDao.cfsSetCntrNoBit(cgNo, cgNo.length());
        grpInsurAppl.setCgNo(cgNo);

        for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
            if (StringUtils.equals(policy.getPolCode(), grpInsurAppl.getFirPolCode())) {
                policy.setCntrNo(cgNo);
                continue;
            }
            policy.setCntrNo(getCntrNo(grpInsurAppl, policy.getPolCode()));
        }

        //sgNo赋值(此处在保单基本信息提交的时候赋值)
        if (StringUtils.equals(CNTR_TYPE.LIST_INSUR.getKey(), grpInsurAppl.getCntrType())) {
            grpInsurAppl.setSgNo(grpInsurAppl.getApplNo());
        }

        //计算合同预计满期日期
        if (StringUtils.equals(INSUR_DUR_TYPE.INSUR_DUR_ONCE.getKey(), grpInsurAppl.getApplState().getInsurDurUnit())) {
            grpInsurAppl.setCntrExpiryDate(inForceDate);
        } else {
            //根据生效日期、投保要约applState下的-保险期间与保险期类型 得出合同预计满期日期
            Date cntrExpiryDate = DateUtils.computeDate(inForceDate, grpInsurAppl.getApplState().getInsurDur(), grpInsurAppl.getApplState().getInsurDurUnit());
            cntrExpiryDate = DateUtils.computeDate(cntrExpiryDate, -1, "D");
            grpInsurAppl.setCntrExpiryDate(cntrExpiryDate);

        }

        //更新保单基本信息
        Criteria criGrpInsurAppl = new Criteria();
        criGrpInsurAppl.and("applNo").is(grpInsurAppl.getApplNo());
        Query queryGrpInsurAppl = new Query();
        queryGrpInsurAppl.addCriteria(criGrpInsurAppl);
        Update updateGrpInsurAppl = new Update();

        updateGrpInsurAppl.set("inForceDate", grpInsurAppl.getInForceDate());
        updateGrpInsurAppl.set("signDate", grpInsurAppl.getSignDate());
        updateGrpInsurAppl.set("cgNo", grpInsurAppl.getCgNo());
        if (!StringUtils.isEmpty(grpInsurAppl.getSgNo())) {
            updateGrpInsurAppl.set("sgNo", grpInsurAppl.getSgNo());
        }
        updateGrpInsurAppl.set("cntrExpiryDate", grpInsurAppl.getCntrExpiryDate());
        updateGrpInsurAppl.set("applState", grpInsurAppl.getApplState());
        
        //基金险功模块业务：在生效的时候，需要计算公共与个人账户金额以及管理费金额
        boolean isFund = checkPolCodeIsFund(grpInsurAppl.getApplState().getPolicyList());
        double accAdminBalanceGG=0; //账户管理费金额(公共)
        if(isFund){//是基金险，计算帐号余额，管理费
        	double accBalance =0; 	  //账户余额
         	String adminFeeCopuType = grpInsurAppl.getFundInsurInfo().getAdminFeeCopuType(); //管理费提取方式
         	double adminFeePct= grpInsurAppl.getFundInsurInfo().getAdminFeePct(); 	//管理费比例
         	if("P".equals(adminFeeCopuType)){	 //按保费计算：
         		//公共账户缴费金额
         		double sumFundPremium = grpInsurAppl.getFundInsurInfo().getSumFundPremium();
         	    //管理费金额=账户总保费*管理费比例/100
         		accAdminBalanceGG = BigDecimalUtils.keepPrecisionDouble(sumFundPremium * adminFeePct /100, 2);
         		//账户总余额=账户总保费-管理费金额
         		accBalance = sumFundPremium - accAdminBalanceGG;
         	}else if("A".equals(adminFeeCopuType)){  //按保额计算
         		//计入公共账户金额
         		double sumFundAmnt = grpInsurAppl.getFundInsurInfo().getSumFundAmnt();
         		//管理费金额=账户总保额*管理费比例/100
         		accAdminBalanceGG = BigDecimalUtils.keepPrecisionDouble(sumFundAmnt * adminFeePct /100, 2);
         		//账户总余额=账户总保费-管理费金额
         		accBalance = sumFundAmnt - accAdminBalanceGG;
         	}
            updateGrpInsurAppl.set("fundInsurInfo.accBalance",accBalance);	//账户余额
            updateGrpInsurAppl.set("fundInsurInfo.accAdminBalance", accAdminBalanceGG);	//账户管理费金额
        }        
        mongoTemplate.updateFirst(queryGrpInsurAppl, updateGrpInsurAppl, GrpInsurAppl.class);

        //如果有清单更新被保人表，procStat:N（新建）-->E(生效)，需要在回退的时候，E回退到N。
        if (StringUtils.equals(LST_PROC_TYPE.ORDINARY_LIST.getKey(), grpInsurAppl.getLstProcType())){
            BasicDBList dbd = new BasicDBList();
            dbd.add("N");
            dbd.add("E");
            Query queryGrpInsured = new Query();
            queryGrpInsured.addCriteria(Criteria.where("applNo").is(grpInsurAppl.getApplNo()));
            queryGrpInsured.addCriteria(Criteria.where("procStat").in(dbd));
            Update updateGrpInsured = new Update();
            updateGrpInsured.set("ipsnInForceDate", inForceDate);
            updateGrpInsured.set("procStat", "E");
            updateGrpInsured.set("cgNo", cgNo);
            updateGrpInsured.set("batNo", "0");
            mongoTemplate.updateMulti(queryGrpInsured, updateGrpInsured, GrpInsured.class);
            
            if(isFund){	//是基金险，计算帐号余额，管理费
            	double accBalanceGR =0; 	   	//账户余额(个人)
                double accAdminBalanceGR=0;   	//账户管理费金额(个人)
                double sumAccAdminBalance=0;  	//账户管理费金额总计(个人)
                double accSumAdminBalance =grpInsurAppl.getFundInsurInfo().getAccSumAdminBalance(); //总管理费金额
                double adminFeePct= grpInsurAppl.getFundInsurInfo().getAdminFeePct(); 			 	//管理费比例
                
                //获取投保单下的有效被保人信息
	            Query query = new Query();
    			query.addCriteria(Criteria.where("applNo").is(grpInsurAppl.getApplNo()));
    			query.addCriteria(Criteria.where("procStat").is("E"));
    			query.with(new Sort(new Sort.Order(Sort.Direction.ASC,"ipsnNo")));
	            List<GrpInsured> grpInsuredList =mongoTemplate.find(query, GrpInsured.class);
	            for(GrpInsured grpInsured: grpInsuredList){
	        		//账户总余额=被保人要约保额
	        		accBalanceGR = getSumFundAmnt(grpInsured,2);
	            	//判断当前遍历的被保人是否是最后一个
	            	if(grpInsured.equals(grpInsuredList.get(grpInsuredList.size()-1))){
	            		//账户管理费金额(个人) = 总管理费金额-公共账户管理费金额-个人账户管理费金额累计之和
	            		accAdminBalanceGR=accSumAdminBalance-sumAccAdminBalance-accAdminBalanceGG;	            		
	            	}else{	            		
	            		//被保人要约实际保费：循环遍历该被保人清单信息GrpInsured.subStateList.premium字段的累计之和
		        		double sumFundAmnt = getSumFundAmnt(grpInsured,1);
	            		//管理费金额=被保人要约实际保费*管理费比例/100
		        		accAdminBalanceGR = BigDecimalUtils.keepPrecisionDouble(sumFundAmnt * adminFeePct /100, 2);
		        		sumAccAdminBalance+=accAdminBalanceGR;
	            	}    	      		
	        		Query grpQuery = new Query();
	        		grpQuery.addCriteria(Criteria.where("applNo").is(grpInsurAppl.getApplNo()));
	        		grpQuery.addCriteria(Criteria.where("ipsnNo").is(grpInsured.getIpsnNo()));
	                Update grpUpdate = new Update();
	                grpUpdate.set("accBalance",accBalanceGR);				//账户余额
	                grpUpdate.set("accAdminBalance", accAdminBalanceGR);	//账户管理费金额
	                mongoTemplate.updateMulti(grpQuery, grpUpdate, GrpInsured.class);
	            }
            }
        }

        return RepeatStatus.FINISHED;
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


	/**
     * 获取被保人保费/保额
     * @param grpInsured 被保人
     * @param 
     * 	type 累计类别  ： 
     * 		1 - 被保人要约实际保费
     * 		2 - 被保人要约保额			 
     * @return 险种保费的累计之和
     */
    private double getSumFundAmnt(GrpInsured grpInsured,int type) {
    	double money=0;
    	for(com.newcore.orbps.models.service.bo.grpinsured.SubState subState: grpInsured.getSubStateList()){
    		if(type ==1 ){
    			money+=subState.getPremium();
			}else{
				money+=subState.getFaceAmnt();
			}
    	}
		return money;
	}


	/**
     * 功能描述:获得生效日期：
     * 如果生效日类型为“1”，且指定生效日期存在，则生效日期=指定生效日期。
     * 如果生效日类型不为“1”，且到账日存在，则生效日期=到账日+1天；若到账日不存在，取当前时间。
     *
     * @param inforceDateType 生效日类型
     * @param designForceDate 指定生效日期
     * @param fundsAcctDate   到账日
     * @return
     */
    public Date getInForceDate(String inforceDateType, Date designForceDate, Date fundsAcctDate) {

        Date date = new Date();

        //生效日期 类型 判断：指定生效日 -取要约中生效日类型 1/0  判断：若为1且  指定生效日不为空
        if (StringUtils.equals(INFORCE_DATE_TYPE.SPECIFY_EFFECT.getKey(), inforceDateType)) { //生效日期类型为指定生效日
            return designForceDate;
        } else {
            //取要约中生效日类型 1/0  判断：若为0且  指定生效日不为空 直接返回当前时间
            if (fundsAcctDate == null) {
                return date;
            } else {
                //取要约中生效日类型 1/0  判断：若为0且  指定生效日 为空 --获取到账日-到账日加一天返回
                return DateUtils.computeDate(fundsAcctDate, 1, "D");

            }
        }
    }


    /**
     * 功能描述:根据生效日期，计算签单日期
     *
     * @param inForceDate  生效日期
     * @param enforcePoint 生效时点
     * @return 处理标记 -签单日期
     */
    public Date computeSignDate(Date inForceDate, int enforcePoint) {
        //根据业务要求修改为签单日为生效日的前n(0、1)天
        //去产品组查生效时点（该生效时间点，需从所有险种中取最大的值）
        // 计算相对日期 输入日期，相对天数，返回日期

        return DateUtils.getRelativeDate(inForceDate, -enforcePoint);

    }

    /**
     * 获取合同号八位流水号
     *
     * @param mgrBranchNo 管理机构号
     * @param inForceDate 签单日
     * @return
     */
    public String getCounterCgNo(String mgrBranchNo, Date inForceDate) {

        Calendar c = Calendar.getInstance();
        c.setTime(inForceDate);
        String yearStr = String.valueOf(c.get(Calendar.YEAR));
        //获取八位流水号
        String counter = untilPolicyDao.cfsGetCounterCgNo(mgrBranchNo, yearStr);
        if (StringUtils.isEmpty(counter)) {
            throw new BusinessException(new Throwable("获取合同流水号失败"));
        }

        return counter;
    }


    /**
     * 生成分单号-除第一主险外的附加险
     *
     * @param grpInsurAppl    ：保单类型
     * @param polCode     ：险种代码
     * @return String
     */
    public String getCntrNo(GrpInsurAppl grpInsurAppl, String polCode) {
        Calendar c = Calendar.getInstance();
        c.setTime(grpInsurAppl.getInForceDate());
        String yearStr = String.valueOf(c.get(Calendar.YEAR));
        //生成流水号
        String counter = untilPolicyDao.cfsGetCounterCntrNo(grpInsurAppl.getMgrBranchNo(), yearStr, grpInsurAppl.getCntrType(), MR_CODE.RIDER.getKey(), grpInsurAppl.getApplState().getInsurDurUnit());
        if (StringUtils.isEmpty(counter)) {
            throw new BusinessException(new Throwable("生成分保单号流水号失败。"));
        }
        String cntrNo = yearStr + grpInsurAppl.getMgrBranchNo() + polCode + counter;
        cntrNo = untilPolicyDao.cfsSetCntrNoBit(cntrNo, cntrNo.length());

        return cntrNo;
    }

}
