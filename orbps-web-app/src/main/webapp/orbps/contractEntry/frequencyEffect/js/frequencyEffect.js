//定义命名空间
com.orbps.contractEntry = {};
com.orbps.contractEntry.frequencyEffect = {};
com.orbps.contractEntry.frequencyEffect.polList = new Array();
com.orbps.contractEntry.frequencyEffect.applInfoForm = $("#applInfoForm")
com.orbps.contractEntry.frequencyEffect.addDialog = $('#btnModel');

$(function() {
    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // 初始化edittable组件
    $("#polListInfo").editDatagridsLoadById();

    // 文件上传下载插件初始化
    $("#file").fileinput({
        'allowedFileExtensions' : [ 'jpg', 'png', 'gif', 'xlsx' ],
        'showUpload' : true,
        'showPreview' : false,
        'showCaption' : true,
        'browseClass' : 'btn btn-success',
    });

    // 增加表格
    $("#btnAdd").click(function() {
        $("#polListInfo").editDatagrids("addRows");
    });

    // 删除表格
    $("#btnDel").click(function() {
        $("#polListInfo").editDatagrids("delRows");
    });

    // 责任信息
    $("#btnSelect")
            .click(
                    function() {
                        var selectData = $("#polListInfo").editDatagrids(
                                "getSelectRows");
                        // 判断选择的是否是一个主险，判断是否添加主险信息
                        if ((null == selectData) || (selectData.length == 0)
                                || (selectData.length > 1)) {
                            lion.util.warning("警告", "请选择一个主险信息");
                            return false;
                        }
                        // com.orbps.contractEntry.frequencyEffect.addDialog.empty();

                        // 加载要约表格信息
                        com.orbps.contractEntry.frequencyEffect.addDialog
                                .load(
                                        "/orbps/orbps/contractEntry/modal/html/insurRespModal.html",
                                        function() {
                                            $(this).modal('toggle');
                                            // 初始化edittable组件
                                            $("#coverageInfo_list")
                                                    .editDatagridsLoadById();
                                            // combo组件初始化
                                            $("#coverageInfo_list")
                                                    .editDatagrids(
                                                            "queryparams",
                                                            selectData);
                                            // 重新加载数据
                                            $("#coverageInfo_list")
                                                    .editDatagrids("reload");
                                            setTimeout(
                                                    function() {
                                                        $("#coverageInfo_list")
                                                                .editDatagrids(
                                                                        "selectRows",
                                                                        com.orbps.contractEntry.grpInsurAppl.grpInsurApplList);
                                                    }, 400);
                                        });
                    });

});
