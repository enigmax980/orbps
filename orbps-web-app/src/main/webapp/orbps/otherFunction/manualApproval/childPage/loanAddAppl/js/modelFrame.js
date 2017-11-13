com.orbps.contractEntry.loanAddAppl.loanAddForm=$("#loanAddForm-beneFiciary")

$(function() {	



	// combo组件初始化
	$("*").comboInitLoad();
	// 调用校验方法

	com.orbps.contractEntry.loanAddAppl.handleForm(com.orbps.contractEntry.loanAddAppl.loanAddForm);
	// 点击next
	$("#btnNext").click(function() {
		
		if(com.orbps.contractEntry.loanAddAppl.applInfoForm.validate().form()){
			com.example.formwizard.submitForm();
			
		}else{
//			document.getElementById("btnPrev").click();
			alert(2222222);
			return false;
		}
	});
	
	//增加表格
	$("#loanAddForm-beneFiciary #btnAdd").click(function() {
		alert(9999);
		$("#fbp-editDataGrid").editDatagrids("addRows");
		alert(0000);
		return false;
	});
	
	//删除表格
	$("#loanAddForm-beneFiciary #btnDel").click(function () {  
		$("#fbp-editDataGrid").editDatagrids("delRows");
		return false;
    }); 
});

