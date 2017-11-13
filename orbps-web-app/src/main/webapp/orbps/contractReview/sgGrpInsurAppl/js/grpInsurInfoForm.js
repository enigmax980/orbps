com.orbps.contractReview.sgGrpInsurAppl.grpInsurInfoForm = $("#grpInsurInfoForm");

//基本信息校验规则
com.orbps.contractReview.sgGrpInsurAppl.applBaseValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
        	gInsuredTotalNum: {
                required : true, 
                isIntGteZero : true
            },
            companyName : {
                required : true
            },
            idNo : {
                required : true
            },
            insuredTotalNum : {
                required : true,
                isIntGteZero : true
            },
            zipCode : {
                required : true,
                isZipCode : true
            },
            contactName : {
                required : true,
            },
            contactIdNo : {
                required : true,
                isIdCardNos : true
            },
            contactMobile : {
                isMobile : true
            },
            contactEmail : {
                email : true
            },
            contactTel : {
                isTel : true
            },
            faxNo : {
                isFax : true
            },
        },
        messages : {
        	gInsuredTotalNum: {
                required : "请输入投保人数", 
                isIntGteZero : "请输入大于0的数字"
            },
            companyName : {
                required : "单位/团体名称不能为空"
            },
            idNo : {
                required : "证件号码不能为空"
            },
            insuredTotalNum : {
                required : "投保人数不能为空",
                isIntGteZero : "请输入大于0的整数"
            },
            zipCode : {
                required : "邮政编码不能为空",
                isZipCode : "请输入正确的邮政编码"
            },      
            contactName : {
                required : "联系人姓名不能为空"
            },
            contactIdNo : {
                required : "联系人证件号码不能为空",               
                isIdCardNos : "请您核对并输入联系人正确的证件号码"
            },
            contactMobile : {
                isMobile : "请输入正确的电话号码"
            },
            contactEmail : {
                email : "请输入正确格式的邮箱"
            },
            contactTel : {
                isTel : "请输入正确的固定电话"
            },
            faxNo : {
                isFax : "请输入正确的传真号码"
            },
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
    // 校验函数初始化
    com.orbps.contractReview.sgGrpInsurAppl
            .applBaseValidateForm(com.orbps.contractReview.sgGrpInsurAppl.grpInsurInfoForm);

  //根据争议处理方式调整页面样式
    $("#gSettleDispute").change(function() {
        if($("#gSettleDispute").val()==="1"){
            $("#parbOrgName").attr("disabled",true);
            $("#parbOrgName").val("");
        }else{
            $("#parbOrgName").attr("disabled",false);
        }
    });

    //根据争议处理方式调整页面样式
    $("#pSettleDispute").change(function() {
        if($("#pSettleDispute").val()==="1"){
            $("#parbOrgName").attr("disabled",true);
            $("#parbOrgName").val("");
        }else{
            $("#parbOrgName").attr("disabled",false);
        }
    });
    // 联系人身份证号验证
    jQuery.validator.addMethod("isIdCardNos", function(value, element) {
        var contactIdTypeValue = $("#grpInsurInfoForm #contactIdType").val();
        // 先判断证件类型是否为身份证再去校验
        if (contactIdTypeValue === "I") {
            return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
        } else {
            return true;
        }
    }, "请正确输入您的身份证号码！");
      

 // 如果联系人证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别、年龄等
 $("#grpInsurInfoForm #contactIdNo").blur(
         function() {
             idcard($("#grpInsurInfoForm #contactIdType"),
                     $("#grpInsurInfoForm #contactIdNo"),
                     $("#grpInsurInfoForm #birthDate"),
                     $("#grpInsurInfoForm #sex"));
         });

 // 当联系人证件类型改变的 时候，将五要素只读变成可写
 $("#grpInsurInfoForm #contactIdType")
         .change(
                 function() {
                	 
                	 com.orbps.contractReview.sgGrpInsurAppl.contactIdTypeValue = $(
                             "#grpInsurInfoForm #contactIdType").val();
                     if ("I" !== com.orbps.contractReview.sgGrpInsurAppl.contactIdTypeValue) {
                         $("#grpInsurInfoForm #birthDate").attr("readonly",
                                 false);
                         $("#grpInsurInfoForm #sex").attr("readonly",
                                 false);
                     }
                 });
 $("#grpInsurInfoForm #companyName").blur(function() {
     var companyName = $("#companyName").val();
     if (companyName === null || "" === companyName) {
         lion.util.info("警告", "单位/团体名称不能为空");
         return false;
     }
 });

 $("#grpInsurInfoForm #idNo").blur(function() {
     var idNo = $("#idNo").val();
     if (idNo === null || "" === idNo) {
         lion.util.info("警告", "证件号码不能为空");
         return false;
     }
 });

 $("#grpInsurInfoForm #insuredTotalNum").blur(function() {
     var insuredTotalNum = $("#insuredTotalNum").val();
     if (insuredTotalNum === null || "" === insuredTotalNum) {
         lion.util.info("警告", "投保人数不能为空");
         return false;
     }
 });
 
 $("#grpInsurInfoForm #zipCode").blur(function() {
     var zipCode = $("#zipCode").val();
     if (zipCode === null || "" === zipCode) {
         lion.util.info("警告", "邮编不能为空");
         return false;
     }
 });
 
 $("#grpInsurInfoForm #townCode").blur(function() {
	    var townCode = $("#townCode").val();
	    if (townCode === null || "" === townCode) {
	        lion.util.info("警告", "乡镇不能为空");
	        return false;
	    }
	});
 
 $("#grpInsurInfoForm #villageCode").blur(function() {
	    var villageCode = $("#villageCode").val();
	    if (villageCode === null || "" === villageCode) {
	        lion.util.info("警告", "村/社区不能为空");
	        return false;
	    }
	});

 $("#grpInsurInfoForm #contactName").blur(function() {
     var contactName = $("#contactName").val();
     if (contactName === null || "" === contactName) {
         lion.util.info("警告", "联系人姓名不能为空");
         return false;
     }
 });

 $("#grpInsurInfoForm #contactIdNo").blur(function() {
     var contactIdNo = $("#contactIdNo").val();
     if (contactIdNo === null || "" === contactIdNo) {
         lion.util.info("警告", "联系人证件号码不能为空");
         return false;
     }
 });

 $("#grpInsurInfoForm #contactMobile").blur(function() {
     var contactMobile = $("#contactMobile").val();
     if (contactMobile === null || "" === contactMobile) {
         lion.util.info("警告", "联系人移动电话不能为空");
         return false;
     }
 });
 $("#grpInsurInfoForm #contactTel").blur(function() {
     var contactTel = $("#contactTel").val();
     if (contactTel === null || "" === contactTel) {
         lion.util.info("警告", "固定电话不能为空");
         return false;
     }
 });
 $("#grpInsurInfoForm #home").blur(function() {
     var home = $("#home").val();
     if (home === null || "" === home) {
         lion.util.info("警告", "详细地址不能为空");
         return false;
     }
 });
//仲裁机构名称字符简直能有一个空格
	$("#grpInsurInfoForm #parbOrgName").blur(function() {
		var parbOrgName = $("#grpInsurInfoForm #parbOrgName").val();
		if (parbOrgName === null || "" === parbOrgName) {
			lion.util.info("警告", "仲裁机构名称不能为空");
			return false;
		}
		// 字符间只能有一个空格
		parbOrgName = parbOrgName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
		$("#grpInsurInfoForm #parbOrgName").val(parbOrgName);
	});
		
});

