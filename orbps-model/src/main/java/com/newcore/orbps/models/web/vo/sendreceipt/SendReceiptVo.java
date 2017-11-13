package com.newcore.orbps.models.web.vo.sendreceipt;

import java.io.Serializable;
import java.util.Date;

/**
 * 核销
 * @author xiaoYe
 *
 */
public class SendReceiptVo  implements Serializable{
	
	private static final long serialVersionUID = 14344544291L;
	
	/** 投保单号 */
	private String applNo;
	/** 投保人姓名 */
	private String applName;
	/** 签收日期 */
	private String signDate;
	/** 建议回访时间 */
	private String visitDate;
	/** excel路径 */
	private String strFilePath;
	
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
	 * @return the visitDate
	 */
	public String getVisitDate() {
		return visitDate;
	}
	/**
	 * @param visitDate the visitDate to set
	 */
	public void setVisitDate(String visitDate) {
		this.visitDate = visitDate;
	}
	/**
	 * @return the strFilePath
	 */
	public String getStrFilePath() {
		return strFilePath;
	}
	/**
	 * @param strFilePath the strFilePath to set
	 */
	public void setStrFilePath(String strFilePath) {
		this.strFilePath = strFilePath;
	}
	/**
	 * @return the applName
	 */
	public String getApplName() {
		return applName;
	}
	/**
	 * @param applName the applName to set
	 */
	public void setApplName(String applName) {
		this.applName = applName;
	}
    /**
     * @return the signDate
     */
    public String getSignDate() {
        return signDate;
    }
    /**
     * @param signDate the signDate to set
     */
    public void setSignDate(String signDate) {
        this.signDate = signDate;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SendReceiptVo [applNo=" + applNo + ", applName=" + applName + ", signDate=" + signDate + ", visitDate="
                + visitDate + ", strFilePath=" + strFilePath + "]";
    }

	
	
}
