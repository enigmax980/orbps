package com.newcore.orbps.service.business;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.halo.core.cache.api.CacheService;
import com.halo.core.cache.support.redis.holder.MapHolder;
import com.newcore.orbps.dao.api.MongoBaseDao;
import com.newcore.orbps.models.service.bo.grpinsurappl.GrpInsurAppl;
import com.newcore.orbps.models.service.bo.grpinsured.ErrorGrpInsured;
import com.newcore.orbps.models.service.bo.grpinsured.GrpInsured;
import com.newcore.orbps.service.business.GrpInsuredValidator;

/**
 * @author wangxiao
 * 创建时间：2016年8月18日下午4:35:03
 */
public class IpsnItemProcessor implements ItemProcessor<GrpInsured, Object> {
	@Autowired
	MongoBaseDao mongoBaseDao;

    @Resource
    CacheService cacheService;
    
    private MapHolder<String> mapHolder;
    
    private String mapName;
    
	GrpInsurAppl grpInsurAppl;
	/* (non-Javadoc)
	 * @see org.springframework.batch.item.ItemProcessor#process(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object process(GrpInsured grpInsured) throws Exception {

        mapHolder = cacheService.getMap(mapName, String.class);
        
//		Map<String, Object> map = new HashMap<>();
//		map.put("applNo", grpInsured.getApplNo());
		
		String grpInsurApplStr = mapHolder.get(grpInsured.getApplNo()+"grpInsurAppl");
		grpInsurAppl = JSON.parseObject(grpInsurApplStr, GrpInsurAppl.class);
		
//		if(grpInsurAppl==null || !StringUtils.equals(grpInsurAppl.getApplNo(),grpInsured.getApplNo())){
//			grpInsurAppl = (GrpInsurAppl) mongoBaseDao.findOne(GrpInsurAppl.class, map);
//		}

		String polMinAgeStr = mapHolder.get(grpInsured.getApplNo()+"polMinAge");
		String polMaxAgeStr = mapHolder.get(grpInsured.getApplNo()+"polMaxAge");
		String polSexStr = mapHolder.get(grpInsured.getApplNo()+"polSex");
		Map<String,String> polMinAgeMap = JSON.parseObject(polMinAgeStr,Map.class);
		Map<String,String> polMaxAgeMap = JSON.parseObject(polMaxAgeStr,Map.class);
		Map<String,String> polSexMap = JSON.parseObject(polSexStr,Map.class);
		
		GrpInsuredValidator grpInsuredValidator = new GrpInsuredValidator(grpInsurAppl);
		grpInsuredValidator.setPolMaxAgeMap(polMaxAgeMap);
		grpInsuredValidator.setPolMinAgeMap(polMinAgeMap);
		grpInsuredValidator.setPolSexMap(polSexMap);
		String errors = grpInsuredValidator.validate(grpInsured);
		if(!StringUtils.isEmpty(errors)){
			ErrorGrpInsured errorGrpInsured = JSON.parseObject(JSON.toJSONString(grpInsured),ErrorGrpInsured.class);
			errorGrpInsured.setRemark("ipsnNo="+grpInsured.getIpsnNo()+"|"+errors);
			return errorGrpInsured;
		}else{
			return grpInsured;
		}
	}
	public String getMapName() {
		return mapName;
	}
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

}
