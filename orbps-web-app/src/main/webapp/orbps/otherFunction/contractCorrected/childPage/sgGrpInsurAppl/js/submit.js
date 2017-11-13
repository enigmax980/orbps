com.common.insuredCount = 0;
com.common.insuredList = new Array();
com.common.insuredType = -1;
com.common.oranLevelCount = 0;
com.common.oranLevelType = -1;
com.common.oranLevelList = new Array();
com.common.zTrees = [];

//表单提交
$("#btnSubmit").click(function(){
 	// 判断共保协议号是否必录
	if ("Y" === com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.agreementNoFlag) {
		var agreementNo = $("#applInfoForm #agreementNo")
				.val();
		if ("" === agreementNo) {
			lion.util.info("提示", "共保协议号不能为空!");
			return false;
		}
	}
	// jquery validate 校验
   if (com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.payInfoForm
                   .validate().form()
           && com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.printInfoForm
                   .validate().form()
           && com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.proposalInfoForm
                   .validate().form()
//           && com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.vAddTaxInfoForm
//                   .validate().form()
           ) {
   	var listType = $("#listType").val();
		if("G" === listType){
			//校验手机与固定电话必须输一个
			if("" === $("#grpInsurInfoForm #contactMobile").val() ){
				lion.util.info("联系人手机号不能为空");
				return false;
			}
			//校验合同打印方式为电子保单时，必须输入联系人电子邮箱
//			if("0" === $("#underNoticeType").val()&& "" === $("#grpInsurInfoForm #contactEmail").val()){
//				lion.util.info("合同打印方式选择了电子保单，请输入联系人电子邮箱");
//				return false;
//			}
			if(!com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.grpInsurInfoForm
           .validate().form()){
				return false;
			};
			var zipCode = $("#zipCode").val();
			var city = com.orbps.publicSearch.checkPostCode(zipCode);
			var province = $("#provinceCode").val();
			province = province.substr(0,2);
			if(city !== province){
				lion.util.info("提示","邮编输入不正确，请选择[ "+province+" ]的对应正确邮编！");
				return false;
			}
		}else if("P" === listType){
			//校验手机与固定电话必须输一个
			if("" === $("#personalInsurInfoForm #joinMobile").val() ){
				lion.util.info("汇交人手机号不能为空");
				return false;
			}
			//校验合同打印方式为电子保单时，必须输入联系人电子邮箱
//			if("0" === $("#underNoticeType").val()&& "" === $("#personalInsurInfoForm #joinEmail").val()){
//				lion.util.info("合同打印方式选择了电子保单，请输入联系人电子邮箱");
//				return false;
//			}
			if(!com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoForm
           .validate().form()){
				return false;
			};
			var postCode = $("#postCode").val();
			var city = com.orbps.publicSearch.checkPostCode(postCode);
			var province = $("#province").val();
			province = province.substr(0,2);
			if(city !== province){
				lion.util.info("提示","邮编输入不正确，请选择[ "+province+" ]的对应正确邮编！");
				return false;
			}
		}
       // select校验
       if (validateSelectVal()) {
		 $("#personalInsurInfoForm #joinBirthDate").attr("disabled",false);
		 //如果证件号码不是身份证时，校验出生日期是否大于18岁
      		var joinBirthDate = $("#personalInsurInfoForm #joinBirthDate").val();
      		joinBirthDate = joinBirthDate.substring(0,4)
      		var date = new Date();
      		var year = date.getFullYear();
      		if((year - joinBirthDate) < 18 ){
      			lion.util.info("提示","请输入大于18岁的汇交人出生日期");
      			return false ;
      		}
		   //  获取要约信息下面的所有险种信息
           var getAddRowsData = $("#fbp-editDataGrid")
                   .editDatagrids("getRowsData");
           // 提交方法
           var sgGrpInsurApplVo = {};
           if("G" === listType){
        	   	if(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.grpInsurInfoForm.validate().form()){
        		   //团单
                   sgGrpInsurApplVo.sgGrpInsurInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.grpInsurInfoForm
                             .serializeObject();
	           	}else{
	           		lion.util.info("汇交人信息输入不正确");
	           		return false;
	           	}
           }else if("P" === listType){
	        	 //个人
	        	 $("#personalInsurInfoForm #joinBirthDate").attr("disabled",false);
	        	 if(com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoForm.validate().form()){
	        		 sgGrpInsurApplVo.sgGrpPersonalInsurInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.personalInsurInfoForm
	                 .serializeObject();
	        	 }else{
	         		lion.util.info("汇交人信息输入不正确");
	         		return false;
	             }
           }
           sgGrpInsurApplVo.sgGrpApplInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.applInfoForm
                   .serializeObject();
           sgGrpInsurApplVo.sgGrpPayInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.payInfoForm
                   .serializeObject();
           sgGrpInsurApplVo.sgGrpPrintInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.printInfoForm
                   .serializeObject();
           sgGrpInsurApplVo.sgGrpProposalInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.proposalInfoForm 
           		.serializeObject();
           sgGrpInsurApplVo.sgGrpVatInfoVo = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.vAddTaxInfoForm
                   .serializeObject();
           sgGrpInsurApplVo.insuredGroupModalVos = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.insuredList;
           sgGrpInsurApplVo.organizaHierarModalVos = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.oranLevelList;
           sgGrpInsurApplVo.addinsuranceVos = getAddRowsData;
           var responseVos = new Array();
           if(Object.prototype.toString.call(com.orbps.common.list) === '[object Array]'){
				for (var i = 0; i < com.orbps.common.list.length; i++) {
					var array_element = com.orbps.common.list[i];
					for (var j = 0; j < array_element.length; j++) {
						var array_elements = array_element[j];
						responseVos.push(array_elements);
					}
				}
			}
           sgGrpInsurApplVo.responseVos = responseVos;
           sgGrpInsurApplVo.listType = $("#listType").val();
           sgGrpInsurApplVo.taskInfo={};
           sgGrpInsurApplVo.taskInfo.taskId = com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.taskId;
           lion.util
                   .postjson(
                           '/orbps/web/orbps/otherfunction/sgGrpInsurAppl/submit',
                           sgGrpInsurApplVo,
                           com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.successSubmit,
                           null,
                           null);
       }
   }
});

	//回退信息
	$("#btnBack").click(function() {
		// 提交方法
		$("#btnBack").attr("disabled",true);
		var backInfo="";
		backInfo = backInfo + $("#applInfoForm #applNo").val() + ",";
		backInfo = backInfo + $("#backStat").val() + ",";
		backInfo = backInfo + $("#backReason").val();
		lion.util.postjson('/orbps/web/orbps/otherfunction/sgGrpInsurAppl/back',backInfo,com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.successBackSubmit,null,null);
	});

	//被保人分组
	 $("#btnGoup").click(function() {
		  com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.empty();
		  com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.load("/orbps/orbps/public/searchModal/html/insuredGroupModal.html",function(){
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
		// 收付费分组
		    $("#btnChargePay")
		            .click(
		                    function() {
		                    	com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.empty();
		                    	com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog
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
    //组织层次架构 
	 $("#btnOranLevel").click(function() {
		 com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.empty();
		 com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.addDialog.load("/orbps/orbps/public/modal/html/organizaHierarModal.html",function(){
			$(this).modal('toggle');
			// combo组件初始化
			$(this).comboInitLoad();
			$(this).combotreeInitLoad();
		});
		setTimeout(function(){
			// 回显
			reloadPublicOranLevelModalTable ();
		},100);
	 });
	 //修改被保人信息
	 $("#submit #btnModify").click(function(){
//		 var listImportIpsnInfoVo ={};
//		 listImportIpsnInfoVo.applNo = $("#applInfoForm #applNo").val(); 
		 $('#btnModel').empty();  		
		 $('#btnModel').load("/orbps/orbps/otherFunction/contractCorrected/childPage/listImport/html/offlineListImport.html",function(){
			 $(this).modal('toggle');
			 $(".fa").removeClass("fa-warning");
			  $(".fa").removeClass("fa-check");
			  $(".fa").removeClass("has-success");
			  $(".fa").removeClass("has-error");
		 });
	 });
// 校验选择信息
function validateSelectVal(){
	var applDate = $("#applInfoForm #applDate").val();
	if("" === applDate){
		lion.util.info("投保申请日期不能为空");
		return false;
	}

	
	var listType = $("#listType").val();
	if("" === listType){
		lion.util.info("汇交人类型不能为空");
		return false;
	}
	
	if("G" === listType){
		//团体汇交
		var gIdType = $("#grpInsurInfoForm #idType").val();
		if(gIdType===null||""===gIdType){
			lion.util.info("团体证件类型不能为空");
			return false;
		}
		
		var gCompanyRegist = $("#grpInsurInfoForm #companyRegist").val();
		if(gCompanyRegist===null||""===gCompanyRegist){
			lion.util.info("企业注册地不能为空");
			return false;
		}
		
		var gDepartmentType = $("#grpInsurInfoForm #departmentType").val();
		if(gDepartmentType===null||""===gDepartmentType){
			lion.util.info("部门类型不能为空");
			return false;
		}
		
		var gOccClassCode = $("#grpInsurInfoForm #occClassCode").val();
		if(gOccClassCode===null||""===gOccClassCode){
			lion.util.info("团体职业类别不能为空");
			return false;
		}	
		
		var gProvinceCode = $("#grpInsurInfoForm #provinceCode").val();
		if(gProvinceCode===null||""===gProvinceCode){
			lion.util.info("省/直辖市不能为空");
			return false;
		}
		
		var gCityCode = $("#grpInsurInfoForm #cityCode").val();
		if(gCityCode===null||""===gCityCode){
			lion.util.info("市/城区不能为空");
			return false;
		}
		
		var gCountyCode = $("#grpInsurInfoForm #countyCode").val();
		if(gCountyCode===null||""===gCountyCode){
			lion.util.info("县/地级市不能为空");
			return false;
		}
		
		var gContactIdType = $("#grpInsurInfoForm #contactIdType").val();
		if(gContactIdType===null||""===gContactIdType){
			lion.util.info("联系人姓名不能为空");
			return false;
		}
		
		var gSettleDispute = $("#grpInsurInfoForm #gSettleDispute").val();
		if (gSettleDispute === null || "" === gSettleDispute) {
			lion.util.info("警告", "请选择争议处理方式");
			return false;
		}
		// 仲裁机构名称
		if (gSettleDispute === "2") {
			var parbOrgName = $("#grpInsurInfoForm #parbOrgName").val();
			if (parbOrgName === "") {
				lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
				return false;
			}
		}
	}else if("P" === listType){
		//个人汇交
		var joinName = $("#personalInsurInfoForm #joinName").val();
		if(joinName===null||""===joinName){
			lion.util.info("汇交人证件类型不能为空 ");
			return false;
		}
		
		var joinSex = $("#personalInsurInfoForm #joinSex").val();
		if(joinSex===null||""===joinSex){
			lion.util.info("汇交人性别 不能为空");
			return false;
		}
		
		var joinBirthDate = $("#personalInsurInfoForm #joinBirthDate").val();
		if(joinBirthDate===null||""===joinBirthDate){
			lion.util.info("汇交人出生日期 不能为空");
			return false;
		}
		var provinceCode = $("#personalInsurInfoForm #province").val();
	    if (provinceCode === null || "" === provinceCode) {
	        lion.util.info("警告", "请选择省/直辖市");
	        return false;
	    }
		var city = $("#personalInsurInfoForm #city").val();
		if (city === null || "" === city) {
		        lion.util.info("警告", "请选择市/城区 ");
		        return false;
		 }
		    
	     var county = $("#personalInsurInfoForm #county").val();
		    if (county === null || "" === county) {
		        lion.util.info("警告", "请选择县/地级市");
		        return false;
		    }
		var pSettleDispute = $("#personalInsurInfoForm #pSettleDispute").val();
	    if (pSettleDispute === null || "" === pSettleDispute) {
	        lion.util.info("警告", "请选择争议处理方式");
	        return false;
	    }
	    // 仲裁机构名称
		if (pSettleDispute === "2") {
			var joinParbOrgName = $("#personalInsurInfoForm #joinParbOrgName").val();
			if (joinParbOrgName === "") {
				lion.util.info("提示", "争议处理方式为仲裁,仲裁机构名称必录");
				return false;
			}
		}
	}
	
	var effectType = $("#proposalInfoForm #effectType").val();
	if(effectType===null||""===effectType){
		lion.util.info("生效方式不能为空");
		return false;
	}
	
	if("1"===effectType){
		var speEffectDate = $("#proposalInfoForm #speEffectDate").val();
		if(speEffectDate===null||""===speEffectDate){
			lion.util.info("生效方式为指定生效时，指定生效日不能为空");
			return false;
		}
	}
	
	var moneyItrvl = $("#payInfoForm #moneyItrvl").val();
	if(moneyItrvl===null||""===moneyItrvl){
		lion.util.info("交费方式不能为空");
		return false;
	}
	
	var premFrom = $("#payInfoForm #premFrom").val();
	if(premFrom===null||""===premFrom){
		lion.util.info("保费来源不能为空");
		return false;
	}
	
	//如果交费形式不是现金和银行交款单，保费来源不是个人账户付款时，校验银行账号信息必须输入
	var moneyinType = $("#moneyinType").val();
	if (moneyinType !== "R" && moneyinType !== "C" && "2" !== premFrom) {
		var bankaccNo = $("#payInfoForm #bankaccNo").val();
		if (bankaccNo === null || "" === bankaccNo) {
			lion.util.info("警告", "银行账号不能为空");
			return false;
		}
		var bankCode = $("#payInfoForm #bankCode").val();
		if (bankCode === null || "" === bankCode) {
			lion.util.info("警告", "开户银行不能为空");
			return false;
		}
		var bankName = $("#payInfoForm #bankBranchName").val();
		if (bankName === null || "" === bankName) {
			lion.util.info("警告", "开户行名不能为空");
			return false;
		}
	}
	
	var multiRevFlag = $("#payInfoForm #multiRevFlag").val();
	if("Y" === multiRevFlag){
		var multiTempYear = $("#payInfoForm #multiTempYear").val();
		if(multiTempYear===null||""===multiTempYear){
			lion.util.info("当选择多期暂交时，多期暂交年数不能为空");
			return false;
		}
	}

	var underNoticeType = $("#printInfoForm #underNoticeType").val();
	if(underNoticeType===null||""===underNoticeType){
		lion.util.info("合同打印方式为空");
		return false;
	}
	
	var listFlag = $("#printInfoForm #listFlag").val();
	if(listFlag===null||""===listFlag){
		lion.util.info("清单标记为空");
		return false;
	}

	if("N" !== listFlag){
		var personalIdPrint = $("#printInfoForm #personalIdPrint").val();
		if(personalIdPrint===null||""===personalIdPrint){
			lion.util.info("个人凭证打印为空");
			return false;
		}
		
		var listPrint = $("#printInfoForm #listPrint").val();
		if(listPrint===null||""===listPrint){
			lion.util.info("清单打印为空");
			return false;
		}
	}
	
	//判断清汇页面的清单标记 只能选择 普通清单和档案清单。
	if($("#listFlag").val()==="N" || $("#listFlag").val() === "M"){
		lion.util.info("警告","该保单不支持无清单和事后补录清单，请检查！");
		return false;
	}
	
	var giftInsFlag = $("#printInfoForm #giftInsFlag").val();
	if(giftInsFlag===null||""===giftInsFlag){
		lion.util.info("赠送险标记为空");
		return false;
	}
	
	var polProperty = $("#printInfoForm #polProperty").val();
	if(polProperty===null||""===polProperty){
		lion.util.info("保单性质为空");
		return false;
	}
	
	var unusualFlag = $("#printInfoForm #unusualFlag").val();
	if(unusualFlag===null||""===unusualFlag){
		lion.util.info("是否异常告知为空");
		return false;
	}
	
	return true;
}
		


//提交成功回调函数  
com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.successSubmit=function (data, arg) {
	if (data.retCode==="1"){
        lion.util.info("提示", "订正成功");
    }else{
        lion.util.info("提示", "订正失败，失败原因："+data.errMsg);
    }
}

com.orbps.otherFunction.contractCorrected.childPage.sgGrpInsurAppl.successBackSubmit=function(data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示", "回退成功");
    }else{
        lion.util.info("提示", "回退失败，失败原因："+data.errMsg);
    }
	$("#btnBack").attr("disabled",false);
}


//查询成功回调函数
function successQueryDetail(data,args) {
    setTimeout(function() {
    	//特别约定
    	$("#convention").val(data.sgGrpProposalInfoVo.convention);
        var jsonStrs = JSON.stringify(data);
        //alert(jsonStrs);
        var objs = eval("(" + jsonStrs + ")");
        var form;
//        alert(JSON.stringify(data));
        for (y in objs) {
            // form = y;
            // var keys = form;
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            var key, value, tagName, type, arr;
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                if("firstChargeDate"==key||"applDate"==key||"chargeDeadline"==key||"speEffectDate"==key||"joinBirthDate"==key){
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
                            } else if (tagName == 'SELECT'
                                    || tagName == 'TEXTAREA') {
                                $("#" + key).combo("val", value);
                            }

                        });
            }
        }
    }, 100);
}

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