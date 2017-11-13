//基本信息校验规则
com.orbps.contractEntry.coinsuranceAgreementEntry.cuForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            name : {
                required : true,
            },
            contactTel : {
            	required : true,
            	isMobile : true,
            },
            idNo :{
            	required : true,
            },
            email : {
            	email : true,
            },
            zipCode: {
            	required : true,
            	isZipCode : true
            },
            contactPsn:{
            	required : true,
            },
            homeAddress:{
            	required : true,
            },
        },
        messages : {
            name : {
                required : "客户名称不能为空",
            },
            contactTel : {
            	required : "联系人手机号不能为空",
            	isMobile : "请输入正确手机号码",
            },
            idNo :{
            	required : "证件号码不能为空",
            },
            email : {
            	email : "请输入正确的邮箱格式",
            },
            zipCode : {
                isZipCode : "请输入正确的邮编"
            },
            contactPsn:{
            	required : "联系人姓名不能为空",
            },
            homeAddress:{
            	required : "详细地址不能为空",
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
    com.orbps.contractEntry.coinsuranceAgreementEntry
            .cuForm(com.orbps.contractEntry.coinsuranceAgreementEntry.customer);

    $("#name").blur(function() {
        var name = $("#name").val();
        if (name == null || "" == name) {
            lion.util.info("警告", "客户名称不能为空");
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

    $("#zipCode").blur(function() {
        var zipCode = $("#zipCode").val();
        if (zipCode == null || "" == zipCode) {
            lion.util.info("警告", "邮政编码不能为空");
            return false;
        }
    });

    $("#contactPsn").blur(function() {
        var contactPsn = $("#contactPsn").val();
        if (contactPsn == null || "" == contactPsn) {
            lion.util.info("警告", "联系人姓名不能为空");
            return false;
        }
    });

    $("#contactTel").blur(function() {
        var contactTel = $("#contactTel").val();
        if (contactTel == null || "" == contactTel) {
            lion.util.info("警告", "联系人电话不能为空");
            return false;
        }
    });

    $("#homeAddress").blur(function() {
        var address = $("#homeAddress").val();
        if (address == null || "" == address) {
            lion.util.info("警告", "详细地址不能为空");
            return false;
        }
    });
   
});
