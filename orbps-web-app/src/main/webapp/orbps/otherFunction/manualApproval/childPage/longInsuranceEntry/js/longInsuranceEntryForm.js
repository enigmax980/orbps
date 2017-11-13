// 新建contractEntry命名空间
com.orbps.contractEntry={};
// 新建contractEntry.longInsuranceEntry命名空间
com.orbps.contractEntry.longInsuranceEntry={};
//编辑或添加对话框
com.orbps.contractEntry.longInsuranceEntry.dialog = $('#btnModels');
com.orbps.contractEntry.longInsuranceEntry.insuredList = new Array();
com.orbps.contractEntry.longInsuranceEntry.oranLevelList = new Array();
com.orbps.contractEntry.longInsuranceEntry.grpInsurApplList = new Array();
com.orbps.contractEntry.longInsuranceEntry.longInsuranceEntryForm = $('#longInsuranceEntryForm');
com.orbps.contractEntry.longInsuranceEntry.busiPrqdCode = null;
//基本信息校验规则

$(function() {	
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	// 初始化edittable组件
	$("#fbp-editDataGrid-one").editDatagridsLoadById();
	// 初始化edittable组件
	$("#fbp-editDataGrids").editDatagridsLoadById();
	// combo组件初始化
	$("*").comboInitLoad();
	// 调用校验方法
//	com.orbps.contractEntry.longInsuranceEntry.handleForm(com.orbps.contractEntry.longInsuranceEntry.longInsuranceEntryForm);
	
	setTimeout(function(){
		//是否人工核保下拉框默认显示否
		$("#longInsuranceEntrys #accountCode").combo("val","N");
	},1000);
	
	//增加表格
	$("#longInsuranceEntrys #btnAdd").click(function() {
		$("#fbp-editDataGrids").editDatagrids("addRows");
	});
	
	//删除表格
	$("#longInsuranceEntrys #btnDel").click(function () {  
		$("#fbp-editDataGrids").editDatagrids("delRows");
    }); 
	
	//查询责任信息
	$("#longInsuranceEntrys #btnSelect").click(function() {
		var selectData = $("#longInsuranceEntrys #fbp-editDataGrids").editDatagrids("getSelectRows");
		// 判断选择的是否是一个主险，判断是否添加主险信息
		if((null == selectData)||(selectData.length==0)||(selectData.length>1)){
			lion.util.warning("警告","请选择一个主险信息");
			return false;
		}
		com.orbps.contractEntry.longInsuranceEntry.dialog.empty();
		alert(5);
		com.orbps.contractEntry.longInsuranceEntry.dialog.load("/orbps/orbps/contractEntry/longInsuranceEntry/html/longInsuranceEntryFormModel.html",function(){
			alert(1);
			$("#coverageInfo_list").editDatagridsLoadById();
			alert(2);
			$(this).modal('toggle');
			// combo组件初始化
			$("#longInsuranceEntrys #coverageInfo_list").editDatagrids("queryparams",selectData);
			// 重新加载数据
			$("#longInsuranceEntrys #coverageInfo_list").editDatagrids("reload");
			setTimeout(function(){
    			$("#longInsuranceEntrys #coverageInfo_list").editDatagrids("selectRows",com.orbps.contractEntry.longInsuranceEntry.grpInsurApplList);
			},400);		
		});
	});
	
	
});

