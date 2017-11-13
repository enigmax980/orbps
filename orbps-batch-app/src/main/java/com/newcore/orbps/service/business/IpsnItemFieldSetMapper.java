package com.newcore.orbps.service.business;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpPolicy;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnStateGrp;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.models.service.bo.grpinsurappl.SubPolicy;
import com.newcore.orbps.models.service.bo.grpinsured.AccInfo;
import com.newcore.orbps.models.service.bo.grpinsured.BnfrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.HldrInfo;
import com.newcore.orbps.models.service.bo.grpinsured.SubState;
import com.newcore.orbps.service.api.ErrFileCreatService;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.ExtDictItem;
import com.newcore.supports.service.api.DictService;

/**
 * @author wangxiao
 * 创建时间：2016年8月16日上午11:09:58
 */
public class IpsnItemFieldSetMapper implements FieldSetMapper<GrpInsured>{
	
	private Logger logger = LoggerFactory.getLogger(IpsnItemFieldSetMapper.class);
	private final static String OCC_CODE_TABLE = "OCC_SUBCLS_CODE";
	private String applNo;
	private GrpInsurAppl grpInsurAppl; 
	
	private MongoBaseDao mongoBaseDao;
	
	@Autowired
	private ErrFileCreatService errFileCreatService;
	
	@Autowired
	private DictService dictService; /*redis获取根据职业类别获取职业风险等级*/
	
