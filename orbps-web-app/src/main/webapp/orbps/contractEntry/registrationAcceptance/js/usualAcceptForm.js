com.orbps.contractEntry.registrationAcceptance.usualAcceptForm = $("#usualAcceptForm");
com.orbps.contractEntry.registrationAcceptance.polName = "";
com.orbps.contractEntry.registrationAcceptance.usualValidateForm = function(
		vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		rules : {
			applNo : {
				required : true,
				isApplNo : true
			},
			applNum : {
				required : true,
				isIntGteZero : true
			},
			insuredNum : {
				required : true,
				isIntGteZero : true
			},
			insurType : {
				required : true
			},
			polCode : {
				required : true
			},
			polName : {
				required : true
			},
			hldrName : {
				required : true
			},
			sumPremium : {
				required : true,
				isFloatGteZero : true
			}
		},
		messages : {
			applNo : {
				required : '请输入投保单号',
				isApplNo : '投保单号为16位字母和数字组合！'
			},
			applNum : {
				required : true,
				isIntGteZero : '投保单数量必须大于0'
			},
			insuredNum : {
				required : '请输入被保险人人数',
				isIntGteZero : '被保险人人数必须大于0'
			},
			insurType : {
				required : "请选择契约形式"
			},
			polCode : {
				required : "请输入险种"
			},
			polName : {
				required : "请输入险种名称"
			},
			hldrName : {
				required : '请输入投保人或汇交人姓名'
			},
			sumPremium : {
				required : '保费合计不能为空',
				isFloatGteZero : '合计保费必须大于0'
			}
		},
		invalidHandler : function(event, validator) {
			Metronic.scrollTo(error2, -200);
		},

		errorPlacement : function(error, element) {
			var icon = $(element).parent('.input-icon').children('i');
			icon.removeClass('fa-check').addClass("fa-warning");
			if (icon.attr('title')
					|| typeof icon.attr('data-original-title') != 'string') {
				icon.attr('data-original-title', icon.attr('title') || '')
						.attr('title', error.text())
			}
		},

		highlight : function(element) {
			$(element).closest('.col-md-2').removeClass("has-success")
					.addClass('has-error');
		},

		success : function(label, element) {
			var icon = $(element).parent('.input-icon').children('i');
			$(element).closest('.col-md-2').removeClass('has-error').addClass(
					'has-success');
			icon.removeClass("fa-warning").addClass("fa-check");
		}
	});
}

