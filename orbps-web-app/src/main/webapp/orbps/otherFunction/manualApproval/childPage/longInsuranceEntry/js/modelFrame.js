com.orbps.contractEntry.longInsuranceEntry.pollicitation=$("#pollicitation-beneFiciary")

$(function() {	
	// combo组件初始化
	$("*").comboInitLoad();
	//增加表格
	$("#longInsuranceEntryForm-beneFiciary #btnAdd").click(function() {
		alert(9999);
		$("#fbp-editDataGrid-one").editDatagrids("addRows");
		alert(0000);
		return false;
	});
	
	//删除表格
	$("#longInsuranceEntryForm-beneFiciary #btnDel").click(function () {  
		$("#fbp-editDataGrid-one").editDatagrids("delRows");
		return false;
    }); 
});

