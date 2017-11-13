package com.newcore.orbps.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.banktrans.MioLog;
import com.newcore.orbps.models.banktrans.MioPlnmioInfo;
import com.newcore.orbps.models.banktrans.PlnmioRec;
import com.newcore.orbps.models.pcms.bo.GrpFaReturn;
import com.newcore.orbps.models.pcms.bo.RetInfo;
import com.newcore.orbps.models.service.bo.GcLppPremRateBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.service.api.ThawProviFeeInfo;
import com.newcore.orbps.service.pcms.api.GrpFaReturnService;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 暂缴费还回
 *
 * @author lijifei
 * 创建时间：2016年10月28日16:26:11
 */
@Service("thawProviFeeInfoServiceImpl")
public class ThawProviFeeInfoServiceImpl implements ThawProviFeeInfo {

	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired
	GrpFaReturnService grpFaReturnServiceClient;

	/**
	 * 日志管理工具实例.
	 */
	private static Logger logger = LoggerFactory.getLogger(ThawProviFeeInfoServiceImpl.class);
	/** 
	 * 方法说明：根据投保单号查询mongodb库中投保单集合[管理机构号]与[第一险种]；收付费相关信息类集合[金额]总额，将查出的值作为参数调用 保单辅助系统接口。
	 * @param applNo    投保单号
	 */
	@Override
	public String temporarilyPayRevert(String applNo) {
		Map<String, Object> map=new HashMap<>();
		map.put("applNo", applNo);
		//根据投保单号查询出管理机构号、主险种代码以及总保费
		GrpInsurAppl grpInsurAppl=(GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
		Map<String,Object> mapMio = new HashMap<String,Object>();
		mapMio.put("applNo", applNo);
		MioPlnmioInfo mioPlnmioInfo = (MioPlnmioInfo) mongoBaseDao.findOne(MioPlnmioInfo.class, mapMio);
		//入参非空校验
		if(null!=grpInsurAppl && null!=mioPlnmioInfo){
			//获得 管理机构号和第一主险
			String mgrBranchNo=grpInsurAppl.getMgrBranchNo();
			String polCode=grpInsurAppl.getFirPolCode();
			double amnt = 0;
			for (PlnmioRec plnmioRec : mioPlnmioInfo.getPlnmioRecList()) {
				String mioItemCode = plnmioRec.getMioItemCode();
				String mioTypeCode = plnmioRec.getMioType();
				//判断需要修改的数据，如果[收付项目代码]+[收付款方式代码]为“FA”，则需要修改。
				if("FA".equals(mioItemCode) && "S"!=mioTypeCode){
					amnt +=plnmioRec.getAmnt();
				}
			}
			int	mutipayTime = 0;
			//判断是否是多期暂交，如果是[还回金额]为[应收金额]除以暂交年数；否则[还回金额]为应收金额。
			if("L".equals(grpInsurAppl.getCntrType())){
				String	isMultiPay =	grpInsurAppl.getPaymentInfo().getIsMultiPay();
				if(("Y").equals(isMultiPay)){
					//多期暂交年数
					mutipayTime = grpInsurAppl.getPaymentInfo().getMutipayTimes();
					amnt = amnt/mutipayTime;
				}
			}
			//非空判断。如果为空直接返回N
			if(!StringUtils.isBlank(mgrBranchNo)&& !StringUtils.isBlank(polCode)&& !StringUtils.isBlank(String.valueOf(amnt))){
				//暂交费还回服务 
				GrpFaReturn  grpFaReturn  = new GrpFaReturn();
				grpFaReturn.setAmnt(amnt);
				grpFaReturn.setApplNo(applNo);
				grpFaReturn.setDataSource(null);
				grpFaReturn.setMgrBranchNo(mgrBranchNo);
				grpFaReturn.setPolCode(polCode);
				//消息头设置
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				//				headerInfo.setOrginSystem("ORBPS");					
				//				headerInfo.getRouteInfo().setBranchNo("120000");
				HeaderInfoHolder.setOutboundHeader(headerInfo);	
				//调用保单辅助-暂缴费还回
				RetInfo retInfo =	grpFaReturnServiceClient.faReturn(grpFaReturn);
				//根据调用保单辅助平台接口返回值，判断是否成功
				if("1".equals(retInfo.getRetCode())){
					//如果retInfo.getRetCode()为1，即成功，则返回Y
					return "Y";
				}else{
					//否则返回N
					return "N";
				}
			}else{
				logger.error("查询不到投保单号为："+applNo+"下对应的[管理机构号]或[第一险种]或[实收数据的Amnt]！");
				return "N";
			}
		}else{
			logger.error("投保单号为："+applNo+"对应的[团单]或[收付费相关信息类]查询不到信息！");
			return "N";
		}
	}

}
