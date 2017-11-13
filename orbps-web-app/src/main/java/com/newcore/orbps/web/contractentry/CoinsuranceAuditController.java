package com.newcore.orbps.web.contractentry;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.authority_support.models.SessionModel;
import com.newcore.authority_support.util.SessionUtils;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.ComCompany;
import com.newcore.orbps.models.service.bo.CommonAgreement;
import com.newcore.orbps.models.service.bo.RetInfoObject;
import com.newcore.orbps.models.service.bo.grpinsurappl.Address;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit.CoInsurComInFoVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit.CoinsuranceAuditZVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit.CoinsuranceInfoVo;
import com.newcore.orbps.models.web.vo.contractentry.coinsuranceaudit.CoinsuranceInsuranceInfoVo;
import com.newcore.orbps.service.api.CommonAgreementService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 共保审批
 * 
 * @author wangyanjie
 *
 */
@Controller
@RequestMapping("/orbps/contractEntry/coinAudit")
public class CoinsuranceAuditController {

	@Autowired
	CommonAgreementService comAgrService;
	@Autowired
	PageQueryService pageQueryService;


	/**
	 * 查询共保信息
	 * 
	 * @author wangyanjie
	 * @param 2016.10.25
	 */
	@RequestMapping(value = "/query")
	public @ResponseMessage CoinsuranceAuditZVo search(@RequestBody String agreementNo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		CommonAgreement commonAgreement = new CommonAgreement();
		CoinsuranceAuditZVo coinsuranceAuditZVo = new CoinsuranceAuditZVo();
		commonAgreement.setAgreementNo(agreementNo);
		RetInfoObject<CommonAgreement> retInfoObject = comAgrService.comAgrSerQuery(commonAgreement);
		if("1".equals(retInfoObject.getRetCode())){
			commonAgreement = retInfoObject.getListObject().get(0);
			coinsuranceAuditZVo = coinsuranceAuditBoToVo(commonAgreement);
			coinsuranceAuditZVo.setAppAddrCountry(commonAgreement.getConvention());
		}
		
		return coinsuranceAuditZVo;
	}

