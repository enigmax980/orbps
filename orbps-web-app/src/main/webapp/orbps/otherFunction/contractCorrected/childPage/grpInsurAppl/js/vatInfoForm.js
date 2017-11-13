com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.vatInfoForm=$("#vatInfoForm");

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.vatValidateForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
//            taxpayerCode : {
//                digits : true
//            }
		},
		messages : {
//			 taxpayerCode : {
//	                digits : '请输入整数'
//	            }
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
        
        success: function (label, element) {
        	var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
	});
}
$(function() {	
	
	//初始化校验信息
//	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.vatValidateForm(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.vatInfoForm);
	
});

