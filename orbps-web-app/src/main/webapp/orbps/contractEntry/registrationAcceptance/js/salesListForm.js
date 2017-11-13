com.orbps.contractEntry.registrationAcceptance.salesListForm = $("#salesListForm");
com.orbps.contractEntry.registrationAcceptance.salesList = new Array();
com.orbps.contractEntry.registrationAcceptance.salesCount = 0;
com.orbps.contractEntry.registrationAcceptance.salesType = -1;
com.orbps.contractEntry.registrationAcceptance.sales = $('#salesListTab');

$(function() {
	// 增加展业人员信息
	$("#salesListForm #btnSalesAdd").click(
			function() {
				if("" === $("#salesListForm #jointFieldWorkFlag").val() || "" === $("#salesListForm #salesChannel").val()){
					lion.util.info("提示","销售员主副标记或销售渠道为空");
					return false;
				}
				// 取到要保存的展业人员信息
				var salesVo = $("#salesListForm").serializeObject();
				var flag = salesVo.jointFieldWorkFlag;
				if("Y" === flag){
					for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.salesList.length; i++) {
						// 主副标记
						var jointFieldWorkFlag = com.orbps.contractEntry.registrationAcceptance.salesList[i].jointFieldWorkFlag;
						if(jointFieldWorkFlag==="Y"){
							lion.util.info("提示","主销售员已经存在，请选择副销售员");
							return false;
						}
						var salesBranchNo = com.orbps.contractEntry.registrationAcceptance.salesList[i].salesBranchNo;
						if("OA" === salesVo.salesChannel){
							var worksiteNo = com.orbps.contractEntry.registrationAcceptance.salesList[i].worksiteNo;
							if(salesBranchNo === salesVo.salesBranchNo && worksiteNo===salesVo.worksiteNo){
								lion.util.info("提示","网点不能重复添加，请修改代理网点号");
								return false;
							}
						}else{
							var salesNo = com.orbps.contractEntry.registrationAcceptance.salesList[i].salesNo;
							if(salesBranchNo === salesVo.salesBranchNo && salesNo===salesVo.salesNo){
								lion.util.info("提示","销售人员不能重复添加，请修改销售人员代码");
								return false;
							}
						}
					}
				}else if("N" === flag){
					for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.salesList.length; i++) {
						var salesBranchNo = com.orbps.contractEntry.registrationAcceptance.salesList[i].salesBranchNo;
						if("OA" === salesVo.salesChannel){
							var worksiteNo = com.orbps.contractEntry.registrationAcceptance.salesList[i].worksiteNo;
							if(salesBranchNo === salesVo.salesBranchNo && worksiteNo===salesVo.worksiteNo){
								lion.util.info("提示","网点不能重复添加，请修改代理网点号");
								return false;
							}
						}else{
							var salesNo = com.orbps.contractEntry.registrationAcceptance.salesList[i].salesNo;
							if(salesBranchNo === salesVo.salesBranchNo && salesNo===salesVo.salesNo){
								lion.util.info("提示","销售人员不能重复添加，请修改销售人员代码");
								return false;
							}
						}
					}
				}
				
				// 需添加非空校验
				if ("OA" === $("#salesListForm #salesChannel").val()) {
					if ("" === $("#salesListForm #salesChannel").val()
							|| "" === $("#salesListForm #salesBranchNo").val()
							|| "" === $("#salesListForm #worksiteNo").val()
							|| "" === $("#salesListForm #worksiteName").val()
							|| "" === $("#salesListForm #businessPct").val()) {
						lion.util.info("警告", "网点信息或展业比例为空");
						return false;
					}
				} else if ("OA" !== $("#salesListForm #salesChannel").val()
						&& "" !== $("#salesListForm #salesChannel").val()) {
					if ("" === $("#salesListForm #salesChannel").val()
							|| "" === $("#salesListForm #salesBranchNo").val()
							|| "" === $("#salesListForm #salesNo").val()
							|| "" === $("#salesListForm #salesName").val()
							|| "" === $("#salesListForm #businessPct").val()) {
						lion.util.info("警告", "销售员信息或展业比例为空");
						return false;
					}
				}
				if($("#businessPct").val()<=0 || $("#businessPct").val() >=100){
					lion.util.info("个人展业比例必须大于0或小于100，请重新输入");
					return false;
				}
				com.orbps.contractEntry.registrationAcceptance.salesList
						.push(salesVo);// 给表格赋值
				com.orbps.contractEntry.registrationAcceptance.salesCount++;

				// 刷新table
				com.orbps.contractEntry.registrationAcceptance
						.reloadSalesListTable();
				// form清空
				$("#salesListForm input[type='text']").val("");
				$("#salesListForm #salesChannel").combo("refresh");
			});
	// 删除展业人员信息
	com.orbps.contractEntry.registrationAcceptance.deleteSalesList = function(
			vform) {
		if (confirm("您确认要删除此条记录吗？")) {
			var radioVal = this.name;
			if (com.orbps.contractEntry.registrationAcceptance.salesList != null
					&& com.orbps.contractEntry.registrationAcceptance.salesList.length > 0) {
				com.orbps.contractEntry.registrationAcceptance.salesList
						.splice(vform, 1);
				com.orbps.contractEntry.registrationAcceptance.salesCount--;
			}
			// 刷新table
			com.orbps.contractEntry.registrationAcceptance
					.reloadSalesListTable();
		}
	};
	// 修改清单信息
	$("#btnSalesUpdate")
			.click(
					function() {
						// 需添加非空校验
						var temp = document.getElementsByName("salesListRad");
						var j = 0;// 用于判断是否有选择的记录
						for (var i = 0; i < temp.length; i++) {
							if (temp[i].checked) {
								if ("OA" === $("#salesListForm #salesChannel")
										.val()) {
									if ("" === $("#salesListForm #salesChannel")
											.val()
											|| "" === $(
													"#salesListForm #salesBranchNo")
													.val()
											|| "" === $(
													"#salesListForm #worksiteNo")
													.val()
											|| "" === $(
													"#salesListForm #worksiteName")
													.val()
											|| "" === $(
													"#salesListForm #businessPct")
													.val()) {
										lion.util.info("警告", "网点信息或展业比例为空");
										return false;
									}
								} else if ("OA" !== $(
										"#salesListForm #salesChannel").val()
										&& "" !== $(
												"#salesListForm #salesChannel")
												.val()) {
									if ("" === $("#salesListForm #salesChannel")
											.val()
											|| "" === $(
													"#salesListForm #salesBranchNo")
													.val()
											|| "" === $(
													"#salesListForm #salesNo")
													.val()
											|| "" === $(
													"#salesListForm #salesName")
													.val()
											|| "" === $(
													"#salesListForm #businessPct")
													.val()) {
										lion.util.info("警告", "销售员信息或展业比例为空");
										return false;
									}
								}
								if($("#businessPct").val()<=0 || $("#businessPct").val() >=100){
									lion.util.info("展业比例不正确，请重新输入");
									return false;
								}
								com.orbps.contractEntry.registrationAcceptance.salesType = temp[i].value;
								var salesVo = $("#salesListForm")
										.serializeObject();
								com.orbps.contractEntry.registrationAcceptance.salesList[com.orbps.contractEntry.registrationAcceptance.salesType] = salesVo;
								com.orbps.contractEntry.registrationAcceptance.salesType = -1;
							} else {
								j++;
							}
						}
						if (j === temp.length) {
							lion.util.info("请选择一条记录");
							return false;
						}
						// 刷新table
						com.orbps.contractEntry.registrationAcceptance
								.reloadSalesListTable();
						// form清空
						$("#salesListForm input[type='text']").val("");
						$("#salesListForm #salesChannel").combo("refresh");
					});
	// 回显展业人员信息。
	com.orbps.contractEntry.registrationAcceptance.querySalesList = function(
			vform) {
		$("#salesListForm input[type='text']").val("");
		$("#salesListForm select2").combo("refresh");
		var radioVal;
		// 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
		var temp = document.getElementsByName("salesListRad");
		for (var i = 0; i < temp.length; i++) {
			if (temp[i].checked) {
				radioVal = temp[i].value;
			}
		}
		com.orbps.contractEntry.registrationAcceptance.salesType = radioVal;
		var salesVo = com.orbps.contractEntry.registrationAcceptance.salesList[radioVal];
		// 回显
		setTimeout(function(){
			com.orbps.contractEntry.registrationAcceptance.showSales(salesVo);
			//给选中的radio置为checked
//			$("input[id='salesListRad"+radioVal+"']").attr("checked","checked");
			$("input[name='salesListRad']").eq(radioVal).attr("checked","checked");
		},100);
	};
});

