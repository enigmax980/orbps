com.orbps.contractEntry.grpInsurAppl.proposalInfoForm = $("#proposalInfoForm");
com.orbps.common.list = new Array();
com.orbps.common.allList = new Array();
com.orbps.contractEntry.grpInsurAppl.selectData;
// 新建险种集合
com.orbps.contractEntry.grpInsurAppl.busiPrdCode = [];
com.orbps.common.busiPrdCode;
// 基本信息校验规则
com.orbps.contractEntry.grpInsurAppl.proposalValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		messages : {
			ipsnNum : {
				required : "被保险人总数不能为空"
			},
			insurDur : {
				required : "保险期间不能为空"
			},
			sumPrem : {
				required : "保费合计不能为空",
				isFloatGteZero : "保费合计必须>=0"
			},
			forceNum : {
				required : "生效频率不能为空",
				isIntGteZero : "请输入>=0的整数"
			}
		},
		rules : {
			ipsnNum : {
				required : true
			},
			insurDur : {
				required : true
			},
			sumPrem : {
				required : true,
				isFloatGteZero : true
			},
			forceNum : {
				required : true,
				isIntGteZero : true
			}
		},
		invalidHandler : function(event, validator) {
			Metronic.scrollTo(error2, -200);
		},

		errorPlacement : function(error, element) {
			var icon = $(element).parent('.input-icon').children('i');
			icon.removeClass('fa-check').addClass("fa-warning");
			if (icon.attr('title')
					|| typeof icon.attr('data-original-title') !== 'string') {
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
	com.orbps.contractEntry.grpInsurAppl
			.proposalValidateForm(com.orbps.contractEntry.grpInsurAppl.proposalInfoForm);

	//根据是否指定生效日修改页面样式
	$("#forceType").change(function() {
		var forceType = $("#forceType").val();
		if (forceType === "0") {
			$("#inForceDateIs").hide();
			$("#inForceDate").val("");
			$("#inForceDate").attr("disabled", true);
			$("#inForceDateBtn").attr("disabled", true);
		} else if (forceType === "1") {
			$("#inForceDateIs").show();
			$("#inForceDate").attr("disabled", false);
			$("#inForceDateBtn").attr("disabled", false);
		} else {
			$("#inForceDateIs").hide();
		}
	});

	//根据是否频次生效修改页面样式
	$("#frequenceEff").change(function() {
		if ($("#frequenceEff").val() === "N") {
			$("#forceNum").val("");
			$(".fa").removeClass("fa-warning");
			$(".fa").removeClass("fa-check");
			$(".fa").removeClass("has-success");
			$(".fa").removeClass("has-error");
			$("#forceNum").attr("disabled", true);
		} else if ($("#frequenceEff").val() === "Y") {
			$("#forceNum").attr("disabled", false);
		}
	});

	// 增加表格
	$("#proposalInfoForm #btnAdd").click(function() {
		$("#fbp-editDataGrid").editDatagrids("addRows");
		return false;
	});

	// 删除险种表格
	$("#btnDel")
			.click(
					function() {
						com.orbps.contractEntry.grpInsurAppl.selectData = $("#fbp-editDataGrid").editDatagrids("getSelectRows");
						if (lion.util.isEmpty(com.orbps.contractEntry.grpInsurAppl.selectData)) {
							lion.util.info("警告", "请选择要删除的主险!");
							return false;
						}
						if(com.orbps.common.proposalGroupList.length > 0){
							if(confirm("请先删除被保人分组中删除该险种,再进行删除要约险种删除")){
								
							}else{
								return false;
							}
						}
						if (Object.prototype.toString.call(com.orbps.contractEntry.grpInsurAppl.selectData) === '[object Array]') {
							if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList.length > 0) {
								for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.selectData.length; i++) {
									var busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData[i].busiPrdCode;
									if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList[0].busiPrdCode === busiPrdCode) {
										lion.util.info("提示", "【" + busiPrdCode
												+ "】是契约受理的险种,不能进行删除,请重新选则!");
										return false;
									}

								}
							}
							for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.selectData.length; i++) {
								var busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData[i].busiPrdCode;
								// 删除所有该险种的责任
								for (var x = 0; x < com.orbps.common.list.length; x++) {
									var array_element = com.orbps.common.list[x];
									var productCodes = array_element.productCode;
									var productCodesStart = productCodes
											.split("-")[0];
									if (busiPrdCode === productCodesStart) {
										com.orbps.common.list.splice(x, 1);
										for (var x = 0; x < com.orbps.common.list.length; x++) {
											var array_element = com.orbps.common.list[x];
											var productCodes = array_element.productCode;
											var productCodesStart = productCodes
													.split("-")[0];
											if (busiPrdCode === productCodesStart) {
												com.orbps.common.list.splice(x,
														1);
												for (var x = 0; x < com.orbps.common.list.length; x++) {
													var array_element = com.orbps.common.list[x];
													var productCodes = array_element.productCode;
													var productCodesStart = productCodes
															.split("-")[0];
													if (busiPrdCode === productCodesStart) {
														com.orbps.common.list
																.splice(x, 1);
														for (var x = 0; x < com.orbps.common.list.length; x++) {
															var array_element = com.orbps.common.list[x];
															var productCodes = array_element.productCode;
															var productCodesStart = productCodes
																	.split("-")[0];
															if (busiPrdCode === productCodesStart) {
																com.orbps.common.list
																		.splice(
																				x,
																				1);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						} else {
							if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList.length > 0) {
								var busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode;
								if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList[0].busiPrdCode === busiPrdCode) {
									lion.util.info("提示", "【" + busiPrdCode
											+ "】是契约受理的险种,不能进行删除,请重新选则!");
									return false;
								}
							}
							// 删除所有该险种的责任
							for (var x = 0; x < com.orbps.common.list.length; x++) {
								var array_element = com.orbps.common.list[x];
								var productCodes = array_element.productCode;
								var productCodesStart = productCodes.split("-")[0];
								if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCodesStart) {
									com.orbps.common.list.splice(x, 1);
									for (var x = 0; x < com.orbps.common.list.length; x++) {
										var array_element = com.orbps.common.list[x];
										var productCodes = array_element.productCode;
										var productCodesStart = productCodes
												.split("-")[0];
										if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCodesStart) {
											com.orbps.common.list.splice(x, 1);
											for (var x = 0; x < com.orbps.common.list.length; x++) {
												var array_element = com.orbps.common.list[x];
												var productCodes = array_element.productCode;
												var productCodesStart = productCodes
														.split("-")[0];
												if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCodesStart) {
													com.orbps.common.list
															.splice(x, 1);
													for (var x = 0; x < com.orbps.common.list.length; x++) {
														var array_element = com.orbps.common.list[x];
														var productCodes = array_element.productCode;
														var productCodesStart = productCodes
																.split("-")[0];
														if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCodesStart) {
															com.orbps.common.list
																	.splice(x,
																			1);
														}
													}
												}
											}
										}
									}
								}
							}
						}
						//删除的时候循环对比险种中的险种，是否有健康险或者是建筑险的险种。然后做对应的页面展示。
						$("#fbp-editDataGrid").editDatagrids("delRows");
						var busiPrdVos = $("#fbp-editDataGrid")
								.editDatagrids("getRowsData");
						var hide1 = 0;
						var hide2 = 0;
						outer:
						for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.constructionInfoArray.length; i++) {//公共保额
							var array_element = com.orbps.contractEntry.grpInsurAppl.constructionInfoArray[i];
							inter:
							for (var j = 0; j < busiPrdVos.length; j++) {
								var array_element1 = busiPrdVos[j].busiPrdCode;
								if(array_element===array_element1){
									hide1 = 1;
									break outer;
								}
							}
						}
						outer:
						for (var j = 0; j < com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray.length; j++) {//健康险
							var array_element = com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray[j];
							inter:
							for (var i = 0; i < busiPrdVos.length; i++) {
								var a = busiPrdVos[i].busiPrdCode;
								if (a === array_element){
									hide2 = 1;
									break outer;
								}
							}
						}
						//如果都等于0 说明两个都没有，直接隐藏from
						if(hide2===0 && hide1===0){
							$("#specialInsurAddInfoForm").hide();
						}
						if(hide2 === 0){
							$("#tab1").hide();
						}
						if(hide1 === 0){
							$("#tab3").hide();
						}
						com.orbps.contractEntry.grpInsurAppl.checkFundInsurInfo();
					});

	$("#ipsnNum").blur(function() {
		var ipsnNum = $("#ipsnNum").val();
		if (ipsnNum === null || "" === ipsnNum || "undefined" === ipsnNum) {
			lion.util.info("警告", "被保险人总数为空");
			return false;
		}
	});

	$("#sumPrem").blur(function() {
		var sumPrem = $("#sumPrem").val();
		if (sumPrem === null || "" === sumPrem || "undefined" === sumPrem) {
			lion.util.info("警告", "保费合计不能为空");
			return false;
		}
	});

	$("#forceNum").blur(function() {
		var forceNum = $("#forceNum").val();
		if (forceNum === null || "" === forceNum) {
			lion.util.info("警告", "生效频率不能为空");
			return false;
		}
	});

});
// 丢失焦点事件(根据险种代码查询险种名称)
//在险种中的险种代码中，
$('body')
		.delegate(
				'table#fbp-editDataGrid #busiPrdCode',
				'blur',
				function() {
					var getAddrowsData = $("#fbp-editDataGrid").editDatagrids(
							"getRowsData");
					var busiPrdCodeName = $("#busiPrdCodeName").val();
					var a = $("#busiPrdCode").val();
					if (busiPrdCodeName === "") {
						for (var i = 0; i < getAddrowsData.length; i++) {
							var busiPrdCode = getAddrowsData[i].busiPrdCode;
							if (a === busiPrdCode && a !== "") {
								lion.util.info("提示", "【" + a
										+ "】险种已经存在，请录入其他险种代码");
								$("#busiPrdCode").val("");
								return false;
							}
						}
					}

					if (a !== "") {
						var responseVo = {};
						responseVo.busiPrdCode = a;
						// 向后台发送请求
						lion.util
								.postjson(
										'/orbps/web/orbps/contractEntry/search/searchBusiName',
										responseVo,
										com.orbps.contractEntry.grpInsurAppl.successSearchBusiprodName,
										null, null);
					} else {
						$("#busiPrdCodeName").val("");
					}
					/**
					 * 检查的险种信息，按条件删除option选项。
					 */
					var busiPrdCode = $("#busiPrdCode").val();
					com.orbps.contractEntry.grpInsurAppl.checkOption(busiPrdCode);
				});
