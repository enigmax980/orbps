com.orbps.common = {};
com.orbps.otherFunction = {};
com.orbps.otherFunction.manualApproval = {};
com.orbps.otherFunction.manualApproval.childPage = {};
com.orbps.otherFunction.manualApproval.childPage.applApproval = {};
//新建contractEntry.sgGrpInsurAppl命名空间
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl = {};
// 编辑或添加对话框
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog = $('#btnModel');
//新建全局命名变量TaskID
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.taskId;
//新建险种责任List
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos = [];
//新建险种List
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd = [];
//新建险种Vo 
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addinsuranceVo;
//新建被保人分组
com.orbps.common.proposalGroupList = new Array();
//新建收付费分组
com.common.chargePayList = new Array();
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.listType;
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.grpSalesListFormVos = [];

//查询成功回调函数
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.successQueryDetail = function(data,args){
	// 将省市县置为可以回显的状态
    if("G"===data.listType){
  	  $("#listType").combo("val", "G")
  	  document.getElementById('personalInsurInfoForm').style.display='none';
  	  document.getElementById('grpInsurInfoForm').style.display='';
  	  setTimeout( function(){
            // 将省市县置为可以回显的状态
//      	  	$(".dist").attr("disabled",false);
//            $(".city").attr("disabled",false);
//            $(".prov").attr("disabled",false);
            $("#grpInsurInfoForm #provinceCode").combo("val",data.sgGrpInsurInfoVo.provinceCode);
            $("#grpInsurInfoForm #cityCode").combo("val",data.sgGrpInsurInfoVo.cityCode);
            $("#grpInsurInfoForm #countyCode").combo("val",data.sgGrpInsurInfoVo.countyCode);
        },1000);
    }else{
  	  $("#listType").combo("val", "P")
  	  document.getElementById('grpInsurInfoForm').style.display='none';
  	  document.getElementById('personalInsurInfoForm').style.display='';
  	  setTimeout( function(){
            // 将省市县置为可以回显的状态
//      	  	$(".dist").attr("disabled",false);
//            $(".city").attr("disabled",false);
//            $(".prov").attr("disabled",false);
            $("#personalInsurInfoForm #province").combo("val",data.sgGrpPersonalInsurInfoVo.province);
            $("#personalInsurInfoForm #city").combo("val",data.sgGrpPersonalInsurInfoVo.city);
            $("#personalInsurInfoForm #county").combo("val",data.sgGrpPersonalInsurInfoVo.county);
        },1000);
    }
    com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.grpSalesListFormVos = data.sgGrpApplInfoVo.grpSalesListFormVos;
      com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.taskId = data.taskInfo.taskId;
//	  data={"listType":"G","addinsuranceVos":[{"amount":10000,"busiPrdCode":"616","busiPrdCodeName":"国寿康优全球团体医疗保险（B型）","insurDur":1,"insurDurUnit":"Y","insuredNum":2,"premium":100,"responseVos":[{"busiPrdCode":"616","productCode":"616-1","productName":"计划一","productNo":"616-1","productNum":10000,"productPremium":100,"subStdPremium":100}]},{"amount":20000,"busiPrdCode":"629","busiPrdCodeName":"国寿附加绿洲住院费用补偿团体医疗保险","insurDur":1,"insurDurUnit":"Y","insuredNum":2,"premium":200,"responseVos":[]}],"chargePayGroupModalVos":[],"insuredGroupModalVos":[],"organizaHierarModalVos":[],"responseVos":[],"sgGrpApplInfoVo":{"agreementNo":"","applDate":1479312000000,"applNo":"6789043212789091","previousPolNo":"1234567890987654321234","quotaEaNo":"12345678","saleCode":"8006","saleName":"陈新","salesBranchNo":"120221","salesChannel":"BS"},"sgGrpInsurInfoVo":{"cityCode":"临沂市","companyName":"单位团体名称一","companyRegist":"CHN","contactEmail":"1254326@qq.com","contactIdNo":"371326199209281241","contactIdType":"I","contactMobile":"15910610192","contactName":"张某某","contactTel":"15910610192","countyCode":"平邑县","departmentType":"2","faxNo":"0531-2921678","gInsuredTotalNum":100,"gSettleDispute":"1","home":"门牌号264号","idNo":"123456789","idType":"B","occClassCode":"05","provinceCode":"山东省","townCode":"地方镇","villageCode":"八一村","zipCode":"273300"},"sgGrpPayInfoVo":{"bankCode":"1103","chargeDeadline":1480435200000,"moneyItrvl":"Y","moneyinType":"T","paymentType":"N","premFrom":"1","renewalChargeFlag":"N"},"sgGrpPersonalInsurInfoVo":{"occClassCode":"05","pSettleDispute":"1"},"sgGrpPrintInfoVo":{"giftInsFlag":"0","listFlag":"L","listPrint":"0","manualUwFlag":"N","personalIdPrint":"0","polProperty":"0","underNoticeType":"L","unusualFlag":"N"},"sgGrpProposalInfoVo":{"addinsuranceVos":[],"effectType":"0","firstChargeDate":1480435200000,"frequencyEffectFlag":"N","insuredTotalNum":100,"sumPrem":20000},"sgGrpVatInfoVo":{"taxIdNo":"123456789"},"chargePayGroupModalVos":[{"groupNo" : 1,"groupName" : "123","num" : 5,"moneyinType" : "W","bankCode" : "1101","bankName" : "建行","bankAccNo" : "89339851"},{"groupNo" : 2,"groupName" :"234","num" : 5,"moneyinType" : "W","bankCode" : "1102","bankName" : "农行","bankAccNo" : "6214830112700579"}],"insuredGroupModalVos":[{"ipsnGrpNo" : 1,"ipsnGrpName" : "王二","occClassCode" : "00","ipsnOccSubclsCode" : "000101","ipsnGrpNum" : 10,"genderRadio": 1.1,"gsRate" : 1.2,"ssRate" : 1.3,"insuranceInfoVos" : [{ "polCode" : "616-1","mrCode": "M","faceAmnt" : 10000.0,"premium" : 100.0,"stdPremium": 100.0,"premRate": 10.1,"recDisount": 10.2},{"polCode" : "616-1","mrCode": "M","faceAmnt" : 10001.0,"premium" :101.0,"stdPremium": 101.0,"premRate": 11.1,"recDisount": 11.2}],"bnfrInfoList" : []}]} 
      setTimeout(function() {
          var jsonStrs = JSON.stringify(data);
          var objs = eval("(" + jsonStrs + ")");
          var form;
          //alert(JSON.stringify(data));
          for (y in objs) {
              var values = objs[y];
              var jsonStrApplicant = JSON.stringify(values);
              var objApplicant = eval("(" + jsonStrApplicant + ")");
              var key, value, tagName, type, arr;
              for (x in objApplicant) {
                  key = x;
                  value = objApplicant[key];
                  if("applDate"==key||"firstChargeDate"==key||"speEffectDate"==key||"joinBirthDate"==key||"inForceDate"==key||"enstPremDeadline"==key||"constructionDur"==key||"until"==key){
      				value = new Date(value).format("yyyy-MM-dd");
      			}
                  $("[name='" + key + "'],[name='" + key + "[]']").each(
                          function() {
                              tagName = $(this)[0].tagName;
                              type = $(this).attr('type');
                              if (tagName == 'INPUT') {
                                  if (type == 'radio') {
                                      $(this).attr('checked',
                                              $(this).val() == value);
                                  } else if (type == 'checkbox') {
                                      arr = value.split(',');
                                      for (var i = 0; i < arr.length; i++) {
                                          if ($(this).val() == arr[i]) {
                                              $(this).attr('checked', true);
                                              break;
                                          }
                                      }
                                  } else {
                                      $("#" + key).val(value);
                                  }
                              } else if (tagName === 'SELECT') {
                                  $("#" + key).combo("val", value);
                              } else if (tagName === 'TEXTAREA') {
              					$("#" + key).val(value);
              				}
                          });
            }
        }
    }, 1000);
      $("#aprovalReason").val(data.aprovalReason);
      
	  //赋值汇交人类型为全局变量
	    com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.listType = data.listType;
      // 赋值被保人分组信息为全局变量
      com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
      //赋值收付费分组信息为全局变量
      com.common.chargePayList = data.chargePayGroupModalVos;
  	// 赋值险种信息为全局变量
      com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd = data.addinsuranceVos;
  	// 加载险种表格
      com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.reloadbusiProdTable();
};

