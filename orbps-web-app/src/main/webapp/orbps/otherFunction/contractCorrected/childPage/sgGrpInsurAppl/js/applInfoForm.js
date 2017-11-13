// 新建命名空间
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl={};
//编辑或添加对话框
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog = $('#btnModel');
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.applInfoForm = $("#applInfoForm");


$(function() {
//	$("#citySelect").citySelect({
//        url:"/resources/global/js/cityselect/js/city.min.json",
//        prov:"北京",
//        city:"北京",
//        town:"朝阳区",
//        /* nodata:"none", */
//	    required:true
//    });
	//投保单号置灰
	$("#applInfoForm #applNo").attr("readonly",true);
	document.getElementById('personalInsurInfoForm').style.display='none';
	
	$("#listType").change(function() {
		var listType = $("#listType").val();
		if(listType=="G"){
			document.getElementById('personalInsurInfoForm').style.display='none';
			document.getElementById('grpInsurInfoForm').style.display='';
		}else{
			document.getElementById('grpInsurInfoForm').style.display='none';
			document.getElementById('personalInsurInfoForm').style.display='';
		}
	});
	// 显示回退按钮
	if (com.orbps.otherFunction.contractCorrected.parentPage.backFlag === "Y") {
		$("#submit #btnSubmitDiv").hide();
		$("#submit #hideKong").hide();
	    $("#submit #btnBackDiv").show();
	    $("#submit #selectDiv").show();
	    $("#submit #backReasonDiv").show();
	} else {
	    $("#submit #selectDiv").hide();
	    $("#submit #btnBackDiv").hide();
	    $("#submit #backReasonDiv").hide();
	    $("#submit #btnSubmitDiv").show();
	    $("#submit #hideKong").show();
	}
});

	$("#btnQuerySales").click(function() {
		com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.empty();
		com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.load(
			"/orbps/orbps/otherFunction/contractCorrected/childPage/sgGrpInsurAppl/html/salesChannelModal.html",
			function() {
				$(this).modal('toggle');
				$(this).comboInitLoad();
				com.orbps.common.saleChannelTableReload(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.grpSalesListFormVos);
		});
	});