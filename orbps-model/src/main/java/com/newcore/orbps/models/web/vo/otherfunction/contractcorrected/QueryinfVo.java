package com.newcore.orbps.models.web.vo.otherfunction.contractcorrected;

import java.io.Serializable;
/**
 * 订正查询表单信息 
 * @author wangyanjie 
 *
 */

public class QueryinfVo  implements Serializable{
	
	private static final long serialVersionUID = 114560000012L;
	
	/** 投保单号 */
	private String applNo;
	/** 投保申请日期 */
	private String applDate;
	/** 投保人、汇交人信息*/
	private String hldrName;
	/** 机构号 */
	private String mgrBranchNo;
	/** 岗位 */
	private String postName;
	/** 契约形式 */
	private String cntrForm;
	/** 当前操作员机构 */
	private String currentOperatorNum;
	/** 当前操作员代码 */
	private String currentOperatorCode;
	/** 当前操作员姓名 */
	private String currentOperatorName;
	/** 处理时间 */
	private String procTime;
	/**
	 * @return the applNo
	 */
	public String getApplNo() {
		return applNo;
	}	
	/**
	 * @param applNo the applNo to set
	 */
	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}
	/**
     * @return the applDate
     */
    public String getApplDate() {
        return applDate;
    }
    /**
     * @param applDate the applDate to set
     */
    public void setApplDate(String applDate) {
        this.applDate = applDate;
    }
    /**
	 * @return the hldrName
	 */
	public String getHldrName() {
		return hldrName;
	}
	/**
	 * @param hldrName the hldrName to set
	 */
	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}
	/**
	 * @return the mgrBranchNo
	 */
	public String getMgrBranchNo() {
		return mgrBranchNo;
	}
	/**
	 * @param mgrBranchNo the mgrBranchNo to set
	 */
	public void setMgrBranchNo(String mgrBranchNo) {
		this.mgrBranchNo = mgrBranchNo;
	}
	/**
	 * @return the postName
	 */
	public String getPostName() {
		return postName;
	}
	/**
	 * @param postName the postName to set
	 */
	public void setPostName(String postName) {
		this.postName = postName;
	}
	/**
	 * @return the cntrForm
	 */
	public String getCntrForm() {
		return cntrForm;
	}
	/**
	 * @param cntrForm the cntrForm to set
	 */
	public void setCntrForm(String cntrForm) {
		this.cntrForm = cntrForm;
	}
	/**
	 * @return the currentOperatorNum
	 */
	public String getCurrentOperatorNum() {
		return currentOperatorNum;
	}
	/**
	 * @param currentOperatorNum the currentOperatorNum to set
	 */
	public void setCurrentOperatorNum(String currentOperatorNum) {
		this.currentOperatorNum = currentOperatorNum;
	}
	/**
	 * @return the currentOperatorCode
	 */
	public String getCurrentOperatorCode() {
		return currentOperatorCode;
	}
	/**
	 * @param currentOperatorCode the currentOperatorCode to set
	 */
	public void setCurrentOperatorCode(String currentOperatorCode) {
		this.currentOperatorCode = currentOperatorCode;
	}
	/**
	 * @return the currentOperatorName
	 */
	public String getCurrentOperatorName() {
		return currentOperatorName;
	}
	/**
	 * @param currentOperatorName the currentOperatorName to set
	 */
	public void setCurrentOperatorName(String currentOperatorName) {
		this.currentOperatorName = currentOperatorName;
	}
    /**
     * @return the procTime
     */
    public String getProcTime() {
        return procTime;
    }
    /**
     * @param procTime the procTime to set
     */
    public void setProcTime(String procTime) {
        this.procTime = procTime;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "QueryinfVo [applNo=" + applNo + ", applDate=" + applDate + ", mgrBranchNo=" + mgrBranchNo
                + ", postName=" + postName + ", cntrForm=" + cntrForm + ", currentOperatorNum=" + currentOperatorNum
                + ", currentOperatorCode=" + currentOperatorCode + ", currentOperatorName=" + currentOperatorName
                + ", procTime=" + procTime + "]";
    }
}
