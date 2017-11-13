package com.newcore.orbps.models.web.vo.contractentry.listimport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.newcore.orbps.models.web.vo.contractentry.modal.InsuredGroupModalVo;
import com.newcore.orbps.models.web.vo.taskinfo.TaskInfo;

/**
 * 清单导入界面Vo对象
 * @author jincong
 *
 */
public class ListImportPageVo  implements Serializable{
	
	private static final long serialVersionUID = 1146080001L;
	
	/** 清单上传信息 */
	private ListImportUploadVo listUploadVo;
	/** 被保险人信息 */
	private ListImportIpsnInfoVo ipsnInfoVo;
	/** 投保人信息 */
    private ListImportHldrInfoVo hldrInfoVo;
    /***保单类型*/
    private String cntrType;
	/** 受益人信息 */
	private List<ListImportBeneficiaryVo> beneficiaryInfo;
	/** 要约信息 */
	private List<ListImportBsInfoVo> bsInfoVo ;
	/** 清单导入信息 */
	private List<ListImportVo> polCodeTb;
	/** 任务信息 */
	private TaskInfo taskInfo;
	/** 缴费账户*/
	private List<AccInfoVo> accInfoList;
	/** 保费来源**/
	private String premSource;
	/** 用于页面的form传值**/
	private List<BsInfoFormList> bsInfoFormList;
	/** 被保人分组信息 */
	private List<InsuredGroupModalVo> insuredGroupModalVos;
	
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
	 * @return the listUploadVo
	 */
	public ListImportUploadVo getListUploadVo() {
		return listUploadVo;
	}
	/**
	 * @param listUploadVo the listUploadVo to set
	 */
	public void setListUploadVo(ListImportUploadVo listUploadVo) {
		this.listUploadVo = listUploadVo;
	}
	/**
	 * @return the ipsnInfoVo
	 */
	public ListImportIpsnInfoVo getIpsnInfoVo() {
		return ipsnInfoVo;
	}
	/**
	 * @param ipsnInfoVo the ipsnInfoVo to set
	 */
	public void setIpsnInfoVo(ListImportIpsnInfoVo ipsnInfoVo) {
		this.ipsnInfoVo = ipsnInfoVo;
	}
	/**
	 * @return the hldrInfoVo
	 */
	public ListImportHldrInfoVo getHldrInfoVo() {
		return hldrInfoVo;
	}
	/**
	 * @param hldrInfoVo the hldrInfoVo to set
	 */
	public void setHldrInfoVo(ListImportHldrInfoVo hldrInfoVo) {
		this.hldrInfoVo = hldrInfoVo;
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
	 * @return the beneficiaryInfo
	 */
	public List<ListImportBeneficiaryVo> getBeneficiaryInfo() {
		return beneficiaryInfo;
	}
	/**
	 * @param beneficiaryInfo the beneficiaryInfo to set
	 */
	public void setBeneficiaryInfo(List<ListImportBeneficiaryVo> beneficiaryInfo) {
		this.beneficiaryInfo = beneficiaryInfo;
	}
	/**
	 * @return the bsInfoVo
	 */
	public List<ListImportBsInfoVo> getBsInfoVo() {
		return bsInfoVo;
	}
	/**
	 * @param bsInfoVo the bsInfoVo to set
	 */
	public void setBsInfoVo(List<ListImportBsInfoVo> bsInfoVo) {
		this.bsInfoVo = bsInfoVo;
	}
	/**
	 * @return the polCodeTb
	 */
	public List<ListImportVo> getPolCodeTb() {
		return polCodeTb;
	}
	/**
	 * @param polCodeTb the polCodeTb to set
	 */
	public void setPolCodeTb(List<ListImportVo> polCodeTb) {
		this.polCodeTb = polCodeTb;
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
	 * @return the accInfoList
	 */
	public List<AccInfoVo> getAccInfoList() {
		return accInfoList;
	}
	/**
	 * @param accInfoList the accInfoList to set
	 */
	public void setAccInfoList(List<AccInfoVo> accInfoList) {
		this.accInfoList = accInfoList;
	}
	/**
	 * @return the premSource
	 */
	public String getPremSource() {
		return premSource;
	}
	/**
	 * @param premSource the premSource to set
	 */
	public void setPremSource(String premSource) {
		this.premSource = premSource;
	}
	/**
	 * @return the bsInfoFormList
	 */
	public List<BsInfoFormList> getBsInfoFormList() {
		return bsInfoFormList;
	}
	/**
	 * @param bsInfoFormList the bsInfoFormList to set
	 */
	public void setBsInfoFormList(List<BsInfoFormList> bsInfoFormList) {
		this.bsInfoFormList = bsInfoFormList;
	}
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "ListImportPageVo [listUploadVo=" + listUploadVo + ", ipsnInfoVo=" + ipsnInfoVo + ", hldrInfoVo="
                + hldrInfoVo + ", cntrType=" + cntrType + ", beneficiaryInfo=" + beneficiaryInfo + ", bsInfoVo="
                + bsInfoVo + ", polCodeTb=" + polCodeTb + ", taskInfo=" + taskInfo + ", accInfoList=" + accInfoList
                + ", premSource=" + premSource + ", bsInfoFormList=" + bsInfoFormList + ", insuredGroupModalVos="
                + insuredGroupModalVos + "]";
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accInfoList == null) ? 0 : accInfoList.hashCode());
        result = prime * result + ((beneficiaryInfo == null) ? 0 : beneficiaryInfo.hashCode());
        result = prime * result + ((bsInfoFormList == null) ? 0 : bsInfoFormList.hashCode());
        result = prime * result + ((bsInfoVo == null) ? 0 : bsInfoVo.hashCode());
        result = prime * result + ((cntrType == null) ? 0 : cntrType.hashCode());
        result = prime * result + ((hldrInfoVo == null) ? 0 : hldrInfoVo.hashCode());
        result = prime * result + ((insuredGroupModalVos == null) ? 0 : insuredGroupModalVos.hashCode());
        result = prime * result + ((ipsnInfoVo == null) ? 0 : ipsnInfoVo.hashCode());
        result = prime * result + ((listUploadVo == null) ? 0 : listUploadVo.hashCode());
        result = prime * result + ((polCodeTb == null) ? 0 : polCodeTb.hashCode());
        result = prime * result + ((premSource == null) ? 0 : premSource.hashCode());
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
        ListImportPageVo other = (ListImportPageVo) obj;
        if (accInfoList == null) {
            if (other.accInfoList != null)
                return false;
        } else if (!accInfoList.equals(other.accInfoList))
            return false;
        if (beneficiaryInfo == null) {
            if (other.beneficiaryInfo != null)
                return false;
        } else if (!beneficiaryInfo.equals(other.beneficiaryInfo))
            return false;
        if (bsInfoFormList == null) {
            if (other.bsInfoFormList != null)
                return false;
        } else if (!bsInfoFormList.equals(other.bsInfoFormList))
            return false;
        if (bsInfoVo == null) {
            if (other.bsInfoVo != null)
                return false;
        } else if (!bsInfoVo.equals(other.bsInfoVo))
            return false;
        if (cntrType == null) {
            if (other.cntrType != null)
                return false;
        } else if (!cntrType.equals(other.cntrType))
            return false;
        if (hldrInfoVo == null) {
            if (other.hldrInfoVo != null)
                return false;
        } else if (!hldrInfoVo.equals(other.hldrInfoVo))
            return false;
        if (insuredGroupModalVos == null) {
            if (other.insuredGroupModalVos != null)
                return false;
        } else if (!insuredGroupModalVos.equals(other.insuredGroupModalVos))
            return false;
        if (ipsnInfoVo == null) {
            if (other.ipsnInfoVo != null)
                return false;
        } else if (!ipsnInfoVo.equals(other.ipsnInfoVo))
            return false;
        if (listUploadVo == null) {
            if (other.listUploadVo != null)
                return false;
        } else if (!listUploadVo.equals(other.listUploadVo))
            return false;
        if (polCodeTb == null) {
            if (other.polCodeTb != null)
                return false;
        } else if (!polCodeTb.equals(other.polCodeTb))
            return false;
        if (premSource == null) {
            if (other.premSource != null)
                return false;
        } else if (!premSource.equals(other.premSource))
            return false;
        if (taskInfo == null) {
            if (other.taskInfo != null)
                return false;
        } else if (!taskInfo.equals(other.taskInfo))
            return false;
        return true;
    }
	
}
