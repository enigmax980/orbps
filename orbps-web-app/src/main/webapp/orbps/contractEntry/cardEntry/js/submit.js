//定义form
com.orbps.contractEntry.cardEntry.submitForm = $('#submitForm');

$(function() {
    // 表单提交
    $('#submitForm #btnSubmit')
            .click(
                    function() {
                        if (com.orbps.contractEntry.cardEntry.applInfoForm
                                .validate().form()
                                || com.orbps.contractEntry.cardEntry.ipsnInfoForm
                                        .validate().form()
                                || com.orbps.contractEntry.cardEntry.accInfoForm
                                        .validate().form()) {
                            if (validateSelectVal()) {
                                // 提交方法
                                var CardEntryVo = {};
                                CardEntryVo.CardApplInfoVo = com.orbps.contractEntry.cardEntry.applInfoForm
                                        .serializeObject();
                                CardEntryVo.CardIpsnlInfoVo = com.orbps.contractEntry.cardEntry.ipsnInfoForm
                                        .serializeObject();
                                CardEntryVo.CardBnfrInfoVo = com.orbps.contractEntry.cardEntry.bnfrInfoForm
                                        .serializeObject();
                                CardEntryVo.CardBsInfoVo = com.orbps.contractEntry.cardEntry.bsInfoForm
                                        .serializeObject();
                                CardEntryVo.CardAccInfoVo = com.orbps.contractEntry.cardEntry.accInfoForm
                                        .serializeObject();
                                alert(JSON.stringify(CardEntryVo));
                                lion.util
                                        .postjson(
                                                '/orbps/web/orbps/contractEntry/grp/submit',
                                                CardEntryVo, successSubmit,
                                                null, null);
                            }
                        }
                    });

    // 校验选择信息
    function validateSelectVal() {
        $('#applInfoForm #salesChannel').blur(function() {
            var salesChannel = $('#salesChannel').val();
            if (salesChannel == null || '' == salesChannel) {
                lion.util.info('警告', '销售渠道代码不能为空');
                return false;
            }
        });

        $('#applInfoForm #idType').blur(function() {
            var idType = $('#applInfoForm #idType').val();
            if (idType == null || '' == idType) {
                lion.util.info('警告', '投保人证件类别不能为空');
                return false;
            }
        });

        $('#applInfoForm #sex').blur(function() {
            var sex = $('#applInfoForm #sex').val();
            if (ipsnIdCode == null || '' == ipsnIdCode) {
                lion.util.info('警告', '投保人性别不能为空');
                return false;
            }
        });

        $('#applInfoForm #birthDate').blur(function() {
            var birthDate = $('#applInfoForm #birthDate').val();
            if (birthDate == null || '' == birthDate) {
                lion.util.info('警告', '出生日期号码不能为空');
                return false;
            }
        });

        $('#applInfoForm #ipsnOccClassCod').blur(function() {
            var ipsnOccClassCod = $('#applInfoForm #ipsnOccClassCod').val();
            if (ipsnOccClassCod == null || '' == ipsnOccClassCod) {
                lion.util.info('警告', '投保人职业代码不能为空');
                return false;
            }
        });

        $('#applInfoForm #nationality').blur(function() {
            var nationality = $('#applInfoForm #nationality').val();
            if (nationality == null || '' == nationality) {
                lion.util.info('警告', '投保人国籍不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #relToHldr').blur(function() {
            var relToHldr = $('#ipsnInfoForm #relToHldr').val();
            if (relToHldr == null || '' == relToHldr) {
                lion.util.info('警告', '与投保人关系不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #jointInsured').blur(function() {
            var jointInsured = $('#ipsnInfoForm #jointInsured').val();
            if (jointInsured == null || '' == jointInsured) {
                lion.util.info('警告', '是否为连带被保人不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #ipsnIdType').blur(function() {
            var ipsnIdType = $('#ipsnInfoForm #ipsnIdType').val();
            if (ipsnIdType == null || '' == ipsnIdType) {
                lion.util.info('警告', '被保人证件类别不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #sex').blur(function() {
            var sex = $('#ipsnInfoForm #sex').val();
            if (sex == null || '' == sex) {
                lion.util.info('警告', '被保人性别不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #birthDate').blur(function() {
            var birthDate = $('#ipsnInfoForm #birthDate').val();
            if (birthDate == null || '' == birthDate) {
                lion.util.info('警告', '被保人出生日期不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #ipsnOccClassCod').blur(function() {
            var ipsnOccClassCod = $('#ipsnInfoForm #ipsnOccClassCod').val();
            if (ipsnOccClassCod == null || '' == ipsnOccClassCod) {
                lion.util.info('警告', '被保人职业代码不能为空');
                return false;
            }
        });

        $('#ipsnInfoForm #relToIpsn').blur(function() {
            var relToIpsn = $('#ipsnInfoForm #relToIpsn').val();
            if (relToIpsn == null || '' == relToIpsn) {
                lion.util.info('警告', '与主被保人关系为空');
                return false;
            }
        });

        $('#ipsnInfoForm #nationality').blur(function() {
            var nationality = $('#ipsnInfoForm #nationality').val();
            if (nationality == null || '' == nationality) {
                lion.util.info('警告', '被保人国籍 为空');
                return false;
            }
        });

        $('#ipsnInfoForm #ipsnSss').blur(function() {
            var ipsnSss = $('#ipsnInfoForm #ipsnSss').val();
            if (ipsnSss == null || '' == ipsnSss) {
                lion.util.info('警告', '被保人医保身份为空');
                return false;
            }
        });

        $('#accInfoForm #ernstMoneyinType').blur(function() {
            var ernstMoneyinType = $('#accInfoForm #ernstMoneyinType').val();
            if (ernstMoneyinType == null || '' == ernstMoneyinType) {
                lion.util.info('警告', '首期交费形式不能为空');
                return false;
            }
        });

        $('#accInfoForm #receiptDate').blur(function() {
            var receiptDate = $('#accInfoForm #receiptDate').val();
            if (receiptDate == null || '' == receiptDate) {
                lion.util.info('警告', '实收日期不能为空');
                return false;
            }
        });

        $('#accInfoForm #continuousInsurance').blur(
                function() {
                    var continuousInsurance = $(
                            '#accInfoForm #continuousInsurance').val();
                    if (continuousInsurance == null
                            || '' == continuousInsurance) {
                        lion.util.info('警告', '是否连续投保不能为空');
                        return false;
                    }
                });

        $('#accInfoForm #currencyCode').blur(function() {
            var currencyCode = $('#accInfoForm #currencyCode').val();
            if (currencyCode == null || '' == currencyCode) {
                lion.util.info('警告', '币种不能为空');
                return false;
            }
        });

        $('#accInfoForm #renewalFeeForm').blur(function() {
            var renewalFeeForm = $('#accInfoForm #renewalFeeForm').val();
            if (renewalFeeForm == null || '' == renewalFeeForm) {
                lion.util.info('警告', '续期交费形式不能为空');
                return false;
            }
        });

        $('#accInfoForm #payBankCode').blur(function() {
            var payBankCode = $('#accInfoForm #payBankCode').val();
            if (payBankCode == null || '' == payBankCode) {
                lion.util.info('警告', '银行代码不能为空');
                return false;
            }
        });

        $('#accInfoForm #accountForm').blur(function() {
            var accountForm = $('#accInfoForm #accountForm').val();
            if (accountForm == null || '' == accountForm) {
                lion.util.info('警告', '账户形式不能为空');
                return false;
            }
        });

        $('#accInfoForm #relToHldr').blur(function() {
            var relToHldr = $('#accInfoForm #relToHldr').val();
            if (relToHldr == null || '' == relToHldr) {
                lion.util.info('警告', '账户与投保人关系不能为空');
                return false;
            }
        });

        $('#accInfoForm #signDate').blur(function() {
            var signDate = $('#accInfoForm #signDate').val();
            if (signDate == null || '' == signDate) {
                lion.util.info('警告', '签单日期不能为空');
                return false;
            }
        });

        $('#accInfoForm #designatedEffectiveDate').blur(
                function() {
                    var designatedEffectiveDate = $(
                            '#accInfoForm #designatedEffectiveDate').val();
                    if (designatedEffectiveDate == null
                            || '' == designatedEffectiveDate) {
                        lion.util.info('警告', '指定生效日不能为空');
                        return false;
                    }
                });

        $('#accInfoForm #policyNature').blur(function() {
            var policyNature = $('#accInfoForm #policyNature').val();
            if (policyNature == null || '' == policyNature) {
                lion.util.info('警告', '保单性质 不能为空');
                return false;
            }
        });

        $('#accInfoForm #sendType').blur(function() {
            var sendType = $('#accInfoForm #sendType').val();
            if (sendType == null || '' == sendType) {
                lion.util.info('警告', '送达类型不能为空');
                return false;
            }
        });

        $('#accInfoForm #insurancePeriodStartDate').blur(
                function() {
                    var insurancePeriodStartDate = $(
                            '#accInfoForm #insurancePeriodStartDate').val();
                    if (insurancePeriodStartDate == null
                            || '' == insurancePeriodStartDate) {
                        lion.util.info('警告', '保险期间起始日期不能为空');
                        return false;
                    }
                });

        $('#accInfoForm #disputeResolution').blur(function() {
            var disputeResolution = $('#accInfoForm #disputeResolution').val();
            if (disputeResolution == null || '' == disputeResolution) {
                lion.util.info('警告', '合同争议处理方式不能为空');
                return false;
            }
        });

        $('#accInfoForm #insurancePeriodEndDate').blur(
                function() {
                    var insurancePeriodEndDate = $(
                            '#accInfoForm #insurancePeriodEndDate').val();
                    if (insurancePeriodEndDate == null
                            || '' == insurancePeriodEndDate) {
                        lion.util.info('警告', '终止日期不能为空');
                        return false;
                    }
                });
    }

    // 添加成功回调函数
    function successSubmit(obj, data, arg) {
        lion.util.info('提示', '提交团单录入信息成功');
        // 成功后刷新页面
        setTimeOut(function() {
            window.location.reload();
        }, 500);
    }
});
