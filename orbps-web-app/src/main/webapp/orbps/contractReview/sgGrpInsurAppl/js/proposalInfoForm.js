com.orbps.common = {};
com.orbps.common.list = new Array();
com.orbps.common.allList = new Array();
com.orbps.contractReview.sgGrpInsurAppl.proposalInfoForm = $("#proposalInfoForm");
//新建contractReview.grpInsurAppl命名空间
com.orbps.contractReview.sgGrpInsurAppl.polList = new Array();
//新建险种集合
com.orbps.contractReview.sgGrpInsurAppl.busiPrdCode = [];

com.orbps.contractReview.sgGrpInsurAppl.selectData;
com.orbps.common.busiPrdCode;

// 基本信息校验规则
com.orbps.contractReview.sgGrpInsurAppl.proposalValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        messages : {
            insuredTotalNum : {
                required : "被保险人总数不能为空",
                isIntGteZero : "请输入>=0的整数"
            },
            sumPrem : {
                required : "保费合计不能为空",
                isFloatGteZero : "保费合计必须>=0"
            },
        },
        rules : {
            insuredTotalNum : {
                required : true,
                isIntGteZero : true
            },
            sumPrem : {
                required : true,
                isFloatGteZero : true
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
	//校验函数初始化
//	com.orbps.contractReview.sgGrpInsurAppl
//	.proposalValidateForm(com.orbps.contractReview.sgGrpInsurAppl.proposalInfoForm);	
	// 增加表格
	$("#btnAdd").click(function() {
	    $("#bsInfoListTb").editDatagrids("addRows");
	});

	// 删除表格
	$("#btnDel").click(function() {
		  com.orbps.contractReview.sgGrpInsurAppl.selectData = $("#bsInfoListTb").editDatagrids(
          "getSelectRows");
	    if ((null === com.orbps.contractReview.sgGrpInsurAppl.selectData) || (com.orbps.contractReview.sgGrpInsurAppl.selectData.length === 0)
                || (com.orbps.contractReview.sgGrpInsurAppl.selectData.length > 1)) {
            lion.util.info("警告", "请选择一个主险信息");
            return false;
        }else{
        	$("#bsInfoListTb").editDatagrids("delRows");
        }
	});

	// 责任信息
	$("#btnSelect").click(function() {
		// 获取选择的险种信息
        com.orbps.contractReview.sgGrpInsurAppl.selectData = $("#bsInfoListTb").editDatagrids("getSelectRows");
        if(null === com.orbps.contractReview.sgGrpInsurAppl.selectData){
        	lion.util.info("警告", "请选择一个险种");
        	return false;
        }
		//循环对比选中的险种，和责任信息中的数据对比
		for (var i = 0; i < com.orbps.contractReview.sgGrpInsurAppl.addinsuranceVos.length; i++) {
			var busiPrdCode = com.orbps.contractReview.sgGrpInsurAppl.addinsuranceVos[i].busiPrdCode;
			if(busiPrdCode===com.orbps.contractReview.sgGrpInsurAppl.selectData.busiPrdCode){
				com.orbps.contractReview.sgGrpInsurAppl.responseVos = com.orbps.contractReview.sgGrpInsurAppl.addinsuranceVos[i].responseVos;
				break;
			}
		}
        // 清空modal
        com.orbps.contractReview.sgGrpInsurAppl.addDialog.empty(); 
        // 加载责任modal
        com.orbps.contractReview.sgGrpInsurAppl.addDialog.load(
            "/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
                // 初始化插件
            	$(this).modal('toggle');
            	com.orbps.contractReview.sgGrpInsurAppl.reloadProductTable();
            });
    });

	com.orbps.contractReview.sgGrpInsurAppl.reloadProductTable = function () {
		$('#coverageInfo_list').find("tbody").empty();
		if (com.orbps.contractReview.sgGrpInsurAppl.responseVos !== null && com.orbps.contractReview.sgGrpInsurAppl.responseVos.length > 0) {
			for (var i = 0; i < com.orbps.contractReview.sgGrpInsurAppl.responseVos.length; i++) {
				$('#coverageInfo_list').find("tbody")
						.append("<tr><td>"
								+ com.orbps.contractReview.sgGrpInsurAppl.responseVos[i].productCode
								+ "</td><td>"
								+ com.orbps.contractReview.sgGrpInsurAppl.responseVos[i].productName
								+ "</td><td>"
								+ com.orbps.contractReview.sgGrpInsurAppl.responseVos[i].productNum
								+ "</td><td>"
								+ com.orbps.contractReview.sgGrpInsurAppl.responseVos[i].productPremium
								+ "</td><td>"
								+ com.orbps.contractReview.sgGrpInsurAppl.responseVos[i].subStdPremium
								+ "</td></tr>");
			}
		} else {
			$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
		}
	}
    // 初始化校验函数
    com.orbps.contractReview.sgGrpInsurAppl
            .proposalValidateForm(com.orbps.contractReview.sgGrpInsurAppl.proposalInfoForm);
    
});

