package com.newcore.orbps.models.uwbps;


import java.io.Serializable;
import java.util.List;

/**
 * Created by huanghaiyang on 2016/8/3.
 */
public class CustRiskInfoVO implements Serializable {

    private static final long serialVersionUID = 5841059434982055776L;

    //客户号
    private Integer partyId;
    //保单风险保额
    private List<ContractRiskAmountVO> contractRiskAmountVOList;
    //累计风险保额
    private List<AccumulateRiskAmntVO> amntVos;
    //投保团体三年平均赔付率
    private double grp3yAvgLossRatio;
    //客户既往拒保标记
    private boolean ipsnHistRejectFlag;
    //客户既往理赔标记
    private boolean ipsnHistCpnstFlag;
    //客户既往条件承保标记
    private boolean ipsnHistConditionFlag;
    //客户既往险种变更标记
    private boolean ipsnHistPolAlterFlag;
    //客户既往延期承保标记\
    private boolean ipsnHistDeferredFlag;
    //客户既往异常告知标记
    private boolean ipsnHistAbnorInfFlag;
    //客户既往拒绝生调标记
    private boolean ipsnHistRejectSurvivalSurveyFlag;
    //客户既往拒绝体检标记
    private boolean ipsnHistRejectPhysicalExamFlag;
    //客户既往核保变更险种标记
    private boolean ipsnHistUdwPolAlterFlag;
    //客户既往核保条件承保标记
    private boolean ipsnHistUdwConditionFlag;
    //客户既往核保延期承保标记
    private boolean ipsnHistUdwDeferredFlag;
    //累计建工险基本保额
    private List<AccumulateConstructFaceAmountVO> amountVOs;
    //重疾险（除防癌）
    private Integer longTermCriticalIllnessFlag;
    //防癌险
    private Integer cancerPreventionInsuranceFlag;
    //寿险
    private Integer longTermLifeInsuranceFlag;
    //护理险
    private Integer longTermCareInsuranceFlag;
    //意外险
    private Integer longTermAccidentInsuranceFlag;
    //储蓄险
    private Integer endowmentInsuranceFlag;
    //不计风险型
    private Integer regardlessRiskInsuranceFlag;
    //残疾险
    private Integer disabilityInsuranceFlag;
    //重大疾病类
    private Integer shortTermCriticalIllnessFlag;
    //小额定期寿险
    private Integer smallTermLifeInsuranceFlag;
    //普通定期寿险
    private Integer generalTermLifeInsuranceFlag;
    //普通意外类
    private Integer generalAccidentInsuranceFlag;
    //旅游意外类
    private Integer travelAccidentInsuranceFlag;
    //交通意外类
    private Integer trafficAccidentInsuranceFlag;
    //紧急救援类
    private Integer emergencyRescueInsuranceFlag;
    //计划生育类
    private Integer familyPlanningInsuranceFlag;
    //借款人意外类
    private Integer borrowerAccidentInsuranceFlag;
    //特殊意外类
    private Integer specialAccidentInsuranceFlag;
    //小额意外类
    private Integer smallAccidentInsuranceFlag;
    //小额医疗类
    private Integer smallMedicalInsuranceFlag;
    //普通医疗类
    private Integer generalMedicalInsuranceFlag;
    //特殊医疗类_单独累计
    private Integer specialMedicalInsuranceFlag;
    //团体短期重大疾病险
    private Integer groupShortTermCriticalIllnessFlag;
    //团体长期重大疾病险
    private Integer groupLongTermCriticalIllnessFlag;
    //团体小额定期寿险
    private Integer groupSmallTermLifeInsuranceFlag;
    //团体短期寿险
    private Integer groupShortTermAccidentInsuranceFlag;
    //团体长期寿险
    private Integer groupLongTermLifeInsuranceFlag;
    //团体普通意外类
    private Integer groupGeneralAccidentInsuranceFlag;
    //团体旅游意外类
    private Integer groupTravelAccidentInsuranceFlag;
    //团体交通意外类
    private Integer groupTrafficAccidentInsuranceFlag;
    //团体紧急救援类
    private Integer groupEmergencyRescueInsuranceFlag;
    //团体计划生育类
    private Integer groupFamilyPlanningInsuranceFlag;
    //团体特殊意外类
    private Integer groupSpecialAccidentInsuranceFlag;
    //团体小额意外类
    private Integer groupSmallAccidentInsuranceFlag;
    //团体小额医疗类
    private Integer groupSmallMedicalInsuranceFlag;
    //团体普通医疗类
    private Integer groupGeneralMedicalInsuranceFlag;
    //团体特殊医疗类
    private Integer groupSpecialMedicalInsuranceFlag;

