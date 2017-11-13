// 新建命名空间
com.orbps.otherFunction ={};
com.orbps.otherFunction.contractQuery ={};
// 编辑或添加对话框
com.orbps.otherFunction.contractQuery.insuranceComList = new Array();
com.orbps.otherFunction.contractQuery.insuranceList = new Array();

$(function() { 
    // datagrid组件初始化
    // $("*").datagridsInitLoad();

    // combo组件初始化
    $("*").comboInitLoad();

    // 日期初始化插件
    $(".date-picker").datepicker({
        autoclose : true,
        language : 'zh-CN'
    });
    
    //省市县三级联动
	$("#citySelect").citySelect({
        url:"/resources/global/js/cityselect/js/city.min.json",
        /* prov:"北京", */
        /* nodata:"none", */
        required:false
    });
  
    //查询共保协议信息功能
    $("#btnQuery").click(function(){
        var agreementNo =  $("#agreementNo").val();
        var coInsurAgreementInfVo = $("#agreementInfForm").serializeObject();
    	if(agreementNo===null|| "" === agreementNo){
    		lion.util.info("警告", "请输入共保协议号查询");
		}else {
        lion.util.postjson(
                '/orbps/web/orbps/contractEntry/coi/query',
                coInsurAgreementInfVo,
                com.orbps.otherFunction.contractQuery.successQuery,
                null,
                null)};
        var commonAgreement = {};
        commonAgreement.agreementNo = agreementNo;
     // 添加查询参数
        $("#insurancecompanylist").datagrids({
            querydata : commonAgreement
        });
        // 重新加载数据
        $("#insurancecompanylist").datagrids('reload');
        // 添加查询参数
        $("#busiprdlist").datagrids({
            querydata : commonAgreement
        });
        // 重新加载数据
        $("#busiprdlist").datagrids('reload');
    });
    
});

//查询成功回调函数
com.orbps.otherFunction.contractQuery.successQuery = function(data,arg){
	//回显方法
    setTimeout(function() {
        var jsonStrs = JSON.stringify(data);
        var objs = eval("(" + jsonStrs + ")");
        var form;
        for (y in objs) {
            form = y;
            var a = form.replace("Vos","Form");
            var formId = a.replace("Vo","Form");
            var values = objs[y];
            var jsonStrApplicant = JSON.stringify(values);
            var objApplicant = eval("(" + jsonStrApplicant + ")");
            var key, value, tagName, type, arr;
            if("appAddrCountry"===form){
            	$("#appAddrCountry").val(values);
            }
            for (x in objApplicant) {
                key = x;
                value = objApplicant[key];
                if("applDate"==key||"inforceDate"==key||"termDate"==key){
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
                                    $("#"+formId +" #"+key).val(value);
                                }
                            }else if(tagName=='SELECT' || tagName=='TEXTAREA'){
                                $("#"+formId +" #"+key).combo("val", value);
                            }
                        });
            }
        }
        $("#agreementInfForm input[type='text']").attr("readOnly", true);
        $("#customerForm input[type='text']").attr("readOnly", true);
        $("#customerForm select").attr("readOnly", true);
        $("#appAddrCountry").attr("readOnly", true);
        $("#agreementInfForm #agreementNo").attr("readOnly",false);
    }, 1000);
    //回显地址信息
    //判断是否正常返回了data数据
    if(lion.util.isNotEmpty(data.customerVo)){
        setTimeout( function(){
            // 将省市县置为可以回显的状态
        	$(".dist").attr("disabled",false);
            $(".city").attr("disabled",false);
            $(".prov").attr("disabled",false);
            $("#customerForm #province").combo("val",data.customerVo.province);
            $("#customerForm #city").combo("val",data.customerVo.city);
            $("#customerForm #county").combo("val",data.customerVo.county);
        },1000);
    }
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
