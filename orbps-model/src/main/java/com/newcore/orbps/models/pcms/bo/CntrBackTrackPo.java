package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.util.Date;

public class CntrBackTrackPo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -7152501164589747187L;
	/**
	 * ID主键
	 */
	private String SeqNo;
	/**
	 * 投保单号
	 */
	private String applNo;
	/**
	 * 处理财务数据标记	 Y/N
	 */
	private String isProcMioFlag;
	/**
	 * 创建时间
	 */
	private Date CreateDate;


	public String getSeqNo() {
		return SeqNo;
	}

	public void setSeqNo(String seqNo) {
		SeqNo = seqNo;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getIsProcMioFlag() {
		return isProcMioFlag;
	}

	public void setIsProcMioFlag(String isProcMioFlag) {
		this.isProcMioFlag = isProcMioFlag;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}
}

