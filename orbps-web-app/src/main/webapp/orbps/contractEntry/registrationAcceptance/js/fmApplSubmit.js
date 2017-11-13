com.orbps.contractEntry.registrationAcceptance.fmApplSubmitForm = $("#fmApplSubmitForm");
com.orbps.contractEntry.registrationAcceptance.fmApplListForm =  $("#fmApplListForm");
// 基本信息校验规则

$(function() {

    // 初始化校验函数
    com.orbps.contractEntry.registrationAcceptance
            .fmApplValidateForm(com.orbps.contractEntry.registrationAcceptance.fmApplSubmitForm);

    // 表单提交
    $("#fmBtnCommit")
            .click(
                    function() {
                        // jquery validate 校验
                        if (com.orbps.contractEntry.registrationAcceptance.fmApplListForm
                                .validate().form()) {
                            // select校验
                            if (validateSelectVal()) {
                                // ajax
                            }
                        }
                    });

    // 校验选择信息
    function validateSelectVal() {
        var entChannelFlag = $("#fmApplListForm #entChannelFlag").val();
        if (entChannelFlag == null || "" == entChannelFlag) {
            lion.util.info("警告", "请选择外包录入标记");
            return false;
        }
        var insurType = $("#fmApplListForm #insurType").val();
        if (insurType == null || "" == insurType) {
            lion.util.info("警告", "请选择契约形式");
            return false;
        }
        var polCode = $("#fmApplListForm #polCode").val();
        if (polCode == null || "" == polCode) {
            lion.util.info("警告", "请选择险种信息");
            return false;
        }
    }
});

//表单提交
$("#fmApplSubmitForm #fmBtnCommit")
        .click(
                function() {
                    // jquery validate 校验
//                    if (com.orbps.contractEntry.registrationAcceptance.usualAcceptForm
//                            .validate().form()
//                            || com.orbps.contractEntry.registrationAcceptance.sgApplListForm
//                                    .validate().form()) {
                        // select校验
                        //if () {
                            // 获取要约信息下面的所有险种信息
//                            var getAddRowsData = $("#fbp-editDataGrid")
//                                    .editDatagrids("getAddRowsData");
                            // // 提交方法
                            var regAcceptanceVo = {};
                            regAcceptanceVo.fmApplListFormVo = com.orbps.contractEntry.registrationAcceptance.fmApplListForm
                                    .serializeObject();                           
                            //alert(JSON.stringify(regAcceptanceVo));
                           //  }
                          //  }
                            lion.util
                                    .postjson(
                                            '/orbps/web/orbps/contractEntry/reg/submitFmA',
                                            regAcceptanceVo,
                                            com.orbps.contractEntry.registrationAcceptance.successSubmit,
                                            null,
                                            null);
//                        }
//                    }
                });



