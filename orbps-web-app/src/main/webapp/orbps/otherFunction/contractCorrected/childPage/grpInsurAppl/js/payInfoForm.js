com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payInfoForm=$("#payInfoForm");
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList = [];
//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
//            stlLimit : {
//                required : true,
//                isFloatGteZero : true
//            }
        },
        messages : {
//            stlLimit : {
//                required : "请输入结算金额",
//                isFloatGteZero : "请输入>=0的结算金额"
//            }
        },
		invalidHandler: function (event, validator) {
            Metronic.scrollTo(error2, -200);
        },
        
		errorPlacement:function(error,element){
			var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            if (icon.attr('title') || typeof icon.attr('data-original-title') != 'string') {
            	icon.attr('data-original-title', icon.attr('title') || '').attr('title', error.text())
            }
		},
	    
	    highlight: function (element) {
            $(element).closest('.col-md-2').removeClass("has-success").addClass('has-error');
        },
        
        success: function (label, element) {
        	var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
	});
}


$(function() {	
	
	// 初始化校验函数
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payValidateForm(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.payInfoForm);
    $("#premFrom").change(function(){
    	if($("#premFrom").val()==="2"){
    		$("#moneyinType").combo("clear");
    		$("#moneyinType").attr("disabled",true);
    	}
    });
  //如果交费形式选着银行转账，校验银行账号必须输入
    $("#payInfoForm #bankAccNo").blur(function() {
    	if ($("#moneyinType").val() === "T") {
    		var bankaccNo = $("#bankAccNo").val();
    		if (bankaccNo === null || "" === bankaccNo) {
    			lion.util.info("警告", "银行账号不能为空");
    			return false;
    		}
    	}
    });

    $("#payInfoForm #bankName").blur(function() {
    	if ($("#moneyinType").val() === "T") {
    		var bankBranchName = $("#bankName").val();
    		if (bankBranchName === null || "" === bankBranchName) {
    			lion.util.info("警告", "开户行名不能为空");
    			return false;
    		}
    	}
    });

    $("#stlLimit").blur(function() {
        var stlLimit = $("#stlLimit").val();
        if (stlLimit === null || "" === stlLimit) {
            lion.util.info("警告", "结算限额不能为空");
            return false;
        }
    });
	
	//根据结算方式更改页面样式
    $("#payInfoForm #stlType").change(function() {
        if($("#stlType").val()==="N"){
            $("#payInfoForm #stlLimit").attr("disabled",true);
            $("#payInfoForm #settlementRatio").attr("disabled",true);
            $("#payInfoForm #btnStlDate").attr("disabled",true);
        }
        else if($("#stlType").val()==="X"){
            $("#stlLimit").attr("disabled",false);
            $("#settlementRatio").attr("disabled",true);
            $("#btnStlDate").attr("disabled",false);
        }else if($("#stlType").val()==="D"){
            $("#stlLimit").attr("disabled",true);
            $("#settlementRatio").attr("disabled",true);
            $("#btnStlDate").attr("disabled",false);
        }else if($("#stlType").val()==="L"){
            $("#stlLimit").attr("disabled",true);
            $("#settlementRatio").attr("disabled",false);
            $("#btnStlDate").attr("disabled",true);
        }
    });
    
 	//如果交费形式是现金，置灰交费开户行，开户名称，银行账号输入框
    $("#moneyinType,#premFrom").change(function(){
    	var ss = $("#moneyinType").val();
    	if($("#moneyinType").val()==="C" || $("#premFrom").val()==="2" || $("#moneyinType").val()==="R"){
            $("#bankCode").attr("disabled",true);
            $("#bankCode").combo("clear");
            $("#bankName").attr("disabled",true);
            $("#bankName").val("");
            $("#bankAccNo").attr("disabled",true);
            $("#bankAccNofa").removeClass("fa-warning");
            $("#bankAccNofa").removeClass("fa-check");
            $("#bankAccNo").val("");
        }else{
            $("#moneyinType").attr("disabled",false);
        	 $("#bankCode").attr("disabled",false);
             $("#bankName").attr("disabled",false);
             $("#bankAccNo").attr("disabled",false);
        }
    });
    	
	// 定期结算日期
	$("#btnStlDate")
			.click(
					function() {
						com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.empty();
						 com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog
								.load(
										"/orbps/orbps/otherFunction/contractCorrected/childPage/grpInsurAppl/html/settlementDateModal.html",
										function() {
											$(this).modal('toggle');
										});
						setTimeout(function() {
							// 回显
							com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.reloadsettlementInsuredModalTable();
						}, 100);
					});

	// 重新加载表格
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.reloadsettlementInsuredModalTable = function () {
		$('#settlementDataGrid').find("tbody").empty();
		if (com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList != null
				&& com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList.length > 0) {
			for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList.length; i++) {
				settlementDatas = new Date(
						com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.settleList[i])
						.format("yyyy-MM-dd");
				$('#settlementDataGrid')
						.find("tbody")
						.append(
								"<tr role='row' class='odd' id='settlementDataGridtr0' data-type='add' data-updating='false'><td ><input type='radio' onclick='com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.dateRadio();' id='insuredRad"
										+ i
										+ "' name='insuredRad' value='"
										+ i
										+ "'></td><td >"
										+ settlementDatas
										+ "</td><td ><a href='javascript:void(0);' onclick='com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.deleteSettleDate("
										+ i
										+ ");' for='insuredDel' id='insuredDel"
										+ i + "'>删除</a></td></tr>");
			}
		} else {
			$('#settlementDataGrid').find("tbody").append(
					"<tr><td colspan='3' align='center'>无记录</td></tr>");
		}
	}
});

