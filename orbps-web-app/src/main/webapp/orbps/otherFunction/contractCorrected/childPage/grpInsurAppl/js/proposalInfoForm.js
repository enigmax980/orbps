com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.proposalInfoForm=$("#proposalInfoForm");
com.orbps.common.allList = new Array();
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos = [];

$(function() {

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

	// 责任信息
	$("#btnSelect").click(function() {
		for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.busiProd.length; i++) {
			var busiPrdCode = com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].busiPrdCode;
			if(busiPrdCode===com.orbps.otherFunction.contractCorrected.childPage.busiProdVo.busiPrdCode){
				com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos=com.orbps.otherFunction.contractCorrected.childPage.busiProd[i].responseVos;
				break;
			}
		}
        // 清空modal
        com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.empty(); 
        // 加载责任modal
        com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.load(
            "/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
                // 初始化插件
            	$(this).modal('toggle');
            	com.orbps.otherFunction.contractCorrected.childPage.reloadProductTable();
            });
    });

	com.orbps.otherFunction.contractCorrected.childPage.reloadProductTable = function () {
		$('#coverageInfo_list').find("tbody").empty();
		if (com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos !== null && com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos.length > 0) {
			for (var i = 0; i < com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos.length; i++) {
				$('#coverageInfo_list').find("tbody")
						.append("<tr><td>"
								+ com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos[i].productCode
								+ "</td><td>"
								+ com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos[i].productName
								+ "</td><td>"
								+ com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos[i].productNum
								+ "</td><td>"
								+ com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos[i].productPremium
								+ "</td><td>"
								+ com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.responseVos[i].subStdPremium
								+ "</td></tr>");
			}
		} else {
			$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
		}
	}
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
});