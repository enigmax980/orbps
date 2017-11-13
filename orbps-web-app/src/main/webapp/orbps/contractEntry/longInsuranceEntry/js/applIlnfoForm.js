com.orbps.contractEntry = {};
com.orbps.contractEntry.longInsuranceEntry = {};
com.orbps.contractEntry.longInsuranceEntry.applIlnfoForm = $("#applIlnfo");
com.orbps.contractEntry.longInsuranceEntry.appIdTypeValue;
com.orbps.contractEntry.longInsuranceEntry.appConnIdTypeValue;
com.orbps.contractEntry.longInsuranceEntry.isIdNo;
// 编辑或添加对话框

// 基本信息校验规则
com.orbps.contractEntry.longInsuranceEntry.applIlnfoValidateForm = function(
        vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            maritalStatus : {
                required : true,
            },
            applNo : {
                required : true,
                isIntGteZero : true
            },
            insuredChannel : {
                required : true,
                zh_verifyl : true
            },
            salesBranchNo : {
                required : true,
                isIntGteZero : true
            },
            customerNo : {
                required : true,
                isIntGteZero : true
            },
            Name : {
                required : true
            },
            partTimeCode : {
                required : true
            },
            sex : {
                required : true
            },
            birthDate : {
                required : true,
            },
            Age : {
                required : true,
                isIntGteZero : true
            },
            marType : {
                required : true
            },
            idNo : {
                required : true
            },
            idTerm : {
                required : true
            },
            occupationType : {
                required : true
            },
            companyBankCode : {
                required : true,
            },
            ptJobType : {
                required : true,
            },
            sparePhone : {
                required : true,
                isMobile : true
            },
            zipCode : {
                required : true
            },
            emergencyPhone : {
                required : true,
                isMobile : true
            },
            mobilePhone : {
                required : true,
                isMobile : true
            },
            lanPhone : {
                required : true,
                isTel : true
            },
            email : {
                required : true,
                isMobile : true
            },
            idType : {
                required : true,

            },
            nationality : {
                required : true,

            },
            societyInsurance : {
                required : true,

            },
            supplementMedical : {
                required : true,
            },
            shiftPerson : {
                required : true,
            },
            posAddress : {
                required : true,
            },
            emergencyPerson : {
                required : true,
            },
            occupationCode : {
                required : true,
            },
            ptJobCode : {
                required : true,
            },
            externalTransfer : {
                required : true,
            },
            supplementaryMedical : {
                required : true,
            },
            socialInsurance : {
                required : true,
            },
        },
        messages : {
            socialInsurance : {
                required : "社会保险/公费医疗不能为空",
            },
            supplementaryMedical : {
                required : "补充医疗不能为空",
            },
            externalTransfer : {
                required : "个人保单外部转移人员不能为空",
            },
            ptJobCode : {
                required : "兼职代码不能为空",
            },
            occupationCode : {
                required : "职业代码不能为空",
            },
            maritalStatus : {
                required : "婚姻状况不能为空",
            },
            applNo : {
                required : "投保单号不能为空",
                isIntGteZero : "请输入大于0数字"
            },
            insuredChannel : {
                required : "投保渠道不能为空"

            },
            salesBranchNo : {
                required : "销售机构号不能为空",
                isIntGteZero : "请输入大于0数字"
            },
            customerNo : {
                required : "顾客号不能为空",
                isIntGteZero : "请输入大于0数字"
            },
            Name : {
                required : "姓名不能为空"
            },
            sex : {
                required : "请选性别"
            },
            birthDate : {
                required : "出生日期不能为空",
            },
            Age : {
                required : "年龄不能为空",
                isIntGteZero : "请输入>=0的整数"
            },
            marType : {
                required : "请选择婚姻状况",
            },
            paperType : {
                required : "请选择证件类型",
            },
            idNo : {
                required : "证件号码不能为空",
                isIdCardNo : "请您核对并输入联系人正确的证件号码"
            },
            idTerm : {
                required : "请选择证件有效期"
            },
            occupationType : {
                required : "职业类别不能为空"
            },
            companyBankCode : {
                required : "请选择职业代码"
            },
            nationality : {
                required : "请选择国籍"
            },
            ptJobType : {
                required : "兼职类别不能为空"
            },
            partTimeCode : {
                required : "兼职代码不能为空"
            },
            sparePhone : {
                required : "备用移动电话不能为空",
                isMobile : "请输入正确的电话号码"
            },
            emergencyPerson : {
                required : "紧急联系人不能为空"
            },
            emergencyPhone : {
                required : "紧急联系人电话不能为空",
                isMobile : "请输入正确的电话号码"
            },
            posAddress : {
                required : "通讯地址不能为空"
            },
            mobilePhone : {
                required : "移动电话不能为空",
                isMobile : "请输入正确的电话号码"
            },
            email : {
                required : "电子邮箱不能为空",
                email : "请输入正确格式的邮箱"
            },
            shiftPerson : {
                required : "请选择转移人员"
            },
            supplementMedical : {
                required : "请选择补充医疗"
            },
            societyInsurance : {
                required : "请选择社会保险/公费医疗"
            },
            lanPhone : {
                required : "固定电话不能为空",
                isTel : "请输入正确的固定电话"
            },
        },
        invalidHandler : function(event, validator) {
            Metronic.scrollTo(error2, -200);
        },

        errorPlacement : function(error, element) {
            var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            if (icon.attr('title')
                    || typeof icon.attr('data-original-title') != 'string') {
                icon.attr('data-original-title', icon.attr('title') || '')
                        .attr('title', error.text())
            }
        },

        highlight : function(element) {
            $(element).closest('.col-md-2').removeClass("has-success")
                    .addClass('has-error');
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
    com.orbps.contractEntry.longInsuranceEntry
            .applIlnfoValidateForm(com.orbps.contractEntry.longInsuranceEntry.applIlnfoForm);

    // 身份证号验证
    jQuery.validator.addMethod("isIdCardNo", function(value, element) {
        com.orbps.contractEntry.longInsuranceEntry.appIdTypeValue = $(
                "#applIlnfo #idType").val();
        // 先判断证件类型是否为身份证再去校验,appIdTypeValue(证件类型的值)再各自需要的js里设置为全局变量,
        if (com.orbps.contractEntry.longInsuranceEntry.appIdTypeValue == "UW") {
        	com.orbps.contractEntry.longInsuranceEntry.isIdNo = this.optional(element) || idCardNoUtil.checkIdCardNo(value);
            return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
        } else {
            return true;
        }
    }, "请正确输入您的身份证号码！");

});

