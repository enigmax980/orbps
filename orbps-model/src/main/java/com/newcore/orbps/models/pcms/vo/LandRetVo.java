package com.newcore.orbps.models.pcms.vo;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 落地执行情况告知参数
 * Created by liushuaifeng on 2017/2/21 0021.
 */
public class LandRetVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4143554479834238482L;

	/**
	 * 投保单号
	 */
	@NotNull(message = "投保单号applNo不能为空")
	@Size(max = 16, message = "投保单号最多{max}个字符：{validatedValue}")
	private String applNo;

	/**
	 * 告知类型：1-财务落地，2-被保人落地
	 */
	@NotNull(message = "告知类型type不能为空")
	@Pattern(regexp = "[1,2,3]", message = "类型type不符合约定值：1-财务落地，2-被保人落地  3-应首付落地")
	private String type;

	/**
	 * 批次号
	 */
	@NotNull(message = "批次号batNo不能为空")
	private Long batNo;
	/**
	 *  返回代码 	0-失败，1-成功
	 */
	@NotNull(message = "返回码retCode不能为空")
	@Pattern(regexp = "[0,1]", message = "返回码retCode不符合约定值：0-失败，1-成功")
	private String retCode;
	/**
	 * 备注
	 */
	private String remark;

	public String getApplNo() {
		return applNo;
	}

	public void setApplNo(String applNo) {
		this.applNo = applNo;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getBatNo() {
		return batNo;
	}

	public void setBatNo(Long batNo) {
		this.batNo = batNo;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
