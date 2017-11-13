// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.offlineList命名空间
com.orbps.contractEntry.archivesListimport = {};
// uploadListForm id
com.orbps.contractEntry.archivesListimport.btnModel = $("#uploadListForm");
//新建cntrType全局变量
com.orbps.contractEntry.archivesListimport.cntrType = {};
//导入进度条展示，全局变量
com.orbps.contractEntry.archivesListimport.pct = 0;
com.orbps.contractEntry.archivesListimport.clearFlag = 0;
com.orbps.contractEntry.archivesListimport.errExportFlag;
com.orbps.contractEntry.archivesListimport.data;

//查询任务ID回调函数
com.orbps.contractEntry.archivesListimport.successQueryDetail = function (data,args){
	com.orbps.contractEntry.archivesListimport.data = data;
	if(data.retCode === "0"){
		lion.util.info("提示",data.errMsg);
		return false;
	}
	//判断团单清单标记是不是档案清单，如果不是提示不允许档案清单补导入
	if("A" !== data.lstProcType){
		lion.util.info("提示","该保单的团体清单标记不是档案清单不允许档案清单补导入！");
		return false;
	}
	$("#applName").val(data.applName);
	$("#applNum").val(data.applNum);
	$("#applDate").val(data.applDate);
	com.orbps.contractEntry.archivesListimport.cntrType = data.cntrType;
}

//清单导入进度查询功能
com.orbps.contractEntry.archivesListimport.showImp = function(){
	var applNo = $("#applNo").val();
	lion.util.postjson("/orbps/web/orbps/contractEntry/offlineList/progressQuery",applNo,com.orbps.contractEntry.archivesListimport.progressQuery,null,null);
}

com.orbps.contractEntry.archivesListimport.progressQuery = function(data,args){
	if(null === data){
		return false;
	}else{
		//进度展示
		com.orbps.contractEntry.archivesListimport.errExportFlag = data.taskState;
		var sum = data.thisIpsnNum;
		var imp = data.thisImportNum;
		var err = data.thisErrorNum;
		$("#spanPct").remove();
		com.orbps.contractEntry.archivesListimport.pct=parseInt(((imp+err)/sum)*100);
		$("#progressBar").attr("style","width: "+com.orbps.contractEntry.archivesListimport.pct+"%;");
		$("#progressBar").append("<span id='spanPct'>"+com.orbps.contractEntry.archivesListimport.pct+"%</span>");
		//页面赋值
		$("#listSumNum").val(data.insuredNum);
		$("#listImpNum").val(data.importNum);
		$("#thisListSumNum").val(sum);
		$("#thisListImpNum").val(imp);
		$("#thisListErrNum").val(err);
		if("C" === com.orbps.contractEntry.archivesListimport.errExportFlag){
			lion.util.info("清单导入完成");
			clearInterval(com.orbps.contractEntry.archivesListimport.clearFlag);
		}else if("E" === com.orbps.contractEntry.archivesListimport.errExportFlag){
			lion.util.info("清单导入错误，请导出并修复错误文件");
			clearInterval(com.orbps.contractEntry.archivesListimport.clearFlag)
		}
	}
}

