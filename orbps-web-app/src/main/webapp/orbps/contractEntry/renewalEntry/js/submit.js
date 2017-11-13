//编辑或添加对话框
com.orbps.contractEntry.renewalEntry.addDialog = $('#btnModel');
com.orbps.contractEntry.renewalEntry.insuredList = new Array();
com.orbps.contractEntry.renewalEntry.oranLevelList = new Array();
com.orbps.contractEntry.renewalEntry.grpInsurApplList = new Array();
com.orbps.contractEntry.renewalEntry.benesList = [];

com.common.insuredCount = 0;
com.common.insuredList = new Array();
com.common.insuredType = -1;
com.common.oranLevelCount = 0;
com.common.oranLevelList = new Array();
com.common.oranLevelType = -1;
$(function() {
    // 表单提交
    $("#btnSubmit")
            .click(
                    function() {

                        if (com.orbps.contractEntry.renewalEntry.applInfo
                                .validate().form()
                                && com.orbps.contractEntry.renewalEntry.atherInfo
                                        .validate().form()) {

                            if (validateSelectVal()) {
                                // // 获取要约信息下面的所有险种信息
                                var getAddRowsData = $("#fbp-editDataGrids")
                                        .editDatagrids("getRowsData");
                                // // 提交方法
                                var renewalEntryVo = {};
                                renewalEntryVo.applInfo = com.orbps.contractEntry.renewalEntry.applInfo
                                        .serializeObject();
                                renewalEntryVo.atherInfo = com.orbps.contractEntry.renewalEntry.atherInfo
                                        .serializeObject();
                                renewalEntryVo.insuredGroupModalVos = com.orbps.contractEntry.renewalEntry.insuredList;
                                renewalEntryVo.organizaHierarModalVos = com.orbps.contractEntry.renewalEntry.oranLevelList;
                                renewalEntryVo.busiPrdVos = getAddRowsData;
                                var responseVos = new Array();
                                for (var i = 0; i < com.orbps.contractEntry.renewalEntry.benesList.length; i++) {
                                    var array_element = com.orbps.contractEntry.renewalEntry.benesList[i];
                                    for (var j = 0; j < array_element.length; j++) {
                                        var array_elements = array_element[j];
                                        responseVos.push(array_elements);
                                    }

                                }
                                renewalEntryVo.responseVos = responseVos;
                                alert(JSON.stringify(renewalEntryVo));
                                lion.util
                                        .postjson(
                                                '/orbps/web/orbps/contractEntry/renew/submit',
                                                renewalEntryVo,
                                                successSubmit,
                                                null,
                                                com.orbps.contractEntry.renewalEntry.applInfo);

                            }
                        }
                    });

});

// 组织层次架构
$("#btnOranLevel").click(
        function() {
            com.orbps.contractEntry.renewalEntry.addDialog.empty();
            com.orbps.contractEntry.renewalEntry.addDialog.load(
                    "/orbps/orbps/public/modal/html/organizaHierarModal.html",
                    function() {
                        $(this).modal('toggle');
                        // combo组件初始化
                        $(this).comboInitLoad();
                        $(this).combotreeInitLoad();
                    });
            setTimeout(function() {
                // 回显
                reloadPublicOranLevelModalTable();
            }, 100);
        });

// 被保人分组
$("#btnGoup").click(
        function() {
            com.orbps.contractEntry.renewalEntry.addDialog.empty();
            com.orbps.contractEntry.renewalEntry.addDialog.load(
                    "/orbps/orbps/public/modal/html/insuredGroupModal.html",
                    function() {
                        $(this).modal('toggle');
                        $(this).comboInitLoad();
                        $(this).combotreeInitLoad();
                        // 刷新table
                    });
            setTimeout(function() {
                // 回显
                reloadPublicInsuredModalTable();
            }, 100);
        });

// datagrid组件初始化
$("*").datagridsInitLoad();
// 上一步下一步控件初始化
$("*").stepInitLoad();
// 日期初始化插件
$(".date-picker").datepicker({
    autoclose : true,
    language : 'zh-CN'
});
// 文件上传下载插件初始化
$("#fileTal").fileinput({
    'allowedFileExtensions' : [ 'jpg', 'png', 'gif', 'xlsx' ],
    'showUpload' : true,
    'showPreview' : false,
    'showCaption' : true,
    'browseClass' : 'btn btn-success',
});
$("#fileVal").fileinput({
    'allowedFileExtensions' : [ 'jpg', 'png', 'gif', 'xlsx' ],
    'showUpload' : true,
    'showPreview' : false,
    'showCaption' : true,
    'browseClass' : 'btn btn-success',
});
$("#fbp-editDataGrids").editDatagridsLoadById();
// 初始化edittable组件
// combo组件初始化
$("*").comboInitLoad();

