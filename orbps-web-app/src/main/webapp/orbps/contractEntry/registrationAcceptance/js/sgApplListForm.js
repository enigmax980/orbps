com.orbps.contractEntry.registrationAcceptance.filesListForm = $("#filesListForm");
com.orbps.contractEntry.registrationAcceptance.applList = new Array();
com.orbps.contractEntry.registrationAcceptance.applCount = 0;
com.orbps.contractEntry.registrationAcceptance.applType = -1;
com.orbps.contractEntry.registrationAcceptance.appl = $('#applListTab');

$(function() {
    // 增加汇交清单
    $("#btnApplAdd").click(
            function() {
                var applNoBegin = $("#applNoBegin").val();
                var applNoEnd = $("#applNoEnd").val();
                var applVo = $("#filesListForm").serializeObject();
                if (applNoBegin == "" || applNoEnd == "") {
                    alert("请输入投保单号");
                } else {
                    com.orbps.contractEntry.registrationAcceptance.applList
                            .push(applVo);
                    com.orbps.contractEntry.registrationAcceptance.applCount++;
                    // 刷新table
                    reloadapplTable();
                }
                $("#applNoBegin").attr("value", "");
                $("#applNoEnd").attr("value", "");
                
            });
    // 删除汇交清单信息
    com.orbps.contractEntry.registrationAcceptance.deleteApplList = function(
            vform) {
        if (confirm("您确认要删除此条记录吗？")) {
            var radioVal = this.name;
            if (com.orbps.contractEntry.registrationAcceptance.applList != null
                    && com.orbps.contractEntry.registrationAcceptance.applList.length > 0) {
                // alert(JSON.stringify(radioVal));//输出radioVal，JSON.stringify是将radioVal序列化。
                com.orbps.contractEntry.registrationAcceptance.applList.splice(
                        vform, 1);
                com.orbps.contractEntry.registrationAcceptance.applCount--;
            }
            // 刷新table
            reloadapplTable();
        }
    };
    // 修改清单信息
    $("#btnApplModify")
            .click(
                    function() {
                        var applNoBegin = $("#applNoBegin").val();
                        var applNoEnd = $("#applNoEnd").val();
                        var applVo = $("#filesListForm").serializeObject();
                        if (applNoBegin == "" || applNoEnd == "") {
                            alert("请输入投保单号");
                        } else {
                            com.orbps.contractEntry.registrationAcceptance.applList[com.orbps.contractEntry.registrationAcceptance.applType] = applVo;
                            com.orbps.contractEntry.registrationAcceptance.applType = -1;
                            // 刷新table
                            reloadapplTable();
                        }
                        $("#applNoBegin").attr("value", "");
                        $("#applNoEnd").attr("value", "");
                    });
    // 回显汇交清单信息。
    com.orbps.contractEntry.registrationAcceptance.queryApplList = function(
            vform) {
        $("#filesListForm input[type='text']").val("");
        $("#filesListForm select2").combo("refresh");
        var radioVal;
        // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
        var temp = document.getElementsByName("applListRad");
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].checked) {
                radioVal = temp[i].value;
            }
        }
        com.orbps.contractEntry.registrationAcceptance.applType = radioVal;
        var applVo = com.orbps.contractEntry.registrationAcceptance.applList[radioVal];
        setTimeout(function() {
            // 回显
            showappl(applVo);
        }, 100);
    };
});

// 重新加载表格
function reloadapplTable() {
    $('#apTaskListTabOne').find("tbody").empty();
    if (com.orbps.contractEntry.registrationAcceptance.applList != null
            && com.orbps.contractEntry.registrationAcceptance.applList.length > 0) {
        for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.applList.length; i++) {
            $('#apTaskListTabOne')
                    .find("tbody")
                    .append(
                            "<tr><td style='width: 2px;'><input type='radio' onClick='com.orbps.contractEntry.registrationAcceptance.queryApplList()' id='applListRad"
                                    + i
                                    + "' name='applListRad' value='"
                                    + i
                                    + "'>"
                                    + (i + 1)
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.applList[i].applNoBegin
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.applList[i].applNoEnd
                                    + "</td><td style='width: 5px;'><a href='javascript:void(0);' onclick='com.orbps.contractEntry.registrationAcceptance.deleteApplList("
                                    + i
                                    + ");' for='applListRad' id='applListDel"
                                    + i + "'>删除</a></td></tr>");
        }
    } else {
        $('#apTaskListTabOne').find("tbody").append(
                "<tr><td colspan='6' align='center'>无记录</td></tr>");
    }
}

// 回显方法
function showappl(msg) {
    jsonStr = JSON.stringify(msg);
    var obj = eval("(" + jsonStr + ")");
    var key, value, tagName, type, arr;
    for (x in obj) {
        key = x;
        value = obj[x];
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
                    // var holderListStr =
                    // key.replace("app","isd");将list元素中id带有app的替换成isd，回显出被保人信息。
                    $("#filesListForm #" + key).val(value);
                }
            } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                // var holderListSle = key.replace("app","isd");
                $("#filesListForm #" + holderListSle).combo("val", value);
            }
        });
    }
}