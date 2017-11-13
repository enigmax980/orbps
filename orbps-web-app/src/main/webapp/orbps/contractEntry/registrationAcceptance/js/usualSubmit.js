com.orbps.contractEntry.registrationAcceptance.usualSubmitForm = $("#usualSubmitForm");
com.orbps.contractEntry.registrationAcceptance.addDialog = $("#btnModel");
com.orbps.common = {};
com.orbps.common.applNo;
com.orbps.common.salesBranchNo;
com.orbps.common.salesChannel;
com.orbps.common.worksiteNo;
com.orbps.common.saleCode;
com.orbps.common.quotaEaNo;
// 基本信息校验规则
$(function() {
	//设置受理时间允许修改
	$("#usualAcceptForm #applDate").attr("disabled",false);
	$("#usualAcceptForm #applDateBtn").attr("disabled",false);
	// 设置受理日期为当前日期
	if ("" === $("#usualAcceptForm #applDate").val()) {
		var date = new Date();
		var seperator1 = "-";
		var year = date.getFullYear();
		var month = date.getMonth() + 1;
		var strDate = date.getDate();
		if (month >= 1 && month <= 9) {
			month = "0" + month;
		}
		if (strDate >= 0 && strDate <= 9) {
			strDate = "0" + strDate;
		}
		var currentdate = year + seperator1 + month + seperator1 + strDate;
		$("#usualAcceptForm #applDate").val(currentdate);
		//设置受理时间不允许修改
		$("#usualAcceptForm #applDate").attr("disabled",true);
		$("#usualAcceptForm #applDateBtn").attr("disabled",true);
	}
	// 日期回显long转String
	Date.prototype.format = function(fmt) {
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
	//初始化页面
//	$("#usualBtnUpdate").attr("disabled", true);
	
	// 初始化校验函数
	com.orbps.contractEntry.registrationAcceptance
			.usualValidateForm(com.orbps.contractEntry.registrationAcceptance.usualSubmitForm);
	// 校验选择信息
	function validateSelectVal() {
		var entChannelFlag = $("#usualAcceptForm #entChannelFlag").val();
		if (entChannelFlag === null || "" === entChannelFlag) {
			lion.util.info("警告", "请选择外包录入标记");
			return false;
		}
		var polCode = $("#usualAcceptForm #polCode").val();
		if (polCode === null || "" === polCode) {
			lion.util.info("警告", "请选择险种信息");
			return false;
		}
		var agreementFlag = $("#usualAcceptForm #agreementFlag").val();
		if (agreementFlag === null || "" === agreementFlag) {
			lion.util.info("警告", "请选择共保标记");
			return false;
		}
	}
});
// 表单提交
$("#usualSubmitForm #usualBtnCommit")
		.click(
				function() {
					if (com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
							.validate().form()) {
						if(com.orbps.contractEntry.registrationAcceptance.selectVal()){
							if ("Y" === $("#agreementFlag").val()) {
								if ("" === $("#agreementNum").val()) {
									lion.util.info("请输入共保协议号");
								}
							}

							// 提交方法
							var regAcceptanceVo = {};
							// 提交资料清单
							var fileGflag = 0;
							var fileLflag = 0;
							if (com.orbps.contractEntry.registrationAcceptance.filesList.length > 0) {
								var filesListFormVos = new Array();
								for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.filesList.length; i++) {
									var array_element = com.orbps.contractEntry.registrationAcceptance.filesList[i];
									if("G" === $("#policyType").val()){
										if("1145"===array_element.fileType || "1146"===array_element.fileType){
											fileGflag = 1;
										}
									}else if ("L" === $("#policyType").val()){
										if("1150"===array_element.fileType){
											fileLflag = 1;
										}
									}
									filesListFormVos.push(array_element);
								}
								regAcceptanceVo.filesListVos = filesListFormVos;
							}
							//校验资料提交是否正确，判断规则：1、保单类型为团单时，1145、1146必须选择一个，不能同时选；2、保单类型为清单汇交件时，1150必须被选择
							if(com.orbps.contractEntry.registrationAcceptance.filesList.length > 0 && "G" === $("#policyType").val() && 0 === fileGflag){
								lion.util.info("保单类型是团单，添加资料时，1145、1146类型的资料至少要选择一项");
								return false;
							}else if(com.orbps.contractEntry.registrationAcceptance.filesList.length > 0 && "L" === $("#policyType").val() && 0 === fileLflag){
								lion.util.info("保单类型是清单汇交件，添加资料时，必须要选择1150的资料类型");
								return false;
							}

							// 提交销售人员信息
							var salesChannel = $("#salesListForm #salesChannel")
									.val();
							var salesName = $("#salesListForm #salesName").val();
							var worksiteName = $("#salesListForm #worksiteName")
									.val();
							var worksiteNo = $("#salesListForm #worksiteNo").val();
							var businessPct = $("#salesListForm #businessPct")
									.val();
							var salesBranchNo = $("#salesListForm #salesBranchNo")
									.val();
							//根据是否有代理网点信息的标识，设置销售员的提交信息
							if("N" === $("#salesAgencyFlag").val()){
								if ("N" === $("#salesDevelopFlag").val()) {
									if ("OA" === $("#salesListForm #salesChannel")
											.val()) {
										if (salesChannel === "" || worksiteName === ""
												|| worksiteNo === "") {
											lion.util.info("请输入网点信息");
											return false;
										} else {
											regAcceptanceVo.salesListFormVos = com.orbps.contractEntry.registrationAcceptance.salesListForm
													.serializeObject();
										}
									} else {
										if (salesChannel === "" || salesName === ""
												|| salesBranchNo === "") {
											lion.util.info("请输入销售员信息");
											return false;
										} else {
											regAcceptanceVo.salesListFormVos = com.orbps.contractEntry.registrationAcceptance.salesListForm
													.serializeObject();
										}
									}
								} else {
									if (com.orbps.contractEntry.registrationAcceptance.salesList.length <= 0) {
										lion.util.info("请添加销售员或网点信息");
										return false;
									} else {
										var salesListFormVos = new Array();
										var sumPct = 0.0;
										var salesFlag = "N";
										var salesFlaglength = 0;
										var salesList = new Array();
										for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.salesList.length; i++) {
											var array_element = com.orbps.contractEntry.registrationAcceptance.salesList[i];
											salesListFormVos.push(array_element);
											sumPct = Number((sumPct + Number(array_element.businessPct))
													.toFixed(2));
											if (i === 0) {
												salesList[0] = array_element;
											} else {
												for (var j = 0; j < salesList.length; j++) {
													if ("" !== array_element.worksiteNo && array_element.salesBranchNo === salesList[j].salesBranchNo
															&& array_element.worksiteNo === salesList[j].worksiteNo) {
														lion.util.info("警告",
																"不允许输入重复网点");
														return false;
													}else if ("" !== array_element.salesNo && array_element.salesBranchNo === salesList[j].salesBranchNo
															&& array_element.salesNo === salesList[j].salesNo) {
														lion.util.info("警告",
																"不允许输入重复销售员");
														return false;
													}
												}
												salesList[i] = array_element;
											}
											if ("Y" === array_element.jointFieldWorkFlag) {
												salesFlag = "Y";
												salesFlaglength++;
											}
										}
										regAcceptanceVo.salesListFormVos = salesListFormVos;
										if ("N" === salesFlag) {
											lion.util.info("警告", "如果是共同展业，必须要有主销售员");
											return false;
										}
										if (salesFlaglength > 1) {
											lion.util.info("主销售员只允许有一个");
											return false;
										}
										if (sumPct !== 100) {
											lion.util.info("警告",
													"展业比例之和必须为100，请核对后重新输入");
											return false;
										}
									}
								}
							}else if("Y" === $("#salesAgencyFlag").val()){
								if("" === $("#branchNoAgency").val() || "" === $("#workSiteNoAgency").val() || "" === $("#workSiteNameAgency").val()
										|| "" === $("#salesBranchNoAgency").val() || "" === $("#salesCodeAgency").val() || "" === $("#salesNameAgency").val() ){
									lion.util.info("网点代理或销售员信息为空");
								}else{
									regAcceptanceVo.salesListFormVos = com.orbps.contractEntry.registrationAcceptance.salesListForm
									.serializeObject();
								}
							}
							
							//设置受理时间允许修改
							$("#usualAcceptForm #applDate").attr("disabled",false);
							$("#usualAcceptForm #applDateBtn").attr("disabled",false);
							regAcceptanceVo.usualAcceptVo = com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
									.serializeObject();
							//设置受理时间不允许修改
							$("#usualAcceptForm #applDate").attr("disabled",true);
							$("#usualAcceptForm #applDateBtn").attr("disabled",true);
							regAcceptanceVo.salesDevelopFlag = $(
									"#salesDevelopFlag").val();
							lion.util
									.postjson(
											'/orbps/web/orbps/contractEntry/reg/submit',
											regAcceptanceVo,
											com.orbps.contractEntry.registrationAcceptance.successSubmit,
											null, null);
						
						}						
					}
				});

