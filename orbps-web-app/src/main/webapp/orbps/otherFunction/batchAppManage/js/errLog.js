/* 页面级命名空间 */
com.orbps.otherFunction = {};
com.orbps.otherFunction.bacthAppManage = {};
com.orbps.otherFunction.bacthAppManage.errLog = {};
com.orbps.otherFunction.bacthAppManage.errLog.num = 0;
$(function() {
	var data = com.orbps.otherFunction.bacthAppManage.taskExecution.errLogMsgs;
	$("#batchErrLogForm").fill(data[com.orbps.otherFunction.bacthAppManage.errLog.num]);
	
	$("#errLogPreviousPage").click(function(){
		if(com.orbps.otherFunction.bacthAppManage.errLog.num == 0){
			com.orbps.otherFunction.bacthAppManage.errLog.num = data.length-1;
		}else{
			--com.orbps.otherFunction.bacthAppManage.errLog.num;
		}
		$("#batchErrLogForm").fill(data[com.orbps.otherFunction.bacthAppManage.errLog.num]);
	});
	
	$("#errLogNextPage").click(function(){
		if(com.orbps.otherFunction.bacthAppManage.errLog.num == data.length-1){
			com.orbps.otherFunction.bacthAppManage.errLog.num = 0;
		}else{
			++com.orbps.otherFunction.bacthAppManage.errLog.num;
		}
		$("#batchErrLogForm").fill(data[com.orbps.otherFunction.bacthAppManage.errLog.num]);
	});
});