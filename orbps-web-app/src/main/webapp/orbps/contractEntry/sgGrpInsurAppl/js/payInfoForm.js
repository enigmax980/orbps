com.orbps.contractEntry.sgGrpInsurAppl.payInfoForm = $("#payInfoForm");

// 基本信息校验规则
com.orbps.contractEntry.sgGrpInsurAppl.payValidateForm = function(vform) {
  var error2 = $('.alert-danger', vform);
  var success2 = $('.alert-success', vform);
  vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
        },
        messages : {         
        },
        invalidHandler : function(event, validator) {
            Metronic.scrollTo(error2, -200);
        },

        errorPlacement : function(error, element) {
            var icon = $(element).parent('.input-icon').children('i');
            icon.removeClass('fa-check').addClass("fa-warning");
            if (icon.attr('title')
                    || typeof icon.attr('data-original-title') !== 'string') {
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
    
    // 初始化校验函数
    com.orbps.contractEntry.sgGrpInsurAppl
            .payValidateForm(com.orbps.contractEntry.sgGrpInsurAppl.payInfoForm);
});
//如果交费形式是现金，置灰交费开户行，开户名称，银行账号输入框
$("#moneyinType,#premFrom").change(function(){
	if($("#moneyinType").val()==="R" || $("#moneyinType").val()==="C" || $("#premFrom").val()==="2"){
        $("#bankCode").attr("disabled",true);
        $("#bankCode").combo("clear");
        $("#bankBranchName").attr("disabled",true);
        $("#bankBranchName").val("");
        $("#bankaccNo").attr("disabled",true);
        $("#bankaccNo").val("");
    }else{
        $("#moneyinType").attr("disabled",false);
    	 $("#bankCode").attr("disabled",false);
         $("#bankBranchName").attr("disabled",false);
         $("#bankaccNo").attr("disabled",false);
    }
});
$("#premFrom").change(function(){
	//如果保费来源是个人账户或团体账户时供费比例和供费金额置灰
	if($("#premFrom").val()==="2" || $("#premFrom").val()==="1"){
		$("#multiPartyScale").attr("disabled",true);
		$("#multiPartyScale").combo("clear");
		$("#multiPartyMoney").attr("disabled",true);
		$("#multiPartyMoney").combo("clear");
	}else{
		$("#multiPartyScale").attr("disabled",false);
		$("#multiPartyMoney").attr("disabled",false);
	}
});

$("#premFrom").change(function(){
	//如果保费来源是个人账户交费形式也要置灰
	if($("#premFrom").val()==="2"){
		$("#moneyinType").attr("disabled",true);
		$("#moneyinType").combo("clear");
	}
});

//供款金额和供款比例不能同时为空
com.orbps.contractEntry.sgGrpInsurAppl.moneyScale = function(){
	var scale = $("#payInfoForm #multiPartyScale").val();
	var money =  $("#payInfoForm #multiPartyMoney").val();
	if((""!==scale && null!==scale) || (""!==money && null!==money)){
		return true;
	}else{
		lion.util.info("警告", "供款金额和供款比例不能同时为空！");
		$(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
		return false;
	}
};
	


//如果交费形式选着银行转账，校验银行账号必须输入
$("#payInfoForm #bankaccNo").blur(function() {
	if ($("#moneyinType").val() === "T" && $("#premFrom").val()!=="2") {
		var bankaccNo = $("#bankaccNo").val();
		if (bankaccNo === null || "" === bankaccNo) {
			lion.util.info("警告", "银行账号不能为空");
			return false;
		}
	}
});

$("#payInfoForm #multiPartyScale").blur(function(){
	com.orbps.contractEntry.sgGrpInsurAppl.moneyScale();
});
$("#payInfoForm #multiPartyMoney").blur(function(){
	com.orbps.contractEntry.sgGrpInsurAppl.moneyScale();
});

$("#payInfoForm #bankBranchName").blur(function() {
	var bankBranchName = $("#bankBranchName").val();
	if ($("#moneyinType").val() === "T" && $("#premFrom").val()!=="2") {
		if (bankBranchName === null || "" === bankBranchName) {
			lion.util.info("警告", "开户行名不能为空");
			return false;
		}
	}
	// 投保人、汇缴人字符间只能有一个空格
	bankBranchName=bankBranchName.replace(/^ +| +$/g,'').replace(/ +/g,' ');
	$("#bankBranchName").val(bankBranchName);
});
//续期扣款改变页面样式
//$("#renewalChargeFlag").change(function(){
//	if($("#renewalChargeFlag").val()==="N"){
//		$("#chargeDeadline").attr("disabled",true);
//		$("#chargeDeadlinebtn").attr("disabled",true);
//		$("#chargeDeadline").val("");
//	    $(".fa").removeClass("fa-warning");
//	    $(".fa").removeClass("fa-check");
//	    $(".fa").removeClass("has-success");
//	    $(".fa").removeClass("has-error");
//	}else if($("#renewalChargeFlag").val()==="Y"){
//		$("#chargeDeadline").attr("disabled",false);
//		$("#chargeDeadlinebtn").attr("disabled",false);
//	}
//});


//多期暂交改变页面样式
$("#multiRevFlag").change(function(){
	if($("#multiRevFlag").val()==="N"){
		$("#multiTempYear").attr("disabled",true)
		$("#multiTempYear").val("");
	    $(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
	}else if($("#multiRevFlag").val()==="Y"){
		$("#multiTempYear").attr("disabled",false)
	}
});

$("#payInfoForm #multiTempYear").blur(function() {
	if($("#multiRevFlag").val()==="Y") {
		var multiTempYear = $("#multiTempYear").val();
		if (multiTempYear === null || "" === multiTempYear) {
        lion.util.info("警告", "请输入多期暂交年数");
        return false;
		}   
	    var zh_verify = /^[0-9]*[1-9][0-9]*$/;
		if(!zh_verify.test( multiTempYear ))
			lion.util.info("警告", "请输入大于零的整数类型多期暂交年数");
		return false;
	}
});

//是否续保改变页面样式
$("#renewFlag").change(function(){
	if($("#renewFlag").val()==="N"){
		$("#renewChargeNum").attr("disabled",true)
		$("#renewChargeNum").val("");
	    $(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
	}else if($("#renewFlag").val()==="Y"){
		$("#renewChargeNum").attr("disabled",false)
	}
});

$("#payInfoForm #renewChargeNum").blur(function() {
	if("Y"===$("#renewFlag").val()){
		var renewChargeNum = $("#renewChargeNum").val();
	    if (renewChargeNum === null || "" === renewChargeNum) {
	        lion.util.info("警告", "续保扣款期数不能为空");
	        return false;
	    }
	    var zh_verify = /^[0-9]*[1-9][0-9]*$/;
		if(!zh_verify.test( renewChargeNum )){
			lion.util.info("警告", "请输入大于零的整数类型续保扣款期数");
			return false;
		}
	}
});