$(function() {
	// 初始化校验函数
	com.orbps.contractEntry.registrationAcceptance
			.usualValidateForm(com.orbps.contractEntry.registrationAcceptance.usualAcceptForm);
	setTimeout(function() {
		// 币种为空值时下拉框默认显示人民币
		var currencyCode = $("#currencyCode").val();
		if(null === currencyCode || "" === currencyCode){			
			$("#currencyCode").combo("val", "CNY");
		}
		// 外包录入标记为空值时下拉框默认显示否
		var entChannelFlag = $("#entChannelFlag").val();
		if(null === entChannelFlag || "" === entChannelFlag){
			$("#entChannelFlag").combo("val", "N");
		}
		// 是否共享客户信息标记下拉框默认显示否
		var paraVal = $("#paraVal").val();
		if(null === paraVal || "" === paraVal){			
			$("#paraVal").combo("val", "N");
		}
		// 是否共保标记下拉框默认显示否
		var agreementFlag = $("#agreementFlag").val();
		if(null === agreementFlag || "" === agreementFlag){			
			$("#agreementFlag").combo("val", "N");
		}
		//是否有网点代理信息，默认为否
		var salesAgencyFlag = $("#salesAgencyFlag").val();
		if(null === salesAgencyFlag || "" === salesAgencyFlag){			
			$("#salesAgencyFlag").combo("val", "N");
		}
		//如果网点代理标识为Y，则共同展业标识置为否
		if("Y" === salesAgencyFlag){
			$("#salesDevelopFlag").combo("val", "N");
			$("#salesDevelopFlag").attr("readOnly",true);
		}
		//是否共同展业默认为否
		var salesDevelopFlag = $("#salesDevelopFlag").val();
		if(null === salesDevelopFlag || "" === salesDevelopFlag){			
			$("#salesDevelopFlag").combo("val", "N");
		}
	}, 1000);
	// 显示共保协议号
	$("#agreementFlag").change(function() {
		var flag = $("#agreementFlag").val();
		if (flag === "Y") {
			$("#agreementNum").val("");
			$("#agreementShow").show();
		} else {
			$("#agreementNum").val("");
			$("#agreementShow").hide();
		}
	});

	// 显示主副标记*
	$("#salesDevelopFlag").change(function() {
		var flag = $("#salesDevelopFlag").val();
		if (flag === "Y") {
			$("#salesDevelopFlagIs").show();
		} else {
			$("#salesDevelopFlagIs").hide();
		}
	});
	
	// 根据销售渠道更改页面样式，默认隐藏网点信息。
	$("#worksiteHideDiv").hide();
	$("#salesListForm #salesChannel").change(function() {
		if ("OA" === $("#salesChannel").val()) {
			$("#worksiteHideDiv").show();
			$("#salesHideDiv").hide();
			$("#salesListForm #salesNo").val("");
		} else {
			$("#worksiteHideDiv").hide();
			$("#salesHideDiv").show();
			$("#salesListForm #worksiteNo").val("");
		}
	});

	// 根据保单类型，控制投保单数量
	$("#policyType").change(function() {
		var flag = $("#policyType").val();
		$("#applNum").val(1);
		$("#applNum").attr("readOnly", true);
		if ("" === flag) {
			$("#applNum").val("");
			$("#applNum").attr("readOnly", false);
		}
	});

	// 显示个单汇交件
	// $("#policyType").change(function(){
	// var flag = $("#policyType").val();
	// if (flag == "L") {
	// $("#sgApplListForm").show();
	// } else {
	// $("#sgApplListForm").hide();
	// }
	// });
	$("#usualBtnUpdate").attr("readOnly", true);
	$("#imageQuery").attr("readOnly", true);
	
	$("#usualAcceptForm #applNo").on("keyup",function(event){
		var $applNoInPut = $(this);
		//获取光标所在文本中的下标
		var pos = getTxt1CursorPosition(this);
		if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
			return ;
		}
		// 先把非数字的都替换掉，除了数字和.
		$applNoInPut.val($applNoInPut.val().replace(/[^\w]/g,''));
		//移动光标到所定位置
		setCaret(this,pos);
	});
});
// 查询险种代码
$("#usualAcceptForm #polCode")
		.blur(
				function() {
					var polCode = $("#usualAcceptForm #polCode").val();
					if (polCode === null || "" === polCode) {
						lion.util.info("警告", "险种信息不能为空");
						com.orbps.contractEntry.registrationAcceptance.polName = "";
						$("#polName").val(com.orbps.contractEntry.registrationAcceptance.polName);
						return false;
					}else{
						var responseVo = {};
						responseVo.busiPrdCode = $("#polCode").val();
						lion.util
							.postjson(
									'/orbps/web/orbps/contractEntry/reg/searchBusiName',
									responseVo,
									com.orbps.contractEntry.registrationAcceptance.successQueryBusiName,
									null, null);
					}
				});

com.orbps.contractEntry.registrationAcceptance.successQueryBusiName = function(
		data, arg) {
	$("#polName").val(data);
	com.orbps.contractEntry.registrationAcceptance.polName = data;
	
	
}

//险种名称丢失焦点事件
$("#polName").blur(function(){
	$("#polName").val(com.orbps.contractEntry.registrationAcceptance.polName);
});

// 非空校验
$("#usualAcceptForm #applNo").blur(function() {
	var applNo = $("#usualAcceptForm #applNo").val();
	if (applNo === null || "" === applNo) {
		lion.util.info("警告", "投保单号不能为空");
		return false;
	}
});
$("#usualAcceptForm #entChannelFlag").blur(function() {
	var entChannelFlag = $("#usualAcceptForm #entChannelFlag").val();
	if (entChannelFlag === null || "" === entChannelFlag) {
		lion.util.info("警告", "外包录入标记不能为空");
		return false;
	}
});
$("#usualAcceptForm #agreementFlag").blur(function() {
	var agreementFlag = $("#usualAcceptForm #agreementFlag").val();
	if (agreementFlag === null || "" === agreementFlag) {
		lion.util.info("警告", "共保标识不能为空");
		return false;
	}
});
$("#usualAcceptForm #insuredNum").blur(function() {
	var insuredNum = $("#usualAcceptForm #insuredNum").val();
	if (insuredNum === null || "" === insuredNum) {
		lion.util.info("警告", "被保险人人数不能为空");
		return false;
	}
});
$("#usualAcceptForm #insurType").blur(function() {
	var insurType = $("#usualAcceptForm #insurType").val();
	if (insurType === null || "" === insurType) {
		lion.util.info("警告", "契约形式不能为空");
		return false;
	}
});

