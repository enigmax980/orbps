// 新建contractReview命名空间
com.orbps.contractReview = {};
//新建contractReview.sgGrpInsurAppl命名空间
com.orbps.contractReview.sgGrpInsurAppl = {};
com.orbps.contractReview.sgGrpInsurAppl.taskId;
//com.orbps.contractReview.sgGrpInsurAppl.data={};
com.orbps.contractReview.sgGrpInsurAppl.applInfoForm = $('#applInfoForm');
com.orbps.contractReview.sgGrpInsurAppl.queryBusiprodList = [];
com.orbps.contractReview.sgGrpInsurAppl.addinsuranceVos = [];
com.orbps.contractReview.sgGrpInsurAppl.acount = 0;
com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos = [];
//基本信息校验规则
com.orbps.contractReview.sgGrpInsurAppl.successQueryDetail = function(data,args){
	com.orbps.contractReview.sgGrpInsurAppl.acount=1;
	com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos = data.sgGrpApplInfoVo.grpSalesListFormVos;
	  //alert("成功"+JSON.stringify(data));
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
              $("#personalInsurInfoForm #province").combo("val",data.sgGrpPersonalInsurInfoVo.province);
              $("#personalInsurInfoForm #city").combo("val",data.sgGrpPersonalInsurInfoVo.city);
              $("#personalInsurInfoForm #county").combo("val",data.sgGrpPersonalInsurInfoVo.county);
              //置灰证件类型是身份证的时候日期 性别只读
              if("I"===$("#personalInsurInfoForm #joinIdType").val()){
         		 $("#personalInsurInfoForm #joinBirthDate").attr("disabled",true);
         		 $("#personalInsurInfoForm #joinBirthDateBtn").attr("disabled",true);
         		 $("#personalInsurInfoForm #joinSex").attr("readonly",true);
         	  }
          },1000);
      }
     // com.orbps.contractReview.sgGrpInsurAppl.data = data;
      com.orbps.contractReview.sgGrpInsurAppl.taskId = data.taskInfo.taskId;
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
        // combo组件初始化
        $("*").comboInitLoad();
        //根据销售渠道，更改页面样式
        if("OA"===data.sgGrpApplInfoVo.salesChannel){
        	$("#worksiteHideDiv").show();
        	$("#salesHideDiv").hide();
        }else{
        	$("#worksiteHideDiv").hide();
        	$("#salesHideDiv").show();
        }
      //收付费分组信息
      com.common.chargePayList = data.chargePayGroupModalVos;
      // 赋值被保人分组信息为全局变量
      com.orbps.common.proposalGroupList = data.insuredGroupModalVos;
      //将娶到的责任信息放入全局变量中。
      com.orbps.contractReview.sgGrpInsurAppl.addinsuranceVos = data.addinsuranceVos;
      // 取到责任信息list，用于被保人分组中的险种及责任下拉框的回显
	  for(var i = 0 ; i < data.addinsuranceVos.length; i++){
	  	//当责任不为空时放入全局变量
		  if(lion.util.isNotEmpty(data.addinsuranceVos[i].responseVos)){
			  for (var j = 0; j < data.addinsuranceVos[i].responseVos.length; j++) {
				var array_element = data.addinsuranceVos[i].responseVos[j];
				com.orbps.common.list.push(array_element);
			}
		  }
	  }
   // 险种表格回显
      $('#bsInfoListTb').editDatagrids("bodyInit", data.addinsuranceVos);
      com.orbps.contractReview.sgGrpInsurAppl.queryBusiprodList = data.addinsuranceVos;

      //被保险人总数
      //$("#proposalInfoForm #insuredTotalNum").val(data.sgGrpProposalInfoVo.insuredTotalNum);
      //特别约定
      $("#proposalInfoForm #convention").val(data.sgGrpProposalInfoVo.convention);
      //如果不是新单状态是清汇基本信息复核，置灰跳转按钮。清汇基本信息复核    或者复核通过
      if("09" !== data.approvalState){
  		$("#btnlocation").attr("disabled",true);
      }
      $("#salesChannel").attr("readonly",true);//销售机构
      $("#giftInsFlag").attr("readOnly",true);//赠送险标记
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
com.orbps.contractReview.sgGrpInsurAppl.applValidateForm = function(vform) {
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
	//个人汇交省市县三级联动控件初始化
	$("#citySelectsP").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        /* prov:"北京", */
        /* nodata:"none", */
        required:false
	})
	//团体汇交省市县三级联动控件初始化
	$("#citySelect").citySelect({
	        url:"/resources/global/js/cityselect/js/city.min.json",
	        /* prov:"北京", */
	        /* nodata:"none", */
	        required:false
	    });
	
	//页面加载时先隐藏网点信息
    $("#worksiteHideDiv").hide();
	
	//服务器测试
	var dataCipher=com.ipbps.getDataCipher();
	var url= '/orbps/web/authSupport/check?serviceName=cntrEntrySgGrpInsurApplService&dataCipher='+dataCipher;
	lion.util.postjson(url,null,com.orbps.contractReview.sgGrpInsurAppl.successQueryDetail,null,null);
