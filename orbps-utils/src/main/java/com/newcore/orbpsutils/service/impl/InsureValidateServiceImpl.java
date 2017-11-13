package com.newcore.orbpsutils.service.impl;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import com.alibaba.druid.util.StringUtils;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbpsutils.dao.api.ProductDao;
import com.newcore.orbpsutils.service.api.InsureValidateService;
import com.newcore.orbpsutils.validation.TimeUtils;

/**
 * 
 * @author zhoushoubo
 *
 */

@Service("insureValidateService")
public class InsureValidateServiceImpl implements InsureValidateService {


	@Autowired
	ProductDao productDao;
	
	/** 
	 * 方法说明：
	 * @param grpInsurAppl    
	 */
	@Override
	public Boolean baseinfoCheck(GrpInsurAppl grpInsurAppl,Errors arg1) {
		String salesTaget;
		int cnt;
		com.newcore.orbps.models.service.bo.Policy policyorcl;
		for(Policy policy:grpInsurAppl.getApplState().getPolicyList()){
			if(StringUtils.isEmpty(policy.getPolCode())){
				arg1.rejectValue("applState", "险种代码不能为空");
			}
			policyorcl=productDao.getPolicyInfo(policy.getPolCode());
			if(policyorcl==null){
				arg1.rejectValue("applState", "险种"+policy.getPolCode()+"要约中险种代码不存在");
				return false;
			}
			//清汇是否校验
			salesTaget=productDao.getSalesTaget(policy.getPolCode());
			if(!StringUtils.isEmpty(salesTaget)){
				if (StringUtils.equals(grpInsurAppl.getCntrType(), "L")){
					if(StringUtils.equals(salesTaget,"G")){
						arg1.rejectValue("cntrType", "险种"+policy.getPolCode()+"销售对象不能为个人");
						return false;
					}
				} else {
					if(!StringUtils.equals(policyorcl.getBusinessType(),"H") &&
					   !StringUtils.isEmpty(policy.getSpeciBusinessLogo())){
						arg1.rejectValue("applState", "险种"+policy.getPolCode()+"非健康险，请不要录入健康险标识");
						return false;
					}
					if(StringUtils.equals(salesTaget,"P")){
						arg1.rejectValue("cntrType", "险种"+policy.getPolCode()+"销售对象不能为团体");
						return false;
					}				
				}
			}
			//保险期间类型校验
			if(!StringUtils.equals(policyorcl.getPremiumType(), "A")){		
				if(StringUtils.equals(policy.getInsurDurUnit(), "Y")){
					if(policy.getInsurDur()!=1){
						arg1.rejectValue("applState", "险种"+policy.getPolCode()+"保险期间不正确");
						return false;
					}
				}else{
					if(policy.getInsurDur()==null || StringUtils.isEmpty(policy.getInsurDurUnit())){
						arg1.rejectValue("applState", "险种"+policy.getPolCode()+"保险期间或保险期间类型为空");
						return false;
					}
					cnt=productDao.getInsurDurCnt(policy.getPolCode(),policy.getInsurDur(),policy.getInsurDurUnit());
					if(cnt==0){
						arg1.rejectValue("applState", "险种"+policy.getPolCode()+"保险期信息不正确");
						return false;
					}
					
				}
			}
			
			//缴费方式校验
			if (policyorcl.getMoneyinItrvl().indexOf(grpInsurAppl.getPaymentInfo().getMoneyinItrvl().trim())==-1){

				arg1.rejectValue("paymentInfo", "险种"+policy.getPolCode()+"的缴费方式不在此范围"+policyorcl.getMoneyinItrvl());
				return false;
			}
		}
		
		return true;
	}

	@Override
	public Boolean ipsninfoCheck(GrpInsured grpInsured,Errors arg1) {
		com.newcore.orbps.models.service.bo.Policy policyorcl;
		long botAge=0l;
		long topAge=0l;
		String botFlag;
		String topFlag;
		if(grpInsured==null){
			arg1.rejectValue("applNo", "对应被保人信息不存在。");
			return false;
		}
		if(grpInsured.getSubStateList()==null){
			arg1.rejectValue("applNo", "对应被保人信息中要约分组号不正确或子要约不存在。");
			return false;
		}
		for(SubState subState:grpInsured.getSubStateList()){
			if(StringUtils.isEmpty(subState.getPolCode())){
				arg1.rejectValue("subStateList", "子要约中险种代码不存在.");
				return false;
			}
			//被保人性别校验
			policyorcl=productDao.getPolicyInfo(subState.getPolCode());
			if(policyorcl==null){
				arg1.rejectValue("subStateList", "子要约中子险种代码"+subState.getPolCode()+"在险种库中不存在.");
				return false;
			}
			if(!StringUtils.equals(policyorcl.getSexSaleTo(),"B")){
				if(!StringUtils.equals(policyorcl.getSexSaleTo(),grpInsured.getIpsnSex())){

					arg1.rejectValue("ipsnSex", "被保人性别不符合此险种"+subState.getPolCode()+"的性别限制。");
					return false;
				}
			}
			//被保人年龄校验
			botFlag=policyorcl.getBotAgeLmt().substring(0, 1);
			switch (botFlag) {
			case "Y":
				botAge=Integer.valueOf(policyorcl.getBotAgeLmt().substring(1));
				break;
			case "M":
				botAge=Integer.valueOf(policyorcl.getBotAgeLmt().substring(1))*30;
				break;
			case "D":
				botAge=Integer.valueOf(policyorcl.getBotAgeLmt().substring(1));
				break;

			default:
				break;
			}
			int intervalDays=TimeUtils.getIntervalDays(grpInsured.getIpsnBirthDate(), Calendar.getInstance().getTime());
			if (StringUtils.equals(botFlag, "Y")){
				if(grpInsured.getIpsnAge()==null){
					return false;
				}
				if(grpInsured.getIpsnAge()<botAge){

					arg1.rejectValue("ipsnAge", "被保人年龄不符合此险种"+subState.getPolCode()+"的年龄下限。");
					return false;
				}
			} else {
			
				if(intervalDays<botAge){
	
					arg1.rejectValue("ipsnAge", "被保人年龄不符合此险种"+subState.getPolCode()+"的年龄下限。");
					return false;
				}
			}
			
			topFlag=policyorcl.getTopAgeLmt().substring(0, 1);
			switch (topFlag) {
			case "Y":
				topAge=Integer.valueOf(policyorcl.getTopAgeLmt().substring(1));
				break;
			case "M":
				topAge=Integer.valueOf(policyorcl.getTopAgeLmt().substring(1))*30;
				break;
			case "D":
				topAge=Integer.valueOf(policyorcl.getTopAgeLmt().substring(1));
				break;

			default:
				break;
			}		

			if (StringUtils.equals(topFlag, "Y")){
				if(grpInsured.getIpsnAge()==null){
					return false;
				}
				if(grpInsured.getIpsnAge()>topAge){

					arg1.rejectValue("ipsnAge", "被保人年龄不符合此险种"+subState.getPolCode()+"的年龄上限。");
					return false;
				}
			} else {
				if(intervalDays>topAge){
	
					arg1.rejectValue("ipsnAge", "被保人年龄不符合此险种"+subState.getPolCode()+"的年龄上限。");
					return false;
				}
			}
		}
		return true;
	}
}
