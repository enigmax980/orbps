com.orbps.otherFunction = {};
com.orbps.otherFunction.manualApproval = {};
com.orbps.otherFunction.manualApproval.parentPage = {};
com.orbps.otherFunction.manualApproval.parentPage.pendingApprovalForm = $("#pendingApprovalForm");
com.orbps.otherFunction.manualApproval.parentPage.apTaskList = new Array();
com.orbps.otherFunction.manualApproval.parentPage.apTaskCount = 0;
com.orbps.otherFunction.manualApproval.parentPage.apTaskType = -1;
com.orbps.otherFunction.manualApproval.parentPage.apTaskListTab = $('#apTaskListTab');
// 编辑或添加对话框
com.orbps.otherFunction.manualApproval.parentPage.addDialog = $('#btnModel');
com.orbps.otherFunction.manualApproval.parentPage.applNo;
$(function() {

    // 时间控件初始化
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // datagrid组件初始化
    $("*").datagridsInitLoad();

    // combo组件初始化
    $("*").comboInitLoad();

    var approvalType;
    approvalType = "通用";
    
    var cntrType = "G";

    // 查询审批任务信息
    $("#pendingApprovalForm #btnQuery")
            .click(
                    function() {
                        // 获取投保单号
                        var applNo = $("#applNo").val();
                        if (applNo == "undefined" || applNo == "") {
                            lion.util.info('提示', '请输入投保单号');
                            return false;
                        } else {
                            // 添加查询参数
                            com.orbps.otherFunction.manualApproval.parentPage.apTaskListTab
                                    .datagrids({
                                        querydata : com.orbps.otherFunction.manualApproval.parentPage.pendingApprovalForm
                                                .serializeObject()
                                    });
                            // 重新加载数据
                            com.orbps.otherFunction.manualApproval.parentPage.apTaskListTab
                                    .datagrids('reload');
                        }
                    });

    // 审批信息
    $("#pendingApprovalForm #btnApproval")
            .click(
                    function() {
                        com.orbps.otherFunction.manualApproval.parentPage.addDialog
                                .empty();
                        if (cntrType == "G") {
                            com.orbps.otherFunction.manualApproval.parentPage.addDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/grpInsurAppl/html/grpInsurAppl.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            setTimeout(
                                    function() {
                                        // 获取投保单号
                                        com.orbps.otherFunction.manualApproval.parentPage.applNo = $(
                                                "#applNo").val();
                                        
                                        if (applNo != null) {
                                            lion.util
                                                    .postjson(
                                                            '/orbps/web/orbps/otherfunction/manualapproval/grpquery',
                                                            com.orbps.otherFunction.manualApproval.parentPage.applNo,
                                                            successQuery,
                                                            null,
                                                            null);
                                        } else {
                                            lion.util.info('提示', '请输入投保单号');
                                            return false;
                                        }
                                    }, 1000);
                        }
                        // 9.30版本暂时不开发通用审批和生效日审批功能，相关代码进行注释。
                        // if (approvalType == "通用") {
                        // // 加载通用审批界面
                        // com.orbps.otherFunction.manualApproval.parentPage.addDialog
                        // .load(
                        // "/orbps/orbps/otherFunction/manualApproval/parentPage/html/usualApprovalModal.html",
                        // function() {
                        // $(this).modal('toggle');
                        // });
                        // setTimeout(
                        // function() {
                        // // 获取投保单号
                        // com.orbps.otherFunction.manualApproval.parentPage.applNo
                        // = $(
                        // "#applNo").val();
                        // if (applNo != null) {
                        // lion.util
                        // .postjson(
                        // '/orbps/web/orbps/otherfunction/manualapproval/query',
                        // com.orbps.otherFunction.manualApproval.parentPage.applNo,
                        // successQuery,
                        // null,
                        // com.orbps.otherFunction.manualApproval.parentPage.pendingApprovalForm);
                        // } else {
                        // lion.util.info('提示', '请输入投保单号');
                        // return false;
                        // }
                        // }, 1000);
                        // } else if (approvalType == "生效日") {
                        // // 加载生效日审批界面
                        // com.orbps.otherFunction.manualApproval.parentPage.addDialog
                        // .load(
                        // "/orbps/orbps/otherFunction/manualApproval/parentPage/html/validDateApprovalModal.html",
                        // function() {
                        // $(this).modal('toggle');
                        // });
                        // setTimeout(
                        // function() {
                        // // 获取投保单号
                        // com.orbps.otherFunction.manualApproval.parentPage.applNo
                        // = $(
                        // "#applNo").val();
                        // if (applNo != null) {
                        // lion.util
                        // .postjson(
                        // '/orbps/web/orbps/otherfunction/manualapproval/queryUsual',
                        // com.orbps.otherFunction.manualApproval.parentPage.applNo,
                        // successQuery,
                        // null,
                        // com.orbps.otherFunction.manualApproval.parentPage.pendingApprovalForm);
                        // } else {
                        // lion.util.info('提示', '请输入投保单号');
                        // return false;
                        // }
                        // }, 1000);
                        // }
                    });

});

// 查询成功回调函数
function successQuery(obj, data, arg) {

    if (approvalType == "1通用") {
        // 获取查询入参
        $("#apTaskInfoTab").datagrids({
            querydata : data.busiPrdCode
        });
        // 重新加载险种表格
        $("#apTaskInfoTab").datagrids('reload');
    } else {
        // 获取查询入参
        $("#apTaskInfoTab").datagrids({
            querydata : data.busiPedCode
        });
        // 重新加载险种表格
        $("#apTaskInfoTab").datagrids('reload');
    }
    setTimeout(function() {
        var jsonStrApplicant = JSON.stringify(data);
        var objApplicant = eval("(" + jsonStrApplicant + ")");
        var key, value, tagName, type, arr;
        for (x in objApplicant) {
            key = x;
            value = objApplicant[x];
            $("[name='" + key + "'],[name='" + key + "[]']").each(function() {
                tagName = $(this)[0].tagName;
                type = $(this).attr('type');
                if (tagName == 'INPUT') {
                    if (type == 'radio') {
                        $(this).attr('checked', $(this).val() == value);
                    } else if (type == 'checkbox') {
                        arr = value.split(',');
                        for (var i = 0; i < arr.length; i++) {
                            if ($(this).val() == arr[i]) {
                                $(this).attr('checked', true);
                                break;
                            }
                        }
                    } else {
                        $("#" + key).val(value);
                    }
                } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                    $("#" + key).combo("val", value);
                }
            });
        }

        $("#approvalInfoForm input[type='text']").attr("readonly", true);
        $("#approvalInfoForm select").attr("readonly", true);
        $("#approvalInfoForm #appNo").attr("readonly", false);
    }, 100);

}