//重新加载险种表格
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.reloadbusiProdTable = function () { 
	$('#fbp-editDataGrid').find("tbody").empty();
	if (com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd !== null && com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd.length > 0) {
		for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd.length; i++) {
			var insurDurUnit = com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].insurDurUnit;
			if(insurDurUnit !== undefined && insurDurUnit !== null){
				insurDurUnit = com.orbps.publicSearch.insurDurUnit(insurDurUnit);
			}else{
				insurDurUnit="";
			}
			$('#fbp-editDataGrid').find("tbody")
					.append("<tr><td ><input type='radio' onclick='com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.queryProductRdio();' id='insuredRad"
					        + i
                            + "'name='insuredRad' value='"
                            + i
                            + "'></td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].busiPrdCode
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].busiPrdName
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].amount
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].premium
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].insuredNum
							+ "</td><td>"
							+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].validateDate
							+ "</td><td>"
							+ insurDurUnit
							+ "</td></tr>");
		}
	} else {
		$('#fbp-editDataGrid').find("tbody").append("<tr><td colspan='8' align='center'>无记录</td></tr>");
	}
};

//日期回显long转String
Date.prototype.format = function(fmt)   { 
	//author: meizz   
  var o = {   
    "M+" : this.getMonth()+1,                 //月份   
    "d+" : this.getDate(),                    //日   
    "h+" : this.getHours(),                   //小时   
    "m+" : this.getMinutes(),                 //分   
    "s+" : this.getSeconds(),                 //秒   
    "q+" : Math.floor((this.getMonth()+3)/3), //季度   
    "S"  : this.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}

//初始化
$(function() {
	document.getElementById('personalInsurInfoForm').style.display='none';
	
	//页面加载时先隐藏网点信息
    $("#worksiteHideDiv").hide();
	
	//判断界面显示个人汇交/团体汇交
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
    // 时间控件初始化
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });

    // datagrid组件初始化
    $("*").datagridsInitLoad();

    // combo组件初始化
    $("*").comboInitLoad();
    
    $(function(){
		$("#citySelect").citySelect({
	        url:"/resources/global/js/cityselect/js/city.min.json",
	        required:false
	    });
    });

    $(function(){
	$("#citySelectsP").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        required:false
    });
    });
    
	 // 设置页面元素只读
	$("#applInfoForm input[type='text']").attr("readOnly",true);
	$("#applInfoForm select").attr("readOnly",true);
	$("#vAddTaxInfoForm input[type='text']").attr("readOnly",true);
	$("#vAddTaxInfoForm select").attr("readOnly",true);
	$("#grpInsurInfoForm input[type='text']").attr("readOnly",true);
	$("#grpInsurInfoForm select").attr("readOnly",true);
	$("#personalInsurInfoForm input[type='text']").attr("readOnly",true);
	$("#personalInsurInfoForm select").attr("readOnly",true);
	$("#proposalInfoForm input[type='text']").attr("readOnly",true);
	$("#proposalInfoForm select").attr("readOnly",true);
	$("#payInfoForm input[type='text']").attr("readOnly",true);
	$("#payInfoForm select").attr("readOnly",true);
	$("#printInfoForm input[type='text']").attr("readOnly",true);
	$("#printInfoForm select").attr("readOnly",true);
	$(".dist").attr("disabled",true);
	$(".city").attr("disabled",true);
	$(".prov").attr("disabled",true);
	$("#applDate").attr("disabled",true);
	$("#applDateBtn").attr("disabled",true);
	$("#joinBirthDate").attr("disabled",true);
	$("#joinBirthDateBtn").attr("disabled",true);
	$("#speEffectDate").attr("disabled",true);
	$("#speEffectDateBtn").attr("disabled",true);
	$("#firstChargeDate").attr("disabled",true);
	$("#firstChargeDateBtn").attr("disabled",true);
	$("#listType").attr("disabled",true);
