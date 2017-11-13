package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 参与共保暂交保费查询服务 入参
 *
 * @author zhanghui
 *
 * @date create on  2016年11月28日下午3:53:27
 */
public class ComEarnestaccQryArgInfo implements Serializable {

	private static final long serialVersionUID = 7115342665862034570L;

	/**协议号**/
	@NotNull(message="[协议号不能为空]")
	@Length(max=18,message="[协议号长度不能大于18位]")
	private String agreementNo;
	
	/**管理机构号**/
	@NotNull(message="[管理机构号不能为空]")
	@Length(max=18,message="[管理机构号长度不能大于18位]")
	private String mgrBranchNo;
	
	/**险种数组**/
	@NotEmpty(message="[险种数组不能为空]")
	private List<String> polCode;

	private String dataSource;
	
	
	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getAgreementNo() {
		return agreementNo;
	}

	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}

	public String getMgrBranchNo() {
		return mgrBranchNo;
	}

	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}

	public List<String> getPolCode() {
		return polCode;
	}

	public void setPolCode(List<String> polCode) {
		this.polCode = polCode;
	}
	
	
}