// 如果联系人证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别、年龄等
$("#idNo").blur(
        function() {
        	var idNo = $("#idNo").val();
        	if(idNo === null || "" === idNo){
       		 	lion.util.info("警告","证件号不能为空");
       		 	return false;
        	}else{
        		if(com.orbps.contractEntry.longInsuranceEntry.isIdNo){
        			idcard($("#applIlnfo #paperType"), $("#applIlnfo #idNo"),
        					$("#applIlnfo #birthDate"), $("#applIlnfo #sex"));
        		}
        	}
        });

// 当联系人证件类型改变的 时候，将五要素只读变成可写
$("#applIlnfo #paperType")
        .change(
                function() {
                    com.orbps.contractEntry.longInsuranceEntry.appConnIdTypeValue = $(
                            "#applIlnfo #paperType").val();
                    if ("UW" != com.orbps.contractEntry.grpInsurAppl.appConnIdTypeValue) {
                        $("#applIlnfo #birthDate").attr("readonly", false);
                        $("#applIlnfo #sex").attr("readonly", false);
                    }
                });

$("#applNo").blur(function() {
    var applNo = $("#applNo").val();
    if (applNo == null || "" == applNo) {
        lion.util.info("警告", "投保单号不能为空");
        return false;
    }
});

$("#customerNo").blur(function() {
    var customerNo = $("#customerNo").val();
    if (customerNo == null || "" == customerNo) {
        lion.util.info("警告", "投保单号不能为空");
        return false;
    }
});

$("#insuredChannel").blur(function() {
    var insuredChannel = $("#insuredChannel").val();
    if (insuredChannel == null || "" == insuredChannel) {
        lion.util.info("警告", "销售渠道不能为空");
        return false;
    }
});

