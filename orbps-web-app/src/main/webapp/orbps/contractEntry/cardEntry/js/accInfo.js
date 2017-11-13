com.orbps.contractEntry.cardEntry.accInfoForm = $('#accInfoForm');

// 基本信息校验规则
com.orbps.contractEntry.cardEntry.accValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            sumPrem : {
                required : true
            },
            moneyinType : {
                required : true
            },
            ernstMoneyinType : {
                required : true
            },
            receiptDate : {
                required : true
            },
            continuousInsurance : {
                required : true
            },
            currencyCode : {
                required : true
            },
            renewalFeeForm : {
                required : true
            },
            payBankCode : {
                required : true
            },
            bankaccNo : {
                required : true
            },
            accountForm : {
                required : true
            },
            accountHolder : {
                required : true
            },
            relToHldr : {
                required : true
            },
            ipsnIdNo : {
                required : true
            },
            signDate : {
                required : true
            },
            designatedEffectiveDate : {
                required : true
            },
            policyNature : {
                required : true
            },
            sendType : {
                required : true
            },
            insurancePeriodStartDate : {
                required : true
            },
            disputeResolution : {
                required : true
            },
            arbitrationCommissionName : {
                required : true
            },
            appOfficeTel : {
                required : true
            },
            insurancePeriodEndDate : {
                required : true
            }

        },
        messages : {
            sumPrem : {
                required : '请输入家庭电话'
            },
            moneyinType : {
                required : '请输入交费方式'
            },
            ernstMoneyinType : {
                required : '请选择首期交费形式'
            },
            receiptDate : {
                required : '请输入实收日期 '
            },
            continuousInsurance : {
                required : '请输入连续投保'
            },
            currencyCode : {
                required : '请输入币种'
            },
            renewalFeeForm : {
                required : '请输入续期交费形式 '
            },
            payBankCode : {
                required : '请选择银行代码'
            },
            bankaccNo : {
                required : '请输入缴费账号 '
            },
            accountForm : {
                required : '请输入账户形式'
            },
            accountHolder : {
                required : '请输入账户持有人 '
            },
            relToHldr : {
                required : '请选择是投保人的'
            },
            ipsnIdNo : {
                required : '请输入身份证号'
            },
            signDate : {
                required : '请输入签单日期'
            },
            designatedEffectiveDate : {
                required : '请输入指定生效日'
            },
            policyNature : {
                required : '请选择保单性质'
            },
            sendType : {
                required : '请输入送达类型'
            },
            insurancePeriodStartDate : {
                required : '请输入保险期间起始日期'
            },
            disputeResolution : {
                required : '请选择合同争议处理方式'
            },
            arbitrationCommissionName : {
                required : '请输入仲裁委员会名称'
            },
            appOfficeTel : {
                required : '请输入同业公司人身保险保额合计'
            },
            insurancePeriodEndDate : {
                required : '请输入终止日期 '
            }

        },
        invalidHandler : function(event, validator) {
            Metronic.scrollTo(error2, -200);
        },

        errorPlacement : function(error, element) {
            var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass('fa-warning');
            if (icon.attr('title')
                    || typeof icon.attr('data-original-title') != 'string') {
                icon.attr('data-original-title', icon.attr('title') || '')
                        .attr('title', error.text());
            }
        },

        highlight : function(element) {
            $(element).closest('.col-md-2').removeClass('has-success')
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
            icon.removeClass('fa-warning').addClass('fa-check');
        }
    });
};
$(function() {

    // 初始化校验信息
    com.orbps.contractEntry.cardEntry
            .accValidateForm(com.orbps.contractEntry.cardEntry.accInfoForm);

    $('#accInfoForm #bankaccNo').blur(function() {
        var bankaccNo = $('#accInfoForm #bankaccNo').val();
        if (bankaccNo == null || '' == bankaccNo) {
            lion.util.info('警告', '缴费账号不能为空');
            return false;
        }
    });

    $('#accInfoForm #accountHolder').blur(function() {
        var accountHolder = $('#accInfoForm #accountHolder').val();
        if (accountHolder == null || '' == accountHolder) {
            lion.util.info('警告', '账户持有人不能为空');
            return false;
        }
    });

    $('#accInfoForm #ipsnIdNo').blur(function() {
        var ipsnIdNo = $('#accInfoForm #ipsnIdNo').val();
        if (ipsnIdNo == null || '' == ipsnIdNo) {
            lion.util.info('警告', '身份证号不能为空');
            return false;
        }
    });

    $('#accInfoForm #arbitrationCommissionName').blur(
            function() {
                var arbitrationCommissionName = $(
                        '#accInfoForm #arbitrationCommissionName').val();
                if (arbitrationCommissionName == null
                        || '' == arbitrationCommissionName) {
                    lion.util.info('警告', '仲裁委员会名称不能为空');
                    return false;
                }
            });

    $('#accInfoForm #appOfficeTel').blur(function() {
        var appOfficeTel = $('#accInfoForm #appOfficeTel').val();
        if (appOfficeTel == null || '' == appOfficeTel) {
            lion.util.info('警告', '同业公司人身保险保额合计不能为空');
            return false;
        }
    });
});