//	$("#until").attr("disabled",true);	
//	$("#untilBtn").attr("disabled",true);
	
	 //服务器测试
	   var dataCipher=com.ipbps.getDataCipher();
	   var url= '/orbps/web/authSupport/check?serviceName=cntrEntrySgGrpInsurApplService&dataCipher='+dataCipher;
	   lion.util.postjson(url,null,com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.successQueryDetail,null,null);
});

//被保人分组
$("#btnGoup").click(function() {
    com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.empty();
        com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.load("/orbps/orbps/public/searchModal/html/insuredGroupModal.html",function(){
			  $(this).modal('toggle');
			  $(this).comboInitLoad();
			  $(".fa").removeClass("fa-warning");
			  $(".fa").removeClass("fa-check");
			  $(".fa").removeClass("has-success");
			  $(".fa").removeClass("has-error");
		  });
		  setTimeout(function(){
			  // 刷新table
	          	com.orbps.common.reloadProposalGroupModalTable();
		  },100);
	 });

//收付费分组
$("#btnChargePay")
        .click(
                function() {
                	com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.empty();
                	com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog
                            .load(
                                    "/orbps/orbps/public/searchModal/html/chargePayGroupModal.html",
                                    function() {
                                        $(this).modal('toggle');
                                        // combo组件初始化
                                        $(this).comboInitLoad();
                                        $(".fa").removeClass("fa-warning");
                          			  $(".fa").removeClass("fa-check");
                          			  $(".fa").removeClass("has-success");
                          			  $(".fa").removeClass("has-error");
                                    });
                    setTimeout(function() {
                    	com.common.reloadPublicChargePayModalTable();
                    },600);
                });

