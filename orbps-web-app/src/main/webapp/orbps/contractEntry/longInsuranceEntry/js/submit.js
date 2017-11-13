com.orbps.contractEntry.modal = {};
com.orbps.contractEntry.longInsuranceEntry.oranLevelGroupInfoForm = $("#grpLevelFrameForm");
com.orbps.contractEntry.longInsuranceEntry.oranLevelCount = 0;
com.orbps.contractEntry.longInsuranceEntry.oranLevelType = -1;
com.orbps.contractEntry.longInsuranceEntry.oranLevel = $('#sys_applOranLevel_list_tb');

com.orbps.contractEntry.longInsuranceEntry.benesList = [];
com.orbps.contractEntry.longInsuranceEntry.insuredList = new Array();
com.orbps.contractEntry.longInsuranceEntry.oranLevelList = new Array();
com.orbps.contractEntry.modal.oranLevelList = new Array();

com.orbps.contractEntry.longInsuranceEntry.addDialog = $('#btnModels');

com.common.insuredCount = 0;
com.common.insuredList = new Array();
com.common.insuredType = -1;
com.common.oranLevelCount = 0;
com.common.oranLevelList = new Array();
com.common.oranLevelType = -1;

$(function() {

    // // 初始化校验函数
    // com.orbps.contractEntry.grpInsurAppl.printValidateForm(com.orbps.contractEntry.grpInsurAppl.printInfoForm);
    // // 表单提交
    $("#btnSubmit")
            .click(
                    function() {
                        // // jquery validate 校验
                        if (com.orbps.contractEntry.longInsuranceEntry.addTaxInfoForm
                                .validate().form()
                                && com.orbps.contractEntry.longInsuranceEntry.applIlnfoForm
                                        .validate().form()
                                && com.orbps.contractEntry.longInsuranceEntry.insurInfoForm
                                        .validate().form()
                                && com.orbps.contractEntry.longInsuranceEntry.offerInfoForm
                                        .validate().form()) {
                            // select校验
                            if (validateSelectVal()) {
                                // // 获取要约信息下面的所有险种信息
                                var getAddRowsData = $("#fbp-editDataGrids")
                                        .editDatagrids("getRowsData");
                                // // 提交方法
                                var longInsuranceEntryVo = {};
                                longInsuranceEntryVo.addTaxInfoVo = com.orbps.contractEntry.longInsuranceEntry.addTaxInfoForm
                                        .serializeObject();
                                longInsuranceEntryVo.applIlnfoVo = com.orbps.contractEntry.longInsuranceEntry.applIlnfoForm
                                        .serializeObject();
                                longInsuranceEntryVo.beneficiaryInfoVo = com.orbps.contractEntry.longInsuranceEntry.beneficiaryInfoForm
                                        .serializeObject();
                                longInsuranceEntryVo.offerInfoVo = com.orbps.contractEntry.longInsuranceEntry.offerInfoForm
                                        .serializeObject();
                                longInsuranceEntryVo.insuredGroupModalVos = com.orbps.contractEntry.longInsuranceEntry.insuredList;
                                longInsuranceEntryVo.organizaHierarModalVos = com.orbps.contractEntry.longInsuranceEntry.oranLevelList;

                                longInsuranceEntryVo.busiPrdVos = getAddRowsData;
                                var responseVos = new Array();
                                for (var i = 0; i < com.orbps.contractEntry.longInsuranceEntry.benesList.length; i++) {
                                    var array_element = com.orbps.contractEntry.longInsuranceEntry.benesList[i];
                                    for (var j = 0; j < array_element.length; j++) {
                                        var array_elements = array_element[j];
                                        responseVos.push(array_elements);
                                    }

                                }
                                longInsuranceEntryVo.responseVos = responseVos;
                                alert(JSON.stringify(longInsuranceEntryVo));
                                lion.util
                                        .postjson(
                                                '/orbps/web/orbps/contractEntry/long/submit',
                                                longInsuranceEntryVo,
                                                successSubmit,
                                                null,
                                                com.orbps.contractEntry.longInsuranceEntry.applIlnfoForm);
                            }
                        }
                    });

    // 通过投保单号查询险种信息
    $("#btnQuery")
            .click(
                    function() {
                        // 取出投保单号
                        var applNo = $("#applInfoForm #applNo").val();
                        // 校验投保单号
                        if (applNo != null || "".equals(applNo)
                                || applNo == "undefined") {
                            var insurApplVo = {};
                            insurApplVo.applInfoVo = com.orbps.contractEntry.grpInsurAppl.applInfoForm
                                    .serializeObject();
                            alert(JSON.stringify(insurApplVo));
                            lion.util
                                    .postjson(
                                            '/orbps/web/orbps/contractEntry/grp/query',
                                            insurApplVo,
                                            successQuery,
                                            null,
                                            com.orbps.contractEntry.grpInsurAppl.applInfoForm);
                        } else {
                            lion.util.info('提示', '请输入正确投保单号');
                        }
                    });

    // 被保人分组
    $("#btnGoup")
            .click(
                    function() {
                        com.orbps.contractEntry.longInsuranceEntry.addDialog
                                .empty();
                        com.orbps.contractEntry.longInsuranceEntry.addDialog
                                .load(
                                        "/orbps/orbps/public/modal/html/insuredGroupModal.html",
                                        function() {
                                            $(this).modal('toggle');
                                            $(this).comboInitLoad();
                                            $(this).combotreeInitLoad();
                                            // 刷新table
                                        });
                        setTimeout(function() {
                            // 回显
                            reloadPublicInsuredModalTable();
                        }, 100);
                    });

    // 组织层次架构
    $("#btnOranLevel")
            .click(
                    function() {
                        com.orbps.contractEntry.longInsuranceEntry.addDialog
                                .empty();
                        com.orbps.contractEntry.longInsuranceEntry.addDialog
                                .load(
                                        "/orbps/orbps/public/modal/html/organizaHierarModal.html",
                                        function() {
                                            $(this).modal('toggle');
                                            // combo组件初始化
                                            $(this).comboInitLoad();
                                            $(this).combotreeInitLoad();
                                        });
                        setTimeout(function() {
                            // 回显
                            reloadPublicOranLevelModalTable();
                        }, 100);
                    });

});