//双击保存按钮 ，循环遍历险种中的数据然后做对应的页面展示
$("#fbp-editDataGrid").find("tbody").dblclick(function() {
	//获取到表格中的值，如果是undefined的话说明是在双击保存，而不是双击编辑。
	if($("#busiPrdCode").val() === undefined){
		var getAddrowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		$("#specialInsurAddInfoForm").hide();
		var hide1 = 0;
		var hide2 = 0;
		$("#tab1").hide();//表头
		$("#tab2").hide();
		$("#tab3").hide();
		//循环要约险种的险种。
		for(var i = 0; i < getAddrowsData.length; i++){
			var a = getAddrowsData[i].busiPrdCode;
			//健康险的比对
			for(var j = 0; j < com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray.length; j++){
				var array_element = com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray[j];
				//如果险种中有健康险的险种
				if(a === array_element){
					$("#specialInsurAddInfoForm").show();
//					$('#myTab p[href="#tab_15_1"]').tab('show');
					$("#tab1").show();
					hide2 = 1;
					// 公共保额使用范围
					var comInsurAmntUse = $("#specialInsurAddInfoForm #comInsurAmntUse").val();
					if ("" === comInsurAmntUse) {
						$("#specialInsurAddInfoForm #comInsurAmntUse").combo("val", "0");
					}
					// 公共保费
					var commPremium = $("#specialInsurAddInfoForm #commPremium").val();
					if ("" === commPremium) {
						$("#specialInsurAddInfoForm #commPremium").val(0);
					}
					break;
				}
				
			}
			//建筑险的对比
			for(var k = 0; k < com.orbps.contractEntry.grpInsurAppl.constructionInfoArray.length; k++){
				var array_elements = com.orbps.contractEntry.grpInsurAppl.constructionInfoArray[k];
				if(a === array_elements){
					$("#specialInsurAddInfoForm").show();
//					$('#myTab p[href="#tab_15_3"]').tab('show');
					$("#tab3").show();
					if ("" === $("#specialInsurAddInfoForm #diseaDeathNums").val()) {
						$("#specialInsurAddInfoForm #diseaDeathNums").val(0);
					}
					if ("" === $("#specialInsurAddInfoForm #diseaDisableNums").val()) {
						$("#specialInsurAddInfoForm #diseaDisableNums").val(0);
					}
					if ("" === $("#specialInsurAddInfoForm #acdntDeathNums").val()) {
						$("#specialInsurAddInfoForm #acdntDeathNums").val(0);
					}
					if ("" === $("#specialInsurAddInfoForm #acdntDisableNums").val()) {
						$("#specialInsurAddInfoForm #acdntDisableNums").val(0);
					}
					var enterpriseLicence = $(
							"#specialInsurAddInfoForm #enterpriseLicence").val();
					if (enterpriseLicence === "") {
						// 页面加载企业资质默认为不分等级
						$("#specialInsurAddInfoForm #enterpriseLicence").combo("val",
								"9");
					}
					var awardGrade = $("#specialInsurAddInfoForm #awardGrade").val();
					if (awardGrade === "") {
						// 争议方式默认值位仲裁
						$("#specialInsurAddInfoForm #awardGrade").combo("val", "O");
					}
					var safetyFlag = $("#specialInsurAddInfoForm #safetyFlag").val();
					if (safetyFlag === "") {
						// 是否有安防措施默认值位仲裁
						$("#specialInsurAddInfoForm #safetyFlag").combo("val", "Y");
					}
					var saftyAcdntFlag = $("#specialInsurAddInfoForm #saftyAcdntFlag")
							.val();
					if (saftyAcdntFlag === "") {
						// 是否有安防措施默认值位仲裁
						$("#specialInsurAddInfoForm #saftyAcdntFlag").combo("val", "N");
					}
					hide1 = 1;
					break;
				}
			}
		}
		if(hide2===0&&hide1===0){
			$("#specialInsurAddInfoForm").hide();
		}
		com.orbps.contractEntry.grpInsurAppl.checkFundInsurInfo();
	}
	/**
	 * 检查双击后的险种信息，按条件删除option选项。
	 */
	var busiPrdCode = $("#busiPrdCode").val();
	com.orbps.contractEntry.grpInsurAppl.checkOption(busiPrdCode);
	
	
});
//聚焦险种代码事件
$('body')
		.delegate(
				'table#fbp-editDataGrid #busiPrdCode',
				'focus',
				function() {
					var a = $("#busiPrdCode").val();
					if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList.length > 0) {
						if (com.orbps.contractEntry.grpInsurAppl.queryBusiprodList[0].busiPrdCode === a) {
							lion.util.info("提示", "【" + a
									+ "】是契约受理的险种,不能修改险种代码和名称!");
							$("#busiPrdCode").val(a);
							$("#busiPrdCode").attr("readOnly", true);
							$("#busiPrdCodeName").attr("readOnly", true);
							return false;
						}
					}
					// 获取要约信息下面的所有险种信息
					var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
				    if(getAddRowsData.length === 0){
				    	return false;
				    }else{
				    	//保险期间类型要跟第一条险种中的保持一致
				    	var array_element = getAddRowsData[0].insurDurUnit;
				    	$("#insurDurUnit").combo("val",array_element);
				    	$("#insurDurUnit").attr("readOnly",true);
				    }
				});

