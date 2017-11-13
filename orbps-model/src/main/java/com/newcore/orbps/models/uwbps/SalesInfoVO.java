package com.newcore.orbps.models.uwbps;
/*
 * Copyright (c) 2016 China Life Insurance(Group) Company.
 */

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 销售相关
 *
 * @author huanghaiyang
 *         创建时间：2016年7月29日
 */
public class SalesInfoVO implements Serializable {
    private static final long serialVersionUID = 2285748013670651190L;
    /**
     * 无法提交
     */

	/* 字段名：销售渠道，长度：2，是否必填：否 */
    private String salesChannel;

    /* 字段名：销售机构，长度：6，是否必填：是 */
    @NotNull(message = "销售机构不能为空")
    private String salesBranchNo;

    /* 字段名：销售员工号，长度：8，是否必填：是 */
    @NotNull(message = "销售员工号不能为空")
    private String salesNo;

    /* 字段名：销售员姓名，长度：200，是否必填：是 */
    @NotNull(message = "销售员姓名不能为空")
    private String saleName;


    /*
     * 字段名：共同展业主副标记，长度：1，是否必填：IF salesDevelopFlag == 1 THEN 非空"
     * 1：主销售员；2：附销售员
     */
    private String develMainFlag;

    /* 字段名：展业比例，是否必填：IF salesDevelopFlag == 1 THEN 非空 */
    private Double developRate;

    /**
     *
     */
    public String getSalesChannel() {
        return salesChannel;
    }

    public void setSalesChannel(String salesChannel) {
        this.salesChannel = salesChannel;
    }

    public String getSalesBranchNo() {
        return salesBranchNo;
    }

    public void setSalesBranchNo(String salesBranchNo) {
        this.salesBranchNo = salesBranchNo;
    }

    public String getSalesNo() {
        return salesNo;
    }

    public void setSalesNo(String salesNo) {
        this.salesNo = salesNo;
    }

    public String getSaleName() {
        return saleName;
    }

    public void setSaleName(String saleName) {
        this.saleName = saleName;
    }

    public String getDevelMainFlag() {
        return develMainFlag;
    }

    public void setDevelMainFlag(String develMainFlag) {
        this.develMainFlag = develMainFlag;
    }

    public Double getDevelopRate() {
        return developRate;
    }

    public void setDevelopRate(Double developRate) {
        this.developRate = developRate;
    }

	public SalesInfoVO(String salesChannel, String salesBranchNo, String salesNo, String saleName, String develMainFlag,
			Double developRate) {
		super();
		this.salesChannel = salesChannel;
		this.salesBranchNo = salesBranchNo;
		this.salesNo = salesNo;
		this.saleName = saleName;
		this.develMainFlag = develMainFlag;
		this.developRate = developRate;
	}

	public SalesInfoVO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}