    public Integer getPartyId() {
        return partyId;
    }

    public void setPartyId(Integer partyId) {
        this.partyId = partyId;
    }

    public List<ContractRiskAmountVO> getContractRiskAmountVOList() {
        return contractRiskAmountVOList;
    }

    public void setContractRiskAmountVOList(List<ContractRiskAmountVO> contractRiskAmountVOList) {
        this.contractRiskAmountVOList = contractRiskAmountVOList;
    }

    public List<AccumulateRiskAmntVO> getAmntVos() {
        return amntVos;
    }

    public void setAmntVos(List<AccumulateRiskAmntVO> amntVos) {
        this.amntVos = amntVos;
    }

    public double getGrp3yAvgLossRatio() {
        return grp3yAvgLossRatio;
    }

    public void setGrp3yAvgLossRatio(double grp3yAvgLossRatio) {
        this.grp3yAvgLossRatio = grp3yAvgLossRatio;
    }

    public boolean isIpsnHistRejectFlag() {
        return ipsnHistRejectFlag;
    }

    public void setIpsnHistRejectFlag(boolean ipsnHistRejectFlag) {
        this.ipsnHistRejectFlag = ipsnHistRejectFlag;
    }

    public boolean isIpsnHistCpnstFlag() {
        return ipsnHistCpnstFlag;
    }

    public void setIpsnHistCpnstFlag(boolean ipsnHistCpnstFlag) {
        this.ipsnHistCpnstFlag = ipsnHistCpnstFlag;
    }

    public boolean isIpsnHistConditionFlag() {
        return ipsnHistConditionFlag;
    }

    public void setIpsnHistConditionFlag(boolean ipsnHistConditionFlag) {
        this.ipsnHistConditionFlag = ipsnHistConditionFlag;
    }

    public boolean isIpsnHistPolAlterFlag() {
        return ipsnHistPolAlterFlag;
    }

    public void setIpsnHistPolAlterFlag(boolean ipsnHistPolAlterFlag) {
        this.ipsnHistPolAlterFlag = ipsnHistPolAlterFlag;
    }

    public boolean isIpsnHistDeferredFlag() {
        return ipsnHistDeferredFlag;
    }

    public void setIpsnHistDeferredFlag(boolean ipsnHistDeferredFlag) {
        this.ipsnHistDeferredFlag = ipsnHistDeferredFlag;
    }

    public boolean isIpsnHistAbnorInfFlag() {
        return ipsnHistAbnorInfFlag;
    }

    public void setIpsnHistAbnorInfFlag(boolean ipsnHistAbnorInfFlag) {
        this.ipsnHistAbnorInfFlag = ipsnHistAbnorInfFlag;
    }

    public boolean isIpsnHistRejectSurvivalSurveyFlag() {
        return ipsnHistRejectSurvivalSurveyFlag;
    }

    public void setIpsnHistRejectSurvivalSurveyFlag(boolean ipsnHistRejectSurvivalSurveyFlag) {
        this.ipsnHistRejectSurvivalSurveyFlag = ipsnHistRejectSurvivalSurveyFlag;
    }

    public boolean isIpsnHistRejectPhysicalExamFlag() {
        return ipsnHistRejectPhysicalExamFlag;
    }

    public void setIpsnHistRejectPhysicalExamFlag(boolean ipsnHistRejectPhysicalExamFlag) {
        this.ipsnHistRejectPhysicalExamFlag = ipsnHistRejectPhysicalExamFlag;
    }

    public boolean isIpsnHistUdwPolAlterFlag() {
        return ipsnHistUdwPolAlterFlag;
    }

    public void setIpsnHistUdwPolAlterFlag(boolean ipsnHistUdwPolAlterFlag) {
        this.ipsnHistUdwPolAlterFlag = ipsnHistUdwPolAlterFlag;
    }

    public boolean isIpsnHistUdwConditionFlag() {
        return ipsnHistUdwConditionFlag;
    }

