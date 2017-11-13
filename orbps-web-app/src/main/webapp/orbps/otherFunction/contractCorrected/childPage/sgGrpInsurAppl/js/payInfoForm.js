com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.payInfoForm = $("#payInfoForm");

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.payValidateForm = function (vform) {
	  var error2 = $('.alert-danger', vform);
	  var success2 = $('.alert-success', vform);
	  vform.validate({
	        errorElement : 'span',
	        errorClass : 'help-block help-block-error',
	        focusInvalid : false,
	        onkeyup : false,
	        ignore : '',
	        rules : {
			},
			messages : {
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
	com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.payValidateForm(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.payInfoForm);
	
});		


$("#payInfoForm #bankaccNo").blur(function() {
    var bankaccNo = $("#bankaccNo").val();
    if (bankaccNo === null || "" === bankaccNo) {
        lion.util.info("警告", "银行账号不能为空");
        return false;
    }
});

//如果交费形式是现金或者银行交款单，置灰交费开户行，开户名称，银行账号输入框
$("#moneyinType,#premFrom").change(
		function() {
			var moneyinType = $("#moneyinType").val();
			if (moneyinType === "R" || moneyinType === "C"
					|| $("#premFrom").val() === "2") {
				$("#bankCode").combo("clear");
				$("#bankCode").attr("disabled", true);
				$("#bankBranchName").val("");
				$("#bankBranchName").attr("disabled", true);
				$("#bankaccNo").val("");
				$("#bankaccNo").attr("disabled", true);
				$("#bankCodeIs").hide();
				$("#bankNameIs").hide();
				$("#bankAccNoIs").hide();
			} else {
				$("#moneyinType").attr("disabled", false);
				$("#bankCode").attr("disabled", false);
				$("#bankBranchName").attr("disabled", false);
				$("#bankaccNo").attr("disabled", false);
				$("#bankCodeIs").show();
				$("#bankNameIs").show();
				$("#bankAccNoIs").show();
			}
		});

$("#premFrom").change(function() {
	var premFrom = $("#premFrom").val();
	//如果保费来源是个人账户或团体账户时供费比例和供费金额置灰
	if (premFrom === "2") {
		$("#multiPartyScale").attr("disabled", true);
		$("#multiPartyScale").combo("clear");
		$("#multiPartyMoney").attr("disabled", true);
		$("#multiPartyMoney").combo("clear");
		$("#moneyinType").attr("disabled", true);
		$("#moneyinType").combo("clear");
		$("#bankCode").attr("disabled", true);
		$("#bankCode").combo("clear");
		$("#bankBranchName").attr("disabled", true);
		$("#bankBranchName").val("");
		$("#bankaccNo").attr("disabled", true);
		$("#bankaccNo").val("");
	} else if (premFrom === "1") {
		$("#multiPartyScale").attr("disabled", true);
		$("#multiPartyScale").combo("clear");
		$("#multiPartyMoney").attr("disabled", true);
		$("#multiPartyMoney").combo("clear");
	} else {
		$("#multiPartyScale").attr("disabled", false);
		$("#multiPartyMoney").attr("disabled", false);
	}
});

//如果交费形式不是现金和银行交款单，校验银行账号必须输入
$("#payInfoForm #bankaccNo").blur(function() {
	var moneyinType = $("#moneyinType").val();
	if (moneyinType === "R" || moneyinType === "C") {
	} else {
		var bankaccNo = $("#payInfoForm #bankaccNo").val();
		if (bankaccNo === null || "" === bankaccNo) {
			lion.util.info("警告", "银行账号不能为空");
			return false;
		}
	}
});

//如果交费形式不是现金和银行交款单，校验开户行必须输入
$("#payInfoForm #bankCode").blur(function() {
	var moneyinType = $("#moneyinType").val();
	if (moneyinType === "R" || moneyinType === "C") {
	} else {
		var bankCode = $("#payInfoForm #bankCode").val();
		if (bankCode === null || "" === bankCode) {
			lion.util.info("警告", "开户银行不能为空");
			return false;
		}
	}
});

//如果交费形式不是现金和银行交款单，校验开户名必须输入
$("#payInfoForm #bankBranchName").blur(function() {
	var moneyinType = $("#moneyinType").val();
	var bankName = $("#payInfoForm #bankBranchName").val();
	if (moneyinType === "R" || moneyinType === "C") {
	} else {
		if (bankName === null || "" === bankName) {
			lion.util.info("警告", "开户行名不能为空");
			return false;
		}
	}
	var $bankNameInput = $(this);
	// 最后一位是小数点的话，移除
	$bankNameInput.val(($bankNameInput.val().replace(/\.$/g, "")));
	// 投银行名只能有一个空格
	bankName = bankName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
	$("#payInfoForm #bankBranchName").val(bankName);
});

// 校验银行帐号
$("#payInfoForm #bankaccNo").blur(function() {
	var bankaccNo = $("#payInfoForm #bankaccNo").val();
	if (bankaccNo === "") {
		lion.util.info("提示", "请输入银行账号");
		return false;
	}
});

//续期扣款改变页面样式，一期不支持续期扣款，注释代码
//$("#renewalChargeFlag").change(function(){
//	if($("#renewalChargeFlag").val()==="N"){
//		$("#trem").attr("disabled",true)
//		$("#trem").val("");
//	}else if($("#renewalChargeFlag").val()==="Y"){
//		$("#trem").attr("disabled",false)
//		var trem = $("#trem").val();
//		if (trem === null || "" === trem) {
//        lion.util.info("警告", "请输入续期扣款期数");
//        return false;
//		}    
//	}else{		
//	}
//});

//$("#trem").blur(function(){       
//        var trem = $("#trem").val();
//		var zh_verify = /^[0-9]*[1-9][0-9]*$/;
//		if(!zh_verify.test(trem))
//			lion.util.info("警告", "请输入大于零的整数类型续期扣款期数");
//		return false;
//});


//多期暂交改变页面样式
$("#multiRevFlag").change(function(){
	if($("#multiRevFlag").val()==="N"){
		$("#multiTempYear").attr("disabled",true)
		$("#multiTempYear").val("");
	}else if($("#multiRevFlag").val()==="Y"){
		$("#multiTempYear").attr("disabled",false)
	}
});

$("#payInfoForm #multiTempYear").blur(function() {
    var multiTempYear = $("#multiTempYear").val();
    var zh_verify = /^[0-9]*[1-9][0-9]*$/;
	if(!zh_verify.test( multiTempYear ))
		lion.util.info("警告", "请输入大于零的整数类型多期暂交年数");
	return false;
});
