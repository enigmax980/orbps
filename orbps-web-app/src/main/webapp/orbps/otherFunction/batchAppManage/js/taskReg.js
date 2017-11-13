/* 模块级名空间 */
com.orbps.otherFunction = {};
com.orbps.otherFunction.bacthAppManage = {};
/* 页面级命名空间 */
com.orbps.otherFunction.bacthAppManage.taskReg = {};
/* 模块定义 */
com.orbps.otherFunction.bacthAppManage.taskReg.addDialog = $("#errLogModal");
/* datagrids */
com.orbps.otherFunction.bacthAppManage.taskReg.table = $('#taskRegDatagridsId');
/* datagrids查询条件表单 */
com.orbps.otherFunction.bacthAppManage.taskReg.queryform = $('#taskRegQueryform');
com.orbps.otherFunction.bacthAppManage.taskReg.taskId = {};
com.orbps.otherFunction.bacthAppManage.taskReg.formatterClickFtn = function(data){
	$("#taskRegId").hide();
	$("#taskExecutionId").show();
	$("#taskExecutionId").empty();
	com.orbps.otherFunction.bacthAppManage.taskReg.taskId.taskId = data;
	$("#taskExecutionId").load("/orbps/orbps/otherFunction/batchAppManage/html/taskExecution.html", function(){
		
	});
}
com.orbps.otherFunction.bacthAppManage.taskReg.formatterFtn = function(data,type,full){
	
	if(full.dataDealStatus === "01"){
		return data;
	}
	
	return '<a href="javascript:void(0)" id="'+data+'" onclick="com.orbps.otherFunction.bacthAppManage.taskReg.formatterClickFtn(this.id)">'+data+'</a>';
}

$(function() {
	// 时间空件初始化
/*	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});*/
	$(".form_datetime").datetimepicker({
	    autoclose: true,
	    isRTL: "left",
	    pickerPosition: "bottom-left",
	    language: "zh-CN"
	});
	// combo组件初始化
	$("*").comboInitLoad();

	// datagrid组件初始化
	com.orbps.otherFunction.bacthAppManage.taskReg.table.datagridsInitLoadById();

	$("#taskRegQueryBtn").click(function() {
		// 添加查询参数
		com.orbps.otherFunction.bacthAppManage.taskReg.table.datagrids({
			querydata : com.orbps.otherFunction.bacthAppManage.taskReg.queryform.serializeObject()
		});
		// 重新加载数据
		com.orbps.otherFunction.bacthAppManage.taskReg.table.datagrids('reload');
	});
	
	//进入批作业控制模块
	$("#taskRegControlBtn").click(function(){
		com.orbps.otherFunction.bacthAppManage.taskReg.addDialog.empty();
		com.orbps.otherFunction.bacthAppManage.taskReg.addDialog.load(
			"/orbps/orbps/otherFunction/batchAppManage/html/batchControl.html",
			function() {
				$(this).modal('toggle');
				$(this).comboInitLoad();
		});
	});
});
