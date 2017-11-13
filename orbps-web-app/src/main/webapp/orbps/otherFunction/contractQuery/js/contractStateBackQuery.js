com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery={};
com.orbps.otherFunction.contractQuery.contractStateBackQuery={};
com.orbps.otherFunction.contractQuery.contractStateBackQuery.contractStateBack= $("#contractStateBackForm");
com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList = [];

//查询成功回调函数
com.orbps.otherFunction.contractQuery.contractStateBackQuery.successQuery = function(data,args){
	if(lion.util.isEmpty(data)){
		lion.util.info("未查询到相关数据");
	}else{
		com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList = data;
		com.orbps.otherFunction.contractQuery.contractStateBackQuery.reloadSalesListTable();
	}
}

$(function (){
	$("*").comboInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
});
//点击查询
$("#btnQuery").click(function(){
	//清空上回查询的list
	com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList = []
	//查询条件
    var approvalVo = com.orbps.otherFunction.contractQuery.contractStateBackQuery.contractStateBack.serializeObject();
    lion.util.info("查询中，请稍后...");
    //调用服务查询
    lion.util.postjson('/orbps/web/orbps/otherfunction/revoke/back',approvalVo,com.orbps.otherFunction.contractQuery.contractStateBackQuery.successQuery);
});

//重新加载表格
com.orbps.otherFunction.contractQuery.contractStateBackQuery.reloadSalesListTable = function() {
	$('#approvalTab').find("tbody").empty();
	if (com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList != null
			&& com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList.length; i++) {
			var queryInfo = com.orbps.otherFunction.contractQuery.contractStateBackQuery.queryInfoList[i];
			var salesBranchNo = queryInfo.salesBranchNo;
			var applNo = queryInfo.applNo;
			var applName = queryInfo.applName;
			var preApplState = queryInfo.preApplState;
			var cnBackReason = "";
			if(undefined !== queryInfo.cnBackReason && null !== queryInfo.cnBackReason){
				cnBackReason = queryInfo.cnBackReason;
			}
			var applState = queryInfo.applState;
			var cnDate = queryInfo.cnDate;
			var cnBranchNo = "";
			if(undefined !== queryInfo.cnBranchNo && null !== queryInfo.cnBranchNo){
				cnBranchNo = queryInfo.cnBranchNo;
			}
			var cnNo = "";
			if(undefined !== queryInfo.cnNo && null !== queryInfo.cnNo){
				cnNo = queryInfo.cnNo;
			}
			var cnName = "";
			if(undefined !== queryInfo.cnName && null !== queryInfo.cnName){
				cnName = queryInfo.cnName;
			}
			$('#approvalTab')
					.find("tbody")
					.append(
							"<tr><td style='width: 20px;'>"
									+ salesBranchNo
									+ "</td><td style='width: 100px;'>"
									+ applNo
									+ "</td><td style='width: 100px;'>"
									+ applName
									+ "</td><td style='width: 100px;'>"
									+ preApplState
									+ "</td><td style='width: 100px;'>"
									+ cnBackReason
									+ "</td><td style='width: 100px;'>"
									+ applState
									+ "</td><td style='width: 100px;'>"
									+ cnDate
									+ "</td><td style='width: 100px;'>"
									+ cnBranchNo
									+ "</td><td style='width: 100px;'>"
									+ cnNo
									+ "</td><td style='width: 100px;'>"
									+ cnName
									+ "</td></tr>");
		}
	} else {
		$('#approvalTab').find("tbody").append(
				"<tr><td colspan='10' align='center'>无记录</td></tr>");
	}
}

$("#btnExport").click(function(){
	
	//查询条件
	var ApprovalVo = com.orbps.otherFunction.contractQuery.contractStateBackQuery.contractStateBack.serializeObject();
	lion.util.postjson("/orbps/web/orbps/otherfunction/revoke/backAll",ApprovalVo,function(data){
		if(data.length>10000){
			lion.util.info("提示:数据导出量超过10000条，请重新录入查询条件进行分批查询！");
			return false;
		}
		lion.util.info("提示","导出结果以当前查询条件为准，开始导出,请稍等…");
		$('#approvalTabExport').find("tbody").empty();
		for (var l = 0; l < data.length; l++) {
			var array_element = data[l];
			$('#approvalTabExport').find("tbody")
				.append("<tr><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.salesBranchNo)
						+ "</td><td style=\"mso-number-format:'\@';\">"
						+ com.orbps.publicExport.checkUndefined(array_element.applNo)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.applName)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.preApplState)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.cnBackReason)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.applState)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.cnDate)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.cnBranchNo)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.cnNo)
						+ "</td><td >"
						+ com.orbps.publicExport.checkUndefined(array_element.cnName)
						+ "</td></tr>");
		}
		com.orbps.publicExport.outExcle("approvalTabExport");
		lion.util.info("导出完成！");
	});
});
