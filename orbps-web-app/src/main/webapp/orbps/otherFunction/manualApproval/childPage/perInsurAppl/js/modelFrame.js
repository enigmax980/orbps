com.orbps.contractEntry.perInsurAppl.pollicitation=$("#pollicitation-beneFiciary")

$(function() {	
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// 上一步下一步控件初始化
	$("*").stepInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});

	// 初始化edittable组件
	$("#fbp-editDataGrid-one").editDatagridsLoadById();
	alert(88888);
	// combo组件初始化
	$("*").comboInitLoad();
	// 调用校验方法
	com.orbps.contractEntry.perInsurAppl.handleForm(com.orbps.contractEntry.perInsurAppl.applInfoForm);
	// 点击next
	$("#btnNext").click(function() {
		
		if(com.orbps.contractEntry.perInsurAppl.applInfoForm.validate().form()){
			com.example.formwizard.submitForm();
			
		}else{
//			document.getElementById("btnPrev").click();
			alert(2222222);
			return false;
		}
	});
	
	//增加表格
	$("#pollicitation-beneFiciary #btnAdd").click(function() {
		alert(9999);
		$("#fbp-editDataGrid-one").editDatagrids("addRows");
		alert(0000);
		return false;
	});
	
	//删除表格
	$("#pollicitation-beneFiciary #btnDel").click(function () {  
		$("#fbp-editDataGrid-one").editDatagrids("delRows");
		return false;
    }); 
	
	//查询责任信息
//	$("#pollicitation-beneFiciary #btnSelect").click(function() {
//		alert(2);
//		var selectData = $("#pollicitation-beneFiciary #fbp-editDataGrid-one").editDatagrids("getSelectRows");
//		// 判断选择的是否是一个主险，判断是否添加主险信息
//		if((null == selectData)||(selectData.length==0)||(selectData.length>1)){
//			lion.util.warning("警告","请选择一个主险信息");
//		alert(3);
//		}
//		com.orbps.contractEntry.perInsurAppl.addDialog.empty();
//		var mainRisk = $("#pollicitation-beneFiciary #fbp-editDataGrid-one").editDatagrids("getSelectRows");
//		com.orbps.contractEntry.perInsurAppl.addDialog.load("/orbps/orbps/contractEntry/perInsurAppl/html/insurRespModal.html",function(){
//			$(this).modal('toggle');
//			// combo组件初始化
//			$("#coverageInfo_list").editDatagrids("queryparams",mainRisk);
//			// 重新加载数据
//			$("#coverageInfo_list").editDatagrids("reload");
//			setTimeout(function(){
//    			$("#coverageInfo_list").editDatagrids("selectRows",com.orbps.contractEntry.perInsurAppl.grpInsurApplList);
//			},400);		
//		});
//	});
});

