package com.newcore.orbps.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Service;

import com.halo.core.dao.support.CustomBeanPropertyRowMapper;
import com.newcore.orbps.models.service.bo.ErrListInfoBo;
import com.newcore.orbps.service.api.CgInsuredErrSelService;

/**
 * @author huanglong
 * @date 2016年8月24日
 * 内容:被保人清单错误查询类
 */

@Service("cgInsuredErrSelService")
public class CgInsuredErrSelServiceImpl implements CgInsuredErrSelService{

	/**
     * 日志管理工具实例.
     */
    private static Logger logger = LoggerFactory.getLogger(CgInsuredErrSelServiceImpl.class);
	
	@Autowired
	JdbcOperations jdbcTemplate;
	
	
	@Override
	public List<ErrListInfoBo> cgInsuredErrSelByCgNO(String cgNo) throws Exception {
		/*根据合同组号从ERR_LIST_INFO中 查询被保人导入失败清单*/
		String	SElECT_CG_NO_SQL="select * from CL_BIZ3.ERR_LIST_INFO where CG_NO =? and (MODIFY_FLAG = ? or MODIFY_FLAG = ?) ";
		return jdbcTemplate.query(SElECT_CG_NO_SQL, new CustomBeanPropertyRowMapper<ErrListInfoBo>(ErrListInfoBo.class),cgNo,"0","1");
	}

}
