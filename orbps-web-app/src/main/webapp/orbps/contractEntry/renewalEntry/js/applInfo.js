// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.renewalEntry命名空间
com.orbps.contractEntry.renewalEntry = {};
// applInfo
com.orbps.contractEntry.renewalEntry.applInfo = $('#applInfo');

// 基本信息校验规则
com.orbps.contractEntry.renewalEntry.applInfoValidateForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        messages : {
            renewalNotice : {
                required : "输入投保通知书"
            },
            previousPolicyNo : {
                required : "请输入上期投保单号",
                isIntGteZero : "请输入大于0的数字"
            },
            unitName : {
                required : "请输入单位/团体名称"
            },
            idNo : {
                required : "请输入证件号码",
                isNumLetter : "证件格式不正确"
            },
            posAddress : {
                required : "请输入通讯地址"
            },
            zipCode : {
                required : "请输入邮编",
                isZipCode : "邮编格式不正确"
            },
            contactsName : {
                required : "请输入联系人姓名"
            },
            contactsIdNo : {
                required : "请输入联系人证件号码"
            },
            contactsBirthlDate : {
                required : "请输入联系人出生日期"
            },
            contactsMobPhone : {
                required : "请输入联系人移动电话"
            },
            contactsEmail : {
                required : "请输入联系人邮箱"
            },
            lanPhone : {
                required : "请输入固定电话",
                isTel : "请输入正确格式电话号码"
            },
            faxNo : {
                required : "请输入传真号码",
                isFax : "请输入正确格式传真"
            },
            insureNumber : {
                required : "请输入投保人数",
                isIntGteZero : "格式不正确"
            },
            totalPremium : {
                required : "请输入总保费",
                isIntGteZero : "请输入大于0数字"
            },

        },
        rules : {
            renewalNotice : {
                required : true
            },
            previousPolicyNo : {
                required : true,
                isIntGteZero : true
            },
            unitName : {
                required : true
            },
            idNo : {
                required : true,
                isNumLetter : true
            },
            posAddress : {
                required : true
            },
            zipCode : {
                required : true,
                isZipCode : true
            },
            contactsName : {
                required : true
            },
            sex : {
                required : true
            },
            contactsIdNo : {
                required : true
            },
            contactsBirthlDate : {
                required : true
            },
            contactsMobPhone : {
                required : true
            },
            contactsEmail : {
                required : true
            },
            lanPhone : {
                required : true,
                isTel : true
            },
            faxNo : {
                required : true,
                isFax : true
            },
            insureNumber : {
                required : true,
                isIntGteZero : true
            },
            totalPremium : {
                required : true,
                isIntGteZero : true
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

    com.orbps.contractEntry.renewalEntry
            .applInfoValidateForm(com.orbps.contractEntry.renewalEntry.applInfo);
});

$(function() {

    $("#applInfo #renewalNotice").blur(function() {
        var renewalNotice = $("#renewalNotice").val();
        if (renewalNotice == null || "" == renewalNotice) {
            lion.util.info("警告", "续保通知书不能为空");
            return false;
        }
    });

    $("#applInfo #previousPolicyNo").blur(function() {
        var previousPolicyNo = $("#previousPolicyNo").val();
        if (previousPolicyNo == null || "" == previousPolicyNo) {
            lion.util.info("警告", "上期保单号不能为空");
            return false;
        }
    });

    $("#applInfo #unitName").blur(function() {
        var unitName = $("#unitName").val();
        if (unitName == null || "" == unitName) {
            lion.util.info("警告", "单位/团体名称不能为空");
            return false;
        }
    });

    $("#applInfo #idNo").blur(function() {
        var idNo = $("#idNo").val();
        if (idNo == null || "" == idNo) {
            lion.util.info("警告", "证件号码不能为空");
            return false;
        }
    });

    $("#applInfo #posAddress").blur(function() {
        var posAddress = $("#posAddress").val();
        if (posAddress == null || "" == posAddress) {
            lion.util.info("警告", "通讯地址不能为空");
            return false;
        }
    });

    $("#applInfo #zipCode").blur(function() {
        var zipCode = $("#zipCode").val();
        if (zipCode == null || "" == zipCode) {
            lion.util.info("警告", "邮编不能为空");
            return false;
        }
    });

    $("#applInfo #contactsName").blur(function() {
        var contactsName = $("#contactsName").val();
        if (contactsName == null || "" == contactsName) {
            lion.util.info("警告", "联系人姓名不能为空");
            return false;
        }
    });

    $("#applInfo #contactsIdNo").blur(function() {
        var contactsIdNo = $("#contactsIdNo").val();
        if (contactsIdNo == null || "" == contactsIdNo) {
            lion.util.info("警告", "联系人证件号码不能为空");
            return false;
        }
    });

    $("#applInfo #contactsMobPhone").blur(function() {
        var contactsMobPhone = $("#contactsMobPhone").val();
        if (contactsMobPhone == null || "" == contactsMobPhone) {
            lion.util.info("警告", "联系人移动电话不能为空");
            return false;
        }
    });

    $("#applInfo #contactsEmail").blur(function() {
        var contactsEmail = $("#contactsEmail").val();
        if (contactsEmail == null || "" == contactsEmail) {
            lion.util.info("警告", "联系人邮箱不能为空");
            return false;
        }
    });

    $("#applInfo #lanPhone").blur(function() {
        var lanPhone = $("#lanPhone").val();
        if (lanPhone == null || "" == lanPhone) {
            lion.util.info("警告", "联系人固定电话不能为空");
            return false;
        }
    });

    $("#applInfo #lanPhone").blur(function() {
        var lanPhone = $("#lanPhone").val();
        if (lanPhone == null || "" == lanPhone) {
            lion.util.info("警告", "联系人邮箱不能为空");
            return false;
        }
    });

    $("#applInfo #faxNo").blur(function() {
        var faxNo = $("#faxNo").val();
        if (faxNo == null || "" == faxNo) {
            lion.util.info("警告", "传真不能为空");
            return false;
        }
    });

    $("#applInfo #insureNumber").blur(function() {
        var insureNumber = $("#insureNumber").val();
        if (insureNumber == null || "" == insureNumber) {
            lion.util.info("警告", "投保人数不能为空");
            return false;
        }
    });

    $("#applInfo #totalPremium").blur(function() {
        var totalPremium = $("#totalPremium").val();
        if (totalPremium == null || "" == totalPremium) {
            lion.util.info("警告", "总保费不能为空");
            return false;
        }
    });
});
