// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.grpInsurAppl命名空间
com.orbps.contractEntry.grpInsurAppl = {};
// treegrid
com.orbps.contractEntry.grpInsurAppl.tableId = $('#treegridId');
// 编辑或添加对话框
com.orbps.contractEntry.grpInsurAppl.addDialog = $('#btnModel');
com.orbps.contractEntry.grpInsurAppl.benesList = [];
com.orbps.contractEntry.grpInsurAppl.applInfoForm = $('#applInfoForm');
com.orbps.contractEntry.grpInsurAppl.queryBusiprodList = [];
com.orbps.contractEntry.grpInsurAppl.taskId;
com.orbps.contractEntry.grpInsurAppl.agreementNoFlag;
com.orbps.contractEntry.grpInsurAppl.dataList;
com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray = [];
com.orbps.contractEntry.grpInsurAppl.constructionInfoArray = [];
com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos = [];
//所有的有关健康险的东西
com.orbps.contractEntry.grpInsurAppl.allHealthInsur;
//基金险的flag
com.orbps.contractEntry.grpInsurAppl.FundInsurFlag;

// 页面跳转查询成功回调函数
com.orbps.contractEntry.grpInsurAppl.successQueryDetail = function(data, args) {
	com.orbps.contractEntry.grpInsurAppl.dataList = data;
	com.orbps.contractEntry.grpInsurAppl.taskId = data.taskInfo.taskId;
	com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos = data.applInfoVo.grpSalesListFormVos;
	var jsonStrs = JSON.stringify(data);
	var objs = eval("(" + jsonStrs + ")");
	var form;
	for (y in objs) {
		var values = objs[y];
		var jsonStrApplicant = JSON.stringify(values);
		var objApplicant = eval("(" + jsonStrApplicant + ")");
		var key, value, tagName, type, arr;
		for (x in objApplicant) {
			key = x;
			value = objApplicant[key];
			if ("applDate" == key || "inForceDate" == key
					|| "enstPremDeadline" == key || "constructionDur" == key
					|| "until" == key || "preMioDate" ==key) {
				value = new Date(value).format("yyyy-MM-dd");
			}
			$("[name='" + key + "'],[name='" + key + "[]']").each(function() {
				tagName = $(this)[0].tagName;
				type = $(this).attr('type');
				if (tagName == 'INPUT') {
					if (type == 'radio') {
						$(this).attr('checked', $(this).val() == value);
					} else if (type == 'checkbox') {
						arr = value.split(',');
						for (var i = 0; i < arr.length; i++) {
							if ($(this).val() == arr[i]) {
								$(this).attr('checked', true);
								break;
							}
						}
					} else {
						$("#" + key).val(value);
					}
				} else if (tagName == 'SELECT') {
					$("#" + key).comboInitLoadById(value);
				} else if (tagName == 'TEXTAREA') {
					$("#" + key).val(value);
				}

			});
		}
	}
	//特别约定单独赋值
	$("#specialPro").val(data.proposalInfoVo.specialPro);
	$("*").comboInitLoad();
	setTimeout(function() {
		// 将省市县置为可以回显的状态
		$(".city").attr("disabled", false);
		$(".prov").attr("disabled", false);
		$(".dist").attr("disabled", false);
		$("#applBaseInfoForm #appAddrProv").combo("val",
				data.applBaseInfoVo.appAddrProv);
		$("#applBaseInfoForm #appAddrCity").combo("val",
				data.applBaseInfoVo.appAddrCity);
		$("#applBaseInfoForm #appAddrCountry").combo("val",
				data.applBaseInfoVo.appAddrCountry);
	}, 1000);

	// 赋值被保人分组信息为全局变量
	com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
	// 取到责任信息list
	for (var i = 0; i < data.busiPrdVos.length; i++) {
		// 当责任不为空时放入全局变量
		if (lion.util.isNotEmpty(data.busiPrdVos[i].responseVos)) {
			for (var j = 0; j < data.busiPrdVos[i].responseVos.length; j++) {
				var array_element = data.busiPrdVos[i].responseVos[j];
				com.orbps.common.list.push(array_element);
			}
		}
	}
	console.log(com.orbps.common.list)
	// 收付费分组信息
	com.common.chargePayList = data.chargePayGroupModalVos;
	// 险种表格回显
	$('#fbp-editDataGrid').editDatagrids("bodyInit", data.busiPrdVos);
	com.orbps.contractEntry.grpInsurAppl.queryBusiprodList = data.busiPrdVos;
	setTimeout(function(){
		$("#specialInsurAddInfoForm").hide();
		var hide1 = 0;
		var hide2 = 0;
		$("#tab1").hide();//表头
		$("#tab2").hide();
		$("#tab3").hide();
		//循环要约险种的险种。
		for(var i = 0; i < data.busiPrdVos.length; i++){
			var a = data.busiPrdVos[i].busiPrdCode;
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
		/***
		 * 查询险种中是否有基金险
		 */
		var busiPrdCode = "";
		for (var i = 0; i < data.busiPrdVos.length; i++) {
			if(busiPrdCode === ""){
				busiPrdCode = data.busiPrdVos[i].busiPrdCode;
			}else{
				busiPrdCode = busiPrdCode + "," + data.busiPrdVos[i].busiPrdCode;
			}
		}
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
	},1000);
	// 结算日期
	if (lion.util.isNotEmpty(data.payInfoVo.settlementDate)) {
		com.orbps.contractEntry.grpInsurAppl.settleList = data.payInfoVo.settlementDate;
	}

	// 计算施工天数
	var constructionDur = $("#specialInsurAddInfoForm #constructionDur").val();
	var until = $("#specialInsurAddInfoForm #until").val();
	if (constructionDur !== "" && until !== "") {
		com.orbps.contractEntry.grpInsurAppl.DateDiff(constructionDur, until);
	}
	// 根据结算方式更改页面样式
	if (data.payInfoVo.stlType === "N") {
		$("#payInfoForm #stlLimit").attr("disabled", true);
		$("#payInfoForm #settlementRatio").attr("disabled", true);
		$("#payInfoForm #btnStlDate").attr("disabled", true);
	} else if (data.payInfoVo.stlType === "X") {
		$("#stlLimit").attr("disabled", false);
		$("#settlementRatio").attr("disabled", true);
		$("#btnStlDate").attr("disabled", false);
	} else if (data.payInfoVo.stlType === "D") {
		$("#stlLimit").attr("disabled", true);
		$("#settlementRatio").attr("disabled", true);
		$("#btnStlDate").attr("disabled", false);
	} else if (data.payInfoVo.stlType === "L") {
		$("#stlLimit").attr("disabled", true);
		$("#settlementRatio").attr("disabled", false);
		$("#btnStlDate").attr("disabled", true);
	}
	
	// 取到组织树节点信息
	if (data.organizaHierarModalVos.length > 0) {
		com.orbps.common.zTrees = com.orbps.common
				.OHMFtn(data.organizaHierarModalVos);
	} else {
		com.orbps.common.zTrees = [ {
			id : 1,
			pId : "#",
			name : "默认节点(请重命名)",
			open : false,
			nocheck : false,
			checked : true,
			noRemoveBtn : true,
			noRenameBtn : true,
			noEditBtn : true,
			chkDisabled : false,
			isEdit : false,
			children : []
		} ];
	}
	com.orbps.common.oranLevelList = data.organizaHierarModalVos;
	console.log(data);
	console.log(com.orbps.common.zNodes);
	// 如果新单状态不是提交，置灰跳转按钮。 02
	if ("02" !== data.approvalState) {
		$("#btnlocation").attr("disabled", true);
	}
	// 增加共保协议号是否必录的标识
	if("agreementNo" in data.applInfoVo){
		if ("" === data.applInfoVo.agreementNo) {
			com.orbps.contractEntry.grpInsurAppl.agreementNoFlag = "N";
		} else {
			com.orbps.contractEntry.grpInsurAppl.agreementNoFlag = "Y";
		}
	}else{
		com.orbps.contractEntry.grpInsurAppl.agreementNoFlag = "N";
	}
	setTimeout(function(){
		var registerArea = $("#applBaseInfoForm #registerArea").val();
		if (registerArea === "") {
			//页面加载时企业注册地默认为中国
			$("#applBaseInfoForm #registerArea").combo("val", "CHN");
		}
		var disputePorcWay = $("#applBaseInfoForm #disputePorcWay").val();
		if (disputePorcWay === "") {
			// 争议处理方式默认值位仲裁
			$("#applBaseInfoForm #disputePorcWay").combo("val", "1");
		}
		var giftFlag = $("#printInfoForm #giftFlag").val();
		$("#printInfoForm #giftFlag").attr("readOnly", true);
		if (giftFlag === "") {
			//赠送险标记默认否。
			$("#printInfoForm #giftFlag").combo("val", "0");
		}
		var applProperty = $("#printInfoForm #applProperty").val();
		if (applProperty === "") {
			//保单性质默认否。
			$("#printInfoForm #applProperty").combo("val", "0");
		}
		var exceptionInform = $("#printInfoForm #exceptionInform").val();
		if (exceptionInform === "") {
			//异常告知下拉框默认显示否
			$("#printInfoForm #exceptionInform").combo("val", "N");
		}
		var ipsnlstId = $("#printInfoForm #ipsnlstId").val();
		if (ipsnlstId === "") {
			//团单清单标记默认为普通清单
			$("#printInfoForm #ipsnlstId").combo("val", "L");
		}
		if ("" === $("#enstPremDeadline").val()) {
			//设置日期，当前日期的后15天
			var myDate = new Date(); //获取今天日期	
			myDate.setDate(myDate.getDate() + 15);
			$("#enstPremDeadline").val(myDate.format("yyyy-MM-dd"));
		}
		var premFrom = $("#payInfoForm #premFrom").val();
		if (premFrom === "") {
			// 使保费来源默认为团体账户付款
			$("#payInfoForm #premFrom").combo("val", "1");
		}
		var stlType = $("#payInfoForm #stlType").val();
		if (stlType === "") {
			// 使结算方式默认为即时结算
			$("#payInfoForm #stlType").combo("val", "N");
		}
	},1500);
}


//页面跳转查询成功回调函数
com.orbps.contractEntry.grpInsurAppl.successQuery = function(data, args) {
	//声明团单大Vo对象
	var grpInsurApplVo = {};
	//将查询出的保单基本信息放入团单大Vo中，用于回显基本信息数据。
	grpInsurApplVo.applBaseInfoVo = data.applBaseInfoVo;
	var jsonStrs = JSON.stringify(grpInsurApplVo);
	var objs = eval("(" + jsonStrs + ")");
	var form;
	for (y in objs) {
		if("payInfoVo"===y){
			continue;
		}
		var values = objs[y];
		var jsonStrApplicant = JSON.stringify(values);
		var objApplicant = eval("(" + jsonStrApplicant + ")");
		var key, value, tagName, type, arr;
		for (x in objApplicant) {
			key = x;
			value = objApplicant[key];
			if(null ===value || "" === value || undefined === value){
				continue;
			}
			if ("applDate" == key || "inForceDate" == key
					|| "enstPremDeadline" == key || "constructionDur" == key
					|| "until" == key) {
				value = new Date(value).format("yyyy-MM-dd");
			}
			$("[name='" + key + "'],[name='" + key + "[]']").each(function() {
				tagName = $(this)[0].tagName;
				type = $(this).attr('type');
				if (tagName == 'INPUT') {
					if (type == 'radio') {
						$(this).attr('checked', $(this).val() == value);
					} else if (type == 'checkbox') {
						arr = value.split(',');
						for (var i = 0; i < arr.length; i++) {
							if ($(this).val() == arr[i]) {
								$(this).attr('checked', true);
								break;
							}
						}
					} else {
						$("#" + key).val(value);
					}
				} else if (tagName == 'SELECT') {
					$("#" + key).comboInitLoadById(value);
				} else if (tagName == 'TEXTAREA') {
					$("#" + key).val(value);
				}

			});
		}
	}
//	if(null !==data.busiPrdVos && undefined !== data.busiPrdVos){
//		// 险种表格回显
//		$('#fbp-editDataGrid').editDatagrids("bodyInit", data.busiPrdVos);
//		com.orbps.contractEntry.grpInsurAppl.queryBusiprodList = data.busiPrdVos;
//	}
	
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

$(function() {
	// 投保单号置灰
	$("#applInfoForm #applNo").attr("readonly", true);
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});

	// 文件上传下载插件初始化
	$("#file").fileinput({
		'allowedFileExtensions' : [ 'jpg', 'png', 'gif', 'xlsx' ],
		'showUpload' : false,
		'showPreview' : false,
		'showCaption' : true,
		'browseClass' : 'btn btn-success',
	});

	// 初始化edittable组件
	$("#fbp-editDataGrid").editDatagridsLoadById();

	// combo组件初始化
//	$("*").comboInitLoad();

	lion.util.postjson('/orbps/web/orbps/contractEntry/read/queryPolCode',null,
			function (data, arg) {
			com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray = [];
			com.orbps.contractEntry.grpInsurAppl.healthPublicAmountArray = data.split("-")[1].split(":")[1].split(",");
			com.orbps.contractEntry.grpInsurAppl.constructionInfoArray = data.split("-")[0].split(":")[1].split(",");
			},null, null);
	//查询所有健康险专项标示的险种
	lion.util.postjson("/orbps/web/orbps/contractEntry/read/queryAllHealthInsur","",function(data){
		com.orbps.contractEntry.grpInsurAppl.allHealthInsur = data;
	});
	
	//服务器调用
	 var dataCipher = com.ipbps.getDataCipher();
	 var url = '/orbps/web/authSupport/check?serviceName=cntrEntryGrpInsurApplService&dataCipher='
	 		+ dataCipher;
	 lion.util.postjson(url, null,com.orbps.contractEntry.grpInsurAppl.successQueryDetail,null, null);
//			var taskInfo = {};
//			taskInfo.bizNo = "2017032700000005"
////			taskInfo.bizNo = "2017000000032402"
//			lion.util.postjson('/orbps/web/orbps/contractEntry/grp/query',taskInfo,com.orbps.contractEntry.grpInsurAppl.successQueryDetail,null,null);
});

