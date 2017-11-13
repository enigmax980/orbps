package com.newcore.orbps.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.model.service.para.GrpInsurApplPara;
import com.newcore.orbps.model.service.para.GrpInsuredPara;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.PsnListHolderInfo;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.HldrInfo;
import com.newcore.orbps.service.api.InsurApplContentRevise;
import com.newcore.orbps.service.api.InsurApplOperUtils;
import com.newcore.orbps.service.api.ProcCorrMioDataService;
import com.newcore.orbps.service.api.ProcCorrectDataEffective;
import com.newcore.orbps.service.business.GrpInsurApplValidator;
import com.newcore.orbps.service.business.ValidatorUtils;
import com.newcore.orbps.service.cmds.api.CreateGrpCstomerAcountService;
import com.newcore.orbps.service.cmds.api.CreatePsnCstomerAcountService;
import com.newcore.orbpsutils.dao.api.PlnmioCommonDao;
import com.newcore.orbpsutils.dao.api.PlnmioRecDao;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.LIST_TYPE;
import com.newcore.supports.dicts.UDW_RESULT;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;


/**
 * 
 * @author liushuaifeng
 *
 *         创建时间：2016年11月4日下午3:45:56
 */

@Service("InsurApplContentRevise")
public class InsurApplContentReviseImpl implements InsurApplContentRevise {

	private static Logger logger = LoggerFactory.getLogger(InsurApplContentReviseImpl.class);

	@Autowired
	MongoBaseDao mongoBaseDao;
	@Autowired
	ProcCorrMioDataService procCorrMioDataService;
	
	@Autowired
	CreatePsnCstomerAcountService restfulBatchCreatePsn;
	
	@Autowired
	CreateGrpCstomerAcountService restfulCreateGrpCstomerAcountService;	
	
	@Autowired
	ProcCorrectDataEffective reSendImpl;
	
	@Autowired
	InsurApplOperUtils insurApplLandUtils;
	
	@Autowired
	PlnmioCommonDao plnmioCommonDao;
	
	@Autowired
	PlnmioRecDao plnmioRecDao;

    @Autowired
    MongoTemplate mongoTemplate;
    
