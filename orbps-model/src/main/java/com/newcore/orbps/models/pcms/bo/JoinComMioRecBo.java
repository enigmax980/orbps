
package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
* <p>参与共保财务与财务队列落地服务入参Bo </p>
* @author zhangyuan
* @date 2016年11月28日
*/
public class JoinComMioRecBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 130791647641176162L;
	
	/**
	 * 管理机构号
	 */
	@NotEmpty(message="[管理机构号不能为空]")
	@Length(max=6,message="[管理机构号长度不能超过6位]") 
	private String mgrBranchNo;
	
	/**
	 * 管理机构号
	 */
	@NotEmpty(message="[管理机构号不能为空]")
	@Length(max=6,message="[管理机构号长度不能超过6位]") 
	private String agreementNo;
	/**
	 * 实收付流水集
	 */
	@NotNull(message="[实收付流水信息不能为空]")
	private List<MioLogInBo> mioLogInBoList;
	/**
	 * 财务接口集
	 */
	@NotNull(message="[财务接口信息不能为空]")
	private List<BusiDataInBo> busiDataInBoList;
	/**
	 * 险种集合 （待用）
	 */
	private List<String> polCodeList;
	/**
	 * 数据源
	 */
	private String dataSource;
	
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	public String getAgreementNo() {
		return agreementNo;
	}
	public void setAgreementNo(String agreementNo) {
		this.agreementNo = agreementNo;
	}
	public List<String> getPolCodeList() {
		return polCodeList;
	}
	public void setPolCodeList(List<String> polCodeList) {
		this.polCodeList = polCodeList;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public List<MioLogInBo> getMioLogInBoList() {
		return mioLogInBoList;
	}
	public void setMioLogInBoList(List<MioLogInBo> mioLogInBoList) {
		this.mioLogInBoList = mioLogInBoList;
	}
	public List<BusiDataInBo> getBusiDataInBoList() {
		return busiDataInBoList;
	}
	public void setBusiDataInBoList(List<BusiDataInBo> busiDataInBoList) {
		this.busiDataInBoList = busiDataInBoList;
	}
	
	
	
	
}
