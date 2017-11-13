com.orbps.contractReview.sgGrpInsurAppl.addDialog = $('#btnModel');
com.orbps.contractReview.sgGrpInsurAppl.benesList = [];
com.orbps.contractReview.sgGrpInsurAppl.insuredList = new Array();
com.orbps.contractReview.sgGrpInsurAppl.oranLevelList = new Array();

com.orbps.contractReview.sgGrpInsurAppl.settleList = new Array();
com.common.oranLevelList = new Array();
com.common.insuredList = new Array();
com.common.insuredType = -1;
com.common.oranLevelCount = 0;
com.common.oranLevelList = new Array();
com.common.oranLevelType = -1;
com.orbps.common.getAddRowsData;
com.common.zTrees = [];
com.common.chargePayList = new Array();
com.orbps.common.proposalGroupList = new Array();
com.orbps.common.insuranceList = new Array();
/** 节点名称存储集合 */
com.orbps.common.treeNodeNameList = [];
com.orbps.common.applNo;
com.orbps.common.quotaEaNo;
com.orbps.common.salesBranchNo;
com.orbps.common.salesChannel;
com.orbps.common.worksiteNo;
com.orbps.common.saleCode;
$(function() {
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});

	// 表单提交
	$("#btnSubmit")
			.click(
					function() {
						// jquery validate 校验
						if (com.orbps.contractReview.sgGrpInsurAppl.applInfoForm
								.validate().form()
								&& com.orbps.contractReview.sgGrpInsurAppl.payInfoForm
										.validate().form()
								&& com.orbps.contractReview.sgGrpInsurAppl.printInfoForm
										.validate().form()
								&& com.orbps.contractReview.sgGrpInsurAppl.proposalInfoForm
										.validate().form()) {
							// 如果选择多期暂缴保费来源必须是团体 交费方式必须是年 和 趸。 Y W
							if ("Y" === $("#multiRevFlag").val()) {
								var premFrom = $("#premFrom").val();
								var moneyItrvl = $("#moneyItrvl").val()
								if ("W" !== moneyItrvl && "Y" !== moneyItrvl) {
									lion.util.info("提示",
											"当选择多期暂交时，交费方式必须是年或者趸！");
									return false;
								}
								if ("1" !== premFrom) {
									lion.util
											.info("提示", "当选择多期暂交时，保费来源必须是团体缴费");
									return false;
								}
							}
							var listType = $("#listType").val();
							if ("G" === listType) {
								// 当汇交人类型是团体汇交的时候，合同打印为电子投保单时，联系人汇交邮箱必填。
//								if ("0" === $("#underNoticeType").val()
//										|| "0" === $("#listPrint").val()) {
//									if ("" === $("#contactEmail").val()) {
//										lion.util.info("警告",
//												"合同打印方式或清单打印为电子保单，请输入联系人邮箱!");
//										return false;
//									}
//
//								}
								// 增加团体汇交联系人移动电话和固定电话同时为空的判断
								var contactMobile = $("#contactMobile").val();
								//var contactTel = $("#contactTel").val();
								if (contactMobile === "") {
									lion.util.info("警告", "联系人移动电话不能为空");
									return false;
								}
								com.orbps.contractReview.sgGrpInsurAppl.grpInsurInfoForm
										.validate().form();
								//判断输入的邮编和所选的地区是否一致
								var zipCode = $("#zipCode").val();
								var city = com.orbps.publicSearch.checkPostCode(zipCode);
								var province = $("#provinceCode").val();
								province = province.substr(0,2);
								if(city !== province){
									lion.util.info("提示","邮编输入不正确，请选择[ "+province+" ]的对应正确邮编！");
									return false;
								}
							} else if ("P" === listType) {
								var contactMobile = $("#joinMobile").val();
								if (contactMobile === "") {
									lion.util.info("警告", "汇交人移动电话不能为空");
									return false;
								}
								com.orbps.contractReview.sgGrpInsurAppl.personalInsurInfoForm
										.validate().form();
								// 当汇交人类型是个人汇交的时候，合同打印为电子投保单时，个人汇交邮箱必填。
								var postCode = $("#postCode").val();
								var city = com.orbps.publicSearch.checkPostCode(postCode);
								var province = $("#province").val();
								province = province.substr(0,2);
								if(city !== province){
									lion.util.info("提示","邮编输入不正确，请选择[ "+province+" ]的对应正确邮编！");
									return false;
								}
							}
							// 如果证件号码不是身份证时，校验出生日期是否大于18岁
							var appIdTypeValue = $("#joinIdType").val()
							var joinBirthDate = $(
									"#personalInsurInfoForm #joinBirthDate")
									.val();
							joinBirthDate = joinBirthDate.substring(0, 4)
							var date = new Date();
							var year = date.getFullYear();
							if ((year - joinBirthDate) < 18) {
								lion.util.info("提示", "请输入大于18岁的汇交人出生日期");
								return false;
							}
							// select校验
							if (com.orbps.contractReview.sgGrpInsurAppl
									.validateSelectVal()) {
								// // 获取要约信息下面的所有险种信息
								var getAddRowsData = $("#bsInfoListTb")
										.editDatagrids("getRowsData");
								// 提交方法
								var sgGrpInsurApplVo = {};
								// 根据汇交人类型序列化vo对象
								if ("G" === listType) {
									if(com.orbps.contractReview.sgGrpInsurAppl.grpInsurInfoForm.validate().form()){
										// 团单
										sgGrpInsurApplVo.sgGrpInsurInfoVo = com.orbps.contractReview.sgGrpInsurAppl.grpInsurInfoForm
												.serializeObject();
			                    	}else{
			                    		lion.util.info("汇交人信息输入不正确");
			                    		return false;
			                    	}
								} else if ("P" === listType) {
									// 个人
									// 放开汇交人出生日期，因为序列化的时候可能获取不到日期
									$("#personalInsurInfoForm #joinBirthDate")
											.attr("disabled", false);
									if(com.orbps.contractReview.sgGrpInsurAppl.personalInsurInfoForm.validate().form()){
										sgGrpInsurApplVo.sgGrpPersonalInsurInfoVo = com.orbps.contractReview.sgGrpInsurAppl.personalInsurInfoForm
										.serializeObject();
					               	}else{
			                    		lion.util.info("汇交人信息输入不正确");
			                    		return false;
			                    	}
								}
								//判断被保人分组信息是否正确
								if(com.orbps.common.proposalGroupList.length>0){
									if("" === $("#printInfoForm #groupType").val()){
										lion.util.info("有被保人分组时，分组类型不能为空");
										return false;
									}
								}else{
									if("" !== $("#printInfoForm #groupType").val()){
										lion.util.info("没有被保人分组时，分组类型必须为空");
										return false;
									}
								}
								var joinIdType = $(
										"#personalInsurInfoForm #joinIdType")
										.val();
								sgGrpInsurApplVo.sgGrpApplInfoVo = com.orbps.contractReview.sgGrpInsurAppl.applInfoForm
										.serializeObject();
								sgGrpInsurApplVo.sgGrpPayInfoVo = com.orbps.contractReview.sgGrpInsurAppl.payInfoForm
										.serializeObject();
								sgGrpInsurApplVo.sgGrpPrintInfoVo = com.orbps.contractReview.sgGrpInsurAppl.printInfoForm
										.serializeObject();
								sgGrpInsurApplVo.sgGrpProposalInfoVo = com.orbps.contractReview.sgGrpInsurAppl.proposalInfoForm
										.serializeObject();
								sgGrpInsurApplVo.sgGrpVatInfoVo = com.orbps.contractReview.sgGrpInsurAppl.vAddTaxInfoForm
										.serializeObject();
								sgGrpInsurApplVo.insuredGroupModalVos = com.orbps.common.proposalGroupList;
								// sgGrpInsurApplVo.organizaHierarModalVos =
								// com.orbps.contractReview.sgGrpInsurAppl.oranLevelList;
								sgGrpInsurApplVo.addinsuranceVos = com.orbps.contractReview.sgGrpInsurAppl.addinsuranceVos;
								sgGrpInsurApplVo.chargePayGroupModalVos = com.common.chargePayList;
								sgGrpInsurApplVo.listType = $("#listType")
										.val();
								sgGrpInsurApplVo.taskInfo = {};
								sgGrpInsurApplVo.taskInfo.taskId = com.orbps.contractReview.sgGrpInsurAppl.taskId;
								if ($("#reviewFlag").val() === "Y") {
									sgGrpInsurApplVo.approvalState = "25";// 复核通过
								} else if ($("#reviewFlag").val() === "N") {
									sgGrpInsurApplVo.approvalState = "26";// 复核不通过
								} else {
									lion.util.info("警告", "请选择复核是否通过！");
									return false;
								}
								// alert("复核通过？"+sgGrpInsurApplVo.approvalState);
								// alert("复合的数据"+JSON.stringify(sgGrpInsurApplVo));
								lion.util
										.postjson(
												'/orbps/web/orbps/contractReview/sg/submit',
												sgGrpInsurApplVo,
												com.orbps.contractReview.sgGrpInsurAppl.successSubmit,
												null, null);
							}
						}
					});
});
// 通过投保单号查询险种信息
$("#btnQuery")
		.click(
				function() {
					// 取出投保单号
					var applNo = $("#applInfoForm #applNo").val();
					// 校验投保单号
					if (applNo !== null || "".equals(applNo)
							|| applNo === "undefined") {
						var insurApplVo = {};
						insurApplVo.applInfoVo = com.orbps.contractReview.sgGrpInsurAppl.applInfoForm
								.serializeObject();
						lion.util
								.postjson(
										'/orbps/web/orbps/contractEntry/grp/query',
										insurApplVo,
										successQuery,
										null,
										com.orbps.contractReview.sgGrpInsurAppl.applInfoForm);
					} else {
						lion.util.info('提示', '请输入正确投保单号');
					}
				});

