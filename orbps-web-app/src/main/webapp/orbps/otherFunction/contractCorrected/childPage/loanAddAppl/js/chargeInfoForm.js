//定义form
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.chargeInfoForm = $('#chargeInfoForm');

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.chargeValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
        rules : {
        	totalPremium : {
				required : true,
				isFloatGteZero : true
			},
			moneyinItrvl : {
				required : true
			}
		},
		messages : {
			totalPremium : {
				required : '请输入保费合计',
				isFloatGteZero : "保费合计必须大于0"
			},
			moneyinItrvl : {
				required : '请选择缴费方式'
			}
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
        
        unhighlight: function (element) {

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
	// 调用校验方法
	com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.chargeValidateForm(com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.chargeInfoForm);
	
	//丢失焦点的非空校验
	$("#chargeInfoForm #totalPremium").blur(function () { 
		var totalPremium = $("#totalPremium").val();
		if(totalPremium==null||""==totalPremium){
			lion.util.info("警告","保费合计不能为空");
			return false;
		}
	});
});

