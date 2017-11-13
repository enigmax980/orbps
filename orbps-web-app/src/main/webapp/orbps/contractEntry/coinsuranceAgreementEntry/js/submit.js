com.orbps.contractEntry.coinsuranceAgreementEntry.subForm = $("#submitForm");
com.orbps.contractEntry.coinsuranceAgreementEntry.successSubmit;
com.orbps.contractEntry.coinsuranceAgreementEntry.successQuery;
com.orbps.contractEntry.coinsuranceAgreementEntry.validDates;

// 基本信息校验规则
$(function() {
	$("input[name='cd']").get(0).checked=true
	$("#agreementNo").attr("readOnly",true);
     //初始化校验函数
    com.orbps.contractEntry.coinsuranceAgreementEntry
            .insurancehForm(com.orbps.contractEntry.coinsuranceAgreementEntry.subForm);
    // 表单提交
    $("#btnSubmit")
            .click(
                    function(){
                    	var check_val = $('input:radio:checked').val();
                    	//计算协议有效日期
                    	var startTime = $("#inforceDate").val();
                    	var endTime = $("#termDate").val();
                    	var insuranceComVos =new Array();
                    	var sumamntPct =0.0;
                    	var sumrespPct =0.0;
                    	var agreementNo =$("#agreementNo").val();
                    	if(check_val==="M" && agreementNo===""){
                			lion.util.info("警告","共保协议号不能为空！");
                			return false;
                    	}
                    	if(startTime!=="" && endTime!==""){
                    	    com.orbps.contractEntry.coinsuranceAgreementEntry.DateDiff(startTime,endTime);
                    	}
                    	//jquery validate 校验
                        if (com.orbps.contractEntry.coinsuranceAgreementEntry.agreementInf
                                .validate().form()
                                && com.orbps.contractEntry.coinsuranceAgreementEntry.customer
                                        .validate().form()
                                ) {
                        	if(com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList.length < 1){
                        		lion.util.info("共保公司信息不能为空");
                        		return;
                        	}
                        	//遍历共保公司信息
                            var connId = "N";
                            var connIdlength = 0;
                        	for(var i=0; i<com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList.length; i++){
                        		var sumCom = com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList[i];
                        		insuranceComVos.push(sumCom);
                        		sumamntPct = Number((sumamntPct+Number(sumCom.amntPct)).toFixed(2));
                        		sumrespPct = Number((sumrespPct+Number(sumCom.responsibilityPct)).toFixed(2));
                                if("M" === sumCom.connIdType){
                                	connId = "M";
                                	connIdlength++;
                                }
                        	}
                        	//校验参保公司份额比例相加是否100
                        	if (sumamntPct !== 100){
                    			lion.util.info("警告","共保保费份额占比不正确，请核对后重新输入");
                    			return false;
                    		}
                    		if (sumrespPct !== 100){
                    			lion.util.info("警告","共保责任份额占比不正确，请核对后重新输入");
                    			return false;
                    		}
                    		//判断首席共保公司是否唯一
                    		if(connIdlength>1){
                    			lion.util.info("警告","首席共保公司只能有一个！");
                    			return false;
                    		}
                    		//判断险种表格条数要大于等于1
                        	if ( com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList.length < 1){
                        		lion.util.info("险种信息不能为空");
                        		return;
                        	}

                             //select校验
                            if (validateSelectVal()) {
                                //校验协议有效日期
                                if(com.orbps.contractEntry.coinsuranceAgreementEntry.validDates<0){
                                    lion.util.info("协议有效期结束日期不能小于协议有效期开始日期");
                                    return false;
                                }
                                 //提交方法
                                var coInsurAgreementEntryVo = {};
                                coInsurAgreementEntryVo.appAddrCountry=$("#appAddrCountry").val();
                                coInsurAgreementEntryVo.agreementInfVo = com.orbps.contractEntry.coinsuranceAgreementEntry.agreementInf
                                        .serializeObject();
                                coInsurAgreementEntryVo.agreementInfVo.transFlag = check_val;
                                //获取输入的日期
                                coInsurAgreementEntryVo.customerVo = com.orbps.contractEntry.coinsuranceAgreementEntry.customer
                                        .serializeObject();
                                coInsurAgreementEntryVo.insuranceComVos = com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList;                              
                                coInsurAgreementEntryVo.insuranceVos = com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList;                        
                                coInsurAgreementEntryVo = lion.util.sanitize(coInsurAgreementEntryVo);
                                lion.util
                                        .postjson(
                                                '/orbps/web/orbps/contractEntry/coi/submit',
                                                coInsurAgreementEntryVo,
                                                com.orbps.contractEntry.coinsuranceAgreementEntry.successSubmit, null, null);
                            }
                       }
                    });
  //字符串转日期格式，strDate要转为日期格式的字符串 
    function getDate(strDate) { 
      var st = strDate; 
      var a = st.split(" "); 
      var b = a[0].split("-"); 
      var c = a[1].split(":"); 
      var date = new Date(b[0], b[1], b[2], c[0], c[1]);
      return date; 
    } 
	// 校验选择信息
	function validateSelectVal() {
	//签署日期
    var applDate = $("#agreementInfForm #applDate").val();
    if (applDate === null || "" === applDate) {
        lion.util.info("警告", "签署日期不能为空");
        return false;
    }
    //有效起始时间
    var inforceDate = $("#agreementInfForm #inforceDate").val();
    if (inforceDate === null || "" === inforceDate) {
        lion.util.info("警告", "有效起始时间不能为空");
        return false;
    }
    //有效终止时间
    var termDate = $("#agreementInfForm #termDate").val();
    if (termDate === null || "" === termDate) {
        lion.util.info("警告", "终止时间不能为空");
        return false;
    }
    
    //证件类型
    var idType = $("#customerForm #idType").val();
    if (idType === null || "" === idType) {
        lion.util.info("警告", "证件类型不能为空");
        return false;
    }
    
    //职业类别
    var occClassCode = $("#customerForm #occClassCode").val();
    if (occClassCode === null || "" === occClassCode) {
        lion.util.info("警告", "职业类别不能为空");
        return false;
    }
    
    //省/直辖市
    var province = $("#customerForm #province").val();
    if (province === null || "" === province) {
        lion.util.info("警告", "省/直辖市不能为空");
        return false;
    }
    
    //市/城区
    var city = $("#customerForm #city").val();
    if (city === null || "" === city) {
        lion.util.info("警告", "市/城区不能为空");
        return false;
    }
    
    //县/地级市
    var county = $("#customerForm #county").val();
    if (county === null || "" === county) {
        lion.util.info("警告", "县/地级市不能为空");
        return false;
    }
    return true;
  }
});

