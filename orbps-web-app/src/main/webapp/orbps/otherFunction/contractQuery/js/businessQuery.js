com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery ={};
com.orbps.otherFunction.contractQuery.mgrBranchList = new Array();
com.orbps.otherFunction.contractQuery.cntrInfoQueryForm = $("#cntrInfoQueryForm");
com.orbps.otherFunction.contractQuery.reloadmgrBranchTable;
//初始化函数
$(function() {
	
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	
	// combo组件初始化
    $("*").comboInitLoad();
    
    // 初始化只有投保单号可写
    $('input,select,textarea',$('form[name="cntrInfoQueryForm"]')).prop('disabled',true);
	$("#applNo").attr("disabled",false);
	
	// 查询点击按钮
	$("#query").click(function () {
		var receiptCvTaskVo = com.orbps.otherFunction.contractQuery.cntrInfoQueryForm.serializeObject();
		// 添加查询参数
		$("#mgrBranchList").datagrids({
			querydata : receiptCvTaskVo
		});
		// 重新加载数据
		$("#mgrBranchList").datagrids('reload');
		setTimeout(function (){
			var getdata = $("#mgrBranchList").datagrids("getdata");
			if (getdata.length<=0){
				lion.util.info("提示", "无查询记录，请输入正确的查询条件！");
			}
		},500);
	});
	
	//按照radio显示查询条件
	$("input[name=visitDate]").click(function(){
		switch($("input[name=visitDate]:checked").attr("id")){
	 		case "applNoQuery":
    	 		$("#newApplState").combo("refresh");
	 		    $('input,textarea',$('form[name="cntrInfoQueryForm"]')).val("");
     			$('input,select,textarea',$('form[name="cntrInfoQueryForm"]')).prop('disabled',true);
     			$("#applNo").attr("disabled",false);
    	 		break;
     		case "conditionQuery":
     			$('input,select,textarea',$('form[name="cntrInfoQueryForm"]')).prop('disabled',false);
     			$("#applNo").val("");
     			$("#applNo").attr("disabled",true);
     			break;
     		default:
     		    break;
		}	
	});
});