//聚焦险种名称代码事件
$('body').delegate('table#fbp-editDataGrid #busiPrdCodeName', 'focus',
		function() {
			$("#busiPrdCodeName").attr("readOnly", true);
		});

// 保额键盘抬起事件
$('body').delegate(
		'table#fbp-editDataGrid #amount',
		'keyup',
		function() {
			if(commevent(this)){
				return;
			}
			if (!isNaN(this.value)) {
				this.value = /^\d+\.?\d{0,2}$/.test(this.value) ? this.value
						: this.value.substring(0, this.value.length - 1)
			} else {
				this.value = ''
			}
			;
			$("#amount").val(this.value);
		});

// 保额丢失焦点事件
$('body').delegate(
		'table#fbp-editDataGrid #amount',
		'blur',
		function() {
			if (!isNaN(this.value)) {
				this.value = /^\d+\.?\d{0,2}$/.test(this.value) ? this.value
						: this.value.substring(0, this.value.length - 1)
			} else {
				this.value = ''
			}
			;
			$("#amount").val(this.value);
		});

// 保费键盘抬起事件
$('body').delegate(
		'table#fbp-editDataGrid #premium',
		'keyup',
		function() {
			if(commevent(this)){
				return;
			}
			if (!isNaN(this.value)) {
				this.value = /^\d+\.?\d{0,2}$/.test(this.value) ? this.value
						: this.value.substring(0, this.value.length - 1)
			} else {
				this.value = ''
			}
			;
			$("#premium").val(this.value);
		});

