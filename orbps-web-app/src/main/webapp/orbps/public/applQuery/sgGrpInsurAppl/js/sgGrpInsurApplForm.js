com.orbps.otherFunction.contractQuery.addDialog = $('#btnModel');
com.orbps.common.proposalGroupList = new Array();
com.orbps.common.salesBranchNo;
com.orbps.common.salesChannel;
com.orbps.common.worksiteNo;
com.orbps.common.saleCode;
com.orbps.common.applNo;
com.orbps.common.quotaEaNo;
com.orbps.otherFunction.contractQuery.grpSalesListFormVos = [];
$(function(){
	// combo组件初始化
    //$("*").comboInitLoad();

	//省市县三级联动
	$("#citySelect").citySelect({
		url:"/resources/global/js/cityselect/js/city.min.json",
		required:false
	});
	//省市县三级联动
	$("#citySelectsP").citySelect({
		url:"/resources/global/js/cityselect/js/city.min.json",
		required:false
	});
	
	// 设置总天数只读
    $("#totalDays").attr("readOnly",true);
    
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
	var data = com.orbps.otherFunction.contractQuery.data;
	//回显方法
	jsonStr = JSON.stringify(data);
	//特别约定
	$("#convention").val(data.sgGrpProposalInfoVo.convention);
    var objs = eval("("+jsonStr+")");
    var listType=objs.listType;
    if(listType==="G"){
		document.getElementById('personalInsurInfoForm').style.display='none';
		document.getElementById('grpInsurInfoForm').style.display='';
	}else{
		document.getElementById('grpInsurInfoForm').style.display='none';
		document.getElementById('personalInsurInfoForm').style.display='';
	}
    //特别约定
    $("#proposalInfoForm #convention").val(data.sgGrpProposalInfoVo.convention);
    $("#listType").combo("val",listType );
    var key,value,tagName,type,arr ,keys,form;
    for (y in objs) {
    	form = y;
    	var a = form.replace("Vos","Form");
    	var formId = a.replace("Vo","Form");
          	
    	if(formId==="sgGrpVatInfoForm"){
    		formId="vAddTaxInfoForm";
    	}else if(formId==="sgGrpApplInfoForm"){
    		formId="applInfoForm";
    	}else if(formId==="sgGrpInsurInfoForm"){
    		formId="grpInsurInfoForm";
    	}else if(formId==="sgGrpPersonalInsurInfoForm"){
    		formId="personalInsurInfoForm";
    	}else if(formId==="sgGrpProposalInfoForm"){
    		formId="proposalInfoForm";
    	}else if(formId==="sgGrpPayInfoForm"){
    		formId="payInfoForm";
    	}else if(formId==="sgGrpPrintInfoForm"){
    		formId="printInfoForm";
    	}else{
    	}
        var values = objs[y];
        var jsonStrApplicant = JSON.stringify(values);
        var objApplicant = eval("(" + jsonStrApplicant + ")");
        var key, value, tagName, type, arr;
        for (x in objApplicant) {
            key = x;
            value = objApplicant[key];
            if("citySelect"===key||"cityCode"===key||"countyCode"===key){
				$("#"+formId +" #"+key).combo("val", value);
            }
            if("province"===key||"city"===key||"county"===key){
            	$("#"+formId +" #"+key).combo("val", value);
          }
          
            if("applDate"===key||"speEffectDate"===key||"firstChargeDate"===key||"joinBirthDate"===key
            		||"effectDate"===key){
				value = new Date(value).format("yyyy-MM-dd");
			}
            $("[name='" + key + "'],[name='" + key + "[]']").each(
                    function() {
                        tagName = $(this)[0].tagName;
                        type = $(this).attr('type');
                        if (tagName === 'INPUT') {
                            if (type === 'radio') {
                                $(this).attr('checked',
                                        $(this).val() == value);
                            } else if (type === 'checkbox') {
                                arr = value.split(',');
                                for (var i = 0; i < arr.length; i++) {
                                    if ($(this).val() === arr[i]) {
                                        $(this).attr('checked', true);
                                        break;
                                    }
                                }
                            } else {
                            	$("#"+formId +" #"+key).val(value);
        					}
        				}else if(tagName==='SELECT'){
        					$("#"+ key).comboInitLoadById(value);
                        }else if(tagName==='TEXTAREA'){
                        	$("#"+formId +" #"+key).combo("val", value);
                        }

                    });
        }
    }
    
 // combo组件初始化
    $("*").comboInitLoad();
    
	setTimeout(function(){
		com.orbps.otherFunction.contractQuery.grpSalesListFormVos =  data.sgGrpApplInfoVo.grpSalesListFormVos;
	    // 将省市县置为可以回显的状态
	    $(".dist").attr("disabled",false);
        $(".city").attr("disabled",false);
        $(".prov").attr("disabled",false);
        $("#grpInsurInfoForm #provinceCode").combo("val",data.sgGrpInsurInfoVo.provinceCode);
        $("#grpInsurInfoForm #cityCode").combo("val",data.sgGrpInsurInfoVo.cityCode);
        $("#grpInsurInfoForm #countyCode").combo("val",data.sgGrpInsurInfoVo.countyCode);
        $("#personalInsurInfoForm #province").combo("val",data.sgGrpPersonalInsurInfoVo.province);
        $("#personalInsurInfoForm #city").combo("val",data.sgGrpPersonalInsurInfoVo.city);
        $("#personalInsurInfoForm #county").combo("val",data.sgGrpPersonalInsurInfoVo.county);
	}, 500);
	
	
	// 赋值被保人分组信息为全局变量
    com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
    // 赋值收付费分组信息为全局变量
    com.common.chargePayList = data.chargePayGroupModalVos;
    com.orbps.common.list = data.responseVos;
    
    //回显险种信息
    $('#bsInfoListTb').editDatagrids("bodyInit", data.addinsuranceVos);
});