// 按回车相当于tab功能(键盘按键触发事件)
$("input:text").keypress(function(e) {
	if (e.which == 13) {// 判断所按是否回车键
		var inputs = $("input:text "); // 获取表单中的所有输入框
		var selects = $("select"); // 获取表单中的所有输入框
		var idx = inputs.index(this); // 获取当前焦点输入框所处的位置
		inputs[idx + 1].focus(); // 设置焦点
		inputs[idx + 1].select(); // 选中文字
		return false; // 取消默认的提交行为
	}
});
// 查询销售机构名称回调函数
com.orbps.contractEntry.grpInsurAppl.successQueryBranchName = function(data,
		arg) {
	if (data === "fail") {
		lion.util.info("销售机构信息不存在，请确认机构代码是否正确");
	} else {
		$("#applInfoForm #salesBranchName").val(data);
	}
}

// 查询销售员姓名回调函数
com.orbps.contractEntry.grpInsurAppl.successQuerySaleName = function(data, arg) {
	if (data === "fail") {
		lion.util.info("销售员不存在，请确认销售渠道，销售机构，销售员代码是否正确");
	} else {
		$("#applInfoForm  #saleName").val(data);
		$("#applInfoForm  #saleName").attr("readOnly", true);
	}
};

// 计算天数差的函数
com.orbps.contractEntry.grpInsurAppl.DateDiff = function(sDate1, sDate2) { // sDate1和sDate2是2006-12-18格式
	var Date1 = new Date(sDate1);
	var Date2 = new Date(sDate2);
	var a = (Date2.getTime() - Date1.getTime()) / (24 * 60 * 60 * 1000) + 1;
	$("#totalDays").val(a);
}