// 保费丢失焦点事件
$('body').delegate(
		'table#fbp-editDataGrid #premium',
		'blur',
		function() {
			if (!isNaN(this.value)) {
				this.value = /^\d+\.?\d{0,2}$/.test(this.value) ? this.value
						: this.value.substring(0, this.value.length - 1)
			} else {
				this.value = ''
			}
			;
			$("#premium").val(this.value);
		});

// 承保人数键盘抬起事件
$('body').delegate('table#fbp-editDataGrid #insuredNum', 'keyup', function() {
	if (this.value.length == 1) {
		commevent(this,/[^1-9]/g, '');
	} else {
		commevent(this,/\D/g, '');
	}
	//$("#insuredNum").val(this.value);
});

// 承保人数丢失焦点事件
$('body').delegate('table#fbp-editDataGrid #insuredNum', 'blur', function() {
	if (this.value.length == 1) {
		if (this.value < 3) {
			this.value = ''
		}
		this.value = this.value.replace(/[^1-9]/g, '')
	} else {
		this.value = this.value.replace(/\D/g, '')
	}
	;
	$("#insuredNum").val(this.value);
});

// 保险期间键盘抬起事件
$('body').delegate('table#fbp-editDataGrid #insurDur', 'keyup', function() {
	if (this.value.length == 1) {
		if (this.value < 1) {
			this.value = '';
		}
		commevent(this,/[^1-9]/g, '');
	} else {
		commevent(this,/\D/g, '');
	}
});

