package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;

public class InsuranceInfoVo   implements Serializable{
    
    private static final long serialVersionUID = 54788442331L;
    
    /** 险种代码 */
    private String polCode;
    /** 主附险性质 */
    private String mrCode;
    /** 险种保额 */
    private Double faceAmnt;
    /** 实际保费 */
    private Double premium;
    /** 标准保费 */
    private Double stdPremium;
    /** 承保费率 */
    private Double premRate;
    /** 费率浮动幅度 */
    private Double recDisount;
    /**
     * @return the polCode
     */
    public String getPolCode() {
        return polCode;
    }
    /**
     * @param polCode the polCode to set
     */
    public void setPolCode(String polCode) {
        this.polCode = polCode;
    }
    /**
     * @return the mrCode
     */
    public String getMrCode() {
        return mrCode;
    }
    /**
     * @param mrCode the mrCode to set
     */
    public void setMrCode(String mrCode) {
        this.mrCode = mrCode;
    }
    /**
     * @return the faceAmnt
     */
    public Double getFaceAmnt() {
        return faceAmnt;
    }
    /**
     * @param faceAmnt the faceAmnt to set
     */
    public void setFaceAmnt(Double faceAmnt) {
        this.faceAmnt = faceAmnt;
    }
    /**
     * @return the premium
     */
    public Double getPremium() {
        return premium;
    }
    /**
     * @param premium the premium to set
     */
    public void setPremium(Double premium) {
        this.premium = premium;
    }
    /**
     * @return the stdPremium
     */
    public Double getStdPremium() {
        return stdPremium;
    }
    /**
     * @param stdPremium the stdPremium to set
     */
    public void setStdPremium(Double stdPremium) {
        this.stdPremium = stdPremium;
    }
    /**
     * @return the premRate
     */
    public Double getPremRate() {
        return premRate;
    }
    /**
     * @param premRate the premRate to set
     */
    public void setPremRate(Double premRate) {
        this.premRate = premRate;
    }
    /**
     * @return the recDisount
     */
    public Double getRecDisount() {
        return recDisount;
    }
    /**
     * @param recDisount the recDisount to set
     */
    public void setRecDisount(Double recDisount) {
        this.recDisount = recDisount;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "InsuranceInfoVo [polCode=" + polCode + ", mrCode=" + mrCode + ", faceAmnt=" + faceAmnt + ", premium="
                + premium + ", stdPremium=" + stdPremium + ", premRate=" + premRate + ", recDisount=" + recDisount
                + "]";
    }

    
}
