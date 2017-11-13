
$(function() {
	//批作业批量启动
	$("#taskRegStartBtn").click(function(){
		lion.util.info("批作业批量启动中,请稍候。。。");
		lion.util.postjson("/orbps/web/orbps/otherFunction/branchControl/batchRestart",null,com.orbps.otherFunction.bacthAppManage.batchStart);
	});
	com.orbps.otherFunction.bacthAppManage.batchStart = function(data,args){
		if(lion.util.isEmpty(data)){
			lion.util.info("批作业启动失败");
			return false;
		}
		if("0" === data.errCode){
			lion.util.info(data.errMsg);
		}
	}
	
	//批作业批量停止
	$("#taskRegStopBtn").click(function(){
		lion.util.info("批作业批量停止中,请稍候。。。");
		lion.util.postjson("/orbps/web/orbps/otherFunction/branchControl/batchStop", null,com.orbps.otherFunction.bacthAppManage.batchStop);
	});
	
	com.orbps.otherFunction.bacthAppManage.batchStop = function(data,args){
		if(lion.util.isEmpty(data)){
			lion.util.info("批作业停止失败");
			return false;
		}
		if("0" === data.errCode){
			lion.util.info(data.errMsg);
		}
	}
	
	//批作业启停结果查询
	$("#batchStatQueryBtn").click(function(){
		lion.util.postjson("/orbps/web/orbps/otherFunction/branchControl/batchStatQuery", null,com.orbps.otherFunction.bacthAppManage.batchQuery);
	});

	com.orbps.otherFunction.bacthAppManage.batchQuery = function(data,args){
		if(lion.util.isEmpty(data)){
			lion.util.info("批作业停止状态查询失败");
			return false;
		}
		
		lion.util.info(data.errMsg);
	}
});
