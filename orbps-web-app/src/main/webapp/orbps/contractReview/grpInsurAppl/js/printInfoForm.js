com.orbps.contractReview.grpInsurAppl.printInfoForm = $("#printInfoForm");
// 基本信息校验规则
com.orbps.contractReview.grpInsurAppl.printValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		rules : {
//			cntrType : {
//				required : true
//			},
//			ipsnlstId : {
//				required : true
//			},
//			giftFlag : {
//				required : true
//			},
//			applProperty : {
//				required : true
//			},
//			exceptionInform : {
//				required : true
//			}
		},
		messages : {
//			cntrType : {
//				required : "合同打印方式不能为空"
//			},
//			ipsnlstId : {
//				required : "团单清单标记不能为空"
//			},
//			giftFlag : {
//				required : "赠送险标记不能为空"
//			},
//			applProperty : {
//				required : "保单性质不能为空"
//			},
//			exceptionInform : {
//				required : "异常告知不能为空"
//			}
		},
		invalidHandler : function(event, validator) {
			Metronic.scrollTo(error2, -200);
		},

		errorPlacement : function(error, element) {
			var icon = $(element).parent('.input-icon').children('i');
			icon.removeClass('fa-check').addClass("fa-warning");
			if (icon.attr('title')
					|| typeof icon.attr('data-original-title') !== 'string') {
				icon.attr('data-original-title', icon.attr('title') || '')
						.attr('title', error.text())
			}
		},

		highlight : function(element) {
			$(element).closest('.col-md-2').removeClass("has-success")
					.addClass('has-error');
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
    // 初始化校验函数
    com.orbps.contractReview.grpInsurAppl
            .printValidateForm(com.orbps.contractReview.grpInsurAppl.printInfoForm);
});

//团单清单标记chenge事件
$("#printInfoForm #ipsnlstId").change(
		function() {
			if ($("#printInfoForm #ipsnlstId").val() === "N"
					&& $("#payInfoForm #premFrom").val() !== "1") {
				lion.util.info("警告", "团体清单标记选择“无清单”时保费来源为“团体账户付款”！");
			}
			$("#printInfoForm #prtIpsnLstType").attr("disabled", false);
			$("#printInfoForm #ipsnVoucherPrt").attr("disabled", false);
			//当页面选择无清单时清单打印置灰，
			if ($("#printInfoForm #ipsnlstId").val() === "N") {
				$("#printInfoForm #ipsnVoucherPrtIs").hide();
				$("#printInfoForm #prtIpsnLstTypeIs").hide();
				$("#printInfoForm #prtIpsnLstType").combo("clear");
				$("#printInfoForm #ipsnVoucherPrt").combo("clear");
				$("#printInfoForm #prtIpsnLstType").attr("disabled", true);
				$("#printInfoForm #ipsnVoucherPrt").attr("disabled", true);
			} else {
				// 展示必录的红*
				$("#printInfoForm #ipsnVoucherPrtIs").show();
				$("#printInfoForm #prtIpsnLstTypeIs").show();
			}

		});

//团单个人凭证打印chenge事件
$("#printInfoForm #ipsnVoucherPrt").change(function(){
	var ipsnVoucherPrt = $("#printInfoForm #ipsnVoucherPrt").val();
	if(ipsnVoucherPrt==="0"){
		$("#connPostcodeId").show();
	}else{
		$("#connPostcodeId").hide();
	}
});