com.orbps.otherFunction = {};
com.orbps.otherFunction.manualApproval = {};
com.orbps.otherFunction.manualApproval.childPage = {};
com.orbps.otherFunction.manualApproval.childPage.apTaskList = new Array();
com.orbps.otherFunction.manualApproval.childPage.apTaskCount = 0;
com.orbps.otherFunction.manualApproval.childPage.apTaskType = -1;
com.orbps.otherFunction.manualApproval.childPage.apTaskListTab = $('#apTaskListTab');
// 编辑或添加对话框
com.orbps.otherFunction.manualApproval.childPage.addDialog = $('#btnModel');
com.orbps.otherFunction.manualApproval.childPage.bizNo;
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
    
    setTimeout(function(){
		//个人凭证打印下拉框默认显示否
		$("#addInsurance #ipsnVoucherPrt").combo("val","N");
	},1000);

    com.orbps.otherFunction.manualApproval.childPage.bizNo=2;

    lion.util.postjson(
                        '/orbps/web/orbps/otherfunction/manualapproval/grpquery',
                        com.orbps.otherFunction.manualApproval.childPage.bizNo,
                        successQueryDetail);

});

//查询成功回调函数
function successQueryDetail(data,args) {
    //alert(1);
    setTimeout(function() {
        var jsonStrs = JSON.stringify(data);
        var objs = eval("(" + jsonStrs + ")");
        var form;
        //alert(JSON.stringify(data));
        for (y in objs) {
            //alert(1);
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
   //跟据险种信息设置页面样式，隐藏特殊险种模块
    if(lion.util.isNotEmpty(data.busiPrdVos[0].busiPrdCodeName)){
    var flag = data.busiPrdVos[0].busiPrdCodeName.indexOf("建筑");
    if(flag > 0){
    	$("#specialInsurAddInfoForm").show();
    }else{
    	$("#specialInsurAddInfoForm").hide();
    }
    }
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
