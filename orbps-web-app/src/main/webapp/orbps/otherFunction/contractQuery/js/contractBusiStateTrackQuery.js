com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery={};
com.orbps.otherFunction.contractQuery.contractBusiStateTrackQuery={};
com.orbps.otherFunction.contractQuery.contractBusiStateTrackQuery.contractStateBackForm = $("#contractStateBackForm");
$(function (){
	// combo组件初始化
	$("*").comboInitLoad();
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
});
//点击查询
$("#btnQuery").click(function(){
	//查询条件
    var commonAgreementRuleVo = com.orbps.otherFunction.contractQuery.contractBusiStateTrackQuery.contractStateBackForm.serializeObject();
    var startDate = commonAgreementRuleVo.startDate;
    var endDate = commonAgreementRuleVo.endDate;
    if(startDate !== "" && endDate !== ""){
    	 var arr1 = startDate.split("-");   
    	    var sdate = new Date(arr1[0],parseInt(arr1[1])-1,arr1[2]);
    	    var arr2 = endDate.split("-");   
    	    var edate = new Date(arr2[0],parseInt(arr2[1])-1,arr2[2]);
    	    if(sdate.getTime()>edate.getTime()){
    	    	lion.util.info("警告","请查看起止日期是否选择正确！");
    			return false;
    	    }
    }
    // 添加查询参数
    $("#busiStateTab").datagrids({
        querydata : commonAgreementRuleVo            
    });
    // 重新加载数据   
    $("#busiStateTab").datagrids('reload');
    setTimeout( function(){
    	// 重新加载数据
    	var getdata = $("#busiStateTab").datagrids('getdata');
    	if(getdata.length<=0){
    		lion.util.info("提示","查询数据为空,请重新输入查询条件")
    	}
    },500);
});
$("#btnExport").click(function(){
	var commonAgreementRuleVo = com.orbps.otherFunction.contractQuery.contractBusiStateTrackQuery.contractStateBackForm.serializeObject();
	var startDate = commonAgreementRuleVo.startDate;
    var endDate = commonAgreementRuleVo.endDate;
    if(startDate !== "" && endDate !== ""){
    	 var arr1 = startDate.split("-");   
    	    var sdate = new Date(arr1[0],parseInt(arr1[1])-1,arr1[2]);
    	    var arr2 = endDate.split("-");   
    	    var edate = new Date(arr2[0],parseInt(arr2[1])-1,arr2[2]);
    	    if(sdate.getTime()>edate.getTime()){
    	    	lion.util.info("警告","请查看起止日期是否选择正确！");
    			return false;
    	    }
    }
	    lion.util.postjson('/orbps/web/orbps/otherfunction/contractQuery/queryAll',commonAgreementRuleVo,com.orbps.otherFunction.contractQuery.contractBusiStateTrackQuery.successQueryDetail,null,null);
	    lion.util.info("提示","导出结果以当前查询条件为准，正在导出，请稍后…");
});
com.orbps.otherFunction.contractQuery.contractBusiStateTrackQuery.successQueryDetail = function (data,arg){
	$('#busiStateTabExport').find("tbody").empty();
	for (var l = 0; l < data.length; l++) {
		var array_element = data[l];
		$('#busiStateTabExport').find("tbody")
		.append("<tr><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.bizzBranchNo)
				+ "</td><td style=\"mso-number-format:'\@';\">"
				+ com.orbps.publicExport.checkUndefined(array_element.applNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.applName)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.bizzNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.bizzName)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.bizzDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.taskPresentStates)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.contractBusiForm)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.polCode)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.payForm)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.totalPremium)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.salesBranchNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.salesChannel)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.salesNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.salesName)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.busiType)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.detailBusiCondition)
				+ "</td></tr>");
	}
	com.orbps.publicExport.outExcle("busiStateTabExport");
}