// 被保人分组
$("#btnGoup").click(
		function() {
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.empty();
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.load(
					"/orbps/orbps/public/modal/html/insuredGroupModal.html",
					function() {
						$(this).modal('toggle');
						$(this).comboInitLoad();
						$(this).combotreeInitLoad();
						// 刷新table
					});
			setTimeout(function() {
				com.orbps.common.getAddRowsData = $("#bsInfoListTb")
						.editDatagrids("getRowsData");
				// 回显
				com.orbps.common.reloadProposalGroupModalTable();
			}, 100);
		});

// 组织层次架构
$("#btnOranLevel").click(
		function() {
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.empty();
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.load(
					"/orbps/orbps/public/modal/html/organizaHierarModal.html",
					function() {
						$(this).modal('toggle');
						// combo组件初始化
						$(this).comboInitLoad();
					});
		});

// 校验选择信息
com.orbps.contractReview.sgGrpInsurAppl.validateSelectVal = function() {

	var applDate = $("#applInfoForm #applDate").val();
	if (applDate === null || "" === applDate) {
		lion.util.info("警告", "请选择投保申请日期");
		return false;
	}

	var listType = $("#listType").val();
	if (listType == "G") {

		var idType = $("#grpInsurInfoForm #idType").val();
		if (idType === null || "" === idType) {
			lion.util.info("警告", "请选择证件类型");
			return false;
		}

		var companyRegist = $("#grpInsurInfoForm #companyRegist").val();
		if (companyRegist === null || "" === companyRegist) {
			lion.util.info("警告", "请选择企业注册地");
			return false;
		}

		var departmentType = $("#grpInsurInfoForm #departmentType").val();
		if (departmentType === null || "" === departmentType) {
			lion.util.info("警告", "请选择部门类型");
			return false;
		}

		var occClassCode = $("#grpInsurInfoForm #occClassCode").val();
		if (occClassCode === null || "" === occClassCode) {
			lion.util.info("警告", "请选择职业类别");
			return false;
		}

		var provinceCode = $("#grpInsurInfoForm #provinceCode").val();
		if (provinceCode === null || "" === provinceCode) {
			lion.util.info("警告", "请选择省/直辖市");
			return false;
		}

		var cityCode = $("#grpInsurInfoForm #cityCode").val();
		if (cityCode === null || "" === cityCode) {
			lion.util.info("警告", "请选择市/城区 ");
			return false;
		}

		var countyCode = $("#grpInsurInfoForm #countyCode").val();
		if (countyCode === null || "" === countyCode) {
			lion.util.info("警告", "请选择县/地级市");
			return false;
		}

		var contactIdTypede = $("#grpInsurInfoForm #contactIdType").val();
		if (contactIdType === null || "" === contactIdType) {
			lion.util.info("警告", "请选择联系人证件类型");
			return false;
		}

		var gSettleDispute = $("#grpInsurInfoForm #gSettleDispute").val();
		if (gSettleDispute === null || "" === gSettleDispute) {
			lion.util.info("警告", "请选择争议处理方式");
			return false;
		}
		// 仲裁机构名称
		if (gSettleDispute === "2") {
			var parbOrgName = $("#grpInsurInfoForm #parbOrgName").val();
			if (parbOrgName === "") {
				lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
				return false;
			}
		}
	} else {

		var joinIdType = $("#personalInsurInfoForm #joinIdType").val();
		if (joinIdType === null || "" === joinIdType) {
			lion.util.info("警告", "请选择汇交人证件类型");
			return false;
		}
		var joinSex = $("#personalInsurInfoForm #joinSex").val();
		if (joinSex === null || "" === joinSex) {
			lion.util.info("警告", "请选择汇交人性别");
			return false;
		}
		var joinBirthDate = $("#personalInsurInfoForm #joinBirthDate").val();
		if (joinBirthDate === null || "" === joinBirthDate) {
			lion.util.info("警告", "请选择汇交人出生日期");
			return false;
		}
	    var provinceCode = $("#personalInsurInfoForm #province").val();
	    if (provinceCode === null || "" === provinceCode) {
	        lion.util.info("警告", "请选择省/直辖市");
	        return false;
	    }
	    
	    var city = $("#personalInsurInfoForm #city").val();
	    if (city === null || "" === city) {
	        lion.util.info("警告", "请选择市/城区 ");
	        return false;
	    }
	    
	    var county = $("#personalInsurInfoForm #county").val();
	    if (county === null || "" === county) {
	        lion.util.info("警告", "请选择县/地级市");
	        return false;
	    }
		var pSettleDispute = $("#personalInsurInfoForm #pSettleDispute").val();
	    if (pSettleDispute === null || "" === pSettleDispute) {
	        lion.util.info("警告", "请选择争议处理方式");
	        return false;
	    }
	    // 仲裁机构名称
		if (pSettleDispute === "2") {
			var joinParbOrgName = $("#personalInsurInfoForm #joinParbOrgName").val();
			if (joinParbOrgName === "") {
				lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
				return false;
			}
		}
	}
	// 如果不是现金提示以下信息
	if ($("#moneyinType").val() !== "C" && $("#moneyinType").val() !== "R" && $("#moneyinType").val() !== "P") {
		// 不用校验银行账号信息
		if ($("#premFrom").val() === "1" || $("#premFrom").val() === "3") {
			var bankCode = $("#bankCode").val();
			if (bankCode === null || "" === bankCode) {
				lion.util.info("警告", "交费开户行不能为空");
				return false;
			}
			var bankaccNo = $("#bankaccNo").val();
			if (bankaccNo === null || "" === bankaccNo) {
				lion.util.info("警告", "银行账号不能为空");
				return false;
			}
			var bankBranchName = $("#bankBranchName").val();
			if (bankBranchName === null || "" === bankBranchName) {
				lion.util.info("警告", "开户行名不能为空");
				return false;
			}
		}
	}

	var insurDurUnit = $("#proposalInfoForm #insurDurUnit").val();
	if (insurDurUnit === null || "" === insurDurUnit) {
		lion.util.info("警告", "请选择保险期间单位");
		return false;
	}

	var effectType = $("#proposalInfoForm #effectType").val();
	if (effectType === null || "" === effectType) {
		lion.util.info("警告", "请选择生效方式");
		return false;
	} else {
		if (effectType === 1) {
			var speEffectDate = $("#proposalInfoForm #speEffectDate").val();
			if (speEffectDate === null || "" === speEffectDate) {
				lion.util.info("警告", "请选择指定生效日");
				return false;
			}
		}
	}

	var firstChargeDate = $("#proposalInfoForm #firstChargeDate").val();
	if (firstChargeDate === null || "" === firstChargeDate) {
		lion.util.info("警告", "请选择首期扣款截止日期");
		return false;
	}

	var moneyItrvl = $("#payInfoForm #moneyItrvl").val();
	if (moneyItrvl === null || "" === moneyItrvl) {
		lion.util.info("警告", "交费方式不能为空");
		return false;
	}

	// var moneyinType = $("#payInfoForm #moneyinType").val();
	// if (moneyinType === null || "" === moneyinType) {
	// lion.util.info("警告", "请选择交费形式");
	// return false;
	// }

	var premFrom = $("#payInfoForm #premFrom").val();
	if (premFrom === null || "" === premFrom) {
		lion.util.info("警告", "请选择保费来源");
		return false;
	}

	// var renewalChargeFlag = $("#payInfoForm #renewalChargeFlag").val();
	// if (renewalChargeFlag === null || "" === renewalChargeFlag) {
	// lion.util.info("警告", "请选择是否续期扣款");
	// return false;
	// }else{
	// if(renewalChargeFlag==='Y'){
	// var chargeDeadline = $("#payInfoForm #chargeDeadline").val();
	// if (chargeDeadline === null || "" === chargeDeadline) {
	// lion.util.info("警告", "请选择扣款截止日期");
	// return false;
	// }
	// }
	// }

	var multiRevFlag = $("#payInfoForm #multiRevFlag").val();
	if (multiRevFlag === null || "" === multiRevFlag) {
		lion.util.info("警告", "请选择是否多期暂交");
		return false;
	} else {
		if (multiRevFlag === 'Y') {
			var multiTempYear = $("#payInfoForm #multiTempYear").val();
			if (multiTempYear === null || "" === multiTempYear) {
				lion.util.info("警告", "请输入多期暂交年数");
				return false;
			}
		}
	}
	var underNoticeType = $("#printInfoForm #underNoticeType").val();
	if (underNoticeType === null || "" === underNoticeType) {
		lion.util.info("警告", "请选择合同打印方式");
		return false;
	}
	var listFlag = $("#printInfoForm #listFlag").val();
	if (listFlag === null || "" === listFlag) {
		lion.util.info("警告", "请选择清单标记");
		return false;
	}
	//判断清汇页面的清单标记 只能选择 普通清单和档案清单。
	if($("#listFlag").val()==="N" || $("#listFlag").val() === "M"){
		lion.util.info("警告","该保单不支持无清单和事后补录清单，请检查！");
		return false;
	}
	// 清单标记为“无清单”时，清单打印 &个人凭证打印不可用 ，不等于的时候再去校验必录校验
	if (listFlag !== "N") {
		var applProperty = $("#printInfoForm #listPrint").val();
		if (applProperty === null || "" === applProperty) {
			lion.util.info("警告", "请选择清单打印");
			return false;
		}
		var personalIdPrint = $("#printInfoForm #personalIdPrint").val();
		if (personalIdPrint === null || "" === personalIdPrint) {
			lion.util.info("警告", "请选择个人凭证打印");
			return false;
		}
	}

	var giftInsFlag = $("#printInfoForm #giftInsFlag").val();
	if (giftInsFlag === null || "" === giftInsFlag) {
		lion.util.info("警告", "请选择赠送险标记");
		return false;
	}

	var polProperty = $("#printInfoForm #polProperty").val();
	if (polProperty === null || "" === polProperty) {
		lion.util.info("警告", "请选择保单性质");
		return false;
	}

	var unusualFlag = $("#printInfoForm #unusualFlag").val();
	if (unusualFlag === null || "" === unusualFlag) {
		lion.util.info("警告", "请选择异常告知");
		return false;
	}
	return true;
}
// 查询成功回调函数
function successQuery(data, obj, arg) {
	var applicant = data.applicantVo;
	var insured = data.insuredVo;
	insuredList = insured;
	reloadInsuredTable();
	var product = data.productsVos;
	busiProdList = product;
	reloadProductsTable();
	setTimeout(function() {
		// 回显投保人
		var jsonStrApplicant = JSON.stringify(applicant);
		var objApplicant = eval("(" + jsonStrApplicant + ")");
		var key, value, tagName, type, arr;
		for (x in objApplicant) {
			key = x;
			value = objApplicant[x];
			if ("firstChargeDate" == key || "applDate" == key
					|| "speEffectDate" == key || "joinBirthDate" == key) {
				value = new Date(value).format("yyyy-MM-dd");
			}
			$("[name='" + key + "'],[name='" + key + "[]']").each(function() {
				tagName = $(this)[0].tagName;
				type = $(this).attr('type');

				if (tagName === 'INPUT') {
					if (type === 'radio') {
						$(this).attr('checked', $(this).val() === value);
					} else if (type === 'checkbox') {
						arr = value.split(',');
						for (var i = 0; i < arr.length; i++) {
							if ($(this).val() === arr[i]) {
								$(this).attr('checked', true);
								break;
							}
						}
					} else {
						$("#applicantSaveForm #" + key).val(value);
					}
				} else if (tagName === 'SELECT' || tagName === 'TEXTAREA') {
					$("#applicantSaveForm #" + key).combo("val", value);
				}

			});
		}

		$("#applicantSaveForm input[type='text']").attr("readonly", true);
		$("#applicantSaveForm select").attr("readonly", true);
		$("#applicantSaveForm #appNo").attr("readonly", false);

		flag = 1;

	}, 100);
}

