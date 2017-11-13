com.orbps.contractEntry.participateInInsurance.insuredList = new Array();
com.orbps.contractEntry.participateInInsurance.policyInfoForm = $("#policyInfoForm");
com.orbps.contractEntry.participateInInsurance.insuredCount = 0;
com.orbps.contractEntry.participateInInsurance.cntrInfo = $('#applListForm');

//基本信息校验规则
com.orbps.contractEntry.participateInInsurance.poliForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            cntrNo : {
                required : true,
            },
        },
        messages : {
            cntrNo : {
                required : "交接凭证号不能为空！"
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

// 初始化函数
$(function() {
    // 初始化校验信息
    com.orbps.contractEntry.participateInInsurance
            .poliForm(com.orbps.contractEntry.participateInInsurance.policyInfoForm);
 
    $("#cntrNo").blur(function() {
        var cntrNo = $("#cntrNo").val();
        if (cntrNo == null || "" == cntrNo) {
            lion.util.info("警告", "交接凭证号不能为空");
            return false;
        }
    });
  //新增
    $("#btnAdd")
    .click(
            function() {
            	var cntrNo = $("#cntrNo").val();
                var companyName = $("#companyName").val();
                var perPolicyNum = $("#perPolicyNum").val();
                var remark = $("#remark").val();
                var insuredVo = $("#policyInfoForm").serializeObject();
                if (lion.util.isEmpty(cntrNo)){
                	lion.util.info("提示","交接凭证号不能为空！");
                	return false;
                }
                if ("" ===remark && "" ===companyName && "" ===perPolicyNum) {
                    lion.util.info("提示","单位名称，单件保单人数，备注不能都为空");
                } else {
                    com.orbps.contractEntry.participateInInsurance.insuredList
                            .push(insuredVo);
                    com.orbps.contractEntry.participateInInsurance.insuredCount++;
                    // 刷新table
                    reloadInsuredModalTable();
                    $("#policyInfoForm").reset();
                }
                var icon = $('#policyInfoForm .input-icon').children('i');
                $('#policyInfoForm .col-md-2').removeClass('has-error');
                $('#policyInfoForm .col-md-2').removeClass('has-success');
                icon.removeClass("fa-warning");
                icon.removeClass("fa-check");
           });

    // 修改
    $("#policyInfoForm #btnEdit")
            .click(
                    function() {
                        // if(com.orbps.contractEntry.participateInInsurance.applicantSaveForm.validate().form()){
                        var insuredBtuVo = $("#policyInfoForm")
                                .serializeObject();
                        // 表单验证成功后执行
                        if (com.orbps.contractEntry.participateInInsurance.insuredType > -1) {
                            com.orbps.contractEntry.participateInInsurance.insuredList[com.orbps.contractEntry.participateInInsurance.insuredType] = insuredBtuVo;
                            com.orbps.contractEntry.participateInInsurance.insuredType = -1;
                        }
                        // 刷新table
                        reloadInsuredModalTable();

                        // form清空
                        $("#policyInfoForm input[type='text']").val("");
                        $("#policyInfoForm select").combo("refresh");
                        // }else{
                        // return ;
                        // }
                        $("#policyInfoForm").reset();
                        var icon = $('#policyInfoForm .input-icon').children('i');
                        $('#policyInfoForm .col-md-2').removeClass('has-error');
                        $('#policyInfoForm .col-md-2').removeClass('has-success');
                        icon.removeClass("fa-warning");
                        icon.removeClass("fa-check");
                    });

    // 删除功能
    com.orbps.contractEntry.participateInInsurance.del = function(vform) {
        if (confirm("您确认要删除此条信息？")) {
            // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
            if (com.orbps.contractEntry.participateInInsurance.insuredList != null
                    && com.orbps.contractEntry.participateInInsurance.insuredList.length > 0) {
                com.orbps.contractEntry.participateInInsurance.insuredList
                        .splice(vform, 1);
                com.orbps.contractEntry.participateInInsurance.insuredCount--;
            }
            // 刷新table
            reloadInsuredModalTable();
        }
    };

    // 清空功能
    $("#policyInfoForm #btnClear").click(function() {
        $("#policyInfoForm").reset();
    });
    
    // 点击radio，将其数据回显到录入界面
    com.orbps.contractEntry.participateInInsurance.radio = function(
            vform) {
        $("#policyInfoForm input[type='text']").val("");
        $("#policyInfoForm select2").combo("refresh");
        var radioVal;
        // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
        var temp = document.getElementsByName("insuredRad");
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].checked) {
                radioVal = temp[i].value;
            }
        }
        com.orbps.contractEntry.participateInInsurance.insuredType = radioVal;
        var insuredVo = com.orbps.contractEntry.participateInInsurance.insuredList[radioVal];
        setTimeout(function() {
            // 回显
        	com.orbps.contractEntry.participateInInsurance.showInsured(insuredVo);
        }, 100);
    };

    // $('#btnModel').on('hide.bs.modal', function () {
    // alert(JSON.stringify(com.orbps.contractEntry.participateInInsurance.insuredList));
    // alert(JSON.stringify(com.orbps.contractEntry.participateInInsurance.insuredModalList));
    // });
    //
    // 根据查询到的数据在码表中找到相应的中文名称显示在表格中
    var codeNameZh;
    function codeListFill(data, nameEn) {
        var param = "nameEn=" + nameEn + "&codeValue=" + data;
        var base = lion.util.context;
        lion.util.postasync(base + "/system/codeList/combox.json", param,
                tableSuccess);
        return codeNameZh;
    }

    // 当选择与投保人关系是本人的时候，回显投保人信息,设置所有input及select都为readonly
    $("#policyInfoForm select[id^='isdRlt']").change(function() {
        var relationToPh = this.value;
        $("#policyInfoForm input[type='text']").attr("readonly", false);
        $("#policyInfoForm select").attr("readonly", false);
        if (relationToPh == "01") {
            var holderMsg = applicantSaveForm.serializeObject();

            showInsured(holderMsg);
            $("#policyInfoForm input[type='text']").attr("readonly", true);
            $("#policyInfoForm select").attr("readonly", true);
            $("#policyInfoForm #isdRlt").attr("readonly", false);
            $("#policyInfoForm #isdCustno").attr("readonly", false);

        }
    });

});

