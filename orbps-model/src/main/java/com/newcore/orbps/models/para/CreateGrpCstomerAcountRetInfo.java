package com.newcore.orbps.models.para;
import java.io.Serializable;

/**
 * 法人客户开户返回类
 * @author wangxiao
 * 创建时间：2016年7月25日下午5:33:50
 */
public class CreateGrpCstomerAcountRetInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6064729513512886116L;

	private String partyId;
	
	private String custNo;
	
	private String flag;
	
	private String modifyFlag;

	/**
	 * 
	 */
	public CreateGrpCstomerAcountRetInfo() {
		super();
	}

	/**
	 * @return the partyId
	 */
	public String getPartyId() {
		return partyId;
	}

	/**
	 * @param partyId the partyId to set
	 */
	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	/**
	 * @return the custNo
	 */
	public String getCustNo() {
		return custNo;
	}

	/**
	 * @param custNo the custNo to set
	 */
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	/**
	 * @return the flag
	 */
	public String getFlag() {
		return flag;
	}

	/**
	 * @param flag the flag to set
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * @return the modifyFlag
	 */
	public String getModifyFlag() {
		return modifyFlag;
	}

	/**
	 * @param modifyFlag the modifyFlag to set
	 */
	public void setModifyFlag(String modifyFlag) {
		this.modifyFlag = modifyFlag;
	}
	
}