// 添加成功回调函数
com.orbps.contractEntry.registrationAcceptance.successSubmit = function(data,
		arg) {
	if (data.retCode === "1") {
		lion.util.info("提示", "提交成功");
		$("#imageQuery").attr("disabled", false);
//		$("#usualAcceptForm #applNo").attr("readOnly", true);
//		$("#usualAcceptForm #applDate").attr("readOnly", true);
	} else {
		lion.util.info("提示", "提交失败，失败原因：" + data.errMsg);
	}
}

// 修改并保存
$("#usualBtnUpdate")
		.click(
				function() {
					if (com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
							.validate().form()) {
						if(com.orbps.contractEntry.registrationAcceptance.selectVal()){
							if ("Y" === $("#agreementFlag").val()) {
								if ("" === $("#agreementNum").val()) {
									lion.util.info("请输入共保协议号");
								}
							}

							// 提交方法
							var regAcceptanceVo = {};
							// 提交资料清单
							var fileGflag = 0;
							var fileLflag = 0;
							if (com.orbps.contractEntry.registrationAcceptance.filesList.length > 0) {
								var filesListFormVos = new Array();
								for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.filesList.length; i++) {
									var array_element = com.orbps.contractEntry.registrationAcceptance.filesList[i];
									if("G" === $("#policyType").val()){
										if("1145"===array_element.fileType || "1146"===array_element.fileType){
											fileGflag = 1;
										}
									}else if ("L" === $("#policyType").val()){
										if("1150"===array_element.fileType){
											fileLflag = 1;
										}
									}
									filesListFormVos.push(array_element);
								}
								regAcceptanceVo.filesListVos = filesListFormVos;
							}
							//校验资料提交是否正确，判断规则：1、保单类型为团单时，1145、1146必须选择一个，不能同时选；2、保单类型为清单汇交件时，1150必须被选择
							if(com.orbps.contractEntry.registrationAcceptance.filesList.length > 0 && "G" === $("#policyType").val() && 0 === fileGflag){
								lion.util.info("保单类型是团单，资料类型不正确，请重新选择");
								return false;
							}else if(com.orbps.contractEntry.registrationAcceptance.filesList.length > 0 && "L" === $("#policyType").val() && 0 === fileLflag){
								lion.util.info("保单类型是清单汇交件，资料类型不正确，请重新选择");
								return false;
							}

							// 提交销售人员信息
							var salesChannel = $("#salesListForm #salesChannel")
									.val();
							var salesName = $("#salesListForm #salesName").val();
							var worksiteName = $("#salesListForm #worksiteName")
									.val();
							var worksiteNo = $("#salesListForm #worksiteNo").val();
							var businessPct = $("#salesListForm #businessPct")
									.val();
							var salesBranchNo = $("#salesListForm #salesBranchNo")
									.val();
							//判断是否有网点代理，根据判断结果，设置销售员的提交信息
							if("N" === $("#salesAgencyFlag").val()){
								//判断是否为共同展业
								if ("N" === $("#salesDevelopFlag").val()) {
									if ("OA" === $("#salesListForm #salesChannel")
											.val()) {
										if (salesChannel === "" || worksiteName === ""
												|| worksiteNo === "") {
											lion.util.info("请输入网点信息");
											return false;
										} else {
											regAcceptanceVo.salesListFormVos = com.orbps.contractEntry.registrationAcceptance.salesListForm
													.serializeObject();
										}
									} else {
										if (salesChannel === "" || salesName === ""
												|| salesBranchNo === "") {
											lion.util.info("请输入销售员信息");
											return false;
										} else {
											regAcceptanceVo.salesListFormVos = com.orbps.contractEntry.registrationAcceptance.salesListForm
													.serializeObject();
										}
									}
								} else {
									if (com.orbps.contractEntry.registrationAcceptance.salesList.length <= 0) {
										lion.util.info("请添加销售员或网点信息");
										return false;
									} else {
										var salesListFormVos = new Array();
										var sumPct = 0.0;
										var salesFlag = "N";
										var salesFlaglength = 0;
										var salesList = new Array();
										for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.salesList.length; i++) {
											var array_element = com.orbps.contractEntry.registrationAcceptance.salesList[i];
											salesListFormVos.push(array_element);
											sumPct = Number((sumPct + Number(array_element.businessPct))
													.toFixed(2));
											if (i === 0) {
												salesList[0] = array_element;
											} else {
												for (var j = 0; j < salesList.length; j++) {
													if (array_element.salesChannel === salesList[j].salesChannel
															&& array_element.salesBranchNo === salesList[j].salesBranchNo
															&& array_element.salesNo === salesList[j].salesNo) {
														lion.util.info("警告",
																"不允许输入重复销售员");
														return false;
													}
												}
												salesList[i] = array_element;
											}
											if ("Y" === array_element.jointFieldWorkFlag) {
												salesFlag = "Y";
												salesFlaglength++;
											}
										}
										regAcceptanceVo.salesListFormVos = salesListFormVos;
										if ("N" === salesFlag) {
											lion.util.info("警告", "如果是共同展业，必须要有主销售员");
											return false;
										}
										if (salesFlaglength > 1) {
											lion.util.info("主销售员只允许有一个");
											return false;
										}
										if (sumPct !== 100) {
											lion.util.info("警告",
													"展业比例之和必须为100，请核对后重新输入");
											return false;
										}
									}
								}
							}else if("Y" === $("#salesAgencyFlag").val()){
								if("" === $("#branchNoAgency").val() || "" === $("#workSiteNoAgency").val() || "" === $("#workSiteNameAgency").val()
										|| "" === $("#salesBranchNoAgency").val() || "" === $("#salesCodeAgency").val() || "" === $("#salesNameAgency").val() ){
									lion.util.info("网点代理或销售员信息为空");
								}else{
									regAcceptanceVo.salesListFormVos = com.orbps.contractEntry.registrationAcceptance.salesListForm
									.serializeObject();
								}
							}
							//设置受理时间允许修改
							$("#usualAcceptForm #applDate").attr("disabled",false);
							$("#usualAcceptForm #applDateBtn").attr("disabled",false);
							regAcceptanceVo.usualAcceptVo = com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
									.serializeObject();
							//设置受理时间不允许修改
							$("#usualAcceptForm #applDate").attr("disabled",true);
							$("#usualAcceptForm #applDateBtn").attr("disabled",true);
							regAcceptanceVo.salesDevelopFlag = $(
									"#salesDevelopFlag").val();
							lion.util
									.postjson(
											'/orbps/web/orbps/contractEntry/reg/save',
											regAcceptanceVo,
											com.orbps.contractEntry.registrationAcceptance.successSave,
											null, null);
						}
					}
				});

