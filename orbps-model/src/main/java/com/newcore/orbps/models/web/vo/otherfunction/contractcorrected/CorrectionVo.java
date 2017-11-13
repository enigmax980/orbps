package com.newcore.orbps.models.web.vo.otherfunction.contractcorrected;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 订正界面Vo
 * @author wangyanjie
 *
 */	

public class CorrectionVo  implements Serializable{
	
	private static final long serialVersionUID = 14344544566L;

	/** 投保单号 */
	private String correctApplNo;
	/** 契约形式 */
    private String correctcntrForm;
	/** 投保开始日期 */
	private String insureStarDate;
	/** 投保结束日期 */
	private String insureEndDate;
	/** 销售渠道 */
	private String correctSalesChannel;
	/** 销售机构 */
	private String correctSalesBranchNo;
	/** 是否包含下级机构 */
    private String findSubBranchNoFlag;
	/** 销售员代码  */
	private String correctSaleCode;
	/** 订正查询信息 */
	private List<QueryinfVo> queryinf = new ArrayList<QueryinfVo>();
    /**
     * @return the correctApplNo
     */
    public String getCorrectApplNo() {
        return correctApplNo;
    }
    /**
     * @param correctApplNo the correctApplNo to set
     */
    public void setCorrectApplNo(String correctApplNo) {
        this.correctApplNo = correctApplNo;
    }

    /**
     * @return the correctcntrForm
     */
    public String getCorrectcntrForm() {
        return correctcntrForm;
    }
    /**
     * @param correctcntrForm the correctcntrForm to set
     */
    public void setCorrectcntrForm(String correctcntrForm) {
        this.correctcntrForm = correctcntrForm;
    }
    /**
     * @return the insureStarDate
     */
    public String getInsureStarDate() {
        return insureStarDate;
    }
    /**
     * @param insureStarDate the insureStarDate to set
     */
    public void setInsureStarDate(String insureStarDate) {
        this.insureStarDate = insureStarDate;
    }
    /**
     * @return the insureEndDate
     */
    public String getInsureEndDate() {
        return insureEndDate;
    }
    /**
     * @param insureEndDate the insureEndDate to set
     */
    public void setInsureEndDate(String insureEndDate) {
        this.insureEndDate = insureEndDate;
    }
    /**
     * @return the correctSalesChannel
     */
    public String getCorrectSalesChannel() {
        return correctSalesChannel;
    }
    /**
     * @param correctSalesChannel the correctSalesChannel to set
     */
    public void setCorrectSalesChannel(String correctSalesChannel) {
        this.correctSalesChannel = correctSalesChannel;
    }
    /**
     * @return the correctSalesBranchNo
     */
    public String getCorrectSalesBranchNo() {
        return correctSalesBranchNo;
    }
    /**
     * @param correctSalesBranchNo the correctSalesBranchNo to set
     */
    public void setCorrectSalesBranchNo(String correctSalesBranchNo) {
        this.correctSalesBranchNo = correctSalesBranchNo;
    }
    /**
     * @return the correctSaleCode
     */
    public String getCorrectSaleCode() {
        return correctSaleCode;
    }
    /**
     * @param correctSaleCode the correctSaleCode to set
     */
    public void setCorrectSaleCode(String correctSaleCode) {
        this.correctSaleCode = correctSaleCode;
    }
    /**
     * @return the queryinf
     */
    public List<QueryinfVo> getQueryinf() {
        return queryinf;
    }
    /**
     * @param queryinf the queryinf to set
     */
    public void setQueryinf(List<QueryinfVo> queryinf) {
        this.queryinf = queryinf;
    }
    /**
     * @return the findSubBranchNoFlag
     */
    public String getFindSubBranchNoFlag() {
        return findSubBranchNoFlag;
    }
    /**
     * @param findSubBranchNoFlag the findSubBranchNoFlag to set
     */
    public void setFindSubBranchNoFlag(String findSubBranchNoFlag) {
        this.findSubBranchNoFlag = findSubBranchNoFlag;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "CorrectionVo [correctApplNo=" + correctApplNo + ", correctcntrForm=" + correctcntrForm
                + ", insureStarDate=" + insureStarDate + ", insureEndDate=" + insureEndDate + ", correctSalesChannel="
                + correctSalesChannel + ", correctSalesBranchNo=" + correctSalesBranchNo + ", findSubBranchNoFlag="
                + findSubBranchNoFlag + ", correctSaleCode=" + correctSaleCode + ", queryinf=" + queryinf + "]";
    }

}
