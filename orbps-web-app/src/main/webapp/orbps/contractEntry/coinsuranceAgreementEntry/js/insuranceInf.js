com.orbps.contractEntry.coinsuranceAgreementEntry.polName="";
//基本信息校验规则
com.orbps.contractEntry.coinsuranceAgreementEntry.insurancehForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
        	polCode : {
        		required : true
        	},
        },
        messages : {
        	polCode : {
        		required : '请输入险种代码'
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
    com.orbps.contractEntry.coinsuranceAgreementEntry
            .insurancehForm(com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceForm);
    //险种名称离开焦点判断不能为空
    $("#polName").blur(function() {
        var polName = $("#polName").val();
        if (polName == null || "" == polName) {
            lion.util.info("警告", "险种名称不能为空");
            return false;
        }
    });

    // 表单新增
    $("#btnAdd1")
            .click(
                    function() {
                        // jquery validate 校验
                        if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceForm
                                .validate().form()) {
                            var insuredVo1s = $("#insuranceForm")
                                    .serializeObject();
                            // 表单验证成功后执行
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList
                                    .push(insuredVo1s);
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCount++;
                            // 刷新table
                            com.orbps.contractEntry.coinsuranceAgreementEntry.reloadInsuranceTable();
                            // form清空
                            $("#insuranceForm input[type='text']").val("");
                            $("#insuranceForm select").combo("refresh");
                            $("#insuranceForm .fa").removeClass("fa-warning");
                            $("#insuranceForm .fa").removeClass("fa-check");
                            $("#insuranceForm .fa").removeClass("has-success");
                            $("#insuranceForm .fa").removeClass("has-error");
                        }
                    });

    // 修改
    $("#insuranceForm #btnEdit1")
            .click(
                    function() {
                        var insuredBtuVo = $("#insuranceForm")
                                .serializeObject();
                        // 表单验证成功后执行
                        if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceType > -1) {
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList[com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceType] = insuredBtuVo;
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceType = -1;
                        }
                        // 刷新table
                        com.orbps.contractEntry.coinsuranceAgreementEntry.reloadInsuranceTable();

                        // form清空
                        $("#insuranceForm input[type='text']").val("");
                        $("#insuranceForm select").combo("refresh");
                        $("#insuranceForm .fa").removeClass("fa-warning");
                        $("#insuranceForm .fa").removeClass("fa-check");
                        $("#insuranceForm .fa").removeClass("has-success");
                        $("#insuranceForm .fa").removeClass("has-error");
                    });

    // 删除功能
    com.orbps.contractEntry.coinsuranceAgreementEntry.del = function(vform) {
        if (confirm("您确认要删除此条信息？")) {
            // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
            if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList != null
                    && com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList.length > 0) {
                com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList
                        .splice(vform, 1);
                com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCount--;
            }
            // 刷新table
            com.orbps.contractEntry.coinsuranceAgreementEntry.reloadInsuranceTable();
        }
    };

    // 清空功能
    $("#insuranceForm #btnInsuranceClear").click(function() {
        $("#insuranceForm").reset();
        $("#insuranceForm .fa").removeClass("fa-warning");
        $("#insuranceForm .fa").removeClass("fa-check");
        $("#insuranceForm .fa").removeClass("has-success");
        $("#insuranceForm .fa").removeClass("has-error");
    });

    // 点击radio，将其数据回显到录入界面
    com.orbps.contractEntry.coinsuranceAgreementEntry.radio = function(vform) {
        $("#insuranceForm input[type='text']").val("");
        $("#insuranceForm select2").combo("refresh");
        var radioVal;
        // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
        var temp = document.getElementsByName("insuredRad");
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].checked) {
                radioVal = temp[i].value;
            }
        }
        com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceType = radioVal;
        insuredVo1 = com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList[radioVal];
        setTimeout(function() {
            // 回显
            showInsured(insuredVo1);
        }, 100);
    };
    
    $("#polCode").blur(function() {
        var polCode = $("#polCode").val();
        if (polCode == null || "" == polCode) {
            lion.util.info("警告", "险种代码不能为空");
            com.orbps.contractEntry.coinsuranceAgreementEntry.polName = "";
            $("#polName").val(com.orbps.contractEntry.coinsuranceAgreementEntry.polName);
            return false;
        }else{
    		var responseVo = {};
    		responseVo.busiPrdCode = polCode;
    		// 向后台发送请求
    		lion.util.postjson(
    				'/orbps/web/orbps/contractEntry/grp/searchBusiName',
    				responseVo,
    				com.orbps.contractEntry.coinsuranceAgreementEntry.successSearchBusiprodName,
    				null,
    				null);	
        }
    });
    
    
});


// 重新加载表格
com.orbps.contractEntry.coinsuranceAgreementEntry.reloadInsuranceTable = function() {
    $('#cntrInfoTb').find("tbody").empty();

    if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList != null
            && com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList.length > 0) {
        for (var i = 0; i < com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList.length; i++) {

            $('#cntrInfoTb')
                    .find("tbody")
                    .append(
                            "<tr><td ><input type='radio' onclick='com.orbps.contractEntry.coinsuranceAgreementEntry.radio();' id='insuredRad"
                                    + i
                                    + "' name='insuredRad' value='"
                                    + i
                                    + "'></td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList[i].polCode
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList[i].polName
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList[i].faceAmnt
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList[i].premium
                                    + "</td><td ><a href='javascript:void(0);' onclick='com.orbps.contractEntry.coinsuranceAgreementEntry.del("
                                    + i
                                    + ");' for='insuredRad' id='insuredDel"
                                    + i + "'>删除</a></td></tr>");
        }
    } else {
        $('#cntrInfoTb').find("tbody").append(
                "<tr><td colspan='7' align='center'>无记录</td></tr>");
    }
}

// 回显方法
function showInsured(msg) {
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
//                    $(this).attr('checked', $(this).val() == value);
                } else if (type == 'checkbox') {
                    arr = value.split(',');
                    for (var i = 0; i < arr.length; i++) {
                        if ($(this).val() == arr[i]) {
                            $(this).attr('checked', true);
                            break;
                        }
                    }
                } else {
                    $("#insuranceForm #" + key).val(value);
                }
            } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                $("#insuranceForm #" + key).combo("val", value);
            }
        });
    }
}


com.orbps.contractEntry.coinsuranceAgreementEntry.successSearchBusiprodName = function (data,arg){
	$("#polName").val(data);
	com.orbps.contractEntry.coinsuranceAgreementEntry.polName = data;
	
}

//险种名称丢失焦点事件
$("#polName").blur(function(){
	$("#polName").val(com.orbps.contractEntry.coinsuranceAgreementEntry.polName);
});
