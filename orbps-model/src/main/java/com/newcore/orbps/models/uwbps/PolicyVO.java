package com.newcore.orbps.models.uwbps;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 险种
 * @author huanghaiyang
 * 创建时间：2016年7月29日
 */
public class PolicyVO implements Serializable{
	private static final long serialVersionUID = -4754176011835332927L;
	/**
	 *  无法提交
	 */
	
	/* 核保ID		于浩添加*/
    private Long udwId;
    /*实际费率 rate		于浩添加*/
	private double rate;
	/*被保人序号，IPSN_NO	于浩添加*/
	private String ipsnNO;
    
	/* 字段名：险种，长度：8，是否必填：是 */
	@NotNull(message = "险种代码不能为空")
	private String polCode;

	private String polNameChn;

	/* 字段名：保险期间，是否必填：是 */
	@NotNull(message = "保险期间不能为空")
	private int insurDur;

	/*保险期间单位   A:保至年龄   Y：保险年数   W 终身   M保险月数   D 保险天数   O一次性*/
	private String insurDurUnit;

	/* 字段名：险种保额，是否必填：是 */
	@NotNull(message = "险种保额不能为空")
	private Double faceAmnt;

	/* 字段名：实际保费，是否必填：是 */
	@NotNull(message = "实际保费部能为空")
	private Double premium;

	/* 字段名：标准保费，是否必填：是 */
	@NotNull(message = "标准保费部能为空")
	private Double stdPremium;

	/* 字段名：险种投保人数，是否必填：是 */
	@NotNull(message = "险种投保人数不能为空")
	private Long polIpsnNum;

	/*缴费期*/
	@NotNull(message = "缴费期不能为空")
	private int moneyinDur;

	/*缴费期间单位   A 保至年龄  Y 缴费年数*/
	@NotNull(message = "缴费期间单位不能为空")
	private String moneyinDurUnit;

	/*主附险性质    M 主险   R附险（被保人）  E  附险（投保人）*/
	private String mrCode;

	/*保费折扣*/
	private double premDiscount ;


	//个人账户缴费金额
	private Double ipsnFundPremium;

	//计入个人账户金额
	private Double ipsnFundAmt;


	// 子险种 存在子险种必填
	private List<SubPolicyVO> subPolicyList;


	public Double getIpsnFundPremium() {
		return ipsnFundPremium;
	}

	public void setIpsnFundPremium(Double ipsnFundPremium) {
		this.ipsnFundPremium = ipsnFundPremium;
	}

	public Double getIpsnFundAmt() {
		return ipsnFundAmt;
	}

	public void setIpsnFundAmt(Double ipsnFundAmt) {
		this.ipsnFundAmt = ipsnFundAmt;
	}

	public String getPolNameChn() {
		return polNameChn;
	}

	public void setPolNameChn(String polNameChn) {
		this.polNameChn = polNameChn;
	}

	/**
	 * 
	 */


	public String getPolCode() {
		return polCode;
	}

	public void setPolCode(String polCode) {
		this.polCode = polCode;
	}

	public int getInsurDur() {
		return insurDur;
	}

	public void setInsurDur(int insurDur) {
		this.insurDur = insurDur;
	}

	public String getInsurDurUnit() {
		return insurDurUnit;
	}

	public void setInsurDurUnit(String insurDurUnit) {
		this.insurDurUnit = insurDurUnit;
	}

	public Double getFaceAmnt() {
		return faceAmnt;
	}

	public void setFaceAmnt(Double faceAmnt) {
		this.faceAmnt = faceAmnt;
	}

	public Double getPremium() {
		return premium;
	}

	public void setPremium(Double premium) {
		this.premium = premium;
	}

	public Double getStdPremium() {
		return stdPremium;
	}

	public void setStdPremium(Double stdPremium) {
		this.stdPremium = stdPremium;
	}

	public Long getPolIpsnNum() {
		return polIpsnNum;
	}

	public void setPolIpsnNum(Long polIpsnNum) {
		this.polIpsnNum = polIpsnNum;
	}

	public int getMoneyinDur() {
		return moneyinDur;
	}

	public void setMoneyinDur(int moneyinDur) {
		this.moneyinDur = moneyinDur;
	}

	public String getMoneyinDurUnit() {
		return moneyinDurUnit;
	}

	public void setMoneyinDurUnit(String moneyinDurUnit) {
		this.moneyinDurUnit = moneyinDurUnit;
	}

	public String getMrCode() {
		return mrCode;
	}

	public void setMrCode(String mrCode) {
		this.mrCode = mrCode;
	}

	public double getPremDiscount() {
		return premDiscount;
	}

	public void setPremDiscount(double premDiscount) {
		this.premDiscount = premDiscount;
	}

	public List<SubPolicyVO> getSubPolicyList() {
		return subPolicyList;
	}

	public void setSubPolicyList(List<SubPolicyVO> subPolicyList) {
		this.subPolicyList = subPolicyList;
	}

	public Long getUdwId() {
		return udwId;
	}

	public void setUdwId(Long udwId) {
		this.udwId = udwId;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getIpsnNO() {
		return ipsnNO;
	}

	public void setIpsnNO(String ipsnNO) {
		this.ipsnNO = ipsnNO;
	}
	
	
	
}
