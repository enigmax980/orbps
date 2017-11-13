package com.newcore.orbps.models.web.vo.contractentry.grpinsurappl;

import java.io.Serializable;
import java.util.List;

import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;

/**
 * 团单面向前端大Vo
 * @author xiaoye
 *
 */
public class GrpInsurApplVo  implements Serializable{
	
	private static final long serialVersionUID = 1434454455L;
	
	/** 投保单信息 */
    private TaskInfo taskInfo;
	/** 投保单信息 */
	private GrpApplInfoVo applInfoVo;
	/** 保单基本信息 */
	private GrpApplBaseInfoVo applBaseInfoVo;
	/** 缴费信息 */
	private GrpPayInfoVo payInfoVo;
	/** 打印信息 */
	private GrpPrintInfoVo printInfoVo;
	/** 要约信息 */
	private GrpProposalInfoVo proposalInfoVo;
	/** 特殊险种附加信息 */
	private  GrpSpecialInsurAddInfoVo specialInsurAddInfoVo;
	/** 增值税信息 */
	private GrpVatInfoVo vatInfoVo;
	/** 被保人分组信息 */
	private List<InsuredGroupModalVo> insuredGroupModalVos;
	/** 组织层次分组信息 */
	private List<OrganizaHierarModalVo> organizaHierarModalVos;
	/** 险种信息 */
	private List<GrpBusiPrdVo> busiPrdVos;
	/** 责任信息 */
	private List<ResponseVo> responseVos;
	/** 收付费分组信息 */
	private List<ChargePayGroupModalVo> chargePayGroupModalVos;
	/** 新单状态 */
    private String approvalState;
    /** 接入通道 */
    private String accessChannel;
    /** 契约类型*/
    private String cntrType;
    /** 进入人工审批的原因，仅用于人工审批功能*/
    private String aprovalReason;
    /** 备注*/
    private String note;
	/**
	 * @return the taskInfo
	 */
	public TaskInfo getTaskInfo() {
		return taskInfo;
	}
	/**
	 * @param taskInfo the taskInfo to set
	 */
	public void setTaskInfo(TaskInfo taskInfo) {
		this.taskInfo = taskInfo;
	}
	/**
	 * @return the applInfoVo
	 */
	public GrpApplInfoVo getApplInfoVo() {
		return applInfoVo;
	}
	/**
	 * @param applInfoVo the applInfoVo to set
	 */
	public void setApplInfoVo(GrpApplInfoVo applInfoVo) {
		this.applInfoVo = applInfoVo;
	}
	/**
	 * @return the applBaseInfoVo
	 */
	public GrpApplBaseInfoVo getApplBaseInfoVo() {
		return applBaseInfoVo;
	}
	/**
	 * @param applBaseInfoVo the applBaseInfoVo to set
	 */
	public void setApplBaseInfoVo(GrpApplBaseInfoVo applBaseInfoVo) {
		this.applBaseInfoVo = applBaseInfoVo;
	}
	/**
	 * @return the payInfoVo
	 */
	public GrpPayInfoVo getPayInfoVo() {
		return payInfoVo;
	}
	/**
	 * @param payInfoVo the payInfoVo to set
	 */
	public void setPayInfoVo(GrpPayInfoVo payInfoVo) {
		this.payInfoVo = payInfoVo;
	}
	/**
	 * @return the printInfoVo
	 */
	public GrpPrintInfoVo getPrintInfoVo() {
		return printInfoVo;
	}
	/**
	 * @param printInfoVo the printInfoVo to set
	 */
	public void setPrintInfoVo(GrpPrintInfoVo printInfoVo) {
		this.printInfoVo = printInfoVo;
	}
	/**
	 * @return the proposalInfoVo
	 */
	public GrpProposalInfoVo getProposalInfoVo() {
		return proposalInfoVo;
	}
	/**
	 * @param proposalInfoVo the proposalInfoVo to set
	 */
	public void setProposalInfoVo(GrpProposalInfoVo proposalInfoVo) {
		this.proposalInfoVo = proposalInfoVo;
	}
	/**
	 * @return the specialInsurAddInfoVo
	 */
	public GrpSpecialInsurAddInfoVo getSpecialInsurAddInfoVo() {
		return specialInsurAddInfoVo;
	}
	/**
	 * @param specialInsurAddInfoVo the specialInsurAddInfoVo to set
	 */
	public void setSpecialInsurAddInfoVo(GrpSpecialInsurAddInfoVo specialInsurAddInfoVo) {
		this.specialInsurAddInfoVo = specialInsurAddInfoVo;
	}
	/**
	 * @return the vatInfoVo
	 */
	public GrpVatInfoVo getVatInfoVo() {
		return vatInfoVo;
	}
	/**
	 * @param vatInfoVo the vatInfoVo to set
	 */
	public void setVatInfoVo(GrpVatInfoVo vatInfoVo) {
		this.vatInfoVo = vatInfoVo;
	}
	/**
	 * @return the insuredGroupModalVos
	 */
	public List<InsuredGroupModalVo> getInsuredGroupModalVos() {
		return insuredGroupModalVos;
	}
	/**
	 * @param insuredGroupModalVos the insuredGroupModalVos to set
	 */
	public void setInsuredGroupModalVos(List<InsuredGroupModalVo> insuredGroupModalVos) {
		this.insuredGroupModalVos = insuredGroupModalVos;
	}
	/**
	 * @return the organizaHierarModalVos
	 */
	public List<OrganizaHierarModalVo> getOrganizaHierarModalVos() {
		return organizaHierarModalVos;
	}
	/**
	 * @param organizaHierarModalVos the organizaHierarModalVos to set
	 */
	public void setOrganizaHierarModalVos(List<OrganizaHierarModalVo> organizaHierarModalVos) {
		this.organizaHierarModalVos = organizaHierarModalVos;
	}
	/**
	 * @return the busiPrdVos
	 */
	public List<GrpBusiPrdVo> getBusiPrdVos() {
		return busiPrdVos;
	}
	/**
	 * @param busiPrdVos the busiPrdVos to set
	 */
	public void setBusiPrdVos(List<GrpBusiPrdVo> busiPrdVos) {
		this.busiPrdVos = busiPrdVos;
	}
	/**
	 * @return the responseVos
	 */
	public List<ResponseVo> getResponseVos() {
		return responseVos;
	}
	/**
	 * @param responseVos the responseVos to set
	 */
	public void setResponseVos(List<ResponseVo> responseVos) {
		this.responseVos = responseVos;
	}
	/**
	 * @return the chargePayGroupModalVos
	 */
	public List<ChargePayGroupModalVo> getChargePayGroupModalVos() {
		return chargePayGroupModalVos;
	}
	/**
	 * @param chargePayGroupModalVos the chargePayGroupModalVos to set
	 */
	public void setChargePayGroupModalVos(List<ChargePayGroupModalVo> chargePayGroupModalVos) {
		this.chargePayGroupModalVos = chargePayGroupModalVos;
	}
	/**
	 * @return the approvalState
	 */
	public String getApprovalState() {
		return approvalState;
	}
	/**
	 * @param approvalState the approvalState to set
	 */
	public void setApprovalState(String approvalState) {
		this.approvalState = approvalState;
	}
	/**
	 * @return the accessChannel
	 */
	public String getAccessChannel() {
		return accessChannel;
	}
	/**
	 * @param accessChannel the accessChannel to set
	 */
	public void setAccessChannel(String accessChannel) {
		this.accessChannel = accessChannel;
	}
	/**
	 * @return the cntrType
	 */
	public String getCntrType() {
		return cntrType;
	}
	/**
	 * @param cntrType the cntrType to set
	 */
	public void setCntrType(String cntrType) {
		this.cntrType = cntrType;
	}
	/**
	 * @return the aprovalReason
	 */
	public String getAprovalReason() {
		return aprovalReason;
	}
	/**
	 * @param aprovalReason the aprovalReason to set
	 */
	public void setAprovalReason(String aprovalReason) {
		this.aprovalReason = aprovalReason;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GrpInsurApplVo [taskInfo=" + taskInfo + ", applInfoVo=" + applInfoVo + ", applBaseInfoVo="
				+ applBaseInfoVo + ", payInfoVo=" + payInfoVo + ", printInfoVo=" + printInfoVo + ", proposalInfoVo="
				+ proposalInfoVo + ", specialInsurAddInfoVo=" + specialInsurAddInfoVo + ", vatInfoVo=" + vatInfoVo
				+ ", insuredGroupModalVos=" + insuredGroupModalVos + ", organizaHierarModalVos="
				+ organizaHierarModalVos + ", busiPrdVos=" + busiPrdVos + ", responseVos=" + responseVos
				+ ", chargePayGroupModalVos=" + chargePayGroupModalVos + ", approvalState=" + approvalState
				+ ", accessChannel=" + accessChannel + ", cntrType=" + cntrType + ", aprovalReason=" + aprovalReason
				+ "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessChannel == null) ? 0 : accessChannel.hashCode());
		result = prime * result + ((applBaseInfoVo == null) ? 0 : applBaseInfoVo.hashCode());
		result = prime * result + ((applInfoVo == null) ? 0 : applInfoVo.hashCode());
		result = prime * result + ((approvalState == null) ? 0 : approvalState.hashCode());
		result = prime * result + ((aprovalReason == null) ? 0 : aprovalReason.hashCode());
		result = prime * result + ((busiPrdVos == null) ? 0 : busiPrdVos.hashCode());
		result = prime * result + ((chargePayGroupModalVos == null) ? 0 : chargePayGroupModalVos.hashCode());
		result = prime * result + ((cntrType == null) ? 0 : cntrType.hashCode());
		result = prime * result + ((insuredGroupModalVos == null) ? 0 : insuredGroupModalVos.hashCode());
		result = prime * result + ((organizaHierarModalVos == null) ? 0 : organizaHierarModalVos.hashCode());
		result = prime * result + ((payInfoVo == null) ? 0 : payInfoVo.hashCode());
		result = prime * result + ((printInfoVo == null) ? 0 : printInfoVo.hashCode());
		result = prime * result + ((proposalInfoVo == null) ? 0 : proposalInfoVo.hashCode());
		result = prime * result + ((responseVos == null) ? 0 : responseVos.hashCode());
		result = prime * result + ((specialInsurAddInfoVo == null) ? 0 : specialInsurAddInfoVo.hashCode());
		result = prime * result + ((taskInfo == null) ? 0 : taskInfo.hashCode());
		result = prime * result + ((vatInfoVo == null) ? 0 : vatInfoVo.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GrpInsurApplVo other = (GrpInsurApplVo) obj;
		if (accessChannel == null) {
			if (other.accessChannel != null)
				return false;
		} else if (!accessChannel.equals(other.accessChannel))
			return false;
		if (applBaseInfoVo == null) {
			if (other.applBaseInfoVo != null)
				return false;
		} else if (!applBaseInfoVo.equals(other.applBaseInfoVo))
			return false;
		if (applInfoVo == null) {
			if (other.applInfoVo != null)
				return false;
		} else if (!applInfoVo.equals(other.applInfoVo))
			return false;
		if (approvalState == null) {
			if (other.approvalState != null)
				return false;
		} else if (!approvalState.equals(other.approvalState))
			return false;
		if (aprovalReason == null) {
			if (other.aprovalReason != null)
				return false;
		} else if (!aprovalReason.equals(other.aprovalReason))
			return false;
		if (busiPrdVos == null) {
			if (other.busiPrdVos != null)
				return false;
		} else if (!busiPrdVos.equals(other.busiPrdVos))
			return false;
		if (chargePayGroupModalVos == null) {
			if (other.chargePayGroupModalVos != null)
				return false;
		} else if (!chargePayGroupModalVos.equals(other.chargePayGroupModalVos))
			return false;
		if (cntrType == null) {
			if (other.cntrType != null)
				return false;
		} else if (!cntrType.equals(other.cntrType))
			return false;
		if (insuredGroupModalVos == null) {
			if (other.insuredGroupModalVos != null)
				return false;
		} else if (!insuredGroupModalVos.equals(other.insuredGroupModalVos))
			return false;
		if (organizaHierarModalVos == null) {
			if (other.organizaHierarModalVos != null)
				return false;
		} else if (!organizaHierarModalVos.equals(other.organizaHierarModalVos))
			return false;
		if (payInfoVo == null) {
			if (other.payInfoVo != null)
				return false;
		} else if (!payInfoVo.equals(other.payInfoVo))
			return false;
		if (printInfoVo == null) {
			if (other.printInfoVo != null)
				return false;
		} else if (!printInfoVo.equals(other.printInfoVo))
			return false;
		if (proposalInfoVo == null) {
			if (other.proposalInfoVo != null)
				return false;
		} else if (!proposalInfoVo.equals(other.proposalInfoVo))
			return false;
		if (responseVos == null) {
			if (other.responseVos != null)
				return false;
		} else if (!responseVos.equals(other.responseVos))
			return false;
		if (specialInsurAddInfoVo == null) {
			if (other.specialInsurAddInfoVo != null)
				return false;
		} else if (!specialInsurAddInfoVo.equals(other.specialInsurAddInfoVo))
			return false;
		if (taskInfo == null) {
			if (other.taskInfo != null)
				return false;
		} else if (!taskInfo.equals(other.taskInfo))
			return false;
		if (vatInfoVo == null) {
			if (other.vatInfoVo != null)
				return false;
		} else if (!vatInfoVo.equals(other.vatInfoVo))
			return false;
		return true;
	}
    
}
