
//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.beneficiaryInfoValidateForm= function (vform) {
	    var error2 = $('.alert-danger', vform);
	    var success2 = $('.alert-success', vform);
	    vform.validate({
			errorElement: 'span',
	        errorClass: 'help-block help-block-error', 
	        focusInvalid: false, 
	        onkeyup:false,
	        ignore: '', 
			rules : {  
				BankNo : {
					required : true,
					isluhmCheck:true
				},
				paymentDate : {
					required : true
				},
				bankCode : {
					required : true
				},
				paymentForm : {
					required : true
				},
				insureDate: {
					required : true
				},
				takeEffectDate : {
					required : true
				},
				labour : {
					required : true
				},
				conductMode : {
					required : true
				},
				arbitrationName : {
					required : true,
				},
				partTimeClass:{
					required : true,
				},
				preparePhone : {
					required : true,
				},
				societyInsurance: {
					required : true,					
				},
				supplementMedical: {
					required : true,					
				},
				shiftPerson: {
					required : true,					
				},
				reprintInsuredNo: {
					required : true,					
				},
				sumPrem: {
					required : true,					
				},
				totalInsuranceAmount: {
					required : true,					
				},
				
			},
			messages : {
				totalInsuranceAmount : {
					required : "保费合计不能为空"
				},
				companyBankCode : {
					required : "请选择首期缴费方式"
					
				},
				applDate : {
					required : "请选择缴费日期"
				},
				payMode : {
					required : "请选择缴费方式"
				},
				companyBankCode : {
					required : "请选择银行代码"
				},
				reprintInsuredNo : {
					required : "转投保单单号不能为空"
				},
				BankNo : {
					required : "银行账号不能为空",
					isluhmCheck:"请输入正确银行账号"
				},
				countMode : {
					required : "请选择计算方式",					
				},
				nature:{
					required : "请选择保单性质",					
				},	
				takeEffectDate:{
					required : "请输入指定生效日",					
				},
			    conductMode : {
					required : "请选择处理方式"
				},
				arbitrationName : {
					required : "仲裁委员会名称不能为空"
				},
				sumPrem : {
					required : "保费合计不能为空"
				},
			},
			invalidHandler: function (event, validator) {
	            Metronic.scrollTo(error2, -200);
	        },
	        
			errorPlacement:function(error,element){
				var icon = $(element).parent('.input-icon').children('i');
	            icon.removeClass('fa-check').addClass("fa-warning");
	            if (icon.attr('title') || typeof icon.attr('data-original-title') != 'string') {
	            	icon.attr('data-original-title', icon.attr('title') || '').attr('title', error.text())
	            }
	        },
		    
		    highlight: function (element) {
	            $(element).closest('.col-md-2').removeClass("has-success").addClass('has-error');
	        },
	        
	        submitHandler: function (form) {
	            success2.show();
	            error2.hide();
	            form[0].submit();
	        },
	        
	        success: function (label, element) {
	        	var icon = $(element).parent('.input-icon').children('i');
	            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
	            icon.removeClass("fa-warning").addClass("fa-check");
	        }
		});
	}


$(function() {	
	// 校验函数初始化
	com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.beneficiaryInfoValidateForm(com.orbps.otherFunction.contractCorrected.childPage.longInsuranceEntry.offerInfoForm);
  
});

//增加表格
$("#offerInfo #btnAdd").click(function() {
	$("#fbp-editDataGrids").editDatagrids("addRows");
	return false;
});

//删除表格
$("#offerInfo #btnDel").click(function () {  
	$("#fbp-editDataGrids").editDatagrids("delRows");
	return false;
}); 


$("#totalInsuranceAmount").blur(function () {
	var totalInsuranceAmount = $("#totalInsuranceAmount").val();
	if(totalInsuranceAmount==null||""==totalInsuranceAmount){
		lion.util.info("警告","保险金合计不能为空");
		return false;
	}
}); 

$("#BankNo").blur(function () {
	var BankNo = $("#BankNo").val();
	if(BankNo==null||""==BankNo){
		lion.util.info("警告","银行账号不能为空");
		return false;
	}
}); 

$("#sumPrem").blur(function () {  
	var sumPrem = $("#sumPrem").val();
	if(sumPrem==null||""==sumPrem){
		lion.util.info("警告","保费合计不能为空");
		return false;
	}
}); 

$("#arbitrationName").blur(function () {  
	var arbitrationName = $("#arbitrationName").val();
	if(arbitrationName==null||""==arbitrationName){
		lion.util.info("警告","仲裁委员会名称不能为空");
		return false;
	}
}); 

$("#reprintInsuredNo").blur(function () {  
	var reprintInsuredNo = $("#reprintInsuredNo").val();
	if(reprintInsuredNo==null||""==reprintInsuredNo){
		lion.util.info("警告","转投保单单号不能为空");
		return false;
	}
});

//责任信息
$("#btnSelect").click(function() {
	var selectData = $("#fbp-editDataGrids").editDatagrids("getSelectRows");
	// 判断选择的是否是一个主险，判断是否添加主险信息
	if((null == selectData)||(selectData.length==0)||(selectData.length>1)){
		lion.util.info("警告","请选择一个主险信息");
		return false;
	}
	com.orbps.contractEntry.grpInsurAppl.addDialog.empty();
	com.orbps.contractEntry.grpInsurAppl.addDialog.load("/orbps/orbps/contractEntry/modal/html/insurRespModal.html",function(){
		$(this).modal('toggle');
		// 初始化edittable组件
		$("#coverageInfo_list").editDatagridsLoadById();
		// combo组件初始化
		$("#coverageInfo_list").editDatagrids("queryparams",selectData);
		// 重新加载数据
		$("#coverageInfo_list").editDatagrids("reload");
		setTimeout(function(){
			// 循环已经选择的责任信息
			for (var i = 0; i < com.orbps.contractEntry.grpInsurAppl.benesList.length; i++) {
				var array_element = com.orbps.contractEntry.grpInsurAppl.benesList[i];
				// 判断选择的责任信息是一条还是多条
				if(array_element.length>=0){
					if(selectData.busiPrdCode==array_element[0].busiPrdCode){
						// 回显责任信息
						$("#coverageInfo_list").editDatagrids("selectRows",array_element);
					}
				}else{
					if(selectData.busiPrdCode==array_element.busiPrdCode){
						$("#coverageInfo_list").editDatagrids("selectRows",array_element);
					}
				}
			}
		},400);		
	});
	return false;
});


