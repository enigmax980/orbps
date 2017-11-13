com.orbps.contractEntry.cardEntry.ipsnInfoForm = $('#ipsnInfoForm');

// 基本信息校验规则
com.orbps.contractEntry.cardEntry.ipsnValidateForm = function(vform) {
var error2 = $('.alert-danger', vform);
var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            relToHldr : {
                required : true
            },
            jointInsured : {
                required : true
            },
            custNo : {
                required : true,
                isIntGteZero : true
            },
            ipsnIdType : {
                required : true
            },
            ipsnIdCode : {
                required : true
            },
            name : {
                required : true,
                zh_verify : true
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
            companyName : {
                required : true
            },
            ipsnOccClassCod : {
                required : true
            },
            communicateAddr : {
                required : true
            },
            occupationCategory : {
                required : true,
            },
            postCode : {
                required : true,
                isZipCode : true
            },
            appOfficeTel : {
                required : true,
                isTel : true
            },
            mPhoneNo : {
                required : true
            },
            email : {
                required : true
            },
            healthStatFlag : {
                required : true
            },
            post : {
                required : true
            },
            incomeSource : {
                required : true
            },
            relToIpsn : {
                required : true
            },
            nationality : {
                required : true
            },
            ipsnSss : {
                required : true
            },
            officeTelephone : {
                required : true,
                isTel : true
            },
            hPhoneNo : {
                required : true,
                isTel : true

            },
        },
        messages : {
            relToHldr : {
                required : '请选择与投保人的关系'
            },
            jointInsured : {
                required : '请输入是否为连带被保人 '
            },
            custNo : {
                required : '请输入被保人客户号'
            },
            ipsnIdType : {
                required : '请输入被保人证件类别'
            },
            ipsnIdCode : {
                required : '请输入被保人证件号码'
            },
            name : {
                required : '请输入被保人姓名'
            },
            sex : {
                required : '请输入被保人性别'
            },
            birthDate : {
                required : '请选择被保人出生日期'
            },
            age : {
                required : '请输入被保人年龄'
            },
            companyName : {
                required : '请输入被保人工作单位'
            },
            ipsnOccClassCod : {
                required : '请输入被保人职业代码 '
            },
            communicateAddr : {
                required : '请输入被保人通讯地址'
            },
            occupationCategory : {
                required : '请输入被保人职业类别'
            },
            postCode : {
                required : '请输入被保人邮编'
            },
            appOfficeTel : {
                required : '请输入被保人联系电话'
            },
            mPhoneNo : {
                required : '请输入被保人手机号'
            },
            email : {
                required : '请输入被保人电子邮件'
            },
            healthStatFlag : {
                required : '请输入被保人异常告知'
            },
            post : {
                required : '请输入被保人职务'
            },
            incomeSource : {
                required : '请输入被保人收入来源'
            },
            relToIpsn : {
                required : '请输入与主被保人关系'
            },
            nationality : {
                required : '请输入被保人国籍'
            },
            ipsnSss : {
                required : '请选择被保人医保身份 '
            },
            officeTelephone : {
                required : '请输入被保人办公电话'
            },
            hPhoneNo : {
                required : '请输入被保人家庭电话'
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
            .ipsnValidateForm(com.orbps.contractEntry.cardEntry.ipsnInfoForm);
    

    $('#ipsnInfoForm #custNo').blur(function() {
        var jcustNo = $('#ipsnInfoForm #custNo').val();
        if (custNo == null || '' == custNo) {
            lion.util.info('警告', '被保人客户号不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #ipsnIdCode').blur(function() {
        var ipsnIdCode = $('#ipsnInfoForm #ipsnIdCode').val();
        if (ipsnIdCode == null || '' == ipsnIdCode) {
            lion.util.info('警告', '被保人证件号码不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #name').blur(function() {
        var name = $('#ipsnInfoForm #name').val();
        if (name == null || '' == name) {
            lion.util.info('警告', '被保人姓名不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #age').blur(function() {
        var age = $('#ipsnInfoForm #age').val();
        if (age == null || '' == age) {
            lion.util.info('警告', '被保人年龄不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #companyName').blur(function() {
        var companyName = $('#ipsnInfoForm #companyName').val();
        if (companyName == null || '' == companyName) {
            lion.util.info('警告', '被保人工作单位不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #communicateAddr').blur(function() {
        var communicateAddr = $('#ipsnInfoForm #communicateAddr').val();
        if (communicateAddr == null || '' == communicateAddr) {
            lion.util.info('警告', '被保人通讯地址不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #occupationCategory').blur(function() {
        var occupationCategory = $('#ipsnInfoForm #occupationCategory').val();
        if (occupationCategory == null || '' == occupationCategory) {
            lion.util.info('警告', '被保人职业类别不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #postCode').blur(function() {
        var postCode = $('#ipsnInfoForm #postCode').val();
        if (postCode == null || '' == postCode) {
            lion.util.info('警告', '被保人邮编不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #appOfficeTel').blur(function() {
        var appOfficeTel = $('#ipsnInfoForm #appOfficeTel').val();
        if (appOfficeTel == null || '' == appOfficeTel) {
            lion.util.info('警告', '被保人联系电话不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #mPhoneNo').blur(function() {
        var mPhoneNo = $('#ipsnInfoForm #mPhoneNo').val();
        if (mPhoneNo == null || '' == mPhoneNo) {
            lion.util.info('警告', '被保人手机号不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #email').blur(function() {
        var email = $('#ipsnInfoForm #email').val();
        if (email == null || '' == email) {
            lion.util.info('警告', '被保人电子邮件不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #healthStatFlag').blur(function() {
        var healthStatFlag = $('#ipsnInfoForm #healthStatFlag').val();
        if (healthStatFlag == null || '' == healthStatFlag) {
            lion.util.info('警告', '被保人异常告知不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #post').blur(function() {
        var post = $('#ipsnInfoForm #post').val();
        if (post == null || '' == post) {
            lion.util.info('警告', '被保人职务不能为空');
            return false;
        }
    });

    $('#ipsnInfoForm #incomeSource').blur(function() {
        var incomeSource = $('#ipsnInfoForm #incomeSource').val();
        if (incomeSource == null || '' == incomeSource) {
            lion.util.info('警告', '被保人收入来源为空');
            return false;
        }
    });

    $('#ipsnInfoForm #officeTelephone').blur(function() {
        var officeTelephone = $('#ipsnInfoForm #officeTelephone').val();
        if (officeTelephone == null || '' == officeTelephone) {
            lion.util.info('警告', '被保人办公电话为空');
            return false;
        }
    });

    $('#ipsnInfoForm #hPhoneNo').blur(function() {
        var hPhoneNo = $('#ipsnInfoForm #hPhoneNo').val();
        if (hPhoneNo === null || '' === hPhoneNo) {
            lion.util.info('警告', '被保人家庭电话为空');
            return false;
        }
    });
    
    setTimeout(function(){
    	$("#jointInsured").combo("val","N");
    },1000);
});
