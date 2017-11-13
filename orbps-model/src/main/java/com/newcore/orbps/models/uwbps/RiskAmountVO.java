package com.newcore.orbps.models.uwbps;


import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3.
 */
public class RiskAmountVO implements Serializable {

    private static final long serialVersionUID = 5841059434982055776L;

    private String riskCode;
    private double riskAmnt;

    public double getRiskAmnt() {
        return riskAmnt;
    }

    public void setRiskAmnt(double riskAmnt) {
        this.riskAmnt = riskAmnt;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }
}
