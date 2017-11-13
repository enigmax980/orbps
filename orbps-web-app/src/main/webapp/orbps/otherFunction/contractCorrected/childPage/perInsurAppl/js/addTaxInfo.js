//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addTaxInfoValidateForm= function (vform) {
	    var error2 = $('.alert-danger', vform);
	    var success2 = $('.alert-success', vform);
	    vform.validate({
			errorElement: 'span',
	        errorClass: 'help-block help-block-error', 
	        focusInvalid: false, 
	        onkeyup:false,
	        ignore: '', 
			rules : {  
				taxpayerName : {
					required : true,
					zh_verify :true
				},
				taxpayNo : {
					required : true,
					isIntGteZero : true
				},
				accountName : {
					required : true
				},
				bankNo : {
					required : true,
					isIntGteZero : true
				},
			},
			messages : {
				bankNo:{
					required : "银行账户不能为空",
					isIntGteZero:"格式不正确"
				},
				taxpayerName : {
					required : "纳税人姓名不能为空",
					zh_verify:"格式不正确"
				},
				taxpayNo : {
					required : "纳税识别号不能为空",
					isIntGteZero:"格式不正确"
				},
				accountName : {
					required : "开户名称不能为空"
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
	com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addTaxInfoValidateForm(com.orbps.otherFunction.contractCorrected.childPage.perInsurAppl.addTaxInfoForm);

});


$("#addedTaxInfo #taxpayerName").blur(function () {  
	var taxpayerName = $("#taxpayerName").val();
	if(taxpayerName==null||""==taxpayerName){
		lion.util.info("警告","纳税人姓名不能为空");
		return false;
	}
}); 


$("#addedTaxInfo #taxpayNo").blur(function () {  
	var taxpayNo = $("#taxpayNo").val();
	if(taxpayNo==null||""==taxpayNo){
		lion.util.info("警告","纳税识别号不能为空");
		return false;
	}
}); 

$("#addedTaxInfo #accountName").blur(function () {  
	var accountName = $("#accountName").val();
	if(accountName==null||""==accountName){
		lion.util.info("警告","开户名称不能为空");
		return false;
	}
}); 

$("#addedTaxInfo #bankNo").blur(function () {  
	var bankNo = $("#bankNo").val();
	if(bankNo==null||""==bankNo){
		lion.util.info("警告","银行账号不能为空");
		return false;
	}
});

