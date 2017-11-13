//基本信息校验规则
com.orbps.contractEntry.combinedPolicy.policyHolderlidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            custNo : {
                digits : true
            },
            name : {
                required : true,
            },
            ipsnIdType : {
                required : true
            },
            ipsnIdCode : {
                required : true
            },
            sex : {
                required : true
            },
            birthDate : {
                required : true
            },
            age : {
                required : true
            },
            IpsnOccClassCod : {
                required : true
            },
            occupationCategory : {
                required : true
            },
            companyName : {
                required : true,
                zh_verify : true
            },
            officeTelephone : {
                required : true
            },
            post : {
                required : true
            },
            incomeSource : {
                required : true
            },
            communicateAddr : {
                required : true
            },
            nationality : {
                required : true
            },
            postCode : {
                required : true,
                isZipCode : true
            },
            hPhoneNo : {
                required : true,
                isTel : true
            },
            mPhneNO : {
                required : true,
                isMobile : true
            },
            mailBox : {
                required : true
            },

        },
        messages : {
            custNo : {
                digits : '请输入数字！'
            },
            name : {
                required : '请输入姓名',
            },
            ipsnIdType : {
                required : '请选择证件类别'
            },
            ipsnIdCode : {
                required : '请输入证件号码'
            },
            sex : {
                required : '请选择性别'
            },
            birthDate : {
                required : '请输入出生日期'
            },
            age : {
                required : '请输入年龄'
            },
            IpsnOccClassCod : {
                required : '请选择职业代码'
            },
            occupationCategory : {
                required : '请选择职业类别'
            },
            companyName : {
                required : '请输入工作单位',
                zh_verifyl : '请输入2~18个字的中文！'
            },
            officeTelephone : {
                required : '请输入办公电话'
            },
            post : {
                required : '请输入职务'
            },
            incomeSource : {
                required : '请输入收入来源'
            },
            communicateAddr : {
                required : '请输入通讯地址'
            },
            nationality : {
                required : '请选择国籍'
            },
            postCode : {
                required : '请输入邮编',
                isZipCode : '请正确填写您的邮政编码'
            },
            hPhoneNo : {
                required : '请输入家庭电话',
                isTel : '请正确填写您的电话号码'
            },
            mPhneNO : {
                required : '请输入手机号',
                isMobile : '请正确填写您的手机号码'
            },
            mailBox : {
                required : '请输入电子邮箱'
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
    // 初始化校验信息
    com.orbps.contractEntry.combinedPolicy
            .policyHolderlidateForm(com.orbps.contractEntry.combinedPolicy.policyHolderForm);

    $("#policyHolderForm #name").blur(function() {
        var name = $("#name").val();
        if (name == null || "" == name) {
            lion.util.info("警告", "姓名不能为空");
            return false;
        }
    });

    $("#policyHolderForm #ipsnIdCode").blur(function() {
        var ipsnIdCode = $("#ipsnIdCode").val();
        if (ipsnIdCode == null || "" == ipsnIdCode) {
            lion.util.info("警告", "证件号码不能为空");
            return false;
        }
    });

    $("#policyHolderForm #age").blur(function() {
        var age = $("#age").val();
        if (age == null || "" == age) {
            lion.util.info("警告", "年龄不能为空");
            return false;
        }
    });

    $("#policyHolderForm #companyName").blur(function() {
        var companyName = $("#companyName").val();
        if (companyName == null || "" == companyName) {
            lion.util.info("警告", "工作单位不能为空");
            return false;
        }
    });

    $("#policyHolderForm #officeTelephone").blur(function() {
        var officeTelephone = $("#officeTelephone").val();
        if (officeTelephone == null || "" == officeTelephone) {
            lion.util.info("警告", "办公电话不能为空");
            return false;
        }
    });

    $("#policyHolderForm #post").blur(function() {
        var post = $("#post").val();
        if (post == null || "" == post) {
            lion.util.info("警告", "职务不能为空");
            return false;
        }
    });

    $("#policyHolderForm #incomeSource").blur(function() {
        var incomeSource = $("#incomeSource").val();
        if (incomeSource == null || "" == incomeSource) {
            lion.util.info("警告", "收入来源不能为空");
            return false;
        }
    });

    $("#policyHolderForm #communicateAddr").blur(function() {
        var communicateAddr = $("#communicateAddr").val();
        if (communicateAddr == null || "" == communicateAddr) {
            lion.util.info("警告", "通讯地址不能为空");
            return false;
        }
    });

    $("#policyHolderForm #postCode").blur(function() {
        var postCode = $("#postCode").val();
        if (postCode == null || "" == postCode) {
            lion.util.info("警告", "邮编不能为空");
            return false;
        }
    });

    $("#policyHolderForm #hPhoneNo").blur(function() {
        var hPhoneNo = $("#hPhoneNo").val();
        if (hPhoneNo == null || "" == hPhoneNo) {
            lion.util.info("警告", "家庭电话不能为空");
            return false;
        }
    });

    $("#policyHolderForm #mPhneNO").blur(function() {
        var mPhneNO = $("#mPhneNO").val();
        if (mPhneNO == null || "" == mPhneNO) {
            lion.util.info("警告", "手机号不能为空");
            return false;
        }
    });

    $("#policyHolderForm #mailBox").blur(function() {
        var mailBox = $("#mailBox").val();
        if (mailBox == null || "" == mailBox) {
            lion.util.info("警告", "电子邮箱不能为空");
            return false;
        }
    });

});
