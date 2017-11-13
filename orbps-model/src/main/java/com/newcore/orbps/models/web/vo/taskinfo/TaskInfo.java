package com.newcore.orbps.models.web.vo.taskinfo;

import java.io.Serializable;
import java.util.Date;

/**
 * 任务流信息
 * @author jincong
 *
 */
public class TaskInfo  implements Serializable{
	
	private static final long serialVersionUID = 14344544291L;
	
	/** 业务流水号 */
	private String bizNo;
	/** 任务ID */
    private String taskId;
    /** 操作机构号 */
    private String operBranchNo;
    /** 操作员工号 */
    private String operClerkNo;
    /** 操作员姓名 */
    private String operClerkName;
    /** 业务状态 */
    private String operState;
    /** 操作时间 */
    private Date operDate;
    /**
     * @return the bizNo
     */
    public String getBizNo() {
        return bizNo;
    }
    /**
     * @param bizNo the bizNo to set
     */
    public void setBizNo(String bizNo) {
        this.bizNo = bizNo;
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
     * @return the operBranchNo
     */
    public String getOperBranchNo() {
        return operBranchNo;
    }
    /**
     * @param operBranchNo the operBranchNo to set
     */
    public void setOperBranchNo(String operBranchNo) {
        this.operBranchNo = operBranchNo;
    }
    /**
     * @return the operClerkNo
     */
    public String getOperClerkNo() {
        return operClerkNo;
    }
    /**
     * @param operClerkNo the operClerkNo to set
     */
    public void setOperClerkNo(String operClerkNo) {
        this.operClerkNo = operClerkNo;
    }
    /**
     * @return the operClerkName
     */
    public String getOperClerkName() {
        return operClerkName;
    }
    /**
     * @param operClerkName the operClerkName to set
     */
    public void setOperClerkName(String operClerkName) {
        this.operClerkName = operClerkName;
    }
    /**
     * @return the operState
     */
    public String getOperState() {
        return operState;
    }
    /**
     * @param operState the operState to set
     */
    public void setOperState(String operState) {
        this.operState = operState;
    }
    /**
     * @return the operDate
     */
    public Date getOperDate() {
        return operDate;
    }
    /**
     * @param operDate the operDate to set
     */
    public void setOperDate(Date operDate) {
        this.operDate = operDate;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "TaskInfo [bizNo=" + bizNo + ", taskId=" + taskId + ", operBranchNo=" + operBranchNo + ", operClerkNo="
                + operClerkNo + ", operClerkName=" + operClerkName + ", operState=" + operState + ", operDate="
                + operDate + "]";
    }

}
