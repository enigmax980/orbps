com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery={};
com.orbps.otherFunction.contractQuery.contractRevokeQuery={};
com.orbps.otherFunction.contractQuery.contractRevokeQuery.contractRevoke= $("#contractRevokeForm");
$(function (){
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// select框初始化
	$("*").comboInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
});
//点击查询
$("#btnQuery").click(function(){
	//查询条件
    var ApprovalVo = com.orbps.otherFunction.contractQuery.contractRevokeQuery.contractRevoke.serializeObject();
    // 添加查询参数
    $("#approvalTab").datagrids({
        querydata : ApprovalVo            
    });
    // 重新加载数据   
    $("#approvalTab").datagrids('reload');
    setTimeout( function(){
    	// 重新加载数据
    	var getdata = $("#approvalTab").datagrids('getdata');
    	if(getdata.length<=0){
    		lion.util.info("提示","查询数据为空,请重新输入查询条件")
    	}
    },1500);
});
$("#btnExport").click(function(){
	
	//查询条件
	var ApprovalVo = com.orbps.otherFunction.contractQuery.contractRevokeQuery.contractRevoke.serializeObject();
	lion.util.postjson("/orbps/web/orbps/otherfunction/revoke/searchAll",ApprovalVo,function(data){
		$('#approvalTabExcel').find("tbody").empty();
		if(data.length>10000){
			lion.util.info("提示:数据导出量超过10000条，请重新录入查询条件进行分批查询！");
			return false;
		}
		lion.util.info("提示","导出结果以当前查询条件为准，开始导出,请稍等…");
		for (var l = 0; l < data.length; l++) {
			var array_element = data[l];
			$('#approvalTabExcel').find("tbody")
			.append("<tr><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.salesBranchNo)
					+ "</td><td style=\"mso-number-format:'\@';\">"
					+ com.orbps.publicExport.checkUndefined(array_element.applNo)
					+ "</td><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.applName)
					+ "</td><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.pclkBranchNo)
					+ "</td><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.pclkNo)
					+ "</td><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.pclkName)
					+ "</td><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.pclkDate)
					+ "</td><td >"
					+ com.orbps.publicExport.checkUndefined(array_element.applState)
					+ "</td></tr>");
		}
		com.orbps.publicExport.outExcle("approvalTabExcel");
		lion.util.info("导出完成！");
	});
});