// 保险期间丢失焦点事件
$('body').delegate('table#fbp-editDataGrid #insurDur', 'blur', function() {
	if (this.value.length == 1) {
		if (this.value < 1) {
			this.value = ''
		}
		this.value = this.value.replace(/[^1-9]/g, '')
	} else {
		this.value = this.value.replace(/\D/g, '')
	}
	;
	$("#insurDur").val(this.value);
});

// 被保险人总数和投保人数保持一致
$("#applNum").blur(function() {
	var applNum = $("#applNum").val();
	$("#ipsnNum").val(applNum);
});

//// 特殊约定字符限制，暂时放开特别约定长度限制
//$("#proposalInfoForm #specialPro").keyup(function(event) {
//	var specialPro = $("#proposalInfoForm #specialPro").val();
//	var len = specialPro.length;
//	var reLen = 0;
//	for (var i = 0; i < len; i++) {
//		if (specialPro.charCodeAt(i) < 27 || specialPro.charCodeAt(i) > 126) {
//			// 全角    
//			reLen += 2;
//		} else {
//			reLen++;
//		}
//		if (reLen > 2000) {
//			specialPro = specialPro.substring(0, i);
//			$("#proposalInfoForm #specialPro").val(specialPro);
//			break;
//		}
//	}
//});

// 保费合计聚焦事件
$("#proposalInfoForm #sumPrem").focus(function(){
	var sum = 0;
	$("#sumPrem").val(sum.toFixed(2));
	var getRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
	for(var index in getRowsData){
		var rowData = getRowsData[index];
		if("premium" in rowData){
			if("" !== rowData["premium"]){
				sum += parseFloat(rowData["premium"]);
			}
		}
	}
	if(0 === sum){
		lion.util.info("请添加险种保费");
	}
	$("#sumPrem").val(sum.toFixed(2));
});

//暂时放开特别约定长度限制
//$("#proposalInfoForm #specialPro").mousedown(function(event) {
//	var specialPro = $("#proposalInfoForm #specialPro").val();
//	var len = specialPro.length;
//	var reLen = 0;
//	for (var i = 0; i < len; i++) {
//		if (specialPro.charCodeAt(i) < 27 || specialPro.charCodeAt(i) > 126) {
//			// 全角    
//			reLen += 2;
//		} else {
//			reLen++;
//		}
//		if (reLen > 2000) {
//			specialPro = specialPro.substring(0, i);
//			$("#proposalInfoForm #specialPro").val(specialPro);
//			break;
//		}
//	}
//});

