//增加表格
$("#beneficiaryInfo #btnAdd").click(function() {
	$("#fbp-editDataGrid-one").editDatagrids("addRows");
	return false;
});

//删除表格
$("#beneficiaryInfo #btnDel").click(function () {  
	$("#fbp-editDataGrid-one").editDatagrids("delRows");
	return false;
}); 




