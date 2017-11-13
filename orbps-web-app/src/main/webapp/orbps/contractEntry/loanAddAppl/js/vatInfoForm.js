//定义form
com.orbps.contractEntry.loanAddAppl.vatInfoForm = $('#vatInfoForm');

// 基本信息校验规则
com.orbps.contractEntry.loanAddAppl.vatValidateForm = function(vform) {
var error2 = $('.alert-danger', vform);
var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            taxpayerName : {
                required : true
            },
            taxpayNo : {
                required : true,
                isNumLetter : true
            },
            bankCode : {
                required : true
            },
            accountName : {
                required : true
            },
            accountNo : {
                required : true,
                isluhmCheck : true
            }
        },
        messages : {
            taxpayerName : {
                required : '请输入纳税人名称'
            },
            taxpayNo : {
                required : '请输入纳税人识别号',
                isNumLetter : '纳税人识别号必须是字母或数字'
            },
            bankCode : {
                required : '请选择开户银行'
            },
            accountName : {
                required : '请输入开户姓名'
            },
            accountNo : {
                required : '请输入银行账号',
                isluhmCheck : '输入的银行卡号不正确'
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
            icon.removeClass('fa-warning').addClass('fa-check');
        }
    });
};

$(function() {
    // 调用校验方法
    com.orbps.contractEntry.loanAddAppl
            .vatValidateForm(com.orbps.contractEntry.loanAddAppl.vatInfoForm);

    // 丢失焦点的非空校验
    $('#vatInfoForm #taxpayerName').blur(function() {
        var taxpayerName = $('#taxpayerName').val();
        if (taxpayerName == null || '' == taxpayerName) {
            lion.util.info('警告', '纳税人姓名不能为空');
            return false;
        }
    });

    $('#vatInfoForm #taxpayNo').blur(function() {
        var taxpayNo = $('#taxpayNo').val();
        if (taxpayNo == null || '' == taxpayNo) {
            lion.util.info('警告', '纳税人识别号不能为空');
            return false;
        }
    });

    $('#vatInfoForm #accountName').blur(function() {
        var accountName = $('#accountName').val();
        if (accountName == null || '' == accountName) {
            lion.util.info('警告', '开户姓名不能为空');
            return false;
        }
    });

    $('#vatInfoForm #accountNo').blur(function() {
        var accountNo = $('#accountNo').val();
        if (accountNo == null || '' == accountNo) {
            lion.util.info('警告', '银行账号不能为空');
            return false;
        }
    });
});
