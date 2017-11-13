//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.paylidateForm = function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
		rules : {
			ernstMoneyinType : {
				required : true,
			},
			receiptDate : {
				required : true,
			},
			continuousInsurance : {
				required : true,
			},
			currencyCode : {
				required : true,
			},
			renewalFeeForm : {
				required : true,
			},
			payBankCode : {
				required : true,
			},
			bankaccNo : {
				required : true,
				digits:true,
			},
			accountForm : {
				required : true,
			},
			accountHolder : {
				required : true,
			},
			ipsnRelationCode : {
				required : true,
			},
			ipsnIdNo : {
				required : true,
			},
			signDate : {
				required : true,
			},
			inforceDate : {
				required : true,
			},
			policyNature : {
				required : true,
			},
			serviceType : {
				required : true,
			},
			appOfficeTel : {
				required : true,
			},
			disputeResolution : {
				required : true,
			},
			arbitrationCommissionName : {
				required : true,
			},
			convention : {
				required : true,
			},
			
				
    
		},
		messages : {
			ernstMoneyinType : {
				required : '请输入首期交费形式',
			},
			receiptDate : {
				required : '请输入实收日期',
			},
			continuousInsurance : {
				required : '请选择是否连续投保',
			},
			currencyCode : {
				required : '请选择币种',
			},
			renewalFeeForm : {
				required : '请选择续期交费形式',
			},
			payBankCode : {
				required : '请选择银行代码',
			},
			bankaccNo : {
				required : '请输入缴费账号',
				digits:'请输入数字！'
			},
			accountForm : {
				required : '请选择账户形式',
			},
			accountHolder : {
				required : '请输入账户持有人',
			},
			ipsnRelationCode : {
				required : '请选择与投保人关系',
			},
			ipsnIdNo : {
				required : '请输入身份证号',
			},
			signDate : {
				required : '请输入签单日期',
			},
			inforceDate : {
				required : '请输入指定生效日',
			},
			policyNature : {
				required : '请选择保单性质 ',
			},
			serviceType : {
				required : '请选择送达类型 ',
			},
			appOfficeTel : {
				required : '请输入同业公司人身保险保额合计',
			},
			disputeResolution : {
				required : '请选择合同争议处理方式',
			},
			arbitrationCommissionName : {
				required : '请输入仲裁委员会名称',
			},
			convention : {
				required : '请输入特别约定',
			},
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
        
        submitHandler: function (form) {
            success2.show();
            error2.hide();
            form[0].submit();
        },
        
        success: function (label, element) {
        	var icon = $(element).parent('.input-icon').children('i');
            $(element).closest('.col-md-2').removeClass('has-error').addClass('has-success');
            icon.removeClass("fa-warning").addClass("fa-check");
        }
	});
}

$(function(){
	// 初始化校验信息
	com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.paylidateForm(com.orbps.otherFunction.contractCorrected.childPage.combinedPolicy.payForm);
	$("#payForm #ernstMoneyinType").blur(function () {  
		var ernstMoneyinType = $("#ernstMoneyinType").val();
		if(ernstMoneyinType==null||""==ernstMoneyinType){
			lion.util.info("警告","首期交费形式不能为空");
			return false;
		}
	}); 
	
	$("#payForm #receiptDate").blur(function () {  
		var receiptDate = $("#receiptDate").val();
		if(receiptDate==null||""==receiptDate){
			lion.util.info("警告","实收日期不能为空");
			return false;
		}
	}); 
	
	$("#payForm #bankaccNo").blur(function () {  
		var bankaccNo = $("#bankaccNo").val();
		if(bankaccNo==null||""==bankaccNo){
			lion.util.info("警告","缴费账号不能为空");
			return false;
		}
	}); 
	
	$("#payForm #accountHolder").blur(function () {  
		var accountHolder = $("#accountHolder").val();
		if(accountHolder==null||""==accountHolder){
			lion.util.info("警告","账户持有人不能为空");
			return false;
		}
	}); 
	
	$("#payForm #ipsnIdNo").blur(function () {  
		var ipsnIdNo = $("#ipsnIdNo").val();
		if(ipsnIdNo==null||""==ipsnIdNo){
			lion.util.info("警告","身份证号不能为空");
			return false;
		}
	}); 
	
	$("#payForm #inforceDate").blur(function () {  
		var inforceDate = $("#inforceDate").val();
		if(inforceDate==null||""==inforceDate){
			lion.util.info("警告","指定生效日不能为空");
			return false;
		}
	}); 
	
	$("#payForm #appOfficeTel").blur(function () {  
		var appOfficeTel = $("#appOfficeTel").val();
		if(appOfficeTel==null||""==appOfficeTel){
			lion.util.info("警告","同业公司人身保险保额合计不能为空");
			return false;
		}
	}); 
	
	$("#payForm #arbitrationCommissionName").blur(function () {  
		var arbitrationCommissionName = $("#arbitrationCommissionName").val();
		if(arbitrationCommissionName==null||""==arbitrationCommissionName){
			lion.util.info("警告","仲裁委员会名称不能为空");
			return false;
		}
	}); 
	
	$("#payForm #convention").blur(function () {  
		var convention = $("#convention").val();
		if(convention==null||""==convention){
			lion.util.info("警告","特别约定不能为空");
			return false;
		}
	}); 
	
	setTimeout(function(){
		//是否连续投保下拉框默认显示否
		$("#payForm #continuousInsurance").combo("val","N");
	},1000);
});

