package com.newcore.orbps.models.uwbps;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by huanghaiyang on 2016/7/28.
 */
public class WebGroupVO implements Serializable {
    private static final long serialVersionUID = 2307043992523842156L;

    /**
     * 团单基本信息
     */

    //投保单号
    @NotNull(message = "投保单号不能为空")
    private String applNo;

    /*清单标志   L:普通清单，A：档案清单，M：事后补录，N：无清单*/
    @NotNull(message = "清单标志不能为空")
    private String lstProcType;

    /*保单性质  0：新单投保，1：续期投保; (默认为0)*/
    private String renewFlag;

    //团单方案审批号
    private String approNo;

    /*契约形式  G：团单；L：清汇；*/
    @NotNull(message = "契约形式不能为空")
    private String cntrType;

    /*汇交人类型  P:个人汇交； G:单位汇交*/
    private String listType;

    //投保日期
    @NotNull(message = "投保日期不能为空")
    private Date applDate;

    /*投保单来源    1.E门店方案；2：E企购方案；3：E支持_团单方案；4：e支持_汇交件方案；*/
    private String accessSource;

    /*赠送保险标志  0：否；1：是。（默认为0）*/
    private String giftFlag;

    /*是否异常告知   0：否；1：是。*/
    @NotNull(message = "是否异常告知不能为空")
    private String notificaStat;

    //总保额
    @NotNull(message = "总保额不能为空")
    private Double sumFaceAmnt;

    //总保费
    @NotNull(message = "总保费不能为空")
    private Double sumPremium;

    //工程名称
    private String iobjName;

    //工程类型
    private String projType;

    //工程地址
    private String projLoc;

    //工程总面积
    private Double iobjSize;

    //工程总造价
    private Double iobjCost;


    //指定生效日期
    private Date inForceDate;

    //管理机构号
    @NotNull(message = "管理机构号不能为空")
    private String mgrBranchNo;


    /**
     * 个人汇交信息
     */
    private PsnListHolderInfoVO psnListHolderInfoVO;

    /**
     * 团体客户信息
     */
    private GrpHolderInfoVO grpHolderInfoVO;

    /**
     * 险种信息
     */
    /* 字段名：险种，长度：8，是否必填：是 */
    @NotNull(message = "险种信息不能为空")
    private List<PolicyVO> policyVOList;

    //团体销售信息
    @NotNull(message = "团体销售信息不能为空")
    List<SalesInfoVO> salesInfoVOList;


    /* 字段名：被保险人编号，是否必填：是 */
    private Double ipsnNo;

    /*缴费方式    M 月缴   Q 季缴   H  半年   Y 年  W 趸    X 不定期*/
    private String moneyinItrvl;

    private long udwId;

    public long getUdwId() {
        return udwId;
    }

    public void setUdwId(long udwId) {
        this.udwId = udwId;
    }

    public String getApplNo() {
        return applNo;
    }

    public void setApplNo(String applNo) {
        this.applNo = applNo;
    }

    public String getLstProcType() {
        return lstProcType;
    }

    public void setLstProcType(String lstProcType) {
        this.lstProcType = lstProcType;
    }

    public String getApproNo() {
        return approNo;
    }

    public void setApproNo(String approNo) {
        this.approNo = approNo;
    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    public Date getApplDate() {
        return applDate;
    }

    public void setApplDate(Date applDate) {
        this.applDate = applDate;
    }

    public String getGiftFlag() {
        return giftFlag;
    }

    public void setGiftFlag(String giftFlag) {
        this.giftFlag = giftFlag;
    }

    public String getNotificaStat() {
        return notificaStat;
    }

    public void setNotificaStat(String notificaStat) {
        this.notificaStat = notificaStat;
    }

    public Double getSumFaceAmnt() {
        return sumFaceAmnt;
    }

    public void setSumFaceAmnt(Double sumFaceAmnt) {
        this.sumFaceAmnt = sumFaceAmnt;
    }

    public Double getSumPremium() {
        return sumPremium;
    }

    public void setSumPremium(Double sumPremium) {
        this.sumPremium = sumPremium;
    }

    public Date getInForceDate() {
        return inForceDate;
    }

    public void setInForceDate(Date inForceDate) {
        this.inForceDate = inForceDate;
    }

    public String getMgrBranchNo() {
        return mgrBranchNo;
    }

    public void setMgrBranchNo(String mgrBranchNo) {
        this.mgrBranchNo = mgrBranchNo;
    }


    public PsnListHolderInfoVO getPsnListHolderInfoVO() {
        return psnListHolderInfoVO;
    }

    public void setPsnListHolderInfoVO(PsnListHolderInfoVO psnListHolderInfoVO) {
        this.psnListHolderInfoVO = psnListHolderInfoVO;
    }

    public GrpHolderInfoVO getGrpHolderInfoVO() {
        return grpHolderInfoVO;
    }

    public void setGrpHolderInfoVO(GrpHolderInfoVO grpHolderInfoVO) {
        this.grpHolderInfoVO = grpHolderInfoVO;
    }

    public List<PolicyVO> getPolicyVOList() {
        return policyVOList;
    }

    public void setPolicyVOList(List<PolicyVO> policyVOList) {
        this.policyVOList = policyVOList;
    }

    public List<SalesInfoVO> getSalesInfoVOList() {
        return salesInfoVOList;
    }

    public void setSalesInfoVOList(List<SalesInfoVO> salesInfoVOList) {
        this.salesInfoVOList = salesInfoVOList;
    }

    public Double getIpsnNo() {
        return ipsnNo;
    }

    public void setIpsnNo(Double ipsnNo) {
        this.ipsnNo = ipsnNo;
    }

    public String getMoneyinItrvl() {
        return moneyinItrvl;
    }

    public void setMoneyinItrvl(String moneyinItrvl) {
        this.moneyinItrvl = moneyinItrvl;
    }

    public String getRenewFlag() {
        return renewFlag;
    }

    public void setRenewFlag(String renewFlag) {
        this.renewFlag = renewFlag;
    }

    public String getCntrType() {
        return cntrType;
    }

    public void setCntrType(String cntrType) {
        this.cntrType = cntrType;
    }

    public String getAccessSource() {
        return accessSource;
    }

    public void setAccessSource(String accessSource) {
        this.accessSource = accessSource;
    }

    public String getIobjName() {
        return iobjName;
    }

    public void setIobjName(String iobjName) {
        this.iobjName = iobjName;
    }

    public String getProjType() {
        return projType;
    }

    public void setProjType(String projType) {
        this.projType = projType;
    }

    public String getProjLoc() {
        return projLoc;
    }

    public void setProjLoc(String projLoc) {
        this.projLoc = projLoc;
    }

    public Double getIobjSize() {
        return iobjSize;
    }

    public void setIobjSize(Double iobjSize) {
        this.iobjSize = iobjSize;
    }

    public Double getIobjCost() {
        return iobjCost;
    }

    public void setIobjCost(Double iobjCost) {
        this.iobjCost = iobjCost;
    }
}
