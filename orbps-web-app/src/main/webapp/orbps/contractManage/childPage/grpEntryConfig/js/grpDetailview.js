// 新建命名空间
com.orbps.contractManage.childPage={};
com.orbps.contractManage.childPage.grpEntryConfig={};
com.orbps.contractManage.childPage.grpEntryConfig.DetailForm=$("#DetailForm");

//基本信息校验规则
com.orbps.contractManage.childPage.grpEntryConfig.DetForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
			taxpayer : {
				required : true
			},
			taxpayerCode : {
				required : true
			},
			companyBankCode : {
				required : true
			},
			companyBankName : {
				required : true
			},
			companyBankAccNo : {
				required : true
			}
		},
		messages : {
			taxpayer : {
				required : '请输入投保单号'
			},
			taxpayerCode : {
				required : '请输入报价审批号'
			},
			companyBankCode : {
				required : '请选择投保日期'
			},
			companyBankName : {
				required : '请输入共保协议号'
			},
			companyBankAccNo : {
				required : '请输入上期保单号'
			}
		},
		invalidHandler: function (event, validator) {
            Metronic.scrollTo(error2, -200);
        },
        
		errorPlacement:function(error,element){
			var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            icon.attr("data-original-title", error.text()).tooltip({'container': 'body'});
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
	//datagrid控件初始化表格 与bootstrap的表格样式不兼容
	// datagrid组件初始化
//	$("*").datagridsInitLoad();
	$("#ruleEntry").editDatagridsLoadById();
	//日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});	
	// 初始化校验信息
	com.orbps.contractManage.childPage.grpEntryConfig.DetForm(com.orbps.contractManage.childPage.grpEntryConfig.DetailForm);
	//增加表格
	$("#DetailForm #btnAdd").click(function() {
		$("#ruleEntry").editDatagrids("addRows");
		return false;
	});
	
	//删除表格
	$("#btnDel").click(function () {
		$("#ruleEntry").editDatagrids("delRows");
		return false;
    }); 
});


