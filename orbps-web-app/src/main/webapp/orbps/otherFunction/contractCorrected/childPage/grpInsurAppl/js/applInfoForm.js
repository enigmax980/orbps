
$(function() {
	//投保单号置灰
	$("#applInfoForm #applNo").attr("readonly",true);

	// 日期初始化插件
	$(".date-picker").datepicker({
		autoclose : true,
		language : 'zh-CN'
	});
	
	// 文件上传下载插件初始化
	$("#file").fileinput({
        'allowedFileExtensions' : ['jpg', 'png','gif','xlsx'],
        'showUpload':true,
        'showPreview':false,
        'showCaption':true,
        'browseClass':'btn btn-success',
    });
	
	// 初始化edittable组件
	$("#fbp-editDataGrid").editDatagridsLoadById();
	
	// combo组件初始化
	$("*").comboInitLoad();

	$("#agreementNo").blur(function () {  
		var agreementNo = $("#agreementNo").val();
		if(agreementNo==null||""==agreementNo){
			lion.util.info("警告","共保协议号不能为空");
			return false;
		}
	}); 

	$("#oldApplNo").blur(function () {  
		var oldApplNo = $("#oldApplNo").val();
		if(oldApplNo==null||""==oldApplNo){
			lion.util.info("警告","上期保单号不能为空");
			return false;
		}
	}); 

});
com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.successQueryDetail = function(data,args){
    setTimeout(function() {
	  //特别约定
      $("#specialPro").val(data.proposalInfoVo.specialPro);
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
              if("applDate"==key||"inForceDate"==key||"enstPremDeadline"==key||"constructionDur"==key||"until"==key){
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

$("#btnQuerySales").click(function() {
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.empty();
	com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.addDialog.load(
		"/orbps/orbps/otherFunction/contractCorrected/childPage/sgGrpInsurAppl/html/salesChannelModal.html",
		function() {
			$(this).modal('toggle');
			$(this).comboInitLoad();
			com.orbps.common.saleChannelTableReload(com.orbps.otherFunction.contractCorrected.childPage.grpInsurAppl.grpSalesListFormVos);
	});
});