package com.newcore.orbps.model.service.gcss.vo;

import javax.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * Created by liushuaifeng on 2017/5/24 0024.
 */

@SuppressWarnings("serial")
@XmlRootElement(name = "notifyApplDataAuditResponse")
@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType(name = "notifyApplDataAuditResponse", namespace = "http://www.e-chinalife.com/soa/")
public class NotifyApplDataAuditRet implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8162303056557622720L;

    @XmlElement(name = "APPLNO", namespace = "http://contractsupport.programme.server.soa.glss.chinalife.com/xsd")
    private String applNo;
    @XmlElement(name = "ERRMSG", namespace = "http://contractsupport.programme.server.soa.glss.chinalife.com/xsd")
    private String errMsg;
    @XmlElement(name = "REMARK", namespace = "http://contractsupport.programme.server.soa.glss.chinalife.com/xsd")
    private String remark;
    @XmlElement(name = "RETCODE", namespace = "http://contractsupport.programme.server.soa.glss.chinalife.com/xsd")
    private String retCode;
    @XmlElement(name = "STATUS", namespace = "http://contractsupport.programme.server.soa.glss.chinalife.com/xsd")
    private String status;

    public String getApplNo() {
        return applNo;
    }

    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