//设置中介代理机构信息
$("#salesAgencyFlag").change(function() {
	var flag = $("#salesAgencyFlag").val();
	if("Y" === flag){
		$("#salesListForm #agencyInfo").show();
		$("#salesDevelopFlagForm #salesDevelopFlag").combo("val","N");
		$("#salesDevelopFlagForm #salesDevelopFlag").attr("readOnly",true);
		$("#salesListForm #salesChannelAgency").combo("val","OA");
		$("#salesListForm #salesChannelAgency").attr("readOnly",true);
		$("#salesListForm #salesChannel").combo("clear");
		$("#salesListForm #salesBranchNo").val("");
		$("#salesListForm #salesNo").val("");
		$("#salesListForm #salesName").val("");
		$("#salesListForm #worksiteNo").val("");
		$("#salesListForm #worksiteName").val("");
		$("#salesListForm #businessPct").val("");
		if(1 === com.orbps.contractEntry.registrationAcceptance.queryFlag){
			com.orbps.contractEntry.registrationAcceptance.salesList = [];
		}
		$("#salesListForm #salesInfo").hide();
	}else if("N" === flag){
		$("#salesListForm #salesChannelAgency").combo("clear");
		$("#salesListForm #branchNoAgency").val("");
		$("#salesListForm #workSiteNoAgency").val("");
		$("#salesListForm #workSiteNameAgency").val("");
		$("#salesListForm #salesBranchNoAgency").val("");
		$("#salesListForm #salesCodeAgency").val("");
		$("#salesListForm #salesNameAgency").val("");
		$("#salesListForm #agencyNo").val("");
		$("#salesListForm #agencyName").val("");
		$("#salesListForm #agencyInfo").hide();
		$("#salesListForm #salesInfo").show();
	}
	if("Y" !== flag){
		$("#salesDevelopFlagForm #salesDevelopFlag").attr("readOnly",false);
	}
	com.orbps.contractEntry.registrationAcceptance.queryFlag = 1;
});

