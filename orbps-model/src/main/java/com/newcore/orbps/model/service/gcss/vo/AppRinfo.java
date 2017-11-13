package com.newcore.orbps.model.service.gcss.vo;

import java.io.Serializable;

public class AppRinfo implements Serializable {
    private String OPERATORNAME;

    private String OPERATORNO;

    private String OPTBRANCH;

    /**
     * @return the oPERATORNAME
     */
    public String getOPERATORNAME() {
        return OPERATORNAME;
    }

    /**
     * @param oPERATORNAME the oPERATORNAME to set
     */
    public void setOPERATORNAME(String oPERATORNAME) {
        OPERATORNAME = oPERATORNAME;
    }

    /**
     * @return the oPERATORNO
     */
    public String getOPERATORNO() {
        return OPERATORNO;
    }

    /**
     * @param oPERATORNO the oPERATORNO to set
     */
    public void setOPERATORNO(String oPERATORNO) {
        OPERATORNO = oPERATORNO;
    }

    /**
     * @return the oPTBRANCH
     */
    public String getOPTBRANCH() {
        return OPTBRANCH;
    }

    /**
     * @param oPTBRANCH the oPTBRANCH to set
     */
    public void setOPTBRANCH(String oPTBRANCH) {
        OPTBRANCH = oPTBRANCH;
    }

}
