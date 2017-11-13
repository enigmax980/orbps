com.orbps.contractEntry.registrationAcceptance.filesListForm = $("#filesListForm");
com.orbps.contractEntry.registrationAcceptance.filesList = new Array();
com.orbps.contractEntry.registrationAcceptance.filesCount = 0;
com.orbps.contractEntry.registrationAcceptance.filesType = -1;
com.orbps.contractEntry.registrationAcceptance.files = $('#filesListTab');

$(function() {
    // 增加文件资料清单
    $("#btnfilesAdd").click(
            function() {
                var fileType = $("#fileType").val();
                var fileNum = $("#fileNum").val();
                var fileVo = $("#filesListForm").serializeObject();
                if ("" ===fileType  || "" === fileNum) {
                    lion.util.info("提示","请选择资料类型并输入资料数量");
                } else {
                    com.orbps.contractEntry.registrationAcceptance.filesList
                            .push(fileVo);
                    com.orbps.contractEntry.registrationAcceptance.filesCount++;
                    // 刷新table
                    com.orbps.contractEntry.registrationAcceptance.reloadfilesTable();
                }
                //重置资料类型的值
                $("#fileType").combo("refresh");
                $("#fileNum").val("");
            });
    // 删除汇交清单信息
    com.orbps.contractEntry.registrationAcceptance.deleteFilesList = function(
            vform) {
        if (confirm("您确认要删除此条记录吗？")) {
            var radioVal = this.name;
            if (com.orbps.contractEntry.registrationAcceptance.filesList != null
                    && com.orbps.contractEntry.registrationAcceptance.filesList.length > 0) {
                // alert(JSON.stringify(radioVal));//输出radioVal，JSON.stringify是将radioVal序列化。
                com.orbps.contractEntry.registrationAcceptance.filesList.splice(
                        vform, 1);
                com.orbps.contractEntry.registrationAcceptance.filesCount--;
            }
            // 刷新table
            com.orbps.contractEntry.registrationAcceptance.reloadfilesTable();
        }
    };
    // 修改清单信息
    $("#btnfilesUpdate")
            .click(
                    function() {
                        var temp = document.getElementsByName("filesListRad");
                        var fileType = $("#fileType").val();
                        var fileNum = $("#fileNum").val();
                        var fileVo = $("#filesListForm").serializeObject();
                        var j = 0;// 用于判断是否有选择的记录
                        for (var i = 0; i < temp.length; i++) {
                            if (temp[i].checked) {
                                if ("" ===fileType  || "" === fileNum) {
                                    lion.util.info("提示","请选择资料类型并输入资料数量");
                                    return false;
                                } else {
                                    com.orbps.contractEntry.registrationAcceptance.filesType = temp[i].value;
                                    var fileVo = $("#filesListForm")
                                            .serializeObject();
                                    com.orbps.contractEntry.registrationAcceptance.filesList[com.orbps.contractEntry.registrationAcceptance.filesType] = fileVo;
                                    com.orbps.contractEntry.registrationAcceptance.filesType = -1;
                                }
                            } else {
                                j++;
                            }
                        }
                        if (j === temp.length) {
                            lion.util.info("请选择一条记录");
                            return false;
                        }
                        $("#fileType").combo("refresh");
                        $("#fileNum").attr("value", "");
                        com.orbps.contractEntry.registrationAcceptance.reloadfilesTable();
                    });
    // 回显资料清单信息。
    com.orbps.contractEntry.registrationAcceptance.queryFilesList = function(
            vform) {
        $("#filesListForm input[type='text']").val("");
        $("#filesListForm select2").combo("refresh");
        var radioVal;
        // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
        var temp = document.getElementsByName("filesListRad");
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].checked) {
                radioVal = temp[i].value;
            }
        }
        com.orbps.contractEntry.registrationAcceptance.filesType = radioVal;
        var applVo = com.orbps.contractEntry.registrationAcceptance.filesList[radioVal];
        setTimeout(function() {
            // 回显
            com.orbps.contractEntry.registrationAcceptance.filesListForm.showFile(applVo);
        }, 100);
    };
});

// 重新加载表格
com.orbps.contractEntry.registrationAcceptance.reloadfilesTable = function() {
    $('#filesListTab').find("tbody").empty();
    if (com.orbps.contractEntry.registrationAcceptance.filesList != null
            && com.orbps.contractEntry.registrationAcceptance.filesList.length > 0) {
        for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.filesList.length; i++) {
            var filesType = com.orbps.publicSearch.filesTypeQuery(com.orbps.contractEntry.registrationAcceptance.filesList[i].fileType);
            $('#filesListTab')
                    .find("tbody")
                    .append(
                            "<tr><td style='width: 2px;'><input type='radio' onClick='com.orbps.contractEntry.registrationAcceptance.queryFilesList()' id='filesListRad"
                                    + i
                                    + "' name='filesListRad' value='"
                                    + i
                                    + "'>"
                                    + (i + 1)
                                    + "</td><td style='width: 40px;'>"
                                    + filesType
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.filesList[i].fileNum
                                    + "</td><td style='width: 5px;'><a href='javascript:void(0);' onclick='com.orbps.contractEntry.registrationAcceptance.deleteFilesList("
                                    + i
                                    + ");' for='filesListRad' id='filesListDel"
                                    + i + "'>删除</a></td></tr>");
        }
    } else {
        $('#filesListTab').find("tbody").append(
                "<tr><td colspan='4' align='center'>无记录</td></tr>");
    }
}

// 回显方法
com.orbps.contractEntry.registrationAcceptance.filesListForm.showFile = function(msg) {
    jsonStr = JSON.stringify(msg);
    var obj = eval("(" + jsonStr + ")");
    var key, value, tagName, type, arr;
    for (x in obj) {
        key = x;
        value = obj[x];
        $("[name='" + key + "'],[name='" + key + "[]']").each(function() {
            tagName = $(this)[0].tagName;
            type = $(this).attr('type');
            if (tagName === 'INPUT') {
                if (type === 'radio') {
                    $(this).attr('checked', $(this).val() == value);
                } else if (type == 'checkbox') {
                    arr = value.split(',');
                    for (var i = 0; i < arr.length; i++) {
                        if ($(this).val() === arr[i]) {
                            $(this).attr('checked', true);
                            break;
                        }
                    }
                } else {
                    // var holderListStr =
                    // key.replace("app","isd");将list元素中id带有app的替换成isd，回显出被保人信息。
                    $("#filesListForm #" + key).val(value);
                }
            } else if (tagName === 'SELECT' || tagName === 'TEXTAREA') {
                // var holderListSle = key.replace("app","isd");
                $("#filesListForm #" + key).combo("val", value);
            }
        });
    }
}