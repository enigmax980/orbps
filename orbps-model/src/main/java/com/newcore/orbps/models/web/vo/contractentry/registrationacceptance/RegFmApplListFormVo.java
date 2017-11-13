package com.newcore.orbps.models.web.vo.contractentry.registrationacceptance;

import java.io.Serializable;
import java.util.Date;

/**
 * 家庭投保清单
 * @author jincong
 *
 */
public class RegFmApplListFormVo implements Serializable{

	private static final long serialVersionUID = 13242344402L;
	
	/**字段名：外包录入标记*/
	private String entChannelFlag;
	
	/**字段名：投保单号*/
	private String applNo;	
	
	/**字段名：申请日期 */
	private Date applDate;	
	
	/**字段名：契约形势 */
	private String insurType;
	
	/**字段名：险种 */
	private String polCode;
	
	/**字段名：保费合计*/
	private Double sumPremiume;
	
	/**字段名：投保人 */
	private String hldrName;
	
	/**字段名：被保险人*/
	private String ipsnName;
	
	/**字段名：销售渠道 */
	private String salesChannel;
	
	/**字段名：销售机构代码 */
	private String salesBranchNo;
	
	/**字段名：销售人员代码 */
	private String salesNo;
	
	
	/**字段名：投保单号*/
	private String applNoTb;	
	
	/**字段名：申请日期 */
	private Date applDateTb;	
	
	/**字段名：契约形势*/
	private String insurTypeTb;
	
	/**字段名：险种 */
	private String polCodeTb;
	
	/**字段名：保费合计 */
	private Double sumPremiumeTb;
	
	/**字段名：投保人*/
	private String hldrNameTb;
		
	/**字段名：被保险人*/
	private String ipsnNameTb;
	
	/**字段名：销售渠道*/
	private String salesChannelTb;
	
	/**字段名：销售机构代码*/
	private String salesBranchNoTb;
	
	/**字段名：销售人员代码 */
	private String salesNoTb;

	public String getEntChannelFlag() {
		return entChannelFlag;
	}

	public void setEntChannelFlag(String entChannelFlag) {
		this.entChannelFlag = entChannelFlag;
	}

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public Date getApplDate() {
		return applDate;
	}

	public void setApplDate(Date applDate) {
		this.applDate = applDate;
	}

	public String getInsurType() {
		return insurType;
	}

	public void setInsurType(String insurType) {
		this.insurType = insurType;
	}

	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public Double getSumPremiume() {
		return sumPremiume;
	}

	public void setSumPremiume(Double sumPremiume) {
		this.sumPremiume = sumPremiume;
	}

	public String getHldrName() {
		return hldrName;
	}

	public void setHldrName(String hldrName) {
		this.hldrName = hldrName;
	}

	public String getIpsnName() {
		return ipsnName;
	}

	public void setIpsnName(String ipsnName) {
		this.ipsnName = ipsnName;
	}

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
	
	

	@Override
	public String toString() {
		return "FmApplListFormVo [entChannelFlag=" + entChannelFlag + ", applNo=" + applNo + ", applDate=" + applDate
				+ ", insurType=" + insurType + ", polCode=" + polCode + ", sumPremiume=" + sumPremiume + ", hldrName="
				+ hldrName + ", ipsnName=" + ipsnName + ", salesChannel=" + salesChannel + ", salesBranchNo="
				+ salesBranchNo + ", salesNo=" + salesNo + ", applNoTb=" + applNoTb + ", applDateTb=" + applDateTb
				+ ", insurTypeTb=" + insurTypeTb + ", polCodeTb=" + polCodeTb + ", sumPremiumeTb=" + sumPremiumeTb
				+ ", hldrNameTb=" + hldrNameTb + ", ipsnNameTb=" + ipsnNameTb + ", salesChannelTb=" + salesChannelTb
				+ ", salesBranchNoTb=" + salesBranchNoTb + ", salesNoTb=" + salesNoTb + "]";
	}

	public String getApplNoTb() {
		return applNoTb;
	}

	public void setApplNoTb(String applNoTb) {
		this.applNoTb = applNoTb;
	}

	public Date getApplDateTb() {
		return applDateTb;
	}

	public void setApplDateTb(Date applDateTb) {
		this.applDateTb = applDateTb;
	}

	public String getInsurTypeTb() {
		return insurTypeTb;
	}

	public void setInsurTypeTb(String insurTypeTb) {
		this.insurTypeTb = insurTypeTb;
	}

	public String getPolCodeTb() {
		return polCodeTb;
	}

	public void setPolCodeTb(String polCodeTb) {
		this.polCodeTb = polCodeTb;
	}

	public Double getSumPremiumeTb() {
		return sumPremiumeTb;
	}

	public void setSumPremiumeTb(Double sumPremiumeTb) {
		this.sumPremiumeTb = sumPremiumeTb;
	}

	public String getHldrNameTb() {
		return hldrNameTb;
	}

	public void setHldrNameTb(String hldrNameTb) {
		this.hldrNameTb = hldrNameTb;
	}

	public String getIpsnNameTb() {
		return ipsnNameTb;
	}

	public void setIpsnNameTb(String ipsnNameTb) {
		this.ipsnNameTb = ipsnNameTb;
	}

	public String getSalesChannelTb() {
		return salesChannelTb;
	}

	public void setSalesChannelTb(String salesChannelTb) {
		this.salesChannelTb = salesChannelTb;
	}

	public String getSalesBranchNoTb() {
		return salesBranchNoTb;
	}

	public void setSalesBranchNoTb(String salesBranchNoTb) {
		this.salesBranchNoTb = salesBranchNoTb;
	}

