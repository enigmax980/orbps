package com.newcore.orbps.models.web.vo.otherfunction.manualapproval;

import java.io.Serializable;

/**
 * 投保信息人工审批任务信息对象
 * @author jincong
 *
 */
public class ApprovalTaskInfo  implements Serializable{
	
	private static final long serialVersionUID = 14344544576L;
	/** 工号 */
    private String clerkNo;
    /** 流程ID */
    private String processDefKey;
    /** 任务DI */
    private String taskDefKey;
    /** 业务子类 */
    private String subItem;
    /** 任务id */
    private String taskId;
    /** 业务流水号 */
    private String businessKey;
    /**
     * @return the clerkNo
     */
    public String getClerkNo() {
        return clerkNo;
    }
    /**
     * @param clerkNo the clerkNo to set
     */
    public void setClerkNo(String clerkNo) {
        this.clerkNo = clerkNo;
    }
    /**
     * @return the processDefKey
     */
    public String getProcessDefKey() {
        return processDefKey;
    }
    /**
     * @param processDefKey the processDefKey to set
     */
    public void setProcessDefKey(String processDefKey) {
        this.processDefKey = processDefKey;
    }
    /**
     * @return the taskDefKey
     */
    public String getTaskDefKey() {
        return taskDefKey;
    }
    /**
     * @param taskDefKey the taskDefKey to set
     */
    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }
    /**
     * @return the subItem
     */
    public String getSubItem() {
        return subItem;
    }
    /**
     * @param subItem the subItem to set
     */
    public void setSubItem(String subItem) {
        this.subItem = subItem;
    }
    /**
     * @return the taskId
     */
    public String getTaskId() {
        return taskId;
    }
    /**
     * @param taskId the taskId to set
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
    /**
     * @return the businessKey
     */
    public String getBusinessKey() {
        return businessKey;
    }
    /**
     * @param businessKey the businessKey to set
     */
    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ApprovalTaskInfo [clerkNo=" + clerkNo + ", processDefKey=" + processDefKey + ", taskDefKey="
                + taskDefKey + ", subItem=" + subItem + ", taskId=" + taskId + ", businessKey=" + businessKey + "]";
    }
}
