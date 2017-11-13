/* 新建命名空间 - 包级 */
com.orbps.sendReceipt = {};
/* 新建命名空间 - 页面级 */
com.orbps.sendReceipt.importw = {};
/* 新建命名空间 - applicantSaveForm表单 */
com.orbps.sendReceipt.importw.ruleQueryForm = $("#ruleQueryForm");
com.orbps.sendReceipt.importw.successQuery;
$(function (){
	//alert("success");
	// combo组件初始化
	$("*").comboInitLoad();
	// datagrid组件初始化
	$("*").datagridsInitLoad();
   });		
//日期初始化插件
$(".date-picker").datepicker({
	autoclose : true,
	language : 'zh-CN'
});
//契约信息查询
$("#ruleQueryForm #query").click(function() {
	//alert(1);
		var receiptCvTaskVo = com.orbps.sendReceipt.importw.ruleQueryForm.serializeObject();
		//alert(JSON.stringify(receiptCvTaskVo));
		var startDate = receiptCvTaskVo.startDate;
	    var endDate = receiptCvTaskVo.endDate;
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
		$("#apTaskListTab").datagrids({
			querydata : receiptCvTaskVo
		});
		// 重新加载数据
		$("#apTaskListTab").datagrids('reload');
		setTimeout(function (){
			var getdata = $("#apTaskListTab").datagrids("getdata");
			//alert(JSON.stringify(getdata));
			if (!getdata.length>0){
				lion.util.info("提示", "无查询记录，请输入正确的查询条件！");
			}
		},500);
});
$("#btnExport").click(function(){
	var receiptCvTaskVo = com.orbps.sendReceipt.importw.ruleQueryForm.serializeObject();
	var startDate = receiptCvTaskVo.startDate;
    var endDate = receiptCvTaskVo.endDate;
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
	lion.util.postjson('/orbps/web/orbps/sendreceipt/CvTask/outExcel',receiptCvTaskVo,com.orbps.sendReceipt.successQueryDetail,null,null);
	lion.util.info("提示","导出结果以当前查询条件为准，正在导出，请稍后…");
});
com.orbps.sendReceipt.successQueryDetail = function (data,arg){
//	alert(JSON.stringify(data));
	$('#apTaskListTabs').find("tbody").empty();
	for (var l = 0; l < data.length; l++) {
		var array_element = data[l];
		$('#apTaskListTabs').find("tbody")
		.append("<tr><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.salesBranchNo)
				+ "</td><td style=\"mso-number-format:'\@';\">"
				+ com.orbps.publicExport.checkUndefined(array_element.applNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.sgName)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.applDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.printDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.signDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.cntrSendType)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.respDate)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.salesBranchNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.oclkClerkNo)
				+ "</td><td >"
				+ com.orbps.publicExport.checkUndefined(array_element.oclkClerkName)
				+ "</td></tr>");
	}
	com.orbps.publicExport.outExcle("apTaskListTabs");
	//method1("apTaskListTabs");
}
