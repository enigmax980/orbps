com.orbps.otherFunction.contractCorrected.childPage.cardEntry.bnfrInfoForm=$("#bnfrInfoForm");

$(function(){
	//datagrid控件初始化表格 与bootstrap的表格样式不兼容
	// datagrid组件初始化
//	$("*").datagridsInitLoad();
	$("#ipsnListInfo").editDatagridsLoadById();
	
	//日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});	
	
	//增加表格
	$("#btnAdd").click(function() {
		$("#ipsnListInfo").editDatagrids("addRows");
	});
	
	//删除表格
	$("#btnDelete").click(function () {  
		$("#ipsnListInfo").editDatagrids("delRows");
    }); 
});