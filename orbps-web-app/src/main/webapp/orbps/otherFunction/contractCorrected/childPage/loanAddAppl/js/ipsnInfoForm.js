//定义form
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.ipsnInfoForm = $('#ipsnInfoForm');

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.ipsnValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
        rules : {
        	relToHldr : {
				required : true
			},
			custName : {
				required : true
			},
			sex : {
				required : true
			},
			birthDate : {
				required : true
			},
			idType : {
				required : true
			},
			idNo: {
				required : true,
				isNumLetter : true
			},
			postalAddress: {
				required : true
			},
			occupationalCodes: {
				required : true
			},
			postCode: {
				required : true
			},
			mobilePhone: {
				required : true,
				isMobile : true
			},
			lanPhone: {
				required : true,
				isTel : true
			},
			email: {
				required : true,
				isZipCode : true
			},
			faxNumber: {
				required : true,
				isFax : true
			},
			coverInfoFlag: {
				required : true
			}
		},
		messages : {
			relToHldr : {
				required : "请选择与投保人的关系"
			},
			custName : {
				required : '请输入客户姓名'
			},
			sex : {
				required : "请选择性别"
			},
			birthDate : {
				required : "请输入出生日期"
			},
			idType : {
				required : "选择证件类别"
			},
			idNo: {
				required : "请输入证件号",
				isNumLetter : "证件号码输入不正确"
			},
			postalAddress: {
				required : "请输入通讯地址"
			},
			occupationalCodes: {
				required : "请输入职业代码"
			},
			postCode: {
				required : "请输入邮编"
			},
			mobilePhone: {
				required : "请输入手机号",
				isMobile : "请输入正确的手机号"
			},
			lanPhone: {
				required : "请输入固定电话",
				isTel : "请输入正确的电话号码"
			},
			email: {
				required : "请输入电子邮箱",
				isZipCode : "请输入正确的电子邮箱"
			},
			faxNumber: {
				required : "请输入传真号",
				isFax : "请输入正确的传真号码"
			},
			coverInfoFlag: {
				required : "请选择是否覆盖以往信息"
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
	
	// 调用校验方法
	com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.ipsnValidateForm(
			com.orbps.otherFunction.contractCorrected.childPage.loanAddAppl.ipsnInfoForm);
	
	setTimeout(function(){
		//是否覆盖以往信息下拉框默认显示否
		$("#ipsnInfoForm #coverInfoFlag").combo("val","N");
	},1000);
	
	//丢失焦点的非空校验
	$("#ipsnInfoForm #custName").blur(function () {  
		var custName = $("#custName").val();
		if(custName==null||""==custName){
			lion.util.info("警告","客户姓名不能为空");
			return false;
		}
	}); 

	$("#ipsnInfoForm #idNo").blur(function () {  
		var idNo = $("#idNo").val();
		if(idNo==null||""==idNo){
			lion.util.info("警告","证件号不能为空");
			return false;
		}
	});

	$("#ipsnInfoForm #postalAddress").blur(function () {  
		var postalAddress = $("#postalAddress").val();
		if(postalAddress==null||""==postalAddress){
			lion.util.info("警告","通讯地址不能为空");
			return false;
		}
	});

	$("#ipsnInfoForm #postCode").blur(function () {  
		var postCode = $("#postCode").val();
		if(postCode==null||""==postCode){
			lion.util.info("警告","邮编不能为空");
			return false;
		}
	});

	$("#ipsnInfoForm #mobilePhone").blur(function () {  
		var mobilePhone = $("#mobilePhone").val();
		if(mobilePhone==null||""==mobilePhone){
			lion.util.info("警告","手机号不能为空");
			return false;
		}
	});

	$("#ipsnInfoForm #lanPhone").blur(function () {  
		var lanPhone = $("#lanPhone").val();
		if(lanPhone==null||""==lanPhone){
			lion.util.info("警告","固定电话不能为空");
			return false;
		}
	});

	$("#ipsnInfoForm #email").blur(function () {  
		var email = $("#email").val();
		if(email==null||""==email){
			lion.util.info("警告","电子邮箱不能为空");
			return false;
		}
	});

	$("#ipsnInfoForm #faxNumber").blur(function () {  
		var faxNumber = $("#faxNumber").val();
		if(faxNumber==null||""==faxNumber){
			lion.util.info("警告","传真号不能为空");
			return false;
		}
	});
	
	
});

