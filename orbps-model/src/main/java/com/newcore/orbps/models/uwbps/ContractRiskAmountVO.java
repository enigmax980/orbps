package com.newcore.orbps.models.uwbps;


import java.io.Serializable;
import java.util.List;

/**
 * Created by huanghaiyang on 2016/8/3.
 */
public class ContractRiskAmountVO implements Serializable {

    private static final long serialVersionUID = 5841059434982055776L;

    private String contractNo;
    private String polCode;
    private String  ipsnNo;
    private double faceAmnt;
    private double premium;
    private List<RiskAmountVO> riskAmountVOList;

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getPolCode() {
        return polCode;
    }

    public void setPolCode(String polCode) {
        this.polCode = polCode;
    }

    public String getIpsnNo() {
        return ipsnNo;
    }

    public void setIpsnNo(String ipsnNo) {
        this.ipsnNo = ipsnNo;
    }

    public double getFaceAmnt() {
        return faceAmnt;
    }

    public void setFaceAmnt(double faceAmnt) {
        this.faceAmnt = faceAmnt;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public List<RiskAmountVO> getRiskAmountVOList() {
        return riskAmountVOList;
    }

    public void setRiskAmountVOList(List<RiskAmountVO> riskAmountVOList) {
        this.riskAmountVOList = riskAmountVOList;
    }
}
