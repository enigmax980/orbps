package com.newcore.orbps.models.uwbps;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3.
 */
public class AccumulateRiskAmntVO implements Serializable {

    private static final long serialVersionUID = 5841059434982055776L;

    //累计风险保额代码
    private String accuRiskCode;
    //累计风险保额
    private double accuRiskAmnt;

    public String getAccuRiskCode() {
        return accuRiskCode;
    }

    public void setAccuRiskCode(String accuRiskCode) {
        this.accuRiskCode = accuRiskCode;
    }

    public double getAccuRiskAmnt() {
        return accuRiskAmnt;
    }

    public void setAccuRiskAmnt(double accuRiskAmnt) {
        this.accuRiskAmnt = accuRiskAmnt;
    }
}