// 查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
	com.orbps.contractEntry.grpInsurAppl.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.contractEntry.grpInsurAppl.grpSalesListFormVos);
	});
});

//根据保单号查询基本信息
$("#oldApplNo")
.blur(
		function() {
			var applNo='';
			applNo= $("#oldApplNo").val();
			if("" === applNo || null === applNo){
				lion.util.info("请输入上期保单号");
				return false;
			}
			if(confirm("是否要用上期保单信息替换现有信息?")){
				lion.util
				.postjson(
						'/orbps/web/orbps/contractEntry/search/searGrpInfo',
						applNo,
						com.orbps.contractEntry.grpInsurAppl.successQuery,
						null, null);
			}
		});

//共保协议号离开焦点校验
$("#applInfoForm #agreementNo")
		.blur(
				function() {
					var agreementNum = $("#applInfoForm #agreementNo").val();
						lion.util
							.postjson(
									'/orbps/web/orbps/public/branchControl/searchAgreement',
									agreementNum,
									com.orbps.contractEntry.grpInsurAppl.successAgreement);
				});

//共保协议号离开焦点回调函数
com.orbps.contractEntry.grpInsurAppl.successAgreement = function(
		data, arg) {
	if(data.retCode==="0"){
		lion.util.info("提示",data.errMsg);
		$("#agreementNo").val("");
	}
}