$("#usualAcceptForm #hldrName").blur(function() {
	var hldrName = $("#usualAcceptForm #hldrName").val();
	if (hldrName === null || "" === hldrName || " " === hldrName) {
		lion.util.info("警告", "投保/汇交人名称不能为空");
		return false;
	}
	var $hldrNameInput = $(this);
	// 最后一位是小数点的话，移除
	$hldrNameInput.val(($hldrNameInput.val().replace(/\.$/g, "")));
	// 投保人、汇缴人字符间只能有一个空格
	hldrName=hldrName.replace(/^ +| +$/g,'').replace(/ +/g,' ');
	$("#usualAcceptForm #hldrName").val(hldrName);
});

//带有中介机构的情况，查询网点信息，查询销售员信息
$("#workSiteNoAgency,#branchNoAgency").blur(
		function() {
			$("#workSiteNameAgency").val("");
			com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
					.qureyWorksite();
		});

$("#salesCodeAgency,#salesBranchNoAgency").blur(
		function() {
			$("#salesNameAgency").val("");
			com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
					.qureyBranchName();
		});

// 丢失销售员代码查询销售员姓名
$("#salesNo,#salesBranchNo").blur(
		function() {
			$("#salesName").val("");
			com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
					.qureyBranchName();
		});
// 当销售渠道改变时也要查询销售员姓名
//$("#salesListForm #salesChannel").change(
//		function() {
//			if ("OA" !== $("#salesListForm #salesChannel").val()) {
//				$("#salesListForm #salesName").val("");
//				com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
//						.qureyBranchName();
//			}
//		});
// 丢失销售员 销售员代码查询销售姓名的公共方法。
com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.qureyBranchName = function() {
	var grpApplInfoVo = {};
	//根据是否有网点代理信息的标识，设置查询条件
	if("Y" === $("#salesAgencyFlag").val()){
		var salesNo = $("#salesListForm #salesCodeAgency").val();
		var salesBranchNo = $("#salesListForm #salesBranchNoAgency").val();
		var salesChannel = "BS";//有网点代理的销售员，销售渠道只能是BS-业务员
		if ("" === salesNo || "" === salesBranchNo || "" === salesChannel) {
			return false;
		}else{
			grpApplInfoVo.salesBranchNo = salesBranchNo;
			grpApplInfoVo.salesChannel = salesChannel;
			grpApplInfoVo.saleCode = salesNo;
		}
	}else if("N" === $("#salesAgencyFlag").val()){
		var salesNo = $("#salesListForm #salesNo").val();
		var salesBranchNo = $("#salesListForm #salesBranchNo").val();
		var salesChannel = $("#salesListForm #salesChannel").val();
		if ("" === salesNo || "" === salesBranchNo || "" === salesChannel) {
			return false;
		}else{
			grpApplInfoVo.salesBranchNo = salesBranchNo;
			grpApplInfoVo.salesChannel = salesChannel;
			grpApplInfoVo.saleCode = salesNo;
		}
	}
	
	lion.util
			.postjson(
					'/orbps/web/orbps/contractEntry/search/querySaleName',
					grpApplInfoVo,
					com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.successQuerySaleName,
					null, null);
};
// 查询销售员姓名回调函数
com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.successQuerySaleName = function(
		data, arg) {
	if (data === "fail") {
		lion.util.info("销售人员不存在或该销售人员的资格证已经过期，请检查输入的销售人员信息是否正确");
	} else {
		//根据网点代理信息的标识，给页面赋值
		if("Y" === $("#salesAgencyFlag").val()){
			$("#salesListForm #salesNameAgency").val(data);
			$("#salesListForm #salesNameAgency").attr("readOnly", true);
		}else if("N" === $("#salesAgencyFlag").val()){
			$("#salesListForm #salesName").val(data);
			$("#salesListForm #salesName").attr("readOnly", true);
		}
	}
};

//丢失代理员代码查询代理员姓名
$("#salesListForm #agencyNo").blur(
		function() {
			$("#salesListForm #agencyName").val("");
			com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
					.qureyAgencyName();
		});

