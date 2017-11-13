//com.orbps.contractManage={};
//com.orbps.contractManage.childPage={};
//com.orbps.contractManage.parentPage={};
com.orbps.contractManage.parentPage.ruleAddInfoForm=$("#ruleAddInfoForm");
$(function() {	
//	 combo组件初始化
	$("*").comboInitLoad();
	
	setTimeout(function(){
    	//是否人工审批下拉框默认显示否
    	$("#artificialApproveFlag").combo("val","N");
    },1000);
});
// 日期初始化插件
$(".date-picker").datepicker({
	autoclose : true,
	language : 'zh-CN'
});
//基本信息校验规则
com.orbps.contractManage.childPage.examineRulesAddValidateForm = function(vform) {
  var error2 = $('.alert-danger', vform);
  var success2 = $('.alert-success', vform);
  vform.validate({
      errorElement : 'span',
      errorClass : 'help-block help-block-error',
      focusInvalid : false,
      onkeyup : false,
      ignore : '',
      rules : {
    	  branchCode : {
              required : true,
              isBranchNo: true
          },
          beforeEffectiveDate : {
              isIntGteZero: true
          },
          afterEffectiveDate : {
              isIntGteZero: true
          }
      },
      messages : {
    	  branchCode : {
              required : '请输入销售机构号',
              isBranchNo: '机构号为6位正整数'
          },
          beforeEffectiveDate : {
              isIntGteZero :'请输入大于等于零的天数'
          },
          afterEffectiveDate : {
              isIntGteZero :'请输入大于等于零的天数'
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

  // 初始化校验信息
com.orbps.contractManage.childPage.examineRulesAddValidateForm(com.orbps.contractManage.parentPage.ruleAddInfoForm);

  $("#branchCode").blur(function() {
      var branchCode = $("#branchCode").val();
      if (branchCode === null || "" === branchCode) {
          lion.util.info("警告", "销售机构号不能为空");
          return false;
      }
  });

  

