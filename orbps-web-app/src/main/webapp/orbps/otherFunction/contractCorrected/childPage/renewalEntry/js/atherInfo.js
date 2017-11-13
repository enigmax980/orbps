// 基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.applInfoValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		messages : {
			disputeHandling : {
				required : "请输入争议处理方式"
			},
			Name : {
				required : "请输入姓名",
			},
			effectiveFrequency : {
				required : "请输入生效频率"
			},
			insurancePeriod : {
				required : "请输入保险期间"
			},
			bankName : {
				required : "请输入银行名称"
			},
			bankAccount : {
				required : "请输入银行账户",
			},
			settlementQuota : {
				required : "请输入结算限额",
			},

		},
		rules : {
			disputeHandling : {
				required : true
			},
			Name : {
				required : true,
			},
			effectiveFrequency : {
				required : true
			},
			insurancePeriod : {
				required : true,
				isNumLetter : true
			},
			bankName : {
				required : true
			},
			bankAccount : {
				required : true,
			},
			settlementQuota : {
				required : true,
			},
		},

		invalidHandler : function(event, validator) {
			Metronic.scrollTo(error2, -200);
		},

		errorPlacement : function(error, element) {
			var icon = $(element).parent('.input-icon').children('i');
			icon.removeClass('fa-check').addClass("fa-warning");
			icon.attr("data-original-title", error.text()).tooltip({
				'container' : 'body'
			});
		},

		highlight : function(element) {
			$(element).closest('.col-md-2').removeClass("has-success")
					.addClass('has-error');
		},

		unhighlight : function(element) {

		},

		submitHandler : function(form) {
			success2.show();
			error2.hide();
			form[0].submit();
		},

		success : function(label, element) {
			var icon = $(element).parent('.input-icon').children('i');
			$(element).closest('.col-md-2').removeClass('has-error').addClass(
					'has-success');
			icon.removeClass("fa-warning").addClass("fa-check");
		}
	});
}


$(function() {
	// 校验函数初始化
	com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.applInfoValidateForm(com.orbps.otherFunction.contractCorrected.childPage.renewalEntry.atherInfo);
	
	setTimeout(function(){
    	//是否选择异常告知下拉框默认显示否
    	$("#atherInfo #exceptionReport").combo("val","N");
    },1000);
	
	
//	$("#exceptionReport").change(function(){
//		var flag = $("#exceptionReport").attr("value","N");
//		alert(flag);
//	});
	
	
	$("#atherInfo #Name").blur(function() {
		var Name = $("#Name").val();
		if (Name == null || "" == Name) {
			lion.util.info("警告", "姓名不能为空");
			return false;
		}
	});

	$("#atherInfo #effectiveFrequency").blur(function() {
		var effectiveFrequency = $("#effectiveFrequency").val();
		if (effectiveFrequency == null || "" == effectiveFrequency) {
			lion.util.info("警告", "生效频率不能为空");
			return false;
		}
	});

	$("#atherInfo #insurancePeriod").blur(function() {
		var insurancePeriod = $("#insurancePeriod").val();
		if (insurancePeriod == null || "" == insurancePeriod) {
			lion.util.info("警告", "保险期间不能为空");
			return false;
		}
	});

	$("#atherInfo #bankName").blur(function() {
		var bankName = $("#bankName").val();
		if (bankName == null || "" == bankName) {
			lion.util.info("警告", "银行名称不能为空");
			return false;
		}
	});

	$("#atherInfo #bankAccount").blur(function() {
		var bankAccount = $("#bankAccount").val();
		if (bankAccount == null || "" == bankAccount) {
			lion.util.info("警告", "银行账户不能为空");
			return false;
		}
	});

	$("#atherInfo #settlementQuota").blur(function() {
		var settlementQuota = $("#settlementQuota").val();
		if (settlementQuota == null || "" == settlementQuota) {
			lion.util.info("警告", "结算限额不能为空");
			return false;
		}
	});
	
});
