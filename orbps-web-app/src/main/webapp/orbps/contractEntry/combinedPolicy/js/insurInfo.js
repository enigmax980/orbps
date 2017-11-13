// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.combinedPolicy命名空间
com.orbps.contractEntry.combinedPolicy = {};
// 编辑或添加对话框
com.orbps.contractEntry.combinedPolicy.insurInfoForm = $('#insurInfoForm');
com.orbps.contractEntry.combinedPolicy.payForm = $("#payForm");
com.orbps.contractEntry.combinedPolicy.policyHolderForm = $("#policyHolderForm");
com.orbps.contractEntry.combinedPolicy.recognizeeForm = $("#recognizeeForm");
com.orbps.contractEntry.combinedPolicy.benesList = [];
// 基本信息校验规则
com.orbps.contractEntry.combinedPolicy.handleForm = function (vform) {
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
        },
        messages : {
            applNo : {
                required : '请输入投保单号',
                isNumLetter : '请输入字母或数字！'
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

    // combo组件初始化
    $("*").comboInitLoad();

    // 初始化校验信息
    com.orbps.contractEntry.combinedPolicy
            .handleForm(com.orbps.contractEntry.combinedPolicy.insurInfoForm);

    $("#applNo").blur(function() {
        var applNo = $("#applNo").val();
        if (applNo == null || "" == applNo) {
            lion.util.info("警告", "投保单号不能为空");
            return false;
        }
    });
});
