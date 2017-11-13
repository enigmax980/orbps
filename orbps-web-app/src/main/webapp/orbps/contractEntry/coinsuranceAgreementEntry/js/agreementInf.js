// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.coinsuranceAgreementEntry命名空间
com.orbps.contractEntry.coinsuranceAgreementEntry = {};
// 编辑或添加对话框
com.orbps.contractEntry.coinsuranceAgreementEntry.agreementInf = $('#agreementInfForm');
com.orbps.contractEntry.coinsuranceAgreementEntry.customer = $("#customerForm");

com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComForm = $("#insuranceComForm");
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList = new Array();
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComCount = 0;
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComType = -1;
var insuranceComVo;
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCom = $('#companyInfoTb');

com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceForm = $("#insuranceForm");
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList = new Array();
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCount = 0;
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceType = -1;
var insuredVo1;
com.orbps.contractEntry.coinsuranceAgreementEntry.insurance = $('#cntrInfoTb');
// 基本信息校验规则
com.orbps.contractEntry.coinsuranceAgreementEntry.handleForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
//            agreementNo : {
//                required : true,
//            },
            agreementName : {
                required : true,
            },
            mgrBranchNo : {
                required : true,
                isBranchNo : true
            },
        },
        messages : {
//            agreementNo : {
//                required : '请输入共保协议号',
//            },
            agreementName : {
                required : '请输入协议名称',
            },
            mgrBranchNo : {
                required : '请输入管理机构',
                isBranchNo : '机构为6位正整数！'
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
    // datagrid组件初始化
    // $("*").datagridsInitLoad();

    // combo组件初始化
    $("*").comboInitLoad();

    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    //省市县三级联动
	$("#citySelect").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        /* prov:"北京", */
        /* nodata:"none", */
        required:false
    });
    // 初始化校验信息
    com.orbps.contractEntry.coinsuranceAgreementEntry
            .handleForm(com.orbps.contractEntry.coinsuranceAgreementEntry.agreementInf);

    $("#agreementNo").blur(function() {
        var agreementNo = $("#agreementNo").val();
        if (agreementNo == null || "" == agreementNo) {
            lion.util.info("警告", "共保协议号不能为空");
            return false;
        }
    });

    $("#agreementName").blur(function() {
        var agreementName = $("#agreementName").val();
        if (agreementName == null || "" == agreementName) {
            lion.util.info("警告", "协议名称不能为空");
            return false;
        }
    });

    $("#mgrBranchNo").blur(function() {
        var mgrBranchNo = $("#mgrBranchNo").val();
        if (mgrBranchNo == null || "" == mgrBranchNo) {
            lion.util.info("警告", "管理机构不能为空");
            return false;
        }
    });
});
