/* 页面级命名空间 */
com.orbps.otherFunction.bacthAppManage.errLogTable = {};
com.orbps.otherFunction.bacthAppManage.errLogTable.num = 0;
com.orbps.otherFunction.bacthAppManage.errLogTable.formatterFtn = function(data, type, full){
	if(data == undefined){
		data = "";
	}
	var str = "<span style='background-color:transparent;cursor:pointer;display:block;overflow:hidden;white-space:nowrap;text-overflow:ellipsis;' data-toggle='tooltip' data-placement='top' title='"+data+"'>"+data+"</span>";
	return str;
}

com.orbps.otherFunction.bacthAppManage.errLogTable.taskErrLogTable = $("#taskErrLogTableId");
$(function() {
	com.orbps.otherFunction.bacthAppManage.errLogTable.taskErrLogTable.attr("data-querydata", JSON.stringify(com.orbps.otherFunction.bacthAppManage.taskExecution.taskExecutionId));
	// datagrid组件初始化
	com.orbps.otherFunction.bacthAppManage.errLogTable.taskErrLogTable.datagridsInitLoadById();
});