	/**
	 * 获取团单基本信息
	 */
	private GrpInsurAppl getGrpInsurAppl(){
		Map<String, Object> map = new HashMap<>();
		map.put("applNo", applNo);
		return (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
	}
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.file.mapping.FieldSetMapper#mapFieldSet(org.springframework.batch.item.file.transform.FieldSet)
	 */
	@Override
	public synchronized GrpInsured mapFieldSet(FieldSet fieldSet) throws BindException {

		GrpInsured grpInsured = new GrpInsured();
		ErrorGrpInsured errorGrpInsured = new ErrorGrpInsured();
		grpInsured.setApplNo(applNo);
		errorGrpInsured.setApplNo(applNo);
		if(grpInsurAppl == null || !StringUtils.equals(grpInsurAppl.getApplNo(),applNo)){
			grpInsurAppl = getGrpInsurAppl();			
		}			
		if(grpInsurAppl==null){
			logger.error(applNo+":根据投保单号查不到保单基本数据。");
			errorGrpInsured.setRemark("“"+applNo+":根据投保单号查不到保单基本数据”");
			mongoBaseDao.insert(errorGrpInsured);
			try {
				errFileCreatService.creatCsvFile(applNo);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("0018",applNo+":根据投保单号查不到保单基本数据。|生成错误文件失败|");
			}
			throw new BusinessException("0018",applNo+":根据投保单号查不到保单基本数据。");
		}
		int subscript = 0;
		int policyNum = fieldSet.readInt(subscript++);
		List<Policy> policylist = grpInsurAppl.getApplState().getPolicyList();
		//文件列宽校验
		int rowNum = 46+policyNum;
		//清汇列宽加7
		if(StringUtils.equals(grpInsurAppl.getCntrType(),CNTR_TYPE.LIST_INSUR.getKey())){
			rowNum +=7;
		}
		//校验清单列数
		if(fieldSet.getFieldCount()-1!=rowNum){
			StringBuilder err = new StringBuilder();
			err.append("“applNo：");
			err.append(applNo);
			if(fieldSet.getFieldCount()-1>1){
				err.append("，ipsnNo:");
				err.append(fieldSet.readString(2));
			}
			err.append("，清单文件模板有误，模版列数：");
			err.append(rowNum);
			err.append("，实际列数： ");
			err.append(fieldSet.getFieldCount()-1);
			err.append("”");
			logger.error(err.toString());
			errorGrpInsured.setRemark(err.toString());
			mongoBaseDao.insert(errorGrpInsured);
			try {
				errFileCreatService.creatCsvFile(applNo);
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw new BusinessException("0018",err.toString()+"|生成错误文件失败|");
			}
			throw new BusinessException("0018", err.toString());
		}
		try{
			grpInsured.setProcStat("N");
			//添加cgNo
			grpInsured.setCgNo(grpInsurAppl.getCgNo());
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setProcFlag(fieldSet.readString(subscript-1));
			}else{
				grpInsured.setProcFlag("N");
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setIpsnNo(fieldSet.readLong(subscript-1));
			}
			if(StringUtils.equals(grpInsurAppl.getCntrType(),"L")){
				HldrInfo hldrInfo = new HldrInfo();
				hldrInfo.setHldrName(fieldSet.readString(subscript++));
				hldrInfo.setHldrSex(fieldSet.readString(subscript++));
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					hldrInfo.setHldrBirthDate(parseDate(fieldSet.readString(subscript-1)));
				}
				hldrInfo.setHldrIdType(fieldSet.readString(subscript++));
				hldrInfo.setHldrIdNo(fieldSet.readString(subscript++));
				hldrInfo.setHldrMobilePhone(fieldSet.readString(subscript++));
				grpInsured.setRelToHldr(fieldSet.readString(subscript++));
				grpInsured.setHldrInfo(hldrInfo);
			}
			grpInsured.setIpsnName(fieldSet.readString(subscript++));
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setMasterIpsnNo(fieldSet.readLong(subscript-1));
			}
			grpInsured.setIpsnRtMstIpsn(fieldSet.readString(subscript++));
			grpInsured.setIpsnType(fieldSet.readString(subscript++));
			grpInsured.setIpsnSex(fieldSet.readString(subscript++));
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				Date date = parseDate(fieldSet.readString(subscript-1));
				grpInsured.setIpsnBirthDate(date);
				grpInsured.setIpsnAge(getAgeByBirthday(date));
			}
			grpInsured.setIpsnIdType(fieldSet.readString(subscript++));
			grpInsured.setIpsnIdNo(fieldSet.readString(subscript++));
			grpInsured.setIpsnMobilePhone(fieldSet.readString(subscript++));
			grpInsured.setIpsnEmail(fieldSet.readString(subscript++));
			String ipsnOccCode = fieldSet.readString(subscript++);
			grpInsured.setIpsnOccCode(ipsnOccCode);
			//根据职业代码获取职业风险等级
			if(!StringUtils.isEmpty(ipsnOccCode) && ipsnOccCode.length()>=6){
				
				ExtDictItem  extDictItem = dictService.getExtDictionaryItem(OCC_CODE_TABLE, ipsnOccCode);
				if (extDictItem != null) {
					grpInsured.setIpsnOccClassLevel(extDictItem.getExtKeys().get(0));
				}		
			}
			grpInsured.setIpsnCompanyLoc(fieldSet.readString(subscript++));
			grpInsured.setIpsnRefNo(fieldSet.readString(subscript++));
			grpInsured.setInServiceFlag(fieldSet.readString(subscript++));
			grpInsured.setIpsnSss(fieldSet.readString(subscript++));
			grpInsured.setIpsnSsc(fieldSet.readString(subscript++));
			grpInsured.setIpsnSsn(fieldSet.readString(subscript++));
			String notificaStat = fieldSet.readString(subscript++);
			grpInsured.setNotificaStat(StringUtils.isEmpty(notificaStat)?YES_NO_FLAG.NO.getKey():notificaStat);
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setFeeGrpNo(fieldSet.readLong(subscript-1));
			}
			grpInsured.setLevelCode(fieldSet.readString(subscript++));
			List<SubState> subStateList = new ArrayList<>();
			//获取险种属组号
			Long claimIpsnGrpNo = null;
			if(policyNum>0){
				claimIpsnGrpNo = StringUtils.isEmpty(fieldSet.readString(subscript++))?null:fieldSet.readLong(subscript-1);
			}
			//获取被保人险种信息
			for(int i=0;i<(policyNum-1)/3;i++){
				String polCode = fieldSet.readString(subscript++);
				String polName = getPolicyNameForPolicyList(policylist,polCode);
				Double faceAmnt = StringUtils.isEmpty(fieldSet.readString(subscript++))?null:fieldSet.readDouble(subscript-1);
				Double premium = StringUtils.isEmpty(fieldSet.readString(subscript++))?null:fieldSet.readDouble(subscript-1);
				Double sumPremium = premium;
				if(!StringUtils.isEmpty(polCode) || faceAmnt!=null || premium!=null){
					SubState subState = new SubState();
					subState.setClaimIpsnGrpNo(claimIpsnGrpNo);
					subState.setPolCode(polCode);
					subState.setPolNameChn(polName);
					subState.setFaceAmnt(faceAmnt);
					subState.setPremium(premium);
					subState.setSumPremium(sumPremium);
					subStateList.add(subState);
				}
			}
			//如果清单中险种信息为空，团单基本信息存在分组时获取基本信息分组中险种信息
			List<IpsnStateGrp> ipsnStateGrpList = grpInsurAppl.getIpsnStateGrpList();
			if(subStateList.isEmpty() && ipsnStateGrpList !=null){
				getSubStateListFromGrpInfo(subStateList, ipsnStateGrpList, claimIpsnGrpNo, policylist);
			}
			
			if(!subStateList.isEmpty()){
				grpInsured.setSubStateList(subStateList);
			}
			List<BnfrInfo> bnfrInfoList = new ArrayList<>();
			BnfrInfo bnfrInfo = new BnfrInfo();
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrName(fieldSet.readString(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrLevel(fieldSet.readLong(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrProfit(fieldSet.readDouble(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrSex(fieldSet.readString(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				Date date = parseDate(fieldSet.readString(subscript-1));
				bnfrInfo.setBnfrBirthDate(date);
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrIdType(fieldSet.readString(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrIdNo(fieldSet.readString(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrMobilePhone(fieldSet.readString(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				bnfrInfo.setBnfrRtIpsn(fieldSet.readString(subscript-1));
			}
			if(bnfrInfo.getBnfrBirthDate()!=null
				||bnfrInfo.getBnfrCustNo()!=null
				||bnfrInfo.getBnfrIdNo()!=null
				||bnfrInfo.getBnfrIdType()!=null
				||bnfrInfo.getBnfrLevel()!=null
				||bnfrInfo.getBnfrMobilePhone()!=null
				||bnfrInfo.getBnfrName()!=null
				||bnfrInfo.getBnfrPartyId()!=null
				||bnfrInfo.getBnfrProfit()!=null
				||bnfrInfo.getBnfrRtIpsn()!=null
				||bnfrInfo.getBnfrSex()!=null){
				bnfrInfoList.add(bnfrInfo);
				grpInsured.setBnfrInfoList(bnfrInfoList);
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setTyNetPayAmnt(fieldSet.readDouble(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setIpsnPayAmount(fieldSet.readDouble(subscript-1));
			}
			if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
				grpInsured.setGrpPayAmount(fieldSet.readDouble(subscript-1));
			}
			List<AccInfo> accInfoList = new ArrayList<>();
			for(int i=0;i<2;i++){
				AccInfo accInfo = new AccInfo();
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					accInfo.setSeqNo(fieldSet.readLong(subscript-1));
				}
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					accInfo.setIpsnPayAmnt(fieldSet.readDouble(subscript-1));
				}
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					accInfo.setIpsnPayPct(fieldSet.readDouble(subscript-1));
				}
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					accInfo.setBankAccName(fieldSet.readString(subscript-1));
				}
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					accInfo.setBankCode(fieldSet.readString(subscript-1));
				}
				if(!StringUtils.isEmpty(fieldSet.readString(subscript++))){
					accInfo.setBankAccNo(fieldSet.readString(subscript-1));
				}
				if(accInfo.getSeqNo()!=null
					||accInfo.getIpsnPayAmnt()!=null
					||accInfo.getIpsnPayPct()!=null
					||accInfo.getBankAccName()!=null
					||accInfo.getBankCode()!=null
					||accInfo.getBankAccNo()!=null){
					accInfoList.add(accInfo);
				}
			}
			if(!accInfoList.isEmpty()){
				grpInsured.setAccInfoList(accInfoList);
			}
		}catch(Exception e){
			logger.error(applNo+": 第"+getColCode(subscript)+"列解析异常");
			errorGrpInsured.setRemark("“"+applNo+": 第"+getColCode(subscript)+"列解析异常”");
			mongoBaseDao.insert(errorGrpInsured);
			try {
				errFileCreatService.creatCsvFile(applNo);
			} catch (IOException e1) {
				logger.error(e1.getMessage(), e1);
				throw new BusinessException("0018",e.getMessage()+"|生成错误文件失败|");
			}
			throw e;
		}
		return grpInsured;
	}
	
	/**
	 * 根据用户生日计算年龄
	 */
	private Long getAgeByBirthday(Date birthday) {
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException(
					"The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth 
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth 
				age--;
			}
		}
		return (long)age;
	}
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	public MongoBaseDao getMongoBaseDao() {
		return mongoBaseDao;
	}
	public void setMongoBaseDao(MongoBaseDao mongoBaseDao) {
		this.mongoBaseDao = mongoBaseDao;
	}
	private Date parseDate(String stringDate){
		Date date = null;
		try {
			date = DateUtils.parseDate(stringDate, "yyyy-MM-dd","yyyy/MM/dd","yyyyMMdd","yyyy.MM.dd");
		} catch (ParseException e) {
			logger.error(applNo+":日期解析失败");
		}
		return date;
	}
	private String getPolicyNameForPolicy(Policy policy,String polCode){
		String policyName = "";
		if(StringUtils.equals(policy.getPolCode(),polCode)){
			policyName = policy.getPolNameChn();
		}
		if(policy.getSubPolicyList()!=null &&
			policy.getSubPolicyList().size()!=0){
			for(SubPolicy subPolicy:policy.getSubPolicyList()){
				if(StringUtils.equals(polCode, subPolicy.getSubPolCode())){
					policyName = subPolicy.getSubPolName();
				}
			}
		}
		return policyName;
	}
	private String getPolicyNameForPolicyList(List<Policy> policylist,String polCode){
		String polName = "";
		for(int j=0;j<policylist.size();j++){
			polName = getPolicyNameForPolicy(policylist.get(j), polCode);
			if(!StringUtils.isEmpty(polName)){
				break;
			}
		}
		return polName;
	}
	private void getSubStateListFromGrpInfo(List<SubState> subStateList,List<IpsnStateGrp> ipsnStateGrpList,Long claimIpsnGrpNo,List<Policy> policylist){
		for(IpsnStateGrp ipsnStateGrp:ipsnStateGrpList){
			for(GrpPolicy grpPolicy:ipsnStateGrp.getGrpPolicyList()){
				if(ipsnStateGrp.getIpsnGrpNo()==claimIpsnGrpNo){
					SubState subState = new SubState();
					subState.setClaimIpsnGrpNo(claimIpsnGrpNo);
					subState.setPolCode(grpPolicy.getPolCode());
					String polName = getPolicyNameForPolicyList(policylist, grpPolicy.getPolCode());
					subState.setPolNameChn(polName);
					subState.setPremium(grpPolicy.getPremium());
					subState.setSumPremium(grpPolicy.getPremium());
					subState.setFaceAmnt(grpPolicy.getFaceAmnt());
					subStateList.add(subState);
				}
			}
		}
	}
	//通过列数获取对应的列字母编号
	private String getColCode(int i){
		String [] ss = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
		StringBuilder s = new StringBuilder();
		if(i>26){
			s.append(getColCode(i/26));
			s.append(getColCode(i%26));
		}else{
			s.append(ss[i-1]);
		}
		return s.toString();
	}
}