	@Override
	public RetInfo reviseGrpBasicContent(GrpInsurApplPara grpInsurApplPara) {
		RetInfo retInfo = new RetInfo();
		GrpInsurAppl grpInsurAppl, newGrpInsurAppl;
		Map<String, Object> queryMap = new HashMap<>();
		GrpInsurApplValidator grpInsurApplValidator = new GrpInsurApplValidator();
		StringBuilder errMsg = new StringBuilder();

		// 入参非空判断
		if ((null == grpInsurApplPara.getGrpInsurAppl()) || (null == grpInsurApplPara.getTraceNode())) {
			throw new BusinessException("0017");
		}
		
		if (StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkBranchNo()) ||
				StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getPclkNo()) || 
				StringUtils.isEmpty(grpInsurApplPara.getTraceNode().getProcStat())) {			
			throw new BusinessException("0019", grpInsurApplPara.getGrpInsurAppl().getApplNo());			
		}
		
		newGrpInsurAppl = grpInsurApplPara.getGrpInsurAppl();
		String applNo = newGrpInsurAppl.getApplNo();
		// 查询保单基本信息
		queryMap.put("applNo", applNo);
		grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);
		
		if (null == grpInsurAppl) {
			throw new BusinessException("0004");
		}
		BindException errors = new BindException(newGrpInsurAppl, "grpInsurAppl");
		retInfo.setApplNo(grpInsurAppl.getApplNo());
		
		grpInsurAppl.setInsurProperty(newGrpInsurAppl.getInsurProperty());
		grpInsurAppl.setApproNo(newGrpInsurAppl.getApproNo());
		grpInsurAppl.setApplBusiType(newGrpInsurAppl.getApplBusiType());
		grpInsurAppl.setCntrPrintType(newGrpInsurAppl.getCntrPrintType());
		grpInsurAppl.setListPrintType(newGrpInsurAppl.getListPrintType());
		grpInsurAppl.setVoucherPrintType(newGrpInsurAppl.getVoucherPrintType());
		grpInsurAppl.setArgueType(newGrpInsurAppl.getArgueType());
		grpInsurAppl.setArbitration(newGrpInsurAppl.getArbitration());
		
		//入参的生效截止日期在原始日期之后
		Date newForeExpDate=newGrpInsurAppl.getPaymentInfo().getForeExpDate();
		Date oldForeExpDate=grpInsurAppl.getPaymentInfo().getForeExpDate();
		if(newForeExpDate != null && oldForeExpDate != null){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String newForeDateStr = dateFormat.format(newForeExpDate);
            String oldForeDateStr = dateFormat.format(oldForeExpDate);
            //大于返回1,等于返回0，小于返回-1
            int isOverTime = newForeDateStr.compareTo(oldForeDateStr);
            //指定日期大于数据库中的执行日期
            if(isOverTime > 0){
				RetInfo ret1=insurApplLandUtils.getIsInsurApplLanding(applNo);
				//保单未生效时
				if(StringUtils.equals(ret1.getRetCode(), "1")){
					//超扣款截止日期
					if(Calendar.getInstance().getTime().after(oldForeExpDate)){
						//修改收费检查表流程控制表MONEY_IN_CHECK_TASK_QUEUE的STATUS从D改成 N
						boolean bRet=plnmioCommonDao.updateMoneyInCheckInforce(applNo);
						if(bRet){
                            //修改应收数据表【PLNMIO_REC】中首期应收数据 的应收状态  proc_stat从D改成 N
							plnmioRecDao.updatePlnmioRecInforce(applNo);
			                //更新mongodb里的被保人状态procStat从O改成N
							Query query = new Query();
			                Criteria criteria = Criteria.where("procStat").is("O").and("applNo").is(applNo).and("remark").ne(UDW_RESULT.REJECT.getDescription());
			                query.addCriteria(criteria);
			                Update update = new Update();
			                update.set("procStat", "N");
			                mongoTemplate.updateMulti(query, update, GrpInsured.class);						
						}
					}
					grpInsurAppl.getPaymentInfo().setForeExpDate(newForeExpDate);
				} else {
	
					retInfo.setRetCode("0");
					retInfo.setErrMsg("该投保单号["+applNo+"]已生效或生效在途,不能进行首期扣款截止日期的变更。");
					return retInfo;
				}
			} else if (isOverTime < 0){
	
				retInfo.setRetCode("0");
				retInfo.setErrMsg("变更后的首期扣款截止日期必须晚于当前的首期扣款截止日期。");
				return retInfo;
			}
		}
		

		/* 清单汇交，汇交人是个人做信息更新 */
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.LIST_INSUR.getKey())) {
			grpInsurApplValidator.validateListInfo(newGrpInsurAppl, errors);
			if (errors.hasErrors()) {

				retInfo.setRetCode("0");

				for (FieldError error : (List<FieldError>) errors.getFieldErrors()) {
					errMsg.append(error.getField());
					errMsg.append(", ");
					errMsg.append(error.getCode());
					errMsg.append(" | ");
				}
				retInfo.setErrMsg(errMsg.toString());
				return retInfo;
			}

			// 通过校验
			if (StringUtils.equals(grpInsurAppl.getSgType(), LIST_TYPE.PSN_SG.getKey())) {

				// 判断个人五要素是否变化
				if (StringUtils.equals(newGrpInsurAppl.getPsnListHolderInfo().getSgName(),
						grpInsurAppl.getPsnListHolderInfo().getSgName())
						&& StringUtils.equals(newGrpInsurAppl.getPsnListHolderInfo().getSgIdType(),
								grpInsurAppl.getPsnListHolderInfo().getSgIdType())
						&& StringUtils.equals(newGrpInsurAppl.getPsnListHolderInfo().getSgIdNo(),
								grpInsurAppl.getPsnListHolderInfo().getSgIdNo())
						&& StringUtils.equals(newGrpInsurAppl.getPsnListHolderInfo().getSgSex(),
								grpInsurAppl.getPsnListHolderInfo().getSgSex())
						&& StringUtils.equals(newGrpInsurAppl.getPsnListHolderInfo().getSgBirthDate().toString(),
								grpInsurAppl.getPsnListHolderInfo().getSgBirthDate().toString())) {

					newGrpInsurAppl.getPsnListHolderInfo()
							.setSgCustNo(grpInsurAppl.getPsnListHolderInfo().getSgCustNo());
					newGrpInsurAppl.getPsnListHolderInfo()
							.setSgPartyId(grpInsurAppl.getPsnListHolderInfo().getSgPartyId());

				} else {

					PsnListHolderInfo psnListHolderInfo = getPsnCustNo(newGrpInsurAppl);
					newGrpInsurAppl.setPsnListHolderInfo(psnListHolderInfo);

				}
				grpInsurAppl.setPsnListHolderInfo(newGrpInsurAppl.getPsnListHolderInfo());
				
			} else {

				// 判断团体三要素是否变化
				if (StringUtils.equals(newGrpInsurAppl.getGrpHolderInfo().getGrpName(),
						grpInsurAppl.getGrpHolderInfo().getGrpName())
						&& StringUtils.equals(newGrpInsurAppl.getGrpHolderInfo().getGrpIdType(),
								grpInsurAppl.getGrpHolderInfo().getGrpIdType())
						&& StringUtils.equals(newGrpInsurAppl.getGrpHolderInfo().getGrpIdNo(),
								grpInsurAppl.getGrpHolderInfo().getGrpIdNo())) {

					newGrpInsurAppl.getGrpHolderInfo().setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
					newGrpInsurAppl.getGrpHolderInfo().setPartyId(grpInsurAppl.getGrpHolderInfo().getPartyId());

				} else {

					GrpHolderInfo grpHolderInfo = getGrpCustNo(newGrpInsurAppl);
					newGrpInsurAppl.setGrpHolderInfo(grpHolderInfo);
				}
				
				grpInsurAppl.setGrpHolderInfo(newGrpInsurAppl.getGrpHolderInfo());
			}

		}
		// 如果是团单
		if (StringUtils.equals(grpInsurAppl.getCntrType(), CNTR_TYPE.GRP_INSUR.getKey())) {
			grpInsurApplValidator.validateGrpInfo(newGrpInsurAppl, errors);

			if (errors.hasErrors()) {
				retInfo.setRetCode("0");

				for (FieldError error : (List<FieldError>) errors.getFieldErrors()) {
					errMsg.append(error.getField());
					errMsg.append(", ");
					errMsg.append(error.getCode());
					errMsg.append(" | ");
				}
				retInfo.setErrMsg(errMsg.toString());
				return retInfo;
			}
			// 判断团体三要素是否变化
			if (StringUtils.equals(grpInsurAppl.getGrpHolderInfo().getGrpName(),
					grpInsurAppl.getGrpHolderInfo().getGrpName())
					&& StringUtils.equals(grpInsurAppl.getGrpHolderInfo().getGrpIdType(),
							grpInsurAppl.getGrpHolderInfo().getGrpIdType())
					&& StringUtils.equals(grpInsurAppl.getGrpHolderInfo().getGrpIdNo(),
							grpInsurAppl.getGrpHolderInfo().getGrpIdNo())) {

				newGrpInsurAppl.getGrpHolderInfo().setGrpCustNo(grpInsurAppl.getGrpHolderInfo().getGrpCustNo());
				newGrpInsurAppl.getGrpHolderInfo().setPartyId(grpInsurAppl.getGrpHolderInfo().getPartyId());

			} else {
				GrpHolderInfo grpHolderInfo = getGrpCustNo(newGrpInsurAppl);
				newGrpInsurAppl.setGrpHolderInfo(grpHolderInfo);
			}
			
			grpInsurAppl.setGrpHolderInfo(newGrpInsurAppl.getGrpHolderInfo());
		}
		
		
		retInfo.setApplNo(applNo);
		//调用订正
		RetInfo retInfoCorrect = procCorrMioDataService.correctDate(applNo);
		if(StringUtils.equals("0",retInfoCorrect.getRetCode())){
			
			retInfo.setRetCode("0");
			retInfo.setErrMsg("不需要订正:" + retInfoCorrect.getErrMsg());
		}else{
			
			RetInfo ret = reSendImpl.correctGrpInsurAppl(grpInsurAppl);
			if(StringUtils.equals("1",ret.getRetCode())){
				
				// 插入操作轨迹
				mongoBaseDao.remove(GrpInsurAppl.class, queryMap);
				mongoBaseDao.insert(grpInsurAppl);
				grpInsurApplPara.getTraceNode().setProcDate(new Date());
				mongoBaseDao.updateOperTrace(applNo, grpInsurApplPara.getTraceNode());
				retInfo.setRetCode("1");				
			} else {
				
				retInfo.setRetCode("0");
				retInfo.setErrMsg("订正失败");				
			}
		}
		return retInfo;
	}

	@Override
	public RetInfo reviseGrpInsuredContent(GrpInsuredPara grpInsuredPara) {
		RetInfo retInfo = new RetInfo();
		GrpInsured grpInsured, newGrpInsured;
		GrpInsurAppl grpInsurAppl;
		Map<String, Object> queryMap = new HashMap<>();
		Map<String, Object> queryBasicMap = new HashMap<>();

		// 入参非空判断  
		if ((null == grpInsuredPara.getGrpInsured()) || (null == grpInsuredPara.getTraceNode())) {
			throw new BusinessException("0017");
		}
		if (StringUtils.isEmpty(grpInsuredPara.getTraceNode().getPclkBranchNo())  ||
				StringUtils.isEmpty(grpInsuredPara.getTraceNode().getPclkNo()) ||
				StringUtils.isEmpty(grpInsuredPara.getTraceNode().getProcStat())) {
			throw new BusinessException("0019");
		}
		
		newGrpInsured = grpInsuredPara.getGrpInsured();
		String applNo = newGrpInsured.getApplNo();
		queryBasicMap.put("applNo", applNo);
		// 查询保单基本信息
		grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryBasicMap);
		if (null == grpInsurAppl) {
			throw new BusinessException("0004");
		}

		// 查询被保人信息
		queryMap.put("applNo", applNo);
		queryMap.put("ipsnNo", newGrpInsured.getIpsnNo());
		grpInsured = (GrpInsured) mongoBaseDao.findOne(GrpInsured.class, queryMap);
		if (null == grpInsured) {
			throw new BusinessException("0003");
		}

		retInfo = validateGrpInsured(newGrpInsured);

		if (StringUtils.equals(retInfo.getRetCode(), "0")) {
			return retInfo;
		}

		// 被保人五要素是否变更
		// 判断个人五要素是否变化
		if (StringUtils.equals(newGrpInsured.getIpsnName(), grpInsured.getIpsnName())
				&& StringUtils.equals(newGrpInsured.getIpsnIdType(), grpInsured.getIpsnIdType())
				&& StringUtils.equals(newGrpInsured.getIpsnIdNo(), grpInsured.getIpsnIdNo())
				&& StringUtils.equals(newGrpInsured.getIpsnSex(), grpInsured.getIpsnSex()) && StringUtils.equals(
						newGrpInsured.getIpsnBirthDate().toString(), grpInsured.getIpsnBirthDate().toString())) {

			// 五要素没有变化，无需开户
			newGrpInsured.setIpsnCustNo(grpInsured.getIpsnCustNo());
			newGrpInsured.setIpsnPartyId(grpInsured.getIpsnPartyId());
		} else {
			JSONObject json = new JSONObject();
			json.put("PROV_BRANCH_NO", grpInsurAppl.getProvBranchNo());
			json.put("SRC_SYS", "ORBPS");// 系统来源
			json.put("CUST_OAC_BRANCH_NO", grpInsurAppl.getMgrBranchNo());// 管理机构
			json.put("APPL_NO", grpInsurAppl.getApplNo());
			json.put("ROLE", "1");
			json.put("IPSN_NO", "");
			json.put("BNFRLEVEL", "");
			json.put("NAME", newGrpInsured.getIpsnName());
			json.put("ID_TYPE", newGrpInsured.getIpsnIdType());
			json.put("ID_NO", newGrpInsured.getIpsnIdNo());
			json.put("SEX", newGrpInsured.getIpsnSex());
			json.put("BIRTH_DATE", DateFormatUtils.format(newGrpInsured.getIpsnBirthDate(), "yyyy-MM-dd"));
			String custNo = "";
			String partyId = "";
			visitIpsnCustNo(json, grpInsurAppl.getMgrBranchNo(), custNo, partyId);
			newGrpInsured.setIpsnCustNo(custNo);
			newGrpInsured.setIpsnPartyId(partyId);
		}

		if (grpInsured.getHldrInfo() != null) {
			HldrInfo hldrInfoNew = newGrpInsured.getHldrInfo();
			HldrInfo hldrInfoDB = grpInsured.getHldrInfo();
			if (StringUtils.equals(hldrInfoNew.getHldrName(), hldrInfoDB.getHldrName())
					&& StringUtils.equals(hldrInfoNew.getHldrIdType(), hldrInfoDB.getHldrIdType())
					&& StringUtils.equals(hldrInfoNew.getHldrIdNo(), hldrInfoDB.getHldrIdNo())
					&& StringUtils.equals(hldrInfoNew.getHldrSex(), hldrInfoDB.getHldrSex()) && StringUtils.equals(
							hldrInfoNew.getHldrBirthDate().toString(), hldrInfoDB.getHldrBirthDate().toString())) {
				newGrpInsured.getHldrInfo().setHldrPartyId(hldrInfoDB.getHldrPartyId());
				newGrpInsured.getHldrInfo().setHldrCustNo(hldrInfoDB.getHldrCustNo());
			} else {
				JSONObject json = new JSONObject();
				json.put("PROV_BRANCH_NO", grpInsurAppl.getProvBranchNo());
				json.put("SRC_SYS", "ORBPS");// 系统来源
				json.put("CUST_OAC_BRANCH_NO", grpInsurAppl.getMgrBranchNo());// 管理机构
				json.put("APPL_NO", grpInsurAppl.getApplNo());
				json.put("ROLE", "1");
				json.put("IPSN_NO", "");
				json.put("BNFRLEVEL", "");
				json.put("NAME", hldrInfoNew.getHldrName());
				json.put("ID_TYPE", hldrInfoNew.getHldrIdType());
				json.put("ID_NO", hldrInfoNew.getHldrIdNo());
				json.put("SEX", hldrInfoNew.getHldrSex());
				json.put("BIRTH_DATE", DateFormatUtils.format(hldrInfoNew.getHldrBirthDate(), "yyyy-MM-dd"));
				String custNo = "";
				String partyId = "";
				visitIpsnCustNo(json, grpInsurAppl.getMgrBranchNo(), custNo, partyId);
				newGrpInsured.getHldrInfo().setHldrPartyId(custNo);
				newGrpInsured.getHldrInfo().setHldrCustNo(partyId);
			}
		}
		// 验证受益人
		BnfrInfo bnfrInfoNew;
		List<BnfrInfo> bnfrInfoListNew = newGrpInsured.getBnfrInfoList();
		if(null != bnfrInfoListNew){
			for (int i = 0; i < bnfrInfoListNew.size(); i++) {
				bnfrInfoNew = bnfrInfoListNew.get(i);
				JSONObject json = new JSONObject();
				json.put("PROV_BRANCH_NO", grpInsurAppl.getProvBranchNo());
				json.put("SRC_SYS", "ORBPS");// 系统来源
				json.put("CUST_OAC_BRANCH_NO", grpInsurAppl.getMgrBranchNo());// 管理机构
				json.put("APPL_NO", grpInsurAppl.getApplNo());
				json.put("ROLE", "1");
				json.put("IPSN_NO", "");
				json.put("BNFRLEVEL", "");
				json.put("NAME", bnfrInfoNew.getBnfrName());
				json.put("ID_TYPE",bnfrInfoNew.getBnfrIdType());
				json.put("ID_NO", bnfrInfoNew.getBnfrIdNo());
				json.put("SEX", bnfrInfoNew.getBnfrSex());
				json.put("BIRTH_DATE", DateFormatUtils.format(bnfrInfoNew.getBnfrBirthDate(), "yyyy-MM-dd"));
				String custNo = "";
				String partyId = "";
				visitIpsnCustNo(json, grpInsurAppl.getMgrBranchNo(), custNo, partyId);
				bnfrInfoNew.setBnfrPartyId(partyId);
				bnfrInfoNew.setBnfrCustNo(custNo);
				bnfrInfoListNew.add(bnfrInfoNew);
			}
			newGrpInsured.setBnfrInfoList(bnfrInfoListNew);
		}
		
		retInfo.setApplNo(applNo);
		//调用订正
		RetInfo retInfoCorrect = procCorrMioDataService.correctDate(applNo);
		if(StringUtils.equals("0",retInfoCorrect.getRetCode())){
			
			retInfo.setRetCode("0");
			retInfo.setErrMsg("不需要订正:" + retInfoCorrect.getErrMsg());
		}else{
			
			RetInfo ret = reSendImpl.correctGrpInsurAppl(grpInsurAppl);
			if(StringUtils.equals("Y",ret.getRetCode())){

				// 插入操作轨迹
				grpInsuredPara.getTraceNode().setProcDate(new Date());
				mongoBaseDao.updateOperTrace(applNo, grpInsuredPara.getTraceNode());
				mongoBaseDao.remove(GrpInsured.class, queryMap);
				mongoBaseDao.insert(newGrpInsured);
				retInfo.setRetCode("1");		
			} else {
				
				retInfo.setRetCode("0");
				retInfo.setErrMsg("订正失败");				
			}
		}
		return retInfo;
	}

	private RetInfo validateGrpInsured(GrpInsured grpInsured) {
		StringBuilder bnfrErrMsg = new StringBuilder();
		RetInfo retInfo = new RetInfo();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		// 与主被保人关系
		if (null == grpInsured.getIpsnRtMstIpsn() || grpInsured.getIpsnRtMstIpsn().isEmpty()) {
			bnfrErrMsg.append("|与主被保险人关系为空，被保人信息添加失败|");
		}

		// 被保险人编号
		if (null == grpInsured.getIpsnNo()) {
			bnfrErrMsg.append("|被保险人编号为空，被保人信息添加失败|");
		}
		// 被保人姓名
		if (null == grpInsured.getIpsnName() || grpInsured.getIpsnName().isEmpty()) {
			bnfrErrMsg.append("|被保险人姓名为空，被保人信息添加失败|");
		}
		// 被保险人类型
		if (null == grpInsured.getIpsnType() || grpInsured.getIpsnType().isEmpty()) {
			bnfrErrMsg.append("|被保险人类型为空，被保人信息添加失败|");
		}
		// 被保险人性别
		if (null == grpInsured.getIpsnSex() || grpInsured.getIpsnSex().isEmpty()) {
			bnfrErrMsg.append("|被保险人性别为空，被保人信息添加失败|");
		}
		// 被保人出生日期
		if (null == grpInsured.getIpsnBirthDate()) {
			bnfrErrMsg.append("|被保人出生日期为空或格式有误，被保人信息添加失败|");
		} else {
			String str = sdf.format(grpInsured.getIpsnBirthDate());
			String testDate = ValidatorUtils.testDate(str);
			if (null == testDate || testDate.isEmpty()) {
				bnfrErrMsg.append("|被保人出生日期格式错误，被保人信息添加失败|");
			}
		}
		// 证件类别不能为空
		if (null == grpInsured.getIpsnIdType() || grpInsured.getIpsnIdType().isEmpty()) {
			bnfrErrMsg.append("|被保人证件类别为空，被保人信息添加失败|");
		}
		// 如果是身份证，则对证件号码进行校验
		if (StringUtils.equals(grpInsured.getIpsnIdType(), "I") && !StringUtils.isEmpty(grpInsured.getIpsnIdNo())) {
			String validRet = ValidatorUtils.validIdNo(grpInsured.getIpsnIdNo());
			if (!StringUtils.equals(validRet, "OK")) {
				bnfrErrMsg.append("|" + validRet + "被保人信息添加失败|");
			}
		}
		// 证件号码不能为空
		if (StringUtils.isEmpty(grpInsured.getIpsnIdNo())) {
			bnfrErrMsg.append("|被保人证件号码为空,被保人信息添加失败|");
			// 证件类别长度为8-18
		} else if (grpInsured.getIpsnIdNo().length() < 8 || grpInsured.getIpsnIdNo().length() > 18) {
			bnfrErrMsg.append("|被保人证件号码长度有误,被保人信息添加失败|");
		}
		// 职业代码
		if (null == grpInsured.getIpsnOccCode() || grpInsured.getIpsnOccCode().isEmpty()) {
			bnfrErrMsg.append("|职业代码为空,被保人信息添加失败|");
			// 职业代码长度为6
		} else if (6 != grpInsured.getIpsnOccCode().length()) {
			bnfrErrMsg.append("|职业代码长度有误,被保人信息添加失败|");
		}
		// 是否异常告知
		if (null == grpInsured.getNotificaStat() || grpInsured.getNotificaStat().isEmpty()) {
			bnfrErrMsg.append("|异常告知为空,被保人信息添加失败|");
		}
		// 受益人信息校验
		if (null != grpInsured.getBnfrInfoList() && !grpInsured.getBnfrInfoList().isEmpty()) {
			
			for (int i = 0; i < grpInsured.getBnfrInfoList().size(); i++) {
				BnfrInfo bnfrInfo = grpInsured.getBnfrInfoList().get(i);
				if (null != bnfrInfo && StringUtils.isEmpty(bnfrInfo.getBnfrName())) {
					bnfrErrMsg.append("|受益人姓名为空,被保人信息添加失败|");
	
				}
				if (null != bnfrInfo && null == bnfrInfo.getBnfrLevel()) {
					bnfrErrMsg.append("|受益人顺序为空,被保人信息添加失败|");
				}
				if (null != bnfrInfo && null == bnfrInfo.getBnfrProfit()) {
					bnfrErrMsg.append("|受益人份额为空,被保人信息添加失败|");
				}
				if (null != bnfrInfo && StringUtils.isEmpty(bnfrInfo.getBnfrSex())) {
					bnfrErrMsg.append("|受益人性别为空,被保人信息添加失败|");
				}
				if (null != bnfrInfo && StringUtils.equals(bnfrInfo.getBnfrIdType(), "I")
						&& !StringUtils.isEmpty(bnfrInfo.getBnfrIdNo())) {
					String validRet = ValidatorUtils.validIdNo(bnfrInfo.getBnfrIdNo());
					if (!StringUtils.equals(validRet, "OK")) {
						bnfrErrMsg.append(validRet);
					}
				}
				if (null != bnfrInfo && StringUtils.isEmpty(bnfrInfo.getBnfrRtIpsn())) {
					bnfrErrMsg.append("|受益人与被保险人关系为空|");
				}
			}
		}
		if(!StringUtils.isEmpty(bnfrErrMsg)){
			retInfo.setErrMsg(bnfrErrMsg.toString());
			retInfo.setRetCode("0");
			return retInfo;
		}
		retInfo.setRetCode("1");
		return retInfo;
	}

	// 个人开户
	private void visitIpsnCustNo(JSONObject json, String mgrBranchNo, String custNo, String partyId) {
		List<Map<String, Object>> list=new ArrayList<>();
		Map<String, Object> map=new HashMap<>();
		map=JSON.parseObject(json.toJSONString());
		list.add(map);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String retjson = restfulBatchCreatePsn.createPsnCstomerAcount(list);
		logger.info("个人汇交人开户发送报文：" + json);
		if (retjson == null) {
			throw new BusinessException("0006");
		}
		logger.info("个人汇交人开户返回报文：" + retjson);
		JSONObject jsonObject = JSON.parseArray(retjson).getJSONObject(0);
		custNo = jsonObject.getString("CUST_NO");
		partyId = jsonObject.getString("PARTY_ID");
	}

	// 法人开户
	private GrpHolderInfo getGrpCustNo(GrpInsurAppl grpInsurAppl) {
		Map<String, Object> json = new HashMap<>();
		String provBranchNo = grpInsurAppl.getProvBranchNo();
		GrpHolderInfo grpHolderInfo = grpInsurAppl.getGrpHolderInfo();
		json.put("CUST_NO", "");
		json.put("PROV_BRANCH_NO", provBranchNo == null ? "120000" : provBranchNo);
		json.put("SRC_SYS", "ORBPS");// 系统来源
		String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
		json.put("CUST_OAC_BRANCH_NO", mgrBranchNo == null ? "120000" : mgrBranchNo);// 管理机构
		json.put("NAME", grpHolderInfo.getGrpName());
		json.put("OLD_NAME", grpHolderInfo.getFormerGrpName() == null ? "" : grpHolderInfo.getFormerGrpName());
		json.put("ID_TYPE", grpHolderInfo.getGrpIdType());
		json.put("ID_NO", grpHolderInfo.getGrpIdNo());
		json.put("LEGAL_CODE", grpHolderInfo.getLegalCode() == null ? "" : grpHolderInfo.getLegalCode());
		json.put("NATURE_CODE", grpHolderInfo.getNatureCode() == null ? "" : grpHolderInfo.getNatureCode());
		json.put("OCC_CLASS_CODE", grpHolderInfo.getOccClassCode());
		json.put("CORP_REP", grpHolderInfo.getCorpRep() == null ? "" : grpHolderInfo.getCorpRep());
		json.put("CONTACT_PSN", grpHolderInfo.getContactName() == null ? "" : grpHolderInfo.getContactName());
		json.put("CONTACT_PSN_SEX", "");
		if (null == grpHolderInfo.getNumOfEnterprise()) {
			json.put("NUM_OF_EMP", "0");
		} else {
			json.put("NUM_OF_EMP", grpHolderInfo.getNumOfEnterprise().toString());
		}
		logger.info("法人开户发送报文：" + json);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String retjson = restfulCreateGrpCstomerAcountService.createGrpCstomerAcount(json);
		if (retjson == null) {
			throw new BusinessException("0006");
		}
		logger.info("法人开户返回报文：" + retjson);
		JSONObject jsonObject = JSON.parseObject(retjson);
		String custNo = jsonObject.getString("CUST_NO");
		String partyId = jsonObject.getString("PARTY_ID");
		grpHolderInfo.setGrpCustNo(custNo);
		grpHolderInfo.setPartyId(partyId);
		return grpHolderInfo;
	}

	// 个人汇交人开户
	private PsnListHolderInfo getPsnCustNo(GrpInsurAppl grpInsurAppl) {
		List<Map<String, Object>> list=new ArrayList<>();
		Map<String, Object> json = new HashMap<>();
		String provBranchNo = grpInsurAppl.getProvBranchNo();
		PsnListHolderInfo psnListHolderInfo = grpInsurAppl.getPsnListHolderInfo();
		json.put("PROV_BRANCH_NO", provBranchNo == null ? "120000" : provBranchNo);
		json.put("SRC_SYS", "ORBPS");// 系统来源
		String mgrBranchNo = grpInsurAppl.getMgrBranchNo();
		json.put("CUST_OAC_BRANCH_NO", mgrBranchNo == null ? "120000" : mgrBranchNo);// 管理机构
		json.put("APPL_NO", grpInsurAppl.getApplNo());
		json.put("ROLE", "1");
		json.put("IPSN_NO", "");
		json.put("BNFRLEVEL", "");
		json.put("NAME", psnListHolderInfo.getSgName());
		json.put("ID_TYPE", psnListHolderInfo.getSgIdType());
		json.put("ID_NO", psnListHolderInfo.getSgIdNo());
		json.put("SEX", psnListHolderInfo.getSgSex());
		json.put("BIRTH_DATE", DateFormatUtils.format(psnListHolderInfo.getSgBirthDate(), "yyyy-MM-dd"));
		list.add(json);
		CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
		HeaderInfoHolder.setOutboundHeader(headerInfo);
		String retjson = restfulBatchCreatePsn.createPsnCstomerAcount(list);
		logger.info("个人汇交人开户发送报文：" + json);
		if (retjson == null) {
			throw new BusinessException("0006");
		}
		logger.info("个人汇交人开户返回报文：" + retjson);
		JSONObject jsonObject = JSON.parseArray(retjson).getJSONObject(0);
		String custNo = jsonObject.getString("CUST_NO");
		String partyId = jsonObject.getString("PARTY_ID");
		psnListHolderInfo.setSgCustNo(custNo);
		psnListHolderInfo.setSgPartyId(partyId);
		return psnListHolderInfo;
	}
	
}