//成功查询险种名称回调函数
com.orbps.contractReview.sgGrpInsurAppl.successSearchBusiprodName = function(data,arg){
	if("fail"==data){
		lion.util.info("","险种不存在，请重新输入险种代码");
		$("#busiPrdName").val("");
		return false;
	}
	$("#busiPrdName").val(data);
	$("#busiPrdName").attr("readOnly",true);
}

//查询责任成功回调函数
com.orbps.contractReview.sgGrpInsurAppl.successbusiPrdCode = function (data,arg){
	$('#coverageInfo_list').editDatagrids("bodyInit", data);
	com.orbps.common.allList = data;
}


//回显保额保费
com.orbps.common.reloadPre = function(productNum,productPremium){
	var getAddRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
	for (var i = 0; i < getAddRowsData.length; i++) {
		var busiPrdCode = getAddRowsData[i].busiPrdCode;
		if(com.orbps.contractReview.sgGrpInsurAppl.selectData.busiPrdCode===busiPrdCode){
			com.orbps.contractReview.sgGrpInsurAppl.selectData.amount = productNum;
			com.orbps.contractReview.sgGrpInsurAppl.selectData.premium = productPremium;
			getAddRowsData.splice(i,1,com.orbps.contractReview.sgGrpInsurAppl.selectData);
			break;
		}
	}
	$('#bsInfoListTb').editDatagrids("bodyInit", getAddRowsData);
}
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

//生效频率改变页面样式，一期不支持频次生效，暂时注释代码----20170112
//$("#frequencyEffectFlag").change(function(){
//	if($("#frequencyEffectFlag").val()==="N"){
//		$("#effectFreq").attr("disabled",true)
//	}else if($("#frequencyEffectFlag").val()==="Y"){
//		$("#effectFreq").attr("disabled",false)
//		var effectFreq = $("#effectFreq").val();
//		if (effectFreq === null || "" === effectFreq) {
//        lion.util.info("警告", "请输入生效频率");
//        return false;
//		}    
//	}else{		
//	}
//});

//$("#proposalInfoForm #effectFreq").blur(
//		function(){
//			var effectFreq = $("#effectFreq").val();
//			var zh_verify = /^[0-9]*[1-9][0-9]*$/;
//			if(!zh_verify.test(effectFreq))
//				lion.util.info("警告", "请输入大于零的整数类型生效频率");
//			return false;
//		});
$("#proposalInfoForm #insuredTotalNum").blur(
        function() {
            var insuredTotalNum = $("#proposalInfoForm #insuredTotalNum").val();
            if (insuredTotalNum === null || "" === insuredTotalNum
                    || "undefined" === insuredTotalNum) {
                lion.util.info("警告", "被保险人总数不能为空");
                return false;
            }
        });

$("#proposalInfoForm #dateType").blur(function() {
    var dateType = $("#dateType").val();
    if (dateType === null || "" === dateType || "undefined" === dateType) {
        lion.util.info("警告", "保险期间不能为空");
        return false;
    }
});




$("#sumPrem").click(function(){
	// 获取要约信息下面的所有险种信息
	var getRowsData = $("#bsInfoListTb").editDatagrids("getRowsData");
	var sum = 0;
	for(var index in getRowsData){
		var rowData = getRowsData[index];
		sum += parseFloat(rowData["premium"]);
	}
	$("#sumPrem").val(sum.toFixed(2));
	//$("#sumPrem").attr("disabled",true);
	

});