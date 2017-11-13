package com.newcore.orbpsutils.dao.api;

import java.util.List;

import com.newcore.orbps.models.finance.MiosNotToBank;
import com.newcore.orbps.models.finance.NotTransInfoVo;

/**
 * @author wangxiao
 * 创建时间：2017年2月27日下午4:19:27
 */
public interface MiosNotToBackDao {
	public List<MiosNotToBank> getMiosNotToBanks(NotTransInfoVo notTransInfoVo); 
	
	public boolean revoMiosNotToBanks(MiosNotToBank miosNotToBank);
	public boolean insMiosNotToBanks(MiosNotToBank miosNotToBank);
}
