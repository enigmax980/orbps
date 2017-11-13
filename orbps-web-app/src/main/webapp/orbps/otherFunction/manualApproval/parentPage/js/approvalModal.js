com.orbps.otherFunction.manualApproval.parentPage.approvalInfoForm = $("#approvalInfoForm");
com.orbps.otherFunction.manualApproval.parentPage.usualListInfo = new Array();
com.orbps.otherFunction.manualApproval.parentPage.orgListInfo = new Array();
com.orbps.otherFunction.manualApproval.parentPage.busiPrdList = new Array();
com.orbps.otherFunction.manualApproval.parentPage.usualCount = 0;
// 编辑或添加对话框
com.orbps.otherFunction.manualApproval.parentPage.usualDialog = $('#approvalModel');

$(function() {
    // 投保信息界面
    $("#approvalInfoForm #btnApplInfo")
            .click(
                    function() {
                        // 定义insurType对象，获取查询到的契约形式，根据契约形式调用对应的查询界面
                        var insurType;
                        insurType = 2;
                        com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                .empty();
                        switch (insurType) {
                        case 0:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/cardEntry/html/cardEntry.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 1:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/combinedPolicy/html/combinedPolicy.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 2:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/grpInsurAppl/html/grpInsurAppl.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 3:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/loanAddAppl/html/loanAddForm.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 4:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/longInsuranceEntry/html/longInsuranceEntryForm.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 5:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/perInsurAppl/html/applInfoForm.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 6:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/renewalEntry/html/renewalEntryForm.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        case 7:
                            com.orbps.otherFunction.manualApproval.parentPage.usualDialog
                                    .load(
                                            "/orbps/orbps/otherFunction/manualApproval/childPage/sgGrpInsurAppl/html/sgGrpInsurAppl.html",
                                            function() {
                                                $(this).modal('toggle');
                                            });
                            break;
                        default:
                            break;
                        }
                        // 向后台发送请求
                        lion.util
                                .postjson(
                                        '/orbps/otherfunction/manualapproval/query',
                                        com.orbps.otherFunction.manualApproval.parentPage.applNo,
                                        successQueryDetail,
                                        null,
                                        com.orbps.otherFunction.manualApproval.parentPage.pendingApprovalForm);
                    });
});

// 查询成功回调函数
function successQueryDetail(obj, data, arg) {

    setTimeout(function() {
        var jsonStrs = JSON.stringify(data);
        var objs = eval("(" + jsonStrs + ")");
        var form;
        for (y in objs) {
            // form = y;
            // var keys = form;
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            var key, value, tagName, type, arr;
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                $("[name='" + key + "'],[name='" + key + "[]']").each(
                        function() {
                            tagName = $(this)[0].tagName;
                            type = $(this).attr('type');
                            if (tagName == 'INPUT') {
                                if (type == 'radio') {
                                    $(this).attr('checked',
                                            $(this).val() == value);
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
                            } else if (tagName == 'SELECT'
                                    || tagName == 'TEXTAREA') {
                                $("#" + key).combo("val", value);
                            }

                        });
            }
        }
    }, 100);

    if (insurType == "7") {
        // 获取查询入参
        $("#coverageInfo_list").datagrids({
            querydata : data.busiPedCode
        });
        // 重新加载险种表格
        $("#coverageInfo_list").datagrids('reload');

        // 获取查询入参
        $("#sys_applOranLevel_list_tb").datagrids({
            querydata : data.busiPedCode
        });
        // 重新加载责任组表格
        $("#sys_applOranLevel_list_tb").datagrids('reload');

        // 获取查询入参
        $("#sys_applinsured_list_tb").datagrids({
            querydata : data.busiPedCode
        });
        // 重新加载被保人表格
        $("#sys_applinsured_list_tb").datagrids('reload');

        // 录入框变为只读
        $("#sgGrpApplInfoForm input[type='text']").attr("readonly", true);
        $("#sgGrpApplInfoForm select").attr("readonly", true);
        $("#sgGrpApplInfoForm #appNo").attr("readonly", false);
    } else if (insurType == "1") {
        // 获取查询入参
        $("#beneficialList").datagrids({
            querydata : data.busiPedCode
        });
        // 重新加载被保人表格
        $("#beneficialList").datagrids('reload');

        // 获取查询入参
        $("#prosalList").datagrids({
            querydata : data.busiPedCode
        });
        // 重新加载被保人表格
        $("#prosalList").datagrids('reload');

        // 录入框变为只读
        $("#cardEntry input[type='text']").attr("readonly", true);
        $("#cardEntry select").attr("readonly", true);
        $("#cardEntry #appNo").attr("readonly", false);
    } else if (insurType == "5") {

    }

}
