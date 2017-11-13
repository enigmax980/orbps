package com.newcore.orbps.web.otherfunction;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.halo.core.web.annotation.CurrentSession;
import com.halo.core.web.annotation.ResponseMessage;
import com.newcore.orbps.models.service.bo.EarnestPayInfo;
import com.newcore.orbps.models.service.bo.EarnestPayList;
import com.newcore.orbps.models.service.bo.IpsnPayBo;
import com.newcore.orbps.models.service.bo.QueryEarnestAccInfoBean;
import com.newcore.orbps.models.service.bo.RetInfo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.otherfunction.earnestpay.AccInfoVo;
import com.newcore.orbps.models.web.vo.otherfunction.earnestpay.EarnestPay;
import com.newcore.orbps.models.web.vo.otherfunction.earnestpay.QueryInfoVo;
import com.newcore.orbps.service.api.ContractQueryService;
import com.newcore.orbps.service.api.InsurApplServer;
import com.newcore.orbps.service.api.ProcEarnestPayPlnmioRecInfo;
import com.newcore.orbps.service.api.QueryAccInfoByApplNo;
import com.newcore.orbps.service.api.QueryipsnPayGrpInfoByApplNo;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.models.web.vo.DataTable;
import com.newcore.supports.models.web.vo.QueryDt;
import com.newcore.supports.service.api.PageQueryService;
import com.newcore.supports.util.HeaderInfoUtils;

/**
 * 咱交费支取
 * 
 * @author jincong
 *
 */
@Controller
@RequestMapping("/orbps/otherfunction/earnestPay")
public class EarnestPayController {

    // @Autowired
    // InsurApplServer grpInsurApplServer;
	/**
     * 日志对象.
     */
    private static Logger logger = LoggerFactory.getLogger(EarnestPayController.class);

    @Autowired
    InsurApplServer insurApplServer;
    /**
     * 分页查询的支持服务
     */
    @Autowired
    PageQueryService pageQueryService;

    @Autowired
    ContractQueryService contractQueryService;
    
    /**
     * 收费组查询
     */
    @Autowired
    QueryipsnPayGrpInfoByApplNo queryipsnPayGrpInfoByApplNo;
    
    /**
     * 暂交费账户查询
     */
    @Autowired
    QueryAccInfoByApplNo queryAccInfoByApplNo;
    
    /**
     * 暂交费支取
     */
    @Autowired
    ProcEarnestPayPlnmioRecInfo procEarnestPayPlnmioRecInfo;

    
    /**
     * 查询收费组信息
     * 
     * @author jincong
     * @date 2017年2月24日
     */

    @RequestMapping(value = "/payGrpInfoSearch")
    public @ResponseMessage DataTable<ChargePayGroupModalVo> payGrpInfoSearch(@CurrentSession Session session, QueryDt query,
    		QueryInfoVo queryInfoVo) {
    	/** 构建PageQuery对象 **/
        // VO转BO
    	IpsnPayBo ipsnPayBo = new IpsnPayBo();
    	if(null != queryInfoVo){
    		ipsnPayBo.setApplNo(queryInfoVo.getApplNo());
    	}
    	
        PageQuery<IpsnPayBo> pageQuery = pageQueryService.tranToPageQuery(query, ipsnPayBo);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<ChargePayGroupModalVo> result = queryipsnPayGrpInfoByApplNo.getIpsnPayGrpList(pageQuery);
        return pageQueryService.tranToDataTable(query.getRequestId(), result);
    }
    
    /**
     * 查询暂交费账户信息
     * 
     * @author jincong
     * @date 2017年2月28日
     */

