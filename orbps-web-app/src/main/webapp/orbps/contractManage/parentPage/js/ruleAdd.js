com.orbps.contractManage = {};
com.orbps.contractManage.parentPage = {};
com.orbps.contractManage.childPage={};
com.orbps.contractManage.parentPage.ruleAddForm = $("#ruleAddForm");
com.orbps.contractManage.parentPage.ruleAddInfoForm = $("#ruleAddInfoForm");
com.orbps.contractManage.parentPage.ruleType;
com.orbps.contractManage.parentPage.addDialog=$('#btnModel');


$(function() {
	// combo组件初始化
	$("*").comboInitLoad();
	// datagrid组件初始化
	$("*").datagridsInitLoad();
	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	$("#ruleSubmit").hide();
	// 清空表单录入
	$("#ruleSubmitForm #btnClear").click(function() {
		$("#ruleAddForm").reset();
		$("#ruleAddInfo").hide();
		$("#ruleSubmit").hide();
		 $(".fa").removeClass("fa-warning");
	     $(".fa").removeClass("fa-check");
	     $(".fa").removeClass("has-success");
	     $(".fa").removeClass("has-error");
	});
	
});


//基本信息校验规则
com.orbps.contractManage.parentPage.ruleAddValidateForm = function(vform) {
  var error2 = $('.alert-danger', vform);
  var success2 = $('.alert-success', vform);
  vform.validate({
      errorElement : 'span',
      errorClass : 'help-block help-block-error',
      focusInvalid : false,
      onkeyup : false,
      ignore : '',
      rules : {
    	  branchNo : {
              required : true,
              isBranchNo: true
          },
          ruleName : {
              required : true,
              zhAndNum_verify30 :true
          },
          ruleChangeReason : {
              required : true,
              zhAndNum_verify100 : true
          }
      },
      messages : {
    	  branchNo : {
              required : '请输入机构号',
              isBranchNo: '机构号为6位正整数'
          },
          ruleName : {
              required : '请输入规则名称',
             zhAndNum_verify30 : '请输入2~30个字的中文或者英文！'
          },
          ruleChangeReason : {
              required : '请输入规则变化原因',
              zhAndNum_verify100 : '请输入2~100个字的中文或者英文！'
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

//初始化校验信息
com.orbps.contractManage.parentPage.ruleAddValidateForm(com.orbps.contractManage.parentPage.ruleAddForm);

  $("#branchNo").blur(function() {
      var branchNo = $("#branchNo").val();
      if (branchNo === null || "" === branchNo) {
          lion.util.info("警告", "机构号不能为空");
          return false;
      }
  });

  $("#ruleName").blur(function() {
      var ruleName = $("#ruleName").val();
      if (ruleName === null || "" === ruleName) {
          lion.util.info("警告", "规则名称不能为空");
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





// 选择规则类型
$("#ruleAddForm #ruleType").change(function() {
	// 获取规则类型
	com.orbps.contractManage.parentPage.ruleType = $("#ruleAddForm #ruleType").val();
	// 显示相应规则页面
	$("#ruleAddInfo").show();
	$("#ruleSubmit").show();
	switch (com.orbps.contractManage.parentPage.ruleType){
	case "0":
		$("#ruleSubmit").hide();
		$("#ruleAddInfo").hide();
		break;
	case "1":
		$("#ruleAddInfoForm").load("/orbps/orbps/contractManage/childPage/examineConfig/html/examineRulesAddView.html");
		break;
	case 2:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/examineConfig/html/examineRulesAddView.html");
		break;
	case 3:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/grpEntryConfig/html/grpEntryRulesAddView.html");
		break;
	case 4:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/identifyConfig/html/identifyRulesAddView.html");
		break;
	case 5:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/noticeConfig/html/noticeRulesAddView.html");
		break;
	case 6:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/prePrintConfig/html/prePrintRulesAddView.html");
		break;
	case 7:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/revokeConfig/html/revokeRulesAddView.html");
		break;
	case 8:
		lion.util.load($("#ruleAddInfo"),"orbps/contractManage/childPage/sendCheckConfig/html/sendCheckRulesAddView.html");
		break;
	default:
		break;
	}
	// 显示按钮组
	$("#ruleSubmitForm").show();
	
});	

// 提交
$("#ruleSubmitForm #btnSubmit").click(function() {
	if("" === $("#ruleAddForm #ruleChangeReason").val()){
		lion.util.info("提示","规则变化原因不能为空");
	}
	if (com.orbps.contractManage.parentPage.ruleAddForm
            .validate().form()
            && com.orbps.contractManage.parentPage.ruleAddInfoForm
                    .validate().form()
            && com.orbps.contractManage.parentPage.validateSelectVal()
            ){
		var contractManageVo = {};
		contractManageVo=  $("#ruleAddForm").serializeObject();
	    var type = $("#ruleType").val();
		if(type==="0"){
			contractManageVo.accountOwnerVo = com.orbps.contractManage.parentPage.accOwnAddForm.serializeObject();
		}else if(type==="1"){
			com.orbps.contractManage.parentPage.ruleAddForm = $("#ruleAddForm");
			com.orbps.contractManage.parentPage.ruleAddInfoForm
			contractManageVo.examineRuleVo = com.orbps.contractManage.parentPage.ruleAddInfoForm.serializeObject();
			var validDate = contractManageVo.examineRuleVo.validDate;
			var invalidDate = contractManageVo.examineRuleVo.invalidDate;
			var validDate = new Date(validDate);
			var invalidDate = new Date(invalidDate);
		    if(validDate.getTime() > invalidDate.getTime()){
		    	lion.util.info("警告","失效日期大于生效日期，请选择！");
		    	return false;
		    }
			contractManageVo.ruleAddVo = com.orbps.contractManage.parentPage.ruleAddForm.serializeObject();       
		}else {
		}
		lion.util.postjson('/orbps/web/orbps/contractManage/rule/submit',contractManageVo,com.orbps.contractManage.parentPage.successSubmit,null,null);
	    return false;
	}
	
});
//计算天数差的函数 
function  DateDiff(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
	var Date1 = new Date(sDate1);
	var Date2 = new Date(sDate2);
    if(Date1.getTime() > Date2.getTime()){
    	return false;
    } else {
    	var  aDate,  oDate1,  oDate2,  iDays;  
    	aDate  =  sDate1.split("-");
    	oDate1  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]) ;   //转换为12-18-2006格式  
    	aDate  =  sDate2.split("-") ; 
    	oDate2  =  new  Date(aDate[1]  +  '-'  +  aDate[2]  +  '-'  +  aDate[0]) ; 
    	iDays  =  parseInt(Math.abs(oDate1  -  oDate2)  /  1000  /  60  /  60  /24) ;   //把相差的毫秒数转换为天数  
    	$("#totalDays").val(iDays);
    	$("#totalDays").attr("readOnly",true);
    }
}    
//添加成功回调函数
com.orbps.contractManage.parentPage.successSubmit=function (data,arg){
	 if (data.retCode==="1"){
	        lion.util.info("提示", "添加成功");
	    }else{
	        lion.util.info("提示", "添加失败，失败原因："+data.errMsg);
	    }
	// 成功后刷新页面
//	setTimeout(function(){
//		window.location.reload();
//	},500);
}
//校验选择信息
com.orbps.contractManage.parentPage.validateSelectVal=function () {
    var ruleType = $("#ruleAddForm #ruleType").val();
    if (ruleType === null || "" === ruleType) {
        lion.util.info("警告", "请选择规则类型");
        return false;
    }
    var cntrForm = $("#ruleAddInfoForm #cntrForm").val();
    if (cntrForm === null || "" === cntrForm) {
        lion.util.info("警告", "请选择契约形式");
        return false;
    }
    var salesChannel = $("#ruleAddInfoForm #salesChannel").val();
    if (salesChannel === null || "" === salesChannel) {
        lion.util.info("警告", "请选择销售渠道");
        return false;
    }
    //产品类型页面暂时不展示
//    var productType = $("#ruleAddInfoForm #productType").val();
//    if (productType === null || "" === productType) {
//        lion.util.info("警告", "请选择产品类型");
//        return false;
//    }
    var artificialApproveFlag = $("#ruleAddInfoForm #artificialApproveFlag").val();
    if (artificialApproveFlag === null || "" === artificialApproveFlag) {
        lion.util.info("警告", "请选择是否人工审批");
        return false;
    }
    return true;
};

//控制规则名称<=30个汉字
$("#ruleAddForm #ruleName")
.on(
		'keyup',
		function(event) {
			// 响应鼠标事件，允许左右方向键移动
			event = window.event || event;
			if (event.keyCode === 37 | event.keyCode === 39) {
				return;
			}

			var ruleName = $("#ruleAddForm #ruleName").val();
			var len = ruleName.length;
			var reLen = 0;
			for (var i = 0; i < len; i++) {
				if (ruleName.charCodeAt(i) < 27
						|| ruleName.charCodeAt(i) > 126) {
					// 全角
					reLen += 2;
				} else {
					reLen++;
				}
			}
			if (reLen > 60) {
				ruleName = ruleName.substring(0, ruleName.length - 1);
				$("#ruleAddForm #ruleName").val(ruleName);
			}
		});

//控制规则变化原因<=100个汉字
$("#ruleAddForm #ruleChangeReason")
.on(
		'keyup',
		function(event) {
			// 响应鼠标事件，允许左右方向键移动
			event = window.event || event;
			if (event.keyCode === 37 | event.keyCode === 39) {
				return;
			}

			var ruleChangeReason = $("#ruleAddForm #ruleChangeReason").val();
			var len = ruleChangeReason.length;
			var reLen = 0;
			for (var i = 0; i < len; i++) {
				if (ruleChangeReason.charCodeAt(i) < 27
						|| ruleChangeReason.charCodeAt(i) > 126) {
					// 全角
					reLen += 2;
				} else {
					reLen++;
				}
			}
			if (reLen > 200) {
				ruleChangeReason = ruleChangeReason.substring(0, ruleChangeReason.length - 1);
				$("#ruleAddForm #ruleChangeReason").val(ruleChangeReason);
			}
		});

