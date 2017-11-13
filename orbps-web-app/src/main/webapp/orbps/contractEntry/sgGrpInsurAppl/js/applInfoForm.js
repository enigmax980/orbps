// 新建contractEntry命名空间
com.orbps.contractEntry = {};
// 新建contractEntry.sgGrpInsurAppl命名空间
com.orbps.contractEntry.sgGrpInsurAppl = {};
com.orbps.contractEntry.sgGrpInsurAppl.taskId;
com.orbps.contractEntry.sgGrpInsurAppl.data={};
com.orbps.contractEntry.sgGrpInsurAppl.busiPrdCode = null;
com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm = $('#applInfoForm');
com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList = [];
com.orbps.contractEntry.sgGrpInsurAppl.agreementNoFlag;
com.orbps.contractEntry.sgGrpInsurAppl.acount = 0;
com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos = [];

com.orbps.contractEntry.sgGrpInsurAppl.successQueryDetail = function(data,args){
	com.orbps.contractEntry.sgGrpInsurAppl.acount=1;
    com.orbps.contractEntry.sgGrpInsurAppl.data = data;
    com.orbps.contractEntry.sgGrpInsurAppl.taskId = data.taskInfo.taskId;
	com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos = data.sgGrpApplInfoVo.grpSalesListFormVos;
    //回显个人汇交、团体汇交
    if("G"===data.listType){
  	  $("#listType").combo("val", "G")
  	  document.getElementById('personalInsurInfoForm').style.display='none';
  	  document.getElementById('grpInsurInfoForm').style.display='';
  	  setTimeout( function(){
            // 将省市县置为可以回显的状态
      	  $(".dist").attr("disabled",false);
            $(".city").attr("disabled",false);
            $(".prov").attr("disabled",false);
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
      	  $(".dist").attr("disabled",false);
            $(".city").attr("disabled",false);
            $(".prov").attr("disabled",false);
            //alert(data.sgGrpPersonalInsurInfoVo.province);
            $("#personalInsurInfoForm #province").combo("val",data.sgGrpPersonalInsurInfoVo.province);
            //alert($("#personalInsurInfoForm #province").val());
            $("#personalInsurInfoForm #city").combo("val",data.sgGrpPersonalInsurInfoVo.city);
            $("#personalInsurInfoForm #county").combo("val",data.sgGrpPersonalInsurInfoVo.county);
        },1000);
    }
      var jsonStrs = JSON.stringify(data);
      var objs = eval("(" + jsonStrs + ")");
      var form;
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
          	if(null ===value || "" === value || undefined === value){
				continue;
			}
				if("firstChargeDate"==key||"applDate"==key||"speEffectDate"==key||"joinBirthDate"==key){
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
                          ) {
                          	$("#"+ key).comboInitLoadById(value);
                          }else if(tagName == 'TEXTAREA'){
                          	//$("#" + key).combo("val", value);
                        	  $("#" + key).val(value);
                          }

                      });
          }
      }
      //特别约定
      $("#proposalInfoForm #convention").val(data.sgGrpProposalInfoVo.convention);
      // combo组件初始化
      $("*").comboInitLoad();
      // 取到责任信息list
	  for(var i = 0 ; i < data.addinsuranceVos.length; i++){
	  	//当责任不为空时放入全局变量
		  if(lion.util.isNotEmpty(data.addinsuranceVos[i].responseVos)){
			  for (var j = 0; j < data.addinsuranceVos[i].responseVos.length; j++) {
				var array_element = data.addinsuranceVos[i].responseVos[j];
				com.orbps.common.list.push(array_element);
			}
		  }
	  }
	//收付费分组信息
    com.common.chargePayList = data.chargePayGroupModalVos;
    // 赋值被保人分组信息为全局变量
    com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
    // 险种表格回显
    $('#bsInfoListTb').editDatagrids("bodyInit", data.addinsuranceVos);
    com.orbps.contractEntry.sgGrpInsurAppl.queryBusiprodList = data.addinsuranceVos;
    //如果不是新单状态是清汇基本信息录入，置灰跳转按钮。 07   
    if("07" !== data.approvalState ){
		$("#btnlocation").attr("disabled",true);
    }
    // 增加共保协议号是否必录的标识
    if("agreementNo" in data.sgGrpApplInfoVo){
    	if ("" === data.sgGrpApplInfoVo.agreementNo) {
    		com.orbps.contractEntry.sgGrpInsurAppl.agreementNoFlag = "N";
    	} else {
    		com.orbps.contractEntry.sgGrpInsurAppl.agreementNoFlag = "Y";
    	}	
	}else{
		com.orbps.contractEntry.sgGrpInsurAppl.agreementNoFlag = "N";
	}
    
	setTimeout(function(){
		//续期扣款截止日期置灰--一起不支持
		//$("#payInfoForm #chargeDeadline").attr("disabled",true);
		//是否是否续期扣款下拉框默认显示否--一起不支持
//	    if("" === $("#payInfoForm #renewalChargeFlag").val()){
//	    	$("#payInfoForm #renewalChargeFlag").combo("val","N");
//	    }
		//设置保费来源的默认值为“团体账户付款”
		if(""===$("#payInfoForm #premFrom").val()){
			$("#payInfoForm #premFrom").combo("val","1")
		}
		//是否多期暂交下拉框默认显示否
		if("" === $("#payInfoForm #multiRevFlag").val()){
			$("#payInfoForm #multiRevFlag").combo("val","N");
		}
		//保单性质默认值为新单投保，并只读。
		if("" === $("#printInfoForm #polProperty").val()){
			 $("#printInfoForm #polProperty").combo("val","0");
		}
		//赠送险标记默认否，并只读。
		if(""===$("#printInfoForm #giftInsFlag").val()){
			 $("#printInfoForm #giftInsFlag").combo("val","0");
		}
		$("#printInfoForm #giftInsFlag").attr("readOnly",true);
		//清单标记默认值普通清单
		if("" === $("#printInfoForm #listFlag").val()){
			 $("#printInfoForm #listFlag").combo("val","L");
		}
		//是否异常通知下拉框默认显示否
	    if("" === $("#printInfoForm #unusualFlag").val()){
	    	$("#printInfoForm #unusualFlag").combo("val","N");
	    }
    	//企业注册地，默认中国
	    if("" === $("#grpInsurInfoForm #companyRegist").val()){
	    	$("#grpInsurInfoForm #companyRegist").combo("val","CHN");
	    }
	    //争议处理方式默认值
		if("" === $("#grpInsurInfoForm #gSettleDispute").val()){
			$("#grpInsurInfoForm #gSettleDispute").combo("val","1");
		}	
    	//争议处理方式默认诉讼
		if(""===$("#personalInsurInfoForm #pSettleDispute").val()){
			$("#personalInsurInfoForm #pSettleDispute").combo("val","1");
		}
		if("I"===$("#personalInsurInfoForm #joinIdType").val()){
			 $("#personalInsurInfoForm #joinBirthDate").attr("disabled",true);
			 $("#personalInsurInfoForm #joinBirthDateBtn").attr("disabled",true);
			 $("#personalInsurInfoForm #joinSex").attr("readonly",true);
		}
	},1500);
}