// 添加成功回调函数
function successSubmit(obj, data, arg) {
    lion.util.info("提示", "提交团单录入信息成功");
    // 成功后刷新页面
    setTimeOut(function() {
        window.location.reload();
    }, 500);
}

// 查询成功回调函数
function successQuery(data, obj, arg) {
    alert(JSON.stringify(data));
    var applicant = data.applicantVo;
    var insured = data.insuredVo;
    insuredList = insured;
    reloadInsuredTable();
    var product = data.productsVos;
    busiProdList = product;
    reloadProductsTable();
    setTimeout(function() {
        // 回显投保人
        var jsonStrApplicant = JSON.stringify(applicant);
        var objApplicant = eval("(" + jsonStrApplicant + ")");
        var key, value, tagName, type, arr;
        for (x in objApplicant) {
            key = x;
            value = objApplicant[x];
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
                        $("#applicantSaveForm #" + key).val(value);
                    }
                } else if (tagName == 'SELECT' || tagName == 'TEXTAREA') {
                    $("#applicantSaveForm #" + key).combo("val", value);
                }

            });
        }

        $("#applicantSaveForm input[type='text']").attr("readonly", true);
        $("#applicantSaveForm select").attr("readonly", true);
        $("#applicantSaveForm #appNo").attr("readonly", false);

        flag = 1;

    }, 100);
}