// 责任信息
$("#btnSelect")
		.click(
				function() {
					// 获取选择的险种信息
					com.orbps.contractEntry.grpInsurAppl.selectData = $(
							"#fbp-editDataGrid").editDatagrids("getSelectRows");
					// 判断选择的是否是一个主险，判断是否添加主险信息
					if (null === com.orbps.contractEntry.grpInsurAppl.selectData) {
						lion.util.info("警告", "请选择主险信息");
						return false;
					} else {
						if ((com.orbps.contractEntry.grpInsurAppl.selectData.length === 0)
								|| (com.orbps.contractEntry.grpInsurAppl.selectData.length > 1)) {
							lion.util.info("警告", "请选择一个主险信息");
							return false;
						}
						if ((null === com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode)) {
							lion.util.info("警告", "请录入险种代码");
							return false;
						}
					}
					com.orbps.common.busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode;
					// 清空modal
					com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
					// 加载责任modal
					com.orbps.contractEntry.grpInsurAppl.addDialog
							.load(
									"/orbps/orbps/public/modal/html/insurRespModal.html",
									function() {
										// 初始化插件
										$(this).modal('toggle');
										// 初始化editTable组件
										$("#coverageInfo_list")
												.editDatagridsLoadById();
										if (com.orbps.contractEntry.grpInsurAppl.busiPrdCode.length > 0) {
											for (var j = 0; j < com.orbps.contractEntry.grpInsurAppl.busiPrdCode.length; j++) {
												var array_element = com.orbps.contractEntry.grpInsurAppl.busiPrdCode[j];
												if (array_element === com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode) {
													var responseVo = {};
													responseVo.busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode;
													lion.util
															.postjson(
																	'/orbps/web/orbps/contractEntry/grp/search',
																	responseVo,
																	com.orbps.contractEntry.grpInsurAppl.successbusiPrdCode);
													// 延时回显
													setTimeout(
															function() {
																for (var k = 0; k < com.orbps.common.allList.length; k++) {
																	var array_element = com.orbps.common.allList[k].productCode;
																	for (var m = 0; m < com.orbps.common.list.length; m++) {
																		var productCode = com.orbps.common.list[m].productCode;
																		if (array_element === productCode) {
																			com.orbps.common.allList
																					.splice(
																							k,
																							1,
																							com.orbps.common.list[m]);//替换方法
																		}
																	}
																}
																// 回显list
																$(
																		'#coverageInfo_list')
																		.editDatagrids(
																				"bodyInit",
																				com.orbps.common.allList);
																// 循环已经保存的责任信息
																if ((com.orbps.common.list).length > 0) {
																	for (var i = 0; i < com.orbps.common.list.length; i++) {
																		var array_element = com.orbps.common.list[i];
																		// 判断选择的责任信息是一条还是多条
																		if (array_element.length >= 0) {
																			var productCode = array_element[0].productCode;
																			if (productCode
																					.indexOf("-") > 0) {
																				var result = productCode
																						.substr(
																								0,
																								productCode
																										.indexOf('-'));
																				if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === result) {
																					// 回显责任信息
																					$(
																							"#coverageInfo_list")
																							.editDatagrids(
																									"selectRows",
																									array_element);
																				}
																			} else {
																				if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCode) {
																					// 回显责任信息
																					$(
																							"#coverageInfo_list")
																							.editDatagrids(
																									"selectRows",
																									array_element);
																				}
																			}
																		} else {
																			var productCode = array_element.productCode;
																			if (productCode
																					.indexOf("-") > 0) {
																				var result = productCode
																						.substr(
																								0,
																								productCode
																										.indexOf('-'));
																				if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === result) {
																					$(
																							"#coverageInfo_list")
																							.editDatagrids(
																									"selectRows",
																									array_element);
																				}
																			} else {
																				if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCode) {
																					$(
																							"#coverageInfo_list")
																							.editDatagrids(
																									"selectRows",
																									array_element);
																				}
																			}
																		}
																	}
																}
															}, 2000);
													return false;
												} else {
													if ((j + 1) === com.orbps.contractEntry.grpInsurAppl.busiPrdCode.length) {
														var responseVo = {};
														responseVo.busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode;
														// 将险种放到集合中
														com.orbps.contractEntry.grpInsurAppl.busiPrdCode
																.push(com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode);
														lion.util
																.postjson(
																		'/orbps/web/orbps/contractEntry/grp/search',
																		responseVo,
																		com.orbps.contractEntry.grpInsurAppl.successbusiPrdCode);
													}
												}
											}
										} else {
											var responseVo = {};
											responseVo.busiPrdCode = com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode;
											// 将险种放到集合中
											com.orbps.contractEntry.grpInsurAppl.busiPrdCode
													.push(com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode);
											lion.util
													.postjson(
															'/orbps/web/orbps/contractEntry/grp/search',
															responseVo,
															com.orbps.contractEntry.grpInsurAppl.successbusiPrdCode);
											// 延时回显
											setTimeout(
													function() {
														for (var k = 0; k < com.orbps.common.allList.length; k++) {
															var array_element = com.orbps.common.allList[k].productCode;
															for (var m = 0; m < com.orbps.common.list.length; m++) {
																var productCode = com.orbps.common.list[m].productCode;
																if (array_element === productCode) {
																	com.orbps.common.allList
																			.splice(
																					k,
																					1,
																					com.orbps.common.list[m]);//替换方法
																}
															}
														}
														// 回显list
														$('#coverageInfo_list')
																.editDatagrids(
																		"bodyInit",
																		com.orbps.common.allList);
														// 循环已经保存的责任信息
														if ((com.orbps.common.list).length > 0) {
															for (var i = 0; i < com.orbps.common.list.length; i++) {
																var array_element = com.orbps.common.list[i];
																// 判断选择的责任信息是一条还是多条
																if (array_element.length >= 0) {
																	var productCode = array_element[0].productCode;
																	if (productCode
																			.indexOf("-") > 0) {
																		var result = productCode
																				.substr(
																						0,
																						productCode
																								.indexOf('-'));
																		if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === result) {
																			// 回显责任信息
																			$(
																					"#coverageInfo_list")
																					.editDatagrids(
																							"selectRows",
																							array_element);
																		}
																	} else {
																		if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCode) {
																			// 回显责任信息
																			$(
																					"#coverageInfo_list")
																					.editDatagrids(
																							"selectRows",
																							array_element);
																		}
																	}
																} else {
																	var productCode = array_element.productCode;
																	if (productCode
																			.indexOf("-") > 0) {
																		var result = productCode
																				.substr(
																						0,
																						productCode
																								.indexOf('-'));
																		if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === result) {
																			$(
																					"#coverageInfo_list")
																					.editDatagrids(
																							"selectRows",
																							array_element);
																		}
																	} else {
																		if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === productCode) {
																			$(
																					"#coverageInfo_list")
																					.editDatagrids(
																							"selectRows",
																							array_element);
																		}
																	}
																}
															}
														}
													}, 2000);
											return false;
										}
									});
				});

