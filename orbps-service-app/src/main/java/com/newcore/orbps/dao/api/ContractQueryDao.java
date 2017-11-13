package com.newcore.orbps.dao.api;


import java.util.Map;

import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.web.vo.otherfunction.contractcorrected.CorrectionVo;
import com.newcore.orbps.models.web.vo.otherfunction.contractquery.CheckQueryVo;
import com.newcore.supports.models.service.bo.PageQuery;

public interface ContractQueryDao {

	Map<String, Object> query(PageQuery<CorrectionVo> pageQuery);

	long queryCount(PageQuery<CheckQueryVo> pageQuery);
	
	GrpInsurAppl queryone(GrpInsurAppl grpInsurAppl);
	
}
