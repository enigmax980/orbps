com.orbps.common = {};
com.orbps.common.list = new Array();
// 增加表格
$("#offerInfo #btnAdd").click(function() {
    $("#fbp-editDataGrids").editDatagrids("addRows");
    return false;
});

// 删除表格
$("#offerInfo #btnDel").click(function() {
    $("#fbp-editDataGrids").editDatagrids("delRows");
    return false;
});

// 责任信息
$("#btnSelect")
        .click(
                function() {
                    var selectData = $("#fbp-editDataGrids").editDatagrids(
                            "getSelectRows");
                    // 判断选择的是否是一个主险，判断是否添加主险信息
                    if ((null == selectData) || (selectData.length == 0)
                            || (selectData.length > 1)) {
                        lion.util.info("警告", "请选择一个主险信息");
                        return false;
                    }

                    com.orbps.contractEntry.renewalEntry.addDialog.empty();
                    com.orbps.contractEntry.renewalEntry.addDialog
                            .load(
                                    "/orbps/orbps/public/modal/html/insurRespModal.html",
                                    function() {
                                        $(this).modal('toggle');
                                        // 初始化edittable组件
                                        $("#coverageInfo_list")
                                                .editDatagridsLoadById();
                                        // combo组件初始化
                                        $("#coverageInfo_list").editDatagrids(
                                                "queryparams", selectData);
                                        // 重新加载数据
                                        $("#coverageInfo_list").editDatagrids(
                                                "reload");
                                        setTimeout(
                                                function() {
                                                    // 循环已经选择的责任信息
                                                    if (com.orbps.common.list != "undefined") {
                                                        for (var i = 0; i < com.orbps.common.list.length; i++) {
                                                            var array_element = com.orbps.common.list[i];
                                                            // 判断选择的责任信息是一条还是多条
                                                            if (array_element.length >= 0) {
                                                                if (selectData.busiPrdCode == array_element[0].busiPrdCode) {
                                                                    // 回显责任信息
                                                                    $(
                                                                            "#coverageInfo_list")
                                                                            .editDatagrids(
                                                                                    "selectRows",
                                                                                    array_element);
                                                                }
                                                            } else {
                                                                if (selectData.busiPrdCode == array_element.busiPrdCode) {
                                                                    $(
                                                                            "#coverageInfo_list")
                                                                            .editDatagrids(
                                                                                    "selectRows",
                                                                                    array_element);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }, 800);
                                    });
                    return false;
                });