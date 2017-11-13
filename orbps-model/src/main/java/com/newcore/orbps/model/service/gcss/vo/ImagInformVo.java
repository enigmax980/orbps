package com.newcore.orbps.model.service.gcss.vo;

import java.io.Serializable;

public class ImagInformVo implements Serializable {
    private String APPLNO;

    private AppRinfo APPRINFO;

    private String NOTICE;

    private String OPTTIME;

    private String PROCFLAG;

    private String TYPE;

    /**
     * @return the aPPLNO
     */
    public String getAPPLNO() {
        return APPLNO;
    }

    /**
     * @param aPPLNO the aPPLNO to set
     */
    public void setAPPLNO(String aPPLNO) {
        APPLNO = aPPLNO;
    }

    /**
     * @return the aPPRINFO
     */
    public AppRinfo getAPPRINFO() {
        return APPRINFO;
    }

    /**
     * @param aPPRINFO the aPPRINFO to set
     */
    public void setAPPRINFO(AppRinfo aPPRINFO) {
        APPRINFO = aPPRINFO;
    }

    /**
     * @return the nOTICE
     */
    public String getNOTICE() {
        return NOTICE;
    }

    /**
     * @param nOTICE the nOTICE to set
     */
    public void setNOTICE(String nOTICE) {
        NOTICE = nOTICE;
    }

    /**
     * @return the oPTTIME
     */
    public String getOPTTIME() {
        return OPTTIME;
    }

    /**
     * @param oPTTIME the oPTTIME to set
     */
    public void setOPTTIME(String oPTTIME) {
        OPTTIME = oPTTIME;
    }

    /**
     * @return the pROCFLAG
     */
    public String getPROCFLAG() {
        return PROCFLAG;
    }

    /**
     * @param pROCFLAG the pROCFLAG to set
     */
    public void setPROCFLAG(String pROCFLAG) {
        PROCFLAG = pROCFLAG;
    }

    /**
     * @return the tYPE
     */
    public String getTYPE() {
        return TYPE;
    }

    /**
     * @param tYPE the tYPE to set
     */
    public void setTYPE(String tYPE) {
        TYPE = tYPE;
    }

}
