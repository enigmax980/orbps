package com.newcore.orbpsutils.dao.api;

import com.newcore.orbps.models.banktrans.BtBranchContrast;

/**
 * 业务应收付对应银行转账号及所属机构表处理DAO接口
 * @author LiSK
 *
 */
public interface BtBranchContrastDao {
	
	
	/**
	 * 根据银行代码、机构号等查询保险公司银行账号
	 * @param bt
	 * @return
	 */
	BtBranchContrast queryBtBranchContrastInfo(BtBranchContrast bt);
}