	@RequestMapping(value = "/queryComCompany")
	public @ResponseMessage DataTable<CoInsurComInFoVo> queryComCompanies(QueryDt query,CommonAgreement commonAgreement) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		RetInfoObject<CommonAgreement> retInfoObject = comAgrService.comAgrSerQuery(commonAgreement);	//调用服务
		if("0".equals(retInfoObject.getRetCode()) || null == retInfoObject.getListObject() || null == retInfoObject.getListObject().get(0)){
			return new DataTable<>();
		}
		List<CoInsurComInFoVo> coInsurComInFoVos = comCompaniesToVo(retInfoObject.getListObject().get(0).getComCompanies());
		List<CoInsurComInFoVo> pagingList = new ArrayList<>();//分页数据
		long page = query.getPage()/query.getRows();//第几页
		int size = query.getRows();//每页条数		
		for(int i = size * (int)page; i < size * (page +1) && i < coInsurComInFoVos.size(); i++){
			pagingList.add(coInsurComInFoVos.get(i));
		}
		//创建PageData。
		PageData<CoInsurComInFoVo> pageData = new PageData<>();        
		//当前页号的列表数据
		pageData.setData(pagingList);
		//总记录数
		pageData.setTotalCount(coInsurComInFoVos.size());
		return  pageQueryService.tranToDataTable(query.getRequestId(), pageData);
	}
	@RequestMapping(value = "/queryComPol")
	public @ResponseMessage DataTable<CoinsuranceInsuranceInfoVo> queryComPol(QueryDt query,CommonAgreement commonAgreement) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		RetInfoObject<CommonAgreement> retInfoObject = comAgrService.comAgrSerQuery(commonAgreement);
		if("0".equals(retInfoObject.getRetCode()) || null == retInfoObject.getListObject() || null == retInfoObject.getListObject().get(0)){
			return  new DataTable<>();
		}
		List<CoinsuranceInsuranceInfoVo> coinsuranceInsuranceInfoVos = policiesToVo(retInfoObject.getListObject().get(0).getPolicies());//对返回值进行处理
		List<CoinsuranceInsuranceInfoVo> pagingList = new ArrayList<>();//分页数据
		long page = query.getPage()/query.getRows();//第几页
		int size = query.getRows();//每页条数
		for(int i = size * (int)page; i < size * (page +1) && i < coinsuranceInsuranceInfoVos.size(); i++){
			pagingList.add(coinsuranceInsuranceInfoVos.get(i));
		}
		//创建PageData。
		PageData<CoinsuranceInsuranceInfoVo> pageData = new PageData<>();        
		//当前页号的列表数据
		pageData.setData(pagingList);
		//总记录数
		pageData.setTotalCount(coinsuranceInsuranceInfoVos.size());
		return  pageQueryService.tranToDataTable(query.getRequestId(), pageData);
	}

	/**
	 * 提交共保审批功能
	 * @author wangyanjie
	 * @date 2016年10月25日
	 */
	@RequestMapping(value = "/submit")
	public @ResponseMessage RetInfo coinsuranceState(@CurrentSession Session session,@RequestBody  CoinsuranceAuditZVo coinsuranceAuditZVo) {
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		CommonAgreement commonAgreement = new CommonAgreement();
		System.out.println(coinsuranceAuditZVo.getCoinsuranceInfoVo());
		if(coinsuranceAuditZVo.getCoinsuranceInfoVo() != null){
		commonAgreement.setAgreementNo(coinsuranceAuditZVo.getCoinsuranceInfoVo().getAgreementNo());
		}
		commonAgreement.setRemark(coinsuranceAuditZVo.getRemark());
		if("Y".equals(coinsuranceAuditZVo.getAuditFindings())){
			commonAgreement.setAgreementStat("K");
		}else {
			commonAgreement.setAgreementStat("C");
		}
		
		//操作员机构操作员代码 从session中获取
		SessionModel sessionModel = SessionUtils.getSessionModel(session);
		//审核员工号取当前操作员工号
		commonAgreement.setVclkNo(sessionModel.getClerkNo());
		//审核员机构取当前机构代码
		commonAgreement.setVclkBranchNo(sessionModel.getBranchNo());
		RetInfo result = new RetInfo();
		String branchStr1 = "";
		String branchStr2 = "";
		branchStr1 = coinsuranceAuditZVo.getCoinsuranceInfoVo().getMgrBranchNo().substring(0, 2);
		branchStr2 = sessionModel.getBranchNo().substring(0, 2);
		//判断机构操作机构和共保协议的管理机构是否在同一个省
		if(branchStr1.equals(branchStr2)){
			result = comAgrService.comAgrSerCheck(commonAgreement);
		}else{
			result.setRetCode("0");
			result.setErrMsg("审批失败，该操作员只能审批本省机构的共保协议");
		}
		
		return result;
	}

	private static CoinsuranceAuditZVo coinsuranceAuditBoToVo(CommonAgreement commonAgreement) {
		CoinsuranceAuditZVo coinsuranceAuditZVo = new CoinsuranceAuditZVo();
		if(null == commonAgreement ){
			return coinsuranceAuditZVo;
		}
		CoinsuranceInfoVo coinsuranceInfoVo = new CoinsuranceInfoVo();
		coinsuranceInfoVo.setAgreementNo(commonAgreement.getAgreementNo()); 
		coinsuranceInfoVo.setAgreementName(commonAgreement.getAgreementName());
		coinsuranceInfoVo.setApplDate(commonAgreement.getSignDate());
		coinsuranceInfoVo.setInforceDate(commonAgreement.getInForceDate());
		coinsuranceInfoVo.setTermDate(commonAgreement.getTermDate());
		coinsuranceInfoVo.setMgrBranchNo(commonAgreement.getMgrBranchNo());
		coinsuranceInfoVo.setAgreement(commonAgreement.getConvention());
		if(null != commonAgreement.getComCustomer()){
			coinsuranceInfoVo.setCustomerName(commonAgreement.getComCustomer().getGrpName());
			coinsuranceInfoVo.setIdType(commonAgreement.getComCustomer().getGrpIdType());
			coinsuranceInfoVo.setIdNo(commonAgreement.getComCustomer().getGrpIdNo());
			if(commonAgreement.getComCustomer().getLegalCode()!=null){
				coinsuranceInfoVo.setEnterpriseNature(commonAgreement.getComCustomer().getLegalCode());
			}else{
				coinsuranceInfoVo.setEnterpriseNature(commonAgreement.getComCustomer().getNatureCode());
			}
			coinsuranceInfoVo.setOccClassCode(commonAgreement.getComCustomer().getOccClassCode());
			coinsuranceInfoVo.setContactPsn(commonAgreement.getComCustomer().getContactName());
			coinsuranceInfoVo.setContactTel(commonAgreement.getComCustomer().getContactMobile());
			coinsuranceInfoVo.setZipCode(commonAgreement.getComCustomer().getAddress().getPostCode());
			String address="";
			if(null!=commonAgreement.getComCustomer().getAddress()){
			Address addressInfo = commonAgreement.getComCustomer().getAddress();
				//解决直辖市问题
				if(!StringUtils.equals(addressInfo.getProvince(),addressInfo.getCity())){
					address+=addressInfo.getProvince()
							+addressInfo.getCity();
				}else{
					address+=addressInfo.getProvince();
				}		
				address+=addressInfo.getCounty()
							+addressInfo.getTown()
							+addressInfo.getVillage()
							+addressInfo.getHomeAddress();
			}
			coinsuranceInfoVo.setAddress(address);
			coinsuranceInfoVo.setFax(commonAgreement.getComCustomer().getFax());
			coinsuranceInfoVo.setEmail(commonAgreement.getComCustomer().getContactEmail());
		} 
		coinsuranceAuditZVo.setCoinsuranceInfoVo(coinsuranceInfoVo); 
		return coinsuranceAuditZVo;
	}
	private static List<CoinsuranceInsuranceInfoVo> policiesToVo(List<Policy> policies){
		List<CoinsuranceInsuranceInfoVo> coinsuranceInsuranceInfoVos = new ArrayList<>();
		if(null == policies || policies.isEmpty()){
			return coinsuranceInsuranceInfoVos;
		}		
		for(Policy policy :policies){
			if(null != policy ){
				CoinsuranceInsuranceInfoVo coinsuranceInsuranceInfoVo = new CoinsuranceInsuranceInfoVo();
				coinsuranceInsuranceInfoVo.setBusiPrdCode(policy.getPolCode());
				coinsuranceInsuranceInfoVo.setBusiPrdCodeName(policy.getPolNameChn());
				coinsuranceInsuranceInfoVo.setAmount(policy.getFaceAmnt());
				coinsuranceInsuranceInfoVo.setPremium(policy.getPremium());
				coinsuranceInsuranceInfoVos.add(coinsuranceInsuranceInfoVo);
			}
		}
		return coinsuranceInsuranceInfoVos;
	}
	private static List<CoInsurComInFoVo> comCompaniesToVo(List<ComCompany> comCompanies){        
		List<CoInsurComInFoVo> coInsurComInFoVos = new ArrayList<>();
		if(null == comCompanies || comCompanies.isEmpty()){
			return coInsurComInFoVos;
		}
		for(ComCompany comCompany :comCompanies){
			if(null != comCompany){
				CoInsurComInFoVo coInsurComInFoVo = new CoInsurComInFoVo();
				coInsurComInFoVo.setCompanyName(comCompany.getCompanyName());
				if ("Y".equals(comCompany.getCompanyFlag())) {
					coInsurComInFoVo.setCompanyFlag("是");
				}else {
					coInsurComInFoVo.setCompanyFlag("否");
				}
				if ("M".equals(comCompany.getCoinsurType())) {
					coInsurComInFoVo.setConnIdType("主承保方");
				}else{
					coInsurComInFoVo.setConnIdType("参与承保方");
				}
				coInsurComInFoVo.setAmntPct(comCompany.getCoinsurAmntPct());
				coInsurComInFoVo.setResponsibilityPct(comCompany.getCoinsurResponsePct());
				coInsurComInFoVo.setBankCode(comCompany.getBankCode());
				coInsurComInFoVo.setAccCustName(comCompany.getBankAccName());
				coInsurComInFoVo.setBankAccNo(comCompany.getBankAccNo());
				coInsurComInFoVos.add(coInsurComInFoVo);
			}
		}
		return coInsurComInFoVos;
	}
}