// 下拉框校验
function validateSelectVal() {
    // 性别
    var sex = $("#applIlnfo #sex").val();
    if (sex == null || "" == sex) {
        lion.util.info("警告", "性别不能为空");
        return false;
    }

    // 出生日期
    var birthDate = $("#applIlnfo #birthDate").val();
    if (birthDate == null || "" == birthDate) {
        lion.util.info("警告", "出生日期不能为空");
        return false;
    }

    // 婚姻状况
    var maritalStatus = $("#applIlnfo #maritalStatus").val();
    if (maritalStatus == null || "" == maritalStatus) {
        lion.util.info("警告", "婚姻状况不能为空");
        return false;
    }

    // 证件类型
    var idType = $("#applIlnfo #idType").val();
    if (idType == null || "" == idType) {
        lion.util.info("警告", "证件类型不能为空");
        return false;
    }

    // 证件有效期
    var idTerm = $("#applIlnfo #idTerm").val();
    if (idTerm == null || "" == idTerm) {
        lion.util.info("警告", "证件有效期不能为空");
        return false;
    }

    // 国籍
    var nationality = $("#applIlnfo #nationality").val();
    if (nationality == null || "" == nationality) {
        lion.util.info("警告", "国籍不能为空");
        return false;
    }

    // 兼职代码
    var ptJobCode = $("#applIlnfo #ptJobCode").val();
    if (ptJobCode == null || "" == ptJobCode) {
        lion.util.info("警告", "兼职代码不能为空");
        return false;
    }

    // 个人保单外部转移人员
    var externalTransfer = $("#applIlnfo #externalTransfer").val();
    if (externalTransfer == null || "" == externalTransfer) {
        lion.util.info("警告", "个人保单外部转移人员不能为空");
        return false;
    }

    // 补充医疗
    var supplementaryMedical = $("#applIlnfo #supplementaryMedical").val();
    if (supplementaryMedical == null || "" == supplementaryMedical) {
        lion.util.info("警告", "个补充医疗不能为空");
        return false;
    }

    // 社会保险/公费医疗
    var socialInsurance = $("#applIlnfo #socialInsurance").val();
    if (socialInsurance == null || "" == socialInsurance) {
        lion.util.info("警告", "社会保险/公费医疗不能为空");
        return false;
    }

    // 与投保人关系
    var relToHldr = $("#insurInfo #relToHldr").val();
    if (relToHldr == null || "" == relToHldr) {
        lion.util.info("警告", "与投保人关系不能为空");
        return false;
    }

    // 性别
    var sex = $("#insurInfo #sex").val();
    if (sex == null || "" == sex) {
        lion.util.info("警告", "性别不能为空");
        return false;
    }

    // 出生日期
    var birthDate = $("#insurInfo #birthDate").val();
    if (birthDate == null || "" == birthDate) {
        lion.util.info("警告", "出生日期不能为空");
        return false;
    }

    // 证件类型
    var idType = $("#insurInfo #idType").val();
    if (idType == null || "" == idType) {
        lion.util.info("警告", "证件类型不能为空");
        return false;
    }

    // 证件号码
    var idNo = $("#insurInfo #idNo").val();
    if (idNo == null || "" == idNo) {
        lion.util.info("警告", "证件号码不能为空");
        return false;
    }

    // 证件有效期
    var idTerm = $("#insurInfo #idTerm").val();
    if (idTerm == null || "" == idTerm) {
        lion.util.info("警告", "证件有效期不能为空");
        return false;
    }

    // 国籍
    var nationality = $("#insurInfo #nationality").val();
    if (nationality == null || "" == nationality) {
        lion.util.info("警告", "国籍不能为空");
        return false;
    }

    // 职业代码
    var occupationCode = $("#insurInfo #occupationCode").val();
    if (occupationCode == null || "" == occupationCode) {
        lion.util.info("警告", "职业代码不能为空");
        return false;
    }

    // 开户银行代码
    var companyBankCode = $("#addTaxInfo #companyBankCode").val();
    if (companyBankCode == null || "" == companyBankCode) {
        lion.util.info("警告", "开户银行代码不能为空");
        return false;
    }

    // 首期缴费方式
    var firstPaymentForm = $("#offerInfo #firstPaymentForm").val();
    if (firstPaymentForm == null || "" == firstPaymentForm) {
        lion.util.info("警告", "首期缴费方式不能为空");
        return false;
    }

    // 交费日期
    var paymentDate = $("#offerInfo #paymentDate").val();
    if (paymentDate == null || "" == paymentDate) {
        lion.util.info("警告", "交费日期不能为空");
        return false;
    }

    // 交费方式
    var paymentForm = $("#offerInfo #paymentForm").val();
    if (paymentForm == null || "" == paymentForm) {
        lion.util.info("警告", "交费方式不能为空");
        return false;
    }

    // 银行代码
    var bankCode = $("#offerInfo #bankCode").val();
    if (bankCode == null || "" == bankCode) {
        lion.util.info("警告", "银行代码不能为空");
        return false;
    }

    // 投保日期
    var insureDate = $("#offerInfo #insureDate").val();
    if (insureDate == null || "" == insureDate) {
        lion.util.info("警告", "投保日期不能为空");
        return false;
    }

    // 续保保费计算方式
    var renewalCalculationMethod = $("#offerInfo #renewalCalculationMethod")
            .val();
    if (renewalCalculationMethod == null || "" == renewalCalculationMethod) {
        lion.util.info("警告", "续保保费计算方式不能为空");
        return false;
    }

    // 保单性质
    var policyNature = $("#offerInfo #policyNature").val();
    if (policyNature == null || "" == policyNature) {
        lion.util.info("警告", "保单性质不能为空");
        return false;
    }

    // 指定生效日
    var designatedEffectiveDate = $("#offerInfo #designatedEffectiveDate")
            .val();
    if (designatedEffectiveDate == null || "" == designatedEffectiveDate) {
        lion.util.info("警告", "指定生效日不能为空");
        return false;
    }

    // 人工核保
    var manualUnderwriting = $("#offerInfo #manualUnderwriting").val();
    if (manualUnderwriting == null || "" == manualUnderwriting) {
        lion.util.info("警告", "人工核保不能为空");
        return false;
    }

    // 合同争议处理方式
    var disputeHandling = $("#offerInfo #disputeHandling").val();
    if (disputeHandling == null || "" == disputeHandling) {
        lion.util.info("警告", "合同争议处理方式不能为空");
        return false;
    }

    return true;
}