    @RequestMapping(value = "/accInfoSearch")
    // @RequiresPermissions("orbps:contractCorrected:query")
    public @ResponseMessage DataTable<AccInfoVo> accInfoSearch(QueryDt query, QueryInfoVo queryInfoVo) {
        /** 构建PageQuery对象 **/
        // VO转BO
    	QueryEarnestAccInfoBean queryEarnestAccInfoBean = new QueryEarnestAccInfoBean();
		if(null != queryInfoVo){
			queryEarnestAccInfoBean.setApplNo(queryInfoVo.getApplNo());
			queryEarnestAccInfoBean.setGrpName(queryInfoVo.getGrpName());
			if(!StringUtils.isEmpty(queryInfoVo.getBirthDate())){
				try {
					Date date = DateUtils.parseDate(queryInfoVo.getBirthDate(), "yyyy-MM-dd");
					queryEarnestAccInfoBean.setIpsnBirthDate(date);
				} catch (ParseException e) {
	                logger.info("日期转换失败，请核对日期格式", e);
	            }
			}
			queryEarnestAccInfoBean.setIpsnIdNo(queryInfoVo.getIpsnIdNo());
			queryEarnestAccInfoBean.setIpsnIdType(queryInfoVo.getIpsnIdType());
			queryEarnestAccInfoBean.setIpsnName(queryInfoVo.getIpsnName());
			queryEarnestAccInfoBean.setIpsnSex(queryInfoVo.getIpsnIdSex());
			//收费组号赋值
			List<Long> list = new ArrayList<>();
			String[] str = queryInfoVo.getPayGrpNos().split(",");
			for(int i= 0; i< str.length; i++){
				if(StringUtils.isNotEmpty(str[i])){
					list.add(Long.valueOf(str[i]));
				}
			}
			queryEarnestAccInfoBean.setFeeGrpNoList(list);
		}
        PageQuery<QueryEarnestAccInfoBean> pageQuery = pageQueryService.tranToPageQuery(query, queryEarnestAccInfoBean);
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        PageData<AccInfoVo> result = queryAccInfoByApplNo.getEarnestAccInfo(pageQuery);
        if(null == result.getData() || result.getData().isEmpty()){
        	throw new BusinessException("0001", "查询数据为空");
        }
        return pageQueryService.tranToDataTable(query.getRequestId(), result);
    }
    
    /**
     * 暂收费支取提交
     * 
     * @author jincong
     * @date 2017年2月24日
     */

    @RequestMapping(value = "/submit")
    public @ResponseMessage RetInfo submit(@CurrentSession Session session, @RequestBody EarnestPay earnestPay) {
    	/** 构建PageQuery对象 **/
        // VO转BO
        CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
        HeaderInfoHolder.setOutboundHeader(headerInfo);
        EarnestPayInfo earnestPayInfo;
        List<EarnestPayInfo> earnestPayInfos = new ArrayList<>();
        EarnestPayList earnestPayList = new EarnestPayList();
        if(null != earnestPay){
        	if(null != earnestPay.getQueryInfoVo()){
        		earnestPayList.setApplNo(earnestPay.getQueryInfoVo().getApplNo());
        	}
        	earnestPayList.setPayAllFlag(earnestPay.getPayAllFlag());
        	if(null != earnestPay.getAccInfoVos() && earnestPay.getAccInfoVos().size()>0){
        		for(int i=0; i<earnestPay.getAccInfoVos().size(); i++){
        			earnestPayInfo = new EarnestPayInfo();
        			if(null != earnestPay.getAccInfoVos().get(i)){
        				earnestPayInfo.setAccNo(earnestPay.getAccInfoVos().get(i).getAccNo());
        				earnestPayInfo.setAccPersonNo(earnestPay.getAccInfoVos().get(i).getAccPersonNo());
        				earnestPayInfo.setAccType(earnestPay.getAccInfoVos().get(i).getAccType());
        				earnestPayInfo.setApplNo(earnestPay.getAccInfoVos().get(i).getApplNo());
        				earnestPayInfo.setBalance(earnestPay.getAccInfoVos().get(i).getBalance());
        				earnestPayInfo.setPlnAmnt(earnestPay.getAccInfoVos().get(i).getPayAmount());
        			}
        			earnestPayInfos.add(earnestPayInfo);
        		}
        	}
        	earnestPayList.setEarnestPayInfoList(earnestPayInfos);
        }
        return procEarnestPayPlnmioRecInfo.drawEarnestAccInfo(earnestPayList);
    }

}