//点击查看责任按钮弹出责任列表
com.orbps.otherFunction.contractQuery.publicRadio = function(vform) {
//	$("#proposalInfoForm input[type='text']").val("");
//	$("#proposalInfoForm select2").combo("refresh");
	var radioVal;
	//回显前加个延时，否则会被refresh掉，refresh的执行时间比较长
	var temp = document.getElementsByName("insuredRaded");
	for(var i=0;i<temp.length;i++){
	     if(temp[i].checked){
	    	 radioVal = temp[i].value;
	     }
	}
	com.orbps.otherFunction.contractQuery.insuredType = radioVal;
	insuredVo= com.orbps.otherFunction.contractQuery.grpBusiPrdList[radioVal];
	com.orbps.otherFunction.contractQuery.responseList = insuredVo;
	setTimeout(function(){
		// 回显
		com.orbps.otherFunction.contractQuery.showInsured(insuredVo);
	},100);
};
//回显方法
com.orbps.otherFunction.contractQuery.showInsured = function(msg){
	jsonStr = JSON.stringify(msg);
    var obj = eval("("+jsonStr+")");
    var key,value,tagName,type,arr;
    for(x in obj){
        key = x;
        value = obj[x];
        if("applDate"===key||"speEffectDate"===key||"firstChargeDate"===key){
			value = new Date(value).format("yyyy-MM-dd");
		}
        $("[name='"+key+"'],[name='"+key+"[]']").each(function(){
            tagName = $(this)[0].tagName;
            type = $(this).attr('type');
            if(tagName==='INPUT'){
                if(type==='radio'){
                    $(this).attr('checked',$(this).val()===value);
                }else if(type==='checkbox'){
                    arr = value.split(',');
                    for(var i =0;i<arr.length;i++){
                        if($(this).val()===arr[i]){
                            $(this).attr('checked',true);
                            break;
                        }
                    }
                }else{
                    $("#proposalInfoForm #"+key).val(value);
                }
            }else if(tagName==='SELECT' || tagName==='TEXTAREA'){
                $("#proposalInfoForm #"+key).combo("val", value);
            }
        });
    }
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

//续期扣款改变页面样式
//$("#renewalChargeFlag").change(function(){
//	if($("#renewalChargeFlag").val()==="N"){
//		$("#chargeDeadline").attr("disabled",true);
//		$("#chargeDeadlinebtn").attr("disabled",true);
//		$("#chargeDeadline").val("");
//	    $(".fa").removeClass("fa-warning");
//	    $(".fa").removeClass("fa-check");
//	    $(".fa").removeClass("has-success");
//	    $(".fa").removeClass("has-error");
//	}else if($("#renewalChargeFlag").val()==="Y"){
//		$("#chargeDeadline").attr("disabled",false);
//		$("#chargeDeadlinebtn").attr("disabled",false);
//	}
//});

//多期暂交改变页面样式
$("#multiRevFlag").change(function(){
	if($("#multiRevFlag").val()==="N"){
		$("#multiTempYear").attr("disabled",true)
		$("#multiTempYear").val("");
	    $(".fa").removeClass("fa-warning");
	    $(".fa").removeClass("fa-check");
	    $(".fa").removeClass("has-success");
	    $(".fa").removeClass("has-error");
	}else if($("#multiRevFlag").val()==="Y"){
		$("#multiTempYear").attr("disabled",false)
	}
});

//根据保费来源更改页面样式
$("#premFrom").change(function() {
	//如果是现金都是灰的
	if($("#moneyinType").val()==="C"){
		//将账号信息置为不可用。
        $("#bankCode").attr("disabled",true);
        $("#bankCode").val("");
        $("#bankName").attr("disabled",true);
        $("#bankName").val("");
        $("#bankAccNo").attr("disabled",true);
        $("#bankAccNo").val("");
	}else{
		if($("#premFrom").val()==="2"){
            //如果选择个人账户，将账号信息置为不可用。
            $("#bankCode").attr("disabled",true);
            $("#bankCode").val("");
            $("#bankName").attr("disabled",true);
            $("#bankName").val("");
            $("#bankAccNo").attr("disabled",true);
            $("#bankAccNo").val("");
        }else{
            $("#bankCode").attr("disabled",false);
            $("#bankName").attr("disabled",false);
            $("#bankAccNo").attr("disabled",false);
        }
	}
    
});
//根据结算方式更改页面样式
$("#payInfoForm #stlType").change(function() {
    if($("#stlType").val()==="N"){
        $("#payInfoForm #stlLimit").attr("disabled",true);
        $("#payInfoForm #settlementRatio").attr("disabled",true);
        $("#payInfoForm #btnStlDate").attr("disabled",true);
    }
    else if($("#stlType").val()==="X"){
        $("#stlLimit").attr("disabled",false);
        $("#settlementRatio").attr("disabled",true);
        $("#btnStlDate").attr("disabled",false);
    }else if($("#stlType").val()==="D"){
        $("#stlLimit").attr("disabled",true);
        $("#settlementRatio").attr("disabled",true);
        $("#btnStlDate").attr("disabled",false);
    }else if($("#stlType").val()==="L"){
        $("#stlLimit").attr("disabled",true);
        $("#settlementRatio").attr("disabled",false);
        $("#btnStlDate").attr("disabled",true);
    }
});

//争议处理方式
$("#grpInsurInfoForm #gSettleDispute").change(function() {
	var disputePorcWay = $("#grpInsurInfoForm #gSettleDispute").val();
	if(disputePorcWay==="1"){
		$("#grpInsurInfoForm #arbOrgName").attr("disabled", true);
	}else{
		$("#grpInsurInfoForm #arbOrgName").attr("disabled", false);
	}
});

//用于回显的责任信息
var responseVos = [];
//点击查看责任按钮弹出责任列表
$("#btnSelect").click(function() {
	// 获取选择的险种信息
     var selectData = $("#bsInfoListTb").editDatagrids(
            "getSelectRows");
	// 判断选择的是否是一个主险
	if ((null == selectData) || (selectData.length == 0) || (selectData.length > 1)) {
		lion.util.info("警告", "请选择一个主险信息");
		return false;
	}
	var data = com.orbps.otherFunction.contractQuery.data;
	//循环对比选中的险种，和责任信息中的数据对比
	for (var i = 0; i < data.addinsuranceVos.length; i++) {
		var busiPrdCode = data.addinsuranceVos[i].busiPrdCode;
		if(busiPrdCode===selectData.busiPrdCode){
			responseVos = data.addinsuranceVos[i].responseVos;
			break;
		}
	}
	$("#btnModel").empty();
	$("#btnModel").load(
			"/orbps/orbps/public/searchModal/html/insurRespModal.html",function() {
	$(this).modal('toggle');
	});
	setTimeout(function() {
		// 回显
		com.orbps.otherFunction.contractQuery.reloadSgResponseModalTable();
	}, 100);
});

com.orbps.otherFunction.contractQuery.reloadSgResponseModalTable = function () {
	$('#coverageInfo_list').find("tbody").empty();
	if (responseVos !== null && responseVos.length > 0) {
		for (var i = 0; i < responseVos.length; i++) {
			$('#coverageInfo_list').find("tbody")
					.append("<tr><td>"
							+ responseVos[i].productCode
							+ "</td><td>"
							+ responseVos[i].productName
							+ "</td><td>"
							+ responseVos[i].productNum
							+ "</td><td>"
							+ responseVos[i].productPremium
							+ "</td><td>"
							+ responseVos[i].subStdPremium
							+ "</td></tr>");
		}
	} else {
		$('#coverageInfo_list').find("tbody").append("<tr><td colspan='5' align='center'>无记录</td></tr>");
	}
}
//被保人分组
$("#btnGoup").click(function() {
	com.orbps.otherFunction.contractQuery.addDialog.empty();
	com.orbps.otherFunction.contractQuery.addDialog.load(
            "/orbps/orbps/public/searchModal/html/insuredGroupModal.html",
            function() {
                $(this).modal('toggle');
                $(this).comboInitLoad();
                // 刷新table
            });
    setTimeout(function() {
        // 回显
    	com.orbps.common.reloadProposalGroupModalTable();
    }, 400);
});

//导出被保人清单
$("#btnExport").click(function(){
	window.location.href="/orbps/web/orbps/contractReview/offlineList/download/"+com.orbps.otherFunction.contractQuery.data.sgGrpApplInfoVo.applNo;
});

//影像信息查询
$("#imageQuery")
        .click(
                function() {
                	var salesBranchNo ="";
                	if(com.orbps.otherFunction.contractQuery.grpSalesListFormVos.length===1){
                		salesBranchNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].salesBranchNo;
                		salesChannel = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].salesChannel;
        		    	worksiteNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].worksiteNo;
        		    	saleCode = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[0].saleCode;
                	}else{
	            		for (var i = 0; i < com.orbps.otherFunction.contractQuery.grpSalesListFormVos.length; i++) {
	            			var jointFieldWorkFlag = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].jointFieldWorkFlag;
	            			if(jointFieldWorkFlag==="Y"){
	            				salesBranchNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].salesBranchNo;
	            				salesChannel = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].salesChannel;
	            		    	worksiteNo = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].worksiteNo;
	            		    	saleCode = com.orbps.otherFunction.contractQuery.grpSalesListFormVos[i].saleCode;
	            				break;
	            			}
	            		}
                	}
                	com.orbps.common.applNo = $("#applInfoForm #applNo").val();
                	com.orbps.common.quotaEaNo = $("#applInfoForm #quotaEaNo").val();
                	if(salesBranchNo !== ""){
                		com.orbps.common.salesBranchNo = salesBranchNo;
                	}
                	com.orbps.common.salesChannel = salesChannel;
                	com.orbps.common.worksiteNo = worksiteNo;
                	com.orbps.common.saleCode = saleCode;
                	com.orbps.otherFunction.contractQuery.addDialog.empty();
                	com.orbps.otherFunction.contractQuery.addDialog.load(
                                  "/orbps/orbps/public/modal/html/imageCollection.html",
                                  function() {
                                      $(this).modal('toggle');
                                      $(this).comboInitLoad();
                                  });
                });
//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.otherFunction.contractQuery.addDialog.empty();
	com.orbps.otherFunction.contractQuery.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.otherFunction.contractQuery.grpSalesListFormVos);
	});
});

//收付费分组
$("#btnChargePay").click(
        function() {
        	com.orbps.otherFunction.contractQuery.addDialog.empty();
        	com.orbps.otherFunction.contractQuery.addDialog.load(
                    "/orbps/orbps/public/searchModal/html/chargePayGroupModal.html",
                    function() {
                        $(this).modal('toggle');
                        $(this).comboInitLoad();
                        // 刷新table
                    });
            setTimeout(function() {
                // 回显
            	com.common.reloadPublicChargePayModalTable();
            }, 400);
        });