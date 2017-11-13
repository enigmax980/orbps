com.orbps.contractReview.grpInsurAppl.applBaseInfoForm = $("#applBaseInfoForm");
com.orbps.contractReview.grpInsurAppl.appIdTypeValue;
com.orbps.contractReview.grpInsurAppl.appConnIdTypeValue;
// 基本信息校验规则
com.orbps.contractReview.grpInsurAppl.applBaseValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		rules : {
			companyName : {
				required : true
			},
			idNo : {
				required : true
			},
			applNum : {
				required : true
			},
			numOfEmp : {
				required : true
			},
			ojEmpNum : {
				required : true
			},
			appAddrProv : {
				required : true
			},
			appAddrCity : {
				required : true
			},
			appAddrCountry : {
				required : true
			},
			appAddrHome : {
				required : true
			},
			appPost : {
				required : true,
				isZipCode : true
			},
			connName : {
				required : true
			},
			connIdNo : {
				required : true,
				isIdCardNo : true
			},
			connPhone : {
				isMobile : true
			},
			connPostcode : {
				email : true
			}
		},
		messages : {
			companyName : {
				required : "单位/团体名称不能为空"
			},
			idNo : {
				required : "证件号码不能为空"
			},
			applNum : {
				required : "投保人数不能为空"
			},
			numOfEmp : {
				required : "企业员工总数不能为空"
			},
			ojEmpNum : {
				required : "在职人数不能为空"
			},
			appAddrProv : {
				required : "省/直辖市不能为空"
			},
			appAddrCity : {
				required : "市/城区不能为空"
			},
			appAddrCountry : {
				required : "县/地级市不能为空"
			},
			appAddrHome : {
				required : "详细地址不能为空"
			},
			appPost : {
				required : "邮编不能为空",
				isZipCode : "请输入正确的邮编"
			},
			connName : {
				required : "联系人姓名不能为空"
			},
			connIdNo : {
				required : "联系人证件号码不能为空",
				isIdCardNo : "请输入正确的身份证号"
			},
			connPhone : {
				isMobile : "请输入正确的手机号码"
			},
			connPostcode : {
				email : "请输入正确格式的邮箱"
			}
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
    // 校验函数初始化
    com.orbps.contractReview.grpInsurAppl
            .applBaseValidateForm(com.orbps.contractReview.grpInsurAppl.applBaseInfoForm);

    // 身份证号验证
    jQuery.validator.addMethod("isIdCardNo", function(value, element) {
        com.orbps.contractReview.grpInsurAppl.appIdTypeValue = $(
                "#applBaseInfoForm #connIdType").val();
        // 先判断证件类型是否为身份证再去校验,appIdTypeValue(证件类型的值)再各自需要的js里设置为全局变量,
        if (com.orbps.contractReview.grpInsurAppl.appIdTypeValue === "I") {
            return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
        } else {
            return true;
        }
    }, "请正确输入您的身份证号码！");
    
  //根据争议处理方式调整页面样式
	$("#disputePorcWay").change(function() {
		if ($("#disputePorcWay").val() === "1") {
			$("#arbOrgNameIs").hide();
			$("#arbOrgName").attr("readonly", true);
			$("#arbOrgName").val("");
		} else {
			$("#arbOrgNameIs").show();
			$("#arbOrgName").attr("readonly", false);
		}
	});

});

$("#arbOrgName").blur(function() {
	var arbOrgName = $("#arbOrgName").val();
	if (arbOrgName === null || "" === arbOrgName) {
		lion.util.info("警告", "仲裁机构名称不能为空");
		return false;
	}
	// 字符间只能有一个空格
	arbOrgName = arbOrgName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
	$("#arbOrgName").val(arbOrgName);
});


$("#companyName").blur(function() {
	var companyName = $("#companyName").val();
	if (companyName === null || "" === companyName) {
		lion.util.info("警告", "单位/团体名称不能为空");
		return false;
	}
	// 字符间只能有一个空格
	companyName = companyName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
	$("#companyName").val(companyName);
});

$("#idNo").blur(function() {
	var idNo = $("#idNo").val();
	if (idNo === null || "" === idNo) {
		lion.util.info("警告", "证件号码不能为空");
		return false;
	}
});

$("#applNum").blur(function() {
	var applNum = $("#applNum").val();
	if (applNum === null || "" === applNum) {
		lion.util.info("警告", "投保人数不能为空");
		return false;
	}
	if(this.value<3){this.value=''}
});

$("#appPost").blur(function() {
	var appPost = $("#appPost").val();
	if (appPost === null || "" === appPost) {
		lion.util.info("警告", "邮编不能为空");
		return false;
	}
});

$("#connName").blur(function() {
	var connName = $("#connName").val();
	if (connName === null || "" === connName) {
		lion.util.info("警告", "联系人姓名不能为空");
		return false;
	}
	// 字符间只能有一个空格
	connName = connName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
});



$(function(){
	$("#citySelect").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        /* prov:"北京", */
        /* nodata:"none", */
        required:false
    });
});

//联系人固定电话、移动电话不能同时为空
com.orbps.contractReview.grpInsurAppl.isPhoneOrTel = function() {
	var phone = $("#applBaseInfoForm #connPhone").val();
	var tel = $("#applBaseInfoForm #appHomeTel").val();
	if (("" !== phone && null !== phone) || ("" !== tel && null !== tel)) {
		return true;
	} else {
		lion.util.info("警告", "联系人移动电话和联系人固定电话不能同时为空！");
		$(".fa").removeClass("fa-warning");
		$(".fa").removeClass("fa-check");
		$(".fa").removeClass("has-success");
		$(".fa").removeClass("has-error");
		return false;
	}
};

//丢失焦点校验联系人移动电话和固定电话是否同时为空
$("#applBaseInfoForm #connPhone").blur(function() {
	com.orbps.contractReview.grpInsurAppl.isPhoneOrTel();
});
$("#applBaseInfoForm #appHomeTel").blur(function() {
	com.orbps.contractReview.grpInsurAppl.isPhoneOrTel();
});