//特殊险种默认展示信息
function showSpecialInsurAddInfo() {
	$("#specialInsurAddInfoForm").show();
	if ("" === $("#specialInsurAddInfoForm #diseaDeathNums").val()) {
		$("#specialInsurAddInfoForm #diseaDeathNums").val(0);
	}
	if ("" === $("#specialInsurAddInfoForm #diseaDisableNums").val()) {
		$("#specialInsurAddInfoForm #diseaDisableNums").val(0);
	}
	if ("" === $("#specialInsurAddInfoForm #acdntDeathNums").val()) {
		$("#specialInsurAddInfoForm #acdntDeathNums").val(0);
	}
	if ("" === $("#specialInsurAddInfoForm #acdntDisableNums").val()) {
		$("#specialInsurAddInfoForm #acdntDisableNums").val(0);
	}
	var enterpriseLicence = $("#specialInsurAddInfoForm #enterpriseLicence")
			.val();
	if (enterpriseLicence === "") {
		//页面加载企业资质默认为不分等级
		$("#specialInsurAddInfoForm #enterpriseLicence").combo("val", "9");
	}
	var awardGrade = $("#specialInsurAddInfoForm #awardGrade").val();
	if (awardGrade === "") {
		// 争议方式默认值位仲裁
		$("#specialInsurAddInfoForm #awardGrade").combo("val", "O");
	}
	var safetyFlag = $("#specialInsurAddInfoForm #safetyFlag").val();
	if (safetyFlag === "") {
		// 是否有安防措施默认值位仲裁
		$("#specialInsurAddInfoForm #safetyFlag").combo("val", "Y");
	}
	var saftyAcdntFlag = $("#specialInsurAddInfoForm #saftyAcdntFlag").val();
	if (saftyAcdntFlag === "") {
		// 是否有安防措施默认值位仲裁
		$("#specialInsurAddInfoForm #saftyAcdntFlag").combo("val", "N");
	}
}

// 成功查询险种名称回调函数
com.orbps.contractEntry.grpInsurAppl.successSearchBusiprodName = function(data,
		arg) {
	if ("fail" == data) {
		lion.util.info("", "险种不存在，请重新输入险种代码");
		$("#busiPrdCodeName").val("");
		return false;
	}
	$("#busiPrdCodeName").val(data);
	$("#busiPrdCodeName").attr("readOnly", true);

}

//查询责任成功回调函数
com.orbps.contractEntry.grpInsurAppl.successbusiPrdCode = function(data, arg) {
	if (data.length === 1) {
		lion.util.info("提示", "该险种无责任!");
		com.orbps.common.allList = '';
		$('#coverageInfo_list').editDatagrids("bodyInit",
				com.orbps.common.allList);
		return false;
	} else {
		if (data[0].productCode === com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode) {
			lion.util.info("提示", "该险种无责任!");
			com.orbps.common.allList = '';
			$('#coverageInfo_list').editDatagrids("bodyInit",
					com.orbps.common.allList);
			return false;
		}
		$('#coverageInfo_list').editDatagrids("bodyInit", data);
		com.orbps.common.allList = data;
	}
}

//回显保额保费
com.orbps.common.reloadPre = function(productNum, productPremium) {
	var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
	for (var i = 0; i < getAddRowsData.length; i++) {
		var busiPrdCode = getAddRowsData[i].busiPrdCode;
		if (com.orbps.contractEntry.grpInsurAppl.selectData.busiPrdCode === busiPrdCode) {
			com.orbps.contractEntry.grpInsurAppl.selectData.amount = productNum;
			com.orbps.contractEntry.grpInsurAppl.selectData.premium = productPremium;
			getAddRowsData.splice(i, 1,
					com.orbps.contractEntry.grpInsurAppl.selectData);
			break;
		}
	}
	$('#fbp-editDataGrid').editDatagrids("bodyInit", getAddRowsData);
}
/**
 * 检查险种信息，按条件删除option选项。
 */
