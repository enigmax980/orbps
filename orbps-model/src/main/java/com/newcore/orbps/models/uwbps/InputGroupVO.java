package com.newcore.orbps.models.uwbps;



/**
 * Created by huanghaiyang on 2016/8/30.
 */
public class InputGroupVO {
    private String taskID;
    private String businessKey;

    private WebGroupVO webGroupVO;
    private CustRiskInfoVO custRiskInfoVO;

    public WebGroupVO getWebGroupVO() {
        return webGroupVO;
    }

    public void setWebGroupVO(WebGroupVO webGroupVO) {
        this.webGroupVO = webGroupVO;
    }

    public CustRiskInfoVO getCustRiskInfoVO() {
        return custRiskInfoVO;
    }

    public void setCustRiskInfoVO(CustRiskInfoVO custRiskInfoVO) {
        this.custRiskInfoVO = custRiskInfoVO;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }


}
