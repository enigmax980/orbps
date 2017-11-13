com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.subForm = $("#submit");
// 基本信息校验规则

$(function() {

	// 初始化校验函数
	com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.paylidateForm(com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.subForm);

	// 表单提交
	$("#btnSubmit")
			.click(
					function() {
						// jquery validate 校验
						if (com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.combinedForm
								.validate().form() 
							||com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.policyHolderForm
									.validate().form()
								||com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.recognizeeForm
										.validate().form()
									||com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.payForm
											.validate().form()) {
										// select校验
										alert(1);
										if (validateSelectVal()) {
											// 获取第一被保人信息下面的所有受益人信息
											var getAddRowsData = $("#ipsnListInfo").editDatagrids("getAddRowsData");
											alert(2);
											// 提交方法
										var ComBinedPolicyVo = {};
										ComBinedPolicyVo.ComBinedInsurVo = com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.insurInfoForm.serializeObject();
										ComBinedPolicyVo.vatInfoVo = com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.policyHolderForm.serializeObject();
										ComBinedPolicyVo.applBaseInfoVo = com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.recognizeeForm.serializeObject();
										ComBinedPolicyVo.proposalInfoVo = com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.payForm.serializeObject();
										ComBinedPolicyVo.ComBinedBeneficiaryVos = getAddRowsData;
										var responseVos = new Array();
										for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.benesList.length; i++) {
											var array_element = com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.benesList[i];
											for (var j = 0; j < array_element.length; j++) {
												var array_elements = array_element[j];
												responseVos.push(array_elements);
											}
											
										}
										ComBinedPolicyVo.responseVos = responseVos;
										alert(JSON.stringify(ComBinedPolicyVo));
										lion.util.postjson('/orbps/web/orbps/contractEntry/grp/submit',ComBinedPolicyVo,successSubmit,null,null);
														}
													}
					});

	
	
	// 校验选择信息
	function validateSelectVal() {

		var ipsnIdType = $("#policyHolderForm #ipsnIdType").val();
		if (ipsnIdType == null || "" == ipsnIdType) {
			lion.util.info("警告", "请选择证件类别");
			return false;
		}
		var sex = $("#policyHolderForm #sex").val();
		if (sex == null || "" == sex) {
			lion.util.info("警告", "请选择性别");
			return false;
		}
		var IpsnOccClassCod = $("#policyHolderForm #IpsnOccClassCod").val();
		if (IpsnOccClassCod == null || "" == IpsnOccClassCod) {
			lion.util.info("警告", "请选择职业代码");
			return false;
		}
		var occupationCategory = $("#policyHolderForm #occupationCategory").val();
		if (occupationCategory == null || "" == occupationCategory) {
			lion.util.info("警告", "请选择职业类别");
			return false;
		}

		var nationality = $("#policyHolderForm #nationality").val();
		if (nationality == null || "" == nationality) {
			lion.util.info("警告", "请选择国籍");
			return false;
		}
		
		var relToMstIpsn = $("#recognizeeForm #relToMstIpsn").val();
		if (relToMstIpsn == null || "" == relToMstIpsn) {
			lion.util.info("警告", "请选择与投保人关系");
			return false;
		}
		
		var relIpsnFlag = $("#recognizeeForm #relIpsnFlag").val();
		if (relIpsnFlag == null || "" == relIpsnFlag) {
			lion.util.info("警告", "请选择是否连带被保人");
			return false;
		}
		
		var ipsnIdType = $("#recognizeeForm #ipsnIdType").val();
		if (ipsnIdType == null || "" == ipsnIdType) {
			lion.util.info("警告", "请选择证件类别");
			return false;
		}
		
		var sex = $("#recognizeeForm #sex").val();
		if (sex == null || "" == sex) {
			lion.util.info("警告", "请选择性别");
			return false;
		}
		
		var IpsnOccClassCod = $("#recognizeeForm #IpsnOccClassCod").val();
		if (IpsnOccClassCod == null || "" == IpsnOccClassCod) {
			lion.util.info("警告", "请选择职业代码");
			return false;
		}
		
		var occupationCategory = $("#recognizeeForm #occupationCategory").val();
		if (occupationCategory == null || "" == occupationCategory) {
			lion.util.info("警告", "请选择职业类别");
			return false;
		}
		
		var relToMstIpsn = $("#recognizeeForm #relToMstIpsn").val();
		if (relToMstIpsn == null || "" == relToMstIpsn) {
			lion.util.info("警告", "请选择与主被保人关系");
			return false;
		}
		
		var nationality = $("#recognizeeForm #nationality").val();
		if (nationality == null || "" == nationality) {
			lion.util.info("警告", "请选择国籍");
			return false;
		}
		
		var ipsnSss = $("#recognizeeForm #ipsnSss").val();
		if (ipsnSss == null || "" == ipsnSss) {
			lion.util.info("警告", "请选择医保身份");
			return false;
		}
		
		var moneyinType = $("#payForm #moneyinType").val();
		if (moneyinType == null || "" == moneyinType) {
			lion.util.info("警告", "请选择交费方式");
			return false;
		}
		
		var ernstMoneyinType = $("#payForm #ernstMoneyinType").val();
		if (ernstMoneyinType == null || "" == ernstMoneyinType) {
			lion.util.info("警告", "请选择首期交费形式");
			return false;
		}
		
		var continuousInsurance = $("#payForm #continuousInsurance").val();
		if (continuousInsurance == null || "" == continuousInsurance) {
			lion.util.info("警告", "请选择连续投保标识");
			return false;
		}
		
		var currencyCode = $("#payForm #currencyCode").val();
		if (currencyCode == null || "" == currencyCode) {
			lion.util.info("警告", "请选择币种");
			return false;
		}
		
		var renewalFeeForm = $("#payForm #renewalFeeForm").val();
		if (renewalFeeForm == null || "" == renewalFeeForm) {
			lion.util.info("警告", "请选择续期交费形式");
			return false;
		}
		
		var payBankCode = $("#payForm #payBankCode").val();
		if (payBankCode == null || "" == payBankCode) {
			lion.util.info("警告", "请选择银行代码");
			return false;
		}
		
		var accountForm = $("#payForm #accountForm").val();
		if (accountForm == null || "" == accountForm) {
			lion.util.info("警告", "请选择账户形式");
			return false;
		}
		
		var ipsnRelationCode = $("#payForm #ipsnRelationCode").val();
		if (ipsnRelationCode == null || "" == ipsnRelationCode) {
			lion.util.info("警告", "请选择与投保人关系");
			return false;
		}
		
		var policyNature = $("#payForm #policyNature").val();
		if (policyNature == null || "" == policyNature) {
			lion.util.info("警告", "请选择保单性质");
			return false;
		}
		
		var serviceType = $("#payForm #serviceType").val();
		if (serviceType == null || "" == serviceType) {
			lion.util.info("警告", "请选择送达类型");
			return false;
		}
		
		var disputeResolution = $("#payForm #disputeResolution").val();
		if (disputeResolution == null || "" == disputeResolution) {
			lion.util.info("警告", "请选择合同争议处理方式");
			return false;
		}
		return true;
	}
});