$(function() {
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
    // 文件上传参数设置
	$("#fileupload").fileinput({
        'allowedFileExtensions' : ['xlsm', 'xlt','xls','xlsx','csv'],
        'showUpload':false,
        'showPreview':false,
        'showCaption':true,
        'browseClass':'btn btn-success',
    });
	
	//查询确认投保单信息时权限控制检查，保单省级机构代码是否与当前操作员省级机构代码相同
	$("#btnQuery").click(function(){
		var applNo = $("#applNo").val();
		if("" === applNo){
			lion.util.info("请输入投保单号");
			return false;
		}
		lion.util
		.postjson(
				'/orbps/web/orbps/public/branchControl/cmpProvinceBranch',
				applNo,
				com.orbps.contractEntry.archivesListimport.cmpProvinceBranch);
	});
	//查询确认投保单信息时权限控制检查回调函数
	com.orbps.contractEntry.archivesListimport.cmpProvinceBranch= function(
			data, arg) {
		var applNo = $("#applNo").val();
		if(data.retCode==="0"){
			lion.util.info("提示", "失败原因：" + data.errMsg);
		}else{
			//查询确认投保单信息
			lion.util.postjson("/orbps/web/orbps/contractEntry/search/applQuery",applNo,com.orbps.contractEntry.archivesListimport.successQueryDetail);
		}
	}
	
    // 文件上传点击事件
    $('#btnImport').click(function(){
    	var applNo = $("#applNo").val();
    	if("" === $("#applNo").val()){
			lion.util.info("请输入投保单号");
			return false;
		}
		lion.util
		.postjson(
				'/orbps/web/orbps/public/branchControl/cmpProvinceBranch',
				applNo,
				com.orbps.contractEntry.archivesListimport.cmpProvinceBranch2);
		
    });
    com.orbps.contractEntry.archivesListimport.cmpProvinceBranch2 = function(
			data, arg) {
		if(data.retCode==="0"){
			lion.util.info("提示", "失败原因：" + data.errMsg);
		}else{
			if(com.orbps.contractEntry.archivesListimport.data.retCode === "0"){
	    		lion.util.info("提示",com.orbps.contractEntry.archivesListimport.data.errMsg);
	    		return false;
	    	}
	    	if("A" !== com.orbps.contractEntry.archivesListimport.data.lstProcType){
	    		lion.util.info("提示","该保单的团体清单标记不是档案清单不允许档案清单补导入！");
	    		return false;
	    	}
	    	if(!confirm("导入前请先确认错误清单已经导出，避免错误文件被覆盖。\n是否要继续导入？")){
	    		return false;
	    	}
	    	var path = $('#fileupload').val(); 
	    	var formData = new FormData($('#uploadListForm')[0]);
	    	if("" === path){
	    		lion.util.info("请选择要上传的文件");
	    		return false;
	    	}
	    	var status = "";
	    	if(document.getElementById("continueImport").checked){
	    		status = "2";
	    	}else{
	    		status = "1";
	    	}
	    	var applNo = status;
	    	applNo = applNo + "," +$("#applNo").val();
	    	//taskid传空
	    	applNo = applNo + "," +"0";
//	    	applNo = applNo + ",14234324324,1234";
	    	lion.util.info("提示","清单开始导入.........");
	    	//显示清单导入进度
	    	clearInterval(com.orbps.contractEntry.archivesListimport.clearFlag);
	    	com.orbps.contractEntry.archivesListimport.clearFlag = setInterval("com.orbps.contractEntry.archivesListimport.showImp()",10000);
			lion.util.postfile("/orbps/web/orbps/contractEntry/offlineList/upload/"+applNo,formData,function(data, args){
				if("0"==data.retCode){
					lion.util.info("提示",data.errMsg);
					clearInterval(com.orbps.contractEntry.archivesListimport.clearFlag);
				}
			});
		}
	}

    // 导出差错
    $('#btnExportError').click(function(){
    	if("" === $("#applNo").val()){
			lion.util.info("请输入投保单号");
			return false;
		}
    	if("E" !== com.orbps.contractEntry.archivesListimport.errExportFlag && "C" !== com.orbps.contractEntry.archivesListimport.errExportFlag){
    		lion.util.info("档案清单正在导入，请等待档案清单导入完成后，再导出错误文件");
    		return false;
    	}
	    applNo = $("#applNo").val();
    	lion.util.postjson(
                '/orbps/web/exp/file/exists',
                applNo,
                com.orbps.contractEntry.archivesListimport.successExists,
                null,
                null);
    });
    
    //停止导清单任务
    $("#btnStopImport").click(function(){
    	var applNo = $("#applNo").val();
		lion.util.info("提示","清单导入停止操作正在进行。。。");
    	lion.util.postjson(
    			'/orbps/web/orbps/contractEntry/offlineList/stopImport',
    			applNo,
    			com.orbps.contractEntry.archivesListimport.stopImport,
    			null,
    			null);
    });
    
    // 导出团单模板
    $('#btnGrpDownload').click(function(){
    	window.location.href="/orbps/web/orbps/contractEntry/offlineList/downloadTemplate/g";
    });
    
    // 导出清汇模板
    $('#btnSgDownload').click(function(){
    	window.location.href="/orbps/web/orbps/contractEntry/offlineList/downloadTemplate/s";
    });
    
    // 全部录入完成		
    $('#btnAllCommit').click(function(){
    	if("" === $("#applNo").val()){
			lion.util.info("请输入投保单号");
			return false;
		}
    	applNo = $("#applNo").val();
    	//taskid传空
    	applNo = applNo + "," +"";
    	//alert("投保单号"+applNo);
    	var cntrType = com.orbps.contractEntry.archivesListimport.cntrType;
    	var str;
    	if("G"===cntrType){
    		str = "03";//团单的清单导入
    	}else{
    		str = "08";//清汇的清单导入
    	}
    	applNo = applNo +","+str;
    	//alert("拼接的字符串，投保单号+taskId+保单类型"+applNo);
    	lion.util.postjson(
                '/orbps/web/orbps/contractEntry/offlineList/submitAll',
                applNo,
                com.orbps.contractEntry.archivesListimport.successSubmitAll,
                null,
                null);
    });
});
// 按回车相当于tab功能(键盘按键触发事件)
$("input:text").keypress(function (e) {
    if (e.which == 13) {// 判断所按是否回车键  
        var inputs = $("input:text "); // 获取表单中的所有输入框  
        var selects = $("select"); // 获取表单中的所有输入框  
        var idx = inputs.index(this); // 获取当前焦点输入框所处的位置  
        inputs[idx + 1].focus(); // 设置焦点  
        inputs[idx + 1].select(); // 选中文字  
        return false; // 取消默认的提交行为  
    }
});

//导出被保人清单
$("#btnExport").click(function(){
	if("" === $("#applNo").val()){
		lion.util.info("请输入投保单号");
		return false;
	}
	window.location.href="/orbps/web/orbps/contractReview/offlineList/download/"+$("#applNo").val();
});

//错误清单导出回调函数
com.orbps.contractEntry.archivesListimport.successExists = function (data,arg){
	if (data === "1"){
		window.location.href="/orbps/web/exp/file/download/"+applNo;
	}else{
		lion.util.info("提示", "导出失败，失败原因：错误清单文件不存在");
	}
}

//停止导清单任务回调函数
com.orbps.contractEntry.archivesListimport.stopImport = function(data,arg){
	if("1" === data.retCode){
		lion.util.info("正在停止清单导入批作业，请稍等。。。");
	}else{
		lion.util.info(data.errMsg);
	}
}

// 全部录入完成回调函数
com.orbps.contractEntry.archivesListimport.successSubmitAll = function (data,arg){
	 if (data.retCode==="1"){
	        lion.util.info("提示", "全部录入完成操作成功");
	    }else{
	        lion.util.info("提示", "全部录入失败，失败原因："+data.errMsg);
	    }
}

//日期回显long转String
Date.prototype.format = function(fmt)   { 
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}