com.orbps.contractEntry.sgGrpInsurAppl.successQuery = function(data,args){
	var jsonStrs = JSON.stringify(data);
      var objs = eval("(" + jsonStrs + ")");
      var form;
      for (y in objs) {
          // form = y;
          // var keys = form;
    	  if("sgGrpPayInfoVo"===y){
    		  continue;
    	  }
    	  if("sgGrpVatInfoVo"===y){
    		  continue;
    	  }
          var values = objs[y];
          var jsonStrApplicant = JSON.stringify(values);
          var objApplicant = eval("(" + jsonStrApplicant + ")");
          var key, value, tagName, type, arr;
          for (x in objApplicant) {
              key = x;
              value = objApplicant[key];
				if("firstChargeDate"==key||"applDate"==key||"speEffectDate"==key||"joinBirthDate"==key){
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
                          ) {
                          	$("#"+ key).comboInitLoadById(value);
                          }else if(tagName == 'TEXTAREA'){
                          	//$("#" + key).combo("val", value);
                        	  $("#" + key).val(value);
                          }

                      });
          }
      }
}

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


// 基本信息校验规则
com.orbps.contractEntry.sgGrpInsurAppl.applValidateForm = function(vform) {
    var error2 = $('.alert-danger', vform);
    var success2 = $('.alert-success', vform);
    vform.validate({
        errorElement : 'span',
        errorClass : 'help-block help-block-error',
        focusInvalid : false,
        onkeyup : false,
        ignore : '',
        rules : {
        	applNo :{
            	required : true
            }
        },
        messages : {
        	applNo :{
        		required : "请输入投保单号"
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
	
	//投保单号置灰
	$("#applInfoForm #applNo").attr("readonly",true);
	
	// 初始化edittable组件
    $("#bsInfoListTb").editDatagridsLoadById();
	
    //页面加载时先隐藏网点信息
    $("#worksiteHideDiv").hide();
    
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
	
	//服务器调用
	var dataCipher=com.ipbps.getDataCipher();
	var url= '/orbps/web/authSupport/check?serviceName=cntrEntrySgGrpInsurApplService&dataCipher='+dataCipher;
	lion.util.postjson(url,null,com.orbps.contractEntry.sgGrpInsurAppl.successQueryDetail,null,null);
//    taskInfo = {};
//    taskInfo.bizNo = "2017030900090001"
//    lion.util.postjson('/orbps/web/orbps/contractEntry/sg/query',taskInfo,com.orbps.contractEntry.sgGrpInsurAppl.successQueryDetail,null,null);
	// 初始化校验信息
    com.orbps.contractEntry.sgGrpInsurAppl.applValidateForm(com.orbps.contractEntry.sgGrpInsurAppl.applInfoForm);

    $("#salesBranchNo").blur(function() {
        var salesBranchNo = $("#salesBranchNo").val();
        if (salesBranchNo === null || "" === salesBranchNo) {
            lion.util.info("警告", "销售机构不能为空");
            return false;
        }
    });

    $("#saleCode").blur(function() {
        var saleCode = $("#saleCode").val();
        if (saleCode === null || "" === saleCode) {
            lion.util.info("警告", "销售员代码不能为空");
            return false;
        }
    });


    // 丢失销售员代码查询销售员姓名
    $("#saleCode,#salesBranchNo").blur(function() {
    	$("#applInfoForm  #saleName").val("");
    	com.orbps.contractEntry.sgGrpInsurAppl.qureyBranchName();
    });

  //查询销售员姓名回调函数
    com.orbps.contractEntry.sgGrpInsurAppl.successQuerySaleName = function (data, arg){
  	if(data==="fail"){
  	    lion.util.info("销售员不存在，请确认销售渠道，销售机构，销售员代码是否正确");
  	}else{
  	    $("#saleName").val(data);
  	    $("#applInfoForm  #saleName").attr("readOnly",true);
  	}
  };
});

//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.contractEntry.sgGrpInsurAppl.addDialog.empty();
	com.orbps.contractEntry.sgGrpInsurAppl.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.contractEntry.sgGrpInsurAppl.grpSalesListFormVos);
	});
});
//根据上期保单号查询基本信息
$("#previousPolNo")
.blur(
		function() {
			var applNo='';
			applNo= $("#previousPolNo").val();
			if("" === applNo || null === applNo){
				lion.util.info("请输入上期保单号");
				return false;
			}
			if(confirm("是否要用上期保单信息替换现有基本信息?")){
				lion.util
				.postjson(
						'/orbps/web/orbps/contractEntry/search/searSgGrpInfo',
						applNo,
						com.orbps.contractEntry.sgGrpInsurAppl.successQuery,
						null, null);
			}
		});


//共保协议号离开焦点校验
$("#applInfoForm #agreementNo")
		.blur(
				function() {
					var agreementNum = $("#applInfoForm #agreementNo").val();
						lion.util
							.postjson(
									'/orbps/web/orbps/public/branchControl/searchAgreement',
									agreementNum,
									com.orbps.contractEntry.sgGrpInsurAppl.successAgreement);
				});

//共保协议号离开焦点回调函数
com.orbps.contractEntry.sgGrpInsurAppl.successAgreement = function(
		data, arg) {
	if(data.retCode==="0"){
		lion.util.info("提示",data.errMsg);
		$("#agreementNo").val("");
	}
}