
package com.newcore.orbps.models.pcms.bo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

/**
* <p>Busi_data表 </p>
* @author zhangyuan
* @date 2016年11月28日
*/
public class BusiDataInBo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6702357035143591287L;
	/**
	 * 数据编号
	 */
	private Long taskSeq;
	/**
	 * 数据来源
	 */
	@NotEmpty(message="[数据来源不能为空]")
	@Length(max=10,message="[数据来源长度不能超过10位]") 
	private String dataSource;
	/**
	 * 业务类别
	 */
	@NotEmpty(message="[业务类别不能为空]")
	@Length(max=3,message="[业务类别长度不能超过3位]") 
	private String transType;
	/**
	 * 产生日期
	 */
	private Date createDate;
	/**
	 * 流水号
	 */
	@Length(max=30,message="[流水号长度不能超过30位]") 
	private String serNo;
	
	/**
	 * 批单号
	 */
	@Length(max=30,message="[批单号长度不能超过30位]") 
	private String batchNo;
	
	/**
	 * 投保单号
	 */
	@Length(max=30,message="[投保单号长度不能超过30位]") 
	private String insureNo;	
	/**
	 * 收付费项目
	 */
	@Length(max=2,message="[收付费项目长度不能超过2位]") 
	private String transCode;	
	
	/**
	 * 险种代码
	 */
	@Length(max=10,message="[险种代码长度不能超过10位]") 
	private String riskCode;
	/**
	 * 收付费方式
	 */
	@Length(max=3,message="[收付费方式长度不能超过3位]") 
	private String payType;
	/**
	 * 应收付日期
	 */
	private Date  ysfDate;
	/**
	 * 投保人/领款人名称
	 */
	@Length(max=200,message="[投保人/领款人名称长度不能超过200位]") 
	private String itemDesc;
	/**
	 * 金额
	 */
	private BigDecimal moneyNum;
	/**
	 * 收据号
	 */
	@Length(max=30,message="[收据号长度不能超过30位]") 
	private String receiptNo;
	/**
	 * 单证印刷号
	 */
	@Length(max=30,message="[单证印刷号长度不能超过30位]") 
	private String recprintNo;
	/**
	 * 支票号/缴款单号
	 */
	@Length(max=15,message="[支票号/缴款单号长度不能超过15位]") 
	private String checkNo;
	/**
	 * 保单管理单位
	 */
	@Length(max=6,message="[保单管理单位长度不能超过6位]") 
	private String mgBranch;
	/**
	 * 销售渠道
	 */
	@Length(max=10,message="[销售渠道长度不能超过10位]") 
	private String sellChannel;
	/**
	 * 代收管理费的操作员所在的省公司代码
	 */
	@Length(max=6,message="[代收管理费的操作员所在的省公司代码长度不能超过6位]") 
	private String superBranch;
	/**
	 * 流水标识号
	 */
	@NotEmpty(message="[流水标识号不能为空]")
	@Length(max=30,message="[流水标识号长度不能超过30位]") 
	private String seqNo;
	/**
	 * 业务日期
	 */
	private Date policyDate;
	/**
	 * 保单号
	 */
	@Length(max=30,message="[保单号长度不能超过30位]") 
	private String policyNo;
	/**
	 * 销售渠道明细
	 */
	@Length(max=10,message="[销售渠道明细长度不能超过10位]") 
	private String channelDetail;
	/**
	 * 缴费方式
	 */
	@Length(max=3,message="[缴费方式长度不能超过3位]") 
	private String payMode;
	/**
	 * 岁满险种标志：到多少岁满期
	 */
	@Length(max=2,message="[岁满险种标志长度不能超过2位]") 
	private String ageInsure;
	/**
	 * 保单生效日
	 */
	private Date operDate;
	/**
	 * 团个单标识
	 */
	@Length(max=2,message="[团个单标识长度不能超过2位]") 
	private String singleFlag;
	/**
	 * 附加险主险代码
	 */
	@Length(max=10,message="[附加险主险代码长度不能超过10位]")
	private String fmaincode;
	/**
	 * 批单号
	 */
	@Length(max=30,message="[批单号长度不能超过30位]")
	private String alterNo;
	/**
	 * 赔案号
	 */
	@Length(max=30,message="[赔案号长度不能超过30位]")
	private String damageNo;
	/**
	 * 保险期间（有效期数）
	 */
	private Long avalibleTerm;
	/**
	 * 保单的缴费年期
	 */
	@Length(max=3,message="[保单的缴费年期长度不能超过3位]")
	private String payTerm;
	/**
	 * 被保人生日
	 */
	private Date birthDay;
	/**
	 * 承保年度
	 */
	@Length(max=4,message="[承保年度长度不能超过4位]")
	private String agreementYear;
	/**
	 * 保险公司银行帐号
	 */
	@Length(max=30,message="[保险公司银行帐号长度不能超过30位]")
	private String accountCode;
	/**
	 * 签单日
	 */
	private Date pactDate;
	/**
	 * 红利的首次领取日期
	 */
	private Date firstpayDate;
	/**
	 * 有效期类型
	 */
	@Length(max=1,message="[有效期类型长度不能超过1位]")
	private String avalibleType;
	/**
	 * 币别
	 */
	@Length(max=3,message="[币别长度不能超过3位]")
	private String currency;
	/**
	 * 年金/满期标志
	 */
	@Length(max=2,message="[年金/满期标志长度不能超过2位]")
	private String lastTerm;
	/**
	 * 特殊业务类别
	 */
	@Length(max=2,message="[特殊业务类别长度不能超过2位]")
	private String spectrans;
	/**
	 * 清算收据号
	 */
	@Length(max=30,message="[清算收据号长度不能超过30位]")
	private String clearno;
	/**
	 * 入帐机构
	 */
	@Length(max=10,message="[入帐机构长度不能超过10位]")
	private String otherBranch;
	/**
	 * 销售人员代码
	 */
	@Length(max=20,message="[销售人员代码不能超过20位]")
	private String sellerCode;
	/**
	 * 操作员编号
	 */
	@Length(max=10,message="[操作员编号不能超过10位]")
	private String operatorCode;
	/**
	 * 操作员机构
	 */
	@Length(max=6,message="[操作员机构不能超过6位]")
	private String operatorBranch;
	/**
	 * 中介机构网点号
	 */
	@Length(max=14,message="[中介机构网点号不能超过14位]")
	private String agencyBranch;
	/**
	 * 到帐确认标识
	 */
	@Length(max=1,message="[到帐确认标识不能超过1位]")
	private String accflag;
	/**
	 * 缴费期次
	 */
	@Length(max=20,message="[缴费期次不能超过20位]")	
	private String paytime;
	/**
	 * 被保险人投保时年龄
	 */
	private Long age;
	/**
	 * 领取方式
	 */
	@Length(max=1,message="[领取方式不能超过1位]")	
	private String takeType;
	/**
	 * 营销员所属销售机构
	 */
	@Length(max=6,message="[营销员所属销售机构不能超过6位]")	
	private String sellerBranch;
	/**
	 * 保险公司银行帐号所属机构
	 */
	@Length(max=6,message="[保险公司银行帐号所属机构不能超过6位]")	
	private String accountBranch;
	/**
	 * 营销员所属部门代码
	 */
	@Length(max=6,message="[营销员所属部门代码不能超过6位]")	
	private String sellerdept;
	private String articleCode;
	/**
	 * 剩余给付年限
	 */
	private Long shengyuYear;
	/**
	 * 现金价值
	 */
	private BigDecimal cashValue;
	/**
	 * 管理费类型
	 */
	@Length(max=2,message="[管理费类型不能超过2位]")	
	private String feetype;
	/**
	 * 产品附加信息
	 */
	private BigDecimal productInfo;
	/**
	 * 产品类型
	 */
	@Length(max=2,message="[产品类型不能超过2位]")	
	private String productType;
	/**
	 * 年金产品名称
	 */
	@Length(max=50,message="[年金产品名称不能超过50位]")	
	private String productName;
	/**
	 * 产品代码
	 */
	@Length(max=22,message="[产品代码不能超过22位]")	
	private String  productCode;
	/**
	 * 产品规模
	 */
	private BigDecimal productSize;
	/**
	 * 团体年金的计划编号
	 */
	@Length(max=20,message="[团体年金的计划编号不能超过20位]")	
	private String planNo;
	/**
	 * 扩展字段1
	 */
	@Length(max=20,message="[扩展字段1不能超过20位]")	
	private String EXT01;
	/**
	 * 扩展字段2
	 */
	@Length(max=20,message="[扩展字段2不能超过20位]")	
	private String EXT02;
	/**
	 * 扩展字段3
	 */
	@Length(max=20,message="[扩展字段3不能超过20位]")	
	private String EXT03;
	/**
	 * 扩展字段4
	 */
	@Length(max=20,message="[扩展字段4不能超过20位]")	
	private String EXT04;
	/**
	 * 扩展字段5
	 */
	@Length(max=20,message="[扩展字段5不能超过20位]")	
	private String EXT05;
	/**
	 * 扩展字段6
	 */
	@Length(max=20,message="[扩展字段6不能超过20位]")	
	private String EXT06;
	/**
	 * 扩展字段7
	 */
	@Length(max=20,message="[扩展字段7不能超过20位]")	
	private String EXT07;
	/**
	 * 扩展字段8
	 */
	@Length(max=20,message="[扩展字段8不能超过20位]")	
	private String EXT08;
	/**
	 * 扩展字段9
	 */
	@Length(max=20,message="[扩展字段9不能超过20位]")	
	private String EXT09;
	/**
	 * 扩展字段10
	 */
	@Length(max=20,message="[扩展字段10不能超过20位]")	
	private String EXT10;
	public Long getTaskSeq() {
		return taskSeq;
	}
	public void setTaskSeq(Long taskSeq) {
		this.taskSeq = taskSeq;
	}
	public String getDataSource() {
		return dataSource;
	}
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}
	public String getTransType() {
		return transType;
	}
	public void setTransType(String transType) {
		this.transType = transType;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getSerNo() {
		return serNo;
	}
	public void setSerNo(String serNo) {
		this.serNo = serNo;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getInsureNo() {
		return insureNo;
	}
	public void setInsureNo(String insureNo) {
		this.insureNo = insureNo;
	}
	public String getTransCode() {
		return transCode;
	}
	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}
	public String getRiskCode() {
		return riskCode;
	}
	public void setRiskCode(String riskCode) {
		this.riskCode = riskCode;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public Date getYsfDate() {
		return ysfDate;
	}
	public void setYsfDate(Date ysfDate) {
		this.ysfDate = ysfDate;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public BigDecimal getMoneyNum() {
		return moneyNum;
	}
	public void setMoneyNum(BigDecimal moneyNum) {
		this.moneyNum = moneyNum;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}
	public String getRecprintNo() {
		return recprintNo;
	}
	public void setRecprintNo(String recprintNo) {
		this.recprintNo = recprintNo;
	}
	public String getCheckNo() {
		return checkNo;
	}
	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}
	public String getMgBranch() {
		return mgBranch;
	}
	public void setMgBranch(String mgBranch) {
		this.mgBranch = mgBranch;
	}
	public String getSellChannel() {
		return sellChannel;
	}
	public void setSellChannel(String sellChannel) {
		this.sellChannel = sellChannel;
	}
	public String getSuperBranch() {
		return superBranch;
	}
	public void setSuperBranch(String superBranch) {
		this.superBranch = superBranch;
	}
	public String getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	public Date getPolicyDate() {
		return policyDate;
	}
	public void setPolicyDate(Date policyDate) {
		this.policyDate = policyDate;
	}
	public String getPolicyNo() {
		return policyNo;
	}
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	public String getChannelDetail() {
		return channelDetail;
	}
	public void setChannelDetail(String channelDetail) {
		this.channelDetail = channelDetail;
	}
	public String getPayMode() {
		return payMode;
	}
	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}
	public String getAgeInsure() {
		return ageInsure;
	}
	public void setAgeInsure(String ageInsure) {
		this.ageInsure = ageInsure;
	}
	public Date getOperDate() {
		return operDate;
	}
	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}
	public String getSingleFlag() {
		return singleFlag;
	}
	public void setSingleFlag(String singleFlag) {
		this.singleFlag = singleFlag;
	}
	public String getFmaincode() {
		return fmaincode;
	}
	public void setFmaincode(String fmaincode) {
		this.fmaincode = fmaincode;
	}
	public String getAlterNo() {
		return alterNo;
	}
	public void setAlterNo(String alterNo) {
		this.alterNo = alterNo;
	}
	public String getDamageNo() {
		return damageNo;
	}
	public void setDamageNo(String damageNo) {
		this.damageNo = damageNo;
	}
	public Long getAvalibleTerm() {
		return avalibleTerm;
	}
	public void setAvalibleTerm(Long avalibleTerm) {
		this.avalibleTerm = avalibleTerm;
	}
	public String getPayTerm() {
		return payTerm;
	}
	public void setPayTerm(String payTerm) {
		this.payTerm = payTerm;
	}
	public Date getBirthDay() {
		return birthDay;
	}
	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}
	public String getAgreementYear() {
		return agreementYear;
	}
	public void setAgreementYear(String agreementYear) {
		this.agreementYear = agreementYear;
	}
	public String getAccountCode() {
		return accountCode;
	}
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	public Date getPactDate() {
		return pactDate;
	}
	public void setPactDate(Date pactDate) {
		this.pactDate = pactDate;
	}
	public Date getFirstpayDate() {
		return firstpayDate;
	}
	public void setFirstpayDate(Date firstpayDate) {
		this.firstpayDate = firstpayDate;
	}
	public String getAvalibleType() {
		return avalibleType;
	}
	public void setAvalibleType(String avalibleType) {
		this.avalibleType = avalibleType;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getLastTerm() {
		return lastTerm;
	}
	public void setLastTerm(String lastTerm) {
		this.lastTerm = lastTerm;
	}
	public String getSpectrans() {
		return spectrans;
	}
	public void setSpectrans(String spectrans) {
		this.spectrans = spectrans;
	}
	public String getClearno() {
		return clearno;
	}
	public void setClearno(String clearno) {
		this.clearno = clearno;
	}
	public String getOtherBranch() {
		return otherBranch;
	}
	public void setOtherBranch(String otherBranch) {
		this.otherBranch = otherBranch;
	}
	public String getSellerCode() {
		return sellerCode;
	}
	public void setSellerCode(String sellerCode) {
		this.sellerCode = sellerCode;
	}
	public String getOperatorCode() {
		return operatorCode;
	}
	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}
	public String getOperatorBranch() {
		return operatorBranch;
	}
	public void setOperatorBranch(String operatorBranch) {
		this.operatorBranch = operatorBranch;
	}
	public String getAgencyBranch() {
		return agencyBranch;
	}
	public void setAgencyBranch(String agencyBranch) {
		this.agencyBranch = agencyBranch;
	}
	public String getAccflag() {
		return accflag;
	}
	public void setAccflag(String accflag) {
		this.accflag = accflag;
	}
	public String getPaytime() {
		return paytime;
	}
	public void setPaytime(String paytime) {
		this.paytime = paytime;
	}
	public Long getAge() {
		return age;
	}
	public void setAge(Long age) {
		this.age = age;
	}
	public String getTakeType() {
		return takeType;
	}
	public void setTakeType(String takeType) {
		this.takeType = takeType;
	}
	public String getSellerBranch() {
		return sellerBranch;
	}
	public void setSellerBranch(String sellerBranch) {
		this.sellerBranch = sellerBranch;
	}
	public String getAccountBranch() {
		return accountBranch;
	}
	public void setAccountBranch(String accountBranch) {
		this.accountBranch = accountBranch;
	}
	public String getSellerdept() {
		return sellerdept;
	}
	public void setSellerdept(String sellerdept) {
		this.sellerdept = sellerdept;
	}
	public String getArticleCode() {
		return articleCode;
	}
	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}
	public Long getShengyuYear() {
		return shengyuYear;
	}
	public void setShengyuYear(Long shengyuYear) {
		this.shengyuYear = shengyuYear;
	}
	public BigDecimal getCashValue() {
		return cashValue;
	}
	public void setCashValue(BigDecimal cashValue) {
		this.cashValue = cashValue;
	}
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public BigDecimal getProductInfo() {
		return productInfo;
	}
	public void setProductInfo(BigDecimal productInfo) {
		this.productInfo = productInfo;
	}
	public String getProductType() {
		return productType;
	}
	public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public BigDecimal getProductSize() {
		return productSize;
	}
	public void setProductSize(BigDecimal productSize) {
		this.productSize = productSize;
	}
	public String getPlanNo() {
		return planNo;
	}
	public void setPlanNo(String planNo) {
		this.planNo = planNo;
	}
	public String getEXT01() {
		return EXT01;
	}
	public void setEXT01(String eXT01) {
		EXT01 = eXT01;
	}
	public String getEXT02() {
		return EXT02;
	}
	public void setEXT02(String eXT02) {
		EXT02 = eXT02;
	}
	public String getEXT03() {
		return EXT03;
	}
	public void setEXT03(String eXT03) {
		EXT03 = eXT03;
	}
	public String getEXT04() {
		return EXT04;
	}
	public void setEXT04(String eXT04) {
		EXT04 = eXT04;
	}
	public String getEXT05() {
		return EXT05;
	}
	public void setEXT05(String eXT05) {
		EXT05 = eXT05;
	}
	public String getEXT06() {
		return EXT06;
	}
	public void setEXT06(String eXT06) {
		EXT06 = eXT06;
	}
	public String getEXT07() {
		return EXT07;
	}
	public void setEXT07(String eXT07) {
		EXT07 = eXT07;
	}
	public String getEXT08() {
		return EXT08;
	}
	public void setEXT08(String eXT08) {
		EXT08 = eXT08;
	}
	public String getEXT09() {
		return EXT09;
	}
	public void setEXT09(String eXT09) {
		EXT09 = eXT09;
	}
	public String getEXT10() {
		return EXT10;
	}
	public void setEXT10(String eXT10) {
		EXT10 = eXT10;
	}
	
	
	
}
