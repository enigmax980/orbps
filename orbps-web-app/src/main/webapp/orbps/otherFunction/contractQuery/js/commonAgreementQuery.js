com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery={};
com.orbps.otherFunction.contractQuery.ruleQueryForm = $("#ruleQueryForm");
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
    var commonAgreementRuleVo = com.orbps.otherFunction.contractQuery.ruleQueryForm.serializeObject();
    var startDate = commonAgreementRuleVo.inForceDate;
    var endDate = commonAgreementRuleVo.termDate;
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
	//alert("sss"+JSON.stringify(commonAgreementRuleVo));
    // 添加查询参数
    $("#commonAgreementTb").datagrids({
        querydata : commonAgreementRuleVo            
    });
    // 重新加载数据   
    $("#commonAgreementTb").datagrids('reload');
    setTimeout( function(){
    	// 重新加载数据
    	var getdata = $("#commonAgreementTb").datagrids('getdata');
    	if(getdata.length<=0){
    		lion.util.info("提示","查询数据为空,请重新输入查询条件")
    	}
    },500);
});
$("#btnExport").click(function(){
	 var commonAgreementRuleVo = com.orbps.otherFunction.contractQuery.ruleQueryForm.serializeObject();
	    var startDate = commonAgreementRuleVo.inForceDate;
	    var endDate = commonAgreementRuleVo.termDate;
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
	    lion.util.postjson('/orbps/web/orbps/otherfunction/poolQuery/outExcel',commonAgreementRuleVo,com.orbps.otherFunction.contractQuery.successQueryDetail,null,null);
	    lion.util.info("提示","导出结果以当前查询条件为准，正在导出，请稍后…");
});
com.orbps.otherFunction.contractQuery.successQueryDetail = function (data,arg){
//	alert(JSON.stringify(data));
	$('#commonAgreementTbs').find("tbody").empty();
	for (var l = 0; l < data.length; l++) {
		var array_element = data[l];
		$('#commonAgreementTbs').find("tbody")
		.append("<tr><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.pclkBranchNo)
				+ "</td><td style=\"mso-number-format:'\@';\">"
				+ com.orbps.publicExport.checkUndefined(array_element.agreementNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.agreementName)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.companyName)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.coinsurType)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.coinsurResponsePct)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.inForceDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.termDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.clerkNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.vclkNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.signDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.remark)
//				+ "</td><td >"
//				+ com.orbps.publicExport.checkUndefined(array_element.pclkName)
//				+ "</td><td >"
//				+ com.orbps.publicExport.checkUndefined(array_element.procDate)
				+ "</td></tr>");
	}
	com.orbps.publicExport.outExcle("commonAgreementTbs");
}
