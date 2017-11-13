package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.dao.api.ProcPSInfoUtil;
import com.newcore.orbps.models.opertrace.TraceNode;
import com.newcore.orbps.models.para.RetInfo;
import com.newcore.orbps.models.pcms.bo.CntrBackInBO;
import com.newcore.orbps.models.pcms.bo.CntrBackOutBO;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.Policy;
import com.newcore.orbps.service.api.InsurApplOperUtils;
import com.newcore.orbps.service.api.ProcCorrectDataEffective;
import com.newcore.orbps.service.api.TaskCntrPrintService;
import com.newcore.orbps.service.pcms.api.GrpCntrListBackService;
import com.newcore.supports.dicts.CNTR_TYPE;
import com.newcore.supports.dicts.NEW_APPL_STATE;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.util.HeaderInfoUtils;


@Service("procCorrectDataEffectiveImpl")
public  class ProcCorrectDataEffectiveImpl implements ProcCorrectDataEffective {


	@Autowired
	MongoBaseDao mongoBaseDao;

	@Autowired 
	GrpCntrListBackService grpCntrListBackService;

	@Autowired 
	InsurApplOperUtils insurApplLandUtils;

	@Autowired 
	ProcPSInfoUtil procPSInfoUtil;

	@Autowired 
	TaskCntrPrintService taskCntrPrintService;

	private static final String RETURN_CODE_ZERO ="0";

	private static final String RETURN_CODE_ONE ="1";

	private static final String RET_CODE_ONE ="1";

	private static final String RET_CODE_TWO ="2";

	private static final String IS_PROC_MIO_FLAG ="N";

	private static final String STATUS ="N";

	private static final String INSUR_APPL_LAND_FLAG ="0";

	@Override
	public RetInfo correctGrpInsurAppl(GrpInsurAppl grpInsurAppl) {

		//声明返回值
		RetInfo retInfoReturn = new RetInfo();
		retInfoReturn.setApplNo(grpInsurAppl.getApplNo());

		//判断保单状态
		RetInfo  retInfo = insurApplLandUtils.getIsInsurApplLanding(grpInsurAppl.getApplNo());
		if(StringUtils.equals(RET_CODE_ONE,retInfo.getRetCode())){//未生效，直接返回成功。
			retInfoReturn.setErrMsg("订正成功");
			retInfoReturn.setRetCode(RETURN_CODE_ONE);
			return retInfoReturn;
		}else if(StringUtils.equals(RET_CODE_TWO,retInfo.getRetCode())){//生效、落地在途，直接返回失败，不支持订正。
			retInfoReturn.setErrMsg("生效落地在途，不支持订正");
			retInfoReturn.setRetCode(RETURN_CODE_ZERO);
			return retInfoReturn;
		}else{//已落地，此时需要重新落地帽子数据
			//组织参数
			List<String> polCodeList = new ArrayList<>();
			for (Policy policy : grpInsurAppl.getApplState().getPolicyList()) {
				polCodeList.add(policy.getPolCode());
			}
			//封装参数
			CntrBackInBO cntrBackInBO = new CntrBackInBO();
			cntrBackInBO.setApplNo(grpInsurAppl.getApplNo());//必填
			cntrBackInBO.setMgrBranchNo(grpInsurAppl.getMgrBranchNo());//必填
			cntrBackInBO.setPolCodeList(polCodeList);//必填
			cntrBackInBO.setIsProcMioFlag(IS_PROC_MIO_FLAG);//必填
			//调用[保单辅助]订正接口
			CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
			HeaderInfoHolder.setOutboundHeader(headerInfo);
			CntrBackOutBO cntrBackOutBO =	grpCntrListBackService.setGrpCntrListBack(cntrBackInBO);

			if(StringUtils.equals(RET_CODE_ONE,cntrBackOutBO.getRetCode())){//未生效，直接返回成功。

				//修改保单落地控制表，重新落地保单帽子信息
				procPSInfoUtil.updateStatusAndApplLandFlagByApplNo(grpInsurAppl.getApplNo(), STATUS, INSUR_APPL_LAND_FLAG);

				//重新调用打印服务
				if(StringUtils.equals(CNTR_TYPE.GRP_INSUR.getKey(), grpInsurAppl.getCntrType())){
					taskCntrPrintService.cntrGrpPrint(grpInsurAppl.getApplNo()); //团单
				}else{
					taskCntrPrintService.cntrLstPrint(grpInsurAppl.getApplNo());//清汇
				}

				/*插入保单生效轨迹*/
				TraceNode traceNode = new TraceNode();
				traceNode.setProcDate(new Date());
				traceNode.setProcStat(NEW_APPL_STATE.PRINT.getKey());
				mongoBaseDao.updateOperTrace(grpInsurAppl.getApplNo(), traceNode);

			}else{
				retInfoReturn.setErrMsg("调用保单辅助订正失败");
				retInfoReturn.setRetCode(RETURN_CODE_ZERO);
				return retInfoReturn;
			}
		}
		retInfoReturn.setErrMsg("订正成功");
		retInfoReturn.setRetCode(RETURN_CODE_ONE);
		return retInfoReturn;
	}

}