// 显示展业人员录入界面
$("#salesDevelopFlag").change(function() {
	var flag = $("#salesDevelopFlag").val();
	if (flag === "Y") {
		com.orbps.contractEntry.registrationAcceptance.reloadSalesListTable();
		$("#salesListForm #businessPct").val("");
		$("#salesListForm #businessPctDiv").show();
		$("#salesInformation").show();
		$("#jointFieldWorkFlag").attr("readOnly", false);
	} else if (flag === "N") {
		if(1 === com.orbps.contractEntry.registrationAcceptance.salesDevelopChangeFlag){
			com.orbps.contractEntry.registrationAcceptance.salesList = new Array();
			com.orbps.contractEntry.registrationAcceptance.salesCount = 0;
		}
		$("#salesListForm #businessPct").val("");
		$("#salesListForm #businessPctDiv").hide();
		$("#salesInformation").hide();
		$("#jointFieldWorkFlag").attr("readOnly", true);
		$("#jointFieldWorkFlag").combo("clear");
	}
	com.orbps.contractEntry.registrationAcceptance.salesDevelopChangeFlag = 1;
});
// 重新加载表格
com.orbps.contractEntry.registrationAcceptance.reloadSalesListTable = function() {
	$('#apTaskListTab').find("tbody").empty();
	if (com.orbps.contractEntry.registrationAcceptance.salesList != null
			&& com.orbps.contractEntry.registrationAcceptance.salesList.length > 0) {
		for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.salesList.length; i++) {
			var salesInfo = com.orbps.contractEntry.registrationAcceptance.salesList[i];
			var salesChannelName = com.orbps.publicSearch
					.salesChannelQuery(salesInfo.salesChannel);
			var jointFieldWorkFlag = com.orbps.publicSearch
					.jointFieldWorkFlag(salesInfo.jointFieldWorkFlag);
			if(jointFieldWorkFlag === undefined){
				$('#apTaskListTab').find("tbody").append(
				"<tr><td colspan='8' align='center'>无记录</td></tr>");
				break;
			}
			var businessPct = salesInfo.businessPct + "%";
			var code = "";
			var name = "";
			if ("" === salesInfo.salesNo && "" !== salesInfo.worksiteNo) {
				code = salesInfo.worksiteNo;
			} else if ("" === salesInfo.worksiteNo && "" !== salesInfo.salesNo) {
				code = salesInfo.salesNo;
			}
			if ("" === salesInfo.salesName && "" !== salesInfo.worksiteName) {
				name = salesInfo.worksiteName;
			} else if ("" === salesInfo.worksiteName
					&& "" !== salesInfo.salesName) {
				name = salesInfo.salesName;
			}
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
									+ code
									+ "</td><td style='width: 20px;'>"
									+ name
									+ "</td><td style='width: 20px;'>"
									+ businessPct
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

// 回显方法
com.orbps.contractEntry.registrationAcceptance.showSales = function(msg) {
	jsonStr = JSON.stringify(msg);// 将msg信息json串解析
	var obj = eval("(" + jsonStr + ")");
	var key, value, tagName, type, arr;
	for (x in obj) {
		key = x;
		value = obj[x];
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
					// var holderListStr =
					// key.replace("app","isd");将list元素中id带有app的替换成isd，回显出被保人信息。
					$("#salesListForm #" + key).val(value);
				}
			} else if (tagName === 'SELECT' || tagName === 'TEXTAREA') {
				// var holderListSle = key.replace("app","isd");
				$("#salesListForm #" + key).combo("val", value);
			}
		});
	}
}

