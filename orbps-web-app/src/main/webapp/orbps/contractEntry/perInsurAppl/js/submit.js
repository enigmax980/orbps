com.orbps.contractEntry.perInsurAppl.benesList = [];
com.orbps.contractEntry.perInsurAppl.insuredList = new Array();
com.orbps.contractEntry.perInsurAppl.oranLevelList = new Array();
com.common.insuredCount = 0;
com.common.insuredList = new Array();
com.common.insuredType = -1;
com.common.oranLevelCount = 0;
com.common.oranLevelList = new Array();
com.common.oranLevelType = -1;
// $(function(){

// // 表单提交
$("#btnSubmit")
        .click(
                function() {

                    // jquery validate 校验
                    // if(com.orbps.contractEntry.perInsurAppl.applicantIlnfo.validate().form()
                    // ||com.orbps.contractEntry.perInsurAppl.addTaxInfoForm.validate().form()
                    // ||com.orbps.contractEntry.perInsurAppl.insurInfoForm.validate().form()
                    // ||com.orbps.contractEntry.perInsurAppl.offerInfoForm.validate().form()
                    // ){
                    // select校验
                    if (Validate()) {
                        // //// 获取要约信息下面的所有险种信息
                        var getAddRowsData = $("#fbp-editDataGrids")
                                .editDatagrids("getRowsData");
                        // //// 提交方法
                        var perInsurApplVo = {};
                        perInsurApplVo.addTaxInfoVo = com.orbps.contractEntry.perInsurAppl.addTaxInfoForm
                                .serializeObject();
                        perInsurApplVo.applIlnfoVo = com.orbps.contractEntry.perInsurAppl.applicantIlnfo
                                .serializeObject();
                        // perInsurApplVo.beneficiaryInfoVo
                        // =com.orbps.contractEntry.perInsurAppl.beneficiaryInfo.serializeObject();
                        perInsurApplVo.offerInfoVo = com.orbps.contractEntry.perInsurAppl.offerInfoForm
                                .serializeObject();
                        perInsurApplVo.beneficiaryInfoVo = com.orbps.contractEntry.perInsurAppl.insurInfoForm
                                .serializeObject();
                        perInsurApplVo.insuredGroupModalVos = com.orbps.contractEntry.perInsurAppl.insuredList;
                        perInsurApplVo.organizaHierarModalVos = com.orbps.contractEntry.perInsurAppl.oranLevelList;
                        perInsurApplVo.busiPrdVos = getAddRowsData;
                        var responseVos = new Array();

                        for (var i = 0; i < com.orbps.contractEntry.perInsurAppl.benesList.length; i++) {
                            alert(com.orbps.contractEntry.perInsurAppl.benesList.length);
                            var array_element = com.orbps.contractEntry.perInsurAppl.benesList[i];
                            alert(array_element);
                            for (var j = 0; j < array_element.length; j++) {
                                var array_elements = array_element[j];
                                responseVos.push(array_elements);
                            }
                        }
                        perInsurApplVo.responseVos = responseVos;
                        alert(JSON.stringify(perInsurApplVo));
                        lion.util.postjson(
                                '/orbps/web/orbps/contractEntry/per/submit',
                                perInsurApplVo, successSubmit, null,
                                com.orbps.contractEntry.perInsurAppl.insurInfo);
                        // }
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
$("#btnGoup").click(
        function() {
            com.orbps.contractEntry.perInsurAppl.addDialog.empty();
            com.orbps.contractEntry.perInsurAppl.addDialog.load(
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
$("#btnOranLevel").click(
        function() {
            com.orbps.contractEntry.perInsurAppl.addDialog.empty();
            com.orbps.contractEntry.perInsurAppl.addDialog.load(
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

// 下拉框信息校验
function Validate() {
    // 性别
    var sex = $("#applicantIlnfo #sex").val();
    if (sex == null || "" == sex) {
        lion.util.info("警告", "性别能为空");
        return false;
    }

    // 出生日期
    var birthlDate = $("#applicantIlnfo #birthlDate").val();
    if (birthlDate == null || "" == birthlDate) {
        lion.util.info("警告", "性别不能为空");
        return false;
    }

    // 证件类型
    var idType = $("#applicantIlnfo #idType").val();
    if (idType == null || "" == idType) {
        lion.util.info("警告", "证件类型不能为空");
        return false;
    }

    var idTerm = $("#applicantIlnfo #idTerm").val();
    if (idTerm == null || "" == idTerm) {
        lion.util.info("警告", "证件有效期不能为空");
        return false;
    }

    // 职业代码
    var occupationCode = $("#applicantIlnfo #occupationCode").val();
    if (occupationCode == null || "" == occupationCode) {
        lion.util.info("警告", "职业代码不能为空");
        return false;
    }

    // 国籍
    var nationality = $("#applicantIlnfo #nationality").val();
    if (nationality == null || "" == nationality) {
        lion.util.info("警告", "国籍不能为空");
        return false;
    }

    // 与投保人关系
    var relToHldr = $("#insuredInfo #relToHldr").val();
    if (relToHldr == null || "" == relToHldr) {
        lion.util.info("警告", "与投保人关系不能为空");
        return false;
    }

    // 性别
    var sex = $("#insuredInfo #sex").val();
    if (sex == null || "" == sex) {
        lion.util.info("警告", "性别能为空");
        return false;
    }

    // 出生日期

    var birthlDate = $("#insuredInfo #birthlDate").val();
    if (birthlDate == null || "" == birthlDate) {
        lion.util.info("警告", "出生日期不能为空");
        return false;
    }

    // 国籍
    var nationality = $("#insuredInfo #nationality").val();
    if (nationality == null || "" == nationality) {
        lion.util.info("警告", "国籍不能为空");
        return false;
    }

    // 证件类型
    var idType = $("#insuredInfo #idType").val();
    if (idType == null || "" == idType) {
        lion.util.info("警告", "证件类型不能为空");
        return false;
    }

    // 证件有效期
    var idTerm = $("#insuredInfo #idTerm").val();
    if (idTerm == null || "" == idTerm) {
        lion.util.info("警告", "证件有效期不能为空");
        return false;
    }

    // 职业代码
    var occupationCode = $("#insuredInfo #occupationCode").val();
    if (occupationCode == null || "" == occupationCode) {
        lion.util.info("警告", "职业代码不能为空");
        return false;
    }

    // 婚姻状况
    var maritalStatus = $("#insuredInfo #maritalStatus").val();
    if (maritalStatus == null || "" == maritalStatus) {
        lion.util.info("警告", "婚姻状况不能为空");
        return false;
    }

    // 兼职代码
    var ptJobCode = $("#insuredInfo #ptJobCode").val();
    if (ptJobCode == null || "" == ptJobCode) {
        lion.util.info("警告", "兼职代码不能为空");
        return false;
    }

    // 开户行代码
    var accountCode = $("#addedTaxInfo #accountCode").val();
    if (accountCode == null || "" == accountCode) {
        lion.util.info("警告", "兼职代码不能为空");
        return false;
    }

    // 缴费方式
    var paymentForm = $("#offerInfo #paymentForm").val();
    if (paymentForm == null || "" == paymentForm) {
        lion.util.info("警告", "交费方式不能为空");
        return false;
    }

    // 首期交费方式
    var firstPaymentForm = $("#offerInfo #firstPaymentForm").val();
    if (firstPaymentForm == null || "" == firstPaymentForm) {
        lion.util.info("警告", "首期交费方式不能为空");
        return false;
    }

    // 交费日期
    var paymentDate = $("#offerInfo #paymentDate").val();
    if (paymentDate == null || "" == paymentDate) {
        lion.util.info("警告", "交费日期不能为空");
        return false;
    }

    // 银行代码
    var bankCode = $("#offerInfo #bankCode").val();
    if (bankCode == null || "" == bankCode) {
        lion.util.info("警告", "银行代码不能为空");
        return false;
    }

    // 保单性质
    var applProperty = $("#offerInfo #applProperty").val();
    if (applProperty == null || "" == applProperty) {
        lion.util.info("警告", "保单性质不能为空");
        return false;
    }

    // 人工核保
    var manualCheck = $("#offerInfo #manualCheck").val();
    if (manualCheck == null || "" == manualCheck) {
        lion.util.info("警告", "人工核保不能为空");
        return false;
    }
    // 合同争议处理方式
    var insurCombined = $("#offerInfo #insurCombined").val();
    if (insurCombined == null || "" == insurCombined) {
        lion.util.info("警告", "合同争议处理方式不能为空");
        return false;
    }

    return true;
}