// 修改并保存回调函数
com.orbps.contractEntry.registrationAcceptance.successSave = function(data, arg) {
	if (data.retCode === "1") {
		lion.util.info("提示", "保存成功");
//		$("#usualAcceptForm #applNo").attr("readOnly", false);
//		$("#usualBtnUpdate").attr("disabled", true);
//		$("#usualBtnCommit").attr("disabled", false);
		$("#imageQuery").attr("disabled", false);
	} else {
		lion.util.info("提示", "提交失败，失败原因：" + data.errMsg);
	}
}

// 清空按钮功能
$("#usualBtnClear").click(function(){
	com.orbps.contractEntry.registrationAcceptance.clearFun();
});

//受理删除功能
$("#usualBtnDelete").click(function(){
	if(!confirm("此操作会删除该保单所有信息。\n确认要删除吗？")){
		return false;
	}
	var applNo = $("#usualAcceptForm #applNo").val();
	lion.util
	.postjson(
			'/orbps/web/orbps/contractEntry/reg/regDelete',
			applNo,
			com.orbps.contractEntry.registrationAcceptance.successDelete);
});

com.orbps.contractEntry.registrationAcceptance.clearFun = function() {
	com.orbps.contractEntry.registrationAcceptance.salesList = new Array();
	com.orbps.contractEntry.registrationAcceptance.filesList = new Array();
	$("#usualAcceptForm").reset();
	$("#salesListForm").reset();
	$("#filesListForm").reset();
	$(".fa").removeClass("fa-warning");
	$(".fa").removeClass("fa-check");
	$(".fa").removeClass("has-success");
	$(".fa").removeClass("has-error");
	// 币种下拉框默认显示人民币
	$("#currencyCode").combo("val", "CNY");
	// 外包录入标记下拉框默认显示否
	$("#entChannelFlag").combo("val", "N");
	// 是否共享客户信息标记下拉框默认显示否
	$("#paraVal").combo("val", "N");
	// 是否共保标记下拉框默认显示否
	$("#agreementFlag").combo("val", "N");
	// 是否共同展业默认为否
	$("#salesDevelopFlag").combo("val", "N");
	//设置受理日期
	//设置受理时间允许修改
	$("#usualAcceptForm #applDate").attr("disabled",false);
	$("#usualAcceptForm #applDateBtn").attr("disabled",false);
	// 设置受理日期为当前日期
	var date = new Date();
	var seperator1 = "-";
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var strDate = date.getDate();
	if (month >= 1 && month <= 9) {
		month = "0" + month;
	}
	if (strDate >= 0 && strDate <= 9) {
		strDate = "0" + strDate;
	}
	var currentdate = year + seperator1 + month + seperator1 + strDate;
	$("#usualAcceptForm #applDate").val(currentdate);
	//设置受理时间不允许修改
	$("#usualAcceptForm #applDate").attr("disabled",true);
	$("#usualAcceptForm #applDateBtn").attr("disabled",true);
	
	// 界面回显还原
//	$("#usualAcceptForm #applNo").attr("readOnly", false);
//	$("#usualBtnUpdate").attr("disabled", true);
//	$("#usualBtnCommit").attr("disabled", false);
	$("#imageQuery").attr("disabled", true);
	com.orbps.contractEntry.registrationAcceptance.reloadfilesTable();
	com.orbps.contractEntry.registrationAcceptance.reloadSalesListTable();
}