    public void setIpsnHistUdwConditionFlag(boolean ipsnHistUdwConditionFlag) {
        this.ipsnHistUdwConditionFlag = ipsnHistUdwConditionFlag;
    }

    public boolean isIpsnHistUdwDeferredFlag() {
        return ipsnHistUdwDeferredFlag;
    }

    public void setIpsnHistUdwDeferredFlag(boolean ipsnHistUdwDeferredFlag) {
        this.ipsnHistUdwDeferredFlag = ipsnHistUdwDeferredFlag;
    }

    public List<AccumulateConstructFaceAmountVO> getAmountVOs() {
        return amountVOs;
    }

    public void setAmountVOs(List<AccumulateConstructFaceAmountVO> amountVOs) {
        this.amountVOs = amountVOs;
    }

    public Integer getLongTermCriticalIllnessFlag() {
        return longTermCriticalIllnessFlag;
    }

    public void setLongTermCriticalIllnessFlag(Integer longTermCriticalIllnessFlag) {
        this.longTermCriticalIllnessFlag = longTermCriticalIllnessFlag;
    }

    public Integer getCancerPreventionInsuranceFlag() {
        return cancerPreventionInsuranceFlag;
    }

    public void setCancerPreventionInsuranceFlag(Integer cancerPreventionInsuranceFlag) {
        this.cancerPreventionInsuranceFlag = cancerPreventionInsuranceFlag;
    }

    public Integer getLongTermLifeInsuranceFlag() {
        return longTermLifeInsuranceFlag;
    }

    public void setLongTermLifeInsuranceFlag(Integer longTermLifeInsuranceFlag) {
        this.longTermLifeInsuranceFlag = longTermLifeInsuranceFlag;
    }

    public Integer getLongTermCareInsuranceFlag() {
        return longTermCareInsuranceFlag;
    }

    public void setLongTermCareInsuranceFlag(Integer longTermCareInsuranceFlag) {
        this.longTermCareInsuranceFlag = longTermCareInsuranceFlag;
    }

    public Integer getLongTermAccidentInsuranceFlag() {
        return longTermAccidentInsuranceFlag;
    }

    public void setLongTermAccidentInsuranceFlag(Integer longTermAccidentInsuranceFlag) {
        this.longTermAccidentInsuranceFlag = longTermAccidentInsuranceFlag;
    }

    public Integer getEndowmentInsuranceFlag() {
        return endowmentInsuranceFlag;
    }

    public void setEndowmentInsuranceFlag(Integer endowmentInsuranceFlag) {
        this.endowmentInsuranceFlag = endowmentInsuranceFlag;
    }

    public Integer getRegardlessRiskInsuranceFlag() {
        return regardlessRiskInsuranceFlag;
    }

    public void setRegardlessRiskInsuranceFlag(Integer regardlessRiskInsuranceFlag) {
        this.regardlessRiskInsuranceFlag = regardlessRiskInsuranceFlag;
    }

    public Integer getDisabilityInsuranceFlag() {
        return disabilityInsuranceFlag;
    }

    public void setDisabilityInsuranceFlag(Integer disabilityInsuranceFlag) {
        this.disabilityInsuranceFlag = disabilityInsuranceFlag;
    }

    public Integer getShortTermCriticalIllnessFlag() {
        return shortTermCriticalIllnessFlag;
    }

    public void setShortTermCriticalIllnessFlag(Integer shortTermCriticalIllnessFlag) {
        this.shortTermCriticalIllnessFlag = shortTermCriticalIllnessFlag;
    }

    public Integer getSmallTermLifeInsuranceFlag() {
        return smallTermLifeInsuranceFlag;
    }

    public void setSmallTermLifeInsuranceFlag(Integer smallTermLifeInsuranceFlag) {
        this.smallTermLifeInsuranceFlag = smallTermLifeInsuranceFlag;
    }

    public Integer getGeneralTermLifeInsuranceFlag() {
        return generalTermLifeInsuranceFlag;
    }

    public void setGeneralTermLifeInsuranceFlag(Integer generalTermLifeInsuranceFlag) {
        this.generalTermLifeInsuranceFlag = generalTermLifeInsuranceFlag;
    }

    public Integer getGeneralAccidentInsuranceFlag() {
        return generalAccidentInsuranceFlag;
    }

    public void setGeneralAccidentInsuranceFlag(Integer generalAccidentInsuranceFlag) {
        this.generalAccidentInsuranceFlag = generalAccidentInsuranceFlag;
    }

