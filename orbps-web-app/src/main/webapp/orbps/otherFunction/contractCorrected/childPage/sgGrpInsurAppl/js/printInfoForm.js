com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.printInfoForm = $("#printInfoForm");

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.ValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
        },
        messages : {
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


$(function(){
	com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.ValidateForm(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.printInfoForm);
});

$("#listFlag").change(function(){
	var listFlag = $("#listFlag").val();
	if("N" === listFlag || "M" === listFlag){
		setTimeout(function(){
			$("#listFlag").combo("clear");
			lion.util.info("提示","该保单不允许选择无清单或事后补录");
		},400);
	}
});