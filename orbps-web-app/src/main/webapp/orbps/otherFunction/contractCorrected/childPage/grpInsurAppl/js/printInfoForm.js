com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printInfoForm=$("#printInfoForm");
//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printValidateForm= function (vform) {
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
	
	// 初始化校验函数
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printValidateForm(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.printInfoForm);
	$("#ipsnlstId").change(function(){
		var ipsnlstId = $("#ipsnlstId").val();
		$("#prtIpsnLstType").attr("readOnly",false);
		$("#ipsnVoucherPrt").attr("readOnly",false);
		//如果是无清单  个人凭证打印  清单打印 职成不可用
		if("N" === ipsnlstId){
			setTimeout(function(){
				$("#prtIpsnLstType").combo("clear");
				$("#ipsnVoucherPrt").combo("clear");
				$("#prtIpsnLstType").attr("readOnly",true);
				$("#ipsnVoucherPrt").attr("readOnly",true);
			},400);
		}
	})
	
});