package com.newcore.orbps.models.web.vo.contractentry.sggrpinsurappl;

import java.io.Serializable;
import java.util.List;

import com.newcore.orbps.models.web.vo.contractentry.modal.ChargePayGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.OrganizaHierarModalVo;
import com.newcore.orbps.models.web.vo.contractentry.modal.ResponseVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;

/**
 * 汇交件面向前端大Vo
 * 
 * @author wangyanjie
 *
 */
public class SgGrpInsurApplVo implements Serializable {

	private static final long serialVersionUID = 1434454455L;

	/** 投保单信息 */
	private SgGrpApplInfoVo sgGrpApplInfoVo;
	/** 团体汇交信息 */
	private SgGrpInsurInfoVo sgGrpInsurInfoVo;
	/** 个人汇交信息 */
	private SgGrpPersonalInsurInfoVo sgGrpPersonalInsurInfoVo;
	/** 缴费信息 */
	private SgGrpPayInfoVo sgGrpPayInfoVo;
	/** 打印信息 */
	private SgGrpPrintInfoVo sgGrpPrintInfoVo;
	/** 要约信息 */
	private SgGrpProposalInfoVo sgGrpProposalInfoVo;
	/** 增值税信息 */
	private SgGrpVatInfoVo sgGrpVatInfoVo;
	/** 被保人分组信息 */
	private List<InsuredGroupModalVo> insuredGroupModalVos;
	/** 组织层次分组信息 */
	private List<OrganizaHierarModalVo> organizaHierarModalVos;
	/** 险种信息 */
	private List<SgGrpAddinsuranceVo> addinsuranceVos;
	/** 责任信息 */
	private List<ResponseVo> responseVos;
	/** 收付费分组信息 */
	private List<ChargePayGroupModalVo> chargePayGroupModalVos;
	/** 汇交人类型*/
	private String listType;
	/** 新单状态 */
    private String approvalState;
    /** 接入通道 */
    private String accessChannel;
    /** 契约类型*/
    private String cntrType;
    /**任务轨迹信息**/
    private TaskInfo taskInfo;
    /** 进入人工审批的原因，仅用于人工审批功能*/
    private String aprovalReason;
    /** 备注 */
    private String note;
	/**
	 * @return the sgGrpApplInfoVo
	 */
	public SgGrpApplInfoVo getSgGrpApplInfoVo() {
		return sgGrpApplInfoVo;
	}
	/**
	 * @param sgGrpApplInfoVo the sgGrpApplInfoVo to set
	 */
	public void setSgGrpApplInfoVo(SgGrpApplInfoVo sgGrpApplInfoVo) {
		this.sgGrpApplInfoVo = sgGrpApplInfoVo;
	}
	/**
	 * @return the sgGrpInsurInfoVo
	 */
	public SgGrpInsurInfoVo getSgGrpInsurInfoVo() {
		return sgGrpInsurInfoVo;
	}
	/**
	 * @param sgGrpInsurInfoVo the sgGrpInsurInfoVo to set
	 */
	public void setSgGrpInsurInfoVo(SgGrpInsurInfoVo sgGrpInsurInfoVo) {
		this.sgGrpInsurInfoVo = sgGrpInsurInfoVo;
	}
	/**
	 * @return the sgGrpPersonalInsurInfoVo
	 */
	public SgGrpPersonalInsurInfoVo getSgGrpPersonalInsurInfoVo() {
		return sgGrpPersonalInsurInfoVo;
	}
	/**
	 * @param sgGrpPersonalInsurInfoVo the sgGrpPersonalInsurInfoVo to set
	 */
	public void setSgGrpPersonalInsurInfoVo(SgGrpPersonalInsurInfoVo sgGrpPersonalInsurInfoVo) {
		this.sgGrpPersonalInsurInfoVo = sgGrpPersonalInsurInfoVo;
	}
	/**
	 * @return the sgGrpPayInfoVo
	 */
	public SgGrpPayInfoVo getSgGrpPayInfoVo() {
		return sgGrpPayInfoVo;
	}
	/**
	 * @param sgGrpPayInfoVo the sgGrpPayInfoVo to set
	 */
	public void setSgGrpPayInfoVo(SgGrpPayInfoVo sgGrpPayInfoVo) {
		this.sgGrpPayInfoVo = sgGrpPayInfoVo;
	}
	/**
	 * @return the sgGrpPrintInfoVo
	 */
	public SgGrpPrintInfoVo getSgGrpPrintInfoVo() {
		return sgGrpPrintInfoVo;
	}
	/**
	 * @param sgGrpPrintInfoVo the sgGrpPrintInfoVo to set
	 */
	public void setSgGrpPrintInfoVo(SgGrpPrintInfoVo sgGrpPrintInfoVo) {
		this.sgGrpPrintInfoVo = sgGrpPrintInfoVo;
	}
	/**
	 * @return the sgGrpProposalInfoVo
	 */
	public SgGrpProposalInfoVo getSgGrpProposalInfoVo() {
		return sgGrpProposalInfoVo;
	}
	/**
	 * @param sgGrpProposalInfoVo the sgGrpProposalInfoVo to set
	 */
	public void setSgGrpProposalInfoVo(SgGrpProposalInfoVo sgGrpProposalInfoVo) {
		this.sgGrpProposalInfoVo = sgGrpProposalInfoVo;
	}
	/**
	 * @return the sgGrpVatInfoVo
	 */
	public SgGrpVatInfoVo getSgGrpVatInfoVo() {
		return sgGrpVatInfoVo;
	}
	/**
	 * @param sgGrpVatInfoVo the sgGrpVatInfoVo to set
	 */
	public void setSgGrpVatInfoVo(SgGrpVatInfoVo sgGrpVatInfoVo) {
		this.sgGrpVatInfoVo = sgGrpVatInfoVo;
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
	 * @return the addinsuranceVos
	 */
	public List<SgGrpAddinsuranceVo> getAddinsuranceVos() {
		return addinsuranceVos;
	}
	/**
	 * @param addinsuranceVos the addinsuranceVos to set
	 */
	public void setAddinsuranceVos(List<SgGrpAddinsuranceVo> addinsuranceVos) {
		this.addinsuranceVos = addinsuranceVos;
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
	 * @return the listType
	 */
	public String getListType() {
		return listType;
	}
	/**
	 * @param listType the listType to set
	 */
	public void setListType(String listType) {
		this.listType = listType;
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
		return "SgGrpInsurApplVo [sgGrpApplInfoVo=" + sgGrpApplInfoVo + ", sgGrpInsurInfoVo=" + sgGrpInsurInfoVo
				+ ", sgGrpPersonalInsurInfoVo=" + sgGrpPersonalInsurInfoVo + ", sgGrpPayInfoVo=" + sgGrpPayInfoVo
				+ ", sgGrpPrintInfoVo=" + sgGrpPrintInfoVo + ", sgGrpProposalInfoVo=" + sgGrpProposalInfoVo
				+ ", sgGrpVatInfoVo=" + sgGrpVatInfoVo + ", insuredGroupModalVos=" + insuredGroupModalVos
				+ ", organizaHierarModalVos=" + organizaHierarModalVos + ", addinsuranceVos=" + addinsuranceVos
				+ ", responseVos=" + responseVos + ", chargePayGroupModalVos=" + chargePayGroupModalVos + ", listType="
				+ listType + ", approvalState=" + approvalState + ", accessChannel=" + accessChannel + ", cntrType="
				+ cntrType + ", taskInfo=" + taskInfo + ", aprovalReason=" + aprovalReason + "]";
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessChannel == null) ? 0 : accessChannel.hashCode());
		result = prime * result + ((addinsuranceVos == null) ? 0 : addinsuranceVos.hashCode());
		result = prime * result + ((approvalState == null) ? 0 : approvalState.hashCode());
		result = prime * result + ((aprovalReason == null) ? 0 : aprovalReason.hashCode());
		result = prime * result + ((chargePayGroupModalVos == null) ? 0 : chargePayGroupModalVos.hashCode());
		result = prime * result + ((cntrType == null) ? 0 : cntrType.hashCode());
		result = prime * result + ((insuredGroupModalVos == null) ? 0 : insuredGroupModalVos.hashCode());
		result = prime * result + ((listType == null) ? 0 : listType.hashCode());
		result = prime * result + ((organizaHierarModalVos == null) ? 0 : organizaHierarModalVos.hashCode());
		result = prime * result + ((responseVos == null) ? 0 : responseVos.hashCode());
		result = prime * result + ((sgGrpApplInfoVo == null) ? 0 : sgGrpApplInfoVo.hashCode());
		result = prime * result + ((sgGrpInsurInfoVo == null) ? 0 : sgGrpInsurInfoVo.hashCode());
		result = prime * result + ((sgGrpPayInfoVo == null) ? 0 : sgGrpPayInfoVo.hashCode());
		result = prime * result + ((sgGrpPersonalInsurInfoVo == null) ? 0 : sgGrpPersonalInsurInfoVo.hashCode());
		result = prime * result + ((sgGrpPrintInfoVo == null) ? 0 : sgGrpPrintInfoVo.hashCode());
		result = prime * result + ((sgGrpProposalInfoVo == null) ? 0 : sgGrpProposalInfoVo.hashCode());
		result = prime * result + ((sgGrpVatInfoVo == null) ? 0 : sgGrpVatInfoVo.hashCode());
		result = prime * result + ((taskInfo == null) ? 0 : taskInfo.hashCode());
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
		SgGrpInsurApplVo other = (SgGrpInsurApplVo) obj;
		if (accessChannel == null) {
			if (other.accessChannel != null)
				return false;
		} else if (!accessChannel.equals(other.accessChannel))
			return false;
		if (addinsuranceVos == null) {
			if (other.addinsuranceVos != null)
				return false;
		} else if (!addinsuranceVos.equals(other.addinsuranceVos))
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
		if (listType == null) {
			if (other.listType != null)
				return false;
		} else if (!listType.equals(other.listType))
			return false;
		if (organizaHierarModalVos == null) {
			if (other.organizaHierarModalVos != null)
				return false;
		} else if (!organizaHierarModalVos.equals(other.organizaHierarModalVos))
			return false;
		if (responseVos == null) {
			if (other.responseVos != null)
				return false;
		} else if (!responseVos.equals(other.responseVos))
			return false;
		if (sgGrpApplInfoVo == null) {
			if (other.sgGrpApplInfoVo != null)
				return false;
		} else if (!sgGrpApplInfoVo.equals(other.sgGrpApplInfoVo))
			return false;
		if (sgGrpInsurInfoVo == null) {
			if (other.sgGrpInsurInfoVo != null)
				return false;
		} else if (!sgGrpInsurInfoVo.equals(other.sgGrpInsurInfoVo))
			return false;
		if (sgGrpPayInfoVo == null) {
			if (other.sgGrpPayInfoVo != null)
				return false;
		} else if (!sgGrpPayInfoVo.equals(other.sgGrpPayInfoVo))
			return false;
		if (sgGrpPersonalInsurInfoVo == null) {
			if (other.sgGrpPersonalInsurInfoVo != null)
				return false;
		} else if (!sgGrpPersonalInsurInfoVo.equals(other.sgGrpPersonalInsurInfoVo))
			return false;
		if (sgGrpPrintInfoVo == null) {
			if (other.sgGrpPrintInfoVo != null)
				return false;
		} else if (!sgGrpPrintInfoVo.equals(other.sgGrpPrintInfoVo))
			return false;
		if (sgGrpProposalInfoVo == null) {
			if (other.sgGrpProposalInfoVo != null)
				return false;
		} else if (!sgGrpProposalInfoVo.equals(other.sgGrpProposalInfoVo))
			return false;
		if (sgGrpVatInfoVo == null) {
			if (other.sgGrpVatInfoVo != null)
				return false;
		} else if (!sgGrpVatInfoVo.equals(other.sgGrpVatInfoVo))
			return false;
		if (taskInfo == null) {
			if (other.taskInfo != null)
				return false;
		} else if (!taskInfo.equals(other.taskInfo))
			return false;
		return true;
	}
	
}
