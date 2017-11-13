//定义form
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.arbitrationInfoForm = $('#arbitrationInfoForm');

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.arbiValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
        rules : {
        	cntrNature : {
				required : true
			},
			manualUwFlag : {
				required : true
			},
			disputeHandType : {
				required : true
			},
			arbitrationName : {
				required : true
			}
		},
		messages : {
			cntrNature : {
				required : '请选择保单性质'
			},
			manualUwFlag : {
				required : '请选择是否人工核保'
			},
			disputeHandType : {
				required : "请选择争议处理方式"
			},
			arbitrationName : {
				required : "请输入仲裁委员会名称"
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
	com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.arbiValidateForm(com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.arbitrationInfoForm);

	//丢失焦点的非空校验
	$("#arbitrationInfoForm #arbitrationName").blur(function () {  
		var arbitrationName = $("#arbitrationName").val();
		if(arbitrationName==null||""==arbitrationName){
			lion.util.info("警告","销售员机构不能为空");
			return false;
		}
	}); 
	
	setTimeout(function(){
		//是否人工核保下拉框默认显示否
		$("#arbitrationInfoForm #manualUwFlag").combo("val","N");
	},1000);
});

