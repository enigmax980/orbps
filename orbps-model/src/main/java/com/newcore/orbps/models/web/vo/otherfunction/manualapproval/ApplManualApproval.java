package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;

/**
 * 投保信息人工审批vo对象
 * @author jincong
 *
 */
public class ApplManualApproval  implements Serializable{
	
	private static final long serialVersionUID = 14344544566L;
	/** 序号 */
	private ApprovalSessionModel approvalSessionModel;
	/** 契约形式 */
	private ApprovalTaskInfo approvalTaskInfo;
    /**
     * @return the approvalSessionModel
     */
    public ApprovalSessionModel getApprovalSessionModel() {
        return approvalSessionModel;
    }
    /**
     * @param approvalSessionModel the approvalSessionModel to set
     */
    public void setApprovalSessionModel(ApprovalSessionModel approvalSessionModel) {
        this.approvalSessionModel = approvalSessionModel;
    }
    /**
     * @return the approvalTaskInfo
     */
    public ApprovalTaskInfo getApprovalTaskInfo() {
        return approvalTaskInfo;
    }
    /**
     * @param approvalTaskInfo the approvalTaskInfo to set
     */
    public void setApprovalTaskInfo(ApprovalTaskInfo approvalTaskInfo) {
        this.approvalTaskInfo = approvalTaskInfo;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApplManualApproval [approvalSessionModel=" + approvalSessionModel + ", approvalTaskInfo="
                + approvalTaskInfo + "]";
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((approvalSessionModel == null) ? 0 : approvalSessionModel.hashCode());
        result = prime * result + ((approvalTaskInfo == null) ? 0 : approvalTaskInfo.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ApplManualApproval other = (ApplManualApproval) obj;
        if (approvalSessionModel == null) {
            if (other.approvalSessionModel != null)
                return false;
        } else if (!approvalSessionModel.equals(other.approvalSessionModel))
            return false;
        if (approvalTaskInfo == null) {
            if (other.approvalTaskInfo != null)
                return false;
        } else if (!approvalTaskInfo.equals(other.approvalTaskInfo))
            return false;
        return true;
    }
	
	
}