    public Integer getTravelAccidentInsuranceFlag() {
        return travelAccidentInsuranceFlag;
    }

    public void setTravelAccidentInsuranceFlag(Integer travelAccidentInsuranceFlag) {
        this.travelAccidentInsuranceFlag = travelAccidentInsuranceFlag;
    }

    public Integer getTrafficAccidentInsuranceFlag() {
        return trafficAccidentInsuranceFlag;
    }

    public void setTrafficAccidentInsuranceFlag(Integer trafficAccidentInsuranceFlag) {
        this.trafficAccidentInsuranceFlag = trafficAccidentInsuranceFlag;
    }

    public Integer getEmergencyRescueInsuranceFlag() {
        return emergencyRescueInsuranceFlag;
    }

    public void setEmergencyRescueInsuranceFlag(Integer emergencyRescueInsuranceFlag) {
        this.emergencyRescueInsuranceFlag = emergencyRescueInsuranceFlag;
    }

    public Integer getFamilyPlanningInsuranceFlag() {
        return familyPlanningInsuranceFlag;
    }

    public void setFamilyPlanningInsuranceFlag(Integer familyPlanningInsuranceFlag) {
        this.familyPlanningInsuranceFlag = familyPlanningInsuranceFlag;
    }

    public Integer getBorrowerAccidentInsuranceFlag() {
        return borrowerAccidentInsuranceFlag;
    }

    public void setBorrowerAccidentInsuranceFlag(Integer borrowerAccidentInsuranceFlag) {
        this.borrowerAccidentInsuranceFlag = borrowerAccidentInsuranceFlag;
    }

    public Integer getSpecialAccidentInsuranceFlag() {
        return specialAccidentInsuranceFlag;
    }

    public void setSpecialAccidentInsuranceFlag(Integer specialAccidentInsuranceFlag) {
        this.specialAccidentInsuranceFlag = specialAccidentInsuranceFlag;
    }

    public Integer getSmallAccidentInsuranceFlag() {
        return smallAccidentInsuranceFlag;
    }

    public void setSmallAccidentInsuranceFlag(Integer smallAccidentInsuranceFlag) {
        this.smallAccidentInsuranceFlag = smallAccidentInsuranceFlag;
    }

    public Integer getSmallMedicalInsuranceFlag() {
        return smallMedicalInsuranceFlag;
    }

    public void setSmallMedicalInsuranceFlag(Integer smallMedicalInsuranceFlag) {
        this.smallMedicalInsuranceFlag = smallMedicalInsuranceFlag;
    }

    public Integer getGeneralMedicalInsuranceFlag() {
        return generalMedicalInsuranceFlag;
    }

    public void setGeneralMedicalInsuranceFlag(Integer generalMedicalInsuranceFlag) {
        this.generalMedicalInsuranceFlag = generalMedicalInsuranceFlag;
    }

    public Integer getSpecialMedicalInsuranceFlag() {
        return specialMedicalInsuranceFlag;
    }

    public void setSpecialMedicalInsuranceFlag(Integer specialMedicalInsuranceFlag) {
        this.specialMedicalInsuranceFlag = specialMedicalInsuranceFlag;
    }

    public Integer getGroupShortTermCriticalIllnessFlag() {
        return groupShortTermCriticalIllnessFlag;
    }

    public void setGroupShortTermCriticalIllnessFlag(Integer groupShortTermCriticalIllnessFlag) {
        this.groupShortTermCriticalIllnessFlag = groupShortTermCriticalIllnessFlag;
    }

    public Integer getGroupLongTermCriticalIllnessFlag() {
        return groupLongTermCriticalIllnessFlag;
    }

    public void setGroupLongTermCriticalIllnessFlag(Integer groupLongTermCriticalIllnessFlag) {
        this.groupLongTermCriticalIllnessFlag = groupLongTermCriticalIllnessFlag;
    }

    public Integer getGroupSmallTermLifeInsuranceFlag() {
        return groupSmallTermLifeInsuranceFlag;
    }

    public void setGroupSmallTermLifeInsuranceFlag(Integer groupSmallTermLifeInsuranceFlag) {
        this.groupSmallTermLifeInsuranceFlag = groupSmallTermLifeInsuranceFlag;
    }

