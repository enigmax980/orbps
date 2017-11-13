
//初始化函数
$(function() {
	// 初始化edittable组件
	
	//添加责任
	$("#longInsuranceEntrys #btnInsure").click(function() {
		com.orbps.contractEntry.longInsuranceEntry.grpInsurApplList = $("#coverageInfo_list").editDatagrids("getSelectRows");
		return false;
		lion.util.info("提示","添加责任信息成功！");
	});
});

