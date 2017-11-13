com.orbps.contractEntry = {};
com.orbps.contractEntry.cardEntry = {};
com.orbps.contractEntry.cardEntry.applInfoForm = $('#applInfoForm');

// 基本信息校验规则
com.orbps.contractEntry.cardEntry.applValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            cardNo : {
                required : true,
                isFloatGteZero : true
            },
            portionNum : {
                required : true
            },
            salesChannel : {
                required : true
            },
            salesBranchNo : {
                required : true,
                isIntGteZero : true
            },
            salesCode : {
                required : true,
                isIntGteZero : true
            },
            custNo : {
                required : true,
                isIntGteZero : true
            },
            name : {
                required : true,
                zh_verify : true
            },
            idType : {
                required : true
            },
            ipsnIdCode : {
                required : true,
                isIdCardNo : true
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
            ipsnOccClassCod : {
                required : true,
                zh_verify : true
            },
            occupationCategory : {
                required : true
            },
            companyName : {
                required : true,
                isNumLetter : true
            },
            tel : {
                required : true,
                isTel : true
            },
            post : {
                required : true,
                isNumLetter : true
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
            mPhneNo : {
                required : true,
                isMobile : true
            },
            email : {
                required : true
            }
        },
        messages : {
            cardNo : {
                required : '请输入数字组成的卡号',
                isFloatGteZero : '请输入数字组成的投保单号'
            },
            salesChannel : {
                required : '请输入销售渠道'
            },
            salesBranchNo : {
                required : '请输入销售员机构号'
            },
            salesCode : {
                required : '请输入销售员工号'
            },
            custNo : {
                required : '请输入投保人客户号'
            },
            name : {
                required : '请输入投保人姓名 '
            },
            idType : {
                required : '请输入投保人证件类别'
            },
            ipsnIdCode : {
                required : '请输入投保人证件号码'
            },
            sex : {
                required : '请选择投保人性别'
            },
            birthDate : {
                required : '请输入投保人日期'
            },
            age : {
                required : '请输入投保人年龄'
            },
            ipsnOccClassCod : {
                required : '请输入投保人职业代码'
            },
            occupationCategory : {
                required : '请输入投保人职业类别'
            },
            companyName : {
                required : '请输入投保人工作单位'
            },
            tel : {
                required : '请输入投保人办公电话'
            },
            post : {
                required : '请输入投保人职务'
            },
            incomeSource : {
                required : '请输入投保人收入来源'
            },
            communicateAddr : {
                required : '请输入投保人通讯地址'
            },
            nationality : {
                required : '请输入投保人国籍'
            },
            postCode : {
                required : '请输入投保人邮编 '
            },
            hPhoneNo : {
                required : '请输入投保人家庭电话'
            },
            mPhneNo : {
                required : '请输入投保人手机号'
            },
            email : {
                required : '请输入投保人电子邮件'
            },
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
    // 日期初始化插件
    $('.date-picker').datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // combo组件初始化
    $('*').comboInitLoad();

    // 初始化校验信息
    com.orbps.contractEntry.cardEntry
            .applValidateForm(com.orbps.contractEntry.cardEntry.applInfoForm);

    // 焦点离开的非空校验
    $('#applInfoForm #cardNo').blur(function() {
        var cardNo = $('#applInfoForm #cardNo').val();
        if (cardNo == null || '' == cardNo) {
            lion.util.info('警告', '卡号代码不能为空');
            return false;
        }
    });
    $('#applInfoForm #portionNum').blur(function() {
        var portionNum = $('#applInfoForm #portionNum').val();
        if (portionNum == null || '' == portionNum) {
            lion.util.info('警告', '份数代码不能为空');
            return false;
        }
    });

    $('#applInfoForm #salesBranchNo').blur(function() {
        var salesBranchNo = $('#applInfoForm #salesBranchNo').val();
        if (salesBranchNo == null || '' == salesBranchNo) {
            lion.util.info('警告', '销售员机构号不能为空');
            return false;
        }
    });
    $('#applInfoForm #salesCode').blur(function() {
        var saleCode = $('#applInfoForm #salesCode').val();
        if (saleCode == null || '' == saleCode) {
            lion.util.info('警告', '销售员工号不能为空');
            return false;
        }
    });
    $('#applInfoForm #custNo').blur(function() {
        var custNo = $('#applInfoForm #custNo').val();
        if (custNo == null || '' == custNo) {
            lion.util.info('警告', '投保人客户号不能为空');
            return false;
        }
    });
    $('#applInfoForm #name').blur(function() {
        var name = $('#applInfoForm #name').val();
        if (name == null || '' == name) {
            lion.util.info('警告', '投保人姓名不能为空');
            return false;
        }
    });
    $('#applInfoForm #ipsnIdCode').blur(function() {
        var ipsnIdCode = $('#applInfoForm #ipsnIdCode').val();
        if (ipsnIdCode == null || '' == ipsnIdCode) {
            lion.util.info('警告', '投保人证件号码不能为空');
            return false;
        }
    });
    $('#applInfoForm #age').blur(function() {
        var age = $('#applInfoForm #age').val();
        if (age == null || '' == age) {
            lion.util.info('警告', '投保人年龄不能为空');
            return false;
        }
    });
    $('#applInfoForm #occupationCategory').blur(function() {
        var occupationCategory = $('#applInfoForm #occupationCategory').val();
        if (occupationCategory == null || '' == occupationCategory) {
            lion.util.info('警告', '投保人职业类别不能为空');
            return false;
        }
    });
    $('#applInfoForm #companyName').blur(function() {
        var companyName = $('#applInfoForm #companyName').val();
        if (companyName == null || '' == companyName) {
            lion.util.info('警告', '投保人工作单位不能为空');
            return false;
        }
    });
    $('#applInfoForm #tel').blur(function() {
        var tel = $('#applInfoForm #tel').val();
        if (tel == null || '' == tel) {
            lion.util.info('警告', '投保人办公电话不能为空');
            return false;
        }
    });
    $('#applInfoForm #post').blur(function() {
        var post = $('#applInfoForm #post').val();
        if (post == null || '' == post) {
            lion.util.info('警告', '投保人职务不能为空');
            return false;
        }
    });
    $('#applInfoForm #incomeSource').blur(function() {
        var incomeSource = $('#applInfoForm #incomeSource').val();
        if (incomeSource == null || '' == incomeSource) {
            lion.util.info('警告', '投保人收入来源不能为空');
            return false;
        }
    });
    $('#applInfoForm #communicateAddr').blur(function() {
        var communicateAddr = $('#applInfoForm #communicateAddr').val();
        if (communicateAddr == null || '' == communicateAddr) {
            lion.util.info('警告', '投保人通讯地址不能为空');
            return false;
        }
    });
    $('#applInfoForm #postCode').blur(function() {
        var postCode = $('#applInfoForm #postCode').val();
        if (postCode == null || '' == postCode) {
            lion.util.info('警告', '投保人邮编不能为空');
            return false;
        }
    });
    $('#applInfoForm #hPhoneNo').blur(function() {
        var hPhoneNo = $('#applInfoForm #hPhoneNo').val();
        if (hPhoneNo == null || '' == hPhoneNo) {
            lion.util.info('警告', '投保人家庭电话不能为空');
            return false;
        }
    });
    $('#applInfoForm #mPhneNo').blur(function() {
        var mPhneNo = $('#applInfoForm #mPhneNo').val();
        if (mPhneNo == null || '' == mPhneNo) {
            lion.util.info('警告', '投保人手机号不能为空');
            return false;
        }
    });
    $('#applInfoForm #email').blur(function() {
        var email = $('#applInfoForm #email').val();
        if (email == null || '' == email) {
            lion.util.info('警告', '投保人电子邮件不能为空');
            return false;
        }
    });
    // 要约信息、特约信息需要查询数据库回显，不可更改
});
