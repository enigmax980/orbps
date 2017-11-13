package com.newcore.orbps.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.IpsnPayBo;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsurappl.IpsnPayGrp;
import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.service.api.QueryipsnPayGrpInfoByApplNo;
import com.newcore.supports.models.service.bo.PageData;
import com.newcore.supports.models.service.bo.PageQuery;

/**
 * 暂收费支取功能
 * @author LJF
 * 2017年2月22日 15:25:50
 */
@Service("queryipsnPayGrpInfoByApplNo")
public class QueryipsnPayGrpInfoByApplNoImpl implements QueryipsnPayGrpInfoByApplNo {

	@Autowired
	MongoBaseDao mongoBaseDao;


	/**
	 * 功能说明：根据投保单号查询保单信息，看保单是否存在，
	 * 		如果不存在，返回失败；
	 * 		如果存在，则把查询到的数据返回。
	 * @param applNo 投保单号
	 */
	@Override
	public PageData<ChargePayGroupModalVo>  getIpsnPayGrpList (PageQuery<IpsnPayBo> applNo) {

		//用于返回
		PageData<ChargePayGroupModalVo> pageData = new PageData<>();
		//前台vo集合，最后封装到PageData<ChargePayGroupModalVo>中
		List<ChargePayGroupModalVo> chargePayGroupModalVoList = new ArrayList<>();
		//中间值，最后转换为List<ChargePayGroupModalVo>
		List<IpsnPayGrp> ipsnPayGrpListReturn = new ArrayList<>();

		//根据投保单号查询保单信息
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("applNo", applNo.getCondition().getApplNo());
		GrpInsurAppl grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, queryMap);

		//如果不存在返回失败！
		if (null == grpInsurAppl) {
			pageData.setData(chargePayGroupModalVoList);
			pageData.setTotalCount(0L);
			return pageData;
		}

		//如果存在，则把查询到的数据返回。
		List<IpsnPayGrp> ipsnPayGrpList = grpInsurAppl.getIpsnPayGrpList();
		if(null == ipsnPayGrpList || ipsnPayGrpList.isEmpty()){
			pageData.setData(chargePayGroupModalVoList);
			pageData.setTotalCount(0L);
			return pageData;
		}else{
			int paySize = ipsnPayGrpList.size();
			int size = applNo.getPageSize();//查询条数
			long startNum = applNo.getPageStartNo();//起始条数
			//如果前台有一个值传空，则初始化值
			if(0 == size){
				size = 15;
			}
			//判断要查询的起始页，条数与实际查询条数的关系
			if(paySize < startNum){
				pageData.setData(chargePayGroupModalVoList);
			}else if(paySize == startNum){
				ipsnPayGrpListReturn.add(ipsnPayGrpList.get(size-1));
				pageData.setData(getChargePayGroupModalVoByIpsnPayGrpList(ipsnPayGrpListReturn));
			}else if(paySize < (startNum+size)){
				for (int i = (int) (startNum); i < ipsnPayGrpList.size(); i++) {
					ipsnPayGrpListReturn.add(ipsnPayGrpList.get(i));
				}
				pageData.setData(getChargePayGroupModalVoByIpsnPayGrpList(ipsnPayGrpListReturn));
			}else if(paySize == (startNum+size)){
				for (int i = (int) (startNum); i < ipsnPayGrpList.size(); i++) {
					ipsnPayGrpListReturn.add(ipsnPayGrpList.get(i));
				}
				pageData.setData(getChargePayGroupModalVoByIpsnPayGrpList(ipsnPayGrpListReturn));
			}else if(paySize > (startNum+size)){
				for (int i = (int) (startNum); i < (startNum+size); i++) {
					ipsnPayGrpListReturn.add(ipsnPayGrpList.get(i));
				}
				pageData.setData(getChargePayGroupModalVoByIpsnPayGrpList(ipsnPayGrpListReturn));
			}
		}
		pageData.setTotalCount(ipsnPayGrpList.size());
		return pageData;
	}

	/**
	 * 将收费组bo转vo
	 * @param ipsnPayGrpListReturn 收费组集合
	 *
	 */
	public List<ChargePayGroupModalVo> getChargePayGroupModalVoByIpsnPayGrpList(List<IpsnPayGrp> ipsnPayGrpListReturn){

		List<ChargePayGroupModalVo> chargePayGroupModalVoList = new ArrayList<>();
		for (IpsnPayGrp ipsnPayGrp : ipsnPayGrpListReturn) {
			ChargePayGroupModalVo chargePayGroupModalVo = new ChargePayGroupModalVo();
			chargePayGroupModalVo.setBankAccNo(ipsnPayGrp.getBankaccNo());
			chargePayGroupModalVo.setBankCode(ipsnPayGrp.getBankCode());
			chargePayGroupModalVo.setBankName(ipsnPayGrp.getBankaccName());
			chargePayGroupModalVo.setGroupName(ipsnPayGrp.getFeeGrpName());
			chargePayGroupModalVo.setGroupNo(ipsnPayGrp.getFeeGrpNo());
			chargePayGroupModalVo.setMoneyinType(ipsnPayGrp.getMoneyinType());
			chargePayGroupModalVo.setNum(ipsnPayGrp.getIpsnGrpNum());
			chargePayGroupModalVoList.add(chargePayGroupModalVo);
		}
		return chargePayGroupModalVoList;
	}
}
