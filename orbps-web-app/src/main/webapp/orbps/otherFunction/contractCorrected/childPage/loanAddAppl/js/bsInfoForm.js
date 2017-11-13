//定义form
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.bsInfoForm = $('#bsInfoForm');

$(function() {
	// 初始化edittable组件
	$("#polListTb").editDatagridsLoadById();
	
	// combo组件初始化
	$("*").comboInitLoad();
	
	//增加表格
	$("#bsInfoForm #btnAdd").click(function() {
		$("#polListTb").editDatagrids("addRows");
	});
	
	//删除表格
	$("#bsInfoForm #btnDel").click(function () {  
		$("#polListTb").editDatagrids("delRows");
    }); 
	
	//查询责任信息
	$("#bsInfoForm #btnSelect").click(function() {
		var selectData = $("#bsInfoForm #polListTb").editDatagrids("getSelectRows");
		// 判断选择的是否是一个主险，判断是否添加主险信息
		if((null == selectData)||(selectData.length==0)||(selectData.length>1)){
			lion.util.warning("警告","请选择一个主险信息");
			return false;
		}
		com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.addDialog.empty();
		com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.addDialog.load("/orbps/orbps/contractEntry/modal/html/insurRespModal.html",function(){
			$("#polListTb").editDatagridsLoadById();
			$(this).modal('toggle');
			// combo组件初始化
			$("#polListTb").editDatagrids("queryparams",selectData);
			// 重新加载数据
			$("#polListTb").editDatagrids("reload");
			setTimeout(function(){
    			$("#bsInfoForm #polListTb").editDatagrids("selectRows",com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.grpInsurApplList);
			},400);
		});
	});
	
	
});