//查询代理员信息
com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.qureyAgencyName = function() {
	var grpApplInfoVo = {};
	//判断北分代理员的查询条件是否为空
	var workSiteNoAgency = $("#salesListForm #workSiteNoAgency").val();
	var salesNo = $("#salesListForm #agencyNo").val();
	var salesBranchNo = $("#salesListForm #salesBranchNoAgency").val();
	var salesChannel = $("#salesListForm #salesChannelAgency").val();
	if ("" === workSiteNoAgency || "" === salesNo || "" === salesBranchNo || "" === salesChannel) {
		return false;
	}else{
		grpApplInfoVo.salesBranchNo = salesBranchNo;
		grpApplInfoVo.salesChannel = salesChannel;
		grpApplInfoVo.saleCode = salesNo;
		grpApplInfoVo.workSiteNo = workSiteNoAgency;
	}
	
	lion.util
			.postjson(
					'/orbps/web/orbps/contractEntry/search/queryAgencyName',
					grpApplInfoVo,
					com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.successQueryAgencyName,
					null, null);
};
// 查询代理员信息回调函数
com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.successQueryAgencyName = function(
		data, arg) {
	if (data === "fail") {
		lion.util.info("代理人员不存在或该代理人员的资格证已经过期，请检查输入的代理人员信息是否正确");
	} else {
		$("#salesListForm #agencyName").val(data);
		$("#salesListForm #agencyName").attr("readOnly", true);
	}
};

// 丢失代理网点号查询网点名称
$("#worksiteNo,#salesBranchNo").blur(
		function() {
			$("#worksiteName").val("");
			com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
					.qureyWorksite();
		});
// 当销售渠道改变时也要查询网点名称
$("#salesListForm #salesChannel").change(
		function() {
			if ("OA" === $("#salesListForm #salesChannel").val()) {
				$("#worksiteName").val("");
				com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
						.qureyWorksite();
			}
		});
// 丢失焦点查询网点信息的公共方法。
com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.qureyWorksite = function() {
	//根据是否有代理网点的标识，设置网点信息的查询条件
	if("Y" === $("#salesAgencyFlag").val()){
		var worksiteNo = $("#salesListForm #workSiteNoAgency").val();
		var salesBranchNo = $("#salesListForm #branchNoAgency").val();
		var salesChannel = $("#salesListForm #salesChannelAgency").val();
		if ("" === worksiteNo || "" === salesBranchNo || "" === salesChannel) {
			return false;
		}
	}else if("N" === $("#salesAgencyFlag").val()){
		var worksiteNo = $("#salesListForm #worksiteNo").val();
		var salesBranchNo = $("#salesListForm #salesBranchNo").val();
		var salesChannel = $("#salesListForm #salesChannel").val();
		if ("" === worksiteNo || "" === salesBranchNo || "" === salesChannel) {
			return false;
		}
	}
	var regSalesListFormVo = com.orbps.contractEntry.registrationAcceptance.salesListForm
			.serializeObject();
	regSalesListFormVo.salesAgencyFlag = $("#salesAgencyFlag").val();
	lion.util
			.postjson(
					'/orbps/web/orbps/contractEntry/search/queryWorksite',
					regSalesListFormVo,
					com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.successQueryWorksiteName,
					null, null);
};
// 查询网点名称回调函数
com.orbps.contractEntry.registrationAcceptance.usualAcceptForm.successQueryWorksiteName = function(
		data, arg) {
	if (data === "fail") {
		lion.util.info("网点不存在，请确认销售渠道，销售机构，代理网点号是否正确");
	} else {
		//根据是否有代理网点的标识，给网点信息赋值
		if("Y" === $("#salesAgencyFlag").val()){
			$("#salesListForm  #workSiteNameAgency").val(data);
			$("#salesListForm  #workSiteNameAgency").attr("readOnly", true);
		}else if("N" === $("#salesAgencyFlag").val()){
			$("#salesListForm  #worksiteName").val(data);
			$("#salesListForm  #worksiteName").attr("readOnly", true);
		}
	}
};

//共保协议号离开焦点校验
$("#usualAcceptForm #agreementNum")
		.blur(
				function() {
					var agreementNum = $("#usualAcceptForm #agreementNum").val();
					if (agreementNum === null || "" === agreementNum) {
						lion.util.info("警告", "共保协议号不能为空");
						return false;
					}else{
						lion.util
							.postjson(
									'/orbps/web/orbps/public/branchControl/searchAgreement',
									agreementNum,
									com.orbps.contractEntry.registrationAcceptance.successAgreement);
					}
				});
//共保协议号离开焦点回调函数
com.orbps.contractEntry.registrationAcceptance.successAgreement = function(
		data, arg) {
	if(data.retCode==="0"){
		lion.util.info("提示", "失败原因：" + data.errMsg);
		$("#agreementNum").val("");
	}
}
