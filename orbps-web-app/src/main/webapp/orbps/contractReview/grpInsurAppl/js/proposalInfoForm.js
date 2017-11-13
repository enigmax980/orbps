com.orbps.contractReview.grpInsurAppl.proposalInfoForm = $("#proposalInfoForm");
com.orbps.common.allList = new Array();
com.orbps.common.list = new Array();
com.orbps.contractReview.grpInsurAppl.selectData;

com.orbps.contractReview.grpInsurAppl.busiPrdVos = [];
// 新建险种集合
com.orbps.contractReview.grpInsurAppl.busiPrdCode = [];
com.orbps.common.busiPrdCode;
com.orbps.common.selectBusiPrdCode;
com.orbps.common.insurDurUnit;
//责任信息
com.orbps.contractReview.grpInsurAppl.responseVos = [];
// 基本信息校验规则
com.orbps.contractReview.grpInsurAppl.proposalValidateForm = function(vform) {
	var error2 = $('.alert-danger', vform);
	var success2 = $('.alert-success', vform);
	vform.validate({
		errorElement : 'span',
		errorClass : 'help-block help-block-error',
		focusInvalid : false,
		onkeyup : false,
		ignore : '',
		messages : {
			ipsnNum : {
				required : "被保险人总数不能为空"
			},
			insurDur : {
				required : "保险期间不能为空"
			},
			sumPrem : {
				required : "保费合计不能为空",
				isFloatGteZero : "保费合计必须>=0"
			},
			forceNum : {
				required : "生效频率不能为空",
				isIntGteZero : "请输入>=0的整数"
			}
		},
		rules : {
			ipsnNum : {
				required : true
			},
			insurDur : {
				required : true
			},
			sumPrem : {
				required : true,
				isFloatGteZero : true
			},
			forceNum : {
				required : true,
				isIntGteZero : true
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

    // 初始化校验函数
    com.orbps.contractReview.grpInsurAppl
            .proposalValidateForm(com.orbps.contractReview.grpInsurAppl.proposalInfoForm);
    
    //根据是否指定生效日修改页面样式
	$("#forceType").change(function() {
		var forceType = $("#forceType").val();
		if (forceType === "0") {
			$("#inForceDateIs").hide();
			$("#inForceDate").val("");
			$("#inForceDate").attr("disabled", true);
			$("#inForceDateBtn").attr("disabled", true);
		} else if (forceType === "1") {
			$("#inForceDateIs").show();
			$("#inForceDate").attr("disabled", false);
			$("#inForceDateBtn").attr("disabled", false);
		} else {
			$("#inForceDateIs").hide();
		}
	});
    
	//根据是否频次生效修改页面样式
	$("#frequenceEff").change(function() {
		if ($("#frequenceEff").val() === "N") {
			$("#forceNum").val("");
			$(".fa").removeClass("fa-warning");
			$(".fa").removeClass("fa-check");
			$(".fa").removeClass("has-success");
			$(".fa").removeClass("has-error");
			$("#forceNum").attr("disabled", true);
		} else if ($("#frequenceEff").val() === "Y") {
			$("#forceNum").attr("disabled", false);
		}
	});


    $("#ipsnNum").blur(function() {
        var ipsnNum = $("#ipsnNum").val();
        if (ipsnNum === null || "" === ipsnNum || "undefined" === ipsnNum) {
            lion.util.info("警告", "被保险人总数为空");
            return false;
        }
    });

    $("#sumPrem").blur(function() {
        var sumPrem = $("#sumPrem").val();
        if (sumPrem === null || "" === sumPrem || "undefined" === sumPrem) {
            lion.util.info("警告", "保费合计不能为空");
            return false;
        }
    });

    $("#forceNum").blur(function() {
        var forceNum = $("#forceNum").val();
        if (forceNum === null || "" === forceNum) {
            lion.util.info("警告", "生效频率不能为空");
            return false;
        }
    });

});

// 丢失焦点事件(根据险种代码查询险种名称)
$('body').delegate('table#fbp-editDataGrid #busiPrdCode' , 'blur', function(){
	var a = $("#busiPrdCode").val();
	if(a!==""){
		var responseVo = {};
		responseVo.busiPrdCode = a;
		// 向后台发送请求
		lion.util.postjson(
				'/orbps/web/orbps/contractEntry/search/searchBusiName',
				responseVo,
				com.orbps.contractReview.grpInsurAppl.successSearchBusiprodName,
				null,
				null);	
	}else{
		$("#busiPrdCodeName").val("");
		return false;
	}
});
	// 责任信息
	$("#btnSelect").click(function() {
		// 获取选择的险种信息
        com.orbps.contractReview.grpInsurAppl.selectData = $("#fbp-editDataGrid").editDatagrids("getSelectRows");
        if(null === com.orbps.contractReview.grpInsurAppl.selectData){
        	lion.util.info("警告", "请选择一个险种");
        	return false;
        }
		//循环对比选中的险种，和责任信息中的数据对比
		for (var i = 0; i < com.orbps.contractReview.grpInsurAppl.busiPrdVos.length; i++) {
			var busiPrdCode = com.orbps.contractReview.grpInsurAppl.busiPrdVos[i].busiPrdCode;
			if(busiPrdCode===com.orbps.contractReview.grpInsurAppl.selectData.busiPrdCode){
				com.orbps.contractReview.grpInsurAppl.responseVos = com.orbps.contractReview.grpInsurAppl.busiPrdVos[i].responseVos;
				break;
			}
		}
        // 清空modal
        com.orbps.contractReview.grpInsurAppl.addDialog.empty(); 
        // 加载责任modal
        com.orbps.contractReview.grpInsurAppl.addDialog.load(
            "/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
                // 初始化插件
            	$(this).modal('toggle');
            	com.orbps.contractReview.grpInsurAppl.reloadProductTable();
            });
    });

	com.orbps.contractReview.grpInsurAppl.reloadProductTable = function () {
		$('#coverageInfo_list').find("tbody").empty();
		if (com.orbps.contractReview.grpInsurAppl.responseVos !== null && com.orbps.contractReview.grpInsurAppl.responseVos.length > 0) {
			for (var i = 0; i < com.orbps.contractReview.grpInsurAppl.responseVos.length; i++) {
				$('#coverageInfo_list').find("tbody")
						.append("<tr><td>"
								+ com.orbps.contractReview.grpInsurAppl.responseVos[i].productCode
								+ "</td><td>"
								+ com.orbps.contractReview.grpInsurAppl.responseVos[i].productName
								+ "</td><td>"
								+ com.orbps.contractReview.grpInsurAppl.responseVos[i].productNum
								+ "</td><td>"
								+ com.orbps.contractReview.grpInsurAppl.responseVos[i].productPremium
								+ "</td><td>"
								+ com.orbps.contractReview.grpInsurAppl.responseVos[i].subStdPremium
								+ "</td></tr>");
			}
		} else {
			$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
		}
	}

	// 被保险人总数和投保人数保持一致
	$("#applNum").blur(function() {
		var applNum = $("#applNum").val();
		$("#ipsnNum").val(applNum);
	});

	/***
	 * 查询tab中的基金险，健康险，建工险的隐藏或者展示
	 */
	com.orbps.contractReview.grpInsurAppl.checkTabshowOrHidden = function (){
		//各个tab的是否隐藏，是隐藏 true 不是隐藏 false
		var tab1 = $('#myTab p[href="#tab_15_1"]').is(':hidden');
		var tab2 = $('#myTab p[href="#tab_15_2"]').is(':hidden');
		var tab3 = $('#myTab p[href="#tab_15_3"]').is(':hidden');
		//alert(tab1+"   " +tab2 +"    " +tab3);
		//如果三个都是隐藏的，隐藏div
		if(tab1 && tab2 && tab3){
			$("#specialInsurAddInfoForm").hide();
		}
		//如果tab3不是隐藏的，各个情况都展示tab3
		if((tab1 && tab2 && !tab3) || (tab1 && !tab2 && !tab3) || (!tab1 && !tab2 && !tab3) || (!tab1 && tab2 && !tab3)){
			$('#myTab p[href="#tab_15_3"]').tab('show');
		}
		//如果tab2不是隐藏的，各种情况都展示tab2
		if((tab1 && !tab2 && tab3) || (!tab1 && !tab2 && tab3)){
			$('#myTab p[href="#tab_15_2"]').tab('show');
		}
		//如果tab2 tab3 都是隐藏的，tabl不是隐藏的展示tab1
		if(!tab1 && tab2 && tab3){
			$('#myTab p[href="#tab_15_1"]').tab('show');
		}
	}