$("#salesBranchNo").blur(function() {
    var salesBranchNo = $("#salesBranchNo").val();
    if (salesBranchNo == null || "" == salesBranchNo) {
        lion.util.info("警告", "销售机构号不能为空");
        return false;
    }
});

$("#customerNo").blur(function() {
    var customerNo = $("#customerNo").val();
    if (customerNo == null || "" == customerNo) {
        lion.util.info("警告", "顾客号不能为空");
        return false;
    }
});

$("#shiftPerson").blur(function() {
    var shiftPerson = $("#shiftPerson").val();
    if (shiftPerson == null || "" == shiftPerson) {
        lion.util.info("警告", "转移不能为空");
        return false;
    }
});

$("#Name").blur(function() {
    var Name = $("#Name").val();
    if (Name == null || "" == Name) {
        lion.util.info("警告", "姓名不能为空");
        return false;
    }
});

$("#paperType").blur(function() {
    var paperType = $("#paperType").val();
    if (paperType == null || "" == paperType) {
        lion.util.info("警告", "证件类型不能为空");
        return false;
    }
});

$("#idNo").blur(function() {
    var idNo = $("#idNo").val();
    if (idNo == null || "" == idNo) {
        lion.util.info("警告", "证件号码不能为空");
        return false;
    }
});

$("#occupationType").blur(function() {
    var occupationType = $("#occupationType").val();
    if (occupationType == null || "" == occupationType) {
        lion.util.info("警告", "职业类别不能为空");
        return false;
    }
});

$("#ptJobType").blur(function() {
    var ptJobType = $("#ptJobType").val();
    if (ptJobType == null || "" == ptJobType) {
        lion.util.info("警告", "兼职类别不能为空");
        return false;
    }

});

$("#sparePhone").blur(function() {
    var connName = $("#sparePhone").val();
    if (connName == null || "" == connName) {
        lion.util.info("警告", "备用电话不能为空");
        return false;
    }
});

$("#emergencyPerson").blur(function() {
    var connIdNo = $("#emergencyPerson").val();
    if (connIdNo == null || "" == connIdNo) {
        lion.util.info("警告", "紧急联系人不能为空");
        return false;
    }
});

$("#emergencyPhone").blur(function() {
    var connIdNo = $("#emergencyPhone").val();
    if (connIdNo == null || "" == connIdNo) {
        lion.util.info("警告", "紧急联系人电话不能为空");
        return false;
    }
});

$("#posAddress").blur(function() {
    var connPhone = $("#posAddress").val();
    if (connPhone == null || "" == connPhone) {
        lion.util.info("警告", "通讯地址不能为空");
        return false;
    }
});

$("#email").blur(function() {
    var email = $("#email").val();
    if (connPostcode == null || "" == connPostcode) {
        lion.util.info("警告", "电子邮箱不能为空");
        return false;
    }
});
$("#mobilePhone").blur(function() {
    var mobilePhone = $("#mobilePhone").val();
    if (mobilePhone == null || "" == mobilePhone) {
        lion.util.info("警告", "移动电话不能为空");
        return false;
    }
});

$("#appHomeFax").blur(function() {
    var appHomeFax = $("#appHomeFax").val();
    if (appHomeFax == null || "" == appHomeFax) {
        lion.util.info("警告", "传真号码不能为空");
        return false;
    }
});
$("#supplementMedical").blur(function() {
    var supplementMedical = $("#supplementMedical").val();
    if (supplementMedical == null || "" == supplementMedical) {
        lion.util.info("警告", "补充医疗不能为空");
        return false;
    }
});
$("#societyInsurance").blur(function() {
    var societyInsurance = $("#societyInsurance").val();
    if (societyInsurance == null || "" == societyInsurance) {
        lion.util.info("警告", "社会保险不能为空");
        return false;
    }
});
$("#lanPhone").blur(function() {
    var lanPhone = $("#lanPhone").val();
    if (lanPhone == null || "" == lanPhone) {
        lion.util.info("警告", "固定电话不能为空");
        return false;
    }
});

$("#Age").blur(function() {
    var Age = $("#Age").val();
    if (Age == null || "" == Age) {
        lion.util.info("警告", "年龄不能为空");
        return false;
    }
});
