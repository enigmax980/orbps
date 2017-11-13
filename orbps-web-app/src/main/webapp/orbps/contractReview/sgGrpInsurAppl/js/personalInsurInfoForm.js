com.orbps.contractReview.sgGrpInsurAppl.personalInsurInfoForm = $("#personalInsurInfoForm");
//基本信息校验规则
com.orbps.contractReview.sgGrpInsurAppl.personBaseValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
            joinName : {
                required : true
                
            },
//                joinIdType : {
//                    required : true
//                },
            joinIdNo : {
            	required : true,
                isIdCardNo : true,
                isIdCard18Age:true
            },
            joinSex : {
                required : true
            },
//                joinBirthDate : {
//                    required : true
//                },
            
            joinMobile : {
                isMobile : true
            },
            joinEmail : {
                email : true
            },
            joinTel : {
                isTel : true
            },
            joinFaxNo : {
                isFax : true
            },
            postCode : {
                required : true,
                isZipCode:true
            },
            joinHome : {
                required : true,
            }
        },
        messages : {
        	 joinName : {
                 required : "汇交人姓名不能为空",
             },
//             joinIdType : {
//                 required : "汇交人证件类型不能为空"
//             },
             joinIdNo : {
                 required : "汇交人证件号码不能为空",
                 isIdCardNo : "请您核对并输入汇交人正确的证件号码",
                 isIdCard18Age :"年龄不能小于18周岁"
             },         
             joinSex : {
                 required : "性别不能为空"
             },
//             joinBirthDate : {
//                 required : "出生日期不能为空"
//             },
             joinMobile : {
                 isMobile : "请输入正确的电话号码"
             },
             joinEmail : {
                 email : "请输入正确格式的邮箱"
             },
             joinTel : {
                 isTel : "请输入正确的固定电话"
             },
             joinFaxNo : {
                 isFax : "请输入正确的传真号码"
             },
             postCode : {
                 required : "邮政编码不能为空",
                 isZipCode : "请输入正确的邮政编码"
             },
             joinHome : {
                 required : "请输入汇交人详细地址"
             }
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
            .personBaseValidateForm(com.orbps.contractReview.sgGrpInsurAppl.personalInsurInfoForm);
    
 // 汇交人身份证号验证
    jQuery.validator.addMethod("isIdCardNo", function(value, element) {
        var appIdTypeValue = $("#personalInsurInfoForm #joinIdType").val();
        // 先判断证件类型是否为身份证再去校验
        if (appIdTypeValue === "I") {
            return this.optional(element) || idCardNoUtil.checkIdCardNo(value);
        } else {
            return true;
        }
    }, "请输入大于18岁的身份证号码！");
 // 大于18岁的身份证号码验证
    jQuery.validator.addMethod("isIdCard18Age", function(value, element) {
    	 var appIdTypeValue = $("#personalInsurInfoForm #joinIdType").val();
        // 先判断证件类型是否为身份证再去校验
        if (appIdTypeValue === "I") {
            return check18Age($("#personalInsurInfoForm #joinIdType"),
					  $("#personalInsurInfoForm #joinIdNo"),
					  $("#personalInsurInfoForm #joinBirthDate"),
					  $("#personalInsurInfoForm #joinSex"));
        } else {
            return true;
        }
    }, "请正确输入您的身份证号码！");
    
  //如果联系人证件类型是身份证，证件号码是18位，则在述标失去焦点时会自动带出客户生日及性别、年龄等
  $("#joinIdNo").blur(
		  function() {
			  idcard($("#personalInsurInfoForm #joinIdType"),
					  $("#personalInsurInfoForm #joinIdNo"),
					  $("#personalInsurInfoForm #joinBirthDate"),
					  $("#personalInsurInfoForm #joinSex"));
  });
  
  $("#joinName").blur(function() {
      var joinName = $("#joinName").val();
      if (joinName === null || "" === joinName) {
          lion.util.info("警告", "汇交人姓名不能为空");
          return false;
      }
  });

  $("#joinIdNo").blur(function() {
      var joinIdNo = $("#joinIdNo").val();
      if (joinIdNo === null || "" === joinIdNo) {
          lion.util.info("警告", "汇交人证件号码不能为空");
          return false;
      }
  });

  $("#joinMobile").blur(function() {
      var joinMobile = $("#joinMobile").val();
      if (joinMobile === null || "" === joinMobile) {
          lion.util.info("警告", "汇交人移动电话不能为空");
          return false;
      }
  });
  $("#postCode").blur(function() {
      var postCode = $("#postCode").val();
      if (postCode === null || "" === postCode) {
          lion.util.info("警告", "汇交人邮编不能为空");
          return false;
      }
  });
  $("#personalInsurInfoForm #home").blur(function() {
      var home = $("#home").val();
      if (home === null || "" === home) {
          lion.util.info("警告", "汇交人详细地址不能为空");
          return false;
      }
  });
  //根据争议处理方式调整页面样式
  $("#personalInsurInfoForm #pSettleDispute").change(function() {
      if($("#pSettleDispute").val()==="1"){
          $("#personalInsurInfoForm #joinParbOrgName").attr("disabled",true);
          $("#personalInsurInfoForm #joinParbOrgName").val("");
      }else{
          $("#personalInsurInfoForm #joinParbOrgName").attr("disabled",false);
      }
  });
	//仲裁机构名称字符简直能有一个空格
	$("#personalInsurInfoForm #joinParbOrgName").blur(function() {
		var joinParbOrgName = $("#personalInsurInfoForm #joinParbOrgName").val();
		if (joinParbOrgName === null || "" === joinParbOrgName) {
			lion.util.info("警告", "仲裁机构名称不能为空");
			return false;
		}
		// 字符间只能有一个空格
		joinParbOrgName = joinParbOrgName.replace(/^ +| +$/g, '').replace(/ +/g, ' ');
		$("#personalInsurInfoForm #joinParbOrgName").val(joinParbOrgName);
	});
});
//当联系人证件类型改变的 时候，将五要素只读变成可写
$("#personalInsurInfoForm #joinIdType").change(function() {
	  if(com.orbps.contractReview.sgGrpInsurAppl.acount === 1 || com.orbps.contractReview.sgGrpInsurAppl.acount ===2){
		  com.orbps.contractReview.sgGrpInsurAppl.acount++;
		  return false;
	  }
	  var joinIdType = $("#personalInsurInfoForm #joinIdType").val();
	  //当页面回显的时候也有一个change事件，这个时候证件类型还没有值，所以到下面会有问题
	  $("#personalInsurInfoForm #joinIdNofa").removeClass("fa-warning");
      $("#personalInsurInfoForm #joinIdNofa").removeClass("fa-check");
      $("#personalInsurInfoForm #joinIdNofa").removeClass("has-success");
      $("#personalInsurInfoForm #joinIdNofa").removeClass("has-error");
	  	if("" !== joinIdType){
	  		if ("I" === joinIdType) {
	  			$("#personalInsurInfoForm #joinBirthDate").val("");
				$("#personalInsurInfoForm #joinBirthDate").attr("disabled",true);
				$("#personalInsurInfoForm #joinBirthDateBtn").attr("disabled",true);
				$("#personalInsurInfoForm #joinSex").attr("readonly",true);
				$("#personalInsurInfoForm #joinSex").combo("clear");
				$("#personalInsurInfoForm #joinIdNo").val("");
			}else{
				 $("#personalInsurInfoForm #joinBirthDate").attr("disabled",false);
				 $("#personalInsurInfoForm #joinBirthDateBtn").attr("disabled",false);
				 $("#personalInsurInfoForm #joinSex").attr("readonly",false);
				 $("#personalInsurInfoForm #joinIdNo").val("");
			}
	  	}
});
