//定义form
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.beneFiciaryForm = $('#beneFiciaryForm');

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.beneValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
        rules : {
        	firstBeneName : {
				required : true
			}
		},
		messages : {
			firstBeneName : {
				required : "请输入第一受益人"
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
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	// 初始化edittable组件
	$("#beneInfoTb").editDatagridsLoadById();
	// combo组件初始化
	$("*").comboInitLoad();
	
	// 调用校验方法
	com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.beneValidateForm(com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.beneFiciaryForm);

	//增加表格
	$("#beneFiciaryForm #btnAdd").click(function() {
		$("#beneInfoTb").editDatagrids("addRows");
		return false;
	});
	
	//删除表格
	$("#beneFiciaryForm #btnDel").click(function () {  
		$("#beneInfoTb").editDatagrids("delRows");
    }); 
	
	//丢失焦点的非空校验
	$("#beneFiciaryForm #firstBeneName").blur(function () {  
		var firstBeneName = $("#firstBeneName").val();
		if(firstBeneName==null||""==firstBeneName){
			lion.util.info("警告","第一受益人不能为空");
			return false;
		}
	});
});