// 非空校验
$("#salesListForm #salesName").blur(function() {
	var salesName = $("#salesListForm #salesName").val();
	if (salesName === null || "" === salesName) {
		lion.util.info("警告", "销售人员姓名不能为空");
		return false;
	}
});

$("#salesListForm #businessPct").blur(function() {
	var businessPct = $("#salesListForm #businessPct").val();
	if (businessPct === null || "" === businessPct) {
		lion.util.info("警告", "展业比例不能为空");
		return false;
	}
});

$("#salesListForm #salesBranchNo").blur(function() {
	var salesBranchNo = $("#salesListForm #salesBranchNo").val();
	if (salesBranchNo === null || "" === salesBranchNo) {
		lion.util.info("警告", "销售机构代码不能为空");
		return false;
	}
});

$("#salesListForm #salesNo").blur(function() {
	var salesNo = $("#salesListForm #salesNo").val();
	if (lion.util.isEmpty(salesNo) || "" === salesNo) {
		lion.util.info("警告", "销售人员代码不能为空");
		return false;
	}
});

$("#salesListForm #branchNoAgency").blur(function() {
	var branchNoAgency = $("#salesListForm #branchNoAgency").val();
	if (lion.util.isEmpty(branchNoAgency) || "" === branchNoAgency) {
		lion.util.info("警告", "销售机构不能为空");
		return false;
	}else{
		$("#salesListForm #salesBranchNoAgency").val(branchNoAgency);
	}
});

$("#salesListForm #workSiteNoAgency").blur(function() {
	var workSiteNoAgency = $("#salesListForm #workSiteNoAgency").val();
	if (lion.util.isEmpty(workSiteNoAgency) || "" === workSiteNoAgency) {
		lion.util.info("警告", "代理网点号不能为空");
		return false;
	}
});

$("#salesListForm #workSiteNameAgency").blur(function() {
	var workSiteNameAgency = $("#salesListForm #workSiteNameAgency").val();
	if (lion.util.isEmpty(workSiteNameAgency) || "" === workSiteNameAgency) {
		lion.util.info("警告", "网点名称不能为空");
		return false;
	}
});

$("#salesListForm #salesCodeAgency").blur(function() {
	var salesCodeAgency = $("#salesListForm #salesCodeAgency").val();
	if (lion.util.isEmpty(salesCodeAgency) || "" === salesCodeAgency) {
		lion.util.info("警告", "销售员代码不能为空");
		return false;
	}
});

$("#salesListForm #salesNameAgency").blur(function() {
	var salesNameAgency = $("#salesListForm #salesNameAgency").val();
	if (lion.util.isEmpty(salesNameAgency) || "" === salesNameAgency) {
		lion.util.info("警告", "销售员姓名不能为空");
		return false;
	}
});

$("#salesListForm #salesNo")
.on(
		'keyup',
		function(event) {
			var $salesNoInput = $(this);
			event = window.event || event;
			//获取光标所在文本中的下标
			var pos = getTxt1CursorPosition(this);
			// 响应鼠标事件，允许左右方向键移动 删除和delete
			if (event.keyCode === 37 || event.keyCode === 39 || event.keyCode === 8 || event.keyCode === 46) {
				return ;
			}
			$salesNoInput.val($salesNoInput.val().replace(
					/[^\dA-Za-z]/g,''));
			//移动光标到所定位置
			setCaret(this,pos);
		}
	)

