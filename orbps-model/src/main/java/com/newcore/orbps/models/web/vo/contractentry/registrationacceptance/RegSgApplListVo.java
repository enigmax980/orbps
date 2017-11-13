package com.newcore.orbps.models.web.vo.contractentry.registrationacceptance;

import java.io.Serializable;

/**
 * 汇交清单
 * @author jincong
 *
 */
public class RegSgApplListVo implements Serializable{
	
	private static final long serialVersionUID = 13242344403L;
	
	/**字段名：投保单号开始*/
	private String applNoBegin;
		
	/**字段名：投保单号结束*/
	private String applNoEnd;

	public String getApplNoBegin() {
		return applNoBegin;
	}

	public void setApplNoBegin(String applNoBegin) {
		this.applNoBegin = applNoBegin;
	}

	public String getApplNoEnd() {
		return applNoEnd;
	}

	public void setApplNoEnd(String applNoEnd) {
		this.applNoEnd = applNoEnd;
	}

	@Override
	public String toString() {
		return "SgApplList [applNoBegin=" + applNoBegin + ", applNoEnd=" + applNoEnd + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((applNoBegin == null) ? 0 : applNoBegin.hashCode());
		result = prime * result + ((applNoEnd == null) ? 0 : applNoEnd.hashCode());
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
		RegSgApplListVo other = (RegSgApplListVo) obj;
		if (applNoBegin == null) {
			if (other.applNoBegin != null)
				return false;
		} else if (!applNoBegin.equals(other.applNoBegin))
			return false;
		if (applNoEnd == null) {
			if (other.applNoEnd != null)
				return false;
		} else if (!applNoEnd.equals(other.applNoEnd))
			return false;
		return true;
	}
	
	
}