// 查询
$("#usualSubmitForm #usualBtnQuery")
		.click(
				function() {
					var regUsualAcceptVo = com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
							.serializeObject();
					com.orbps.contractEntry.registrationAcceptance.clearFun();
					$("#usualAcceptForm #applNo").val(regUsualAcceptVo.applNo);
					lion.util
							.postjson(
									'/orbps/web/orbps/contractEntry/reg/query',
									regUsualAcceptVo,
									com.orbps.contractEntry.registrationAcceptance.successQuery,
									null, null);

				});
function judge(data) {
	for ( var i in data) {
		return "T";
	}
	return "F";
}

// 查询成功回调函数
com.orbps.contractEntry.registrationAcceptance.successQuery = function(data,
		arg) {
	if ("F" === judge(data.usualAcceptVo)) {
		lion.util.info("没有查询到相关的受理信息");
		return false;
	}
	//设置受理时间允许修改
	$("#usualAcceptForm #applDate").attr("disabled",false);
	$("#usualAcceptForm #applDateBtn").attr("disabled",false);
	
//	$("#usualAcceptForm #applNo").attr("readOnly", true);
//	$("#usualBtnUpdate").attr("disabled", false);
	$("#imageQuery").attr("disabled", false);
//	$("#usualBtnCommit").attr("disabled", true);
	var msg = data.usualAcceptVo;
	// 回显方法
	jsonStr = JSON.stringify(msg);
	var obj = eval("(" + jsonStr + ")");
	var key, value, tagName, type, arr;
	for (x in obj) {
		key = x;
		value = obj[x];
		if ("salesDevelopFlag" === key) {
			$("#salesDevelopFlagForm #" + key).combo("val", value);
			continue;
		}
		if ("cntrType" === key) {
			$("#usualAcceptForm #policyType").combo("val", value);
		}
		if ("applDate" === key) {
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
					$("#usualAcceptForm #" + key).val(value);
				}
			} else if (tagName === 'SELECT' || tagName === 'TEXTAREA') {
				$("#usualAcceptForm #" + key).combo("val", value);
			}
		});
	}
	// 回显方法
	com.orbps.contractEntry.registrationAcceptance.salesList = data.salesListFormVos;
	if (com.orbps.contractEntry.registrationAcceptance.salesList != null
			&& com.orbps.contractEntry.registrationAcceptance.salesList.length == 1) {
		//设置查询回调标识
		com.orbps.contractEntry.registrationAcceptance.queryFlag = 0;
		com.orbps.contractEntry.registrationAcceptance.salesDevelopChangeFlag = 0;
		//判断是否有网点代理，并设置网点代理的标识
		if("salesChannelAgency" in com.orbps.contractEntry.registrationAcceptance.salesList[0] && "branchNoAgency" in com.orbps.contractEntry.registrationAcceptance.salesList[0] &&
				"workSiteNoAgency" in com.orbps.contractEntry.registrationAcceptance.salesList[0] && "workSiteNameAgency" in com.orbps.contractEntry.registrationAcceptance.salesList[0]){
			$("#salesDevelopFlagForm #salesAgencyFlag").combo("val","Y");
			$("#salesListForm #salesBranchNoAgency").val(com.orbps.contractEntry.registrationAcceptance.salesList[0].branchNoAgency);
		}else{
			$("#salesDevelopFlagForm #salesAgencyFlag").combo("val","N");
		}
		
		var msg = JSON
				.stringify(com.orbps.contractEntry.registrationAcceptance.salesList[0]);
		var obj = eval("(" + msg + ")");
		var key, value, tagName, type, arr;
		for (x in obj) {
			key = x;
			value = obj[x];
			if ("businessPct" === key) {
				continue;
			}
			if ("salesChannel" === key) {
				$("#salesListForm #" + key).combo("val", value);
				continue;
			}
			if ("salesChannelAgency" === key) {
				$("#salesListForm #" + key).combo("val", value);
				continue;
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
						$("#salesListForm #" + key).val(value);
					}
				}
			});
		}
	} else {
		// 重新加载表格

		$('#apTaskListTab').find("tbody").empty();
		if (com.orbps.contractEntry.registrationAcceptance.salesList !== null
				&& com.orbps.contractEntry.registrationAcceptance.salesList.length > 0) {

			for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.salesList.length; i++) {
				var salesChannelName = com.orbps.publicSearch
						.salesChannelQuery(com.orbps.contractEntry.registrationAcceptance.salesList[i].salesChannel);
				var jointFieldWorkFlag = com.orbps.publicSearch
						.jointFieldWorkFlag(com.orbps.contractEntry.registrationAcceptance.salesList[i].jointFieldWorkFlag);
				$('#apTaskListTab')
						.find("tbody")
						.append(
								"<tr><td style='width: 2px;'><input type='radio' onClick='com.orbps.contractEntry.registrationAcceptance.querySalesList()' id='salesListRad"
										+ i
										+ "' name='salesListRad' value='"
										+ i
										+ "'>"
										+ (i + 1)
										+ "</td><td style='width: 20px;'>"
										+ jointFieldWorkFlag
										+ "</td><td style='width: 20px;'>"
										+ salesChannelName
										+ "</td><td style='width: 20px;'>"
										+ com.orbps.contractEntry.registrationAcceptance.salesList[i].salesBranchNo
										+ "</td><td style='width: 20px;'>"
										+ com.orbps.contractEntry.registrationAcceptance.salesList[i].salesNo
										+ "</td><td style='width: 20px;'>"
										+ com.orbps.contractEntry.registrationAcceptance.salesList[i].salesName
										+ "</td><td style='width: 20px;'>"
										+ com.orbps.contractEntry.registrationAcceptance.salesList[i].businessPct
										+ "</td><td style='width: 5px;'><a href='javascript:void(0);' onclick='com.orbps.contractEntry.registrationAcceptance.deleteSalesList("
										+ i
										+ ");' for='salesListRad' id='salesListDel"
										+ i + "'>删除</a></td></tr>");
			}
		} else {
			$('#apTaskListTab').find("tbody").append(
					"<tr><td colspan='8' align='center'>无记录</td></tr>");
		}
	}
	
	//设置销售员界面展示
	if("Y" === $("#salesAgencyFlag").val()){
		$("#salesListForm #agencyInfo").show();
		$("#salesDevelopFlagForm #salesDevelopFlag").combo("val","N");
		$("#salesDevelopFlagForm #salesDevelopFlag").attr("readOnly",true);
		$("#salesListForm #salesChannelAgency").combo("val","OA");
		$("#salesListForm #salesChannelAgency").attr("readOnly",true);
		$("#salesListForm #salesInfo").hide();
	}else if("N" === $("#salesAgencyFlag").val()){
		$("#salesListForm #agencyInfo").hide();
		$("#salesListForm #salesInfo").show();
		$("#salesDevelopFlagForm #salesDevelopFlag").attr("readOnly",false);
		if ("OA" === $("#salesChannel").val()) {
			$("#worksiteHideDiv").show();
			$("#salesHideDiv").hide();
		} else {
			$("#worksiteHideDiv").hide();
			$("#salesHideDiv").show();
		}
	}

	// 重新加载资料表格
	com.orbps.contractEntry.registrationAcceptance.filesList = data.filesListVos;
	$('#filesListTab').find("tbody").empty();
	if (data.filesListVos !== null && data.filesListVos.length > 0
			&& data.filesListVos != undefined) {
		for (var i = 0; i < data.filesListVos.length; i++) {
			var fileTypeName = com.orbps.publicSearch
					.filesTypeQuery(data.filesListVos[i].fileType);
			$('#filesListTab')
					.find("tbody")
					.append(
							"<tr><td style='width: 2px;'><input type='radio' onClick='com.orbps.contractEntry.registrationAcceptance.queryFilesList()' id='filesListRad"
									+ i
									+ "' name='filesListRad' value='"
									+ i
									+ "'>"
									+ (i + 1)
									+ "</td><td style='width: 20px;'>"
									+ fileTypeName
									+ "</td><td style='width: 20px;'>"
									+ data.filesListVos[i].fileNum
									+ "</td><td style='width: 5px;'><a href='javascript:void(0);' onclick='com.orbps.contractEntry.registrationAcceptance.deleteFilesList("
									+ i
									+ ");' for='filesListRad' id='filesListDel"
									+ i + "'>删除</a></td></tr>");
		}
	} else {
		$('#filesListTab').find("tbody").append(
				"<tr><td colspan='4' align='center'>无记录</td></tr>");
	}
	//设置受理时间不允许修改
	$("#usualAcceptForm #applDate").attr("disabled",true);
	$("#usualAcceptForm #applDateBtn").attr("disabled",true);
}

