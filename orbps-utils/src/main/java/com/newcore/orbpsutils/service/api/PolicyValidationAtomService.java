package com.newcore.orbpsutils.service.api;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;

/**
 * @author huanglong
 * @date 2017年1月9日
 * @content 险种校验原子服务
 */

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface PolicyValidationAtomService {
	/**
	 * @author huanglong
	 * @date 2017年1月10日
	 * @param PolicyValidationAtomService
	 * @return int
	 * @content 校验险种保险期间是否合法   返回值 -1失败或不符合险种要求  ,0-成功 ,1-成功存在特约
	 */
	@POST
	@Path("/Period")
	public int ValidaPolPeriod(Policy policy);
	
	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return boolean
	 * @content 校验险种是否包含现有缴费方式
	 */
	@POST
	@Path("/PayType")
	public boolean ValidaPayType(String polCode,String moneyinItrvl);
	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return Map<String,String>
	 * @content  查询险种下子险的互斥子险种
	 */	
	@POST
	@Path("/Mutex")
	public Map<String, String> ValidaMutex(String polSubCode);
	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return RetInfo
	 * @content 用于被保人年龄性别 是否符合险种要求的校验
	 */
	
	@POST
	@Path("/PolAndIpsn")
	public RetInfo ValidaPolAndIpsn(GrpInsured grpInsured);
	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomServiceImpl
	 * @return Map<String,String>
	 * @content 一级分类-保险101   
	 * 			二类分类-长期201   短期201   
	 * 			三级分类-团体301   个人302 
	 * 			四级分类-寿险401   养老年金402  健康险403   意外险404  
	 * 			五级分类-定期寿险501   终身险502   年金503  两全504  医疗险 505 重大疾病险506  失能险507 护理险508   
	 * 			六级分类-费用报销601  定额给付602
	 * 			附加分类-普通型901 投连型902 分红型903 万能型904 变额年金905
	 * 			详见 CAT_CODE枚举类
	 */
	@POST
	@Path("/PolType")
	public Map<String, String> ValidaPolType(String polCode);
	/**
	 * @author huanglong
	 * @date 2017年1月12日
	 * @param PolicyValidationAtomService
	 * @return Map<String,String>
	 * @content 险种承保约束信息 可查询: 销售对象salesTaget  被保人数量 ipsnCount  是否允许单独投保applSingleFlag  承保来源insurSource 是否存在人员约束ipsnLimitFlag  及其产品该服务所有返回值
	 */
	@POST
	@Path("/SalesLimit")
	public Map<String, Object> ValidaSalesLimit(String polCode);
	/**
	 * @author huanglong
	 * @date 2017年1月11日
	 * @param PolicyValidationAtomService
	 * @return RetInfo
	 * @content --1.所有险种的保险期单位必须一样；
	 *	  		--2.所有险种的保险期间必须小于等于第一主险；
	 *  		--3.对所有险种的保险期间、单位的合法性校验； （包含约定）
	 * 			--4.含有子险种的，对子险种的互斥做校验；
	 *			--5.对交费方式校验，所有险种都必须包含所选择的交费方式，例如：选月交的必须都含有月交；
	 */
	@POST
	@Path("/ApplState")
	public RetInfo ValidaApplState(GrpInsurAppl grpInsurAppl);
	/**
	 * @authormengjiangbo
	 * @date 2017年5月24日
	 * @param PolicyValidationAtomService
	 * @return RetInfo
	 * @content --1  如果团单录入纸质保单需要检查数据库纸质保单开关是否打开
	 * 	2 如果是纸质保单并且质保保单开关已打开 检验所有险种是否存在配置库表中，如有不存在的 不能使用纸质保单
	 *  3 反之则通过校验 
	 */
	public String ValidaPolConfig(List<Policy> policyList);
	
}
