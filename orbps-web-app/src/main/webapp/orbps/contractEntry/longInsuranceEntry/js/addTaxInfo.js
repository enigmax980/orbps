com.orbps.contractEntry.longInsuranceEntry.addTaxInfoForm = $("#addTaxInfo");
com.orbps.contractEntry.longInsuranceEntry.appIdTypeValue;
com.orbps.contractEntry.longInsuranceEntry.appConnIdTypeValue;
// 基本信息校验规则
com.orbps.contractEntry.longInsuranceEntry.addTaxInfoValidateForm = function(
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
            taxpayer : {
                required : true
            },
            taxpayerCode : {
                required : true,
            },
            accountCode : {
                required : true
            },
            accountName : {
                required : true
            },
            companyBankAccNo : {
                required : true,
                isluhmCheck : true
            },
            companyBankName : {
                required : true,
            },
        },
        messages : {

            taxpayer : {
                required : "纳税人姓名不能为空"
            },
            taxpayerCode : {
                required : "纳税识别号不能为空"

            },
            accountCode : {
                required : "请选择开户行代码"
            },
            companyBankName : {
                required : "开户名称不能为空"
            },
            companyBankAccNo : {
                required : "银行账号不能为空",
                isluhmCheck : "请输入正确银行卡号"
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
            .addTaxInfoValidateForm(com.orbps.contractEntry.longInsuranceEntry.addTaxInfoForm);

});

$("#addTaxInfo #taxpayer").blur(function() {
    var taxpayer = $("#taxpayer").val();
    if (taxpayer == null || "" == taxpayer) {
        lion.util.info("警告", "纳税人姓名不能为空");
        return false;
    }
});

$("#addTaxInfo #companyBankAccNo").blur(function() {
    var companyBankAccNo = $("#companyBankAccNo").val();
    if (companyBankAccNo == null || "" == companyBankAccNo) {
        lion.util.info("警告", "银行账号不能为空");
        return false;
    }
});

$("#addTaxInfo #taxpayerCode").blur(function() {
    var taxpayerCode = $("#taxpayerCode").val();
    if (taxpayerCode == null || "" == taxpayerCode) {
        lion.util.info("警告", "纳税识别号不能为空");
        return false;
    }
});

$("#addTaxInfo #companyBankName").blur(function() {
    var companyBankName = $("#companyBankName").val();
    if (companyBankName == null || "" == companyBankName) {
        lion.util.info("警告", "开户名称不能为空");
        return false;
    }
});

$("#addTaxInfo #bankNo").blur(function() {
    var bankNo = $("#bankNo").val();
    if (bankNo == null || "" == bankNo) {
        lion.util.info("警告", "银行账号不能为空");
        return false;
    }
});