// 添加成功回调函数
function successSubmit(obj, data, arg) {
    lion.util.info("提示", "提交团单录入信息成功");
    // 成功后刷新页面
    setTimeOut(function() {
        window.location.reload();
    }, 500);
}

function validateSelectVal() {

    // 职业类型
    var occupationType = $("#applInfo #occupationType").val();
    if (occupationType == null || "" == occupationType) {
        lion.util.info("警告", "职业类型不能为空");
        return false;
    }

    // 证件类型
    var idType = $("#applInfo #idType").val();
    if (idType == null || "" == idType) {
        lion.util.info("警告", "职业类型不能为空");
        return false;
    }

    // 联系人证件类型
    var contactsIdType = $("#applInfo #contactsIdType").val();
    if (contactsIdType == null || "" == contactsIdType) {
        lion.util.info("警告", "联系人证件类型不能为空");
        return false;
    }

    // 联系人性别
    var contactsSex = $("#applInfo #contactsSex").val();
    if (contactsSex == null || "" == contactsSex) {
        lion.util.info("警告", "联系人性别不能为空");
        return false;
    }

    // 联系人出生日期
    var contactsBirthlDate = $("#applInfo #contactsBirthlDate").val();
    if (contactsBirthlDate == null || "" == contactsBirthlDate) {
        lion.util.info("警告", "联系人出生日期不能为空");
        return false;
    }

    // 联系人出生日期
    var contactsBirthlDate = $("#applInfo #contactsBirthlDate").val();
    if (contactsBirthlDate == null || "" == contactsBirthlDate) {
        lion.util.info("警告", "联系人出生日期不能为空");
        return false;
    }

    // 联系人出生日期
    var Term = $("#atherInfo #Term").val();
    if (Term == null || "" == Term) {
        lion.util.info("警告", "联系人出生日期不能为空");
        return false;
    }

    // 结算方式
    var settlementMethod = $("#atherInfo #settlementMethod").val();
    if (settlementMethod == null || "" == settlementMethod) {
        lion.util.info("警告", "结算方式不能为空");
        return false;
    }

    // 结算日期
    var settlementlDate = $("#atherInfo #settlementlDate").val();
    if (settlementlDate == null || "" == settlementlDate) {
        lion.util.info("警告", "结算方式不能为空");
        return false;
    }

    // 缴费形式
    var paymentForm = $("#atherInfo #paymentForm").val();
    if (paymentForm == null || "" == paymentForm) {
        lion.util.info("警告", "缴费形式不能为空");
        return false;
    }

    // 缴费方式
    var paymentMethod = $("#atherInfo #paymentMethod").val();
    if (paymentMethod == null || "" == paymentMethod) {
        lion.util.info("警告", "缴费形式不能为空");
        return false;
    }

    // 首期扣款日
    var initialStart = $("#atherInfo #initialStart").val();
    if (initialStart == null || "" == initialStart) {
        lion.util.info("警告", "首期扣款和截止日不能为空");
        return false;
    }

    // 截止日
    var deadlineDay = $("#atherInfo #deadlineDay").val();
    if (deadlineDay == null || "" == deadlineDay) {
        lion.util.info("警告", "首期扣款和截止日不能为空");
        return false;
    }

    // 保费来源
    var premiumSource = $("#atherInfo #premiumSource").val();
    if (premiumSource == null || "" == premiumSource) {
        lion.util.info("警告", "首期扣款和截止日不能为空");
        return false;
    }

    // 银行代码
    var bankCode = $("#atherInfo #bankCode").val();
    if (bankCode == null || "" == bankCode) {
        lion.util.info("警告", "银行代码不能为空");
        return false;
    }

    // 异常报告
    var exceptionReport = $("#atherInfo #exceptionReport").val();
    if (exceptionReport == null || "" == exceptionReport) {
        lion.util.info("警告", "异常报告不能为空");
        return false;
    }

    // 清单标志
    var listFlag = $("#atherInfo #listFlag").val();
    if (listFlag == null || "" == listFlag) {
        lion.util.info("警告", "清单标志不能为空");
        return false;
    }

    // 清单标志
    var listPrint = $("#atherInfo #listPrint").val();
    if (listPrint == null || "" == listPrint) {
        lion.util.info("警告", "清单标志不能为空");
        return false;
    }

    // 个人凭证
    var personalDigitalID = $("#atherInfo #personalDigitalID").val();
    if (personalDigitalID == null || "" == personalDigitalID) {
        lion.util.info("警告", "个人凭证不能为空");
        return false;
    }

    // 保单类型
    var policyType = $("#atherInfo #policyType").val();
    if (policyType == null || "" == policyType) {
        lion.util.info("警告", "保单类型不能为空");
        return false;
    }

    // 赠送保险标志
    var giftSign = $("#atherInfo #giftSign").val();
    if (giftSign == null || "" == giftSign) {
        lion.util.info("警告", "赠送保险标志不能为空");
        return false;
    }

    return true;
}
