//基本信息校验规则
com.orbps.contractManage.childPage.examineRulesDetailViewValidateForm = function(vform) {
  var error2 = $('.alert-danger', vform);
  var success2 = $('.alert-success', vform);
  vform.validate({
      errorElement : 'span',
      errorClass : 'help-block help-block-error',
      focusInvalid : false,
      onkeyup : false,
      ignore : '',
      rules : {
//    	  branchNo : {
//              required : true,
//              isBranchNo: true
//          },
          ruleChangeReason :{
        	  required : true,
        	  zh_verify100 : true
          },
          beforeEffectiveDate : {
              required : true,
              isIntGteZero: true
          },
          afterEffectiveDate : {
              required : true,
              isIntGteZero: true
          },
          ruleName :{
        	  required : true,
        	  zh_verify30 :true
          }
      },
      messages : {
//    	  branchNo : {
//              required : '请输入销售机构代码',
//              isBranchNo: '销售机构代码为6位正整数'
//          },
          ruleChangeReason :{
        	  required : '请输入规则变化原因',
        	  zh_verify100 : '请输入2~100个字的中文！'
          },
          beforeEffectiveDate : {
              required : '请输入生效日往前追溯天数',
              isIntGteZero :'请输入大于等于零的整数'
          },
          afterEffectiveDate : {
              required : '请输入生效日往后指定天数',
              isIntGteZero :'请输入大于等于零的整数'
          },
          ruleName :{
        	  required : '请输入规则名称',
        	  zh_verify30 : '请输入2~30个字的中文！'
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
$(function(){


  // 初始化校验信息
com.orbps.contractManage.childPage.examineRulesDetailViewValidateForm(com.orbps.contractManage.parentPage.accOwnAddForm);

$("#accOwnAddForm #mgrBranchNo").attr("readOnly",true);
}); 

//$("#accOwnAddForm #branchNo").blur(function() {
//    var branchNo = $("#branchNo").val();
//    if (branchNo === null || "" === branchNo) {
//        lion.util.info("警告", "销售机构代码不能为空");
//        return false;
//    }
//});
$("#ruleName").blur(function() {
    var ruleName = $("#ruleName").val();
    if (ruleName === null || "" === ruleName) {
        lion.util.info("警告", "规则名称不能为空");
        return false;
    }
    if(ruleName.length > 30){
    	 lion.util.info("警告", "规则名称长度不能大于30个字符");
         return false;

    }
});
$("#ruleChangeReason").blur(function() {
    var ruleChangeReason = $("#ruleChangeReason").val();
    if (ruleChangeReason === null || "" === ruleChangeReason) {
        lion.util.info("警告", "规则变化原因不能为空");
        return false;
    }
    if(ruleChangeReason.length > 100){
    	lion.util.info("警告", "规则变化原因长度小于100个汉字。");
        return false;
    }
});
$("#accOwnAddForm #beforeEffectiveDate").blur(function() {
    var beforeEffectiveDate = $("#beforeEffectiveDate").val();
    if (beforeEffectiveDate === null || "" === beforeEffectiveDate) {
        lion.util.info("警告", "生效日往前追溯天数不能为空");
        return false;
    }
});

$("#afterEffectiveDate").blur(function() {
    var afterEffectiveDate = $("#afterEffectiveDate").val();
    if (afterEffectiveDate === null || "" === afterEffectiveDate) {
        lion.util.info("警告", "生效日往后指定天数不能为空");
        return false;
    }
});