    public Integer getGroupShortTermAccidentInsuranceFlag() {
        return groupShortTermAccidentInsuranceFlag;
    }

    public void setGroupShortTermAccidentInsuranceFlag(Integer groupShortTermAccidentInsuranceFlag) {
        this.groupShortTermAccidentInsuranceFlag = groupShortTermAccidentInsuranceFlag;
    }

    public Integer getGroupLongTermLifeInsuranceFlag() {
        return groupLongTermLifeInsuranceFlag;
    }

    public void setGroupLongTermLifeInsuranceFlag(Integer groupLongTermLifeInsuranceFlag) {
        this.groupLongTermLifeInsuranceFlag = groupLongTermLifeInsuranceFlag;
    }

    public Integer getGroupGeneralAccidentInsuranceFlag() {
        return groupGeneralAccidentInsuranceFlag;
    }

    public void setGroupGeneralAccidentInsuranceFlag(Integer groupGeneralAccidentInsuranceFlag) {
        this.groupGeneralAccidentInsuranceFlag = groupGeneralAccidentInsuranceFlag;
    }

    public Integer getGroupTravelAccidentInsuranceFlag() {
        return groupTravelAccidentInsuranceFlag;
    }

    public void setGroupTravelAccidentInsuranceFlag(Integer groupTravelAccidentInsuranceFlag) {
        this.groupTravelAccidentInsuranceFlag = groupTravelAccidentInsuranceFlag;
    }

    public Integer getGroupTrafficAccidentInsuranceFlag() {
        return groupTrafficAccidentInsuranceFlag;
    }

    public void setGroupTrafficAccidentInsuranceFlag(Integer groupTrafficAccidentInsuranceFlag) {
        this.groupTrafficAccidentInsuranceFlag = groupTrafficAccidentInsuranceFlag;
    }

    public Integer getGroupEmergencyRescueInsuranceFlag() {
        return groupEmergencyRescueInsuranceFlag;
    }

    public void setGroupEmergencyRescueInsuranceFlag(Integer groupEmergencyRescueInsuranceFlag) {
        this.groupEmergencyRescueInsuranceFlag = groupEmergencyRescueInsuranceFlag;
    }

    public Integer getGroupFamilyPlanningInsuranceFlag() {
        return groupFamilyPlanningInsuranceFlag;
    }

    public void setGroupFamilyPlanningInsuranceFlag(Integer groupFamilyPlanningInsuranceFlag) {
        this.groupFamilyPlanningInsuranceFlag = groupFamilyPlanningInsuranceFlag;
    }

    public Integer getGroupSpecialAccidentInsuranceFlag() {
        return groupSpecialAccidentInsuranceFlag;
    }

    public void setGroupSpecialAccidentInsuranceFlag(Integer groupSpecialAccidentInsuranceFlag) {
        this.groupSpecialAccidentInsuranceFlag = groupSpecialAccidentInsuranceFlag;
    }

    public Integer getGroupSmallAccidentInsuranceFlag() {
        return groupSmallAccidentInsuranceFlag;
    }

    public void setGroupSmallAccidentInsuranceFlag(Integer groupSmallAccidentInsuranceFlag) {
        this.groupSmallAccidentInsuranceFlag = groupSmallAccidentInsuranceFlag;
    }

    public Integer getGroupSmallMedicalInsuranceFlag() {
        return groupSmallMedicalInsuranceFlag;
    }

    public void setGroupSmallMedicalInsuranceFlag(Integer groupSmallMedicalInsuranceFlag) {
        this.groupSmallMedicalInsuranceFlag = groupSmallMedicalInsuranceFlag;
    }

    public Integer getGroupGeneralMedicalInsuranceFlag() {
        return groupGeneralMedicalInsuranceFlag;
    }

    public void setGroupGeneralMedicalInsuranceFlag(Integer groupGeneralMedicalInsuranceFlag) {
        this.groupGeneralMedicalInsuranceFlag = groupGeneralMedicalInsuranceFlag;
    }

    public Integer getGroupSpecialMedicalInsuranceFlag() {
        return groupSpecialMedicalInsuranceFlag;
    }

    public void setGroupSpecialMedicalInsuranceFlag(Integer groupSpecialMedicalInsuranceFlag) {
        this.groupSpecialMedicalInsuranceFlag = groupSpecialMedicalInsuranceFlag;
    }
}
