package com.newcore.orbps.dao.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.alibaba.druid.util.StringUtils;
import com.halo.core.exception.BusinessException;
import com.halo.core.header.HeaderInfoHolder;
import com.newcore.authority_support.models.Branch;
import com.newcore.authority_support.service.api.BranchService;
import com.newcore.orbps.dao.api.ContractQueryDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.insurapplregist.InsurApplRegist;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.CheckQueryVo;
import com.newcore.orbps.util.BranchNoUtils;
import com.newcore.supports.dicts.YES_NO_FLAG;
import com.newcore.supports.models.cxf.CxfHeader;
import com.newcore.supports.models.service.bo.PageQuery;
import com.newcore.supports.util.HeaderInfoUtils;

@Repository("contractQueryDaoImpl")
public class ContractQueryDaoImpl implements ContractQueryDao {
	
	private static Logger logger = LoggerFactory.getLogger(ContractQueryDaoImpl.class);

	@Autowired
	MongoTemplate mongoTemplate;
	@Autowired
	BranchService branchService;

	@Override
	public Map<String,Object> query(PageQuery<CorrectionVo> correctionVo) {
		Query query = new Query();
		if(null == correctionVo.getCondition()){
			throw new BusinessException("005");
		}
		if (!StringUtils.isEmpty(correctionVo.getCondition().getCorrectApplNo())) {
			query.addCriteria(Criteria.where("billNo").is(
					correctionVo.getCondition().getCorrectApplNo()));
		}
		Criteria criteria = new Criteria();
		if (!StringUtils.isEmpty(correctionVo.getCondition().getInsureStarDate())
			||!StringUtils.isEmpty(correctionVo.getCondition().getInsureEndDate())){
			criteria = Criteria.where("acceptDate");
		}
		if (!StringUtils.isEmpty(correctionVo.getCondition().getInsureStarDate())){
			criteria.gte(stringToDate(correctionVo.getCondition().getInsureStarDate()));
		}
		if (!StringUtils.isEmpty(correctionVo.getCondition().getInsureEndDate())){
			criteria.lte(stringToDate(correctionVo.getCondition().getInsureEndDate()));
		}
		Criteria salesInfoCriteria = new Criteria();
		if (!StringUtils.isEmpty(correctionVo.getCondition().getCorrectSalesChannel())) {
			salesInfoCriteria.and("salesChannel").is(
					correctionVo.getCondition().getCorrectSalesChannel());
		}
		if (!StringUtils.isEmpty(correctionVo.getCondition().getCorrectSalesBranchNo())){
			if(StringUtils.equals(correctionVo.getCondition().getFindSubBranchNoFlag(),YES_NO_FLAG.YES.getKey())) {
				CxfHeader headerInfo = HeaderInfoUtils.buildDefaultHeaderInfo();
				HeaderInfoHolder.setOutboundHeader(headerInfo);
				Branch subBranch = branchService.findSubBranchAll(correctionVo.getCondition().getCorrectSalesBranchNo());
				List<String> salesBranchNos = BranchNoUtils.getAllSubBranchNo(subBranch);
				salesBranchNos.add(correctionVo.getCondition().getCorrectSalesBranchNo());
				salesInfoCriteria.and("salesBranchNo").in(salesBranchNos);
			}else{
				salesInfoCriteria.and("salesBranchNo").is(correctionVo.getCondition().getCorrectSalesBranchNo());
			}
		}
		if (!StringUtils.isEmpty(correctionVo.getCondition().getCorrectSaleCode())) {
			salesInfoCriteria.and("salesNo").is(
					correctionVo.getCondition().getCorrectSaleCode());
		}
		criteria.andOperator(Criteria.where("salesInfos").elemMatch(salesInfoCriteria));
		query.addCriteria(criteria);
		if(!StringUtils.isEmpty(correctionVo.getCondition().getCorrectcntrForm())){
			query.addCriteria(Criteria.where("cntrType").is(
					correctionVo.getCondition().getCorrectcntrForm()));
		}
		long num = mongoTemplate.count(query, InsurApplRegist.class);
		query.skip((int) correctionVo.getPageStartNo());
		query.limit(correctionVo.getPageSize());
		List<InsurApplRegist> list = mongoTemplate.find(query,InsurApplRegist.class);
		Map<String,Object> map = new HashMap<>();
		map.put("num", num);
		map.put("list", list);
		return map;
	}

	@Override
	public long queryCount(PageQuery<CheckQueryVo> checkQueryVo) {
		Query query = new Query();
		if (!StringUtils.isEmpty(checkQueryVo.getCondition().getApplNo())) {
			query.addCriteria(Criteria.where("applNo").is(
					checkQueryVo.getCondition().getApplNo()));
		}
		if (!StringUtils.isEmpty(checkQueryVo.getCondition().getSalesBranchNo())) {
			query.addCriteria(Criteria.where("salesInfoList.salesBranchNo").is(
					checkQueryVo.getCondition().getSalesBranchNo()));
		}
		if (!StringUtils.isEmpty(checkQueryVo.getCondition().getSalesChannel())) {
			query.addCriteria(Criteria.where("salesInfoList.salesChannel").is(
					checkQueryVo.getCondition().getSalesChannel()));
		}
		if (!StringUtils.isEmpty(checkQueryVo.getCondition().getSalesNo())) {
			query.addCriteria(Criteria.where("salesInfoList.salesNo").is(
					checkQueryVo.getCondition().getSalesNo()));
		}
		if (checkQueryVo.getCondition().getBgForceDate() != null) {
			query.addCriteria(Criteria.where("inForceDate").gt(
					checkQueryVo.getCondition().getBgForceDate()));
		}
		if (checkQueryVo.getCondition().getEdForceDate() != null) {
			query.addCriteria(Criteria.where("inForceDate").lt(
					checkQueryVo.getCondition().getEdForceDate()));
		}
		return mongoTemplate.find(query, GrpInsurAppl.class).size();
	}

	@Override
	public GrpInsurAppl queryone(GrpInsurAppl grpInsurAppl) {
		Query query = new Query();
		if(!StringUtils.isEmpty(grpInsurAppl.getApplNo())){
			query.addCriteria(Criteria.where("applNo").is(grpInsurAppl.getApplNo()));
		}else if(!StringUtils.isEmpty(grpInsurAppl.getCgNo())){
			query.addCriteria(Criteria.where("cgNo").is(grpInsurAppl.getCgNo()));
		}
		return mongoTemplate.findOne(query, GrpInsurAppl.class);
	}
	
	private Date stringToDate(String strDate){
		Date date = null;
		try {
			date = DateUtils.parseDate(strDate, "yyyy-MM-dd");
		} catch (ParseException e) {
			logger.info("日期解析错误",e);
		}
		return date;
	}
}
