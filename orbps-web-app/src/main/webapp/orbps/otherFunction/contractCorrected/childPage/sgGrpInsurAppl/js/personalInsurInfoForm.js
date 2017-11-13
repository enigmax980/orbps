com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoForm = $("#personalInsurInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoFormCount = 0;

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personBaseValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
            joinName : {
                required : true,
            },
            joinIdNo : {
            	required : true,
                isIdCardNo : true
            },
            joinMobile : {
                isMobile : true
            },
            joinEmail : {
            	email : true
            },
            postCode : {
                required : true,
            },
            joinHome : {
                required : true,
            }
        },
        messages : {
            joinName : {
                required : "汇交人姓名不能为空",
            },
            joinIdNo : {
                required : "汇交人证件号码不能为空",
                isIdCardNo : "请您核对并输入汇交人正确的证件号码",
            },         
            joinMobile : {
                isMobile : "请输入正确的电话号码"
            },
            joinEmail : {
            	email : "汇交人邮箱格式不正确"
            },
            postCode : {
                required : "邮编不能为空"
            },
            joinHome : {
                required : "请输入汇交人详细地址"
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
        
        success: function (label, element) {
        	var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
	});
}


$(function() {	
// 校验函数初始化	
	com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personBaseValidateForm(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoForm);
	$("#citySelectsP").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        /* prov:"北京", */
        /* nodata:"none", */
        required:false
	})
});


//汇交人身份证号验证
jQuery.validator.addMethod("isIdCardNo", function(value, element) {
    var appIdTypeValue = $("#personalInsurInfoForm #joinIdType").val();
    // 先判断证件类型是否为身份证再去校验
    if (appIdTypeValue === "I") {
        return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
    } else {
        return true;
    }
}, "请正确输入您的身份证号码！");

//如果联系人证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别、年龄等
$("#joinIdNo").blur(
	  function() {
		  idcard($("#personalInsurInfoForm #joinIdType"),
				  $("#personalInsurInfoForm #joinIdNo"),
				  $("#personalInsurInfoForm #joinBirthDate"),
				  $("#personalInsurInfoForm #joinSex"));
});

// 当联系人证件类型改变的 时候，将五要素只读变成可写
$("#personalInsurInfoForm #joinIdType").change(function() {
		var joinIdType = $("#personalInsurInfoForm #joinIdType").val();
		if(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoFormCount === 0 || com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoFormCount === 1){
			com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoFormCount++;
			return false;
		}
		if ("I" === joinIdType) {
			$("#personalInsurInfoForm #joinBirthDate").val("");
			$("#personalInsurInfoForm #joinBirthDate").attr("disabled",true);
			$("#personalInsurInfoForm #joinBirthDateBtn").attr("disabled",true);
			$("#personalInsurInfoForm #joinSex").attr("readonly",true);
			$("#personalInsurInfoForm #joinSex").combo("refresh");
			$("#personalInsurInfoForm #joinIdNo").val("");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("fa-warning");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("fa-check");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("has-success");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("has-error");
		}else{
			$("#personalInsurInfoForm #joinBirthDate").attr("disabled",false);
			$("#personalInsurInfoForm #joinBirthDateBtn").attr("disabled",false);
			$("#personalInsurInfoForm #joinSex").attr("readonly",false);
			$("#personalInsurInfoForm #joinIdNo").val("");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("fa-warning");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("fa-check");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("has-success");
			$("#personalInsurInfoForm #joinIdNoFa").removeClass("has-error");
		}
});

$("#joinName").blur(function() {
    var joinName = $("#joinName").val();
    if (joinName === null || "" === joinName) {
        lion.util.info("警告", "汇交人姓名不能为空");
        return false;
    }
});

$("#joinIdNo").blur(function() {
    var joinIdNo = $("#joinIdNo").val();
    if (joinIdNo === null || "" === joinIdNo) {
        lion.util.info("警告", "汇交人证件号码不能为空");
        return false;
    }
});

$("#joinMobile").blur(function() {
    var joinMobile = $("#joinMobile").val();
    if (joinMobile === null || "" === joinMobile) {
        lion.util.info("警告", "汇交人移动电话不能为空");
        return false;
    }
});

$("#postCode").blur(function() {
    var postCode = $("#postCode").val();
    if (postCode === null || "" === postCode) {
        lion.util.info("警告", "汇交人邮编不能为空");
        return false;
    }
});
//根据争议处理方式调整页面样式
$("#personalInsurInfoForm #pSettleDispute").change(function() {
    if($("#pSettleDispute").val()==="1"){
        $("#personalInsurInfoForm #joinParbOrgName").attr("disabled",true);
        $("#personalInsurInfoForm #joinParbOrgName").val("");
    }else{
        $("#personalInsurInfoForm #joinParbOrgName").attr("disabled",false);
    }
});
//仲裁机构名称字符简直能有一个空格
$("#personalInsurInfoForm #joinParbOrgName").blur(function() {
	var joinParbOrgName = $("#personalInsurInfoForm #joinParbOrgName").val();
	if (joinParbOrgName === null || "" === joinParbOrgName) {
		lion.util.info("警告", "仲裁机构名称不能为空");
		return false;
	}
	// 字符间只能有一个空格
	joinParbOrgName = joinParbOrgName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
	$("#personalInsurInfoForm #joinParbOrgName").val(joinParbOrgName);
}); 
