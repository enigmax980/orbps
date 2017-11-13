com.orbps.contractEntry.cardEntry.applInfoForm=$("#applInfoForm");

$(function(){
	//datagrid控件初始化表格 与bootstrap的表格样式不兼容
//	$('#applListTab').datagrid();
//	$("#treegridId").datagrid();
	//日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});	
});