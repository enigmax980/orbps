com.orbps.contractReview.sgGrpInsurAppl.payInfoForm = $("#payInfoForm");

// 基本信息校验规则
com.orbps.contractReview.sgGrpInsurAppl.payValidateForm = function(vform) {
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
    com.orbps.contractReview.sgGrpInsurAppl
            .payValidateForm(com.orbps.contractReview.sgGrpInsurAppl.payInfoForm);
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
	//如果保费来源是个人账户交费形式也要置灰
	if($("#premFrom").val()==="2"){
		$("#moneyinType").attr("disabled",true);
		$("#moneyinType").combo("clear");
	}
})
	


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

$("#payInfoForm #bankBranchName").blur(function() {
	if ($("#moneyinType").val() === "T" && $("#premFrom").val()!=="2") {
		var bankBranchName = $("#bankBranchName").val();
		if (bankBranchName === null || "" === bankBranchName) {
			lion.util.info("警告", "开户行名不能为空");
			return false;
		}
	}
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