com.orbps.contractEntry.grpInsurAppl.checkOption = function(busiPrdCode){
	if(busiPrdCode !== undefined && busiPrdCode !== ""){
		lion.util.postjson("/orbps/web/orbps/contractEntry/read/queryHealthInsurFlag",busiPrdCode,function(data){
//			alert("双击 || 离开焦点 "+data);
			// 获取险种id
			var healthInsurFlag = document.getElementById("healthInsurFlag");
			//后台数据如果返回是字符串2，说明该险种的健康险专项标识是不用删除的。
			if(data === "2" ){
				//如果是少option的险种换成多的险种了，下拉框要重新加载一下，以免值不全
				$("#healthInsurFlag").combo("refresh"); 
				return false;
			}
			//如果返回的是空字符串，说明该险种没有健康险专项标识。清空option
			if(data === ""){
				$("#healthInsurFlag").empty(); 
				return false;
			}
			//如果返回的有数据，把返回的字符串解析成string数组，然后跟当前险种对比
			var string = data.split(",")
			for (var i = 0; i < string.length; i++) {
				if(busiPrdCode === string[i]){
					//判断select框的个数，如果小于1，说明option中没有值，需要从新加载select框
					if($("#healthInsurFlag option").size() < 1){
						$("#healthInsurFlag").combo("refresh"); 
						//加超时因为重新加载select框需要一定的时间，不然，删除option的时候就会有问题。
						setTimeout(function(){
							$("#healthInsurFlag option[value='A1']").remove(); //删除Select中Value='A1'的Option 
							$("#healthInsurFlag option[value='A3']").remove(); //删除Select中Value='A3'的Option 
							$("#healthInsurFlag option[value='A5']").remove(); //删除Select中Value='A5'的Option 
							$("#healthInsurFlag option[value='A9']").remove(); //删除Select中Value='A9'的Option 
						},2000);
					}else{
						$("#healthInsurFlag").combo("clear");
						$("#healthInsurFlag option[value='A1']").remove(); //删除Select中Value='A1'的Option 
						$("#healthInsurFlag option[value='A3']").remove(); //删除Select中Value='A3'的Option 
						$("#healthInsurFlag option[value='A5']").remove(); //删除Select中Value='A5'的Option 
						$("#healthInsurFlag option[value='A9']").remove(); //删除Select中Value='A9'的Option 
					}
					break;
				}
			}
		});
	}
}
com.orbps.contractEntry.grpInsurAppl.checkFundInsurInfo = function(){
	//如果基金险标示已经有了，就不用再去检查是否有基金险
		var getAddRowsData = $("#fbp-editDataGrid").editDatagrids("getRowsData");
		var busiPrdCode = "";
		for (var i = 0; i < getAddRowsData.length; i++) {
			if(busiPrdCode === ""){
				busiPrdCode = getAddRowsData[i].busiPrdCode;
			}else{
				busiPrdCode = busiPrdCode + "," + getAddRowsData[i].busiPrdCode;
			}
			
		}
		if(busiPrdCode === "" || busiPrdCode === undefined){
			return false;
		}
		//查询险种列表中是否含有基金险，然后做对应的页面展示
		lion.util.postjson("/orbps/web/orbps/contractEntry/read/queryFundInsurInfo",busiPrdCode,function(data){
			if(data === "1"){
				//有基金险
				$("#specialInsurAddInfoForm").show();
				$("#tab2").show();
				com.orbps.contractEntry.grpInsurAppl.FundInsurFlag = "Y";
			}else{
				$("#tab2").hide();
				com.orbps.contractEntry.grpInsurAppl.FundInsurFlag = "N";
			}
			com.orbps.contractEntry.grpInsurAppl.checkTabshowOrHidden();
		})

}
/***
 * 查询tab中的基金险，健康险，建工险的隐藏或者展示
 */
com.orbps.contractEntry.grpInsurAppl.checkTabshowOrHidden = function (){
	//各个tab的是否隐藏，是隐藏 true 不是隐藏 false
	var tab1 = $('#myTab p[href="#tab_15_1"]').is(':hidden');
	var tab2 = $('#myTab p[href="#tab_15_2"]').is(':hidden');
	var tab3 = $('#myTab p[href="#tab_15_3"]').is(':hidden');
	//alert(tab1+"   " +tab2 +"    " +tab3);
	//如果三个都是隐藏的，隐藏div
	if(tab1 && tab2 && tab3){
		$("#specialInsurAddInfoForm").hide();
	}
	//如果tab3不是隐藏的，各个情况都展示tab3
	if((tab1 && tab2 && !tab3) || (tab1 && !tab2 && !tab3) || (!tab1 && !tab2 && !tab3) || (!tab1 && tab2 && !tab3)){
		$('#myTab p[href="#tab_15_3"]').tab('show');
	}
	//如果tab2不是隐藏的，各种情况都展示tab2
	if((tab1 && !tab2 && tab3) || (!tab1 && !tab2 && tab3)){
		$('#myTab p[href="#tab_15_2"]').tab('show');
	}
	//如果tab2 tab3 都是隐藏的，tabl不是隐藏的展示tab1
	if(!tab1 && tab2 && tab3){
		$('#myTab p[href="#tab_15_1"]').tab('show');
	}
}
