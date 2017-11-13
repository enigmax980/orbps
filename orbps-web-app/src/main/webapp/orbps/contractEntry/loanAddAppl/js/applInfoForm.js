// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.loanAddAppl命名空间
com.orbps.contractEntry.loanAddAppl = {};

// 编辑或添加对话框
com.orbps.contractEntry.loanAddAppl.addDialog = $('#btnModel');
com.orbps.contractEntry.loanAddAppl.insuredList = new Array();
com.orbps.contractEntry.loanAddAppl.oranLevelList = new Array();
com.orbps.contractEntry.loanAddAppl.grpInsurApplList = new Array();
com.orbps.contractEntry.loanAddAppl.applInfoForm = $('#applInfoForm');

// 基本信息校验规则
com.orbps.contractEntry.loanAddAppl.applValidateForm = function(vform) {
var error2 = $('.alert-danger', vform);
var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            applNo : {
                required : true,
                isNumLetter : true
            },
            applDate : {
                required : true
            },
            salesChannel : {
                required : true
            },
            salesBranch : {
                required : true
            },
            salesClerkNo : {
                required : true,
                isNumLetter : true
            }
        },
        messages : {
            applNo : {
                required : '请输入投保单号',
                isNumLetter : '投保单号必须为字母或数字'
            },
            applDate : {
                required : '请选择投保日期'
            },
            salesChannel : {
                required : '请选择销售渠道'
            },
            salesBranch : {
                required : '请输入销售机构'
            },
            salesClerkNo : {
                required : '请输入销售员号',
                isNumLetter : '销售员工号必须为字母或数字'
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
    // 日期初始化插件
    $('.date-picker').datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // 初始化校验方法
    com.orbps.contractEntry.loanAddAppl
            .applValidateForm(com.orbps.contractEntry.loanAddAppl.applInfoForm);

    // 丢失焦点的非空校验
    $('#applInfoForm #applNo').blur(function() {
        var applNo = $('#applNo').val();
        if (applNo == null || '' == applNo) {
            lion.util.info('警告', '投保单号不能为空');
            return false;
        }
    });

    $('#applInfoForm #salesBranch').blur(function() {
        var salesBranch = $('#salesBranch').val();
        if (salesBranch == null || '' == salesBranch) {
            lion.util.info('警告', '销售机构不能为空');
            return false;
        }
    });

    $('#applInfoForm #salesClerkNo').blur(function() {
        var salesClerkNo = $('#salesClerkNo').val();
        if (salesClerkNo == null || '' == salesClerkNo) {
            lion.util.info('警告', '销售员机构不能为空');
            return false;
        }
    });
});
