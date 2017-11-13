package com.newcore.orbps.models.web.vo.contractentry.registrationacceptance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 受理界面vo对象
 * @author jincong
 *
 */
public class RegAcceptanceVo implements Serializable{

	private static final long serialVersionUID = 13242344401L;
	
	/** 投保信息 */
	private RegUsualAcceptVo usualAcceptVo =new RegUsualAcceptVo();
	/** 资料清单 */
    private List<RegFilesListVo> filesListVos =new ArrayList<RegFilesListVo>();
	/** 汇交清单 */
	private List<RegSgApplListVo> SgApplListVos = new ArrayList<RegSgApplListVo>(); 
	/** 家庭投保清单 */
	private List<RegFmApplListFormVo> fmApplListFormVos = new ArrayList<RegFmApplListFormVo>(); 
	/* 字段名：销售人员是否共同展业标识，长度：2，是否必填：否*/
	private String salesDevelopFlag;
	/** 展业信息 */
	private List<RegSalesListFormVo> salesListFormVos = new ArrayList<RegSalesListFormVo>();
	
	
    /**
     * @return the usualAcceptVo
     */
    public RegUsualAcceptVo getUsualAcceptVo() {
        return usualAcceptVo;
    }
    /**
     * @param usualAcceptVo the usualAcceptVo to set
     */
    public void setUsualAcceptVo(RegUsualAcceptVo usualAcceptVo) {
        this.usualAcceptVo = usualAcceptVo;
    }
    /**
     * @return the filesListVos
     */
    public List<RegFilesListVo> getFilesListVos() {
        return filesListVos;
    }
    /**
     * @param filesListVos the filesListVos to set
     */
    public void setFilesListVos(List<RegFilesListVo> filesListVos) {
        this.filesListVos = filesListVos;
    }
    /**
     * @return the sgApplListVos
     */
    public List<RegSgApplListVo> getSgApplListVos() {
        return SgApplListVos;
    }
    /**
     * @param sgApplListVos the sgApplListVos to set
     */
    public void setSgApplListVos(List<RegSgApplListVo> sgApplListVos) {
        SgApplListVos = sgApplListVos;
    }
    /**
     * @return the fmApplListFormVos
     */
    public List<RegFmApplListFormVo> getFmApplListFormVos() {
        return fmApplListFormVos;
    }
    /**
     * @param fmApplListFormVos the fmApplListFormVos to set
     */
    public void setFmApplListFormVos(List<RegFmApplListFormVo> fmApplListFormVos) {
        this.fmApplListFormVos = fmApplListFormVos;
    }
    
    public String getSalesDevelopFlag() {
		return salesDevelopFlag;
	}
	public void setSalesDevelopFlag(String salesDevelopFlag) {
		this.salesDevelopFlag = salesDevelopFlag;
	}
	/**
     * @return the salesListFormVos
     */
    public List<RegSalesListFormVo> getSalesListFormVos() {
        return salesListFormVos;
    }
    /**
     * @param salesListFormVos the salesListFormVos to set
     */
    public void setSalesListFormVos(List<RegSalesListFormVo> salesListFormVos) {
        this.salesListFormVos = salesListFormVos;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "RegAcceptanceVo [usualAcceptVo=" + usualAcceptVo + ", filesListVos=" + filesListVos + ", SgApplListVos="
                + SgApplListVos + ", fmApplListFormVos=" + fmApplListFormVos + ", salesListFormVos=" + salesListFormVos
                + "]";
    }
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((SgApplListVos == null) ? 0 : SgApplListVos.hashCode());
        result = prime * result + ((filesListVos == null) ? 0 : filesListVos.hashCode());
        result = prime * result + ((fmApplListFormVos == null) ? 0 : fmApplListFormVos.hashCode());
        result = prime * result + ((salesListFormVos == null) ? 0 : salesListFormVos.hashCode());
        result = prime * result + ((usualAcceptVo == null) ? 0 : usualAcceptVo.hashCode());
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
        RegAcceptanceVo other = (RegAcceptanceVo) obj;
        if (SgApplListVos == null) {
            if (other.SgApplListVos != null)
                return false;
        } else if (!SgApplListVos.equals(other.SgApplListVos))
            return false;
        if (filesListVos == null) {
            if (other.filesListVos != null)
                return false;
        } else if (!filesListVos.equals(other.filesListVos))
            return false;
        if (fmApplListFormVos == null) {
            if (other.fmApplListFormVos != null)
                return false;
        } else if (!fmApplListFormVos.equals(other.fmApplListFormVos))
            return false;
        if (salesListFormVos == null) {
            if (other.salesListFormVos != null)
                return false;
        } else if (!salesListFormVos.equals(other.salesListFormVos))
            return false;
        if (usualAcceptVo == null) {
            if (other.usualAcceptVo != null)
                return false;
        } else if (!usualAcceptVo.equals(other.usualAcceptVo))
            return false;
        return true;
    }
    
}