//	taskInfo = {};
//    taskInfo.bizNo = "2017011000020007"
//    lion.util.postjson('/orbps/web/orbps/contractEntry/sg/query',taskInfo,com.orbps.contractReview.sgGrpInsurAppl.successQueryDetail,null,null);

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
    // 初始化校验信息
    com.orbps.contractReview.sgGrpInsurAppl
            .applValidateForm(com.orbps.contractReview.sgGrpInsurAppl.applInfoForm);

    $("#salesBranchNo").blur(function() {
        var salesBranchNo = $("#salesBranchNo").val();
        if (salesBranchNo === null || "" === salesBranchNo) {
            lion.util.info("警告", "销售机构不能为空");
            return false;
        }
    });

    $("#salesCode").blur(function() {
        var salesCode = $("#salesCode").val();
        if (salesCode === null || "" === salesCode) {
            lion.util.info("警告", "销售员代码不能为空");
            return false;
        }
    });


    // 丢失销售员代码查询销售员姓名
    $("#saleCode,#salesBranchNo").blur(function() {
    	$("#applInfoForm  #saleName").val("");
    	com.orbps.contractReview.sgGrpInsurAppl.qureyBranchName();
    });
    $("#salesChannel").change(function(){
    	$("#applInfoForm  #saleName").val("");
    	com.orbps.contractReview.sgGrpInsurAppl.qureyBranchName();
    });
 

  //查询销售员姓名回调函数
    com.orbps.contractReview.sgGrpInsurAppl.successQuerySaleName = function (data, arg){
  	if(data==="fail"){
  	    lion.util.info("销售员不存在，请确认销售渠道，销售机构，销售员代码是否正确");
  	}else{
  	    $("#saleName").val(data);
  	    $("#applInfoForm  #saleName").attr("readOnly",true);
  	}
  };
  com.orbps.contractReview.sgGrpInsurAppl.qureyBranchName = function(){
	  var saleCode = $("#saleCode").val();
      var salesBranchNo = $("#salesBranchNo").val();
      var salesChannel = $("#salesChannel").val();
      if ("" === salesBranchNo || "" === saleCode || ""===salesChannel) {
          return false;
      }else{
    	  var grpApplInfoVo = $("#applInfoForm").serializeObject();
  		lion.util.postjson(
  				'/orbps/web/orbps/contractEntry/grp/querySaleName',
  				grpApplInfoVo,
  				com.orbps.contractReview.sgGrpInsurAppl.successQuerySaleName,
  				null,
  				null);
      }
};
});

//查询销售渠道信息
$("#btnQuerySales").click(function() {
	com.orbps.contractReview.sgGrpInsurAppl.addDialog.empty();
	com.orbps.contractReview.sgGrpInsurAppl.addDialog.load(
		"/orbps/orbps/public/modal/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.contractReview.sgGrpInsurAppl.grpSalesListFormVos);
	});
});