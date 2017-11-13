com.orbps.contractEntry.registrationAcceptance.fmApplListForm = $("#fmApplListForm");
com.orbps.contractEntry.registrationAcceptance.fmApplList = new Array();
com.orbps.contractEntry.registrationAcceptance.fmApplCount = 0;
com.orbps.contractEntry.registrationAcceptance.fmApplType = -1;
com.orbps.contractEntry.registrationAcceptance.fmAppl = $('#fmApplListTab');

com.orbps.contractEntry.registrationAcceptance.fmApplValidateForm = function(
        vform) {
var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            entChannelFlag : {
                required : true
            },
            applNo : {
                required : true,
                isNumLetter : true
            },
            applDate : {
                required : true
            },
            insurType : {
                required : true
            },
            polCode : {
                required : true
            },
            hldrName : {
                required : true,
                zh_verifyl : true
            },
            ipsnName : {
                required : true,
                zh_verifyl : true
            }
        },
        messages : {
            entChannelFlag : {
                required : "请选择外包录入标记"
            },
            applNo : {
                required : '请输入投保单号',
                isNumLetter : '请输入字母或数字组成的投保单号'
            },
            applDate : {
                required : '请选择申请日期'
            },
            insurType : {
                required : "请选择契约形式"
            },
            polCode : {
                required : "请选择险种"
            },
            hldrName : {
                required : "请输入投保人或汇交人姓名",
                zh_verifyl : "请输入2到18位的汉字"
            },
            ipsnName : {
                required : "请输入被保险人姓名",
                zh_verifyl : "请输入2到18位的汉字"
            }
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

        success : function(label, element) {
            var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass(
                    'has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
    });
}

$(function() {
    // 初始化校验方法
    com.orbps.contractEntry.registrationAcceptance
            .fmApplValidateForm(com.orbps.contractEntry.registrationAcceptance.fmApplListForm);

    // 增加家庭投保清单信息
    $("#fmApplListForm #btnSpnAdd").click(
            function() {
                // 需添加非空校验
                var fmApplVo = $("#fmApplListForm").serializeObject();
                com.orbps.contractEntry.registrationAcceptance.fmApplList
                        .push(fmApplVo);// 给表格赋值
                com.orbps.contractEntry.registrationAcceptance.fmApplCount++;
                // 刷新table
                reloadfmApplListTable();
                // form清空
                $("#fmApplListForm input[type='text']").val("");
                $("#fmApplListForm select").combo("refresh");
            });
    // 删除展业人员信息
    com.orbps.contractEntry.registrationAcceptance.deletefmApplList = function(
            vform) {
        if (confirm("您确认要删除此条记录吗？")) {
            var radioVal = this.name;
            if (com.orbps.contractEntry.registrationAcceptance.fmApplList != null
                    && com.orbps.contractEntry.registrationAcceptance.fmApplList.length > 0) {
                // alert(JSON.stringify(radioVal));//输出radioVal，JSON.stringify是将radioVal序列化。
                com.orbps.contractEntry.registrationAcceptance.fmApplList
                        .splice(vform, 1);
                com.orbps.contractEntry.registrationAcceptance.fmApplCount--;
            }
            // 刷新table
            reloadfmApplListTable();
        }
    };
    // 修改清单信息
    $("#btnSpnModify")
            .click(
                    function() {
                        // 需添加非空校验
                        var fmApplVo = $("#fmApplListForm").serializeObject();
                        com.orbps.contractEntry.registrationAcceptance.fmApplList[com.orbps.contractEntry.registrationAcceptance.fmApplType] = fmApplVo;
                        com.orbps.contractEntry.registrationAcceptance.fmApplType = -1;
                        // 刷新table
                        reloadfmApplListTable();
                        // form清空
                        $("#fmApplListForm input[type='text']").val("");
                        $("#fmApplListForm select").combo("refresh");
                    });
    // 回显展业人员信息。
    com.orbps.contractEntry.registrationAcceptance.queryfmApplList = function(
            vform) {
        $("#fmApplListForm input[type='text']").val("");
        $("#fmApplListForm select2").combo("refresh");
        var radioVal;
        // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
        var temp = document.getElementsByName("fmApplListRad");
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].checked) {
                radioVal = temp[i].value;
            }
        }
        com.orbps.contractEntry.registrationAcceptance.fmApplType = radioVal;
        var fmApplVo = com.orbps.contractEntry.registrationAcceptance.fmApplList[radioVal];
        setTimeout(function() {
            // 回显
            showSales(fmApplVo);
        }, 100);
    };
});