Date.prototype.format = function(fmt) {
	// author: meizz
	var o = {
		"M+" : this.getMonth() + 1, // 月份
		"d+" : this.getDate(), // 日
		"h+" : this.getHours(), // 小时
		"m+" : this.getMinutes(), // 分
		"s+" : this.getSeconds(), // 秒
		"q+" : Math.floor((this.getMonth() + 3) / 3), // 季度
		"S" : this.getMilliseconds()
	// 毫秒
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}
// 清空按钮功能
$("#btnClear").click(function() {
	$("#applInfoForm").reset();
	$("#grpInsurInfoForm").reset();
	$("#payInfoForm").reset();
	$("#personalInsurInfoForm").reset();
	$("#printInfoForm").reset();
	$("#proposalInfoForm").reset();
	$("#vAddTaxInfoForm").reset();
	$(".fa").removeClass("fa-warning");
	$(".fa").removeClass("fa-check");
	$(".fa").removeClass("has-success");
	$(".fa").removeClass("has-error");
});

// 收付费分组
$("#btnChargePay").click(
		function() {
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.empty();
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.load(
					"/orbps/orbps/public/modal/html/chargePayGroupModal.html",
					function() {
						$(this).modal('toggle');
						// combo组件初始化
						$(this).comboInitLoad();
					});
			setTimeout(function() {
				com.common.reloadPublicChargePayModalTable();
			}, 100);
		});

// 影像信息查询
$("#imageQuery").click(
		function() {
			var salesBranchNo ="";
			if(com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos.length===1){
    			salesBranchNo = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[0].salesBranchNo;
    			salesChannel = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[0].salesChannel;
            	worksiteNo = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[0].worksiteNo;
            	saleCode = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[0].saleCode;
			}else{
	    		for (var i = 0; i < com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos.length; i++) {
	    			var jointFieldWorkFlag = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[i].jointFieldWorkFlag;
	    			if(jointFieldWorkFlag==="Y"){
	    				salesBranchNo = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[i].salesBranchNo;
	    				salesChannel = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[i].salesChannel;
                    	worksiteNo = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[i].worksiteNo;
                    	saleCode = com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos[i].saleCode;
	    				break;
	    			}
	    		}
    		}
			com.orbps.common.applNo = $("#applInfoForm #applNo").val();
			com.orbps.common.quotaEaNo = $("#applInfoForm #quotaEaNo").val();
			if (salesBranchNo !== "") {
				com.orbps.common.salesBranchNo = salesBranchNo;
			}
			com.orbps.common.salesChannel = salesChannel;
        	com.orbps.common.worksiteNo = worksiteNo;
        	com.orbps.common.saleCode = saleCode;
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.empty();
			com.orbps.contractReview.sgGrpInsurAppl.addDialog.load(
					"/orbps/orbps/public/modal/html/imageCollection.html",
					function() {
						$(this).modal('toggle');
						$(this).comboInitLoad();
					});
		});

// 添加成功回调函数
com.orbps.contractReview.sgGrpInsurAppl.successSubmit = function(data, arg) {
	// alert("chenggong");
	if (data.retCode === "1") {
		lion.util.info("提示", "复核清汇信息成功");
		// 提交成功，判断有无清单有清单，跳转按钮置成可点操作
		// 如果是团单清单标记 是无清单 置灰跳转按钮 复合不通过 按钮置不可点
		if ("L" !== $("#listFlag").val() || "N" === $("#reviewFlag").val()) {
			$("#btnlocation").attr("disabled", true);
		} else {
			$("#btnlocation").attr("disabled", false);
		}
	} else {
		lion.util.info("提示", "复核失败，失败原因：" + data.errMsg);
	}
}
$("#btnlocation")
		.click(
				function() {
					var applNo = $("#applNo").val();
					var taskId = com.orbps.contractReview.sgGrpInsurAppl.taskId;
					var applNoTaskId = applNo + "," + taskId;
					com.ipbps
							.menuFtn(
									"orbps/orbps/contractReview/listImport/html/offlineListImport.html",
									applNoTaskId);
				});