//添加成功回调函数
com.orbps.contractEntry.coinsuranceAgreementEntry.successSubmit=function (data,arg){
	if (data.retCode==="1"){
        lion.util.info("提示","提交成功");
        // 成功后回显共保协议号
        $("#agreementNo").val(data.applNo);
    }else if (data.retCode==="0"){
        lion.util.info("提示",data.errMsg);
    }
}

//查询共保协议信息
$("#btnQuery").click(function() {
	var agreementNo =$("#agreementNo").val();
	var coInsurAgreementInfVo ={};
	coInsurAgreementInfVo.agreementNo = agreementNo;
	if(agreementNo===null|| "" === agreementNo){
		lion.util.info("警告", "请输入共保协议号查询");
	}else{// 向后台发送请求
		lion.util.postjson('/orbps/web/orbps/contractEntry/coi/query',coInsurAgreementInfVo,com.orbps.contractEntry.coinsuranceAgreementEntry.successQuery,null,null);
	}
});


//新增修改回显
function demo(){
	$("#agreementNo").attr("readOnly",true);
    $("#agreementNo").val("");
    $(".fa").removeClass("fa-warning");
    $(".fa").removeClass("fa-check");
    $(".fa").removeClass("has-success");
    $(".fa").removeClass("has-error");
    }
function demo1(){
	$("#agreementNo").attr("readOnly",false);
    $(".fa").removeClass("fa-warning");
    $(".fa").removeClass("fa-check");
    $(".fa").removeClass("has-success");
    $(".fa").removeClass("has-error");
	}

//清空功能
$("#submitForm #btnClear").click(function(){
    $("#agreementInfForm").reset();
    $("#customerForm").reset();
    $(".fa").removeClass("fa-warning");
    $(".fa").removeClass("fa-check");
    $(".fa").removeClass("has-success");
    $(".fa").removeClass("has-error");
});



//查询成功回调函数
com.orbps.contractEntry.coinsuranceAgreementEntry.successQuery = function(data,arg){
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
    
    //回显共保公司信息
    com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComList = data.insuranceComVos;
    com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceComTable();
    //回显险种信息
    com.orbps.contractEntry.coinsuranceAgreementEntry.insuranceList = data.insuranceVos;
    com.orbps.contractEntry.coinsuranceAgreementEntry.reloadInsuranceTable();
      
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

//计算天数差的函数 
com.orbps.contractEntry.coinsuranceAgreementEntry.DateDiff = function(sDate1,  sDate2){    //sDate1和sDate2是2006-12-18格式  
    var Date1 = new Date(sDate1);
    var Date2 = new Date(sDate2);
    com.orbps.contractEntry.coinsuranceAgreementEntry.validDates = (Date2.getTime() - Date1.getTime())/(24 * 60 * 60 * 1000);
}