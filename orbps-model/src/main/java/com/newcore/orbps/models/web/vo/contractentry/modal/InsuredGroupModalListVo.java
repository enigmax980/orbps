package com.newcore.orbps.models.web.vo.contractentry.modal;

import java.io.Serializable;
import java.util.List;

/** 
* @ClassName: InsuredGroupModalListVo 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author  jiangbomeng
* @date 2016年11月22日 下午5:13:38 
*  
*/
public class InsuredGroupModalListVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8846444803690147404L;

	/**
	 * 错误提示 1 成功 0 失败 String  长度2
	 */
	private String errCode;
	
	/**
	 * 错误提示信息  长度500
	 */
	private String eccMsg;
	
	/**
	 * 子险种列表  array
	 */
	
	private List<ResponseVo> insuredGroupModalListVo;

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public String getEccMsg() {
		return eccMsg;
	}

	public void setEccMsg(String eccMsg) {
		this.eccMsg = eccMsg;
	}

	public List<ResponseVo> getInsuredGroupModalListVo() {
		return insuredGroupModalListVo;
	}

	public void setInsuredGroupModalListVo(List<ResponseVo> insuredGroupModalListVo) {
		this.insuredGroupModalListVo = insuredGroupModalListVo;
	}

	
	
}