// 重新加载表格
function reloadfmApplListTable() {
    $('#fmApplListTable').find("tbody").empty();
    if (com.orbps.contractEntry.registrationAcceptance.fmApplList != null
            && com.orbps.contractEntry.registrationAcceptance.fmApplList.length > 0) {
        for (var i = 0; i < com.orbps.contractEntry.registrationAcceptance.fmApplList.length; i++) {
            $('#fmApplListTable')
                    .find("tbody")
                    .append(
                            "<tr><td style='width: 2px;'><input type='radio' onClick='com.orbps.contractEntry.registrationAcceptance.queryfmApplList()' id='fmApplListRad"
                                    + i
                                    + "' name='fmApplListRad' value='"
                                    + i
                                    + "'>"
                                    + (i + 1)
                                    + "</td><td style='width: 20px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].applNo
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].applDate
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].cntrType
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].polCode
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].sumPremium
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].hldrName
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].ipsnName
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].salesChannel
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].salesBranchNo
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].salesNo
                                    + "</td><td style='width: 10px;'>"
                                    + com.orbps.contractEntry.registrationAcceptance.fmApplList[i].salesNo
                                    + "</td><td style='width: 5px;'><a href='javascript:void(0);' onclick='com.orbps.contractEntry.registrationAcceptance.deletefmApplList("
                                    + i
                                    + ");' for='fmApplListRad' id='fmApplListDel"
                                    + i + "'>删除</a></td></tr>");
        }
    } else {
        $('#fmApplListTable').find("tbody").append(
                "<tr><td colspan='13' align='center'>无记录</td></tr>");
    }
}

// 回显方法
function showSales(msg) {
    jsonStr = JSON.stringify(msg);// 将msg信息json串解析
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
                    $("#fmApplListForm #" + key).val(value);
                }
            } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                // var holderListSle = key.replace("app","isd");
                $("#fmApplListForm #" + key).combo("val", value);
            }
        });
    }
}

// 非空校验
$("#fmApplListForm #entChannelFlag").blur(function() {
    var entChannelFlag = $("#fmApplListForm #entChannelFlag").val();
    if (entChannelFlag == null || "" == entChannelFlag) {
        lion.util.info("警告", "外包录入标记不能为空");
        return false;
    }
});
$("#fmApplListForm #applNo").blur(function() {
    var applNo = $("#fmApplListForm #applNo").val();
    if (applNo == null || "" == applNo) {
        lion.util.info("警告", "投保单号不能为空");
        return false;
    }
});
$("#fmApplListForm #applDate").blur(function() {
    var applDate = $("#fmApplListForm #applDate").val();
    if (applDate == null || "" == applDate) {
        lion.util.info("警告", "投保日期不能为空");
        return false;
    }
});
$("#fmApplListForm #insurType").blur(function() {
    var insurType = $("#fmApplListForm #insurType").val();
    if (insurType == null || "" == insurType) {
        lion.util.info("警告", "契约形式不能为空");
        return false;
    }
});
$("#fmApplListForm #polCode").blur(function() {
    var polCode = $("#fmApplListForm #polCode").val();
    if (polCode == null || "" == polCode) {
        lion.util.info("警告", "险种信息不能为空");
        return false;
    }
});
$("#fmApplListForm #hldrName").blur(function() {
    var hldrName = $("#fmApplListForm #hldrName").val();
    if (hldrName == null || "" == hldrName) {
        lion.util.info("警告", "投保/汇交人名称不能为空");
        return false;
    }
});
$("#fmApplListForm #insuredNum").blur(function() {
    var insuredNum = $("#fmApplListForm #insuredNum").val();
    if (insuredNum == null || "" == insuredNum) {
        lion.util.info("警告", "被保险人人数不能为空");
        return false;
    }
});
