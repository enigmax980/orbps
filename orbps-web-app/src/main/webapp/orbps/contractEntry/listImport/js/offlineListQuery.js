// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.offlineList命名空间
com.orbps.contractEntry.listimport = {};
// 新建表单id
com.orbps.contractEntry.listimport.offlineListImportForm = $("#offlineListImportForm");
// 新建表格id
com.orbps.contractEntry.listimport.sys_applinsured_list_tb = $("#sys_applinsured_list_tb");

$(function() {

	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});

	// combo组件初始化
	$("*").comboInitLoad();
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

// 查询表格信息
$("#btnQuery")
		.click(
				function() {
					// 添加查询参数 成功c 失败e 新建 N
					var state = com.orbps.contractEntry.listimport.offlineListImportForm
							.serializeObject();
					if ("S" == state.taskState) {
						state.taskState = "C";
					} else if ("F" == state.taskState) {
						state.taskState = "E";
					}
					//alert(state.taskState);
					// 添加查询参数
					com.orbps.contractEntry.listimport.sys_applinsured_list_tb
							.datagrids({
								querydata : state
							});
					// 重新加载数据
					com.orbps.contractEntry.listimport.sys_applinsured_list_tb
							.datagrids('reload');
				});
//清空查询条件
$("#btnClear").click(function(){
	$("#offlineListImportForm").reset();
//    $(".fa").removeClass("fa-warning");
//    $(".fa").removeClass("fa-check");
//    $(".fa").removeClass("has-success");
//    $(".fa").removeClass("has-error");
});

//错误导出
$("#btnExport").click(function(){
	
	//查询条件
	var state = com.orbps.contractEntry.listimport.offlineListImportForm.serializeObject();
	if ("S" == state.taskState) {
		state.taskState = "C";
	} else if ("F" == state.taskState) {
		state.taskState = "E";
	}
	lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/dataExport",state,function(data){
		if(data.length>10000){
			lion.util.info("提示:数据导出量超过10000条，请重新录入查询条件进行分批查询！");
			return false;
		}
		lion.util.info("提示","导出结果以当前查询条件为准，正在导出，请稍后…");
		$('#tabExport').find("tbody").empty();
		for (var l = 0; l < data.length; l++) {
			var array_element = data[l];
			$('#tabExport').find("tbody")
				.append("<tr><td style=\"mso-number-format:'\@';\">"
						+ com.orbps.publicExport.checkUndefined(array_element.applNo)
						+ "</td><td style=\"mso-number-format:'\@';\">"
						+ com.orbps.publicExport.checkUndefined(array_element.custNo)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.custName)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.salesBranchNo)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.salesCode)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.operateBranch)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.operatorNo)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.operatorName)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.insuredNum)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.importSp)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.taskState)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.errorNum)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.taskStartTime)
						+ "</td><td>"
						+ com.orbps.publicExport.checkUndefined(array_element.taskEndTime)
						+ "</td></tr>");
		}
		com.orbps.publicExport.outExcle("tabExport");
		lion.util.info("导出完成！");
	});
});
