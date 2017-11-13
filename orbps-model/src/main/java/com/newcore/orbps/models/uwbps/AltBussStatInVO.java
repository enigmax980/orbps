package com.newcore.orbps.models.uwbps;

import java.io.Serializable;

/**
 * 修改新单状态模型 
 * @author yuhao 
 * 
 */
public class AltBussStatInVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3834315739005618686L;
	//业务流水号businesskey 
	private String businessKey;
	//新单状态 new_appl_stat 契约状态 
	private String newApplState;
	//处理人员机构号PCLK_BRANCH_NO
	private String pclkBranchNo;
	//处理人员工号PCLK_NO
    private String pclkNo;
    //处理人员姓名PCLK_NAME
    private String pclkName;
    /**
     * session
     */
    private SessionModel session;
    
	public SessionModel getSession() {
		return session;
	}
	public void setSession(SessionModel session) {
		this.session = session;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getNewApplState() {
		return newApplState;
	}
	public void setNewApplState(String newApplState) {
		this.newApplState = newApplState;
	}
	public String getPclkBranchNo() {
		return pclkBranchNo;
	}
	public void setPclkBranchNo(String pclkBranchNo) {
		this.pclkBranchNo = pclkBranchNo;
	}
	public String getPclkNo() {
		return pclkNo;
	}
	public void setPclkNo(String pclkNo) {
		this.pclkNo = pclkNo;
	}
	public String getPclkName() {
		return pclkName;
	}
	public void setPclkName(String pclkName) {
		this.pclkName = pclkName;
	}
	
	
}