//重新加载表格
function reloadInsuredModalTable() {
    $('#applListForm').find("tbody").empty();
	document.getElementById('scroller').style.height='200px';
    document.getElementById('scroller').style.overflow='auto';
    
    if (com.orbps.contractEntry.participateInInsurance.insuredList != null
            && com.orbps.contractEntry.participateInInsurance.insuredList.length > 0) {
        for (var i = 0; i < com.orbps.contractEntry.participateInInsurance.insuredList.length; i++) {
            $('#applListForm')
                    .find("tbody")
                    .append(
                            "<tr><td><input type='radio' onClick='com.orbps.contractEntry.participateInInsurance.radio()' id='insuredRad"
                                    + i
                                    + "' name='insuredRad' value='"
                                    + i
                                    + "'>"
                                    + (i + 1)
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.participateInInsurance.insuredList[i].cntrNo
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.participateInInsurance.insuredList[i].companyName
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.participateInInsurance.insuredList[i].perPolicyNum
                                    + "</td><td style='width: 40px;'>"
                                    + com.orbps.contractEntry.participateInInsurance.insuredList[i].remark
                                    + "</td><td style='width: 5px;'><a href='javascript:void(0);' onclick='com.orbps.contractEntry.participateInInsurance.del("
                                    + i
                                    + ");' for='insuredRad' id='insuredDel"
                                    + i + "'>删除</a></td></tr>");
        }
    } else {
        $('#applListForm').find("tbody").append(
                "<tr><td colspan='6' align='center'>无记录</td></tr>");
    }
}
//页面清空功能
$("#submit #btnEmpty").click(function() {
    $("#participateInfoForm").reset();
    $("#policyInfoForm").reset();
    if (com.orbps.contractEntry.participateInInsurance.insuredList != null
            && com.orbps.contractEntry.participateInInsurance.insuredList.length > 0) {
        com.orbps.contractEntry.participateInInsurance.insuredList
                .splice(vform, 1);
        com.orbps.contractEntry.participateInInsurance.insuredCount--;
    }
    reloadInsuredModalTable();
});
// 回显方法
com.orbps.contractEntry.participateInInsurance.showInsured = function(msg) {
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
                    $("#policyInfoForm #" + key).val(value);
                }
            } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                $("#policyInfoForm #" + key).combo("val", value);
            }
        });
    }
}
