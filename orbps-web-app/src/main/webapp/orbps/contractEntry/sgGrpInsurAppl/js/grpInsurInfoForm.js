com.orbps.contractEntry.sgGrpInsurAppl.grpInsurInfoForm = $("#grpInsurInfoForm");


com.orbps.contractEntry.sgGrpInsurAppl.applBaseValidateForm = function(vform) {
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

//联系人固定电话、移动电话不能同时为空
com.orbps.contractEntry.sgGrpInsurAppl.isPhoneOrTel = function(){
	var phone = $("#grpInsurInfoForm #contactMobile").val();
	var tel =  $("#grpInsurInfoForm #contactTel").val();
	if((""!==phone && null!==phone) || (""!==tel && null!==tel)){
		return true;
	}else{
		lion.util.info("警告", "联系人移动电话和联系人固定电话不能同时为空！");
		$(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
		return false;
	}
};


$(function() {
    //被保险人总数和投保人数相等
	$("#grpInsurInfoForm #gInsuredTotalNum").change(function() {
		var gInsuredTotalNum=$("#grpInsurInfoForm #gInsuredTotalNum").val();
		$("#proposalInfoForm #insuredTotalNum").val(gInsuredTotalNum);
	
    });
	//暂时没用到
	//联系人证件号码为200个字符
//	$("#grpInsurInfoForm #contactIdNo").on(
//			'keyup',
//			function(event) {
//	var contactIdNo = $("#grpInsurInfoForm #contactIdNo").val();
//	var len = contactIdNo.length;
//	var reLen = 0;
//	for (var i = 0; i < len; i++) {        
//		if (contactIdNo.charCodeAt(i) < 27 || contactIdNo.charCodeAt(i) > 126) {
//			// 全角    
//			reLen += 2;
//		} else {
//			reLen++;
//		}
//	}
//	if(reLen>25){
//		contactIdNo = contactIdNo.substring(0,contactIdNo.length-1);
//		$("#grpInsurInfoForm #contactIdNo").val(contactIdNo);
//	}
//});
	
	//联系人姓名长度为200个字符
//	$("#grpInsurInfoForm #contactName").on(
//			'keyup',
//			function(event) {
//	var contactName = $("#grpInsurInfoForm #contactName").val();
//	var len = contactName.length;
//	var reLen = 0;
//	for (var i = 0; i < len; i++) {        
//		if (contactName.charCodeAt(i) < 27 || contactName.charCodeAt(i) > 126) {
//			// 全角    
//			reLen += 2;
//		} else {
//			reLen++;
//		}
//	}
//	if(reLen>200){
//		contactName = contactName.substring(0,contactName.length-1);
//		$("#grpInsurInfoForm #contactName").val(contactName);
//	}
//});
	
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
	
    // 校验函数初始化
    com.orbps.contractEntry.sgGrpInsurAppl
            .applBaseValidateForm(com.orbps.contractEntry.sgGrpInsurAppl.grpInsurInfoForm);
    
    $("#citySelect").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        /* prov:"北京", */
        /* nodata:"none", */
        required:false
    });
    
    //根据争议处理方式调整页面样式
    $("#grpInsurInfoForm #gSettleDispute").change(function() {
        if($("#gSettleDispute").val()==="1"){
            $("#parbOrgName").attr("disabled",true);
            $("#parbOrgName").val("");
        }else{
            $("#parbOrgName").attr("disabled",false);
        }
    });

    $("#grpInsurInfoForm #parbOrgName").blur(function(){
    	if($("#gSettleDispute").val()==="2"){
    		var arbOrgName = $("#parbOrgName").val();
    	     if (arbOrgName === null || "" === arbOrgName) {
    	         lion.util.info("警告", "仲裁机构名称不能为空");
    	         return false;
    	     }
    	}
    }) 

    $("#grpInsurInfoForm #contactMobile").blur(function(){
    	com.orbps.contractEntry.sgGrpInsurAppl.isPhoneOrTel();
    });
    $("#grpInsurInfoForm #contactTel").blur(function(){
    	com.orbps.contractEntry.sgGrpInsurAppl.isPhoneOrTel();
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
                	 
                	 com.orbps.contractEntry.sgGrpInsurAppl.contactIdTypeValue = $(
                             "#grpInsurInfoForm #contactIdType").val();
                     if ("I" !== com.orbps.contractEntry.sgGrpInsurAppl.contactIdTypeValue) {
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
  //投保人、汇缴人字符间只能有一个空格
     companyName=companyName.replace(/^ +| +$/g,'').replace(/ +/g,' ');
 	$("#companyName").val(companyName);
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
         lion.util.info("警告", "邮政编码不能为空");
         return false;
     }
 });
 
 $("#grpInsurInfoForm #contactName").blur(function() {
     var contactName = $("#contactName").val();
     if (contactName === null || "" === contactName) {
         lion.util.info("警告", "联系人姓名不能为空");
         return false;
     }
  // 联系人姓名字符间只能有一个空格
     contactName=contactName.replace(/^ +| +$/g,'').replace(/ +/g,' ');
  	$("#contactName").val(contactName);
 });

 $("#grpInsurInfoForm #contactIdNo").blur(function() {
     var contactIdNo = $("#contactIdNo").val();
     if (contactIdNo === null || "" === contactIdNo) {
         lion.util.info("警告", "联系人证件号码不能为空");
         return false;
     }
 });
 $("#grpInsurInfoForm #home").blur(function() {
     var address = $("#grpInsurInfoForm #home").val();
     if (address === null || "" === address) {
         lion.util.info("警告", "详细地址不能为空");
         return false;
     }
 });
});