	public String getSalesNoTb() {
		return salesNoTb;
	}

	public void setSalesNoTb(String salesNoTb) {
		this.salesNoTb = salesNoTb;
	}

	public void setSalesNo(String salesNo) {
		this.salesNo = salesNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applDate == null) ? 0 : applDate.hashCode());
		result = prime * result + ((applDateTb == null) ? 0 : applDateTb.hashCode());
		result = prime * result + ((applNo == null) ? 0 : applNo.hashCode());
		result = prime * result + ((applNoTb == null) ? 0 : applNoTb.hashCode());
		result = prime * result + ((entChannelFlag == null) ? 0 : entChannelFlag.hashCode());
		result = prime * result + ((hldrName == null) ? 0 : hldrName.hashCode());
		result = prime * result + ((hldrNameTb == null) ? 0 : hldrNameTb.hashCode());
		result = prime * result + ((insurType == null) ? 0 : insurType.hashCode());
		result = prime * result + ((insurTypeTb == null) ? 0 : insurTypeTb.hashCode());
		result = prime * result + ((ipsnName == null) ? 0 : ipsnName.hashCode());
		result = prime * result + ((ipsnNameTb == null) ? 0 : ipsnNameTb.hashCode());
		result = prime * result + ((polCode == null) ? 0 : polCode.hashCode());
		result = prime * result + ((polCodeTb == null) ? 0 : polCodeTb.hashCode());
		result = prime * result + ((salesBranchNo == null) ? 0 : salesBranchNo.hashCode());
		result = prime * result + ((salesBranchNoTb == null) ? 0 : salesBranchNoTb.hashCode());
		result = prime * result + ((salesChannel == null) ? 0 : salesChannel.hashCode());
		result = prime * result + ((salesChannelTb == null) ? 0 : salesChannelTb.hashCode());
		result = prime * result + ((salesNo == null) ? 0 : salesNo.hashCode());
		result = prime * result + ((salesNoTb == null) ? 0 : salesNoTb.hashCode());
		result = prime * result + ((sumPremiume == null) ? 0 : sumPremiume.hashCode());
		result = prime * result + ((sumPremiumeTb == null) ? 0 : sumPremiumeTb.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RegFmApplListFormVo other = (RegFmApplListFormVo) obj;
		if (applDate == null) {
			if (other.applDate != null)
				return false;
		} else if (!applDate.equals(other.applDate))
			return false;
		if (applDateTb == null) {
			if (other.applDateTb != null)
				return false;
		} else if (!applDateTb.equals(other.applDateTb))
			return false;
		if (applNo == null) {
			if (other.applNo != null)
				return false;
		} else if (!applNo.equals(other.applNo))
			return false;
		if (applNoTb == null) {
			if (other.applNoTb != null)
				return false;
		} else if (!applNoTb.equals(other.applNoTb))
			return false;
		if (entChannelFlag == null) {
			if (other.entChannelFlag != null)
				return false;
		} else if (!entChannelFlag.equals(other.entChannelFlag))
			return false;
		if (hldrName == null) {
			if (other.hldrName != null)
				return false;
		} else if (!hldrName.equals(other.hldrName))
			return false;
		if (hldrNameTb == null) {
			if (other.hldrNameTb != null)
				return false;
		} else if (!hldrNameTb.equals(other.hldrNameTb))
			return false;
		if (insurType == null) {
			if (other.insurType != null)
				return false;
		} else if (!insurType.equals(other.insurType))
			return false;
		if (insurTypeTb == null) {
			if (other.insurTypeTb != null)
				return false;
		} else if (!insurTypeTb.equals(other.insurTypeTb))
			return false;
		if (ipsnName == null) {
			if (other.ipsnName != null)
				return false;
		} else if (!ipsnName.equals(other.ipsnName))
			return false;
		if (ipsnNameTb == null) {
			if (other.ipsnNameTb != null)
				return false;
		} else if (!ipsnNameTb.equals(other.ipsnNameTb))
			return false;
		if (polCode == null) {
			if (other.polCode != null)
				return false;
		} else if (!polCode.equals(other.polCode))
			return false;
		if (polCodeTb == null) {
			if (other.polCodeTb != null)
				return false;
		} else if (!polCodeTb.equals(other.polCodeTb))
			return false;
		if (salesBranchNo == null) {
			if (other.salesBranchNo != null)
				return false;
		} else if (!salesBranchNo.equals(other.salesBranchNo))
			return false;
		if (salesBranchNoTb == null) {
			if (other.salesBranchNoTb != null)
				return false;
		} else if (!salesBranchNoTb.equals(other.salesBranchNoTb))
			return false;
		if (salesChannel == null) {
			if (other.salesChannel != null)
				return false;
		} else if (!salesChannel.equals(other.salesChannel))
			return false;
		if (salesChannelTb == null) {
			if (other.salesChannelTb != null)
				return false;
		} else if (!salesChannelTb.equals(other.salesChannelTb))
			return false;
		if (salesNo == null) {
			if (other.salesNo != null)
				return false;
		} else if (!salesNo.equals(other.salesNo))
			return false;
		if (salesNoTb == null) {
			if (other.salesNoTb != null)
				return false;
		} else if (!salesNoTb.equals(other.salesNoTb))
			return false;
		if (sumPremiume == null) {
			if (other.sumPremiume != null)
				return false;
		} else if (!sumPremiume.equals(other.sumPremiume))
			return false;
		if (sumPremiumeTb == null) {
			if (other.sumPremiumeTb != null)
				return false;
		} else if (!sumPremiumeTb.equals(other.sumPremiumeTb))
			return false;
		return true;
	}

	
	
}