// 影像查询
$("#imageQuery").click(
		function() {
			var salesBranchNo = $("#salesListForm #salesBranchNo").val();
			com.orbps.common.applNo = $("#usualAcceptForm #applNo").val();
			com.orbps.common.quotaEaNo = $("#usualAcceptForm #groupPriceNo").val();
			if($("#salesAgencyFlag").val() === "N"){
				com.orbps.common.salesBranchNo = salesBranchNo;
				com.orbps.common.salesChannel = $("#salesListForm #salesChannel").val();
				com.orbps.common.worksiteNo = $("#salesListForm #worksiteNo").val();
				com.orbps.common.saleCode = $("#salesListForm #salesNo").val();
			}else{
				com.orbps.common.salesChannel = "BS";
				com.orbps.common.salesBranchNo = $("#salesListForm #branchNoAgency").val();
				com.orbps.common.saleCode = $("#salesListForm #salesCodeAgency").val();
			}
			com.orbps.contractEntry.registrationAcceptance.addDialog.empty();
			com.orbps.contractEntry.registrationAcceptance.addDialog.load(
					"/orbps/orbps/public/modal/html/imageCollection.html",
					function() {
						$(this).modal('toggle');
						$(this).comboInitLoad();
					});
		});

$("#sumPremium").on(
		'keyup',
		function(event) {
			var $amountInput = $(this);
			// 响应鼠标事件，允许左右方向键移动
			event = window.event || event;
			if (event.keyCode === 37 | event.keyCode === 39) {
				return;
			}
			//获取光标所在文本中的下标
			var pos = getTxt1CursorPosition(this);
			// 先把非数字的都替换掉，除了数字和.
			$amountInput.val($amountInput.val().replace(/[^\d.]/g, "").
			// 只允许一个小数点
			replace(/^\./g, "").replace(/\.{2,}/g, ".").
			// 只能输入小数点后两位
			replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(
					/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'));
			//移动光标到所定位置
			setCaret(this,pos);
		});
$("#sumPremium").on('blur', function() {
	var $amountInput = $(this);
	// 最后一位是小数点的话，移除
	$amountInput.val(($amountInput.val().replace(/\.$/g, "")));
});

$("#usualAcceptForm #hldrName")
		.on(
				'keyup',
				function(event) {
					var $hldrNameInput = $(this);
					// 响应鼠标事件，允许左右方向键移动
					event = window.event || event;
					if (event.keyCode === 37 | event.keyCode === 39) {
						return;
					}
					// 先把非数字的都替换掉，除了数字和.
					$hldrNameInput.val($hldrNameInput.val().replace(
							/[^\a-\z\A-\Z0-9\u4E00-\u9FA5\.\(\)\（\）\·\-\ ]/g,
							''));

					var hldrName = $("#usualAcceptForm #hldrName").val();
					var len = hldrName.length;
					var reLen = 0;
					for (var i = 0; i < len; i++) {
						if (hldrName.charCodeAt(i) < 27
								|| hldrName.charCodeAt(i) > 126) {
							// 全角
							reLen += 2;
						} else {
							reLen++;
						}
					}
					if (reLen > 200) {
						hldrName = hldrName.substring(0, hldrName.length - 1);
						$("#usualAcceptForm #hldrName").val(hldrName);
					}
				});

$("#salesListForm #businessPct").on(
		'keyup',
		function(event) {
			var $businessPct = $(this);
			// 响应鼠标事件，允许左右方向键移动
			event = window.event || event;
			if (event.keyCode === 37 | event.keyCode === 39) {
				return;
			}
			// 先把非数字的都替换掉，除了数字和.
			$businessPct.val($businessPct.val().replace(/[^\d.]/g, "").
			// 只允许一个小数点
			replace(/^\./g, "").replace(/\.{2,}/g, ".").
			// 只能输入小数点后两位
			replace(".", "$#$").replace(/\./g, "").replace("$#$", ".").replace(
					/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'));
		});
$("#salesListForm #businessPct").on('blur', function() {
	var $businessPct = $(this);
	// 最后一位是小数点的话，移除
	$businessPct.val(($businessPct.val().replace(/\.$/g, "")));
	//展业比例丢失焦点校验,每个人的展业比例必须>0,<100%
	if($businessPct.value<=0||$businessPct.value>=100){$businessPct.value=''};
});

com.orbps.contractEntry.registrationAcceptance.successDelete = function(data,args){
	if (data.retCode === "1") {
		lion.util.info("提示", "删除成功");
	} else {
		lion.util.info("提示", "删除失败，失败原因：" + data.errMsg);
	}
}

com.orbps.contractEntry.registrationAcceptance.selectVal = function(){
	var policyType = $("#usualAcceptForm #policyType").val();
	if(null === policyType || "" === policyType){
		lion.util.info("保单类型不能为空");
		return false;
	}
	var entChannelFlag = $("#entChannelFlag").val();
	if(null === entChannelFlag || "" === entChannelFlag){
		lion.util.info("外包录入标记不能为空");
		return false;
	}
	var applDate = $("#usualAcceptForm #applDate").val();
	if(null === applDate || "" === applDate){
		lion.util.info("受理日期不能为空");
		return false;
	}
	var paraVal = $("#paraVal").val();
	if(null === paraVal || "" === paraVal){
		lion.util.info("是否共享客户信息不能为空");
		return false;
	}
	var agreementFlag = $("#agreementFlag").val();
	if(null === agreementFlag || "" === agreementFlag){
		lion.util.info("是否共保不能为空");
		return false;
	}
	var salesDevelopFlag = $("#salesDevelopFlagForm #salesDevelopFlag").val();
	if(null === salesDevelopFlag || "" === salesDevelopFlag){
		lion.util.info("是否共同展业不能为空");
		return false;
	}
	return true;
}
