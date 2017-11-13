// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.cardEntry命名空间
com.orbps.contractEntry.cardEntry = {};
// 新建contractEntry.grpInsurAppl命名空间
com.orbps.contractEntry.grpInsurAppl = {};
// treegrid
com.orbps.contractEntry.cardEntry.tableId = $('#treegridId');
// 编辑或添加对话框
com.orbps.contractEntry.cardEntry.addDialog = $('#btnModel');
com.orbps.contractEntry.cardEntry.insuredList = new Array();
com.orbps.contractEntry.cardEntry.oranLevelList = new Array();
com.orbps.contractEntry.cardEntry.grpInsurApplList = new Array();
com.orbps.contractEntry.cardEntry.benesList = [];
com.orbps.contractEntry.cardEntry.applInfoForm = $('#applInfoForm');
com.orbps.contractEntry.cardEntry.busiPrdCode = null;
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
            ipsnIdType : {
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
            ccupationCategory : {
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
            ipsnIdType : {
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
                required : '请输入销售渠道',

            },
            salesBranchNo : {
                required : '请输入销售员机构号',

            },
            salesCode : {
                required : '请输入销售员工号',

            },
            custNo : {
                required : '请输入客户号',

            },
            name : {
                required : '请输入姓名 '
            },
            ipsnIdType : {
                required : '请输入证件类别'
            },
            ipsnIdCode : {
                required : '请输入证件号码'
            },
            sex : {
                required : '请输入性别',

            },
            birthDate : {
                required : '请输出生日期'
            },
            age : {
                required : '请输入年龄'
            },
            ipsnOccClassCod : {
                required : '请输入职业代码'
            },
            ccupationCategory : {
                required : '请输入职业类别'
            },
            companyName : {
                required : '请输入工作单位',

            },
            tel : {
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
            ipsnIdType : {
                required : '请输入国籍'
            },
            postCode : {
                required : '请输入邮编 '
            },
            hPhoneNo : {
                required : '请输入家庭电话'
            },
            mPhneNo : {
                required : '请输入手机号'
            },
            email : {
                required : '请输入电子邮件'
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
    // 初始化校验信息
    com.orbps.contractEntry.cardEntry
            .applValidateForm(com.orbps.contractEntry.cardEntry.applInfoForm);

    $('#cardNo').blur(function() {
        var cardNo = $('#cardNo').val();
        if (cardNo == null || '' == cardNo) {
            lion.util.info('警告', '卡号代码不能为空');
            return false;
        }
    });
    $('#portionNum').blur(function() {
        var portionNum = $('#portionNum').val();
        if (portionNum == null || '' == portionNum) {
            lion.util.info('警告', '份数代码不能为空');
            return false;
        }
    });
    $('#salesChannel').blur(function() {
        var salesChannel = $('#salesChannel').val();
        if (salesChannel == null || '' == salesChannel) {
            lion.util.info('警告', '销售渠道代码不能为空');
            return false;
        }
    });

    $('#salesBranchNo').blur(function() {
        var salesBranchNo = $('#salesBranchNo').val();
        if (salesBranchNo == null || '' == salesBranchNo) {
            lion.util.info('警告', '销售员机构号不能为空');
            return false;
        }
    });
    $('#salesCode').blur(function() {
        var saleCode = $('#salesCode').val();
        if (saleCode == null || '' == saleCode) {
            lion.util.info('警告', '销售员工号不能为空');
            return false;
        }
    });
    $('#custNo').blur(function() {
        var custNo = $('#custNo').val();
        if (custNo == null || '' == custNo) {
            lion.util.info('警告', '客户号不能为空');
            return false;
        }
    });
    $('#name').blur(function() {
        var name = $('#name').val();
        if (name == null || '' == name) {
            lion.util.info('警告', '姓名不能为空');
            return false;
        }
    });
    $('#ipsnIdType').blur(function() {
        var ipsnIdType = $('#ipsnIdType').val();
        if (ipsnIdType == null || '' == ipsnIdType) {
            lion.util.info('警告', '证件类别不能为空');
            return false;
        }
    });
    $('#ipsnIdCode').blur(function() {
        var ipsnIdCode = $('ipsnIdCode').val();
        if (ipsnIdCode == null || '' == ipsnIdCode) {
            lion.util.info('警告', '证件号码不能为空');
            return false;
        }
    });
    $('#sex').blur(function() {
        var sex = $('ipsnIdCode').val();
        if (ipsnIdCode == null || '' == ipsnIdCode) {
            lion.util.info('警告', '性别号码不能为空');
            return false;
        }
    });
    $('#birthDate').blur(function() {
        var birthDate = $('birthDate').val();
        if (birthDate == null || '' == birthDate) {
            lion.util.info('警告', '出生日期号码不能为空');
            return false;
        }
    });
    $('#age').blur(function() {
        var age = $('age').val();
        if (age == null || '' == age) {
            lion.util.info('警告', '年龄号码不能为空');
            return false;
        }
    });
    $('#ipsnOccClassCod').blur(function() {
        var ipsnOccClassCod = $('ipsnOccClassCod').val();
        if (ipsnOccClassCod == null || '' == ipsnOccClassCod) {
            lion.util.info('警告', '职业代码不能为空');
            return false;
        }
    });
    $('#occupationCategory').blur(function() {
        var occupationCategory = $('occupationCategory').val();
        if (occupationCategory == null || '' == occupationCategory) {
            lion.util.info('警告', '职业类别不能为空');
            return false;
        }
    });
    $('#companyName').blur(function() {
        var companyName = $('companyName').val();
        if (companyName == null || '' == companyName) {
            lion.util.info('警告', '工作单位不能为空');
            return false;
        }
    });
    $('#tel').blur(function() {
        var tel = $('tel').val();
        if (tel == null || '' == tel) {
            lion.util.info('警告', '办公电话不能为空');
            return false;
        }
    });
    $('#tel').blur(function() {
        var tel = $('tel').val();
        if (tel == null || '' == tel) {
            lion.util.info('警告', '办公电话不能为空');
            return false;
        }
    });
    $('#post').blur(function() {
        var post = $('post').val();
        if (post == null || '' == post) {
            lion.util.info('警告', '职务不能为空');
            return false;
        }
    });
    $('#incomeSource').blur(function() {
        var incomeSource = $('incomeSource').val();
        if (incomeSource == null || '' == incomeSource) {
            lion.util.info('警告', '收入来源不能为空');
            return false;
        }
    });
    $('#communicateAddr').blur(function() {
        var communicateAddr = $('communicateAddr').val();
        if (communicateAddr == null || '' == communicateAddr) {
            lion.util.info('警告', '通讯地址不能为空');
            return false;
        }
    });
    $('#nationality').blur(function() {
        var nationality = $('nationality').val();
        if (nationality == null || '' == nationality) {
            lion.util.info('警告', '国籍不能为空');
            return false;
        }
    });
    $('#postCode').blur(function() {
        var postCode = $('postCode').val();
        if (postCode == null || '' == postCode) {
            lion.util.info('警告', '邮编不能为空');
            return false;
        }
    });
    $('#hPhoneNo').blur(function() {
        var hPhoneNo = $('hPhoneNo').val();
        if (hPhoneNo == null || '' == hPhoneNo) {
            lion.util.info('警告', '家庭电话不能为空');
            return false;
        }
    });
    $('#mPhneNo').blur(function() {
        var mPhneNo = $('mPhneNo').val();
        if (mPhneNo == null || '' == mPhneNo) {
            lion.util.info('警告', '手机号不能为空');
            return false;
        }
    });
    $('#email').blur(function() {
        var email = $('email').val();
        if (email == null || '' == email) {
            lion.util.info('警告', '电子邮件不能为空');
            return false;
        }
    });
    // 要约信息、特约信息需要查询数据库回显，不可更改
});
