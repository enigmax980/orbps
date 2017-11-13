//基本信息校验规则
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            companyName : {
                required : true
            },
            companyFlag : {
                required : true
            },
            connIdType : {
                required : true
            },
            amntPct : {
                required : true,
                isBilityPct : true
            },
            responsibilityPct : {
                required : true,
                isBilityPct : true
            },
        },
        messages : {
            companyName : {
                required : '请输入共保公司名称'
            },
            companyFlag : {
                required : '请选择是否本公司'
            },
            connIdType : {
                required : '请选择共保身份'
            },
            amntPct : {
                required : '请输入共保保费份额占比',
                isBilityPct : "占比份额必须大于0且小于100"
            },
            responsibilityPct : {
                required : '请输入共保责任份额占比',
                isBilityPct : "占比份额必须大于0且小于100"
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
            .insuranForm(com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComForm);

    $("#companyName").blur(function() {
        var companyName = $("#companyName").val();
        if (companyName == null || "" == companyName) {
            lion.util.info("警告", "共保公司名称不能为空");
            return false;
        }
    });

    $("#companyFlag").blur(function() {
        var companyFlag = $("#companyFlag").val();
        if (companyFlag == null || "" == companyFlag) {
            lion.util.info("警告", "请选择是否本公司");
            return false;
        }
    });

    $("#connIdType").blur(function() {
        var connIdType = $("#insuranceComForm #connIdType").val();
        if (connIdType == null || "" == connIdType) {
            lion.util.info("警告", "请选择共保身份");
            return false;
        }
    });

    $("#amntPct").blur(function() {
        var amntPct = $("#amntPct").val();
        if (amntPct == null || "" == amntPct) {
            lion.util.info("警告", "共保保费份额占比不能为空");
            return false;
        }
        else{
        	$("#responsibilityPct").val(amntPct)
        }
    });

    $("#responsibilityPct").blur(function() {
        var responsibilityPct = $("#responsibilityPct").val();
        if (responsibilityPct == null || "" == responsibilityPct) {
            lion.util.info("警告", "共保责任份额占比不能为空");
            return false;
        }
    });

    
    
    setTimeout(function(){
    	//是否本公司，默认显示否
    	$("#companyFlag").combo("val","Y");
    },1000);
    
    // 表单新增
    $("#btnAdd")
            .click(
                    function() {
                        if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComForm
                                .validate().form()){
                            var insuranceComVos = $("#insuranceComForm")
                            .serializeObject();
                            // 表单验证成功后执行
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList
                                    .push(insuranceComVos);
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComCount++;
                            // 刷新table
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComTable();
                            // form清空
                            $("#insuranceComForm input[type='text']").val("");
                            $("#insuranceComForm select").combo("refresh");
                            $("#insuranceComForm .fa").removeClass("fa-warning");
                            $("#insuranceComForm .fa").removeClass("fa-check");
                            $("#insuranceComForm .fa").removeClass("has-success");
                            $("#insuranceComForm .fa").removeClass("has-error");
                        }
                    });

    // 修改
    $("#insuranceComForm #btnEdit")
            .click(
                    function() {
                        var insuredBtuVo = $("#insuranceComForm")
                                .serializeObject();
                        // 表单验证成功后执行
                        if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComType > -1) {
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComType] = insuredBtuVo;
                            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComType = -1;
                        }
                        // 刷新table
                        com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComTable();

                        // form清空
                        $("#insuranceComForm input[type='text']").val("");
                        $("#insuranceComForm select").combo("refresh");
                        $("#insuranceComForm .fa").removeClass("fa-warning");
                        $("#insuranceComForm .fa").removeClass("fa-check");
                        $("#insuranceComForm .fa").removeClass("has-success");
                        $("#insuranceComForm .fa").removeClass("has-error");
                    });

    // 删除功能
    com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCom.del = function(
            vform) {
        if (confirm("您确认要删除此条信息？")) {
            // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
            if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList != null
                    && com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList.length > 0) {
                com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList
                        .splice(vform, 1);
                com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComCount--;
            }
            // 刷新table
            com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComTable();
        }
    };

    // 清空功能
    $("#insuranceComForm #btnCompanyInfoClear").click(function() {
        $("#insuranceComForm").reset();
        $("#insuranceComForm .fa").removeClass("fa-warning");
        $("#insuranceComForm .fa").removeClass("fa-check");
        $("#insuranceComForm .fa").removeClass("has-success");
        $("#insuranceComForm .fa").removeClass("has-error");
    });

    // 点击radio，将其数据回显到录入界面
    com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCom.radio = function(
            vform) {
        $("#insuranceComForm input[type='text']").val("");
        $("#insuranceComForm select2").combo("refresh");
        var radioVal;
        // 回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
        var temp = document.getElementsByName("insuredRad");
        for (var i = 0; i < temp.length; i++) {
            if (temp[i].checked) {
                radioVal = temp[i].value;
            }
        }
//        $("#insuranceComForm #insuredRad").attr('checked',$(this).val() == radioVal);
        com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComType = radioVal;
        insuranceComVo = com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[radioVal];
        setTimeout(function() {
            // 回显
            showInsuranceCom(insuranceComVo);
        }, 100);
    };
});

// 重新加载表格
com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComTable = function() {
    $('#companyInfoTb').find("tbody").empty();
    //alert($('#bankCode').val());
    if (com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList != null
            && com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList.length > 0) {
        for (var i = 0; i < com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList.length; i++) {
            var companyFlagTb;
            var connIdTypeTb;
            var bankCodeTb="";
            
            if("Y"===com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].companyFlag){
                companyFlagTb="是";
            }else if("N"===com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].companyFlag){
                companyFlagTb="否";
            }
            if("M"===com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].connIdType){
                connIdTypeTb="首席共保";
            }else if("N"===com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].connIdType){
                connIdTypeTb="参与共保";
            }
            var bankCode =com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].bankCode;
            if ( bankCode === null || bankCode === undefined || bankCode === ""){
            	 bankCodeTb=bankCode;
            }else{
            	 bankCodeTb = com.orbps.publicSearch.bankCodeQuery(bankCode);
            }
            $('#companyInfoTb')
                    .find("tbody")
                    .append(
                            "<tr><td ><input type='radio' onclick='com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCom.radio();' id='insuredRad"
                                    + i
                                    + "' name='insuredRad' value='"
                                    + i
                                    + "'></td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].companyName
                                    + "</td><td >"
                                    + companyFlagTb
                                    + "</td><td >"
                                    + connIdTypeTb
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].amntPct
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].responsibilityPct
                                    + "</td><td >"
                                    + bankCodeTb
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].accCustName
                                    + "</td><td >"
                                    + com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i].bankAccNo

                                    + "</td><td ><a href='javascript:void(0);' onclick='com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceCom.del("
                                    + i
                                    + ");' for='insuredRad' id='insuredDel"
                                    + i + "'>删除</a></td></tr>");
        }
    } else {
        $('#companyInfoTb').find("tbody").append(
                "<tr><td colspan='9' align='center'>无记录</td></tr>");
    }
}

// 回显方法
function showInsuranceCom(msg) {
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
                    $("#insuranceComForm #" + key).val(value);
                }
            } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                $("#insuranceComForm #" + key).combo("val", value);
            }
        });
    }
}