// 表单提交
$("#btnSubmit").click(
        function() {
        			var sgGrpInsurApplVo = {};
                    sgGrpInsurApplVo.taskInfo={};
                    sgGrpInsurApplVo.sgGrpApplInfoVo={};
                    sgGrpInsurApplVo.taskInfo.taskId = com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.taskId;
                    sgGrpInsurApplVo.taskInfo.bizNo=$("#applInfoForm #applNo").val();
                    sgGrpInsurApplVo.sgGrpApplInfoVo.applNo = $("#applInfoForm #applNo").val();
                    sgGrpInsurApplVo.note = $("#note").val();
                    sgGrpInsurApplVo.approvalState = $("#reviewFlag").val();
                    lion.util
                            .postjson(
                                    '/orbps/web/orbps/otherfunction/sgmanualapproval/submit',
                                    sgGrpInsurApplVo,
                                    com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.successSubmit,
                                    null,
                                    null);
                }
            
        );
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.successSubmit=function (data) {
	  if (data.retCode==="1"){
	        lion.util.info("提示", "提交清汇审批信息成功");
	    }else{
	        lion.util.info("提示", "提交失败，失败原因："+data.errMsg);
	    }
  // 成功后刷新页面
  setTimeOut(function() {
      window.location.reload();
  }, 500);
}

//查看责任信息

com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.queryProductRdio = function () {
		var radioVal;
		var temp = document.getElementsByName("insuredRad");
		for(var i=0;i<temp.length;i++){
		     if(temp[i].checked){
		    	 radioVal = temp[i].value;
		     }
		}
		com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addinsuranceVo = com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[radioVal];
	}

// 责任信息
$("#btnSelect").click(function() {
	for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd.length; i++) {
		var busiPrdCode = com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].busiPrdCode;
		if(busiPrdCode===com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addinsuranceVo.busiPrdCode){
			com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos=com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.busiProd[i].responseVos;
			break;
		}
	}
			// 清空modal
        com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.empty(); 
        // 加载责任modal
        com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.load(
            "/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
                // 初始化插件
            	$(this).modal('toggle');
				com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.reloadProductTable();
            });
    });
//责任回显
com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.reloadProductTable = function () {
		$('#coverageInfo_list').find("tbody").empty();
		if (com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos !== null && com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos.length > 0) {
			for (var i = 0; i < com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos.length; i++) {
				$('#coverageInfo_list').find("tbody")
						.append("<tr><td>"
								+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos[i].productCode
								+ "</td><td>"
								+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos[i].productName
								+ "</td><td>"
								+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos[i].productNum
								+ "</td><td>"
								+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos[i].productPremium
								+ "</td><td>"
								+ com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.responseVos[i].subStdPremium
								+ "</td></tr>");
			}
		} else {
			$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
		}
	}
	
//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.empty();
	com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.otherFunction.manualApproval.childPage.applApproval.sgGrpInsurAppl.grpSalesListFormVos);
	});
});
