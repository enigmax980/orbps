/* 页面级命名空间 */
com.orbps.otherFunction.bacthAppManage.taskExecution = {};
/* datagrids */
com.orbps.otherFunction.bacthAppManage.taskExecution.table = $('#taskExecutionDatagridsId');
/* datagrids查询条件表单 */
com.orbps.otherFunction.bacthAppManage.taskExecution.queryform = $('#taskExecutionQueryform');
com.orbps.otherFunction.bacthAppManage.taskExecution.errLogModal = $('#errLogModal');
com.orbps.otherFunction.bacthAppManage.taskExecution.taskExecutionId = {};
com.orbps.otherFunction.bacthAppManage.taskExecution.formatterLogFtn = function(taskExecutionId){
	com.orbps.otherFunction.bacthAppManage.taskExecution.taskExecutionId.taskExecutionId = taskExecutionId;
	com.orbps.otherFunction.bacthAppManage.taskExecution.errLogModal.load("/orbps/orbps/otherFunction/batchAppManage/html/errLogTable.html",function(){
		$(this).find('.modal-header h4 span').text('错误信息');
		$(this).modal('toggle');
	});
	/*lion.util.post("/example/web/batch/manage/logmsg/"+taskExecutionId, "", function(data, args){
		if(lion.util.isEmpty(data)){
			lion.util.info("提示:", "无错误信息。");
			return ;
		}
		com.orbps.otherFunction.bacthAppManage.taskExecution.errLogMsgs = data;
		com.orbps.otherFunction.bacthAppManage.taskExecution.errLogModal.empty();
		com.orbps.otherFunction.bacthAppManage.taskExecution.errLogModal.load("/example/example/batch/html/errLog.html",function(){
			$(this).find('.modal-header h4 span').text('错误信息，共'+data.length+'条');
			$(this).modal('toggle');
		});
	});*/
}
com.orbps.otherFunction.bacthAppManage.taskExecution.formatterRepeatFtn = function(taskExecutionId){
	lion.util.post("/orbps/web/batch/manage/restart/"+taskExecutionId, "", function(data, args){
		lion.util.info("提示：", data);
	});
}
com.orbps.otherFunction.bacthAppManage.taskExecution.formatterFtn = function(data,type,full){
	var btnList = "";
	if(full.dataDealStatus === "03"){
		btnList += "<a href='javascript:void(0)' name='"+full.taskExecutionId+"' class='btn btn-sm green' onclick='com.orbps.otherFunction.bacthAppManage.taskExecution.formatterLogFtn(this.name)'>日志查询</a>";
		if(full.isRestart){
			btnList += "&nbsp;&nbsp;<a href='javascript:void(0)' name='"+full.taskExecutionId+"' class='btn btn-sm green' onclick='com.orbps.otherFunction.bacthAppManage.taskExecution.formatterRepeatFtn(this.name)'>重启</a>";
		}		
	}
	return btnList;
}

$(function() {
	// 时间空件初始化
	$(".form_datetime").datetimepicker({
	    autoclose: true,
	    isRTL: "left",
	    pickerPosition: "bottom-right",
	    language: "zh-CN"
	});
	// combo组件初始化
	$("*").comboInitLoad();

	com.orbps.otherFunction.bacthAppManage.taskExecution.table.attr("data-querydata", JSON.stringify(com.orbps.otherFunction.bacthAppManage.taskReg.taskId));
	com.orbps.otherFunction.bacthAppManage.taskExecution.queryform.find("#taskId").val(com.orbps.otherFunction.bacthAppManage.taskReg.taskId.taskId);
	
	// datagrid组件初始化
	com.orbps.otherFunction.bacthAppManage.taskExecution.table.datagridsInitLoadById();

	$("#taskExecutionQueryBtn").click(function() {
		// 添加查询参数
		com.orbps.otherFunction.bacthAppManage.taskExecution.table.datagrids({
			querydata : com.orbps.otherFunction.bacthAppManage.taskExecution.queryform.serializeObject()
		});
		// 重新加载数据
		com.orbps.otherFunction.bacthAppManage.taskExecution.table.datagrids('reload');
	});
	
	$("#taskExecutionBackBtn").click(function(){
		$("#taskRegId").show();
		$("#taskExecutionId").hide();
	});
	
	$("#taskExecutionRepeatBtn").click(function(){
		
	});
});
