com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.proposalInfoForm = $("#proposalInfoForm");

com.orbps.common = {};
com.orbps.common.list = new Array();
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos = [];

//基本信息校验规则
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.proposalValidateForm= function (vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
		errorElement: 'span',
        errorClass: 'help-block help-block-error', 
        focusInvalid: false, 
        onkeyup:false,
        ignore: '', 
        messages : {
        	insuredTotalNum : {
				required : "被保险人总数不能为空",
				isIntGteZero : "请输入>=0的整数"
			},
			sumPrem : {
				required : "保费合计不能为空",
				isFloatGteZero : "保费合计必须>=0"
			}
		},
		rules : {
			insuredTotalNum : {
				required : true,
				isIntGteZero : true
			},
			sumPrem : {
				required : true,
				isFloatGteZero : true
			}
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
	com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.proposalValidateForm(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.proposalInfoForm);

	
//日期初始化插件
$(".date-picker").datepicker({
	autoclose : true,
	language : 'zh-CN'
});	
// combo组件初始化
$("*").comboInitLoad();

// 文件上传下载插件初始化
$("#file").fileinput({
    'allowedFileExtensions' : ['jpg', 'png','gif','xlsx'],
    'showUpload':true,
    'showPreview':false,
    'showCaption':true,
    'browseClass':'btn btn-success',
});

//指定生效日改变页面样式
$("#effectType").change(function(){
	if($("#effectType").val()==="0"){
		$("#speEffectDate").attr("disabled",true);
		$("#speEffectDatebtn").attr("disabled",true);
		$("#speEffectDate").val("");
	}else if($("#effectType").val()==="1"){
		$("#speEffectDate").attr("disabled",false);
		$("#speEffectDatebtn").attr("disabled",false);
	}
});
//当指定生效日期的时候提示用户选择生效日
$("#speEffectDate").blur(function(){
	if($("#effectType").val()==="1"){
		lion.util.info("警告", "请选择生效日");
	}
});

com.orbps.otherFunction.contractCorrected.childPage.queryProductRdio = function () {
	var radioVal;
	var temp = document.getElementsByName("insuredRad");
	for(var i=0;i<temp.length;i++){
	     if(temp[i].checked){
	    	 radioVal = temp[i].value;
	     }
	}
	com.orbps.otherFunction.contractCorrected.childPage.busiProdVo = com.orbps.otherFunction.contractCorrected.childPage.busiProd[radioVal];
}
//责任信息
$("#btnSelect").click(function() {
 // 清空modal
 com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.empty(); 
 for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.busiProd.length; i++) {
		var busiPrdCode = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].busiPrdCode;
		//alert("选择的busiPrdCode"+busiPrdCode);
		if(busiPrdCode===com.orbps.otherFunction.contractCorrected.childPage.busiProdVo.busiPrdCode){
			//alert("xunahuan");
			com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos=com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].responseVos;
			break;
		}
	}
 // 加载责任modal
 com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.load(
     "/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
         // 初始化插件
     	$(this).modal('toggle');
     	//alert(JSON.stringify(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos));
     	com.orbps.otherFunction.contractCorrected.childPage.reloadProductTable();
     });
});

com.orbps.otherFunction.contractCorrected.childPage.reloadProductTable = function () {
	$('#coverageInfo_list').find("tbody").empty();
	if (com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos !== null && com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos.length; i++) {
			$('#coverageInfo_list').find("tbody")
					.append("<tr><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos[i].productCode
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos[i].productName
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos[i].productNum
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos[i].productPremium
							+ "</td><td>"
							+ com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.responseVos[i].subStdPremium
							+ "</td></tr>");
		}
	} else {
		$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
	}
}

});	

